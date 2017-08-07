/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.java.business;

import com.ftu.constanst.InventoryConstanst;
import com.ftu.inventory.bean.SessionBean;
import com.ftu.inventory.bo.*;
import com.ftu.inventory.dao.*;
import com.ftu.inventory.dto.StockGoodsInvSerialDTO;
import com.ftu.staff.bo.*;
import com.ftu.staff.dao.ShopDAO;
import com.ftu.staff.dao.StaffDAO;
import com.ftu.ws.BusinessService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author E5420
 */
public class ExEGoods {

    String orderCode;
    StockTransaction st;
    TransactionAction ta;
    Date d;
    String note;
    String referenCode;

    public ExEGoods(StockTransaction st, String orderCode, Long reasonId, String referenCode, String note) {
        this.orderCode = orderCode;
        this.st = st;
        d = new Date();
        ta = new TransactionAction(orderCode, InventoryConstanst.TransactionType.EX_STAFF, st.getExportStaffId(), st.getFromShopId(), InventoryConstanst.TransactionStatus.EX_STAFF, st.getFromShopId(), null);
        ta.setReasonId(reasonId);
        ta.setDescription(note);
        //st.setStockTransactionType(ta.getTransactionType().equals(InventoryConstanst.TransactionType.BACK) ? InventoryConstanst.StockTransactionType.BACK : InventoryConstanst.StockTransactionType.DEL);
        this.referenCode =referenCode;
        this.note = note;
        getData();
    }

    private void getData() {
        //     session.save(ta);
        HashMap<String, TransactionActionDetail> mapDetail = new HashMap<>();

        StockGoodsInvSerialDAO sgisDAO = new StockGoodsInvSerialDAO();
        for (StockTransactionDetail detail : st.getLsDetail()) {
            if(detail.getSetSerial() != null && !detail.getSetSerial().isEmpty()){
                for (StockGoodsInvSerialDTO s : detail.getSetSerial()) {
                    s = sgisDAO.convertEmtitytoDTO(sgisDAO.findById(s.getId()));
                    TransactionActionDetail tad = mapDetail.get(s.getProviderId().toString()+"-"+s.getEquipmentProfileId());
                    StockTransactionSerial sts = new StockTransactionSerial(s.getSerial(), s.getSerialSearch(), s.getEquipmentProfileStatus(), s.getEquipmentProfileId(), s.getProviderId());
                    if (tad == null) {
                        tad = new TransactionActionDetail(s.getEquipmentProfileId(), s.getProviderId());
                        mapDetail.put(s.getProviderId().toString()+"-"+s.getEquipmentProfileId(), tad);
                    }
                    tad.setQuantity(tad.getQuantity() + 1);
                    tad.setReferenceCode(referenCode);
                    tad.setDescription(note);
                    detail.getLsSerial().add(sts);
                }
                ta.getLsDetail().addAll(mapDetail.values());
            }else {
                TransactionActionDetail tad = new TransactionActionDetail(detail.getGoodsId(), detail.getProviderId());
                tad.setQuantity(detail.getQuantity());
                ta.getLsDetail().add(tad);

            }

        }

    }

    private void saveTransaction() {
        TransactionActionDAO taDAO = new TransactionActionDAO();
        ta.setCreateDatetime(d);
//        ta.setAssessmentDatetime(d);
//        ta.setAssessmentStaffId(ta.getCreateStaffId());
        taDAO.saveOrUpdate(ta);
        TransactionActionDetailDAO tadDAO = new TransactionActionDetailDAO();
        for (TransactionActionDetail tad : ta.getLsDetail()) {
//            tad.setReferenceCode(ta.getReferenceCode());
            tad.setTransactionActionId(ta.getTransactionActionId());
            tad.setCreateDatetime(d);
            tadDAO.saveOrUpdate(tad);
        }
    }

