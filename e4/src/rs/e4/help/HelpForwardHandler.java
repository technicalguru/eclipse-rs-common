 
package rs.e4.help;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.CanExecute;

/**
 * Handles the forward command.
 * @author ralph
 *
 */
public class HelpForwardHandler extends AbstractHelpHandler {
	
	@Execute
	public void execute() {
		HelpPart part = getHelpPartObject();
		if (part != null) part.goForward();
	}
	
	
	@CanExecute
	public boolean canExecute() {
		HelpPart part = getHelpPartObject();
		if (part != null) return part.canGoForward();
		return false;
	}
		
}