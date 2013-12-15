/**
 * 
 */
package rs.e4.splash;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.splash.AbstractSplashHandler;

import rs.e4.Plugin;

/**
 * A splash handler with login.
 * @author ralph
 *
 */
public abstract class LoginSplashHandler extends AbstractSplashHandler {

	protected enum SplashStatus {
		NONE,
		SUCCESS,
		ERROR,
		CANCELLED
	}

	private LocalResourceManager resourceManager;

	private Composite composite;

	private Text passwordText;

	private Button okButton;

	private Button cancelButton;

	private Label progressLabel;

	private Label errorLabel;
	private String errorText;

	private ProgressBar progressBar;

	private volatile SplashStatus splashStatus = null;

	private ISplashFormHandler formHandler;

	/**
	 * Constructor.
	 */
	public LoginSplashHandler(ISplashFormHandler formHandler) {
		composite = null;
		okButton = null;
		cancelButton = null;
		passwordText = null;
		splashStatus = SplashStatus.NONE;
		this.formHandler = formHandler;
	}

	/**
	 * {@inheritDoc}
	 */
	public void init(final Shell splash) {
		// Position the shell in the center of the display
		Rectangle displayArea = splash.getDisplay().getPrimaryMonitor().getBounds();
		splash.setSize(450,300);
		Point shellSize = splash.getSize();
		int x = (displayArea.width - shellSize.x) / 2;
		int y = (displayArea.height - shellSize.y) / 2;
		splash.setLocation(x, y);

		// Store the shell
		super.init(splash);

		// Configure the shell layout
		configureSplash();
		// Create UI
		composite = new Composite(splash, SWT.NONE);
		resourceManager = new LocalResourceManager(JFaceResources.getResources(), composite);
		createUI(composite);
		// Create UI listeners
		createUIListeners();
		// Force the splash screen to layout
		splash.layout(true);
		// Keep the splash screen visible and prevent the RCP application from
		// loading until the close button is clicked.
		splash.open();
		splash.forceActive();
		setFocus();
		doEventLoop();
	}

	/**
	 * Returns the local resource manager.
	 * @return the local resource manager
	 */
	protected LocalResourceManager getResourceManager() {
		return resourceManager;
	}

	/**
	 * Loops until the dialog has finished.
	 */
	@SuppressWarnings("incomplete-switch")
	private void doEventLoop() {
		Shell splash = getSplash();
		while ((splashStatus != SplashStatus.SUCCESS) && (splashStatus != SplashStatus.CANCELLED)) {
			if (splash.getDisplay().readAndDispatch() == false) {
				switch (splashStatus) {
				case SUCCESS:
					loginSuccess();
					break;
				case ERROR:
					loginFailure();
				}
				splash.getDisplay().sleep();
			}
		}
	}

	/**
	 * Sets the focus in the password field.
	 * Override if you need to change this.
	 */
	protected void setFocus() {
		passwordText.forceFocus();
	}

	/**
	 * Stops the event loop and hides the splash.
	 */
	protected void loginSuccess() {
		if (isCheckProgress()) {
			toggleCheckProgress(false);
			toggleError(false);
			composite.setVisible(false);
		}
	}

	/**
	 * Shows the error message
	 */
	private void loginFailure() {
		if (isCheckProgress()) {
			toggleCheckProgress(false);
			toggleError(true);
			errorLabel.setText(errorText+"    "); //NON-NLS-1 //$NON-NLS-1$
			setControlsEnabled(true);
		}
	}

	/**
	 * Creates the listeners to the UI.
	 */
	private void createUIListeners() {
		// Create the OK button listeners
		createOkButtonListeners();
		// Create the cancel button listeners
		createCancelButtonListeners();
		// Create listener to press ok on return
		createPasswordListeners();
	}

