/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bo;

import com.ftu.constanst.InventoryConstanst;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author E5420
 */
@Entity
@Table(name = "STOCK_GOODS_INV_SERIAL")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "StockGoodsInvSerial.findAll", query = "SELECT s FROM StockGoodsInvSerial s"),
        @NamedQuery(name = "StockGoodsInvSerial.findById", query = "SELECT s FROM StockGoodsInvSerial s WHERE s.id = :id"),
        @NamedQuery(name = "StockGoodsInvSerial.findBySerial", query = "SELECT s FROM StockGoodsInvSerial s WHERE s.serial = :serial"),
        @NamedQuery(name = "StockGoodsInvSerial.findByGoodsStatus", query = "SELECT s FROM StockGoodsInvSerial s WHERE s.equipmentProfileStatus = :equipmentProfileStatus"),
        @NamedQuery(name = "StockGoodsInvSerial.findByStockStatus", query = "SELECT s FROM StockGoodsInvSerial s WHERE s.stockStatus = :stockStatus"),
        @NamedQuery(name = "StockGoodsInvSerial.findByInstockDatetime", query = "SELECT s FROM StockGoodsInvSerial s WHERE s.instockDatetime = :instockDatetime"),
        @NamedQuery(name = "StockGoodsInvSerial.findByOutstockDatetime", query = "SELECT s FROM StockGoodsInvSerial s WHERE s.outstockDatetime = :outstockDatetime")})
