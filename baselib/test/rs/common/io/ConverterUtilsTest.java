/**
 * 
 */
package rs.common.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

import rs.baselib.io.ConverterUtils;

/**
 * Tests the byte converter.
 * @author ralph
 *
 */
public class ConverterUtilsTest {

	/**
	 * Test method for {@link rsbudget.data.encrypt.ConverterUtils#toBytes(short)}.
	 * Test method for {@link rsbudget.data.encrypt.ConverterUtils#toShort(byte[])}.
	 */
	@Test
	public void testToBytesShort() {
		short orig = -100; short diff = 1;
		for (int i=0; i<1024; i++) {
			orig += diff;
			short converted = ConverterUtils.toShort(ConverterUtils.toBytes(orig));
			assertEquals("Conversion failed", orig, converted);
		}
	}

	/**
	 * Test method for {@link rsbudget.data.encrypt.ConverterUtils#toBytes(int)}.
	 * Test method for {@link rsbudget.data.encrypt.ConverterUtils#toInt(byte[])}.
	 */
	@Test
	public void testToBytesInt() {
		int orig = -100; int diff = 1;
		for (int i=0; i<65536; i++) {
			orig += diff;
			int converted = ConverterUtils.toInt(ConverterUtils.toBytes(orig));
			assertEquals("Conversion failed", orig, converted);
		}
	}

	/**
	 * Test method for {@link rsbudget.data.encrypt.ConverterUtils#toBytes(long)}.
	 * Test method for {@link rsbudget.data.encrypt.ConverterUtils#toLong(byte[])}.
	 */
	@Test
	public void testToBytesLong() {
		long orig = -100; long diff = 1;
		for (int i=0; i<65536; i++) {
			orig += diff;
			long converted = ConverterUtils.toLong(ConverterUtils.toBytes(orig));
			assertEquals("Conversion failed", orig, converted);
		}
	}

	/**
	 * Test method for {@link rsbudget.data.encrypt.ConverterUtils#toBytes(float)}.
	 */
	@Test
	public void testToBytesFloat() {
		float orig = -100; float diff = 0.1f;
		for (int i=0; i<65536; i++) {
			orig += diff;
			float converted = ConverterUtils.toFloat(ConverterUtils.toBytes(orig));
			assertEquals("Conversion failed", orig, converted, 0.01f);
		}
	}

	/**
	 * Test method for {@link rsbudget.data.encrypt.ConverterUtils#toBytes(double)}.
	 * Test method for {@link rsbudget.data.encrypt.ConverterUtils#toDouble(byte[])}.
	 */
	@Test
	public void testToBytesDouble() {
		double orig = -100; double diff = 0.1f;
		for (int i=0; i<65536; i++) {
			orig += diff;
			double converted = ConverterUtils.toDouble(ConverterUtils.toBytes(orig));
			assertEquals("Conversion failed", orig, converted, 0.01f);
		}
	}

	/**
	 * Test method for {@link rsbudget.data.encrypt.ConverterUtils#toBytes(char)}.
	 * Test method for {@link rsbudget.data.encrypt.ConverterUtils#toChar(byte[])}.
	 */
	@Test
	public void testToBytesChar() throws UnsupportedEncodingException {
		for (int orig=0; orig<65536; orig++) {
			if (!Character.isDefined(orig)) continue;
			char converted = ConverterUtils.toChar(ConverterUtils.toBytes((char)orig));
			assertEquals("Conversion failed", orig, converted);
		}
	}

	/**
	 * Test method for {@link rsbudget.data.encrypt.ConverterUtils#toBytes(char[])}.
	 * Test method for {@link rsbudget.data.encrypt.ConverterUtils#toCharArray(byte[])}.
	 */
	@Test
	public void testToBytesCharArray() throws UnsupportedEncodingException {
		String s = " !\"§$%&/()=?1234567890abcdefghijklmnopqrstuvwxyzäöüßABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜ<>,.-|;:_°^*+~'#``\\}][{²";
		char orig[] = s.toCharArray();
		char converted[] = ConverterUtils.toCharArray(ConverterUtils.toBytes(orig));
		assertArrayEquals("Conversion failed", orig, converted);
	}

