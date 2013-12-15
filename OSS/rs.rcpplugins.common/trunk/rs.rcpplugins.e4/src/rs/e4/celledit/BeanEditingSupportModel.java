/**
 * 
 */
package rs.e4.celledit;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;


/**
 * A simple editing support model that sets and retrieves values via a bean property.
 * Note that there is a listener where you can hook in to the actions, e.g.
 * for database or transaction management.
 * @author ralph
 *
 */
public class BeanEditingSupportModel implements IEditingSupportModel {

	private String beanProperty;
	private Set<IEditingSupportModelListener> listeners = new HashSet<IEditingSupportModelListener>();
	
	/**
	 * Constructor.
	 */
	public BeanEditingSupportModel(String beanProperty) {
		this.beanProperty = beanProperty;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canEdit(Object element) {
		try {
			PropertyUtils.getProperty(element, beanProperty);
			return true;
		} catch (RuntimeException e) {
			throw e;
		} catch (NoSuchMethodException e) {
			// Ignore
		} catch (Exception e) {
			throw new RuntimeException("Cannot retrieve "+beanProperty, e);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getValue(Object element) {
		try {
			beforeGetValue(element);
			Object rc = _getValue(element);
			afterGetValue(element);
			return rc;
		} catch (RuntimeException e) {
			throw e;
		} catch (NoSuchMethodException e) {
			// Ignore
		} catch (Exception e) {
			throw new RuntimeException("Cannot retrieve "+beanProperty, e);
		}
		return null;
	}

	/**
	 * Actually retrieves the value.
	 * @param element the bean
	 * @return bean property value
	 * @throws Exception
	 */
	protected Object _getValue(Object element) throws Exception {
		return PropertyUtils.getProperty(element, beanProperty);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(Object element, Object value) {
		try {
			beforeSetValue(element, value);
			_setValue(element, value);
			afterSetValue(element, value);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("Cannot set "+beanProperty+" to "+value+"["+(value != null ? value.getClass().getSimpleName() : "")+"]", e);
		}
	}

	/**
	 * Actually sets the bean property
	 * @param element the bean
	 * @param value the property value
	 * @throws Exception
	 */
	protected void _setValue(Object element, Object value) throws Exception {
		BeanUtils.setProperty(element, beanProperty, value);
	}
	
	/**
	 * Called before value will be retrieved.
	 * @param element bean
	 */
	protected void beforeGetValue(Object element) {
		for (IEditingSupportModelListener l : listeners) l.beforeGetValue(element, beanProperty);
	}
	
	/**
	 * Called after value was retrieved.
	 * @param element bean
	 */
	protected void afterGetValue(Object element) {
		for (IEditingSupportModelListener l : listeners) l.afterGetValue(element, beanProperty);
	}

	/**
	 * Called before value will be set.
	 * @param element bean
	 * @param value property value
	 */
	protected void beforeSetValue(Object element, Object value) {
		for (IEditingSupportModelListener l : listeners) l.beforeSetValue(element, beanProperty, value);
	}
	
	/**
	 * Called after value was set.
	 * @param element bean
	 * @param value property value
	 */
	protected void afterSetValue(Object element, Object value) {
		for (IEditingSupportModelListener l : listeners) l.afterSetValue(element, beanProperty, value);
	}
	
	/**
	 * Adds a listener.
	 * @param listener
	 */
	public void addEditingSupportModelListener(IEditingSupportModelListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Removes a listener.
	 * @param listener
	 */
	public void removeEditingSupportModelListener(IEditingSupportModelListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Returns the name of the bean property.
	 * @return property name
	 */
	public String getBeanProperty() {
		return beanProperty;
	}
}
