/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bean;

import com.ftu.inventory.bo.*;
import com.ftu.inventory.dao.StockGoodsDAO;
import com.ftu.inventory.dto.StockGoodsInvSerialDTO;
import com.ftu.language.LanguageBean;
import com.ftu.constanst.InventoryConstanst;
import com.ftu.java.bo.SerialA;
import com.ftu.staff.bo.ApDomain;
import com.ftu.staff.bo.Shop;
import com.ftu.staff.bo.Staff;
import com.ftu.utils.AnalysisSerial;
import com.ftu.utils.StringUtils;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;

/**
 *
 * @author E5420
 */
@ManagedBean(eager = true)
@SessionScoped
public class ExSubShopRequire implements Serializable {

	SimpleDateFormat sf = new SimpleDateFormat(".yy.MM.dd.");
	private String orderCode;
	private List<ApDomain> listReason = new ArrayList<>();
	private Long reasonId;
	private Long typeId;
	private List<ApDomain> listTransType;
	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;
	@ManagedProperty(value = "#{languageBean}")
	private LanguageBean languageBean;
	@ManagedProperty(value = "#{transactionNotificationBean}")
    private TransactionNotificationBean transactionNotificationBean;
	private Shop shop;
	private Staff staff;

	//    private Long goodsId;
	//    private String goodsName;
	private String goods;
	private List<EquipmentsProfile> lsProfile;
	private EquipmentsProfile selectedProfile;

	private Long goodStatus;
	private String searchSerial;
	private String quantity;
	private String description;

	private List<StockGoodsInvSerialDTO> lsData;
	private StockTransactionDetail stDetail = new StockTransactionDetail();
	private StockTransaction st;
	private TransactionAction ta;

	private String goodsStatusName;
	private Boolean disableReason;
	private Boolean disableAdd;

	private Long goodsGroupId;
	private List<EquipmentsGroup> lsGoodsGroup;

	private int sizers;
	//private List<SerialA> setAnaSerial;
	private List<StockGoodsInvSerialDTO> setLsData;
	private List<StockGoodsInvSerialDTO> setLsDataSelect;
	private List<StockGoodsInvSerialDTO> setLsDataSelectAll;
	private Date createDate;

	@PostConstruct
	public void init() {
		sessionBean.loadData(sessionBean.getStaff(), sessionBean.getShop());
		setCreateDate(new Date());
		setDisableAdd(true);
		setShop(getSessionBean().getShop());
		setStaff(getSessionBean().getStaff());
		setSt(new StockTransaction(null, staff.getShopId(), null, shop.getShopId(), 
				InventoryConstanst.StockTransactionType.TRANS, 
				InventoryConstanst.StockTransactionStatus.WAIT, 
				InventoryConstanst.StockTransactionDeliveryType.WAIT));
		setListTransType(getSessionBean().getService().getListExSubShopReasons());
		setLsGoodsGroup(sessionBean.getLsgGroupActive());
		changeType();
		changeGoodsGroup();
	}

	public void changeGoodsGroup() {
		if (lsGoodsGroup != null && !lsGoodsGroup.isEmpty() && (goodsGroupId == null || goodsGroupId == 0L)) {
			setGoodsGroupId(lsGoodsGroup.get(0).getEquipmentsGroupId());
		}
		goods = "";
		//        goodsId = null;
		//        goodsName = "";
		selectedProfile = null;
		if (getGoodsGroupId() != null && getGoodsGroupId() > 0) {
			List<EquipmentsProfile> lsg = sessionBean.getLsEquipments();
			lsProfile = new ArrayList<>();
			if (lsg != null && !lsg.isEmpty()) {
				for (EquipmentsProfile g : lsg) {
					if(InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(g.getEquipProfileStatus())){
						continue;
					}
					if (goodsGroupId.equals(g.getEquipmentsGroupId())) {
						lsProfile.add(g);
					}
				}
			}
		}

		if (!lsProfile.isEmpty()) {
			selectedProfile = lsProfile.get(0);
			goods = selectedProfile.getProfileCode() + " | " + selectedProfile.getProfileName();
			//            goodsId = g.getProfileId();
			//            goodsName = g.getProfileName();
		} else {
			goods = "";
			//            goodsId = null;
			//            goodsName = "";
		}
	}