	/**
	 * Test method for {@link rsbudget.data.encrypt.ConverterUtils#toBytes(byte)}.
	 * Test method for {@link rsbudget.data.encrypt.ConverterUtils#toByte(byte[])}.
	 */
	@Test
	public void testToBytesByte() {
		byte orig = 0; byte diff = 1;
		for (int i=0; i<256; i++) {
			orig += diff;
			byte converted = ConverterUtils.toByte(ConverterUtils.toBytes(orig));
			assertEquals("Conversion failed", orig, converted);
		}
	}

	/**
	 * Test method for {@link rsbudget.data.encrypt.ConverterUtils#toBytes(boolean)}.
	 * Test method for {@link rsbudget.data.encrypt.ConverterUtils#toBoolean(byte[])}.
	 */
	@Test
	public void testToBytesBoolean() {
		assertEquals("Conversion failed", true, ConverterUtils.toBoolean(ConverterUtils.toBytes(true)));
		assertEquals("Conversion failed", false, ConverterUtils.toBoolean(ConverterUtils.toBytes(false)));
	}

	/**
	 * Test method for {@link rsbudget.data.encrypt.ConverterUtils#toBytes(java.lang.String)}.
	 * Test method for {@link rsbudget.data.encrypt.ConverterUtils#toString(byte[])}.
	 */
	@Test
	public void testToBytesString() throws UnsupportedEncodingException {
		String orig = " !\"§$%&/()=?1234567890abcdefghijklmnopqrstuvwxyzäöüßABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜ<>,.-|;:_°^*+~'#``\\}][{²";
		String converted = ConverterUtils.toString(ConverterUtils.toBytes(orig));
		assertEquals("Conversion failed", orig, converted);
	}

	/**
	 * Test method for {@link rsbudget.data.encrypt.ConverterUtils#toBytes(java.util.Date)}.
	 * Test method for {@link rsbudget.data.encrypt.ConverterUtils#toDate(byte[])}.
	 */
	@Test
	public void testToBytesDate() throws UnsupportedEncodingException {
		Date orig = new Date();
		Date converted = ConverterUtils.toDate(ConverterUtils.toBytes(orig));
		assertEquals("Conversion failed", orig, converted);
	}

	/**
	 * Test method for {@link rsbudget.data.encrypt.ConverterUtils#toBytes(java.sql.Timestamp)}.
	 * Test method for {@link rsbudget.data.encrypt.ConverterUtils#toTimestamp(byte[])}.
	 */
	@Test
	public void testToBytesTimestamp() throws UnsupportedEncodingException {
		Timestamp orig = new Timestamp(System.currentTimeMillis());
		Timestamp converted = ConverterUtils.toTimestamp(ConverterUtils.toBytes(orig));
		assertEquals("Conversion failed", orig, converted);
	}

	/**
	 * Test method for {@link rsbudget.data.encrypt.ConverterUtils#toBytes(java.util.Locale)}.
	 * Test method for {@link rsbudget.data.encrypt.ConverterUtils#toLocale(byte[])}.
	 *
	@Test
	public void testToBytesLocale() throws UnsupportedEncodingException {
		for (Locale orig : Locale.getAvailableLocales()) {
			if (orig.toLanguageTag().equals("nn-NO")) continue; // This doesnt work :(
			Locale converted = ConverterUtils.toLocale(ConverterUtils.toBytes(orig));
			assertEquals("Conversion failed", orig, converted);
		}
	}
	*/
	
	/**
	 * Test method for {@link rsbudget.data.encrypt.ConverterUtils#toBytes(java.util.TimeZone)}.
	 * Test method for {@link rsbudget.data.encrypt.ConverterUtils#toTimeZone(byte[])}.
	 */
	@Test
	public void testToBytesTimeZone() throws UnsupportedEncodingException {
		for (String id : TimeZone.getAvailableIDs()) {
			TimeZone orig = TimeZone.getTimeZone(id);
			TimeZone converted = ConverterUtils.toTimeZone(ConverterUtils.toBytes(orig));
			assertEquals("Conversion failed", orig, converted);
		}
	}

}
