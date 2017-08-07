/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.staff.bo;

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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author E5420
 */
@Entity
@Table(name = "AP_DOMAIN")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ApDomain.findAll", query = "SELECT a FROM ApDomain a"),
    @NamedQuery(name = "ApDomain.findByType", query = "SELECT a FROM ApDomain a WHERE a.type = :type"),
    @NamedQuery(name = "ApDomain.findByCode", query = "SELECT a FROM ApDomain a WHERE a.code = :code"),
    @NamedQuery(name = "ApDomain.findByName", query = "SELECT a FROM ApDomain a WHERE a.name = :name"),
    @NamedQuery(name = "ApDomain.findByDescription", query = "SELECT a FROM ApDomain a WHERE a.description = :description"),
    @NamedQuery(name = "ApDomain.findByStatus", query = "SELECT a FROM ApDomain a WHERE a.status = :status"),
    @NamedQuery(name = "ApDomain.findByValue", query = "SELECT a FROM ApDomain a WHERE a.value = :value"),
    @NamedQuery(name = "ApDomain.findById", query = "SELECT a FROM ApDomain a WHERE a.id = :id"),
    @NamedQuery(name = "ApDomain.findByParentId", query = "SELECT a FROM ApDomain a WHERE a.parentId = :parentId")})
public class ApDomain implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "CODE")
    private String code;
    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "VALUE")
    private Long value;
	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @SequenceGenerator(name = "AP_DOMAIN_SEQ", sequenceName = "AP_DOMAIN_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "AP_DOMAIN_SEQ")
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "PARENT_ID")
    private Long parentId;

    public ApDomain() {
    }

    public ApDomain(Long id) {
        this.id = id;
    }

    public ApDomain(String name, Long value) {
        this.name = name;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
        // not set
        if (!(object instanceof ApDomain)) {
            return false;
        }
        ApDomain other = (ApDomain) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ftu.staff.bo.ApDomain[ id=" + id + " ]";
    }

    public ApDomain(ApDomain apDomain) {
        if(apDomain==null) return;
        this.id = apDomain.id;
        this.type = apDomain.type;
        this.code = apDomain.code;
        this.name = apDomain.name;
        this.description = apDomain.description;
        this.status = apDomain.status;
        this.value = apDomain.value;
        this.parentId = apDomain.parentId;
    }
}
