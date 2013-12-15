/**
 * 
 */
package rs.e4.help;

import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

/**
 * Abstract handler for the help system.
 * @author ralph
 *
 */
public class AbstractHelpHandler {

	@Inject
	private EPartService partService;
	
	/**
	 * Constructor.
	 */
	public AbstractHelpHandler() {
	}

	/**
	 * Opens the help part.
	 * @return whether help was visible before
	 */
	protected boolean openHelp() {
		boolean rc = false;
		MPart helpPart = getHelpPart();
		if (helpPart != null) {
			rc = partService.isPartVisible(helpPart);
			
			// if part was not visible before: clear its history
			HelpPart obj = getHelpPartObject();
			if (obj != null) {
				obj.clearHistory();
			}
			
			// hide the part
			partService.hidePart(helpPart);

			// required if initial not visible
			helpPart.setVisible(true);

			// show the part
			partService.showPart(helpPart, PartState.VISIBLE); 
			
			// Ensure the container visibility
			MElementContainer<?> container = helpPart.getParent();
			rc &= container.isVisible() && container.isOnTop();
			container.setVisible(false);
			container.setVisible(true);
			container.setOnTop(true);
		}
		return rc;
	}
	
	/**
	 * Returns the help part.
	 * @return the help part model object
	 */
	protected MPart getHelpPart() {
		return partService.findPart(HelpPart.ID);
	}
	
	/**
	 * Returns the actual help part object.
	 * @return the object or null if it cannot be found.
	 */
	protected HelpPart getHelpPartObject() {
		MPart helpPart = getHelpPart();
		if (helpPart != null) {
			return (HelpPart)helpPart.getObject();
		}
		return null;
	}
}
