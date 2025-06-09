package userInterface;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import stryckyzzzComponents.StryckyzzzFilterPanel;
import stryckyzzzComponents.UrlButtonPanel;
import stryckyzzzUrlCrawler.CrawlerController;
import utils.Logger;
import utils.interfaces.ContentChangeListener;

import java.awt.FlowLayout;
import javax.swing.JScrollPane;


/**
 * CrawlerMain is used to make the UI, i know this doesn't follow naming conventions
 * <p>
 * This is purely UI logic and AWT/SWING code i tabbed to my liking
 * This also updates dynamically from the visited URLs and depending on which filters you have
 * Filters can be chosen before crawling, if crawling filters will only hide the unwanted URLs
 * @author Mélodie Bazeries Guilbault
 */
public class CrawlerMain {

    public static final String CrawlerVersion = "CustomCrawler/0.1-dev";
    private static CrawlerController cc;
    private static final String defaultLinkFormat = "https://www.";
	
	private  JFrame frame = new JFrame();
		private JPanel topPanel = new JPanel();
			private JLabel crawlerLabel = new JLabel("Strycky's Crawler");
			private JLabel URLLabel = new JLabel("URL : ");
			private JTextArea StryckyzzzJTextArea = new StryckyzzzJTextArea(defaultLinkFormat);
		private JPanel centerPanel = new JPanel();
			private JScrollPane linkScrollPane = new JScrollPane();
				private JPanel linkPanel = new JPanel();
			private JButton seeResultButton = new JButton("See Results");
		private JPanel filterPanel = new JPanel();
			private JScrollPane filterScrollPane = new JScrollPane();
				private StryckyzzzFilterPanel SzzzFP = new StryckyzzzFilterPanel();
			private JButton filterButton = new JButton("Filter Results");
		private JPanel bottomPanel = new JPanel();
			private JButton stopCrawlingButton = new JButton("Stop Crawling");
			private String buttonDefaultText = "Start Crawling";
			private JButton startCrawlButton = new JButton(buttonDefaultText);
			private String crawlingTextDefault = "Click 'Start crawling' to crawl";
			private JLabel crawlingText = new JLabel(crawlingTextDefault);	
			
	public boolean isCrawling = false;

	/**
	 * 
	 */
    public void window() {
    	frame.setMaximumSize(new Dimension(1920, 1080));
    	frame.setMinimumSize(new Dimension(900, 600));
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.getContentPane().setLayout(new BorderLayout(5, 3));
    	frame.setTitle(CrawlerVersion);
    	frame.getContentPane().add(topPanel, BorderLayout.NORTH);
    		topPanel.setLayout(new BorderLayout());
    		topPanel.add(crawlerLabel, BorderLayout.WEST);
				crawlerLabel.setFont(new Font("CrawlerName", Font.BOLD, 15));
			URLLabel.setHorizontalAlignment(SwingConstants.TRAILING);
			topPanel.add(URLLabel, BorderLayout.CENTER);
				URLLabel.setFont(new Font("URLLabelName", Font.BOLD, 18));
				URLLabel.addContentChangeListener(new ContentChangeListener() {
	                private void checkEmpty() {
	                    SwingUtilities.invokeLater(() -> {
	                        if (URLLabel.getText().trim().isEmpty()) {
	                        	URLLabel.setText(defaultLinkFormat);
	                        }
	                    });
	                }

	                @Override
	                public void insertUpdate(DocumentEvent e) {
	                    // Optional: do nothing on insert
	                }

	                @Override
	                public void removeUpdate(DocumentEvent e) {
	                    checkEmpty();
	                }

	                @Override
	                public void changedUpdate(DocumentEvent e) {
	                    // Not used for plain text components
	                }
	            });
			topPanel.add(URLTextArea, BorderLayout.EAST);
		frame.getContentPane().add(centerPanel, BorderLayout.CENTER);
			centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
			centerPanel.add(linkScrollPane);
				linkScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				linkScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				linkScrollPane.setViewportView(linkPanel);
				linkScrollPane.setAlignmentX(Component.RIGHT_ALIGNMENT);
				centerPanel.addComponentListener(new ComponentAdapter() {
				    @Override
				    public void componentResized(ComponentEvent e) {
				    	linkScrollPane.setPreferredSize(new Dimension(centerPanel.getWidth() - 200, centerPanel.getHeight() - 100));
				        centerPanel.revalidate();
				    }
				});
			centerPanel.add(seeResultButton);
				seeResultButton.addActionListener(seeButtonResultActionListener());
				seeResultButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
		frame.getContentPane().add(filterPanel, BorderLayout.WEST);
			filterPanel.add(filterScrollPane);
				filterScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				filterScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				filterScrollPane.setViewportView(SzzzFP);
				filterScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
				filterPanel.addComponentListener(new ComponentAdapter() {
					@Override
					public void componentResized(ComponentEvent e) {
						filterScrollPane.setPreferredSize(
								new Dimension(
										frame.getContentPane().getWidth() / 3  + 100, 
										(int) (frame.getContentPane().getHeight() / 1.33)
										)
								); 
					}
				});
		frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
			bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			bottomPanel.add(stopCrawlingButton);
				stopCrawlingButton.addActionListener(stopCrawlingButtonListener());
				stopCrawlingButton.setEnabled(false);
			bottomPanel.add(startCrawlButton);
				startCrawlButton.addActionListener(startCrawlingButtonListener());
			bottomPanel.add(crawlingText);
		frame.setVisible(true);
    }

	private ActionListener seeButtonResultActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Logger.logInfo("See Results Button pressed");
				new UrlButtonPanel(linkPanel);
				linkScrollPane.revalidate();
				linkScrollPane.repaint();
			}
		};
	}
    
    /**
     * 
     * @return ActionListener
     */
    private ActionListener stopCrawlingButtonListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isCrawling) {
					cc.crawlerStop();
					stopCrawlingButton.setEnabled(false);
					crawlingText.setText(crawlingTextDefault);
				}
			}
		};
	}

    /**
     * Returns an ActionListener to start crawling when the button is clicked.
     * Handles threading and UI updates safely.
     */
    private ActionListener startCrawlingButtonListener() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isCrawling) {
                    Logger.logInfo("Crawler already started, stop clicking the button");
                    startCrawlButton.setEnabled(false);
                    startCrawlButton.setText("Already Crawling");
                    return;
                }

                cc = new CrawlerController(URLTextArea.getText());
                isCrawling = true;
                String originalText = startCrawlButton.getText();

                Logger.resetLogFile();
                Logger.logInfo("Crawler started.");
                Logger.logDuration("Crawling process");
                crawlingText.setText("Currently Crawling");
                startCrawlButton.setEnabled(false);
                stopCrawlingButton.setEnabled(true);

                new Thread(() -> {
                    cc.startCrawl();  // Assumes this is blocking

                    SwingUtilities.invokeLater(() -> {
                        isCrawling = false;
                        startCrawlButton.setEnabled(true);  // Re-enable
                        startCrawlButton.setText(originalText);
                        crawlingText.setText("Crawling finished");
                        stopCrawlingButton.setEnabled(false);
                        Logger.logInfo("Crawling process completed.");
                    });
                }).start();
            }
        };
    }

}
