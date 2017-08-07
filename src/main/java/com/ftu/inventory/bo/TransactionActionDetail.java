/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bo;

import com.ftu.constanst.InventoryConstanst;
import com.ftu.inventory.dto.StockGoodsInvSerialDTO;

import java.io.Serializable;
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
@Table(name = "TRANSACTION_ACTION_DETAIL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TransactionActionDetail.findAll", query = "SELECT t FROM TransactionActionDetail t"),
    @NamedQuery(name = "TransactionActionDetail.findByQuantity", query = "SELECT t FROM TransactionActionDetail t WHERE t.quantity = :quantity"),
    @NamedQuery(name = "TransactionActionDetail.findByTransactionActionDetailId", query = "SELECT t FROM TransactionActionDetail t WHERE t.transactionActionDetailId = :transactionActionDetailId")})
public class TransactionActionDetail implements Serializable {

    private static long serialVersionUID = 1L;

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }
    @Column(name = "QUANTITY")
    private Long quantity;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @SequenceGenerator(name = "TRANSACTION_ACTION_DETAIL_SEQ", sequenceName = "TRANSACTION_ACTION_DETAIL_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "TRANSACTION_ACTION_DETAIL_SEQ")
    @Basic(optional = false)
    @Column(name = "TRANSACTION_ACTION_DETAIL_ID")
    private Long transactionActionDetailId;
    @Column(name = "TRANSACTION_ACTION_ID")
    private Long transactionActionId;
    @Column(name = "PROVIDER_ID")
    private Long providerId;
    @Column(name = "GOODS_ID")
    private Long goodsId;
    @Column(name = "CREATE_DATETIME")
     @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;
    @Column(name = "REFERENCE_CODE")
    private String referenceCode;
    @Column(name = "DESCRIPTION")
    private String description;
    @Transient
    private List<StockTransactionSerial> lsStockTransactionSerial;
    @Transient
    private String goodsName;
    @Transient
    private String providerName;
    @Transient
    private Long total;
    @Transient
    private Long totalErr;
    @Transient
    private String goodsCode;
    @Transient
    private String goodsGroupName;
    @Transient
    private String goodsStatusName;
    @Transient
    private Long goodsStatus;
    @Transient
    private List<StockGoodsInvSerialDTO> setSerial;
    @Transient
    private Integer setSize;
    @Transient
    private Long rowkey;
    @Transient
    private TransactionAction transactionAction;
    @Transient
    private String compactTransactionType;
    @Transient
    private int index;
    @Transient 
    private String strCreateDatetime;
    @Transient
    private String strCreateDatetimeSord;
    @Transient
    private String staffName;
    @Transient
    private String fromShop;
    @Transient
    private String specification;
    @Transient
    private String transactionActionCode;
    @Transient
    private String unitType;
    @Transient
    private String serial;
    @Transient
    private String tranType;
    public TransactionActionDetail() {
    }

    public TransactionActionDetail(Long gId, Long pId) {
        this.goodsId = gId;
        this.providerId = pId;
//        if(quantity==null){
            this.quantity = 0L;
//        }else {
//            this.quantity = quantity;
//        }

    }

    public TransactionActionDetail(Long transactionActionDetailId) {
        this.transactionActionDetailId = transactionActionDetailId;
    }

    public Long getQuantity() {
        return quantity == null ? 0L : quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getTransactionActionDetailId() {
        return transactionActionDetailId;
    }

    public void setTransactionActionDetailId(Long transactionActionDetailId) {
        this.transactionActionDetailId = transactionActionDetailId;
    }

    public Long getTransactionActionId() {
        return transactionActionId;
    }

    public void setTransactionActionId(Long transactionActionId) {
        this.transactionActionId = transactionActionId;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
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
        hash += (getTransactionActionDetailId() != null ? getTransactionActionDetailId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransactionActionDetail)) {
            return false;
        }
        TransactionActionDetail other = (TransactionActionDetail) object;
        if ((this.getTransactionActionDetailId() == null && other.getTransactionActionDetailId() != null) || (this.getTransactionActionDetailId() != null && !this.transactionActionDetailId.equals(other.transactionActionDetailId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "core.Hibernate.BO.Inventory.TransactionActionDetail[ transactionActionDetailId=" + getTransactionActionDetailId() + " ]";
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
     * @return the total
     */
    public Long getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(Long total) {
        this.total = total;
    }

    /**
     * @return the totalErr
     */
    public Long getTotalErr() {
        return totalErr;
    }

    /**
     * @param totalErr the totalErr to set
     */
    public void setTotalErr(Long totalErr) {
        this.totalErr = totalErr;
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
     * @return the lsStockTransactionSerial
     */
    public List<StockTransactionSerial> getLsStockTransactionSerial() {
        if (lsStockTransactionSerial == null) {
            lsStockTransactionSerial = new ArrayList<>();
        }
        return lsStockTransactionSerial;
    }

    /**
     * @param lsStockTransactionSerial the lsStockTransactionSerial to set
     */
    public void setLsStockTransactionSerial(List<StockTransactionSerial> lsStockTransactionSerial) {
        this.lsStockTransactionSerial = lsStockTransactionSerial;
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
     * @return the goodsStatus
     */
    public Long getGoodsStatus() {
        goodsStatus=goodsStatus==null?InventoryConstanst.GoodsStatus.NOMAL:goodsStatus;
        
        return goodsStatus;
    }

    /**
     * @param goodsStatus the goodsStatus to set
     */
    public void setGoodsStatus(Long goodsStatus) {
        this.goodsStatus = goodsStatus;
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
     * @return the setSize
     */
    public Integer getSetSize() {
        return setSize;
    }

    /**
     * @param setSize the setSize to set
     */
    public void setSetSize(Integer setSize) {
        this.setSize = setSize;
    }

    /**
     * @return the rowkey
     */
    public Long getRowkey() {
        rowkey=transactionActionDetailId!=null?transactionActionDetailId:(goodsId!=null&&getGoodsStatus()!=null?goodsId * 10+getGoodsStatus():-1);
        return rowkey;
    }

    /**
     * @param rowkey the rowkey to set
     */
    public void setRowkey(Long rowkey) {
        this.rowkey = rowkey;
    }

    /**
     * @return the createDatetime
     */
    public Date getCreateDatetime() {
        return createDatetime;
    }

    /**
     * @param createDatetime the createDatetime to set
     */
    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

	public TransactionAction getTransactionAction() {
		return transactionAction;
	}

	public void setTransactionAction(TransactionAction transactionAction) {
		this.transactionAction = transactionAction;
	}

	public String getCompactTransactionType() {
		return compactTransactionType;
	}

	public void setCompactTransactionType(String compactTransactionType) {
		this.compactTransactionType = compactTransactionType;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getStrCreateDatetime() {
		return strCreateDatetime;
	}

	public void setStrCreateDatetime(String strCreateDatetime) {
		this.strCreateDatetime = strCreateDatetime;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getFromShop() {
		return fromShop;
	}

	public void setFromShop(String fromShop) {
		this.fromShop = fromShop;
	}

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String spec) {
        this.specification = spec;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    public String getTransactionActionCode() {
        return transactionActionCode;
    }

    public void setTransactionActionCode(String transactionActionCode) {
        this.transactionActionCode = transactionActionCode;
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

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public TransactionActionDetail(TransactionActionDetail obj) {
        this.transactionActionId = obj.transactionActionId;
        this.transactionActionDetailId = obj.transactionActionDetailId;
        this.quantity = obj.quantity;
        this.transactionActionId = obj.transactionActionId;
        this.providerId = obj.providerId;
        this.goodsId = obj.goodsId;
        this.createDatetime = obj.createDatetime;
        this.referenceCode = obj.referenceCode;
        this.description = obj.description;
        this.lsStockTransactionSerial = obj.lsStockTransactionSerial;
        this.goodsName = obj.goodsName;
        this.providerName = obj.providerName;
        this.total = obj.total;
        this.totalErr = obj.totalErr;
        this.goodsCode = obj.goodsCode;
        this.goodsGroupName = obj.goodsGroupName;
        this.goodsStatusName = obj.goodsStatusName;
        this.goodsStatus = obj.goodsStatus;
        this.setSerial = obj.setSerial;
        this.setSize = obj.setSize;
        this.rowkey = obj.rowkey;
        this.transactionAction = obj.transactionAction;
        this.compactTransactionType = compactTransactionType;
        this.index = obj.index;
        this.strCreateDatetime = obj.strCreateDatetime;
        this.staffName = obj.staffName;
        this.fromShop = obj.fromShop;
        this.specification = obj.specification;
        this.transactionActionCode = obj.transactionActionCode;
        this.unitType = obj.unitType;
        this.serial = obj.serial;
        this.tranType = obj.tranType;
        this.strCreateDatetimeSord = obj.strCreateDatetimeSord;
    }

    public String getStrCreateDatetimeSord() {
        return strCreateDatetimeSord;
    }

    public void setStrCreateDatetimeSord(String strCreateDatetimeSord) {
        this.strCreateDatetimeSord = strCreateDatetimeSord;
    }
}
