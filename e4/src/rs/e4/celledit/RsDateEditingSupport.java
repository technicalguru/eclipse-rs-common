/**
 * 
 */
package rs.e4.celledit;

import java.util.Date;

import org.eclipse.jface.viewers.TableViewer;

import rs.baselib.util.RsDate;

/**
 * Supports only {@link RsDate}s.
 * @author ralph
 *
 */
public class RsDateEditingSupport extends DateEditingSupport {

	/**
	 * Constructor.
	 */
	public RsDateEditingSupport(TableViewer viewer, IEditingSupportModel model, boolean allowNull) {
		super(viewer, model, allowNull);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setValue(Object element, Object value) {
		if ((value != null) && value.equals("") && isAllowNull()) value = null;
		if (value != null) {
			value = new RsDate((Date)value);
		}
		setPropertyValue(element, value);
		getViewer().refresh();
	}


}
