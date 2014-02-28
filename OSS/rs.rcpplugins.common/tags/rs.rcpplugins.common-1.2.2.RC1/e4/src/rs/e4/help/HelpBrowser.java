/**
 * 
 */
package rs.e4.help;

import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.internal.contexts.EclipseContext;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.io.FileFinder;
import rs.baselib.util.CommonUtils;
import rs.e4.E4Utils;
import rs.e4.Plugin;
import rs.e4.help.internal.BrowserHistory;
import rs.e4.help.internal.BrowserHistory.HelpPageType;
import rs.e4.help.internal.BrowserHistory.HistoryEntry;
import rs.e4.help.internal.Toc;
import rs.e4.help.internal.TocTreeViewContentProvider;
import rs.e4.help.internal.TocTreeViewLabelProvider;

/**
 * The Help Browser.
 * This object must be created via DI to set all required properties.
 * @author ralph
 *
 */
public class HelpBrowser extends Composite {

	private static Logger log = LoggerFactory.getLogger(HelpBrowser.class);

	private FormToolkit toolkit;
	private LocalResourceManager resourceManager;
	private ScrolledComposite browserPart;
	private Browser browser;
	private LocationListener locationListener;
	private TreeViewer contentsTree = null;
	private Composite browserStack;
	private StackLayout browserStackLayout;
	private BrowserHistory history = new BrowserHistory();
	private boolean loading = false;

	@Inject
	private HelpSystem help;

	@Inject
	private IEclipseContext context;

	@Inject UISynchronize uiSynchronize;

	private TocTreeViewLabelProvider treeLabelProvider;

	/**
	 * Constructor.
	 * @param parent
	 * @param style
	 */
	@Inject
	public HelpBrowser(Composite parent) {
		super(parent, SWT.NONE);
		resourceManager = new LocalResourceManager(JFaceResources.getResources(), parent);
	}

	/**
	 * Constructor.
	 * @param parent
	 * @param style
	 */
	public HelpBrowser(Composite parent, int style, HelpSystem helpSystem) {
		super(parent, style);
		resourceManager = new LocalResourceManager(JFaceResources.getResources(), parent);
		this.help = helpSystem;
		createContents();
	}

	/**
	 * Creates the help browsers components.
	 */
	@PostConstruct
	protected void createContents() {
		toolkit = new FormToolkit(getDisplay());
		setLayout(new FillLayout());

		browserPart = createBrowserPart(this);
	}

	/**
	 * Creates the tree viewer.
	 * @param parent parent composite of the viewer
	 * @return the viewer created
	 */
	protected TreeViewer createContentsTree(Composite parent) {
		TreeViewer viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL );
		viewer.setContentProvider(new TocTreeViewContentProvider());

		IEclipseContext tempContext = new EclipseContext(null);
		tempContext.set(LocalResourceManager.class, resourceManager);
		tempContext.set(TreeViewer.class, viewer);
		treeLabelProvider = ContextInjectionFactory.make(TocTreeViewLabelProvider.class, context, tempContext);
		viewer.setLabelProvider(treeLabelProvider);
		viewer.setInput(help.getPrimaryTocs());
		viewer.addTreeListener(treeLabelProvider);
		viewer.refresh(true);

