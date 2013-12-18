/**
 * 
 */
package rs.e4.log;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.equinox.log.ExtendedLogReaderService;
import org.eclipse.equinox.log.LogFilter;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;
import org.osgi.service.log.LogReaderService;
import org.osgi.service.log.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bridges the OSGI Logging Service to SLF4J.
 * @author ralph
 *
 */
public class Slf4jBridgeService implements LogReaderService, ExtendedLogReaderService {

	private Map<LogReaderService, Slf4jLogListener> logReaderMap;
	private Map<ExtendedLogReaderService, Slf4jLogListener> extendedLogReaderMap;

	/**
	 * Constructor.
	 */
	public Slf4jBridgeService() {
		logReaderMap = new HashMap<LogReaderService, Slf4jLogListener>();
		extendedLogReaderMap = new HashMap<ExtendedLogReaderService, Slf4jLogListener>();
		System.out.println("Service started");
	}

	public void addLogReaderService(LogReaderService aLogReaderService) {
		Slf4jLogListener listener = new Slf4jLogListener();
		logReaderMap.put(aLogReaderService, listener);
		aLogReaderService.addLogListener(listener);
		System.out.println("Service added");
	}

	public void removeLogReaderService(LogReaderService aLogReaderService) {
		Slf4jLogListener listener = logReaderMap.remove(aLogReaderService);
		if (listener != null) aLogReaderService.removeLogListener(listener);
		System.out.println("Service removed");
	}

	public void addExtendedLogReaderService(ExtendedLogReaderService aLogReaderService) {
		Slf4jLogListener listener = new Slf4jLogListener();
		extendedLogReaderMap.put(aLogReaderService, listener);
		aLogReaderService.addLogListener(listener);
		System.out.println("XService added");
	}

	public void removeExtendedLogReaderService(ExtendedLogReaderService aLogReaderService) {
		Slf4jLogListener listener = extendedLogReaderMap.remove(aLogReaderService);
		if (listener != null) aLogReaderService.removeLogListener(listener);
		System.out.println("XService removed");
	}

	private class Slf4jLogListener implements LogListener {
		public void logged(LogEntry entry) {
			Slf4jBridgeService.this.logged(entry);
		}
	}

	protected void logged(LogEntry entry) {
		Logger log = null;
		Bundle bundle = entry.getBundle();
		if (bundle == null) {
			ServiceReference<?> ref = entry.getServiceReference();
			if (ref != null) {
				bundle = ref.getBundle();
			}
		}
		if (bundle != null) {
			log = LoggerFactory.getLogger(bundle.getSymbolicName());
		}

		Throwable t = entry.getException();
		String msg  = entry.getMessage();
		System.out.println("Logging \""+msg+"\"");
		if (t == null) {
			switch (entry.getLevel()) {
			case LogService.LOG_ERROR:   log.error(msg); break;
			case LogService.LOG_WARNING: log.warn(msg); break;
			case LogService.LOG_INFO:    log.info(msg); break;
			case LogService.LOG_DEBUG:   log.debug(msg); break;
			}
		} else {
			switch (entry.getLevel()) {
			case LogService.LOG_ERROR:   log.error(msg, t); break;
			case LogService.LOG_WARNING: log.warn(msg, t); break;
			case LogService.LOG_INFO:    log.info(msg, t); break;
			case LogService.LOG_DEBUG:   log.debug(msg, t); break;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addLogListener(LogListener listener, LogFilter filter) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addLogListener(LogListener listener) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeLogListener(LogListener listener) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumeration getLog() {
		// TODO Auto-generated method stub
		return null;
	}
}
