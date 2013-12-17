/**
 * 
 */
package rs.e4.splash;

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;

import rs.e4.Plugin;

/**
 * The form with a password in it.
 * @author ralph
 *
 */
public class PasswordSplashHandler extends LoginSplashHandler {

	
	/**
	 * Constructor.
	 * @param formHandler
	 */
	public PasswordSplashHandler(ISplashFormHandler formHandler) {
		super(formHandler);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void createUI(Composite parent) {
		
		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		parent.setLayout(layout);
		
		Composite form = new Composite(parent, SWT.NONE);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		form.setLayoutData(gd);
		createForm(form);
		
		// A composite for the buttons
		Composite buttons = new Composite(parent, SWT.NONE);
		buttons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		createButtons(buttons);

		Composite progress = new Composite(parent, SWT.NONE);
		progress.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, true));
		createProgressInfo(progress);
		
	}

	/**
	 * Creates the pure form.
	 * @param parent
	 */
	protected void createForm(Composite parent) {
		GridLayout layout = createFormLayout();
		parent.setLayout(layout);
		
		// The branding
		createBranding(parent);
		
		// A label
		Label label = new Label(parent, SWT.NONE);
		label.setText(Plugin.translate("splash.login.label.password"));
		applyFormLabelLayoutData(label);
		
		// The password field
		Text pwd = createPasswordText(parent);
		applyFormControlLayoutData(pwd);
		
		// error info
		Label error = createErrorInfo(parent);
		error.setAlignment(SWT.RIGHT);
		GridData gd = new GridData(SWT.FILL, SWT.TOP, true, false);
		error.setLayoutData(gd);
	}
	
	protected GridLayout createFormLayout() {
		GridLayout layout = new GridLayout(1, false);
		layout.marginTop = 30;
		layout.marginHeight = 5;
		layout.marginWidth = 10;
		layout.marginLeft = 240;
		layout.marginRight = 20;
		layout.horizontalSpacing = 1;
		return layout;
	}
	
	protected void createBranding(Composite parent) {
		
	}
	
	/**
	 * Applies the layout for the label in the form.
	 * @param label
	 */
	protected void applyFormLabelLayoutData(Label label) {
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
		label.setLayoutData(gd);
	}
	
	/**
	 * Applies the layout for a control in the form.
	 * @param control
	 */
	protected void applyFormControlLayoutData(Control control) {
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		control.setLayoutData(gd);
	}
	
	/**
	 * Creates the buttons.
	 * @param parent
	 */
	protected void createButtons(Composite parent) {
		GridLayout layout = createButtonLayout();
		parent.setLayout(layout);

		// OK and cancel button
		Button ok = createOkButton(parent);
		applyButtonLayoutData(ok, true);
		
		Button cancel = createCancelButton(parent);
		applyButtonLayoutData(cancel, false);
	}
	
	protected GridLayout createButtonLayout() {
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 10;
		layout.marginRight = 20;
		return layout;
	}
	
	/**
	 * Applies the layout data for the button.
	 * @param button
	 */
	protected void applyButtonLayoutData(Button button, boolean isFirst) {
		GridData gd = new GridData(isFirst ? SWT.RIGHT : SWT.LEFT, SWT.CENTER, isFirst, false);
		gd.widthHint = 75;
		button.setLayoutData(gd);
	}
	
	/**
	 * Creates the progress info.
	 * @param parent
	 */
	protected void createProgressInfo(Composite parent) {
		GridLayout layout = new GridLayout(1, true);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		parent.setLayout(layout);
		
		// progress label
		Label progressLabel = createProgressLabel(parent);
		progressLabel.setFont(getResourceManager().createFont(FontDescriptor.createFrom("Arial", 10, SWT.ITALIC))); //$NON-NLS-1$
		//progressLabel.setForeground(getResourceManager().createColor(new RGB(255,255,255)));
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd.horizontalIndent = 5;
		progressLabel.setLayoutData(gd);
		
		// progress bar
		ProgressBar bar = createProgressBar(parent);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		bar.setLayoutData(gd);
		

	}
}
