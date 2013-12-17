/**
 * 
 */
package rs.e4.util;

import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.ILabelProvider;

/**
 * A general label provider that generates the text from the bean
 * property.
 * @author ralph
 *
 */
public class BeanColumnLabelProvider extends AbstractColumnLabelProvider {

	/** the bean property to be retrieved from an object for display */
	private String beanProperty;

	/**
	 * Constructor.
	 */
	public BeanColumnLabelProvider(String beanProperty, ILabelProvider valueLabelProvider, LocalResourceManager resourceManager) {
		super(valueLabelProvider, resourceManager);
		setBeanProperty(beanProperty);
	}

	/**
	 * Constructor.
	 */
	public BeanColumnLabelProvider(String beanProperty, LocalResourceManager resourceManager) {
		this(beanProperty, null, resourceManager);
	}

	/**
	 * Retrieves the value from the element.
	 * @param element element to get the value from
	 * @return the value to be used for formatting
	 */
	public Object getValue(Object element) {
		try {
			if (element == null) return getNullValue();
			Object value = PropertyUtils.getProperty(element, getBeanProperty());
			if (value == null) value = getNullValue();
			return value;
		} catch (NestedNullException e) {
			return getNullValue();
		} catch (Throwable t) {
			throw new RuntimeException("Cannot retrieve value:", t);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getErrorValue(Throwable t) {
		return "$"+getBeanProperty()+"$";
	}

	/**
	 * Returns the beanProperty.
	 * @return the beanProperty
	 */
	public String getBeanProperty() {
		return beanProperty;
	}

	/**
	 * Sets the beanProperty.
	 * @param beanProperty the beanProperty to set
	 */
	public void setBeanProperty(String beanProperty) {
		this.beanProperty = beanProperty;
	}

}
