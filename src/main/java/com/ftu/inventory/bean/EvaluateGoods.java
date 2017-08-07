/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bean;

import com.ftu.constanst.InventoryConstanst;
import com.ftu.inventory.bo.*;
import com.ftu.inventory.bussiness.*;
import com.ftu.inventory.dao.StockGoodsInvNoSerialDAO;
import com.ftu.inventory.dto.StockGoodsInvSerialDTO;
import com.ftu.inventory.exportExcel.ExportExcel;
import com.ftu.java.business.EquipmentsProfileService;
import com.ftu.language.LanguageBean;
import com.ftu.staff.bo.ApDomain;
import com.ftu.staff.bo.Staff;
import com.ftu.staff.bussiness.AppDomainService;
import com.ftu.utils.DateTimeUtils;
import com.ftu.utils.StringUtils;
import com.ftu.ws.BusinessService;
import org.primefaces.context.RequestContext;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.Visibility;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import java.io.FileInputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author E5420
 */
@ManagedBean(name = "evaluateGoods", eager = true)
@ViewScoped
public class EvaluateGoods implements Serializable {

    private String reason;
    private Long goodsId;
    private Long providerId;
    private String fromSerial;
    private String toSerial;
    private Long equipmentStatus;
    private Date actionDate;
    private Long actionUserId;
    private String equipmentName;
    private String serial;

    List<Staff> lstStaff = new ArrayList<>();

    private Long statusChange;
    private List<StockGoodsInvSerialDTO> lsData;
    private List<StockGoodsInvSerialDTO> lsDataSelect;
    private List<StockGoodsInvSerialDTO> lsDataRs = new ArrayList<>();
    private Set<StockGoodsInvSerialDTO> lsDataRsSelecteds = new HashSet<>();
    private List<StockGoodsInvSerialDTO> lsDataUpdate;
    @ManagedProperty(value = "#{sessionBean}")
    private SessionBean sessionBean;
    @ManagedProperty(value = "#{languageBean}")
    private LanguageBean languageBean;
    List<Provider> lsProviders;
    private Long goodsGroupId;
    private List<EquipmentsProfile> lsGoods;
    List<EquipmentsGroup> lsGoodsGroup;
    private int setSize;
    private int lsSize;
    private List<ApDomain> listEvalueType;
    private Long typeId;
    private List<ApDomain> listReasons;
    private Long reasonId;
    private List<ApDomain> listStockStatus;
    private List<ApDomain> lstGroupType;
    private String stockStatusId;
    private Boolean disableStatus;
    private Boolean disableStock;
    private Boolean disableType;
    private Boolean notRenderStock;
    private Boolean notRenderStatus;
    private String statusName;
    private String status;
    private StockGoods stockGoodsUpdate;
    private StockGoods stockGoodsUpdateOld;
    private Long quantityOldSave = null;
    private Long availQuantityOldSave = null;
    private EquipmentHistory equipmentHistoryUpdate;
    private EquipmentHistory equipmentHistoryUpdateOld;
    private EquipmentsProfile equipmentsProfileUpdate;
    private ActionAuditService ActionAuditService = new ActionAuditService();
    private Long groupTypeId;
    Staff staff;
    List<EquipmentsGroup> lsgGroup = new ArrayList<>();

    private BusinessService businessService = new BusinessService();
    private String goods;
    private StockGoodsService stockGoodsService  = new StockGoodsService();
    private EquipmentHistoryService equipmentHistoryService = new EquipmentHistoryService();
    private EquipmentsProfileService equipmentsProfileService = new EquipmentsProfileService();
    private EquipmentsDetailService equipmentsDetailService = new EquipmentsDetailService();
    private EquipmentHistoryDetailService equipmentHistoryDetailService = new EquipmentHistoryDetailService();
    //huy
    private int sumDataSelected = 0;
    private int belowSumDataSelected;
    private boolean renderReason;

    private transient StreamedContent fileExport;
//    private List<ApDomain> listDomainGoodsGroup = new ArrayList<>();

    @PostConstruct
    public void init() {
    	sessionBean.loadData(sessionBean.getStaff(), sessionBean.getShop());
        lsProviders = sessionBean.getLsProvider();
        lsGoodsGroup = sessionBean.getLsgGroupActive();
        listEvalueType = sessionBean.getService().getListEvaluaType();
        listStockStatus = sessionBean.getService().getListTransStockStatus();
//        changeGoodsGroup();
        changeType();
        staff = sessionBean.getStaff();
        lstStaff = businessService.getAllStaffByTransactionAction();
        stockStatusId = InventoryConstanst.GoodsTransType.GOODS_STATUS.toString();
//        setLsData(getSessionBean().getService().searchStockEquipmentsInvSerial(fromSerial, toSerial, new StockGoodsInvSerialDTO(), goodsGroupId, null, null));
        AppDomainService service = new AppDomainService();
        lstGroupType = service.findAllByType(InventoryConstanst.ApDomainType.EQUIP_TYPE);
        if(lstGroupType==null){
            lstGroupType = new ArrayList<>();
        }
        stockGoodsUpdate = new StockGoods();
        if(listReasons!=null&&!listReasons.isEmpty()){
            reasonId = listReasons.get(0).getValue();
        }
        if(sessionBean.getListStatus()!=null&&!sessionBean.getListStatus().isEmpty()){
            equipmentStatus = sessionBean.getListStatus().get(0).getValue();
        }
        actionDate = new Date();
        actionUserId = sessionBean.getStaff().getStaffId();
//        lsGoodsGroup = sessionBean.getLsgGroupActive();
//        setLsgDomainGroup( getService().getDomainGoodsGroup());
        initCbb();
//        listReasons = sessionBean.getService().getListEvaluaReason(InventoryConstanst.REASON_CODE.evaluate);
        search();
    }

