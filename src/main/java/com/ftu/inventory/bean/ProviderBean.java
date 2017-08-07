package com.ftu.inventory.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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

import com.ftu.inventory.bo.Provider;
import com.ftu.java.business.ProviderService;
import com.ftu.language.LanguageBean;
import com.ftu.staff.bo.Staff;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;

@ManagedBean
@ViewScoped
public class ProviderBean implements Validator, Serializable {
	private ProviderService service = new ProviderService();

	public String TYPE_EDIT = "edit";
	public String TYPE_ADD = "add";
	public String TYPE_SEARCH = "search";
	private String type;
	private List<Provider> listProviders;
	private Provider providerSelected = new Provider();
	@ManagedProperty("#{languageBean}")
	private LanguageBean languageBean;
	private List<Provider> listProviderSelected;
	private String resourceLog = InventoryConstanst.RESOURCE_LOG.LOG_PROVIDER;
	private Provider providerSelectedOld ;
	@PostConstruct
	public void init() {
		loadProvider(false);
	}

	public List<Provider> getListProviderSelected() {
		return listProviderSelected;
	}

	public void setListProviderSelected(List<Provider> listProviderSelected) {
		this.listProviderSelected = listProviderSelected;
	}

	public List<Provider> getListProviders() {
		return listProviders;
	}

