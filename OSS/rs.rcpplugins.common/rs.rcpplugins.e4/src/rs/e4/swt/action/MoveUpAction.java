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
public class MoveUpAction extends ObservableListAction {

	/**
	 * Constructor.
	 * @param viewer
	 * @param list
	 */
	public MoveUpAction(TableViewer viewer, IObservableList list) {
		super(viewer, list);
		setAccelerator(SWT.CTRL|SWT.ARROW_UP);
		setDescription(Plugin.translate("actions.move-up.description"));
		setText(Plugin.translate("actions.move-up.text"));
		setToolTipText(Plugin.translate("actions.move-up.tooltip"));
		setImageDescriptor(ImageDescriptor.createFromURL(FileFinder.find(MoveUpAction.class, "resources/icons/arrow-090.png")));
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
		if (index > 0) {
			// Move all selected objects at index-1
			index--;
			for (Object row : rows) {
				list.move(list.indexOf(row), index++);
			}
		}
	}

	
}
