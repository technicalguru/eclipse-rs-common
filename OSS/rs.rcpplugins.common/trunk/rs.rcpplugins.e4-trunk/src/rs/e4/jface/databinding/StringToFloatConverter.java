/**
 * 
 */
package rs.e4.jface.databinding;

import java.text.NumberFormat;
import java.util.Locale;

import org.eclipse.core.databinding.conversion.IConverter;

import rs.baselib.lang.LangUtils;
import rs.baselib.util.CommonUtils;

/**
 * Converts strings to integers.
 * @author ralph
 *
 */
public class StringToFloatConverter implements IConverter {

	private float nullValue = 0;
	private NumberFormat format = NumberFormat.getNumberInstance(Locale.getDefault());
	
	/**
	 * Constructor.
	 */
	public StringToFloatConverter() {
		this(-1);
	}

	/**
	 * Constructor.
	 */
	public StringToFloatConverter(float nullValue) {
		setNullValue(nullValue);
	}

	/**
	 * Returns the {@link #nullValue}.
	 * @return the nullValue
	 */
	public float getNullValue() {
		return nullValue;
	}

	/**
	 * Sets the {@link #nullValue}.
	 * @param nullValue the nullValue to set
	 */
	public void setNullValue(float nullValue) {
		this.nullValue = nullValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object convert(Object o) {
		String s = LangUtils.getString(o);
		if (CommonUtils.isEmpty(s)) return getNullValue();
		try {
			return format.parse(s);
		} catch (Exception e) {}
		return 0f;
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
		return Float.TYPE;
	}

	
}