    private void saveStock() {
        StockTransactionDAO stDAO = new StockTransactionDAO();
        StockTransactionDetailDAO stdDAO = new StockTransactionDetailDAO();
        StockTransactionSerialDAO stsDAO = new StockTransactionSerialDAO();
        StockGoodsDAO sgDAO = new StockGoodsDAO();
        StockGoodsInvSerialDAO sgisDAO = new StockGoodsInvSerialDAO();
        EquipmentsProfileDAO equipmentsProfileDAO = new EquipmentsProfileDAO();
        StockGoodsInvNoSerialDAO sginsDAO = new StockGoodsInvNoSerialDAO();
        EquipmentDetailDAO detailDAO = new EquipmentDetailDAO();
        String[] lstColumn = {InventoryConstanst.ACTION_AU_DETAIL.SHOP_ID,
                InventoryConstanst.ACTION_AU_DETAIL.STAFF_ID};
        String[] lstColumnNosrial = {InventoryConstanst.ACTION_AU_DETAIL.SHOP_ID, InventoryConstanst.ACTION_AU_DETAIL.STAFF_ID, InventoryConstanst.ACTION_AU_DETAIL.QUANTITY_KTV};
        BusinessService businessService = new BusinessService();

        st.setTransactionActionId(ta.getTransactionActionId());
        st.setStockTransactionDatetime(d);
        st.setFinishDatetime(d);
        stDAO.saveOrUpdate(st);
        StaffDAO staffDAO = new StaffDAO();
        ShopDAO shopDAO  = new ShopDAO();
        for (StockTransactionDetail std : st.getLsDetail()) {
            //Add Stock Detail, Stock Serial
            std.setStockTransactionId(st.getStockTransactionId());
            std.setStdDatetime(d);
            stdDAO.saveOrUpdate(std);
//            sgDAO.updateQuantity(true, std.getQuantity(), std.getGoodsId(), std.getGoodsStatus(), st.getFromShopId());
            EquipmentsProfile profile = equipmentsProfileDAO.findByProfileId(std.getGoodsId());
            if(InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                for (StockGoodsInvSerialDTO sgis : std.getSetSerial()) {
                    //Insert history
                    EquipmentsDetail detail = detailDAO.findBySerial(sgis.getSerial());
                    if(detail!=null){
                        detail.setStaffId(ta.getCreateStaffId());
                        detail.setShopId(ta.getCreateShopId());
                        com.ftu.staff.bo.Staff staff = null;
                        Shop shop = null;
                        if(sgis.getStaffId()!=null){
                            staff = staffDAO.findById(sgis.getStaffId());
                        }
                        if(ta.getFromShopId()!=null){
                             shop = shopDAO.findById(ta.getFromShopId());
                        }
                        String[] valuesOld = new String[]{shop==null?"":shop.getShopName(),staff==null? "":staff.getStaffName()  };
                        if(st.getImportStaffId()!=null){
                            staff = staffDAO.findById(st.getImportStaffId());
                        }
                        if(ta.getCreateShopId()!=null){
                            shop = shopDAO.findById(ta.getCreateShopId());
                        }
                        String[] valuesNew = new String[]{shop==null?"":shop.getShopName(), staff == null? "":staff.getStaffName()};
                        businessService.insertHistory(detail, lstColumn, valuesNew,
                                valuesOld, InventoryConstanst.ACTION_TYPE.TYPE_10L);
                    }


                    sgis.setStockStatus(InventoryConstanst.StockStatus.EX_USED);
                    sgis.setCurrentTaId(ta.getTransactionActionId());
                    sgis.setStaffId(st.getImportStaffId());
                    sgisDAO.updateStockTransBySqlExKTV(sgisDAO.convertDTOtoEntity(sgis));
                    //todo:fixed hang da su dung
                    if(InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                        StockGoods sg = sgDAO.getStockGoods(std.getGoodsId(), sgis.getEquipmentProfileStatus(), ta.getFromShopId());
                        if(sg!=null){
                            sg.setAvailableQuantity(sg.getAvailableQuantity() - 1L);
                            sg.setQuantity(sg.getQuantity() - 1L);
                            sgDAO.saveOrUpdate(sg);
                        }
                    }
                    //todo:end

                }
                stsDAO.InsertByStockEquipmentsInvSerial(std.getStockTransactionDetailId(), st.getTransactionActionId(), std.getGoodsId(), std.getGoodsStatus());
            }else {
                sgDAO.updateQuantityKTV(true, std.getQuantity(), std.getGoodsId(), std.getGoodsStatus(), st.getFromShopId());
                StockGoodsInvNoSerial sgins = sginsDAO.getStockGoodsNoserial(std.getGoodsId(), InventoryConstanst.GoodsStatus.NOMAL,
                        ta.getFromShopId(), InventoryConstanst.StockStatus.INSTOCK);
                sgins.setQuantity(sgins.getQuantity() - std.getQuantity());
                sgins.setCurrentTaId(ta.getTransactionActionId());
                sginsDAO.saveOrUpdate(sgins);
                Long providerId = null;
                List<EquipmentsDetail> lsDetail = detailDAO.findByProfile(std.getGoodsId());
                if(lsDetail!=null && !lsDetail.isEmpty()){
                    providerId = lsDetail.get(0).getProviderId();
                }
//                StockGoodsInvNoSerial sginsAdd = new StockGoodsInvNoSerial(ta.getFromShopId(), std.getGoodsId(),
//                        std.getQuantity(), std.getGoodsStatus(), InventoryConstanst.StockStatus.EX_USED,
//                        providerId, new Date(), ta.getTransactionActionId());
//                sginsAdd.setStaffId(st.getImportStaffId());
//                sginsDAO.saveOrUpdate(sginsAdd);


                StockGoodsInvNoSerial sginsAdd =sginsDAO.getStockGoodsNoserial(std.getGoodsId(), std.getGoodsStatus(),
                        ta.getFromShopId(), InventoryConstanst.StockStatus.EX_USED);
                if(sginsAdd == null){
                     sginsAdd = new StockGoodsInvNoSerial(ta.getFromShopId(), std.getGoodsId(),
                            std.getQuantity(), std.getGoodsStatus(), InventoryConstanst.StockStatus.EX_USED,
                            providerId, new Date(), ta.getTransactionActionId());
                    sginsAdd.setStaffId(st.getImportStaffId());
                }else {
                    sginsAdd.setStaffId(st.getImportStaffId());
                    sginsAdd.setQuantity(sginsAdd.getQuantity() + std.getQuantity());
                    sginsAdd.setCurrentTaId(ta.getTransactionActionId());
                }
                sginsDAO.saveOrUpdate(sginsAdd);

                StockTransactionSerial stSerial = new StockTransactionSerial();
                stSerial.setImTransactionId(ta.getTransactionActionId());
                stSerial.setStockTransactionDetailId(std.getStockTransactionDetailId());
                stSerial.setEquipmentProfileId(std.getGoodsId());
                stSerial.setEquipmentProfileStatus(std.getGoodsStatus());
                stSerial.setProviderId(providerId);
                stSerial.setStsDatetime(new Date());
                stsDAO.saveOrUpdate(stSerial);
//Insert history
                List<EquipmentsDetail> details = detailDAO.findByProfile(std.getGoodsId());
                if(details!=null && !details.isEmpty()){
                    EquipmentsDetail detail = details.get(0);
                    if(detail!=null){
                        detail.setStaffId(ta.getCreateStaffId());
                        detail.setShopId(ta.getCreateShopId());
                        com.ftu.staff.bo.Staff staff = null;
                        Shop shop = null;
//                        if(sgins.getStaffId()!=null){
//                            staff = staffDAO.findById(sgins.getStaffId());
//                        }
                        if(ta.getFromShopId()!=null){
                            shop = shopDAO.findById(ta.getFromShopId());
                        }
                        String[] valuesOld = new String[]{shop==null?"":shop.getShopName(),"", ""  };
                        if(st.getImportStaffId()!=null){
                            staff = staffDAO.findById(st.getImportStaffId());
                        }
//                        if(ta.getCreateShopId()!=null){
//                            shop = shopDAO.findById(ta.getCreateShopId());
//                        }
                        String[] valuesNew = new String[]{"", staff == null? "":staff.getStaffName(), std.getQuantity().toString() };
                        businessService.insertHistory(detail, lstColumnNosrial, valuesNew,
                                valuesOld, InventoryConstanst.ACTION_TYPE.TYPE_10L);
                    }
                }
            }
        }
    }

    public Boolean exportKTV() {
        saveTransaction();
        saveStock();
        return true;
    }
    public Boolean export() {
        saveTransaction();
        saveStock();
        return true;
    }
}
