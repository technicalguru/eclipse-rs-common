/**
 * 
 */
package rs.e4.celledit;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

/**
 * Editing support for text fields.
 * @author ralph
 *
 */
public class TextEditingSupport extends AbstractEditingSupport {

	/**
	 * Constructor.
	 * @param viewer
	 * @param model
	 * @param allowNull
	 */
	public TextEditingSupport(TableViewer viewer, IEditingSupportModel model, boolean allowNull) {
		super(viewer, model, allowNull);

	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected CellEditor getCellEditor(Object element) {
		return new TextCellEditor(((TableViewer)getViewer()).getTable());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final Object getValue(Object element) {
		Object rc = getPropertyValue(element);
		if (rc == null) rc = "";
		return rc;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setValue(Object element, Object value) {
		if (value.equals("") && isAllowNull()) value = null;
		setPropertyValue(element, value);
		getViewer().refresh();
	}


}
