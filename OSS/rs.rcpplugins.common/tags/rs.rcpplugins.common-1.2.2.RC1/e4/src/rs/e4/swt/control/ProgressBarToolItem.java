/**
 * 
 */
package rs.e4.swt.control;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ProgressBar;

/**
 * A progress bar to be used in tool bars.
 * @author ralph
 *
 */
public class ProgressBarToolItem implements IProgressMonitor {

	private ProgressBar progressBar;
	private String taskName;

	@Inject UISynchronize sync;

	@PostConstruct
	public void createControls(Composite parent) {
		progressBar = new ProgressBar(parent, SWT.SMOOTH);
		progressBar.setBounds(100, 10, 200, 20);
		progressBar.setVisible(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void worked(final int work) {
		sync.syncExec(new Runnable() {
			@Override
			public void run() {
				progressBar.setSelection(progressBar.getSelection() + work);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void subTask(final String name) {
		sync.syncExec(new Runnable() {
			@Override
			public void run() {
				progressBar.setToolTipText(taskName+": "+name);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTaskName(final String name) {
		taskName = name;
		sync.syncExec(new Runnable() {
			@Override
			public void run() {
				progressBar.setToolTipText(taskName);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCanceled(boolean value) {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCanceled() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void internalWorked(double work) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void done() {
		sync.syncExec(new Runnable() {
			@Override
			public void run() {
				progressBar.setVisible(false);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beginTask(final String name, final int totalWork) {
		taskName = name;
		sync.syncExec(new Runnable() {
			@Override
			public void run() {
				progressBar.setMaximum(totalWork);
				progressBar.setToolTipText(taskName);
				progressBar.setVisible(true);
			}
		});
	}
} 