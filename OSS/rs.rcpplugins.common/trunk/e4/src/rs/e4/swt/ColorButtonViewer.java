/**
 * 
 */
package rs.e4.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;

import rs.e4.Plugin;

/**
 * A controller for buttons that open color pickers.
 * @author ralph
 *
 */
public class ColorButtonViewer {

	protected static int MARGIN = 6;
	protected static int ARC_SIZE = 5;
	
	private Button button;
	private RGB color;
	private String dialogTitle;
	
	/**
	 * Constructor.
	 */
	public ColorButtonViewer(Composite parent) {
		this(parent, SWT.PUSH);
	}

	/**
	 * Constructor.
	 */
	public ColorButtonViewer(Composite parent, int style) {
		this(new Button(parent, style));
	}

	/**
	 * Constructor.
	 */
	public ColorButtonViewer(Button button) {
		this.button = button;
		setDialogTitle(Plugin.translate("colorbuttonviewer.dialog.title"));
		
		// Make it a square button?
		button.setText("     ");
		button.setToolTipText(Plugin.translate("colorbuttonviewer.tooltip"));
		Point size = button.getSize();
		button.setSize(size.y, size.y);
		
		// Install all listeners
		installListeners();
	}

	/**
	 * Returns the controlled button.
	 * @return
	 */
	public Button getButton() {
		return button;
	}
	
	/**
	 * Returns the dialogTitle.
	 * @return the dialogTitle
	 */
	public String getDialogTitle() {
		return dialogTitle;
	}

	/**
	 * Sets the dialogTitle.
	 * @param dialogTitle the dialogTitle to set
	 */
	public void setDialogTitle(String dialogTitle) {
		this.dialogTitle = dialogTitle;
	}

	/**
	 * Installs the listeners.
	 */
	protected void installListeners() {
		button.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				buttonSelected(e);
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				buttonSelected(e);
			}
		});
		button.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				paintButton(e);
			}
		});
	}
	
	/**
	 * Returns the selected color.
	 * @return
	 */
	public RGB getColor() {
		return color;
	}
	
	/**
	 * Sets the new color.
	 * @param color
	 */
	public void setColor(RGB color) {
		this.color = color;
		getButton().redraw();
	}
	
	/**
	 * Opens the Color Picker.
	 * @param e
	 */
	protected void buttonSelected(SelectionEvent e) {
		ColorDialog dlg = new ColorDialog(getButton().getShell());
		dlg.setRGB(getColor());
		dlg.setText(getDialogTitle());
		RGB rgb = dlg.open();
        if (rgb != null) {
        	setColor(rgb);
        }
	}
	
	protected void paintButton(PaintEvent e) {
		Color c1 = new Color(e.display, getColor());
		Point size = getButton().getSize();
        e.gc.setBackground(c1);
        e.gc.setAlpha(255);
        e.gc.fillRoundRectangle(MARGIN, MARGIN, size.x-MARGIN*2, size.y-MARGIN*2, ARC_SIZE, ARC_SIZE);
        c1.dispose();
        
        /*
        c1 = new Color(e.display, 205,205,205);
        e.gc.setForeground(c1);
        e.gc.drawRoundRectangle(MARGIN-1, MARGIN-1, size.x-MARGIN*2+2, size.y-MARGIN*2+2, ARC_SIZE, ARC_SIZE);
        c1.dispose();
        */
	}
}
