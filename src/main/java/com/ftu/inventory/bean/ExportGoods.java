/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bean;

import com.ftu.inventory.bo.*;
import com.ftu.inventory.bussiness.EquipmentsDetailService;
import com.ftu.inventory.dto.StockGoodsInvSerialDTO;
import com.ftu.inventory.exportExcel.ExportExcel;
import com.ftu.language.LanguageBean;
import com.ftu.constanst.InventoryConstanst;
import com.ftu.java.bo.SerialA;
import com.ftu.staff.bo.ApDomain;
import com.ftu.staff.bo.Shop;
import com.ftu.utils.AnalysisSerial;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 * @author E5420
 */
@ManagedBean(name = "exportGoods", eager = true)
@SessionScoped
public class ExportGoods implements Serializable {

    SimpleDateFormat sf = new SimpleDateFormat(".yy.MM.dd.");
    private Long reasonId;
    private Long typeId;
    private Long goodsId;
    private String fromSerial;
    private String toSerial;
    private String orderCode;
    private String resonWaranty;
    private Long goodStatus;
    private List<ApDomain> listReasons;
    private List<ApDomain> listTransType;
    private Long statusChange;
    private List<StockGoodsInvSerialDTO> lsData;
    private StockGoodsInvSerialDTO lsDataSelected;
    private List<StockGoodsInvSerialDTO> lsDataRsSelected;
    private List<SerialA> lsAnaLsData;
    private List<SerialA> lsAnaLsRs;
    private String goodsName;
    private String goodsStatusName;
    private StockTransactionDetail stDetail = new StockTransactionDetail();
    private StockTransaction st = new StockTransaction();
    @ManagedProperty(value = "#{sessionBean}")
    private SessionBean sessionBean;
    @ManagedProperty(value = "#{languageBean}")
    private LanguageBean languageBean;
    private Long goodsGroupId;
    private List<EquipmentsProfile> lsGoods;
    List<EquipmentsGroup> lsGoodsGroup;
    private boolean disableAdd;
    private String goods;
    private String reasonName;
    private List<StockGoodsInvSerialDTO> lsDataRs;
    private List<SerialA> lsAnaLsDataSelect;
    private Date today = new Date();
    private Long staffIdTo;
    private String profileName;
    private String staffName;
    private List<EquipmentsDetail> equipmentsDetails;
    private EquipmentsDetailService equipmentsDetailService = new EquipmentsDetailService();
    //huy
    private Long sumDataSelected = 0L;
    private Long rightSumDataSelected = 0L;
    private  Boolean disableSelected = true;
    private Boolean disablePrint = true;

    private transient StreamedContent fileExport;

    @PostConstruct
    public void init() {
    	sessionBean.loadData(sessionBean.getStaff(), sessionBean.getShop());
        goodStatus = InventoryConstanst.GoodsStatus.ERROR;
        listTransType = sessionBean.getService().getExportReason();
        changeType();
        lsGoodsGroup = sessionBean.getLsgGroup();
        changeGoodsGroup();
    }

    public void hanleLsDataSelect() {
        sumDataSelected = 0L;
        for (SerialA temp : lsAnaLsDataSelect) {
            sumDataSelected = sumDataSelected + temp.getCount();
        }
    }

    public void getSumRightTable() {
        rightSumDataSelected = 0L;
        for (SerialA temp : lsAnaLsRs) {
            rightSumDataSelected += temp.getCount();
        }
    }

    public void changeGoodsGroup() {
        if (lsGoodsGroup == null || lsGoodsGroup.isEmpty()) {
            lsGoods = new ArrayList<>();
            goodsId = -1L;
            goods = "";
            profileName = "";
            return;
        }

        if (lsGoodsGroup != null && !lsGoodsGroup.isEmpty() && (goodsGroupId == null || goodsGroupId == 0L)) {
            setGoodsGroupId(lsGoodsGroup.get(0).getEquipmentsGroupId());
        }
        if (getGoodsGroupId() != null && getGoodsGroupId() > 0) {
            List<EquipmentsProfile> lsg = sessionBean.getLsEquipments();
            lsGoods = new ArrayList<>();
            if (lsg != null && !lsg.isEmpty()) {
                for (EquipmentsProfile g : lsg) {
                    if (goodsGroupId.equals(g.getEquipmentsGroupId())
                            && InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(g.getManagementType())) {
                        lsGoods.add(g);
                    }
                }
            }
        }
        if (!lsGoods.isEmpty()) {
            EquipmentsProfile g = lsGoods.get(0);
            goodsId = g.getProfileId();
            goods = g.getProfileCode() + "-" + g.getProfileName();
            profileName = g.getProfileName();
        } else {
            goodsId = 0L;
            goods = "";
            profileName = "";
        }
    }

