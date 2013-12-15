/**
 * 
 */
package rsbaselib.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;

/**
 * Provides the label for a month.
 * @author ralph
 *
 */
public class MonthLabelProvider extends LabelProvider {

	public static final ILabelProvider INSTANCE = new MonthLabelProvider();
	
	private int offset = 0;
	private DateFormat formatter = new SimpleDateFormat("MMMM", Locale.getDefault());
	
	/**
	 * Constructor.
	 */
	public MonthLabelProvider() {
		this(0);
	}

	/**
	 * Constructor.
	 */
	public MonthLabelProvider(int offset) {
		this.offset = offset;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getText(Object element) {
		if (element instanceof Number) {
			int month = ((Number)element).intValue()+offset;
			Calendar cal = new GregorianCalendar(2000, month, 1);
			return formatter.format(cal.getTime());
		}
		return "";
	}

	
}
