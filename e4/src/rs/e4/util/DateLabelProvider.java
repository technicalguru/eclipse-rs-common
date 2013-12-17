/**
 * 
 */
package rs.e4.util;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.eclipse.jface.viewers.LabelProvider;

import rs.baselib.util.RsDate;

/**
 * Formats with currency.
 * @author ralph
 *
 */
public class DateLabelProvider extends LabelProvider {

	public static final DateLabelProvider INSTANCE = new DateLabelProvider();
	
	private DateFormat formatter;
	
	/**
	 * Constructor.
	 */
	public DateLabelProvider() {
		this(Locale.getDefault());
	}
	
	/**
	 * Constructor.
	 */
	public DateLabelProvider(Locale locale) {
		formatter = DateFormat.getDateInstance(DateFormat.SHORT, locale);
	}


	@Override
	public String getText(Object element) {
		if (element == null) return "";
		if (element instanceof RsDate) {
			return formatter.format(((RsDate)element).getTime());
		} else if (element instanceof Date) {
			return formatter.format((Date)element);
		}
		return "$"+element.toString()+"$";
	}

}
