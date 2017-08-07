/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bean;

import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.ftu.inventory.bo.StockGoodsInvSerial;
import com.ftu.inventory.dto.StockGoodsInvSerialDTO;
import com.ftu.java.bo.LazyEquipmentsModel;
import com.ftu.java.bo.LazyGoodsModel;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;

import com.ftu.inventory.bo.EquipmentsProfile;
import com.ftu.inventory.bo.EquipmentsGroup;
import com.ftu.inventory.bo.StockGoods;
import com.ftu.inventory.exportExcel.ExportExcel;
import com.ftu.language.LanguageBean;
import com.ftu.staff.bo.ApDomain;
import com.ftu.staff.bo.Shop;
import com.ftu.utils.ComponentUtils;

/**
 *
 * @author E5420
 */
@ManagedBean
@ViewScoped
public class CreateDetailReport implements Serializable {

	// private Shop shop = new Shop();
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
	private LazyDataModel<StockGoodsInvSerialDTO> lsData;

	private Long providerId;
	private StockGoods sg = new StockGoods();
	private transient StreamedContent fileExport;

	// huytv10
	Long createShopId;
	private List<Shop> listShop;
	private String goods;
	private Long goodsId;
	private Long groupId;
	private Long stockGoodsStatus;
	List<ApDomain> listGoodStt;
	List<EquipmentsGroup> lsGoodsGroup;
	private List<ApDomain> listStockStatus;
	private Long stockStatusId;
	private Long inStockSum;
	private Long blockSum;
	private StockGoodsInvSerialDTO SGITemp;
	private String fromSerialTemp;
	private String toSerialTemp;
	private String goodsGroupName;

	@PostConstruct
	public void init() {
		sessionBean.loadData(sessionBean.getStaff(), sessionBean.getShop());
//		listShop = new ArrayList<>();
//		_getListShop(sessionBean.getService().getAllShop(sessionBean.getShop().getShopId()));
        listShop = sessionBean.getService().getTreeShop(sessionBean.getShop().getShopId());
		listStockStatus = sessionBean.getService().getListSearchStocktatus();
		listStatus = sessionBean.getService().getListViewStocktatusForExport();
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

	public void search() {
		ComponentUtils cu = new ComponentUtils();
		DataTable dt = (DataTable) cu.findComponent("dtEtagSerial");
		if (dt != null) {
			dt.setFirst(0);
		}
		String sts = "";
		if (stockStatus == null || stockStatus == 0L) {
			if (listStatus != null) {
				for (ApDomain a : listStatus) {
					if (a.getValue() != null) {
						sts += a.getValue().toString() + ",";
					}
				}
				sts = sts.substring(0, sts.length() - 1);
			}
		} else {
			sts = stockStatus.toString();
		}

		if (goodsId != null) {
			setGoodsGroupName(sessionBean.getGoodsGroupNameByGoods(goodsId.toString()));
			lsData = null;
			StockGoodsInvSerialDTO sgis = new StockGoodsInvSerialDTO(fromSerial.trim(), stockGoodsStatus, sts, createShopId,
					providerId, goodsId);
			lsData = new LazyGoodsModel(fromSerial.trim(), getToSerial().trim(), sgis, null, null);
			SGITemp = sgis;
			fromSerialTemp = fromSerial.trim();
			toSerialTemp = getToSerial().trim();
		}
	}

	public void changeGoodsGroup() {
		List<EquipmentsProfile> lsg = sessionBean.getLsEquipments();
		lsGoods = new ArrayList<>();
		if (!sessionBean.getLsgGroup().isEmpty() && groupId == null) {
			groupId = sessionBean.getLsgGroup().get(0).getEquipmentsGroupId();
		}
		if (lsg != null && !lsg.isEmpty() && groupId != null && groupId > 0) {
			for (EquipmentsProfile g : lsg) {
				if (groupId.equals(g.getEquipmentsGroupId())) {
					lsGoods.add(g);
				}
			}
			if (goods == null && (!lsGoods.isEmpty())) {
				goodsId = lsGoods.get(0).getProfileId();
			}
		}
	}

	/**
	 * Báo cáo chi tiết hàng tồn kho
	 */
	public void exportInstockGoodsDetailReport() {
		String templateFileName = "temp_bc_chitiet.xlsx";
		String fileDownloadName = "baocao_chitiet.xlsx";
		String sts = "";
		if (stockStatus == null || stockStatus == 0L) {
			if (listStatus != null) {
				for (ApDomain a : listStatus) {
					if (a.getValue() != null) {
						sts += a.getValue().toString() + ",";
					}
				}
				sts = sts.substring(0, sts.length() - 1);
			}
		} else {
			sts = stockStatus.toString();
		}
		exportStockEquipmentsInvSerial(sts, fileDownloadName, templateFileName);
	}

	public void exportStockEquipmentsInvSerial(String stockStatus, String fileDownloadName, String templateFileName) {
		if (SGITemp == null) {
			languageBean.showMeseage("Info", "inven.stock.good.not.found");
			return;
		}

		int dataSize = sessionBean.getService()
				.searchStockEquipmentsInvSerialSize(fromSerialTemp, toSerialTemp, SGITemp, null).intValue();
		List<StockGoodsInvSerialDTO> datasource = sessionBean.getService().searchStockEquipmentsInvSerial(fromSerialTemp,
				toSerialTemp, SGITemp, null, 0, dataSize);

		if (datasource == null || datasource.isEmpty()) {
			languageBean.showMeseage("Info", "inven.stock.good.not.found");
			return;
		}

		for (int i = 0; i < datasource.size(); i++) {
			StockGoodsInvSerialDTO STI = datasource.get(i);
			STI.setIndex(i + 1L);
			if (goodsId != null) {
				STI.setGoodsGroupName(sessionBean.getGoodsGroupNameByGoods(goodsId.toString()));
			}
			STI.setStockStatusName(sessionBean.getStockStatusName(STI.getStockStatus()));
		}

		try {
			ExportExcel ee = new ExportExcel();
			String s = ee.exportListStockInvenSerial(datasource, sessionBean.getStaff().getStaffName(),
					sessionBean.getShopById(createShopId).getShopName(), templateFileName);
			fileExport = new DefaultStreamedContent(new FileInputStream(s), "xlsx", fileDownloadName);
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
	 * @return the lsData
	 */
	public LazyDataModel<StockGoodsInvSerialDTO> getLsData() {
		return lsData;
	}

	/**
	 * @param lsData
	 *            the lsData to set
	 */
	public void setLsData(LazyDataModel<StockGoodsInvSerialDTO> lsData) {
		this.lsData = lsData;
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
	 * @return the sg
	 */
	public StockGoods getSg() {
		return sg;
	}

	/**
	 * @param sg
	 *            the sg to set
	 */
	public void setSg(StockGoods sg) {
		this.sg = sg;
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

	public Long getStockGoodsStatus() {
		return stockGoodsStatus;
	}

	public void setStockGoodsStatus(Long stockGoodsStatus) {
		this.stockGoodsStatus = stockGoodsStatus;
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

	public Long getInStockSum() {
		return inStockSum;
	}

	public void setInStockSum(Long inStockSum) {
		this.inStockSum = inStockSum;
	}

	public Long getBlockSum() {
		return blockSum;
	}

	public void setBlockSum(Long blockSum) {
		this.blockSum = blockSum;
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

	public String getGoodsGroupName() {
		return goodsGroupName;
	}

	public void setGoodsGroupName(String goodsGroupName) {
		this.goodsGroupName = goodsGroupName;
	}

}
