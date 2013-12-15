/**
 * 
 */
package rs.e4.help;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;

/**
 * Stops loading a page.
 * @author ralph
 *
 */
public class HelpStopHandler extends AbstractHelpHandler {

	@Execute
	public void execute() {
		HelpPart part = getHelpPartObject();
		if (part != null) part.stop();
	}

	@CanExecute
	public boolean canExecute() {
		HelpPart part = getHelpPartObject();
		if (part != null) return part.isLoading();
		return false;
	}
}