	public void setListProviders(List<Provider> listProviders) {
		this.listProviders = listProviders;
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

	public Provider getProviderSelected() {
		return providerSelected;
	}

	public void setProviderSelected(Provider providerSelected) {
		this.providerSelected = providerSelected;
	}

	public LanguageBean getLanguageBean() {
		return languageBean;
	}

	public void setLanguageBean(LanguageBean languageBean) {
		this.languageBean = languageBean;
	}

	public void onRowSelect() {
		// System.out.println(staffSelected.getUserName());
	}

	private void loadProvider(boolean search) {
		Provider pv = new Provider();
		if (!search) {
//			pv.setEquipmentStatus("1");
		}
		listProviders = service.getAllProviders(pv, search);
	}

	public void onRowSelected() {

	}

	public void save() {
		FacesMessage message = null;
		try {
			if (type.equals(TYPE_ADD)) {
				providerSelected.setStatus("1");
			}
			providerSelected.setContractNo(providerSelected.getContractNo().toUpperCase().trim());
			service.saveOrUpdate(providerSelected);
			loadProvider(false);
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
					.getSession(false);
			SessionData sessionData = (SessionData) session.getAttribute("username");
			if (type.equals(TYPE_ADD)) {
				LogsConsumer.logInsert(sessionData.getTransEntity(),
						new Object[] { providerSelected }, resourceLog, Calendar.getInstance().getTime());
//				reset();
			}else {
				LogsConsumer.logUpdate(sessionData.getTransEntity(), new Object[] { providerSelectedOld },
						new Object[] {providerSelected}, resourceLog, Calendar.getInstance().getTime());
			}
			reset();
			listProviderSelected.clear();
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false), 
					languageBean.getMessage("common.action.save.success",null,false));
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlSave').hide();");
		} catch (Exception ex) {
			ex.printStackTrace();
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR,languageBean.getMessage("common.confirm.header", null, false), 
					languageBean.getMessage("common.action.save.error",null,false));
		}
		RequestContext.getCurrentInstance().showMessageInDialog(message);
	}

	public void delete() {
		FacesMessage message = null;
		try {
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
					.getSession(false);
			SessionData sessionData = (SessionData) session.getAttribute("username");



			for (Provider pv : listProviderSelected) {
				providerSelectedOld = new Provider(pv);
				pv.setStatus("0");
				service.saveOrUpdate(pv);
//				LogsConsumer.logUpdate(sessionData.getTransEntity(), new Object[] { providerSelectedOld },
//						new Object[] {pv}, resourceLog, Calendar.getInstance().getTime());
				LogsConsumer.logDelete(sessionData.getTransEntity(),
						new Object[] {pv},resourceLog,Calendar.getInstance().getTime());

			}
			loadProvider(false);
			reset();
			listProviderSelected.clear();
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false), 
					languageBean.getMessage("common.action.delete.success",null,false));
		} catch (Exception e) {
			e.printStackTrace();
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, languageBean.getMessage("common.confirm.header", null, false), 
					languageBean.getMessage("common.acction.delete.error",null,false));
		} finally {

		}
		RequestContext.getCurrentInstance().showMessageInDialog(message);
	}

	public void filterSearch() {
		listProviders = service.getAllProviders(providerSelected, true);
	}

	public void beforeDelete() {

		FacesMessage message = null;
		if (listProviderSelected == null || listProviderSelected.isEmpty()) {
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false), 
					languageBean.getMessage("common.action.beforedelete",null,false));
			RequestContext context = RequestContext.getCurrentInstance();
			context.showMessageInDialog(message);
		} else {
			String msg = "";
			for (Provider st:listProviderSelected) {
				if(InventoryConstanst.ProviderStatus.IN_ACTIVE.toString().equals(st.getStatus())){
					if(msg.isEmpty()){
						msg+=st.getProviderCode();
					}else {
						msg+=", " +st.getProviderCode();
					}
				}
			}
			if(!msg.isEmpty()){
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header",null,false),
						languageBean.getMessage("common.action.beforedelete.active.argu",new Object[]{msg},true));
				RequestContext.getCurrentInstance().showMessageInDialog(message);
				return;
			}
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("document.getElementById('frm:confirmButton').click(); ");

		}
	}

	public void beforeSave() {

		FacesMessage message = null;
		if (listProviderSelected == null || listProviderSelected.isEmpty()) {
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false), 
					languageBean.getMessage("common.acction.beforesave",null,false));
			RequestContext context = RequestContext.getCurrentInstance();
			context.showMessageInDialog(message);
		} else {
			if (listProviderSelected.size() > 1) {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, languageBean.getMessage("common.confirm.header", null, false),
						languageBean.getMessage("common.action.beforesave.multiplerecord",null,false));
				RequestContext context = RequestContext.getCurrentInstance();
				context.showMessageInDialog(message);
			} else {
				providerSelected = listProviderSelected.get(0);
				providerSelectedOld = new Provider(providerSelected);
				RequestContext context = RequestContext.getCurrentInstance();
				context.execute("PF('dlSave').show();");
			}
		}
	}

	public Provider reset() {
		providerSelected = new Provider();
		return providerSelected;
	}

	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object object) throws ValidatorException {
		// TODO Auto-generated method stub
		if (!type.equals(TYPE_SEARCH)) {
			boolean valid = true;
			if (object == null) {
				return;
			}

			FacesMessage msg = null;
			String id = arg1.getId();
			if(id.equals("code")&&object.toString() != null && !object.toString().trim().isEmpty()){
				Provider obj = service.findByProviderCode(object.toString().trim());
				if(obj!=null&&!obj.getProviderId().equals(providerSelected.getProviderId())){
					valid = false;
					msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false),
							languageBean.getMessage("provider.validator.code.exit", null, false));
				}
			}
			if(id.equals("idate")){
				Date value = (Date) object;
				valid = true;
			/*
			 * if (validate(value)) { valid = false; msg = new FacesMessage(
			 * "Thông báo", "Số hợp đồng đã tồn tại"); }
			 */
				if (validateContractDate(value)) {
					valid = false;
					msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false),
							languageBean.getMessage("provider.validator.contractdate", null, false));
				}
			}

			if (!valid) {
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
			}
		}
	}

	private boolean validateContractDate(Date cdate) {
		if (cdate.after(new Date())) {
			return true;
		}
		return false;
	}

	/*
	 * private boolean validate(String str) { try { Provider st =
	 * service.checkExists(str.trim()); if (st != null) { if
	 * (!st.getProviderId().equals(providerSelected.getProviderId())) { return
	 * true; } } } catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); return false; } return false; }
	 */

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
