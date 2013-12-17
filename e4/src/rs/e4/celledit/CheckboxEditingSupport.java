/**
 * 
 */
package rs.e4.celledit;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.TableViewer;

/**
 * Editing support for checkboxes.
 * @author ralph
 *
 */
public class CheckboxEditingSupport extends AbstractEditingSupport {

	/**
	 * Constructor.
	 * @param viewer
	 * @param model
	 * @param allowNull
	 */
	public CheckboxEditingSupport(TableViewer viewer, IEditingSupportModel model) {
		super(viewer, model, false);

	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected CellEditor getCellEditor(Object element) {
		return new CheckboxCellEditor(((TableViewer)getViewer()).getTable());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final Object getValue(Object element) {
		return getPropertyValue(element);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setValue(Object element, Object value) {
		setPropertyValue(element, value);
		getViewer().refresh();
	}


}
