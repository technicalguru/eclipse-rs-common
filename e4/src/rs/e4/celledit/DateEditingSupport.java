/**
 * 
 */
package rs.e4.celledit;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;

/**
 * Supports date editing.
 * @author ralph
 *
 */
public class DateEditingSupport extends AbstractEditingSupport {

	/**
	 * @param viewer
	 * @param model
	 * @param allowNull
	 */
	public DateEditingSupport(TableViewer viewer, IEditingSupportModel model, boolean allowNull) {
		super(viewer, model, allowNull);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected CellEditor getCellEditor(Object element) {
		RsDateChooserComboCellEditor rc = new RsDateChooserComboCellEditor(((TableViewer)getViewer()).getTable());
		return rc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final Object getValue(Object element) {
		Object rc = getPropertyValue(element);
		if (rc != null) {
			if (rc instanceof Date) return rc;
			if (rc instanceof Calendar) return ((Calendar)rc).getTime();
			if (rc instanceof Long) return new Date(((Long)rc).longValue());
		}
		return rc;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setValue(Object element, Object value) {
		if (value.equals("") && isAllowNull()) value = null;
		if (value != null) {
			Object oldValue = getPropertyValue(element);
			if (oldValue != null) {
				if (oldValue instanceof Calendar) {
					try {
						Calendar cal = (Calendar)oldValue.getClass().newInstance();
						cal.setTime((Date)value);
						value = cal;
					} catch (Exception e) {
						throw new RuntimeException("Cannot create new instance of "+oldValue.getClass());
					}
				} else if (oldValue instanceof Long) {
					value = ((Date)value).getTime();
				}
			}
		}
		setPropertyValue(element, value);
		getViewer().refresh();
	}


}
