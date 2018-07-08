/**
 * 
 */
package rsbaselib.util;

import java.math.BigDecimal;

/**
 * Temporary methods until RS BaseLib is extended
 * @author ralph
 *
 */
public class RsCommonUtils {

	/**
	 * Converts the object to a BigDecimal.
	 * @param o object to be converted
	 * @return {@link BigDecimal#ZERO} if o is null or cannot be parsed, BigDecimal value of object otherwise
	 */
	public static BigDecimal getBigDecimal(Object o) {
		return getBigDecimal(o, BigDecimal.ZERO);
	}
	
	/**
	 * Converts the object to a BigDecimal.
	 * @param o object to be converted
	 * @param defaultValue the default value to return
	 * @return default if object is null or cannot be parsed, BigDecimal value of object otherwise
	 */
	public static BigDecimal getBigDecimal(Object o, BigDecimal defaultValue) {
		if (o == null) return defaultValue;
		
		if (o instanceof BigDecimal) {
			return (BigDecimal)o;
		}
		
		if (o instanceof Number) {
			return new BigDecimal(((Number)o).doubleValue());
		}
		
		try {
			return BigDecimal.valueOf(Double.parseDouble(o.toString()));
		} catch (Exception e) {
			// ignore
		}
		return defaultValue;
	}

	/**
	 * Compare two numbers.
	 * @param bd1 - number 1
	 * @param bd2 - number 2
	 * @return {@code true} when both are equal (considering null values)
	 */
	public static boolean equals(BigDecimal bd1, BigDecimal bd2) {
		if ((bd1 == null) && (bd2 == null)) return true;
		if ((bd1 != null) && (bd2 != null)) return bd1.compareTo(bd2) == 0;
		return false;
	}
}
