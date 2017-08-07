package com.ftu.staff.bean;

import java.io.Serializable;
import java.util.*;

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
import com.ftu.constanst.InventoryConstanst;
import com.ftu.inventory.bo.Provider;
import com.ftu.staff.bussiness.ShopService;
import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.TreeNode;

import com.ftu.admin.consumer.AuthorizationConsumer;
import com.ftu.admin.consumer.entity.IdentityEntity;
import com.ftu.admin.consumer.entity.IdentityOutput;
import com.ftu.admin.consumer.entity.SessionData;
import com.ftu.inventory.bean.SessionBean;
import com.ftu.language.LanguageBean;
import com.ftu.staff.bo.ApDomain;
import com.ftu.staff.bo.LazyStaffModel;
import com.ftu.staff.bo.Shop;
import com.ftu.staff.bo.Staff;
import com.ftu.staff.bussiness.AppDomainService;
import com.ftu.staff.bussiness.StaffService;
import org.primefaces.model.Visibility;

@ManagedBean
@ViewScoped
public class StaffBean implements Serializable, Validator {

	private StaffService staffService = new StaffService();
	private AppDomainService domainService = new AppDomainService();
	@ManagedProperty("#{shopBean}")
	private ShopBean shopBean;
	public String TYPE_EDIT = "edit";
	public String TYPE_ADD = "add";
	public String TYPE_SEARCH = "search";
	private String type;
	private List<Staff> liStaffs;
	private Long mtParent;
	private TreeNode selectedNode;
	private TreeNode selectedNodeStaff;
	private List<Shop> listShops;
	private Staff staffSelected = new Staff();
	@ManagedProperty("#{languageBean}")
	private LanguageBean languageBean;
	@ManagedProperty("#{sessionBean}")
	private SessionBean sessionBean;
	private List<ApDomain> listDomainsStaffType;
	private List<ApDomain> listGender;
	private Shop shopSelected;
	private Staff temp;
	private LazyDataModel<Staff> lazyStaff;
	private List<Staff> listStaffSelecteds;
	private ShopService shopService = new ShopService();
	private String resourceLog = InventoryConstanst.RESOURCE_LOG.LOG_START;
	private Staff staffSelectedOld;
	@PostConstruct
	public void init() {
		getAllStaff();
		listDomainsStaffType = domainService.findAllByType(InventoryConstanst.ApDomainType.STAFF_TYPE);
		listGender = domainService.findAllByType("GENDER");
	}

	public LazyDataModel<Staff> getLazyStaff() {
		return lazyStaff;
	}

	public void setLazyStaff(LazyDataModel<Staff> lazyStaff) {
		this.lazyStaff = lazyStaff;
	}

	public List<Staff> getListStaffSelecteds() {
		return listStaffSelecteds;
	}

	public void setListStaffSelecteds(List<Staff> listStaffSelecteds) {
		this.listStaffSelecteds = listStaffSelecteds;
	}

	public void setListDomainsStaffType(List<ApDomain> listDomainsStaffType) {
		this.listDomainsStaffType = listDomainsStaffType;
	}

	public void setListGender(List<ApDomain> listGender) {
		this.listGender = listGender;
	}

	public Staff getTemp() {
		return temp;
	}

	public void setTemp(Staff temp) {
		this.temp = temp;
	}

	public LanguageBean getLanguageBean() {
		return languageBean;
	}

	public void setLanguageBean(LanguageBean languageBean) {
		this.languageBean = languageBean;
	}

	public List<ApDomain> getListGender() {
		return listGender;
	}

	public List<ApDomain> getListDomainsStaffType() {
		return listDomainsStaffType;
	}

	public Staff getStaffSelected() {
		return staffSelected;
	}

	public void setStaffSelected(Staff staffSelected) {
		this.staffSelected = staffSelected;
	}

	public ShopBean getShopBean() {
		return shopBean;
	}

	public void setShopBean(ShopBean shopBean) {
		this.shopBean = shopBean;
	}

	public Long getMtParent() {
		return mtParent;
	}

	public void setMtParent(Long mtParent) {
		this.mtParent = mtParent;
	}

	public List<Shop> getListShops() {
		return listShops;
	}

