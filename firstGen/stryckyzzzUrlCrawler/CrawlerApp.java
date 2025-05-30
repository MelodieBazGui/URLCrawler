package stryckyzzzUrlCrawler;

import java.awt.EventQueue;

import javax.swing.JFrame;

import userInterface.CrawlerMain;
import utils.Logger;

public class CrawlerApp {

	/**
	 * Simple App Launcher, this does not keep any values outside of CrawlerMain and subsequently used classes
	 * @author Mélodie Bazeries Guilbault
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Logger.resetLogFile();
					CrawlerMain cm = new CrawlerMain();
					cm.window();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
