/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bean;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.ftu.constanst.InventoryConstanst;
import com.ftu.inventory.bo.EquipmentsProfile;
import com.ftu.inventory.bo.EquipmentsGroup;
import com.ftu.inventory.bo.StockGoods;
import com.ftu.inventory.exportExcel.ExportExcel;
import com.ftu.language.LanguageBean;
import com.ftu.staff.bo.ApDomain;
import com.ftu.staff.bo.Shop;

/**
 *
 * @author E5420
 */
@ManagedBean
@ViewScoped
public class CreateSelledGeneralReport implements Serializable {

//	private Shop shop = new Shop();
	Long selectId;
	private List<StockGoods> lsStockGoods;

	// private List<SerialA> lsSerial;
	List<EquipmentsProfile> lsGoods;
	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean = new SessionBean();
	@ManagedProperty(value = "#{languageBean}")
	private LanguageBean languageBean;
	private String fromSerial;
	private String toSerial;
	private List<ApDomain> listStatus;
	private Long stockStatus;

	private Long providerId;
	private transient StreamedContent fileExport;

	// huytv10
	Long createShopId;
	private List<Shop> listShop;
	private String goods;
	private Long goodsId;
	private Long groupId;
	List<ApDomain> listGoodStt;
	List<EquipmentsGroup> lsGoodsGroup;
	private List<ApDomain> listStockStatus;
	private Long stockStatusId;
	private Long soldSum;

	@PostConstruct
	public void init() {
		sessionBean.loadData(sessionBean.getStaff(), sessionBean.getShop());
//		listShop = new ArrayList<>();
//		 _getListShop(sessionBean.getService().getAllShop(sessionBean.getShop().getShopId()));
        listShop = sessionBean.getService().getTreeShop(sessionBean.getShop().getShopId());
		listStockStatus = sessionBean.getService().getListSearchStocktatus();
		listStatus = sessionBean.getService().getListViewStocktatus();
		listGoodStt = sessionBean.getService().getListGoodsStatus();
		lsGoodsGroup = sessionBean.getLsgGroup();
		changeGoodsGroup();
	}
	
	public void _getListShop(Shop s) {
        listShop.add(s);
        if (s.getChildShops() != null) {
            for (Shop sub : s.getChildShops()) {
                _getListShop(sub);
            }
        }

    }

	public void searchStockGood() {
		if (createShopId == null && !listShop.isEmpty()) {
            createShopId = listShop.get(0).getShopId();
        }
		soldSum = 0L;
		setLsStockGoods(
				sessionBean.getService().searchStockGoods(createShopId, goodsId, groupId, InventoryConstanst.GoodsStatus.NOMAL));
		if (lsStockGoods != null) {
			List<StockGoods> listSGSold = new ArrayList<>();
			for (StockGoods sg : getLsStockGoods()) {
				EquipmentsProfile g = sessionBean.getService().getGoodsById(sg.getGoodsId());
				Long sold = getSessionBean().getService().getStockSize(sg.getGoodsId(), sg.getGoodsStatus(),
						sg.getShopId(), Long.parseLong(InventoryConstanst.StockStatus.SOLD), g.getSerial());
				if(sold > 0){
					sg.setSold(sold);
					
					sg.setProfileName(g.getProfileName());
					sg.setProfileCode(g.getProfileCode());
					sg.setEquipmentsGroup(sessionBean.getGoodsGroup(g.getEquipmentsGroupId()).getEquipmentsGroupName());
					sg.setGoodsStatusName(getSessionBean().getService().getEquipsStatusName(sg.getGoodsStatus()));
					listSGSold.add(sg);
					soldSum += sold;
				}
			}
			lsStockGoods = listSGSold;
		}

	}

	public void changeGoodsGroup() {
		if (lsGoodsGroup != null && !lsGoodsGroup.isEmpty() && (groupId == null || groupId == 0L)) {
			// setGroupId(lsEquipmentGroup.get(0).getGoodsGroupId());
			lsGoods = new ArrayList<>();
			searchStockGood();
			return;

		}
		if (getGroupId() != null && getGroupId() > 0) {
			List<EquipmentsProfile> lsg = sessionBean.getLsEquipments();
			lsGoods = new ArrayList<>();
			if (lsg != null && !lsg.isEmpty()) {
				for (EquipmentsProfile g : lsg) {
					if (groupId.equals(g.getEquipmentsGroupId())) {
						lsGoods.add(g);
					}
				}
			}
		}

		if (lsGoods.isEmpty()) {
			searchStockGood();
			return;
		} else {
			goodsId = null;
			goods = "";
		}
		searchStockGood();
	}

