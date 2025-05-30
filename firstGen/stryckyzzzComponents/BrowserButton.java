package stryckyzzzComponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import crawlerUtils.BrowserHandler;
import utils.Logger;

public class BrowserButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7257505083095171591L;

	public BrowserButton(String s) {
		super(s);
		addActionListener(addBrowserHandle(s));
	}

	private ActionListener addBrowserHandle(String s) {
		return new ActionListener() {
            public void actionPerformed(ActionEvent a) {
            	try {
					BrowserHandler.openUrlInDefaultBrowser(s);
				} catch (Exception e) {
					Logger.logError("Failed to open Browser", e);
				}
            }
		};
	}

}
