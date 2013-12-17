package rs.e4.splash;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;

/**
 * A monitor connected to a progress bar and a label.
 * @author ralph
 *
 */
public class ProgressBarMonitor implements IProgressMonitor {

	private ProgressBar progressBar;
	private Label progressLabel;
	private boolean canceled = false;
	
	/**
	 * 
	 * Constructor.
	 * @param progressBar the bar for progress
	 * @param progressLabel the label (can be null)
	 */
	public ProgressBarMonitor(ProgressBar progressBar, Label progressLabel) {
		setProgressBar(progressBar);
		setProgressLabel(progressLabel);
	}
	
	/**
	 * Returns the progressBar.
	 * @return the progressBar
	 */
	public ProgressBar getProgressBar() {
		return progressBar;
	}


	/**
	 * Sets the progressBar.
	 * @param progressBar the progressBar to set
	 */
	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}


	/**
	 * Returns the progressLabel.
	 * @return the progressLabel
	 */
	public Label getProgressLabel() {
		return progressLabel;
	}


	/**
	 * Sets the progressLabel.
	 * @param progressLabel the progressLabel to set
	 */
	public void setProgressLabel(Label progressLabel) {
		this.progressLabel = progressLabel;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beginTask(String name, final int totalWork) {
		setText(name);
		getProgressBar().getDisplay().syncExec(new Runnable() {
			public void run() {
				getProgressBar().setMaximum(totalWork);
				getProgressBar().setSelection(0);
			}
		});
	}

	/**
	 * Sets the text on the label.
	 * @param text
	 */
	public void setText(final String text) {
		if (getProgressLabel() != null) {
			getProgressLabel().getDisplay().syncExec(new Runnable() {
				public void run() {
					getProgressLabel().setText(text != null ? text : "");
				}
			});
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void done() {
		getProgressBar().getDisplay().syncExec(new Runnable() {
			public void run() {
				getProgressBar().setSelection(getProgressBar().getMaximum());
			}
		});
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
	public boolean isCanceled() {
		return canceled;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCanceled(boolean value) {
		this.canceled = value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTaskName(String name) {
		setText(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void subTask(String name) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void worked(final int work) {
		getProgressBar().getDisplay().syncExec(new Runnable() {
			public void run() {
				getProgressBar().setSelection(work);
			}
		});
	}
	
}