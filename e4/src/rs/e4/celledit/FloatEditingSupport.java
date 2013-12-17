/**
 * 
 */
package rs.e4.celledit;

import java.text.NumberFormat;
import java.util.Locale;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Editing support for text fields.
 * @author ralph
 *
 */
public class FloatEditingSupport extends AbstractEditingSupport {

	private NumberFormat format = NumberFormat.getNumberInstance(Locale.getDefault());
	private Logger log = LoggerFactory.getLogger(FloatEditingSupport.class);
	
	/**
	 * Constructor.
	 * @param viewer
	 * @param model
	 * @param allowNull
	 */
	public FloatEditingSupport(TableViewer viewer, IEditingSupportModel model, boolean allowNull) {
		super(viewer, model, allowNull);

	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected CellEditor getCellEditor(Object element) {
		TextCellEditor ed = new TextCellEditor(((TableViewer)getViewer()).getTable());
		ed.setValidator(new FloatCellValidator(isAllowNull()));
		return ed;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final Object getValue(Object element) {
		Object rc = getPropertyValue(element);
		if (rc == null) rc = "";
		return rc.toString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object format(Object value) {
		if (value == null) return null;
		return format.format(((Number)value).floatValue());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setValue(Object element, Object value) {
		if (value.equals("") && isAllowNull()) {
			setPropertyValue(element, null);
		} else {
			try {
				setPropertyValue(element, format.parse((String)value).floatValue());
			} catch (Exception e) {
				// Do nothing
				log.error("Cannot parse "+value, e);
			}
		}
		getViewer().refresh();
	}


}