	/**
	 * Báo cáo tổng hợp số lượng hàng đã bán
	 * 
	 * @throws IOException
	 */
	public void exportExcel() throws IOException {
		try {
			ExportExcel ee = new ExportExcel();
			String s = ee.exportListStockGoodsSold(lsStockGoods, sessionBean.getStaff().getStaffName(), sessionBean.getShopById(createShopId).getShopName());
			fileExport = new DefaultStreamedContent(new FileInputStream(s), "xlsx", "baocao_tonghop_daban.xlsx");
			// downloadFile("exportStocks_1460711967900.xlsx");
		} catch (Exception ex) {
			ex.printStackTrace();
			languageBean.showMeseage("Info", "errorProcess");
		}

	}

	/**
	 * @return the lsStockGoods
	 */
	public List<StockGoods> getLsStockGoods() {
		return lsStockGoods;
	}

	/**
	 * @param lsStockGoods
	 *            the lsStockGoods to set
	 */
	public void setLsStockGoods(List<StockGoods> lsStockGoods) {
		this.lsStockGoods = lsStockGoods;
	}

	public SessionBean getSessionBean() {
		return sessionBean;
	}

	/**
	 * @param sessionBean
	 *            the sessionBean to set
	 */
	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}

	/**
	 * @return the fromSerial
	 */
	public String getFromSerial() {
		return fromSerial;
	}

	/**
	 * @param fromSerial
	 *            the fromSerial to set
	 */
	public void setFromSerial(String fromSerial) {
		this.fromSerial = fromSerial;
	}

	/**
	 * @return the listStatus
	 */
	public List<ApDomain> getListStatus() {
		return listStatus;
	}

	/**
	 * @param listStatus
	 *            the listStatus to set
	 */
	public void setListStatus(List<ApDomain> listStatus) {
		this.listStatus = listStatus;
	}

	/**
	 * @return the stockStatus
	 */
	public Long getStockStatus() {
		return stockStatus;
	}

	/**
	 * @param stockStatus
	 *            the stockStatus to set
	 */
	public void setStockStatus(Long stockStatus) {
		this.stockStatus = stockStatus;
	}

	/**
	 * @return the providerId
	 */
	public Long getProviderId() {
		return providerId;
	}

	/**
	 * @param providerId
	 *            the providerId to set
	 */
	public void setProviderId(Long providerId) {
		this.providerId = providerId;
	}

	/**
	 * @return the languageBean
	 */
	public LanguageBean getLanguageBean() {
		return languageBean;
	}

	/**
	 * @param languageBean
	 *            the languageBean to set
	 */
	public void setLanguageBean(LanguageBean languageBean) {
		this.languageBean = languageBean;
	}

	/**
	 * @return the toSerial
	 */
	public String getToSerial() {
		return toSerial;
	}

	/**
	 * @param toSerial
	 *            the toSerial to set
	 */
	public void setToSerial(String toSerial) {
		this.toSerial = toSerial;
	}

	/**
	 * @return the fileExport
	 */
	public StreamedContent getFileExport() {
		return fileExport;
	}

	/**
	 * @param fileExport
	 *            the fileExport to set
	 */
	public void setFileExport(StreamedContent fileExport) {
		this.fileExport = fileExport;
	}

	public List<EquipmentsProfile> getLsGoods() {
		return lsGoods;
	}

	public void setLsGoods(List<EquipmentsProfile> lsGoods) {
		this.lsGoods = lsGoods;
	}

	public String getGoods() {
		return goods;
	}

	public void setGoods(String goods) {
		this.goods = goods;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public List<ApDomain> getListGoodStt() {
		return listGoodStt;
	}

	public void setListGoodStt(List<ApDomain> listGoodStt) {
		this.listGoodStt = listGoodStt;
	}

	public List<EquipmentsGroup> getLsGoodsGroup() {
		return lsGoodsGroup;
	}

	public void setLsGoodsGroup(List<EquipmentsGroup> lsGoodsGroup) {
		this.lsGoodsGroup = lsGoodsGroup;
	}

	public List<ApDomain> getListStockStatus() {
		return listStockStatus;
	}

	public void setListStockStatus(List<ApDomain> listStockStatus) {
		this.listStockStatus = listStockStatus;
	}

	public Long getStockStatusId() {
		return stockStatusId;
	}

	public void setStockStatusId(Long stockStatusId) {
		this.stockStatusId = stockStatusId;
	}

	public Long getCreateShopId() {
		return createShopId;
	}

	public void setCreateShopId(Long createShopId) {
		this.createShopId = createShopId;
	}

	public List<Shop> getListShop() {
		return listShop;
	}

	public void setListShop(List<Shop> listShop) {
		this.listShop = listShop;
	}

	public Long getSoldSum() {
		return soldSum;
	}

	public void setSoldSum(Long soldSum) {
		this.soldSum = soldSum;
	}
	
	
}
