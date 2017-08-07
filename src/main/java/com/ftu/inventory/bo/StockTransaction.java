/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author E5420
 */
@Entity
@Table(name = "STOCK_TRANSACTION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StockTransaction.findAll", query = "SELECT s FROM StockTransaction s"),
    @NamedQuery(name = "StockTransaction.findByStockTransactionDatetime", query = "SELECT s FROM StockTransaction s WHERE s.stockTransactionDatetime = :stockTransactionDatetime"),
    @NamedQuery(name = "StockTransaction.findByExportStaffId", query = "SELECT s FROM StockTransaction s WHERE s.exportStaffId = :exportStaffId"),
    @NamedQuery(name = "StockTransaction.findByFromShopId", query = "SELECT s FROM StockTransaction s WHERE s.fromShopId = :fromShopId"),
    @NamedQuery(name = "StockTransaction.findByImportStaffId", query = "SELECT s FROM StockTransaction s WHERE s.importStaffId = :importStaffId"),
    @NamedQuery(name = "StockTransaction.findByToShopId", query = "SELECT s FROM StockTransaction s WHERE s.toShopId = :toShopId"),
    @NamedQuery(name = "StockTransaction.findByStockTransactionType", query = "SELECT s FROM StockTransaction s WHERE s.stockTransactionType = :stockTransactionType"),
    @NamedQuery(name = "StockTransaction.findByStockTransStatus", query = "SELECT s FROM StockTransaction s WHERE s.stockTransStatus = :stockTransStatus"),
    @NamedQuery(name = "StockTransaction.findByDeliveryType", query = "SELECT s FROM StockTransaction s WHERE s.deliveryType = :deliveryType"),
    @NamedQuery(name = "StockTransaction.findByFinishDatetime", query = "SELECT s FROM StockTransaction s WHERE s.finishDatetime = :finishDatetime"),
    @NamedQuery(name = "StockTransaction.findByStockTransactionId", query = "SELECT s FROM StockTransaction s WHERE s.stockTransactionId = :stockTransactionId")})
public class StockTransaction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "STOCK_TRANSACTION_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date stockTransactionDatetime;
    @Column(name = "EXPORT_STAFF_ID")
    private Long exportStaffId;
    @Column(name = "FROM_SHOP_ID")
    private Long fromShopId;
    @Column(name = "IMPORT_STAFF_ID")
    private Long importStaffId;
    @Column(name = "TO_SHOP_ID")
    private Long toShopId;
    @Column(name = "STOCK_TRANSACTION_TYPE")
    private String stockTransactionType;
    @Column(name = "STOCK_TRANS_STATUS")
    private String stockTransStatus;
    @Column(name = "DELIVERY_TYPE")
    private String deliveryType;
    @Column(name = "FINISH_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date finishDatetime;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @SequenceGenerator(name = "STOCK_TRANSACTION_SEQ", sequenceName = "STOCK_TRANSACTION_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "STOCK_TRANSACTION_SEQ")
    @Basic(optional = false)
    @Column(name = "STOCK_TRANSACTION_ID")
    private Long stockTransactionId;
    @Column(name = "TRANSACTION_ACTION_ID")
    private Long transactionActionId;
    @Transient
    private List<StockTransactionDetail> lsDetail;

    public StockTransaction() {
        stockTransactionDatetime = new Date();
    }

    public StockTransaction(Long exStaffId, Long exShopId, Long imStaffId, Long imShopId, String type, String status, String detype) {
        this.deliveryType = detype;
        this.exportStaffId = exStaffId;
        this.finishDatetime = new Date();
        this.fromShopId = exShopId;
        this.importStaffId = imStaffId;
        this.stockTransStatus = status;
        this.stockTransactionDatetime = new Date();
        this.stockTransactionType = type;
        this.toShopId = imShopId;
    }

    public StockTransaction(Long stockTransactionId) {
        this.stockTransactionId = stockTransactionId;
    }

    public Date getStockTransactionDatetime() {
        return stockTransactionDatetime;
    }

    public void setStockTransactionDatetime(Date stockTransactionDatetime) {
        this.stockTransactionDatetime = stockTransactionDatetime;
    }

    public Long getExportStaffId() {
        return exportStaffId;
    }

    public void setExportStaffId(Long exportStaffId) {
        this.exportStaffId = exportStaffId;
    }

    public Long getFromShopId() {
        return fromShopId;
    }

    public void setFromShopId(Long fromShopId) {
        this.fromShopId = fromShopId;
    }

    public Long getImportStaffId() {
        return importStaffId;
    }

    public void setImportStaffId(Long importStaffId) {
        this.importStaffId = importStaffId;
    }

    public Long getToShopId() {
        return toShopId;
    }

    public void setToShopId(Long toShopId) {
        this.toShopId = toShopId;
    }

    public String getStockTransactionType() {
        return stockTransactionType;
    }

    public void setStockTransactionType(String stockTransactionType) {
        this.stockTransactionType = stockTransactionType;
    }

    public String getStockTransStatus() {
        return stockTransStatus;
    }

    public void setStockTransStatus(String stockTransStatus) {
        this.stockTransStatus = stockTransStatus;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public Date getFinishDatetime() {
        return finishDatetime;
    }

    public void setFinishDatetime(Date finishDatetime) {
        this.finishDatetime = finishDatetime;
    }

    public Long getStockTransactionId() {
        return stockTransactionId;
    }

    public void setStockTransactionId(Long stockTransactionId) {
        this.stockTransactionId = stockTransactionId;
    }

    public Long getTransactionActionId() {
        return transactionActionId;
    }

    public void setTransactionActionId(Long transactionActionId) {
        this.transactionActionId = transactionActionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockTransactionId != null ? stockTransactionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockTransaction)) {
            return false;
        }
        StockTransaction other = (StockTransaction) object;
        if ((this.stockTransactionId == null && other.stockTransactionId != null) || (this.stockTransactionId != null && !this.stockTransactionId.equals(other.stockTransactionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "core.Hibernate.BO.Inventory.StockTransaction[ stockTransactionId=" + stockTransactionId + " ]";
    }

    /**
     * @return the lsDetail
     */
    public List<StockTransactionDetail> getLsDetail() {
        if (lsDetail == null) {
            lsDetail = new ArrayList<>();
        }
        return lsDetail;
    }

    /**
     * @param lsDetail the lsDetail to set
     */
    public void setLsDetail(List<StockTransactionDetail> lsDetail) {
        this.lsDetail = lsDetail;
    }

}
