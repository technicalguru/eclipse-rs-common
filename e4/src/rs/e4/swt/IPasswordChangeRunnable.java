/**
 * 
 */
package rs.e4.swt;

/**
 * An interface providing the action to password changes.
 * @author ralph
 *
 */
public interface IPasswordChangeRunnable {

	/**
	 * Provides information whether an old password needs to be checked.
	 * @return <code>true</code> when old password must be checked
	 */
	public boolean isCheckingOldPassword();
	
	/**
	 * Checks whether the given password is the old password.
	 * @param password the password to be checked.
	 * @return <code>true</code> when password matches the old password
	 */
	public boolean checkOldPassword(char password[]);
	
	/**
	 * Sets the new password.
	 * @param password the password to be set.
	 * @return error message to be displayed or <code>null</code> if successful
	 */
	public String setNewPassword(char password[]);
}
