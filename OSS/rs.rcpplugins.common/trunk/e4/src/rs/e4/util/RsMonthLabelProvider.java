/**
 * 
 */
package rs.e4.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.eclipse.jface.viewers.LabelProvider;

import rs.baselib.util.RsMonth;

/**
 * Formats with RsMonth.
 * @author ralph
 *
 */
public class RsMonthLabelProvider extends LabelProvider {

	public static final RsMonthLabelProvider INSTANCE = new RsMonthLabelProvider();
	
	private final SimpleDateFormat HEADER_FORMATTER = new SimpleDateFormat("MMMM yyyy");
	
	/**
	 * Constructor.
	 */
	public RsMonthLabelProvider() {
		this(Locale.getDefault());
	}
	
	/**
	 * Constructor.
	 */
	public RsMonthLabelProvider(Locale locale) {
	}


	@Override
	public String getText(Object element) {
		if (element == null) return "";
		if (element instanceof RsMonth) {
			return HEADER_FORMATTER.format(((RsMonth)element).getBegin().getTime());
		} else if (element instanceof Date) {
			return HEADER_FORMATTER.format((Date)element);
		}
		return "$"+element.toString()+"$";
	}

}
