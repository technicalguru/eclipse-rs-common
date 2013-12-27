/**
 * 
 */
package rs.e4.util;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Display;

/**
 * An adapter for a {@link IProgressMonitor} that must be called within the UI thread only.
 * <p>Usage:</p>
 * <pre>
 * IProgressMonitor someMonitor = ...
 * SwtSyncProgressMonitor adapter = new SwtSyncProgressMonitor(someMonitor, display);
 * E4Utils.configureJob(job, adapter);
 * </pre>
 * @author ralph
 *
 */
public class SwtSyncProgressMonitor implements IProgressMonitor {

	/** The monitor to synchronize */
	private IProgressMonitor monitor;
	/** The display to be synchonized with */
	private Display display;
	/** Holds the result of an {@link IProgressMonitor#isCanceled()} call */
	private volatile boolean canceled = false;
	
	/**
	 * Constructor.
	 */
	public SwtSyncProgressMonitor(IProgressMonitor monitor, Display display) {
		this.monitor = monitor;
		this.display = display;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beginTask(final String name, final int totalWork) {
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				monitor.beginTask(name, totalWork);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void done() {
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				monitor.done();
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void internalWorked(final double work) {
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				monitor.internalWorked(work);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCanceled() {
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				canceled = monitor.isCanceled();
			}
		});
		return canceled;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCanceled(final boolean value) {
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				monitor.setCanceled(value);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTaskName(final String name) {
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				monitor.setTaskName(name);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void subTask(final String name) {
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				monitor.subTask(name);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void worked(final int work) {
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				monitor.worked(work);
			}
		});
	}

}
