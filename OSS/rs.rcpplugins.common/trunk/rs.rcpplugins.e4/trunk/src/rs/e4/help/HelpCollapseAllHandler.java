 
package rs.e4.help;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.CanExecute;

/**
 * Handles the forward command.
 * @author ralph
 *
 */
public class HelpCollapseAllHandler extends AbstractHelpHandler {
	
	@Execute
	public void execute() {
		HelpPart part = getHelpPartObject();
		if (part != null) part.collapseAll();
	}
	
	
	@CanExecute
	public boolean canExecute() {
		HelpPart part = getHelpPartObject();
		if (part != null) return part.isTreeVisible();
		return false;
	}
		
}