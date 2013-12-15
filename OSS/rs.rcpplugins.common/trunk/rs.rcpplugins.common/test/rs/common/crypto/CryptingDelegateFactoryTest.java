/**
 * 
 */
package rs.common.crypto;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import rs.baselib.crypto.DefaultCryptingDelegateFactory;
import rs.baselib.crypto.EncryptionUtils;
import rs.baselib.crypto.ICryptingDelegate;
import rs.baselib.crypto.ICryptingDelegateFactory;

/**
 * @author ralph
 *
 */
public class CryptingDelegateFactoryTest {

	/**
	 * Test method.
	 */
	@Test
	public void testBytes() throws Exception {
		for (int i=0; i<1024; i++) {
			testBytes(EncryptionUtils.generateRandomBytes(System.currentTimeMillis(), 1024));
		}
	}
	
	/**
	 * Test method.
	 */
	@Test
	public void testLengths() throws Exception {
		for (int i=0; i<200; i++) {
			int l = testBytes(EncryptionUtils.generateRandomBytes(System.currentTimeMillis(), i));
			System.out.println("length="+i+" encrypted_length="+l);
		}
	}
	
	/**
	 * Test encryption/decryption given bytes.
	 * @param orig
	 * @throws Exception
	 */
	protected int testBytes(byte orig[]) throws Exception {
		ICryptingDelegateFactory factory = DefaultCryptingDelegateFactory.getInstance();
		assertNotNull("No factory", factory);
		ICryptingDelegate delegate = factory.getCryptingDelegate();
		byte encrypted[] = delegate.encrypt(orig);
		byte tested[] = delegate.decrypt(encrypted);
		assertArrayEquals(orig, tested);
		return encrypted.length;
	}
}
