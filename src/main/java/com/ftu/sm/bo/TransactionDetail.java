/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.sm.bo;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author E5420
 */
@Entity
@Table(name = "TRANSACTION_DETAIL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TransactionDetail.findAll", query = "SELECT t FROM TransactionDetail t"),
    @NamedQuery(name = "TransactionDetail.findByTransDetailId", query = "SELECT t FROM TransactionDetail t WHERE t.transDetailId = :transDetailId"),
    @NamedQuery(name = "TransactionDetail.findByTransId", query = "SELECT t FROM TransactionDetail t WHERE t.transId = :transId"),
    @NamedQuery(name = "TransactionDetail.findByGoodsId", query = "SELECT t FROM TransactionDetail t WHERE t.goodsId = :goodsId"),
    @NamedQuery(name = "TransactionDetail.findByStateId", query = "SELECT t FROM TransactionDetail t WHERE t.stateId = :stateId"),
    @NamedQuery(name = "TransactionDetail.findByQuantity", query = "SELECT t FROM TransactionDetail t WHERE t.quantity = :quantity"),
    @NamedQuery(name = "TransactionDetail.findByPriceId", query = "SELECT t FROM TransactionDetail t WHERE t.priceId = :priceId"),
    @NamedQuery(name = "TransactionDetail.findByDiscountId", query = "SELECT t FROM TransactionDetail t WHERE t.discountId = :discountId"),
    @NamedQuery(name = "TransactionDetail.findByPromotionId", query = "SELECT t FROM TransactionDetail t WHERE t.promotionId = :promotionId"),
    @NamedQuery(name = "TransactionDetail.findByCreateDatetime", query = "SELECT t FROM TransactionDetail t WHERE t.createDatetime = :createDatetime"),
    @NamedQuery(name = "TransactionDetail.findByPrice", query = "SELECT t FROM TransactionDetail t WHERE t.price = :price"),
    @NamedQuery(name = "TransactionDetail.findByDiscount", query = "SELECT t FROM TransactionDetail t WHERE t.discount = :discount"),
    @NamedQuery(name = "TransactionDetail.findByPromotion", query = "SELECT t FROM TransactionDetail t WHERE t.promotion = :promotion"),
    @NamedQuery(name = "TransactionDetail.findByGoodsStatus", query = "SELECT t FROM TransactionDetail t WHERE t.goodsStatus = :goodsStatus"),
    @NamedQuery(name = "TransactionDetail.findByAttachDetailId", query = "SELECT t FROM TransactionDetail t WHERE t.attachDetailId = :attachDetailId"),
    @NamedQuery(name = "TransactionDetail.findByShopId", query = "SELECT t FROM TransactionDetail t WHERE t.shopId = :shopId")})
public class TransactionDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @SequenceGenerator(name = "TRANSACTION_DETAIL_SEQ", sequenceName = "TRANSACTION_DETAIL_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "TRANSACTION_DETAIL_SEQ")
    @Basic(optional = false)
    @Column(name = "TRANS_DETAIL_ID")
    private Long transDetailId;
    @Basic(optional = false)
    @Column(name = "TRANS_ID")
    private long transId;
    @Basic(optional = false)
    @Column(name = "GOODS_ID")
    private long goodsId;
    @Basic(optional = false)
    @Column(name = "STATE_ID")
    private long stateId;
    @Basic(optional = false)
    @Column(name = "QUANTITY")
    private long quantity;
    @Basic(optional = false)
    @Column(name = "PRICE_ID")
    private long priceId;
    @Column(name = "DISCOUNT_ID")
    private Long discountId;
    @Column(name = "PROMOTION_ID")
    private Long promotionId;
    @Basic(optional = false)
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.DATE)
    private Date createDatetime;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PRICE")
    private Double price;
    @Column(name = "DISCOUNT")
    private Long discount;
    @Column(name = "PROMOTION")
    private Long promotion;
    @Column(name = "GOODS_STATUS")
    private Long goodsStatus;
    @Column(name = "ATTACH_DETAIL_ID")
    private Long attachDetailId;
    @Column(name = "SHOP_ID")
    private Long shopId;

    public TransactionDetail() {
    }

    public TransactionDetail(Long transDetailId) {
        this.transDetailId = transDetailId;
    }

    public TransactionDetail(Long transDetailId, long transId, long goodsId, long stateId, long quantity, long priceId, Date createDatetime) {
        this.transDetailId = transDetailId;
        this.transId = transId;
        this.goodsId = goodsId;
        this.stateId = stateId;
        this.quantity = quantity;
        this.priceId = priceId;
        this.createDatetime = createDatetime;
    }

    public Long getTransDetailId() {
        return transDetailId;
    }

    public void setTransDetailId(Long transDetailId) {
        this.transDetailId = transDetailId;
    }

    public long getTransId() {
        return transId;
    }

    public void setTransId(long transId) {
        this.transId = transId;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public long getStateId() {
        return stateId;
    }

    public void setStateId(long stateId) {
        this.stateId = stateId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public long getPriceId() {
        return priceId;
    }

    public void setPriceId(long priceId) {
        this.priceId = priceId;
    }

    public Long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }

    public Long getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Long promotionId) {
        this.promotionId = promotionId;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getDiscount() {
        return discount;
    }

    public void setDiscount(Long discount) {
        this.discount = discount;
    }

    public Long getPromotion() {
        return promotion;
    }

    public void setPromotion(Long promotion) {
        this.promotion = promotion;
    }

    public Long getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(Long goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public Long getAttachDetailId() {
        return attachDetailId;
    }

    public void setAttachDetailId(Long attachDetailId) {
        this.attachDetailId = attachDetailId;
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
        hash += (transDetailId != null ? transDetailId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransactionDetail)) {
            return false;
        }
        TransactionDetail other = (TransactionDetail) object;
        if ((this.transDetailId == null && other.transDetailId != null) || (this.transDetailId != null && !this.transDetailId.equals(other.transDetailId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ftu.sm.bo.TransactionDetail[ transDetailId=" + transDetailId + " ]";
    }

}
