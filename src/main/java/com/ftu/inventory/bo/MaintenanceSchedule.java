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
@Table(name = "MAINTENANCE_SCHEDULE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MaintenanceSchedule.findAll", query = "SELECT m FROM MaintenanceSchedule m"),
    @NamedQuery(name = "MaintenanceSchedule.findByScheduleId", query = "SELECT m FROM MaintenanceSchedule m WHERE m.scheduleId = :scheduleId"),
    @NamedQuery(name = "MaintenanceSchedule.findByScheduleCode", query = "SELECT m FROM MaintenanceSchedule m WHERE m.scheduleCode = :scheduleCode"),
    @NamedQuery(name = "MaintenanceSchedule.findByScheduleName", query = "SELECT m FROM MaintenanceSchedule m WHERE m.scheduleName = :scheduleName"),
    @NamedQuery(name = "MaintenanceSchedule.findByStatus", query = "SELECT m FROM MaintenanceSchedule m WHERE m.status = :status"),
    @NamedQuery(name = "MaintenanceSchedule.findByFromDate", query = "SELECT m FROM MaintenanceSchedule m WHERE m.fromDate = :fromDate"),
    @NamedQuery(name = "MaintenanceSchedule.findByToDate", query = "SELECT m FROM MaintenanceSchedule m WHERE m.toDate = :toDate"),
    @NamedQuery(name = "MaintenanceSchedule.findByDescription", query = "SELECT m FROM MaintenanceSchedule m WHERE m.description = :description"),
    @NamedQuery(name = "MaintenanceSchedule.findByCreatedDate", query = "SELECT m FROM MaintenanceSchedule m WHERE m.createdDate = :createdDate"),
    @NamedQuery(name = "MaintenanceSchedule.findByApprovedDate", query = "SELECT m FROM MaintenanceSchedule m WHERE m.approvedDate = :approvedDate"),
    @NamedQuery(name = "MaintenanceSchedule.findByClosedDate", query = "SELECT m FROM MaintenanceSchedule m WHERE m.closedDate = :closedDate"),
    @NamedQuery(name = "MaintenanceSchedule.findByComments", query = "SELECT m FROM MaintenanceSchedule m WHERE m.comments = :comments"),
    @NamedQuery(name = "MaintenanceSchedule.findByHidden", query = "SELECT m FROM MaintenanceSchedule m WHERE m.hidden = :hidden")})
public class MaintenanceSchedule implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @SequenceGenerator(name = "SCHEDULE_SEQ", sequenceName = "SCHEDULE_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SCHEDULE_SEQ")
    @Basic(optional = false)
    @Column(name = "SCHEDULE_ID")
    private BigDecimal scheduleId;
    @Basic(optional = false)
    @Column(name = "SCHEDULE_CODE")
    private String scheduleCode;
    @Basic(optional = false)
    @Column(name = "SCHEDULE_NAME")
    private String scheduleName;
    @Basic(optional = false)
    @Column(name = "STATUS")
    private BigInteger status;
    @Column(name = "FROM_DATE")
    @Temporal(TemporalType.DATE)
    private Date fromDate;
    @Column(name = "TO_DATE")
    @Temporal(TemporalType.DATE)
    private Date toDate;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.DATE)
    private Date createdDate;
    @Column(name = "APPROVED_DATE")
    @Temporal(TemporalType.DATE)
    private Date approvedDate;
    @Column(name = "CLOSED_DATE")
    @Temporal(TemporalType.DATE)
    private Date closedDate;
    @Column(name = "COMMENTS")
    private String comments;
    @Basic(optional = false)
    @Column(name = "HIDDEN")
    private BigInteger hidden;
//    @ManyToMany(mappedBy = "maintenanceScheduleCollection")
//    private Collection<MaintenanceEquipment> maintenanceEquipmentCollection;
//    @JoinColumn(name = "SHOP_ID", referencedColumnName = "SHOP_ID")
//    @ManyToOne
    @Column(name = "SHOP_ID")
    private Long shopId;
//    @JoinColumn(name = "STAFF_ID", referencedColumnName = "STAFF_ID")
//    @ManyToOne
    @Column(name = "STAFF_ID")
    private Long staffId;

    public MaintenanceSchedule() {
    }

    public MaintenanceSchedule(BigDecimal scheduleId) {
        this.scheduleId = scheduleId;
    }

    public MaintenanceSchedule(BigDecimal scheduleId, String scheduleCode, String scheduleName, BigInteger status, BigInteger hidden) {
        this.scheduleId = scheduleId;
        this.scheduleCode = scheduleCode;
        this.scheduleName = scheduleName;
        this.status = status;
        this.hidden = hidden;
    }

    public BigDecimal getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(BigDecimal scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getScheduleCode() {
        return scheduleCode;
    }

    public void setScheduleCode(String scheduleCode) {
        this.scheduleCode = scheduleCode;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public BigInteger getStatus() {
        return status;
    }

    public void setStatus(BigInteger status) {
        this.status = status;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public Date getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(Date closedDate) {
        this.closedDate = closedDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public BigInteger getHidden() {
        return hidden;
    }

    public void setHidden(BigInteger hidden) {
        this.hidden = hidden;
    }

//    @XmlTransient
//    public Collection<MaintenanceEquipment> getMaintenanceEquipmentCollection() {
//        return maintenanceEquipmentCollection;
//    }
//
//    public void setMaintenanceEquipmentCollection(Collection<MaintenanceEquipment> maintenanceEquipmentCollection) {
//        this.maintenanceEquipmentCollection = maintenanceEquipmentCollection;
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
        hash += (scheduleId != null ? scheduleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MaintenanceSchedule)) {
            return false;
        }
        MaintenanceSchedule other = (MaintenanceSchedule) object;
        if ((this.scheduleId == null && other.scheduleId != null) || (this.scheduleId != null && !this.scheduleId.equals(other.scheduleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.MaintenanceSchedule[ scheduleId=" + scheduleId + " ]";
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
