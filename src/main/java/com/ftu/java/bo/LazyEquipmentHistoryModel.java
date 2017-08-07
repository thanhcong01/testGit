/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.java.bo;

import com.ftu.inventory.bo.EquipmentHistory;
import com.ftu.inventory.bo.EquipmentsDetail;
import com.ftu.inventory.bussiness.EquipmentHistoryService;
import com.ftu.inventory.dao.EquipmentDetailDAO;
import com.ftu.inventory.dao.EquipmentHistoryDAO;
import com.ftu.staff.bo.Shop;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author E5420
 */
public class LazyEquipmentHistoryModel extends LazyDataModel<EquipmentHistory> implements Serializable {

    private List<EquipmentHistory> datasource;
    private List<Shop> listShop;
    private EquipmentHistory equipmentHistory;
    private Long equipmentId;
    int f = -1;

    public LazyEquipmentHistoryModel( EquipmentHistory equipmentHistory, Long equipmentId) {
        this.listShop = listShop;
        if(equipmentHistory==null){
            this.equipmentHistory = new EquipmentHistory();
        }else {
            this.equipmentHistory = equipmentHistory;
        }

        this.equipmentId = equipmentId;
        f = 0;
    }



    @Override
    public EquipmentHistory getRowData(String rowKey) {
        if (datasource != null) {
            for (EquipmentHistory g : datasource) {
                if (g.getEquipmentHistoryId().toString().equals(rowKey)) {
                    return g;
                }
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(EquipmentHistory g) {
        return g.getEquipmentHistoryId();
    }

    @Override
    public List<EquipmentHistory> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        EquipmentHistoryDAO sgiDAO = new EquipmentHistoryDAO();
        if (f == 0) {
            first = 0;
        }
        equipmentHistory.setEquipmentId(equipmentId);
        datasource = sgiDAO.searchByEquipmentId(equipmentHistory, first, pageSize);
        int dataSize = sgiDAO.searchSize(equipmentHistory).intValue();
        this.setRowCount(dataSize);
        f = -1;
        return datasource;
    }


}
