package com.ftu.wapp.component;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.commandbutton.CommandButtonRenderer;

/**
 * @version n/a
 * @author haitx3
 */
public class CommandButtonRendererEx extends CommandButtonRenderer {

	public CommandButtonRendererEx() {
	}

	// @Override
	// protected void encodeMarkup(FacesContext context, CommandButton
	// component) throws IOException {
	// if (component.getId().equals("deletePrivilege")) {
	// boolean isDisable = component.isDisabled();
	// if (!isDisable) {
	// isDisable = CommonRenderer.isDisable(context, component);
	// component.setDisabled(isDisable);
	// }
	// }
	// super.encodeMarkup(context, component);
	// }

	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {

		boolean isDisable = CommonRenderer.isDisable(context, component);
		if (isDisable) {
			((CommandButton) component).setDisabled(isDisable);
		}

	}

}
