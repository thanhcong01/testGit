package com.ftu.validator;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;

import com.ftu.admin.consumer.AuthorizationConsumer;
import com.ftu.admin.consumer.entity.AttributeEntity;
import com.ftu.admin.consumer.entity.AttributeOutput;
import com.ftu.admin.consumer.entity.SessionData;
import com.ftu.admin.consumer.utils.ConsumerPropeties;
import com.ftu.utils.StringUtil;
import com.ftu.utils.resourceBundle.MessageProvider;
//import com.ftu.wapp.resource.bundle.MessageProvider;
//import com.ftu.wapp.session.SessionBean;

@FacesValidator("passwordValidator")
public class PasswordValidator implements Validator {

	private Pattern pattern;
	private Matcher matcher;

	static FacesMessage msg;
	static String label;

	public static FacesMessage getFacesMessage(String strLabel, Severity strSeverity) {
		label = strLabel;
		msg.setSeverity(strSeverity);
		return msg;
	}

	@Override
	public void validate(FacesContext facesContext, UIComponent uiComponent, Object object) throws ValidatorException {
		boolean valid = false;
		Object value = object;

		if (!StringUtil.stringIsNullOrEmty(value)) {
			valid = validate(value.toString().trim());
			if (!valid) {
				label = (uiComponent.getAttributes().get("msglabel") != null)
						? uiComponent.getAttributes().get("msglabel").toString()
						: MessageProvider.getValue("common.data.undefined");
				FacesMessage msg = getFacesMessage(label, FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
			}
		}
	}

	public static FacesMessage checkValidate(String strLabel, Object value, Severity strSeverity) {
		if (!StringUtil.stringIsNullOrEmty(value)) {
			PasswordValidator validator = new PasswordValidator();
			boolean valid = validator.validate(value.toString().trim());
			if (!valid) {
				label = !StringUtil.stringIsNullOrEmty(strLabel) ? strLabel
						: MessageProvider.getValue("common.data.undefined");
				FacesMessage msg = getFacesMessage(label, FacesMessage.SEVERITY_ERROR);
				return msg;
			}
		}
		return null;
	}

	public static boolean checkValidate(Object value) {
		if (!StringUtil.stringIsNullOrEmty(value)) {
			PasswordValidator validator = new PasswordValidator();
			return validator.validate(value.toString().trim());
		}
		return true;
	}

//	public boolean validate(String hex) {
//		try {
//			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
//					.getSession(false);
//			SessionData data = (SessionData) session.getAttribute("username");
//			AttributeOutput output = AuthorizationConsumer.getAttributesByApp(data.getTransEntity(),
//					ConsumerPropeties.getProperty("consumer.app"));
//			List<AttributeEntity> lst = output.getAttributeEntities();
//
//			int length = 0;
//			String strPattern = "";
//			int strong = 0;
//			for (AttributeEntity attributeEntity : lst) {
//				if (attributeEntity.getCode().equals("POLICY_PASSWORD_MIN") && attributeEntity.getValue() != null && !attributeEntity.getValue().isEmpty()) {
//					length= Integer.parseInt(attributeEntity.getValue().trim()) ;
//				}
//				if (attributeEntity.getCode().equals("POLICY_PASSWORD_STRONG") && attributeEntity.getValue() != null) {
//					strong = Integer.valueOf(attributeEntity.getValue());
//				}
//			}
//
//			for (AttributeEntity attributeEntity : lst) {
//				if (attributeEntity.getCode().equals("POLICY_PASSWORD_PATTERN") && attributeEntity.getValue() != null) {
//					strPattern = attributeEntity.getValue();
//				}
//				if (attributeEntity.getCode().equals("POLICY_PASSWORD_STRONG") && attributeEntity.getValue() != null
//						&& strong > 0) {
//					msg = new FacesMessage(MessageProvider.getValue("common.dialog.message.title"),
//							MessageProvider.getValue("common.data.msg.invalid.pass-length.args", label, length));
//				}
//				if (strong == 0) {
//					if (attributeEntity.getCode().equals("POLICY_PASSWORD_MIN") && attributeEntity.getValue() != null
//							&& length <= 0) {
////						length = Integer.valueOf(attributeEntity.getValue());
//						msg = new FacesMessage(MessageProvider.getValue("common.dialog.message.title"),
//								MessageProvider.getValue("common.data.msg.invalid.pass-length.args", label, length));
//					}
//				}
//			}
//
//			if (strong == 1) {
//				pattern = Pattern.compile(strPattern);
//				Matcher m = pattern.matcher(hex);
//				if (!m.matches()) {
//					return false;
//				}
//			}
//
//			if (hex.length() < length) {
//				return false;
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return true;
//	}


	public boolean validate(String hex) {
		try {

			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
					.getSession(false);
			SessionData data = (SessionData) session.getAttribute("username");
			AttributeOutput output = AuthorizationConsumer.getAttributesByApp(data.getTransEntity(),
					ConsumerPropeties.getProperty("consumer.app"));
//			AttributeOutput output = AuthorizationConsumer.getAttributesByApp(SessionBean.getTransEntity(),
//					ResourceBundleUtil.getString("consumer.app"));
			List<AttributeEntity> lst = output.getAttributeEntities();

			int length = 0;
			String strPattern = "";
			int strong = 0;
			for (AttributeEntity attributeEntity : lst) {
				if (attributeEntity.getCode().equals("POLICY_PASSWORD_MIN") && attributeEntity.getValue() != null && !attributeEntity.getValue().isEmpty()) {
					length= Integer.parseInt(attributeEntity.getValue().trim()) ;
				}
			}
			for (AttributeEntity attributeEntity : lst) {
				if (attributeEntity.getCode().equals("POLICY_PASSWORD_MIN") && attributeEntity.getValue() != null
						&& length <= 0) {
					length = Integer.valueOf(attributeEntity.getValue());
					msg = new FacesMessage(MessageProvider.getValue("common.dialog.message.title"),
							MessageProvider.getValue("common.data.msg.invalid.pass-length.args", length));
				}
				if (attributeEntity.getCode().equals("POLICY_PASSWORD_PATTERN") && attributeEntity.getValue() != null) {
					strPattern = attributeEntity.getValue();
				}
				if (attributeEntity.getCode().equals("POLICY_PASSWORD_STRONG") && attributeEntity.getValue() != null
						&& strong <= 0) {
					strong = Integer.valueOf(attributeEntity.getValue());
					msg = new FacesMessage(MessageProvider.getValue("common.dialog.message.title"),
							MessageProvider.getValue("common.data.msg.invalid.pass-strong.args", length));
				}
			}

			if (hex.length() < length) {
				return false;
			}

			if (strong == 1) {
				// pattern = Pattern.compile(strPattern);
				// matcher = pattern.matcher(hex);
				// if (matcher.matches() == false) {
				// return false;
				// }
				if (!hex.matches(strPattern)) {
					return false;

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public static void main(String[] a) {
		// String p =
		// "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}";
		String p = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{6,}$";
		System.out.println("Rydf50%".matches(p));
		System.out.println("rydfrr@".matches(p));

	}
}
