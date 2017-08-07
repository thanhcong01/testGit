/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ftu.inventory.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DuongThanhCong
 */
@Entity
@Table(name = "MAINTENANCE_SCRIPT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MaintenanceScript.findAll", query = "SELECT m FROM MaintenanceScript m"),
    @NamedQuery(name = "MaintenanceScript.findByScriptId", query = "SELECT m FROM MaintenanceScript m WHERE m.scriptId = :scriptId"),
    @NamedQuery(name = "MaintenanceScript.findByScriptCode", query = "SELECT m FROM MaintenanceScript m WHERE m.scriptCode = :scriptCode"),
    @NamedQuery(name = "MaintenanceScript.findByScriptName", query = "SELECT m FROM MaintenanceScript m WHERE m.scriptName = :scriptName"),
    @NamedQuery(name = "MaintenanceScript.findByCreatedDate", query = "SELECT m FROM MaintenanceScript m WHERE m.createdDate = :createdDate"),
    @NamedQuery(name = "MaintenanceScript.findByContent", query = "SELECT m FROM MaintenanceScript m WHERE m.content = :content")})
public class MaintenanceScript implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @SequenceGenerator(name = "SCRIPT_SEQ", sequenceName = "SCRIPT_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SCRIPT_SEQ")
    @Basic(optional = false)
    @Column(name = "SCRIPT_ID")
    private BigDecimal scriptId;
    @Basic(optional = false)
    @Column(name = "SCRIPT_CODE")
    private String scriptCode;
    @Basic(optional = false)
    @Column(name = "SCRIPT_NAME")
    private String scriptName;
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.DATE)
    private Date createdDate;
    @Column(name = "CONTENT")
    private String content;
//    @JoinColumn(name = "EQUIPMENT_GROUP_ID", referencedColumnName = "EQUIPMENT_GROUP_ID")
//    @ManyToOne
    @Column(name = "EQUIPMENT_GROUP_ID")
    private Long equipmentGroupId;
//    @JoinColumn(name = "SHOP_ID", referencedColumnName = "SHOP_ID")
//    @ManyToOne
    @Column(name = "SHOP_ID")
    private Long shopId;
//    @JoinColumn(name = "STAFF_ID", referencedColumnName = "STAFF_ID")
//    @ManyToOne
    @Column(name = "STAFF_ID")
    private Long staffId;

    public MaintenanceScript() {
    }

    public MaintenanceScript(BigDecimal scriptId) {
        this.scriptId = scriptId;
    }

    public MaintenanceScript(BigDecimal scriptId, String scriptCode, String scriptName) {
        this.scriptId = scriptId;
        this.scriptCode = scriptCode;
        this.scriptName = scriptName;
    }

    public BigDecimal getScriptId() {
        return scriptId;
    }

    public void setScriptId(BigDecimal scriptId) {
        this.scriptId = scriptId;
    }

    public String getScriptCode() {
        return scriptCode;
    }

    public void setScriptCode(String scriptCode) {
        this.scriptCode = scriptCode;
    }

    public String getScriptName() {
        return scriptName;
    }

    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

//    public EquipmentGroup getEquipmentGroupId() {
//        return equipmentGroupId;
//    }
//
//    public void setEquipmentGroupId(EquipmentGroup equipmentGroupId) {
//        this.equipmentGroupId = equipmentGroupId;
//    }
//
//    public Shop getShopId() {
//        return shopId;
//    }
//
//    public void setShopId(Shop shopId) {
//        this.shopId = shopId;
//    }
//
//    public Staff getStaffId() {
//        return staffId;
//    }
//
//    public void setStaffId(Staff staffId) {
//        this.staffId = staffId;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (scriptId != null ? scriptId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MaintenanceScript)) {
            return false;
        }
        MaintenanceScript other = (MaintenanceScript) object;
        if ((this.scriptId == null && other.scriptId != null) || (this.scriptId != null && !this.scriptId.equals(other.scriptId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.MaintenanceScript[ scriptId=" + scriptId + " ]";
    }

    public Long getEquipmentGroupId() {
        return equipmentGroupId;
    }

    public void setEquipmentGroupId(Long equipmentGroupId) {
        this.equipmentGroupId = equipmentGroupId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }
}
