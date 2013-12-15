/**
 * 
 */
package rs.e4.swt.action;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;

import rs.baselib.io.FileFinder;
import rs.data.api.dao.IGeneralDAO;
import rs.e4.Plugin;

/**
 * Adds a new row from a DAO to a table.
 * @author ralph
 *
 */
public class AddDaoRowAction extends ObservableListAction {

	private IGeneralDAO<?, ?> dao;
	private int editColumn;
	
	/**
	 * Constructor.
	 * @param viewer
	 * @param list
	 */
	public AddDaoRowAction(TableViewer viewer, IObservableList list, IGeneralDAO<?, ?> dao, int editColumn) {
		super(viewer, list);
		this.dao = dao;
		this.editColumn = editColumn;
		setAccelerator(SWT.INSERT);
		setDescription(Plugin.translate("actions.insert-row.description"));
		setText(Plugin.translate("actions.insert-row.text"));
		setToolTipText(Plugin.translate("actions.insert-row.tooltip"));
		setImageDescriptor(ImageDescriptor.createFromURL(FileFinder.find(AddDaoRowAction.class, "resources/icons/table--plus.png")));
		setEnabled(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		final Object newRow = dao.newInstance();
		IObservableList list = getList();
		list.add(newRow);
		getViewer().getControl().getDisplay().asyncExec(new Runnable() {
			public void run() {
				getViewer().reveal(newRow);
				if (editColumn >= 0) getViewer().editElement(newRow, editColumn);
			}
		});
	}
	
	
}
