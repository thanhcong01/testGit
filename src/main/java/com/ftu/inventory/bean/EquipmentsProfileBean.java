package com.ftu.inventory.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
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
import javax.servlet.http.HttpSession;

import com.ftu.admin.consumer.LogsConsumer;
import com.ftu.admin.consumer.entity.SessionData;
import com.ftu.constanst.InventoryConstanst;
import org.primefaces.context.RequestContext;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.TreeNode;

import com.ftu.admin.consumer.utils.StringUtil;
import com.ftu.inventory.bo.EquipmentsProfile;
import com.ftu.inventory.bo.EquipmentsGroup;
import com.ftu.java.business.EquipmentsGroupService;
import com.ftu.java.business.EquipmentsProfileService;
import com.ftu.language.LanguageBean;
import com.ftu.sm.bean.GoodsPriceBean;
import com.ftu.staff.bo.ApDomain;
import com.ftu.staff.bussiness.AppDomainService;
import org.primefaces.model.Visibility;

@ManagedBean
@ViewScoped
public class EquipmentsProfileBean implements Serializable, Validator {
	private EquipmentsProfileService service = new EquipmentsProfileService();
	private EquipmentsGroupService groupService = new EquipmentsGroupService();
	private AppDomainService domainService = new AppDomainService();
	public String TYPE_EDIT = "edit";
	public String TYPE_ADD = "add";
	public String TYPE_SEARCH = "search";
	private String type;
	private List<EquipmentsProfile> listGoods;
	private EquipmentsProfile profileSelected = new EquipmentsProfile();
	@ManagedProperty("#{languageBean}")
	private LanguageBean languageBean;
	@ManagedProperty("#{goodsAllBean}")
	private GoodsAllBean bean;
	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;
	private List<EquipmentsGroup> listGroups;
	private List<EquipmentsGroup> listGroupsAll;
	private List<ApDomain> listDomains;
	private List<ApDomain> listUnit;
	private List<ApDomain> listOrigin;
	private List<ApDomain> listManufactury;
	private List<EquipmentsProfile> listProfilesSelected;
	private String resourceLog = InventoryConstanst.RESOURCE_LOG.LOG_EQUIP;
	private EquipmentsProfile profileSelectedOld;
	@PostConstruct
	public void init() {
		loadAll();
	}

	public List<EquipmentsProfile> getListProfilesSelected() {
		return listProfilesSelected;
	}

