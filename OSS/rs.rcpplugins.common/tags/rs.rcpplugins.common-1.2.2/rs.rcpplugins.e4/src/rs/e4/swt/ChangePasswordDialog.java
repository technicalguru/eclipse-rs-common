/**
 * 
 */
package rs.e4.swt;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.slf4j.LoggerFactory;

import rs.e4.Plugin;
import rs.e4.SwtUtils;

/**
 * A dialog for changing passwords.
 * @author ralph
 *
 */
public class ChangePasswordDialog extends TitleAreaDialog {

	private IPasswordChangeRunnable runnable;
	
	private Text oldPasswordText;
	private Text newPasswordText1;
	private Text newPasswordText2;
	
	/**
	 * Constructor.
	 */
	public ChangePasswordDialog(Shell shell, IPasswordChangeRunnable runnable) {
		super(shell);
		this.runnable = runnable;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText(Plugin.translate("changepassword.title"));
		setTitle(Plugin.translate("changepassword.title"));
		Composite area = (Composite) super.createDialogArea(parent);

		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		if (runnable.isCheckingOldPassword()) {
			oldPasswordText = createText(container, "changepassword.oldpasswd.label");
		}
		newPasswordText1 = createText(container, "changepassword.newpasswd1.label");
		newPasswordText2 = createText(container, "changepassword.newpasswd2.label");
		return parent;
	}

	protected Text createText(Composite parent, String labelKey) {
		Label l = new Label(parent, SWT.LEFT);
		l.setText(Plugin.translate(labelKey));
		GridData gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		gd.verticalIndent = 5;
		l.setLayoutData(gd);
		
		Text rc = new Text(parent, SWT.PASSWORD|SWT.BORDER);
		rc.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		rc.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				checkButtons();
			}
		});
		return rc;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		getButton(IDialogConstants.OK_ID).setEnabled(false);
	}

	protected void checkButtons() {
		boolean doEnable = true;
		String oldPwd = null;
		
		if (oldPasswordText != null) {
			boolean old = !SwtUtils.isTextEmpty(oldPasswordText);
			doEnable &= old;
			if (old) oldPwd = new String(oldPasswordText.getTextChars());
		}
		boolean new1 = !SwtUtils.isTextEmpty(newPasswordText1); 
		boolean new2 = !SwtUtils.isTextEmpty(newPasswordText2);
		
		doEnable &= new1 & new2;
		if (new1 & new2) {
			String s1 = new String(newPasswordText1.getTextChars());
			String s2 = new String(newPasswordText2.getTextChars());
			if (!s1.equals(s2)) {
				doEnable = false;
				setErrorMessage(Plugin.translate("changepassword.error.mismatch"));
			} else if ((oldPwd != null) && oldPwd.equals(s1)) {
				setMessage(Plugin.translate("changepassword.error.same"), IMessageProvider.WARNING);
				setErrorMessage(null);
			} else {
				setMessage(null, IMessageProvider.WARNING);
				setErrorMessage(null);
			}
		}
		
		if (doEnable) {
		}
		getButton(IDialogConstants.OK_ID).setEnabled(doEnable);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void okPressed() {
		if (oldPasswordText != null) {
			char passwd[] = oldPasswordText.getTextChars();
			if (!runnable.checkOldPassword(passwd)) {
				MessageDialog.openError(getShell(), Plugin.translate("error.title"), Plugin.translate("changepassword.error.invalidold"));
				return;
			}
		}
		char newPasswd[] = newPasswordText1.getTextChars();
		try {
			String error = runnable.setNewPassword(newPasswd); 
			if (error != null) {
				MessageDialog.openError(getShell(), Plugin.translate("error.title"), Plugin.translate("changepassword.error.cannotset", error));
				return;
			}
		} catch (Exception e) {
			LoggerFactory.getLogger(getClass()).error("Cannot change password", e);
			MessageDialog.openError(getShell(), Plugin.translate("error.title"), Plugin.translate("changepassword.error.exception", e.getLocalizedMessage()));
			return;
		}
		MessageDialog.openInformation(getShell(), Plugin.translate("changepassword.ok.title"), Plugin.translate("changepassword.ok.message"));
		super.okPressed();
	}

}
