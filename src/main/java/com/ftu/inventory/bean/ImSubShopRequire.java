/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bean;

import com.ftu.language.LanguageBean;
import com.ftu.constanst.InventoryConstanst;
import com.ftu.inventory.bo.EquipmentsProfile;
import com.ftu.inventory.bo.EquipmentsGroup;
import com.ftu.inventory.bo.TransactionAction;
import com.ftu.inventory.bo.TransactionActionDetail;
import com.ftu.staff.bo.ApDomain;
import com.ftu.staff.bo.Shop;
import com.ftu.staff.bo.Staff;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;

/**
 *
 * @author E5420
 */
@ManagedBean(eager = true)
@ViewScoped
public class ImSubShopRequire implements Serializable {

    SimpleDateFormat sf = new SimpleDateFormat(".yy.MM.dd.");
    private String orderCode;

    @ManagedProperty(value = "#{sessionBean}")
    private SessionBean sessionBean;
    @ManagedProperty(value = "#{languageBean}")
    private LanguageBean languageBean;
    @ManagedProperty(value = "#{transactionNotificationBean}")
    private TransactionNotificationBean transactionNotificationBean;
    private List<ApDomain> listReason = new ArrayList<>();
    private Long reasonId;
    private List<ApDomain> transType;
    private Long typeId;
    private TransactionAction ta = new TransactionAction();
    private Long goodsId;
    private String quantity;
    private String equipmentName;
    private String unit;
    HashMap<Long, TransactionActionDetail> mapDetail = new HashMap<>();
    Staff staff;
    private Long goodsGroupId;
    private List<EquipmentsProfile> lsGoods;
    List<EquipmentsGroup> lsGoodsGroup;
    List<Shop> listShop;
    private Boolean disableAdd;
    private String description;
    private String goods;
    private String fromShop;
    private Long shopId;
    private Shop shopParent;
    private Date actionDate;

    @PostConstruct
    public void init() {
    	sessionBean.loadData(sessionBean.getStaff(), sessionBean.getShop());
    	actionDate = new Date();
        staff = getSessionBean().getStaff();

        transType = getSessionBean().getService().getImReasonsNC();
        lsGoodsGroup = sessionBean.getLsgGroupActive();
        changeType();
        changeGoodsGroup();
        listShop = sessionBean.getService().getListShop(getSessionBean().getShop().getShopParentId(),getSessionBean().getShop().getShopId());
        List<Shop> lsShopRemove = new ArrayList<>();
        if(listShop!=null && !listShop.isEmpty()){
            for (Shop s:listShop ) {
                if(InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(s.getShopStatus())){
                    lsShopRemove.add(s);
                }
            }
            listShop.removeAll(lsShopRemove);
        }
        shopParent = sessionBean.getService().getShopChildById(getSessionBean().getShop().getShopParentId());
    }

    public void changeGoodsGroup() {
        if (lsGoodsGroup != null && !lsGoodsGroup.isEmpty() && (goodsGroupId == null || goodsGroupId == 0L)) {
            setGoodsGroupId(lsGoodsGroup.get(0).getEquipmentsGroupId());
        }
        if (goodsGroupId != null && goodsGroupId > 0) {
            List<EquipmentsProfile> lsg = sessionBean.getLsEquipments();
            lsGoods = new ArrayList<>();
            if (lsg != null && !lsg.isEmpty()) {
                for (EquipmentsProfile g : lsg) {
                    if(InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(g.getEquipProfileStatus())){
                        continue;
                    }
                    if (goodsGroupId.equals(g.getEquipmentsGroupId())) {
                        lsGoods.add(g);
                    }
                }
            }
        }
        if (!lsGoods.isEmpty()) {
            EquipmentsProfile g = lsGoods.get(0);
            goodsId = g.getProfileId();
            goods = g.getProfileCode() + " | " + g.getProfileName();
            equipmentName = g.getProfileName();
            unit = sessionBean.getUnitByUnitName(g.getUnit());
        } else {
            goodsId = 0L;
            goods = "";
            equipmentName = "";
            unit = "";
        }
    }

    public void goodsSelect() {
        if(goods==null || goods.isEmpty()){
            return;
        }
    	equipmentName = "";
    	unit="";
        goodsId = null;
        if (lsGoods != null) {
            for (EquipmentsProfile g : lsGoods) {
                if(InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(g.getEquipProfileStatus())){
                    continue;
                }
                if ((g.getProfileCode() + "-" + g.getProfileName()).equals(goods)) {
                    goodsId = g.getProfileId();
                    equipmentName = g.getProfileName();
                    unit = sessionBean.getUnitByUnitName(g.getUnit());
                    break;
                }
            }
        }
    }