	public void setListShops(List<Shop> listShops) {
		this.listShops = listShops;
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public TreeNode getSelectedNodeStaff() {
		return selectedNodeStaff;
	}

	public void setSelectedNodeStaff(TreeNode selectedNodeStaff) {
		this.selectedNodeStaff = selectedNodeStaff;
	}

	public List<Staff> getLiStaffs() {
		return liStaffs;
	}

	public void setLiStaffs(List<Staff> liStaffs) {
		this.liStaffs = liStaffs;
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

	public String getTYPE_EDIT() {
		return TYPE_EDIT;
	}

	public String getTYPE_SEARCH() {
		return TYPE_SEARCH;
	}

	public Staff reset() {
		staffSelected = new Staff();
		return staffSelected;
	}

	public void onRowSelect() {
		// System.out.println(staffSelected.getUserName());
	}

	public void onRowSelected() {

	}
	
	public String getPriceType(String code){
		for(ApDomain ap:listDomainsStaffType){
			if(ap.getCode().equals(code)){
				return ap.getName();
			}
		}
		return null;
	}

	public List<String> autoCompleteUsername(String query) {
		try {
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
					.getSession(false);
			SessionData data = (SessionData) session.getAttribute("username");
			IdentityOutput output = AuthorizationConsumer.getAllIdentities(data == null ? null : data.getTransEntity());
			List<IdentityEntity> list = output.getIdentities();

			List<String> filteredThemes = new ArrayList<String>();

			for (int i = 0; i < list.size(); i++) {
				IdentityEntity skin = list.get(i);
				if (skin.getUsername().toLowerCase().contains(query.toLowerCase())) {
					filteredThemes.add(skin.getUsername());
				}
			}
			return filteredThemes;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public List<String> autoCompleteUsernameActive(String query) {
		try {
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
					.getSession(false);
			SessionData data = (SessionData) session.getAttribute("username");
			IdentityOutput output = AuthorizationConsumer.getAllIdentities(data == null ? null : data.getTransEntity());
			List<IdentityEntity> list = output.getIdentities();

			List<String> filteredThemes = new ArrayList<String>();

			for (int i = 0; i < list.size(); i++) {
				IdentityEntity skin = list.get(i);
				if(!InventoryConstanst.ProviderStatus.ACTIVE.toString().equals(skin.getStatus())){
					continue;
				}
				if (skin.getUsername().toLowerCase().contains(query.toLowerCase())) {
					filteredThemes.add(skin.getUsername());
				}
			}
			return filteredThemes;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public void filterSearch() {
//		System.out.println(staffSelected.getStaffCode());
		staffSelected.setShopId(mtParent);
		staffSelected.setListShopId(listShopIdsInitSearchAllStatus(sessionBean.getShop().getShopId()));
		// liStaffs = staffService.getAllWithParams(staffSelected, true);
		lazyStaff = new LazyStaffModel(staffSelected, true, true);
		shopBean.setSelectedTreeNode(sessionBean.getShop().getShopId());
	}

	private boolean validateDOB(Date dob) {
		Date curDate = new Date();
		long d = curDate.getTime() - dob.getTime();
		int t = (int) (d / (1 / (3.16887646) * 100000000000.0));
		if (t < 18) {
			return false;
		}
		return true;
	}

	private int validateIssueDate(Date isdate, Date dob) {
		int errorCode = 0;
		Date curDate = new Date();
		long d = curDate.getTime() - isdate.getTime();
		int t = (int) (d / (1 / (3.16887646) * 100000000000.0));
		if (!isdate.before(curDate)) {
			errorCode = 1;
		} else {
			if (dob == null) {
				errorCode = 4;
			} else {
				if (!isdate.after(dob)) {
					errorCode = 2;
				} else {
					if (t > 15) {
						errorCode = 3;
					} else {
						errorCode = 0;
					}
				}
			}
		}
		return errorCode;
	}

	public void save() {
		FacesMessage message = null;
		try {
			if (type.equals(TYPE_ADD)) {
				staffSelected.setStaffStatus(1L);
				staffSelected.setShopId(mtParent);
			}
			staffSelected.setStaffCode(staffSelected.getStaffCode().toUpperCase().trim());
			staffService.saveOrUpdate(staffSelected);
			loadStaff(false);

			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
					.getSession(false);
			SessionData sessionData = (SessionData) session.getAttribute("username");
			if (type.equals(TYPE_ADD)) {
				LogsConsumer.logInsert(sessionData.getTransEntity(),
						new Object[] { staffSelected }, resourceLog, Calendar.getInstance().getTime());
			}else {
				LogsConsumer.logUpdate(sessionData.getTransEntity(), new Object[] { staffSelectedOld },
						new Object[] {staffSelected}, resourceLog, Calendar.getInstance().getTime());
			}
			reset();
			listStaffSelecteds.clear();
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlSave').hide();");
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header",null,false),
					languageBean.getMessage("common.action.save.success",null,false));
			filterSearch2();
		} catch (Exception ex) {
			ex.printStackTrace();
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, languageBean.getMessage("common.confirm.header",null,false),
					languageBean.getMessage("common.action.save.error",null,false));
		}
		RequestContext.getCurrentInstance().showMessageInDialog(message);

	}

	public void filterSearch2() {
		staffSelected.setShopId(mtParent);
		staffSelected.setListShopId(listShopIdsInitSearchAllStatus(mtParent));
		lazyStaff = new LazyStaffModel(staffSelected, true, true);
	}

	public void onRowSelect(SelectEvent event) {
		temp = (Staff) event.getObject();
	}
	public void beforeDelete() {

		FacesMessage message = null;
		if (listStaffSelecteds == null || listStaffSelecteds.isEmpty()) {
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header",null,false), 
					languageBean.getMessage("common.action.beforedelete",null,false));
			RequestContext context = RequestContext.getCurrentInstance();
			context.showMessageInDialog(message);
		}else {
			String msg = "";
			for (Staff st:listStaffSelecteds) {
				if(InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(st.getStaffStatus())){
					if(msg.isEmpty()){
						msg+=st.getStaffCode();
					}else {
						msg+=", " +st.getStaffCode();
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
		if (listStaffSelecteds == null||listStaffSelecteds.isEmpty()) {
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header",null,false),
					languageBean.getMessage("common.acction.beforesave",null,false));
			RequestContext context = RequestContext.getCurrentInstance();
			context.showMessageInDialog(message);
		} else {
			if (listStaffSelecteds.size() > 1) {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, languageBean.getMessage("common.confirm.header",null,false),
						languageBean.getMessage("common.action.beforesave.multiplerecord",null,false));
				RequestContext context = RequestContext.getCurrentInstance();
				context.showMessageInDialog(message);
			} else {
				staffSelected = listStaffSelecteds.get(0);
				staffSelectedOld =  new Staff(staffSelected);
				RequestContext context = RequestContext.getCurrentInstance();
				context.execute("PF('dlSave').show();");
			}
		}
	}

	public void delete() {
		FacesMessage message = null;
		try {
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
					.getSession(false);
			SessionData sessionData = (SessionData) session.getAttribute("username");

			for (Staff st : listStaffSelecteds) {
				staffSelectedOld =  new Staff(staffSelected);
				st.setStaffStatus(0L);
				staffService.saveOrUpdate(st);
//				LogsConsumer.logUpdate(sessionData.getTransEntity(), new Object[] { staffSelectedOld },
//						new Object[] {st}, resourceLog, Calendar.getInstance().getTime());
				LogsConsumer.logDelete(sessionData.getTransEntity(),
						new Object[] {st},resourceLog,Calendar.getInstance().getTime());
			}
			loadStaff(false);
			reset();
			listStaffSelecteds.clear();
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header",null,false), 
					languageBean.getMessage("common.action.delete.success",null,false));
			filterSearch2();
		} catch (Exception e) {
			e.printStackTrace();
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, languageBean.getMessage("common.confirm.header",null,false),
					languageBean.getMessage("common.acction.delete.error",null,false));
		} finally {

		}
		RequestContext.getCurrentInstance().showMessageInDialog(message);

	}

	private void loadStaff(boolean search) {
		Staff st = new Staff();
		st.setShopId(mtParent);
		st.setListShopId(listShopIds(shopSelected));
//		if (!search) {
//			st.setStaffStatus(1L);
//		}
		// liStaffs = staffService.getAllWithParams(st, false);
		lazyStaff = new LazyStaffModel(st, false, true);
	}

	private void getAllStaff() {
		try {
//			List<Shop> list = shopBean.getAllShopParrent();
//			if (list != null && !list.isEmpty()) {
				Shop shop = sessionBean.getShop();//shopBean.getAllShopParrent().get(0);
				Staff staff = new Staff();
				mtParent = shop.getShopId();
				shopSelected = shop;
				staff.setShopId(shop.getShopId());
				staff.setListShopId(listShopIdsInitSearchAllStatus(mtParent));
//				staff.setStaffStatus(1L);
				// liStaffs = staffService.getAllWithParams(staff, false);
				lazyStaff = new LazyStaffModel(staff, false, true);
				shopBean.setSelectedTreeNode(shop.getShopId());
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private List<Long> listShopIdsInit(Long parent) {
		List<Long> lst = new ArrayList<Long>();
		List<Shop> childs = shopService.findAllShopTree(parent);
		for (Shop s : childs) {
			lst.add(s.getShopId());
		}
		return lst;
	}
	private List<Long> listShopIdsInitSearch(Long parent) {
		List<Long> lst = new ArrayList<Long>();
		List<Shop> childs = shopService.getAllShopChildrent(parent);
		for (Shop s : childs) {
			lst.add(s.getShopId());
		}
		return lst;
	}
	private List<Long> listShopIdsInitSearchAllStatus(Long parent) {
		List<Long> lst = new ArrayList<Long>();
		List<Shop> childs = shopService.getAllShopChildrentAllStatus(parent);
		for (Shop s : childs) {
			lst.add(s.getShopId());
		}
		return lst;
	}
	public void onNodeSelect(NodeSelectEvent event) {
		staffSelected = new Staff();
		Shop s = (Shop) selectedNode.getData();
		if (!selectedNode.getParent().isExpanded()) {
			selectedNode.getParent().setExpanded(true);
		}
		if (!selectedNode.isExpanded()) {
			selectedNode.setExpanded(true);
		}
		mtParent = s.getShopId();
		shopSelected = s;
		Staff st = new Staff();
		st.setShopId(mtParent);
		st.setListShopId(listShopIdsInitSearchAllStatus(mtParent));
//		st.setStaffStatus(1L);
		// liStaffs = staffService.getAllWithParams(st, false);
		lazyStaff = new LazyStaffModel(st, false, true);
	}

	private List<Long> listShopIds(Shop parent) {
		List<Long> lst = new ArrayList<Long>();
		List<Shop> childs = parent.getChildShops();
		for (Shop s : childs) {
			lst.add(s.getShopId());
		}
		return lst;
	}

	private Date dobValidate = null;
	private Date idateValidate = null;
	@Override
	public void validate(FacesContext arg0, UIComponent uiComponent, Object object) throws ValidatorException {
		// TODO Auto-generated method stub
		if (!type.equals(TYPE_SEARCH)) {
			boolean valid = true;
			if (object == null) {
				return;
			}
			FacesMessage msg = null;
			String id = uiComponent.getId();
			valid = true;
			if (id.equals("code")) {
				String value = object.toString();
				if (validate(value,true)) {
					valid = false;
					msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false), languageBean.getMessage("staff.validator.code", null, false));
				}
			}

			if (id.equals("use")) {
				String value = object.toString();
				if (validate(value,false)) {
					valid = false;
					msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false), languageBean.getMessage("staff.validator.use", null, false));
				}
			}
			if (id.equals("idNo")) {
				String value = object.toString();
				if (value.trim().length()<8||value.trim().length()>15) {
					valid = false;
					msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false), languageBean.getMessage("staff.validator.idNo", null, false));
				}
			}
			if (id.equals("dob")) {
				Date dob = (Date) object;
				dobValidate = dob;
				if (!validateDOB(dob)) {
					valid = false;
					msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false), languageBean.getMessage("staff.validator.dob", null, false));
				}
			}

			if (id.equals("idate")) {
				Date isdate = (Date) object;
				idateValidate = isdate;
				Date dob = staffSelected.getDob();
				int errorCode = validateIssueDate(idateValidate, dobValidate);
				if (errorCode == 1) {
					valid = false;
					msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false), languageBean.getMessage("staff.validator.idate1", null, false));
				} else if (errorCode == 2) {
					valid = false;
					msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false), languageBean.getMessage("staff.validator.idate2", null, false));
				} else if (errorCode == 3) {
					valid = false;
					msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false), languageBean.getMessage("staff.validator.idate3", null, false));
				} else if (errorCode == 4) {
//					valid = false;
//					msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false), languageBean.getMessage("staff.validator.idate4", null, false));
				}
			}





			if (!valid) {
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
			}
		}
	}

	private boolean validate(String str,boolean code) {
		try {
			Staff st= null;
			if(code){
				st = staffService.checkExists(str.trim());
			}else{
				st = staffService.checkUsernameExists(str.trim());
			}
			if (st != null) {
				if (!st.getStaffId().equals(staffSelected.getStaffId())) {
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

	/**
	 * @return the sessionBean
	 */
	public SessionBean getSessionBean() {
		return sessionBean;
	}

	/**
	 * @param sessionBean
	 *            the sessionBean to set
	 */
	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
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
