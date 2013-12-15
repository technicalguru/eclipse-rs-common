/**
 * 
 */
package rs.e4.celledit;

/**
 * Gets informed about actions in an editing support model.
 * @author ralph
 *
 */
public interface IEditingSupportModelListener {
	/**
	 * Called before value will be retrieved.
	 * @param element bean
	 * @param beanProperty name of property
	 */
	public void beforeGetValue(Object element, String beanProperty);
	
	/**
	 * Called after value was retrieved.
	 * @param element bean
	 * @param beanProperty name of property
	 */
	public void afterGetValue(Object element, String beanProperty);

	/**
	 * Called before value will be set.
	 * @param element bean
	 * @param beanProperty name of property
	 * @param value property value
	 */
	public void beforeSetValue(Object element, String beanProperty, Object value);
	
	/**
	 * Called after value was set.
	 * @param element bean
	 * @param beanProperty name of property
	 * @param value property value
	 */
	public void afterSetValue(Object element, String beanProperty, Object value);

}