		viewer.getTree().addMouseListener(new ClickListener());
		viewer.getTree().addMouseMoveListener(treeLabelProvider);
		return viewer;
	}

	/**
	 * Creates the browser.
	 * @param parent parent composite
	 * @return the scrolled composite where the browser sits in.
	 */
	protected ScrolledComposite createBrowserPart(Composite parent) {
		ScrolledComposite rc = new ScrolledComposite(parent, SWT.H_SCROLL|SWT.V_SCROLL);
		Composite c = new Composite(rc, SWT.NONE);
		c.setLayout(new GridLayout(1, false));

		createBrowserToolbar(c);

		browserStack = new Composite(c, SWT.NONE);
		browserStack.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		browserStackLayout = new StackLayout();
		browserStack.setLayout(browserStackLayout);

		// Browser
		try {
			locationListener = new LocationListener() {
				@Override
				public void changing(final LocationEvent event) {
					loading = true;
					if (!isCurrent(event.location)) {
						if (event.location.startsWith("file:///")) {
							if (!isSameFile(event.location)) { 
								// We need to react here
								event.doit = false;
								uiSynchronize.asyncExec(new Runnable() {
									public void run() {
										HistoryEntry current = history.getCurrentEntry();
										openHelpPage(current.bundle, current.url, event.location.substring(7));
									}
								});
							} else {
								// just the anchor changes
								HistoryEntry current = history.getCurrentEntry();
								try {
									history.add(current.bundle, new URL(event.location));
								} catch (MalformedURLException e) {
									// Can be ignored, we trust the browser
								}
							}
						} else if (event.location.startsWith("file:") || event.location.startsWith("http:") || event.location.startsWith("https:") ) {
							history.add(event.location);
						}
					}
				}

				@Override
				public void changed(LocationEvent event) {
					loading = false;
				}
			};
			browser = new Browser(browserStack, SWT.NONE);
			browser.addLocationListener(locationListener);
		} catch (SWTError e) {
			/* Browser widget could not be instantiated */
			Label label = new Label(c, SWT.CENTER | SWT.WRAP);
			label.setText(Plugin.translate("helpbrowser.error.nobrowser"));
		}

		// Contents Tree
		contentsTree = createContentsTree(browserStack);
		browserStackLayout.topControl = contentsTree.getTree();
		history.add(HelpPageType.CONTENTS);

		rc.setContent(c);
		rc.setExpandHorizontal(true);
		rc.setExpandVertical(true);
		return rc;
	}

	/**
	 * Returns whether the given location is the current content of the browser.
	 * @param location the location to be checked
	 * @return whether this is the current browser content
	 */
	protected boolean isCurrent(String location) {
		HistoryEntry entry = history.getCurrentEntry();
		if (entry != null) {
			try {
				URL url = new URL(location);
				if ((entry.url != null) && entry.url.equals(url)) return true;
			} catch (MalformedURLException e) {}
		}
		return false;
	}

	/**
	 * Returns whether the given location is the same file as contents of the browser.
	 * @param location the location to be checked
	 * @return whether this is the current browser file
	 */
	protected boolean isSameFile(String location) {
		HistoryEntry entry = history.getCurrentEntry();
		if (entry != null) {
			try {
				URL url = new URL(location);
				if ((entry.url != null) && entry.url.sameFile(url)) return true;
			} catch (MalformedURLException e) {}
		}
		return false;
	}

	/**
	 * Creates the browser toolbar.
	 * @param parent the parent for the toolbar 
	 */
	protected void createBrowserToolbar(Composite parent) {
		ToolBar toolBar = new ToolBar(parent, SWT.FLAT|SWT.WRAP|SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		//GridDataFactory.fillDefaults().align(SWT.END, SWT.CENTER).grab(true, false).applyTo(toolBar);

		// Contents link alltopic_co.gif
		ToolItem item = new ToolItem(toolBar, SWT.PUSH);
		item.setText(Plugin.translate("helpbrowser.label.contents"));
		item.setImage(resourceManager.createImage(ImageDescriptor.createFromURL(FileFinder.find(getClass(), "resources/icons/help/alltopics_co.gif"))));
		item.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openContents();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				openContents();
			}
		});
		// All registered top topics

	}

	/**
	 * Opens the given URL in the browser.
	 * @param url url to be opened
	 */
	public void setLocation(String url) {
		browser.setUrl(url);
	}

	/**
	 * Returns the browserPart.
	 * @return the browserPart
	 */
	protected ScrolledComposite getBrowserPart() {
		return browserPart;
	}

	/**
	 * Returns the browser.
	 * @return the browser
	 */
	protected Browser getBrowser() {
		return browser;
	}

	/**
	 * Returns whether the contents tree is visible.
	 * @return <code>true</code> when index tree is visible
	 */
	public boolean isTreeVisible() {
		return (contentsTree != null) && (browserStackLayout.topControl == contentsTree.getTree());
	}

	/**
	 * Returns whether the help browser is visible.
	 * @return <code>true</code> when help browser is visible
	 */
	public boolean isBrowserVisible() {
		return browserStackLayout.topControl == browser;
	}

	/**
	 * Collapses the contents tree.
	 */
	public void collapseAll() {
		if (contentsTree != null) contentsTree.collapseAll(); 
	}

	/**
	 * Expands contents tree to given level.
	 * @param level level to expand to
	 */
	public void expandToLevel(int level) {
		if (contentsTree != null) {
			contentsTree.expandToLevel(level+1);
		}
	}

	/**
	 * Clears the history.
	 */
	public void clearHistory() {
		history.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		toolkit.dispose();
		treeLabelProvider.dispose();
		super.dispose();
	}

	/**
	 * Open the contents tree.
	 */
	public void openContents() {
		if ((contentsTree != null) && (browserStackLayout.topControl != contentsTree.getTree())) {
			HistoryEntry entry = history.add(HelpPageType.CONTENTS);
			show(entry);
		}
	}

	/**
	 * Returns whether navigation forwards is possible.
	 * @return <code>true</code> or <code>false</code>
	 */
	public boolean canGoForward() {
		return history.canGoForward();
	}

	/**
	 * Returns whether navigation backwards is possible.
	 * @return <code>true</code> or <code>false</code>
	 */
	public boolean canGoBack() {
		return history.canGoBack();
	}

	/**
	 * Navigates forward in history.
	 * @return <code>true</code> when navigation was successful
	 */
	public boolean goForward() {
		if (canGoForward()) {
			HistoryEntry entry = history.goForward();
			show(entry);
		}
		return false;
	}

	/**
	 * Navigates back in history.
	 * @return <code>true</code> when navigation was successful
	 */
	public boolean goBack() {
		if (canGoBack()) {
			HistoryEntry entry = history.goBack();
			show(entry);
		}
		return false;
	}

	/**
	 * Refreshes the browser.
	 */
	public void refresh() {
		if (browser != null) browser.refresh();
	}

	/**
	 * Stops the browser from loading.
	 */
	public void stop() {
		if (browser != null) browser.stop();
	}

	/**
	 * Returns the {@link #loading}.
	 * @return the loading
	 */
	public boolean isLoading() {
		return loading;
	}

	/**
	 * Opens the link from the given context.
	 * @param contextBundle bundle context
	 * @param contextUrl URL context
	 * @param link new link
	 */
	public void openHelpPage(Bundle contextBundle, URL contextUrl, String link) {
		if (browserStackLayout.topControl != browser) {
			browserStackLayout.topControl = browser;
			browserStack.layout();
		}

		HistoryEntry entry = getContentUrl(contextBundle, contextUrl, link);
		String text = null;
		if ((entry != null) && (entry.url != null)) {
			try {
				String anchor = entry.url.getRef();
				entry.url = CommonUtils.setAnchor(FileLocator.toFileURL(entry.url), anchor);
			} catch (Exception e) {
				log.error("Cannot load help page", e);
				text = "<h1>Error 404 Not Found</h1>"+link+" ("+entry.url+")";
				entry = new HistoryEntry(HelpPageType.ERROR, text);
			}
		} else if (entry == null) {
			text = "<h1>Error 404 Not Found</h1>"+link;
			entry = new HistoryEntry(HelpPageType.ERROR, text);
		}
		history.add(entry);
		show(entry);
	}

	/**
	 * Shows the entry without any history modification.
	 * @param entry history entry to be shown
	 */
	protected void show(HistoryEntry entry) {
		try {
			switch (entry.type) {
			case PAGE: 
				if (browserStackLayout.topControl != browser) {
					browserStackLayout.topControl = browser;
					browserStack.layout();
				}
				browser.setUrl(FileLocator.toFileURL(entry.url).toString());
				break;
			case EXTERNAL: 
				if (browserStackLayout.topControl != browser) {
					browserStackLayout.topControl = browser;
					browserStack.layout();
				}
				browser.setUrl(entry.url.toString());
				break;
			case ERROR:
				if (browserStackLayout.topControl != browser) {
					browserStackLayout.topControl = browser;
					browserStack.layout();
				}
				browser.setText(entry.text);
				break;
			case CONTENTS:
				if ((contentsTree != null) && (browserStackLayout.topControl != contentsTree.getTree())) {
					browserStackLayout.topControl = contentsTree.getTree();
					browserStack.layout();
				}
				break;
			case INDEX:
				// DO nothing yet
			}
		} catch (Exception e) {
			log.error("Cannot show content", e);
		}
	}

	/**
	 * Returns the new URL for th elink in the given context.
	 * @param contextBundle the bundle from where the link is called
	 * @param contextUrl the URL from where the link is called
	 * @param link the actual link
	 * @return the new history entry, can be <code>null</code> if no such file exists
	 */
	public HistoryEntry getContentUrl(Bundle contextBundle, URL contextUrl, String link) {
		HistoryEntry rc = null;

		// Strip off the workspace/bundle prefix
		link = stripBundle(contextBundle, link);

		// Split off the anchor
		String s[] = link.split("#", 2);
		link = s[0];
		String anchor = s.length > 1 ? s[1] : null;

		if (link.startsWith("/")) {
			if (link.startsWith("/PLUGINSROOT/")) {
				// /PLUGINSROOT/pluginid/path
				String parts[] = link.split("\\/", 4);
				Bundle bundle = Platform.getBundle(parts[2]);
				URL url = E4Utils.find(bundle, "/$nl$/"+parts[3], null);
				if (url != null) {
					url = CommonUtils.setAnchor(url, anchor);
					rc = new HistoryEntry(bundle, url);
				} else {
					String text = "<h1>Error 404 Not Found</h1>"+link+" ("+link+")";
					rc = new HistoryEntry(HelpPageType.ERROR, text);
				}
			} else {
				// local path
				if (contextUrl != null) {
					URL url = E4Utils.find(contextBundle, "/$nl$"+link, null);
					if (url != null) {
						url = CommonUtils.setAnchor(url, anchor);
						rc = new HistoryEntry(contextBundle, url);
					} else {
						String text = "<h1>Error 404 Not Found</h1>"+link+" ("+link+")";
						rc = new HistoryEntry(HelpPageType.ERROR, text);
					}
				} else {
					log.error("Cannot find \""+link+"\"");
				}
			}
		} else {
			// A relative link
			if (contextUrl != null) {
				int pos = contextUrl.getPath().lastIndexOf('/');
				String path = pos > 0 ? "/$nl$"+contextUrl.getPath().substring(0, pos+1)+link : "/$nl$/"+link;
				URL url = E4Utils.find(contextBundle, path, null);
				if (url != null) {
					url = CommonUtils.setAnchor(url, anchor);
					rc = new HistoryEntry(contextBundle, url);
				} else {
					String text = "<h1>Error 404 Not Found</h1>"+link+" ("+link+")";
					rc = new HistoryEntry(HelpPageType.ERROR, text);
				}
			} else {
				log.error("Cannot find \""+link+"\"");
			}
		}
		return rc;
	}

	/**
	 * Strips off any bundle location and language prefix from the link.
	 * The action is necessary to switch seamlessly to another language page
	 * in case the required page is not available in the same language.
	 * @param bundle the context bundle
	 * @param link the link to be opened
	 * @return the link cleaned off by bundle and language prefixes
	 */
	protected String stripBundle(Bundle bundle, String link) {
		//		log.info("stripBundle: "+link);
		//		log.info("stripBundle: config  ="+Platform.getConfigurationLocation().getURL());
		//		log.info("stripBundle: install ="+Platform.getInstallLocation().getURL());
		//		log.info("stripBundle: instance="+Platform.getInstanceLocation().getURL());
		//		log.info("stripBundle: user    ="+Platform.getUserLocation().getURL());
		if (bundle == null) return link;
		String bundleLocation = bundle.getLocation();
		//log.info("stripBundle:     bundleLocation="+bundleLocation);
		if (bundleLocation.startsWith("reference:file:")) bundleLocation = bundleLocation.substring(15);
		//		log.info("stripBundle:     bundleLocation="+bundleLocation);
		//		log.info("stripBundle:     link="+link);
		int pos = link.indexOf(bundleLocation);
		if (pos >= 0) {
			link = link.substring(pos+bundleLocation.length());
			//log.info("stripBundle:     strippedBundle="+link);
			// Strip off language prefixes
			for (String nl : E4Utils.getNLVariants()) {
				nl = nl.replaceAll("\\/", "_");
				if (link.startsWith(nl+"/")) {
					link = link.substring(nl.length());
					break;
				}

			}
			if (link.startsWith("default/")) link = link.substring(7);
			//log.info("stripBundle:     strippedNL="+link);
			if (!link.startsWith("/")) link = '/'+link;
		}
		//		log.info("stripBundle:     return="+link);
		return link;
	}

	/**
	 * Implements the link behaviour for the contents tree.
	 * @author ralph
	 *
	 */
	protected class ClickListener implements MouseListener {

		@Override
		public void mouseDoubleClick(MouseEvent e) {
			// Do nothing
		}

		@Override
		public void mouseDown(MouseEvent e) {
		}

		@Override
		public void mouseUp(MouseEvent e) {
			TreeItem item = contentsTree.getTree().getItem(new Point(e.x, e.y));
			if (item != null) {
				Toc toc = (Toc)item.getData();
				if (toc.hasLink()) {
					openHelpPage(toc.getLocalRoot().getBundle(), toc.getLocalRoot().getUrl(), toc.getLink());
				}
			}
		}

	}

}
