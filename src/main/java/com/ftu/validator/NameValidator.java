package com.ftu.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

@FacesValidator("nameValidator")
public class NameValidator implements Validator {
	
	private LanguageBean languageBean;
	private Pattern pattern;
	private Matcher matcher;

	public NameValidator(){
		FacesContext fc = FacesContext.getCurrentInstance();
        languageBean = (LanguageBean) fc.getApplication().evaluateExpressionGet(fc, "#{languageBean}",
                LanguageBean.class);
	}
	
	private FacesMessage getFacesMessage(String strLabel, Severity strSeverity) {
		FacesMessage msg = new FacesMessage(languageBean.getMessage("validator.message.header", null, false),
				languageBean.getMessage("validator.message.cmnd.content",new String[]{strLabel}, true));
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
		// pattern =
		// Pattern.compile(MessageProvider.getValue("common.patten.validate.name"));
		pattern = Pattern.compile("^(a-z|A-Z|0-9)*[^@!~`#$%^&*(){}:;|<>?/\'\"]*$");
		matcher = pattern.matcher(hex);
		return matcher.matches();
	}
}
