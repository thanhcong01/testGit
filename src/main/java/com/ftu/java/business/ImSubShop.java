/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.java.business;

import com.ftu.constanst.InventoryConstanst;
import com.ftu.exception.AppException;
import com.ftu.inventory.bo.*;
import com.ftu.inventory.bo.StockGoodsInvSerial;
import com.ftu.inventory.bussiness.EquipmentsDetailService;
import com.ftu.inventory.dao.*;
import com.ftu.inventory.dto.StockGoodsInvSerialDTO;
import com.ftu.staff.bo.Shop;
import com.ftu.staff.dao.ShopDAO;
import com.ftu.ws.BusinessService;

import java.util.Date;
import java.util.List;

/**
 *
 * @author E5420
 */
public class ImSubShop {

    TransactionAction ta;
    Long staffId;
    List<TransactionActionDetail> lsDetail;
    String description;
    Long actionType;
    String tockStatusBlock = InventoryConstanst.StockStatus.BLOCK_TD;
    public ImSubShop(TransactionAction ta, Long staffId, Long actionType, String tockStatus) {

        this.ta = ta;
        description=ta.getDescription() == null ? "" : ta.getDescription();
        if (!ta.getLsDetail().isEmpty()) {
            lsDetail = ta.getLsDetail();
        } else {
            TransactionActionDetailDAO tadDAO = new TransactionActionDetailDAO();
            lsDetail = tadDAO.getTransactionActionDetails(ta.getTransactionActionId());
        }
        this.staffId = staffId;
        this.actionType = actionType;
        this.tockStatusBlock = tockStatus;
    }

    public boolean imRequire() {
        TransactionActionDAO taDAO = new TransactionActionDAO();
        TransactionActionDetailDAO tadDAO = new TransactionActionDetailDAO();
        Date d = new Date();
        //
//        ta.setFromShopId(ta.getFromShopId()==null?ta.getCreateShopId():ta.getFromShopId());
//        ta.setToShopId(ta.getToShopId()==null?ta.getCreateShopId():ta.getToShopId());
        ta.setCreateDatetime(d);
        //ta.setEquipmentStatus(InventoryConstanst.TransactionStatus.CREATE);
        //
        taDAO.saveOrUpdate(ta);
        for (TransactionActionDetail tad : lsDetail) {
            tad.setTransactionActionId(ta.getTransactionActionId());
            tad.setCreateDatetime(d);
            tadDAO.saveOrUpdate(tad);
        }
        return true;
    }

    public boolean imReject() {
        TransactionActionDAO taDAO = new TransactionActionDAO();
        if (!ta.getStatus().equals(InventoryConstanst.TransactionStatus.CREATE) && !ta.getStatus().equals(InventoryConstanst.TransactionStatus.CREATE_R)) {
            return false;
        }
        ta.setAssessmentDatetime(new Date());
        ta.setAssessmentStaffId(staffId);
        ta.setDescription(description);
        if(ta.getStatus().equals(InventoryConstanst.TransactionStatus.CREATE)) {
            ta.setStatus(InventoryConstanst.TransactionStatus.CANCEL);
        }else{
            ta.setStatus(InventoryConstanst.TransactionStatus.CANCEL_R);
        }
        taDAO.saveOrUpdate(ta);

        return true;
    }

    public boolean imApproveApprove() {
        TransactionActionDAO taDAO = new TransactionActionDAO();
        ta = taDAO.findById(ta.getTransactionActionId());
        if (!ta.getStatus().equals(InventoryConstanst.TransactionStatus.CREATE) && !ta.getStatus().equals(InventoryConstanst.TransactionStatus.CREATE_R)) {
            return false;
        }
        ta.setAssessmentDatetime(new Date());
        ta.setDescription(description);
        ta.setAssessmentStaffId(staffId);
        ta.setDescription(description);
        if(ta.getStatus().equals(InventoryConstanst.TransactionStatus.CREATE)) {
            ta.setStatus(InventoryConstanst.TransactionStatus.APPROVE);
        }else{
            ta.setStatus(InventoryConstanst.TransactionStatus.APPROVE_R);
        }
        taDAO.saveOrUpdate(ta);
        return true;
    }

