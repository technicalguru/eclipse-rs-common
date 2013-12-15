 
package rs.e4.help;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.CanExecute;

/**
 * Handles the forward command.
 * @author ralph
 *
 */
public class HelpBackHandler extends AbstractHelpHandler {
	
	@Execute
	public void execute() {
		HelpPart part = getHelpPartObject();
		if (part != null) part.goBack();
	}
	
	
	@CanExecute
	public boolean canExecute() {
		HelpPart part = getHelpPartObject();
		if (part != null) return part.canGoBack();
		return false;
	}
		
}