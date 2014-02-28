/**
 * 
 */
package rs.e4.util;

import org.eclipse.swt.graphics.Font;

/**
 * Provides fonts for table cells.
 * @author ralph
 *
 */
public interface ICellFontProvider {


	/**
	 * Returns the font of the cell.
	 * @param element element of the row
	 * @param cellValue value being displayed in cell
	 * @return the font or null
	 */
	public Font getFont(Object element, Object cellValue);
}
