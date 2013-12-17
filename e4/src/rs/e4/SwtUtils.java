/**
 * 
 */
package rs.e4;

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.slf4j.LoggerFactory;

/**
 * Some basic method for SWT.
 * @author ralph
 *
 */
public class SwtUtils {

	/** Execute synchronously to display */
	public static void syncExec(Display display, Runnable target) { 
		if (display != null && !display.isDisposed() && display.getThread().isAlive()) {
			display.syncExec(target);
		} else { 
			LoggerFactory.getLogger(SwtUtils.class).warn("Could not delegate task to SWT thread: display invalid");
		}
	}

	/** Execute asynchronously to display */
	public static void asyncExec(Display display, Runnable target) { 
		if (display != null && !display.isDisposed() && display.getThread().isAlive()) {
			display.asyncExec(target);
		} else { 
			LoggerFactory.getLogger(SwtUtils.class).warn("Could not delegate task to SWT thread: display invalid");
		}
	}

	/**
	 * Returns the content of the text synchronously.
	 * The method can be called from any thread and will synchronize with SWT.
	 * @param text the text
	 * @return the content of the text field
	 */
	public static String getText(Text text) {
		TextGetter getter = new TextGetter(text);
		syncExec(text.getDisplay(), getter);
		return getter.getText();
	}

	/**
	 * Sets the content of the text synchronously.
	 * The method can be called from any thread and will synchronize with SWT.
	 * @param text the text
	 * @param s the new content of the text field
	 */
	public static void setText(Text text, String s) {
		TextSetter setter = new TextSetter(text, s);
		syncExec(text.getDisplay(), setter);
	}

	/**
	 * Returns true when the text field contains something useful.
	 * @param text text field
	 * @return true or false
	 */
	public static boolean isTextEmpty(Text text) {
		return getText(text).trim().length() == 0;
	}

	/**
	 * Derives a bold font.
	 * @param font original font.
	 * @return
	 */
	public static FontDescriptor deriveBoldFont(Font font) {
		return deriveBoldFont(font.getFontData());
	}

	/**
	 * Derives a bold font.
	 * @param font original font.
	 * @return
	 */
	public static FontDescriptor deriveBoldFont(FontDescriptor font) {
		return deriveBoldFont(font.getFontData());
	}

	/**
	 * Derives a bold font.
	 * @param fontData original font.
	 * @return
	 */
	public static FontDescriptor deriveBoldFont(FontData fontData[]) {
		FontData fdl[] = FontDescriptor.copy(fontData);
		for (FontData fd : fdl) fd.setStyle(fd.getStyle() | SWT.BOLD);
		return FontDescriptor.createFrom(fdl);
	}

	/**
	 * Derives a plain font.
	 * @param font original font.
	 * @return
	 */
	public static FontDescriptor derivePlainFont(Font font) {
		return derivePlainFont(font.getFontData());
	}

	/**
	 * Derives a plain font.
	 * @param font original font.
	 * @return
	 */
	public static FontDescriptor derivePlainFont(FontDescriptor font) {
		return derivePlainFont(font.getFontData());
	}

	/**
	 * Derives a plain font.
	 * @param fontData original font.
	 * @return
	 */
	public static FontDescriptor derivePlainFont(FontData fontData[]) {
		FontData fdl[] = FontDescriptor.copy(fontData);
		for (FontData fd : fdl) fd.setStyle(fd.getStyle() & ~SWT.BOLD);
		return FontDescriptor.createFrom(fdl);
	}

	/**
	 * Derives a font with given size.
	 * @param font original font.
	 * @param newSize new size.
	 * @return
	 */
	public static FontDescriptor deriveFont(Font font, int newSize) {
		return deriveFont(font.getFontData(), newSize);
	}

	/**
	 * Derives a font with given size.
	 * @param font original font.
	 * @param newSize new size.
	 * @return
	 */
	public static FontDescriptor deriveFont(FontDescriptor font, int newSize) {
		return deriveFont(font.getFontData(), newSize);
	}

	/**
	 * Derives a font with given size.
	 * @param fontData original font.
	 * @param newSize new size.
	 * @return
	 */
	public static FontDescriptor deriveFont(FontData fontData[], int newSize) {
		FontData fdl[] = FontDescriptor.copy(fontData);
		for (FontData fd : fdl) fd.setHeight(newSize);
		return FontDescriptor.createFrom(fdl);
	}

	/**
	 * Produces a radio button or checkbox image.
	 * @param shell shell to be used
	 * @param buttonStyle style of button ({@link SWT#RADIO} or {@link SWT#CHECK})
	 * @param selected <code>true</code> when the box is ticked
	 * @return the image
	 */
	public static Image makeShot(Shell shell, int buttonStyle, boolean selected) {
		int style = (SWT.CHECK | SWT.RADIO) & buttonStyle;
		if ((style == 0) || (style == (SWT.CHECK | SWT.RADIO))) {
			throw new IllegalArgumentException("buttonStyle must be SWT.CHECK or SWT.RADIO");
		}
		
		// Hopefully no platform uses exactly this color
		// because we'll make it transparent in the image.
		Color greenScreen = new Color(shell.getDisplay(), 222, 223, 224);

		Shell tempShell = new Shell(shell, SWT.NO_TRIM);

		// otherwise we have a default gray color
		tempShell.setBackground(greenScreen);

		Button button = new Button(tempShell, buttonStyle);
		button.setBackground(greenScreen);
		button.setSelection(selected);

		// otherwise an image is located in a corner
		button.setLocation(1, 1);
		Point bsize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT);

		// otherwise an image is stretched by width
		bsize.x = Math.max(bsize.x - 1, bsize.y - 1);
		bsize.y = Math.max(bsize.x - 1, bsize.y - 1);
		button.setSize(bsize);
		tempShell.setSize(bsize);

		tempShell.open();
		GC gc = new GC(tempShell);
		Image image = new Image(shell.getDisplay(), bsize.x, bsize.y);
		gc.copyArea(image, 0, 0);
		gc.dispose();
		tempShell.close();

		ImageData imageData = image.getImageData();
		imageData.transparentPixel = imageData.palette.getPixel(greenScreen.getRGB());

		return new Image(shell.getDisplay(), imageData);
	}

	/**
	 * Returns the text of a text field.
	 * @author ralph
	 *
	 */
	private static class TextGetter implements Runnable {
		private Text text;
		private String rc = "";
		public TextGetter(Text text) {
			this.text = text;
		}
		public void run() {
			if (!text.isDisposed()) {
				rc = text.getText();
			}
		}
		public String getText() {
			return rc;
		}
	}

	/**
	 * Sets the text of a text field.
	 * @author ralph
	 *
	 */
	private static class TextSetter implements Runnable {
		private Text text;
		private String s;
		public TextSetter(Text text, String s) {
			this.text = text;
			this.s = s;
		}
		public void run() {
			if (!text.isDisposed()) {
				text.setText(s);
			}
		}
	}
	
}
