/**
 * 
 */
package rs.e4.util;

import java.util.Comparator;

import org.eclipse.jface.viewers.ILabelProvider;

import rs.baselib.util.DefaultComparator;
import rs.baselib.util.IValueProvider;

/**
 * A sub-implementation of default comparator that
 * first retrieves the object from a viewer item
 * before sorting the items. You can use a value provider or
 * a label provider to retrieve values for it.
 * @see BeanComparator
 * @author ralph
 *
 */
public class DefaultViewerComparator implements Comparator<Object> {

	private Comparator<Object> comparator = DefaultComparator.INSTANCE;
	private IValueProvider valueProvider;
	private ILabelProvider labelProvider;
	
	/**
	 * Constructor.
	 */
	public DefaultViewerComparator(IValueProvider valueProvider) {
		this(valueProvider, null);
	}

	/**
	 * Constructor.
	 */
	public DefaultViewerComparator(IValueProvider valueProvider, Comparator<Object> comparator) {
		this.valueProvider = valueProvider;
		this.comparator = comparator != null ? comparator : DefaultComparator.INSTANCE;
	}

	/**
	 * Constructor.
	 */
	public DefaultViewerComparator(ILabelProvider labelProvider) {
		this.labelProvider = labelProvider;
	}

	/**
	 * Constructor.
	 */
	public DefaultViewerComparator(ILabelProvider labelProvider, Comparator<Object> comparator) {
		this.labelProvider = labelProvider;
		this.comparator = comparator != null ? comparator : DefaultComparator.INSTANCE;
	}
	
	/**
	 * Returns the comparator.
	 * @return the comparator
	 */
	public Comparator<Object> getComparator() {
		return comparator;
	}

	/**
	 * Returns the valueProvider.
	 * @return the valueProvider
	 */
	public IValueProvider getValueProvider() {
		return valueProvider;
	}

	/**
	 * Sets the valueProvider.
	 * @param valueProvider the valueProvider to set
	 */
	public void setValueProvider(IValueProvider valueProvider) {
		this.valueProvider = valueProvider;
	}

	/**
	 * Returns the labelProvider.
	 * @return the labelProvider
	 */
	public ILabelProvider getLabelProvider() {
		return labelProvider;
	}

	/**
	 * Sets the labelProvider.
	 * @param labelProvider the labelProvider to set
	 */
	public void setLabelProvider(ILabelProvider labelProvider) {
		this.labelProvider = labelProvider;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compare(Object o1, Object o2) {
		IValueProvider p = getValueProvider();
		ILabelProvider l = getLabelProvider();
		if (p != null) {
			o1 = p.getValue(o1);
			o2 = p.getValue(o2);
		} else if (l != null) {
			o1 = l.getText(o1);
			o2 = l.getText(o2);
		}
		return getComparator().compare(o1, o2);
	}

	
}
