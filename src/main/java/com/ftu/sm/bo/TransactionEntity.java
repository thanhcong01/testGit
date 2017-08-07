/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ftu.sm.bo;

import java.io.Serializable;
import java.util.Date;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author E5420
 */
@Entity
@Table(name = "TRANSACTION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TransactionEntity.findAll", query = "SELECT t FROM TransactionEntity t"),
    @NamedQuery(name = "TransactionEntity.findByTransId", query = "SELECT t FROM TransactionEntity t WHERE t.transId = :transId"),
    @NamedQuery(name = "TransactionEntity.findByInvoiceId", query = "SELECT t FROM TransactionEntity t WHERE t.invoiceId = :invoiceId"),
    @NamedQuery(name = "TransactionEntity.findByShopId", query = "SELECT t FROM TransactionEntity t WHERE t.shopId = :shopId"),
    @NamedQuery(name = "TransactionEntity.findByStaffId", query = "SELECT t FROM TransactionEntity t WHERE t.staffId = :staffId"),
    @NamedQuery(name = "TransactionEntity.findByPayMethod", query = "SELECT t FROM TransactionEntity t WHERE t.payMethod = :payMethod"),
    @NamedQuery(name = "TransactionEntity.findByAmount", query = "SELECT t FROM TransactionEntity t WHERE t.amount = :amount"),
    @NamedQuery(name = "TransactionEntity.findByTax", query = "SELECT t FROM TransactionEntity t WHERE t.tax = :tax"),
    @NamedQuery(name = "TransactionEntity.findByAmountTax", query = "SELECT t FROM TransactionEntity t WHERE t.amountTax = :amountTax"),
    @NamedQuery(name = "TransactionEntity.findByOrgTotal", query = "SELECT t FROM TransactionEntity t WHERE t.orgTotal = :orgTotal"),
    @NamedQuery(name = "TransactionEntity.findByDiscount", query = "SELECT t FROM TransactionEntity t WHERE t.discount = :discount"),
    @NamedQuery(name = "TransactionEntity.findByStatus", query = "SELECT t FROM TransactionEntity t WHERE t.status = :status"),
    @NamedQuery(name = "TransactionEntity.findByCreateDatetime", query = "SELECT t FROM TransactionEntity t WHERE t.createDatetime = :createDatetime"),
    @NamedQuery(name = "TransactionEntity.findByUsername", query = "SELECT t FROM TransactionEntity t WHERE t.username = :username"),
    @NamedQuery(name = "TransactionEntity.findByApprover", query = "SELECT t FROM TransactionEntity t WHERE t.approver = :approver"),
    @NamedQuery(name = "TransactionEntity.findByApproveDate", query = "SELECT t FROM TransactionEntity t WHERE t.approveDate = :approveDate"),
    @NamedQuery(name = "TransactionEntity.findByCusType", query = "SELECT t FROM TransactionEntity t WHERE t.cusType = :cusType"),
    @NamedQuery(name = "TransactionEntity.findByPromotion", query = "SELECT t FROM TransactionEntity t WHERE t.promotion = :promotion"),
    @NamedQuery(name = "TransactionEntity.findByGrandTotal", query = "SELECT t FROM TransactionEntity t WHERE t.grandTotal = :grandTotal"),
    @NamedQuery(name = "TransactionEntity.findByDestroyDate", query = "SELECT t FROM TransactionEntity t WHERE t.destroyDate = :destroyDate"),
    @NamedQuery(name = "TransactionEntity.findByDestroyer", query = "SELECT t FROM TransactionEntity t WHERE t.destroyer = :destroyer"),
    @NamedQuery(name = "TransactionEntity.findByConsignId", query = "SELECT t FROM TransactionEntity t WHERE t.consignId = :consignId"),
    @NamedQuery(name = "TransactionEntity.findByCreditNo", query = "SELECT t FROM TransactionEntity t WHERE t.creditNo = :creditNo"),
    @NamedQuery(name = "TransactionEntity.findByStockTransId", query = "SELECT t FROM TransactionEntity t WHERE t.stockTransId = :stockTransId"),
    @NamedQuery(name = "TransactionEntity.findByTaxRate", query = "SELECT t FROM TransactionEntity t WHERE t.taxRate = :taxRate"),
    @NamedQuery(name = "TransactionEntity.findByCustId", query = "SELECT t FROM TransactionEntity t WHERE t.custId = :custId"),
    @NamedQuery(name = "TransactionEntity.findByCustName", query = "SELECT t FROM TransactionEntity t WHERE t.custName = :custName"),
    @NamedQuery(name = "TransactionEntity.findByCustAddress", query = "SELECT t FROM TransactionEntity t WHERE t.custAddress = :custAddress"),
    @NamedQuery(name = "TransactionEntity.findByTaxCode", query = "SELECT t FROM TransactionEntity t WHERE t.taxCode = :taxCode"),
    @NamedQuery(name = "TransactionEntity.findByBillAccount", query = "SELECT t FROM TransactionEntity t WHERE t.billAccount = :billAccount"),
    @NamedQuery(name = "TransactionEntity.findByCompany", query = "SELECT t FROM TransactionEntity t WHERE t.company = :company"),
    @NamedQuery(name = "TransactionEntity.findByConfirmPayment", query = "SELECT t FROM TransactionEntity t WHERE t.confirmPayment = :confirmPayment"),
    @NamedQuery(name = "TransactionEntity.findByAdjustFromTransId", query = "SELECT t FROM TransactionEntity t WHERE t.adjustFromTransId = :adjustFromTransId")})
