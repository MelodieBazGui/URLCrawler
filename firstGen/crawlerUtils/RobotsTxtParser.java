package crawlerUtils;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import utils.Logger;

/**
 * Class RobotsTxtParser
 * <p>
 * This class is made to get rid of any links that would not follow the site robot.txt, there is no implemented way around robot.txt, as i wish not to make a crawler that can be flagged or blocked from the site
 * @author Mélodie Bazeries Guilbault
 */
public class RobotsTxtParser {

    private static final Set<String> disallowedPaths = ConcurrentHashMap.newKeySet();

    /**
     * Parses robot.txt file to follow the sites guideline for robots
     * <p>
     * This makes it more polite to the site, as you could force find other URLs, but it doesn't really need to be a feature
     * @param robotsUrl
     */
    public static void parseRobotsTxt(String robotsUrl) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(robotsUrl).openConnection();
            conn.setRequestProperty("User-Agent", "CustomCrawler/0.1-dev");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                boolean appliesToUs = false;
                String line;

                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.toLowerCase().startsWith("user-agent:")) {
                        String agent = line.substring(11).trim();
                        appliesToUs = agent.equals("*") || agent.equalsIgnoreCase("CustomCrawler");
                    } else if (appliesToUs && line.toLowerCase().startsWith("disallow:")) {
                        String path = line.substring(9).trim();
                        if (!path.isEmpty()) {
                            disallowedPaths.add(path);
                        }
                    }
                }
            }
        } catch (IOException e) {
            Logger.logError("parseRobotsTxt", e);
        }
    }

    public static boolean isAllowed(String url) {
        try {
            String path = new URL(url).getPath();
            return disallowedPaths.stream().noneMatch(path::startsWith);
        } catch (MalformedURLException e) {
            Logger.logError("isAllowed -> " + url, e);
            return false;
        }
    }
}
