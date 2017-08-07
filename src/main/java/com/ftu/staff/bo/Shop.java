/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.staff.bo;

import com.ftu.constanst.InventoryConstanst;
import com.ftu.language.LanguageBean;
import com.ftu.staff.bean.ShopBean;

import java.io.Serializable;
import java.util.*;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author E5420
 */
@Entity
@Table(name = "SHOP")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Shop.findAll", query = "SELECT s FROM Shop s"),
    @NamedQuery(name = "Shop.findByShopId", query = "SELECT s FROM Shop s WHERE s.shopId = :shopId"),
    @NamedQuery(name = "Shop.findByShopCode", query = "SELECT s FROM Shop s WHERE s.shopCode = :shopCode"),
    @NamedQuery(name = "Shop.findByShopParentId", query = "SELECT s FROM Shop s WHERE s.shopParentId = :shopParentId"),
    @NamedQuery(name = "Shop.findByShopName", query = "SELECT s FROM Shop s WHERE s.shopName = :shopName"),
    @NamedQuery(name = "Shop.findByShopStatus", query = "SELECT s FROM Shop s WHERE s.shopStatus = :shopStatus"),
    @NamedQuery(name = "Shop.findByAddress", query = "SELECT s FROM Shop s WHERE s.address = :address"),
    @NamedQuery(name = "Shop.findByShopType", query = "SELECT s FROM Shop s WHERE s.shopType = :shopType"),
    @NamedQuery(name = "Shop.findByContactName", query = "SELECT s FROM Shop s WHERE s.contactName = :contactName"),
    @NamedQuery(name = "Shop.findByContactTitle", query = "SELECT s FROM Shop s WHERE s.contactTitle = :contactTitle"),
    @NamedQuery(name = "Shop.findByTelNumber", query = "SELECT s FROM Shop s WHERE s.telNumber = :telNumber"),
    @NamedQuery(name = "Shop.findByFax", query = "SELECT s FROM Shop s WHERE s.fax = :fax"),
    @NamedQuery(name = "Shop.findByEmail", query = "SELECT s FROM Shop s WHERE s.email = :email"),
    @NamedQuery(name = "Shop.findByDescription", query = "SELECT s FROM Shop s WHERE s.description = :description"),
    @NamedQuery(name = "Shop.findByProvince", query = "SELECT s FROM Shop s WHERE s.province = :province")})
public class Shop implements Serializable {

