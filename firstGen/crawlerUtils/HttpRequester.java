package crawlerUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import userInterface.CrawlerMain;
import utils.Logger;

/**
 * 'Pretends to be a Browser and gets the links' Class
 * <p>
 * Everything is handled through deprecated URL class, retries in case of bad response from the site and has a delay to prevent being blocked by target site
 * @author Mélodie Bazeries Guilbault
 */
public class HttpRequester {

    private static final int MAX_RETRIES = 3;
    private static final int INITIAL_DELAY_MS = 1000;

    public static String fetchHTML(String urlString) {
        int attempt = 0;
        int delay = INITIAL_DELAY_MS;

        while (attempt < MAX_RETRIES) {
            attempt++;
            try {
                long startTime = System.currentTimeMillis();

                HttpURLConnection conn = (HttpURLConnection) new URL(urlString).openConnection();
                conn.setRequestMethod("GET");

                //Sets itself up like a browser, information is meaningless unless the site has proper safegards
                conn.setRequestProperty("User-Agent", 
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                    + "(KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36" + CrawlerMain.CrawlerVersion);
                conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);

                int status = conn.getResponseCode();
                Logger.logInfo("HTTP response: " + status + " for " + urlString);

                if (status >= 300 && status < 400) {
                    Logger.logWarn("Redirect received, skipping: " + urlString);
                    return null;
                }

                if (status != HttpURLConnection.HTTP_OK) {
                    Logger.logWarn("Non-OK response " + status + " for " + urlString);
                    return null;
                }

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }

                    long endTime = System.currentTimeMillis();
                    Logger.logInfo("Fetched " + urlString + " in " + (endTime - startTime) + "ms");

                    return content.toString();
                }

            } catch (IOException e) {
                Logger.logWarn("Attempt " + attempt + " failed for: " + urlString);
                Logger.logError("fetchHTML attempt #" + attempt + " -> " + urlString, e);
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException ignored) {}
                delay *= 2;
                //Backs off exponentially to prevent site from blocking this app's access to it
            }
        }

        Logger.logError("Failed to fetch after retries: " + urlString, new IOException(urlString));
        return null;
    }
}
