/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author E5420
 */
@Entity
@Table(name = "TRANSACTION_ACTION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TransactionAction.findAll", query = "SELECT t FROM TransactionAction t"),
    @NamedQuery(name = "TransactionAction.findByTransactionActionId", query = "SELECT t FROM TransactionAction t WHERE t.transactionActionId = :transactionActionId"),
    @NamedQuery(name = "TransactionAction.findByTransactionActionCode", query = "SELECT t FROM TransactionAction t WHERE t.transactionActionCode = :transactionActionCode"),
    @NamedQuery(name = "TransactionAction.findByTransactionType", query = "SELECT t FROM TransactionAction t WHERE t.transactionType = :transactionType"),
    @NamedQuery(name = "TransactionAction.findByCreateStaffId", query = "SELECT t FROM TransactionAction t WHERE t.createStaffId = :createStaffId"),
    @NamedQuery(name = "TransactionAction.findByCreateDatetime", query = "SELECT t FROM TransactionAction t WHERE t.createDatetime = :createDatetime"),
    @NamedQuery(name = "TransactionAction.findByAssessmentStaffId", query = "SELECT t FROM TransactionAction t WHERE t.assessmentStaffId = :assessmentStaffId"),
    @NamedQuery(name = "TransactionAction.findByAssessmentDatetime", query = "SELECT t FROM TransactionAction t WHERE t.assessmentDatetime = :assessmentDatetime"),
    @NamedQuery(name = "TransactionAction.findByDescription", query = "SELECT t FROM TransactionAction t WHERE t.description = :description"),
    @NamedQuery(name = "TransactionAction.findByStatus", query = "SELECT t FROM TransactionAction t WHERE t.status = :status"),
    @NamedQuery(name = "TransactionAction.findByCreateShopId", query = "SELECT t FROM TransactionAction t WHERE t.createShopId = :createShopId"),
    @NamedQuery(name = "TransactionAction.findByFromShopId", query = "SELECT t FROM TransactionAction t WHERE t.fromShopId = :fromShopId"),
    @NamedQuery(name = "TransactionAction.findByToShopId", query = "SELECT t FROM TransactionAction t WHERE t.toShopId = :toShopId"),
    @NamedQuery(name = "TransactionAction.findByReasonId", query = "SELECT t FROM TransactionAction t WHERE t.reasonId = :reasonId")})
