/**
 * 
 */
package rs.e4.celledit;

import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TableViewer;

/**
 * Editing support for combo boxes.
 * @author ralph
 *
 */
public class ComboBoxEditingSupport extends AbstractEditingSupport {

	private List<?> options;
	private String display[];

	/**
	 * Constructor.
	 * @param viewer
	 * @param model
	 * @param allowNull
	 */
	public ComboBoxEditingSupport(TableViewer viewer, IComboBoxEditingSupportModel model, boolean allowNull) {
		super(viewer, model, allowNull);

	}

	/**
	 * Refreshes all options.
	 */
	protected void update(Object element) {
		options = createOptions(element);
		if (isAllowNull()) {
			display = new String[options.size()+1];
			display[0] = "";
			for (int i=0; i<options.size(); i++) {
				display[i+1] = getDisplay(options.get(i));
			}
		} else {
			display = new String[options.size()];
			for (int i=0; i<options.size(); i++) {
				display[i] = getDisplay(options.get(i));
			}
		}
	}
	

	/**
	 * Returns the display for the given value.
	 * @param value object
	 * @return display
	 */
	protected String getDisplay(Object value) {
		return ((IComboBoxEditingSupportModel)getModel()).getDisplay(value);
	}
	

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected CellEditor getCellEditor(Object element) {
		update(element);
		ComboBoxCellEditor rc = new ComboBoxCellEditor(((TableViewer)getViewer()).getTable(), display);
		rc.setActivationStyle(ComboBoxCellEditor.DROP_DOWN_ON_KEY_ACTIVATION|ComboBoxCellEditor.DROP_DOWN_ON_MOUSE_ACTIVATION|ComboBoxCellEditor.DROP_DOWN_ON_PROGRAMMATIC_ACTIVATION|ComboBoxCellEditor.DROP_DOWN_ON_TRAVERSE_ACTIVATION);
		return rc;
	}

	/**
	 * Create the options.
	 * This method forwards to the model.
	 * The NULL option must not be returned here.
	 * @return the options.
	 */
	protected List<?> createOptions(Object element) {
		return ((IComboBoxEditingSupportModel)getModel()).getOptions(element);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final Object getValue(Object element) {
		Object value = getPropertyValue(element);
		int index = options.indexOf(value);
		if (isAllowNull()) index++;
		if (index < 0) index = 0;
		return index;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setValue(Object element, Object value) {
		if (value != null) {
			int index = (Integer)value;
			if (isAllowNull()) index--;
			setPropertyValue(element, index < 0 ? null : options.get(index));
		} else {
			setPropertyValue(element, null);
		}
		getViewer().refresh();
	}


}
