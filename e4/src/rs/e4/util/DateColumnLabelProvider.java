/**
 * 
 */
package rs.e4.util;

import java.util.Locale;

import org.eclipse.jface.resource.LocalResourceManager;

/**
 * Formats currencies.
 * @author ralph
 *
 */
public class DateColumnLabelProvider extends BeanColumnLabelProvider {

	/**
	 * @param beanProperty
	 * @param valueLabelProvider
	 */
	public DateColumnLabelProvider(String beanProperty, LocalResourceManager resourceManager) {
		this(beanProperty, Locale.getDefault(), resourceManager);

	}

	/**
	 * @param beanProperty
	 */
	public DateColumnLabelProvider(String beanProperty, Locale locale, LocalResourceManager resourceManager) {
		super(beanProperty, new DateLabelProvider(locale), resourceManager);
	}

	
}
