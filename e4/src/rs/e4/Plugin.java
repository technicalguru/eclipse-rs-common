/**
 * 
 */
package rs.e4;


/**
 * Some main methods for the plugin
 * @author ralph
 *
 */
public class Plugin {

	public static final String SYMBOLIC_NAME = "rs.rcpplugins.common.e4";
	public static final String CONTRIBUTOR_URI = "platform:/plugin/"+SYMBOLIC_NAME;
	
	
	/**
	 * Translate the given key.
	 * @param key key to translate
	 * @return translated value
	 */
	public static String translate(String key, Object... args) {
		return E4Utils.translate(key, CONTRIBUTOR_URI, args);
	}
}
