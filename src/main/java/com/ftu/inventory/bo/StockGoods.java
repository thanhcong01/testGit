/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bo;

import java.io.Serializable;
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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author E5420
 */
@Entity
@Table(name = "STOCK_GOODS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StockGoods.findAll", query = "SELECT s FROM StockGoods s"),
    @NamedQuery(name = "StockGoods.findByStockGoodsId", query = "SELECT s FROM StockGoods s WHERE s.stockGoodsId = :stockGoodsId"),
    @NamedQuery(name = "StockGoods.findByQuantity", query = "SELECT s FROM StockGoods s WHERE s.quantity = :quantity"),
    @NamedQuery(name = "StockGoods.findByGoodsStatus", query = "SELECT s FROM StockGoods s WHERE s.goodsStatus = :goodsStatus")})
public class StockGoods implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @SequenceGenerator(name = "STOCK_GOODS_SEQ", sequenceName = "STOCK_GOODS_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "STOCK_GOODS_SEQ")
    @Basic(optional = false)
    @Column(name = "STOCK_GOODS_ID")
    private Long stockGoodsId;
    @Column(name = "QUANTITY")
    private Long quantity;
    @Column(name = "GOODS_STATUS")
    private Long goodsStatus;
    @Column(name = "AVAILABLE_QUANTITY")
//@Transient
    private Long availableQuantity;
    @Column(name = "SHOP_ID")
    private Long shopId;
    @Column(name = "GOODS_ID")
    private Long goodsId;
    //PDF
    @Transient
    private String orderCode;
    @Transient
    private String profileName;
    @Transient
    private String profileCode;
    @Transient
    private String equipmentsGroup;
    @Transient
    private String goodsStatusName;
    @Transient
    private Boolean check;
    @Transient
    private Long rowKey;
    @Transient
    private Long quantitySold;
    @Transient
    private Long quantityBlock;
    @Transient 
    private Long sold;
    @Transient
    private Long index;
    @Transient
    private String indexStr;
    @Transient
    private EquipmentsProfile goods;
    @Transient
    private Long instockErr;
    @Transient
    private Long goodsErr;
    @Transient 
    private Long goodsNormal;
    @Transient
    private Long equipentGroupId;
    @Transient
    private Long countBlockSerial;
    @Transient
    private String equipentGroupCode;
    @Transient
    private String pxkCode;
    @Transient
    private String specification;
    @Transient
    private Long lifeCycle;
    @Transient
    private Long maintancePeriod;
    @Transient
    private String unit;
    @Transient
    private String unitName;
    @Transient
    private String approCode;
    @Transient
    private String linkDocument;
    @Transient
    private Long quantityUpdate;
    @Transient
    private Long errorQuantity;
    @Transient
    private Long exStaffQuantity;
    @Transient
    private Long usedQuantity;
    @Transient
    private Long warrantyQuantity;
    @Transient
    private Long exchangedQuantity;
    
    public StockGoods() {
    }

    public StockGoods(Long quan, Long aquan, Long shopId, Long goodsId) {
        this.quantity = quan;
        this.availableQuantity = aquan;
        this.shopId = shopId;
        this.goodsId = goodsId;
    }

    public StockGoods(Long stockGoodsId) {
        this.stockGoodsId = stockGoodsId;
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

    public Long getEquipmentsGroupId() {
		return equipentGroupId;
	}

	public void setEquipmentsGroupId(Long goodsGroupId) {
		this.equipentGroupId = goodsGroupId;
	}

	public Long getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(Long goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public Long getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Long availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
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
        hash += (stockGoodsId != null ? stockGoodsId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockGoods)) {
            return false;
        }
        StockGoods other = (StockGoods) object;
        if ((this.stockGoodsId == null && other.stockGoodsId != null) || (this.stockGoodsId != null && !this.stockGoodsId.equals(other.stockGoodsId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "core.Hibernate.BO.Inventory.StockGoods[ stockGoodsId=" + stockGoodsId + " ]";
    }

    /**
     * @return the check
     */
    public Boolean getCheck() {
        return check;
    }

    /**
     * @param check the check to set
     */
    public void setCheck(Boolean check) {
        this.check = check;
    }

    /**
     * @return the orderCode
     */
    public String getOrderCode() {
        return orderCode;
    }

    /**
     * @param orderCode the orderCode to set
     */
    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
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
     * @return the goods
     */
    public EquipmentsProfile getGoods() {
        return goods;
    }

    /**
     * @param goods the goods to set
     */
    public void setGoods(EquipmentsProfile goods) {
        this.goods = goods;
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
     * @return the rowKey
     */
    public Long getRowKey() {
        rowKey = stockGoodsId != null ? stockGoodsId :(goodsId!=null&&goodsStatus!=null? goodsId * 10 + goodsStatus:-1);
        return rowKey;
    }

    /**
     * @param rowKey the rowKey to set
     */
    public void setRowKey(Long rowKey) {
        this.rowKey = rowKey;
    }

    /**
     * @return the quantitySold
     */
    public Long getQuantitySold() {
        return quantitySold;
    }

    /**
     * @param quantitySold the quantitySold to set
     */
    public void setQuantitySold(Long quantitySold) {
        this.quantitySold = quantitySold;
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

    /**
     * @return the goodsGroup
     */
    public String getEquipmentsGroup() {
        return equipmentsGroup;
    }

    /**
     * @param goodsGroup the goodsGroup to set
     */
    public void setEquipmentsGroup(String goodsGroup) {
        this.equipmentsGroup = goodsGroup;
    }

    /**
     * @return the index
     */
    public Long getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(Long index) {
        this.index = index;
    }

    /**
     * @return the quantityBlock
     */
    public Long getQuantityBlock() {
        return quantityBlock;
    }

    /**
     * @param quantityBlock the quantityBlock to set
     */
    public void setQuantityBlock(Long quantityBlock) {
        this.quantityBlock = quantityBlock;
    }

	public Long getSold() {
		return sold;
	}

	public void setSold(Long sold) {
		this.sold = sold;
	}

	public Long getInstockErr() {
		return instockErr;
	}

	public void setInstockErr(Long instockErr) {
		this.instockErr = instockErr;
	}

	public Long getGoodsErr() {
		return goodsErr;
	}

	public void setGoodsErr(Long goodsErr) {
		this.goodsErr = goodsErr;
	}

	public Long getGoodsNormal() {
		return goodsNormal;
	}

	public void setGoodsNormal(Long goodsNormal) {
		this.goodsNormal = goodsNormal;
	}

    public Long getEquipentGroupId() {
        return equipentGroupId;
    }

    public void setEquipentGroupId(Long equipentGroupId) {
        this.equipentGroupId = equipentGroupId;
    }

    public Long getCountBlockSerial() {
        return countBlockSerial;
    }

    public void setCountBlockSerial(Long countBlockSerial) {
        this.countBlockSerial = countBlockSerial;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getApproCode() {
        return approCode;
    }

    public void setApproCode(String approCode) {
        this.approCode = approCode;
    }

    public String getLinkDocument() {
        return linkDocument;
    }

    public void setLinkDocument(String linkDocument) {
        this.linkDocument = linkDocument;
    }

    public Long getQuantityUpdate() {
        return quantityUpdate;
    }

    public void setQuantityUpdate(Long quantityUpdate) {
        this.quantityUpdate = quantityUpdate;
    }

	/**
	 * @return the errorQuantity
	 */
	public Long getErrorQuantity() {
		return errorQuantity;
	}

	/**
	 * @param errorQuantity the errorQuantity to set
	 */
	public void setErrorQuantity(Long errorQuantity) {
		this.errorQuantity = errorQuantity;
	}

	/**
	 * @return the exStaffQuantity
	 */
	public Long getExStaffQuantity() {
		return exStaffQuantity;
	}

	/**
	 * @param exStaffQuantity the exStaffQuantity to set
	 */
	public void setExStaffQuantity(Long exStaffQuantity) {
		this.exStaffQuantity = exStaffQuantity;
	}

	/**
	 * @return the usedQuantity
	 */
	public Long getUsedQuantity() {
		return usedQuantity;
	}

	/**
	 * @param usedQuantity the usedQuantity to set
	 */
	public void setUsedQuantity(Long usedQuantity) {
		this.usedQuantity = usedQuantity;
	}

	/**
	 * @return the warrantyQuantity
	 */
	public Long getWarrantyQuantity() {
		return warrantyQuantity;
	}

	/**
	 * @param warrantyQuantity the warrantyQuantity to set
	 */
	public void setWarrantyQuantity(Long warrantyQuantity) {
		this.warrantyQuantity = warrantyQuantity;
	}

	/**
	 * @return the exchangedQuantity
	 */
	public Long getExchangedQuantity() {
		return exchangedQuantity;
	}

	/**
	 * @param exchangedQuantity the exchangedQuantity to set
	 */
	public void setExchangedQuantity(Long exchangedQuantity) {
		this.exchangedQuantity = exchangedQuantity;
	}

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getIndexStr() {
        return indexStr;
    }

    public void setIndexStr(String indexStr) {
        this.indexStr = indexStr;
    }

    public StockGoods(StockGoods obj) {
	    this.stockGoodsId = obj.stockGoodsId;
        this.quantity = obj.quantity;
        this.goodsStatus = obj.goodsStatus;
        this.availableQuantity = obj.availableQuantity;
        this.shopId = obj.shopId;
        this.goodsId = obj.goodsId;
        this.orderCode = obj.orderCode;
        this.profileName = obj.profileName;
        this.profileCode = obj.profileCode;
        this.equipmentsGroup = obj.equipmentsGroup;
        this.goodsStatusName = obj.goodsStatusName;
        this.check = obj.check;
        this.rowKey = obj.rowKey;
        this.quantitySold = obj.quantitySold;
        this.quantityBlock = obj.quantityBlock;
        this.sold = obj.sold;
        this.index = obj.index;
        this.indexStr = obj.indexStr;
        this.goods = obj.goods;
        this.instockErr = obj.instockErr;
        this.goodsErr = obj.goodsErr;
        this.goodsNormal = obj.goodsNormal;
        this.equipentGroupId = obj.equipentGroupId;
        this.countBlockSerial = obj.countBlockSerial;
        this.equipentGroupCode = obj.equipentGroupCode;
        this.pxkCode = obj.pxkCode;
        this.specification = obj.specification;
        this.lifeCycle = obj.lifeCycle;
        this.maintancePeriod = obj.maintancePeriod;
        this.unit = obj.unit;
        this.unitName = obj.unitName;
        this.approCode = obj.approCode;
        this.linkDocument = obj.linkDocument;
        this.quantityUpdate = obj.quantityUpdate;
        this.errorQuantity = obj.errorQuantity;
        this.exStaffQuantity = obj.exStaffQuantity;
        this.usedQuantity = obj.usedQuantity;
        this.warrantyQuantity = obj.warrantyQuantity;
        this.exchangedQuantity = obj.exchangedQuantity;
    }
}
