package com.ftu.inventory.bean;

import java.io.Serializable;
import java.util.ArrayList;
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
import com.ftu.inventory.bo.EquipmentsProfile;
import com.ftu.java.business.EquipmentsProfileService;
import com.ftu.staff.bo.ApDomain;
import com.ftu.staff.bussiness.AppDomainService;
import org.primefaces.context.RequestContext;

import com.ftu.admin.consumer.utils.StringUtil;
import com.ftu.inventory.bo.EquipmentsGroup;
import com.ftu.java.business.EquipmentsGroupService;
import com.ftu.language.LanguageBean;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;

@ManagedBean
@ViewScoped
public class EquipmentsGroupBean implements Serializable, Validator {
	private EquipmentsGroupService service = new EquipmentsGroupService();
	private EquipmentsProfileService serviceProfile = new EquipmentsProfileService();
	@ManagedProperty("#{goodsAllBean}")
	private GoodsAllBean bean;
	public String TYPE_EDIT = "edit";
	public String TYPE_ADD = "add";
	public String TYPE_SEARCH = "search";
	private String type;
	private List<EquipmentsGroup> listEquipmentsGroup;
	private EquipmentsGroup groupSelected = new EquipmentsGroup();
	@ManagedProperty("#{languageBean}")
	private LanguageBean languageBean;
	private  String resourceLog = InventoryConstanst.RESOURCE_LOG.LOG_GROUP_EQUIP;
	private EquipmentsGroup groupSelectedOld;

	private List<EquipmentsGroup> listGroupSelected;
	private List<ApDomain> lstGroupType;
	private List<ApDomain> lstGroupTypeActive;

	@PostConstruct
	public void init() {
		listEquipmentsGroup = service.getAllEquipmentsGroup(null, false, -1, -1);
		AppDomainService service = new AppDomainService();
		lstGroupType = service.findAllByTypeAllStatus(InventoryConstanst.ApDomainType.EQUIP_TYPE);
		lstGroupTypeActive = service.findAllByType(InventoryConstanst.ApDomainType.EQUIP_TYPE);
		if(lstGroupType==null){
			lstGroupType = new ArrayList<>();
			lstGroupTypeActive = new ArrayList<>();
		}
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

	public List<EquipmentsGroup> getListEquipmentsGroup() {
		return listEquipmentsGroup;
	}

	public void setListEquipmentsGroup(List<EquipmentsGroup> listEquipmentsGroup) {
		this.listEquipmentsGroup = listEquipmentsGroup;
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
		EquipmentsProfileBean goodsBean = (EquipmentsProfileBean) fc.getApplication().evaluateExpressionGet(fc, "#{equipmentsProfileBean}",
				EquipmentsProfileBean.class);
		goodsBean.setListGroups(service.getAllEquipmentsGroup(null, false, -1, -1));
	}

	public void save() {
		FacesMessage message = null;
		try {
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
					.getSession(false);
			SessionData sessionData = (SessionData) session.getAttribute("username");
			groupSelected.setEquipmentsGroupCode(groupSelected.getEquipmentsGroupCode().toUpperCase().trim());
			if(groupSelected.getEquipmentsGroupId() == null){
				groupSelected.setEquipmentsGroupStatus(1L);
				LogsConsumer.logInsert(sessionData.getTransEntity(),
						new Object[] { groupSelected }, resourceLog, Calendar.getInstance().getTime());
			}else {
				LogsConsumer.logUpdate(sessionData.getTransEntity(), new Object[] { groupSelectedOld },
						new Object[] {groupSelected}, resourceLog, Calendar.getInstance().getTime());
			}

			service.saveOrUpdate(groupSelected);
			listEquipmentsGroup = service.getAllEquipmentsGroup(null, false, -1, -1);
			bean.reinit();
			loadListGroup();
			reset();
			listGroupSelected.clear();
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false), 
			languageBean.getMessage("common.action.save.success", null, false));
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlSaveGG').hide();");
			reset();
		} catch (Exception ex) {
			ex.printStackTrace();
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, languageBean.getMessage("common.confirm.header", null, false), 
			languageBean.getMessage("common.action.save.error", null, false));
		}
		RequestContext.getCurrentInstance().showMessageInDialog(message);
	}

	public void delete() {
		FacesMessage message = null;
		EquipmentsProfileService equipmentsProfileService = new EquipmentsProfileService();
		try {
			String msg = "";
			for (EquipmentsGroup gg : listGroupSelected) {
				List<EquipmentsProfile> lst  = equipmentsProfileService.findByGroupId(gg.getEquipmentsGroupId());
				if(lst!=null && !lst.isEmpty()){
					if(msg.isEmpty()){
						msg+=gg.getEquipmentsGroupName();
					}else {
						msg+= ", " + gg.getEquipmentsGroupName();
					}
				}
			}
			if(!msg.isEmpty()){
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false),
						languageBean.getMessage("equipment.group.not.delete", new Object[]{msg}, true));
				RequestContext.getCurrentInstance().showMessageInDialog(message);
				return;
			}
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
					.getSession(false);
			SessionData sessionData = (SessionData) session.getAttribute("username");
			for (EquipmentsGroup gg : listGroupSelected) {
				groupSelectedOld = new EquipmentsGroup(gg);
				gg.setEquipmentsGroupStatus(0L);
				service.saveOrUpdate(gg);
//				LogsConsumer.logUpdate(sessionData.getTransEntity(), new Object[] { groupSelectedOld },
//						new Object[] {gg}, resourceLog, Calendar.getInstance().getTime());
				LogsConsumer.logDelete(sessionData.getTransEntity(),
						new Object[] {gg},resourceLog,Calendar.getInstance().getTime());
			}
			listEquipmentsGroup = service.getAllEquipmentsGroup(null, false, -1, -1);
			bean.reinit();
			listGroupSelected.clear();
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
		listEquipmentsGroup = service.getAllEquipmentsGroup(groupSelected, true, -1, -1);
	}

	public void beforeDelete() {
		FacesMessage message = null;
		if (listGroupSelected == null || listGroupSelected.isEmpty()) {
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false), 
			languageBean.getMessage("common.action.beforedelete", null, false));
			RequestContext context = RequestContext.getCurrentInstance();
			context.update("frmBr");
			context.showMessageInDialog(message);
		} else  {
			String msg = "";
			for (EquipmentsGroup gg : listGroupSelected) {
				if(InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(gg.getEquipmentsGroupStatus())){
					if(msg.isEmpty()){
						msg+=gg.getEquipmentsGroupName();
					}else {
						msg+= ", " + gg.getEquipmentsGroupName();
					}
				}
			}
			if(!msg.isEmpty()){
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false),
						languageBean.getMessage("common.action.beforedelete.active.argu", new Object[]{msg}, true));
				RequestContext.getCurrentInstance().showMessageInDialog(message);
				return;
			}


			List<Long> objs = new ArrayList<Long>();
			for (EquipmentsGroup gg : listGroupSelected) {
				objs.add(gg.getEquipmentsGroupId());
			}
