 
package rs.e4.help;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.osgi.service.event.Event;

/**
 * A handler to open context help.
 * @author ralph
 *
 */
public class ContextHelpHandler extends AbstractHelpHandler {
	
	/**
	 * Execute help handler
	 */
	@Execute
	public void execute() {
		openHelp();
		
		// TODO: When no help context is available
		getHelpPartObject().openContents();
		getHelpPartObject().expandToLevel(1);
	}

	@Inject
	@Optional
	private void helpUrlChanged(@UIEventTopic(HelpSystem.TOPIC_CONTEXT_URL)Event event) {
	}
	
	@Inject
	@Optional
	private void helpIdChanged(@UIEventTopic(HelpSystem.TOPIC_CONTEXT_ID)Event event) {
	}

}