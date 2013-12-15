/**
 * 
 */
package rs.e4.jface.databinding;

import java.text.NumberFormat;
import java.util.Locale;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Text;

import rs.baselib.util.CommonUtils;

/**
 * Validates float values and displays an error decorator.
 * @author ralph
 *
 */
public class FloatValidator implements ModifyListener {
	private NumberFormat format = NumberFormat.getNumberInstance(Locale.getDefault());
	private ControlDecoration dec = null;
	private Text text;
	
	/**
	 * Constructor with error decorator at top-right position.
	 * @param text the text control
	 * @param errorMessage the error message t be shown
	 */
	public FloatValidator(Text text, String errorMessage) {
		this(text, null, errorMessage, SWT.TOP|SWT.RIGHT);
	}
	
	/**
	 * Constructor with error decorator.
	 * @param text the text control
	 * @param errorMessage the error message t be shown
	 * @param style the location of the decorator
	 */
	public FloatValidator(Text text, String errorMessage, int style) {
		this(text, null, errorMessage, style);
	}
	
	/**
	 * Constructor.
	 * @param text the text control
	 * @param decoratorImage the decorator image to be shown
	 * @param errorMessage the error message t be shown
	 * @param style the location of the decorator
	 */
	public FloatValidator(Text text, Image decoratorImage, String errorMessage, int style) {
		this.text = text;
		if (decoratorImage == null) decoratorImage = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
		dec = new ControlDecoration(text, style);
		dec.setImage(decoratorImage);
		dec.setDescriptionText(errorMessage);
		dec.hide();
	}

	@Override
	public void modifyText(ModifyEvent event) {
		String s = text.getText();
		if (CommonUtils.isEmpty(s)) dec.hide();
		else try {
			format.parse(s);
			dec.hide();
		} catch (Exception e) {
			dec.show();
		}
	}
}
