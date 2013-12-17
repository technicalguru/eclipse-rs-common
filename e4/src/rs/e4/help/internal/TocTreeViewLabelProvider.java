/**
 * 
 */
package rs.e4.help.internal;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import rs.baselib.io.FileFinder;

/**
 * The label provider for the contents tree.
 * @author ralph
 *
 */
public class TocTreeViewLabelProvider extends StyledCellLabelProvider implements ITreeViewerListener, MouseMoveListener {

	private Image tocOpenImage;
	private Image tocClosedImage;
	private Image topicFolderImage;
	private Image topicImage;
	private Color foreground;
	private Color background;
	private Set<Toc> opened = new HashSet<>();
	private Toc hovered;
	private Cursor linkCursor;
	private Cursor normalCursor;
	private Cursor cursor;
	
	@Inject
	private UISynchronize synch;

	@Inject
	private TreeViewer treeViewer;

	/**
	 * Constructor.
	 */
	public TocTreeViewLabelProvider() {

	}

	@PostConstruct
	protected void postConstruct(LocalResourceManager resourceManager) {
		tocOpenImage = resourceManager.createImage(ImageDescriptor.createFromURL(FileFinder.find(getClass(), "resources/icons/help/toc_open.gif")));
		tocClosedImage = resourceManager.createImage(ImageDescriptor.createFromURL(FileFinder.find(getClass(), "resources/icons/help/toc_closed.gif")));
		topicFolderImage = resourceManager.createImage(ImageDescriptor.createFromURL(FileFinder.find(getClass(), "resources/icons/help/container_obj.gif")));
		topicImage = resourceManager.createImage(ImageDescriptor.createFromURL(FileFinder.find(getClass(), "resources/icons/help/topic.gif")));
		foreground = resourceManager.createColor(new RGB(0,0,139));
		background = treeViewer.getTree().getBackground();
		linkCursor = new Cursor(treeViewer.getTree().getDisplay(), SWT.CURSOR_HAND);
		normalCursor = new Cursor(treeViewer.getTree().getDisplay(), SWT.CURSOR_ARROW);
		cursor = normalCursor;
	}

	public String getText(Object element) {
		Toc toc = (Toc) element;
		String name = toc.getLabel();
		if (name == null) name = toc.getTopic().getLabel();
		return name;
	}

	@Override
	public void update(ViewerCell cell)	{
		Toc toc = (Toc) cell.getElement();
		String s = getText(toc);
		
		/* make text look like a link */
		StyledString text = new StyledString();
		StyleRange myStyledRange = new StyleRange(0, s.length(), getForeground(toc), null);
		if (hovered == toc && toc.hasLink()) {
			myStyledRange.underline = true;
		}
		text.append(s, StyledString.DECORATIONS_STYLER);
		cell.setText(text.toString());

		StyleRange[] range = { myStyledRange };
		cell.setStyleRanges(range);
		cell.setImage(getImage(toc));
		cell.setForeground(getForeground(toc));
		cell.setBackground(getBackground(toc));
		cell.setFont(getFont(toc));
		super.update(cell);
	}

	public Image getImage(Object element) {
		Toc toc = (Toc) element;
		if (toc.getParent() == toc.getRoot()) {
			return opened.contains(toc) ? tocOpenImage : tocClosedImage;
		} else if (!toc.getChildren().isEmpty()) {
			return topicFolderImage;
		}
		return topicImage;
	}

	/**
	 * {@inheritDoc}
	 */
	public Font getFont(Object element) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Color getForeground(Object element) {
		Toc toc = (Toc) element;
		return toc.hasLink() ? foreground : null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Color getBackground(Object element) {
		return background;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void treeCollapsed(TreeExpansionEvent event) {
		opened.remove((Toc)event.getElement());
		refreshViewer();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void treeExpanded(TreeExpansionEvent event) {
		opened.add((Toc)event.getElement());
		refreshViewer();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void mouseMove(MouseEvent e) {
		Tree tree = (Tree)e.getSource();
		Point point = new Point (e.x, e.y);
		TreeItem item = tree.getItem(point);
		boolean doRefresh = false;
		if (item != null) {
			Toc toc = (Toc)item.getData();
			doRefresh = hovered != toc;
			hovered = toc;
			cursor = toc.hasLink() ? linkCursor : normalCursor;
		} else {
			doRefresh = hovered != null;
			hovered = null;
			cursor = normalCursor;
		}
		if (doRefresh) refreshViewer();
	}

	protected void refreshViewer() {
		synch.asyncExec(new Runnable() {

			@Override
			public void run() {
				treeViewer.refresh(true);
				Tree tree = treeViewer.getTree();
				Cursor oldCursor = tree.getCursor();
				if (oldCursor != cursor) {
					tree.setCursor(cursor);
				}
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		normalCursor.dispose();
		linkCursor.dispose();
		super.dispose();
	}


}
