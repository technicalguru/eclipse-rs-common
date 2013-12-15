/**
 * 
 */
package rs.e4;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimBar;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimElement;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuFactory;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBarElement;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBarSeparator;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

/**
 * A processor adding toolbar icons to all menus.
 * @author ralph
 *
 */
public class ToolbarProcessor {

	@Execute
	public void execute(EModelService modelService, MApplication application) {
		for (MWindow window : application.getChildren()) {
			if (window instanceof MTrimmedWindow) {
				MTrimmedWindow trimmedWindow = (MTrimmedWindow)window;
				for (MTrimBar trimBar : trimmedWindow.getTrimBars()) {
					for (MTrimElement elem : trimBar.getChildren()) {
						if (elem instanceof MToolBar) {
							//if (elem.getTags().contains("addhelp")) {
							addHelpToolItem((MToolBar)elem, application);
							//}
						}
					}
				}
			}
		}
	}

	protected void addHelpToolItem(MToolBar toolBar, MApplication application) {
		removeCommand(toolBar, "rs.rcpplugins.e4.command.help");
		addSeparator(toolBar);
		MCommand command = getCommand(application, "rs.rcpplugins.e4.command.help");
		addCommand(toolBar, command, "rs.rcpplugins.e4.handledtoolitem.help", "platform:/plugin/rs.rcpplugins.e4/resources/icons/question-white.png", Plugin.translate("%e4xmi.menu.help.label"));
	}

	protected static void removeCommand(MToolBar toolBar, String commandId) {
		List<MToolBarElement> toBeRemoved = new ArrayList<>();
		List<MToolBarElement>  children = toolBar.getChildren();
		for (MToolBarElement elem : children) {
			if (elem instanceof MHandledItem) {
				MCommand command = ((MHandledItem)elem).getCommand();
				if (command != null) {
					String s = command.getElementId();
					if (commandId.equals(s)) {
						toBeRemoved.add(elem);
					}
				}
			}
		}
		children.removeAll(toBeRemoved);
	}

	protected static void addSeparator(MToolBar toolBar) {
		// Only when last element is not a separator
		List<MToolBarElement>  children = toolBar.getChildren();
		if ((children.size() == 0) || !(children.get(children.size()-1) instanceof MToolBarSeparator)) {
			MToolBarSeparator separator = MMenuFactory.INSTANCE.createToolBarSeparator();
			children.add(separator);
		}
	}

	protected static void addCommand(MToolBar toolBar, MCommand command, String elementId, String iconUri, String tooltip) {
		List<MToolBarElement>  children = toolBar.getChildren();
		MHandledToolItem toolItem = MMenuFactory.INSTANCE.createHandledToolItem();
		toolItem.setElementId(elementId);
		toolItem.setCommand(command);
		toolItem.setIconURI(iconUri);
		toolItem.setTooltip(tooltip);
		children.add(toolItem);
	}

	protected static MCommand getCommand(MApplication application, String commandId) {
		for (MCommand command : application.getCommands()) {
			if (command.getElementId().equals(commandId)) {
				return command;
			}
		}
		return null;
	}
}