    public boolean imExApprove() throws AppException {
        TransactionActionDAO taDAO = new TransactionActionDAO();
        Date d = new Date();
        ta = taDAO.findById(ta.getTransactionActionId());
        if (!ta.getStatus().equals(InventoryConstanst.TransactionStatus.APPROVE) 
        		&& !ta.getStatus().equals(InventoryConstanst.TransactionStatus.APPROVE_R)) {
            return false;
        }
//        if (true) {
//        System.out.println("aaaaalsksfsdkfsdklf");
//        return false;
//        }
        //   StockTransactionDetailDAO stdDAO = new StockTransactionDetailDAO();
        StockGoodsDAO sgDAO = new StockGoodsDAO();
        StockGoodsInvSerialDAO sgiDAO = new StockGoodsInvSerialDAO();
        StockTransactionSerialDAO stsDAO = new StockTransactionSerialDAO();
        StockTransactionDetailDAO stdDAO = new StockTransactionDetailDAO();
        EquipmentsProfileDAO equipmentsProfileDAO = new EquipmentsProfileDAO();
        EquipmentDetailDAO detailDAO = new EquipmentDetailDAO();
        StockGoodsInvNoSerialDAO sginsDAO = new StockGoodsInvNoSerialDAO();
        if(ta.getStatus().equals(InventoryConstanst.TransactionStatus.APPROVE)) {
            ta.setStatus(InventoryConstanst.TransactionStatus.EX);
        }else {
            ta.setStatus(InventoryConstanst.TransactionStatus.EX_R);
        }
        ta.setDescription(description);
        taDAO.saveOrUpdate(ta);
        StockTransactionDAO stDAO = new StockTransactionDAO();
        StockTransaction st = new StockTransaction(staffId, ta.getFromShopId(), null, ta.getToShopId(), InventoryConstanst.StockTransactionType.TRANS, InventoryConstanst.StockTransactionStatus.WAIT, InventoryConstanst.StockTransactionDeliveryType.TRANS);
        st.setTransactionActionId(ta.getTransactionActionId());
        st.setStockTransactionDatetime(d);
        stDAO.saveOrUpdate(st);
        for (TransactionActionDetail tad : lsDetail) {
            //todo: trang thai o day dung 1
            EquipmentsProfile profile = equipmentsProfileDAO.findByProfileId(tad.getGoodsId());
            for (StockGoodsInvSerialDTO sgi : tad.getSetSerial()) {
                sgi.setStockStatus(tockStatusBlock);
                sgi.setCurrentTaId(ta.getTransactionActionId());
                sgiDAO.updateStockTransBySql(sgiDAO.convertDTOtoEntity(sgi));
                //todo:fixed hang da su dung
                if(InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                    StockGoods sg = sgDAO.getStockGoods(tad.getGoodsId(), sgi.getEquipmentProfileStatus(), ta.getFromShopId());
                    if(sg!=null){
                        sg.setAvailableQuantity(sg.getAvailableQuantity() - 1L);
                        sgDAO.saveOrUpdate(sg);
                    }
                }
                //todo:end
            }
            StockTransactionDetail std = new StockTransactionDetail(tad.getQuantity(), tad.getGoodsId(), InventoryConstanst.GoodsStatus.NOMAL);
            std.setStockTransactionId(st.getStockTransactionId());
            std.setStdDatetime(d);
            stdDAO.saveOrUpdate(std);

//            StockGoods sg = sgDAO.getStockGoods(tad.getGoodsId(), InventoryConstanst.GoodsStatus.NOMAL, ta.getFromShopId());
            if(InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                //todo: co serial, ----
                stsDAO.InsertByStockEquipmentsInvSerial(std.getStockTransactionDetailId(), ta.getTransactionActionId(), std.getGoodsId(), std.getGoodsStatus());
                //todo: end
                //todo:fixed hang da su dung
//                sg.setAvailableQuantity(sg.getAvailableQuantity() - tad.getSetSerial().size());
//                sgDAO.saveOrUpdate(sg);
                //todo:end
            }else {
                //todo: ko serial
                StockGoods sg = sgDAO.getStockGoods(tad.getGoodsId(), InventoryConstanst.GoodsStatus.NOMAL, ta.getFromShopId());
                StockGoodsInvNoSerial sgins = sginsDAO.getStockGoodsNoserial(tad.getGoodsId(), InventoryConstanst.GoodsStatus.NOMAL,
                        ta.getFromShopId(), InventoryConstanst.StockStatus.INSTOCK);
                sgins.setQuantity(sgins.getQuantity() - tad.getQuantity());
                sgins.setCurrentTaId(ta.getTransactionActionId());
                sginsDAO.saveOrUpdate(sgins);
                List<EquipmentsDetail> lsDetail = detailDAO.findByProfile(tad.getGoodsId());
                Long providerId = null;
                if(lsDetail !=null && !lsDetail.isEmpty()){
                    providerId = lsDetail.get(0).getProviderId();
                }
                StockGoodsInvNoSerial sginsAdd =sginsDAO.getStockGoodsNoserial(tad.getGoodsId(), InventoryConstanst.GoodsStatus.NOMAL,
                        ta.getFromShopId(), tockStatusBlock);
                if(sginsAdd == null){
                    sginsAdd = new StockGoodsInvNoSerial(ta.getFromShopId(), tad.getGoodsId(),
                            tad.getQuantity(), tad.getGoodsStatus(), tockStatusBlock,
                            providerId, new Date(), ta.getTransactionActionId());
                }else {
                    sginsAdd.setQuantity(sginsAdd.getQuantity() + tad.getQuantity());
                    sginsAdd.setCurrentTaId(ta.getTransactionActionId());
                }
                sginsDAO.saveOrUpdate(sginsAdd);

                StockTransactionSerial stSerial = new StockTransactionSerial();
                stSerial.setImTransactionId(ta.getTransactionActionId());
                stSerial.setStockTransactionDetailId(std.getStockTransactionDetailId());
                stSerial.setEquipmentProfileId(tad.getGoodsId());
                stSerial.setEquipmentProfileStatus(tad.getGoodsStatus());
                stSerial.setProviderId(providerId);
                stSerial.setStsDatetime(new Date());
                stsDAO.saveOrUpdate(stSerial);
                //todo:end
                sg.setAvailableQuantity(sg.getAvailableQuantity() - tad.getQuantity());
                sgDAO.saveOrUpdate(sg);
            }
        }
        return true;
    }

