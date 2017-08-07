package com.ftu.admin.controller;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.primefaces.context.RequestContext;

import com.ftu.admin.component.FM;
import com.ftu.admin.consumer.AuthenticationConsumer;
import com.ftu.admin.consumer.entity.AuthenticationOutput;
import com.ftu.inventory.bean.SessionBean;
import com.ftu.utils.StringUtil;
import com.ftu.utils.resourceBundle.MessageProvider;

@ManagedBean(name = "passwordController")
@ViewScoped
public class PasswordController implements Serializable, Validator {

	private String oldPassword;
	private String newPassword;
	private String reEnterPassword;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getReEnterPassword() {
		return reEnterPassword;
	}

	public void setReEnterPassword(String reEnterPassword) {
		this.reEnterPassword = reEnterPassword;
	}

	public PasswordController() {
		System.out.println("again");
	}

	public void changePassword() {
		AuthenticationOutput authenOutput = null;
		try {
			authenOutput = AuthenticationConsumer.changePassword(SessionBean.getTransEntity(), oldPassword,
					newPassword);
			String code = authenOutput.getErrorCode();
			String message = authenOutput.getErrorMessage();

			if (!StringUtil.stringIsNullOrEmty(code) && !StringUtil.stringIsNullOrEmty(message)) {
				FM.showInfoGrowlMessage(MessageProvider.getValue("common.dialog.message.title"), code + ": " + message);

//				RequestContext.getCurrentInstance().update("confirmMessages");
			} else {
				RequestContext.getCurrentInstance().execute("PF('dlgChangePassword').hide() ");
				RequestContext.getCurrentInstance().execute("PF('showSuccess').show() ");
				if(SessionBean.getTransEntity().getChanglePass()){
					SessionBean.getTransEntity().setChanglePass(false);
//					FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void validate(FacesContext facesContext, UIComponent uiComponent, Object object) throws ValidatorException {
		UIInput newPasswordField = (UIInput) facesContext.getViewRoot().findComponent("frmChangePwd:newPassword");
		UIInput oldPasswordField = (UIInput) facesContext.getViewRoot().findComponent("frmChangePwd:oldPassword");
		UIInput newPasswordAgainField = (UIInput) facesContext.getViewRoot()
				.findComponent("frmChangePwd:reEnterPassword");

		if (newPasswordField == null || oldPasswordField == null || newPasswordAgainField == null) {
			return;
		}

		String value = (String) object;
		String kindOfValidate = uiComponent.getAttributes().get("kindOfValidate").toString();

		String oldPasswordValue = "";
		String newPasswordValue = "";
		String newPasswordAgain = "";

		AuthenticationOutput authenOutput = null;
		if ("validateOldPwd".equals(kindOfValidate)) {
			// validate old password here
			oldPasswordValue = value;

			try {
				authenOutput = AuthenticationConsumer.verifyPassword(SessionBean.getTransEntity(),
						SessionBean.getTransEntity().getUsername(), oldPasswordValue);
			} catch (Exception e) {
				e.printStackTrace();
			}

			String code = authenOutput.getErrorCode();
			String message = authenOutput.getErrorMessage();

			if (!StringUtil.stringIsNullOrEmty(code) && !StringUtil.stringIsNullOrEmty(message)) {
				FacesMessage msg = new FacesMessage(MessageProvider.getValue("common.dialog.message.title"),
						MessageProvider.getValue("page.caption.topbar-admin.dlgChangePassword.oldPassword.err"));
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
			}
		}

		if ("validateReEnterPwd".equals(kindOfValidate)) {
			// validate newPasswordAgain here
			newPasswordValue = (String) newPasswordField.getValue();
			newPasswordAgain = value;

			if (StringUtil.stringIsNullOrEmty(newPasswordValue) || StringUtil.stringIsNullOrEmty(newPasswordAgain)) {
				return;

			}

			if (!newPasswordValue.equals(newPasswordAgain)) {
				FacesMessage msg = new FacesMessage(MessageProvider.getValue("common.dialog.message.title"),
						MessageProvider.getValue(
								"page.caption.topbar-admin.dlgChangePassword.reEnterNewPassword.notEqualErr"));
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
			}
		}
	}
}