	public void goodsSelect() {
		if(goods==null || goods.isEmpty()){
			return;
		}
		selectedProfile = null;
		if (lsProfile != null) {
			for (EquipmentsProfile g : lsProfile) {
				if(InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(g.getEquipProfileStatus())){
					continue;
				}
				if ((g.getProfileCode() + " | " + g.getProfileName()).equals(goods)) {
					//                    goodsId = g.getProfileId();
					//                    goodsName = g.getProfileName();
					selectedProfile = g;
					resetForm();
					break;
				}
			}
		}
	}

	public List<String> completeGoods(String gs) {
		goods = gs;
		List<String> rs = new ArrayList<>();
		if (lsProfile != null && !lsProfile.isEmpty()) {
			for (EquipmentsProfile g : lsProfile) {
				if(InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(g.getEquipProfileStatus())){
					continue;
				}
				if (gs == null || gs.isEmpty() || g.getProfileCode().toLowerCase().contains(goods.toLowerCase())) {
					rs.add(g.getProfileCode() + " | " + g.getProfileName());
				}
			}
		}
		goodsSelect();
		return rs;
	}

	public void changeType() {
		if (listTransType != null && !listTransType.isEmpty()) {
			if (typeId == null || typeId == 0L) {
				typeId = listTransType.get(0).getValue();
			}
		}
		if (typeId != null && typeId > 0) {
			//goodStatus = typeId.toString().equals(InventoryConstanst.TransactionType.EX_PARENT_ERR) ? InventoryConstanst.GoodsStatus.ERROR : InventoryConstanst.GoodsStatus.NOMAL;

			if (typeId.toString().equals(InventoryConstanst.TransactionType.EX_PARENT))
				goodStatus = InventoryConstanst.GoodsStatus.NOMAL;
			else if (typeId.toString().equals(InventoryConstanst.TransactionType.EX_PARENT_ERR))
				goodStatus = InventoryConstanst.GoodsStatus.ERROR;
			else if (typeId.toString().equals(InventoryConstanst.TransactionType.EX_PARENT_USED))
				goodStatus = InventoryConstanst.GoodsStatus.USED;

			orderCode = "EX_" + typeId + sf.format(new Date()) + sessionBean.getService().getTransactionActionCount(typeId.toString()).toString();
			listReason = sessionBean.getService().getListReason(typeId.toString());
			if (listReason != null) {
				reasonId = listReason.get(0).getValue();
			}
		} else {
			goodStatus = 0L;
			orderCode = "";
		}
		lsData = null;
	}

	public void search() {
		//        Integer quan = null;
		if (listTransType == null || listTransType.isEmpty() || sessionBean.getLsEquipments() == null || sessionBean.getLsEquipments().isEmpty()) {
			return;
		}
		typeId = (typeId == null || typeId < 1) ? listTransType.get(0).getValue() : typeId;
		changeType();
		//        if (searchSerial != null && !searchSerial.trim().isEmpty() && !searchSerial.matches("^[0-9A-Fa-f]+$")) {
		//
		//            languageBean.showMeseage("Info", "fromSerialReq");
		//            return;
		//
		//        }
		//        if (toSerial != null && !toSerial.trim().isEmpty() && !toSerial.matches("^[0-9A-Fa-f]+$")) {
		//
		//            languageBean.showMeseage("Info", "toSerialReq");
		//            return;
		//
		//        }
		//        if (quantity == null || quantity.trim().isEmpty()) {
		//            languageBean.showMeseage("Info", "qttReqErr");
		//            return;
		//        } else {
		//            if (!quantity.matches("^[0-9]+$")) {
		//
		//                languageBean.showMeseage("Info", "qttReqErr2");
		//                return;
		//
		//            } else {
		//                quan = Integer.parseInt(getQuantity());
		//                if (quan <= 0) {
		//                    languageBean.showMeseage("Info", "qttReqErr2");
		//                    return;
		//                }
		//            }
		//        }
		StockGoodsInvSerialDTO sgis = new StockGoodsInvSerialDTO(searchSerial, getGoodStatus(), InventoryConstanst.StockStatus.INSTOCK, 
				shop.getShopId(), null, selectedProfile.getProfileId());
		setSetLsData(sessionBean.getService().searchStockEquipmentsInvSerial(null, null, sgis, goodsGroupId, 0, 0));
		if (setLsData == null || setLsData.isEmpty()) {
			languageBean.showMeseage("Info", "noRecordFound");
		}
		setLsData.removeAll(stDetail.getSetSerial());
		int a = 1; 
		a= a++;
	}

