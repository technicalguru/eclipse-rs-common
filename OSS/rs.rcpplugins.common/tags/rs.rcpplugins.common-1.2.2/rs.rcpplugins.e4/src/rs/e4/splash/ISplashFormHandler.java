/**
 * 
 */
package rs.e4.splash;

import org.eclipse.ui.progress.IJobRunnable;

/**
 * Handles authentification.
 * @author ralph
 *
 */
public interface ISplashFormHandler extends IJobRunnable {

	/** property for login name */
	public static final String PROPERTY_LOGIN = "login";
	/** property for password */
	public static final String PROPERTY_PASSWORD = "password";
	/** property for server */
	public static final String PROPERTY_SERVER = "server";
	/** property for role */
	public static final String PROPERTY_ROLE = "role";
	
	/**
	 * Sets the property being retrieved from the splash form.
	 * @param property
	 * @param value
	 */
	public void setSplashProperty(String property, Object value);
	
}