    public boolean imImApprove() {
        StockTransactionDAO stDAO = new StockTransactionDAO();
        TransactionActionDAO taDAO = new TransactionActionDAO();
        ta = taDAO.findById(ta.getTransactionActionId());
        if (!ta.getStatus().equals(InventoryConstanst.TransactionStatus.EX) && !ta.getStatus().equals(InventoryConstanst.TransactionStatus.EX_R)) {
            return false;
        }
        StockGoodsDAO sgDAO = new StockGoodsDAO();
        StockGoodsInvSerialDAO sgiDAO = new StockGoodsInvSerialDAO();
        EquipmentsProfileDAO equipmentsProfileDAO = new EquipmentsProfileDAO();
        EquipmentDetailDAO detailDAO = new EquipmentDetailDAO();
        StockGoodsInvNoSerialDAO sginsDAO = new StockGoodsInvNoSerialDAO();
        StockTransaction st = stDAO.getLastByTransaction(ta.getTransactionActionId());
        st.setStockTransStatus(InventoryConstanst.StockTransactionStatus.SUCCESS);
        st.setDeliveryType(InventoryConstanst.StockTransactionDeliveryType.TRANS);
        st.setFinishDatetime(new Date());
        st.setImportStaffId(staffId);
        stDAO.saveOrUpdate(st);
        if(ta.getStatus().equals(InventoryConstanst.TransactionStatus.EX)) {
            ta.setStatus(InventoryConstanst.TransactionStatus.IM);
        }else{
            ta.setStatus(InventoryConstanst.TransactionStatus.IM_R);
        }
        ta.setDescription(description);
        taDAO.saveOrUpdate(ta);
        List<StockGoodsInvSerial> setSerial = sgiDAO.findByTaId(ta.getTransactionActionId());
//        List<StockGoodsInvSerial> setSerial = sgiDAO.findByTaId(ta.getTransactionActionId());
        boolean isFirst = false;
        String[] lstColumn = {InventoryConstanst.ACTION_AU_DETAIL.SHOP_ID};
        String[] lstColumnNosrial = {InventoryConstanst.ACTION_AU_DETAIL.SHOP_ID, InventoryConstanst.ACTION_AU_DETAIL.QUANTITY_DC};
        BusinessService businessService = new BusinessService();
        ShopDAO shopDAO  = new ShopDAO();
        for (TransactionActionDetail tad : lsDetail) {
            EquipmentsProfile profile = equipmentsProfileDAO.findByProfileId(tad.getGoodsId());
            if(InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                if(!isFirst){
                    for (StockGoodsInvSerial objSerial:setSerial) {
                        StockGoods sg = sgDAO.getStockGoods(objSerial.getEquipmentProfileId(), objSerial.getEquipmentProfileStatus(),
                                ta.getCreateShopId());
                        if (sg == null) {
                            sg = new StockGoods(0L, 0L, ta.getCreateShopId(), objSerial.getEquipmentProfileId());
                            sg.setGoodsStatus(objSerial.getEquipmentProfileStatus());
                        }
                        sg.setAvailableQuantity(sg.getAvailableQuantity() + 1L);
                        sg.setQuantity(sg.getQuantity() + 1L);
//                        sg.setAvailableQuantity(sg.getAvailableQuantity() + tad.getQuantity());
//                        sg.setQuantity(sg.getQuantity() + tad.getQuantity());
                        sgDAO.saveOrUpdate(sg);

                        StockGoods sgp = sgDAO.getStockGoods(objSerial.getEquipmentProfileId(), objSerial.getEquipmentProfileStatus(),
                        ta.getFromShopId());
                        if (sgp == null) {
                            sgp = new StockGoods(0L, 0L, ta.getToShopId(), objSerial.getEquipmentProfileId());
                            sgp.setGoodsStatus(objSerial.getEquipmentProfileStatus());
                        }
                        sgp.setQuantity(sgp.getQuantity() - 1L);
                        sgDAO.saveOrUpdate(sgp);
                        EquipmentsDetail detail = detailDAO.findBySerial(objSerial.getSerial());
                        if(detail!=null){
                            detail.setStaffId(ta.getCreateStaffId());
                            detail.setShopId(ta.getCreateShopId());
//                            ShopDAO shopDAO  = new ShopDAO();
                            Shop shop = null;
                            if(ta.getFromShopId()!=null){
                                shop = shopDAO.findById(ta.getFromShopId());
                            }
                            String[] valuesOld = new String[]{shop==null?"":shop.getShopName()};

                            if(ta.getCreateShopId()!=null){
                                shop = shopDAO.findById(ta.getCreateShopId());
                            }
                            String[] valuesNew = new String[]{shop==null?"":shop.getShopName()};
                            businessService.insertHistory(detail, lstColumn, valuesNew,
                                    valuesOld, actionType);
                        }

                    }
                }
                isFirst = true;
            }else {
                StockGoods sg = sgDAO.getStockGoods(tad.getGoodsId(), InventoryConstanst.GoodsStatus.NOMAL, ta.getCreateShopId());
                if (sg == null) {
                    sg = new StockGoods(0L, 0L, ta.getCreateShopId(), tad.getGoodsId());
                    sg.setGoodsStatus(InventoryConstanst.GoodsStatus.NOMAL);
                }
                StockGoodsInvNoSerial sgins = sginsDAO.getStockGoodsNoserial(tad.getGoodsId(), InventoryConstanst.GoodsStatus.NOMAL,
                        ta.getFromShopId(),tockStatusBlock);

                StockGoodsInvNoSerial sginsIn = sginsDAO.getStockGoodsNoserial(tad.getGoodsId(), InventoryConstanst.GoodsStatus.NOMAL,
                        ta.getCreateShopId(),InventoryConstanst.StockStatus.INSTOCK);
                if(sginsIn == null){
                    sginsIn = new StockGoodsInvNoSerial(ta.getCreateShopId(), tad.getGoodsId(),
                            tad.getQuantity(), sgins.getEquipmentStatus(), InventoryConstanst.StockStatus.INSTOCK,
                            sgins.getProviderId(), new Date(), ta.getTransactionActionId());
                }else {
                    sginsIn.setQuantity(tad.getQuantity() + sginsIn.getQuantity());
                    sginsIn.setCurrentTaId(ta.getTransactionActionId());
                }
                sgins.setQuantity(sgins.getQuantity() - tad.getQuantity());
                sginsDAO.saveOrUpdate(sginsIn);
                if(sgins.getQuantity().equals(0L)){
                    sginsDAO.delete(sgins);
                }else {
                    sginsDAO.saveOrUpdate(sgins);
                }
                sg.setAvailableQuantity(sg.getAvailableQuantity() + tad.getQuantity());
                sg.setQuantity(sg.getQuantity() + tad.getQuantity());
                sgDAO.saveOrUpdate(sg);

                List<EquipmentsDetail> details = detailDAO.findByProfile(tad.getGoodsId());
                if(details!=null && !details.isEmpty()){
                    EquipmentsDetail detail = details.get(0);
                    if(detail!=null){
                        detail.setStaffId(ta.getCreateStaffId());
                        detail.setShopId(ta.getCreateShopId());
                        Shop shop = null;
                        if(ta.getFromShopId()!=null){
                            shop = shopDAO.findById(ta.getFromShopId());
                        }
                        String[] valuesOld = new String[]{shop==null?"":shop.getShopName(), ""};

                        if(ta.getCreateShopId()!=null){
                            shop = shopDAO.findById(ta.getCreateShopId());
                        }
                        String[] valuesNew = new String[]{shop==null?"":shop.getShopName(), tad.getQuantity().toString() };
                        businessService.insertHistory(detail, lstColumnNosrial, valuesNew,
                                valuesOld, actionType);
                    }
                }

            }


            if(InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
            }else {
                StockGoods sgp = sgDAO.getStockGoods(tad.getGoodsId(), InventoryConstanst.GoodsStatus.NOMAL, ta.getFromShopId());
                if (sgp == null) {
                    sgp = new StockGoods(0L, 0L, ta.getToShopId(), tad.getGoodsId());
                    sgp.setGoodsStatus(InventoryConstanst.GoodsStatus.NOMAL);
                }
                sgp.setQuantity(sgp.getQuantity() - tad.getQuantity());
//                sgp.setAvailableQuantity(sgp.getAvailableQuantity());
                sgDAO.saveOrUpdate(sgp);
            }
            sgiDAO.updateByTaId(ta.getTransactionActionId(), ta.getToShopId(), InventoryConstanst.StockStatus.INSTOCK, ta.getTransactionActionId());
        }
        return true;
    }
}