	public StockTransactionDetail getStockTransactionDetailByProfileId(Long pId, Long status) {
		for (StockTransactionDetail std : getSt().getLsDetail()) {
			if (std.getGoodsId().equals(pId) && std.getGoodsStatus().equals(status)) {
				return std;
			}
		}
		StockTransactionDetail std = new StockTransactionDetail(0L, pId, status);
		//getSt().getLsDetail().add(std);
		setLsData=new ArrayList<>();
		setLsDataSelect = new ArrayList<>();
		return std;
	}

	public void add() {
		if (setLsDataSelect == null || setLsDataSelect.isEmpty()) {
			languageBean.showMeseage("Info", "chooseReq", new Object[]{"Serial"});
			return;
		}


		if (stDetail == null) {
			stDetail = getStockTransactionDetailByProfileId(lsData.get(0).getEquipmentProfileId(), lsData.get(0).getEquipmentProfileStatus());
			getSt().getLsDetail().add(stDetail);
		}

		if (stDetail.getQuantity() < stDetail.getSetSerial().size() + setLsDataSelect.size()) {
			languageBean.showMeseage("Info", "quantity.max.size", new Object[]{stDetail.getQuantity() });
			return;
		}

		setLsDataSelectAll.addAll(setLsDataSelect);
		stDetail.getSetSerial().addAll(setLsDataSelect);
		setLsData.removeAll(setLsDataSelect);
		setLsDataSelect = new ArrayList<>();

		//        setLsData=new ArrayList<>();
		//        Long qt = 0L + stDetail.getSetSerial().size();
		//        stDetail.setQuantity(qt);
		//    lsData = new ArrayList<>();
		searchSerial = "";
		//        toSerial = "";
	}

	public void addAll() {
		if (lsData == null || lsData.isEmpty()) {
			languageBean.showMeseage("Info", "chooseReq", new Object[]{"Serial"});
			return;
		}
		if (stDetail == null)
			stDetail = getStockTransactionDetailByProfileId(lsData.get(0).getEquipmentProfileId(), lsData.get(0).getEquipmentProfileStatus());
		stDetail.getSetSerial().addAll(lsData);
		lsData = new ArrayList<>();
		Long qt = 0L + stDetail.getSetSerial().size();
		stDetail.setQuantity(qt);
		//    lsData = new ArrayList<>();
		searchSerial = "";
		//        toSerial = "";
	}

