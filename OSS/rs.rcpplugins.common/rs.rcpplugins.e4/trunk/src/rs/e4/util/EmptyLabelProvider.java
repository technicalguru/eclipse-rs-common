/**
 * 
 */
package rs.e4.util;

import org.eclipse.jface.viewers.LabelProvider;

/**
 * A label provider always returning null.
 * This provider can be useful when u don't want to see any label but just colors or images.
 * @author ralph
 *
 */
public class EmptyLabelProvider extends LabelProvider {

	private static final EmptyLabelProvider defaultInstance = new EmptyLabelProvider();
	
	/**
	 * Returns the default instance.
	 * @return
	 */
	public static LabelProvider getInstance() {
		return defaultInstance;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getText(Object element) {
		return "";
	}

	
}
