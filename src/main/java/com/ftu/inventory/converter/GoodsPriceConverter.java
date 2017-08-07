package com.ftu.inventory.converter;

import java.util.List;

import javax.el.ELContext;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.ftu.admin.consumer.utils.StringUtil;
import com.ftu.inventory.bo.EquipmentsProfile;
import com.ftu.sm.bean.GoodsPriceBean;
import com.ftu.sm.bo.GoodsPrice;

@FacesConverter(value = "gpConverter")
public class GoodsPriceConverter implements Converter {
	private ELContext elContext;
	private GoodsPriceBean pc;
	public List<EquipmentsProfile> listGoods;

	public GoodsPriceConverter() {
		elContext = FacesContext.getCurrentInstance().getELContext();
		pc = (GoodsPriceBean) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext,
				null, "goodsPriceBean");
		listGoods = pc.getListGoods();
	}

	public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
		if (StringUtil.stringIsNullOrEmty(submittedValue)) {
			return null;
		} else {
			if (listGoods != null && !listGoods.isEmpty())
				for (EquipmentsProfile t : listGoods) {
					if (t.getProfileName().equalsIgnoreCase(submittedValue)) {
						return t;
					}
				}
		}
		return null;
	}

	public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
		if (value.equals(null)) {
			return "";
		} else {
			EquipmentsProfile g = ((EquipmentsProfile) value);
			String data = g.getProfileName();
			if (StringUtil.stringIsNullOrEmty(data)) {
				return "";
			} else {
				return data;
			}
		}
	}
}
