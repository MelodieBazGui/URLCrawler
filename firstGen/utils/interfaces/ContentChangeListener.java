package utils.interfaces;

public interface ContentChangeListener {
	
	    void update(String newValue);
	    boolean hasChanged();
	    boolean isEmpty();
	    String setToDefault();
	    String getCurrent();
	    String getPrevious();
	    
}