	public void detailInfo() {
		changeType();
		if (selectedProfile == null || selectedProfile.getProfileId() == 0L) {
			languageBean.showMeseage("Info", "importReqView");
			return;
		}
		Long quan = null;
		if (quantity == null || quantity.trim().isEmpty()) {
			languageBean.showMeseage("Info", "qttReqErr");
			return;
		} else {
			if (!quantity.matches("^[0-9]+$")) {

				languageBean.showMeseage("Info", "qttReqErr2");
				return;

			} else {
				quan = Long.parseLong(getQuantity());
				if (quan <= 0) {
					languageBean.showMeseage("Info", "qttReqErr2");
					return;
				}
			}
		}

		StockGoodsDAO sgDao = new StockGoodsDAO();
		StockGoods sg = sgDao.getStockGoods(selectedProfile.getProfileId(), goodStatus, sessionBean.getShop().getShopId());

		if (sg != null)
		{
			stDetail = getStockTransactionDetailByProfileId(selectedProfile.getProfileId(), goodStatus);
			if(stDetail != null && sg.getAvailableQuantity() >= quan+stDetail.getQuantity()) {
				if(stDetail.getQuantity().equals(0L)){
					getSt().getLsDetail().add(stDetail);
				}
				stDetail.setProfileManagementType(selectedProfile.getManagementType());
				stDetail.setQuantity(stDetail.getQuantity() + quan);
				stDetail.setDescription(description);
				searchSerial = "";
				setLsDataSelectAll = new ArrayList<>();
				setLsData = new ArrayList<>();
				setLsDataSelect = new ArrayList<>();
				//        toSerial = "";
				//quantity = "10000";
				if (selectedProfile.getSerial())
				{
					FacesContext fc = FacesContext.getCurrentInstance();
					NavigationHandler nh = fc.getApplication().getNavigationHandler();
					nh.handleNavigation(fc, null, "/ex-subshop-require-sub.xhtml?faces-redirect=false");
					search();
				}
				else
				{

					RequestContext context = RequestContext.getCurrentInstance();
					context.execute("PF('dtWidgetVar').unselectAllRows();");
				}
			}
			else
			{
				languageBean.showMeseage("Info", "quantity.size.not enough", new Object[]{sg.getAvailableQuantity() - stDetail.getQuantity()});
			}
		}else 
		{
			languageBean.showMeseage("Info", "stockgood.notfound");
		}

	}

	public void viewClick(/*SelectEvent event*/) {
		if (stDetail != null && InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(stDetail.getProfileManagementType())) {
			//stDetail = (StockTransactionDetail) event.getObject();
			searchSerial = "";
			setLsDataSelectAll = new ArrayList<>();
			setLsData = new ArrayList<>();
			setLsDataSelect = new ArrayList<>();
			//setLsData.clear();
			//setLsDataSelect.clear();
			//        toSerial = "";
			//        quantity = "10000";
			FacesContext fc = FacesContext.getCurrentInstance();
			NavigationHandler nh = fc.getApplication().getNavigationHandler();
			nh.handleNavigation(fc, null, "/ex-subshop-require-sub.xhtml?faces-redirect=false");
		}
	}
	public boolean btnViewClick(/*SelectEvent event*/) {
		if (stDetail != null && InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(stDetail.getProfileManagementType())) {
			return false;
		}
		return true;
	}

	public void refresh() {
		getSt().setLsDetail(new ArrayList<StockTransactionDetail>());
		stDetail = new StockTransactionDetail();
		changeType();
	}

	public void resetForm()
	{
		quantity = "";
		description = "";

	}

	public String exitSub() {
		if (stDetail.getSetSerial().size() != stDetail.getQuantity()) {
			languageBean.showMeseage("Info", "quantity.max.size", new Object[]{stDetail.getQuantity()});
			return "";
		}
		//        if (stDetail.getSetSerial().isEmpty()) {
		//            st.getLsDetail().remove(stDetail);
		//        }
		Long qt = 0L + stDetail.getSetSerial().size();
		//stDetail.setQuantity(qt);
		stDetail = new StockTransactionDetail();
		quantity = "";
		description = "";
		return "ex-subshop-require";
	}

