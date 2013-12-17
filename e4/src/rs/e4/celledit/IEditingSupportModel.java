/**
 * 
 */
package rs.e4.celledit;


/**
 * Provides the values for editing support.
 * @author ralph
 *
 */
public interface IEditingSupportModel {

	/**
	 * Returns true when the element can be edited.
	 * @param element element
	 * @return true or false
	 */
	public boolean canEdit(Object element);
	
	/**
	 * Provide the value of the element to be edited.
	 * @param element element
	 * @return property value
	 */
	public Object getValue(Object element);
	
	/**
	 * Set the value on the element after successful editing.
	 * @param element element
	 * @param value property value
	 */
	public void setValue(Object element, Object value);
	
	/**
	 * Adds a listener.
	 * @param listener
	 */
	public void addEditingSupportModelListener(IEditingSupportModelListener listener);
	
	/**
	 * Removes a listener.
	 * @param listener
	 */
	public void removeEditingSupportModelListener(IEditingSupportModelListener listener);

}
