package com.ftu.inventory.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.primefaces.context.RequestContext;

import com.ftu.admin.consumer.utils.StringUtil;
import com.ftu.inventory.bo.EquipmentsProfile;
import com.ftu.inventory.bo.EquipmentsGroup;
import com.ftu.java.business.EquipmentsGroupService;
import com.ftu.language.LanguageBean;

@ManagedBean
@ViewScoped
public class GoodsGroupBean implements Serializable, Validator {
	private EquipmentsGroupService service = new EquipmentsGroupService();
	@ManagedProperty("#{goodsAllBean}")
	private GoodsAllBean bean;
	public String TYPE_EDIT = "edit";
	public String TYPE_ADD = "add";
	public String TYPE_SEARCH = "search";
	private String type;
	private List<EquipmentsGroup> listGoodsGroup;
	private EquipmentsGroup groupSelected = new EquipmentsGroup();
	@ManagedProperty("#{languageBean}")
	private LanguageBean languageBean;

	private List<EquipmentsGroup> listGroupSelected;

	@PostConstruct
	public void init() {
		listGoodsGroup = service.getAllEquipmentsGroup(null, false, -1, -1);
	}

	public List<EquipmentsGroup> getListGroupSelected() {
		return listGroupSelected;
	}

	public void setListGroupSelected(List<EquipmentsGroup> listGroupSelected) {
		this.listGroupSelected = listGroupSelected;
	}

	public GoodsAllBean getBean() {
		return bean;
	}

	public void setBean(GoodsAllBean bean) {
		this.bean = bean;
	}

	public EquipmentsGroup getGroupSelected() {
		return groupSelected;
	}

	public void setGroupSelected(EquipmentsGroup groupSelected) {
		this.groupSelected = groupSelected;
	}

	public List<EquipmentsGroup> getListGoodsGroup() {
		return listGoodsGroup;
	}

	public void setListGoodsGroup(List<EquipmentsGroup> listGoodsGroup) {
		this.listGoodsGroup = listGoodsGroup;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTYPE_ADD() {
		return TYPE_ADD;
	}

	public void setTYPE_ADD(String tYPE_ADD) {
		TYPE_ADD = tYPE_ADD;
	}

	public String getTYPE_EDIT() {
		return TYPE_EDIT;
	}

	public void setTYPE_EDIT(String tYPE_EDIT) {
		TYPE_EDIT = tYPE_EDIT;
	}

	public String getTYPE_SEARCH() {
		return TYPE_SEARCH;
	}

	public void setTYPE_SEARCH(String tYPE_SEARCH) {
		TYPE_SEARCH = tYPE_SEARCH;
	}

	public LanguageBean getLanguageBean() {
		return languageBean;
	}

	public void setLanguageBean(LanguageBean languageBean) {
		this.languageBean = languageBean;
	}

	public void onRowSelect() {

	}

	private void loadListGroup() {
		FacesContext fc = FacesContext.getCurrentInstance();
		GoodsBean goodsBean = (GoodsBean) fc.getApplication().evaluateExpressionGet(fc, "#{goodsBean}",
				GoodsBean.class);
		goodsBean.setListGroups(service.getAllEquipmentsGroup(null, false, -1, -1));
	}

	public void save() {
		FacesMessage message = null;
		try {
			groupSelected.setEquipmentsGroupCode(groupSelected.getEquipmentsGroupCode().toUpperCase().trim());
			service.saveOrUpdate(groupSelected);
			listGoodsGroup = service.getAllEquipmentsGroup(null, false, -1, -1);
			bean.reinit();
			loadListGroup();
			if (type.equals(TYPE_ADD)) {
				reset();
			}
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false), 
			languageBean.getMessage("common.action.save.success", null, false));
		} catch (Exception ex) {
			ex.printStackTrace();
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, languageBean.getMessage("common.confirm.header", null, false), 
			languageBean.getMessage("common.action.save.error", null, false));
		}
		RequestContext.getCurrentInstance().showMessageInDialog(message);
	}

	public void delete() {
		FacesMessage message = null;
		try {
			for (EquipmentsGroup gg : listGroupSelected) {
				service.delete(gg);
			}
			listGoodsGroup = service.getAllEquipmentsGroup(null, false, -1, -1);
			bean.reinit();
			loadListGroup();
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false), 
			languageBean.getMessage("common.action.delete.success", null, false));
		} catch (Exception e) {
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, languageBean.getMessage("common.confirm.header", null, false),
					languageBean.getMessage("common.action.delete.relationerror", null, false));
		} finally {

		}
		RequestContext.getCurrentInstance().showMessageInDialog(message);
	}

	public void filterSearch() {
		listGoodsGroup = service.getAllEquipmentsGroup(groupSelected, true, -1, -1);
	}

	public void beforeDelete() {

		FacesMessage message = null;
		if (listGroupSelected == null || listGroupSelected.isEmpty()) {
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false), 
			languageBean.getMessage("common.action.beforedelete", null, false));
			RequestContext context = RequestContext.getCurrentInstance();
			context.showMessageInDialog(message);
		} else {
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("document.getElementById('tv:frmGG:confirmButtonGG').click(); ");
		}
	}

	public void beforeSave() {

		FacesMessage message = null;
		if (listGroupSelected == null || listGroupSelected.isEmpty()) {
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false), 
			languageBean.getMessage("common.acction.beforesave", null, false));
			RequestContext context = RequestContext.getCurrentInstance();
			context.showMessageInDialog(message);
		} else {
			if (listGroupSelected.size() > 1) {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, languageBean.getMessage("common.confirm.header", null, false),
						languageBean.getMessage("common.action.beforesave.multiplerecord", null, false));
				RequestContext context = RequestContext.getCurrentInstance();
				context.showMessageInDialog(message);
			} else {
				groupSelected = listGroupSelected.get(0);
				RequestContext context = RequestContext.getCurrentInstance();
				context.execute("PF('dlSaveGG').show();");
			}
		}
	}

	public EquipmentsGroup reset() {
		groupSelected = new EquipmentsGroup();
		return groupSelected;
	}

	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object object) throws ValidatorException {
		// TODO Auto-generated method stub
		if (!type.equals(TYPE_SEARCH)) {
			boolean valid = true;
			if (StringUtil.stringIsNullOrEmty(object)) {
				return;
			}

			FacesMessage msg = null;
			String value = object.toString();
			valid = true;
			if (validate(value)) {
				valid = false;
				msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false), 
				languageBean.getMessage("goodsgroup.validator.code", null, false));
			}
			if (!valid) {
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
			}
		}
	}

	private boolean validate(String str) {
		try {
			EquipmentsGroup st = service.checkExists(str.trim());
			if (st != null) {
				if (!st.getEquipmentsGroupId().equals(groupSelected.getEquipmentsGroupId())) {
					return true;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
	}
}
