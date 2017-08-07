/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ftu.inventory.bo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DuongThanhCong
 */
@Entity
@Table(name = "EQUIPMENT_HISTORY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EquipmentHistory.findAll", query = "SELECT e FROM EquipmentHistory e"),
    @NamedQuery(name = "EquipmentHistory.findByEquipmentHistoryId", query = "SELECT e FROM EquipmentHistory e WHERE e.equipmentHistoryId = :equipmentHistoryId"),
    @NamedQuery(name = "EquipmentHistory.findByEquipmentId", query = "SELECT e FROM EquipmentHistory e WHERE e.equipmentId = :equipmentId"),
    @NamedQuery(name = "EquipmentHistory.findByCreatedDatetime", query = "SELECT e FROM EquipmentHistory e WHERE e.createdDatetime = :createdDatetime"),
    @NamedQuery(name = "EquipmentHistory.findByStaffId", query = "SELECT e FROM EquipmentHistory e WHERE e.staffId = :staffId"),
    @NamedQuery(name = "EquipmentHistory.findByShopId", query = "SELECT e FROM EquipmentHistory e WHERE e.shopId = :shopId"),
    @NamedQuery(name = "EquipmentHistory.findByReferenceId", query = "SELECT e FROM EquipmentHistory e WHERE e.referenceId = :referenceId"),
    @NamedQuery(name = "EquipmentHistory.findByDocument", query = "SELECT e FROM EquipmentHistory e WHERE e.document = :document")})
public class EquipmentHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    @SequenceGenerator(name = "EQUIPMENT_HISTORY_SEQ", sequenceName = "EQUIPMENT_HISTORY_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "EQUIPMENT_HISTORY_SEQ")
    @Basic(optional = false)
    @Column(name = "EQUIPMENT_HISTORY_ID")
    private Long equipmentHistoryId;
    @Column(name = "EQUIPMENT_ID")
    private Long equipmentId;
    @Column(name = "CREATED_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDatetime;
    @Column(name = "STAFF_ID")
    private Long staffId;
    @Column(name = "SHOP_ID")
    private Long shopId;
    @Column(name = "REFERENCE_ID")
    private String referenceId;
    @Column(name = "DOCUMENT")
    private String document;

    @Column(name = "NOTE")
    private String note;
    @Transient
    private Long actionAuditId;
    @Transient
    private Long actionType;
    @Transient
    private Date actionDate;
    @Transient
    private Long positionId;
    @Transient
    private Long equipmentStatus;
    @Transient
    private String specification;
    @Transient
    private Long maintancePerio;
    @Transient
    private Long lifeCycle;
    @Transient
    private String shopCode;
    @Transient
    private String shopName;
    @Transient
    private String positionCode;
    @Transient
    private String positionName;
    @Transient
    private String lanCode;
    @Transient
    private Long shopIdHis;
    @Transient
    private String actionDateStr;
    @Transient
    private Long equipError;
    @Transient
    private String createDateStr;
    @Transient
    private String actionAuditDateStr;
    @Transient
    private String rowKey;
    @Transient
    private boolean notSerial = false;
    public EquipmentHistory() {
    }

    public EquipmentHistory(Long equipmentHistoryId) {
        this.equipmentHistoryId = equipmentHistoryId;
    }

    public Long getEquipmentHistoryId() {
        return equipmentHistoryId;
    }

    public void setEquipmentHistoryId(Long equipmentHistoryId) {
        this.equipmentHistoryId = equipmentHistoryId;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Date getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(Date createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (equipmentHistoryId != null ? equipmentHistoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EquipmentHistory)) {
            return false;
        }
        EquipmentHistory other = (EquipmentHistory) object;
        if ((this.equipmentHistoryId == null && other.equipmentHistoryId != null) || (this.equipmentHistoryId != null && !this.equipmentHistoryId.equals(other.equipmentHistoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.EquipmentHistory[ equipmentHistoryId=" + equipmentHistoryId + " ]";
    }

    public Long getActionAuditId() {
        return actionAuditId;
    }

    public void setActionAuditId(Long actionAuditId) {
        this.actionAuditId = actionAuditId;
    }

    public Long getActionType() {
        return actionType;
    }

    public void setActionType(Long actionType) {
        this.actionType = actionType;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public Long getEquipmentStatus() {
        return equipmentStatus;
    }

    public void setEquipmentStatus(Long equipmentStatus) {
        this.equipmentStatus = equipmentStatus;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public Long getMaintancePerio() {
        return maintancePerio;
    }

    public void setMaintancePerio(Long maintancePerio) {
        this.maintancePerio = maintancePerio;
    }

    public Long getLifeCycle() {
        return lifeCycle;
    }

    public void setLifeCycle(Long lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getPositionCode() {
        return positionCode;
    }

    public void setPositionCode(String positionCode) {
        this.positionCode = positionCode;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getLanCode() {
        return lanCode;
    }

    public void setLanCode(String lanCode) {
        this.lanCode = lanCode;
    }

    public String getActionDateStr() {
        if(actionDate!=null){
            return new SimpleDateFormat("dd/MM/yyyy").format(actionDate);
        }
        return actionDateStr;
    }

    public void setActionDateStr(String actionDateStr) {
        this.actionDateStr = actionDateStr;
    }

    public Long getShopIdHis() {
        return shopIdHis;
    }

    public void setShopIdHis(Long shopIdHis) {
        this.shopIdHis = shopIdHis;
    }

    public Long getEquipError() {
        return equipError;
    }

    public void setEquipError(Long equipError) {
        this.equipError = equipError;
    }

    public String getRowKey() {
        if(rowKey==null || rowKey.isEmpty()){
            rowKey = this.equipmentHistoryId + "_" + this.equipmentId + (new SimpleDateFormat("hh_mm_ss").format(new Date()));
        }
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public String getCreateDateStr() {
        if(createdDatetime!=null){
            return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(createdDatetime);
        }
        return createDateStr;
    }

    public String getCreateDateStr2() {
        if(createdDatetime!=null){
            return new SimpleDateFormat("yyyy/MM/dd HH:mm").format(createdDatetime);
        }
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public String getActionAuditDateStr() {
        if(createdDatetime!=null){
            return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(createdDatetime);
        }
        return actionAuditDateStr;
    }

    public void setActionAuditDateStr(String actionAuditDateStr) {
        this.actionAuditDateStr = actionAuditDateStr;
    }

    public boolean isNotSerial() {
        return notSerial;
    }

    public void setNotSerial(boolean notSerial) {
        this.notSerial = notSerial;
    }
}
