/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bean;

import com.ftu.admin.consumer.entity.SessionData;
import com.ftu.constanst.InventoryConstanst;
import com.ftu.inventory.bo.*;
import com.ftu.inventory.bussiness.EquipmentHistoryDetailService;
import com.ftu.inventory.bussiness.EquipmentHistoryService;
import com.ftu.inventory.bussiness.StockGoodsService;
import com.ftu.inventory.dao.TransactionActionDAO;
import com.ftu.inventory.dto.StockGoodsInvSerialDTO;
import com.ftu.java.business.EquipmentsProfileService;
import com.ftu.language.LanguageBean;
import com.ftu.staff.bo.ApDomain;
import com.ftu.staff.bo.Position;
import com.ftu.staff.bo.Shop;
import com.ftu.staff.bo.Staff;
import com.ftu.staff.bussiness.AppDomainService;
import com.ftu.staff.bussiness.PositionService;
import com.ftu.staff.bussiness.ShopService;
import com.ftu.utils.StringUtils;
import com.ftu.ws.BusinessService;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.*;

/**
 *
 * @author E5420
 */
@ManagedBean(name = "searchEquipmentBean", eager = true)
@ViewScoped
public class SearchEquipmentBean implements Serializable {

    private List<StockGoodsInvSerialDTO> lsData = new ArrayList<>();
    private List<StockGoodsInvSerialDTO> lsDataSelect = new ArrayList<>();
    private StockGoodsInvSerialDTO dataSelect;
    private List<StockGoodsInvSerialDTO> lsDataEquiment;
    private List<EquipmentHistory> lsDataAudit;
    private List<StockGoodsInvSerialDTO> lsDataMaintain;

