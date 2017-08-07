/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bo;

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
@Table(name = "STOCK_TRANSACTION_SERIAL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StockTransactionSerial.findAll", query = "SELECT s FROM StockTransactionSerial s"),
    @NamedQuery(name = "StockTransactionSerial.findByStockTransactionDetailId", query = "SELECT s FROM StockTransactionSerial s WHERE s.stockTransactionDetailId = :stockTransactionDetailId"),
    @NamedQuery(name = "StockTransactionSerial.findByStockTransactionSerialId", query = "SELECT s FROM StockTransactionSerial s WHERE s.stockTransactionSerialId = :stockTransactionSerialId"),
    @NamedQuery(name = "StockTransactionSerial.findByStsDatetime", query = "SELECT s FROM StockTransactionSerial s WHERE s.stsDatetime = :stsDatetime"),
    @NamedQuery(name = "StockTransactionSerial.findBySerial", query = "SELECT s FROM StockTransactionSerial s WHERE s.serial = :serial")})
public class StockTransactionSerial implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @SequenceGenerator(name = "STOCK_TRANSACTION_SERIAL_SEQ", sequenceName = "STOCK_TRANSACTION_SERIAL_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "STOCK_TRANSACTION_SERIAL_SEQ")
    @Basic(optional = false)
    @Column(name = "STOCK_TRANSACTION_SERIAL_ID")
    private Long stockTransactionSerialId;
    @Column(name = "STS_DATETIME")
    @Temporal(TemporalType.DATE)
    private Date stsDatetime;
    @Column(name = "SERIAL")
    private String serial;
//    @Column(name = "EQUIPMENT_PROFILE_ID")
    @Column(name = "GOODS_ID")
    private Long equipmentProfileId;
    @Column(name = "PROVIDER_ID")
    private Long providerId;
//    @Column(name = "EQUIPMENT_STATUS")
    @Column(name = "GOODS_STATUS")
    private Long equipmentProfileStatus;
    @Column(name = "STOCK_TRANSACTION_DETAIL_ID")
    private Long stockTransactionDetailId;
    @Column(name = "IM_TRANSACTION_ID")
    private Long imTransactionId;
    @Column(name = "EQUIPMENT_CODE")
    private String equipmentCode;
    @Column(name = "SERIAL_SEARCH")
    private String serialSearch;
    @Column(name = "REASON")
    private String reason;
    //@Transient
    //private EtagDetail etagDetail;
    @Transient
    private EquipmentsDetail equipmentDetail;
    @Transient
    private String goodsStatusName;
    @Transient
    private String goodsName;
    @Transient
    private String provider;
    @Transient
    private String transactionActionCode;
    @Transient
    private String typeName;
    @Transient
    private String goodsGroupName;
    @Transient
    private String goodsCode;
    @Transient
    private int index;
    @Transient 
    private String strCreateDatetime;
    @Transient
    private Long quantity;
    @Transient
    private String profileManagementType;
    @Transient
    private String reasonName;
    @Transient
    private String lastMaintenDate;
    @Transient
    private Date expireDateWarranty;
    @Transient
    private String expireDateWarrantyStr;
    @Transient
    private Long wanranprio;
    @Transient
    private String countCo;
    @Transient
    private String countCq;
    @Transient
    private String warrantyStatus;
    
    
    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public StockTransactionSerial() {
    }

    public StockTransactionSerial(String serial, String ict, String pid, String serialSearch, Long gStatus, Long gId, Long prId) {
        this.serial = serial;
        this.serialSearch = serialSearch;
        this.equipmentProfileStatus = gStatus;
        this.equipmentProfileId = gId;
        this.providerId = prId;
        this.stsDatetime = new Date();
//        this.etagDetail = new EtagDetail(serial, ict, pid, prId, equipmentCode, serialSearch,gId, gStatus);
    }

    public StockTransactionSerial(String serial, String serialSearch, Long gStatus, Long gId, Long prId) {
        this.serial = serial;
        this.serialSearch = serialSearch;
        this.equipmentProfileStatus = gStatus;
        this.equipmentProfileId = gId;
        this.providerId = prId;
        this.stsDatetime = new Date();
    }

    public StockTransactionSerial(Long stockTransactionSerialId) {
        this.stockTransactionSerialId = stockTransactionSerialId;
    }

    public Long getStockTransactionDetailId() {
        return stockTransactionDetailId;
    }

    public void setStockTransactionDetailId(Long stockTransactionDetailId) {
        this.stockTransactionDetailId = stockTransactionDetailId;
    }

    public Long getStockTransactionSerialId() {
        return stockTransactionSerialId;
    }

    public void setStockTransactionSerialId(Long stockTransactionSerialId) {
        this.stockTransactionSerialId = stockTransactionSerialId;
    }

    public Date getStsDatetime() {
        return stsDatetime;
    }

    public void setStsDatetime(Date stsDatetime) {
        this.stsDatetime = stsDatetime;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
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
        hash += (stockTransactionSerialId != null ? stockTransactionSerialId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockTransactionSerial)) {
            return false;
        }
        StockTransactionSerial other = (StockTransactionSerial) object;
        if ((this.stockTransactionSerialId == null && other.stockTransactionSerialId != null) || (this.stockTransactionSerialId != null && !this.stockTransactionSerialId.equals(other.stockTransactionSerialId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "core.Hibernate.BO.Inventory.StockTransactionSerial[ stockTransactionSerialId=" + stockTransactionSerialId + " ]";
    }

    /**
     * @return the goodsStatus
     */
    public Long getEquipmentProfileStatus() {
        return equipmentProfileStatus;
    }

    /**
     * @param goodsStatus the goodsStatus to set
     */
    public void setEquipmentProfileStatus(Long goodsStatus) {
        this.equipmentProfileStatus = goodsStatus;
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

//    public EtagDetail getEtagDetail() {
//        return etagDetail;
//    }
//
//    public void setEtagDetail(EtagDetail etagDetail) {
//        this.etagDetail = etagDetail;
//    }

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
     * @return the provider
     */
    public String getProvider() {
        return provider;
    }

    /**
     * @param provider the provider to set
     */
    public void setProvider(String provider) {
        this.provider = provider;
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
     * @return the etagNumber
     */
    public String getEquipmentCode() {
        return equipmentCode;
    }

    /**
     * @param equipmentCode the etagNumber to set
     */
    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

	public String getTransactionActionCode() {
		return transactionActionCode;
	}

	public void setTransactionActionCode(String transactionActionCode) {
		this.transactionActionCode = transactionActionCode;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getGoodsGroupName() {
		return goodsGroupName;
	}

	public void setGoodsGroupName(String goodsGroupName) {
		this.goodsGroupName = goodsGroupName;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
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

	public String getSerialSearch() {
		return serialSearch;
	}

	public void setSerialSearch(String serialSearch) {
		this.serialSearch = serialSearch;
	}

	public EquipmentsDetail getEquipmentDetail() {
		return equipmentDetail;
	}

	public void setEquipmentDetail(EquipmentsDetail equipmentDetail) {
		this.equipmentDetail = equipmentDetail;
	}

//    public Long getEquipType() {
//        return equipType;
//    }
//
//    public void setEquipType(Long equipType) {
//        this.equipType = equipType;
//    }

    public String getReasonName() {
        return reasonName;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }

    public String getLastMaintenDate() {
        return lastMaintenDate;
    }

    public void setLastMaintenDate(String lastMaintenDate) {
        this.lastMaintenDate = lastMaintenDate;
    }

    public String getProfileManagementType() {
        return profileManagementType;
    }

    public void setProfileManagementType(String profileManagementType) {
        this.profileManagementType = profileManagementType;
    }

    public StockTransactionSerial(StockTransactionSerial stockTransactionSerial) {
        this.stsDatetime = stockTransactionSerial.stsDatetime;
        this.serial = stockTransactionSerial.serial;
        this.equipmentProfileId = stockTransactionSerial.equipmentProfileId;
        this.providerId = stockTransactionSerial.providerId;
        this.equipmentProfileStatus = stockTransactionSerial.equipmentProfileStatus;
        this.stockTransactionDetailId = stockTransactionSerial.stockTransactionDetailId;
        this.imTransactionId = stockTransactionSerial.imTransactionId;
        this.equipmentCode = stockTransactionSerial.equipmentCode;
        this.serialSearch = stockTransactionSerial.serialSearch;
    }

    public String getExpireDateWarrantyStr() {
        if(expireDateWarranty!=null){
            return new SimpleDateFormat("dd/MM/yyyy").format(expireDateWarranty);
        }
        return expireDateWarrantyStr;
    }

    public void setExpireDateWarrantyStr(String expireDateWarrantyStr) {
    	expireDateWarranty = null;
        this.expireDateWarrantyStr = expireDateWarrantyStr;
    }

    public Long getWanranprio() {
        return wanranprio;
    }

    public void setWanranprio(Long wanranprio) {
        this.wanranprio = wanranprio;
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

    public Date getExpireDateWarranty() {
        return expireDateWarranty;
    }

    public void setExpireDateWarranty(Date expireDateWarranty) {
        this.expireDateWarranty = expireDateWarranty;
    }

	/**
	 * @return the warrantyStatus
	 */
	public String getWarrantyStatus() {
		return warrantyStatus;
	}

	/**
	 * @param warrantyStatus the warrantyStatus to set
	 */
	public void setWarrantyStatus(String warrantyStatus) {
		this.warrantyStatus = warrantyStatus;
	}

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
