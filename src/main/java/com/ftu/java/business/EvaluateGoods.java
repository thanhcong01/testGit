/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.java.business;

import com.ftu.inventory.bo.GoodsStatusTrans;
import com.ftu.inventory.bo.GoodsStatusTransSerial;
import com.ftu.inventory.bo.StockGoodsInvSerial;
import com.ftu.inventory.bo.StockGoods;
import com.ftu.inventory.dao.GoodsStatusTransDAO;
import com.ftu.inventory.dao.GoodsStatusTransSerialDAO;
import com.ftu.inventory.dao.StockGoodsDAO;
import com.ftu.inventory.dao.StockGoodsInvSerialDAO;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author E5420
 */
public class EvaluateGoods {

    private Set<StockGoodsInvSerial> lsDataRs;
    GoodsStatusTrans gs;

    public EvaluateGoods(Set<StockGoodsInvSerial> lsDataRs, GoodsStatusTrans gs) {
        this.lsDataRs = lsDataRs;
        this.gs = gs;
    }

    public Boolean evaluate() {
        GoodsStatusTransDAO gstDAO = new GoodsStatusTransDAO();
        GoodsStatusTransSerialDAO gstsDAO = new GoodsStatusTransSerialDAO();
        StockGoodsInvSerialDAO sgisDAO = new StockGoodsInvSerialDAO();
        StockGoodsDAO sgDAO = new StockGoodsDAO();
        Date d = new Date();
        gs.setTransDatetime(d);
        gstDAO.saveOrUpdate(gs);
        HashMap<String, Boolean> checkStockGoods = new HashMap<String, Boolean>();
        for (StockGoodsInvSerial ss : lsDataRs) {
            ss = sgisDAO.findById(ss.getId());
            if (gs.getStockStatusTrans() == null) {
                GoodsStatusTransSerial gss = new GoodsStatusTransSerial(ss.getEquipmentProfileStatus(), gs.getGoodsStsTrans(), ss.getEquipmentProfileId(), ss.getProviderId(), ss.getSerial(), gs.getGoodsStatusTransId());
                gss.setTransDatetime(d);
                gstsDAO.saveOrUpdate(gss);
                ss.setEquipmentProfileStatus(gs.getGoodsStsTrans());
                sgisDAO.saveOrUpdate(ss);
                sgDAO.updateQuantity(Boolean.TRUE, 1L, ss.getEquipmentProfileId(), ss.getEquipmentProfileStatus(), ss.getShopId());
//                StockGoods sgO = sgDAO.getStockGoods(ss.getEquipmentProfileId(), ss.getGoodsStatus(), ss.getShopId());
//                if (sgO != null) {
//                    sgO.setQuantity(sgO.getQuantity() - 1);
//                    sgO.setAvailableQuantity(sgO.getAvailableQuantity() - 1);
//                    sgDAO.saveOrUpdate(sgO);
//                }
                if (checkStockGoods.get(ss.getEquipmentProfileId().toString() + "-" + gs.getGoodsStsTrans().toString() + "-" + ss.getShopId()) != null || sgDAO.checkStockGoods(ss.getEquipmentProfileId(), ss.getEquipmentProfileStatus(), ss.getShopId())) {
                    sgDAO.updateQuantity(Boolean.FALSE, 1L, ss.getEquipmentProfileId(), gs.getGoodsStsTrans(), ss.getShopId());
                    if(checkStockGoods.get(ss.getEquipmentProfileId().toString() + "-" + gs.getGoodsStsTrans().toString() + "-" + ss.getShopId()) == null)
                    {
                        checkStockGoods.put(ss.getEquipmentProfileId().toString() + "-" + gs.getGoodsStsTrans().toString() + "-" + ss.getShopId(), Boolean.TRUE);
                    }

                } else {
                    StockGoods sgN = new StockGoods(0L, 0L, ss.getShopId(), ss.getEquipmentProfileId());
                    sgN.setGoodsStatus(gs.getGoodsStsTrans());
                    sgN.setQuantity(sgN.getQuantity() + 1);
                    sgN.setAvailableQuantity(sgN.getAvailableQuantity() + 1);
                    sgDAO.saveOrUpdate(sgN);
                    checkStockGoods.put(ss.getEquipmentProfileId().toString() + "-" + gs.getGoodsStsTrans().toString() + "-" + ss.getShopId(), Boolean.TRUE);
                }
//                StockGoods sgN = sgDAO.getStockGoods(ss.getEquipmentProfileId(), gs.getGoodsStsTrans(), ss.getShopId());
//                if (sgN == null) {
//                    sgN = new StockGoods(0L, 0L, ss.getShopId(), ss.getEquipmentProfileId());
//                    sgN.setGoodsStatus(gs.getGoodsStsTrans());
//                }
//                sgN.setQuantity(sgN.getQuantity() + 1);
//                sgN.setAvailableQuantity(sgN.getAvailableQuantity() + 1);
//                sgDAO.saveOrUpdate(sgN);
            } else {
                GoodsStatusTransSerial gss = new GoodsStatusTransSerial(Long.parseLong(ss.getStockStatus()), Long.parseLong(gs.getStockStatusTrans()), ss.getEquipmentProfileId(), ss.getProviderId(), ss.getSerial(), gs.getGoodsStatusTransId());
                gss.setTransDatetime(d);
                gstsDAO.saveOrUpdate(gss);
                ss.setStockStatus(gs.getStockStatusTrans());
                sgisDAO.saveOrUpdate(ss);
            }
        }
        return true;
    }
}
