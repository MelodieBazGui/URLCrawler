package stryckyzzzUrlCrawler;

import java.util.concurrent.*;

import crawlerUtils.HttpRequester;
import crawlerUtils.LinkExtractor;
import crawlerUtils.RobotsTxtParser;
import crawlerUtils.fileHandler.TxtFileHandler;
import utils.Logger;
import utils.ObservableSet;

import java.util.*;
import java.io.*;
import java.net.URL;

/**
 * CrawlerControler class, handles the crawling and the sub classes in order to get every accessible visited URL
 * Relies on the deprecated URL class
 * <p>
 * CrawlerControler heavily relies on try/catch statements for debugging
 * @author Mélodie Bazeries Guilbault
 */
public class CrawlerController {

    private final String baseUrl;
    private ObservableSet<String> visited = new ObservableSet<String>(ConcurrentHashMap.newKeySet());
    private final Queue<String> toVisit = new ConcurrentLinkedQueue<>();
    private volatile boolean stopRequested = false;
    public boolean finishedCrawling;
	private Boolean invalidURL = null;

    public CrawlerController(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * startCrawl method, implements the concurrencies to support multiple crawlLoop method
     * <p>
     * Relies heavily on try/catch statements, this is not the best way to implement this class
     * Logs most actions taken in debut_log.txt
     */
    public void startCrawl() {
    	new File(System.getProperty("user.dir") + "found_urls.txt");
    	finishedCrawling = false;
        long startTime = System.currentTimeMillis();

        try {
            TxtFileHandler.resetOutputFile("found_urls.txt");
            RobotsTxtParser.parseRobotsTxt(getDomainRobotsTxt(baseUrl));
        } catch (Exception e) {
            Logger.logError("startCrawl setup", e);
            invalidURL = true;
            return;
        }

        invalidURL = false;
        Logger.logInfo("Starting crawl at: " + baseUrl);
        toVisit.add(baseUrl);

        int workerCount = 50;

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<?>> futures = new ArrayList<>();
            for (int i = 0; i < workerCount; i++) {
                futures.add(executor.submit(this::crawlLoop));
            }
            for (Future<?> f : futures) {
                f.get();
            }
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.MINUTES);

        } catch (Exception e) {
            Logger.logError("startCrawl", e);
        }

        long endTime = System.currentTimeMillis();
        Logger.logInfo("✅ Crawl finished in " + (endTime - startTime) + " ms");
        finishedCrawling = true;
    }

    /**
     * crawlerStop method
     * <p>
     * Gives the user the possibility to stop crawling manually
     */
    public void crawlerStop() {
        Logger.logInfo("🛑 Stop requested by user.");
        stopRequested = true;
    }

    /**
     * crawlLoop method, is a method that is concurrent and in multiple worker threads, crawls URLs from the base URL until stopped or no more URL can be crawled
     * <p>
     * Method checks to know if a stop has been requested, will stop any crawler loop if so
     */
    private void crawlLoop() {
        while (!stopRequested) {

            String url = toVisit.poll();
            if (url == null) break;

            if (visited.contains(url) || !RobotsTxtParser.isAllowed(url)) continue;

            visited.add(url);
            Logger.logInfo("Crawling: " + url);

            String html = HttpRequester.fetchHTML(url);
            if (html == null || html.isBlank()) {
                Logger.logWarn("No HTML returned for: " + url);
                continue;
            }
            
            try {
                TxtFileHandler.writeToOutputFile(url, "found_urls.txt");
            } catch (IOException e) {
                Logger.logError("Failed to write URL: " + url, e);
            }

            List<String> links = LinkExtractor.extractLinks(html, url);
            for (String link : links) {
                if (stopRequested) break;
                if (link.startsWith(baseUrl) && !visited.contains(link) && link.contains("graine")) {
                    toVisit.add(link);
                    try {
                        TxtFileHandler.writeToOutputFile(link, "found_urls.txt");
                    } catch (IOException e) {
                        Logger.logError("Failed to write URL: " + link, e);
                    }
                }
            }
        }
    }

    /**
     * getDomainRobotsTxt method, get's the site's robot.txt
     * <p>
     * Either returned a parsed string if not failed (robots.txt found and accessible)
     * <p>
     * Or returns the Base url while logging it in the debug_log.txt file as an error
     * @param url
     * @return string
     */
    private String getDomainRobotsTxt(String url) {
        try {
            URL parsed = new URL(url);
            return parsed.getProtocol() + "://" + parsed.getHost() + "/robots.txt";
        } catch (Exception e) {
            Logger.logError("getDomainRobotsTxt -> " + url, e);
            return "https://www.royalqueenseeds.fr/robots.txt";
        }
    }

	public Boolean getInvalidURL() {
		return invalidURL;
	}
}