	public String refreshStd() {
		stDetail.getSetSerial().removeAll(setLsDataSelectAll);
		getSt().getLsDetail().remove(stDetail);

		//Long qt = 0L + stDetail.getSetSerial().size();
		//stDetail.setQuantity(qt);

		stDetail = new StockTransactionDetail();
		quantity = "";
		description = "";
		return "ex-subshop-require";
	}
	public void clickStd() {
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('cfImport').show();");
	}
	//    public void approSerialSelected(){
	//		int size = 0;
	//		if(stDetail.getSetSerial()!=null){
	//			size = stDetail.getSetSerial().size();
	//		}
	//		if (stDetail.getQuantity() != size ) {
	//			languageBean.showMeseage("Info", "quantity.max.size", new Object[] { std.getQuantity()});
	//			setSerial = new HashSet<>();
	//			return;
	//		}
	//		FacesContext fc = FacesContext.getCurrentInstance();
	//		NavigationHandler nh = fc.getApplication().getNavigationHandler();
	//		nh.handleNavigation(fc, null, "/im-pshop-ex-goods.xhtml?faces-redirect=false");
	//	}
	//	public void removeSerialSelected(){
	//		int size = 0;
	//		if(std.getSetSerial()!=null){
	//			std.getSetSerial().clear();
	//		}
	//		FacesContext fc = FacesContext.getCurrentInstance();
	//		NavigationHandler nh = fc.getApplication().getNavigationHandler();
	//		nh.handleNavigation(fc, null, "/im-pshop-ex-goods.xhtml?faces-redirect=false");
	//	}

	public void removeRs(StockGoodsInvSerialDTO gs) {
		getStDetail().getSetSerial().remove(gs);
		//stDetail.setQuantity(stDetail.getQuantity() - 1);
	}

	public void removeDtRs(StockTransactionDetail gs) {
		getSt().getLsDetail().remove(gs);
		if (stDetail != null && gs.getRowkey() == stDetail.getRowkey()) {
			stDetail = null;
		}
	}

	public String checkSerial() {
		if (getSt() == null || getSt().getLsDetail().isEmpty()) {
			return "Stock detail is empty !";
		}
		for (StockTransactionDetail item : getSt().getLsDetail()) {
			if ( (item.getProfileManagementType() != null && item.getProfileManagementType().equals("3") )&& (item.getSetSerial() == null || item.getSetSerial().isEmpty() )) {
				return "Detail " + item.getGoodsName() + " has no Serial";
			}
		}
		return null;
	}

	public void action() {
		changeType();
		if (getSt().getLsDetail().isEmpty()) {
			return;
		} else {
			String s = checkSerial();
			if (s != null) {
				return;
			}
			if (typeId < 1L || reasonId == null || reasonId < 1L) {
				return;
			}
		}
		try {
			boolean k = sessionBean.getService().exRequire(ta, getSt(), InventoryConstanst.StockStatus.BLOCK_XT);
			if (k) {
				getLanguageBean().showMeseage("success", "succesProces");
			} else {
				getLanguageBean().showMeseage("Info", "errorProcess");
			}
			setSt(new StockTransaction(null, staff.getShopId(), null, shop.getShopId(), InventoryConstanst.StockTransactionType.TRANS, InventoryConstanst.StockTransactionStatus.WAIT, InventoryConstanst.StockTransactionDeliveryType.WAIT));
			refresh();
			transactionNotificationBean.init();
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('cfExSReq').hide();");
		} catch (Exception ex) {
			ex.printStackTrace();
			getLanguageBean().showMeseage("error", "errorProcess");
		}
	}

	public void save() {
		changeType();
		if (getSt().getLsDetail().isEmpty()) {
			return;
		} else {
			String s = checkSerial();
			if (s != null) {
				languageBean.showMeseage_("Info", s);
				return;
			}
			if (typeId < 1L  || reasonId == null || reasonId < 1L) {
				languageBean.showMeseage("Info", "exrequireReason");
				return;
			}
			//description="";
			ta = new TransactionAction(orderCode, typeId.toString(), staff.getStaffId(), staff.getShopId(), InventoryConstanst.TransactionStatus.CREATE, shop.getShopId(), shop.getShopParentId());
			ta.setReasonId(reasonId);
			ta.setReasonName(getReasonName(reasonId));
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('cfExSReq').show();");
		}
	}

	private String getReasonName(Long reasonId) {
		if (listReason != null) {
			for (ApDomain d : listReason) {
				if (d.getValue().equals(reasonId)) {
					return d.getName();
				}
			}
		}
		return "";
	}

