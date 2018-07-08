/**
 * 
 */
package rs.e4.jface.databinding;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.eclipse.core.databinding.conversion.IConverter;

import rs.baselib.lang.LangUtils;
import rs.baselib.util.CommonUtils;

/**
 * Converts strings to big decimals.
 * @author ralph
 *
 */
public class StringToBigDecimalConverter implements IConverter {

	private BigDecimal nullValue = BigDecimal.ZERO;
	private DecimalFormat format;
	
	/**
	 * Constructor.
	 */
	public StringToBigDecimalConverter() {
		this(BigDecimal.ZERO);
	}

	/**
	 * Constructor.
	 */
	public StringToBigDecimalConverter(BigDecimal nullValue) {
		setNullValue(nullValue);
		format = new DecimalFormat();
		format.setParseBigDecimal(true);
	}

	/**
	 * Returns the {@link #nullValue}.
	 * @return the nullValue
	 */
	public BigDecimal getNullValue() {
		return nullValue;
	}

	/**
	 * Sets the {@link #nullValue}.
	 * @param nullValue the nullValue to set
	 */
	public void setNullValue(BigDecimal nullValue) {
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
		return BigDecimal.class;
	}

	
}
