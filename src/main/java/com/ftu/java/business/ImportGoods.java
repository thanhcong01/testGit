/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.java.business;

import com.ftu.constanst.InventoryConstanst;
import com.ftu.exception.AppException;
import com.ftu.hibernate.HibernateUtil;
import com.ftu.inventory.bean.SessionBean;
import com.ftu.inventory.bo.*;
import com.ftu.inventory.bussiness.EquipmentHistoryService;
import com.ftu.inventory.dao.*;
import com.ftu.inventory.dto.StockGoodsInvSerialDTO;
import com.ftu.staff.bo.Shop;
import com.ftu.staff.bo.Staff;
import com.ftu.staff.bussiness.ShopService;
import com.ftu.staff.dao.ShopDAO;
import com.ftu.utils.DateTimeUtils;
import com.ftu.ws.BusinessService;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author E5420
 */
public class ImportGoods {

    List<StockTransactionDetail> lsStockDetail;
    Collection<TransactionActionDetail> lsTaDetail;
    TransactionAction ta;
    StockTransaction st;
    String tacode = "";
    Staff staff;
    Long reasonId;
    private EquipmentHistoryDAO equipmentHistoryDAO = new EquipmentHistoryDAO();
    private ActionAuditDAO actionAuditDAO = new ActionAuditDAO();
    private EquipmentHistoryDetailDAO equipmentHistoryDetailDAO = new EquipmentHistoryDetailDAO();
    private Long handleType = 1L;
    private Long actionType;
    // SimpleDateFormat dtf = new SimpleDateFormat("dd-MM-yyyy");

    public ImportGoods(List<StockTransactionDetail> lsStockDetail, Staff staff, String tacode, Long reasonId, Long handleType) {
        this.staff = staff;
        this.lsStockDetail = lsStockDetail;
        this.tacode = tacode;
        this.reasonId = reasonId;
        this.handleType = handleType;
    }

