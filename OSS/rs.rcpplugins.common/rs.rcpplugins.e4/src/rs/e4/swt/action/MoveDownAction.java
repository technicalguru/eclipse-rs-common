/**
 * 
 */
package rs.e4.swt.action;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;

import rs.baselib.io.FileFinder;
import rs.e4.Plugin;

/**
 * Moves up the selected rows by one index.
 * @author ralph
 *
 */
public class MoveDownAction extends ObservableListAction {

	/**
	 * Constructor.
	 * @param viewer
	 * @param list
	 */
	public MoveDownAction(TableViewer viewer, IObservableList list) {
		super(viewer, list);
		setAccelerator(SWT.CTRL|SWT.ARROW_DOWN);
		setDescription(Plugin.translate("actions.move-down.description"));
		setText(Plugin.translate("actions.move-down.text"));
		setToolTipText(Plugin.translate("actions.move-down.tooltip"));
		setImageDescriptor(ImageDescriptor.createFromURL(FileFinder.find(MoveDownAction.class, "resources/icons/arrow-270.png")));
		setEnabled(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void selectionChanged(IStructuredSelection selection) {
		super.selectionChanged(selection);
		setEnabled(!selection.isEmpty());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		IObservableList list = getList();
		Object rows[] = getSelection().toArray();
		int index = list.indexOf(rows[0]);
		if (index < list.size()-1) {
			int firstTxPosition = index++;
			// Move all selected objects at index+1
			for (Object row : rows) {
				list.move(list.indexOf(row), firstTxPosition+1);
				firstTxPosition++;
			}
			
		}
	}

	
}
