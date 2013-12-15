/**
 * 
 */
package rs.e4.swt.events;

import org.eclipse.swt.events.VerifyEvent;

/**
 * Verifies input according to length limitation.
 * @author ralph
 *
 */
public class LengthVerifyListener extends AbstractVerifyListener {

	private int maxLength;
	
	/**
	 * Constructor.
	 */
	public LengthVerifyListener(int maxLength) {
		this.maxLength = maxLength;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void verifyText(VerifyEvent evt) {
		// Is it a number?
		if (!isDeleteEvent(evt)) {
			evt.doit = getNewText(evt).length() <= maxLength;
		}
	}
}
