package userInterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import stryckyzzzComponents.BrowserButton;
import stryckyzzzComponents.StryckyzzzFilterComponent;
import stryckyzzzUrlCrawler.CrawlerController;
import utils.Logger;

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
	
	private  JFrame frame = new JFrame();
		private JPanel topPanel = new JPanel();
			private JLabel crawlerLabel = new JLabel("Strycky's Crawler");
			private JLabel URLLabel = new JLabel("URL : ");
			private JTextArea URLTextArea = new JTextArea("https://www.royalqueenseeds.fr");
		private JPanel centerPanel = new JPanel();
			private JScrollPane linkScrollPane = new JScrollPane();
			private JButton seeResultButton = new JButton("See Results");
		private JPanel filterPanel = new JPanel();
			private JScrollPane filterScrollPane = new JScrollPane();
				private JPanel linkPanel = new JPanel();
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
			topPanel.add(URLTextArea, BorderLayout.EAST);
		frame.getContentPane().add(centerPanel, BorderLayout.CENTER);
			centerPanel.add(linkScrollPane);
				linkScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
					linkScrollPane.add(linkPanel);
						linkPanel.setLayout(new BoxLayout(linkPanel, BoxLayout.X_AXIS));
			centerPanel.add(seeResultButton);
				seeResultButton.addActionListener(seeButtonResultActionListener());
		frame.getContentPane().add(filterPanel, BorderLayout.WEST);
			filterPanel.add(filterScrollPane);
				filterScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
					filterScrollPane.add(new StryckyzzzFilterComponent());
			filterPanel.add(filterButton);
				//filterButton.addActionListener(filterResultPanel());
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
				updateJScrollPane();
				Logger.logInfo("See Results Button pressed");
			}
		};
	}

	private void updateJScrollPane() {
		if (cc != null) {
			cc.getVisited().forEach(s -> {
				linkScrollPane.add(new BrowserButton(s));
				Logger.logInfo("Added button for URL : " + s);
				linkScrollPane.repaint();
				Logger.logInfo("Repainted linkScrollPane");
			});
		} else {
			Logger.logInfo("CrawlerControler is Null, no buttons added");
		}
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
     * Method that starts the crawling when the button is clicked
     * This is quite weirdly written and i have no better idea to make that better
     * @return ActionListener
     */
	private ActionListener startCrawlingButtonListener() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	cc = new CrawlerController(URLTextArea.getText());
                if (!isCrawling) {
                    Logger.resetLogFile();
                    Logger.logInfo("Crawler started.");
                    isCrawling = true;
                    crawlingText.setText("Currently Crawling");
                    cc.startCrawl();
                    stopCrawlingButton.setEnabled(true);
                    Logger.logInfo("Crawling process completed.");
                    Logger.logDuration("Crawling process");
                } else {
                	Logger.logInfo("Crawler already started, stop clicking the button");
                    startCrawlButton.setEnabled(false);
                    String originalText = startCrawlButton.getText();
                    startCrawlButton.setText("Already Crawling");
                    //Yes i am using a thread instead of a javax.swing.Timer, deal with it.
                    new Thread(() -> {
                        try {
                            Thread.sleep(1000);
                            //Yes i am using javax.swing.SwingUtilities anyway, deal with it.
                            SwingUtilities.invokeLater(() -> {
                                startCrawlButton.setText(originalText);
                                startCrawlButton.setEnabled(true);
                            });
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }).start();
                }
            }
        };
	}
}