    public void hanleLsDataSelect(){
    	sumDataSelected = lsDataSelect.size();
    }

    public void changeStatusName() {
        if (typeId != null) {
            if (typeId.equals(InventoryConstanst.GoodsTransType.GOODS_STATUS)) {
                if (equipmentStatus != null) {
                    Long gts = equipmentStatus.equals(InventoryConstanst.GoodsStatus.ERROR) ? InventoryConstanst.GoodsStatus.NOMAL : InventoryConstanst.GoodsStatus.ERROR;
                    if(equipmentStatus.equals(InventoryConstanst.GoodsStatus.NOMAL)){
                    	renderReason = false;
                    } else{
                    	renderReason = true;
                    }
                    statusName = sessionBean.getService().getEquipsStatusName(gts);
                }
            }
            if (typeId.equals(InventoryConstanst.GoodsTransType.STOCK_STATUS)) {
                if (stockStatusId != null) {
                    String gts = stockStatusId.toString().equals(InventoryConstanst.StockStatus.INSTOCK) ? InventoryConstanst.StockStatus.INSTOCK_ERR : InventoryConstanst.StockStatus.INSTOCK;
                    statusName = sessionBean.getStockStatusName(gts);
                }
            }
        }
    }


    public void changeStatus() {
//        lsData = new ArrayList<>();
        changeStatusName();
    }

    public void changeStockStatus() {
        lsData = new ArrayList<>();
        changeStatusName();
    }

    public void changeType() {
        if (!listEvalueType.isEmpty()) {
            typeId = typeId == null ? listEvalueType.get(0).getValue(): typeId;
            status = listEvalueType.get(0).getName();
            for (ApDomain d : listEvalueType) {
                if (typeId.equals(d.getValue())) {
                    status = d.getName();
                    break;
                }
            }
            listReasons = sessionBean.getService().getListEvaluaReason(typeId);
//            if (!listReasons.isEmpty()) {
//                reasonId = listReasons.get(0).getValue();
//            }
//            if (listStockStatus != null) {
//                stockStatusId = listStockStatus.get(0).getValue();
//            }
//            equipmentStatus = sessionBean.getListStatus().get(1).getValue();
        }
        changeStatusName();
    }

    public void changeGoodsGroup() {
        List<EquipmentsProfile> lsg = sessionBean.getLsEquipments();
    	if (lsGoodsGroup == null || lsGoodsGroup.isEmpty())
		{
			lsGoods = new ArrayList<>();
			goodsId = -1L;
			goods = "Tất cả";
			return;
		}

        if (getGoodsGroupId() != null && getGoodsGroupId() > 0) {
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
        }else {
            lsGoods = new ArrayList<>();
            for (EquipmentsProfile g : lsg) {
                if(InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(g.getEquipProfileStatus())){
                    continue;
                }
                lsGoods.add(g);
            }
            goodsId = null;
            goods ="Tất cả";
            equipmentName ="";
            return;
        }
        goodsId = null;
        equipmentName = "";
        goods = "Tất cả";
//        if(lsGoods!=null && !lsGoods.isEmpty()){
//            goodsId = lsGoods.get(0).getProfileId();
//            goods = equipmentName =  lsGoods.get(0).getProfileCode() + " | " + lsGoods.get(0).getProfileName();
//        }else {
//            goodsId = null;
//            equipmentName = "";
//            goods = "";
//        }


//        if (lsGoods!=null && !lsGoods.isEmpty()) {
//            EquipmentsProfile g = lsGoods.get(0);
//            goodsId = g.getProfileId();
//            equipmentName = g.getProfileName();
//            goods = g.getProfileCode() + " | " + g.getProfileName();
//        } else {
//            goodsId = null;
//            goods ="Tất cả";
//        }
    }
    public void initCbb(){
        List<EquipmentsGroup> lst = sessionBean.getLsgGroup();
//        if(lst!=null && !lst.isEmpty()){
//            goodsGroupId = lst.get(0).getEquipmentsGroupId();
//        }
//        if(sessionBean.getLsgDomainGroup()!=null && !sessionBean.getLsgDomainGroup().isEmpty()){
//            groupTypeId = sessionBean.getLsgDomainGroup().get(0).getValue();
//        }
        selectEquipType();
//        changeGoodsGroup();
    }
    public void goodsSelect() {
        if (lsGoods != null) {
            if(goods.equals("Tất cả")){
                goodsId = null;
                equipmentName ="";
            }else {
                for (EquipmentsProfile g : lsGoods) {
                    if(InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(g.getEquipProfileStatus())){
                        continue;
                    }
                    if ((g.getProfileCode() + " | " + g.getProfileName()).equals(goods)) {
                        goodsId = g.getProfileId();
                        equipmentName = g.getProfileName();
                        break;
                    }
                }
            }
        }
    }

