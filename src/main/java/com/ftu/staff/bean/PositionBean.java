package com.ftu.staff.bean;

import com.ftu.admin.consumer.AuthorizationConsumer;
import com.ftu.admin.consumer.LogsConsumer;
import com.ftu.admin.consumer.entity.IdentityEntity;
import com.ftu.admin.consumer.entity.IdentityOutput;
import com.ftu.admin.consumer.entity.SessionData;
import com.ftu.constanst.InventoryConstanst;
import com.ftu.inventory.bean.SessionBean;
import com.ftu.inventory.bussiness.EquipmentsDetailService;
import com.ftu.language.LanguageBean;
import com.ftu.staff.bo.ApDomain;
import com.ftu.staff.bo.LazyPositionModel;
import com.ftu.staff.bo.Position;
import com.ftu.staff.bo.Shop;
import com.ftu.staff.bussiness.AppDomainService;
import com.ftu.staff.bussiness.PositionService;
import com.ftu.staff.bussiness.ShopService;
import com.ftu.utils.StringUtils;
import com.google.gson.Gson;
import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.TreeNode;
import org.primefaces.model.Visibility;

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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class PositionBean implements Serializable, Validator {
	private PositionService positionService = new PositionService();
	private AppDomainService domainService = new AppDomainService();
	@ManagedProperty("#{shopBean}")
	private ShopBean shopBean;
	public String TYPE_EDIT = "edit";
	public String TYPE_ADD = "add";
	public String TYPE_SEARCH = "search";
	private String type;
	private List<Position> liPositions;
	private Long mtParent;
	private TreeNode selectedNode;
	private TreeNode selectedNodePosition;
	private List<Shop> listShops;
	private Position positionSelected = new Position();
	private Position positionSelectedGrid = new Position();
	@ManagedProperty("#{languageBean}")
	private LanguageBean languageBean;
	@ManagedProperty("#{sessionBean}")
	private SessionBean sessionBean;
	private List<ApDomain> listDomainsPositionType;
	private List<ApDomain> lstPositionType;
	private List<Position> positionAll = new ArrayList<Position>();
	private Shop shopSelected;
	private Position temp;
	private LazyDataModel<Position> lazyPosition;
	private List<Position> listPositionSelecteds;
	private ShopService shopService = new ShopService();
	private String resourceLog = InventoryConstanst.RESOURCE_LOG.LOG_POSITION;
	private Position positionSelectedOld;

	@PostConstruct
	public void init() {
		getAllPosition();
		listDomainsPositionType = domainService.findAllByTypeAllStatus(InventoryConstanst.ApDomainType.FIGURE_NAME_TYPE);
		lstPositionType = domainService.findAllByTypeAllStatus(InventoryConstanst.ApDomainType.POSITION_TYPE);
		positionAll = positionService.getAll();
	}

	public String getFigureName(Long value) {
		for (ApDomain ap : listDomainsPositionType) {
			if (ap.getValue().equals(value)) {
				return ap.getName();
			}
		}
		return "";
	}
	public String getManagementName(Long value) {
		for (ApDomain ap : lstPositionType) {
			if (ap.getValue().equals(value)) {
				return ap.getName();
			}
		}
		return "";
	}

	//    public List<String> autoCompleteUsername(String query) {
	//        try {
	//            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
	//                    .getSession(false);
	//            SessionData data = (SessionData) session.getAttribute("username");
	//            IdentityOutput output = AuthorizationConsumer.getAllIdentities(data == null ? null : data.getTransEntity());
	//            List<IdentityEntity> list = output.getIdentities();
	//
	//            List<String> filteredThemes = new ArrayList<String>();
	//
	//            for (int i = 0; i < list.size(); i++) {
	//                IdentityEntity skin = list.get(i);
	//                if (skin.getUsername().toLowerCase().contains(query.toLowerCase())) {
	//                    filteredThemes.add(skin.getUsername());
	//                }
	//            }
	//            return filteredThemes;
	//        } catch (Exception e) {
	//            // TODO Auto-generated catch block
	//            e.printStackTrace();
	//        }
	//        return null;
	//    }

	public void filterSearch() {
		//        positionSelected.setShopId();
		positionSelected.setShopIds(listShopIdsInitSearchAllStatus(mtParent));
		positionSelected.setShopId(mtParent);
		// liPositions = positionService.getAllWithParams(positionSelected, true);
		lazyPosition = new LazyPositionModel(positionSelected, true, true);
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

	//    private int validateIssueDate(Date isdate, Date dob) {
	//        int errorCode = 0;
	//        Date curDate = new Date();
	//        long d = curDate.getTime() - isdate.getTime();
	//        int t = (int) (d / (1 / (3.16887646) * 100000000000.0));
	//        if (!isdate.before(curDate)) {
	//            errorCode = 1;
	//        } else {
	//            if (dob == null) {
	//                errorCode = 4;
	//            } else {
	//                if (!isdate.after(dob)) {
	//                    errorCode = 2;
	//                } else {
	//                    if (t > 15) {
	//                        errorCode = 3;
	//                    } else {
	//                        errorCode = 0;
	//                    }
	//                }
	//            }
	//        }
	//        return errorCode;
	//    }

	public void save() {
		FacesMessage message = null;
		try {
			if(!positionService.findByPositonCodeId(positionSelected.getPositionCode(), positionSelected.getPositionId()).isEmpty()){
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false),
						languageBean.getMessage("position.not.duplicate.code", null, false));
				RequestContext.getCurrentInstance().showMessageInDialog(message);
				return;
			}


			if (type.equals(TYPE_ADD)) {
				positionSelected.setShopId(mtParent);
			}
			if(positionSelected.getPositionId()==null){
				positionSelected.setPositionStatus(1L);
			}
			positionSelected.setPositionCode(positionSelected.getPositionCode().toUpperCase().trim());
			positionService.saveOrUpdate(positionSelected);
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlSave').hide();");
			shopBean.setSelectedTreeNode(shopSelected.getShopId());
			loadPosition(false);
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
					.getSession(false);
			SessionData sessionData = (SessionData) session.getAttribute("username");
			if (type.equals(TYPE_ADD)) {
				LogsConsumer.logInsert(sessionData.getTransEntity(),
						new Object[] { positionSelected }, resourceLog, Calendar.getInstance().getTime());
				//                reset();
			}else {
				LogsConsumer.logUpdate(sessionData.getTransEntity(), new Object[] { positionSelectedOld },
						new Object[] {positionSelected}, resourceLog, Calendar.getInstance().getTime());
			}
			reset();
			if(listPositionSelecteds==null){
				listPositionSelecteds = new ArrayList<>();
			}
			listPositionSelecteds.clear();
			positionSelectedGrid = new Position();
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false),
					languageBean.getMessage("common.action.save.success", null, false));
		} catch (Exception ex) {
			ex.printStackTrace();
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, languageBean.getMessage("common.confirm.header", null, false),
					languageBean.getMessage("common.action.save.error", null, false));
		}
		RequestContext.getCurrentInstance().showMessageInDialog(message);

	}

	public void onRowSelect(SelectEvent event) {
		temp = (Position) event.getObject();
	}

	public void beforeDelete() {
		FacesMessage message = null;
		if (listPositionSelecteds == null || listPositionSelecteds.isEmpty()) {
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false),
					languageBean.getMessage("common.action.beforedelete", null, false));
			RequestContext context = RequestContext.getCurrentInstance();
			context.showMessageInDialog(message);
		} else {
			String msg = "";
			for (Position g : listPositionSelecteds) {
				if(InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(g.getPositionStatus())){
					if(msg.isEmpty()){
						msg+=g.getPositionName();
					}else {
						msg+= ", " + g.getPositionName();
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
		positionAll = positionService.getAll();
		FacesMessage message = null;
		if (listPositionSelecteds == null || listPositionSelecteds.isEmpty()) {
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false),
					languageBean.getMessage("common.acction.beforesave", null, false));
			RequestContext context = RequestContext.getCurrentInstance();
			context.showMessageInDialog(message);
		} else {
			positionAll = positionService.getAll();

			if (listPositionSelecteds.size() > 1) {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, languageBean.getMessage("common.confirm.header", null, false),
						languageBean.getMessage("common.action.beforesave.multiplerecord", null, false));
				RequestContext context = RequestContext.getCurrentInstance();
				context.showMessageInDialog(message);
			} else {
				positionSelected = listPositionSelecteds.get(0);
				positionSelectedOld = new Position(positionSelected);
				positionAll.remove(positionSelected);
				RequestContext context = RequestContext.getCurrentInstance();
				context.execute("PF('dlSave').show();");
			}
		}
	}

	public void delete() {
		FacesMessage message = null;

		try {
			EquipmentsDetailService equipmentsDetailService = new EquipmentsDetailService();
			String msg = "";
			for (Position st : listPositionSelecteds) {
				if(equipmentsDetailService.countPosition(st.getPositionId()) > 0){
					if(msg.isEmpty()){
						msg+=st.getPositionName();
					}else {
						msg+= ", " + st.getPositionName();
					}
				}
			}
			if(!msg.isEmpty()){
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false),
						languageBean.getMessage("position.not.delete", new Object[]{msg}, true));
				RequestContext.getCurrentInstance().showMessageInDialog(message);
				return;
			}
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
					.getSession(false);
			SessionData sessionData = (SessionData) session.getAttribute("username");
			for (Position st : listPositionSelecteds) {
				positionSelectedOld = new Position(positionSelected);
				st.setPositionStatus(0L);
				positionService.saveOrUpdate(st);
//				LogsConsumer.logUpdate(sessionData.getTransEntity(), new Object[] { positionSelectedOld },
//						new Object[] {st}, resourceLog, Calendar.getInstance().getTime());
				LogsConsumer.logDelete(sessionData.getTransEntity(),
						new Object[] {st},resourceLog,Calendar.getInstance().getTime());
			}
			//            shopBean.setSelectedTreeNode(shopSelected.getShopId());
			loadPosition(false);
			reset();
			listPositionSelecteds.clear();
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false),
					languageBean.getMessage("common.action.delete.success", null, false));
		} catch (Exception e) {
			e.printStackTrace();
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, languageBean.getMessage("common.confirm.header", null, false),
					languageBean.getMessage("common.acction.delete.error", null, false));
		} finally {

		}
		RequestContext.getCurrentInstance().showMessageInDialog(message);

	}

	public void savePosition()
	{
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			Map<String,String> params = context.getExternalContext().getRequestParameterMap();
			String jsonData = params.get("JsonData");
			List<Position> lPos = new ArrayList<Position>(); 
			lPos = Arrays.asList(new Gson().fromJson(jsonData, Position[].class));
			System.out.println(lPos);
			for (Position pos : lPos)
				positionService.saveOrUpdate(pos);

			HttpSession session = (HttpSession) context.getExternalContext()
					.getSession(false);
			SessionData sessionData = (SessionData) session.getAttribute("username");
			LogsConsumer.logUpdate(sessionData.getTransEntity(),  listPositionSelecteds.toArray() ,
					lPos.toArray(), resourceLog, Calendar.getInstance().getTime());

			loadPosition(false);
			reset();
			if(listPositionSelecteds==null){
				listPositionSelecteds = new ArrayList<>();
			}
			listPositionSelecteds.clear();
			positionSelectedGrid = new Position();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false),
					languageBean.getMessage("common.action.save.success", null, false));
			RequestContext.getCurrentInstance().showMessageInDialog(message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private void loadPosition(boolean search) {
		Position st = new Position();
		st.setShopId(mtParent);
		st.setShopIds(listShopIdsInit(mtParent));
		lazyPosition = new LazyPositionModel(st, false, true);
		positionAll = positionService.getAll();
	}

	private void getAllPosition() {
		try {
			//            List<Shop> list = shopBean.getAllShopParrent();
			//            if (list != null && !list.isEmpty()) {
			Shop shop = sessionBean.getShop();
			Position position = new Position();
			mtParent = shop.getShopId();
			shopSelected = shop;
			position.setShopId(shop.getShopId());
			position.setShopIds(listShopIdsInit(mtParent));
			// liPositions = positionService.getAllWithParams(position, false);
			lazyPosition = new LazyPositionModel(position, false, true);
			shopBean.setSelectedTreeNode(shopSelected.getShopId());
			//            }
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

	public void onNodeSelect(NodeSelectEvent event) {
		positionSelected = new Position();
		Shop s = (Shop) selectedNode.getData();
		if (!selectedNode.getParent().isExpanded()) {
			selectedNode.getParent().setExpanded(true);
		}
		if (!selectedNode.isExpanded()) {
			selectedNode.setExpanded(true);
		}
		shopSelected = s;
		mtParent = s.getShopId();
		Position st = new Position();
		st.setShopId(mtParent);
		st.setShopIds(listShopIdsInitSearch(mtParent));
		lazyPosition = new LazyPositionModel(st, false, true);
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
	private List<Long> listShopIds(Shop parent) {
		List<Long> lst = new ArrayList<Long>();
		List<Shop> childs = parent.getChildShops();
		for (Shop s : childs) {
			lst.add(s.getShopId());
		}
		return lst;
	}

	@Override
	public void validate(FacesContext arg0, UIComponent uiComponent, Object object) throws ValidatorException {
		// TODO Auto-generated method stub
		if (type==null || !type.equals(TYPE_SEARCH)) {
			boolean valid = true;
			if (object == null) {
				return;
			}
			FacesMessage msg = null;
			String id = uiComponent.getId();
			valid = true;
			if (id.equals("positonCode")) {
				String value = object.toString().trim();
				if (value != null && !value.trim().isEmpty() && !positionService.findByPositonCodeId(value, positionSelected.getPositionId()).isEmpty()) {
					valid = false;
					msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false), languageBean.getMessage("position.validator.code.exits", null, false));
				}
			}

			if (id.equals("figurex")) {
				String value = object.toString();
				if (value != null && !value.trim().isEmpty() && !StringUtils.isNumberLong̣̣(value)) {
					valid = false;
					msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false),
							languageBean.getMessage("position.validator.figurex.number", null, false));
				}
			}
			if (id.equals("figureY")) {
				String value = object.toString();
				if (value != null && !value.trim().isEmpty() && !StringUtils.isNumberLong̣̣(value)) {
					valid = false;
					msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false), languageBean.getMessage("position.validator.figurey.number", null, false));
				}
			}
			if (id.equals("figureName")) {
				String value = object.toString();
				if(value != null && !value.trim().isEmpty()
						&& domainService.findByTypeAndValue(InventoryConstanst.ApDomainType.FIGURE_NAME_TYPE,Long.parseLong(value)) == null){
					valid = false;
					msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false),
							languageBean.getMessage("position.type.figure", null, false));
				}
			}
			if (id.equals("managementType")) {
				String value = object.toString();
				if(value != null && !value.trim().isEmpty()
						&& domainService.findByTypeAndValue(InventoryConstanst.ApDomainType.POSITION_TYPE,Long.parseLong(value)) == null){
					valid = false;
					msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false),
							languageBean.getMessage("position.type.position", null, false));
				}
			}

			if (id.equals("ipAddress")) {
				String value = object.toString();
				if (value != null && !value.trim().isEmpty() && !StringUtils.isIpAddresṣ̣(value)) {
					valid = false;
					msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false), languageBean.getMessage("position.validator.ipAddress.vali", null, false));
				}
			}


			if (!valid) {
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
			}
		}
	}


	private boolean validate(String positionCode, Long positionId) {
		try {
			Position st = null;
			if (positionCode != null && !positionCode.isEmpty()) {
				st = positionService.findByPositionCode(positionCode.trim());
				if (positionId != null && st != null && !st.getPositionId().equals(positionId)) {
					return false;
				} else if (positionId == null && st != null) {
					return false;
				}

			}
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		//        return false;
	}

	/**
	 * @return the sessionBean
	 */
	public SessionBean getSessionBean() {
		return sessionBean;
	}

	/**
	 * @param sessionBean the sessionBean to set
	 */
	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}

	public LazyDataModel<Position> getLazyPosition() {
		return lazyPosition;
	}

	public void setLazyPosition(LazyDataModel<Position> lazyPosition) {
		this.lazyPosition = lazyPosition;
	}

	public List<Position> getListPositionSelecteds() {
		return listPositionSelecteds;
	}

	public void setListPositionSelecteds(List<Position> listPositionSelecteds) {
		this.listPositionSelecteds = listPositionSelecteds;
	}

	public void setListDomainsPositionType(List<ApDomain> listDomainsPositionType) {
		this.listDomainsPositionType = listDomainsPositionType;
	}


	public Position getTemp() {
		return temp;
	}

	public void setTemp(Position temp) {
		this.temp = temp;
	}

	public LanguageBean getLanguageBean() {
		return languageBean;
	}

	public void setLanguageBean(LanguageBean languageBean) {
		this.languageBean = languageBean;
	}


	public List<ApDomain> getListDomainsPositionType() {
		return listDomainsPositionType;
	}

	public Position getPositionSelected() {
		return positionSelected;
	}

	public void setPositionSelected(Position positionSelected) {
		this.positionSelected = positionSelected;
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

	public TreeNode getSelectedNodePosition() {
		return selectedNodePosition;
	}

	public void setSelectedNodePosition(TreeNode selectedNodePosition) {
		this.selectedNodePosition = selectedNodePosition;
	}

	public List<Position> getLiPositions() {
		return liPositions;
	}

	public void setLiPositions(List<Position> liPositions) {
		this.liPositions = liPositions;
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

	public Position reset() {
		positionAll = positionService.getAll();
		positionSelected = new Position();
		return positionSelected;
	}

	public void onRowSelect() {
		// System.out.println(positionSelected.getUserName());
	}
	public String getPositionsAsJson() {
		//        if(listPositionSelecteds==null) return "";
		return new Gson().toJson(listPositionSelecteds);
	}
	public String getPositionsToSting() {
		if(listPositionSelecteds==null) return "";
		StringBuilder result = new StringBuilder(new String(""));
		for (Position p:listPositionSelecteds) {
			result.append(p.getPositionId());
			result.append(",");
			result.append(p.getFigureX());
			result.append(",");
			result.append(p.getFigureY());
			result.append(";");
		}
		System.out.println(result.toString());
		return result.toString();
	}
	public void onRowSelected() {
		if(listPositionSelecteds==null){
			listPositionSelecteds = new ArrayList<>();
			listPositionSelecteds.add(positionSelectedGrid);
		}else {
			listPositionSelecteds.clear();
			listPositionSelecteds.add(positionSelectedGrid);
		}
	}

	public List<Position> getPositionAll() {
		return positionAll;
	}

	public void setPositionAll(List<Position> positionAll) {
		this.positionAll = positionAll;
	}

	public List<ApDomain> getLstPositionType() {
		return lstPositionType;
	}

	public void setLstPositionType(List<ApDomain> lstPositionType) {
		this.lstPositionType = lstPositionType;
	}

	public String getResourceLog() {
		return resourceLog;
	}

	public void setResourceLog(String resourceLog) {
		this.resourceLog = resourceLog;
	}

	public Position getPositionSelectedGrid() {
		return positionSelectedGrid;
	}
	public Boolean getDisableBtn(){
		if(positionSelectedGrid== null || positionSelectedGrid.getPositionId()==null){
			return true;
		}
		if(InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(positionSelectedGrid.getPositionStatus())){
			return true;
		}
		return false;
	}

	public void setPositionSelectedGrid(Position positionSelectedGrid) {
		this.positionSelectedGrid = positionSelectedGrid;
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
