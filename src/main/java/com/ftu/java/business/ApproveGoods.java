/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.java.business;

import com.ftu.constanst.InventoryConstanst;
import com.ftu.inventory.bean.SessionBean;
import com.ftu.inventory.bo.*;
import com.ftu.inventory.bussiness.EquipmentsDetailService;
import com.ftu.inventory.dao.*;
import com.ftu.ws.BusinessService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author E5420
 */
public class ApproveGoods {

    TransactionAction ta;
    List<StockTransactionDetail> lsStoclDetail;
    List<StockTransactionSerial> lsSerials;
    private Long staffId;
    private Long handleType = 0L;
    String description;
    public ApproveGoods(TransactionAction ta, Long handleType) {
        this.ta = ta;
        description=ta.getDescription()== null ? "" : ta.getDescription();
        this.staffId=ta.getAssessmentStaffId();
        this.handleType = handleType;
        if(this.handleType == null) this.handleType = InventoryConstanst.ImportType.importWarranty;
    }

    public void deleteDataReject(){
        EquipmentDetailDAO detailDAO = new EquipmentDetailDAO();
        //voi truong hop khong thay the serial
        List<EquipmentsDetail> lst =  detailDAO.getByImTransactionId(ta.getTransactionActionId());
        if(lst!=null){
            for (EquipmentsDetail obj:lst) {
                detailDAO.delete(obj);
            }
        }
    }
    public Boolean reject() {
        deleteDataReject();
        TransactionActionDAO taDAO = new TransactionActionDAO();
        ta = taDAO.findById(ta.getTransactionActionId());
        if (ta == null || ta.getStatus().equals(InventoryConstanst.TransactionStatus.APPROVE)) {
            return false;
        }
        ta.setAssessmentStaffId(staffId);
        ta.setAssessmentDatetime(new Date());
        ta.setDescription(description);
        ta.setStatus(InventoryConstanst.TransactionStatus.CANCEL);
        taDAO.saveOrUpdate(ta);
        StockTransactionDAO stDAO = new StockTransactionDAO();
        stDAO.rejectStock(ta.getTransactionActionId(),ta.getAssessmentStaffId());
  //      EtagDetailDAO edDAO = new EtagDetailDAO();
 //       edDAO.deleteByTaId(ta.getTransactionActionId());
//        deleteStock();
//        deleteTransaction();
        return true;
    }

