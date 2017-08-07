/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ftu.staff.bo;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author E5420
 */
@Entity
@Table(name = "STAFF")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Staff.findAll", query = "SELECT s FROM Staff s"),
    @NamedQuery(name = "Staff.findByStaffId", query = "SELECT s FROM Staff s WHERE s.staffId = :staffId"),
    @NamedQuery(name = "Staff.findByStaffCode", query = "SELECT s FROM Staff s WHERE s.staffCode = :staffCode"),
    @NamedQuery(name = "Staff.findByUserName", query = "SELECT s FROM Staff s WHERE s.userName = :userName"),
    @NamedQuery(name = "Staff.findByStaffName", query = "SELECT s FROM Staff s WHERE s.staffName = :staffName"),
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
    private Long staffId;
    @Column(name = "STAFF_CODE")
    private String staffCode;
    @Column(name = "USER_NAME")
    private String userName;
    @Column(name = "STAFF_NAME")
    private String staffName;
    @Column(name = "ID_NO")
    private String idNo;
    @Column(name = "MOBI_NUMBER")
    private String mobiNumber;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "DOB")
    @Temporal(TemporalType.DATE)
    private Date dob;
    @Column(name = "STAFF_STATUS")
    private Long staffStatus;
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
    @Column(name = "SHOP_ID")
    private Long shopId;
    
    @Transient
    private List<Long> listShopId;

    public Staff() {
    }
    @Transient
    public List<Long> getListShopId() {
		return listShopId;
	}
    @Transient
    public void setListShopId(List<Long> listShopId) {
		this.listShopId = listShopId;
	}
    
    public Staff(Long staffId) {
        this.staffId = staffId;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
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

    public Long getStaffStatus() {
        return staffStatus;
    }

    public void setStaffStatus(Long staffStatus) {
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

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

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
        return "core.Hibernate.BO.Inventory.Staff[ staffId=" + staffId + " ]";
    }

    public Staff(Staff staff) {
        if(staff == null) return;
        this.staffId = staff.staffId;
        this.staffCode = staff.staffCode;
        this.userName = staff.userName;
        this.staffName = staff.staffName;
        this.idNo = staff.idNo;
        this.mobiNumber = staff.mobiNumber;
        this.email = staff.email;
        this.dob = staff.dob;
        this.staffStatus = staff.staffStatus;
        this.startDate = staff.startDate;
        this.endDate = staff.endDate;
        this.staffType = staff.staffType;
        this.issueDate = staff.issueDate;
        this.issuePlace = staff.issuePlace;
        this.gender = staff.gender;
        this.shopId = staff.shopId;
    }
}
