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
 * Removes a row from a table.
 * @author ralph
 *
 */
public class RemoveRowAction extends ObservableListAction {

	/**
	 * Constructor.
	 * @param viewer
	 * @param list
	 */
	public RemoveRowAction(TableViewer viewer, IObservableList list) {
		super(viewer, list);
		setAccelerator(SWT.DEL);
		setDescription(Plugin.translate("actions.remove-row.description"));
		setText(Plugin.translate("actions.remove-row.text"));
		setToolTipText(Plugin.translate("actions.remove-row.tooltip"));
		setImageDescriptor(ImageDescriptor.createFromURL(FileFinder.find(RemoveRowAction.class, "resources/icons/table--minus.png")));
		setEnabled(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		IStructuredSelection selection = (IStructuredSelection)getViewer().getSelection();
		IObservableList list = getList();
		if (!selection.isEmpty()) {
			for (Object row : selection.toArray()) {
				list.remove(row);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void selectionChanged(IStructuredSelection selection) {
		setEnabled(!selection.isEmpty());
	}
	
	
}
