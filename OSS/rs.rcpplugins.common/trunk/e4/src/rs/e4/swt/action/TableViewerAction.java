/**
 * 
 */
package rs.e4.swt.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;

/**
 * An abstract action that belongs to a table.
 * @author ralph
 *
 */
public abstract class TableViewerAction extends Action implements ISelectionChangedListener {

	private TableViewer viewer;
	private IStructuredSelection selection;
	
	/**
	 * Constructor.
	 */
	public TableViewerAction(TableViewer viewer) {
		this.viewer = viewer;
		viewer.addSelectionChangedListener(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		
		ISelection selection = event.getSelection();
		if (selection instanceof IStructuredSelection) {
			this.selection = (IStructuredSelection)selection;
			selectionChanged((IStructuredSelection)selection);
		}
		
	}

	/**
	 * Override this if you need to react on selection changes, e.g. enabling/disabling
	 * the action. Default implementation saves the selection.
	 * @param selection the new selection.
	 */
	protected void selectionChanged(IStructuredSelection selection) {
	}

	
	/**
	 * Returns the selection.
	 * @return the selection
	 */
	public final IStructuredSelection getSelection() {
		return selection;
	}

	/**
	 * Returns the viewer.
	 * @return the viewer
	 */
	public final TableViewer getViewer() {
		return viewer;
	}
	
	
}
