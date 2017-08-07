/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "GOODS_STATUS_TRANS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GoodsStatusTrans.findAll", query = "SELECT g FROM GoodsStatusTrans g"),
    @NamedQuery(name = "GoodsStatusTrans.findByGoodsStatusTransId", query = "SELECT g FROM GoodsStatusTrans g WHERE g.goodsStatusTransId = :goodsStatusTransId"),
    @NamedQuery(name = "GoodsStatusTrans.findByTransDatetime", query = "SELECT g FROM GoodsStatusTrans g WHERE g.transDatetime = :transDatetime"),
    @NamedQuery(name = "GoodsStatusTrans.findByReasonId", query = "SELECT g FROM GoodsStatusTrans g WHERE g.reasonId = :reasonId")})
public class GoodsStatusTrans implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @SequenceGenerator(name = "GOODS_STATUS_TRANS_SEQ", sequenceName = "GOODS_STATUS_TRANS_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "GOODS_STATUS_TRANS_SEQ")
    @Basic(optional = false)
    @Column(name = "GOODS_STATUS_TRANS_ID")
    private Long goodsStatusTransId;
    @Column(name = "TRANS_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transDatetime;
    @Column(name = "REASON_ID")
    private Long reasonId;
    @Column(name = "NOTE")
    private String note;
    @Column(name = "STAFF_ID")
    private Long staffId;
    @Column(name = "SHOP_ID")
    private Long shopId;
    @Transient
    private List<GoodsStatusTransSerial> listSerial;
    @Transient
    private String shopName;
    @Transient
    private String transDate;
    @Transient
    private String stockStatusTrans;
    @Transient
    private Long goodsStsTrans;
    @Transient
    private String reason;

    public GoodsStatusTrans() {
    }

    public GoodsStatusTrans(Long goodsStatusTransId) {
        this.goodsStatusTransId = goodsStatusTransId;
    }

    public Long getGoodsStatusTransId() {
        return goodsStatusTransId;
    }

    public void setGoodsStatusTransId(Long goodsStatusTransId) {
        this.goodsStatusTransId = goodsStatusTransId;
    }

    public Date getTransDatetime() {
        return transDatetime;
    }

    public void setTransDatetime(Date transDatetime) {
        this.transDatetime = transDatetime;
    }

    public Long getReasonId() {
        return reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (goodsStatusTransId != null ? goodsStatusTransId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GoodsStatusTrans)) {
            return false;
        }
        GoodsStatusTrans other = (GoodsStatusTrans) object;
        if ((this.goodsStatusTransId == null && other.goodsStatusTransId != null) || (this.goodsStatusTransId != null && !this.goodsStatusTransId.equals(other.goodsStatusTransId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "core.Hibernate.BO.Inventory.GoodsStatusTrans[ goodsStatusTransId=" + goodsStatusTransId + " ]";
    }

    /**
     * @return the listSerial
     */
    public List<GoodsStatusTransSerial> getListSerial() {
        return listSerial;
    }

    /**
     * @param listSerial the listSerial to set
     */
    public void setListSerial(List<GoodsStatusTransSerial> listSerial) {
        this.listSerial = listSerial;
    }

    public String getTransDate() {
        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
        if (transDatetime != null) {
            transDate = sf.format(transDatetime);
        }
        return transDate;
    }

    /**
     * @param transDate the transDate to set
     */
    public void setTransDate(String transDate) {

        this.transDate = transDate;
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
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @return the stockStatusTrans
     */
    public String getStockStatusTrans() {
        return stockStatusTrans;
    }

    /**
     * @param stockStatusTrans the stockStatusTrans to set
     */
    public void setStockStatusTrans(String stockStatusTrans) {
        this.stockStatusTrans = stockStatusTrans;
    }

    /**
     * @return the goodsStsTrans
     */
    public Long getGoodsStsTrans() {
        return goodsStsTrans;
    }

    /**
     * @param goodsStsTrans the goodsStsTrans to set
     */
    public void setGoodsStsTrans(Long goodsStsTrans) {
        this.goodsStsTrans = goodsStsTrans;
    }

    /**
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason the reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

}