	/**
	 * Creates a traverse listener on the field
	 * to trigger OK when the user hits ENTER.
	 * @param text text field to listen for.
	 */
	protected void createOkListener(Text text) {
		text.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				if( e.detail == SWT.TRAVERSE_RETURN ) {
					handleOk();
				}
			}

		});
	}

	/**
	 * Adds listeners for the password field
	 */
	protected void createPasswordListeners() {
		createOkListener(passwordText);
	}

	/**
	 * Adds a listener to the button that triggers the
	 * CANCEL action. 
	 * @param button button
	 */
	protected void addCancelSelectionListener(Button button) {
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleCancel();
			}
		});
	}

	/**
	 * Adds listeners for the cancel button.
	 */
	private void createCancelButtonListeners() {
		addCancelSelectionListener(cancelButton);
	}

	/**
	 * Hides the splash and exits the application.
	 */
	private void handleCancel() {
		// Abort the loading of the RCP application
		getSplash().getDisplay().close();
		System.exit(0);
	}

	/**
	 * Adds a listener to the button that triggers the
	 * OK action. 
	 * @param button button
	 */
	protected void addOkSelectionListener(Button button) {
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleOk();
			}
		});
	}

	/**
	 * Adds listeners for the OK button.
	 */
	private void createOkButtonListeners() {
		addOkSelectionListener(okButton);
	}

	/**
	 * Handles the OK button.
	 */
	protected void handleOk() {
		initFormHandler(formHandler);
		setControlsEnabled(false);
		toggleCheckProgress(true);
		toggleError(false);
		splashStatus = SplashStatus.NONE;

		Thread t = new Thread() {

			@Override
			public void run() {
				IProgressMonitor monitor = new ProgressBarMonitor(progressBar, progressLabel);
				IStatus status = formHandler.run(monitor);	
				switch (status.getSeverity()) {
				case IStatus.OK:
					splashStatus = SplashStatus.SUCCESS;
					break;
				case IStatus.ERROR:
					splashStatus = SplashStatus.ERROR;
					errorText = status.getMessage();
					break;
				}
				getSplash().getDisplay().wake();
			}

		};
		t.start();
	}

	/**
	 * Initializes the form handler with form values so it can start running.
	 * @param formHandler
	 */
	protected void initFormHandler(ISplashFormHandler formHandler) {
		formHandler.setSplashProperty(ISplashFormHandler.PROPERTY_PASSWORD, passwordText.getText());
	}

	/**
	 * Sets the dialog controls enabled/disabled.
	 * @param enabled
	 */
	private void setControlsEnabled(final boolean enabled) {
		getSplash().getDisplay().syncExec(new Runnable() {
			public void run() {
				if (passwordText.isDisposed()) return;
				setEnabled(enabled);
			}
		});
	}

	/**
	 * Sets the dialog controls enabled/disabled.
	 * @param enabled
	 */
	protected void setEnabled(boolean enabled) {
		passwordText.setEnabled(enabled);
		okButton.setEnabled(enabled);
		cancelButton.setEnabled(enabled);
	}

	/**
	 * Creates the UI.
	 */
	protected abstract void createUI(Composite parent);


	/**
	 * Creates the progress label.
	 */
	protected Label createProgressLabel(Composite composite) {
		progressLabel = new Label(composite, SWT.NONE);
		progressLabel.setText(""); //$NON-NLS-1$
		progressLabel.setVisible(false);
		return progressLabel;
	}

	/**
	 * Creates the progress bar.
	 * @param composite
	 * @return
	 */
	protected ProgressBar createProgressBar(Composite composite) {
		progressBar = new ProgressBar(composite, SWT.NONE);
		progressBar.setVisible(false);
		return progressBar;
	}

	/**
	 * Creates the error info.
	 */
	protected Label createErrorInfo(Composite errorInfo) {
		errorLabel = new Label(composite, SWT.NONE);
		errorLabel.setText(""); //$NON-NLS-1$
		errorLabel.setForeground(getResourceManager().createColor(new RGB(192,0,0)));
		errorLabel.setVisible(false);
		return errorLabel;
	}

	/**
	 * Makes the progress info visible/invisible.
	 * @param state
	 */
	protected void toggleCheckProgress(boolean state) {
		progressLabel.setVisible(state);
		progressBar.setVisible(state);
		composite.layout();
	}

	/**
	 * Returns true when the checking process is running.
	 * @return
	 */
	protected boolean isCheckProgress() {
		if (progressLabel == null) return false;
		return progressLabel.isVisible();
	}
	/**
	 * Makes the error info visible/invisible.
	 * @param state
	 */
	protected void toggleError(boolean state) {
		errorLabel.setVisible(state);
		composite.layout();
	}

	/**
	 * Creates the CANCEL button.
	 */
	protected Button createCancelButton(Composite composite) {
		// Create the button
		cancelButton = new Button(composite, SWT.PUSH);
		cancelButton.setText(Plugin.translate("splash.login.button.cancel"));
		return cancelButton;
	}

	/**
	 * Creates the OK button.
	 */
	protected Button createOkButton(Composite composite) {
		// Create the button
		okButton = new Button(composite, SWT.PUSH|SWT.TRANSPARENT);
		okButton.setText(Plugin.translate("splash.login.button.ok")); 
		return okButton;
	}

	/**
	 * Creates the password field.
	 */
	protected Text createPasswordText(Composite parent) {
		int style = SWT.PASSWORD | SWT.BORDER;
		passwordText = new Text(parent, style);
		passwordText.setText(""); //$NON-NLS-1$
		return passwordText;
	}

	/**
	 * Configures the splash window.
	 */
	protected void configureSplash() {
		// Configure layout
		FillLayout layout = new FillLayout();
		getSplash().setLayout(layout);
		// Force shell to inherit the splash background
		getSplash().setBackgroundMode(SWT.INHERIT_DEFAULT);
	}

} 