public class TransactionAction implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @SequenceGenerator(name = "TRANSACTION_ACTION_SEQ", sequenceName = "TRANSACTION_ACTION_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "TRANSACTION_ACTION_SEQ")
    @Basic(optional = false)
    @Column(name = "TRANSACTION_ACTION_ID")
    private Long transactionActionId;
    @Column(name = "TRANSACTION_ACTION_CODE")
    private String transactionActionCode;
    @Column(name = "TRANSACTION_TYPE")
    private String transactionType;
    @Column(name = "CREATE_STAFF_ID")
    private Long createStaffId;
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;
    @Column(name = "ASSESSMENT_STAFF_ID")
    private Long assessmentStaffId;
    @Column(name = "ASSESSMENT_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date assessmentDatetime;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "CREATE_SHOP_ID")
    private Long createShopId;
    @Column(name = "FROM_SHOP_ID")
    private Long fromShopId;
    @Column(name = "TO_SHOP_ID")
    private Long toShopId;
    @Column(name = "REASON_ID")
    private Long reasonId;
    @Transient
    private List<TransactionActionDetail> lsDetail;
    @Transient
    private List<StockTransaction> lsStock;
    @Transient
    private String staffName;
    @Transient
    private String reasonName;
    @Transient
    private String typeName;
    @Transient
    private String statusName;
    @Transient
    private String shopName;
    @Transient
    private String toShop;
    @Transient
    private String createDateString;
    @Transient
    private String assDateString;
    @Transient
    private String assStaffName;
    @Transient
    private String importStaff;
    @Transient
    private String exportStaff;
    @Transient
    private String importDate;
    @Transient
    private String exportDate;
    @Transient
    private Date fromDate;
    @Transient
    private Date toDate;
    @Transient
    private Long index;
    @Transient
    private Long quantity;
    @Transient
    private String goodsGroupName;
    @Transient
    private String goodsName;
    @Transient
    private String goodsCode;
    @Transient
    private String referenceCode;
    @Transient
    private String codePath;
    
    public TransactionAction() {
    }

    public TransactionAction(Long transactionActionId) {
        this.transactionActionId = transactionActionId;
    }

    public TransactionAction(String code, String type, Long cstaffId, Long cshopId, String status, Long fShopId, Long tShopId) {
        this.transactionActionCode = code;
        this.transactionType = type;
        this.createDatetime = new Date();
        this.createStaffId = cstaffId;
        this.createShopId = cshopId;
        this.status = status;
        this.fromShopId = fShopId;
        this.toShopId = tShopId;
    }

    public Long getTransactionActionId() {
        return transactionActionId;
    }

    public void setTransactionActionId(Long transactionActionId) {
        this.transactionActionId = transactionActionId;
    }

    public String getTransactionActionCode() {
        return transactionActionCode;
    }

    public void setTransactionActionCode(String transactionActionCode) {
        this.transactionActionCode = transactionActionCode;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Long getCreateStaffId() {
        return createStaffId;
    }

    public void setCreateStaffId(Long createStaffId) {
        this.createStaffId = createStaffId;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Long getAssessmentStaffId() {
        return assessmentStaffId;
    }

    public void setAssessmentStaffId(Long assessmentStaffId) {
        this.assessmentStaffId = assessmentStaffId;
    }

    public Date getAssessmentDatetime() {
        return assessmentDatetime;
    }

    public void setAssessmentDatetime(Date assessmentDatetime) {
        this.assessmentDatetime = assessmentDatetime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCreateShopId() {
        return createShopId;
    }

    public void setCreateShopId(Long createShopId) {
        this.createShopId = createShopId;
    }

    public Long getFromShopId() {
        return fromShopId;
    }

    public void setFromShopId(Long fromShopId) {
        this.fromShopId = fromShopId;
    }

    public Long getToShopId() {
        return toShopId;
    }

    public void setToShopId(Long toShopId) {
        this.toShopId = toShopId;
    }

    public Long getReasonId() {
        return reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transactionActionId != null ? transactionActionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransactionAction)) {
            return false;
        }
        TransactionAction other = (TransactionAction) object;
        if ((this.transactionActionId == null && other.transactionActionId != null) || (this.transactionActionId != null && !this.transactionActionId.equals(other.transactionActionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "core.Hibernate.BO.Inventory.TransactionAction[ transactionActionId=" + transactionActionId + " ]";
    }

    /**
     * @return the lsDetail
     */
    public List<TransactionActionDetail> getLsDetail() {
        if (lsDetail == null) {
            lsDetail = new ArrayList<>();
        }
        return lsDetail;
    }

    /**
     * @param lsDetail the lsDetail to set
     */
    public void setLsDetail(List<TransactionActionDetail> lsDetail) {
        this.lsDetail = lsDetail;
    }

    /**
     * @return the lsStock
     */
    public List<StockTransaction> getLsStock() {
        return lsStock;
    }

    /**
     * @param lsStock the lsStock to set
     */
    public void setLsStock(List<StockTransaction> lsStock) {
        this.lsStock = lsStock;
    }

    /**
     * @return the staffName
     */
    public String getStaffName() {
        return staffName;
    }

    /**
     * @param staffName the staffName to set
     */
    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    /**
     * @return the reasonName
     */
    public String getReasonName() {
        return reasonName;
    }

    /**
     * @param reasonName the reasonName to set
     */
    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }

    /**
     * @return the statusName
     */
    public String getStatusName() {
        return statusName;
    }

    /**
     * @param statusName the statusName to set
     */
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    /**
     * @return the shopName
     */
    public String getShopName() {
        return shopName;
    }

    /**
     * @param shopName the shopName to set
     */
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    /**
     * @return the createDateString
     */
    public String getCreateDateString() {
        if (createDatetime == null) {
            return "";
        }
        SimpleDateFormat fomat = new SimpleDateFormat("dd/MM/yyyy");
        return fomat.format(createDatetime);
    }

    /**
     * @param createDateString the createDateString to set
     */
    public void setCreateDateString(String createDateString) {
        this.createDateString = createDateString;
    }
    public String getCreateDateString2() {
        if (createDatetime == null) {
            return "";
        }
        SimpleDateFormat fomat = new SimpleDateFormat("yyyy/MM/dd");
        return fomat.format(createDatetime);
    }
    /**
     * @return the toShop
     */
    public String getToShop() {
        return toShop;
    }

    /**
     * @param toShop the toShop to set
     */
    public void setToShop(String toShop) {
        this.toShop = toShop;
    }

    /**
     * @return the assDateString
     */
//    public String getAssDateString() {
//        if (assessmentDatetime == null) {
//            return "";
//        }
//        SimpleDateFormat fomat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");;
//        return fomat.format(assessmentDatetime);
//    }
    public String getAssDateString() {
        if (assessmentDatetime == null) {
            return "";
        }
        SimpleDateFormat fomat = new SimpleDateFormat("dd/MM/yyyy");;
        return fomat.format(assessmentDatetime);
    }

    /**
     * @param assDateString the assDateString to set
     */
    public void setAssDateString(String assDateString) {
        this.assDateString = assDateString;
    }
    public String getAssDateString2() {
        if (assessmentDatetime == null) {
            return "";
        }
        SimpleDateFormat fomat = new SimpleDateFormat("yyyy/MM/dd");;
        return fomat.format(assessmentDatetime);
    }
    /**
     * @return the assStaffName
     */
    public String getAssStaffName() {
        return assStaffName;
    }

    /**
     * @param assStaffName the assStaffName to set
     */
    public void setAssStaffName(String assStaffName) {
        this.assStaffName = assStaffName;
    }

    /**
     * @return the typeName
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * @param typeName the typeName to set
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * @return the importStaff
     */
    public String getImportStaff() {
        return importStaff;
    }

    /**
     * @param importStaff the importStaff to set
     */
    public void setImportStaff(String importStaff) {
        this.importStaff = importStaff;
    }

    /**
     * @return the exportStaff
     */
    public String getExportStaff() {
        return exportStaff;
    }

    /**
     * @param exportStaff the exportStaff to set
     */
    public void setExportStaff(String exportStaff) {
        this.exportStaff = exportStaff;
    }

    /**
     * @return the importDate
     */
    public String getImportDate() {
        return importDate;
    }

    /**
     * @param importDate the importDate to set
     */
    public void setImportDate(String importDate) {
        this.importDate = importDate;
    }

    /**
     * @return the exportDate
     */
    public String getExportDate() {
        return exportDate;
    }
    public String getExportDateString() {
        if (exportDate == null) {
            return "";
        }
        SimpleDateFormat fomat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");;
        return fomat.format(exportDate);
    }

    /**
     * @param exportDate the exportDate to set
     */
    public void setExportDate(String exportDate) {
        this.exportDate = exportDate;
    }

    /**
     * @return the fromDate
     */
    public Date getFromDate() {
        return fromDate;
    }

    /**
     * @param fromDate the fromDate to set
     */
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @return the toDate
     */
    public Date getToDate() {
        return toDate;
    }

    /**
     * @param toDate the toDate to set
     */
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

	public Long getIndex() {
		return index;
	}

	public void setIndex(Long index) {
		this.index = index;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public String getGoodsGroupName() {
		return goodsGroupName;
	}

	public void setGoodsGroupName(String goodsGroupName) {
		this.goodsGroupName = goodsGroupName;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }
    
    public String getCodePath() {
        return codePath;
    }

    public void setCodePath(String codePath) {
        this.codePath = codePath;
    }
}
