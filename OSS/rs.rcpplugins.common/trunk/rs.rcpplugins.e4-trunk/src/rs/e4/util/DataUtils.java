/**
 * 
 */
package rs.e4.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.e4.core.contexts.IEclipseContext;

import rs.data.api.IDaoFactory;
import rs.data.api.dao.IGeneralDAO;

/**
 * Some data utilities.
 * @author ralph
 *
 */
public class DataUtils {


	/**
	 * Register the factory and its DAOs at the context.
	 * @param context context to register at.
	 */
	public static void register(IDaoFactory factory, IEclipseContext context) {
		Class<?> classes[] = getInterfaceClasses(factory, IDaoFactory.class); 
		for (Class<?> clazz : classes) {
			context.set(clazz.getName(), factory);
		}
		for (IGeneralDAO<?, ?> dao : factory.getDaos()) {
			classes = getInterfaceClasses(dao, IGeneralDAO.class); 
			for (Class<?> clazz : classes) {
				context.set(clazz.getName(), dao);
			}
		}
	}

	protected static Class<?>[] getInterfaceClasses(Object object, Class<?> parentInterface) {
		List<Class<?>> rc = new ArrayList<Class<?>>();
		for (Class<?> clazz : object.getClass().getInterfaces()) {
			if (!clazz.equals(parentInterface)) {
				if (parentInterface.isAssignableFrom(clazz)) {
					rc.add(clazz);
				}
			}
		}
		Class<?> l[] = new Class<?>[rc.size()];
		rc.toArray(l);
		return l;
	}
}
