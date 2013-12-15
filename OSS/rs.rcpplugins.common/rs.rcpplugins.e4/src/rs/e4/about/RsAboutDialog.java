/**
 * 
 */
package rs.e4.about;

import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.preference.JFacePreferences;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.internal.ProductProperties;
import org.eclipse.ui.internal.about.AboutItem;
import org.eclipse.ui.internal.about.AboutTextManager;
import org.osgi.framework.Version;

import rs.e4.Plugin;

/**
 * The About Dialog until E4 is ready.
 * @author ralph
 *
 */
public class RsAboutDialog extends TrayDialog {

	private LocalResourceManager resourceManager;
	private ImageDescriptor titleAreaImageDescriptor;

	/**
	 * Image registry key for banner image (value
	 * <code>"dialog_title_banner_image"</code>).
	 */
	public static final String DLG_IMG_TITLE_BANNER = "dialog_title_banner_image";//$NON-NLS-1$

	// Minimum dialog width (in dialog units)
	private static final int MIN_DIALOG_WIDTH = 350;

	// Minimum dialog height (in dialog units)
	private static final int MIN_DIALOG_HEIGHT = 150;

	private Label titleImageLabel;

	private RGB titleAreaRGB;
	
	private static RGB HYPERLINK_COLOR = new RGB(0,0,139);
	
	Color titleAreaColor;

	private Composite workArea;

	private Label titleLabel;

	private Image titleAreaImage;

	private Composite infoParent;

	private boolean titleImageLargest = true;

	private IProduct product = Platform.getProduct();

	private String aboutText;

	private StyledText textLabel;

	/**
	 * Instantiate a new title area dialog.
	 * 
	 * @param parentShell
	 *            the parent SWT shell
	 */
	public RsAboutDialog(Shell parentShell) {
		super(parentShell);
		JFaceResources.getColorRegistry().put(JFacePreferences.HYPERLINK_COLOR, HYPERLINK_COLOR);

	}

	/**
	 * Sets the title image
	 * @param descriptor
	 */
	public void setTitleImage(ImageDescriptor descriptor) {
		titleAreaImageDescriptor = descriptor;
	}

	/**
	 * {@inheritDoc}
	 */
	protected Control createContents(Composite parent) {
		resourceManager = new LocalResourceManager(JFaceResources.getResources(), parent);

		// create the overall composite
		Composite contents = new Composite(parent, SWT.NONE);
		contents.setLayoutData(new GridData(GridData.FILL_BOTH));
		// initialize the dialog units
		initializeDialogUnits(contents);
		FormLayout layout = new FormLayout();
		contents.setLayout(layout);
		// Now create a work area for the rest of the dialog
		workArea = new Composite(contents, SWT.NONE);
		GridLayout childLayout = new GridLayout();
		childLayout.marginHeight = 0;
		childLayout.marginWidth = 0;
		childLayout.verticalSpacing = 0;
		workArea.setLayout(childLayout);
		if (titleAreaImageDescriptor == null) {
			titleAreaImageDescriptor = ProductProperties.getAboutImage(product);
		}
		if (titleAreaImageDescriptor != null) {
			setTitleImage(resourceManager.createImage(titleAreaImageDescriptor));
		}

		Control top = createTitleArea(contents);
		resetWorkAreaAttachments(top);
		workArea.setFont(JFaceResources.getDialogFont());
		// initialize the dialog units
		initializeDialogUnits(workArea);
		// create the dialog area and button bar
		dialogArea = createDialogArea(workArea);
		buttonBar = createButtonBar(workArea);

		return contents;
	}

	/**
	 * Creates and returns the contents of the upper part of this dialog (above
	 * the button bar).
	 * <p>
	 * The <code>Dialog</code> implementation of this framework method creates
	 * and returns a new <code>Composite</code> with no margins and spacing.
	 * Subclasses should override.
	 * </p>
	 * 
	 * @param parent
	 *            The parent composite to contain the dialog area
	 * @return the dialog area control
	 */
	protected Control createDialogArea(Composite parent) {
		// create the top level composite for the dialog area
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setFont(parent.getFont());
		// Build the separator line
		Label titleBarSeparator = new Label(composite, SWT.HORIZONTAL
				| SWT.SEPARATOR);
		titleBarSeparator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return composite;
	}

