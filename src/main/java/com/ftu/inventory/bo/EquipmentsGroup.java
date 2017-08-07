/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ftu.inventory.bo;

import com.ftu.language.LanguageBean;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.faces.bean.ManagedProperty;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "EQUIPMENT_GROUP")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EquipmentsGroup.findAll", query = "SELECT g FROM EquipmentsGroup g"),
    @NamedQuery(name = "EquipmentsGroup.findByEquipmentsGroupId", query = "SELECT g FROM EquipmentsGroup g WHERE g.equipmentsGroupId = :equipmentsGroupId"),
    @NamedQuery(name = "EquipmentsGroup.findByEquipmentsGroupName", query = "SELECT g FROM EquipmentsGroup g WHERE g.equipmentsGroupName = :equipmentsGroupName")})
public class EquipmentsGroup implements Serializable {
     private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @SequenceGenerator(name = "EQUIPMENTS_GROUP_SEQ", sequenceName = "EQUIPMENTS_GROUP_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "EQUIPMENTS_GROUP_SEQ")
    @Basic(optional = false)
    @Column(name = "EQUIPMENT_GROUP_ID")
    private Long equipmentsGroupId;
    @Column(name = "EQUIPMENT_GROUP_NAME")
    private String equipmentsGroupName;
    @Column(name = "EQUIPMENT_GROUP_CODE")
    private String equipmentsGroupCode;
    @Column(name = "EQUIPMENT_GROUP_TYPE")
    private Long equipmentsGroupType;
    @Column(name = "MAINTANCE_SCRIPT")
    private String maintanceScript;
    @Column(name = "EQUIPMENT_GROUP_STATUS")
    private Long equipmentsGroupStatus;
    @Transient
    private List<EquipmentsProfile> listGoods;
    @Transient
    private String equipmentsGroupTypeName;
    public EquipmentsGroup() {
    }
    @Transient
    public List<EquipmentsProfile> getListGoods() {
		return listGoods;
	}
    @Transient
    public void setListGoods(List<EquipmentsProfile> listGoods) {
		this.listGoods = listGoods;
	}
    @ManagedProperty("#{languageBean}")
    @Transient
    private LanguageBean languageBean;

    public EquipmentsGroup(Long equipmentsGroupId) {
        this.equipmentsGroupId = equipmentsGroupId;
    }

    public Long getEquipmentsGroupId() {
        return equipmentsGroupId;
    }

    public void setEquipmentsGroupId(Long equipmentsGroupId) {
        this.equipmentsGroupId = equipmentsGroupId;
    }

    public String getEquipmentsGroupName() {
        return equipmentsGroupName;
    }

    public void setEquipmentsGroupName(String equipmentsGroupName) {
        this.equipmentsGroupName = equipmentsGroupName;
    }
    
    public String getEquipmentsGroupCode() {
		return equipmentsGroupCode;
	}
    
    public void setEquipmentsGroupCode(String equipmentsGroupCode) {
		this.equipmentsGroupCode = equipmentsGroupCode;
	}

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (equipmentsGroupId != null ? equipmentsGroupId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EquipmentsGroup)) {
            return false;
        }
        EquipmentsGroup other = (EquipmentsGroup) object;
        if ((this.equipmentsGroupId == null && other.equipmentsGroupId != null) || (this.equipmentsGroupId != null && !this.equipmentsGroupId.equals(other.equipmentsGroupId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return equipmentsGroupName;
    }

    public Long getEquipmentsGroupType() {
        return equipmentsGroupType;
    }

    public void setEquipmentsGroupType(Long equipmentsGroupType) {
        this.equipmentsGroupType = equipmentsGroupType;
    }

    public String getEquipmentsGroupTypeName() {
//        if(equipmentsGroupType!=null&&equipmentsGroupType.equals(1L)){
//            return "Thiết bị";
//        }else if(equipmentsGroupType!=null&&equipmentsGroupType.equals(2L)){
//            return "Vật tư";
//        }
        return equipmentsGroupTypeName;
    }

    public void setEquipmentsGroupTypeName(String equipmentsGroupTypeName) {
        this.equipmentsGroupTypeName = equipmentsGroupTypeName;
    }

    public String getMaintanceScript() {
        return maintanceScript;
    }

    public void setMaintanceScript(String maintanceScript) {
        this.maintanceScript = maintanceScript;
    }

    public Long getEquipmentsGroupStatus() {
        return equipmentsGroupStatus;
    }

    public void setEquipmentsGroupStatus(Long equipmentsGroupStatus) {
        this.equipmentsGroupStatus = equipmentsGroupStatus;
    }

    public EquipmentsGroup(EquipmentsGroup equipmentsGroup) {
        this.equipmentsGroupName = equipmentsGroup.equipmentsGroupName;
        this.equipmentsGroupCode = equipmentsGroup.equipmentsGroupCode;
        this.equipmentsGroupType = equipmentsGroup.equipmentsGroupType;
        this.maintanceScript = equipmentsGroup.maintanceScript;
        this.equipmentsGroupStatus = equipmentsGroup.equipmentsGroupStatus;
        this.equipmentsGroupTypeName = equipmentsGroup.equipmentsGroupTypeName;
        this.languageBean = equipmentsGroup.languageBean;
    }
}
