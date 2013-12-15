/**
 * 
 */
package rs.e4.jface.databinding;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.IDisposeListener;
import org.eclipse.core.databinding.observable.IStaleListener;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.property.Properties;

import rs.baselib.lang.LangUtils;
import rs.data.api.bo.IGeneralBO;
import rs.data.api.dao.IGeneralDAO;
import rs.data.event.DaoEvent;
import rs.data.event.IDaoListener;

/**
 * Listens to a DAO and updates its list accordingly.
 * @author ralph
 *
 */
public class ObservableDaoList<T extends IGeneralBO<?>> implements IObservableList {

	private Class<T> typeClass;
	/** The DAO we listen for */
	private IGeneralDAO<?, T> dao;
	/** Sorting the result */
	private Comparator<T> sorter;
	/** The list being observed */
	private IObservableList observedList;
	/** Listener for DAOs. */
	private IDaoListener listener = new IDaoListener() {
		
		@Override
		public void handleDaoEvent(DaoEvent event) {
			ObservableDaoList.this.handleDaoEvent(event);
		}
	};
	
	/**
	 * Constructor.
	 * @param dao the DAO to listen for.
	 */
	@SuppressWarnings("unchecked")
	public ObservableDaoList(IGeneralDAO<?, T> dao) {
		typeClass = (Class<T>)LangUtils.getTypeArguments(ObservableDaoList.class, getClass()).get(0);
		setDao(dao);
	}

	/**
	 * Returns the typed class.
	 * @return
	 */
	public Class<T> getTypeClass() {
		return typeClass;
	}
	
	/**
	 * Creates the list to be observed.
	 */
	protected List<?> createList() {
		IGeneralDAO<?, T> dao = getDao();
		dao.getFactory().begin();
		List<T> rc = dao.findDefaultAll();
		dao.getFactory().commit();
		
		if (getSorter() != null) Collections.sort(rc, getSorter());
		return rc;
	}

	/**
	 * Returns the dao.
	 * @return the dao
	 */
	protected IGeneralDAO<?, T> getDao() {
		return dao;
	}

	/**
	 * Sets the dao.
	 * @param dao the dao to set
	 */
	protected void setDao(IGeneralDAO<?, T> dao) {
		this.dao = dao;
		this.dao.addDaoListener(listener);
		this.observedList = Properties.selfList(getTypeClass()).observe(createList());
	}

	/**
	 * Handles the DAO event.
	 * @param event
	 */
	protected void handleDaoEvent(DaoEvent event) {
		switch (event.getType()) {
		case OBJECT_CREATED:
			add(event.getObject());
			break;
		case OBJECT_UPDATED:
			int idx = indexOf(event.getObject());
			if (idx >= 0) set(idx, event.getObject());
			break;
		case OBJECT_DELETED:
			remove(event.getObject());
			break;
		case ALL_DEFAULT_DELETED:
			removeDefaultAll();
			break;
		case ALL_DELETED:
			removeAll();
			break;
		}
	}
	
	/**
	 * Removes all objects from the list.
	 */
	public void removeAll() {
		clear();
	}
	
	/**
	 * Removes all default objects from the list.
	 * Descendants must override this method.
	 */
	public void removeDefaultAll() {
		removeAll();
	}
	
	/**
	 * Returns the sorter.
	 * @return the sorter
	 */
	public Comparator<T> getSorter() {
		return sorter;
	}

	/**
	 * Sets the sorter.
	 * @param sorter the sorter to set
	 */
	public void setSorter(Comparator<T> sorter) {
		this.sorter = sorter;
	}

	
	/**
	 * @return
	 * @see org.eclipse.core.databinding.observable.IObservable#getRealm()
	 */
	public Realm getRealm() {
		return observedList.getRealm();
	}

	/**
	 * @param listener
	 * @see org.eclipse.core.databinding.observable.list.IObservableList#addListChangeListener(org.eclipse.core.databinding.observable.list.IListChangeListener)
	 */
	public void addListChangeListener(IListChangeListener listener) {
		observedList.addListChangeListener(listener);
	}

	/**
	 * @param listener
	 * @see org.eclipse.core.databinding.observable.list.IObservableList#removeListChangeListener(org.eclipse.core.databinding.observable.list.IListChangeListener)
	 */
	public void removeListChangeListener(IListChangeListener listener) {
		observedList.removeListChangeListener(listener);
	}

