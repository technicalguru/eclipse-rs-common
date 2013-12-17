/**
 * 
 */
package rs.e4.util;

import java.text.Format;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;

/**
 * A default adapter for fonts, colors, images and labels.
 * @author ralph
 *
 */
public class ColumnAdapter extends DefaultLabelProvider implements ICellColorProvider, ICellFontProvider, ICellImageProvider {

	private static ColumnAdapter defaultInstance = new ColumnAdapter();
	
	/**
	 * Returns the default instance that cannot be altered.
	 * @return the default instance.
	 */
	public static ColumnAdapter getInstance() {
		return defaultInstance;
	}
	
	/**
	 * Constructor.
	 */
	public ColumnAdapter() {
	}

	/**
	 * Constructor.
	 * @param format
	 * @param nullValue
	 */
	public ColumnAdapter(Format format, String nullValue) {
		super(format, nullValue);
		
	}

	/**
	 * Constructor.
	 * @param format
	 */
	public ColumnAdapter(Format format) {
		this(format, null);
		
	}

	/**
	 * Constructor.
	 * @param nullValue
	 */
	public ColumnAdapter(String nullValue) {
		this(null, nullValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Font getFont(Object element, Object cellValue) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RGB getForeground(Object element, Object cellValue) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RGB getBackground(Object element, Object cellValue) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Image getImage(Object element, Object cellValue) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean isDefaultInstance() {
		return this == defaultInstance;
	}

	

}
