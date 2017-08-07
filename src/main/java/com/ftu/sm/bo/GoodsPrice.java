/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ftu.sm.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
@Table(name = "GOODS_PRICE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GoodsPrice.findAll", query = "SELECT g FROM GoodsPrice g"),
    @NamedQuery(name = "GoodsPrice.findByPriceId", query = "SELECT g FROM GoodsPrice g WHERE g.priceId = :priceId"),
    @NamedQuery(name = "GoodsPrice.findByGoodsId", query = "SELECT g FROM GoodsPrice g WHERE g.goodsId = :goodsId"),
    @NamedQuery(name = "GoodsPrice.findByPriceType", query = "SELECT g FROM GoodsPrice g WHERE g.priceType = :priceType"),
    @NamedQuery(name = "GoodsPrice.findByPrice", query = "SELECT g FROM GoodsPrice g WHERE g.price = :price"),
    @NamedQuery(name = "GoodsPrice.findByStaDate", query = "SELECT g FROM GoodsPrice g WHERE g.staDate = :staDate"),
    @NamedQuery(name = "GoodsPrice.findByEndDate", query = "SELECT g FROM GoodsPrice g WHERE g.endDate = :endDate"),
    @NamedQuery(name = "GoodsPrice.findByUsername", query = "SELECT g FROM GoodsPrice g WHERE g.username = :username"),
    @NamedQuery(name = "GoodsPrice.findByStatus", query = "SELECT g FROM GoodsPrice g WHERE g.status = :status"),
    @NamedQuery(name = "GoodsPrice.findByVat", query = "SELECT g FROM GoodsPrice g WHERE g.vat = :vat")})
public class GoodsPrice implements Serializable {
    private static final Long serialVersionUID = 1L;
    @SequenceGenerator(name = "GOODS_PRICE_SEQ", sequenceName = "GOODS_PRICE_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "GOODS_PRICE_SEQ")
    @Basic(optional = false)
    @Column(name = "PRICE_ID")
    private Long priceId;
    @Basic(optional = false)
    @Column(name = "GOODS_ID")
    private Long goodsId;
    @Basic(optional = false)
    @Column(name = "PRICE_TYPE")
    private String priceType;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PRICE")
    private Double price;
    @Basic(optional = false)
    @Column(name = "STA_DATE")
    @Temporal(TemporalType.DATE)
    private Date staDate;
    @Column(name = "END_DATE")
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Basic(optional = false)
    @Column(name = "USERNAME")
    private String username;
    @Basic(optional = false)
    @Column(name = "STATUS")
    private String status;
    @Column(name = "VAT")
    private Long vat;
    
    @Transient
    private List<Long> listId;
    
    @Transient
    private String goodsName;
    @Transient
    private Long goodsGroupId;

    public GoodsPrice() {
    }

    public GoodsPrice(Long priceId) {
        this.priceId = priceId;
    }

    public GoodsPrice(Long priceId, Long goodsId, String priceType, Date staDate, String username, String status) {
        this.priceId = priceId;
        this.goodsId = goodsId;
        this.priceType = priceType;
        this.staDate = staDate;
        this.username = username;
        this.status = status;
    }
    
    public List<Long> getListId() {
		return listId;
	}
    
    public void setListId(List<Long> listId) {
		this.listId = listId;
	}
    
    public Long getGoodsGroupId() {
		return goodsGroupId;
	}
    
    public void setGoodsGroupId(Long goodsGroupId) {
		this.goodsGroupId = goodsGroupId;
	}
    
    public String getGoodsName() {
		return goodsName;
	}
    
    public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

    public Long getPriceId() {
        return priceId;
    }

    public void setPriceId(Long priceId) {
        this.priceId = priceId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getStaDate() {
        return staDate;
    }

    public void setStaDate(Date staDate) {
        this.staDate = staDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getVat() {
        return vat;
    }

    public void setVat(Long vat) {
        this.vat = vat;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (priceId != null ? priceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GoodsPrice)) {
            return false;
        }
        GoodsPrice other = (GoodsPrice) object;
        if ((this.priceId == null && other.priceId != null) || (this.priceId != null && !this.priceId.equals(other.priceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ftu.sm.bo.GoodsPrice[ priceId=" + priceId + " ]";
    }
    
}
