/**
 * 
 */
package rs.e4.util;

import org.eclipse.jface.viewers.LabelProvider;

import rs.baselib.util.BeanValueProvider;

/**
 * A simple label provider based on beans.
 * @author ralph
 *
 */
public class BeanLabelProvider extends LabelProvider {

	/** the value provider */
	private BeanValueProvider valueProvider;
	/** the value to be used when the property is null */
	private String nullValue;

	/**
	 * Constructor.
	 */
	public BeanLabelProvider(String beanProperty) {
		valueProvider = new BeanValueProvider(beanProperty);
	}

	/**
	 * {@inheritDoc}
	 * You should not override here but {@link #format(Object)}.
	 */
	@Override
	public String getText(Object element) {
		Object value = valueProvider.getValue(element);
		if (value == null) value = getNullValue();
		return format(value);
	}

	/**
	 * The default implementation returns the string representation.
	 * @param value value to format (can be {@link #getNullValue() the null value})
	 * @return a string representing the value ("" for NULL).
	 */
	protected String format(Object value) {
		return value != null ? value.toString() : "";
	}
	
	/**
	 * Returns the beanProperty.
	 * @return the beanProperty
	 */
	public String getBeanProperty() {
		return valueProvider.getBeanProperty();
	}

	/**
	 * Sets the beanProperty.
	 * @param beanProperty the beanProperty to set
	 */
	public void setBeanProperty(String beanProperty) {
		valueProvider.setBeanProperty(beanProperty);
	}

	/**
	 * Returns the nullValue.
	 * @return the nullValue
	 */
	public String getNullValue() {
		return nullValue;
	}

	/**
	 * Sets the nullValue.
	 * @param nullValue the nullValue to set
	 */
	public void setNullValue(String nullValue) {
		this.nullValue = nullValue;
	}


}