    public List<String> completeGoods(String gs) {
        goods = gs;
        List<String> rs = new ArrayList<>();
        if (lsGoods != null && !lsGoods.isEmpty()) {
            for (EquipmentsProfile g : lsGoods) {
                if(InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(g.getEquipProfileStatus())){
                    continue;
                }
                if (gs == null || gs.isEmpty() || g.getProfileCode().toLowerCase().contains(goods.toLowerCase())) {
                    rs.add(g.getProfileCode() + "-" + g.getProfileName());
                }
            }
        }
        goodsSelect();
        return rs;
    }
    public List<String> completeFromShop(String gs) {
        fromShop = gs;
        List<String> rs = new ArrayList<>();
//        rs.add(" ");
        if (listShop != null && !listShop.isEmpty()) {
            for (Shop g : listShop) {
                if(InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(g.getShopStatus())){
                    continue;
                }
                String filter = g.getShopCode() +"-" + g.getShopName();
                if (gs == null || gs.isEmpty() || filter.toLowerCase().contains(fromShop.toLowerCase())) {
                    rs.add(filter);
                }
            }
        }
        if(gs.trim().isEmpty()){
            shopId = null;
        }
        return rs;
    }

    public void fromShopSelect() {
        shopId = null;
        if (listShop != null) {
            for (Shop g : listShop) {
                if(InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(g.getShopStatus())){
                    continue;
                }
                String filter = g.getShopCode() +"-" + g.getShopName();
                if (filter.equals(fromShop)) {
                    shopId = g.getShopId();
                    break;
                }
            }
        }
    }

    public void changeType() {
        if (transType != null && !transType.isEmpty()) {
            if (typeId == null || typeId == 0L) {
                typeId = transType.get(0).getValue();
            }
        }
        if (typeId != null && typeId > 0) {

            orderCode = "IM_" + typeId + sf.format(new Date()) + sessionBean.getService().getTransactionActionCount(typeId.toString()).toString();
        } else {
            orderCode = "";
        }
        listReason = sessionBean.getService().getListReason(typeId == null? "":typeId.toString());
        if (listReason != null && !listReason.isEmpty()) {
            reasonId = listReason.get(0).getValue();
        }
    }

    public void removeRs(TransactionActionDetail std) {
        TransactionActionDetail detail = mapDetail.get(std.getGoodsId());
        if (detail != null) {
            mapDetail.remove(std.getGoodsId());
        }
        List<TransactionActionDetail> ls = new ArrayList<>(mapDetail.values());
        ta.setLsDetail(ls);
    }

    public void add() {
//        if(shopId == null){
//            getLanguageBean().showMeseage("Info", "from.shop.not.empty");
//            return;
//        }
        if (goodsId == null || goodsId < 1L || quantity.trim().isEmpty() || !quantity.matches("[\\d]+$")) {
            getLanguageBean().showMeseage("Info", "imRequireAdd");
            return;
        }
        Long quan = Long.parseLong(quantity);
        if (quan < 1) {
            getLanguageBean().showMeseage("Info", "imRequireAdd");
            return;
        }
        TransactionActionDetail detail = mapDetail.get(getGoodsId());
        if (detail == null) {
            EquipmentsProfile goods = getSessionBean().getGoods(getGoodsId().toString());
            detail = new TransactionActionDetail();
            detail.setQuantity(quan);
            detail.setGoodsId(getGoodsId());
            detail.setGoodsStatus(InventoryConstanst.GoodsStatus.NOMAL);
            detail.setGoodsName(goods.getProfileName());
            detail.setGoodsCode(goods.getProfileCode());
            detail.setGoodsGroupName(sessionBean.getGoodsGroupNameByGoods(getGoodsId().toString().toString()));
            detail.setGoodsStatusName(getSessionBean().getService().getEquipsStatusName(detail.getGoodsStatus()));
            mapDetail.put(getGoodsId(), detail);
        } else {
            detail.setQuantity(detail.getQuantity() + quan);
        }
        ta.setLsDetail(new ArrayList<>(mapDetail.values()));
        setQuantity(null);
    }

