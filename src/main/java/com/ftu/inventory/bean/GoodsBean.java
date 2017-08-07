package com.ftu.inventory.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.primefaces.context.RequestContext;
import org.primefaces.model.TreeNode;

import com.ftu.admin.consumer.utils.StringUtil;
import com.ftu.inventory.bo.EquipmentsProfile;
import com.ftu.inventory.bo.EquipmentsGroup;
import com.ftu.java.business.EquipmentsGroupService;
import com.ftu.java.business.GoodService;
import com.ftu.language.LanguageBean;
import com.ftu.sm.bean.GoodsPriceBean;
import com.ftu.staff.bo.ApDomain;
import com.ftu.staff.bussiness.AppDomainService;

@ManagedBean
@ViewScoped
public class GoodsBean implements Serializable, Validator {
	private GoodService service = new GoodService();
	private EquipmentsGroupService groupService = new EquipmentsGroupService();
	private AppDomainService domainService = new AppDomainService();
	public String TYPE_EDIT = "edit";
	public String TYPE_ADD = "add";
	public String TYPE_SEARCH = "search";
	private String type;
	private List<EquipmentsProfile> listGoods;
	private EquipmentsProfile goodSelected = new EquipmentsProfile();
	@ManagedProperty("#{languageBean}")
	private LanguageBean languageBean;
	@ManagedProperty("#{goodsAllBean}")
	private GoodsAllBean bean;
	private List<EquipmentsGroup> listGroups;
	private List<ApDomain> listDomains;
	private List<EquipmentsProfile> listGoodsSelected;

	@PostConstruct
	public void init() {
		loadAll();
	}

	public List<EquipmentsProfile> getListGoodsSelected() {
		return listGoodsSelected;
	}

	public void setListGoodsSelected(List<EquipmentsProfile> listGoodsSelected) {
		this.listGoodsSelected = listGoodsSelected;
	}

	public List<EquipmentsGroup> getListGroups() {
		return listGroups;
	}

	public void setListGroups(List<EquipmentsGroup> listGroups) {
		this.listGroups = listGroups;
	}

	public List<ApDomain> getListDomains() {
		return listDomains;
	}

	public void setListDomains(List<ApDomain> listDomains) {
		this.listDomains = listDomains;
	}

	public GoodsAllBean getBean() {
		return bean;
	}

	public void setBean(GoodsAllBean bean) {
		this.bean = bean;
	}

	public EquipmentsProfile getGoodSelected() {
		return goodSelected;
	}

	public void setGoodSelected(EquipmentsProfile goodSelected) {
		this.goodSelected = goodSelected;
	}

	public List<EquipmentsProfile> getListGoods() {
		return listGoods;
	}

	public void setListGoods(List<EquipmentsProfile> listGoods) {
		this.listGoods = listGoods;
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

	private void loadDomain() {
		ApDomain domain = new ApDomain();
		domain.setType("MANAGEMENT_TYPE");
		listDomains = domainService.getAllDomains(domain, false);
		listGroups = groupService.getAllEquipmentsGroup(null, false, -1, -1);
	}

	public void loadGoods() {
		TreeNode node = bean.getSelectedNode();
		Long id = null;
		if (node != null) {
			Object object = node.getData();
			if (object != null) {
				if (object instanceof EquipmentsProfile) {
					EquipmentsProfile tmp = (EquipmentsProfile) object;
					id = tmp.getEquipmentsGroupId();

				} else if (object instanceof EquipmentsGroup) {
					EquipmentsGroup tmp = (EquipmentsGroup) object;
					id = tmp.getEquipmentsGroupId();
				}
			}
		}
		EquipmentsProfile g = new EquipmentsProfile();
		g.setEquipmentsGroupId(id);
		listGoods = service.getAllGoods(g, false, -1, -1);
	}

	public void loadAll() {
		loadGoods();
		loadDomain();
	}

	private void setSelectedTree(Object goods) {
		if (goods != null) {
			bean.setSelectedTreeNode(goods);
		}
	}

	public void onRowSelect() {
		// setSelectedTree(goodSelected);
	}

	private void loadListGoods() {
		FacesContext fc = FacesContext.getCurrentInstance();
		GoodsPriceBean goodsPriceBean = (GoodsPriceBean) fc.getApplication().evaluateExpressionGet(fc,
				"#{goodsPriceBean}", GoodsPriceBean.class);
		goodsPriceBean.setListGoods(service.getAllGoods(null, false, -1, -1));
	}

	public void save() {
		FacesMessage message = null;
		try {
			goodSelected.setProfileCode(goodSelected.getProfileCode().toUpperCase().trim());
			service.saveOrUpdate(goodSelected);
			loadGoods();
			bean.reinit();
			loadListGoods();
			setSelectedTree(goodSelected);
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
			for (EquipmentsProfile g : listGoodsSelected) {
				service.delete(g);
			}
			loadGoods();
			bean.reinit();
			loadListGoods();
			setSelectedTree(listGroups.get(0));
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
		listGoods = service.getAllGoods(goodSelected, true, -1, -1);
	}

	public void beforeDelete() {

		FacesMessage message = null;
		if (listGoodsSelected == null || listGoodsSelected.isEmpty()) {
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false), 
					languageBean.getMessage("common.action.beforedelete", null, false));
			RequestContext context = RequestContext.getCurrentInstance();
			context.showMessageInDialog(message);
		} else {
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("document.getElementById('tv:frmG:confirmButtonG').click(); ");
		}
	}

	public void beforeSave() {

		FacesMessage message = null;
		if (listGoodsSelected == null||listGoodsSelected.isEmpty()) {
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false), 
					languageBean.getMessage("common.acction.beforesave", null, false));
			RequestContext context = RequestContext.getCurrentInstance();
			context.showMessageInDialog(message);
		} else {
			if (listGoodsSelected.size() > 1) {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, languageBean.getMessage("common.confirm.header", null, false),
						languageBean.getMessage("common.action.beforesave.multiplerecord", null, false));
				RequestContext context = RequestContext.getCurrentInstance();
				context.showMessageInDialog(message);
			} else {
				goodSelected = listGoodsSelected.get(0);
				RequestContext context = RequestContext.getCurrentInstance();
				context.execute("PF('dlSaveG').show();");
			}
		}
	}

	public EquipmentsProfile reset() {
		goodSelected = new EquipmentsProfile();
		return goodSelected;
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
						languageBean.getMessage("goods.validator.code", null, false));
			}
			if (!valid) {
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
			}
		}
	}

	private boolean validate(String str) {
		try {
			EquipmentsProfile st = service.checkExists(str.trim());
			if (st != null) {
				if (!st.getProfileId().equals(goodSelected.getProfileId())) {
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
