package stryckyzzzComponents;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextArea;

import utils.interfaces.ContentChangeListener;

public class StryckyzzzJTextArea extends JTextArea implements ContentChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8673621449413904005L;

	private String previous;
	public static final String DEFAULT_TEXT = "https://www.yourweburl.extension";
	
	public StryckyzzzJTextArea (String s) {
		super(s);
		previous = s;
	}

	@Override
	public void update(String newValue) {
		this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                previous = getText();
            }

            @Override
            public void focusLost(FocusEvent e) {
                String newText = getText();
                if (hasChanged()) {
                    setText(newText);
                }
                if (isEmpty()) {
                	setToDefault();
                }
            }
        });
	}

	@Override
	public boolean hasChanged() {
		if(!this.getText().contains(previous)) {
			return false;
		}
		return true;
	}

	@Override
	public String getCurrent() {
		return this.getText();
	}

	@Override
	public String getPrevious() {
		return this.previous;
	}

	@Override
	public boolean isEmpty() {
		return this.getText().isBlank();
	}

	@Override
	public void setToDefault() {
		setText(DEFAULT_TEXT);
	}
	
}
