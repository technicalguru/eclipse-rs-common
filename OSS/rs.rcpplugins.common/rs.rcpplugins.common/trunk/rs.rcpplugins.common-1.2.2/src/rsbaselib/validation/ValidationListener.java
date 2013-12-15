/**
 * 
 */
package rsbaselib.validation;

/**
 * Listens to validations.
 * @author ralph
 *
 */
public interface ValidationListener {

	/**
	 * Informs about failed validation with error message.
	 * @param errorMessage message to be displayed
	 */
	public void validationFailed(String errorMessage);
	/**
	 * Informs about successful validation.
	 */
	public void validationSuceeded();
	
}