    @Column(name = "SHOP_PARENT_ID")
    private Long shopParentId;
    @Column(name = "SHOP_STATUS")
    private Long shopStatus;
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @SequenceGenerator(name = "SHOP_SEQ", sequenceName = "SHOP_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SHOP_SEQ")
    @Basic(optional = false)
    @Column(name = "SHOP_ID")
    private Long shopId;
    @Column(name = "SHOP_CODE")
    private String shopCode;
    @Column(name = "SHOP_NAME")
    private String shopName;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "SHOP_TYPE")
    private String shopType;
    @Column(name = "CONTACT_NAME")
    private String contactName;
    @Column(name = "CONTACT_TITLE")
    private String contactTitle;
    @Column(name = "TEL_NUMBER")
    private String telNumber;
    @Column(name = "FAX")
    private String fax;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "PROVINCE")
    private String province;
    @Column(name = "LATITUDE")
    private double latitude;
    @Column(name = "LONGITUDE")
    private double longitude;
    @Column(name = "CODE_PATH")
    private String codePath;
    @Column(name = "STATUS_CANCEL")
    private Boolean statusCancel;
    @Transient
    private Long statusCancelCbb;
    @Transient
    private Shop parentShop;
    @Transient
    private List<Shop> childShops = new ArrayList<Shop>();
    @Transient
    private String statusName;

    public Shop() {
    }

    public Shop(Long shopId) {
        this.shopId = shopId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public Long getShopParentId() {
        return shopParentId;
    }

    public void setShopParentId(Long shopParentId) {
        this.shopParentId = shopParentId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Long getShopStatus() {
        return shopStatus;
    }

    public void setShopStatus(Long shopStatus) {
        this.shopStatus = shopStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactTitle() {
        return contactTitle;
    }

    public void setContactTitle(String contactTitle) {
        this.contactTitle = contactTitle;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (shopId != null ? shopId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Shop)) {
            return false;
        }
        Shop other = (Shop) object;
        if ((this.shopId == null && other.shopId != null) || (this.shopId != null && !this.shopId.equals(other.shopId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "core.Hibernate.BO.Inventory.Shop[ shopId=" + shopId + " ]";
    }

    /**
     * @return the parentShop
     */
    public Shop getParentShop() {
        return parentShop;
    }

    /**
     * @param parentShop the parentShop to set
     */
    public void setParentShop(Shop parentShop) {
        this.parentShop = parentShop;
    }

    /**
     * @return the childShops
     */
    public List<Shop> getChildShops() {
        if(childShops!=null && !childShops.isEmpty()){
            Collections.sort(childShops, new Comparator(){
			public int compare(Object o1, Object o2) {
				Shop p1 = (Shop) o1;
				Shop p2 = (Shop) o2;
				return (p1.getShopCode() + " - "+ p1.getShopName()).compareToIgnoreCase((p2.getShopCode() + " - "+ p2.getShopName()));
			}
		});
        }
        return childShops;
    }

    /**
     * @param childShops the childShops to set
     */
    public void setChildShops(List<Shop> childShops) {
        this.childShops = childShops;
    }

    public String getCodePath() {
        return codePath;
    }

    public void setCodePath(String codePath) {
        this.codePath = codePath;
    }

    public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public Shop(Long shopId,String shopCode, String shopName, String address, String contactName, String telNumber) {
        this.shopId = shopId;
        this.shopCode = shopCode;
        this.shopName = shopName;
        this.address = address;
        this.contactName = contactName;
        this.telNumber = telNumber;
    }

    public Boolean getStatusCancel() {
        return statusCancel;
    }

    public void setStatusCancel(Boolean statusCancel) {
        this.statusCancel = statusCancel;
    }

    public Long getStatusCancelCbb() {
        return statusCancelCbb;
    }

    public void setStatusCancelCbb(Long statusCancelCbb) {
        this.statusCancelCbb = statusCancelCbb;
        if(statusCancelCbb==null || statusCancelCbb < 0L){
            statusCancel = null;
        }else if(statusCancelCbb.equals(1L)){
            statusCancel = true;
        }else {
            statusCancel = false;
        }
    }

    public String getStatusName() {
//        ShopBean shopBean = new ShopBean();
        LanguageBean languageBean = new LanguageBean();
        if(InventoryConstanst.ProviderStatus.ACTIVE.equals(shopStatus)){
            statusName = languageBean.getMessage("common.status.1",null,false);
        }else {
            statusName = languageBean.getMessage("common.status.0",null,false);
        }
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Shop(Shop shop) {
        if(shop == null){
            return;
        }
        this.shopId = shop.shopId;
        this.shopParentId = shop.shopParentId;
        this.shopStatus = shop.shopStatus;
        this.shopCode = shop.shopCode;
        this.shopName = shop.shopName;
        this.address = shop.address;
        this.shopType = shop.shopType;
        this.contactName = shop.contactName;
        this.contactTitle = shop.contactTitle;
        this.telNumber = shop.telNumber;
        this.fax = shop.fax;
        this.email = shop.email;
        this.description = shop.description;
        this.province = shop.province;
        this.latitude = shop.latitude;
        this.longitude = shop.longitude;
        this.codePath = shop.codePath;
        this.statusCancel = shop.statusCancel;
        this.statusCancelCbb = shop.statusCancelCbb;
        this.parentShop = null;
        this.childShops = null;
        this.statusName = shop.statusName == null? "": shop.statusName ;
    }
}