public class TransactionEntity implements Serializable {
    private static final Long serialVersionUID = 1L;
    @SequenceGenerator(name = "TRANSACTION_SEQ", sequenceName = "TRANSACTION_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "TRANSACTION_SEQ")
    @Basic(optional = false)
    @Column(name = "TRANS_ID")
    private Long transId;
    @Column(name = "INVOICE_ID")
    private Long invoiceId;
    @Basic(optional = false)
    @Column(name = "SHOP_ID")
    private Long shopId;
    @Basic(optional = false)
    @Column(name = "STAFF_ID")
    private Long staffId;
    @Basic(optional = false)
    @Column(name = "PAY_METHOD")
    private String payMethod;
    @Basic(optional = false)
    @Column(name = "AMOUNT")
    private Long amount;
    @Basic(optional = false)
    @Column(name = "TAX")
    private Long tax;
    @Basic(optional = false)
    @Column(name = "AMOUNT_TAX")
    private Long amountTax;
    @Basic(optional = false)
    @Column(name = "ORG_TOTAL")
    private Long orgTotal;
    @Basic(optional = false)
    @Column(name = "DISCOUNT")
    private Long discount;
    @Basic(optional = false)
    @Column(name = "STATUS")
    private String status;
    @Basic(optional = false)
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.DATE)
    private Date createDatetime;
    @Basic(optional = false)
    @Column(name = "USERNAME")
    private String username;
    @Column(name = "APPROVER")
    private String approver;
    @Column(name = "APPROVE_DATE")
    @Temporal(TemporalType.DATE)
    private Date approveDate;
    @Column(name = "CUS_TYPE")
    private String cusType;
    @Column(name = "PROMOTION")
    private Long  promotion;
    @Basic(optional = false)
    @Column(name = "GRAND_TOTAL")
    private Long grandTotal;
    @Column(name = "DESTROY_DATE")
    @Temporal(TemporalType.DATE)
    private Date destroyDate;
    @Column(name = "DESTROYER")
    private String destroyer;
    @Column(name = "CONSIGN_ID")
    private Long consignId;
    @Column(name = "CREDIT_NO")
    private String creditNo;
    @Column(name = "STOCK_TRANS_ID")
    private Long stockTransId;
    @Column(name = "TAX_RATE")
    private Long taxRate;
    @Column(name = "CUST_ID")
    private Long custId;
    @Column(name = "CUST_NAME")
    private String custName;
    @Column(name = "CUST_ADDRESS")
    private String custAddress;
    @Column(name = "TAX_CODE")
    private String taxCode;
    @Column(name = "BILL_ACCOUNT")
    private String billAccount;
    @Column(name = "COMPANY")
    private String company;
    @Basic(optional = false)
    @Column(name = "CONFIRM_PAYMENT")
    private String confirmPayment;
    @Column(name = "ADJUST_FROM_TRANS_ID")
    private Long adjustFromTransId;

