/**
 * 
 */
package rs.e4.celledit;

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.swt.SWT;

/**
 * React on double-clicks instead of single clicks
 * @author ralph
 *
 */
public class CellEditorActivationStrategy extends
		ColumnViewerEditorActivationStrategy {

	/**
	 * Constructor.
	 * @param viewer
	 */
	public CellEditorActivationStrategy(ColumnViewer viewer) {
		super(viewer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean isEditorActivationEvent(ColumnViewerEditorActivationEvent event) {
		return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL
				|| event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION
				|| (event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED && event.keyCode == SWT.CR)
				|| event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
	}

	
}
