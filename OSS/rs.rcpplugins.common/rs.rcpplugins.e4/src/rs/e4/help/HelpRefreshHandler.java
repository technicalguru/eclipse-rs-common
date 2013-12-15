/**
 * 
 */
package rs.e4.help;

import org.eclipse.e4.core.di.annotations.Execute;

/**
 * Refreshes the help pages (Reload).
 * @author ralph
 *
 */
public class HelpRefreshHandler extends AbstractHelpHandler {

	@Execute
	public void execute() {
		HelpPart part = getHelpPartObject();
		if (part != null) part.refresh();
	}
}
