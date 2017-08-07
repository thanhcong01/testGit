/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.java.bo;

import com.ftu.inventory.bo.Provider;
import com.ftu.inventory.bo.StockTransactionSerial;
import com.ftu.inventory.dao.StockTransactionSerialDAO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author E5420
 */
public class LazyStockSerialModel extends LazyDataModel<StockTransactionSerial> implements Serializable {

    private List<StockTransactionSerial> datasource;
    private String from;
    private String to;
    private Long transId;
    private Long stockId;
    private Long providerId;
    private Long goodsId;
    private Long gstatus;
    int f = -1;

    public LazyStockSerialModel(String from, String to, Long transId, Long stockId, Long providerId, Long goodsId, Long gstatus) {
        f = 0;
//        if (from != null && from.length() >= 8)
//        	this.from = from.substring(from.length()-8);
//        if (to != null && to.length() >= 8)
//        	this.to = to.substring(to.length()-8);
        this.from = from;
        this.to = to;
        this.transId = transId;
        this.stockId = stockId;
        this.providerId = providerId;
        this.goodsId = goodsId;
        this.gstatus = gstatus;
    }


    @Override
    public StockTransactionSerial getRowData(String rowKey) {
        if (datasource != null) {
            for (StockTransactionSerial g : datasource) {
                if (g.getStockTransactionSerialId().toString().equals(rowKey)) {
                    return g;
                }
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(StockTransactionSerial g
    ) {
        return g.getStockTransactionSerialId();
    }

    @Override
    public List<StockTransactionSerial> load(int first, int pageSize, String sortField, SortOrder sortOrder,
                                             Map<String, Object> filters
    ) {
        StockTransactionSerialDAO stdDAO = new StockTransactionSerialDAO();
        if (f == 0) {
            first = 0;
        }
        boolean desc;
        if(sortOrder.name().equals("DESCENDING")){
            desc = false;
        } else{
            desc = true;
        }
        datasource = stdDAO.searchTransSearch(from, to, providerId, goodsId, gstatus, transId, stockId,null,
                first, pageSize, sortField, desc);
        if (datasource != null) {  
            int dataSize = stdDAO.searchTransSearchSize(from, to, providerId, goodsId, gstatus, transId, stockId).intValue();
            this.setRowCount(dataSize);
        }
        f = -1;
        return datasource;
    }
}
