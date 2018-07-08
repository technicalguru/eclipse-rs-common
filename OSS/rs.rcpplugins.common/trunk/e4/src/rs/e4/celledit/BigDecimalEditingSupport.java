/**
 * 
 */
package rs.e4.celledit;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

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
public class BigDecimalEditingSupport extends AbstractEditingSupport {

	private DecimalFormat format;
	private Logger log = LoggerFactory.getLogger(BigDecimalEditingSupport.class);
	private int scale;
	
	/**
	 * Constructor.
	 * @param viewer
	 * @param model
	 * @param allowNull
	 */
	public BigDecimalEditingSupport(TableViewer viewer, IEditingSupportModel model, boolean allowNull, int scale) {
		super(viewer, model, allowNull);
		format = new DecimalFormat();
		format.setParseBigDecimal(true);
		format.setMinimumFractionDigits(scale);
		format.setMaximumFractionDigits(scale);
		this.scale = scale;
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
		return format.format(((Number)value));
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
				BigDecimal bd = (BigDecimal)format.parse((String)value);
				bd.setScale(scale, RoundingMode.HALF_UP);
				setPropertyValue(element, bd);
			} catch (Exception e) {
				// Do nothing
				log.error("Cannot parse "+value, e);
			}
		}
		getViewer().refresh();
	}


}
