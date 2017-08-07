/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ftu.inventory.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author DuongThanhCong
 */
@Entity
@Table(name = "MAINTENANCE_EQUIPMENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MaintenanceEquipment.findAll", query = "SELECT m FROM MaintenanceEquipment m"),
    @NamedQuery(name = "MaintenanceEquipment.findByMaintenanceEquipmentId", query = "SELECT m FROM MaintenanceEquipment m WHERE m.maintenanceEquipmentId = :maintenanceEquipmentId"),
    @NamedQuery(name = "MaintenanceEquipment.findByMaintenanceCount", query = "SELECT m FROM MaintenanceEquipment m WHERE m.maintenanceCount = :maintenanceCount"),
    @NamedQuery(name = "MaintenanceEquipment.findByLastMaintainDate", query = "SELECT m FROM MaintenanceEquipment m WHERE m.lastMaintainDate = :lastMaintainDate")})
public class MaintenanceEquipment implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @SequenceGenerator(name = "MAINTENANCE_EQUIPMENT_SEQ", sequenceName = "MAINTENANCE_EQUIPMENT_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "MAINTENANCE_EQUIPMENT_SEQ")
    @Basic(optional = false)
    @Column(name = "MAINTENANCE_EQUIPMENT_ID")
    private BigDecimal maintenanceEquipmentId;
    @Column(name = "MAINTENANCE_COUNT")
    private BigInteger maintenanceCount;
    @Column(name = "LAST_MAINTAIN_DATE")
    @Temporal(TemporalType.DATE)
    private Date lastMaintainDate;
//    @JoinTable(name = "MAINTENANCE_SCHEDULE_EQUIPMENT", joinColumns = {
//        @JoinColumn(name = "MAINTENANCE_EQUIPMENT_ID", referencedColumnName = "MAINTENANCE_EQUIPMENT_ID")}, inverseJoinColumns = {
//        @JoinColumn(name = "SCHEDULE_ID", referencedColumnName = "SCHEDULE_ID")})
//    @ManyToMany
////    private Collection<MaintenanceSchedule> maintenanceScheduleCollection;
//    private Long MaintenanceScheduleId;
    @Column(name = "EQUIPMENT_ID")
    private Long equipmentId;

    public MaintenanceEquipment() {
    }

    public MaintenanceEquipment(BigDecimal maintenanceEquipmentId) {
        this.maintenanceEquipmentId = maintenanceEquipmentId;
    }

    public BigDecimal getMaintenanceEquipmentId() {
        return maintenanceEquipmentId;
    }

    public void setMaintenanceEquipmentId(BigDecimal maintenanceEquipmentId) {
        this.maintenanceEquipmentId = maintenanceEquipmentId;
    }

    public BigInteger getMaintenanceCount() {
        return maintenanceCount;
    }

    public void setMaintenanceCount(BigInteger maintenanceCount) {
        this.maintenanceCount = maintenanceCount;
    }

    public Date getLastMaintainDate() {
        return lastMaintainDate;
    }

    public void setLastMaintainDate(Date lastMaintainDate) {
        this.lastMaintainDate = lastMaintainDate;
    }

//    @XmlTransient
//    public Collection<MaintenanceSchedule> getMaintenanceScheduleCollection() {
//        return maintenanceScheduleCollection;
//    }
//
//    public void setMaintenanceScheduleCollection(Collection<MaintenanceSchedule> maintenanceScheduleCollection) {
//        this.maintenanceScheduleCollection = maintenanceScheduleCollection;
//    }
//
//    public EquipmentDetail getEquipmentId() {
//        return equipmentId;
//    }
//
//    public void setEquipmentId(EquipmentDetail equipmentId) {
//        this.equipmentId = equipmentId;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (maintenanceEquipmentId != null ? maintenanceEquipmentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MaintenanceEquipment)) {
            return false;
        }
        MaintenanceEquipment other = (MaintenanceEquipment) object;
        if ((this.maintenanceEquipmentId == null && other.maintenanceEquipmentId != null) || (this.maintenanceEquipmentId != null && !this.maintenanceEquipmentId.equals(other.maintenanceEquipmentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.MaintenanceEquipment[ maintenanceEquipmentId=" + maintenanceEquipmentId + " ]";
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }
}
