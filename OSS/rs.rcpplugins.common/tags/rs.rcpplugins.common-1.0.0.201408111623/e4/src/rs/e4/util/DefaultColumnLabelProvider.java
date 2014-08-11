/**
 * 
 */
package rs.e4.util;

import org.eclipse.jface.resource.LocalResourceManager;

/**
 * Simply print the the value.
 * @author ralph
 *
 */
public class DefaultColumnLabelProvider extends AbstractColumnLabelProvider {

	/**
	 * Constructor.
	 * @param resourceManager
	 */
	public DefaultColumnLabelProvider(LocalResourceManager resourceManager) {
		super(resourceManager);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getValue(Object element) {
		return element == null ? "" : element.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getErrorValue(Throwable t) {
		return t.getMessage();
	}

}