    public Boolean approve() {
        StockTransactionDetailDAO stdDAO = new StockTransactionDetailDAO();
        StockTransactionSerialDAO stsDAO = new StockTransactionSerialDAO();
        StockTransactionDAO stDAO = new StockTransactionDAO();
        TransactionActionDAO taDAO = new TransactionActionDAO();
        StockGoodsDAO stgDAO = new StockGoodsDAO();
        EquipmentsProfileDAO profileDAO = new EquipmentsProfileDAO();
        EquipmentDetailDAO detailDAO = new EquipmentDetailDAO();
        TransactionActionDetailDAO transactionActionDetailDAO = new TransactionActionDetailDAO();
        ta = taDAO.findById(ta.getTransactionActionId());
        if (ta == null || ta.getStatus().equals(InventoryConstanst.TransactionStatus.APPROVE)) {
            return false;
        }
//inserHistory old
        SessionBean sessionBean = new SessionBean();
        List<String[]> lsValueOld = new ArrayList<>();
        String[] lstColumn = {InventoryConstanst.ACTION_AU_DETAIL.SHOP_ID};
        List<String[]> lsValueOldNoSerial = new ArrayList<>();
        String[] lstColumnNoSerial = {InventoryConstanst.ACTION_AU_DETAIL.SHOP_ID, InventoryConstanst.ACTION_AU_DETAIL.QUANTITY,
                InventoryConstanst.ACTION_AU_DETAIL.PROVIDER_ID };
        List<EquipmentsDetail> lstDetail =  detailDAO.getByImTransactionId(ta.getTransactionActionId());
        if(lstDetail!=null && !lstDetail.isEmpty()){
            for (EquipmentsDetail detail:lstDetail) {
//                if(detail.getSerial()==null || detail.getSerial().isEmpty()) continue;
//                StockGoods sg = stgDAO.getStockGoods(detail.getEquipmentsProfileId(), detail.getEquipmentStatus(), ta.getCreateShopId());
                String[] valuesOld = new String[]{""};
                lsValueOld.add(valuesOld);
                valuesOld = new String[]{"", "", ""};
                lsValueOldNoSerial.add(valuesOld);
            }
        }



        ta.setAssessmentStaffId(staffId);
        ta.setAssessmentDatetime(new Date());
        ta.setDescription(description);
        ta.setStatus(InventoryConstanst.TransactionStatus.APPROVE);
        taDAO.saveOrUpdate(ta);
        StockGoodsInvSerialDAO sgisDAO = new StockGoodsInvSerialDAO();
        StockGoodsInvNoSerialDAO sginsDAO = new StockGoodsInvNoSerialDAO();
        lsStoclDetail = stdDAO.getDetailsByTransaction(ta.getTransactionActionId());
        lsSerials = stsDAO.getStockSerials(ta.getTransactionActionId());
        stDAO.approveStock(ta.getTransactionActionId(),ta.getAssessmentStaffId());

        for (StockTransactionDetail tad : lsStoclDetail) {
            StockGoods sg = stgDAO.getStockGoods(tad.getGoodsId(), tad.getGoodsStatus(), ta.getCreateShopId());
            if (sg == null) {
                sg = new StockGoods(0L, 0L, ta.getCreateShopId(), tad.getGoodsId());
                sg.setGoodsStatus(tad.getGoodsStatus());
                sg.setAvailableQuantity(sg.getAvailableQuantity() + tad.getQuantity());
                sg.setQuantity(sg.getQuantity() + tad.getQuantity());
            } else {
                sg.setAvailableQuantity(sg.getAvailableQuantity() + tad.getQuantity());
                sg.setQuantity(sg.getQuantity() + tad.getQuantity());
            }
            EquipmentsProfile profile = profileDAO.findByProfileId(tad.getGoodsId());
            if (profile!=null && !InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                StockGoodsInvNoSerial sgins = sginsDAO.getStockGoodsNoserial(tad.getGoodsId(), tad.getGoodsStatus(),
                        ta.getCreateShopId(), InventoryConstanst.StockStatus.INSTOCK);
                if (sgins == null) {
                    sgins = new StockGoodsInvNoSerial(ta.getCreateShopId(), tad.getGoodsId(),
                            tad.getQuantity(), tad.getGoodsStatus(), InventoryConstanst.StockStatus.INSTOCK,
                            tad.getProviderId(), new Date(), ta.getTransactionActionId());
                } else {
                    sgins.setQuantity(sgins.getQuantity() + tad.getQuantity());
                    sgins.setCurrentTaId(ta.getTransactionActionId());
                }
                sginsDAO.saveOrUpdate(sgins);
            }

            stgDAO.saveOrUpdate(sg);
        }
//        if(handleType.equals(InventoryConstanst.ImportType.importWarranty)){
//            sgisDAO.updateByStockTransactionSerialOld(ta.getTransactionActionId());
//            sgisDAO.updateByStockTransactionSerialChange(ta.getTransactionActionId(), ta.getCreateShopId());
//            sgisDAO.InsertByStockTransactionSerialNew(ta.getTransactionActionId(), ta.getCreateShopId());
//        }else {
            sgisDAO.InsertByStockTransactionSerial(ta.getTransactionActionId(), ta.getCreateShopId());
//        }
        //insertHistory new
//        List<EquipmentsDetail> lstDetail =  detailDAO.getByImTransactionId(ta.getTransactionActionId());
        BusinessService businessService = new BusinessService();
        List<TransactionActionDetail> lsDt = transactionActionDetailDAO.getTransactionActionDetails(ta.getTransactionActionId());
        if(lsDt!=null && !lsDt.isEmpty()){
            for (TransactionActionDetail objDt:lsDt) {
                EquipmentsProfile profile = profileDAO.findByProfileId(objDt.getGoodsId());
                if (profile!=null && !InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                    EquipmentsDetail detail = detailDAO.findByProfile(objDt.getGoodsId()).get(0);
                    detail.setShopId(ta.getCreateShopId());
                    detail.setStaffId(ta.getCreateStaffId());
                    String[] valuesNew = {sessionBean.getShopNameById(ta.getCreateShopId()),objDt.getQuantity().toString(),sessionBean.getProviderName(objDt.getProviderId().toString())};
                            businessService.insertHistory(detail, lstColumnNoSerial, valuesNew,
                                    new String[]{"", "", ""}, InventoryConstanst.ACTION_TYPE.TYPE_6L);
                }
            }
        }

        if(lstDetail!=null && !lstDetail.isEmpty()){
            int i = 0;
            for (EquipmentsDetail detail:lstDetail) {
                detail.setShopId(ta.getCreateShopId());
                detail.setStaffId(ta.getCreateStaffId());

                if(detail.getSerial()==null || detail.getSerial().isEmpty()) {
//                    for (StockTransactionDetail tad : lsStoclDetail) {
//                        if(tad.getGoodsId().equals(detail.getEquipmentsProfileId())){
//                            String[] valuesNew = {sessionBean.getShopNameById(ta.getCreateShopId()),tad.getQuantity().toString(),sessionBean.getProviderName(detail.getProviderId().toString())};
//                            businessService.insertHistory(detail, lstColumnNoSerial, valuesNew,
//                                    new String[]{"", "", ""}, InventoryConstanst.ACTION_TYPE.TYPE_6L);
//                            break;
//                        }
//                    }
                    i++;
                }else {
//                StockGoods sg = stgDAO.getStockGoods(detail.getEquipmentsProfileId(), detail.getEquipmentStatus(), ta.getCreateShopId());
                    String[] valuesNew = {sessionBean.getShopNameById(ta.getCreateShopId())};
                    businessService.insertHistory(detail, lstColumn, valuesNew,
                            lsValueOld.get(i++), InventoryConstanst.ACTION_TYPE.TYPE_6L);
                }

            }
        }
        return true;
    }

