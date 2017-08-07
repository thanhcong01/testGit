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
 * @author Cao Cuong
 */
@Entity
@Table(name = "TRANSACTION_NOTIFICATION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TransactionNotification.findAll", query = "SELECT t FROM TransactionNotification t"),
    @NamedQuery(name = "TransactionNotification.findById", query = "SELECT t FROM TransactionNotification t WHERE t.id = :id"),
    @NamedQuery(name = "TransactionNotification.findByName", query = "SELECT t FROM TransactionNotification t WHERE t.name = :name"),
    @NamedQuery(name = "TransactionNotification.findByUrl", query = "SELECT t FROM TransactionNotification t WHERE t.url = :url"),
    @NamedQuery(name = "TransactionNotification.findByTransactionStatus", query = "SELECT t FROM TransactionNotification t WHERE t.transactionStatus = :transactionStatus"),
    @NamedQuery(name = "TransactionNotification.findByTransactionType", query = "SELECT t FROM TransactionNotification t WHERE t.transactionType = :transactionType"),
    @NamedQuery(name = "TransactionNotification.findByIsImportStock", query = "SELECT t FROM TransactionNotification t WHERE t.isImportStock = :isImportStock")})
public class TransactionNotification implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @SequenceGenerator(name = "TRANSACTION_NOTIFICATION_SEQ", sequenceName = "TRANSACTION_NOTIFICATION_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "TRANSACTION_NOTIFICATION_SEQ")
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "URL")
    private String url;
    @Column(name = "TRANSACTION_STATUS")
    private String transactionStatus;
    @Column(name = "TRANSACTION_TYPE")
    private String transactionType;
    @Column(name = "IS_IMPORT_STOCK")
    private Long isImportStock;
    @Transient
    private Long count;
    @Transient
    private String[] types;

    public TransactionNotification() {
    }

    public TransactionNotification(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Long getIsImportStock() {
        return isImportStock;
    }

    public void setIsImportStock(Long isImportStock) {
        this.isImportStock = isImportStock;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransactionNotification)) {
            return false;
        }
        TransactionNotification other = (TransactionNotification) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ftu.inventory.bo.TransactionNotification[ id=" + id + " ]";
    }

    /**
     * @return the count
     */
    public Long getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(Long count) {
        this.count = count;
    }

    /**
     * @return the status
     */
    public String[] getTypes() {
        if (transactionType == null || transactionType.trim().isEmpty()) {
            return null;
        }
        return transactionType.split(",");
    }

    /**
     * @param types the types to set
     */
    public void setTypes(String[] types) {
        this.types = types;
    }

}
