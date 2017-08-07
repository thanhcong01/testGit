/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.java.business;

import com.ftu.constanst.InventoryConstanst;
import com.ftu.inventory.bo.*;
import com.ftu.inventory.dao.*;
import com.ftu.inventory.dto.StockGoodsInvSerialDTO;
import com.ftu.staff.bo.Shop;
import com.ftu.staff.dao.ShopDAO;
import com.ftu.ws.BusinessService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author E5420
 */
public class ExportGoods {

    String orderCode;
    StockTransaction st;
    TransactionAction ta;
    Date d;
    String staffName;

    public ExportGoods(StockTransaction st, String type, String orderCode, Long reasonId, String staffName) {
        this.orderCode = orderCode;
        this.staffName = staffName;
        this.st = st;
        d = new Date();
        ta = new TransactionAction(orderCode, type, st.getExportStaffId(), st.getFromShopId(), InventoryConstanst.TransactionStatus.EX_WARRANTY, st.getFromShopId(), null);
        ta.setReasonId(reasonId);
        //st.setStockTransactionType(ta.getTransactionType().equals(InventoryConstanst.TransactionType.BACK) ? InventoryConstanst.StockTransactionType.BACK : InventoryConstanst.StockTransactionType.DEL);
        getData();
    }

    private void getData() {
        //     session.save(ta);
        HashMap<String, TransactionActionDetail> mapDetail = new HashMap<>();

        StockGoodsInvSerialDAO sgisDAO = new StockGoodsInvSerialDAO();
        for (StockTransactionDetail detail : st.getLsDetail()) {
            for (StockGoodsInvSerialDTO s : detail.getSetSerial()) {
                String note = s.getReasonsWarranty();
                String serial = s.getSerial();
                s = sgisDAO.convertEmtitytoDTO(sgisDAO.findById(s.getId()));
                TransactionActionDetail tad = mapDetail.get(s.getProviderId().toString()+"-"+s.getEquipmentProfileId() + "-"+serial);
                StockTransactionSerial sts = new StockTransactionSerial(s.getSerial(), s.getSerialSearch(), s.getEquipmentProfileStatus(), s.getEquipmentProfileId(), s.getProviderId());
                if (tad == null) {
                    tad = new TransactionActionDetail(s.getEquipmentProfileId(), s.getProviderId());
                    tad.setDescription(note);
                    mapDetail.put(s.getProviderId().toString()+"-"+s.getEquipmentProfileId() + "-"+serial, tad);
                }
                tad.setQuantity(tad.getQuantity() + 1);
                detail.getLsSerial().add(sts);
            }
        }
        ta.getLsDetail().addAll(mapDetail.values());
    }

