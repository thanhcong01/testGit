/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bo;

import com.ftu.java.bo.*;
import com.ftu.inventory.bo.GoodsStatusTransSerial;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author E5420
 */
@Entity
@Table(name = "ETAG_DETAIL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EtagDetail.findAll", query = "SELECT e FROM EtagDetail e"),
    @NamedQuery(name = "EtagDetail.findById", query = "SELECT e FROM EtagDetail e WHERE e.id = :id"),
    @NamedQuery(name = "EtagDetail.findBySerial", query = "SELECT e FROM EtagDetail e WHERE e.serial = :serial"),
    @NamedQuery(name = "EtagDetail.findByItc", query = "SELECT e FROM EtagDetail e WHERE e.itc = :itc"),
    @NamedQuery(name = "EtagDetail.findByPid", query = "SELECT e FROM EtagDetail e WHERE e.pid = :pid")})
public class EtagDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @SequenceGenerator(name = "ETAG_DETAIL_SEQ", sequenceName = "ETAG_DETAIL_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ETAG_DETAIL_SEQ")
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "SERIAL")
    private String serial;
    @Column(name = "ITC")
    private String itc;
    @Column(name = "PID")
    private String pid;
    @Column(name = "IM_TRANSACTION_ID")
    private Long imTransactionId;
    @Column(name = "PROVIDER_ID")
    private Long providerId;
    @Column(name = "ETAG_NUMBER")
    private String etagNumber;
    @Column(name = "GOODS_ID")
    private Long goodsId;
    @Column(name = "GOODS_STATUS")
    private Long goodsStatus;
    @Column(name = "SERIAL_SEARCH")
    private String serialSearch;
    

    public EtagDetail() {
    }

    public EtagDetail(String serial, String itc, String pid,Long prvId,String egNum,String serialSearch,Long gId,Long gsts) {
        this.serial = serial;
        this.serialSearch = serialSearch;
        this.itc = itc;
        this.pid = pid;
        this.providerId=prvId;
        this.etagNumber=egNum;
        this.goodsId=gId;
        this.goodsStatus=gsts;
    }

    public EtagDetail(Long id) {
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
        this.serial = serial;
    }

    public String getItc() {
        return itc;
    }

    public void setItc(String itc) {
        this.itc = itc;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
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
        if (!(object instanceof EtagDetail)) {
            return false;
        }
        EtagDetail other = (EtagDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ftu.java.bo.EtagDetail[ id=" + id + " ]";
    }

    /**
     * @return the imTransactionId
     */
    public Long getImTransactionId() {
        return imTransactionId;
    }

    /**
     * @param imTransactionId the imTransactionId to set
     */
    public void setImTransactionId(Long imTransactionId) {
        this.imTransactionId = imTransactionId;
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
     * @return the etagNumber
     */
    public String getEtagNumber() {
        return etagNumber;
    }

    /**
     * @param etagNumber the etagNumber to set
     */
    public void setEtagNumber(String etagNumber) {
        this.etagNumber = etagNumber;
    }

    /**
     * @return the goodsId
     */
    public Long getGoodsId() {
        return goodsId;
    }

    /**
     * @param goodsId the goodsId to set
     */
    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    /**
     * @return the goodsStatus
     */
    public Long getGoodsStatus() {
        return goodsStatus;
    }

    /**
     * @param goodsStatus the goodsStatus to set
     */
    public void setGoodsStatus(Long goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

	public String getSerialSearch() {
		return serialSearch;
	}

	public void setSerialSearch(String serialSearch) {
		this.serialSearch = serialSearch;
	}
}
