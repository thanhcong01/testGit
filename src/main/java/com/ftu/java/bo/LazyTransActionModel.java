/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.java.bo;

import com.ftu.inventory.bo.TransactionAction;
import com.ftu.inventory.dao.TransactionActionDAO;
import com.ftu.staff.bo.ApDomain;
import com.ftu.staff.bo.Shop;
import com.ftu.staff.bo.Staff;
import com.ftu.staff.dao.ShopDAO;
import com.ftu.staff.dao.StaffDAO;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author E5420
 */
public class LazyTransActionModel extends LazyDataModel<TransactionAction> implements Serializable {

    private List<TransactionAction> datasource;
    private List<Shop> listShop;
    private TransactionAction ta;
    private List<ApDomain> listTransStatus;
    private List<ApDomain> listTransType;
    private List<Staff> listStaff;
    private List<ApDomain> listReason;
    private String codePath;
    int f = -1;

    public LazyTransActionModel(TransactionAction ta, List<Shop> listShop, List<ApDomain> listTransStatus, List<ApDomain> listTransType, List<Staff> listStaff, List<ApDomain> listReason) {
        this.listShop = listShop;
        this.ta = ta;
        this.listTransStatus = listTransStatus;
        this.listTransType = listTransType;
        this.listStaff = listStaff;
        this.listReason = listReason;
        f = 0;
    }
    
    

    public LazyTransActionModel() {
	}



	private String getStaff(Long sId) {
        if (listStaff != null && sId != null) {
            for (Staff s : listStaff) {
                if (s.getStaffId().equals(sId)) {
                    return s.getStaffName();
                }
            }
        }
        if (sId != null) {
            StaffDAO stDAO = new StaffDAO();
            Staff s = stDAO.findById(sId);
            if (s != null) {
                return s.getStaffName();
            }
        }
        return "";
    }

    private String getTransStatus(Long taId) {
        if (listTransStatus != null) {
            for (ApDomain d : listTransStatus) {
                if (d.getValue().equals(taId)) {
                    return d.getName();
                }
            }
        }
        return "";
    }

    private String getReasonName(Long reasonId) {
        if (listReason != null) {
            for (ApDomain d : listReason) {
            	try{
                if (d.getValue().equals(reasonId)) {
                    return d.getName();
                }
            	}catch (Exception e)
            	{
            		System.out.println(e.getMessage());
            	}
            }
        }
        return "";
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

    private String getShopName(Long shopId) {
        if (listShop != null) {
            for (Shop d : listShop) {
                if (d.getShopId().equals(shopId)) {
                    return d.getShopName();
                }
            }
        }
        if (shopId != null) {
            ShopDAO sDAO = new ShopDAO();
            Shop s = sDAO.findById(shopId);
            if (s != null) {
                return s.getShopName();
            }
        }
        return "";
    }

    @Override
    public TransactionAction getRowData(String rowKey) {
//        if (datasource == null) {
//            TransactionActionDAO taDAO = new TransactionActionDAO();
//            datasource = taDAO.search(ta, listTransType, listTransStatus, 0, 1);
//        }
        if (datasource != null) {
            for (TransactionAction g : datasource) {
                if (g.getTransactionActionId().toString().equals(rowKey) || g.getTransactionActionCode().equals(rowKey)) {
                    return g;
                }
            }
        }

        return null;
    }

    @Override
    public List<TransactionAction> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        TransactionActionDAO taDAO = new TransactionActionDAO();
        if (f == 0) {
            first = 0;
        }
        if(sortField == null){
        	sortField = "createDatetime";
        }
        boolean desc;
        if(sortOrder.name().equals("DESCENDING")){
        	desc = false;
        } else{
        	desc = true;
        }
        datasource = taDAO.lazySearchAndSort(ta, listTransType, listTransStatus, first, pageSize, desc,  sortField);
        if (datasource != null) {
            for (TransactionAction s : datasource) {
                s.setShopName(getShopName(s.getCreateShopId() == null ? s.getFromShopId() : s.getCreateShopId()));
                s.setToShop(getShopName(s.getToShopId()));
                s.setTypeName(getTransType(Long.parseLong(s.getTransactionType())));
                s.setReasonName(getReasonName(s.getReasonId()));
                s.setStatusName(getTransStatus(Long.parseLong(s.getStatus())));
                s.setStaffName(getStaff(s.getCreateStaffId()));
                s.setAssStaffName(getStaff(s.getAssessmentStaffId()));
            }
            int dataSize = taDAO.lazySearchSize(ta, listTransType, listTransStatus).intValue();
            this.setRowCount(dataSize);
        }
        f = -1;
        return datasource;
    }
}
