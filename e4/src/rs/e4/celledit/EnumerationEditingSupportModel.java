/**
 * 
 */
package rs.e4.celledit;

import java.util.List;

import rs.baselib.util.CommonUtils;

/**
 * @author ralph
 *
 */
public class EnumerationEditingSupportModel extends AbstractComboBoxEditingSupportModel {
	
	private Class<? extends Enum<?>> clazz;
	
	/**
	 * Constructor.
	 */
	public EnumerationEditingSupportModel(Class<? extends Enum<?>> clazz, String beanProperty) {
		super(beanProperty);
		this.clazz = clazz;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public String getDisplay(Object object) {
		return CommonUtils.getDisplay((Enum<?>)object);
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected List<?> createOptions(Object object) {
		return CommonUtils.getOptionList(clazz);
	}

	
}
