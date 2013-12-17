/**
 * 
 */
package rs.e4.util;

import java.util.Locale;
import java.util.Map;

import org.eclipse.jface.viewers.LabelProvider;

import rs.baselib.util.CommonUtils;

/**
 * Provides the label from a map.
 * @author ralph
 *
 */
public class MapLabelProvider extends LabelProvider {

	private Map<?,?> map;
	
	/**
	 * Constructor.
	 */
	public MapLabelProvider(Map<?,?> map) {
		this.map = map;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getText(Object element) {
		return CommonUtils.getDisplay(map.get(element), Locale.getDefault());
	}

}