    public void goodsSelect() {
        if (lsGoods != null) {
            for (EquipmentsProfile g : lsGoods) {
                if ((g.getProfileCode() + "-" + g.getProfileName()).equals(goods)) {
                    goodsId = g.getProfileId();
                    profileName = g.getProfileName();
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
                if (gs == null || gs.isEmpty() || g.getProfileCode().toLowerCase().contains(goods.toLowerCase())) {
                    rs.add(g.getProfileCode() + "-" + g.getProfileName());
                }
            }
        }
        return rs;
    }

    public void search() {
//        lsAnaLsDataSelect.clear();
        lsDataSelected = null;
        if (getGoodsId() < 1L || goodStatus < 1L) {
            getLanguageBean().showMeseage("Info", "exportRequiredSearch");
            lsData = new ArrayList<>();
            return;
        }
        StockGoodsInvSerialDTO sgis = new StockGoodsInvSerialDTO(fromSerial, getGoodStatus(), InventoryConstanst.StockStatus.INSTOCK, sessionBean.getShop().getShopId(), null, getGoodsId());
        sgis.setEquipmentProfileStatus(goodStatus);
        sgis.setEquipmentProfileId(goodsId);
        for (EquipmentsProfile g : lsGoods) {
            if(g.getProfileId().equals(goodsId) && InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(g.getManagementType())){
                setLsData(sessionBean.getService().getSerialStockGoodsExMaintain(sgis));
                break;
            }else if(g.getProfileId().equals(goodsId)){
                if(!fromSerial.isEmpty()){
                    lsData.clear();
                    return;
                }
                equipmentsDetails = equipmentsDetailService.findByProfile(getGoodsId());
                setLsData(sessionBean.getService().getNoStockGoodsExMaintain(sgis));
//                setLsData(sessionBean.getService().getNotSerialStockGoodsEx(sgis));
//                aliasNotSerial();
                break;
            }
        }

    }

    public void aliasNotSerial(){
        if (lsData==null || equipmentsDetails == null) return;
        for (StockGoodsInvSerialDTO dto : lsData) {
            for (EquipmentsDetail det:equipmentsDetails) {
                if(det.getProviderId().equals(dto.getEquipmentProfileId())){
                    dto.setWarrantyStatus(det.getWarantyStatus());
                    dto.setWarantyExpiredDate(det.getWarantyExpiredDate());
                    dto.setEquimentDetailId(det.getId());
                    dto.setSpecification(det.getEquipmentSpecification());
                }
            }

        }
    }
    public void changeType() {
        if (listTransType != null && !listTransType.isEmpty()) {
            if (typeId == null || typeId == 0L) {
                typeId = listTransType.get(0).getValue();
            }
        }
        if (typeId != null && typeId > 0) {

            orderCode = "EX_" + typeId + sf.format(new Date()) + sessionBean.getService().getTransactionActionCount(typeId.toString()).toString();
        } else {
            orderCode = "";
        }
        listReasons = sessionBean.getService().getListReason(typeId.toString());
    }

    public StockTransactionDetail getByGoodsId(Long gId, Long gsts, Long quantityCurrent) {
        for (StockTransactionDetail std : getSt().getLsDetail()) {
            if (std.getGoodsId().equals(gId) && std.getGoodsStatus().equals(gsts)) {
                std.setQuantity(std.getQuantity()+quantityCurrent);
                return std;
            }
        }
//        EquipmentsProfile g = sessionBean.getGoods(gId.toString());
        StockTransactionDetail std = new StockTransactionDetail(quantityCurrent, gId, gsts);
        st.getLsDetail().add(std);
        return std;
    }
    public void clickCheckbox(){
    }
    public void viewClick() {
        if(lsDataRsSelected!=null){
            if(lsDataSelected==null || lsDataRsSelected.contains(lsDataSelected) ){
                disableSelected = true;
                return;
            }
        }else if(lsDataSelected == null){
            disableSelected  = true;
            return;
        }
        disableSelected = false;
    }
    public void saveSelected() {
        if(resonWaranty.trim().isEmpty()){
            getLanguageBean().showMeseage("Info", "reson.not.empty");
            return;
        }
        lsDataSelected.setReasonsWarranty(resonWaranty);
        if(lsDataRsSelected==null || lsDataRsSelected.isEmpty()){
            lsDataRsSelected = new ArrayList<>();
            if(lsDataSelected!=null){
                lsDataRsSelected.add(lsDataSelected);
            }
        }else if(lsDataSelected!=null) {
            if(!lsDataRsSelected.contains(lsDataSelected)){
                lsDataRsSelected.add(lsDataSelected);
            }
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('dlSelectedData').hide();");
        disableSelected = true;
        resonWaranty = "";
        if(lsDataRsSelected!=null && !lsDataRsSelected.isEmpty()){
            disablePrint = false;
        }else {
            disablePrint = true;
        }

    }
    public void addSelected() {
        if(lsDataSelected == null){
            getLanguageBean().showMeseage("success", "equip.not.selected");
            return;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('dlSelectedData').show();");
    }
    public String checkSerial() {
//        if (st == null || st.getLsDetail().isEmpty()) {
//            return "Stock detail is empty !";
//        }
//        for (StockTransactionDetail item : st.getLsDetail()) {
//            if (item.getSetSerial() == null || item.getSetSerial().isEmpty()) {
//                return "Detail " + item.getProfileName() + " has no Serial";
//            }
//        }
        return null;
    }

    public void export() {
        try {
            Long sumQuantity = 0L;
            int index = 0;
            for (StockGoodsInvSerialDTO a : lsDataRsSelected) {
                sumQuantity += a.getQuantity();
                a.setIndexPri(++index);
                a.setGoodsGroupName(sessionBean.getGoodsGroupNameByGoods(a.getEquipmentProfileId().toString()));
                a.setProfileCode(sessionBean.getEquipProfileCode(a.getEquipmentProfileId().toString()));
                a.setProfileName(sessionBean.getEquipProfileName(a.getEquipmentProfileId().toString()));
            }

            TransactionAction tmp = new TransactionAction(orderCode, typeId.toString(), sessionBean.getStaff().getStaffId(), sessionBean.getStaff().getShopId(),
                    null, sessionBean.getShop().getShopId(), null);

            tmp.setAssStaffName(sessionBean.getStaff().getStaffName());
            tmp.setStaffName(staffName);
//            tmp.setReasonName(getReasonName(reasonId));
//            tmp.setDescription(note != null && !note.trim().isEmpty() ? staff.getUserName() + ": " + note : "");
            tmp.setCreateDatetime(new Date());
            tmp.setAssessmentDatetime(new Date());


            ExportExcel ee = new ExportExcel();
            String createShop = sessionBean.getService().getShopById().getShopName();
            String s = ee.printWarranty(createShop, null, lsDataRsSelected, tmp,
                    sumQuantity);
//			listTADTemp.clear();
            fileExport = new DefaultStreamedContent(new FileInputStream(s), "xlsx", "phieu_xuat_kho.xlsx");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void action() {
        changeType();
        if (typeId <= 0) {
            return;
        } else {
            String s = checkSerial();
            if (s != null) {
                return;
            }
        }
        if(lsDataRsSelected == null || lsDataRsSelected.isEmpty()){
            getLanguageBean().showMeseage("success", "equip.not.selected");
            return;
        }
        try {
            StockTransaction strs = new StockTransaction(sessionBean.getStaff().getStaffId(),
                    sessionBean.getShop().getShopId(), null, null, typeId.toString(),
                    InventoryConstanst.StockTransactionStatus.SUCCESS,
                    InventoryConstanst.StockTransactionDeliveryType.TRANS);
//            List<StockGoodsInvSerialDTO> lsg = new ArrayList<>();
            List<Long> lstEquipmentId = new ArrayList<>();
            List<Long> lstProfileId = new ArrayList<>();
            for (StockGoodsInvSerialDTO sg : lsDataRsSelected) {
                stDetail = getByGoodsId(sg.getEquipmentProfileId(), sg.getEquipmentProfileStatus(),
                        sg.getQuantity());
                if (sg.getSerial()!=null && !sg.getSerial().isEmpty()) {
                    stDetail.getSetSerial().add(sg);
                }else {
                    continue;
                }
                if(sg.getEquimentDetailId()!=null){
                    EquipmentsDetail detailUpdate = equipmentsDetailService.findById(sg.getEquimentDetailId());
                    detailUpdate.setWarrantyReason(sg.getReasonsWarranty());
                    detailUpdate.setWarantyStatus(InventoryConstanst.WARAN_STATUS.WARAN_STATUS_1L);
                    if(detailUpdate.getWarrantyCount()==null || detailUpdate.getWarrantyCount().equals(0L)){
                        detailUpdate.setWarrantyCount(1L);
                    }else {
                        detailUpdate.setWarrantyCount(detailUpdate.getWarrantyCount() + 1L);
                    }
                    detailUpdate.setLastUsedDate(new Date());
                    equipmentsDetailService.saveOrUpdate(detailUpdate);
                }

//                for (EquipmentsProfile g : lsGoods) {
//                    if(g.getProfileId().equals(sg.getEquipmentProfileId())
//                            && InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(g.getManagementType())){
//                        if(sg.getEquimentDetailId()!=null && !lstEquipmentId.contains(sg.getEquimentDetailId())){
//                            lstEquipmentId.add(sg.getEquimentDetailId());
//                        }
//                        break;
//                    }else if(g.getProfileId().equals(sg.getEquipmentProfileId())){
//                        if(sg.getEquipmentProfileId()!=null && !lstEquipmentId.contains(sg.getEquipmentProfileId())){
//                            lstProfileId.add(sg.getEquipmentProfileId());
//                        }
//                        break;
//
//                    }
//                }

            }
            strs.setLsDetail(st.getLsDetail());
//            sessionBean.getService().exportSave(typeId, orderCode, st, reasonId);
            sessionBean.getService().exportSave(typeId, orderCode, strs, reasonId, staffName);
//            equipmentsDetailService.updateWaranStatus(lstEquipmentId, lstProfileId, InventoryConstanst.WARAN_STATUS.WARAN_STATUS_1L);
            getLanguageBean().showMeseage("success", "succesProces");
            staffName = "";
            refresh();
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('cfExport').hide();");
        } catch (Exception ex) {
            ex.printStackTrace();
            getLanguageBean().showMeseage("error", "errorProcess");
        }
    }

    private String getReasonName(Long reasonId) {
        if (listReasons != null) {
            for (ApDomain d : listReasons) {
                if (d.getValue().equals(reasonId)) {
                    return d.getName();
                }
            }
        }
        return "";
    }

    public void save() {
        changeType();
        if(lsDataRsSelected == null || lsDataRsSelected.isEmpty()){
            getLanguageBean().showMeseage("success", "equip.not.selected");
            return;
        }
        if (typeId <= 0) {
            getLanguageBean().showMeseage("Info", "exrequireReason");
        } else {
            String s = checkSerial();
            if (s != null) {
                languageBean.showMeseage_("Info", s);
                return;
            }
            reasonName = getReasonName(reasonId);
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('cfExport').show();");
        }
    }

    public void addAll() {

    }

    public void add() {

    }

    public void refresh() {
        getSt().setLsDetail(new ArrayList<StockTransactionDetail>());
        stDetail = new StockTransactionDetail();
        lsDataRs = new ArrayList<>();
        lsData = new ArrayList<>();
        lsDataRsSelected = new ArrayList<>();
        lsDataSelected = null;
        changeType();
        search();
    }

    public void removeRs(StockGoodsInvSerialDTO gs) {
        getStDetail().getSetSerial().remove(gs);
        stDetail.setQuantity(stDetail.getQuantity() - 1);
    }

    public void removeSgRs(StockTransactionDetail sts) {
        st.getLsDetail().remove(sts);
        if (stDetail != null && sts.getRowkey().equals(stDetail.getRowkey())) {
            stDetail = null;
        }
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
     * @return the goodsName
     */
    public String getGoodsName() {
        return goodsName;
    }

    /**
     * @param goodsName the goodsName to set
     */
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

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

    /**
     * @return the st
     */
    public StockTransaction getSt() {
        return st;
    }

    /**
     * @param st the st to set
     */
    public void setSt(StockTransaction st) {
        this.st = st;
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
     * @return the disableAdd
     */
    public boolean isDisableAdd() {
        return checkSerial() != null;
    }

    /**
     * @param disableAdd the disableAdd to set
     */
    public void setDisableAdd(boolean disableAdd) {
        this.disableAdd = disableAdd;
    }

    /**
     * @return the lsAnaLsData
     */
    public List<SerialA> getLsAnaLsData() {
        if (lsData == null || lsData.isEmpty()) {
            lsAnaLsData = new ArrayList<>();

        } else {
            AnalysisSerial ann = new AnalysisSerial(null, lsData);
            lsAnaLsData = ann.analysisInvSerial();
        }
        return lsAnaLsData;
    }

    /**
     * @param lsAnaLsData the lsAnaLsData to set
     */
    public void setLsAnaLsData(List<SerialA> lsAnaLsData) {
        this.lsAnaLsData = lsAnaLsData;
    }

    /**
     * @return the lsAnaLsRs
     */
    public List<SerialA> getLsAnaLsRs() {
        lsAnaLsRs = new ArrayList<>();
        if (st != null && !st.getLsDetail().isEmpty()) {
            for (StockTransactionDetail std : st.getLsDetail()) {
                List<StockGoodsInvSerialDTO> ls = new ArrayList<>(std.getSetSerial());
                AnalysisSerial ann = new AnalysisSerial(null, ls);
                List<SerialA> lsA = ann.analysisInvSerial();
                for (SerialA sa : lsA) {
                    sa.setStatusName(std.getGoodsStatusName());
                    sa.setGoodsName(std.getGoodsName());
                    sa.setGoodsId(std.getGoodsId());
                }
                lsAnaLsRs.addAll(lsA);
            }
        }
        getSumRightTable();
        return lsAnaLsRs;
    }

    public  void removeSeleted(StockGoodsInvSerialDTO dto){
        if(lsDataRsSelected!=null && !lsDataRsSelected.isEmpty()){
            lsDataRsSelected.remove(dto);
        }
        if(lsDataRsSelected!=null && !lsDataRsSelected.isEmpty()){
            disablePrint = false;
        }else {
            disablePrint  = true;
        }
//        if(lsDataSelected!=null && !lsDataSelected.isEmpty()){
//            lsDataSelected.remove(dto);
//        }
    }

    /**
     * @param lsAnaLsRs the lsAnaLsRs to set
     */
    public void setLsAnaLsRs(List<SerialA> lsAnaLsRs) {
        this.lsAnaLsRs = lsAnaLsRs;
    }

    /**
     * @return the lsDataRs
     */
    public List<StockGoodsInvSerialDTO> getLsDataRs() {
        if (lsDataRs != null && !lsDataRs.isEmpty()) {
            Collections.sort(lsDataRs, new Comparator<StockGoodsInvSerialDTO>() {
                @Override
                public int compare(StockGoodsInvSerialDTO t, StockGoodsInvSerialDTO t1) {
//                    int rs = t.getProviderId().intValue() - t1.getProviderId().intValue();
//                    return rs == 0 ? new Integer(t.getSerial()) - new Integer(t1.getSerial()) : rs;
                    return (t.getSerial().compareTo(t1.getSerial()));
                }
            });
        }
        return lsDataRs;
    }

    /**
     * @param lsDataRs the lsDataRs to set
     */
    public void setLsDataRs(List<StockGoodsInvSerialDTO> lsDataRs) {
        this.lsDataRs = lsDataRs;
    }

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
     * @return the lsAnaLsDataSelect
     */
    public List<SerialA> getLsAnaLsDataSelect() {
        return lsAnaLsDataSelect;
    }

    /**
     * @param lsAnaLsDataSelect the lsAnaLsDataSelect to set
     */
    public void setLsAnaLsDataSelect(List<SerialA> lsAnaLsDataSelect) {
        this.lsAnaLsDataSelect = lsAnaLsDataSelect;
    }

    /**
     * @return the reasonName
     */
    public String getReasonName() {
        return reasonName;
    }

    /**
     * @param reasonName the reasonName to set
     */
    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }

    public Long getSumDataSelected() {
        return sumDataSelected;
    }

    public void setSumDataSelected(Long sumDataSelected) {
        this.sumDataSelected = sumDataSelected;
    }

    public Long getRightSumDataSelected() {
        return rightSumDataSelected;
    }

    public void setRightSumDataSelected(Long rightSumDataSelected) {
        this.rightSumDataSelected = rightSumDataSelected;
    }

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public Long getStaffIdTo() {
        return staffIdTo;
    }

    public void setStaffIdTo(Long staffIdTo) {
        this.staffIdTo = staffIdTo;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public StockGoodsInvSerialDTO getLsDataSelected() {
        return lsDataSelected;
    }

    public void setLsDataSelected(StockGoodsInvSerialDTO lsDataSelected) {
        this.lsDataSelected = lsDataSelected;
    }

    public List<StockGoodsInvSerialDTO> getLsDataRsSelected() {
        return lsDataRsSelected;
    }

    public void setLsDataRsSelected(List<StockGoodsInvSerialDTO> lsDataRsSelected) {
        this.lsDataRsSelected = lsDataRsSelected;
    }

    public Boolean getDisableSelected() {
        return disableSelected;
    }

    public void setDisableSelected(Boolean disableSelected) {
        this.disableSelected = disableSelected;
    }

    public String getResonWaranty() {
        return resonWaranty;
    }

    public void setResonWaranty(String resonWaranty) {
        this.resonWaranty = resonWaranty;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public Boolean getDisablePrint() {
        return disablePrint;
    }

    public void setDisablePrint(Boolean disablePrint) {
        this.disablePrint = disablePrint;
    }

    public StreamedContent getFileExport() {
        return fileExport;
    }

    public void setFileExport(StreamedContent fileExport) {
        this.fileExport = fileExport;
    }
}
