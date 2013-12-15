/**
 * 
 */
package rs.e4.celledit;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rs.baselib.util.DefaultComparator;

/**
 * Abstract combo box editing support model.
 * @author ralph
 *
 */
public abstract class AbstractComboBoxEditingSupportModel extends BeanEditingSupportModel implements IComboBoxEditingSupportModel {

	private Comparator<Object> sorter = new DefaultComparator();
	
	/**
	 * Constructor.
	 */
	public AbstractComboBoxEditingSupportModel(String beanProperty) {
		super(beanProperty);
	}

	/**
	 * Sort the options.
	 * This method relies on {@link #getSorter()}. For more special
	 * sortings this method can be overwritten, too.
	 * @param options options to be sorted
	 * @return sorted options.
	 */
	protected List<?> sort(List<?> options) {
		Comparator<Object> c = getSorter();
		if (c != null) Collections.sort(options, c);
		return options;
	}
	
	/**
	 * Sets the sorter for the lists.
	 * @param sorter
	 */
	public void setSorter(Comparator<Object> sorter) {
		this.sorter = sorter;
	}
	
	/**
	 * Returns the comparator for the option list.
	 * The method can return null for no sorting. Default is comparison according {@link DisplayProvider#getDisplay()}.
	 * @return comparator.
	 */
	public Comparator<Object> getSorter() {
		return sorter;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public List<?> getOptions(Object element) {
		return sort(createOptions(element));
	}

	/**
	 * Create the options.
	 * @return options
	 */
	protected abstract List<?> createOptions(Object element);
}
