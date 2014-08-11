
package rs.e4.help;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;

/**
 * A handler to open the help system.
 * @author ralph
 *
 */
public class HelpHandler extends AbstractHelpHandler {

	/**
	 * Execute help handler
	 */
	@Execute
	public void execute(MHandledItem handledItem) {
		openHelp();
		
		getHelpPartObject().openContents();
		getHelpPartObject().expandToLevel(2);
	}


}