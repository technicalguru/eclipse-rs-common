/**
 * 
 */
package rs.e4.celledit;

import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;

/**
 * Abstract implementation of editing support.
 * @author ralph
 *
 */
public abstract class AbstractEditingSupport extends EditingSupport {

	private IEditingSupportModel model;
	private boolean allowNull;
	
	/**
	 * Constructor.
	 * @param viewer
	 */
	public AbstractEditingSupport(TableViewer viewer, IEditingSupportModel model, boolean allowNull) {
		super(viewer);
		this.model = model;
		this.allowNull = allowNull;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean canEdit(Object element) {
		return model.canEdit(element);
	}

	/**
	 * Provide the value of the element to be edited.
	 * @param element element
	 * @return property value
	 * @see IEditingSupportModel
	 */
	protected Object getPropertyValue(Object element) {
		return format(model.getValue(element));
	}
	
	/**
	 * Formats the value for initial entry in editing widget.
	 * @param value value to be formatted (can be null)
	 * @return formatted value.
	 */
	protected Object format(Object value) {
		return value;
	}
	
	/**
	 * Set the value on the element after successful editing.
	 * @param element element
	 * @param value property value
	 * @see IEditingSupportModel
	 */
	protected void setPropertyValue(Object element, Object value) {
		model.setValue(element, value);
	}

	/**
	 * Returns the model.
	 * @return the model
	 */
	public IEditingSupportModel getModel() {
		return model;
	}

	/**
	 * Returns the allowNull.
	 * @return the allowNull
	 */
	public boolean isAllowNull() {
		return allowNull;
	}
	
	
}