    public Boolean approveWaranty() {
        StockTransactionDetailDAO stdDAO = new StockTransactionDetailDAO();
        StockTransactionSerialDAO stsDAO = new StockTransactionSerialDAO();
        StockTransactionDAO stDAO = new StockTransactionDAO();
        TransactionActionDAO taDAO = new TransactionActionDAO();
        EquipmentsProfileDAO profileDAO = new EquipmentsProfileDAO();
        EquipmentHistoryDetailDAO equipmentHistoryDetailDAO = new EquipmentHistoryDetailDAO();
        EquipmentDetailDAO detailDAO = new EquipmentDetailDAO();
        ta = taDAO.findById(ta.getTransactionActionId());
        if (ta == null || ta.getStatus().equals(InventoryConstanst.TransactionStatus.IM_WARRANTY) 
        		|| ta.getStatus().equals(InventoryConstanst.TransactionStatus.APPROVE)) {
            return false;
        }
        ta.setAssessmentStaffId(staffId);
        ta.setAssessmentDatetime(new Date());
        ta.setDescription(description);
        if(handleType.equals(InventoryConstanst.ImportType.importWarranty)){
        	ta.setStatus(InventoryConstanst.TransactionStatus.IM_WARRANTY);
        } else
        	ta.setStatus(InventoryConstanst.TransactionStatus.APPROVE);
        taDAO.saveOrUpdate(ta);
        StockGoodsInvSerialDAO sgisDAO = new StockGoodsInvSerialDAO();
        StockGoodsInvNoSerialDAO sginsDAO = new StockGoodsInvNoSerialDAO();
        lsStoclDetail = stdDAO.getDetailsByTransaction(ta.getTransactionActionId());
        lsSerials = stsDAO.getStockSerials(ta.getTransactionActionId());
        stDAO.approveStock(ta.getTransactionActionId(),ta.getAssessmentStaffId());
        StockGoodsDAO stgDAO = new StockGoodsDAO();

        SessionBean sessionBean = new SessionBean();
        List<String[]> lsValueOld = new ArrayList<>();
        String[] lstColumn = {InventoryConstanst.ACTION_AU_DETAIL.SHOP_ID};
        List<EquipmentsDetail> lstDetail =  detailDAO.getByImTransactionId(ta.getTransactionActionId());
        if(lstDetail!=null && !lstDetail.isEmpty()){
            for (EquipmentsDetail detail:lstDetail) {
//                if(detail.getSerial()==null || detail.getSerial().isEmpty()) {
//                    continue;
//                }
//                StockGoods sg = stgDAO.getStockGoods(detail.getEquipmentsProfileId(), detail.getEquipmentStatus(), ta.getCreateShopId());
                String[] valuesOld = new String[]{""};
                lsValueOld.add(valuesOld);
            }
        }

        for (StockTransactionDetail tad : lsStoclDetail) {
            StockGoods sg = stgDAO.getStockGoods(tad.getGoodsId(), tad.getGoodsStatus(), ta.getCreateShopId());
            if (sg == null) {
                sg = new StockGoods(0L, 0L, ta.getCreateShopId(), tad.getGoodsId());
                sg.setGoodsStatus(tad.getGoodsStatus());
                sg.setAvailableQuantity(sg.getAvailableQuantity() + tad.getQuantity());
                sg.setQuantity(sg.getQuantity() + tad.getQuantity());
            } else {
                sg.setAvailableQuantity(sg.getAvailableQuantity() + tad.getQuantity());
                sg.setQuantity(sg.getQuantity() + tad.getQuantity());
            }
            EquipmentsProfile profile = profileDAO.findByProfileId(tad.getGoodsId());
            if (profile!=null && !InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                StockGoodsInvNoSerial sgins = sginsDAO.getStockGoodsNoserial(tad.getGoodsId(), tad.getGoodsStatus(),
                        ta.getCreateShopId(), InventoryConstanst.StockStatus.INSTOCK);
                if (sgins == null) {
                    sgins = new StockGoodsInvNoSerial(ta.getCreateShopId(), tad.getGoodsId(),
                            tad.getQuantity(), tad.getGoodsStatus(), InventoryConstanst.StockStatus.INSTOCK,
                            detailDAO.findByProfile(tad.getGoodsId()).get(0).getProviderId(), new Date(), ta.getTransactionActionId());
                } else {
                    sgins.setQuantity(sgins.getQuantity() + tad.getQuantity());
                    sgins.setCurrentTaId(ta.getTransactionActionId());
                }
                sginsDAO.saveOrUpdate(sgins);
            }

            stgDAO.saveOrUpdate(sg);
        }
        if(handleType.equals(InventoryConstanst.ImportType.importWarranty)){
            sgisDAO.updateByStockTransactionSerialOld(ta.getTransactionActionId());
            sgisDAO.updateByStockTransactionSerialChange(ta.getTransactionActionId(), ta.getCreateShopId());
            sgisDAO.InsertByStockTransactionSerialNew(ta.getTransactionActionId(), ta.getCreateShopId());
        }else {
            sgisDAO.InsertByStockTransactionSerial(ta.getTransactionActionId(), ta.getCreateShopId());
        }
        BusinessService businessService = new BusinessService();
        if(lstDetail!=null && !lstDetail.isEmpty()){
            int i = 0;
            for (EquipmentsDetail detail:lstDetail) {
                if(detail.getSerial()==null || detail.getSerial().isEmpty()) {
                    i++;
                    continue;
                }
                if(InventoryConstanst.GoodsStatus.NOMAL.equals(detail.getStatus())
                        || InventoryConstanst.GoodsStatus.ERROR.equals(detail.getStatus())){
                    i++;
                    detail.setShopId(ta.getToShopId());
                    detail.setStaffId(ta.getCreateStaffId());
                    String serialOld =  equipmentHistoryDetailDAO.getSerialOldBySerialNew(detail.getSerial(), InventoryConstanst.ACTION_AU_DETAIL.SERIAL);
                    String[] lstColumnNew = {InventoryConstanst.ACTION_AU_DETAIL.SHOP_ID, InventoryConstanst.ACTION_AU_DETAIL.SERIAL_APPRO};
                    String[] valuesOld = new String[]{"", serialOld ==null?"":serialOld};
                    String[] valuesNew = {sessionBean.getShopNameById(ta.getCreateShopId()), detail.getSerial()};
                    if(InventoryConstanst.GoodsStatus.ERROR.equals(detail.getStatus())){
                        EquipmentHistoryDetail hisOld =  equipmentHistoryDetailDAO.getDetailBySerialOld(detail.getSerial(),
                                InventoryConstanst.ACTION_AU_DETAIL.SERIAL);
                        if(hisOld!=null){
                            valuesOld = new String[]{"",hisOld.getOldValue()};
                            valuesNew = new String[]{sessionBean.getShopNameById(ta.getCreateShopId()),hisOld.getNewValue()};
                        }
                    }

                    businessService.insertHistory(detail, lstColumnNew, valuesNew,
                            valuesOld, InventoryConstanst.ACTION_TYPE.TYPE_13L);
                }else {
                    detail.setShopId(ta.getToShopId());
                    detail.setStaffId(ta.getCreateStaffId());
                    String[] valuesNew = {sessionBean.getShopNameById(ta.getCreateShopId())};
                    businessService.insertHistory(detail, lstColumn, valuesNew,
                            lsValueOld.get(i++), InventoryConstanst.ACTION_TYPE.TYPE_13L);
                }
            }
            for (EquipmentsDetail detail:lstDetail) {
                if(InventoryConstanst.GoodsStatus.NOMAL.equals(detail.getStatus())
                        || InventoryConstanst.GoodsStatus.ERROR.equals(detail.getStatus())){
                    equipmentHistoryDetailDAO.deleteHisDetailNewSerial(detail.getSerial(), InventoryConstanst.ACTION_AU_DETAIL.SERIAL);
                }
            }
        }
        return true;
    }

