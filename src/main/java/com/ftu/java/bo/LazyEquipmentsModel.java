/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.java.bo;

import com.ftu.constanst.InventoryConstanst;
import com.ftu.inventory.bo.EquipmentsDetail;
import com.ftu.inventory.bo.EquipmentsProfile;
import com.ftu.inventory.dao.EquipmentDetailDAO;
import com.ftu.inventory.dao.EquipmentsProfileDAO;
import com.ftu.staff.bo.Shop;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author E5420
 */
public class LazyEquipmentsModel extends LazyDataModel<EquipmentsDetail> implements Serializable {

    private List<EquipmentsDetail> datasource;
    private List<Shop> listShop;
    private EquipmentsDetail equipmentsDetail;
    private Long goodsGroupId;
    String from;
    String to;
    int f = -1;

    public LazyEquipmentsModel(String from, String to, EquipmentsDetail equipmentsDetail, Long goodsGroupId, List<Shop> listShop) {
        this.listShop = listShop;
        this.equipmentsDetail = equipmentsDetail;
        if (from != null && from.length() >= 8)
        	this.from = from.substring(from.length()-8);
        if (to != null && to.length() >= 8)
        	this.to = to.substring(to.length()-8);
        this.goodsGroupId = goodsGroupId;
        f = 0;
    }

    private String getShopName(Long shopId) {
        if (listShop != null) {
            for (Shop d : listShop) {
                if (d.getShopId().equals(shopId)) {
                    return d.getShopName();
                }
            }
        }
        return "";
    }

    @Override
    public EquipmentsDetail getRowData(String rowKey) {
        if (datasource != null) {
            for (EquipmentsDetail g : datasource) {
                if (g.getId().toString().equals(rowKey)) {
                    return g;
                }
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(EquipmentsDetail g) {
        return g.getId();
    }

    @Override
    public List<EquipmentsDetail> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        EquipmentDetailDAO sgiDAO = new EquipmentDetailDAO();
        if (f == 0) {
            first = 0;
        }
        EquipmentsProfileDAO profileDAO = new EquipmentsProfileDAO();
        EquipmentsProfile profile = profileDAO.findByProfileId(equipmentsDetail.getEquipmentsProfileId()==null? 0L:equipmentsDetail.getEquipmentsProfileId());
        boolean desc;
        if(sortOrder.name().equals("DESCENDING")){
            desc = false;
        } else{
            desc = true;
        }
        if(profile==null || InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
            datasource = sgiDAO.search(equipmentsDetail, first, pageSize, sortField, desc);
            int dataSize = sgiDAO.searchSize(equipmentsDetail).intValue();
            this.setRowCount(dataSize);
        }else {
            datasource = sgiDAO.searchNoserial(equipmentsDetail, first, pageSize, sortField, desc);
            int dataSize = sgiDAO.searchSizeNoserial(equipmentsDetail).intValue();
            this.setRowCount(dataSize);

        }
        if (datasource != null) {
            for (EquipmentsDetail s : datasource) {
                s.setShopName(getShopName(s.getShopId()));
            }
        }
        f = -1;
        return datasource;
    }
}
