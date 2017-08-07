package com.ftu.wapp.component;

import java.io.IOException;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.primefaces.component.menu.Menu;
import org.primefaces.component.menu.MenuRenderer;
import org.primefaces.component.menuitem.UIMenuItem;
import org.primefaces.model.menu.MenuElement;
/**
 * @version n/a
 * @author haitx3
 */
public class CommandMenuRendererEx extends MenuRenderer {

	public CommandMenuRendererEx() {
	}

	@Override
	protected void encodeElements(FacesContext context, Menu menu, List<MenuElement> menuElements) throws IOException {
		for (UIComponent component : menu.getChildren()) {
			for (String key : component.getAttributes().keySet()) {
				if (key.equals("PRIVILEGE")) {
					boolean isDisable = CommonRenderer.isDisable(context, component);
					if (isDisable) {
						((UIMenuItem) component).setDisabled(isDisable);
					}
				}
			}
		}
		super.encodeElements(context, menu, menuElements);
	}
}