	public void setListProfilesSelected(List<EquipmentsProfile> listProfilesSelected) {
		this.listProfilesSelected = listProfilesSelected;
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

	public EquipmentsProfile getProfileSelected() {
		return profileSelected;
	}

	public void setProfileSelected(EquipmentsProfile profileSelected) {
		this.profileSelected = profileSelected;
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
		domain.setType(InventoryConstanst.ApDomainType.MANTENANCE_TYPE);
//		domain.setStatus("1");
		listDomains = domainService.getAllDomains(domain, false);
		domain.setType(InventoryConstanst.ApDomainType.UNIT_TYPE);
		listUnit = domainService.getAllDomains(domain, false);
		domain.setType(InventoryConstanst.ApDomainType.ORIGIN_TYPE);
		listOrigin = domainService.getAllDomains(domain, false);
		domain.setType(InventoryConstanst.ApDomainType.MANUFACTURE_TYPE);
		listManufactury = domainService.getAllDomains(domain, false);
		EquipmentsGroup equipmentsGroup = new EquipmentsGroup();
		equipmentsGroup.setEquipmentsGroupStatus(1L);
		listGroups = groupService.getAllEquipmentsGroup(equipmentsGroup, false, -1, -1);
		listGroupsAll = sessionBean.getLsgGroup();
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
		listGoods = service.getAllProfile(g, false, -1, -1);
	}

	public void loadAll() {
		loadGoods();
		loadDomain();
	}

//	private void setSelectedTree(Object goods) {
//		if (goods != null) {
////			bean.setSelectedTreeNode(goods);
//		}
//	}

//	public void onRowSelect() {
//		// setSelectedTree(profileSelected);
//	}

	private void loadListGoods() {
		FacesContext fc = FacesContext.getCurrentInstance();
		GoodsPriceBean goodsPriceBean = (GoodsPriceBean) fc.getApplication().evaluateExpressionGet(fc,
				"#{goodsPriceBean}", GoodsPriceBean.class);
		goodsPriceBean.setListGoods(service.getAllProfile(null, false, -1, -1));
	}

	public void save() {
		FacesMessage message = null;
		try {
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
					.getSession(false);
			SessionData sessionData = (SessionData) session.getAttribute("username");
			profileSelected.setProfileCode(profileSelected.getProfileCode().toUpperCase().trim());
			if(profileSelected.getProfileId() == null){
				profileSelected.setEquipProfileStatus(1L);
			}
			service.saveOrUpdate(profileSelected);
			loadGoods();
			bean.reinit();
			loadListGoods();
//			setSelectedTree(profileSelected);
			if (type.equals(TYPE_ADD)) {
				LogsConsumer.logInsert(sessionData.getTransEntity(),
						new Object[] { profileSelected }, resourceLog, Calendar.getInstance().getTime());
				reset();
			}else {
				LogsConsumer.logUpdate(sessionData.getTransEntity(), new Object[] { profileSelectedOld },
						new Object[] {new EquipmentsProfile(profileSelected)}, resourceLog, Calendar.getInstance().getTime());
			}
			reset();
			listProfilesSelected.clear();
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false), 
					languageBean.getMessage("common.action.save.success", null, false));
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlSaveG').hide();");

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
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
					.getSession(false);
			SessionData sessionData = (SessionData) session.getAttribute("username");
			String msg = "";
			for (EquipmentsProfile g : listProfilesSelected) {
				if(sessionBean.getService().getQuantityByProfileId(g.getProfileId()) > 0L){
//					System.out.println("======================="+ sessionBean.getService().getQuantityByProfileId(g.getProfileId()));
					if(msg.isEmpty()){
						msg+= g.getProfileName();
					}else {
						msg+=", "+ g.getProfileName();
					}
				}
			}
			if(!msg.isEmpty()){
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false),
						languageBean.getMessage("equipment.equip.not.delete", new Object[]{msg}, true));
				RequestContext.getCurrentInstance().showMessageInDialog(message);
				return;
			}
			for (EquipmentsProfile g : listProfilesSelected) {
				profileSelectedOld = new EquipmentsProfile(g);
				g.setEquipProfileStatus(0L);
				service.saveOrUpdate(g);
//				LogsConsumer.logUpdate(sessionData.getTransEntity(), new Object[] { profileSelectedOld },
//						new Object[] {new EquipmentsProfile(g)}, resourceLog, Calendar.getInstance().getTime());
				LogsConsumer.logDelete(sessionData.getTransEntity(),
						new Object[] {new EquipmentsProfile(g)},resourceLog,Calendar.getInstance().getTime());
			}
			loadGoods();
			bean.reinit();
			reset();
			listProfilesSelected.clear();
			loadListGoods();
