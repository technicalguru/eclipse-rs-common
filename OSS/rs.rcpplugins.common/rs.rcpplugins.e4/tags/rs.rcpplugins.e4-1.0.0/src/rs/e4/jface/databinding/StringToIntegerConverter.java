/**
 * 
 */
package rs.e4.jface.databinding;

import org.eclipse.core.databinding.conversion.IConverter;

import rs.baselib.lang.LangUtils;
import rs.baselib.util.CommonUtils;

/**
 * Converts strings to integers.
 * @author ralph
 *
 */
public class StringToIntegerConverter implements IConverter {

	private int nullValue = -1;
	
	/**
	 * Constructor.
	 */
	public StringToIntegerConverter() {
		this(-1);
	}

	/**
	 * Constructor.
	 */
	public StringToIntegerConverter(int nullValue) {
		setNullValue(nullValue);
	}

	/**
	 * Returns the {@link #nullValue}.
	 * @return the nullValue
	 */
	public int getNullValue() {
		return nullValue;
	}

	/**
	 * Sets the {@link #nullValue}.
	 * @param nullValue the nullValue to set
	 */
	public void setNullValue(int nullValue) {
		this.nullValue = nullValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object convert(Object o) {
		String s = LangUtils.getString(o);
		if (CommonUtils.isEmpty(s)) return getNullValue();
		return LangUtils.getInt(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getFromType() {
		return String.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getToType() {
		return Integer.TYPE;
	}

	
}
