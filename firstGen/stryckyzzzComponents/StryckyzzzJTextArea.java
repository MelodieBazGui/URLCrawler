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
	
}
