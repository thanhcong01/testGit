///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.ftu.inventory.bean;
//
//import com.ftu.language.LanguageBean;
//import com.ftu.constanst.InventoryConstanst;
//import com.ftu.inventory.bo.Goods;
//import com.ftu.inventory.bo.GoodsGroup;
//import com.ftu.inventory.bo.StockGoodsInvSerial;
//import com.ftu.inventory.bo.StockTransaction;
//import com.ftu.inventory.bo.StockTransactionDetail;
//import com.ftu.inventory.bo.TransactionAction;
//import com.ftu.inventory.bo.TransactionActionDetail;
//import com.ftu.inventory.exportExcel.ExportExcel;
//import com.ftu.java.bo.SerialA;
//import com.ftu.staff.bo.ApDomain;
//import com.ftu.staff.bo.Shop;
//import com.ftu.staff.bo.Staff;
//import com.ftu.utils.AnalysisSerial;
//import com.ftu.utils.StringUtils;
//
//import java.io.FileInputStream;
//import java.io.Serializable;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import javax.annotation.PostConstruct;
//import javax.faces.application.NavigationHandler;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.ManagedProperty;
//import javax.faces.bean.SessionScoped;
//import javax.faces.context.FacesContext;
//import org.primefaces.context.RequestContext;
//import org.primefaces.event.SelectEvent;
//import org.primefaces.model.DefaultStreamedContent;
//import org.primefaces.model.StreamedContent;
//
///**
// *
// * @author E5420
// */
//@ManagedBean(eager = true)
//@SessionScoped
//public class ExEShopExGoods implements Serializable {
//
//	SimpleDateFormat sf = new SimpleDateFormat(".yy.MM.dd.");
//	private String orderCode;
//	private List<ApDomain> listReason = new ArrayList<>();
//	private Long reasonId;
//	private Long typeId;
//	private List<ApDomain> listTransType;
//	@ManagedProperty(value = "#{sessionBean}")
//	private SessionBean sessionBean;
//	@ManagedProperty(value = "#{languageBean}")
//	private LanguageBean languageBean;
//	private Shop shop;
//	private Staff staff;
//	private Long goodsId;
//	private Long goodStatus;
//	private String fromSerial;
//	private String toSerial;
//	private String quantity;
//	private List<StockGoodsInvSerial> lsData;
//	private StockTransactionDetail stDetail = new StockTransactionDetail();
//	private StockTransaction st;
//	private TransactionAction ta;
//	private String goodsName;
//	private String goodsStatusName;
//	private Boolean disableReason;
//	private Boolean disableAdd;
//	private Boolean disablePrint;
//	private Long goodsGroupId;
//	private List<Goods> lsGoods;
//	List<GoodsGroup> lsGoodsGroup;
//	private String goods;
//	private int sizers;
//	private List<SerialA> setAnaSerial;
//	private List<SerialA> setLsData;
//	private List<SerialA> setLsDataSelect;
//	private String description;
//	
//	private transient StreamedContent fileExport;
//
//	@PostConstruct
//	public void init() {
//		disableAdd = true;
//		disablePrint = true;
//		setShop(getSessionBean().getShop());
//		setStaff(getSessionBean().getStaff());
//		setSt(new StockTransaction(null, staff.getShopId(), null, shop.getShopId(),
//				InventoryConstanst.StockTransactionType.TRANS, InventoryConstanst.StockTransactionStatus.SUCCESS,
//				InventoryConstanst.StockTransactionDeliveryType.TRANS));
//		listTransType = getSessionBean().getService().getListExSubShopReasons();
//		lsGoodsGroup = sessionBean.getLsgGroup();
//		changeType();
//		changeGoodsGroup();
//	}
//
//	public void changeGoodsGroup() {
//		if (lsGoodsGroup != null && !lsGoodsGroup.isEmpty() && (goodsGroupId == null || goodsGroupId == 0L)) {
//			setGoodsGroupId(lsGoodsGroup.get(0).getGoodsGroupId());
//		}
//		goods = "";
//		goodsId = null;
//		if (getGoodsGroupId() != null && getGoodsGroupId() > 0) {
//			List<Goods> lsg = sessionBean.getLsGoods();
//			lsGoods = new ArrayList<>();
//			if (lsg != null && !lsg.isEmpty()) {
//				for (Goods g : lsg) {
//					if (goodsGroupId.equals(g.getGoodsGroupId())) {
//						lsGoods.add(g);
//					}
//				}
//			}
//		}
//	}
//
//	public void goodsSelect() {
//		if (lsGoods != null) {
//			for (Goods g : lsGoods) {
//				if ((g.getGoodsCode() + "-" + g.getProfileName()).equals(goods)) {
//					goodsId = g.getGoodsId();
//					break;
//				}
//			}
//		}
//	}
//
//	public List<String> completeGoods(String gs) {
//		goods = gs;
//		List<String> rs = new ArrayList<>();
//		if (lsGoods != null && !lsGoods.isEmpty()) {
//			for (Goods g : lsGoods) {
//				if (gs == null || gs.isEmpty() || g.getGoodsCode().toLowerCase().contains(goods.toLowerCase())) {
//					rs.add(g.getGoodsCode() + "-" + g.getProfileName());
//				}
//			}
//		}
//		return rs;
//	}
//
//	public void changeType() {
//		if (listTransType != null && !listTransType.isEmpty()) {
//			if (typeId == null || typeId == 0L) {
//				typeId = listTransType.get(0).getValue();
//			}
//		}
//		if (typeId != null && typeId > 0) {
//			goodStatus = typeId.toString().equals(InventoryConstanst.TransactionType.EX_PARENT_ERR)
//					? InventoryConstanst.GoodsStatus.ERROR : InventoryConstanst.GoodsStatus.NOMAL;
//			orderCode = "EX_" + typeId + sf.format(new Date())
//					+ sessionBean.getService().getTransactionActionCount(typeId.toString()).toString();
//			listReason = sessionBean.getService().getListReason(typeId.toString());
//			if (listReason != null) {
//				reasonId = listReason.get(0).getValue();
//			}
//		} else {
//			goodStatus = 0L;
//			orderCode = "";
//		}
//		lsData = null;
//	}
//
//	public void refreshStd() {
//		if (stDetail != null) {
//			stDetail.setSetSerial(new HashSet<StockGoodsInvSerial>());
//		}
//	}
//
//	public void search() {
//		Integer quan = null;
//		if (listTransType == null || listTransType.isEmpty() || sessionBean.getLsGoods() == null
//				|| sessionBean.getLsGoods().isEmpty()) {
//			return;
//		}
//		typeId = (typeId == null || typeId < 1) ? listTransType.get(0).getValue() : typeId;
//		changeType();
//		if (fromSerial != null && !fromSerial.trim().isEmpty() && !fromSerial.matches("^[0-9A-Fa-f]+$")) {
//
//			languageBean.showMeseage("Info", "fromSerialReq");
//			return;
//
//		}
//		if (toSerial != null && !toSerial.trim().isEmpty() && !toSerial.matches("^[0-9A-Fa-f]+$")) {
//
//			languageBean.showMeseage("Info", "toSerialReq");
//			return;
//
//		}
//		if (quantity == null || quantity.trim().isEmpty()) {
//			languageBean.showMeseage("Info", "qttReqErr");
//			return;
//		} else {
//			if (!quantity.matches("^[0-9]+$")) {
//
//				languageBean.showMeseage("Info", "qttReqErr2");
//				return;
//
//			} else {
//				quan = Integer.parseInt(getQuantity());
//				if (quan <= 0) {
//					languageBean.showMeseage("Info", "qttReqErr2");
//					return;
//				}
//			}
//		}
//		StockGoodsInvSerial sgis = new StockGoodsInvSerial("", getGoodStatus(), InventoryConstanst.StockStatus.INSTOCK,
//				shop.getShopId(), null, getGoodsId());
//		setLsData(
//				sessionBean.getService().searchStockGoodsInvSerial(fromSerial, toSerial, sgis, goodsGroupId, 0, quan));
//		if (lsData == null || lsData.isEmpty()) {
//			languageBean.showMeseage("Info", "noRecordFound");
//		}
//		lsData.removeAll(stDetail.getSetSerial());
//	}
//
//	public StockTransactionDetail getByGoodsId(Long gId, Long gsts) {
//		for (StockTransactionDetail std : getSt().getLsDetail()) {
//			if (std.getGoodsId().equals(goodsId) && std.getGoodsStatus().equals(gsts)) {
//				return std;
//			}
//		}
//		StockTransactionDetail std = new StockTransactionDetail(0L, gId, gsts);
//		getSt().getLsDetail().add(std);
//		return std;
//	}
//
//	public void add() {
//		if (setLsDataSelect == null || setLsDataSelect.isEmpty()) {
//			languageBean.showMeseage("Info", "chooseReq", new Object[] { "Serial" });
//			return;
//		}
//		List<StockGoodsInvSerial> lsg = new ArrayList<>();
//
//		for (SerialA s : setLsDataSelect) {
//			for (StockGoodsInvSerial sg : lsData) {
//				if (sg.getProviderId().equals(s.getProviderId())
//						&& StringUtils.compareHexStrings(sg.getSerialSearch(), s.getFromSerialSearch())
//						&& StringUtils.compareHexStrings(s.getToSerialSearch(), sg.getSerialSearch())) {
//					lsg.add(sg);
//				}
//			}
//		}
//		stDetail = getByGoodsId(lsData.get(0).getGoodsId(), lsData.get(0).getGoodsStatus());
//		stDetail.getSetSerial().addAll(lsg);
//		lsData = new ArrayList<>();
//		Long qt = 0L + stDetail.getSetSerial().size();
//		stDetail.setQuantity(qt);
//		// lsData = new ArrayList<>();
//		fromSerial = "";
//		toSerial = "";
//	}
//
//	public void addAll() {
//		if (lsData == null || lsData.isEmpty()) {
//			languageBean.showMeseage("Info", "chooseReq", new Object[] { "Serial" });
//			return;
//		}
//
//		stDetail = getByGoodsId(lsData.get(0).getGoodsId(), lsData.get(0).getGoodsStatus());
//		stDetail.getSetSerial().addAll(lsData);
//		lsData = new ArrayList<>();
//		Long qt = 0L + stDetail.getSetSerial().size();
//		stDetail.setQuantity(qt);
//		// lsData = new ArrayList<>();
//		fromSerial = "";
//		toSerial = "";
//	}
//
//	public void detailInfo() {
//		changeType();
//		if (goodsId == null || goodsId == 0L) {
//			languageBean.showMeseage("Info", "importReqView");
//			return;
//		}
//		stDetail = getByGoodsId(goodsId, goodStatus);
//		fromSerial = "";
//		toSerial = "";
//		quantity = "1";
//		FacesContext fc = FacesContext.getCurrentInstance();
//		NavigationHandler nh = fc.getApplication().getNavigationHandler();
//		nh.handleNavigation(fc, null, "/ex-eshop-ex-goods-sub.xhtml?faces-redirect=false");
//	}
//
//	public void viewClick(SelectEvent event) {
//		stDetail = (StockTransactionDetail) event.getObject();
//		fromSerial = "";
//		toSerial = "";
//		quantity = "1";
//		FacesContext fc = FacesContext.getCurrentInstance();
//		NavigationHandler nh = fc.getApplication().getNavigationHandler();
//		nh.handleNavigation(fc, null, "/ex-eshop-ex-goods-sub.xhtml?faces-redirect=false");
//	}
//
//	public void refresh() {
//		disablePrint = true;
//		disableAdd = true;
//		setSt(new StockTransaction(null, staff.getShopId(), null, shop.getShopId(),
//				InventoryConstanst.StockTransactionType.TRANS, InventoryConstanst.StockTransactionStatus.SUCCESS,
//				InventoryConstanst.StockTransactionDeliveryType.TRANS));
//		getSt().setLsDetail(new ArrayList<StockTransactionDetail>());
//		stDetail = new StockTransactionDetail();
//		changeType();
//	}
//
//	public String exitSub() {
//		if (stDetail.getSetSerial().isEmpty()) {
//			st.getLsDetail().remove(stDetail);
//		}
//		stDetail = new StockTransactionDetail();
//		return "ex-eshop-ex-goods";
//	}
//
//	public void removeRs(StockGoodsInvSerial gs) {
//		getStDetail().getSetSerial().remove(gs);
//		stDetail.setQuantity(stDetail.getQuantity() - 1);
//	}
//
//	public void removeDtRs(StockTransactionDetail gs) {
//		getSt().getLsDetail().remove(gs);
//		if (stDetail != null && gs.getRowkey() == stDetail.getRowkey()) {
//			stDetail = null;
//		}
//	}
//
//	public String checkSerial() {
//		if (getSt() == null || getSt().getLsDetail().isEmpty()) {
//			disableAdd = true;
//			return "Stock detail is empty !";
//		}
//		for (StockTransactionDetail item : getSt().getLsDetail()) {
//			if (item.getSetSerial() == null || item.getSetSerial().isEmpty()) {
//				disableAdd = true;
//				return "Detail " + item.getProfileName() + " has no Serial";
//			}
//		}
//		disableAdd = false;
//		return null;
//	}
//
//	public void action() {
//		changeType();
//		if (getSt().getLsDetail().isEmpty()) {
//			return;
//		} else {
//			String s = checkSerial();
//			if (s != null) {
//				return;
//			}
//			if (typeId < 1L || reasonId == null || reasonId < 1L) {
//				return;
//			}
//		}
//		try {
//
//			for (StockTransactionDetail std : st.getLsDetail()) {
//				if (std.getSetSerial().isEmpty()) {
//					std.getLsSerial().remove(std);
//				}
//			}
//			ta.setDescription(description != null && !description.trim().isEmpty()
//					? staff.getUserName() + ": " + description : "");
//			boolean k = sessionBean.getService().exEShop(ta, getSt(),staff.getStaffId());
//			if (k) {
//				getLanguageBean().showMeseage("success", "succesProces");
//			} else {
//				getLanguageBean().showMeseage("Info", "errorProcess");
//			}
////			setSt(new StockTransaction(null, staff.getShopId(), null, shop.getShopId(),
////					InventoryConstanst.StockTransactionType.TRANS, InventoryConstanst.StockTransactionStatus.SUCCESS,
////					InventoryConstanst.StockTransactionDeliveryType.TRANS));
//			disablePrint = false;
//			disableAdd = true;
//			//refresh();
//			RequestContext context = RequestContext.getCurrentInstance();
//			context.execute("PF('cfExSReq').hide();");
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			getLanguageBean().showMeseage("error", "errorProcess");
//		}
//	}
//
//	public void save() {
//		changeType();
//		if (getSt().getLsDetail().isEmpty()) {
//			return;
//		} else {
//			String s = checkSerial();
//			if (s != null) {
//				languageBean.showMeseage_("Info", s);
//				return;
//			}
//			if (typeId < 1L || reasonId == null || reasonId < 1L) {
//				languageBean.showMeseage("Info", "exrequireReason");
//				return;
//			}
//			description = "";
//			ta = new TransactionAction(orderCode, typeId.toString(), staff.getStaffId(), staff.getShopId(),
//					InventoryConstanst.TransactionStatus.CREATE, shop.getShopId(), shop.getShopParentId());
//			ta.setReasonId(reasonId);
//			ta.setReasonName(getReasonName(reasonId));
//			RequestContext context = RequestContext.getCurrentInstance();
//			context.execute("PF('cfExSReq').show();");
//		}
//	}
//	
//	public void export(){
//		if (getSt().getLsDetail().isEmpty()) {
//			languageBean.showMeseage("Info", "NoDetailReq");
//		
//			return;
//		}
//    	try {
//    			Long sumQuantity = 0L;
//    			int index = 0;
//    			HashMap<String, TransactionActionDetail> mapDetail = new HashMap<>();
//    			for (StockTransactionDetail std : getSt().getLsDetail()) {
//    				sumQuantity += std.getQuantity();
//    				
//    				for (StockGoodsInvSerial sgi : std.getSetSerial()) {
//    					TransactionActionDetail tad = mapDetail.get(sgi.getProviderId().toString()+"-"+sgi.getGoodsId().toString());
//    					if (tad == null) {
//    						tad = new TransactionActionDetail(sgi.getGoodsId(), sgi.getProviderId());
//    						tad.setIndex(++index);
//    						tad.setProviderName(sessionBean.getService().getProviderName(sgi.getProviderId()));
//    						tad.setGoodsGroupName(sessionBean.getGoodsGroupNameByGoods(sgi.getGoodsId().toString()));
//    						tad.setGoodsCode(sessionBean.getGoodsCode(sgi.getGoodsId().toString()));
//    						tad.setProfileName(sessionBean.getProfileName(sgi.getGoodsId().toString()));
//    						tad.setGoodsStatusName(sessionBean.getService().getGoodsStatusName(sgi.getGoodsStatus().toString()));
//    						mapDetail.put(sgi.getProviderId().toString()+"-"+sgi.getGoodsId().toString(), tad);
//    					}
//    					tad.setQuantity(tad.getQuantity() + 1);
//    				}
//    			}
//    			TransactionAction tmp = new TransactionAction(orderCode, typeId.toString(), staff.getStaffId(), staff.getShopId(),
//    					InventoryConstanst.TransactionStatus.EX_ES, null,null);
//
//    			tmp.setAssessmentStaffId(staff.getStaffId());
//    			
//    			tmp.setReasonId(reasonId);
//    			tmp.setReasonName(getReasonName(reasonId));
//    			tmp.setDescription(description != null && !description.trim().isEmpty() ? staff.getUserName() + ": " + description : "");
//    			tmp.setCreateDatetime(new Date());
//    			tmp.setAssessmentDatetime(new Date());
//			ExportExcel ee = new ExportExcel();
//			String s = ee.printTransactionActionDetail(shop.getShopName(),sessionBean.getService().getShopNameById(shop.getShopParentId()), new ArrayList<TransactionActionDetail>(mapDetail.values()), tmp, sumQuantity);
//			
//			setFileExport(new DefaultStreamedContent(new FileInputStream(s), "xlsx", "phieu_xuat_kho.xlsx"));
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//    }
//
//	private String getReasonName(Long reasonId) {
//		if (listReason != null) {
//			for (ApDomain d : listReason) {
//				if (d.getValue().equals(reasonId)) {
//					return d.getName();
//				}
//			}
//		}
//		return "";
//	}
//
//	/**
//	 * @return the reasonId
//	 */
//	public Long getReasonId() {
//		return reasonId;
//	}
//
//	/**
//	 * @param reasonId
//	 *            the reasonId to set
//	 */
//	public void setReasonId(Long reasonId) {
//		this.reasonId = reasonId;
//	}
//
//	/**
//	 * @return the orderCode
//	 */
//	public String getOrderCode() {
//		return orderCode;
//	}
//
//	/**
//	 * @param orderCode
//	 *            the orderCode to set
//	 */
//	public void setOrderCode(String orderCode) {
//		this.orderCode = orderCode;
//	}
//
//	/**
//	 * @return the listReason
//	 */
//	public List<ApDomain> getListReason() {
//		return listReason;
//	}
//
//	/**
//	 * @param listReason
//	 *            the listReason to set
//	 */
//	public void setListReason(List<ApDomain> listReason) {
//		this.listReason = listReason;
//	}
//
//	/**
//	 * @return the sessionBean
//	 */
//	public SessionBean getSessionBean() {
//		return sessionBean;
//	}
//
//	/**
//	 * @param sessionBean
//	 *            the sessionBean to set
//	 */
//	public void setSessionBean(SessionBean sessionBean) {
//		this.sessionBean = sessionBean;
//	}
//
//	/**
//	 * @return the languageBean
//	 */
//	public LanguageBean getLanguageBean() {
//		return languageBean;
//	}
//
//	/**
//	 * @param languageBean
//	 *            the languageBean to set
//	 */
//	public void setLanguageBean(LanguageBean languageBean) {
//		this.languageBean = languageBean;
//	}
//
//	/**
//	 * @return the shop
//	 */
//	public Shop getShop() {
//		return shop;
//	}
//
//	/**
//	 * @param shop
//	 *            the shop to set
//	 */
//	public void setShop(Shop shop) {
//		this.shop = shop;
//	}
//
//	/**
//	 * @return the staff
//	 */
//	public Staff getStaff() {
//		return staff;
//	}
//
//	/**
//	 * @param staff
//	 *            the staff to set
//	 */
//	public void setStaff(Staff staff) {
//		this.staff = staff;
//	}
//
//	/**
//	 * @return the stDetail
//	 */
//	public StockTransactionDetail getStDetail() {
//		return stDetail;
//	}
//
//	/**
//	 * @param stDetail
//	 *            the stDetail to set
//	 */
//	public void setStDetail(StockTransactionDetail stDetail) {
//		this.stDetail = stDetail;
//	}
//
//	/**
//	 * @return the goodsId
//	 */
//	public Long getGoodsId() {
//		return goodsId;
//	}
//
//	/**
//	 * @param goodsId
//	 *            the goodsId to set
//	 */
//	public void setGoodsId(Long goodsId) {
//		this.goodsId = goodsId;
//	}
//
//	/**
//	 * @return the goodStatus
//	 */
//	public Long getGoodStatus() {
//		return goodStatus;
//	}
//
//	/**
//	 * @param goodStatus
//	 *            the goodStatus to set
//	 */
//	public void setGoodStatus(Long goodStatus) {
//		this.goodStatus = goodStatus;
//	}
//
//	/**
//	 * @return the fromSerial
//	 */
//	public String getFromSerial() {
//		return fromSerial;
//	}
//
//	/**
//	 * @param fromSerial
//	 *            the fromSerial to set
//	 */
//	public void setFromSerial(String fromSerial) {
//		this.fromSerial = fromSerial;
//	}
//
//	/**
//	 * @return the toSerial
//	 */
//	public String getToSerial() {
//		return toSerial;
//	}
//
//	/**
//	 * @param toSerial
//	 *            the toSerial to set
//	 */
//	public void setToSerial(String toSerial) {
//		this.toSerial = toSerial;
//	}
//
//	/**
//	 * @return the lsData
//	 */
//	public List<StockGoodsInvSerial> getLsData() {
//		return lsData;
//	}
//
//	/**
//	 * @param lsData
//	 *            the lsData to set
//	 */
//	public void setLsData(List<StockGoodsInvSerial> lsData) {
//		this.lsData = lsData;
//	}
//
//	/**
//	 * @return the st
//	 */
//	public StockTransaction getSt() {
//		if (st != null) {
//			for (StockTransactionDetail std : st.getLsDetail()) {
//				if (std.getSetSerial().isEmpty() && !std.getGoodsId().equals(stDetail.getGoodsId())) {
//					st.getLsDetail().remove(std);
//				}
//			}
//		}
//		return st;
//	}
//
//	/**
//	 * @param st
//	 *            the st to set
//	 */
//	public void setSt(StockTransaction st) {
//		this.st = st;
//	}
//
//	/**
//	 * @return the ta
//	 */
//	public TransactionAction getTa() {
//		return ta != null ? ta : new TransactionAction();
//	}
//
//	/**
//	 * @param ta
//	 *            the ta to set
//	 */
//	public void setTa(TransactionAction ta) {
//		this.ta = ta;
//	}
//
//	/**
//	 * @return the goodsName
//	 */
//	public String getProfileName() {
//		return goodsName;
//	}
//
//	/**
//	 * @param goodsName
//	 *            the goodsName to set
//	 */
//	public void setProfileName(String goodsName) {
//		this.goodsName = goodsName;
//	}
//
//	/**
//	 * @return the goodsStatusName
//	 */
//	public String getGoodsStatusName() {
//		return goodsStatusName;
//	}
//
//	/**
//	 * @param goodsStatusName
//	 *            the goodsStatusName to set
//	 */
//	public void setGoodsStatusName(String goodsStatusName) {
//		this.goodsStatusName = goodsStatusName;
//	}
//
//	/**
//	 * @return the disableReason
//	 */
//	public Boolean getDisableReason() {
//		if (getSt() == null || getSt().getLsDetail() == null || getSt().getLsDetail().isEmpty()) {
//			disableReason = false;
//		} else {
//			disableReason = true;
//		}
//		return disableReason;
//	}
//
//	/**
//	 * @param disableReason
//	 *            the disableReason to set
//	 */
//	public void setDisableReason(Boolean disableReason) {
//		this.disableReason = disableReason;
//	}
//
//	/**
//	 * @return the lsGoods
//	 */
//	public List<Goods> getLsGoods() {
//		return lsGoods;
//	}
//
//	/**
//	 * @param lsGoods
//	 *            the lsGoods to set
//	 */
//	public void setLsGoods(List<Goods> lsGoods) {
//		this.lsGoods = lsGoods;
//	}
//
//	/**
//	 * @return the goodsGroupId
//	 */
//	public Long getGoodsGroupId() {
//		return goodsGroupId;
//	}
//
//	/**
//	 * @param goodsGroupId
//	 *            the goodsGroupId to set
//	 */
//	public void setGoodsGroupId(Long goodsGroupId) {
//		this.goodsGroupId = goodsGroupId;
//	}
//
//	/**
//	 * @return the disableAdd
//	 */
//	public Boolean getDisableAdd() {
//
//		return checkSerial() != null;
//	}
//
//	/**
//	 * @param disableAdd
//	 *            the disableAdd to set
//	 */
//	public void setDisableAdd(Boolean disableAdd) {
//		this.disableAdd = disableAdd;
//	}
//
//	public int getSizers() {
//		if (stDetail == null || stDetail.getSetSerial().isEmpty()) {
//			sizers = 0;
//		} else {
//			sizers = stDetail.getSetSerial().size();
//		}
//		return sizers;
//	}
//
//	/**
//	 * @param sizers
//	 *            the sizers to set
//	 */
//	public void setSizers(int sizers) {
//		this.sizers = sizers;
//	}
//
//	public List<SerialA> getSetAnaSerial() {
//		setAnaSerial = new ArrayList<>();
//		if (stDetail != null && !stDetail.getSetSerial().isEmpty()) {
//			AnalysisSerial an = new AnalysisSerial(null, new ArrayList<>(stDetail.getSetSerial()));
//			setAnaSerial = an.analysisInvSerial();
//		}
//		return setAnaSerial;
//	}
//
//	/**
//	 * @param setAnaSerial
//	 *            the setAnaSerial to set
//	 */
//	public void setSetAnaSerial(List<SerialA> setAnaSerial) {
//		this.setAnaSerial = setAnaSerial;
//	}
//
//	/**
//	 * @return the listTransType
//	 */
//	public List<ApDomain> getListTransType() {
//		return listTransType;
//	}
//
//	/**
//	 * @param listTransType
//	 *            the listTransType to set
//	 */
//	public void setListTransType(List<ApDomain> listTransType) {
//		this.listTransType = listTransType;
//	}
//
//	/**
//	 * @return the typeId
//	 */
//	public Long getTypeId() {
//		return typeId;
//	}
//
//	/**
//	 * @param typeId
//	 *            the typeId to set
//	 */
//	public void setTypeId(Long typeId) {
//		this.typeId = typeId;
//	}
//
//	/**
//	 * @return the setLsData
//	 */
//	public List<SerialA> getSetLsData() {
//		setLsData = new ArrayList<>();
//		if (lsData != null && !lsData.isEmpty()) {
//			AnalysisSerial an = new AnalysisSerial(null, new ArrayList<>(lsData));
//			setLsData = an.analysisInvSerial();
//		}
//		return setLsData;
//	}
//
//	/**
//	 * @param setLsData
//	 *            the setLsData to set
//	 */
//	public void setSetLsData(List<SerialA> setLsData) {
//		this.setLsData = setLsData;
//	}
//
//	/**
//	 * @return the goods
//	 */
//	public String getGoods() {
//		return goods;
//	}
//
//	/**
//	 * @param goods
//	 *            the goods to set
//	 */
//	public void setGoods(String goods) {
//		this.goods = goods;
//	}
//
//	/**
//	 * @return the setLsDataSelect
//	 */
//	public List<SerialA> getSetLsDataSelect() {
//		return setLsDataSelect;
//	}
//
//	/**
//	 * @param setLsDataSelect
//	 *            the setLsDataSelect to set
//	 */
//	public void setSetLsDataSelect(List<SerialA> setLsDataSelect) {
//		this.setLsDataSelect = setLsDataSelect;
//	}
//
//	/**
//	 * @return the quantity
//	 */
//	public String getQuantity() {
//		return quantity;
//	}
//
//	/**
//	 * @param quantity
//	 *            the quantity to set
//	 */
//	public void setQuantity(String quantity) {
//		this.quantity = quantity;
//	}
//
//	/**
//	 * @return the description
//	 */
//	public String getDescription() {
//		return description;
//	}
//
//	/**
//	 * @param description
//	 *            the description to set
//	 */
//	public void setDescription(String description) {
//		this.description = description;
//	}
//
//	public StreamedContent getFileExport() {
//		return fileExport;
//	}
//
//	public void setFileExport(StreamedContent fileExport) {
//		this.fileExport = fileExport;
//	}
//
//	/**
//	 * @return the disablePrint
//	 */
//	public Boolean getDisablePrint() {
//		return disablePrint;
//	}
//
//	/**
//	 * @param disablePrint the disablePrint to set
//	 */
//	public void setDisablePrint(Boolean disablePrint) {
//		this.disablePrint = disablePrint;
//	}
//}
