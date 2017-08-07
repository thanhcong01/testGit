/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ftu.inventory.bo;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 *
 * @author E5420
 */
@Entity
@Table(name = "PROVIDER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Provider.findAll", query = "SELECT p FROM Provider p"),
    @NamedQuery(name = "Provider.findByProviderId", query = "SELECT p FROM Provider p WHERE p.providerId = :providerId"),
    @NamedQuery(name = "Provider.findByProviderName", query = "SELECT p FROM Provider p WHERE p.providerName = :providerName")})
public class Provider implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @SequenceGenerator(name = "PROVIDER_SEQ", sequenceName = "PROVIDER_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "PROVIDER_SEQ")
    @Basic(optional = false)
    @Column(name = "PROVIDER_ID")
    private Long providerId;
    @Column(name = "PROVIDER_NAME")
    private String providerName;
    @Column(name = "CONTRACT_NO")
    private String contractNo;
    @Column(name = "CONTRACT_DATE")
    @Temporal(TemporalType.DATE)
    private Date contractDate;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "PHONE")
    private String phone;
    @Column(name = "FAX")
    private String fax;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "PROVIDER_CODE")
    private String providerCode;
    @Column(name = "CONTACT_NAME")
    private String contactName;
    @Column(name = "CONTACT_NUMBER")
    private String contactNumber;
    @Transient
    private Long wanranprio;
    @Transient
    private Date wanranExpriDate;
    @Transient
    private String wanranExpriDateStr;

    public Provider() {
    }

    public Provider(Long providerId) {
        this.providerId = providerId;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contactNo) {
		this.contractNo = contactNo;
	}

	public Date getContractDate() {
		return contractDate;
	}

	public void setContractDate(Date contractDate) {
		this.contractDate = contractDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (providerId != null ? providerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Provider)) {
            return false;
        }
        Provider other = (Provider) object;
        if ((this.providerId == null && other.providerId != null) || (this.providerId != null && !this.providerId.equals(other.providerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "core.Hibernate.BO.Inventory.Provider[ providerId=" + providerId + " ]";
    }

    public String getProviderCode() {
        return providerCode;
    }

    public void setProviderCode(String providerCode) {
        this.providerCode = providerCode;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Long getWanranprio() {
        return wanranprio;
    }

    public void setWanranprio(Long wanranprio) {
        this.wanranprio = wanranprio;
    }

    public Date getWanranExpriDate() {
        return wanranExpriDate;
    }

    public void setWanranExpriDate(Date wanranExpriDate) {
        this.wanranExpriDate = wanranExpriDate;
    }

    public String getWanranExpriDateStr() {
        return wanranExpriDateStr;
    }

    public void setWanranExpriDateStr(String wanranExpriDateStr) {
        this.wanranExpriDateStr = wanranExpriDateStr;
    }

    public Provider(Provider provider) {
        if(provider == null) return;
        this.providerId = provider.providerId;
        this.providerName = provider.providerName;
        this.contractNo = provider.contractNo;
        this.contractDate = provider.contractDate;
        this.address = provider.address;
        this.phone = provider.phone;
        this.fax = provider.fax;
        this.status = provider.status;
        this.providerCode = provider.providerCode;
        this.contactName = provider.contactName;
        this.contactNumber = provider.contactNumber;
        this.wanranprio = provider.wanranprio;
        this.wanranExpriDate = provider.wanranExpriDate;
        this.wanranExpriDateStr = provider.wanranExpriDateStr;
    }
}
