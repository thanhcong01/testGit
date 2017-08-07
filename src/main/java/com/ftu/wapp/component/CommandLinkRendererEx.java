package com.ftu.wapp.component;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.primefaces.component.commandlink.CommandLink;
import org.primefaces.component.commandlink.CommandLinkRenderer;

/**
 * @version n/a
 * @author haitx3
 */
public class CommandLinkRendererEx extends CommandLinkRenderer {

	public CommandLinkRendererEx() {
	}

	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		boolean isDisable = CommonRenderer.isDisable(context, component);
		if (isDisable) {
			((CommandLink) component).setDisabled(isDisable);
		}
		// super.encodeEnd(context, component);
	}

}
