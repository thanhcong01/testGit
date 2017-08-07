/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bo;

import com.ftu.inventory.bo.EtagDetail;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
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
@Table(name = "GOODS_STATUS_TRANS_SERIAL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GoodsStatusTransSerial.findAll", query = "SELECT g FROM GoodsStatusTransSerial g"),
    @NamedQuery(name = "GoodsStatusTransSerial.findByGoodsStatusTransSerialId", query = "SELECT g FROM GoodsStatusTransSerial g WHERE g.goodsStatusTransSerialId = :goodsStatusTransSerialId"),
    @NamedQuery(name = "GoodsStatusTransSerial.findBySerial", query = "SELECT g FROM GoodsStatusTransSerial g WHERE g.serial = :serial"),
    @NamedQuery(name = "GoodsStatusTransSerial.findByOldGoodsStatus", query = "SELECT g FROM GoodsStatusTransSerial g WHERE g.oldGoodsStatus = :oldGoodsStatus"),
    @NamedQuery(name = "GoodsStatusTransSerial.findByNewGoodsStatus", query = "SELECT g FROM GoodsStatusTransSerial g WHERE g.newGoodsStatus = :newGoodsStatus"),
    @NamedQuery(name = "GoodsStatusTransSerial.findByGoodsStatusTransId", query = "SELECT g FROM GoodsStatusTransSerial g WHERE g.goodsStatusTransId = :goodsStatusTransId")})
public class GoodsStatusTransSerial implements Serializable {

    @Column(name = "OLD_GOODS_STATUS")
    private Long oldGoodsStatus;
    @Column(name = "NEW_GOODS_STATUS")
    private Long newGoodsStatus;
    @Column(name = "GOODS_STATUS_TRANS_ID")
    private Long goodsStatusTransId;
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @SequenceGenerator(name = "GOODS_STATUS_TRANS_SERIAL_SEQ", sequenceName = "GOODS_STATUS_TRANS_SERIAL_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "GOODS_STATUS_TRANS_SERIAL_SEQ")
    @Basic(optional = false)
    @Column(name = "GOODS_STATUS_TRANS_SERIAL_ID")
    private Long goodsStatusTransSerialId;
    @Column(name = "SERIAL")
    private String serial;
    @Column(name = "GOODS_ID")
    private Long goodsId;
    @Column(name = "PROVIDER_ID")
    private Long providerId;
    @Column(name = "TRANS_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transDatetime;
    @Transient
    private GoodsStatusTrans goodsTrans;
    @Transient
    private String oldStatus;
    @Transient
    private String newStatus;

    public GoodsStatusTransSerial() {
    }

    public GoodsStatusTransSerial(Long oStatus, Long nStatus, Long goodsId, Long pId, String serial, Long gstId) {
        this.oldGoodsStatus = oStatus;
        this.newGoodsStatus = nStatus;
        this.goodsId = goodsId;
        this.providerId = pId;
        this.serial = serial;
        this.goodsStatusTransId = gstId;
    }

    public GoodsStatusTransSerial(Long goodsStatusTransSerialId) {
        this.goodsStatusTransSerialId = goodsStatusTransSerialId;
    }

    public Long getGoodsStatusTransSerialId() {
        return goodsStatusTransSerialId;
    }

    public void setGoodsStatusTransSerialId(Long goodsStatusTransSerialId) {
        this.goodsStatusTransSerialId = goodsStatusTransSerialId;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Long getOldGoodsStatus() {
        return oldGoodsStatus;
    }

    public void setOldGoodsStatus(Long oldGoodsStatus) {
        this.oldGoodsStatus = oldGoodsStatus;
    }

    public Long getNewGoodsStatus() {
        return newGoodsStatus;
    }

    public void setNewGoodsStatus(Long newGoodsStatus) {
        this.newGoodsStatus = newGoodsStatus;
    }

    public Long getGoodsStatusTransId() {
        return goodsStatusTransId;
    }

    public void setGoodsStatusTransId(Long goodsStatusTransId) {
        this.goodsStatusTransId = goodsStatusTransId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (goodsStatusTransSerialId != null ? goodsStatusTransSerialId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GoodsStatusTransSerial)) {
            return false;
        }
        GoodsStatusTransSerial other = (GoodsStatusTransSerial) object;
        if ((this.goodsStatusTransSerialId == null && other.goodsStatusTransSerialId != null) || (this.goodsStatusTransSerialId != null && !this.goodsStatusTransSerialId.equals(other.goodsStatusTransSerialId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "core.Hibernate.BO.Inventory.GoodsStatusTransSerial[ goodsStatusTransSerialId=" + goodsStatusTransSerialId + " ]";
    }

    /**
     * @return the providerId
     */
    public Long getProviderId() {
        return providerId;
    }

    /**
     * @param providerId the providerId to set
     */
    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    /**
     * @return the goodsTrans
     */
    public GoodsStatusTrans getGoodsTrans() {
        if (goodsTrans == null) {
            goodsTrans = new GoodsStatusTrans();
        }
        return goodsTrans;
    }

    /**
     * @param goodsTrans the goodsTrans to set
     */
    public void setGoodsTrans(GoodsStatusTrans goodsTrans) {
        this.goodsTrans = goodsTrans;
    }

    /**
     * @return the newStatus
     */
    public String getNewStatus() {
        return newStatus;
    }

    /**
     * @param newStatus the newStatus to set
     */
    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    /**
     * @return the oldStatus
     */
    public String getOldStatus() {
        return oldStatus;
    }

    /**
     * @param oldStatus the oldStatus to set
     */
    public void setOldStatus(String oldStatus) {
        this.oldStatus = oldStatus;
    }

    /**
     * @return the transDatetime
     */
    public Date getTransDatetime() {
        return transDatetime;
    }

    /**
     * @param transDatetime the transDatetime to set
     */
    public void setTransDatetime(Date transDatetime) {
        this.transDatetime = transDatetime;
    }

}
