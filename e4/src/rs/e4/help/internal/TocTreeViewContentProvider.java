/**
 * 
 */
package rs.e4.help.internal;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * The content provider for the contents tree.
 * @author ralph
 *
 */
public class TocTreeViewContentProvider implements ITreeContentProvider {

	/**
	 * Constructor.
	 */
	public TocTreeViewContentProvider() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		return (Toc[]) inputElement;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] getChildren(Object parentElement) {
		Toc toc = (Toc) parentElement;
		return toc.getChildren().toArray();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getParent(Object element) {
		Toc toc = (Toc) element;
		if (toc.getParent() == toc.getRoot()) return null;
		return toc.getParent();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasChildren(Object element) {
		Toc toc = (Toc) element;
		return !toc.getChildren().isEmpty();
	}

}
