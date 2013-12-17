/**
 * 
 */
package rs.e4.celledit;

import java.util.List;

/**
 * Additional methods for combo boxes.
 * @author ralph
 *
 */
public interface IComboBoxEditingSupportModel extends IEditingSupportModel {
	
	/**
	 * Returns the display for the given object
	 * @param object object
	 * @return display
	 */
	public String getDisplay(Object object);
	
	/**
	 * Returns the options sorted.
	 * The NULL option must not be returned here.
	 * @return the options.
	 */
	public List<?> getOptions(Object object);

}
