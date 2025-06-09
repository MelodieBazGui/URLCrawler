package stryckyzzzComponents;
import javax.swing.*;

import crawlerUtils.LinkExtractor;
import utils.Logger;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class UrlButtonPanel extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5334487801107674141L;

	public UrlButtonPanel(JPanel targetPanel) {
	
        targetPanel.removeAll();
        targetPanel.setLayout(new BoxLayout(targetPanel, BoxLayout.Y_AXIS));

        List<String> lines = LinkExtractor.getLinks();
		for (String line : lines) {
		    String trimmed = line.trim();
		    if (!trimmed.isEmpty()) {
		        BrowserButton button = new BrowserButton(trimmed);
		        button.setAlignmentX(Component.LEFT_ALIGNMENT);
		        targetPanel.add(button);
		    }
		}
        targetPanel.revalidate();
        targetPanel.repaint();
    }
}
