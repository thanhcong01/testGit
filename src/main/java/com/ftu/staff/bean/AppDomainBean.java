package com.ftu.staff.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.ftu.admin.consumer.LogsConsumer;
import com.ftu.admin.consumer.entity.SessionData;
import com.ftu.constanst.InventoryConstanst;
import org.primefaces.context.RequestContext;

import com.ftu.language.LanguageBean;
import com.ftu.staff.bo.ApDomain;
import com.ftu.staff.bussiness.AppDomainService;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;

@ManagedBean
@ViewScoped
public class AppDomainBean implements Serializable {
	private AppDomainService service = new AppDomainService();

	public String TYPE_EDIT = "edit";
	public String TYPE_ADD = "add";
	public String TYPE_SEARCH = "search";
	private String type;
	private List<ApDomain> listApDomains;
	private ApDomain domainSelected = new ApDomain();
	private List<ApDomain> listAppDomainSelected;
	@ManagedProperty("#{languageBean}")
	private LanguageBean languageBean;
	private String resourceLog = InventoryConstanst.RESOURCE_LOG.LOG_APDOIMAIN;
	private ApDomain domainSelectedOld;

	@PostConstruct
	public void init() {
		listApDomains = service.getAllDomains(new ApDomain(), false);
	}

	public List<ApDomain> getListAppDomainSelected() {
		return listAppDomainSelected;
	}

	public void setListAppDomainSelected(List<ApDomain> listAppDomainSelected) {
		this.listAppDomainSelected = listAppDomainSelected;
	}

	public ApDomain getDomainSelected() {
		return domainSelected;
	}

	public void setDomainSelected(ApDomain domainSelected) {
		this.domainSelected = domainSelected;
	}

	public List<ApDomain> getListApDomains() {
		return listApDomains;
	}

	public void setListApDomains(List<ApDomain> listApDomains) {
		this.listApDomains = listApDomains;
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

	public void onRowSelected() {
		// System.out.println(staffSelected.getUserName());
	}

	private void loadDomain() {

	}

	public void save() {
		FacesMessage message = null;
		try {
			if (type.equals(TYPE_ADD)) {
				domainSelected.setStatus("1");
			}
			domainSelected.setType(domainSelected.getType().trim().toUpperCase());
			domainSelected.setCode(domainSelected.getCode().trim().toUpperCase());
			ApDomain apOld =  service.findByTypeAndValue(domainSelected.getType(),domainSelected.getCode(), domainSelected.getValue());
			if(domainSelected.getId() != null && apOld!=null && !domainSelected.getId().equals(apOld.getId())){
				message = new FacesMessage(FacesMessage.SEVERITY_INFO,
						languageBean.getMessage("common.confirm.header", null, false),
						languageBean.getMessage("apdoimain.duplicate.type.value", null, false));
				RequestContext.getCurrentInstance().showMessageInDialog(message);
				return;
			}else if(domainSelected.getId() == null && apOld!=null){
				message = new FacesMessage(FacesMessage.SEVERITY_INFO,
						languageBean.getMessage("common.confirm.header", null, false),
						languageBean.getMessage("apdoimain.duplicate.type.value", null, false));
				RequestContext.getCurrentInstance().showMessageInDialog(message);
				return;
			}
			service.saveOrUpdate(domainSelected);
			listApDomains = service.getAllDomains(null, false);
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
					.getSession(false);
			SessionData sessionData = (SessionData) session.getAttribute("username");
			if (type.equals(TYPE_ADD)) {
				LogsConsumer.logInsert(sessionData.getTransEntity(),
						new Object[] { domainSelected }, resourceLog, Calendar.getInstance().getTime());
//				reset();
			}else {
				LogsConsumer.logUpdate(sessionData.getTransEntity(), new Object[] { domainSelectedOld },
						new Object[] {domainSelected}, resourceLog, Calendar.getInstance().getTime());
			}
			reset();
			listAppDomainSelected.clear();
			message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					languageBean.getMessage("common.confirm.header", null, false),
					languageBean.getMessage("common.action.save.success", null, false));
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlSave').hide();");
		} catch (Exception ex) {
			ex.printStackTrace();
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					languageBean.getMessage("common.confirm.header", null, false),
					languageBean.getMessage("common.action.save.error", null, false));
		}
		RequestContext.getCurrentInstance().showMessageInDialog(message);
	}

	public void delete() {
		FacesMessage message = null;
		try {
			// service.delete(domainSelected);
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
					.getSession(false);
			SessionData sessionData = (SessionData) session.getAttribute("username");
			for (ApDomain ad : listAppDomainSelected) {
				domainSelectedOld = new ApDomain(domainSelected);
				ad.setStatus("0");
				service.saveOrUpdate(ad);
//				LogsConsumer.logUpdate(sessionData.getTransEntity(), new Object[] { domainSelectedOld },
//						new Object[] {ad}, resourceLog, Calendar.getInstance().getTime());
				LogsConsumer.logDelete(sessionData.getTransEntity(),
						new Object[] {ad},resourceLog,Calendar.getInstance().getTime());
			}

			listApDomains = service.getAllDomains(null, false);
			reset();
			listAppDomainSelected.clear();
			message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					languageBean.getMessage("common.confirm.header", null, false),
					languageBean.getMessage("common.action.delete.success", null, false));
		} catch (Exception e) {
			e.printStackTrace();
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					languageBean.getMessage("common.confirm.header", null, false),
					languageBean.getMessage("common.acction.delete.error", null, false));
		} finally {

		}
		RequestContext.getCurrentInstance().showMessageInDialog(message);
	}

	public void filterSearch() {
		listApDomains = service.getAllDomains(domainSelected, true);
	}

	public void beforeDelete() {

		FacesMessage message = null;
		if (listAppDomainSelected == null || listAppDomainSelected.isEmpty()) {
			message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					languageBean.getMessage("common.confirm.header", null, false),
					languageBean.getMessage("common.action.beforedelete", null, false));
			RequestContext context = RequestContext.getCurrentInstance();
			context.showMessageInDialog(message);
		} else {
			String msg = "";
			for (ApDomain g : listAppDomainSelected) {
				if(InventoryConstanst.ProviderStatus.IN_ACTIVE.toString().equals(g.getStatus())){
					if(msg.isEmpty()){
						msg+=g.getName();
					}else {
						msg+= ", " + g.getName();
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
			context.execute("document.getElementById('frm:confirmButton').click(); ");
		}
	}

	public void beforeSave() {

		FacesMessage message = null;
		if (listAppDomainSelected == null || listAppDomainSelected.isEmpty()) {
			message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					languageBean.getMessage("common.confirm.header", null, false),
					languageBean.getMessage("common.acction.beforesave", null, false));
			RequestContext context = RequestContext.getCurrentInstance();
			context.showMessageInDialog(message);
		} else {
			if (listAppDomainSelected.size() > 1) {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, languageBean.getMessage("common.confirm.header", null, false),
						languageBean.getMessage("common.action.beforesave.multiplerecord",null,false));
				RequestContext context = RequestContext.getCurrentInstance();
				context.showMessageInDialog(message);
			} else {
				domainSelected = listAppDomainSelected.get(0);
				domainSelectedOld = new ApDomain(domainSelected);
				RequestContext context = RequestContext.getCurrentInstance();
				context.execute("PF('dlSave').show();");
			}
		}
	}

	public ApDomain reset() {
		domainSelected = new ApDomain();
		return domainSelected;
	}

	public String getResourceLog() {
		return resourceLog;
	}

	public void setResourceLog(String resourceLog) {
		this.resourceLog = resourceLog;
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