//			setSelectedTree(listGroups.get(0));
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
		listGoods = service.getAllProfile(profileSelected, true, -1, -1);
	}

	public void beforeDelete() {

		FacesMessage message = null;
		if (listProfilesSelected == null || listProfilesSelected.isEmpty()) {
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false), 
					languageBean.getMessage("common.action.beforedelete", null, false));
			RequestContext context = RequestContext.getCurrentInstance();
			context.showMessageInDialog(message);
		} else {
			String msg = "";
			for (EquipmentsProfile g : listProfilesSelected) {
				if(InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(g.getEquipProfileStatus())){
					if(msg.isEmpty()){
						msg+=g.getProfileName();
					}else {
						msg+= ", " + g.getProfileName();
					}
				}
			}
			if(!msg.isEmpty()){
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false),
						languageBean.getMessage("common.action.beforedelete.active.argu", new Object[]{msg}, true));
				RequestContext.getCurrentInstance().showMessageInDialog(message);
				return;
			}

			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("document.getElementById('frmG:confirmButtonG').click(); ");
		}
	}

	public void beforeSave() {

		FacesMessage message = null;
		if (listProfilesSelected == null||listProfilesSelected.isEmpty()) {
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false), 
					languageBean.getMessage("common.acction.beforesave", null, false));
			RequestContext context = RequestContext.getCurrentInstance();
			context.showMessageInDialog(message);
		} else {
			if (listProfilesSelected.size() > 1) {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, languageBean.getMessage("common.confirm.header", null, false),
						languageBean.getMessage("common.action.beforesave.multiplerecord", null, false));
				RequestContext context = RequestContext.getCurrentInstance();
				context.showMessageInDialog(message);
			} else {
				profileSelected = listProfilesSelected.get(0);
				profileSelectedOld = new EquipmentsProfile(profileSelected);
				RequestContext context = RequestContext.getCurrentInstance();
				context.execute("PF('dlSaveG').show();");
			}
		}
	}

	public EquipmentsProfile reset() {
		profileSelected = new EquipmentsProfile();
		return profileSelected;
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
			String value = object.toString().trim();
			valid = true;
			String id = arg1.getId();
			if (id.equals("codeG")) {
				if (validate(value)) {
					valid = false;
					msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false),
							languageBean.getMessage("goods.validator.code", null, false));
				}
			}
			if (id.equals("unit")) {
				if(value==null || value.isEmpty()){
					valid = false;
					msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false),
							languageBean.getMessage("profile.unit.entity", null, false));
				}else if(domainService.findByTypeAndValue(InventoryConstanst.ApDomainType.UNIT_TYPE,Long.parseLong(value)) == null) {
					valid = false;
					msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false),
							languageBean.getMessage("profile.unit.entity.in.active", null, false));
				}

			}
			if (id.equals("manufacture")) {
				if(value==null || value.isEmpty()){
					valid = false;
					msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false),
							languageBean.getMessage("manufacture.unit.entity", null, false));
				}else if(domainService.findByTypeAndValue(InventoryConstanst.ApDomainType.MANUFACTURE_TYPE,Long.parseLong(value)) == null) {
					valid = false;
					msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false),
							languageBean.getMessage("manufacture.unit.entity.in.active", null, false));
				}
			}
			if (id.equals("origin")) {
				if(value==null || value.isEmpty()){
					valid = false;
					msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false),
							languageBean.getMessage("origin.unit.entity", null, false));
				}else if(domainService.findByTypeAndValue(InventoryConstanst.ApDomainType.ORIGIN_TYPE,Long.parseLong(value)) == null) {
					valid = false;
					msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false),
							languageBean.getMessage("origin.unit.entity.in.active", null, false));
				}
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
				if (!st.getProfileId().equals(profileSelected.getProfileId())) {
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
//	public String getUnitName(Long value){
//		if(value==null) return "";
//		for (ApDomain app:listUnit) {
//			if(app.getValue().equals(value)){
//				return app.getName();
//			}
//		}
//		return "";
//	}
	public String getUnitName(String value){
		if(value==null || listUnit == null || listUnit.isEmpty()) return "";
		for (ApDomain app:listUnit) {
			if(value!=null&& !value.isEmpty()&& app.getValue().toString().equals(value)){
				return app.getName();
			}
		}
		return "";
	}

	public String getOriginName(String value){
		if(value==null || listOrigin==null || listOrigin.isEmpty()) return "";
		for (ApDomain app:listOrigin) {
			if(app.getValue() == null ) continue;
			if(app.getValue().toString().equals(value)){
				return app.getName();
			}
		}
		return value;
	}

//	public String getManufacturyName(Long value){
//		for (ApDomain app:listManufactury) {
//			if(app.getValue().equals(value)){
//				return app.getName();
//			}
//		}
//		return "";
//	}
	public String getManufacturyName(String value){
		if(value==null||value.isEmpty()||listManufactury ==null || listManufactury.isEmpty()) return "";
		for (ApDomain app:listManufactury) {
			if(app.getValue().toString().equals(value)){
				return app.getName();
			}
		}
		return value;
	}
	public List<ApDomain> getListUnit() {
		return listUnit;
	}

	public void setListUnit(List<ApDomain> listUnit) {
		this.listUnit = listUnit;
	}

	public List<ApDomain> getListOrigin() {
		return listOrigin;
	}

	public void setListOrigin(List<ApDomain> listOrigin) {
		this.listOrigin = listOrigin;
	}

	public List<ApDomain> getListManufactury() {
		return listManufactury;
	}

	public void setListManufactury(List<ApDomain> listManufactury) {
		this.listManufactury = listManufactury;
	}

	public SessionBean getSessionBean() {
		return sessionBean;
	}

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}

	public List<EquipmentsGroup> getListGroupsAll() {
		listGroupsAll = sessionBean.getLsgGroup();
		return listGroupsAll;
	}

	public void setListGroupsAll(List<EquipmentsGroup> listGroupsAll) {
		this.listGroupsAll = listGroupsAll;
	}

	public String getResourceLog() {
		return resourceLog;
	}

	public void setResourceLog(String resourceLog) {
		this.resourceLog = resourceLog;
	}
	public void onRowSelected() {
	}

	private List<Boolean> visibleTable = Arrays.asList(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
			true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true);
	public void onToggleActiveEtag(ToggleEvent e) {
		visibleTable.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
	}

	public List<Boolean> getVisibleTable() {
		return visibleTable;
	}

	public void setVisibleTable(List<Boolean> visibleTable) {
		this.visibleTable = visibleTable;
	}
}