	/**
	 * @return the reasonId
	 */
	public Long getReasonId() {
		return reasonId;
	}

	/**
	 * @param reasonId the reasonId to set
	 */
	public void setReasonId(Long reasonId) {
		this.reasonId = reasonId;
	}

	/**
	 * @return the orderCode
	 */
	public String getOrderCode() {
		return orderCode;
	}

	/**
	 * @param orderCode the orderCode to set
	 */
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	/**
	 * @return the listReason
	 */
	public List<ApDomain> getListReason() {
		return listReason;
	}

	/**
	 * @param listReason the listReason to set
	 */
	public void setListReason(List<ApDomain> listReason) {
		this.listReason = listReason;
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

	/**
	 * @return the languageBean
	 */
	public LanguageBean getLanguageBean() {
		return languageBean;
	}

	/**
	 * @param languageBean the languageBean to set
	 */
	public void setLanguageBean(LanguageBean languageBean) {
		this.languageBean = languageBean;
	}

	/**
	 * @return the transactionNotificationBean
	 */
	public TransactionNotificationBean getTransactionNotificationBean() {
		return transactionNotificationBean;
	}

	/**
	 * @param transactionNotificationBean the transactionNotificationBean to set
	 */
	public void setTransactionNotificationBean(TransactionNotificationBean transactionNotificationBean) {
		this.transactionNotificationBean = transactionNotificationBean;
	}

	/**
	 * @return the shop
	 */
	public Shop getShop() {
		return shop;
	}

	/**
	 * @param shop the shop to set
	 */
	public void setShop(Shop shop) {
		this.shop = shop;
	}

	/**
	 * @return the staff
	 */
	public Staff getStaff() {
		return staff;
	}

	/**
	 * @param staff the staff to set
	 */
	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	/**
	 * @return the stDetail
	 */
	public StockTransactionDetail getStDetail() {
		return stDetail;
	}

	/**
	 * @param stDetail the stDetail to set
	 */
	public void setStDetail(StockTransactionDetail stDetail) {
		this.stDetail = stDetail;
	}

	//    /**
	//     * @return the goodsId
	//     */
	//    public Long getGoodsId() {
	//        return goodsId;
	//    }
	//
	//    /**
	//     * @param goodsId the goodsId to set
	//     */
	//    public void setGoodsId(Long goodsId) {
	//        this.goodsId = goodsId;
	//    }

	/**
	 * @return the goodStatus
	 */
	public Long getGoodStatus() {
		return goodStatus;
	}

	/**
	 * @param goodStatus the goodStatus to set
	 */
	public void setGoodStatus(Long goodStatus) {
		this.goodStatus = goodStatus;
	}

	/**
	 * @return the fromSerial
	 */
	public String getSearchSerial() {
		return searchSerial;
	}

	/**
	 * @param serial the fromSerial to set
	 */
	public void setSearchSerial(String serial) {
		this.searchSerial = serial;
	}



	/**
	 * @return the lsData
	 */
	public List<StockGoodsInvSerialDTO> getLsData() {
		return lsData;
	}

	/**
	 * @param lsData the lsData to set
	 */
	public void setLsData(List<StockGoodsInvSerialDTO> lsData) {
		this.lsData = lsData;
	}

	/**
	 * @return the st
	 */
	public StockTransaction getSt() {
		//        if (st != null && stDetail != null) {
		//            for (StockTransactionDetail std : st.getLsDetail()) {
		//                if (std.getSetSerial().isEmpty() && !std.getGoodsId().equals(stDetail.getGoodsId())) {
		//                    st.getLsDetail().remove(std);
		//                }
		//            }
		//        }
		return st;
	}

	/**
	 * @param st the st to set
	 */
	public void setSt(StockTransaction st) {
		this.st = st;
	}

	/**
	 * @return the ta
	 */
	public TransactionAction getTa() {
		return ta!=null?ta:new TransactionAction();
	}

	/**
	 * @param ta the ta to set
	 */
	public void setTa(TransactionAction ta) {
		this.ta = ta;
	}

	//    /**
	//     * @return the goodsName
	//     */
	//    public String getProfileName() {
	//        return goodsName;
	//    }
	//
	//    /**
	//     * @param goodsName the goodsName to set
	//     */
	//    public void setProfileName(String goodsName) {
	//        this.goodsName = goodsName;
	//    }

	/**
	 * @return the goodsStatusName
	 */
	public String getGoodsStatusName() {
		return goodsStatusName;
	}

	/**
	 * @param goodsStatusName the goodsStatusName to set
	 */
	public void setGoodsStatusName(String goodsStatusName) {
		this.goodsStatusName = goodsStatusName;
	}

	/**
	 * @return the disableReason
	 */
	public Boolean getDisableReason() {
		if (getSt() == null || getSt().getLsDetail() == null || getSt().getLsDetail().isEmpty()) {
			disableReason = false;
		} else {
			disableReason = true;
		}
		return disableReason;
	}

	/**
	 * @param disableReason the disableReason to set
	 */
	public void setDisableReason(Boolean disableReason) {
		this.disableReason = disableReason;
	}

	/**
	 * @return the lsGoods
	 */
	public List<EquipmentsProfile> getLsProfile() {
		return lsProfile;
	}

	/**
	 * @param list the lsGoods to set
	 */
	public void setLsProfile(List<EquipmentsProfile> list) {
		this.lsProfile = list;
	}

	/**
	 * @return the goodsGroupId
	 */
	public Long getGoodsGroupId() {
		return goodsGroupId;
	}

	/**
	 * @param goodsGroupId the goodsGroupId to set
	 */
	public void setGoodsGroupId(Long goodsGroupId) {
		this.goodsGroupId = goodsGroupId;
	}

	/**
	 * @return the disableAdd
	 */
	public Boolean getDisableAdd() {

		return checkSerial() != null;
	}

	/**
	 * @param disableAdd the disableAdd to set
	 */
	public void setDisableAdd(Boolean disableAdd) {
		this.disableAdd = disableAdd;
	}

	public int getSizers() {
		if (stDetail == null || stDetail.getSetSerial().isEmpty()) {
			sizers = 0;
		} else {
			sizers = stDetail.getSetSerial().size();
		}
		return sizers;
	}

	/**
	 * @param sizers the sizers to set
	 */
	public void setSizers(int sizers) {
		this.sizers = sizers;
	}

	//    public List<SerialA> getSetAnaSerial() {
	//        setAnaSerial = new ArrayList<>();
	//        if (stDetail != null && !stDetail.getSetSerial().isEmpty()) {
	//            AnalysisSerial an = new AnalysisSerial(null, new ArrayList<>(stDetail.getSetSerial()));
	//            setAnaSerial = an.analysisInvSerial();
	//        }
	//        return setAnaSerial;
	//    }
	//
	//    /**
	//     * @param setAnaSerial the setAnaSerial to set
	//     */
	//    public void setSetAnaSerial(List<SerialA> setAnaSerial) {
	//        this.setAnaSerial = setAnaSerial;
	//    }

	/**
	 * @return the listTransType
	 */
	public List<ApDomain> getListTransType() {
		return listTransType;
	}

	/**
	 * @param listTransType the listTransType to set
	 */
	public void setListTransType(List<ApDomain> listTransType) {
		this.listTransType = listTransType;
	}

	/**
	 * @return the typeId
	 */
	public Long getTypeId() {
		return typeId;
	}

	/**
	 * @param typeId the typeId to set
	 */
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	//    /**
	//     * @return the setLsData
	//     */
	//    public List<SerialA> getSetLsData() {
	//        setLsData = new ArrayList<>();
	//        if (lsData != null && !lsData.isEmpty()) {
	//            AnalysisSerial an = new AnalysisSerial(null, new ArrayList<>(lsData));
	//            setLsData = an.analysisInvSerial();
	//        }
	//        return setLsData;
	//    }
	//
	//    /**
	//     * @param setLsData the setLsData to set
	//     */
	//    public void setSetLsData(List<SerialA> setLsData) {
	//        this.setLsData = setLsData;
	//    }

	/**
	 * @return the goods
	 */
	public String getGoods() {
		return goods;
	}

	/**
	 * @param goods the goods to set
	 */
	public void setGoods(String goods) {
		this.goods = goods;
	}
	//
	//    /**
	//     * @return the setLsDataSelect
	//     */
	//    public List<SerialA> getSetLsDataSelect() {
	//        return setLsDataSelect;
	//    }
	//
	//    /**
	//     * @param setLsDataSelect the setLsDataSelect to set
	//     */
	//    public void setSetLsDataSelect(List<SerialA> setLsDataSelect) {
	//        this.setLsDataSelect = setLsDataSelect;
	//    }

	/**
	 * @return the quantity
	 */
	public String getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the lsGoodsGroup
	 */
	public List<EquipmentsGroup> getLsGoodsGroup() {
		return lsGoodsGroup;
	}

	/**
	 * @param lsGoodsGroup the lsGoodsGroup to set
	 */
	public void setLsGoodsGroup(List<EquipmentsGroup> lsGoodsGroup) {
		this.lsGoodsGroup = lsGoodsGroup;
	}

	/**
	 * @return the setLsData
	 */
	public List<StockGoodsInvSerialDTO> getSetLsData() {
		return setLsData;
	}

	/**
	 * @param setLsData the setLsData to set
	 */
	public void setSetLsData(List<StockGoodsInvSerialDTO> setLsData) {
		this.setLsData = setLsData;
	}

	/**
	 * @return the setLsDataSelect
	 */
	public List<StockGoodsInvSerialDTO> getSetLsDataSelect() {
		return setLsDataSelect;
	}

	/**
	 * @param setLsDataSelect the setLsDataSelect to set
	 */
	public void setSetLsDataSelect(List<StockGoodsInvSerialDTO> setLsDataSelect) {
		this.setLsDataSelect = setLsDataSelect;
	}

	/**
	 * @return the setLsDataSelectAll
	 */
	public List<StockGoodsInvSerialDTO> getSetLsDataSelectAll() {
		return setLsDataSelectAll;
	}

	/**
	 * @param setLsDataSelectAll the setLsDataSelectAll to set
	 */
	public void setSetLsDataSelectAll(List<StockGoodsInvSerialDTO> setLsDataSelectAll) {
		this.setLsDataSelectAll = setLsDataSelectAll;
	}

	/**
	 * @return the selectedProfile
	 */
	public EquipmentsProfile getSelectedProfile() {
		return selectedProfile;
	}

	/**
	 * @param selectedProfile the selectedProfile to set
	 */
	public void setSelectedProfile(EquipmentsProfile selectedProfile) {
		this.selectedProfile = selectedProfile;
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


	private List<Boolean> visibleTable2 = Arrays.asList(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
			true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true);
	public void onToggleActiveEtag2(ToggleEvent e) {
		visibleTable2.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
	}

	public List<Boolean> getVisibleTable2() {
		return visibleTable2;
	}

	public void setVisibleTable2(List<Boolean> visibleTable2) {
		this.visibleTable2 = visibleTable2;
	}


	private List<Boolean> visibleTable3 = Arrays.asList(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
			true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true);
	public void onToggleActiveEtag3(ToggleEvent e) {
		visibleTable3.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
	}

	public List<Boolean> getVisibleTable3() {
		return visibleTable3;
	}

	public void setVisibleTable3(List<Boolean> visibleTable3) {
		this.visibleTable3 = visibleTable3;
	}



	private List<Boolean> visibleTable4 = Arrays.asList(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
			true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true);
	public void onToggleActiveEtag4(ToggleEvent e) {
		visibleTable4.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
	}

	public List<Boolean> getVisibleTable4() {
		return visibleTable4;
	}

	public void setVisibleTable4(List<Boolean> visibleTable4) {
		this.visibleTable4 = visibleTable4;
	}
}
