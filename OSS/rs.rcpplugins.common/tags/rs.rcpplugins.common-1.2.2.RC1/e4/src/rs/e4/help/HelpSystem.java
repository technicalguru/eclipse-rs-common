/**
 * 
 */
package rs.e4.help;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.osgi.framework.Bundle;
import org.osgi.service.event.Event;
import org.slf4j.LoggerFactory;

import rs.baselib.lang.LangUtils;
import rs.e4.E4Utils;
import rs.e4.help.internal.HelpTopic;
import rs.e4.help.internal.Toc;

/**
 * Defines some properties for the help system.
 * @author ralph
 *
 */
@Singleton
@Creatable
public class HelpSystem {

	public static final String TOPIC_CONTEXT_URL = "rsbudget/help/context/url";
	public static final String TOPIC_CONTEXT_ID = "rsbudget/help/context/id";

	private static final String TOC_ID = "rs.rcpplugins.common.e4.help";

	private Toc root = new Toc(null);
	private List<Toc> tocEntries;
	private List<Toc> primaryTocEntries;
	
	@Inject
	private IEventBroker eventBroker;

	@Inject
	private IExtensionRegistry registry;

	/**
	 * Constructor.
	 */
	public HelpSystem() {
		tocEntries = new ArrayList<>();
		primaryTocEntries = new ArrayList<>();
	}

	@PostConstruct
	protected void postConstruct() {
		loadTocs();
	}

	/**
	 * Sets the help URL to be shown.
	 * @param url
	 */
	public void setHelpContextUrl(String url) {
		eventBroker.send(TOPIC_CONTEXT_URL, url);
	}

	/**
	 * Sets the help id to be shown.
	 * @param id
	 */
	public void setHelpContextId(String id) {
		eventBroker.send(TOPIC_CONTEXT_ID, id);
	}

	/**
	 * Loads all extensions.
	 */
	protected void loadTocs() {
		IConfigurationElement[] config = registry.getConfigurationElementsFor(TOC_ID);
		for (IConfigurationElement e : config) {
			String file = e.getAttribute("file");
			try {
				boolean primary = LangUtils.getBoolean(e.getAttribute("primary"));
				String bundleName = e.getContributor().getName();
				Bundle bundle = Platform.getBundle(bundleName);
				URL url = E4Utils.find(bundle, file, null);
				if (url != null) {
					addToc(bundle, url, primary);
				}
			} catch (Throwable t) {
				LoggerFactory.getLogger(HelpSystem.class).error("Cannot load TOC file: "+file, t);
			}
		}

	}

	/**
	 * Adds a TOC file at the root.
	 * @param url url of file to be added
	 */
	public void addToc(Bundle bundle, URL url, boolean primary) throws ConfigurationException {
		XMLConfiguration cfg = new XMLConfiguration(url);
		addToc(loadToc(root, cfg, bundle, url), primary);
	}

	/**
	 * Adds a TOC entry.
	 * @param toc entry to be added
	 * @param primary whether this is a primary entry
	 */
	protected void addToc(Toc toc, boolean primary) {
		tocEntries.add(toc);
		if (primary) primaryTocEntries.add(toc);
	}
	
	public Toc[] getPrimaryTocs() {
		return primaryTocEntries.toArray(new Toc[primaryTocEntries.size()]);
	}
	
	@Inject
	@Optional
	private void partActivation(@UIEventTopic(UIEvents.UILifeCycle.ACTIVATE)Event event) {
		MPart part = (MPart)event.getProperty("ChangedElement");
		setHelpContextId(part.getElementId());
	}

	/**
	 * Loads the TOC entry described by the configuration
	 * @param parent parent TOC entry
	 * @param cfg configuration to be loaded
	 * @param url URL associated with config
	 * @return the TOC tree loaded
	 * @throws ConfigurationException when a config problem occurred
	 */
	protected static Toc loadToc(Toc parent, HierarchicalConfiguration cfg, Bundle bundle, URL url) throws ConfigurationException {
		Toc toc = parent.createChild();
		toc.setUrl(bundle, url);
		
		// Label of this TOC
		String label = localize(bundle, cfg.getString("[@label]", null));
		toc.setLabel(label);
		
		// direct html link
		String href = cfg.getString("[@topic]", null);
		if (href != null) {
			HelpTopic topic = new HelpTopic(label, href);
			toc.setTopic(topic);
		}
		
		// A link to a another anchor
		String anchor = cfg.getString("[@link_to]", null);
		toc.setLinkTo(anchor);
		
		// All children from here
		for (HierarchicalConfiguration subConfig : cfg.configurationsAt("topic")) {
			loadTopic(toc, subConfig, bundle, url);
		}
		return toc;
	}
	
	/**
	 * Loads the topic described by the configuration into the given TOC entry.
	 * @param parent the parent TOC entry
	 * @param cfg configuration describing the topic
	 * @param url URL the TOC was loaded from
	 * @throws ConfigurationException when a config problem occurred
	 */
	protected static void loadTopic(Toc parent, HierarchicalConfiguration cfg, Bundle bundle, URL url) throws ConfigurationException {

		// Label
		String label = localize(bundle, cfg.getString("[@label]", null));
		
		// Link to TOC entry
		try {
			HierarchicalConfiguration subConfig = cfg.configurationAt("link(0)", false);
			if (subConfig != null) {
				URL childURL = E4Utils.find(bundle, "$nl$/"+subConfig.getString("[@toc]"), null);
				XMLConfiguration tocCfg = new XMLConfiguration(childURL);
				Toc toc = loadToc(parent, tocCfg, bundle, childURL);
				if (label != null) {
					toc.setLabel(label);
				}
				return;
			}
		}  catch (Exception e) {
			// No such link, ignore
		}
		
		// Href
		String href = cfg.getString("[@href]", null);
		
		// Create TOC entry with topic
		Toc toc = parent.createChild();
		HelpTopic topic = new HelpTopic(label, href);
		toc.setTopic(topic);
		
		// Load sub topics
		for (HierarchicalConfiguration subConfig : cfg.configurationsAt("topic")) {
			loadTopic(toc, subConfig, bundle, url);
		}
		
	}
	
	protected static String localize(Bundle bundle, String s) {
		if (s == null) return null;
		if (s.startsWith("%")) {
			s = E4Utils.translate(s, "platform:/plugin/"+bundle.getSymbolicName());
		}
		return s;
	}
}