    public TransactionEntity() {
    }

    public TransactionEntity(Long transId) {
        this.transId = transId;
    }

    public TransactionEntity(Long transId, Long shopId, Long staffId, String payMethod, Long amount, Long tax, Long amountTax, Long orgTotal, Long discount, String status, Date createDatetime, String username, Long grandTotal, String confirmPayment) {
        this.transId = transId;
        this.shopId = shopId;
        this.staffId = staffId;
        this.payMethod = payMethod;
        this.amount = amount;
        this.tax = tax;
        this.amountTax = amountTax;
        this.orgTotal = orgTotal;
        this.discount = discount;
        this.status = status;
        this.createDatetime = createDatetime;
        this.username = username;
        this.grandTotal = grandTotal;
        this.confirmPayment = confirmPayment;
    }

    public Long getTransId() {
        return transId;
    }

    public void setTransId(Long transId) {
        this.transId = transId;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
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

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getTax() {
        return tax;
    }

    public void setTax(Long tax) {
        this.tax = tax;
    }

    public Long getAmountTax() {
        return amountTax;
    }

    public void setAmountTax(Long amountTax) {
        this.amountTax = amountTax;
    }

    public Long getOrgTotal() {
        return orgTotal;
    }

    public void setOrgTotal(Long orgTotal) {
        this.orgTotal = orgTotal;
    }

    public Long getDiscount() {
        return discount;
    }

    public void setDiscount(Long discount) {
        this.discount = discount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public String getCusType() {
        return cusType;
    }

    public void setCusType(String cusType) {
        this.cusType = cusType;
    }

    public Long  getPromotion() {
        return promotion;
    }

    public void setPromotion(Long  promotion) {
        this.promotion = promotion;
    }

    public Long getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(Long grandTotal) {
        this.grandTotal = grandTotal;
    }

    public Date getDestroyDate() {
        return destroyDate;
    }

    public void setDestroyDate(Date destroyDate) {
        this.destroyDate = destroyDate;
    }

    public String getDestroyer() {
        return destroyer;
    }

    public void setDestroyer(String destroyer) {
        this.destroyer = destroyer;
    }

    public Long getConsignId() {
        return consignId;
    }

    public void setConsignId(Long consignId) {
        this.consignId = consignId;
    }

    public String getCreditNo() {
        return creditNo;
    }

    public void setCreditNo(String creditNo) {
        this.creditNo = creditNo;
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public Long getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Long taxRate) {
        this.taxRate = taxRate;
    }

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustAddress() {
        return custAddress;
    }

    public void setCustAddress(String custAddress) {
        this.custAddress = custAddress;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getBillAccount() {
        return billAccount;
    }

    public void setBillAccount(String billAccount) {
        this.billAccount = billAccount;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getConfirmPayment() {
        return confirmPayment;
    }

    public void setConfirmPayment(String confirmPayment) {
        this.confirmPayment = confirmPayment;
    }

    public Long getAdjustFromTransId() {
        return adjustFromTransId;
    }

    public void setAdjustFromTransId(Long adjustFromTransId) {
        this.adjustFromTransId = adjustFromTransId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transId != null ? transId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransactionEntity)) {
            return false;
        }
        TransactionEntity other = (TransactionEntity) object;
        if ((this.transId == null && other.transId != null) || (this.transId != null && !this.transId.equals(other.transId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ftu.sm.bo.Transaction[ transId=" + transId + " ]";
    }
    
}
