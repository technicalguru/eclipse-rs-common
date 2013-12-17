/**
 * 
 */
package rs.e4.help;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.internal.contexts.EclipseContext;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * The part containing an SWT browser only.
 * @author ralph
 *
 */
public class HelpPart {

	public static final String ID = "rs.rcpplugins.common.e4.part.help";
		
	private FormToolkit toolkit;
	private HelpBrowser browser;
	
	/**
	 * Constructor.
	 */
	public HelpPart() {
	}

	@PostConstruct
	public void createControls(Composite parent, IEclipseContext context) {
		toolkit = new FormToolkit(parent.getDisplay());
		parent.setLayout(new FillLayout());

		IEclipseContext tempContext = new EclipseContext(null);
		tempContext.set(Composite.class, parent);
		browser = ContextInjectionFactory.make(HelpBrowser.class, context, tempContext);
		toolkit.paintBordersFor(browser);
		
		// TODO All controls into toolbar?
		// TODO Status+Progress Bar
		 
	}

	@PreDestroy
	public void dispose() {
		if (toolkit != null) toolkit.dispose();
	}
	
	/**
	 * Open the given location
	 * @param url url to be opened
	 */
	public void setLocation(String url) {
		if (browser != null) {
			browser.setLocation(url);
		}
	}

	/**
	 * @return
	 * @see rs.e4.help.HelpBrowser#canGoForward()
	 */
	public boolean canGoForward() {
		return browser.canGoForward();
	}

	/**
	 * @return
	 * @see rs.e4.help.HelpBrowser#canGoBack()
	 */
	public boolean canGoBack() {
		return browser.canGoBack();
	}

	/**
	 * @return
	 * @see rs.e4.help.HelpBrowser#goForward()
	 */
	public boolean goForward() {
		return browser.goForward();
	}

	/**
	 * @return
	 * @see rs.e4.help.HelpBrowser#goBack()
	 */
	public boolean goBack() {
		return browser.goBack();
	}

	/**
	 * Refreshes the contents.
	 */
	public void refresh() {
		browser.refresh();
	}
	
	/**
	 * Stops the browser from loading.
	 */
	public void stop() {
		browser.stop();
	}
		
	/**
	 * Returns the {@link #loading}.
	 * @return the loading
	 */
	public boolean isLoading() {
		return browser.isLoading();
	}

	/**
	 * @see rs.e4.help.HelpBrowser#openContents()
	 */
	public void openContents() {
		browser.openContents();
	}
	
	/**
	 * @see rs.e4.help.HelpBrowser#isTreeVisible()
	 */
	public boolean isTreeVisible() {
		return browser.isTreeVisible();
	}
	
	/**
	 * Collapses the contents tree.
	 * @see rs.e4.help.HelpBrowser#collapseAll()
	 */
	public void collapseAll() {
		browser.collapseAll();
	}
	
	
	/**
	 * Expands the contents tree to level x.
	 * @param level level to expand to
	 * @see rs.e4.help.HelpBrowser#expandToLevel(int)
	 */
	public void expandToLevel(int level) {
		browser.expandToLevel(level);
	}

	/**
	 * Clears the history.
	 */
	public void clearHistory() {
		browser.clearHistory();
	}
}
