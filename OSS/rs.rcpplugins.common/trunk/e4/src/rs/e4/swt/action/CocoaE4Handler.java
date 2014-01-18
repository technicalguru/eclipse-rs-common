/**
 * 
 */
package rs.e4.swt.action;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.slf4j.LoggerFactory;

/**
 * Used to register Cocoa specific handlers.
 * @author ralph
 *
 */
public class CocoaE4Handler extends Action implements Listener, IAction {

	private Class<?> handlerClass;
	private  IEclipseContext context;
	private Object handler;

	/**
	 * Constructor.
	 */
	public CocoaE4Handler(Class<?> handlerClass, String label, IEclipseContext context) {
		this.handlerClass = handlerClass;
		setText(label);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleEvent(Event event) {
		run();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		try {
			if (handler == null) {
				handler = ContextInjectionFactory.make(handlerClass, context);
			}
			if (handler != null) {
				ContextInjectionFactory.invoke(handler, Execute.class, context);
			}
		} catch (Throwable t) {
			LoggerFactory.getLogger(getClass()).error("Cannot invoke handler \""+handlerClass.getName()+"\"", t);
		}
	}


}