//			if(serviceProfile.getSizeProfilesByGroup(objs) > 0){
//				message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false),
//						languageBean.getMessage("common.action.beforedelete.exit", null, false));
//				RequestContext context = RequestContext.getCurrentInstance();
//				context.update("frmBr");
//				context.showMessageInDialog(message);
//			}else{
				RequestContext context = RequestContext.getCurrentInstance();
				context.execute("document.getElementById('frmGG:confirmButtonGG').click(); ");
//			}


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
				groupSelectedOld = new EquipmentsGroup(groupSelected);
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
	public void validate(FacesContext arg0, UIComponent uiComponent, Object object) throws ValidatorException {
		// TODO Auto-generated method stub
		if (type == null || !type.equals(TYPE_SEARCH)) {
			boolean valid = true;
			if (StringUtil.stringIsNullOrEmty(object)) {
				return;
			}

			FacesMessage msg = null;
			String id = uiComponent.getId();
			String value = object.toString();
			valid = true;
			if (id.equals("type")) {
				for (ApDomain ap:lstGroupType) {
					if(ap.getValue().equals(Long.parseLong(value)) && InventoryConstanst.ProviderStatus.IN_ACTIVE.toString().equals(ap.getStatus())){
						valid = false;
						msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false),
								languageBean.getMessage("goodsgroup.validator.type.inActive", null, false));
						break;
					}
				}

			}
			if (id.equals("codeGG") && validate(value)) {
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

	public List<ApDomain> getLstGroupType() {
		return lstGroupType;
	}

	public void setLstGroupType(List<ApDomain> lstGroupType) {
		this.lstGroupType = lstGroupType;
	}

	public String getGroupTypeName(Long value){
		for (ApDomain app:lstGroupType) {
			if(app.getValue().equals(value)){
				return app.getName();
			}
		}
		return "";
	}
	public void onRowSelected() {

	}
	public List<ApDomain> getLstGroupTypeActive() {
		return lstGroupTypeActive;
	}

	public void setLstGroupTypeActive(List<ApDomain> lstGroupTypeActive) {
		this.lstGroupTypeActive = lstGroupTypeActive;
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
