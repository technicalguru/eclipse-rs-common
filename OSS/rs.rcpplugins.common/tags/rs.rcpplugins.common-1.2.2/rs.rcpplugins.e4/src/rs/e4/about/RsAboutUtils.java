/**
 * 
 */
package rs.e4.about;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.internal.browser.DefaultWorkbenchBrowserSupport;
import org.slf4j.LoggerFactory;

import rs.e4.Plugin;

/**
 * @author ralph
 *
 */
public class RsAboutUtils {

	public static void openLink(Shell shell, String href) {
		if (href.startsWith("file:")) { //$NON-NLS-1$
			href = href.substring(5);
			while (href.startsWith("/")) { //$NON-NLS-1$
				href = href.substring(1);
			}
			href = "file:///" + href; //$NON-NLS-1$
		}
		IWorkbenchBrowserSupport support = new DefaultWorkbenchBrowserSupport();

		try {
			IWebBrowser browser = support.getExternalBrowser();
			browser.openURL(new URL(urlEncodeForSpaces(href.toCharArray())));
		} catch (MalformedURLException e) {
			openWebBrowserError(shell, href, e);
		} catch (PartInitException e) {
			openWebBrowserError(shell, href, e);
		}
	
	}
	
	/**
	 * This method encodes the url, removes the spaces from the url and replaces
	 * the same with <code>"%20"</code>. This method is required to fix Bug
	 * 77840.
	 * 
	 * @since 3.0.2
	 */
	public static String urlEncodeForSpaces(char[] input) {
		StringBuffer retu = new StringBuffer(input.length);
		for (int i = 0; i < input.length; i++) {
			if (input[i] == ' ') {
				retu.append("%20"); //$NON-NLS-1$
			} else {
				retu.append(input[i]);
			}
		}
		return retu.toString();
	}

	/**
	 * display an error message
	 */
	public static void openWebBrowserError(Shell shell, final String href, final Throwable t) {
		String title = Plugin.translate("about.error.nobrowser.title");
		String msg = Plugin.translate("about.error.nobrowser.message", href);
		LoggerFactory.getLogger(RsAboutUtils.class).error("Cannot open browser", t);
		MessageDialog.openError(shell, title, msg);
	}



}
