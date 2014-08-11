/**
 * 
 */
package rs.e4.util;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * An adapter for a {@link ProgressMonitorDialog} that is not attached to a job
 * but used in a different context, e.g. with a simple runnable. 
 * <p>The dialog opens when the monitor is called with {@link #beginTask(String, int)}. 
 * The dialog is closed as soon as {@link #done()} is called.</p>
 * <p>Usage:</p>
 * <pre>
 * ProgressMonitorDialog dlg = ...
 * ProgressDialogController monitor = new ProgressDialogController(dlg);
 * monitor.beginTask("starting", 10);
 * ...
 * monitor.done();
 * </pre>
 * <p>Use the {@link JobUtils#configureProgressMonitor(org.eclipse.core.runtime.jobs.Job, Shell)} when a Job context is given.</p>
 * @see SwtSyncProgressMonitor
 * @see rs.e4.E4Utils
 * 
 * @author ralph
 *
 */
public class ProgressDialogController implements IProgressMonitor {

	/** The dialog to synchronize */
	private ProgressMonitorDialog dlg;
	/** The display */
	private Display display;
	/** Holds the result of an {@link IProgressMonitor#isCanceled()} call */
	private volatile boolean canceled = false;
	
	/**
	 * Constructor.
	 * @param shell the parent shell the dialog shall be created for.
	 */
	public ProgressDialogController(Shell shell) {
		this.dlg = new ProgressMonitorDialog(shell);
		this.display = shell.getDisplay();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beginTask(final String name, final int totalWork) {
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				getDialog().open();
				getDialogMonitor().beginTask(name, totalWork);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void done() {
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				getDialogMonitor().done();
				getDialog().close();
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void internalWorked(final double work) {
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				getDialogMonitor().internalWorked(work);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCanceled() {
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				canceled = getDialogMonitor().isCanceled();
			}
		});
		return canceled;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCanceled(final boolean value) {
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				getDialogMonitor().setCanceled(value);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTaskName(final String name) {
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				getDialogMonitor().setTaskName(name);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void subTask(final String name) {
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				getDialogMonitor().subTask(name);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void worked(final int work) {
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				getDialogMonitor().worked(work);
			}
		});
	}

	/**
	 * Returns the dialog.
	 * @return the progress dialog
	 */
	public ProgressMonitorDialog getDialog() {
		return dlg;
	}
	
	/**
	 * Returns the display of the dialog.
	 * @return the display
	 */
	public Display getDisplay() {
		return display;
	}
	
	/**
	 * Returns the monitor object of the dialog.
	 * @return the monitor
	 */
	protected IProgressMonitor getDialogMonitor() {
		return getDialog().getProgressMonitor();
	}
}
