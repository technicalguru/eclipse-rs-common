/**
 * 
 */
package rs.e4.util;

import org.eclipse.swt.graphics.RGB;

/**
 * Provides colors for table cells.
 * @author ralph
 *
 */
public interface ICellColorProvider {

	/**
	 * Returns the foreground color of the cell.
	 * @param element element of the row
	 * @param cellValue value being displayed in cell
	 * @return the foreground color or null
	 */
	public RGB getForeground(Object element, Object cellValue);

	/**
	 * Returns the background color of the cell.
	 * @param element element of the row
	 * @param cellValue value being displayed in cell
	 * @return the background color or null
	 */
	public RGB getBackground(Object element, Object cellValue);
	
}
