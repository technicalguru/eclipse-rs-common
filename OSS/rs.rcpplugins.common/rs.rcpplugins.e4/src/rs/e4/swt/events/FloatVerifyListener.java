/**
 * 
 */
package rs.e4.swt.events;

import org.eclipse.swt.events.VerifyEvent;

/**
 * Verifies input according to required float values.
 * @author ralph
 *
 */
public class FloatVerifyListener extends AbstractVerifyListener {

	/**
	 * Constructor.
	 */
	public FloatVerifyListener() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void verifyText(VerifyEvent evt) {
		// Is it a number?
		if (!isDeleteEvent(evt)) {
			try {
				Float.parseFloat(getNewText(evt));
			} catch (NumberFormatException e) {
				evt.doit = false;
			}
		}
	}
}
