/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.java.bo;

import com.ftu.inventory.bo.StockGoodsInvSerial;
import com.ftu.inventory.dao.StockGoodsInvSerialDAO;
import com.ftu.inventory.dto.StockGoodsInvSerialDTO;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author E5420
 */
public class LazyEvaluatesEquipmentModel extends LazyDataModel<StockGoodsInvSerialDTO> implements Serializable {

    private List<StockGoodsInvSerialDTO> datasource;
    private StockGoodsInvSerialDTO stockGoodsInvSerial;
    int f = -1;

    public LazyEvaluatesEquipmentModel(StockGoodsInvSerialDTO stockGoodsInvSerial) {
//        this.listShop = listShop;
        this.stockGoodsInvSerial = stockGoodsInvSerial;
//        if (from != null && from.length() >= 8)
//        	this.from = from.substring(from.length()-8);
//        if (to != null && to.length() >= 8)
//        	this.to = to.substring(to.length()-8);
//        this.goodsGroupId = goodsGroupId;
        f = 0;
    }

//    private String getShopName(Long shopId) {
//        if (listShop != null) {
//            for (Shop d : listShop) {
//                if (d.getShopId().equals(shopId)) {
//                    return d.getShopName();
//                }
//            }
//        }
//        return "";
//    }

    @Override
    public StockGoodsInvSerialDTO getRowData(String rowKey) {
        if (datasource != null) {
            for (StockGoodsInvSerialDTO g : datasource) {
                if (g.getId().toString().equals(rowKey)) {
                    return g;
                }
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(StockGoodsInvSerialDTO g) {
        return g.getId();
    }

    @Override
    public List<StockGoodsInvSerialDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        StockGoodsInvSerialDAO sgisDAO = new StockGoodsInvSerialDAO();
        if (f == 0) {
            first = 0;
        }
        boolean desc;
        if(sortOrder.name().equals("DESCENDING")){
            desc = false;
        } else{
            desc = true;
        }
        datasource = sgisDAO.search(stockGoodsInvSerial, first, pageSize, sortField, desc);

        int dataSize = sgisDAO.searchSize(stockGoodsInvSerial).intValue();
        this.setRowCount(dataSize);
        f = -1;
        return datasource;
    }
}
