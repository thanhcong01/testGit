/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import com.ftu.inventory.bo.StockGoodsInvSerial;
import com.ftu.inventory.dto.StockGoodsInvSerialDTO;
import com.ftu.java.bo.LazyEquipmentsModel;
import com.ftu.java.bo.LazyGoodsModel;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.MenuActionEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.Visibility;
import org.primefaces.model.menu.MenuItem;

import com.ftu.constanst.InventoryConstanst;
import com.ftu.hibernate.HibernateUtil;
import com.ftu.inventory.bo.EquipmentsProfile;
import com.ftu.inventory.bo.EquipmentsGroup;
import com.ftu.inventory.bo.StockGoods;
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
public class ViewStock implements Serializable {

	private Shop shop;
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

	// private Long totalInStockQuan;
	// private Long totalBlockQuan;

	@PostConstruct
	public void init() {
		sessionBean.loadData(sessionBean.getStaff(), sessionBean.getShop());
		groupId = 0L;
		goodsId = 0L;
		providerId = 0L;
		listStockStatus = sessionBean.getService().getListSearchStocktatus();
		listStatus = sessionBean.getService().getListViewStocktatus();
		listGoodStt = sessionBean.getService().getListGoodsStatus();
		lsGoodsGroup = sessionBean.getLsgGroup();
		changeGoodsGroup();
	}

	public void searchStockGood() {
		inStockSum = 0L;
		blockSum = 0L;
		List<StockGoods> removeList = new ArrayList<StockGoods>();
		setLsStockGoods(
				sessionBean.getService().searchStockGoods(shop.getShopId(), goodsId, groupId, stockGoodsStatus));
		if (lsStockGoods != null) {
			List<StockGoods> lstSTG = new ArrayList<>();
			for (StockGoods sg : getLsStockGoods()) {
				lstSTG.add(new StockGoods(sg));
			}
			for (StockGoods sg : lstSTG) {
//				System.out.println("===============================  sg.id = "+sg.getStockGoodsId() );
				EquipmentsProfile g = sessionBean.getService().getGoodsById(sg.getGoodsId());
				if (g != null){
					sg.setProfileName(g.getProfileName());
					sg.setProfileCode(g.getProfileCode());
					sg.setEquipmentsGroupId(g.getEquipmentsGroupId());
					sg.setEquipmentsGroup(sessionBean.getGoodsGroup(g.getEquipmentsGroupId()).getEquipmentsGroupName());
					sg.setGoodsStatusName(getSessionBean().getService().getEquipsStatusName(sg.getGoodsStatus()));
					sg.setSpecification(g.getSpecification());
					
					Long sumQuan = 0L;
					Long aQuan = getSessionBean().getService().getStockSize(sg.getGoodsId(), sg.getGoodsStatus(),
							sg.getShopId(), Long.parseLong(InventoryConstanst.StockStatus.INSTOCK),g.getSerial());
					sg.setAvailableQuantity(aQuan);
					sumQuan += aQuan;
					
					Long bQuan = getSessionBean().getService().getStockSize(sg.getGoodsId(), sg.getGoodsStatus(),
							sg.getShopId(), Long.parseLong(InventoryConstanst.StockStatus.BLOCK_TD),g.getSerial())
							+ getSessionBean().getService().getStockSize(sg.getGoodsId(), sg.getGoodsStatus(),
							sg.getShopId(), Long.parseLong(InventoryConstanst.StockStatus.BLOCK_NC),g.getSerial())
							+ getSessionBean().getService().getStockSize(sg.getGoodsId(), sg.getGoodsStatus(),
							sg.getShopId(), Long.parseLong(InventoryConstanst.StockStatus.BLOCK_XT),g.getSerial());
					sg.setQuantityBlock(bQuan);
					sumQuan += bQuan;
					
					Long eQuan = getSessionBean().getService().getStockSize(sg.getGoodsId(), sg.getGoodsStatus(),
							sg.getShopId(), Long.parseLong(InventoryConstanst.StockStatus.CANCEL),g.getSerial());
					sg.setErrorQuantity(eQuan);
					sumQuan += eQuan;
					
					Long euQuan = getSessionBean().getService().getStockSize(sg.getGoodsId(), sg.getGoodsStatus(),
							sg.getShopId(), Long.parseLong(InventoryConstanst.StockStatus.EX_USED),g.getSerial());
					sg.setExStaffQuantity(euQuan);
					sumQuan += euQuan;
					
					Long uQuan = getSessionBean().getService().getStockSize(sg.getGoodsId(), sg.getGoodsStatus(),
							sg.getShopId(), Long.parseLong(InventoryConstanst.StockStatus.USED),g.getSerial());
					sg.setUsedQuantity(uQuan);
					sumQuan += uQuan;
					
					Long wQuan = getSessionBean().getService().getStockSize(sg.getGoodsId(), sg.getGoodsStatus(),
							sg.getShopId(), Long.parseLong(InventoryConstanst.StockStatus.EX_WARANTIED),g.getSerial());
					sg.setWarrantyQuantity(wQuan);
					sumQuan += wQuan;
					
					Long exQuan = getSessionBean().getService().getStockSize(sg.getGoodsId(), sg.getGoodsStatus(),
							sg.getShopId(), Long.parseLong(InventoryConstanst.StockStatus.CHANGE_WARANTIED),g.getSerial());
					sg.setExchangedQuantity(exQuan);
					sumQuan += exQuan;
					
					if (sumQuan == 0){
						removeList.add(sg);
						continue;
					}
					//HibernateUtil.getSession().evict(sg);
					inStockSum = aQuan + inStockSum;
					blockSum = bQuan + blockSum;
//					System.out.println("===============================end  sg.id = "+sg.getStockGoodsId() );
				}
			}
			lstSTG.removeAll(removeList);
			lsStockGoods.clear();
			lsStockGoods.addAll(lstSTG);
		}

		sg = new StockGoods();
		lsData = null;
	}

