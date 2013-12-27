/**
 * 
 */
package rs.e4.util;

import java.util.WeakHashMap;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.ProgressProvider;

/**
 * A {@link ProgressProvider} that assigns individual {@link Job}s with
 * {@link IProgressMonitor}s.
 * <p>The class should not be used directly but through {@link JobUtils}:</p>
 * <pre>
 * Job myJob = ...
 * IProgressMonitor myMonitor = ...
 * JobUtils.configureProgressMonitor(myJob, ImyMonitor);
 * </pre>
 * @author ralph
 *
 */
public class JobProgressProvider extends ProgressProvider {

	/** The monitor for all jobs not assigned a specific monitor */
	private IProgressMonitor defaultMonitor = null;
	/** Monitors for jobs */
	private WeakHashMap<Job, IProgressMonitor> monitors;
	
	/**
	 * Constructor.
	 */
	public JobProgressProvider() {
		monitors = new WeakHashMap<>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IProgressMonitor createMonitor(Job job) {
		return monitors.get(job);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IProgressMonitor getDefaultMonitor() {
		if (defaultMonitor != null) return defaultMonitor;
		return super.getDefaultMonitor();
	}

	/**
	 * Sets the defaultMonitor.
	 * @param defaultMonitor the defaultMonitor to set
	 */
	public void setDefaultMonitor(IProgressMonitor defaultMonitor) {
		this.defaultMonitor = defaultMonitor;
	}

	/**
	 * Registers the monitor for the given job.
	 * @param job the job
	 * @param monitor the monitor
	 */
	public void setMonitor(Job job, IProgressMonitor monitor) {
		monitors.put(job, monitor);
	}
}