    public List<String> completeGoods(String gs) {
        goods = gs;
        List<String> rs = new ArrayList<>();

        rs.add("Tất cả");
        if (lsGoods != null && !lsGoods.isEmpty()) {
            for (EquipmentsProfile g : lsGoods) {
                if(InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(g.getEquipProfileStatus())){
                    continue;
                }
                if (gs == null || gs.isEmpty() || g.getProfileCode().toLowerCase().contains(goods.toLowerCase())) {
                    rs.add(g.getProfileCode() + " | " + g.getProfileName());
                }
            }
        }
        return rs;
    }
//    private void _getListReasons() {
//        listReasons = new ArrayList<>();
//        listReasons.add(new Data("Hỏng do dán nhãn", InventoryConstanst.GoodsStatusReason.ERROR_1));
//        listReasons.add(new Data("Hỏng khi nhập hàng", InventoryConstanst.GoodsStatusReason.ERROR_2));
//    }

    public void search() {
//        if (goodsId == null) {
//            languageBean.showMeseage("info", "not.equipment.not.empty");
//            return;
//        }
        StockGoodsInvSerialDTO sgis = new StockGoodsInvSerialDTO();
        sgis.setResonId(reasonId);
        sgis.setEquipmentProfileStatus(equipmentStatus);
        sgis.setCreateDateTime(actionDate);
        sgis.setStaffId(actionUserId);
        sgis.setNote(reason);
        sgis.setGroupId(goodsGroupId);
        sgis.setEquipmentProfileId(goodsId);
        sgis.setProviderId(providerId);
        sgis.setSerial(serial);
        sgis.setStockStatus(stockStatusId);
        sgis.setGroupType(groupTypeId);
        sgis.setShopId(sessionBean.getShop().getShopId());

        setLsData(getSessionBean().getService().searchStockEquipmentsInvSerial(fromSerial, toSerial, sgis, goodsGroupId, null, null));
//        if (lsData == null || lsData.isEmpty()) {
//            languageBean.showMeseage("info", "NoSerialSearch");
//            return;
//        }
        for (StockGoodsInvSerialDTO ss : getLsData()) {
            ss.setOldGoodsStatus(ss.getEquipmentProfileStatus());
            ss.setOldStatusName(ss.getStatusName());
        }
        initDatars();
        lsData.removeAll(lsDataRs);
    }

