/**
 * 
 */
package rs.e4.about;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.internal.about.AboutTextManager;

/**
 * @author ralph
 *
 */
public class RsAboutTextManager extends AboutTextManager {

	private StyledText styledText;

	private Cursor handCursor;

	private Cursor busyCursor;

	private boolean mouseDown = false;

	private boolean dragEvent = false;

	/**
	 * Constructor.
	 */
	public RsAboutTextManager(StyledText text) {
		super(text);
		this.styledText = text;
		createCursors();
		addListeners();
	}

    private void createCursors() {
        handCursor = new Cursor(styledText.getDisplay(), SWT.CURSOR_HAND);
        busyCursor = new Cursor(styledText.getDisplay(), SWT.CURSOR_WAIT);
        styledText.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                handCursor.dispose();
                handCursor = null;
                busyCursor.dispose();
                busyCursor = null;
            }
        });
    }

    protected void addListeners() {
		if (styledText != null) {
			styledText.addMouseListener(new MouseAdapter() {
				public void mouseDown(MouseEvent e) {
					if (e.button != 1) {
						return;
					}
					mouseDown = true;
				}

				public void mouseUp(MouseEvent e) {
					mouseDown = false;
					int offset = styledText.getCaretOffset();
					if (dragEvent) {
						// don't activate a link during a drag/mouse up operation
						dragEvent = false;
						if (getItem() != null && getItem().isLinkAt(offset)) {
							styledText.setCursor(handCursor);
						}
					} else if (getItem() != null && getItem().isLinkAt(offset)) {
						styledText.setCursor(busyCursor);
						openLink(getItem().getLinkAt(offset));
						StyleRange selectionRange = getCurrentRange();
						if (selectionRange != null) {
							styledText.setSelectionRange(selectionRange.start, selectionRange.length);
						}
						styledText.setCursor(null);
					}
				}
			});

			styledText.addMouseMoveListener(new MouseMoveListener() {
				public void mouseMove(MouseEvent e) {
					// Do not change cursor on drag events
					if (mouseDown) {
						if (!dragEvent) {
							StyledText text = (StyledText) e.widget;
							text.setCursor(null);
						}
						dragEvent = true;
						return;
					}
					StyledText text = (StyledText) e.widget;
					int offset = -1;
					try {
						offset = text.getOffsetAtLocation(new Point(e.x, e.y));
					} catch (IllegalArgumentException ex) {
						// leave value as -1
					}
					if (offset == -1) {
						text.setCursor(null);
					} else if (getItem() != null && getItem().isLinkAt(offset)) {
						text.setCursor(handCursor);
					} else {
						text.setCursor(null);
					}
				}
			});

			styledText.addTraverseListener(new TraverseListener() {
				public void keyTraversed(TraverseEvent e) {
					switch (e.detail) {
					case SWT.TRAVERSE_ESCAPE:
						e.doit = true;
						break;
					case SWT.TRAVERSE_TAB_NEXT:
						//Previously traverse out in the backward direction?
						Point nextSelection = styledText.getSelection();
						int charCount = styledText.getCharCount();
						if ((nextSelection.x == charCount)
								&& (nextSelection.y == charCount)) {
							styledText.setSelection(0);
						}
						StyleRange nextRange = findNextRange();
						if (nextRange == null) {
							// Next time in start at beginning, also used by 
							// TRAVERSE_TAB_PREVIOUS to indicate we traversed out
							// in the forward direction
							styledText.setSelection(0);
							e.doit = true;
						} else {
							styledText.setSelectionRange(nextRange.start,
									nextRange.length);
							e.doit = true;
							e.detail = SWT.TRAVERSE_NONE;
						}
						break;
					case SWT.TRAVERSE_TAB_PREVIOUS:
						//Previously traverse out in the forward direction?
						Point previousSelection = styledText.getSelection();
						if ((previousSelection.x == 0)
								&& (previousSelection.y == 0)) {
							styledText.setSelection(styledText.getCharCount());
						}
						StyleRange previousRange = findPreviousRange();
						if (previousRange == null) {
							// Next time in start at the end, also used by 
							// TRAVERSE_TAB_NEXT to indicate we traversed out
							// in the backward direction
							styledText.setSelection(styledText.getCharCount());
							e.doit = true;
						} else {
							styledText.setSelectionRange(previousRange.start,
									previousRange.length);
							e.doit = true;
							e.detail = SWT.TRAVERSE_NONE;
						}
						break;
					default:
						break;
					}
				}
			});

			//Listen for Tab and Space to allow keyboard navigation
			styledText.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent event) {
					StyledText text = (StyledText) event.widget;
					if (event.character == ' ' || event.character == SWT.CR) {
						if (getItem() != null) {
							//Be sure we are in the selection
							int offset = text.getSelection().x + 1;

							if (getItem().isLinkAt(offset)) {
								text.setCursor(busyCursor);
								openLink(getItem().getLinkAt(offset));
								StyleRange selectionRange = getCurrentRange();
								text.setSelectionRange(selectionRange.start,
										selectionRange.length);
								text.setCursor(null);
							}
						}
						return;
					}
				}
			});
		}
	}

	protected void openLink(String href) {
		RsAboutUtils.openLink(styledText.getShell(), href);
	}

	/**
	 * Find the range of the current selection.
	 */
	protected StyleRange getCurrentRange() {
		StyleRange[] ranges = styledText.getStyleRanges();
		int currentSelectionEnd = styledText.getSelection().y;
		int currentSelectionStart = styledText.getSelection().x;

		for (int i = 0; i < ranges.length; i++) {
			if ((currentSelectionStart >= ranges[i].start)
					&& (currentSelectionEnd <= (ranges[i].start + ranges[i].length))) {
				return ranges[i];
			}
		}
		return null;
	}

	/**
	 * Find the next range after the current 
	 * selection.
	 */
	protected StyleRange findNextRange() {
		StyleRange[] ranges = styledText.getStyleRanges();
		int currentSelectionEnd = styledText.getSelection().y;

		for (int i = 0; i < ranges.length; i++) {
			if (ranges[i].start >= currentSelectionEnd) {
				return ranges[i];
			}
		}
		return null;
	}

	/**
	 * Find the previous range before the current selection.
	 */
	protected StyleRange findPreviousRange() {
		StyleRange[] ranges = styledText.getStyleRanges();
		int currentSelectionStart = styledText.getSelection().x;

		for (int i = ranges.length - 1; i > -1; i--) {
			if ((ranges[i].start + ranges[i].length - 1) < currentSelectionStart) {
				return ranges[i];
			}
		}
		return null;
	}

}
