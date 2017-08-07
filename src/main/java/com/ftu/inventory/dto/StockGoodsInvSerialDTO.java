/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.dto;

import com.ftu.constanst.InventoryConstanst;
import com.ftu.inventory.bo.EquipmentHistory;
import com.ftu.inventory.bo.TransactionAction;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author E5420
 */
public class StockGoodsInvSerialDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String serial;
    private Long equipmentProfileStatus;
    private String stockStatus;
    private String etagNumber;
    private Date instockDatetime;
    private String instockDatetimeStr;
    private Date outstockDatetime;
    private Long shopId;
    private Long providerId;
    private Long equipmentProfileId;
    private Long currentTaId;
    private String serialSearch;
    private String reasonName;
    private String oldStatusName;
    private String lastMaintenDate;
    private String lastMaintenDateSort;
    private String statusName;
    private Long quantity;
    private String rowKey;
    private String reasonsWarranty;
    private Long oldGoodsStatus;
    private Boolean changWaranNew;
    private String staffName;
    private String profileName;
    private String providerName;
    private EquipmentHistory equipmentHistory;
    private Boolean notSerial = false;
    private String stockStatusName;
    private String transactionType;
    private String shopName;
    
    private String shopCode;
    
    private String profileCode;
    
    private Long index;

    private int indexPri;

    private String goodsGroupName;
    
    private String strOutstockDatetime;
    
    private TransactionAction transactionAction;
    
    private Long countBlockSerial;
    
    private String equipentGroupCode;
    
    private String pxkCode;
    
    private String specification;
    
    private Long lifeCycle;
    
    private Long maintancePeriod;
    
    private Long waranCount;
    
    private Long resonId;
    
    private Date createDateTime;
    
    private Long staffId;
    
    private String note;
    
    private Long groupId;
    
    private Long equimentDetailId;
    
    private Long mseId;
    
    private String unit;
    
    private String providerCode;
    
    private String origin;
    private String originName;
    
    private Long groupType;
    
    private String groupTypeName;
    
    private Long availableCount;

    private Long availableQuantity;
    
    private Date actionDate;
    
    private Long statusMse;
    
    private String actionDateStr;
    
    private Long actionType;
    
    private String equipmentStatusName;
    
    private String nameStr;
    
    private Long transactionActionId;
    
    private String lanCode;
    
    private Long positionId;
    
    private String positionCode;
    
    private String positionName;
    
    private Long warrantyStatus;
    private String warrantyStatusStr;
    
    private Long warrantyPeriod;
    
    private Date warantyExpiredDate;
    
    private String warantyExpiredDateStr;
    
    private Long countDayWaran;
    
    private  Date lastUsedDate;
    
    private  String lastUsedDateStr;
    private Long warantiyCount;
    private Date lastWaranty;
    private String lastWarantyStr;
    private String coNuber;
    private  String cqNumber;
    private Long imTransaction;
    private Long stockGoodsId;
    private Long subQuantity;

    public StockGoodsInvSerialDTO() {
    }

    public StockGoodsInvSerialDTO(String serial, Long gstatus, String stockStatus, Long shopId, Long prvId, Long gId) {
        this.serial = serial;
        this.equipmentProfileStatus = gstatus;
        this.stockStatus = stockStatus;
        this.shopId = shopId;
        this.providerId = prvId;
        this.equipmentProfileId = gId;
    }

    public StockGoodsInvSerialDTO(Long id) {
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

    public Long getEquipmentProfileStatus() {
        return equipmentProfileStatus;
    }

    public void setEquipmentProfileStatus(Long goodsStatus) {
        this.equipmentProfileStatus = goodsStatus;
    }

    public String getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(String stockStatus) {
        this.stockStatus = stockStatus;
    }

    public Date getInstockDatetime() {
        return instockDatetime;
    }

    public void setInstockDatetime(Date instockDatetime) {
        this.instockDatetime = instockDatetime;
    }

    public Date getOutstockDatetime() {
        return outstockDatetime;
    }

    public void setOutstockDatetime(Date outstockDatetime) {
        this.outstockDatetime = outstockDatetime;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockGoodsInvSerialDTO)) {
            return false;
        }
        StockGoodsInvSerialDTO other = (StockGoodsInvSerialDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "core.Hibernate.BO.Inventory.StockGoodsInvSerial[ id=" + id + " ]";
    	//return serialSearch;
    }

    /**
     * @return the statusName
     */
    public String getStatusName() {
        //statusName = InventoryConstanst.GoodsStatus.NOMAL.equals(equipmentProfileStatus) ? "Hàng mới" : "Hàng hỏng";
    	if (InventoryConstanst.GoodsStatus.NOMAL.equals(equipmentProfileStatus))
        	statusName = "Hàng mới";
		else if (InventoryConstanst.GoodsStatus.ERROR.equals(equipmentProfileStatus))
			statusName = "Hàng hỏng";
		else if (InventoryConstanst.GoodsStatus.USED.equals(equipmentProfileStatus))
			statusName = "Hàng đã sử dụng";
        return statusName;
    }

    /**
     * @param statusName the statusName to set
     */
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    /**
     * @return the oldGoodsStatus
     */
    public Long getOldGoodsStatus() {
        return oldGoodsStatus;
    }

    /**
     * @param oldGoodsStatus the oldGoodsStatus to set
     */
    public void setOldGoodsStatus(Long oldGoodsStatus) {
        this.oldGoodsStatus = oldGoodsStatus;
    }

    /**
     * @return the profileName
     */
    public String getProfileName() {
        return profileName;
    }

    /**
     * @param profileName the profileName to set
     */
    public void setProfileName(String profileName) {
        this.profileName = profileName;
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
     * @return the oldStatusName
     */
    public String getOldStatusName() {
        return oldStatusName;
    }

    /**
     * @param oldStatusName the oldStatusName to set
     */
    public void setOldStatusName(String oldStatusName) {
        this.oldStatusName = oldStatusName;
    }

    /**
     * @return the stockStatusName
     */
  
    

    /**
     * @param stockStatusName the stockStatusName to set
     */
    public void setStockStatusName(String stockStatusName) {
        this.stockStatusName = stockStatusName;
    }

    public String getStockStatusName() {
		return stockStatusName;
	}

	/**
     * @return the currentTaId
     */
    public Long getCurrentTaId() {
        return currentTaId;
    }

    /**
     * @param currentTaId the currentTaId to set
     */
    public void setCurrentTaId(Long currentTaId) {
        this.currentTaId = currentTaId;
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
     * @return the profileCode
     */
    public String getProfileCode() {
        return profileCode;
    }

    /**
     * @param profileCode the profileCode to set
     */
    public void setProfileCode(String profileCode) {
        this.profileCode = profileCode;
    }

	public Long getIndex() {
		return index;
	}

	public void setIndex(Long index) {
		this.index = index;
	}

	public String getGoodsGroupName() {
		return goodsGroupName;
	}

	public void setGoodsGroupName(String goodsGroupName) {
		this.goodsGroupName = goodsGroupName;
	}

	public String getStrOutstockDatetime() {
		return strOutstockDatetime;
	}

	public void setStrOutstockDatetime(String strOutstockDatetime) {
		this.strOutstockDatetime = strOutstockDatetime;
	}

	public TransactionAction getTransactionAction() {
		return transactionAction;
	}

	public void setTransactionAction(TransactionAction transactionAction) {
		this.transactionAction = transactionAction;
	}

	public Long getCountBlockSerial() {
		return countBlockSerial;
	}

	public void setCountBlockSerial(Long countBlockSerial) {
		this.countBlockSerial = countBlockSerial;
	}

	public String getSerialSearch() {
		return serialSearch;
	}

	public void setSerialSearch(String serialSearch) {
		this.serialSearch = serialSearch;
	}

    public String getEquipentGroupCode() {
        return equipentGroupCode;
    }

    public void setEquipentGroupCode(String equipentGroupCode) {
        this.equipentGroupCode = equipentGroupCode;
    }

    public String getPxkCode() {
        return pxkCode;
    }

    public void setPxkCode(String pxkCode) {
        this.pxkCode = pxkCode;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specifiCation) {
        this.specification = specifiCation;
    }
 
    public Long getLifeCycle() {
        return lifeCycle;
    }

    public void setLifeCycle(Long lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    public Long getMaintancePeriod() {
        return maintancePeriod;
    }

    public void setMaintancePeriod(Long maintancePeriod) {
        this.maintancePeriod = maintancePeriod;
    }

    public Long getResonId() {
        return resonId;
    }

    public void setResonId(Long resonId) {
        this.resonId = resonId;
    }

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getProviderCode() {
        return providerCode;
    }

    public void setProviderCode(String providerCode) {
        this.providerCode = providerCode;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public Long getGroupType() {
        return groupType;
    }

    public void setGroupType(Long groupType) {
        this.groupType = groupType;
    }

    public String getGroupTypeName() {
        return groupTypeName;
    }

    public void setGroupTypeName(String groupTypeName) {
        this.groupTypeName = groupTypeName;
    }

    public Long getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(Long availableCount) {
        this.availableCount = availableCount;
    }

    public String getEquipmentStatusName() {
        return equipmentStatusName;
    }

    public void setEquipmentStatusName(String equipmentStatusName) {
        this.equipmentStatusName = equipmentStatusName;
    }

    public String getNameStr() {
        return nameStr;
    }

    public void setNameStr(String nameStr) {
        this.nameStr = nameStr;
    }

    public Long getTransactionActionId() {
        return transactionActionId;
    }

    public void setTransactionActionId(Long transactionActionId) {
        this.transactionActionId = transactionActionId;
    }

    public String getLanCode() {
        return lanCode;
    }

    public void setLanCode(String lanCode) {
        this.lanCode = lanCode;
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public Long getWarrantyStatus() {
        if(warrantyStatus == null) warrantyStatus = 2L;
        return warrantyStatus;
    }

    public void setWarrantyStatus(Long warrantyStatus) {
        this.warrantyStatus = warrantyStatus;
    }

    public Long getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public void setWarrantyPeriod(Long warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }

    public Date getWarantyExpiredDate() {
        return warantyExpiredDate;
    }

    public void setWarantyExpiredDate(Date warantyExpiredDate) {
        this.warantyExpiredDate = warantyExpiredDate;
    }

    public Long getEquimentDetailId() {
        return equimentDetailId;
    }

    public void setEquimentDetailId(Long equimentDetailId) {
        this.equimentDetailId = equimentDetailId;
    }

    public String getWarantyExpiredDateStr() {
        if(warantyExpiredDate!=null){
             return new SimpleDateFormat("dd/MM/yyyy").format(warantyExpiredDate);
        }
        return warantyExpiredDateStr;
    }
    public String getWarantyExpiredDateStr2() {
        if(warantyExpiredDate!=null){
            return new SimpleDateFormat("yyyy/MM/dd").format(warantyExpiredDate);
        }
        return warantyExpiredDateStr;
    }

    public void setWarantyExpiredDateStr(String warantyExpiredDateStr) {
        this.warantyExpiredDateStr = warantyExpiredDateStr;
    }

    public String getInstockDatetimeStr() {
        if(instockDatetime!=null){
            return new SimpleDateFormat("dd/MM/yyyy").format(instockDatetime);
        }
        return instockDatetimeStr;
    }

    public String getInstockDatetimeStr2() {
        if(instockDatetime!=null){
            return new SimpleDateFormat("yyyy/MM/dd").format(instockDatetime);
        }
        return instockDatetimeStr;
    }

    public void setInstockDatetimeStr(String instockDatetimeStr) {
        this.instockDatetimeStr = instockDatetimeStr;
    }

    public Long getCountDayWaran() {
        if(warantyExpiredDate!=null){
            long days = TimeUnit.DAYS.toDays(TimeUnit.DAYS.convert((warantyExpiredDate.getTime() - (new Date()).getTime()), TimeUnit.MILLISECONDS));
            if(days <= 0){
                days =0;
            }else days++;
            return days;
        }
        return countDayWaran;
    }

    public void setCountDayWaran(Long countDayWaran) {
        this.countDayWaran = countDayWaran;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getPositionCode() {
        return positionCode;
    }

    public void setPositionCode(String positionCode) {
        this.positionCode = positionCode;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public Long getWaranCount() {
        return waranCount;
    }

    public void setWaranCount(Long waranCount) {
        this.waranCount = waranCount;
    }

    public Date getLastUsedDate() {
        return lastUsedDate;
    }

    public void setLastUsedDate(Date lastUsedDate) {
        this.lastUsedDate = lastUsedDate;
    }

    public String getLastUsedDateStr() {
        if(lastUsedDate!=null){
            return new SimpleDateFormat("dd/MM/yyyy").format(lastUsedDate);
        }
        return lastUsedDateStr;
    }

    public void setLastUsedDateStr(String lastUsedDateStr) {
        this.lastUsedDateStr = lastUsedDateStr;
    }

    public Long getActionType() {
        return actionType;
    }

    public void setActionType(Long actionType) {
        this.actionType = actionType;
    }

    public String getActionDateStr() {
        if(actionDate!=null){
            return new SimpleDateFormat("dd/MM/yyyy").format(actionDate);
        }
        return actionDateStr;
    }

    public void setActionDateStr(String actionDateStr) {
        this.actionDateStr = actionDateStr;
    }

    public Long getMseId() {
        return mseId;
    }

    public void setMseId(Long mseId) {
        this.mseId = mseId;
    }

    public Long getStatusMse() {
        return statusMse;
    }

    public void setStatusMse(Long statusMse) {
        this.statusMse = statusMse;
    }

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

    public String getCoNuber() {
        return coNuber;
    }

    public void setCoNuber(String coNuber) {
        this.coNuber = coNuber;
    }

    public String getCqNumber() {
        return cqNumber;
    }

    public void setCqNumber(String cqNumber) {
        this.cqNumber = cqNumber;
    }

    public Long getImTransaction() {
        return imTransaction;
    }

    public void setImTransaction(Long imTransaction) {
        this.imTransaction = imTransaction;
    }

    public Long getStockGoodsId() {
        return stockGoodsId;
    }

    public void setStockGoodsId(Long stockGoodsId) {
        this.stockGoodsId = stockGoodsId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Long availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public String getRowKey() {
        rowKey = "-";
        if(id!=null){
            rowKey+=id;
        }
        if(equipmentProfileId!=null){
            rowKey+=equipmentProfileId;
        }
        if(stockGoodsId!=null){
            rowKey+=stockGoodsId;
        }
        if(equimentDetailId!=null){
            rowKey+=equimentDetailId;
        }
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public String getReasonsWarranty() {
        return reasonsWarranty;
    }

    public void setReasonsWarranty(String reasonsWarranty) {
        this.reasonsWarranty = reasonsWarranty;
    }

    public Boolean getChangWaranNew() {
        return changWaranNew;
    }

    public void setChangWaranNew(Boolean changWaranNew) {
        this.changWaranNew = changWaranNew;
    }

    public StockGoodsInvSerialDTO(StockGoodsInvSerialDTO dto) {
        this.id = dto.id;
        this.serial = dto.serial;
        this.equipmentProfileStatus = dto.equipmentProfileStatus;
        this.stockStatus = dto.stockStatus;
        this.etagNumber = dto.etagNumber;
        this.instockDatetime = dto.instockDatetime;
        this.instockDatetimeStr = dto.instockDatetimeStr;
        this.outstockDatetime = dto.outstockDatetime;
        this.shopId = dto.shopId;
        this.providerId = dto.providerId;
        this.equipmentProfileId = dto.equipmentProfileId;
        this.currentTaId = dto.currentTaId;
        this.serialSearch = dto.serialSearch;
        this.reasonName = dto.reasonName;
        this.oldStatusName = dto.oldStatusName;
        this.lastMaintenDate = dto.lastMaintenDate;
        this.statusName = dto.statusName;
        this.quantity = dto.quantity;
        this.rowKey = dto.rowKey;
        this.reasonsWarranty = dto.reasonsWarranty;
        this.oldGoodsStatus = dto.oldGoodsStatus;
        this.changWaranNew = dto.changWaranNew;
        this.profileName = dto.profileName;
        this.providerName = dto.providerName;
        this.stockStatusName = dto.stockStatusName;
        this.shopName = dto.shopName;
        this.shopCode = dto.shopCode;
        this.profileCode = dto.profileCode;
        this.index = dto.index;
        this.goodsGroupName = dto.goodsGroupName;
        this.strOutstockDatetime = dto.strOutstockDatetime;
        this.transactionAction = dto.transactionAction;
        this.countBlockSerial = dto.countBlockSerial;
        this.equipentGroupCode = dto.equipentGroupCode;
        this.pxkCode = dto.pxkCode;
        this.specification = dto.specification;
        this.lifeCycle = dto.lifeCycle;
        this.maintancePeriod = dto.maintancePeriod;
        this.waranCount = dto.waranCount;
        this.resonId = dto.resonId;
        this.createDateTime = dto.createDateTime;
        this.staffId = dto.staffId;
        this.note = dto.note;
        this.groupId = dto.groupId;
        this.equimentDetailId = dto.equimentDetailId;
        this.mseId = dto.mseId;
        this.unit = dto.unit;
        this.providerCode = dto.providerCode;
        this.origin = dto.origin;
        this.groupType = dto.groupType;
        this.groupTypeName = dto.groupTypeName;
        this.availableCount = dto.availableCount;
        this.availableQuantity = dto.availableQuantity;
        this.actionDate = dto.actionDate;
        this.statusMse = dto.statusMse;
        this.actionDateStr = dto.actionDateStr;
        this.actionType = dto.actionType;
        this.equipmentStatusName = dto.equipmentStatusName;
        this.nameStr = dto.nameStr;
        this.transactionActionId = dto.transactionActionId;
        this.lanCode = dto.lanCode;
        this.positionId = dto.positionId;
        this.positionCode = dto.positionCode;
        this.positionName = dto.positionName;
        this.warrantyStatus = dto.warrantyStatus;
        this.warrantyPeriod = dto.warrantyPeriod;
        this.warantyExpiredDate = dto.warantyExpiredDate;
        this.warantyExpiredDateStr = dto.warantyExpiredDateStr;
        this.countDayWaran = dto.countDayWaran;
        this.lastUsedDate = dto.lastUsedDate;
        this.lastUsedDateStr = dto.lastUsedDateStr;
        this.coNuber = dto.coNuber;
        this.cqNumber = dto.cqNumber;
        this.imTransaction = dto.imTransaction;
        this.stockGoodsId = dto.stockGoodsId;
    }

    public Long getWarantiyCount() {
        return warantiyCount;
    }

    public void setWarantiyCount(Long warantiyCount) {
        this.warantiyCount = warantiyCount;
    }

    public Date getLastWaranty() {
        return lastWaranty;
    }

    public void setLastWaranty(Date lastWaranty) {
        this.lastWaranty = lastWaranty;
    }

    public String getLastWarantyStr() {
        if(lastWaranty!=null){
            return new SimpleDateFormat("dd/MM/yyyy").format(lastWaranty);
        }
        return lastWarantyStr;
    }

    public void setLastWarantyStr(String lastWarantyStr) {
        this.lastWarantyStr = lastWarantyStr;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public EquipmentHistory getEquipmentHistory() {
        return equipmentHistory;
    }

    public void setEquipmentHistory(EquipmentHistory equipmentHistory) {
        this.equipmentHistory = equipmentHistory;
    }

    public int getIndexPri() {
        return indexPri;
    }

    public void setIndexPri(int indexPri) {
        this.indexPri = indexPri;
    }

    public Long getSubQuantity() {
        return subQuantity;
    }

    public void setSubQuantity(Long subQuantity) {
        this.subQuantity = subQuantity;
    }

	/**
	 * @return the warrantyStatusStr
	 */
	public String getWarrantyStatusStr() {
		return warrantyStatusStr;
	}

	/**
	 * @param warrantyStatusStr the warrantyStatusStr to set
	 */
	public void setWarrantyStatusStr(String warrantyStatusStr) {
		this.warrantyStatusStr = warrantyStatusStr;
	}

	/**
	 * @return the originName
	 */
	public String getOriginName() {
		return originName;
	}

	/**
	 * @param originName the originName to set
	 */
	public void setOriginName(String originName) {
		this.originName = originName;
	}

    public Boolean getNotSerial() {
        return notSerial;
    }

    public void setNotSerial(Boolean notSerial) {
        this.notSerial = notSerial;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getLastMaintenDateSort() {
	    if(lastMaintenDate == null || lastMaintenDate.isEmpty() ){
	        return "";
        }
        String[] str = lastMaintenDate.split("/");
        return str[2]+"/"+str[1]+"/"+str[0];
    }

    public void setLastMaintenDateSort(String lastMaintenDateSort) {
        this.lastMaintenDateSort = lastMaintenDateSort;
    }
}
