/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ftu.inventory.bo;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DuongThanhCong
 */
@Entity
@Table(name = "EQUIPMENT_HISTORY_DETAIL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EquipmentHistoryDetail.findAll", query = "SELECT e FROM EquipmentHistoryDetail e"),
    @NamedQuery(name = "EquipmentHistoryDetail.findByEquipmentHistoryDetailId", query = "SELECT e FROM EquipmentHistoryDetail e WHERE e.equipmentHistoryDetailId = :equipmentHistoryDetailId"),
    @NamedQuery(name = "EquipmentHistoryDetail.findByEquipmentHistoryId", query = "SELECT e FROM EquipmentHistoryDetail e WHERE e.equipmentHistoryId = :equipmentHistoryId"),
    @NamedQuery(name = "EquipmentHistoryDetail.findByFeildName", query = "SELECT e FROM EquipmentHistoryDetail e WHERE e.feildName = :feildName"),
    @NamedQuery(name = "EquipmentHistoryDetail.findByOldValue", query = "SELECT e FROM EquipmentHistoryDetail e WHERE e.oldValue = :oldValue"),
    @NamedQuery(name = "EquipmentHistoryDetail.findByNewValue", query = "SELECT e FROM EquipmentHistoryDetail e WHERE e.newValue = :newValue")})
public class EquipmentHistoryDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @SequenceGenerator(name = "EQUIPMENT_HISTORY_DETAIL_SEQ", sequenceName = "EQUIPMENT_HISTORY_DETAIL_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "EQUIPMENT_HISTORY_DETAIL_SEQ")
    @Basic(optional = false)
    @Column(name = "EQUIPMENT_HISTORY_DETAIL_ID")
    private Long equipmentHistoryDetailId;
    @Column(name = "EQUIPMENT_HISTORY_ID")
    private Long equipmentHistoryId;
    @Column(name = "FEILD_NAME")
    private String feildName;
    @Column(name = "OLD_VALUE")
    private String oldValue;
    @Column(name = "NEW_VALUE")
    private String newValue;

    public EquipmentHistoryDetail() {
    }

    public EquipmentHistoryDetail(Long equipmentHistoryDetailId) {
        this.equipmentHistoryDetailId = equipmentHistoryDetailId;
    }

    public Long getEquipmentHistoryDetailId() {
        return equipmentHistoryDetailId;
    }

    public void setEquipmentHistoryDetailId(Long equipmentHistoryDetailId) {
        this.equipmentHistoryDetailId = equipmentHistoryDetailId;
    }

    public Long getEquipmentHistoryId() {
        return equipmentHistoryId;
    }

    public void setEquipmentHistoryId(Long equipmentHistoryId) {
        this.equipmentHistoryId = equipmentHistoryId;
    }

    public String getFeildName() {
        return feildName;
    }

    public void setFeildName(String feildName) {
        this.feildName = feildName;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (equipmentHistoryDetailId != null ? equipmentHistoryDetailId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EquipmentHistoryDetail)) {
            return false;
        }
        EquipmentHistoryDetail other = (EquipmentHistoryDetail) object;
        if ((this.equipmentHistoryDetailId == null && other.equipmentHistoryDetailId != null) || (this.equipmentHistoryDetailId != null && !this.equipmentHistoryDetailId.equals(other.equipmentHistoryDetailId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.EquipmentHistoryDetail[ equipmentHistoryDetailId=" + equipmentHistoryDetailId + " ]";
    }
    
}
