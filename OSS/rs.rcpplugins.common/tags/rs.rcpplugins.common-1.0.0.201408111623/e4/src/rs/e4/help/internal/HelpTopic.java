/**
 * 
 */
package rs.e4.help.internal;

/**
 * A definition of a topic.
 * @author ralph
 *
 */
public class HelpTopic {

	private String label;
	private String anchorId;
	private String href;
	
	/**
	 * Constructor.
	 */
	public HelpTopic(String label) {
		this(label, null);
	}

	/**
	 * Constructor.
	 */
	public HelpTopic(String label, String href) {
		setLabel(label);
		setHref(href);
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
	 * Returns the {@link #anchorId}.
	 * @return the anchorId
	 */
	public String getAnchorId() {
		return anchorId;
	}

	/**
	 * Sets the {@link #anchorId}.
	 * @param anchorId the anchorId to set
	 */
	public void setAnchorId(String anchorId) {
		this.anchorId = anchorId;
	}

	/**
	 * Returns the {@link #href}.
	 * @return the href
	 */
	public String getHref() {
		return href;
	}

	/**
	 * Sets the {@link #href}.
	 * @param href the href to set
	 */
	public void setHref(String href) {
		this.href = href;
	}

	
}
