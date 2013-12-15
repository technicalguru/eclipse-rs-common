/**
 * 
 */
package rsbaselib.validation;

/**
 * Interface for objects performing validation.
 * @author ralph
 *
 */
public interface Validator {

	/**
	 * Adds a validation listener.
	 * @param listener listener
	 */
	public void addValidationListener(ValidationListener listener);
	
	/**
	 * Removes a validation listener.
	 * @param listener listener
	 */
	public void removeValidationListener(ValidationListener listener);
	
	/**
	 * Validates the input.
	 * @return true when validation succeeded
	 */
	public boolean validate();
}
