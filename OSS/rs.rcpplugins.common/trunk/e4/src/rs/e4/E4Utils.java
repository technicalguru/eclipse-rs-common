/**
 * 
 */
package rs.e4;

import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.translation.TranslationService;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.e4.ui.model.application.commands.MHandler;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

/**
 * Utilities for E4.
 * @author ralph
 *
 */
public class E4Utils {

	public static final TranslationService TRANSLATIONS = getTopContext().get(TranslationService.class);

	private static String NL_VARIANTS[] = null;
	
	/**
	 * Finds the top Eclipse context.
	 * @return the eclipse context.
	 */
	public static IEclipseContext getTopContext() {
		return E4Workbench.getServiceContext();

	}
	
	/**
	 * Finds the top Eclipse context.
	 * @return the eclipse context.
	 */
	public static IEclipseContext getContext(Class<?> clazz) {
		Bundle bundle = FrameworkUtil.getBundle(clazz);
		BundleContext bundleContext = bundle.getBundleContext();

		return EclipseContextFactory.getServiceContext(bundleContext);
	}
	
	/**
	 * Finds an Eclipse context for the given class that fits the given type.
	 * The type is not always ensured as at least the top-most context will be returned.
	 * @param clazz class that requires context
	 * @param type type that the context should be of
	 * @return the eclipse context.
	 */
	public static IEclipseContext findContext(Class<?> clazz, Class<? extends IEclipseContext> type) {
		Bundle bundle = FrameworkUtil.getBundle(clazz);
		BundleContext bundleContext = bundle.getBundleContext();
		
		IEclipseContext ctx = EclipseContextFactory.getServiceContext(bundleContext);
		while ((ctx != null) && (ctx.getParent() != null)) {
			if ((type != null) && type.isInstance(ctx)) break;
			ctx = ctx.getParent();
		}
		return ctx;
	}
	
	/**
	 * Translate the given key.
	 * @param key key to translate
	 * @param contributorUri the contributor URI of the translation
	 */
	public static String translate(String key, String contributorUri, Object... args) {
		if ((key == null) || (key.length() == 0)) return "";
		if (key.charAt(0) != '%') key = '%'+key;
		String rc = TRANSLATIONS.translate(key, contributorUri);
		if ((args == null) || (args.length == 0)) return rc;
		return MessageFormat.format(rc, args);
	}

	/**
	 * Enhances the {@link FileLocator#find(Bundle, org.eclipse.core.runtime.IPath, Map)} method
	 * by allowing a default $nl$ location "default".
	 * @param bundle the bundle argument to {@link FileLocator#find(Bundle, org.eclipse.core.runtime.IPath, Map)}
	 * @param path the path argument to {@link FileLocator#find(Bundle, org.eclipse.core.runtime.IPath, Map)}
	 * @param defaultPath the default path to be used
	 * @param override the override argument to {@link FileLocator#find(Bundle, org.eclipse.core.runtime.IPath, Map)}
	 * @return the URL found or null
	 */
	public static URL find(Bundle bundle, String path, Map<String,String> override) {
		return find(bundle, path, "default", override);
	}
	
	/**
	 * Enhances the {@link FileLocator#find(Bundle, org.eclipse.core.runtime.IPath, Map)} method
	 * by allowing a specified default $nl$ location.
	 * @param bundle the bundle argument to {@link FileLocator#find(Bundle, org.eclipse.core.runtime.IPath, Map)}
	 * @param path the path argument to {@link FileLocator#find(Bundle, org.eclipse.core.runtime.IPath, Map)}
	 * @param defaultPath the default path to be used
	 * @param override the override argument to {@link FileLocator#find(Bundle, org.eclipse.core.runtime.IPath, Map)}
	 * @return the URL found or null
	 */
	public static URL find(Bundle bundle, String path, String defaultPath, Map<String,String> override) {
		String nlVariants[] = getNLPathVariants(path, defaultPath);
		URL url = null;
		for (int i=0; i<nlVariants.length; i++) {
			url = FileLocator.find(bundle, new Path(nlVariants[i]), override);
			if (url != null) {
				return url;
			}
		}
		return null;
	}
	
	/**
	 * Returns path locale variants.
	 * @param s the path definition (containing $nl$ as placeholder)
	 * @param defaultString a default location (can be null)
	 * @return
	 */
	public static String[] getNLPathVariants(String s, String defaultString) {
		String nlVariants[] = getNLVariants();
		if (s.indexOf("$nl$") >= 0) {
			String rc[] = new String[nlVariants.length+2];
			for (int i=0; i<nlVariants.length; i++) {
				rc[i] = s.replaceAll("\\$nl\\$", nlVariants[i].replaceAll("_", "\\/"));
			}
			if (defaultString != null) rc[nlVariants.length] = s.replaceAll("\\$nl\\$", defaultString);
			else rc[nlVariants.length] = s.replaceAll("\\$nl\\$\\/?", "");
			rc[nlVariants.length+1] = s.replaceAll("\\$nl\\$\\/?", "");
			return rc;
		}
		return new String[] { s };
	}
	
	/**
	 * Returns the Locale variants in order of priority.
	 * @return the variants
	 */
	public static String[] getNLVariants() {
		if (NL_VARIANTS == null) {
			List<String> variants = new ArrayList<>();
			Locale locale  = Locale.getDefault();
			String lang    = locale.getLanguage();
			String country = locale.getCountry();
			String variant = locale.getVariant();
			if ((country != null) && (country.length() > 0)) {
				if ((variant != null) && (variant.length() > 0)) variants.add(lang+"_"+country+"_"+variant);
				variants.add(lang+"_"+country);
			}
			variants.add(lang);
			// Add English as default
			variants.add("en_UK");
			variants.add("en");
			NL_VARIANTS = variants.toArray(new String[variants.size()]); 
		}
		return NL_VARIANTS;
	}
	
	/**
	 * Returns the handler object for the given command in the part. 
	 * @param part the part to check
	 * @param commandId the command ID
	 * @return the handler object
	 */
	public static Object getHandlerFor(MPart part, String commandId) {
		if (part == null) return null;
		List<MHandler> l = part.getHandlers();
		for (MHandler h : l) {
			if (h.getCommand().getElementId().equals(commandId)) {
				return h.getObject();
			}
		}
		return null;
	}
	
}
