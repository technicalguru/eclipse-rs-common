/**
 * 
 */
package rs.e4.util;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

import rs.baselib.lang.LangUtils;
import rs.e4.SwtUtils;

/**
 * Image Provider for a radio button image.
 * @author ralph
 *
 */
public class RadioButtonImageProvider implements ICellImageProvider {
	
	private static final String CHECKED_KEY = "RADIO_CHECKED";
	private static final String UNCHECK_KEY = "RADIO_UNCHECKED";

	public RadioButtonImageProvider(Shell shell) {
		if( JFaceResources.getImageRegistry().getDescriptor(CHECKED_KEY) == null ) {
			JFaceResources.getImageRegistry().put(UNCHECK_KEY, SwtUtils.makeShot(shell, SWT.CHECK, false));
			JFaceResources.getImageRegistry().put(CHECKED_KEY, SwtUtils.makeShot(shell, SWT.CHECK, true));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Image getImage(Object element, Object cellValue) {
		if(LangUtils.getBoolean(cellValue)) {
			return JFaceResources.getImageRegistry().getDescriptor(CHECKED_KEY).createImage();
		} else {
			return JFaceResources.getImageRegistry().getDescriptor(UNCHECK_KEY).createImage();
		}
	}

}