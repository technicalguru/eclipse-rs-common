/**
 * 
 */
package rs.e4.help.internal;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.Bundle;
import org.slf4j.LoggerFactory;

/**
 * The history of the browser.
 * @author ralph
 *
 */
public class BrowserHistory {

	/** new entries are added at the end */
	private List<HistoryEntry> history = new ArrayList<>();
	private int currentIndex = -1;
	
	/**
	 * Constructor.
	 */
	public BrowserHistory() {
	}

	/**
	 * Returns the current entry.
	 * @return the entry or null
	 */
	public HistoryEntry getCurrentEntry() {
		if (currentIndex >= 0) return history.get(currentIndex);
		return null;
	}
	
	/**
	 * Adds the help page to the history.
	 * Any pages after the current page in history will be lost.
	 * @param bundle bundle context information
	 * @param url URL of help page
	 */
	public HistoryEntry add(Bundle bundle, URL url) {
		return add(new HistoryEntry(bundle, url));
	}

	/**
	 * Adds the help page to the history.
	 * Any pages after the current page in history will be lost.
	 * @param type help page type
	 */
	public HistoryEntry add(HelpPageType type) {
		return add(type, null);
	}

	/**
	 * Adds the help page to the history.
	 * Any pages after the current page in history will be lost.
	 * @param type help page type
	 * @param text to be stored along
	 */
	public HistoryEntry add(HelpPageType type, String text) {
		return add(new HistoryEntry(type, text));
	}
	
	/**
	 * Adds the external page to the history.
	 * Any pages after the current page in history will be lost.
	 * @param text to be stored along
	 */
	public HistoryEntry add(String url) {
		try {
			return add(new HistoryEntry(new URL(url)));
		} catch (MalformedURLException e) {
			LoggerFactory.getLogger(getClass()).error("Cannot construct URL from: "+url, e);
		}
		return null;
	}
	
	/**
	 * Adds the help page to the history.
	 * Any pages after the current page in history will be lost.
	 * @param entry entry to be added
	 */
	public HistoryEntry add(HistoryEntry entry) {
		currentIndex++;
		while (currentIndex < history.size()) {
			history.remove(currentIndex);
		}
		history.add(entry);
		return entry;
	}
	
	/**
	 * Whether it is possible to go back in history.
	 * @return <code>true</code> if there are entries in the history
	 */
	public boolean canGoBack() {
		return currentIndex > 0;
	}
	
	/**
	 * Returns the previous entry in history.
	 * @return the entry or <code>null</code> if there is none.
	 */
	public HistoryEntry goBack() {
		if (canGoBack()) {
			currentIndex--;
			return getCurrentEntry();
		}
		return null;
	}
	
	/**
	 * Whether it is possible to go forward in history.
	 * @return <code>true</code> if there are entries in the history
	 */
	public boolean canGoForward() {
		return currentIndex < history.size() - 1;
	}
	
	/**
	 * Returns the next entry in history.
	 * @return the entry or <code>null</code> if there is none.
	 */
	public HistoryEntry goForward() {
		if (canGoForward()) {
			currentIndex++;
			return getCurrentEntry();
		}
		return null;
	}
	
	/**
	 * Clears the history.
	 */
	public void clear() {
		history.clear();
		currentIndex = -1;
	}
	
	/** Type of help pages */
	public static enum HelpPageType {
		CONTENTS, INDEX, PAGE, ERROR, EXTERNAL
	}
	
	/**
	 * The entry holding information about a help page.
	 * @author ralph
	 *
	 */
	public static class HistoryEntry {
		public HelpPageType type;
		public Bundle bundle;
		public URL url;
		public String text;
		public HistoryEntry(Bundle bundle, URL url) {
			this.type = HelpPageType.PAGE;
			this.bundle = bundle;
			this.url = url;
		}
		public HistoryEntry(HelpPageType type, String text) {
			this.type = type;
			this.text = text;
		}
		public HistoryEntry(URL url) {
			this.type = HelpPageType.EXTERNAL;
			this.url = url;
		}
	}
}
