/**
 * 
 */
package rs.e4.swt.action;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.InjectionException;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;

import rs.baselib.lang.LangUtils;

/**
 * Connects a push button to command handler.
 * @author ralph
 *
 */
public class ButtonHandler implements SelectionListener, DisposeListener {

	private static Timer timer = new Timer(true);
	
	@Inject
	private Button button;
	
	@Inject
	@Named("handler")
	private Object handler;
	
	@Inject
	private IEclipseContext context;
	
	@Inject
	private UISynchronize uiSynchronize;
	
	/** The task being scheduled for execution */
	private CanExecuteHandler task;
	
	/**
	 * Constructor.
	 */
	public ButtonHandler() {
		
	}

	@PostConstruct
	protected void postConstruct() {
		button.addSelectionListener(this);
		button.addDisposeListener(this);
		setupTimer();
	}
	
	/**
	 * Returns the {@link #button}.
	 * @return the button
	 */
	public Button getButton() {
		return button;
	}

	/**
	 * Returns the {@link #handler}.
	 * @return the handler
	 */
	public Object getHandler() {
		return handler;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void widgetDisposed(DisposeEvent e) {
		shutdownTimer();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		executeHandler(e);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		executeHandler(e);
	}

	/**
	 * Executes the handler
	 * @param e event that invokes the handler
	 */
	protected void executeHandler(SelectionEvent e) {
		try {
			ContextInjectionFactory.invoke(getHandler(), Execute.class, context);
		} catch (InjectionException e2) {
		}
	}
	
	/**
	 * Sets up the timer and starts it to check the {@link CanExecute} method of the handler. 
	 */
	protected void setupTimer() {
		this.task = new CanExecuteHandler();
		timer.schedule(this.task, 50L, 200L);
	}
	
	/**
	 * Shuts down the timer.
	 */
	protected void shutdownTimer() {
		task.cancel();
	}
	
	/**
	 * The task checking the {@link CanExecute} method of the handler. 
	 * @author ralph
	 *
	 */
	protected class CanExecuteHandler extends TimerTask {
		public void run() {
			try {
				Object rc = ContextInjectionFactory.invoke(getHandler(), CanExecute.class, context);
				setEnabled(LangUtils.getBoolean(rc));
			} catch (InjectionException e) {
				setEnabled(true);
			}
		}
	}
	
	/**
	 * Enables the button.
	 * @param b <code>true</code> when button shall be enabled or not.
	 */
	protected void setEnabled(final boolean b) {
		uiSynchronize.asyncExec(new Runnable() {
			public void run() {
				if (!getButton().isDisposed()) {
					getButton().setEnabled(b);
				}
			}
		});
	}
	
}
