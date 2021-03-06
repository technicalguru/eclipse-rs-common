/**
 * 
 */
package rs.e4.util;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimBar;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * Various useful methods to monitor the execution of a {@link Job} with
 * {@link IProgressMonitor}s and {@link ProgressMonitorDialog}s.
 * 
 * @author ralph
 *
 */
public class JobUtils {

	/** A synch object */
	private static Object SYNCH_OBJECT = new Object();
	
	/** The progress provider class (null until used by {@link #configureProgressMonitor(Job, IProgressMonitor)}. */
	private static JobProgressProvider progressProvider = null;

	/**
	 * Returns the progress provider for the JobManager.
	 * <p>The method will create the provider and assign it to the {@link JobManager} when
	 * it is called the first time. Subsequent calls will just return the instance but not
	 * modify the {@link JobManager} anymore.
	 * @return the {@link JobProgressProvider}
	 */
	public static JobProgressProvider getProgressProvider() {
		if (progressProvider == null) {
			synchronized(SYNCH_OBJECT) {
				if (progressProvider == null) {
					progressProvider = new JobProgressProvider();
					Job.getJobManager().setProgressProvider(progressProvider);
				}
			}
		}
		return progressProvider;
	}
	
	/**
	 * Configures the Job so it uses the given progress monitor.
	 * @param job Job to be configured
	 * @param monitor the monitor to be assigned
	 */
	public static void configureProgressMonitor(Job job, IProgressMonitor monitor) {
		// setting the progress monitor
		IJobManager manager = Job.getJobManager();
		
		JobProgressProvider provider = getProgressProvider();
		provider.setMonitor(job, monitor);
		manager.setProgressProvider(provider);
	}
	
	
	/**
	 * Configures the Job so it opens a progress dialog while running.
	 * @param job Job to be configured
	 * @param shell the shell where the dialog should open (blocked)
	 * @return the progress monitor being used for this job
	 */
	public static void configureProgressMonitor(Job job, Shell shell) {
		final ProgressMonitorDialog dlg = new ProgressMonitorDialog(shell);
		dlg.create();
		job.addJobChangeListener(new JobChangeAdapter() {

			/**
			 * {@inheritDoc}
			 */
			@Override
			public void aboutToRun(IJobChangeEvent event) {
				dlg.getShell().getDisplay().syncExec(new Runnable() {
					public void run() {
						dlg.open();
					}
				});
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public void done(IJobChangeEvent event) {
				dlg.getShell().getDisplay().syncExec(new Runnable() {
					public void run() {
						dlg.close();
					}
				});
			}
			
		});
		configureProgressMonitor(job, new SwtSyncProgressMonitor(dlg.getProgressMonitor(), shell.getDisplay()));
	}
	
	/**
	 * Creates a progress monitor that opens a progress dialog.
	 * @param shell the shell where the dialog should open (blocked)
	 * @return the progress monitor being used for this job
	 */
	public static IProgressMonitor createProgressMonitor(Shell shell) {
		return new ProgressDialogController(shell);
	}
	
	/**
	 * Finds the first progress monitor in the trim bars of the window.
	 * @param window the window model
	 * @return the progress monitor or null if none found
	 */
	public static IProgressMonitor findProgressToolItem(MTrimmedWindow window) {
		for (MTrimBar bar : window.getTrimBars()) {
			IProgressMonitor rc = findProgressToolItem(bar);
			if (rc != null) return rc;
		}
		return null;
	}
	
	/**
	 * Finds the first progress monitor in the container.
	 * @param container the container model
	 * @return the progress monitor or null if none found
	 */
	public static IProgressMonitor findProgressToolItem(MElementContainer<?> container) {
		for (MUIElement element : container.getChildren()) {
			IProgressMonitor rc = getProgressToolItem(element);
			if (rc != null) return rc;
		}
		return null;
	}
	
	/**
	 * Returns the progress monitor associated with this element.
	 * @param element the element
	 * @return the progress monitor or null if none attached 
	 */
	public static IProgressMonitor getProgressToolItem(MUIElement element) {
		Object widget = element.getWidget();
		if (widget instanceof IProgressMonitor) {
			return (IProgressMonitor)widget;
		}
		return null;
	}
	
}
