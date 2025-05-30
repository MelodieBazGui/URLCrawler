package crawlerUtils;

import java.util.*;
import java.util.regex.*;

import utils.Logger;

import java.net.*;

/**
 * This class uses patterns to find links, this pattern is honestly quite weird and i do not assure this would yield the proper URLs on a site without skipping a few
 * @author Mélodie Bazeries Guilbault
 */
public class LinkExtractor {

    private static final Pattern LINK_PATTERN = Pattern.compile(
        "<a\\s+(?:[^>]*?\\s+)?href=([\"'])(.*?)\\1",
        Pattern.CASE_INSENSITIVE);

    public static List<String> extractLinks(String html, String baseUrl) {
        List<String> links = new ArrayList<>();
        Matcher matcher = LINK_PATTERN.matcher(html);
        while (matcher.find()) {
            String link = matcher.group(2).trim();
            // Skip empty, javascript, mailto, or fragment links
            if (link.isEmpty() ||
                link.startsWith("javascript:") ||
                link.startsWith("mailto:") ||
                link.startsWith("#")) {
                Logger.logWarn("Invalid link skipped: " + link);
                continue;
            }
            // Normalize urls using deprecated URL
            try {
                URL base = new URL(baseUrl);
                URL absolute = new URL(base, link);
                String normalized = absolute.toString();

                // Prevents from accessing another domain or mixing base and absolute
                if (normalized.startsWith(baseUrl)) {
                    links.add(normalized);
                }
            } catch (MalformedURLException e) {
            	//if it breaks anyway, i'm never too tired of try/catch to handle my personal issues
                Logger.logWarn("Malformed URL skipped: " + link);
            }
        }
        return links;
    }
}

