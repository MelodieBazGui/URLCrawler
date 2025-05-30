package crawlerUtils;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import utils.Logger;

public class BrowserHandler {
	public static void openUrlInDefaultBrowser(String url) throws Exception {
	    Logger.logInfo("Attempting to open " + url +" in the default browser now...");
	    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
	        Desktop.getDesktop().browse(new URI(url));
	    } else {
	        throw new UnsupportedOperationException("Desktop browsing not supported on this platform.");
	    }
	}
}
