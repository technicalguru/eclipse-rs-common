/**
 * 
 */
package rs.e4.celledit;

import org.eclipse.nebula.widgets.datechooser.DateChooserCombo;
import org.eclipse.swt.widgets.Composite;

/**
 * @author ralph
 *
 */
public class RsDateChooserCombo extends DateChooserCombo {

	/**
	 * Constructor.
	 * @param parent
	 * @param style
	 */
	public RsDateChooserCombo(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * Opens the popup.
	 */
	public void openPopup() {
		dropDown(true);
	}
}