public class StockGoodsInvSerial implements Serializable {
    //    StockGoodsInvSerial
//    StockGoodsInvSerial
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @SequenceGenerator(name = "STOCK_GOODS_INV_SERIAL_SEQ", sequenceName = "STOCK_GOODS_INV_SERIAL_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "STOCK_GOODS_INV_SERIAL_SEQ")
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "SERIAL")
    private String serial;
    @Column(name = "EQUIPMENT_STATUS")
    private Long equipmentProfileStatus;
    @Column(name = "STOCK_STATUS")
    private String stockStatus;
    // TODO: đơij sau khi chuyển staffId sang DTO thì đổi tên etagNumber thành staffId
    @Column(name = "STAFF_ID")
    private Long etagNumber;
    @Column(name = "INSTOCK_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date instockDatetime;
    @Column(name = "OUTSTOCK_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date outstockDatetime;
    @Column(name = "SHOP_ID")
    private Long shopId;
    @Column(name = "PROVIDER_ID")
    private Long providerId;
    @Column(name = "EQUIPMENT_PROFILE_ID")
    private Long equipmentProfileId;
    @Column(name = "CURRENT_TA_ID")
    private Long currentTaId;
    @Column(name = "SERIAL_SEARCH")
    private String serialSearch;
    @Column(name = "STAFF_NAME")
    private String staffName;

    @Transient
    private String oldStatusName;
    @Transient
    private String statusName;
    @Transient
    private Long oldGoodsStatus;
    @Transient
    private String profileName;
    @Transient
    private String providerName;
    @Transient
    private String stockStatusName;
    @Transient
    private String shopName;
    @Transient
    private String profileCode;
    @Transient
    private Long index;
    @Transient
    private String goodsGroupName;
    @Transient
    private String strOutstockDatetime;
    @Transient
    private TransactionAction transactionAction;
    @Transient
    private Long countBlockSerial;


    public StockGoodsInvSerial() {
    }

    public StockGoodsInvSerial(String serial, Long gstatus, String stockStatus, Long shopId, Long prvId, Long gId) {
        this.serial = serial!=null? serial.trim():"";
        this.equipmentProfileStatus = gstatus;
        this.stockStatus = stockStatus;
        this.shopId = shopId;
        this.providerId = prvId;
        this.equipmentProfileId = gId;
    }

    public StockGoodsInvSerial(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial!=null?serial.trim():"";
    }

    public Long getEquipmentProfileStatus() {
        return equipmentProfileStatus;
    }

    public void setEquipmentProfileStatus(Long goodsStatus) {
        this.equipmentProfileStatus = goodsStatus;
    }

    public String getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(String stockStatus) {
        this.stockStatus = stockStatus;
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

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public Long getEquipmentProfileId() {
        return equipmentProfileId;
    }

    public void setEquipmentProfileId(Long goodsId) {
        this.equipmentProfileId = goodsId;
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
        if (!(object instanceof StockGoodsInvSerial)) {
            return false;
        }
        StockGoodsInvSerial other = (StockGoodsInvSerial) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "core.Hibernate.BO.Inventory.StockGoodsInvSerial[ id=" + id + " ]";
        //return serialSearch;
    }

    /**
     * @return the statusName
     */
    public String getStatusName() {
        //statusName = InventoryConstanst.GoodsStatus.NOMAL.equals(equipmentProfileStatus) ? "Hàng mới" : "Hàng hỏng";
        if (InventoryConstanst.GoodsStatus.NOMAL.equals(equipmentProfileStatus))
        	statusName = "Hàng mới";
		else if (InventoryConstanst.GoodsStatus.ERROR.equals(equipmentProfileStatus))
			statusName = "Hàng hỏng";
		else if (InventoryConstanst.GoodsStatus.USED.equals(equipmentProfileStatus))
			statusName = "Hàng đã sử dụng";
        return statusName;
    }

    /**
     * @param statusName the statusName to set
     */
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    /**
     * @return the oldGoodsStatus
     */
    public Long getOldGoodsStatus() {
        return oldGoodsStatus;
    }

    /**
     * @param oldGoodsStatus the oldGoodsStatus to set
     */
    public void setOldGoodsStatus(Long oldGoodsStatus) {
        this.oldGoodsStatus = oldGoodsStatus;
    }

    /**
     * @return the profileName
     */
    public String getProfileName() {
        return profileName;
    }

    /**
     * @param profileName the profileName to set
     */
    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    /**
     * @return the providerName
     */
    public String getProviderName() {
        return providerName;
    }

    /**
     * @param providerName the providerName to set
     */
    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    /**
     * @return the oldStatusName
     */
    public String getOldStatusName() {
        return oldStatusName;
    }

    /**
     * @param oldStatusName the oldStatusName to set
     */
    public void setOldStatusName(String oldStatusName) {
        this.oldStatusName = oldStatusName;
    }

    /**
     * @return the stockStatusName
     */



    /**
     * @param stockStatusName the stockStatusName to set
     */
    public void setStockStatusName(String stockStatusName) {
        this.stockStatusName = stockStatusName;
    }

    public String getStockStatusName() {
        return stockStatusName;
    }

    /**
     * @return the currentTaId
     */
    public Long getCurrentTaId() {
        return currentTaId;
    }

    /**
     * @param currentTaId the currentTaId to set
     */
    public void setCurrentTaId(Long currentTaId) {
        this.currentTaId = currentTaId;
    }

    /**
     * @return the shopName
     */
    public String getShopName() {
        return shopName;
    }

    /**
     * @param shopName the shopName to set
     */
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    /**
     * @return the etagNumber
     */

    public Long getEtagNumber() {
        return etagNumber;
    }

    /**
     * @param etagNumber the etagNumber to set
     */
    public void setEtagNumber(Long etagNumber) {
        this.etagNumber = etagNumber;
    }

    /**
     * @return the profileCode
     */
    public String getProfileCode() {
        return profileCode;
    }

    /**
     * @param profileCode the profileCode to set
     */
    public void setProfileCode(String profileCode) {
        this.profileCode = profileCode;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getGoodsGroupName() {
        return goodsGroupName;
    }

    public void setGoodsGroupName(String goodsGroupName) {
        this.goodsGroupName = goodsGroupName;
    }

    public String getStrOutstockDatetime() {
        return strOutstockDatetime;
    }

    public void setStrOutstockDatetime(String strOutstockDatetime) {
        this.strOutstockDatetime = strOutstockDatetime;
    }

    public TransactionAction getTransactionAction() {
        return transactionAction;
    }

    public void setTransactionAction(TransactionAction transactionAction) {
        this.transactionAction = transactionAction;
    }

    public Long getCountBlockSerial() {
        return countBlockSerial;
    }

    public void setCountBlockSerial(Long countBlockSerial) {
        this.countBlockSerial = countBlockSerial;
    }

    public String getSerialSearch() {
        return serialSearch;
    }

    public void setSerialSearch(String serialSearch) {
        this.serialSearch = serialSearch;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
}
