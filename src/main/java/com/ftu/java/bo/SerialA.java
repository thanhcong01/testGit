/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.java.bo;

import com.ftu.constanst.InventoryConstanst;
import com.ftu.inventory.bo.StockTransactionSerial;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author E5420
 */
public class SerialA implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fromSerial;
    private String toSerial;
    private Long count;
    private Long goodsId;
    private Long providerId;
    private Long status;
    private String statusName;
    private String goodsName;
    private String providerName;
    private String rowKey;

    private Long index;
    private String typeName;
    private String transactionActionCode;
    private String goodsGroupName;
    private String goodsCode;
    private String strCreateDatetime;
    private Long maintancePeriod;
    private Long lyceCycle;
    private Date instockDatetime;
    private String instockDatetimeStr;
    private Long equipmentProfileStatus;
    private Long warranStatus;
    private Date expireDateWarranty;
    private String expireDateWarrantyStr;
    private String  stockStatus;
    private Long wanranprio;
    private String countCo;
    private String countCq;
    public SerialA(String fromSerial, String toSerial) {
        this.fromSerial = fromSerial;
        this.toSerial = toSerial;
    }

    public SerialA() {

    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (index != null ? index.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SerialA)) {
            return false;
        }
        SerialA other = (SerialA) object;
        if ((this.index == null && other.index != null) || (this.index != null && !this.index.equals(other.index))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ftu.java.bo.inventory.SerialA[ index=" + index + " ]";
    }
    
    /**
     * @return the status
     */
    public Long getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Long status) {
        this.status = status;
    }

    /**
     * @return the fromSerial
     */
    public String getFromSerial() {
        return fromSerial;
    }

    /**
     * @return the fromSerialSearch
     */
    public String getFromSerialSearch() {
        return fromSerial != null && fromSerial.length() >=8?
        		fromSerial.substring(fromSerial.length()-8):fromSerial;
    }
    
    /**
     * @param fromSerial the fromSerial to set
     */
    public void setFromSerial(String fromSerial) {
        this.fromSerial = fromSerial;
    }

    /**
     * @return the toSerial
     */
    public String getToSerial() {
        return toSerial;
    }
    
    /**
     * @return the toSerialSearch
     */
    public String getToSerialSearch() {
        return toSerial != null && toSerial.length() >=8?
        		toSerial.substring(toSerial.length()-8):toSerial;
    }

    /**
     * @param toSerial the toSerial to set
     */
    public void setToSerial(String toSerial) {
        this.toSerial = toSerial;
    }

    /**
     * @return the count
     */
    public Long getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(Long count) {
        this.count = count;
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
     * @return the statusName
     */
    public String getStatusName() {
        return statusName;
    }

    /**
     * @param statusName the statusName to set
     */
    public void setStatusName(String statusName) {
        this.statusName = statusName;
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
     * @return the rowKey
     */
    public String getRowKey() {
        rowKey = "-";
        if (fromSerial != null) {
            rowKey = fromSerial.toString();
        }
        if(goodsId!=null){
            rowKey += "-"+goodsId;
        }
        if (providerId != null) {
            rowKey += "-" + providerId.toString();
        }
        return rowKey;
    }

    /**
     * @param rowKey the rowKey to set
     */
    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTransactionActionCode() {
		return transactionActionCode;
	}

	public void setTransactionActionCode(String transactionActionCode) {
		this.transactionActionCode = transactionActionCode;
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

	public String getStrCreateDatetime() {
		return strCreateDatetime;
	}

	public void setStrCreateDatetime(String strCreateDatetime) {
		this.strCreateDatetime = strCreateDatetime;
	}

	public Long getIndex() {
		return index;
	}

	public void setIndex(Long index) {
		this.index = index;
	}

    public Long getMaintancePeriod() {
        return maintancePeriod;
    }

    public void setMaintancePeriod(Long maintancePeriod) {
        this.maintancePeriod = maintancePeriod;
    }

    public Long getLyceCycle() {
        return lyceCycle;
    }

    public void setLyceCycle(Long lyceCycle) {
        this.lyceCycle = lyceCycle;
    }

    public Date getInstockDatetime() {
        return instockDatetime;
    }

    public void setInstockDatetime(Date instockDatetime) {
        this.instockDatetime = instockDatetime;
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

    public Long getEquipmentProfileStatus() {
        return equipmentProfileStatus;
    }

    public void setEquipmentProfileStatus(Long equipmentProfileStatus) {
        this.equipmentProfileStatus = equipmentProfileStatus;
    }

    public String getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(String stockStatus) {
        this.stockStatus = stockStatus;
    }

    public Long getWarranStatus() {
        return warranStatus;
    }

    public void setWarranStatus(Long warranStatus) {
        this.warranStatus = warranStatus;
    }

    public Date getExpireDateWarranty() {
        return expireDateWarranty;
    }

    public void setExpireDateWarranty(Date expireDateWarranty) {
        this.expireDateWarranty = expireDateWarranty;
    }

    public String getExpireDateWarrantyStr() {

        if(expireDateWarranty!=null){
            return new SimpleDateFormat("dd/MM/yyyy").format(expireDateWarranty);
        }
        return expireDateWarrantyStr;
    }
    public String getExpireDateWarrantyStrSort() {

        if(expireDateWarranty!=null){
            return new SimpleDateFormat("yyyy/MM/dd").format(expireDateWarranty);
        }
        return expireDateWarrantyStr;
    }

    public void setExpireDateWarrantyStr(String expireDateWarrantyStr) {
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
}
