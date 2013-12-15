/**
 * 
 */
package rs.e4.swt.action;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.jface.viewers.TableViewer;

/**
 * Extends the {@link TableViewerAction} by an observable list.
 * @author ralph
 *
 */
public abstract class ObservableListAction extends TableViewerAction {

	private IObservableList list;
	
	/**
	 * Constructor.
	 * @param viewer
	 */
	public ObservableListAction(TableViewer viewer, IObservableList list) {
		super(viewer);
		this.list = list;
	}

	/**
	 * Returns the list.
	 * @return the list
	 */
	public final IObservableList getList() {
		return list;
	}

	
}
