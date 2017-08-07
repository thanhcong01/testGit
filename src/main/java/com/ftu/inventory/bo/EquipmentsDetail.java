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

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author E5420
 */
@Entity
@Table(name = "EQUIPMENT_DETAIL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EquipmentsDetail.findAll", query = "SELECT e FROM EquipmentsDetail e"),
    @NamedQuery(name = "EquipmentsDetail.findById", query = "SELECT e FROM EquipmentsDetail e WHERE e.id = :id"),
    @NamedQuery(name = "EquipmentsDetail.findBySerial", query = "SELECT e FROM EquipmentsDetail e WHERE e.serial = :serial")})
public class EquipmentsDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @SequenceGenerator(name = "EQUIPMENT_DETAIL_SEQ", sequenceName = "EQUIPMENT_DETAIL_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "EQUIPMENT_DETAIL_SEQ")
    @Basic(optional = false)
    @Column(name = "EQUIPMENT_ID")
    private Long id;
    @Column(name = "SERIAL")
    private String serial;
    @Column(name = "IM_TRANSACTION_ID")
    private Long imTransactionId;
//	equipments
    @Column(name = "EQUIPMENT_PROFILE_ID")
    private Long equipmentsProfileId;
    
    @Column(name = "EQUIPMENT_GROUP_ID")
    private Long equipmentsGroupId;
    
    @Column(name = "EQUIPMENT_STATUS")
    private Long status;


    @Column(name = "SPECIFICATION")
    private String equipmentSpecification;
    
    @Column(name = "POSITION_ID")
    private Long positionId;
    
    @Column(name = "PROVIDER_ID")
    private Long providerId;
    
    @Column(name = "LIFE_CYCLE")
    private Long lifeCycle;
    
    @Column(name = "LAST_USED_DATE")
    @Temporal(TemporalType.DATE)
    private Date lastUsedDate;
    
    @Column(name = "MAINTANCE_PERIOD")
    private Long maintancePeriod;
    
    @Column(name = "WARRANTY_COUNT")
    private Long warrantyCount;
    
    @Column(name = "WARRANTY_PERIOD")
    private Long warrantyPeriod;
    
    @Column(name = "EQUIPMENT_CODE")
    private String equipmentCode;
    
    @Column(name = "CO_NUMBER")
    private String coNumber;
    
    @Column(name = "CQ_NUMBER")
    private String cqNumber;
	@Column(name = "WARANTY_EXPIRED_DATE")
//@Transient
	@Temporal(TemporalType.DATE)
	private Date warantyExpiredDate;
	
	@Column(name = "WARANTY_STATUS")
	private Long warantyStatus;
	
	@Column(name = "WARRANTY_REASON")
    private String warrantyReason;

	@Transient
	private String equipmentsProfileCode;
	@Transient
	private Long transactionActionID;
	@Transient
	private Date dateInventory;
	@Transient
	private Long staffId;
	@Transient
	private Long actionType;

	@Transient
	private Date actionDate;
	@Transient
	private Long shopId;
	@Transient
	private Boolean changWaranNew;

	@Transient
	private Long userReceiver;
	@Transient
	private String shopName;
	@Transient
	private String transactionActionCode;
	@Transient
	private String referenCode;
	@Transient
	private String profileName;
	@Transient
	private String groupCode;
	@Transient
	private String equipments;
	@Transient
	private String staffName;
	@Transient
	private String providerName;
	@Transient
	private String providerCode;
	@Transient
	private Boolean updateSetupProfile;
	@Transient
	private Long stockStatusId;
	@Transient
	private Long quantitySerial;
	@Transient
	private String serialNew;
	@Transient
	private List<EquipmentHistory> equipmentHistories;
	@Transient
	private int sizeEquipmentHistory;
	@Transient
	private Long statusOld;
	@Transient
	private String lastUsedDateStr;

	public EquipmentsDetail() {
    }
    
    public EquipmentsDetail(String serial, Long equipmentsProfileId, Long groupId, Long status,
							String equipmentSpecification, Long positionId, Long providerId, Long lifeCycle,
							Long maintancePeriod, Long warrantyCount, Long warrantyPeriod, String equipmentCode, String coNumber,
							String cqNumber) {
		super();
		this.serial = serial;
//		this.imTransactionId = imTransactionId;
		this.equipmentsProfileId = equipmentsProfileId;
		this.equipmentsGroupId = groupId;
		this.status = status;
		this.equipmentSpecification = equipmentSpecification;
		this.positionId = positionId;
		this.providerId = providerId;
		this.lifeCycle = lifeCycle;
//		this.lastUsedDate = lastUsedDate;
		this.maintancePeriod = maintancePeriod;
		this.warrantyCount = warrantyCount;
		this.warrantyPeriod = warrantyPeriod;
		this.equipmentCode = equipmentCode;
		this.coNumber = coNumber;
		this.cqNumber = cqNumber;
	}

	public String getLastUsedDateStr() {
		if(lastUsedDate!=null){
			return new SimpleDateFormat("dd/MM/yyyy").format(lastUsedDate);
		}
		return "";
	}

	public void setLastUsedDateStr(String lastUsedDateStr) {
		this.lastUsedDateStr = lastUsedDateStr;
	}
	public String getLastUsedDateStr2() {
		if(lastUsedDate!=null){
			return new SimpleDateFormat("yyyy/MM/dd").format(lastUsedDate);
		}
		return "";
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
        if (!(object instanceof EquipmentsDetail)) {
            return false;
        }
        EquipmentsDetail other = (EquipmentsDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ftu.java.bo.EquipmentsDetail[ id=" + id + " ]";
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
		this.serial = serial!=null? serial.trim():"";
	}



	public Long getImTransactionId() {
		return imTransactionId;
	}



	public void setImTransactionId(Long imTransactionId) {
		this.imTransactionId = imTransactionId;
	}



	public Long getEquipmentsProfileId() {
		return equipmentsProfileId;
	}



	public void setEquipmentsProfileId(Long profileId) {
		this.equipmentsProfileId = profileId;
	}



	public Long getEquipmentsGroupId() {
		return equipmentsGroupId;
	}



	public void setEquipmentsGroupId(Long groupId) {
		this.equipmentsGroupId = groupId;
	}



	public Long getStatus() {
		return status;
	}



	public void setStatus(Long status) {
		this.status = status;
	}



	public String getEquipmentSpecification() {
		return equipmentSpecification;
	}



	public void setEquipmentSpecification(String equipmentSpecification) {
		this.equipmentSpecification = equipmentSpecification;
	}



	public Long getPositionId() {
		return positionId;
	}



	public void setPositionId(Long positionId) {
		this.positionId = positionId;
	}



	public Long getProviderId() {
		return providerId;
	}



	public void setProviderId(Long providerId) {
		this.providerId = providerId;
	}



	public Long getLifeCycle() {
		return lifeCycle;
	}



	public void setLifeCycle(Long lifeCycle) {
		this.lifeCycle = lifeCycle;
	}



	public Date getLastUsedDate() {
		return lastUsedDate;
	}



	public void setLastUsedDate(Date lastUsedDate) {
		this.lastUsedDate = lastUsedDate;
	}



	public Long getMaintancePeriod() {
		return maintancePeriod;
	}



	public void setMaintancePeriod(Long maintancePeriod) {
		this.maintancePeriod = maintancePeriod;
	}



	public Long getWarrantyCount() {
		return warrantyCount;
	}



	public void setWarrantyCount(Long warrantyCount) {
		this.warrantyCount = warrantyCount;
	}



	public Long getWarrantyPeriod() {
		return warrantyPeriod;
	}



	public void setWarrantyPeriod(Long warrantyPeriod) {
		this.warrantyPeriod = warrantyPeriod;
	}



	public String getEquipmentCode() {
		return equipmentCode;
	}



	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
	}



	public String getCoNumber() {
		return coNumber;
	}



	public void setCoNumber(String coNumber) {
		this.coNumber = coNumber;
	}



	public String getCqNumber() {
		return cqNumber;
	}



	public void setCqNumber(String cqNumber) {
		this.cqNumber = cqNumber;
	}

	public String getEquipmentsProfileCode() {
		return equipmentsProfileCode;
	}

	public void setEquipmentsProfileCode(String equipmentsProfileCode) {
		this.equipmentsProfileCode = equipmentsProfileCode;
	}

	public String getTransactionActionCode() {
		return transactionActionCode;
	}

	public void setTransactionActionCode(String transactionActionCode) {
		this.transactionActionCode = transactionActionCode;
	}

	public Long getTransactionActionID() {
		return transactionActionID;
	}

	public void setTransactionActionID(Long transactionActionID) {
		this.transactionActionID = transactionActionID;
	}

	public Date getDateInventory() {
		return dateInventory;
	}

	public void setDateInventory(Date dateInventory) {
		this.dateInventory = dateInventory;
	}

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public Long getActionType() {
		return actionType;
	}

	public void setActionType(Long actionType) {
		this.actionType = actionType;
	}

	public Date getActionDate() {
		return actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Long getUserReceiver() {
		return userReceiver;
	}

	public void setUserReceiver(Long userReceiver) {
		this.userReceiver = userReceiver;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Date getWarantyExpiredDate() {
		return warantyExpiredDate;
	}

	public void setWarantyExpiredDate(Date warantyExpiredDate) {
		this.warantyExpiredDate = warantyExpiredDate;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getEquipments() {
		return equipments;
	}

	public void setEquipments(String equipments) {
		this.equipments = equipments;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public Long getWarantyStatus() {
		if(warantyStatus == null) warantyStatus = 2L;

		return warantyStatus;
	}

	public void setWarantyStatus(Long warantyStatus) {
		this.warantyStatus = warantyStatus;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getProviderCode() {
		return providerCode;
	}

	public void setProviderCode(String providerCode) {
		this.providerCode = providerCode;
	}

	public String getReferenCode() {
		return referenCode;
	}

	public void setReferenCode(String referenCode) {
		this.referenCode = referenCode;
	}

	public Boolean getUpdateSetupProfile() {
		return updateSetupProfile;
	}

	public void setUpdateSetupProfile(Boolean updateSetupProfile) {
		this.updateSetupProfile = updateSetupProfile;
	}

	public List<EquipmentHistory> getEquipmentHistories() {
		return equipmentHistories;
	}

	public void setEquipmentHistories(List<EquipmentHistory> equipmentHistories) {
		this.equipmentHistories = equipmentHistories;
	}

	public int getSizeEquipmentHistory() {
		if(equipmentHistories!=null){
			sizeEquipmentHistory = equipmentHistories.size();
		}else {
			sizeEquipmentHistory = 0;
		}
		return sizeEquipmentHistory;
	}

	public void setSizeEquipmentHistory(int sizeEquipmentHistory) {
		this.sizeEquipmentHistory = sizeEquipmentHistory;
	}

	public Long getStockStatusId() {
		return stockStatusId;
	}

	public void setStockStatusId(Long stockStatusId) {
		this.stockStatusId = stockStatusId;
	}

	public Long getQuantitySerial() {
		return quantitySerial;
	}

	public void setQuantitySerial(Long quantitySerial) {
		this.quantitySerial = quantitySerial;
	}

	public Boolean getChangWaranNew() {
		return changWaranNew;
	}

	public void setChangWaranNew(Boolean changWaranNew) {
		this.changWaranNew = changWaranNew;
	}

	public String getSerialNew() {
		return serialNew;
	}

	public void setSerialNew(String serialNew) {
		this.serialNew = serialNew!=null?serialNew.trim():"";
	}

	/**
	 * @return the warrantyReason
	 */
	public String getWarrantyReason() {
		return warrantyReason;
	}

	/**
	 * @param warrantyReason the warrantyReason to set
	 */
	public void setWarrantyReason(String warrantyReason) {
		this.warrantyReason = warrantyReason;
	}

	public Long getStatusOld() {
		return statusOld;
	}

	public void setStatusOld(Long statusOld) {
		this.statusOld = statusOld;
	}
}
