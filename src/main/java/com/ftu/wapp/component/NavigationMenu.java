package com.ftu.wapp.component;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class NavigationMenu {
	public String menuActive;
	
	public String setStrActive(String a) {
		menuActive = a+".xhtml";
		System.out.println(menuActive);
		return menuActive;
	}
}
