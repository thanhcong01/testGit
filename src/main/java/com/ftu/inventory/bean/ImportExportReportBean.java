package com.ftu.inventory.bean;

import com.ftu.admin.consumer.utils.StringUtil;
import com.ftu.inventory.bo.ImportExportReport;
import com.ftu.inventory.bo.TransactionActionDetail;
import com.ftu.inventory.dao.ImportExportReportDAO;
import com.ftu.inventory.exportExcel.ExportExcel;
import com.ftu.java.bo.LazyImportExportReportlModel;
import com.ftu.language.LanguageBean;
import com.ftu.staff.bo.Shop;
import com.ftu.staff.dao.ShopDAO;
import com.ftu.utils.DateTimeUtils;

import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.*;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import java.io.FileInputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

@ManagedBean
@ViewScoped
public class ImportExportReportBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;
	@ManagedProperty(value = "#{languageBean}")
	private LanguageBean languageBean;
	private Date fromCreateDate;
	private Date toCreateDate;
	private Long shopId;
	private List<ImportExportReport> lazyReport;
	private Map<Long,String> mapShop;
	
	private transient StreamedContent fileExport;
	private String strKeyFilter;
	private TreeNode root;
	private TreeNode selectedNode;
	private HashMap<Long, TreeNode> mapTreeNode;
	private HashMap<Long, Boolean> mapNodeCollapse;
	private List<Shop> listShop;
	Long createShopId;
	private  Date dateMax;
	@PostConstruct
	public void init() {
		sessionBean.loadData(sessionBean.getStaff(), sessionBean.getShop());
		ShopDAO shopDao = new ShopDAO();
		List<Shop> listShop = shopDao.findAllShopActive();
		mapShop = new LinkedHashMap<>();

		if(listShop != null && listShop.size() >0){
			for(Shop s : listShop){
				mapShop.put(s.getShopId(),s.getShopName());
			}
		}
		this.setShopId(sessionBean.getShop().getShopId());
		this.setFromCreateDate(DateTimeUtils.addDay(new Date(), -30));
		dateMax = new Date();
		try {
			if (DateTimeUtils.compare2Date(fromCreateDate,DateTimeUtils.convertStringToDate("23-09-2016")) == -1)
			{
				this.setFromCreateDate(DateTimeUtils.convertStringToDate("23-09-2016"));
			}
		}catch(Exception e){};
		this.setToCreateDate(new Date());
		mapNodeCollapse = new HashMap<Long, Boolean>();
		getAllShop();
		buildTreeShop();
		createShopId = sessionBean.getShop().getShopId();
		setSelectedTreeNode(createShopId);
		search();
	}

	public void search(){
		try {
			ImportExportReportDAO dao = new ImportExportReportDAO();

			if(fromCreateDate == null || toCreateDate == null){
				languageBean.showMeseage("Info", "export.fromdate.toDate.notnull");
				return ;
			}
		if(DateTimeUtils.compare2Date(fromCreateDate, toCreateDate) > 0){
			languageBean.showMeseage("Info", "export.fromdate.not.than.todate");
			return ;
		}
			SimpleDateFormat sm = new SimpleDateFormat("dd-MMM-yy");
			lazyReport =   dao.getDataForReport(sm.format(fromCreateDate),sm.format(toCreateDate),createShopId,null,null);
			for (ImportExportReport obj:lazyReport ) {
				obj.setUnitName(sessionBean.getUnitByUnitName(obj.getUnit()));
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}

	}

	public void export_excel() {
		try {
			ImportExportReportDAO dao = new ImportExportReportDAO();
			SimpleDateFormat sm = new SimpleDateFormat("dd-MMM-yy");
			List<ImportExportReport> lsResult =
					dao.getDataForReport(sm.format(fromCreateDate),sm.format(toCreateDate),createShopId,null,null);
			int i = 0;
			for (ImportExportReport obj:lsResult ) {
				obj.setUnitName(sessionBean.getUnitByUnitName(obj.getUnit()));
				obj.setCount(++i);
			}

			 
			ExportExcel ee = new ExportExcel();
			 String s = ee.exportImportExportInstockReport(fromCreateDate,toCreateDate,mapShop.get(shopId), lsResult,0l,0l,0l,0l);
			 setFileExport(new DefaultStreamedContent(new FileInputStream(s),
			 "xlsx", "bc_tonghop_xuatnhap_kho.xlsx"));	
			 
		} catch (Exception ex) {
			ex.printStackTrace();
			languageBean.showMeseage("Info", "errorProcess");
		}
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

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public List<ImportExportReport> getLazyReport() {
		return lazyReport;
	}

	public void setLazyReport(List<ImportExportReport> lazyReport) {
		this.lazyReport = lazyReport;
	}

	public Map<Long, String> getMapShop() {
		return mapShop;
	}

	public void setMapShop(Map<Long, String> mapShop) {
		this.mapShop = mapShop;
	}

	public SessionBean getSessionBean() {
		return sessionBean;
	}

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}

	public LanguageBean getLanguageBean() {
		return languageBean;
	}

	public void setLanguageBean(LanguageBean languageBean) {
		this.languageBean = languageBean;
	}

	public StreamedContent getFileExport() {
		return fileExport;
	}

	public void setFileExport(StreamedContent fileExport) {
		this.fileExport = fileExport;
	}

	public String getStrKeyFilter() {
		return strKeyFilter;
	}

	public void setStrKeyFilter(String strKeyFilter) {
		this.strKeyFilter = strKeyFilter;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public HashMap<Long, TreeNode> getMapTreeNode() {
		return mapTreeNode;
	}

	public void setMapTreeNode(HashMap<Long, TreeNode> mapTreeNode) {
		this.mapTreeNode = mapTreeNode;
	}

	public HashMap<Long, Boolean> getMapNodeCollapse() {
		return mapNodeCollapse;
	}

	public void setMapNodeCollapse(HashMap<Long, Boolean> mapNodeCollapse) {
		this.mapNodeCollapse = mapNodeCollapse;
	}

	public List<Shop> getListShop() {
		return listShop;
	}

	public void setListShop(List<Shop> listShop) {
		this.listShop = listShop;
	}

	public void treesSelect() {
		searchDataForTree();
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
	private void getAllShop() {
		try {
			mapTreeNode = new HashMap<Long, TreeNode>();
			listShop = sessionBean.getService().getTreeShopAll(sessionBean.getShop().getShopId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private boolean filterObject(Shop s) {
		if (!StringUtil.stringIsNullOrEmty(strKeyFilter)) {
			String str = s.getShopCode() + " - " +s.getShopName();
			if (str.toUpperCase().contains(strKeyFilter.trim().toUpperCase())) {
				return true;
			}
			return false;
		}
		return true;
	}
	private void clearTreeDatas() {
		mapTreeNode.clear();
		selectedNode = null;
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
	private Shop getShopRootForShop(LinkedHashMap<Long, Shop> mapShop, Shop s) {
		Shop parent = s.getParentShop();
		if (parent != null) {
			mapShop.put(parent.getShopId(), parent);
			getShopRootForShop(mapShop, parent);
		}
		return parent;
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

	public Long getCreateShopId() {
		return createShopId;
	}

	public void setCreateShopId(Long createShopId) {
		this.createShopId = createShopId;
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
		search();
	}

	public Date getDateMax() {
		return dateMax;
	}

	public void setDateMax(Date dateMax) {
		this.dateMax = dateMax;
	}
}