    private boolean createTransactionAction() throws AppException {
        //  String date = dtf.format(new Date());
        Date d = new Date();
        TransactionActionDAO taDAO = new TransactionActionDAO();
        TransactionActionDetailDAO tadDAO = new TransactionActionDetailDAO();
        StockTransactionDAO stDAO = new StockTransactionDAO();
        StockTransactionDetailDAO stdDAO = new StockTransactionDetailDAO();
        //update KTV
        StockTransactionSerialDAO stsDAO = new StockTransactionSerialDAO();
        StockGoodsInvSerialDAO sgisDAO = new StockGoodsInvSerialDAO();
        EquipmentsProfileDAO equipmentsProfileDAO = new EquipmentsProfileDAO();
        StockGoodsInvNoSerialDAO sginsDAO = new StockGoodsInvNoSerialDAO();
//        EquipmentDetailDAO detailDAO = new EquipmentDetailDAO();
//        end
        StockGoodsDAO stgDAO = new StockGoodsDAO();
        EquipmentDetailDAO edDAO = new EquipmentDetailDAO();
//        stsDAO.getStockSerials(s.gets)
        ta = new TransactionAction();
        //add bao duong, sua chua
//        ta.setTransactionActionId(lsStockDetail.get(0).getUdTaId());
        //==end
        ta.setTransactionActionCode(tacode);
        
        if(handleType.equals(InventoryConstanst.ImportType.importGood)){
        	ta.setTransactionType(InventoryConstanst.TransactionType.IM);
        	ta.setStatus(InventoryConstanst.TransactionStatus.CREATE);
            actionType = InventoryConstanst.ACTION_TYPE.TYPE_6L;
        }else if(handleType.equals(InventoryConstanst.ImportType.importKTV)){
        	ta.setTransactionType(InventoryConstanst.TransactionType.IM_STAFF);
        	ta.setStatus(InventoryConstanst.TransactionStatus.IM_STAFF);
            actionType = InventoryConstanst.ACTION_TYPE.TYPE_11L;
        }else if(handleType.equals(InventoryConstanst.ImportType.importWarranty)){
        	ta.setTransactionType(InventoryConstanst.TransactionType.IM_WARRANTY);
            ta.setStatus(InventoryConstanst.TransactionStatus.CREATE_WARRANTY);
            actionType = InventoryConstanst.ACTION_TYPE.TYPE_13L;
        }
        
        ta.setCreateDatetime(d);
        ta.setReasonId(reasonId);

        ta.setCreateShopId(staff.getShopId());
        ta.setCreateStaffId(staff.getStaffId());
        ta.setToShopId(staff.getShopId());
        taDAO.saveOrUpdate(ta);
        st = new StockTransaction();
        //them: sua chua, bao duong
//        st.setStockTransactionId(lsStockDetail.get(0).getUdStaId());
        //==end
        // st.setFinishDatetime(new Date());
        st.setStockTransactionDatetime(d);

        st.setStockTransStatus(InventoryConstanst.StockTransactionStatus.WAIT);
        st.setDeliveryType(InventoryConstanst.StockTransactionDeliveryType.WAIT);
        st.setStockTransactionDatetime(new Date());
        if(handleType.equals(InventoryConstanst.ImportType.importGood)){
            st.setStockTransactionType(InventoryConstanst.StockTransactionType.IM);
        }else if(handleType.equals(InventoryConstanst.ImportType.importKTV)){
            st.setStockTransactionType(InventoryConstanst.StockTransactionType.IM_STAFF);
        }else{
            st.setStockTransactionType(InventoryConstanst.StockTransactionType.IM_WARRANTY);
        }
        st.setTransactionActionId(ta.getTransactionActionId());
        st.setToShopId(staff.getShopId());
        stDAO.saveOrUpdate(st);
        HashMap<String, TransactionActionDetail> mapDetail = new HashMap<>();
        String[] lstColumn = {InventoryConstanst.ACTION_AU_DETAIL.SHOP_ID};
        String[] lstColumnNoSerial = {InventoryConstanst.ACTION_AU_DETAIL.SHOP_ID, InventoryConstanst.ACTION_AU_DETAIL.QUANTITY};
        BusinessService businessService = new BusinessService();
        ShopDAO shopDAO  = new ShopDAO();
        for (StockTransactionDetail std : lsStockDetail) {
            Long siz = 0L + std.getLsSerial().size();
            std.setStockTransactionId(st.getStockTransactionId());
            if(InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(std.getProfileManagementType())){
                std.setQuantity(siz);
            }
            std.setStdDatetime(d);
            stdDAO.saveOrUpdate(std);
            //update stock quanti nhap hang ky thuat vien
            if(handleType.equals(InventoryConstanst.ImportType.importKTV)){
                for (StockGoodsInvSerialDTO sgis : std.getSetSerial()) {
                    StockGoods sg = stgDAO.getStockGoods(sgis.getEquipmentProfileId(), sgis.getEquipmentProfileStatus(), ta.getCreateShopId());
                    if (sg == null) {
                        sg = new StockGoods(0L, 0L, ta.getCreateShopId(), sgis.getEquipmentProfileId());
                        sg.setGoodsStatus(std.getGoodsStatus());
                        sg.setAvailableQuantity(sg.getAvailableQuantity() + 1L);
                        sg.setQuantity(sg.getQuantity() + 1L);
                    } else {
                        sg.setAvailableQuantity(sg.getAvailableQuantity() + 1L);
                        sg.setQuantity(sg.getQuantity() + 1L);
                    }
                    stgDAO.saveOrUpdate(sg);
                }
            }

            for (StockTransactionSerial s : std.getLsSerial()) {
                EquipmentsDetail ed = s.getEquipmentDetail();
                ed.setImTransactionId(ta.getTransactionActionId());
                //cap nhat stocktransactionserial
                StockTransactionSerial stockTransactionSerial = new StockTransactionSerial();
                stockTransactionSerial.setImTransactionId(ta.getTransactionActionId());
                stockTransactionSerial.setStockTransactionDetailId(std.getStockTransactionDetailId());
                stockTransactionSerial.setEquipmentProfileId(std.getGoodsId());
                stockTransactionSerial.setSerial(ed.getSerial());
                stockTransactionSerial.setEquipmentProfileStatus(std.getGoodsStatus());
                stockTransactionSerial.setEquipmentCode(ed.getEquipmentCode());
                stockTransactionSerial.setProviderId(ed.getProviderId());
                stockTransactionSerial.setSerialSearch(ed.getSerial());
                stockTransactionSerial.setStsDatetime(new Date());
                try {
                    String serialOld = "";
                    Long equipmentId = null;
                    if(handleType.equals(InventoryConstanst.ImportType.importWarranty)){
                        if(ed.getChangWaranNew()){
                            EquipmentsDetail edOl = edDAO.findBySerial(ed.getSerial());
                            serialOld = edOl.getSerial();
                            equipmentId = edOl.getId();
                            edOl.setImTransactionId(ta.getTransactionActionId());
                            edOl.setWarantyStatus(InventoryConstanst.WARAN_STATUS.WARAN_STATUS_1L);
                            edOl.setStatus(InventoryConstanst.GoodsStatus.ERROR);
                            edDAO.saveOrUpdate(edOl);
                            StockTransactionSerial stockTransactionSerialOld = new StockTransactionSerial(stockTransactionSerial);
                            stockTransactionSerialOld.setEquipmentProfileStatus(InventoryConstanst.GoodsStatus.ERROR);
                            stsDAO.saveOrUpdate(stockTransactionSerialOld);
                            ed.setSerial(ed.getSerialNew());
                            ed.setWarantyStatus(InventoryConstanst.WARAN_STATUS.WARAN_STATUS_2L);
                            ed.setWarrantyCount(null);
                            stockTransactionSerial.setEquipmentProfileStatus(std.getGoodsStatus());
                            stockTransactionSerial.setSerial(ed.getSerial());
                            stockTransactionSerial.setSerialSearch(ed.getSerial());

                        }
                    }
                    if(InventoryConstanst.ImportType.importGood.equals(handleType)){
                        if(ed.getSerial()!=null && !ed.getSerial().isEmpty()){
                            edDAO.saveOrUpdate(ed);
                        }else if(ed.getId()==null){
                            List<EquipmentsDetail> equipmentsDetails = edDAO.findByProfile(ed.getEquipmentsProfileId());
                            if(equipmentsDetails == null || equipmentsDetails.isEmpty()){
                                edDAO.saveOrUpdate(ed);
                            }
                        }
                    }else {
                        if(InventoryConstanst.ImportType.importKTV.equals(handleType)){
                            if(ed!=null && ed.getId()!=null){
                                edDAO.saveOrUpdate(ed);
                            }
                        }else {
                            edDAO.saveOrUpdate(ed);
                        }

                    }

                    stsDAO.saveOrUpdate(stockTransactionSerial);

                    if(handleType.equals(InventoryConstanst.ImportType.importWarranty)) {
                        if (ed.getChangWaranNew()) {
                            insertHistory(serialOld, ed.getSerial(), equipmentId);
                            insertHistory(serialOld, ed.getSerial(), ed.getId());
                        }
                    }
//                    edDAO.insertBysql(ed);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    AppException e = new AppException("ERROR-I001", new Object[]{ed.getSerial(), s.getProvider()}, new Locale(InventoryConstanst.local));
                    return false;
                }
                //
                TransactionActionDetail tad = mapDetail.get(s.getProviderId().toString()+"-"+s.getEquipmentProfileId());
                if (tad == null) {
                    tad = new TransactionActionDetail(s.getEquipmentProfileId(), s.getProviderId());
//                    tad.setQuantity(s.getQuantity());
                    tad.setQuantity(std.getQuantity());
                    mapDetail.put(s.getProviderId().toString()+"-"+s.getEquipmentProfileId(), tad);
                }
                tad.setReferenceCode(ed.getReferenCode());
//                tad.setQuantity(tad.getQuantity() + 1);
//                                tad.setQuantity(s.getQuantity());
            }
            //todo: xem lai trong truong hop sua chua, bao hanh
//            stsDAO.insertByEtag(std, ta.getTransactionActionId());
            //end
            //update nhap hang ktv
            if(handleType.equals(InventoryConstanst.ImportType.importKTV)){
                EquipmentsProfile profile = equipmentsProfileDAO.findByProfileId(std.getGoodsId());
                if(InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                    for (StockGoodsInvSerialDTO sgis : std.getSetSerial()) {
                        sgis.setStockStatus(InventoryConstanst.StockStatus.INSTOCK);
                        sgis.setCurrentTaId(ta.getTransactionActionId());
                        sgis.setEquipmentProfileStatus(std.getGoodsStatus());
                        sgisDAO.updateStockTransBySqlKTV(sgisDAO.convertDTOtoEntity(sgis));

                        EquipmentsDetail detail = null;
                        if(sgis.getSerial() !=null && !sgis.getSerial().isEmpty()){
                            detail = edDAO.findBySerial(sgis.getSerial());
                        }else {
                            detail = null;
                        }
                        if(detail!=null){
                            detail.setStaffId(ta.getCreateStaffId());
                            detail.setShopId(ta.getToShopId());
                            Shop shop = null;
//                        if(ta.getFromShopId()!=null){
//                            shop = shopDAO.findById(ta.getFromShopId());
//                        }
//                        String[] valuesOld = new String[]{shop==null?"":shop.getShopName()};
                            String[] valuesOld = new String[]{""};
                            if(ta.getCreateShopId()!=null){
                                shop = shopDAO.findById(ta.getToShopId());
                            }
                            String[] valuesNew = new String[]{shop==null?"":shop.getShopName()};
                            businessService.insertHistory(detail, lstColumn, valuesNew,
                                    valuesOld, actionType);
                        }

                    }
                }else {
                    StockGoodsInvNoSerial sgins = sginsDAO.getStockGoodsNoserial(std.getGoodsId(), InventoryConstanst.GoodsStatus.NOMAL,
                            ta.getCreateShopId(), InventoryConstanst.StockStatus.EX_USED);
                    sgins.setQuantity(sgins.getQuantity() - std.getQuantity());
                    sgins.setCurrentTaId(ta.getTransactionActionId());
                    sginsDAO.saveOrUpdate(sgins);
                    EquipmentsDetail detail = edDAO.findByProfile(std.getGoodsId()).get(0);
                    Long providerId = detail.getProviderId();
                    StockGoodsInvNoSerial sginsAdd =sginsDAO.getStockGoodsNoserial(std.getGoodsId(), std.getGoodsStatus(),
                            ta.getCreateShopId(), InventoryConstanst.StockStatus.INSTOCK);
                    if(sginsAdd == null){
                        sginsAdd = new StockGoodsInvNoSerial(ta.getCreateShopId(), std.getGoodsId(),
                                std.getQuantity(), std.getGoodsStatus(), InventoryConstanst.StockStatus.INSTOCK,
                                providerId, new Date(), ta.getTransactionActionId());
                    }else {
                        sginsAdd.setQuantity(sginsAdd.getQuantity() + std.getQuantity());
                        sginsAdd.setCurrentTaId(ta.getTransactionActionId());
                    }
                    Shop shop = null;
                    if(ta.getCreateShopId()!=null){
                        shop = shopDAO.findById(ta.getToShopId());
                    }
                    detail.setStaffId(ta.getCreateStaffId());
                    detail.setShopId(ta.getToShopId());
                    String[] valuesNew = new String[]{shop==null?"":shop.getShopName(), std.getQuantity().toString()};
                    businessService.insertHistory(detail, lstColumnNoSerial, valuesNew,
                            new String[]{"", ""}, actionType);

                    stgDAO.updateQuantity(false, std.getQuantity(), std.getGoodsId(),  Long.parseLong(InventoryConstanst.StockStatus.INSTOCK) ,ta.getCreateShopId() );
                    sginsDAO.saveOrUpdate(sginsAdd);
                }
            }
            //end

        }
        try {
            lsTaDetail = mapDetail.values();
            for (TransactionActionDetail tad : lsTaDetail) {
                tad.setTransactionActionId(ta.getTransactionActionId());
                tad.setCreateDatetime(d);
                tadDAO.saveOrUpdate(tad);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        try {
            HibernateUtil.commitCurrentSessions();
        }catch (Exception ex){
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(ShopService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

        return true;
    }

    public Boolean importGoods() throws AppException {
        return createTransactionAction();
    }

    public void insertHistory(String serialOld, String serialNew, Long equipmentIdNew) throws Exception{
        try {
            ActionAudit actionAudit = new ActionAudit();
            EquipmentHistory equipmentHistory = new EquipmentHistory();
            equipmentHistory.setEquipmentId(equipmentIdNew);
            equipmentHistory.setCreatedDatetime(DateTimeUtils.getDate());
            equipmentHistory.setStaffId(staff.getStaffId());
            equipmentHistory.setShopId(staff.getShopId());
            equipmentHistoryDAO.saveOrUpdate(equipmentHistory);

            actionAudit.setReferenceId(equipmentHistory.getEquipmentHistoryId());
            actionAudit.setActionDatetime(DateTimeUtils.getDate());
            actionAudit.setActionType(InventoryConstanst.ACTION_TYPE.TYPE_4L);
            actionAudit.setReferenceId(equipmentHistory.getEquipmentHistoryId());
            actionAuditDAO.saveOrUpdate(actionAudit);

            EquipmentHistoryDetail obj = new EquipmentHistoryDetail();
            obj.setEquipmentHistoryId(equipmentHistory.getEquipmentHistoryId());
            obj.setFeildName(InventoryConstanst.ACTION_AU_DETAIL.SERIAL);
            obj.setNewValue(serialNew);
            obj.setOldValue(serialOld);
            equipmentHistoryDetailDAO.saveOrUpdate(obj);

        }catch (Exception ex){
            throw  ex;
        }
    }

}
