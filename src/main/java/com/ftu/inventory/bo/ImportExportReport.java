package com.ftu.inventory.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import java.io.Serializable;

/**
 * Created by PhamTan on 9/21/2016.
 */
@Entity
public class ImportExportReport implements Serializable {

	@Id
	@Column(name="id")
	private int Id;

	@Column(name="shop_name")
	private String shopName;
	@Column(name="equipment_group_name")
	private String equipmentGroupName;
	@Column(name="profile_code")
	private String profileCode;
	@Column(name="profile_name")
	private String profileName;
	@Column(name="unit")
	private String unit;
	@Column(name="EQUIPMENT_STATUS")
	private String equipmentStatus;
	@Transient
	private String statusStr;
	@Column(name="old_balance")
	private String oldBallance;
	@Column(name="import_amount")
	private String importAmount;
	@Column(name="export_amount")
	private String exportAmount;
	@Column(name="new_balance")
	private String newBallance;
	@Transient
	private String unitName;
	@Transient
	private int count;
	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getProfileCode() {
		return profileCode;
	}

	public void setProfileCode(String profileCode) {
		this.profileCode = profileCode;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public String getEquipmentStatus() {
		return equipmentStatus;
	}

	public void setEquipmentStatus(String equipmentStatus) {
		this.equipmentStatus = equipmentStatus;
	}

	public String getOldBallance() {
		return oldBallance;
	}

	public void setOldBallance(String oldBallance) {
		this.oldBallance = oldBallance;
	}

	public String getImportAmount() {
		return importAmount;
	}

	public void setImportAmount(String importAmount) {
		this.importAmount = importAmount;
	}

	public String getExportAmount() {
		return exportAmount;
	}

	public void setExportAmount(String exportAmount) {
		this.exportAmount = exportAmount;
	}

	public String getNewBallance() {
		return newBallance;
	}

	public void setNewBallance(String newBallance) {
		this.newBallance = newBallance;
	}

	public ImportExportReport() {
	}

	public String getStatusStr() {
		switch(equipmentStatus)
		{
		case "1":
			return "Hàng mới";
		case "2":
			return "Hàng hỏng";
		case "3":
			return "Hàng đã sử dụng";
		default:
			return equipmentStatus;
		}

	}

	public void setStatusStr(String statusStr) {
		//this.statusStr = statusStr;
	}

	public String getEquipmentGroupName() {
		return equipmentGroupName;
	}

	public void setEquipmentGroupName(String equipmentGroupName) {
		this.equipmentGroupName = equipmentGroupName;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
