/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ftu.inventory.bo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DuongThanhCong
 */
@Entity
@Table(name = "STOCK_GOODS_INV_NOSERIAL")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "StockGoodsInvNoSerial.findAll", query = "SELECT s FROM StockGoodsInvNoSerial s"),
        @NamedQuery(name = "StockGoodsInvNoSerial.findById", query = "SELECT s FROM StockGoodsInvNoSerial s WHERE s.id = :id"),
        @NamedQuery(name = "StockGoodsInvNoSerial.findByShopId", query = "SELECT s FROM StockGoodsInvNoSerial s WHERE s.shopId = :shopId"),
        @NamedQuery(name = "StockGoodsInvNoSerial.findByEquipmentProfileId", query = "SELECT s FROM StockGoodsInvNoSerial s WHERE s.equipmentProfileId = :equipmentProfileId"),
        @NamedQuery(name = "StockGoodsInvNoSerial.findByQuantity", query = "SELECT s FROM StockGoodsInvNoSerial s WHERE s.quantity = :quantity"),
        @NamedQuery(name = "StockGoodsInvNoSerial.findByEquipmentStatus", query = "SELECT s FROM StockGoodsInvNoSerial s WHERE s.equipmentStatus = :equipmentStatus"),
        @NamedQuery(name = "StockGoodsInvNoSerial.findByStockStatus", query = "SELECT s FROM StockGoodsInvNoSerial s WHERE s.stockStatus = :stockStatus"),
        @NamedQuery(name = "StockGoodsInvNoSerial.findByProviderId", query = "SELECT s FROM StockGoodsInvNoSerial s WHERE s.providerId = :providerId"),
        @NamedQuery(name = "StockGoodsInvNoSerial.findByInstockDatetime", query = "SELECT s FROM StockGoodsInvNoSerial s WHERE s.instockDatetime = :instockDatetime"),
        @NamedQuery(name = "StockGoodsInvNoSerial.findByOutstockDatetime", query = "SELECT s FROM StockGoodsInvNoSerial s WHERE s.outstockDatetime = :outstockDatetime"),
        @NamedQuery(name = "StockGoodsInvNoSerial.findByCurrentTaId", query = "SELECT s FROM StockGoodsInvNoSerial s WHERE s.currentTaId = :currentTaId"),
        @NamedQuery(name = "StockGoodsInvNoSerial.findByStaffId", query = "SELECT s FROM StockGoodsInvNoSerial s WHERE s.staffId = :staffId"),
        @NamedQuery(name = "StockGoodsInvNoSerial.findBySerialSearch", query = "SELECT s FROM StockGoodsInvNoSerial s WHERE s.serialSearch = :serialSearch")})
public class StockGoodsInvNoSerial implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @SequenceGenerator(name = "STOCK_GOODS_INV_SERIAL_SEQ", sequenceName = "STOCK_GOODS_INV_SERIAL_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "STOCK_GOODS_INV_SERIAL_SEQ")
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "SHOP_ID")
    private Long shopId;
    @Column(name = "EQUIPMENT_PROFILE_ID")
    private Long equipmentProfileId;
    @Column(name = "QUANTITY")
    private Long quantity;
    @Column(name = "EQUIPMENT_STATUS")
    private Long equipmentStatus;
    @Column(name = "STOCK_STATUS")
    private String stockStatus;
    @Column(name = "PROVIDER_ID")
    private Long providerId;
    @Column(name = "INSTOCK_DATETIME")
    @Temporal(TemporalType.DATE)
    private Date instockDatetime;
    @Column(name = "OUTSTOCK_DATETIME")
    @Temporal(TemporalType.DATE)
    private Date outstockDatetime;
    @Column(name = "CURRENT_TA_ID")
    private Long currentTaId;
    @Column(name = "STAFF_ID")
    private Long staffId;
    @Column(name = "SERIAL_SEARCH")
    private String serialSearch;
    @Column(name = "STAFF_NAME")
    private String staffName;
    public StockGoodsInvNoSerial() {
    }


    public StockGoodsInvNoSerial(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getEquipmentProfileId() {
        return equipmentProfileId;
    }

    public void setEquipmentProfileId(Long equipmentProfileId) {
        this.equipmentProfileId = equipmentProfileId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getEquipmentStatus() {
        return equipmentStatus;
    }

    public void setEquipmentStatus(Long equipmentStatus) {
        this.equipmentStatus = equipmentStatus;
    }

    public String getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(String stockStatus) {
        this.stockStatus = stockStatus;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public Date getInstockDatetime() {
        return instockDatetime;
    }

    public void setInstockDatetime(Date instockDatetime) {
        this.instockDatetime = instockDatetime;
    }

    public Date getOutstockDatetime() {
        return outstockDatetime;
    }

    public void setOutstockDatetime(Date outstockDatetime) {
        this.outstockDatetime = outstockDatetime;
    }

    public Long getCurrentTaId() {
        return currentTaId;
    }

    public void setCurrentTaId(Long currentTaId) {
        this.currentTaId = currentTaId;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getSerialSearch() {
        return serialSearch;
    }

    public void setSerialSearch(String serialSearch) {
        this.serialSearch = serialSearch;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockGoodsInvNoSerial)) {
            return false;
        }
        StockGoodsInvNoSerial other = (StockGoodsInvNoSerial) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.StockGoodsInvNoSerial[ id=" + id + " ]";
    }

    public StockGoodsInvNoSerial(Long shopId, Long equipmentProfileId, Long quantity, Long equipmentStatus,
                                 String stockStatus, Long providerId, Date instockDatetime, Long currentTaId) {
        this.shopId = shopId;
        this.equipmentProfileId = equipmentProfileId;
        this.quantity = quantity;
        this.equipmentStatus = equipmentStatus;
        this.stockStatus = stockStatus;
        this.providerId = providerId;
        this.instockDatetime = instockDatetime;
        this.currentTaId = currentTaId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
}
