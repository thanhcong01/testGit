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
@Table(name = "EQUIPMENT_PROFILE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EquipmentsProfile.findAll", query = "SELECT g FROM EquipmentsProfile g order by  UPPER(g.profileCode) ASC , UPPER(g.profileName) ASC "),
    @NamedQuery(name = "EquipmentsProfile.findByEquipmentsProfileId", query = "SELECT g FROM EquipmentsProfile g WHERE g.profileId = :profileId"),
    @NamedQuery(name = "EquipmentsProfile.findByEquipmentsProfileName", query = "SELECT g FROM EquipmentsProfile g WHERE g.profileName = :goodsName"),
    @NamedQuery(name = "EquipmentsProfile.findByManagementType", query = "SELECT g FROM EquipmentsProfile g WHERE g.managementType = :managementType")})
public class EquipmentsProfile implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @SequenceGenerator(name = "EQUIPMENT_PROFILE_SEQ", sequenceName = "EQUIPMENT_PROFILE_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "EQUIPMENT_PROFILE_SEQ")
    @Basic(optional = false)
    @Column(name = "PROFILE_ID")
    private Long profileId;
    @Column(name = "PROFILE_NAME")
    private String profileName;
    @Column(name = "PROFILE_CODE")
    private String profileCode;
    @Column(name = "MANAGEMENT_TYPE")
    private String managementType;
    @Column(name = "EQUIPMENT_GROUP_ID")
    private Long equipmentsGroupId;
    @Column(name = "UNIT")
    private String unit;
    @Column(name = "SPARE_PART")
    private Boolean sparePart;
    @Column(name = "MANUFACTURE")
    private String manufacture;
    @Column(name = "ORIGIN")
    private String origin;
    @Column(name = "SPECIFICATION")
    private String specification;
    @Column(name = "ICON")
    private String icon;
    @Column(name = "EQUIPMENT_PROFILE_STATUS")
    private Long equipProfileStatus;
//    @Column(name = "IS_SERIAL")
    @Transient
    private Boolean serial;
    @Transient
    private Boolean disableSerial;


    @Transient
    private EquipmentsGroup equipmentsGroup;

    public EquipmentsProfile() {
    }
    @Transient
    public EquipmentsGroup getEquipmentsGroup() {
		return equipmentsGroup;
	}
    @Transient
    public void setEquipmentsGroup(EquipmentsGroup equipmentsGroup) {
		this.equipmentsGroup = equipmentsGroup;
	}

    public EquipmentsProfile(Long goodsId) {
        this.profileId = goodsId;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long goodsId) {
        this.profileId = goodsId;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String goodsName) {
        this.profileName = goodsName;
    }

    public String getManagementType() {
        return managementType;
    }

    public void setManagementType(String managementType) {
        this.managementType = managementType;
    }

    public Long getEquipmentsGroupId() {
        return equipmentsGroupId;
    }

    public void setEquipmentsGroupId(Long goodsGroupId) {
        this.equipmentsGroupId = goodsGroupId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (profileId != null ? profileId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EquipmentsProfile)) {
            return false;
        }
        EquipmentsProfile other = (EquipmentsProfile) object;
        if ((this.profileId == null && other.profileId != null) || (this.profileId != null && !this.profileId.equals(other.profileId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return profileName;
    }

    /**
     * @return the goodsCode
     */
    public String getProfileCode() {
        return profileCode;
    }

    /**
     * @param goodsCode the goodsCode to set
     */
    public void setProfileCode(String goodsCode) {
        this.profileCode = goodsCode;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Boolean getSparePart() {
        return sparePart;
    }

    public void setSparePart(Boolean sparePart) {
        this.sparePart = sparePart;
    }

    public String getManufacture() {
        return manufacture;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getSerial() {
        if(managementType!=null&&managementType.equals("3")){
            serial = true;
            return true;
        }else{
            serial = false;
            return false;
        }
    }

    public void setSerial(Boolean serial) {
        this.serial = serial;
        if(serial){
            managementType = "3";
        }else {
            managementType = "";
        }
    }

    public Boolean getDisableSerial() {
        if(this.getProfileId()!=null){
            disableSerial = true;
            return disableSerial;
        }
        disableSerial = false;
        return disableSerial;
    }

    public Long getEquipProfileStatus() {
        return equipProfileStatus;
    }

    public void setEquipProfileStatus(Long equipProfileStatus) {
        this.equipProfileStatus = equipProfileStatus;
    }

    public void setDisableSerial(Boolean disableSerial) {
        this.disableSerial = disableSerial;
    }

    public EquipmentsProfile(EquipmentsProfile equipmentsProfile) {
        if(equipmentsProfile == null) return;
        this.profileId = equipmentsProfile.profileId;
        this.profileName = equipmentsProfile.profileName;
        this.profileCode = equipmentsProfile.profileCode;
        this.managementType = equipmentsProfile.managementType;
        this.equipmentsGroupId = equipmentsProfile.equipmentsGroupId;
        this.unit = equipmentsProfile.unit;
        this.sparePart = equipmentsProfile.sparePart;
        this.manufacture = equipmentsProfile.manufacture;
        this.origin = equipmentsProfile.origin;
        this.specification = equipmentsProfile.specification;
        this.icon = equipmentsProfile.icon;
        this.equipProfileStatus = equipmentsProfile.equipProfileStatus;
        this.serial = equipmentsProfile.serial;
        this.disableSerial = equipmentsProfile.disableSerial;
    }
}
