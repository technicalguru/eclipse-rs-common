/**
 * 
 */
package rs.e4.celledit;

import org.eclipse.nebula.widgets.datechooser.DateChooserComboCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * @author ralph
 *
 */
public class RsDateChooserComboCellEditor extends DateChooserComboCellEditor {

	/**
	 * Constructor.
	 * @param parent
	 */
	public RsDateChooserComboCellEditor(Composite parent) {
		super(parent);

	}

	/**
	 * Constructor.
	 * @param parent
	 * @param style
	 */
	public RsDateChooserComboCellEditor(Composite parent, int style) {
		super(parent, style);

	}

	/**
	 * {@inheritDoc}
	 */
	protected Control createControl(Composite parent) {
		combo = new RsDateChooserCombo(parent, getStyle());
		combo.setFont(parent.getFont());

		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				switch ( event.type ) {
				case SWT.Traverse :
					if ( event.detail == SWT.TRAVERSE_ESCAPE
					|| event.detail == SWT.TRAVERSE_RETURN ) {
						event.doit = false;
					}
					break;

				case SWT.FocusOut :
					RsDateChooserComboCellEditor.this.focusLost();
					break;
				}				
			}
		};
		combo.addListener(SWT.Traverse, listener);
		combo.addListener(SWT.FocusOut, listener);
		return combo;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doSetFocus() {
		super.doSetFocus();
		((RsDateChooserCombo)combo).openPopup();
	}

	
}
