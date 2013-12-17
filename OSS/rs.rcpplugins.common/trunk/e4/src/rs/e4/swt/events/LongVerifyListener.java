/**
 * 
 */
package rs.e4.swt.events;

import org.eclipse.swt.events.VerifyEvent;

/**
 * Verifies input according to required long values.
 * @author ralph
 *
 */
public class LongVerifyListener extends AbstractVerifyListener {

	/**
	 * Constructor.
	 */
	public LongVerifyListener() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void verifyText(VerifyEvent evt) {
		// Is it a number?
		if (!isDeleteEvent(evt)) {
			try {
				Long.parseLong(evt.text);
			} catch (NumberFormatException e) {
				evt.doit = false;
			}
		}
	}
}