    private Set<StockGoodsInvSerialDTO> lsDataRs = new HashSet<>();
    private Set<StockGoodsInvSerialDTO> lsDataRsSelecteds = new HashSet<>();
    @ManagedProperty(value = "#{sessionBean}")
    private SessionBean sessionBean;
    @ManagedProperty(value = "#{languageBean}")
    private LanguageBean languageBean;
    private List<EquipmentsProfile> lsGoods;
//    List<EquipmentsGroup> lsEquipmentGroup;
    private List<ApDomain> listEvalueType;
    private List<ApDomain> listReasons;
    private List<ApDomain> listStockStatus;
    private List<ApDomain> lstGroupType;
    private List<TransactionAction> lstTransactionAction;
    List<ApDomain> listActionType;
    List<ApDomain> listMaintenStatus;
    List<ApDomain> listWarantytatus;
    List<ApDomain> listSearchStocktatus;
    List<ApDomain> lstEquipmentStatus;
    List<StockGoodsInvNoSerial> lstStockGoodsInvNoSerial;
    private List<Shop> listShop;
    private Integer index = 0;
    private String createShop;
    private Long shopId;
    private List<EquipmentsGroup> lsgGroup;
    private List<Position> lsPosition;
    List<EquipmentHistoryDetail> lsHisDetail;
    private ShopService shopService = new ShopService();
    private List<ApDomain> listTransType;
    private BusinessService businessService = new BusinessService();
    private StockGoodsService stockGoodsService  = new StockGoodsService();
    private EquipmentHistoryService equipmentHistoryService = new EquipmentHistoryService();
    private EquipmentsProfileService equipmentsProfileService = new EquipmentsProfileService();
    private StockGoodsInvSerialDTO stockGoodsInvForm = new StockGoodsInvSerialDTO();
    private EquipmentHistoryDetailService equipmentHistoryDetailService = new EquipmentHistoryDetailService();
    private List<TransactionAction> lstTranAction = new ArrayList<>();
    @PostConstruct
    public void init() {
    	sessionBean.loadData(sessionBean.getStaff(), sessionBean.getShop());
//        lsProviders = sessionBean.getLsProvider();
//        lsEquipmentGroup = sessionBean.getLsgGroup();
        listEvalueType = sessionBean.getService().getListEvaluaType();
        listStockStatus = sessionBean.getService().getListSearchStocktatus();;
        changeGoodsGroup();
//        setLsData(getSessionBean().getService().searchEquipment(new StockGoodsInvSerialDTO(), null, null));
        AppDomainService service = new AppDomainService();
        lstGroupType = service.findAllByType(InventoryConstanst.ApDomainType.EQUIP_TYPE);
        if(lstGroupType==null){
            lstGroupType = new ArrayList<>();
        }
        listActionType = sessionBean.getListActionType();
        listMaintenStatus = businessService.getListMaintenanceStatus();
        listWarantytatus = businessService.getListWarantytatus();
        listSearchStocktatus = businessService.getListSearchStocktatus();
        lstEquipmentStatus = businessService.getListGoodsStatus();
        lstTransactionAction = businessService.findAllJoinEqDetail();
        listTransType = sessionBean.getService().getListTransType();
        listShop = new ArrayList<>();
        _getListShop(sessionBean.getService().getAllShop(null));
        lsgGroup = sessionBean.getLsgGroup();
        initCbb();
        search();
        lsGoods=sessionBean.getLsEquipments();
    }
    public List<ApDomain> getLstStockUpdateDetail(){
        List<com.ftu.staff.bo.ApDomain> lstA = new ArrayList<>();
        for (com.ftu.staff.bo.ApDomain ap: sessionBean.getService().getListViewStocktatus()) {
//            if(ap.getValue().toString().equals(InventoryConstanst.StockStatus.INSTOCK)
//                    || ap.getValue().toString().equals(InventoryConstanst.StockStatus.EX_USED)
//                    || ap.getValue().toString().equals(InventoryConstanst.StockStatus.USED)){
//                lstA.add(ap);
//            }
            lstA.add(ap);
        }
        return lstA;
    }
    public String getEquipmentStatusName(Long type) {
        try {
            if (type == null) {
                return "";
            }
            for (ApDomain ap : lstEquipmentStatus) {
                if (type.equals(ap.getValue())) {
                    return ap.getName();
                }
            }
            return "";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }
    public String getActionTypeName(Long type) {
        try {
            if (type == null) {
                return "";
            }
            for (ApDomain ap : listActionType) {
                if (type.equals(ap.getValue())) {
                    return ap.getName();
                }
            }
            return "";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }
    public String getListSearchStocktatus(Long type) {
        try {
            if (type == null) {
                return "";
            }
            for (ApDomain ap : listSearchStocktatus) {
                if (type.equals(ap.getValue())) {
                    return ap.getName();
                }
            }
            return "";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }
    public String getStocktatusNameByTaId(Long type, Long taId) {
        try {
            if (type == null) {
                return "";
            }
//            if (type.toString().equals(InventoryConstanst.StockStatus.BLOCK)&& taId!=null){
//                for (TransactionAction obj:lstTranAction) {
//                    if(taId.equals(obj.getTransactionActionId())){
//                        if(listTransType!=null && obj.getTransactionType()!=null){
//                            for (ApDomain ap:listTransType) {
//                                if(ap.getValue()!=null && obj.getTransactionType().equals(ap.getValue().toString())){
//                                    return ap.getName();
//                                }
//                            }
//                        }
//                    }
//                }
//            }else {
//                for (ApDomain ap : listSearchStocktatus) {
//                    if (type.equals(ap.getValue())) {
//                        return ap.getName();
//                    }
//                }
//            }
            for (ApDomain ap : listSearchStocktatus) {
                if (type.equals(ap.getValue())) {
                    return ap.getName();
                }
            }
            return "";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }
    public String getTransTypeName(String type) {
        try {
            if (type == null) {
                return "";
            }
            for (ApDomain ap:listTransType) {
                if(ap.getValue()!=null && type.equals(ap.getValue().toString())){
                    return ap.getName();
                }
            }
            return "";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }
    public String getWarantyStatusName(Long type) {
        try {
            if (type == null) {
                return "";
            }
            for (ApDomain ap : listWarantytatus) {
                if (type.equals(ap.getValue())) {
                    return ap.getName();
                }
            }
            return "";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }
    public String getMaintenStatusName(Long type) {
        try {
            if (type == null) {
                return "";
            }
            for (ApDomain ap : listMaintenStatus) {
                if (type.equals(ap.getValue())) {
                    return ap.getName();
                }
            }
            return "";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public void changeStatusName() {
//        todo
//        if (stockGoodsInvForm.getEquipmentProfileStatus() != null) {
//            if (stockGoodsInvForm.getEquipmentProfileStatus().equals(InventoryConstanst.GoodsTransType.GOODS_STATUS)) {
//                if (stockGoodsInvForm.getEquipmentProfileStatus() != null) {
//                    Long gts = stockGoodsInvForm.getEquipmentProfileStatus().equals(InventoryConstanst.GoodsStatus.ERROR) ? InventoryConstanst.GoodsStatus.NOMAL : InventoryConstanst.GoodsStatus.ERROR;
//
//                    stockGoodsInvForm.setEquipmentStatusName(sessionBean.getService().getEquipsStatusName(gts));;
//                }
//            }
//            if (stockGoodsInvForm.getEquipmentProfileStatus().equals(InventoryConstanst.GoodsTransType.STOCK_STATUS)) {
//                if (stockGoodsInvForm.getStockStatus() != null) {
//                    String gts = stockGoodsInvForm.getStockStatus().toString().equals(InventoryConstanst.StockStatus.INSTOCK) ? InventoryConstanst.StockStatus.INSTOCK_ERR : InventoryConstanst.StockStatus.INSTOCK;
//                    stockGoodsInvForm.setStockStatusName(sessionBean.getStockStatusName(gts));
//                }
//            }
//        }
    }


    public void changeStatus() {
        lsData = new ArrayList<>();
        changeStatusName();
    }

    public void changeStockStatus() {
        lsData = new ArrayList<>();
        changeStatusName();
    }

    public void changeType() {
////        if (!listEvalueType.isEmpty()) {
////            typeId = typeId == null ? listEvalueType.get(0).getValue(): typeId;
////            status = listEvalueType.get(0).getName();
////            for (ApDomain d : listEvalueType) {
////                if (typeId.equals(d.getValue())) {
////                    status = d.getName();
////                    break;
////                }
////            }
////            listReasons = sessionBean.getService().getListEvaluaReason(typeId);
////        }
//        changeStatusName();
    }
    public void initCbb(){
        shopId = sessionBean.getShop().getShopId();
        stockGoodsInvForm.setShopId(sessionBean.getShop().getShopId());
        createShop = sessionBean.getShop().getShopName();
        if(lsgGroup!=null && !lsgGroup.isEmpty()){
//            stockGoodsInvForm.setGroupId(lsgGroup.get(0).getEquipmentsGroupId());
        }
        stockGoodsInvForm.setProfileName("Tất cả");
        PositionService ps = new PositionService();
        lsPosition =  ps.findByShopIdAllChi(shopId);
        changeGoodsGroup();
    }
    public void changeGoodsGroup() {
    	if (lsgGroup == null || lsgGroup.isEmpty())
		{
			lsGoods = new ArrayList<>();
            stockGoodsInvForm.setGroupId(null);

//            stockGoodsInvForm.setGoodsGroupName("Tất cả");
			return;
		}
        stockGoodsInvForm.setEquipmentProfileId(null);
        stockGoodsInvForm.setProfileName("Tất cả");
        stockGoodsInvForm.setNameStr("");
        if(stockGoodsInvForm.getGroupId()==null){
            lsGoods=sessionBean.getLsEquipments();
            return;
        }
        if (stockGoodsInvForm.getGroupId() != null && stockGoodsInvForm.getGroupId() > 0) {
            List<EquipmentsProfile> lsg = sessionBean.getLsEquipments();
            lsGoods = new ArrayList<>();
            if (lsg != null && !lsg.isEmpty()) {
                for (EquipmentsProfile g : lsg) {
                    if (stockGoodsInvForm.getGroupId().equals(g.getEquipmentsGroupId())) {
                        lsGoods.add(g);
                    }
                }
            }
        }
        stockGoodsInvForm.setEquipmentProfileId(null);
        stockGoodsInvForm.setNameStr("");
        stockGoodsInvForm.setProfileName("Tất cả");
    }

    public void goodsSelect() {
        if (lsGoods != null) {
            if(stockGoodsInvForm.getProfileName().equals("Tất cả")){
                stockGoodsInvForm.setEquipmentProfileId(null);
                stockGoodsInvForm.setProfileName("Tất cả");
                stockGoodsInvForm.setNameStr("");
            }
            for (EquipmentsProfile g : lsGoods) {
                String search = (g.getProfileCode() + " | " + g.getProfileName()).toLowerCase();
                if (search.equals(stockGoodsInvForm.getProfileName().toLowerCase())) {
                    stockGoodsInvForm.setEquipmentProfileId(g.getProfileId());
                    stockGoodsInvForm.setNameStr(g.getProfileName());
                    break;
                }
            }
        }
    }

    public List<String> completeGoods(String gs) {
        stockGoodsInvForm.setProfileName(gs);
        List<String> rs = new ArrayList<>();
        rs.add("Tất cả");
        if (lsGoods != null && !lsGoods.isEmpty()) {
            for (EquipmentsProfile g : lsGoods) {
                String search = g.getProfileCode() + " | " + g.getProfileName();
                if (gs == null || gs.isEmpty() || search.toLowerCase().contains(gs.toLowerCase())) {
                    rs.add(g.getProfileCode() + " | " + g.getProfileName());
                }
            }
        }
        return rs;
    }
    public void search() {
//        TransactionActionDAO taDAO = new TransactionActionDAO();
//        lstTranAction = taDAO.findAllByType(InventoryConstanst.TransactionType.INSTANT);
        lsData.clear();
        lsData.addAll(getSessionBean().getService().searchEquipment(stockGoodsInvForm, null, null));
        lsData.addAll(getSessionBean().getService().searchEquipmentNoSerial(stockGoodsInvForm, null, null));
        for (StockGoodsInvSerialDTO ss : getLsData()) {
            ss.setOldGoodsStatus(ss.getEquipmentProfileStatus());
            ss.setOldStatusName(ss.getStatusName());
        }
        lsData.removeAll(lsDataRs);
        dataSelect = null;
        lsDataAudit = new ArrayList<>();
        lsDataEquiment  = new ArrayList<>();
        lsDataMaintain =  new ArrayList<>();
    }
    public Long getQuantityGrid(String serial, Long shopId, Long equipId, Long status, Long stockStatus){
        if(serial != null && !serial.isEmpty()){
            return 1L;
        }else if(lstStockGoodsInvNoSerial!=null && !lstStockGoodsInvNoSerial.isEmpty()) {
            for (StockGoodsInvNoSerial stg:lstStockGoodsInvNoSerial) {
                if(stg.getShopId()!=null && stg.getEquipmentProfileId()!=null && stg.getEquipmentStatus()!=null && stg.getStockStatus() !=null
                        && stg.getShopId().equals(shopId) && stg.getEquipmentProfileId().equals(equipId)
                        && stg.getEquipmentStatus().equals(status) && stg.getStockStatus().equals(stockStatus)){
                    System.out.println(shopId+":"+equipId+":"+status+":"+stockStatus);
                    return stg.getQuantity();
                }
            }
        }
        return 0L;
    }
    private List<Long> listShopIdsInitSearch(Long parent) {
        List<Long> lst = new ArrayList<Long>();
        List<Shop> childs = shopService.getAllShopChildrent(parent);
        for (Shop s : childs) {
            lst.add(s.getShopId());
        }
        return lst;
    }
    public void add() {
        if (lsDataSelect == null || lsDataSelect.isEmpty()) {
            languageBean.showMeseage("Info", "chooseReq", new Object[]{"Serial"});
            return;
        }
        Long id = null;
        for (StockGoodsInvSerialDTO data:lsDataSelect) {
            if(id == null){
                id = data.getEquipmentProfileId();
            }else if(!id.equals(data.getEquipmentProfileId())){
                languageBean.showMeseage("Info", "audit.selected.equipment");
                return;
            }
        }
        for (StockGoodsInvSerialDTO data:lsDataRs) {
            if(id == null){
                id = data.getEquipmentProfileId();
            }else if(!id.equals(data.getEquipmentProfileId())){
                languageBean.showMeseage("Info", "audit.selected.equipment");
                return;
            }
        }
//        getLsDataRs().addAll(lsDataSelect);
//        lsData.removeAll(lsDataSelect);
    }

    public void addAll() {
        if (lsData == null || lsData.isEmpty()) {
            languageBean.showMeseage("Info", "chooseReq", new Object[]{"Serial"});
            return;
        }
//        getLsDataRs().addAll(lsData);
        lsData = new ArrayList<>();
    }

    public void refresh() {
        lsDataRs = new HashSet<>();
        search();
    }

    public void removeRs(StockGoodsInvSerialDTO gs) {
        lsDataRs.remove(gs);
        search();
    }

    public void beforeSave() {
        FacesMessage message = null;
        if(lsDataRs==null||lsDataRs.isEmpty()){
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false),
                    languageBean.getMessage("common.acction.beforesave", null, false));
            RequestContext context = RequestContext.getCurrentInstance();
            context.showMessageInDialog(message);
        }else {
            for (StockGoodsInvSerialDTO ss : lsDataRs) {
                if(ss.getEquipmentProfileId()==null){
                    continue;
                }
                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("PF('dlSave').show();");
            }

        }
    }

    public void save() {
//        stockGoodsService.saveOrUpdate(stockGoodsUpdate);
//        equipmentHistoryService.saveOrUpdate(equipmentHistoryUpdate);
//        equipmentsProfileService.saveOrUpdate(equipmentsProfileUpdate);
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('dlSave').hide();");
                context.update("frm");
        getLanguageBean().showMeseage("success", "succesProces");

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
     * @param lsDataRs the lsDataRs to set
     */
    public void setLsDataRs(Set<StockGoodsInvSerialDTO> lsDataRs) {
        this.lsDataRs = lsDataRs;
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
     * @return the listEvalueType
     */
    public List<ApDomain> getListEvalueType() {
        return listEvalueType;
    }

    /**
     * @param listEvalueType the listEvalueType to set
     */
    public void setListEvalueType(List<ApDomain> listEvalueType) {
        this.listEvalueType = listEvalueType;
    }

    /**
     * @return the listReasons
     */
    public List<ApDomain> getListReasons() {
        return listReasons;
    }

    /**
     * @param listReasons the listReasons to set
     */
    public void setListReasons(List<ApDomain> listReasons) {
        this.listReasons = listReasons;
    }

    /**
     * @return the listStockStatus
     */
    public List<ApDomain> getListStockStatus() {
        return listStockStatus;
    }

    /**
     * @param listStockStatus the listStockStatus to set
     */
    public void setListStockStatus(List<ApDomain> listStockStatus) {
        this.listStockStatus = listStockStatus;
    }



    /**
     * @return the lsDataSelect
     */
    public List<StockGoodsInvSerialDTO> getLsDataSelect() {
        return lsDataSelect;
    }

    /**
     * @param lsDataSelect the lsDataSelect to set
     */
    public void setLsDataSelect(List<StockGoodsInvSerialDTO> lsDataSelect) {
        this.lsDataSelect = lsDataSelect;
    }


    public String getGroupTypeName(Long value){
        for (ApDomain app:lstGroupType) {
            if(app.getValue().equals(value)){
                return app.getName();
            }
        }
        return "";
    }

    public List<ApDomain> getLstGroupType() {
        return lstGroupType;
    }

    public void setLstGroupType(List<ApDomain> lstGroupType) {
        this.lstGroupType = lstGroupType;
    }


    public void validate(FacesContext arg0, UIComponent uiComponent, Object object) throws ValidatorException {
        // TODO Auto-generated method stub
        if (true) {
            boolean valid = true;
            if (object == null) {
                return;
            }
            FacesMessage msg = null;
            String id = uiComponent.getId();
            valid = true;
            if (id.equals("aviableQuantity")) {
                String value = object.toString().trim();
                if (!StringUtils.isNumberLong̣̣(value) || Long.parseLong(value) < 0) {
                    valid = false;
                    msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false), languageBean.getMessage("audit.validator.aviableQuantity.number", null, false));
                }
            }
            if (id.equals("approCode")) {
                String value = object.toString().trim();
                if (value!=null && !value.isEmpty() && (!StringUtils.isNumberLong̣̣(value) || Long.parseLong(value) < 0)) {
                    valid = false;
                    msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false), languageBean.getMessage("audit.validator.reference.number", null, false));
                }
            }

            if (id.equals("linkDocument")) {
                String value = object.toString();
                if (value!=null && !value.isEmpty() && !StringUtils.isUrlValidate(value)) {
                    valid = false;
                    msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false), languageBean.getMessage("audit.validator.url.validate", null, false));
                }
            }

            if (!valid) {
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            }
        }
    }

    public void onTabChanged(TabChangeEvent e) {
        try {
//            String id = e.getTab().getId();
//            currentTab = id;
//            if (id.equals("gtab")) {
//                loadGoodsPrice(null);
//                if (selectedNode == null) {
//                    collapseAllTreeNode(true);
//                    FacesContext fc = FacesContext.getCurrentInstance();
//                    EquipmentsProfileBean goodsBean = (EquipmentsProfileBean) fc.getApplication().evaluateExpressionGet(fc, "#{equipmentsProfileBean}",
//                            EquipmentsProfileBean.class);
//                    goodsBean.setListGroups(groupService.getAllEquipmentsGroup(null, false, -1, -1));
////                    goodsBean.setGoodSelected(null);
//                }
//            } else if(id.equals("ggtab")){
//                clearSelection(selectedNode);
//                selectedNode = null;
//                collapseAllTreeNode(false);
//            }else{
//                if(selectedNode == null){
//                    collapseAllTreeNode(true);
//                    FacesContext fc = FacesContext.getCurrentInstance();
//                    GoodsPriceBean goodsPriceBean = (GoodsPriceBean) fc.getApplication().evaluateExpressionGet(fc, "#{goodsPriceBean}",
//                            GoodsPriceBean.class);
//                    goodsPriceBean.setListGoodsPrice(priceService.getAllGoods(null, false, -1, -1));
//                    goodsPriceBean.setPriceSelected(null);
//                }
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void view(SelectEvent event) {
        StockGoodsInvSerialDTO  objClick = (StockGoodsInvSerialDTO) event.getObject();
        if (dataSelect == null ) {
            return;
        }

        setLsDataEquiment(getSessionBean().getService().searchEquipmentId(dataSelect.getEquimentDetailId(), null, null));
        EquipmentHistory obj = new EquipmentHistory();
        obj.setEquipmentId(dataSelect.getEquimentDetailId());

        EquipmentsProfile profile = equipmentsProfileService.findByProfileId(objClick.getEquipmentProfileId());
        if(profile!=null && InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
            obj.setNotSerial(false);
            obj.setShopId(sessionBean.getShop().getShopId());
        }else {
            obj.setNotSerial(true);
            obj.setShopId(objClick.getShopId());
        }

        setLsDataAudit(equipmentHistoryService.searchByEquipmentId(obj, null, null));
//        for (EquipmentHistory his:lsDataAudit) {
//            obj.setNote(dataSelect.getSerial());
//        }
        setLsDataMaintain(getSessionBean().getService().searchMainTen(dataSelect.getSerial(), dataSelect.getEquimentDetailId(), null, null));

    }
    public String getStaffName(Long staffId){
        if (staffId==null) return "";
        Staff s = sessionBean.getStaff(staffId);
        if(s == null) return "";
        return  s.getStaffName();
    }
    public void viewDetailHis(Long hisId){
        RequestContext context = RequestContext.getCurrentInstance();
        if(hisId == null) {
            languageBean.showMeseage("Info", "not.his.detail");
            context.update("frm:messages");
            return;
        }
        lsHisDetail =  equipmentHistoryDetailService.findByEquipmentHistoryId(hisId);
        if(lsHisDetail ==null || lsHisDetail.isEmpty()){
            languageBean.showMeseage("Info", "not.his.detail");
            context.update("frm:messages");
            return;
        }

        context.execute("PF('viewDetailHis').show();");
    }
    public String getSerial(){
        if(dataSelect!=null){
            return dataSelect.getSerial();
        }
        return "";
    }

    public StockGoodsInvSerialDTO getStockGoodsInvForm() {
        return stockGoodsInvForm;
    }

    public void setStockGoodsInvForm(StockGoodsInvSerialDTO stockGoodsInvForm) {
        this.stockGoodsInvForm = stockGoodsInvForm;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public StockGoodsInvSerialDTO getDataSelect() {
        return dataSelect;
    }

    public void setDataSelect(StockGoodsInvSerialDTO dataSelect) {
        this.dataSelect = dataSelect;
    }

    public List<StockGoodsInvSerialDTO> getLsDataEquiment() {
        return lsDataEquiment;
    }

    public void setLsDataEquiment(List<StockGoodsInvSerialDTO> lsDataEquiment) {
        this.lsDataEquiment = lsDataEquiment;
    }

    public List<EquipmentHistory> getLsDataAudit() {
        return lsDataAudit;
    }

    public void setLsDataAudit(List<EquipmentHistory> lsDataAudit) {
        this.lsDataAudit = lsDataAudit;
    }

    public List<StockGoodsInvSerialDTO> getLsDataMaintain() {
        return lsDataMaintain;
    }

    public void setLsDataMaintain(List<StockGoodsInvSerialDTO> lsDataMaintain) {
        this.lsDataMaintain = lsDataMaintain;
    }

    public List<TransactionAction> getLstTransactionAction() {
        return lstTransactionAction;
    }

    public void setLstTransactionAction(List<TransactionAction> lstTransactionAction) {
        this.lstTransactionAction = lstTransactionAction;
    }

    public List<Shop> getListShop() {
        return listShop;
    }

    public void setListShop(List<Shop> listShop) {
        this.listShop = listShop;
    }
    public void _getListShop(Shop s) {
        if (listShop.size() == 0) {
//    		Shop allShop = new Shop(-1l);
//            allShop.setShopName("Tất cả");
//            listShop.add(0,allShop);
        }
        listShop.add(s);
        if (s.getChildShops() != null) {
            for (Shop sub : s.getChildShops()) {
                _getListShop(sub);
            }
        }
    }

    public String getCreateShop() {
        return createShop;
    }

    public void setCreateShop(String createShop) {
        this.createShop = createShop;
    }
    public List<String> completeCreateShop(String query) {
        try {
//            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
//                    .getSession(false);
//            SessionData data = (SessionData) session.getAttribute("username");
            List<String> filteredThemes = new ArrayList<String>();
//            createShop = "Tất cả";
//            filteredThemes.add("Tất cả");
            for (int i = 0; i < listShop.size(); i++) {
                Shop skin = listShop.get(i);
                if (skin.getShopName().toLowerCase().contains(query.toLowerCase())) {
                    filteredThemes.add(skin.getShopName());
                }
            }
            return filteredThemes;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    public void createShopSelect() {
        if (listShop != null) {
            if(createShop.equals("Tất cả")){
                shopId = null;
                stockGoodsInvForm.setShopId(null);
            }else{
                for (Shop shop : listShop) {
                    if (shop.getShopName().equals(createShop)) {
                        shopId = shop.getShopId();
                        stockGoodsInvForm.setShopId(shop.getShopId());
                        break;
                    }
                }
            }
            PositionService ps = new PositionService();
            lsPosition =  ps.findByShopIdAllChi(shopId);
        }
    }

    public List<EquipmentsGroup> getLsgGroup() {
        return lsgGroup;
    }

    public void setLsgGroup(List<EquipmentsGroup> lsgGroup) {
        this.lsgGroup = lsgGroup;
    }

    public List<EquipmentHistoryDetail> getLsHisDetail() {
        return lsHisDetail;
    }

    public void setLsHisDetail(List<EquipmentHistoryDetail> lsHisDetail) {
        this.lsHisDetail = lsHisDetail;
    }
	/**
	 * @return the lsPosition
	 */
	public List<Position> getLsPosition() {
		return lsPosition;
	}
	/**
	 * @param lsPosition the lsPosition to set
	 */
	public void setLsPosition(List<Position> lsPosition) {
		this.lsPosition = lsPosition;
	}

    public List<StockGoodsInvNoSerial> getLstStockGoodsInvNoSerial() {
        return lstStockGoodsInvNoSerial;
    }

    public void setLstStockGoodsInvNoSerial(List<StockGoodsInvNoSerial> lstStockGoodsInvNoSerial) {
        this.lstStockGoodsInvNoSerial = lstStockGoodsInvNoSerial;
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