	/**
	 * @return
	 * @see org.eclipse.core.databinding.observable.list.IObservableList#size()
	 */
	public int size() {
		return observedList.size();
	}

	/**
	 * @return
	 * @see org.eclipse.core.databinding.observable.list.IObservableList#isEmpty()
	 */
	public boolean isEmpty() {
		return observedList.isEmpty();
	}

	/**
	 * @param listener
	 * @see org.eclipse.core.databinding.observable.IObservable#addChangeListener(org.eclipse.core.databinding.observable.IChangeListener)
	 */
	public void addChangeListener(IChangeListener listener) {
		observedList.addChangeListener(listener);
	}

	/**
	 * @param o
	 * @return
	 * @see org.eclipse.core.databinding.observable.list.IObservableList#contains(java.lang.Object)
	 */
	public boolean contains(Object o) {
		return observedList.contains(o);
	}

	/**
	 * @return
	 * @see org.eclipse.core.databinding.observable.list.IObservableList#iterator()
	 */
	@SuppressWarnings("rawtypes")
	public Iterator iterator() {
		return observedList.iterator();
	}

	/**
	 * @return
	 * @see org.eclipse.core.databinding.observable.list.IObservableList#toArray()
	 */
	public Object[] toArray() {
		return observedList.toArray();
	}

	/**
	 * @param a
	 * @return
	 * @see org.eclipse.core.databinding.observable.list.IObservableList#toArray(java.lang.Object[])
	 */
	public Object[] toArray(Object[] a) {
		return observedList.toArray(a);
	}

	/**
	 * @param o
	 * @return
	 * @see org.eclipse.core.databinding.observable.list.IObservableList#add(java.lang.Object)
	 */
	public boolean add(Object o) {
		return observedList.add(o);
	}

	/**
	 * @param o
	 * @return
	 * @see org.eclipse.core.databinding.observable.list.IObservableList#remove(java.lang.Object)
	 */
	public boolean remove(Object o) {
		return observedList.remove(o);
	}

	/**
	 * @param c
	 * @return
	 * @see org.eclipse.core.databinding.observable.list.IObservableList#containsAll(java.util.Collection)
	 */
	@SuppressWarnings("rawtypes")
	public boolean containsAll(Collection c) {
		return observedList.containsAll(c);
	}

	/**
	 * @param listener
	 * @see org.eclipse.core.databinding.observable.IObservable#removeChangeListener(org.eclipse.core.databinding.observable.IChangeListener)
	 */
	public void removeChangeListener(IChangeListener listener) {
		observedList.removeChangeListener(listener);
	}

	/**
	 * @param c
	 * @return
	 * @see org.eclipse.core.databinding.observable.list.IObservableList#addAll(java.util.Collection)
	 */
	@SuppressWarnings("rawtypes")
	public boolean addAll(Collection c) {
		return observedList.addAll(c);
	}

	/**
	 * @param index
	 * @param c
	 * @return
	 * @see org.eclipse.core.databinding.observable.list.IObservableList#addAll(int, java.util.Collection)
	 */
	@SuppressWarnings("rawtypes")
	public boolean addAll(int index, Collection c) {
		return observedList.addAll(index, c);
	}

	/**
	 * @param c
	 * @return
	 * @see org.eclipse.core.databinding.observable.list.IObservableList#removeAll(java.util.Collection)
	 */
	@SuppressWarnings("rawtypes")
	public boolean removeAll(Collection c) {
		return observedList.removeAll(c);
	}

	/**
	 * @param c
	 * @return
	 * @see org.eclipse.core.databinding.observable.list.IObservableList#retainAll(java.util.Collection)
	 */
	@SuppressWarnings("rawtypes")
	public boolean retainAll(Collection c) {
		return observedList.retainAll(c);
	}

	/**
	 * @param listener
	 * @see org.eclipse.core.databinding.observable.IObservable#addStaleListener(org.eclipse.core.databinding.observable.IStaleListener)
	 */
	public void addStaleListener(IStaleListener listener) {
		observedList.addStaleListener(listener);
	}

