/**
 * 
 */
package rs.e4.swt.events;

import org.eclipse.swt.events.VerifyEvent;

/**
 * Verifies input according to required integer values.
 * @author ralph
 *
 */
public class IntegerVerifyListener extends AbstractVerifyListener {

	/**
	 * Constructor.
	 */
	public IntegerVerifyListener() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void verifyText(VerifyEvent evt) {
		// Is it a number?
		if (!isDeleteEvent(evt)) {
			try {
				Integer.parseInt(evt.text);
			} catch (NumberFormatException e) {
				evt.doit = false;
			}
		}
	}
}