    private void saveTransaction() {
        TransactionActionDAO taDAO = new TransactionActionDAO();
        ta.setCreateDatetime(d);
        //comment xuat bh, sua chua
//        ta.setAssessmentDatetime(d);
//        ta.setAssessmentStaffId(ta.getCreateStaffId());

        taDAO.saveOrUpdate(ta);
        TransactionActionDetailDAO tadDAO = new TransactionActionDetailDAO();
        for (TransactionActionDetail tad : ta.getLsDetail()) {
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
        ShopDAO shopDAO = new ShopDAO();
        BusinessService businessService = new BusinessService();
        st.setTransactionActionId(ta.getTransactionActionId());
        st.setStockTransactionDatetime(d);
        st.setFinishDatetime(d);
        stDAO.saveOrUpdate(st);
        String[] lstColumn = {InventoryConstanst.ACTION_AU_DETAIL.SHOP_ID};//,
                //InventoryConstanst.ACTION_AU_DETAIL.STAFF_ID};
        for (StockTransactionDetail std : st.getLsDetail()) {
            //Add Stock Detail, Stock Serial
            std.setStockTransactionId(st.getStockTransactionId());
            std.setStdDatetime(d);
            stdDAO.saveOrUpdate(std);
            EquipmentsProfile profile = equipmentsProfileDAO.findByProfileId(std.getGoodsId());
            if(InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                for (StockGoodsInvSerialDTO sgis : std.getSetSerial()) {

                    EquipmentsDetail detail = detailDAO.findBySerial(sgis.getSerial());
                    if(detail!=null){
                        detail.setStaffId(ta.getCreateStaffId());
                        detail.setShopId(ta.getCreateShopId());
                        com.ftu.staff.bo.Staff staff = null;
                        Shop shop = null;
                        if(ta.getFromShopId()!=null){
                            shop = shopDAO.findById(ta.getCreateShopId());
                        }
                        String[] valuesOld = new String[]{shop==null?"":shop.getShopName()};//,sgis.getStaffName()  };

                        if(ta.getCreateShopId()!=null){
                            shop = shopDAO.findById(ta.getCreateShopId());
                        }
                        String[] valuesNew = new String[]{""};//, staffName};
                        businessService.insertHistory(detail, lstColumn, valuesNew,
                                valuesOld, InventoryConstanst.ACTION_TYPE.TYPE_12L);
                    }


                    sgis.setStockStatus(InventoryConstanst.StockStatus.EX_WARANTIED);
                    sgis.setCurrentTaId(ta.getTransactionActionId());
                    sgis.setStaffName(staffName);
                    sgisDAO.updateStockTransBySql(sgisDAO.convertDTOtoEntity(sgis));
                    //todo:fixed hang da su dung
                    if(InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                        StockGoods sg = sgDAO.getStockGoods(std.getGoodsId(), sgis.getEquipmentProfileStatus(), ta.getFromShopId());
                        if(sg!=null){
                            sg.setAvailableQuantity(sg.getAvailableQuantity() - 1L);
                            sg.setQuantity(sg.getQuantity() - 1L);
                            sgDAO.saveOrUpdate(sg);
                        }
                    }
                    StockTransactionSerial stss = new StockTransactionSerial();
                    stss.setImTransactionId( st.getTransactionActionId());
                    stss.setStockTransactionDetailId(std.getStockTransactionDetailId());
                    stss.setEquipmentProfileId(std.getGoodsId());
                    stss.setSerial(sgis.getSerial());
                    stss.setEquipmentProfileStatus(sgis.getEquipmentProfileStatus());
                    stss.setProviderId(sgis.getProviderId());
                    stss.setSerialSearch(sgis.getSerialSearch());
                    stss.setReason(sgis.getReasonsWarranty());
                    stss.setStsDatetime(new Date());
                    stsDAO.saveOrUpdate(stss);
                    //todo:end
                }
//                stsDAO.InsertByStockEquipmentsInvSerial(std.getStockTransactionDetailId(), st.getTransactionActionId(), std.getGoodsId(), std.getGoodsStatus());

            }else {
                sgDAO.updateQuantity(true, std.getQuantity(), std.getGoodsId(), std.getGoodsStatus(), st.getFromShopId());
                StockGoodsInvNoSerial sgins = sginsDAO.getStockGoodsNoserial(std.getGoodsId(), InventoryConstanst.GoodsStatus.NOMAL,
                        ta.getFromShopId(), InventoryConstanst.StockStatus.INSTOCK);
                sgins.setQuantity(sgins.getQuantity() - std.getQuantity());
                sgins.setCurrentTaId(ta.getTransactionActionId());
                
                sginsDAO.saveOrUpdate(sgins);

                Long providerId = detailDAO.findByProfile(std.getGoodsId()).get(0).getProviderId();
                StockGoodsInvNoSerial sginsAdd = new StockGoodsInvNoSerial(ta.getFromShopId(), std.getGoodsId(),
                        std.getQuantity(), std.getGoodsStatus(), InventoryConstanst.StockStatus.EX_WARANTIED,
                        providerId, new Date(), ta.getTransactionActionId());
                sginsAdd.setStaffName(staffName);
                sginsDAO.saveOrUpdate(sginsAdd);

                StockTransactionSerial stSerial = new StockTransactionSerial();
                stSerial.setImTransactionId(ta.getTransactionActionId());
                stSerial.setStockTransactionDetailId(std.getStockTransactionDetailId());
                stSerial.setEquipmentProfileId(std.getGoodsId());
                stSerial.setEquipmentProfileStatus(std.getGoodsStatus());
                stSerial.setProviderId(providerId);
                stSerial.setStsDatetime(new Date());
                stsDAO.saveOrUpdate(stSerial);

//                List<EquipmentsDetail> details = detailDAO.findByProfile(std.getGoodsId());
//                if(details!=null && !details.isEmpty()){
//                    EquipmentsDetail detail = details.get(0);
//                    if(detail!=null){
//                        detail.setStaffId(ta.getCreateStaffId());
//                        detail.setShopId(ta.getCreateShopId());
//                        Shop shop = null;
//                        if(ta.getFromShopId()!=null){
//                            shop = shopDAO.findById(ta.getFromShopId());
//                        }
//                        String[] valuesOld = new String[]{shop==null?"":shop.getShopName(),staffName  };
//
//                        if(ta.getCreateShopId()!=null){
//                            shop = shopDAO.findById(ta.getCreateShopId());
//                        }
//                        String[] valuesNew = new String[]{shop==null?"":shop.getShopName(), staffName};
//                        businessService.insertHistory(detail, lstColumn, valuesNew,
//                                valuesOld, InventoryConstanst.ACTION_TYPE.TYPE_12L);
//                    }
//                }
            }

        }
    }

    public Boolean export() {
        saveTransaction();
        saveStock();
        return true;
    }
}