    public void showDialog(){
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
        FacesMessage message = null;
        EquipmentsProfile profile = equipmentsProfileService.findByProfileId(lsDataSelect.get(0).getEquipmentProfileId());
        EquipmentsDetail detail = null;
        List<EquipmentsDetail> lst = equipmentsDetailService.findByProfile(profile.getProfileId());
//        equipmentHistoryUpdate  = null;
        if(lst!=null && !lst.isEmpty()){
            detail = lst.get(0);
            equipmentHistoryUpdateOld = equipmentHistoryService.findByEquipmentId(detail.getId());
//            equipmentHistoryUpdate = equipmentHistoryService.findByEquipmentId(detail.getId());
        }else {
            detail = new EquipmentsDetail();
            equipmentHistoryUpdateOld = new EquipmentHistory();
            equipmentHistoryUpdate = null;
        }
//        if (equipmentHistoryUpdate == null) {
//            equipmentHistoryUpdate = new EquipmentHistory();
//            equipmentHistoryUpdate.setShopId(staff.getShopId());
//            equipmentHistoryUpdate.setStaffId(staff.getStaffId());
//            equipmentHistoryUpdate.setCreatedDatetime(new Date());
//            equipmentHistoryUpdate.setEquipmentId(detail.getId());
//        }
        equipmentHistoryUpdate = new EquipmentHistory();
        equipmentHistoryUpdate.setShopId(staff.getShopId());
        equipmentHistoryUpdate.setStaffId(staff.getStaffId());
        equipmentHistoryUpdate.setCreatedDatetime(new Date());
        equipmentHistoryUpdate.setEquipmentId(detail.getId());

        stockGoodsUpdateOld = stockGoodsService.getStockGoods(lsDataSelect.get(0).getEquipmentProfileId(),
                lsDataSelect.get(0).getEquipmentProfileStatus(), lsDataSelect.get(0).getShopId());
        quantityOldSave = stockGoodsUpdateOld.getQuantity();
        availQuantityOldSave = stockGoodsUpdateOld.getAvailableQuantity();
        if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
            long quantitySerial = 0L;
            for (StockGoodsInvSerialDTO ss : lsDataSelect) {
                if(ss.getEquipmentProfileId()==null){
                    continue;
                }
                if(profile == null){
                    profile = equipmentsProfileService.findByProfileId(ss.getEquipmentProfileId());
                }

                    stockGoodsUpdate = stockGoodsService.getStockGoods(ss.getEquipmentProfileId(),
                            ss.getEquipmentProfileStatus(), ss.getShopId());
                    equipmentsProfileUpdate = equipmentsProfileService.findByProfileId(ss.getEquipmentProfileId());

                    if (stockGoodsUpdate == null) {
                        message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false),
                                languageBean.getMessage("not.equipment.stockgood", null, false));
                        RequestContext context = RequestContext.getCurrentInstance();
                        context.showMessageInDialog(message);
                        return;
                    }
//                    stockGoodsUpdate.setQuantityUpdate(stockGoodsUpdate.getAvailableQuantity());
                    if (equipmentsProfileUpdate == null) {
                        equipmentsProfileUpdate = new EquipmentsProfile();
                        equipmentsProfileUpdate.setProfileId(ss.getEquipmentProfileId());
                    }
                    stockGoodsUpdate.setUnit(equipmentsProfileUpdate.getUnit());
//                    stockGoodsUpdate.setLinkDocument(equipmentHistoryUpdate.getDocument());
//                    stockGoodsUpdate.setApproCode(equipmentHistoryUpdate.getReferenceId() == null ? "" : equipmentHistoryUpdate.getReferenceId().toString());
            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('dlSave').show();");
        }else {
            add();
        }


    }
    public void addRecordRs(){
        Long subQuantity = 0L;
        if(stockGoodsUpdate.getQuantity()==null || stockGoodsUpdate.getAvailableQuantity()== null || stockGoodsUpdate.getQuantityUpdate() == null){
            RequestContext context = RequestContext.getCurrentInstance();
//            context.execute("PF('dlSave').hide();");
            context.update("frm:messages");
            getLanguageBean().showMeseage("error", "audit.data.not.undefile");
            return;
        }else if(stockGoodsUpdate.getQuantity()!=null&& stockGoodsUpdate.getAvailableQuantity()!= null &&
                (stockGoodsUpdate.getQuantity() < stockGoodsUpdate.getQuantityUpdate() || stockGoodsUpdate.getQuantityUpdate() > stockGoodsUpdate.getAvailableQuantity())){
            RequestContext context = RequestContext.getCurrentInstance();
//            context.execute("PF('dlSave').hide();");
            context.update("frm:messages");
            getLanguageBean().showMeseage("error", "audit.data.quantity.not.than",
                    new Object[]{(stockGoodsUpdate.getQuantity() > stockGoodsUpdate.getAvailableQuantity() ? stockGoodsUpdate.getAvailableQuantity():stockGoodsUpdate.getQuantity())+1});
            return;
        }else {
            subQuantity = stockGoodsUpdate.getAvailableQuantity() - stockGoodsUpdate.getQuantityUpdate();
            Long quan =   stockGoodsUpdate.getQuantity() + stockGoodsUpdate.getQuantityUpdate()
                    - stockGoodsUpdate.getAvailableQuantity();
            quan = quan < 0L? 0L:quan;
            stockGoodsUpdate.setQuantity(quan);
            stockGoodsUpdate.setAvailableQuantity(stockGoodsUpdate.getQuantityUpdate());
        }
        equipmentHistoryUpdate.setDocument(stockGoodsUpdate.getLinkDocument());
        equipmentHistoryUpdate.setNote(reason);
        if(stockGoodsUpdate.getApproCode()!=null && !stockGoodsUpdate.getApproCode().isEmpty()){
            equipmentHistoryUpdate.setReferenceId(stockGoodsUpdate.getApproCode());
        }else{
            equipmentHistoryUpdate.setReferenceId(null);
        }
        if(equipmentHistoryUpdate.getEquipmentHistoryId()==null){
            equipmentHistoryUpdate.setShopId(staff.getShopId());
            equipmentHistoryUpdate.setStaffId(staff.getStaffId());
            equipmentHistoryUpdate.setCreatedDatetime(new Date());
        }


        List<StockGoodsInvSerialDTO> lstDTO = new ArrayList<>();
        lstDTO.addAll(lsDataSelect);
        for (StockGoodsInvSerialDTO dto:lstDTO) {
            dto.setQuantity(stockGoodsUpdate.getQuantity());
            dto.setAvailableQuantity(stockGoodsUpdate.getAvailableQuantity());
            dto.setCountBlockSerial(stockGoodsUpdate.getAvailableQuantity());
            dto.setEquipmentHistory(equipmentHistoryUpdate);
            dto.setSubQuantity(subQuantity);
        }
        getLsDataRs().addAll(lstDTO);
        lsData.removeAll(lsDataSelect);
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('dlSave').hide();");
    }
    public void add() {
        getLsDataRs().addAll(lsDataSelect);
        lsData.removeAll(lsDataSelect);
    }

    public void addAll() {
        if (lsData == null || lsData.isEmpty()) {
            languageBean.showMeseage("Info", "chooseReq", new Object[]{"Serial"});
            return;
        }
        getLsDataRs().addAll(lsData);
        lsData = new ArrayList<>();
    }

    public void refresh() {
        lsDataRs = new ArrayList<>();
        reason = "";
        search();
    }

    public void removeRs(StockGoodsInvSerialDTO gs) {
        lsDataRs.remove(gs);
        search();
    }



    public void beforeSave() {
    }

    public void save() {
        EquipmentsProfile profile = null;
        List<Long> currentTaids = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        Boolean isSerial = false;
        //save reason_id
        GoodsStatusTrans goodsStatusTrans = new GoodsStatusTrans();
        goodsStatusTrans.setTransDatetime(new Date());
        goodsStatusTrans.setStaffId(sessionBean.getStaff().getStaffId());
        goodsStatusTrans.setShopId(sessionBean.getShop().getShopId());
        goodsStatusTrans.setReasonId(reasonId);
        goodsStatusTrans.setNote(reason);
        sessionBean.getService().saveOrUpdateGoodsStatusTrans(goodsStatusTrans);


        for (StockGoodsInvSerialDTO ss : lsDataRs) {
            if(ss.getEquipmentProfileId()==null){
                continue;
            }
            if(profile == null){
                profile = equipmentsProfileService.findByProfileId(ss.getEquipmentProfileId());
            }
//            if(ss.getCurrentTaId()!=null && !currentTaids.contains(ss.getCurrentTaId())){
//                currentTaids.add(ss.getCurrentTaId());
//            }
            //save reasonId;
            saveGoodsStatusTrans(ss, goodsStatusTrans.getGoodsStatusTransId());

            if(profile!=null &&
                    !InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())) {
                if(ss.getId()!=null){
                    businessService.updateStockNoSerialEquipstatus(ss.getEquipmentProfileStatus(), null, ss.getId(),
                            stockGoodsUpdate.getAvailableQuantity());
                }
                equipmentHistoryUpdateOld = equipmentHistoryService.findByEquipmentId(ss.getEquipmentHistory().getEquipmentId());
                equipmentHistoryService.saveOrUpdate(ss.getEquipmentHistory());
                equipmentHistoryUpdate = ss.getEquipmentHistory();


                StockGoodsInvNoSerial sgins = sessionBean.getService().getStockGoodsNoserial(ss.getEquipmentProfileId(),
                        ss.getEquipmentProfileStatus(),
                        ss.getShopId(), InventoryConstanst.StockStatus.CANCEL);
                if(sgins == null){
                    sgins = new StockGoodsInvNoSerial();
                    sgins.setShopId(ss.getShopId());
                    sgins.setEquipmentProfileId(ss.getEquipmentProfileId());
                    sgins.setQuantity(ss.getSubQuantity());
                    sgins.setStockStatus(InventoryConstanst.StockStatus.CANCEL);
                    sgins.setInstockDatetime(ss.getInstockDatetime());
                    sgins.setOutstockDatetime(new Date());
                    sgins.setCurrentTaId(ss.getCurrentTaId());
                    sgins.setStaffId(sessionBean.getStaff().getStaffId());
                    sgins.setEquipmentStatus(ss.getEquipmentProfileStatus());
                    sgins.setProviderId(ss.getProviderId());
                }else {
                    sgins.setQuantity(sgins.getQuantity() + ss.getSubQuantity());
                    sgins.setCurrentTaId(ss.getCurrentTaId());
                }
                sessionBean.getService().saveOrUpdateNoSerial(sgins);

                saveHistoryDetail(isSerial,stockGoodsUpdate, InventoryConstanst.StockStatus.INSTOCK );
                stockGoodsService.saveOrUpdate(stockGoodsUpdate);
                isSerial = false;
            }else if(profile!=null){
                StockGoods stockGoods = stockGoodsService.getStockGoods(ss.getEquipmentProfileId(),
                        ss.getEquipmentProfileStatus(), ss.getShopId());
                quantityOldSave = stockGoods.getQuantity();
                availQuantityOldSave = stockGoods.getAvailableQuantity();
                stockGoods.setQuantity(stockGoods.getQuantity() - 1L);
                stockGoods.setAvailableQuantity(stockGoods.getAvailableQuantity() - 1L);
                businessService.saveOrUpdateStockGoods(stockGoods);
                ss.setStockStatus(InventoryConstanst.StockStatus.CANCEL);
                businessService.saveOrUpdateSerial(ss);
                isSerial =true;

                EquipmentsDetail detail = equipmentsDetailService.findBySerial(ss.getSerial());
                EquipmentHistory history = null;
                if(detail!=null ){
//                    history = equipmentHistoryService.findByEquipmentId(detail.getId());
                }else {
                    detail = new EquipmentsDetail();
                    history = null;
                }
                if (history == null) {
                    history = new EquipmentHistory();
                    history.setShopId(staff.getShopId());
                    history.setStaffId(staff.getStaffId());
                    history.setCreatedDatetime(DateTimeUtils.getDate());
                    history.setEquipmentId(detail.getId());
                }
                equipmentHistoryUpdateOld = equipmentHistoryService.findByEquipmentId(detail.getId());
                equipmentHistoryService.saveOrUpdate(history);
                equipmentHistoryUpdate = history;
                saveHistoryDetail(isSerial,stockGoods, InventoryConstanst.StockStatus.INSTOCK );
            }
        }

