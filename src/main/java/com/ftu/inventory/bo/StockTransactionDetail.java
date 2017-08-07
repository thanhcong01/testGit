/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bo;

import com.ftu.inventory.dto.StockGoodsInvSerialDTO;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
@Table(name = "STOCK_TRANSACTION_DETAIL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StockTransactionDetail.findAll", query = "SELECT s FROM StockTransactionDetail s"),
    @NamedQuery(name = "StockTransactionDetail.findByStockTransactionDetailId", query = "SELECT s FROM StockTransactionDetail s WHERE s.stockTransactionDetailId = :stockTransactionDetailId"),
    @NamedQuery(name = "StockTransactionDetail.findByQuantity", query = "SELECT s FROM StockTransactionDetail s WHERE s.quantity = :quantity"),
    @NamedQuery(name = "StockTransactionDetail.findByStdDatetime", query = "SELECT s FROM StockTransactionDetail s WHERE s.stdDatetime = :stdDatetime")})
public class StockTransactionDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @SequenceGenerator(name = "STOCK_TRANSACTION_DETAIL_SEQ", sequenceName = "STOCK_TRANSACTION_DETAIL_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "STOCK_TRANSACTION_DETAIL_SEQ")
    @Basic(optional = false)
    @Column(name = "STOCK_TRANSACTION_DETAIL_ID")
    private Long stockTransactionDetailId;
    @Column(name = "QUANTITY")
    private Long quantity;
    @Column(name = "STD_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date stdDatetime;
    @Column(name = "STOCK_TRANSACTION_ID")
    private Long stockTransactionId;
    @Column(name = "GOODS_ID")
    private Long goodsId;

    @Column(name = "GOODS_STATUS")
    private Long goodsStatus;
    @Column(name = "DESCRIPTION")
    private String description;
    @Transient
    private List<StockTransactionSerial> lsSerial;
    @Transient
    private List<StockGoodsInvSerialDTO> setSerial;
    @Transient
    private String referenCode;
    @Transient
    private String goodsName;
    @Transient
    private String goodsGroupName;
    @Transient
    private String ordercode;
    @Transient
    private String goodsStatusName;
    @Transient
    private Long rowkey;
    @Transient
    private String goodsCode;
    @Transient
    private int setSize;
    @Transient
    private int index;
    @Transient
    private String provider;
    @Transient
    private Long providerId;
    @Transient
    private String unitType;
    @Transient
    private Long wanranprio;
    @Transient
    private Date wanranExpriDate;
    @Transient
    private String wanranExpriDateStr;
    @Transient
    private String countCo;
    @Transient
    private String countCq;
    @Transient
    private Long maintancePeriod;
    @Transient
    private Long udTaId;
    @Transient
    private Long upStaDetailId;
    @Transient
    private Long upStaSerial ;
    @Transient
    private Long udStaId;
    @Transient
    private String profileManagementType;
    @Transient
    private String serial;
//    private Long equipType;

    public StockTransactionDetail() {
    }

    public StockTransactionDetail(Long quan, Long goodsId, Long gstatus) {
        this.quantity = quan;
        this.goodsId = goodsId;
        this.stdDatetime = new Date();
        this.goodsStatus = gstatus;
    }

    public StockTransactionDetail(Long stockTransactionDetailId) {
        this.stockTransactionDetailId = stockTransactionDetailId;
    }

    public Long getStockTransactionDetailId() {
        return stockTransactionDetailId;
    }

    public void setStockTransactionDetailId(Long stockTransactionDetailId) {
        this.stockTransactionDetailId = stockTransactionDetailId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Date getStdDatetime() {
        return stdDatetime;
    }

    public void setStdDatetime(Date stdDatetime) {
        this.stdDatetime = stdDatetime;
    }

    public Long getStockTransactionId() {
        return stockTransactionId;
    }

    public void setStockTransactionId(Long stockTransactionId) {
        this.stockTransactionId = stockTransactionId;
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
        hash += (stockTransactionDetailId != null ? stockTransactionDetailId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockTransactionDetail)) {
            return false;
        }
        StockTransactionDetail other = (StockTransactionDetail) object;
        if ((this.stockTransactionDetailId == null && other.stockTransactionDetailId != null) || (this.stockTransactionDetailId != null && !this.stockTransactionDetailId.equals(other.stockTransactionDetailId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "core.Hibernate.BO.Inventory.StockTransactionDetail[ stockTransactionDetailId=" + stockTransactionDetailId + " ]";
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

    /**
     * @return the lsSerial
     */
    public List<StockTransactionSerial> getLsSerial() {
        if (lsSerial == null) {
            lsSerial = new ArrayList<>();
        }
        return lsSerial;
    }

    /**
     * @param lsSerial the lsSerial to set
     */
    public void setLsSerial(List<StockTransactionSerial> lsSerial) {
        this.lsSerial = lsSerial;
    }

    /**
     * @return the goodsName
     */
    public String getGoodsName() {
        return goodsName;
    }

    /**
     * @param goodsName the goodsName to set
     */
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    /**
     * @return the ordercode
     */
    public String getOrdercode() {
        return ordercode;
    }

    /**
     * @param ordercode the ordercode to set
     */
    public void setOrdercode(String ordercode) {
        this.ordercode = ordercode;
    }

    /**
     * @return the goodsStatusName
     */
    public String getGoodsStatusName() {
        return goodsStatusName;
    }

    /**
     * @param goodsStatusName the goodsStatusName to set
     */
    public void setGoodsStatusName(String goodsStatusName) {
        this.goodsStatusName = goodsStatusName;
    }

    /**
     * @return the rowkey
     */
    public Long getRowkey() {
        if(goodsId != null && providerId != null && goodsStatus!=null ){
            return (goodsId + providerId+goodsStatus);
        }
        if(stockTransactionDetailId != null){
            return stockTransactionDetailId;
        }
        if(goodsId != null && providerId != null ){
            return (goodsId + providerId);
        }
        if(goodsId!=null){
            return goodsId;
        }
        rowkey = -1l;
//        rowkey = stockTransactionDetailId != null ? stockTransactionDetailId : (goodsId != null && providerId != null ? (goodsId + providerId) : -1);
        return rowkey;
    }

    /**
     * @return the rowkey
     */
    public String getRowkeyString() {
        return (goodsId + "-"+ providerId+ "-"+ goodsStatus);
    }
    /**
     * @param rowkey the rowkey to set
     */
    public void setRowkey(Long rowkey) {
        this.rowkey = rowkey;
    }

    /**
     * @return the setSerial
     */
    public List<StockGoodsInvSerialDTO> getSetSerial() {
        if (setSerial == null) {
            setSerial = new ArrayList<>();
        }
        return setSerial;
    }

    /**
     * @param setSerial the setSerial to set
     */
    public void setSetSerial(List<StockGoodsInvSerialDTO> setSerial) {
        this.setSerial = setSerial;
    }

    /**
     * @return the goodsGroupName
     */
    public String getGoodsGroupName() {
        return goodsGroupName;
    }

    /**
     * @param goodsGroupName the goodsGroupName to set
     */
    public void setGoodsGroupName(String goodsGroupName) {
        this.goodsGroupName = goodsGroupName;
    }

    /**
     * @return the goodsCode
     */
    public String getGoodsCode() {
        return goodsCode;
    }

    /**
     * @param goodsCode the goodsCode to set
     */
    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    /**
     * @return the setSize
     */
    public int getSetSize() {
        return setSize;
    }

    /**
     * @param setSize the setSize to set
     */
    public void setSetSize(int setSize) {
        this.setSize = setSize;
    }

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public Long getWanranprio() {
        return wanranprio;
    }

    public void setWanranprio(Long wanranprio) {
        this.wanranprio = wanranprio;
    }

    public Date getWanranExpriDate() {
        return wanranExpriDate;
    }

    public void setWanranExpriDate(Date wanranExpriDate) {
        this.wanranExpriDate = wanranExpriDate;
    }

    public String getWanranExpriDateStr() {
        if(wanranExpriDate!=null){
            return new SimpleDateFormat("dd/MM/yyyy").format(wanranExpriDate);
        }
        return wanranExpriDateStr;
    }

    public void setWanranExpriDateStr(String wanranExpriDateStr) {
        this.wanranExpriDateStr = wanranExpriDateStr;
    }

    public String getCountCo() {
        return countCo;
    }

    public void setCountCo(String countCo) {
        this.countCo = countCo;
    }

    public String getCountCq() {
        return countCq;
    }

    public void setCountCq(String countCq) {
        this.countCq = countCq;
    }

    public Long getMaintancePeriod() {
        return maintancePeriod;
    }

    public void setMaintancePeriod(Long maintancePeriod) {
        this.maintancePeriod = maintancePeriod;
    }

    public String getReferenCode() {
        return referenCode;
    }

    public void setReferenCode(String referenCode) {
        this.referenCode = referenCode;
    }

    public Long getUdTaId() {
        return udTaId;
    }

    public void setUdTaId(Long udTaId) {
        this.udTaId = udTaId;
    }

    public Long getUpStaDetailId() {
        return upStaDetailId;
    }

    public void setUpStaDetailId(Long upStaDetailId) {
        this.upStaDetailId = upStaDetailId;
    }

    public Long getUpStaSerial() {
        return upStaSerial;
    }

    public void setUpStaSerial(Long upStaSerial) {
        this.upStaSerial = upStaSerial;
    }

    public Long getUdStaId() {
        return udStaId;
    }

    public void setUdStaId(Long udStaId) {
        this.udStaId = udStaId;
    }

    public String getProfileManagementType() {
        return profileManagementType;
    }

    public void setProfileManagementType(String profileManagementType) {
        this.profileManagementType = profileManagementType;
    }

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public StockTransactionDetail(StockTransactionDetail stockTransactionDetail) {
	    if (stockTransactionDetail==null) return;
	    this.stockTransactionDetailId = stockTransactionDetail.stockTransactionDetailId;
        this.quantity = stockTransactionDetail.quantity;
        this.stdDatetime = stockTransactionDetail.stdDatetime;
        this.stockTransactionId = stockTransactionDetail.stockTransactionId;
        this.goodsId = stockTransactionDetail.goodsId;
        this.goodsStatus = stockTransactionDetail.goodsStatus;
        this.description = stockTransactionDetail.description;
        this.lsSerial = stockTransactionDetail.lsSerial;
        this.setSerial = stockTransactionDetail.setSerial;
        this.referenCode = stockTransactionDetail.referenCode;
        this.goodsName = stockTransactionDetail.goodsName;
        this.goodsGroupName = stockTransactionDetail.goodsGroupName;
        this.ordercode = stockTransactionDetail.ordercode;
        this.goodsStatusName = stockTransactionDetail.goodsStatusName;
        this.rowkey = stockTransactionDetail.rowkey;
        this.goodsCode = stockTransactionDetail.goodsCode;
        this.setSize = stockTransactionDetail.setSize;
        this.index = stockTransactionDetail.index;
        this.provider = stockTransactionDetail.provider;
        this.providerId = stockTransactionDetail.providerId;
        this.unitType = stockTransactionDetail.unitType;
        this.wanranprio = stockTransactionDetail.wanranprio;
        this.wanranExpriDate = stockTransactionDetail.wanranExpriDate;
        this.wanranExpriDateStr = stockTransactionDetail.wanranExpriDateStr;
        this.countCo = stockTransactionDetail.countCo;
        this.countCq = stockTransactionDetail.countCq;
        this.maintancePeriod = stockTransactionDetail.maintancePeriod;
        this.udTaId = stockTransactionDetail.udTaId;
        this.upStaDetailId = stockTransactionDetail.upStaDetailId;
        this.upStaSerial = stockTransactionDetail.upStaSerial;
        this.udStaId = stockTransactionDetail.udStaId;
        this.profileManagementType = stockTransactionDetail.profileManagementType;
        this.serial = stockTransactionDetail.serial;
    }
}
