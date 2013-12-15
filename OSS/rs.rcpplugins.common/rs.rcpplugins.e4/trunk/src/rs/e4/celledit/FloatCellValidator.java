/**
 * 
 */
package rs.e4.celledit;

import java.text.NumberFormat;
import java.util.Locale;

import org.eclipse.jface.viewers.ICellEditorValidator;

/**
 * Checks float values.
 * @author ralph
 *
 */
public class FloatCellValidator implements ICellEditorValidator {

	private boolean allowNull;
	private NumberFormat format = NumberFormat.getNumberInstance(Locale.getDefault());
	
	/**
	 * Constructor.
	 */
	public FloatCellValidator(boolean allowNull) {
		this.allowNull = allowNull;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String isValid(Object value) {
		try {
			String s = ((String)value).trim();
			if (s.equals("")) {
				if (allowNull) return null;
				return "Invalid value";
			}
			format.parse(s);
			return null;
		} catch (Exception e) {
			return "Invalid value";
		}
	}

}
