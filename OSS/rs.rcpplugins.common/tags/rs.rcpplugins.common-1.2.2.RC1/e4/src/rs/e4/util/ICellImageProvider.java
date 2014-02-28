/**
 * 
 */
package rs.e4.util;

import org.eclipse.swt.graphics.Image;

/**
 * Provides images for table cells.
 * @author ralph
 *
 */
public interface ICellImageProvider {


	/**
	 * Returns the image of the cell.
	 * @param element element of the row
	 * @param cellValue value being displayed in cell
	 * @return the image or null
	 */
	public Image getImage(Object element, Object cellValue);
}
