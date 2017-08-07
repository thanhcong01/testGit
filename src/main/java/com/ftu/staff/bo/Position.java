/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ftu.staff.bo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DuongThanhCong
 */
@Entity
@Table(name = "POSITION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Position.findAll", query = "SELECT p FROM Position p"),
    @NamedQuery(name = "Position.findByPositionId", query = "SELECT p FROM Position p WHERE p.positionId = :positionId"),
    @NamedQuery(name = "Position.findByPositionCode", query = "SELECT p FROM Position p WHERE p.positionCode = :positionCode"),
    @NamedQuery(name = "Position.findByPositionName", query = "SELECT p FROM Position p WHERE p.positionName = :positionName"),
    @NamedQuery(name = "Position.findByLinkId", query = "SELECT p FROM Position p WHERE p.linkId = :linkId"),
    @NamedQuery(name = "Position.findByLaneCode", query = "SELECT p FROM Position p WHERE p.laneCode = :laneCode"),
    @NamedQuery(name = "Position.findByIpAddress", query = "SELECT p FROM Position p WHERE p.ipAddress = :ipAddress"),
    @NamedQuery(name = "Position.findByFigureName", query = "SELECT p FROM Position p WHERE p.figureName = :figureName"),
    @NamedQuery(name = "Position.findByFigureX", query = "SELECT p FROM Position p WHERE p.figureX = :figureX"),
    @NamedQuery(name = "Position.findByFigureY", query = "SELECT p FROM Position p WHERE p.figureY = :figureY"),
    @NamedQuery(name = "Position.findByDescription", query = "SELECT p FROM Position p WHERE p.description = :description")})
public class Position implements Serializable {
    private static final long serialVersionUID = 1L;
    @SequenceGenerator(name = "POSITION_SEQ", sequenceName = "POSITION_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "POSITION_SEQ")
    @Basic(optional = false)
    @Column(name = "POSITION_ID")
    private Long positionId;
    @Column(name = "POSITION_CODE")
    private String positionCode;
    @Column(name = "POSITION_NAME")
    private String positionName;
    @Column(name = "LINK_ID")
    private Long linkId;
    @Column(name = "LANE_CODE")
    private String laneCode;
    @Column(name = "IP_ADDRESS")
    private String ipAddress;
    @Column(name = "FIGURE_NAME")
    private String figureName;
    @Column(name = "FIGURE_X")
    private String figureX;
    @Column(name = "FIGURE_Y")
    private String figureY;
    @Column(name = "DESCRIPTION")
    private String description;
//    @JoinColumn(name = "SHOP_ID", referencedColumnName = "SHOP_ID")
//    @ManyToOne
    @Column(name = "SHOP_ID")
    private Long shopId;
    @Column(name = "MANAGEMENT_TYPE")
    private Long managementType;
    @Column(name = "POSITION_STATUS")
    private Long positionStatus;


    @Transient
    private List<Long> shopIds;
    @Transient
    private String  LinkPostionCode;
    @Transient
    private String  LinkPostionName;
    
    @Transient
    private String svgStr;
    
    /**
	 * @return the svgStr
	 */
	public String getSvgStr() {
		return "<circle name=\"circleFill\" id=\"lane_003_6519\" r=\"10\" cx=\"" + figureX + "\" cy=\""+ figureY + "\" fill=\"#009e0f\" \r\n" +
				"										class=\"device_online\" transform=\"matrix(1 0 0 1 0 0)\"\r\n" + 
				"									 	onmousedown=\"selectElement(evt)\" ></circle> ";
	}

	/**
	 * @param svgStr the svgStr to set
	 */
	public void setSvgStr(String svgStr) {
		this.svgStr = svgStr;
	}

	public Position() {
    }

    public Position(Long positionId) {
        this.positionId = positionId;
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
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

    public Long getLinkId() {
        return linkId;
    }

    public void setLinkId(Long linkId) {
        this.linkId = linkId;
    }

    public String getLaneCode() {
        return laneCode;
    }

    public void setLaneCode(String laneCode) {
        this.laneCode = laneCode;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getFigureName() {
        return figureName;
    }

    public void setFigureName(String figureName) {
        this.figureName = figureName;
    }

    public String getFigureX() {
        return figureX;
    }

    public void setFigureX(String figureX) {
        this.figureX = figureX;
    }

    public String getFigureY() {
        return figureY;
    }

    public void setFigureY(String figureY) {
        this.figureY = figureY;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        hash += (positionId != null ? positionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Position)) {
            return false;
        }
        Position other = (Position) object;
        if ((this.positionId == null && other.positionId != null) || (this.positionId != null && !this.positionId.equals(other.positionId))) {
            return false;
        }
        return true;
    }

    public Position(Long positionId, String positionCode, String positionName, Long linkId, String laneCode, String ipAddress, String figureName, String figureX, String figureY, String description, Long shopId) {
        this.positionId = positionId;
        this.positionCode = positionCode;
        this.positionName = positionName;
        this.linkId = linkId;
        this.laneCode = laneCode;
        this.ipAddress = ipAddress;
        this.figureName = figureName;
        this.figureX = figureX;
        this.figureY = figureY;
        this.description = description;
        this.shopId = shopId;
    }

    @Override
    public String toString() {
        return "Position{" +
                "positionId=" + positionId +
                ", positionCode='" + positionCode + '\'' +
                ", positionName='" + positionName + '\'' +
                ", linkId=" + linkId +
                ", laneCode='" + laneCode + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", figureName='" + figureName + '\'' +
                ", figureX='" + figureX + '\'' +
                ", figureY='" + figureY + '\'' +
                ", description='" + description + '\'' +
                ", shopId=" + shopId +
                '}';
    }

    public List<Long> getShopIds() {
        return shopIds;
    }

    public void setShopIds(List<Long> shopIds) {
        this.shopIds = shopIds;
    }

    public String getLinkPostionCode() {
        return LinkPostionCode;
    }

    public void setLinkPostionCode(String linkPostionCode) {
        LinkPostionCode = linkPostionCode;
    }

    public String getLinkPostionName() {
        return LinkPostionName;
    }

    public void setLinkPostionName(String linkPostionName) {
        LinkPostionName = linkPostionName;
    }

    public Long getManagementType() {
        return managementType;
    }

    public void setManagementType(Long managementType) {
        this.managementType = managementType;
    }

    public Long getPositionStatus() {
        return positionStatus;
    }

    public void setPositionStatus(Long positionStatus) {
        this.positionStatus = positionStatus;
    }

    public Position(Position position) {
        if(position == null) return;
        this.positionId = position.getPositionId();
        this.positionCode = position.positionCode;
        this.positionName = position.positionName;
        this.linkId = position.linkId;
        this.laneCode = position.laneCode;
        this.ipAddress = position.ipAddress;
        this.figureName = position.figureName;
        this.figureX = position.figureX;
        this.figureY = position.figureY;
        this.description = position.description;
        this.shopId = position.shopId;
        this.managementType = position.managementType;
        this.positionStatus = position.positionStatus;
        this.shopIds = position.shopIds;
        this.LinkPostionCode = position.getLinkPostionCode();
        this.LinkPostionName = position.getLinkPostionName();
        this.svgStr = position.svgStr;
    }
}