//        if(ids!=null&&!ids.isEmpty()&&equipmentStatus!=null){
//            sessionBean.getService().updateStockGoodsEquipstatus(ids,equipmentStatus);
//        }
//        if(currentTaids!=null&&!currentTaids.isEmpty()&&reasonId!=null){
//            sessionBean.getService().updateReasonId(currentTaids,reasonId);
//        }

        if(!isSerial){
//            equipmentHistoryService.saveOrUpdate(equipmentHistoryUpdate);
//            saveHistoryDetail(isSerial,stockGoodsUpdate, InventoryConstanst.StockStatus.INSTOCK );
//            stockGoodsService.saveOrUpdate(stockGoodsUpdate);
        }

        RequestContext context = RequestContext.getCurrentInstance();
//        context.execute("PF('dlSave').hide();");
                context.update("frm:messages");
        context.update("frm:dtSearch");
        context.update("frm:dtResult");
        getLanguageBean().showMeseage("success", "succesProces");
        refresh();
        search();
    }
    public void saveGoodsStatusTrans(StockGoodsInvSerialDTO ss, Long goodsStatusTransId){
        GoodsStatusTransSerial statusTransSerial = new GoodsStatusTransSerial();
        statusTransSerial.setGoodsId(ss.getEquipmentProfileId());
        statusTransSerial.setSerial(ss.getSerial());
        statusTransSerial.setTransDatetime(new Date());
        statusTransSerial.setOldGoodsStatus(ss.getEquipmentProfileStatus());
        statusTransSerial.setNewGoodsStatus(ss.getEquipmentProfileStatus());
        statusTransSerial.setGoodsStatusTransId(goodsStatusTransId);
        statusTransSerial.setProviderId(ss.getProviderId());
        sessionBean.getService().saveOrUpdatGSTS(statusTransSerial);
    }
    public void saveHistoryDetail(Boolean isSerial, StockGoods stgNew, String stockStatusOld){
        String[] lstColumn ;
        if(isSerial){
            lstColumn =  new String[]{"AVAILABLE_QUANTITY","STOCK_STATUS"};
        }else {
            lstColumn =  new String[]{"AVAILABLE_QUANTITY", "REFERENCE_ID", "DOCUMENT"};
        }
        List<EquipmentHistoryDetail> lstHistoryDetail = new ArrayList<>();
        if (stgNew==null || equipmentHistoryUpdate == null) return;
        ActionAudit actionAudit =new ActionAudit();
        actionAudit.setReferenceId(equipmentHistoryUpdate.getEquipmentHistoryId());
        actionAudit.setActionDatetime(DateTimeUtils.getDate());
        actionAudit.setActionType(InventoryConstanst.ACTION_TYPE.TYPE_5L);
        ActionAuditService.saveOrUpdate(actionAudit);
        for (int i=0; i<lstColumn.length;i++){
            EquipmentHistoryDetail obj = new EquipmentHistoryDetail();
            obj.setEquipmentHistoryId(equipmentHistoryUpdate.getEquipmentHistoryId());
            switch (lstColumn[i]) {
                case "AVAILABLE_QUANTITY":
                    obj.setFeildName(InventoryConstanst.ACTION_AU_DETAIL.AVAILABLE_QUANTITY);
                    obj.setNewValue(stgNew.getAvailableQuantity() == null ? "":stgNew.getAvailableQuantity().toString());
                    if(availQuantityOldSave!=null) {
                        obj.setOldValue(availQuantityOldSave.toString());
                    }else {
                        obj.setOldValue("");
                    }
                    break;
                case "STOCK_STATUS":
                    obj.setFeildName(InventoryConstanst.ACTION_AU_DETAIL.STOCK_STATUS);
                    obj.setNewValue(sessionBean.getStockStatusName(InventoryConstanst.StockStatus.CANCEL));
                    if(stockStatusOld!=null) {
                        obj.setOldValue(sessionBean.getStockStatusName(stockStatusOld));
                    }else {
                        obj.setOldValue("");
                    }
                    break;
                case "REFERENCE_ID":
                    obj.setFeildName(InventoryConstanst.ACTION_AU_DETAIL.REFERENCE_ID_DC);
                    obj.setNewValue(equipmentHistoryUpdate.getReferenceId()==null?"":equipmentHistoryUpdate.getReferenceId().toString());
                    if(equipmentHistoryUpdateOld!=null){
                        obj.setOldValue(equipmentHistoryUpdateOld.getReferenceId()==null ? "":equipmentHistoryUpdateOld.getReferenceId().toString());
                    }else {
                        obj.setOldValue("");
                    }

                    break;
                case "DOCUMENT":
                    obj.setFeildName(InventoryConstanst.ACTION_AU_DETAIL.DOCUMENT);
                    obj.setNewValue(equipmentHistoryUpdate.getDocument() == null ? "":equipmentHistoryUpdate.getDocument().toString());
                    if(equipmentHistoryUpdateOld!=null){
                        obj.setOldValue(equipmentHistoryUpdateOld.getDocument() == null ? "":equipmentHistoryUpdateOld.getDocument());
                    }else {
                        obj.setOldValue("");
                    }

                    break;
            }
            lstHistoryDetail.add(obj);
        }
        equipmentHistoryDetailService.saveOrUpdate(lstHistoryDetail);
//        ActionAudit actionAudit = new ActionAudit();
    }
    public void initDatars(){
        List<StockGoodsInvSerialDTO> lst = new ArrayList<>();
        for (StockGoodsInvSerialDTO obj: lsDataRs) {
            if(obj.getEquipmentProfileId().equals(goodsId)){
                lst.add(obj);
            }
//            for (StockGoodsInvSerialDTO objNew:lsData) {
//                if(obj.getId().equals(objNew.getId())){
//                    lst.add(objNew);
//                    break;
//                }
//                if(obj.getEquipmentProfileId().equals(goodsId)){
//                    lst.add(objNew);
//                    break;
//                }
//            }
        }
        lsDataRs.clear();
        lsDataRs.addAll(lst);
    }
    /**
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason the reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
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
    public Long getProviderId() {
        return providerId;
    }

    /**
     * @param providerId the providerId to set
     */
    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    /**
     * @return the fromSerial
     */
    public String getFromSerial() {
        return fromSerial;
    }

    /**
     * @param fromSerial the fromSerial to set
     */
    public void setFromSerial(String fromSerial) {
        this.fromSerial = fromSerial;
    }

    /**
     * @return the toSerial
     */
    public String getToSerial() {
        return toSerial;
    }

    /**
     * @param toSerial the toSerial to set
     */
    public void setToSerial(String toSerial) {
        this.toSerial = toSerial;
    }

    /**
     * @return the equipmentStatus
     */
    public Long getEquipmentStatus() {
        return equipmentStatus;
    }

    /**
     * @param equipmentStatus the equipmentStatus to set
     */
    public void setEquipmentStatus(Long equipmentStatus) {
        this.equipmentStatus = equipmentStatus;
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
     * @return the lsDataRs
     */
    public List<StockGoodsInvSerialDTO> getLsDataRs() {
    	belowSumDataSelected = lsDataRs.size();
        return lsDataRs;
    }

    /**
     * @param lsDataRs the lsDataRs to set
     */
    public void setLsDataRs(List<StockGoodsInvSerialDTO> lsDataRs) {
        this.lsDataRs = lsDataRs;
    }

    /**
     * @return the statusChange
     */
    public Long getStatusChange() {
        return statusChange;
    }

    /**
     * @param statusChange the statusChange to set
     */
    public void setStatusChange(Long statusChange) {
        this.statusChange = statusChange;
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
     * @return the setSize
     */
    public int getSetSize() {
        if (lsDataRs == null) {
            setSize = 0;
        } else {
            setSize = lsDataRs.size();
        }
        return setSize;
    }

    /**
     * @param setSize the setSize to set
     */
    public void setSetSize(int setSize) {
        this.setSize = setSize;
    }

    /**
     * @return the lsSize
     */
    public int getLsSize() {
        if (lsData == null) {
            lsSize = 0;
        } else {
            lsSize = lsData.size();
        }
        return lsSize;
    }

    /**
     * @param lsSize the lsSize to set
     */
    public void setLsSize(int lsSize) {
        this.lsSize = lsSize;
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
     * @return the stockStatusId
     */
    public String getStockStatusId() {
        return stockStatusId;
    }

    /**
     * @param stockStatusId the stockStatusId to set
     */
    public void setStockStatusId(String stockStatusId) {
        this.stockStatusId = stockStatusId;
    }

    /**
     * @return the disableStatus
     */
    public Boolean getDisableStatus() {
        disableStatus = false;
        if (typeId != null) {
            disableStatus = typeId.equals(InventoryConstanst.GoodsTransType.STOCK_STATUS);
        }
        if (lsDataRs != null && !lsDataRs.isEmpty()) {
            disableStatus = true;
        }
        return disableStatus;
    }

    /**
     * @param disableStatus the disableStatus to set
     */
    public void setDisableStatus(Boolean disableStatus) {
        this.disableStatus = disableStatus;
    }

    /**
     * @return the disableType
     */
    public Boolean getDisableType() {
        return lsDataRs != null && !lsDataRs.isEmpty();
    }

    /**
     * @param disableType the disableType to set
     */
    public void setDisableType(Boolean disableType) {
        this.disableType = disableType;
    }

    /**
     * @return the disableStock
     */
    public Boolean getDisableStock() {
        disableStock = false;
        if (typeId != null) {
            disableStock = typeId.equals(InventoryConstanst.GoodsTransType.GOODS_STATUS);
        }
        if (lsDataRs != null && !lsDataRs.isEmpty()) {
            disableStock = true;
        }
        return disableStock;
    }

    /**
     * @param disableStock the disableStock to set
     */
    public void setDisableStock(Boolean disableStock) {
        this.disableStock = disableStock;
    }

    /**
     * @return the statusName
     */
    public String getStatusName() {

        return statusName;
    }

    /**
     * @param statusName the statusName to set
     */
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
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

    /**
     * @return the notRenderStock
     */
    public Boolean getNotRenderStock() {
        notRenderStock = false;
        if (typeId != null) {
            notRenderStock = typeId.equals(InventoryConstanst.GoodsTransType.GOODS_STATUS);
        }
        return notRenderStock;
    }

    /**
     * @param notRenderStock the notRenderStock to set
     */
    public void setNotRenderStock(Boolean notRenderStock) {
        this.notRenderStock = notRenderStock;
    }

    /**
     * @return the notRenderStatus
     */
    public Boolean getNotRenderStatus() {
         notRenderStatus = false;
        if (typeId != null) {
            notRenderStatus = typeId.equals(InventoryConstanst.GoodsTransType.STOCK_STATUS);
        }
        return notRenderStatus;
    }

    public String getGroupTypeName(Long value){
        for (ApDomain app:lstGroupType) {
            if(app.getValue().equals(value)){
                return app.getName();
            }
        }
        return "";
    }
    /**
     * @param notRenderStatus the notRenderStatus to set
     */
    public void setNotRenderStatus(Boolean notRenderStatus) {

        this.notRenderStatus = notRenderStatus;
    }

	public int getSumDataSelected() {
		return sumDataSelected;
	}

	public void setSumDataSelected(int sumDataSelected) {
		this.sumDataSelected = sumDataSelected;
	}

	public int getBelowSumDataSelected() {
		return belowSumDataSelected;
	}

	public void setBelowSumDataSelected(int belowSumDataSelected) {
		this.belowSumDataSelected = belowSumDataSelected;
	}

	public boolean isRenderReason() {
		return renderReason;
	}

	public void setRenderReason(boolean renderReason) {
		this.renderReason = renderReason;
	}

    /**
     * @return the bundle
     */
    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public Long getActionUserId() {
        return actionUserId;
    }

    public void setActionUserId(Long actionUserId) {
        this.actionUserId = actionUserId;
    }

    public List<Staff> getLstStaff() {
        return lstStaff;
    }

    public void setLstStaff(List<Staff> lstStaff) {
        this.lstStaff = lstStaff;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public List<ApDomain> getLstGroupType() {
        return lstGroupType;
    }

    public void setLstGroupType(List<ApDomain> lstGroupType) {
        this.lstGroupType = lstGroupType;
    }

    public StockGoods getStockGoodsUpdate() {
        return stockGoodsUpdate;
    }

    public void setStockGoodsUpdate(StockGoods stockGoodsUpdate) {
        this.stockGoodsUpdate = stockGoodsUpdate;
    }

    public EquipmentHistory getEquipmentHistoryUpdate() {
        return equipmentHistoryUpdate;
    }

    public void setEquipmentHistoryUpdate(EquipmentHistory equipmentHistoryUpdate) {
        this.equipmentHistoryUpdate = equipmentHistoryUpdate;
    }

    public EquipmentsProfile getEquipmentsProfileUpdate() {
        return equipmentsProfileUpdate;
    }

    public void setEquipmentsProfileUpdate(EquipmentsProfile equipmentsProfileUpdate) {
        this.equipmentsProfileUpdate = equipmentsProfileUpdate;
    }

    public StreamedContent getFileExport() {
        return fileExport;
    }

    public void setFileExport(StreamedContent fileExport) {
        this.fileExport = fileExport;
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
//            if (id.equals("approCode")) {
//                String value = object.toString().trim();
//                if (value!=null && !value.isEmpty() && (!StringUtils.isNumberLong̣̣(value) || Long.parseLong(value) < 0)) {
//                    valid = false;
//                    msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false), languageBean.getMessage("audit.validator.reference.number", null, false));
//                }
//            }

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

    public void export_excel() {
        try {

            if (lsDataRs == null || lsDataRs.isEmpty()) {
                languageBean.showMeseage("Info", "audit.not.record");
                RequestContext context = RequestContext.getCurrentInstance();
                context.update("frm");
                return;
            }
            List<StockGoodsInvSerialDTO> lsResult = new ArrayList<>();
            Long countAvai =null;
            if(stockGoodsUpdate!=null){
                countAvai = stockGoodsUpdate.getAvailableQuantity();
            }
            Long indext = 0L;
            for (StockGoodsInvSerialDTO obj:lsDataRs) {
                obj.setIndex(++indext);
                obj.setGroupTypeName(getGroupTypeName(obj.getGroupType()));
                obj.setEquipmentStatusName(sessionBean.getService().getEquipsStatusName(obj.getEquipmentProfileStatus()));
                obj.setAvailableCount(countAvai);
                lsResult.add(obj);
            }


            ExportExcel ee = new ExportExcel();
            String s = ee.exportAudit(lsResult, sessionBean.getStaff().getStaffName());
            setFileExport(new DefaultStreamedContent(new FileInputStream(s),
                    "xlsx", "dieu_chinh_tbvt.xlsx"));

        } catch (Exception ex) {
            ex.printStackTrace();
            languageBean.showMeseage("Info", "errorProcess");
        }
    }

    public Long getGroupTypeId() {
        return groupTypeId;
    }

    public void setGroupTypeId(Long groupTypeId) {
        this.groupTypeId = groupTypeId;
    }

    public List<EquipmentsGroup> getLsgGroup() {
        return lsgGroup;
    }

    public void setLsgGroup(List<EquipmentsGroup> lsgGroup) {
        this.lsgGroup = lsgGroup;
    }

    public void selectEquipType() {
        lsgGroup.clear();
        if(groupTypeId!=null){
            lsgGroup = getGoodsGroupByType(groupTypeId);
            goodsGroupId = null;
//            if(lsgGroup!=null && !lsgGroup.isEmpty()){
//                goodsGroupId = lsgGroup.get(0).getEquipmentsGroupId();
//            }
        }else {
            lsgGroup = sessionBean.getLsgGroupActive();
            goodsGroupId = null;
        }
        changeGoodsGroup();
    }
    public List<EquipmentsGroup>  getGoodsGroupByType(Long equipType) {
        List<EquipmentsGroup> objs = new ArrayList<>();
        List<EquipmentsGroup> lsAll = sessionBean.getLsgGroupActive();
        if ( lsAll!= null && equipType != null) {
            for (EquipmentsGroup g : lsAll) {
                if (equipType.equals(g.getEquipmentsGroupType())) {
                    objs.add(g);
                }
            }
        }
        return objs;
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
}
