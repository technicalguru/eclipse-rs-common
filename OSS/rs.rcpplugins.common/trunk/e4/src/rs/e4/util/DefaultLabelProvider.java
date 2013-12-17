/**
 * 
 */
package rs.e4.util;

import java.text.Format;
import java.util.Locale;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import rs.baselib.util.ILocaleDisplayProvider;

/**
 * A default label provider that provides the text for a value
 * by using a null label and a formatter.
 * @author ralph
 *
 */
public class DefaultLabelProvider extends LabelProvider {

	private static final DefaultLabelProvider defaultProvider = new DefaultLabelProvider();
	
	/**
	 * Returns a default label provider.
	 * @return the default provider.
	 */
	public static DefaultLabelProvider getInstance() {
		return defaultProvider;
	}
	
	/** the label to be used when the property is null */
	private String nullLabel;
	/** the format to be used */
	private Format format;
	
	/**
	 * Constructor.
	 */
	public DefaultLabelProvider() {
	}

	/**
	 * Constructor.
	 */
	public DefaultLabelProvider(Format format, String nullValue) {
		setFormat(format);
		setNullLabel(nullValue);
	}

	/**
	 * Constructor.
	 */
	public DefaultLabelProvider(Format format) {
		this(format, null);
	}

	/**
	 * Constructor.
	 */
	public DefaultLabelProvider(String nullValue) {
		this(null, nullValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Image getImage(Object element) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getText(Object element) {
		String rc = null;
		if (element != null) rc = format(element);
		if (rc == null) rc = getNullLabel();
		if (rc == null) rc = "";
		return rc;
	}

	/**
	 * Format the object with the {@link #getFormat()}
	 * object if set. Otherwise return toString().
	 * @param element element to be formatted (null-safe)
	 * @return the formatted element label
	 */
	protected String format(Object element) {
		Format format = getFormat();
		if (format != null) return format.format(element);
		if (element instanceof ILocaleDisplayProvider) return ((ILocaleDisplayProvider)element).getDisplay(Locale.getDefault());
		return element.toString();
	}
	
	/**
	 * Returns the nullLabel.
	 * @return the nullLabel
	 */
	public String getNullLabel() {
		return nullLabel;
	}

	/**
	 * Sets the nullLabel.
	 * @param nullLabel the nullLabel to set
	 */
	public void setNullLabel(String nullLabel) {
		checkInstance();
		this.nullLabel = nullLabel;
	}

	/**
	 * Returns the format.
	 * @return the format
	 */
	public Format getFormat() {
		return format;
	}

	/**
	 * Sets the format.
	 * @param format the format to set
	 */
	public void setFormat(Format format) {
		checkInstance();
		this.format = format;
	}

	/**
	 * Ensures that this instance is not the default provider. 
	 */
	protected void checkInstance() {
		if (isDefaultInstance()) throw new IllegalStateException("The default provider cannot be altered");
	}
	
	/**
	 * Returns true when this instance is the default instance.
	 * Descendant shall override this method if they want to block
	 * altering static instances.
	 * @return true when altering shall be blocked.
	 */
	protected boolean isDefaultInstance() {
		return this == defaultProvider;
	}
}
