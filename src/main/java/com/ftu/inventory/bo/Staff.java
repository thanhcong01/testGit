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
@Table(name = "STAFF")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Staff.findAll", query = "SELECT s FROM Staff s"),
    @NamedQuery(name = "Staff.findByStaffId", query = "SELECT s FROM Staff s WHERE s.staffId = :staffId"),
    @NamedQuery(name = "Staff.findByStaffCode", query = "SELECT s FROM Staff s WHERE s.staffCode = :staffCode"),
    @NamedQuery(name = "Staff.findByStaffName", query = "SELECT s FROM Staff s WHERE s.staffName = :staffName"),
    @NamedQuery(name = "Staff.findByUserName", query = "SELECT s FROM Staff s WHERE s.userName = :userName"),
    @NamedQuery(name = "Staff.findByIdNo", query = "SELECT s FROM Staff s WHERE s.idNo = :idNo"),
    @NamedQuery(name = "Staff.findByMobiNumber", query = "SELECT s FROM Staff s WHERE s.mobiNumber = :mobiNumber"),
    @NamedQuery(name = "Staff.findByEmail", query = "SELECT s FROM Staff s WHERE s.email = :email"),
    @NamedQuery(name = "Staff.findByDob", query = "SELECT s FROM Staff s WHERE s.dob = :dob"),
    @NamedQuery(name = "Staff.findByStaffStatus", query = "SELECT s FROM Staff s WHERE s.staffStatus = :staffStatus"),
    @NamedQuery(name = "Staff.findByStartDate", query = "SELECT s FROM Staff s WHERE s.startDate = :startDate"),
    @NamedQuery(name = "Staff.findByEndDate", query = "SELECT s FROM Staff s WHERE s.endDate = :endDate"),
    @NamedQuery(name = "Staff.findByStaffType", query = "SELECT s FROM Staff s WHERE s.staffType = :staffType"),
    @NamedQuery(name = "Staff.findByIssueDate", query = "SELECT s FROM Staff s WHERE s.issueDate = :issueDate"),
    @NamedQuery(name = "Staff.findByIssuePlace", query = "SELECT s FROM Staff s WHERE s.issuePlace = :issuePlace"),
    @NamedQuery(name = "Staff.findByGender", query = "SELECT s FROM Staff s WHERE s.gender = :gender")})
public class Staff implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @SequenceGenerator(name = "STAFF_SEQ", sequenceName = "STAFF_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "STAFF_SEQ")
    @Basic(optional = false)
    @Column(name = "STAFF_ID")
    private BigDecimal staffId;
    @Column(name = "STAFF_CODE")
    private String staffCode;
    @Column(name = "STAFF_NAME")
    private String staffName;
    @Column(name = "USER_NAME")
    private String userName;
    @Column(name = "ID_NO")
    private String idNo;
    @Column(name = "MOBI_NUMBER")
    private String mobiNumber;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "DOB")
    @Temporal(TemporalType.DATE)
    private Date dob;
    @Basic(optional = false)
    @Column(name = "STAFF_STATUS")
    private BigInteger staffStatus;
    @Column(name = "START_DATE")
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Column(name = "END_DATE")
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Column(name = "STAFF_TYPE")
    private String staffType;
    @Column(name = "ISSUE_DATE")
    @Temporal(TemporalType.DATE)
    private Date issueDate;
    @Column(name = "ISSUE_PLACE")
    private String issuePlace;
    @Column(name = "GENDER")
    private String gender;
//    @OneToMany(mappedBy = "staffId")
//    private Collection<MaintenanceScript> maintenanceScriptCollection;
//    @JoinColumn(name = "SHOP_ID", referencedColumnName = "SHOP_ID")
//    @ManyToOne
//    @OneToMany(mappedBy = "staffId")
    @Column(name = "SHOP_ID")
    private Long shopId;
//    @OneToMany(mappedBy = "staffId")
//    private Collection<MaintenanceSchedule> maintenanceScheduleCollection;

    public Staff() {
    }

    public Staff(BigDecimal staffId) {
        this.staffId = staffId;
    }

    public Staff(BigDecimal staffId, BigInteger staffStatus) {
        this.staffId = staffId;
        this.staffStatus = staffStatus;
    }

    public BigDecimal getStaffId() {
        return staffId;
    }

    public void setStaffId(BigDecimal staffId) {
        this.staffId = staffId;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getMobiNumber() {
        return mobiNumber;
    }

    public void setMobiNumber(String mobiNumber) {
        this.mobiNumber = mobiNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public BigInteger getStaffStatus() {
        return staffStatus;
    }

    public void setStaffStatus(BigInteger staffStatus) {
        this.staffStatus = staffStatus;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStaffType() {
        return staffType;
    }

    public void setStaffType(String staffType) {
        this.staffType = staffType;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getIssuePlace() {
        return issuePlace;
    }

    public void setIssuePlace(String issuePlace) {
        this.issuePlace = issuePlace;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

//    @XmlTransient
//    public Collection<MaintenanceScript> getMaintenanceScriptCollection() {
//        return maintenanceScriptCollection;
//    }
//
//    public void setMaintenanceScriptCollection(Collection<MaintenanceScript> maintenanceScriptCollection) {
//        this.maintenanceScriptCollection = maintenanceScriptCollection;
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
//    @XmlTransient
//    public Collection<MaintenanceSchedule> getMaintenanceScheduleCollection() {
//        return maintenanceScheduleCollection;
//    }
//
//    public void setMaintenanceScheduleCollection(Collection<MaintenanceSchedule> maintenanceScheduleCollection) {
//        this.maintenanceScheduleCollection = maintenanceScheduleCollection;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (staffId != null ? staffId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Staff)) {
            return false;
        }
        Staff other = (Staff) object;
        if ((this.staffId == null && other.staffId != null) || (this.staffId != null && !this.staffId.equals(other.staffId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.Staff[ staffId=" + staffId + " ]";
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

}