    public void deleteDataWarranty(){
        EquipmentDetailDAO detailDAO = new EquipmentDetailDAO();
        EquipmentHistoryDetailDAO equipmentHistoryDetailDAO = new EquipmentHistoryDetailDAO();
        //voi truong hop khong thay the serial
        List<EquipmentsDetail> lst =  detailDAO.getByImTransactionId(ta.getTransactionActionId());
        if(lst!=null){
            List<Long> lstId = new ArrayList<>();
            for (EquipmentsDetail obj:lst) {
                if(InventoryConstanst.GoodsStatus.USED.equals(obj.getStatus())){
                    lstId.add(obj.getId());
                }else if(InventoryConstanst.GoodsStatus.NOMAL.equals(obj.getStatus())){
                    equipmentHistoryDetailDAO.deleteHisDetailNewSerial(obj.getSerial(), InventoryConstanst.ACTION_AU_DETAIL.SERIAL);
                    detailDAO.delete(obj);
                }
            }
            if(!lstId.isEmpty()){
                detailDAO.updateStatus(lstId, InventoryConstanst.GoodsStatus.ERROR);
            }
        }
    }

    public Boolean rejectWaranty() {
        deleteDataWarranty();
        TransactionActionDAO taDAO = new TransactionActionDAO();
        ta = taDAO.findById(ta.getTransactionActionId());
        if (ta == null || ta.getStatus().equals(InventoryConstanst.TransactionStatus.IM_WARRANTY) 
        		|| ta.getStatus().equals(InventoryConstanst.TransactionStatus.APPROVE)) {
            return false;
        }
        ta.setAssessmentStaffId(staffId);
        ta.setAssessmentDatetime(new Date());
        ta.setDescription(description);
        if(handleType.equals(InventoryConstanst.ImportType.importWarranty)){
        	ta.setStatus(InventoryConstanst.TransactionStatus.CANCEL_WARRANTY);
        }
        else
        	ta.setStatus(InventoryConstanst.TransactionStatus.CANCEL);
        taDAO.saveOrUpdate(ta);
        StockTransactionDAO stDAO = new StockTransactionDAO();
        stDAO.rejectStock(ta.getTransactionActionId(),ta.getAssessmentStaffId());
//        EquipmentDetailDAO edDAO = new EquipmentDetailDAO();
//        edDAO.deleteByTaId(ta.getTransactionActionId());
//        deleteStock();
//        deleteTransaction();
        return true;
    }

}
