/**
 * 
 */
package rs.e4.help.internal;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.Bundle;

/**
 * Defines a Table of Contents entry.
 * @author ralph
 *
 */
public class Toc {

	private Toc parent;
	/** The bundle */
	private Bundle bundle;
	/** URL of file this TOC was defined */
	private URL url;
	/** Defined label for this TOC entry */
	private String label;
	/** Link for this TOC entry */
	private String linkTo;
	/** Topic to be linked to */
	private HelpTopic topic;
	/** Children of this TOC entry */
	private List<Toc> children;
	
	/**
	 * Constructor.
	 * @param parent the parent TOC entry
	 */
	public Toc(Toc parent) {
		this(parent, null, null);
	}
	
	/**
	 * Constructor.
	 * @param parent the parent TOC entry
	 * @param label the label to be displayed
	 * @param topic the associated topic to be linked
	 */
	public Toc(Toc parent, String label) {
		this(parent, label, null);
	}
	
	/**
	 * Constructor.
	 * @param parent the parent TOC entry
	 * @param label the label to be displayed
	 * @param topic the associated topic to be linked
	 */
	public Toc(Toc parent, String label, HelpTopic topic) {
		this.parent = parent;
		this.label = label;
		this.topic = topic;
		children = new ArrayList<>();
	}

	/**
	 * Returns the {@link #label}.
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Sets the {@link #label}.
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Returns the {@link #topic}.
	 * @return the topic
	 */
	public HelpTopic getTopic() {
		return topic;
	}

	/**
	 * Sets the {@link #topic}.
	 * @param topic the topic to set
	 */
	public void setTopic(HelpTopic topic) {
		this.topic = topic;
	}

	/**
	 * Returns the {@link #parent}.
	 * @return the parent
	 */
	public Toc getParent() {
		return parent;
	}

	/**
	 * Returns the {@link #linkTo}.
	 * @return the linkTo
	 */
	public String getLinkTo() {
		return linkTo;
	}

	public boolean hasLink() {
		HelpTopic topic = getTopic();
		if (topic != null) {
			return topic.getHref() != null;
		}
		return false;
	}
	
	public String getLink() {
		HelpTopic topic = getTopic();
		if (topic != null) {
			return topic.getHref();
		}
		return null;
	}
	
	/**
	 * Sets the {@link #linkTo}.
	 * @param linkTo the linkTo to set
	 */
	public void setLinkTo(String linkTo) {
		this.linkTo = linkTo;
	}

	/**
	 * Returns the {@link #children}.
	 * @return the children
	 */
	public List<Toc> getChildren() {
		return new ArrayList<Toc>(children);
	}

	/**
	 * Add child to end of list.
	 * @param child child to be added
	 */
	private void addChild(Toc child) {
		addChild(child, children.size());
	}
	
	/**
	 * Add child to list.
	 * @param child child to be added
	 * @param index where to insert the child
	 */
	private void addChild(Toc child, int index) {
		children.add(index, child);
	}
	
	/**
	 * Create a new TOC as child of this TOC.
	 * @return the new child
	 */
	public Toc createChild() {
		Toc rc = new Toc(this);
		addChild(rc);
		return rc;
	}
	
	/**
	 * Is this TOC a root entry?
	 * @return <code>true</code> when this TOC is a root entry
	 */
	public boolean isRoot() {
		return getParent() == null;
	}
	
	/**
	 * Returns the root TOC entry.
	 * @return root TOC entry
	 */
	public Toc getRoot() {
		if (isRoot()) return this;
		return getParent().getRoot(); 
	}
	
	/**
	 * Returns the local root TOC entry.
	 * The local root entry is the first entry in parent hierarchy that has a filename defined.
	 * @return local root TOC entry
	 */
	public Toc getLocalRoot() {
		if ((getUrl() != null ) || isRoot()) return this;
		return getParent().getLocalRoot(); 
	}
	
	/**
	 * Returns the {@link #url}.
	 * @return the URL
	 */
	public URL getUrl() {
		return url;
	}

	/**
	 * Returns the bundle.
	 * @return the bundle
	 */
	public Bundle getBundle() {
		return bundle;
	}

	/**
	 * Sets the {@link #url}.
	 * @param url the URL to set
	 */
	public void setUrl(Bundle bundle, URL url) {
		this.bundle = bundle;
		this.url = url;
	}

	/**
	 * Returns true when this TOC is the given anchor.
	 * @param anchorId anchor to be checked
	 * @return <code>true</code> when TOC defines this anchor
	 */
	public boolean isAnchor(String anchorId) {
		HelpTopic topic = getTopic();
		if (topic == null) return false;
		return (anchorId.equals(topic.getAnchorId()));
	}
	
	/**
	 * Finds the topic with given anchor ID.
	 * @param anchorId anchor to be found
	 * @return the topic or <code>null</code> if not found
	 */
	public HelpTopic findAnchor(String anchorId) {
		// Shortcut in case it's this TOC entry
		if (isAnchor(anchorId)) return getTopic();
		// Ask the root to handle this problem
		return findAnchor(getRoot(), anchorId);
	}
	
	/**
	 * Returns the TOC entry with given link.
	 * @param filename filename of TOC (<code>null</code> for local search)
	 * @param anchorId anchor to be found
	 * @return the TOC entry or null
	 */
	public Toc findToc(String filename, String anchorId) {
		Toc toc = filename != null? findTocRoot(getRoot(), filename) : getLocalRoot();
		return findToc(toc, anchorId);
	}
	
	/**
	 * Find the TOC entry defined at given file.
	 * @param toc TOC to be searched at
	 * @param filename filename to be looked for
	 * @return the TOC entry or <code>null</code> if not found
	 */
	protected static Toc findTocRoot(Toc toc, String filename) {
		for (Toc child : toc.children) {
			URL url = child.getUrl();
			if (url != null) {
				String f = child.getUrl().getFile();
				if (filename.equals(f)) return child;
			}
			Toc rc = findToc(child, filename);
			if (rc != null) return rc;
		}
		return null;
	}
	
	/**
	 * Find the topic with given anchor at given TOC entry.
	 * @param toc the TOC entry to look at
	 * @param anchorId the anchor ID to be found
	 * @return the help topic with given anchor or <code>null</code> if not found
	 */
	protected static HelpTopic findAnchor(Toc toc, String anchorId) {
		Toc t = findToc(toc, anchorId);
		if (t != null) return t.getTopic();
		return null;
	}
	
	/**
	 * Find the TOC entry with given anchor at given TOC entry.
	 * @param toc the TOC entry to look at
	 * @param anchorId the anchor ID to be found
	 * @return the help topic with given anchor or <code>null</code> if not found
	 */
	protected static Toc findToc(Toc toc, String anchorId) {
		if (toc.isAnchor(anchorId)) return toc;
		for (Toc child : toc.getChildren()) {
			Toc rc = findToc(child, anchorId);
			if (rc != null) return rc;
		}
		return null;
	}

}
