/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bean;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.ftu.inventory.bo.*;
import com.ftu.inventory.dao.EquipmentDetailDAO;
import com.ftu.inventory.dao.StockGoodsInvSerialDAO;
import com.ftu.inventory.dto.StockGoodsInvSerialDTO;

import com.ftu.staff.bussiness.AppDomainService;
import com.ftu.utils.DateTimeUtils;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;

import com.ftu.admin.consumer.utils.StringUtil;
import com.ftu.constanst.InventoryConstanst;
import com.ftu.inventory.exportExcel.ExportExcel;
import com.ftu.java.bo.LazyGoodsModel;
import com.ftu.java.bo.LazyShopModel;
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
public class CreateInstockGeneralReport implements Serializable {

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

	private TreeNode root;
	private TreeNode selectedNode;
	private HashMap<Long, TreeNode> mapTreeNode;
	private HashMap<Long, Boolean> mapNodeCollapse;

	private String strKeyFilter;
	private List<ApDomain> listWarantytatus;
	List<EquipmentsDetail> lstAllEquipmentsDetail = new ArrayList<>();
	private Date fromCreateDate;
	private Date toCreateDate;
	private TreeMap<Integer, String> mapRD = new TreeMap<Integer, String>();

	@PostConstruct
	public void init() {
//		sessionBean.loadData(sessionBean.getStaff(), sessionBean.getShop());
		//		listShop = new ArrayList<>();
		//		 _getListShop(sessionBean.getService().getAllShop(sessionBean.getShop().getShopId()));
		//        listShop = sessionBean.getService().getTreeShop(sessionBean.getShop().getShopId());
		listWarantytatus = sessionBean.getService().getListWarantytatus();
		mapNodeCollapse = new HashMap<Long, Boolean>();
		getAllShop();
		listStockStatus = sessionBean.getService().getListSearchStocktatus();
		listStatus = sessionBean.getService().getListViewStocktatus();
		listGoodStt = sessionBean.getService().getListGoodsStatus();
		lsGoodsGroup = sessionBean.getLsgGroup();
		buildTreeShop();
		changeGoodsGroup();
		lsGoods = sessionBean.getLsEquipments();
		this.setFromCreateDate(DateTimeUtils.addDay(new Date(), -30));

		try {
			if (DateTimeUtils.compare2Date(fromCreateDate,DateTimeUtils.convertStringToDate("16-05-2016")) == -1)
			{
				this.setFromCreateDate(DateTimeUtils.convertStringToDate("16-05-2016"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		this.setToCreateDate(new Date());
		createShopId = sessionBean.getShop().getShopId();
		setSelectedTreeNode(createShopId);
		searchStockGood();
		goods = "Tất cả";
		initRomanNumber();
	}
	private void initRomanNumber(){
		mapRD.put(1000, "M");
		mapRD.put(900, "CM");
		mapRD.put(500, "D");
		mapRD.put(400, "CD");
		mapRD.put(100, "C");
		mapRD.put(90, "XC");
		mapRD.put(50, "L");
		mapRD.put(40, "XL");
		mapRD.put(10, "X");
		mapRD.put(9, "IX");
		mapRD.put(5, "V");
		mapRD.put(4, "IV");
		mapRD.put(1, "I");
	}


	public String toRoman(int number) {
		int l =  mapRD.floorKey(number);
		if ( number == l ) {
			return mapRD.get(number);
		}
		return mapRD.get(l) + toRoman(number-l);
	}

	public void setSelectedTreeNode(Long shopId) {
		if (shopId == 0) {

		} else {
			for (Long key : mapTreeNode.keySet()) {
				TreeNode node = mapTreeNode.get(key);
				if (node.isSelected()) {
					node.setSelected(false);
				}
				if (shopId!=null && key!=null && key.equals(shopId)) {
					node.setSelected(true);
					setSelectedNode(node);
				}
			}
		}
	}

	private void getAllShop() {
		try {
			mapTreeNode = new HashMap<Long, TreeNode>();
			listShop = sessionBean.getService().getTreeShopAll(sessionBean.getShop().getShopId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void treesSelect() {
      searchDataForTree();
	}
	
	private void clearTreeDatas() {
		mapTreeNode.clear();
		selectedNode = null;
	}

	private void buildTreeShop() {
		clearTreeDatas();
		this.root = new DefaultTreeNode("", null);
		boolean root = true;
		for (Shop s : listShop) {
			//            if ((s.getShopParentId() == null || (s.getChildShops() !=null && s.getChildShops().size() >0)) && root) {
			if (((s.getChildShops() !=null)) && root) {
				TreeNode node = new DefaultTreeNode(s, this.root);
				//                if(selectedNode == null){
				//                    selectedNode = node;
				//                }
				root = false;
				mapTreeNode.put(s.getShopId(), node);
				if (mapNodeCollapse.containsKey(s.getShopId())) {
					node.setExpanded(false);
				} else {
					node.setExpanded(true);
				}
				List<Shop> listChilds = s.getChildShops();
				if (listChilds != null) {
					if (listChilds.size() > 0) {
						buildTreeNode(s, listChilds, node);
					}
				}
			}
		}
	}

	private void buildTreeNode(Shop parent, List<Shop> list, TreeNode parentNode) {
		for (Shop s : list) {
			if (s.getShopParentId().equals(parent.getShopId())) {
				TreeNode node = new DefaultTreeNode(s, parentNode);
				mapTreeNode.put(s.getShopId(), node);
				if (mapNodeCollapse.containsKey(s.getShopId())) {
					node.setExpanded(false);
				} else {
					node.setExpanded(true);
				}
				List<Shop> listChilds = s.getChildShops();
				if (listChilds.size() > 0) {
					buildTreeNode(s, listChilds, node);
				}
			}
		}
	}

	public void searchDataForTree() {
		mapNodeCollapse.clear();
		reInitDataForTree();
	}

	private void reInitDataForTree() {
		getAllShop();
		if (StringUtil.stringIsNullOrEmty(strKeyFilter) || strKeyFilter.trim().length() <= 0) {
			buildTreeShop();
			return;
		}

		LinkedHashMap<Long, Shop> mapShop = new LinkedHashMap<Long, Shop>();
		List<Shop> list = new ArrayList<Shop>();
		for (Shop s : listShop) {
			if (filterObject(s)) {
				list.add(s);
				mapShop.put(s.getShopId(), s);
				getChildsForShop(mapShop, s.getChildShops(), s);
			}
		}

		for (Shop s : list) {
			getShopRootForShop(mapShop, s);
		}

		list.clear();
		for (Shop s : listShop) {
			if (mapShop.containsKey(s.getShopId())) {
				s.getChildShops().clear();
				for (Shop s2 : mapShop.values()) {
					if (s2.getShopParentId() != null && s2.getShopParentId().equals(s.getShopId())) {
						s.getChildShops().add(s2);
					}
				}
				list.add(s);
			}
		}

		listShop = list;
		buildTreeShop();
	}

	private boolean filterObject(Shop s) {
		if (!StringUtil.stringIsNullOrEmty(strKeyFilter)) {
			String str = s.getShopCode() + " - " +s.getShopName();
//			if (s.getAddress().toUpperCase().contains(strKeyFilter.trim().toUpperCase())
//					|| str.toUpperCase().contains(strKeyFilter.trim().toUpperCase())) {
//				return true;
//			}
			if (str.toUpperCase().contains(strKeyFilter.trim().toUpperCase())) {
				return true;
			}
			return false;
		}
		return true;
	}

	private Shop getShopRootForShop(LinkedHashMap<Long, Shop> mapShop, Shop s) {
		Shop parent = s.getParentShop();
		if (parent != null) {
			mapShop.put(parent.getShopId(), parent);
			getShopRootForShop(mapShop, parent);
		}
		return parent;
	}

	private LinkedHashMap<Long, Shop> getChildsForShop(LinkedHashMap<Long, Shop> mapShop, List<Shop> childs,
			Shop shop) {
		childs = shop.getChildShops();
		if (childs != null) {
			mapShop.put(shop.getShopId(), shop);
			for (Shop s : childs) {
				if(filterObject(s)){
					getChildsForShop(mapShop, childs, s);
				}
			}
		}
		return mapShop;
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
//		if(fromCreateDate == null || toCreateDate == null){
//			languageBean.showMeseage("Info", "export.fromdate.toDate.notnull");
//			return ;
//		}
		inStockSum = 0L;
		blockSum = 0L;
		if (createShopId == null && !listShop.isEmpty()) {
			createShopId = listShop.get(0).getShopId();
		}
		setLsStockGoods(
				sessionBean.getService().searchStockGoods(createShopId, goodsId, groupId, stockGoodsStatus));
		List<StockGoods> removes = new ArrayList<>();
		if (getLsStockGoods() != null) {
			for (StockGoods sg : getLsStockGoods()) {
				EquipmentsProfile g = sessionBean.getService().getGoodsById(sg.getGoodsId());
				if (g != null){
				sg.setProfileName(g.getProfileName());
				sg.setProfileCode(g.getProfileCode());
				sg.setSpecification(g.getSpecification());
				sg.setEquipmentsGroup(sessionBean.getGoodsGroup(g.getEquipmentsGroupId()).getEquipmentsGroupName());
				sg.setGoodsStatusName(getSessionBean().getService().getEquipsStatusName(sg.getGoodsStatus()));

				Long aQuan = getSessionBean().getService().getStockSize(sg.getGoodsId(), sg.getGoodsStatus(),
						sg.getShopId(), Long.parseLong(InventoryConstanst.StockStatus.INSTOCK), g.getSerial());
				sg.setAvailableQuantity(aQuan);
				Long quan = getSessionBean().getService().getStockSize(sg.getGoodsId(), sg.getGoodsStatus(),
						sg.getShopId(), Long.parseLong(InventoryConstanst.StockStatus.BLOCK_TD), g.getSerial())
						+ getSessionBean().getService().getStockSize(sg.getGoodsId(), sg.getGoodsStatus(),
						sg.getShopId(), Long.parseLong(InventoryConstanst.StockStatus.BLOCK_NC), g.getSerial())
						+ getSessionBean().getService().getStockSize(sg.getGoodsId(), sg.getGoodsStatus(),
						sg.getShopId(), Long.parseLong(InventoryConstanst.StockStatus.BLOCK_XT), g.getSerial());
				sg.setQuantityBlock(quan);
				if(quan + aQuan < 1){
					removes.add(sg);
				}
				inStockSum = aQuan + inStockSum;
				blockSum = quan + blockSum;
				}
			}
			getLsStockGoods().removeAll(removes);
		}

		sg = new StockGoods();
		lsData = null;
	}

	public void changeGoodsGroup() {
		if (lsGoodsGroup == null || lsGoodsGroup.isEmpty())
		{
			lsGoods = new ArrayList<>();
			goodsId = -1L;
			goods = "Tất cả";
//			searchStockGood();
			return;
		}
		if (lsGoodsGroup != null && !lsGoodsGroup.isEmpty() && (groupId == null || groupId == 0L)) {
			// setGroupId(lsEquipmentGroup.get(0).getGoodsGroupId());
			lsGoods = sessionBean.getLsEquipments();
//			searchStockGood();
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
		}else {
			lsGoods = sessionBean.getLsEquipments();
		}

		if (lsGoods.isEmpty()) {
//			searchStockGood();
			return;
		} else {
			goodsId = null;
			goods = "Tất cả";
		}
//		searchStockGood();
	}

	public void onNodeSelect(NodeSelectEvent event) {
        
        Shop s = (Shop) selectedNode.getData();
        if (!selectedNode.getParent().isExpanded()) {
            selectedNode.getParent().setExpanded(true);
        }
        if (!selectedNode.isExpanded()) {
            selectedNode.setExpanded(true);
        }
        createShopId = s.getShopId();
        searchStockGood();
    }
	/**
	 * Báo cáo tổng hợp hàng tồn kho
	 * 
	 * @throws IOException
	 */
	public void exportInstockGoodsGeneralReport() throws IOException {
		try {
			ExportExcel ee = new ExportExcel();
			if (getLsStockGoods() != null) {
				for (StockGoods sg : lsStockGoods) {
					EquipmentsProfile g = sessionBean.getService().getGoodsById(sg.getGoodsId());
					if (g != null){
					sg.setProfileName(g.getProfileName());
					sg.setProfileCode(g.getProfileCode());
					sg.setSpecification(g.getSpecification());
					sg.setEquipmentsGroup(sessionBean.getGoodsGroup(g.getEquipmentsGroupId()).getEquipmentsGroupName());
					sg.setGoodsStatusName(getSessionBean().getService().getEquipsStatusName(sg.getGoodsStatus()));
					sg.setUnitName(sessionBean.getUnitByUnitName(g.getUnit()));
					}
				}
			}
			if(lsStockGoods==null||lsStockGoods.isEmpty()){
				languageBean.showMeseage("Info", "export.empty.null");
				return;
			}
			String s = ee.exportListStockGoods(lsStockGoods, sessionBean.getStaff().getStaffName(), sessionBean.getShopById(createShopId).getShopName());
			fileExport = new DefaultStreamedContent(new FileInputStream(s), "xlsx", "baocao_hangton_tonghop.xlsx");
			// downloadFile("exportStocks_1460711967900.xlsx");
		} catch (Exception ex) {
			ex.printStackTrace();
			languageBean.showMeseage("Info", "errorProcess");
		}

	}
	public void exportInstockGoodsGeneralReportKKTB() throws IOException {
		try {
			AppDomainService service = new AppDomainService();
			ExportExcel ee = new ExportExcel();
			 TreeMap<Long, List<StockGoods>> mapType = new TreeMap<Long, List<StockGoods>>();
			List<EquipmentsGroup> lstGroupEquip = sessionBean.getLsgGroup();
			List<ApDomain> lstGroupType = service.findAllByTypeAllStatus(InventoryConstanst.ApDomainType.EQUIP_TYPE);
			List<Long> keys = new ArrayList<>();
			List<StockGoods> results = new ArrayList<>();
			if (getLsStockGoods() != null) {
				int ramdom = 1;
				int index = 1;
				List<StockGoods> lsAll = new ArrayList<>();
				lsAll.addAll(lsStockGoods);
				for (StockGoods sg : lsAll) {
					EquipmentsProfile g = sessionBean.getService().getGoodsById(sg.getGoodsId());
					if (g != null){
						sg.setProfileName(g.getProfileName());
						sg.setProfileCode(g.getProfileCode());
						sg.setSpecification(g.getSpecification());
						sg.setEquipmentsGroup(sessionBean.getGoodsGroup(g.getEquipmentsGroupId()).getEquipmentsGroupName());
						sg.setGoodsStatusName(getSessionBean().getService().getEquipsStatusName(sg.getGoodsStatus()));
						sg.setUnitName(sessionBean.getUnitByUnitName(g.getUnit()));
						if(lstGroupEquip!=null){
							for (EquipmentsGroup gr:lstGroupEquip) {
								if(gr.getEquipmentsGroupId().equals(g.getEquipmentsGroupId())){
									List<StockGoods> lsEquip = mapType.get(gr.getEquipmentsGroupType());
									if(lsEquip == null){
										StockGoods stgRamdom = new StockGoods();
										lsEquip = new ArrayList<>();
										stgRamdom.setIndexStr(toRoman(ramdom++));
										for (ApDomain obj:lstGroupType){
											if(obj.getValue() == null ) continue;
											if(obj.getValue().equals(gr.getEquipmentsGroupType())){
												stgRamdom.setProfileName(obj.getName());
												break;
											}
										}
										lsEquip.add(stgRamdom);
										lsEquip.add(sg);
										sg.setIndexStr((lsEquip.size() - 1) + "");
										sg.setAvailableQuantity(sg.getAvailableQuantity()+sg.getQuantityBlock());
										mapType.put(gr.getEquipmentsGroupType(),lsEquip );
										keys.add(gr.getEquipmentsGroupType());
									}else {
										lsEquip.add(sg);
										sg.setAvailableQuantity(sg.getAvailableQuantity()+sg.getQuantityBlock());
										sg.setIndexStr((lsEquip.size() - 1) + "");
									}
									break;
								}
							}
						}
					}
				}
				for (Long key:keys) {
					results.addAll(mapType.get(key));
				}
			}
			if(results==null||results.isEmpty()){
				languageBean.showMeseage("Info", "export.empty.null");
				return;
			}
			String s = ee.exportListStockGoodsKKVT(results, sessionBean.getStaff().getStaffName(), sessionBean.getShopById(createShopId).getShopName());
			fileExport = new DefaultStreamedContent(new FileInputStream(s), "xlsx", "baocao_kiemke_thietbi.xlsx");
			// downloadFile("exportStocks_1460711967900.xlsx");
		} catch (Exception ex) {
			ex.printStackTrace();
			languageBean.showMeseage("Info", "errorProcess");
		}

	}
	
	public void exportInstockGoodsDetailReport() {
		String templateFileName = "temp_bc_hangton_chitiet.xlsx";
		String fileDownloadName = "baocao_hangton_chitiet.xlsx";
		String sts = "";
		if (stockStatus == null || stockStatus == 0L) {
			if (listStatus != null) {
				for (ApDomain a : listStatus) {
					if(a.getValue() == null
							|| (!a.getValue().toString().equals(InventoryConstanst.StockStatus.INSTOCK)
							&& !a.getValue().toString().equals(InventoryConstanst.StockStatus.BLOCK_TD)
							&& !a.getValue().toString().equals(InventoryConstanst.StockStatus.BLOCK_NC)
							&& !a.getValue().toString().equals(InventoryConstanst.StockStatus.BLOCK_XT))){
						continue;
					}
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
		if (getSg() == null) {
			fileExport = null;
			languageBean.showMeseage("Info", "inven.stock.good.not.found");
			return;
		}

		StockGoodsInvSerialDTO sgis = new StockGoodsInvSerialDTO(fromSerial, getSg().getGoodsStatus(), stockStatus, createShopId,
				providerId, getSg().getGoodsId());
		
		StockGoodsInvSerialDAO sgiDAO = new StockGoodsInvSerialDAO();
		List<StockGoodsInvSerialDTO> datasource = sgiDAO.search(sgis, null, null, null, null );
		if (datasource == null || datasource.isEmpty()) {
			languageBean.showMeseage("Info", "inven.stock.good.not.found");
			fileExport = null;
			return;
		}
//        if (datasource != null) {
//            for (StockGoodsInvSerialDTO s : datasource) {
//                s.setShopName(getShopName(createShopId));
//            }
//        }

        //int dataSize = sgiDAO.searchSize(sgis).intValue();
		
		//int dataSize = sessionBean.getService().searchStockEquipmentsInvSerialSize(fromSerial, null, sgis, null).intValue();
		//List<StockGoodsInvSerialDTO> datasource = sessionBean.getService().searchStockEquipmentsInvSerial(fromSerialTemp, toSerialTemp, SGITemp, null, 0, dataSize);

		if (datasource == null || datasource.isEmpty()) {
			fileExport = null;
			languageBean.showMeseage("Info", "inven.stock.good.not.found");
			return;
		}
		EquipmentDetailDAO dao = new EquipmentDetailDAO();
		lstAllEquipmentsDetail =  dao.findAll();
		for (int i = 0; i < datasource.size(); i++) {
			StockGoodsInvSerialDTO STI = datasource.get(i);
			STI.setIndex(i + 1L);
			
			//if (g != null)
			{
				STI.setGoodsGroupName(sessionBean.getGoodsGroupNameByGoods(STI.getEquipmentProfileId().toString()));
			}
			STI.setStockStatusName(sessionBean.getStockStatusName(STI.getStockStatus()));
			STI.setWarrantyStatusStr(getWarrantyStatusName(STI.getWarrantyStatus()));
			STI.setReasonName(getWarrantyReason(STI.getEquipmentProfileId(), STI.getSerial()));
			STI.setOriginName(sessionBean.getOrigin(STI.getOrigin()));
			System.out.println(STI.getWarantyExpiredDate());
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

	public String getWarrantyReason(Long profileId, String serial){
		if(lstAllEquipmentsDetail==null || profileId==null) return "";
		for (EquipmentsDetail obj:lstAllEquipmentsDetail) {
			if(obj.getEquipmentsProfileId()!=null&&obj.getEquipmentsProfileId().equals(profileId)) {
				if ((serial == null && obj.getSerial() == null) || (serial.isEmpty() && obj.getSerial().isEmpty()) ||
						(obj.getSerial() != null && obj.getSerial().equals(serial))) {
					if (obj.getWarrantyReason() != null) {
						return obj.getWarrantyReason();
					}
				}
			}
		}
		return "";
	}


	private String getWarrantyStatusName(Long status)
	{
		for (ApDomain ap : listWarantytatus) {
			if (ap.getValue().equals(status)) {
				return ap.getName();
			}
		}
		return "";
	}
	public void viewClick() {
		providerId = null;
		//stockStatus = 0L;
		String sts = "";
		if (listStatus != null) {
			for (ApDomain a : listStatus) {
				if(a.getValue() == null
						|| (!a.getValue().toString().equals(InventoryConstanst.StockStatus.INSTOCK)
						&& !a.getValue().toString().equals(InventoryConstanst.StockStatus.BLOCK_TD)
						&& !a.getValue().toString().equals(InventoryConstanst.StockStatus.BLOCK_NC)
						&& !a.getValue().toString().equals(InventoryConstanst.StockStatus.BLOCK_XT))){
					continue;
				}
				if (a.getValue() != null) {
					sts += a.getValue().toString() + ",";
				}
			}
			sts = sts.substring(0, sts.length() - 1);
		}

		// setSg((StockGoods) event.getObject());
		StockGoodsInvSerialDTO sgis = new StockGoodsInvSerialDTO(fromSerial, getSg().getGoodsStatus(), sts, createShopId,
				providerId, getSg().getGoodsId());
		lsData = new LazyGoodsModel(null, null, sgis, null, null);
		fromSerial = "";
		//stockStatus = 0L;
		// AnalysisSerial ann = new AnalysisSerial(null, lsSgi);
		// setLsSerial(ann.analysisInvSerial());
		search();
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
					if(a.getValue() == null
							|| (!a.getValue().toString().equals(InventoryConstanst.StockStatus.INSTOCK)
							&& !a.getValue().toString().equals(InventoryConstanst.StockStatus.BLOCK_TD)
							&& !a.getValue().toString().equals(InventoryConstanst.StockStatus.BLOCK_NC)
							&& !a.getValue().toString().equals(InventoryConstanst.StockStatus.BLOCK_XT))){
						continue;
					}
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
		StockGoodsInvSerialDTO sgis = new StockGoodsInvSerialDTO(fromSerial, getSg().getGoodsStatus(), sts, createShopId,
				providerId, getSg().getGoodsId());
		lsData = new LazyGoodsModel(fromSerial, getToSerial(), sgis, null, null);
	}

	public List<String> completeGoods(String gs) {
		goods = gs;
		List<String> rs = new ArrayList<>();
		rs.add("Tất cả");
		if (lsGoods != null && !lsGoods.isEmpty()) {
			for (EquipmentsProfile g : lsGoods) {
//				if(InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(g.getEquipProfileStatus())){
//					continue;
//				}
				String search = g.getProfileCode() + " | " + g.getProfileName();
				if (gs == null || gs.isEmpty() || search.toLowerCase().contains(goods.toLowerCase())) {
					rs.add(g.getProfileCode() + " | " + g.getProfileName());
				}
			}
		}
		goodsSelect();
		return rs;
	}
	public void goodsSelect() {
		goodsId = null;
		if(goods.equals("Tất cả")){
			return;
		}
		if (lsGoods != null) {
			for (EquipmentsProfile g : lsGoods) {
//				if(InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(g.getEquipProfileStatus())){
//					continue;
//				}
				if ((g.getProfileCode() + " | " + g.getProfileName()).equals(goods)) {
					goodsId = g.getProfileId();
					break;
				}
			}
		}
		if(goodsId==null){
			goods = "Tất cả";
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

	/**
	 * @return the root
	 */
	public TreeNode getRoot() {
		return root;
	}

	/**
	 * @param root the root to set
	 */
	public void setRoot(TreeNode root) {
		this.root = root;
	}

	/**
	 * @return the selectedNode
	 */
	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	/**
	 * @param selectedNode the selectedNode to set
	 */
	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	/**
	 * @return the mapTreeNode
	 */
	public HashMap<Long, TreeNode> getMapTreeNode() {
		return mapTreeNode;
	}

	/**
	 * @param mapTreeNode the mapTreeNode to set
	 */
	public void setMapTreeNode(HashMap<Long, TreeNode> mapTreeNode) {
		this.mapTreeNode = mapTreeNode;
	}

	/**
	 * @return the mapNodeCollapse
	 */
	public HashMap<Long, Boolean> getMapNodeCollapse() {
		return mapNodeCollapse;
	}

	/**
	 * @param mapNodeCollapse the mapNodeCollapse to set
	 */
	public void setMapNodeCollapse(HashMap<Long, Boolean> mapNodeCollapse) {
		this.mapNodeCollapse = mapNodeCollapse;
	}

	/**
	 * @return the strKeyFilter
	 */
	public String getStrKeyFilter() {
		return strKeyFilter;
	}

	/**
	 * @param strKeyFilter the strKeyFilter to set
	 */
	public void setStrKeyFilter(String strKeyFilter) {
		this.strKeyFilter = strKeyFilter;
	}

	public Date getFromCreateDate() {
		return fromCreateDate;
	}

	public void setFromCreateDate(Date fromCreateDate) {
		this.fromCreateDate = fromCreateDate;
	}

	public Date getToCreateDate() {
		return toCreateDate;
	}

	public void setToCreateDate(Date toCreateDate) {
		this.toCreateDate = toCreateDate;
	}

	public List<ApDomain> getListWarantytatus() {
		return listWarantytatus;
	}

	public void setListWarantytatus(List<ApDomain> listWarantytatus) {
		this.listWarantytatus = listWarantytatus;
	}

	public List<EquipmentsDetail> getLstAllEquipmentsDetail() {
		return lstAllEquipmentsDetail;
	}

	public void setLstAllEquipmentsDetail(List<EquipmentsDetail> lstAllEquipmentsDetail) {
		this.lstAllEquipmentsDetail = lstAllEquipmentsDetail;
	}
}
