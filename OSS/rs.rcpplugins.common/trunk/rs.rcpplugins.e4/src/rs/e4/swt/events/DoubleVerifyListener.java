/**
 * 
 */
package rs.e4.swt.events;

import org.eclipse.swt.events.VerifyEvent;

/**
 * Verifies input according to required double values.
 * @author ralph
 *
 */
public class DoubleVerifyListener extends AbstractVerifyListener {

	/**
	 * Constructor.
	 */
	public DoubleVerifyListener() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void verifyText(VerifyEvent evt) {
		// Is it a number?
		if (!isDeleteEvent(evt)) {
			try {
				Double.parseDouble(getNewText(evt));
			} catch (NumberFormatException e) {
				evt.doit = false;
			}
		}
	}
}
