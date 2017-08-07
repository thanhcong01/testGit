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
@Table(name = "ACTION_GOODS_MAP")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ActionGoodsMap.findAll", query = "SELECT a FROM ActionGoodsMap a"),
    @NamedQuery(name = "ActionGoodsMap.findByActionGoodsMapId", query = "SELECT a FROM ActionGoodsMap a WHERE a.actionGoodsMapId = :actionGoodsMapId"),
    @NamedQuery(name = "ActionGoodsMap.findByActionCode", query = "SELECT a FROM ActionGoodsMap a WHERE a.actionCode = :actionCode"),
    @NamedQuery(name = "ActionGoodsMap.findByGoodsId", query = "SELECT a FROM ActionGoodsMap a WHERE a.goodsId = :goodsId"),
    @NamedQuery(name = "ActionGoodsMap.findByStartDate", query = "SELECT a FROM ActionGoodsMap a WHERE a.startDate = :startDate"),
    @NamedQuery(name = "ActionGoodsMap.findByEndDate", query = "SELECT a FROM ActionGoodsMap a WHERE a.endDate = :endDate"),
    @NamedQuery(name = "ActionGoodsMap.findByCreateDate", query = "SELECT a FROM ActionGoodsMap a WHERE a.createDate = :createDate"),
    @NamedQuery(name = "ActionGoodsMap.findByDescription", query = "SELECT a FROM ActionGoodsMap a WHERE a.description = :description"),
    @NamedQuery(name = "ActionGoodsMap.findByQuantity", query = "SELECT a FROM ActionGoodsMap a WHERE a.quantity = :quantity"),
    @NamedQuery(name = "ActionGoodsMap.findBySystemType", query = "SELECT a FROM ActionGoodsMap a WHERE a.systemType = :systemType"),
    @NamedQuery(name = "ActionGoodsMap.findByCommAmount", query = "SELECT a FROM ActionGoodsMap a WHERE a.commAmount = :commAmount"),
    @NamedQuery(name = "ActionGoodsMap.findByReasonId", query = "SELECT a FROM ActionGoodsMap a WHERE a.reasonId = :reasonId"),
    @NamedQuery(name = "ActionGoodsMap.findByPriceType", query = "SELECT a FROM ActionGoodsMap a WHERE a.priceType = :priceType")})
public class ActionGoodsMap implements Serializable {
    private static final long serialVersionUID = 1L;
    @SequenceGenerator(name = "ACTION_GOODS_MAP_SEQ", sequenceName = "ACTION_GOODS_MAP_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ACTION_GOODS_MAP_SEQ")
    @Basic(optional = false)
    @Column(name = "ACTION_GOODS_MAP_ID")
    private Long actionGoodsMapId;
    @Column(name = "ACTION_CODE")
    private String actionCode;
    @Column(name = "GOODS_ID")
    private Long goodsId;
    @Column(name = "START_DATE")
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Column(name = "END_DATE")
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.DATE)
    private Date createDate;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "QUANTITY")
    private Long  quantity;
    @Column(name = "SYSTEM_TYPE")
    private String systemType;
    @Column(name = "COMM_AMOUNT")
    private Long  commAmount;
    @Column(name = "REASON_ID")
    private String reasonId;
    @Column(name = "PRICE_TYPE")
    private String priceType;

    public ActionGoodsMap() {
    }

    public ActionGoodsMap(Long actionGoodsMapId) {
        this.actionGoodsMapId = actionGoodsMapId;
    }

    public Long getActionGoodsMapId() {
        return actionGoodsMapId;
    }

    public void setActionGoodsMapId(Long actionGoodsMapId) {
        this.actionGoodsMapId = actionGoodsMapId;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long  getQuantity() {
        return quantity;
    }

    public void setQuantity(Long  quantity) {
        this.quantity = quantity;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public Long  getCommAmount() {
        return commAmount;
    }

    public void setCommAmount(Long  commAmount) {
        this.commAmount = commAmount;
    }

    public String getReasonId() {
        return reasonId;
    }

    public void setReasonId(String reasonId) {
        this.reasonId = reasonId;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (actionGoodsMapId != null ? actionGoodsMapId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActionGoodsMap)) {
            return false;
        }
        ActionGoodsMap other = (ActionGoodsMap) object;
        if ((this.actionGoodsMapId == null && other.actionGoodsMapId != null) || (this.actionGoodsMapId != null && !this.actionGoodsMapId.equals(other.actionGoodsMapId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ftu.sm.bo.ActionGoodsMap[ actionGoodsMapId=" + actionGoodsMapId + " ]";
    }
    
}