	/**
	 * @param o
	 * @return
	 * @see org.eclipse.core.databinding.observable.list.IObservableList#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		return observedList.equals(o);
	}

	/**
	 * @return
	 * @see org.eclipse.core.databinding.observable.list.IObservableList#hashCode()
	 */
	public int hashCode() {
		return observedList.hashCode();
	}

	/**
	 * @param index
	 * @return
	 * @see org.eclipse.core.databinding.observable.list.IObservableList#get(int)
	 */
	public Object get(int index) {
		return observedList.get(index);
	}

	/**
	 * @param index
	 * @param element
	 * @return
	 * @see org.eclipse.core.databinding.observable.list.IObservableList#set(int, java.lang.Object)
	 */
	public Object set(int index, Object element) {
		return observedList.set(index, element);
	}

	/**
	 * @param listener
	 * @see org.eclipse.core.databinding.observable.IObservable#removeStaleListener(org.eclipse.core.databinding.observable.IStaleListener)
	 */
	public void removeStaleListener(IStaleListener listener) {
		observedList.removeStaleListener(listener);
	}

	/**
	 * @param oldIndex
	 * @param newIndex
	 * @return
	 * @see org.eclipse.core.databinding.observable.list.IObservableList#move(int, int)
	 */
	public Object move(int oldIndex, int newIndex) {
		return observedList.move(oldIndex, newIndex);
	}

	/**
	 * @return
	 * @see org.eclipse.core.databinding.observable.IObservable#isStale()
	 */
	public boolean isStale() {
		return observedList.isStale();
	}

	/**
	 * @param listener
	 * @see org.eclipse.core.databinding.observable.IObservable#addDisposeListener(org.eclipse.core.databinding.observable.IDisposeListener)
	 */
	public void addDisposeListener(IDisposeListener listener) {
		observedList.addDisposeListener(listener);
	}

	/**
	 * @param index
	 * @return
	 * @see org.eclipse.core.databinding.observable.list.IObservableList#remove(int)
	 */
	public Object remove(int index) {
		return observedList.remove(index);
	}

	/**
	 * @param o
	 * @return
	 * @see org.eclipse.core.databinding.observable.list.IObservableList#indexOf(java.lang.Object)
	 */
	public int indexOf(Object o) {
		return observedList.indexOf(o);
	}

	/**
	 * @param o
	 * @return
	 * @see org.eclipse.core.databinding.observable.list.IObservableList#lastIndexOf(java.lang.Object)
	 */
	public int lastIndexOf(Object o) {
		return observedList.lastIndexOf(o);
	}

	/**
	 * @param listener
	 * @see org.eclipse.core.databinding.observable.IObservable#removeDisposeListener(org.eclipse.core.databinding.observable.IDisposeListener)
	 */
	public void removeDisposeListener(IDisposeListener listener) {
		observedList.removeDisposeListener(listener);
	}

	/**
	 * @return
	 * @see org.eclipse.core.databinding.observable.list.IObservableList#listIterator()
	 */
	@SuppressWarnings("rawtypes")
	public ListIterator listIterator() {
		return observedList.listIterator();
	}

	/**
	 * @param index
	 * @return
	 * @see org.eclipse.core.databinding.observable.list.IObservableList#listIterator(int)
	 */
	@SuppressWarnings("rawtypes")
	public ListIterator listIterator(int index) {
		return observedList.listIterator(index);
	}

	/**
	 * @param fromIndex
	 * @param toIndex
	 * @return
	 * @see org.eclipse.core.databinding.observable.list.IObservableList#subList(int, int)
	 */
	@SuppressWarnings("rawtypes")
	public List subList(int fromIndex, int toIndex) {
		return observedList.subList(fromIndex, toIndex);
	}

	/**
	 * @return
	 * @see org.eclipse.core.databinding.observable.list.IObservableList#getElementType()
	 */
	public Object getElementType() {
		return observedList.getElementType();
	}

	/**
	 * @return
	 * @see org.eclipse.core.databinding.observable.IObservable#isDisposed()
	 */
	public boolean isDisposed() {
		return observedList.isDisposed();
	}

	/**
	 * 
	 * @see java.util.List#clear()
	 */
	public void clear() {
		observedList.clear();
	}

	/**
	 * @param index
	 * @param element
	 * @see java.util.List#add(int, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public void add(int index, Object element) {
		observedList.add(index, element);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		getDao().removeDaoListener(listener);
		observedList.dispose();
	}

	
}
