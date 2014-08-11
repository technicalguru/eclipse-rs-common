/**
 * 
 */
package rs.e4.jface.databinding;

import org.apache.commons.collections.Predicate;
import org.eclipse.core.databinding.conversion.IConverter;

import rs.baselib.lang.LangUtils;

/**
 * Converts strings to integers.
 * @author ralph
 *
 */
public class IntegerToStringConverter implements IConverter {

	/** A default converter */
	public static final IConverter DEFAULT_INSTANCE = new IntegerToStringConverter();
	/** A default converter for positive values only */
	public static final IConverter POSITIVE_INSTANCE = new IntegerToStringConverter("", new Predicate() {
		
		@Override
		public boolean evaluate(Object object) {
			return LangUtils.getInt(object) <= 0;
		}
	});
	
	private String nullValue = "";
	private Predicate nullPredicate = null;
	
	/**
	 * Constructor.
	 */
	public IntegerToStringConverter() {
		this("", null);
	}

	/**
	 * Constructor.
	 */
	public IntegerToStringConverter(String nullValue, Predicate predicate) {
		setNullValue(nullValue);
		setNullPredicate(predicate);
	}

	/**
	 * Returns the {@link #nullValue}.
	 * @return the nullValue
	 */
	public String getNullValue() {
		return nullValue;
	}

	/**
	 * Sets the {@link #nullValue}.
	 * @param nullValue the nullValue to set
	 */
	public void setNullValue(String nullValue) {
		this.nullValue = nullValue;
	}

	/**
	 * Returns the {@link #nullPredicate}.
	 * @return the nullPredicate
	 */
	public Predicate getNullPredicate() {
		return nullPredicate;
	}

	/**
	 * Sets the {@link #nullPredicate}.
	 * @param nullPredicate the nullPredicate to set
	 */
	public void setNullPredicate(Predicate nullPredicate) {
		this.nullPredicate = nullPredicate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object convert(Object o) {
		int i = LangUtils.getInt(o);
		if (isNullValue(i)) return getNullValue();
		return Integer.toString(i);
	}

	/**
	 * Always returns false.
	 * @param i integer to be checked
	 * @return true when this is a null value
	 */
	protected boolean isNullValue(int i) {
		if (getNullPredicate() == null) return false;
		return getNullPredicate().evaluate(i);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getFromType() {
		return Integer.TYPE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getToType() {
		return String.class;
	}

	
}
