/**
 * 
 */
package rs.e4.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An abstract label provider that provides basic value and 
 * formatting behaviour.
 * 
 * The secondary provider vor colors, fonts and images are requested in order of
 * sequence. The first provider returning a value will determine the value.
 * @author ralph
 *
 */
public abstract class AbstractColumnLabelProvider extends ColumnLabelProvider {

	private static Logger log = LoggerFactory.getLogger(AbstractColumnLabelProvider.class);

	/** The resource manager */
	private LocalResourceManager resourceManager;
	/** the provider delivering the string out of the value. */
	private ILabelProvider valueLabelProvider;
	/** The color providers for the cell */
	private List<ICellColorProvider> colorProviders = new ArrayList<>();
	/** The font providers to be used */
	private List<ICellFontProvider> fontProviders = new ArrayList<>();
	/** the image providers to be used */
	private List<ICellImageProvider> imageProviders = new ArrayList<>();
	/** the value to be used when the property is null */
	private Object nullValue;
	
	/**
	 * Constructor.
	 */
	public AbstractColumnLabelProvider(LocalResourceManager resourceManager) {
		this(null, resourceManager);
	}

	/**
	 * Constructor.
	 */
	public AbstractColumnLabelProvider(ILabelProvider valueLabelProvider, LocalResourceManager resourceManager) {
		setValueLabelProvider(valueLabelProvider);
		this.resourceManager = resourceManager;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getText(Object element) {
		try {
			// Retrieve the value
			Object value = getValue(element);

			// Format the value
			return getValueLabelProvider().getText(value);
		} catch (Throwable t) {
			log.error("Cannot build text: ", t);
			return getErrorValue(t);
		}
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Font getFont(Object element) {
		try {
			// Retrieve the value
			Object value = getValue(element);

			// Format the value
			Font rc = null;
			for (ICellFontProvider provider : getFontProviders()) {
				rc = provider.getFont(element, value);
				if (rc != null) return rc;
			}
		} catch (Throwable t) {
			log.error("Cannot create font: ", t);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Color getBackground(Object element) {
		try {
			// Retrieve the value
			Object value = getValue(element);

			// Format the value
			RGB rc = null;
			for (ICellColorProvider provider : getColorProviders()) {
				rc = provider.getBackground(element, value);
				if (rc != null) return resourceManager.createColor(rc);
			}
		} catch (Throwable t) {
			log.error("Cannot create color: ", t);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Color getForeground(Object element) {
		try {
			// Retrieve the value
			Object value = getValue(element);

			// Format the value
			RGB rc = null;
			for (ICellColorProvider provider : getColorProviders()) {
				rc = provider.getForeground(element, value);
				if (rc != null) return resourceManager.createColor(rc);
			}
		} catch (Throwable t) {
			log.error("Cannot create color: ", t);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Image getImage(Object element) {
		try {
			// Retrieve the value
			Object value = getValue(element);

			// Format the value
			Image rc = null;
			for (ICellImageProvider provider : getImageProviders()) {
				rc = provider.getImage(element, value);
				if (rc != null) return rc;
			}
		} catch (Throwable t) {
			log.error("Cannot create image: ", t);
		}
		return null;
	}

	/**
	 * Retrieves the value from the element.
	 * @param element element to get the value from
	 * @return the value to be used for formatting
	 */
	public abstract Object getValue(Object element);

	/**
	 * Returns the label to be used in case of errors.
	 * @return
	 */
	protected abstract String getErrorValue(Throwable t);
	
	/**
	 * Returns the nullValue.
	 * @return the nullValue
	 */
	public Object getNullValue() {
		return nullValue;
	}

	/**
	 * Sets the nullValue.
	 * @param nullValue the nullValue to set
	 */
	public void setNullValue(Object nullValue) {
		this.nullValue = nullValue;
	}

	/**
	 * Returns the valueLabelProvider.
	 * @return the valueLabelProvider
	 */
	public ILabelProvider getValueLabelProvider() {
		return valueLabelProvider;
	}

	/**
	 * Sets the valueLabelProvider.
	 * @param valueLabelProvider the valueLabelProvider to set
	 */
	public void setValueLabelProvider(ILabelProvider valueLabelProvider) {
		if (valueLabelProvider == null) valueLabelProvider = ColumnAdapter.getInstance();
		this.valueLabelProvider = valueLabelProvider;
	}

	/**
	 * Returns the colorProvider.
	 * @return the colorProvider
	 */
	public ICellColorProvider[] getColorProviders() {
		return colorProviders.toArray(new ICellColorProvider[colorProviders.size()]);
	}

	/**
	 * Sets the colorProvider.
	 * @param colorProvider the colorProvider to set
	 */
	public void addColorProvider(ICellColorProvider colorProvider) {
		colorProviders.add(colorProvider);
	}

	/**
	 * Returns the fontProvider.
	 * @return the fontProvider
	 */
	public ICellFontProvider[] getFontProviders() {
		return fontProviders.toArray(new ICellFontProvider[fontProviders.size()]);
	}

	/**
	 * Sets the fontProvider.
	 * @param fontProvider the fontProvider to set
	 */
	public void addFontProvider(ICellFontProvider fontProvider) {
		fontProviders.add(fontProvider);
	}

	/**
	 * Returns the imageProvider.
	 * @return the imageProvider
	 */
	public ICellImageProvider[] getImageProviders() {
		return imageProviders.toArray(new ICellImageProvider[imageProviders.size()]);
	}

	/**
	 * Sets the imageProvider.
	 * @param imageProvider the imageProvider to set
	 */
	public void setImageProvider(ICellImageProvider imageProvider) {
		imageProviders.add(imageProvider);
	}
	
}