	/**
	 * Creates the dialog's title area.
	 * 
	 * @param parent
	 *            the SWT parent for the title area widgets
	 * @return Control with the highest x axis value.
	 */
	protected Control createTitleArea(Composite parent) {

		// add a dispose listener
		parent.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				if (titleAreaColor != null) {
					titleAreaColor.dispose();
				}
			}
		});
		// Determine the background color of the title bar
		Display display = parent.getDisplay();
		Color background;
		Color foreground;
		if (titleAreaRGB != null) {
			titleAreaColor = new Color(display, titleAreaRGB);
			background = titleAreaColor;
			foreground = null;
		} else {
			background = JFaceColors.getBannerBackground(display);
			foreground = JFaceColors.getBannerForeground(display);
		}

		parent.setBackground(background);
		int verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		int horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);

		// Dialog image @ left
		titleImageLabel = new Label(parent, SWT.CENTER);
		titleImageLabel.setBackground(background);
		if (titleAreaImage == null) {
			titleImageLabel.setImage(JFaceResources.getImage(DLG_IMG_TITLE_BANNER));
		} else {
			titleImageLabel.setImage(titleAreaImage);
		}

		FormData imageData = new FormData();
		imageData.top = new FormAttachment(0, 0);
		// Note: do not use horizontalSpacing on the right as that would be a
		// regression from
		// the R2.x style where there was no margin on the right and images are
		// flush to the right
		// hand side. see reopened comments in 41172
		imageData.left = new FormAttachment(0, 0); // horizontalSpacing
		titleImageLabel.setLayoutData(imageData);

		// Content @ top, right
		infoParent = new Composite(parent, SWT.NONE);
		JFaceColors.setColors(infoParent, foreground, background);
		infoParent.setFont(JFaceResources.getBannerFont());
		FormData infoData = new FormData();
		infoData.top = new FormAttachment(0, verticalSpacing);
		infoData.right = new FormAttachment(100, 0);
		infoData.left = new FormAttachment(titleImageLabel, horizontalSpacing);
		infoParent.setLayoutData(infoData);
		createInfoParent(infoParent);
		JFaceColors.setColors(titleLabel, foreground, background);
		determineTitleImageLargest();


		if (titleImageLargest) return titleImageLabel;
		return infoParent;
	}

	/**
	 * Creates the about part.
	 * @param parent
	 */
	protected void createInfoParent(Composite parent) {
		GridLayout gl = new GridLayout(1, false);
		gl.marginRight = 10;
		parent.setLayout(gl);
		titleLabel = new Label(parent, SWT.NONE);
		titleLabel.setFont(JFaceResources.getBannerFont());
		titleLabel.setText(ProductProperties.getProductName(product));
		titleLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		textLabel = new StyledText(parent, SWT.MULTI|SWT.READ_ONLY|SWT.WRAP);
		AboutItem item = AboutTextManager.scan(getAboutText());
		textLabel.setText(item.getText());
		GridData gd = new GridData(SWT.FILL, SWT.TOP, true, true);
		gd.verticalIndent = 10;
		titleLabel.setLayoutData(gd);

		RsAboutTextManager aboutTextManager = new RsAboutTextManager(textLabel);
		aboutTextManager.setItem(item);
	}

	/**
	 * Returns the version string
	 * @param version version
	 * @return the version string
	 */
	protected String getVersionString(Version version) {
		return version.getMajor()+"."+version.getMinor()+"."+version.getMicro();
	}

	/**
	 * Returns the text to be displayed
	 * @return the about text
	 */
	public String getAboutText() {
		if (aboutText != null) {
			return aboutText;
		}

		String s = ProductProperties.getAboutText(product);
		s += "\n\nVersion: "+getVersionString(product.getDefiningBundle().getVersion());
		//		s += "\nBuild Id: "+ProductProperties.getProductId(product);
		//		s += "\n\n"+licenseText;
		//		s += "\n"+licenseUrl;
		return s;
	}

	/**
	 * Sets the about text
	 * @param text new text or null if it shall be computed
	 */
	public void setAboutText(String text) {
		this.aboutText = text;
	}

	/**
	 * Determine if the title image is larger than the title message and message
	 * area. This is used for layout decisions.
	 */
	private void determineTitleImageLargest() {
		int titleY = titleImageLabel.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
		int verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		int labelY = infoParent.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
		labelY += verticalSpacing;
		labelY += verticalSpacing;
		titleImageLargest = titleY > labelY;
	}

	/**
	 * The <code>TitleAreaDialog</code> implementation of this
	 * <code>Window</code> methods returns an initial size which is at least
	 * some reasonable minimum.
	 * 
	 * @return the initial size of the dialog
	 */
	protected Point getInitialSize() {
		Point shellSize = super.getInitialSize();
		return new Point(Math.max(
				convertHorizontalDLUsToPixels(MIN_DIALOG_WIDTH), shellSize.x),
				Math.max(convertVerticalDLUsToPixels(MIN_DIALOG_HEIGHT),
						shellSize.y));
	}

	/**
	 * Returns the title image label.
	 * 
	 * @return the title image label
	 */
	protected Label getTitleImageLabel() {
		return titleImageLabel;
	}


	/**
	 * Sets the title to be shown in the title area of this dialog.
	 * 
	 * @param newTitle
	 *            the title show
	 */
	public void setTitle(String newTitle) {
		if (titleLabel == null)
			return;
		String title = newTitle;
		if (title == null)
			title = "";//$NON-NLS-1$
		titleLabel.setText(title);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Plugin.translate("about.title", ProductProperties.getProductName(product)));
	}

	/**
	 * Sets the title bar color for this dialog.
	 * 
	 * @param color
	 *            the title bar color
	 */
	public void setTitleAreaColor(RGB color) {
		titleAreaRGB = color;
	}

	/**
	 * Sets the title image to be shown in the title area of this dialog.
	 * 
	 * @param newTitleImage
	 *            the title image to be shown
	 */
	public void setTitleImage(Image newTitleImage) {
		titleAreaImage = newTitleImage;
		if (titleImageLabel != null) {
			titleImageLabel.setImage(newTitleImage);
			determineTitleImageLargest();
			Control top;
			top = titleImageLabel;
			resetWorkAreaAttachments(top);
		}
	}

	/**
	 * Reset the attachment of the workArea to now attach to top as the top
	 * control.
	 * 
	 * @param top
	 */
	private void resetWorkAreaAttachments(Control top) {
		FormData childData = new FormData();
		childData.top = new FormAttachment(top);
		childData.right = new FormAttachment(100, 0);
		childData.left = new FormAttachment(0, 0);
		childData.bottom = new FormAttachment(100, 0);
		workArea.setLayoutData(childData);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK and Cancel buttons by default
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
	}
}
