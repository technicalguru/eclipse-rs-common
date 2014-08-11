/**
 * 
 */
package rs.e4.util;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Provides alternating colors for each row.
 * @author ralph
 *
 */
public class AlternatingBackgroundColorProvider extends ColumnAdapter {

	private IObservableList model;
	private RGB oddColor = null;
	private RGB evenColor = null;
	private static RGB defaultOddColor = null;
	private static RGB defaultEvenColor = null;
	
	public static AlternatingBackgroundColorProvider newJFaceInstance(IObservableList model) {
		return new AlternatingBackgroundColorProvider(model, defaultOddColor(), defaultEvenColor());
	}
	
	public static RGB defaultOddColor() {
		if (defaultOddColor == null) {
			defaultOddColor = Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND).getRGB();
		}
		return defaultOddColor;
	}
	
	public static RGB defaultEvenColor() {
		return defaultEvenColor;
	}
	
	/**
	 * Constructor.
	 */
	public AlternatingBackgroundColorProvider(IObservableList model, RGB oddColor, RGB evenColor) {
		setModel(model);
		setOddColor(oddColor);
		setEvenColor(evenColor);
	}

	/**
	 * Returns the model.
	 * @return the model
	 */
	public IObservableList getModel() {
		return model;
	}

	/**
	 * Sets the model.
	 * @param model the model to set
	 */
	public void setModel(IObservableList model) {
		this.model = model;
	}

	/**
	 * Returns the oddColor.
	 * @return the oddColor
	 */
	public RGB getOddColor() {
		return oddColor;
	}

	/**
	 * Sets the oddColor.
	 * @param oddColor the oddColor to set
	 */
	public void setOddColor(RGB oddColor) {
		this.oddColor = oddColor;
	}

	/**
	 * Returns the evenColor.
	 * @return the evenColor
	 */
	public RGB getEvenColor() {
		return evenColor;
	}

	/**
	 * Sets the evenColor.
	 * @param evenColor the evenColor to set
	 */
	public void setEvenColor(RGB evenColor) {
		this.evenColor = evenColor;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RGB getBackground(Object element, Object cellValue) {
		if (model.indexOf(element) % 2 != 0) return getOddColor();
		return getEvenColor();
	}
	

}