    public void action() {
        changeType();
        try {
            if (ta.getLsDetail().isEmpty()) {
                return;
            }
            if (typeId < 1L || reasonId < 1L) {
                return;
            }
            if(shopId == null){
                getLanguageBean().showMeseage("Info", "from.shop.not.empty");
                return;
            }
            ta.setDescription(description != null && !description.trim().isEmpty() ? staff.getUserName() + ": " + description : "");
            sessionBean.getService().imRequire(ta, null, InventoryConstanst.StockStatus.BLOCK_NC);
            getLanguageBean().showMeseage("success", "succesProces");
            //   setSt(new StockTransaction(null, sessionBean.getShop().getShopParentId(), null, staff.getShopId(), InventoryConstanst.StockTransactionType.TRANS, InventoryConstanst.StockTransactionStatus.WAIT, InventoryConstanst.StockTransactionDeliveryType.WAIT));
            refresh();
            
            transactionNotificationBean.init();
            changeType();
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('cfImSReq').hide();");
        } catch (Exception ex) {
            ex.printStackTrace();
            getLanguageBean().showMeseage("error", "errorProcess");
        }

    }

    public void save() {
        changeType();
        if (ta.getLsDetail().isEmpty()) {
            return;
        }
        if (typeId < 1L || reasonId < 1L) {
            languageBean.showMeseage("Info", "exrequireReason");
            return;
        }
        if(shopId == null){
            getLanguageBean().showMeseage("Info", "from.shop.not.empty");
            return;
        }
        description = "";
        ta = new TransactionAction(orderCode, typeId.toString(), staff.getStaffId(), staff.getShopId(), InventoryConstanst.TransactionStatus.CREATE_R, shopId, staff.getShopId());
        if(shopId!=null) {
            ta.setFromShopId(shopId);
        }
        ta.setReasonId(reasonId);
        ta.setLsDetail(new ArrayList<>(mapDetail.values()));
        ta.setReasonName(getReasonName(reasonId));
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('cfImSReq').show();");
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

    public void refresh() {
        changeType();
        ta = new TransactionAction(orderCode, typeId.toString(), staff.getStaffId(), staff.getShopId(), InventoryConstanst.TransactionStatus.CREATE, sessionBean.getShop().getShopParentId(), staff.getShopId());
        //    setReasonId(null);
        setQuantity(null);
        setGoodsId(null);
        setGoods("");
        setEquipmentName( "");
        setUnit("");
        mapDetail = new HashMap<>();
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
     * @return the goodsId
     */
    public Long getGoodsId() {
        return goodsId;
    }

    /**
     * @param goodsId the goodsId to set
     */
    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    /**
     * @return the providerId
     */
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
     * @return the lsGoods
     */
    public List<EquipmentsProfile> getLsGoods() {
        return lsGoods;
    }

    /**
     * @param lsGoods the lsGoods to set
     */
    public void setLsGoods(List<EquipmentsProfile> lsGoods) {
        this.lsGoods = lsGoods;
    }

    /**
     * @return the disableAdd
     */
    public Boolean getDisableAdd() {
        disableAdd = false;
        if (ta == null || ta.getLsDetail().isEmpty()) {
            disableAdd = true;
        }
        return disableAdd;
    }

    /**
     * @param disableAdd the disableAdd to set
     */
    public void setDisableAdd(Boolean disableAdd) {
        this.disableAdd = disableAdd;
    }

    /**
     * @return the transType
     */
    public List<ApDomain> getTransType() {
        return transType;
    }

    /**
     * @param transType the transType to set
     */
    public void setTransType(List<ApDomain> transType) {
        this.transType = transType;
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

    /**
     * @return the ta
     */
    public TransactionAction getTa() {
        return ta;
    }

    /**
     * @param ta the ta to set
     */
    public void setTa(TransactionAction ta) {
        this.ta = ta;
    }

    public List<Shop> getListShop() {
        return listShop;
    }

    public void setListShop(List<Shop> listShop) {
        this.listShop = listShop;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Shop getShopParent() {
        return shopParent;
    }

    public void setShopParent(Shop shopParent) {
        this.shopParent = shopParent;
    }

	/**
	 * @return the actionDate
	 */
	public Date getActionDate() {
		return actionDate;
	}

	/**
	 * @param actionDate the actionDate to set
	 */
	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}

	/**
	 * @return the equipmentName
	 */
	public String getEquipmentName() {
		return equipmentName;
	}

	/**
	 * @param equipmentName the equipmentName to set
	 */
	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}

	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

    public String getFromShop() {
        return fromShop;
    }

    public void setFromShop(String fromShop) {
        this.fromShop = fromShop;
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
