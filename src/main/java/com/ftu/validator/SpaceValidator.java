package com.ftu.validator;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import com.ftu.admin.consumer.utils.StringUtil;
import com.ftu.language.LanguageBean;


@FacesValidator("spaceValidator")
public class SpaceValidator implements Validator {
	
	private LanguageBean languageBean;
	
	public SpaceValidator(){
		FacesContext fc = FacesContext.getCurrentInstance();
        languageBean = (LanguageBean) fc.getApplication().evaluateExpressionGet(fc, "#{languageBean}",
                LanguageBean.class);
	}
	
	private FacesMessage getFacesMessage(String strLabel, Severity strSeverity) {
		FacesMessage msg = new FacesMessage(languageBean.getMessage("validator.message.header", null, false),
				languageBean.getMessage("validator.message.space.content",new String[]{strLabel}, true));
		msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		return msg;
	}

	@Override
	public void validate(FacesContext facesContext, UIComponent uiComponent, Object object) throws ValidatorException {
		boolean valid = false;
		Object value = object;

		if (!StringUtil.stringIsNullOrEmty(value)) {
			valid = validate(value.toString().trim());
			if (!valid) {
				String label = (uiComponent.getAttributes().get("msglabel") != null) ? uiComponent.getAttributes()
						.get("msglabel").toString() : languageBean.getMessage("validator.message.indefine", null, false);
				FacesMessage msg = getFacesMessage(label, FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
			}
		}
	}

	public boolean validate(String hex) {
		return !(hex.indexOf(" ") >= 0);
	}
	
	public LanguageBean getLanguageBean() {
		return languageBean;
	}
	
	public void setLanguageBean(LanguageBean languageBean) {
		this.languageBean = languageBean;
	}
}
