/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.java.bo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.ftu.constanst.InventoryConstanst;
import com.ftu.inventory.bo.TransactionAction;
import com.ftu.inventory.bo.TransactionActionDetail;
import com.ftu.inventory.dao.TransactionActionDetailDAO;
import com.ftu.staff.bo.ApDomain;
import com.ftu.staff.bo.Shop;
import com.ftu.staff.bo.Staff;
import com.ftu.staff.dao.ShopDAO;
import com.ftu.staff.dao.StaffDAO;

/**
 *
 * @author E5420
 */
public class LazyTransActionDetailModel extends LazyDataModel<TransactionActionDetail> implements Serializable {

	private List<TransactionActionDetail> datasource;
	private Long shopId;
	private Date fromCreateDatetime;
	private Date toCreateDatetime;
	private List<Long> lsType;
	private String compactTransactionType;
	private List<ApDomain> listTransType;
	int f = -1;

	public LazyTransActionDetailModel(List<ApDomain> listTransType ,Long shopId, Date fromCreateDatetime, Date toCreateDatetime, List<Long> lsType,
			String compactTransactionType) {
		super();
		this.listTransType = listTransType;
		this.shopId = shopId;
		this.fromCreateDatetime = fromCreateDatetime;
		this.toCreateDatetime = toCreateDatetime;
		this.lsType = lsType;
		this.compactTransactionType = compactTransactionType;
	}
	
	private String getTransType(Long taId) {
        if (listTransType != null) {
            for (ApDomain d : listTransType) {
                if (d.getValue().equals(taId)) {
                    return d.getName();
                }
            }
        }
        return "";
    }

	@Override
	public TransactionActionDetail getRowData(String rowKey) {
		if (datasource != null) {
			for (TransactionActionDetail g : datasource) {
				if (g.getTransactionActionDetailId().toString().equals(rowKey)) {
					return g;
				}
			}
		}

		return null;
	}
	
	@Override
    public Object getRowKey(TransactionActionDetail transactionActionDetail) {
        return transactionActionDetail.getTransactionActionDetailId();
    }

	@Override
	public List<TransactionActionDetail> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
		boolean desc;
		if(sortOrder.name().equals("DESCENDING")){
			desc = false;
		} else{
			desc = true;
		}
		TransactionActionDetailDAO taDAO = new TransactionActionDetailDAO();
		if (f == 0) {
			first = 0;
		}
		if (compactTransactionType.equals(InventoryConstanst.TransactionTypeCompact.EX)) {
			datasource = taDAO.searchFromShop(null,shopId, fromCreateDatetime, toCreateDatetime, lsType, null, first,
					pageSize, sortField, desc);
			int dataSize = taDAO.searchSizeFromShop(null, shopId, fromCreateDatetime, toCreateDatetime, lsType, null)
					.intValue();
			this.setRowCount(dataSize);
		} else{
			datasource = taDAO.searchToShop(null, shopId, fromCreateDatetime, toCreateDatetime, lsType, null, first,
					pageSize, sortField, desc);
			int dataSize = taDAO.searchSizeToShop(null,shopId, fromCreateDatetime, toCreateDatetime, lsType, null)
					.intValue();
			this.setRowCount(dataSize);
		}

		
		if(datasource != null){
			SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
			for(TransactionActionDetail ta: datasource){
				if(ta.getTransactionAction() != null){
					ta.setStrCreateDatetime(sf.format(ta.getCreateDatetime()));
					ta.getTransactionAction().setTypeName(getTransType(Long.parseLong(ta.getTransactionAction().getTransactionType())));
				}
			}
		}

		f = -1;
		return datasource;
	}
}
