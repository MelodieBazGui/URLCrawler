package utils.interfaces;

public interface ContentChangeListener {
	
	    void update(String newValue);
	    boolean hasChanged();
	    boolean isEmpty();
	    void setToDefault();
	    String getCurrent();
	    String getPrevious();
	    
}
