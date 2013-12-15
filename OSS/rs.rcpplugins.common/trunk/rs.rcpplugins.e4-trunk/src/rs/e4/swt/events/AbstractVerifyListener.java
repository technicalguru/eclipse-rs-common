/**
 * 
 */
package rs.e4.swt.events;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Text;

import rs.e4.SwtUtils;

/**
 * Some general methods for verify listeners.
 * @author ralph
 *
 */
public abstract class AbstractVerifyListener implements VerifyListener {

	/**
	 * Constructor.
	 */
	public AbstractVerifyListener() {
	}

	/**
	 * Returns true when the event describes a control event (DEL, BACKSPACE)
	 * @param evt event to be checked
	 * @return <code>true</code> when event describes a control code
	 */
	protected boolean isDeleteEvent(VerifyEvent evt) {
		switch (evt.keyCode) {
		case SWT.BS:
		case SWT.DEL:
			return true;
		}
		return false;
	}
	
	/**
	 * Returns the new text from this event.
	 * @param evt change event
	 * @return new text
	 * @see #getNewText(String, VerifyEvent)
	 */
	protected String getNewText(VerifyEvent evt) {
		Text text = (Text)evt.widget;
		return getNewText(SwtUtils.getText(text), evt);
	}
	
	/**
	 * Returns the text as it would be changed to by the given event
	 * @param oldText old text string
	 * @param evt change event
	 * @return the new text
	 */
	public static String getNewText(String oldText, VerifyEvent evt) {
		StringBuilder newText = new StringBuilder(oldText);
		
		if (evt.keyCode == SWT.NONE) {
			// a replace event e.g. caused by setText()
			newText.replace(evt.start, evt.end, evt.text);
		} else if (evt.keyCode == SWT.BS) {
			// a backspace
			newText.delete(evt.start, evt.end);
		} else if (evt.keyCode == SWT.DEL) {
			// a delete
			newText.delete(evt.start, evt.end);
		} else {
			// any other key typed
			newText.replace(evt.start, evt.end, evt.text);
		}
		return newText.toString();
	}
}