	public void changeGoodsGroup() {
		if(shop == null){
			shop = sessionBean.getShop();
		}
		if (lsGoodsGroup == null || lsGoodsGroup.isEmpty())
		{
			lsGoods = new ArrayList<>();
			goodsId = -1L;
			goods = "";
			searchStockGood();
			return;
		}
		if (lsGoodsGroup != null && !lsGoodsGroup.isEmpty() && (groupId == null || groupId == 0L)) {
			// setGroupId(lsEquipmentGroup.get(0).getGoodsGroupId());
			lsGoods = sessionBean.getLsEquipments();
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
		if (lsGoods==null || lsGoods.isEmpty()) {
			searchStockGood();
			return;
		} else {
			goodsId = null;
			goods = "";
		}
		searchStockGood();
	}

	public void viewClick() {
		providerId = 0L;
		stockStatus = 0L;
		String sts = "";
		if (listStatus != null) {
			for (ApDomain a : listStatus) {
				if (a.getValue() != null) {
					sts += a.getValue().toString() + ",";
				}
			}
			sts = sts.substring(0, sts.length() - 1);
		}

		// setSg((StockGoods) event.getObject());
		StockGoodsInvSerialDTO sgis = new StockGoodsInvSerialDTO(fromSerial, getSg().getGoodsStatus(), sts, shop.getShopId(),
				providerId, getSg().getGoodsId());
		lsData = new LazyGoodsModel(null, null, sgis, null, null);
		fromSerial = "";
		stockStatus = 0L;
		// AnalysisSerial ann = new AnalysisSerial(null, lsSgi);
		// setLsSerial(ann.analysisInvSerial());
		search();
	}

	public void deleteSelected(){
		sg = null;
	}
	public void search() {
		ComponentUtils cu = new ComponentUtils();
		DataTable dt = (DataTable) cu.findComponent("dtEtagSerial");
		if (dt != null) {
			dt.setFirst(0);
		}
		if (getSg() == null || getSg().getStockGoodsId() == null) {
			languageBean.showMeseage("Info", "importReqView");
			return;
		}

		if (!sessionBean.checkProfileHasSerial(getSg().getGoodsId())) {
			languageBean.showMeseage("Info", "serialNotRequired");
			return;
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
		lsData = null;
		StockGoodsInvSerialDTO sgis = new StockGoodsInvSerialDTO(fromSerial, getSg().getGoodsStatus(), sts, shop.getShopId(),
				providerId, getSg().getGoodsId());
		lsData = new LazyGoodsModel(fromSerial, getToSerial(), sgis, null, null);
	}


	private EquipmentsProfile getGoods(Long gId) {
		EquipmentsProfile rs = null;
		if (lsGoods != null && gId != null) {
			for (EquipmentsProfile g : lsGoods) {
				if (gId.equals(g.getProfileId())) {
					return g;
				}
			}
		}
		return rs;
	}

	public void viewStock(ActionEvent event) {
		inStockSum = 0L;
		blockSum = 0L;
		goodsId = null;
		groupId = null;
		stockGoodsStatus = null;
		MenuItem menuItem = ((MenuActionEvent) event).getMenuItem();
		selectId = Long.parseLong(menuItem.getParams().get("shop").get(0));
		if (selectId != null) {
			setShop(sessionBean.getService().getShopChildById(selectId));
			if (shop.getShopId() != null) {
				List<StockGoods> removeList = new ArrayList<StockGoods>();
				lsGoodsGroup = sessionBean.getLsgGroup();
				lsGoods = sessionBean.getLsEquipments();
				setLsStockGoods(sessionBean.getService().getStockGoodsByShop(selectId));
				if (lsStockGoods != null) {
					List<StockGoods> lstSTG = new ArrayList<>();
					for (StockGoods sg : getLsStockGoods()) {
						lstSTG.add(new StockGoods(sg));
					}
					for (StockGoods sg : lstSTG) {
						EquipmentsProfile g = getGoods(sg.getGoodsId());
						if (g != null)
						{
							sg.setProfileName(g.getProfileName());
							sg.setProfileCode(g.getProfileCode());
							sg.setEquipmentsGroupId(g.getEquipmentsGroupId());
							sg.setEquipmentsGroup(sessionBean.getGoodsGroup(g.getEquipmentsGroupId()).getEquipmentsGroupName());
							sg.setSpecification(g.getSpecification());
							// String s = ses
							// InventoryConstanst.GoodsStatus.ERROR.equals() ?
							// InventoryConstanst.GoodsStatusName.ERROR :
							// InventoryConstanst.GoodsStatusName.NOMAL;
							sg.setGoodsStatusName(
									getSessionBean().getService().getEquipsStatusName(sg.getGoodsStatus()));

							Long sumQuan = 0L;
							Long aQuan = getSessionBean().getService().getStockSize(sg.getGoodsId(), sg.getGoodsStatus(),
									sg.getShopId(), Long.parseLong(InventoryConstanst.StockStatus.INSTOCK),g.getSerial());
							sg.setAvailableQuantity(aQuan);
							sumQuan += aQuan;
							
							Long quan = getSessionBean().getService().getStockSize(sg.getGoodsId(), sg.getGoodsStatus(),
									sg.getShopId(), Long.parseLong(InventoryConstanst.StockStatus.BLOCK_TD),g.getSerial())
									+ getSessionBean().getService().getStockSize(sg.getGoodsId(), sg.getGoodsStatus(),
									sg.getShopId(), Long.parseLong(InventoryConstanst.StockStatus.BLOCK_NC),g.getSerial())
									+ getSessionBean().getService().getStockSize(sg.getGoodsId(), sg.getGoodsStatus(),
									sg.getShopId(), Long.parseLong(InventoryConstanst.StockStatus.BLOCK_XT),g.getSerial());
							sg.setQuantityBlock(quan);
							sumQuan += quan;
							
							Long eQuan = getSessionBean().getService().getStockSize(sg.getGoodsId(), sg.getGoodsStatus(),
									sg.getShopId(), Long.parseLong(InventoryConstanst.StockStatus.CANCEL),g.getSerial());
							sg.setErrorQuantity(eQuan);
							sumQuan += eQuan;
							
							Long euQuan = getSessionBean().getService().getStockSize(sg.getGoodsId(), sg.getGoodsStatus(),
									sg.getShopId(), Long.parseLong(InventoryConstanst.StockStatus.EX_USED),g.getSerial());
							sg.setExStaffQuantity(euQuan);
							sumQuan += euQuan;
							
							Long uQuan = getSessionBean().getService().getStockSize(sg.getGoodsId(), sg.getGoodsStatus(),
									sg.getShopId(), Long.parseLong(InventoryConstanst.StockStatus.USED),g.getSerial());
							sg.setUsedQuantity(uQuan);
							sumQuan += uQuan;
							
							Long wQuan = getSessionBean().getService().getStockSize(sg.getGoodsId(), sg.getGoodsStatus(),
									sg.getShopId(), Long.parseLong(InventoryConstanst.StockStatus.EX_WARANTIED),g.getSerial());
							sg.setWarrantyQuantity(wQuan);
							sumQuan += wQuan;
							
							Long exQuan = getSessionBean().getService().getStockSize(sg.getGoodsId(), sg.getGoodsStatus(),
									sg.getShopId(), Long.parseLong(InventoryConstanst.StockStatus.CHANGE_WARANTIED),g.getSerial());
							sg.setExchangedQuantity(exQuan);
							sumQuan += exQuan;
							//HibernateUtil.getSession().evict(sg);
							// Long sold =
							// getSessionBean().getService().getStockSize(sg.getEquipmentProfileId(),
							// sg.getGoodsStatus(),
							// sg.getShopId(),
							// Long.parseLong(InventoryConstanst.StockStatus.SOLD));
							// sg.setSold(sold);
							// sg.setQuantitySold(getSessionBean().getService().getStockSize(sg.getEquipmentProfileId(),
							// sg.getGoodsStatus(), sg.getShopId(),
							// Long.parseLong(InventoryConstanst.StockStatus.SOLD)));
							if (sumQuan == 0){
								removeList.add(sg);
								continue;
							}
							inStockSum = aQuan + inStockSum;
							blockSum = quan + blockSum;
						}
					}
					lstSTG.removeAll(removeList);
					lsStockGoods.clear();
					lsStockGoods.addAll(lstSTG);
				}
			}
		}
		lsData = null;
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('viewStock').show();");
	}

	public void viewStockFromButton() {
		inStockSum = 0L;
		blockSum = 0L;
		goodsId = null;
		groupId = null;
		stockGoodsStatus = null;
		shop = sessionBean.getShop();
		listStatus = sessionBean.getService().getListViewStocktatus();
		lsGoods = sessionBean.getLsEquipments();
		List<StockGoods> removeList = new ArrayList<StockGoods>();
		setLsStockGoods(sessionBean.getService().getStockGoodsByShop(shop.getShopId()));
		// totalInStockQuan = getSessionBean().getService().getStockSize(null,
		// null,
		// shop.getShopId(),
		// Long.parseLong(InventoryConstanst.StockStatus.INSTOCK));
		// totalBlockQuan = getSessionBean().getService().getStockSize(null,
		// null,
		// shop.getShopId(),
		// Long.parseLong(InventoryConstanst.StockStatus.BLOCK));
		if (lsStockGoods != null) {
			List<StockGoods> lstSTG = new ArrayList<>();
			for (StockGoods sg : getLsStockGoods()) {
				lstSTG.add(new StockGoods(sg));
			}
			for (StockGoods sg : lstSTG) {
				EquipmentsProfile g = getGoods(sg.getGoodsId());
				sg.setProfileName(g.getProfileName());
				sg.setProfileCode(g.getProfileCode());
				sg.setEquipmentsGroupId(g.getEquipmentsGroupId());
				sg.setEquipmentsGroup(sessionBean.getGoodsGroup(g.getEquipmentsGroupId()).getEquipmentsGroupName());
				// String s = ses InventoryConstanst.GoodsStatus.ERROR.equals()
				// ? InventoryConstanst.GoodsStatusName.ERROR :
				// InventoryConstanst.GoodsStatusName.NOMAL;
				sg.setSpecification(g.getSpecification());
				sg.setGoodsStatusName(getSessionBean().getService().getEquipsStatusName(sg.getGoodsStatus()));

				Long sumQuan = 0L;
				Long aQuan = getSessionBean().getService().getStockSize(sg.getGoodsId(), sg.getGoodsStatus(),
						sg.getShopId(), Long.parseLong(InventoryConstanst.StockStatus.INSTOCK),g.getSerial());
				sg.setAvailableQuantity(aQuan);
				sumQuan += aQuan;
				
				Long eQuan = getSessionBean().getService().getStockSize(sg.getGoodsId(), sg.getGoodsStatus(),
						sg.getShopId(), Long.parseLong(InventoryConstanst.StockStatus.CANCEL),g.getSerial());
				sg.setErrorQuantity(eQuan);
				sumQuan += eQuan;
				
				Long euQuan = getSessionBean().getService().getStockSize(sg.getGoodsId(), sg.getGoodsStatus(),
						sg.getShopId(), Long.parseLong(InventoryConstanst.StockStatus.EX_USED),g.getSerial());
				sg.setExStaffQuantity(euQuan);
				sumQuan += euQuan;
				
				Long uQuan = getSessionBean().getService().getStockSize(sg.getGoodsId(), sg.getGoodsStatus(),
						sg.getShopId(), Long.parseLong(InventoryConstanst.StockStatus.USED),g.getSerial());
				sg.setUsedQuantity(uQuan);
				sumQuan += uQuan;
				
				Long wQuan = getSessionBean().getService().getStockSize(sg.getGoodsId(), sg.getGoodsStatus(),
						sg.getShopId(), Long.parseLong(InventoryConstanst.StockStatus.EX_WARANTIED),g.getSerial());
				sg.setWarrantyQuantity(wQuan);
				sumQuan += wQuan;
				
				Long exQuan = getSessionBean().getService().getStockSize(sg.getGoodsId(), sg.getGoodsStatus(),
						sg.getShopId(), Long.parseLong(InventoryConstanst.StockStatus.CHANGE_WARANTIED),g.getSerial());
				sg.setExchangedQuantity(exQuan);
				sumQuan += exQuan;
				
				Long bQuan = getSessionBean().getService().getStockSize(sg.getGoodsId(), sg.getGoodsStatus(),
										sg.getShopId(), Long.parseLong(InventoryConstanst.StockStatus.BLOCK_TD),g.getSerial())
						+ getSessionBean().getService().getStockSize(sg.getGoodsId(), sg.getGoodsStatus(),
						sg.getShopId(), Long.parseLong(InventoryConstanst.StockStatus.BLOCK_NC),g.getSerial())
						+ getSessionBean().getService().getStockSize(sg.getGoodsId(), sg.getGoodsStatus(),
						sg.getShopId(), Long.parseLong(InventoryConstanst.StockStatus.BLOCK_XT),g.getSerial());
				sg.setQuantityBlock(bQuan);
				sumQuan += bQuan;
				//HibernateUtil.getSession().evict(sg);
				// sg.setQuantitySold(getSessionBean().getService().getStockSize(sg.getEquipmentProfileId(),
				// sg.getGoodsStatus(), sg.getShopId(),
				// Long.parseLong(InventoryConstanst.StockStatus.SOLD)));
				if (sumQuan == 0){
					removeList.add(sg);
					continue;
				}
				inStockSum = aQuan + inStockSum;
				blockSum = bQuan + blockSum;
			}
			lstSTG.removeAll(removeList);
			lsStockGoods.clear();
			lsStockGoods.addAll(lstSTG);
		}
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('viewStock').show();");
	}

	/**
	 * @return the shop
	 */
	public Shop getShop() {
		return shop;
	}

	/**
	 * @param shop
	 *            the shop to set
	 */
	public void setShop(Shop shop) {
		this.shop = shop;
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
	private List<Boolean> visibleTable = Arrays.asList(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
			true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true);
	public void onToggleActiveEtag(ToggleEvent e) {
		visibleTable.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
	}

	public List<Boolean> getVisibleTable() {
		return visibleTable;
	}

	public void setVisibleTable(List<Boolean> visibleTable) {
		this.visibleTable = visibleTable;
	}

	private List<Boolean> visibleTable2 = Arrays.asList(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
			true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true);
	public void onToggleActiveEtag2(ToggleEvent e) {
		visibleTable2.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
	}

	public List<Boolean> getVisibleTable2() {
		return visibleTable2;
	}

	public void setVisibleTable2(List<Boolean> visibleTable2) {
		this.visibleTable2 = visibleTable2;
	}
}
