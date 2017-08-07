/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.ws;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.ftu.constanst.InventoryConstanst;
import com.ftu.exception.AppException;
import com.ftu.inventory.bo.*;
import com.ftu.inventory.dao.*;
import com.ftu.inventory.dto.StockGoodsInvSerialDTO;
import com.ftu.java.business.*;
import com.ftu.staff.bo.ApDomain;
import com.ftu.staff.bo.Position;
import com.ftu.staff.bo.Shop;
import com.ftu.staff.bo.Staff;
import com.ftu.staff.dao.AppDomainDAO;
import com.ftu.staff.dao.ShopDAO;
import com.ftu.staff.dao.StaffDAO;
import com.ftu.utils.DateTimeUtils;

/**
 *
 * @author E5420
 */
public class BusinessService implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	// Menu
	private Long shopId;
	private Long staffId;

	public BusinessService(Long shopId, Long staffId) {
		this.shopId = shopId;
		this.staffId = staffId;
	}
	public BusinessService(){
	}

	public Shop getShopById() {
		ShopDAO sDAO = new ShopDAO();
		return sDAO.findById(shopId);
	}

	public Shop getShopChildById(Long id) {
		ShopDAO sDAO = new ShopDAO();
		return sDAO.findById(id);
	}

	public Long getStockSize(Long goodsId, Long status, Long shopId, Long stockSts, boolean hasSerial) {
		if (hasSerial) {
			StockGoodsInvSerialDAO sgiDAO = new StockGoodsInvSerialDAO();
			return sgiDAO.getSizeStock(goodsId, status, shopId, stockSts);
		}
		else
		{
			StockGoodsInvNoSerialDAO sginDAO = new StockGoodsInvNoSerialDAO();
			return sginDAO.getSizeStock(goodsId, status, shopId, stockSts);
		}
	}

	public String getShopNameById(Long id) {
		ShopDAO dao = new ShopDAO();
		if (dao.findById(id) == null) {
			return null;
		}
		return dao.findById(id).getShopName();
	}

	public String getShopAddressById(Long id) {
		ShopDAO dao = new ShopDAO();
		if (dao.findById(id) == null) {
			return null;
		}
		return dao.findById(id).getAddress();
	}

	public List<TransactionActionDetail> searchSizeListIm(String transactionActionCode, Long toShopId, Date fromCreateDatetime, Date toCreateDatetime,
			List<Long> lsType, List<ApDomain> lsStatus, Integer start, Integer get) {
		TransactionActionDetailDAO taDAO = new TransactionActionDetailDAO();
		return taDAO.searchToShop(transactionActionCode,toShopId, fromCreateDatetime, toCreateDatetime, lsType, lsStatus, start, get, null, null);
	}

	public List<StockGoodsInvSerial> searchBlockSerial(String from, String to, StockGoodsInvSerial sgi,
			Long goodsGroupId, Long toShopId) {
		if (from != null && from.length() >= 8)
			from = from.substring(from.length() - 8);
		if (to != null && to.length() >= 8)
			to = to.substring(to.length() - 8);
		StockGoodsInvSerialDAO sgiDAO = new StockGoodsInvSerialDAO();
		return sgiDAO.searchBlockSerial(from, to, sgi, goodsGroupId, toShopId);
	}

	public List<InvImExReportBo> searchInvImExRp(Date fromDate, Date toDate, Long fromShopId,Long toShopId,String orderType, String orderStatus,String orderCode) {

		StockGoodsInvSerialDAO sgiDAO = new StockGoodsInvSerialDAO();
		return sgiDAO.searchInvImExRp(fromDate, toDate, fromShopId, toShopId, orderType,orderStatus,orderCode);
	}

	public List<StockGoodsInvSerial> searchGeneralBlockSerial(String from, String to, StockGoodsInvSerial sgi,
			Long goodsGroupId, String codePath) {
		if (from != null && from.length() >= 8)
			from = from.substring(from.length() - 8);
		if (to != null && to.length() >= 8)
			to = to.substring(to.length() - 8);
		StockGoodsInvSerialDAO sgiDAO = new StockGoodsInvSerialDAO();
		return sgiDAO.searchGeneralBlockSerial(from, to, sgi, goodsGroupId,codePath);
	}

	public Long getSizeStock(Long goodsGroupId, Long goodsId, Long status, Long shopId, Long stockSts) {
		StockGoodsInvSerialDAO sgiDAO = new StockGoodsInvSerialDAO();
		return sgiDAO.getStockSize(goodsGroupId, goodsId, status, shopId, stockSts);
	}
	public StockGoods getStockGoods(Long goodsId, Long status, Long shopId) {
		StockGoodsDAO sDAO = new StockGoodsDAO();
		return sDAO.getStockGoods(goodsId, status, shopId);
	}
	public void saveOrUpdateStockGoods(StockGoods g) {
		StockGoodsDAO sDAO = new StockGoodsDAO();
		sDAO.saveOrUpdate(g);
	}
	public void updateQuantity(Boolean desc, Long quan, Long goodsId, Long status, Long shopId) {
		StockGoodsDAO sDAO = new StockGoodsDAO();
		sDAO.updateQuantity(desc,quan, goodsId, status, shopId );
	}
	public void updateQuantityDetailUpdate(Boolean desc, Long quan, Long goodsId, Long status, Long shopId) {
		StockGoodsDAO sDAO = new StockGoodsDAO();
		sDAO.updateQuantityDetailUpdate(desc,quan, goodsId, status, shopId );
	}

	public Long getStockSizeByTaId(Long taId, String stockStatus, Long gId, Long gStatus) {
		StockGoodsInvSerialDAO sgiDAO = new StockGoodsInvSerialDAO();
		return sgiDAO.getSizeByTaId(taId, stockStatus, gId, gStatus);
	}

	public List<StockGoods> getStockGoodsByShop(Long shopId) {
		StockGoodsDAO sDAO = new StockGoodsDAO();
		return sDAO.getByShopId(shopId);
	}

	public List<StockGoodsInvNoSerial> getStockGoodNoSerialByShops(List<Long> shopId) {
		StockGoodsInvNoSerialDAO sDAO = new StockGoodsInvNoSerialDAO();
		return sDAO.getStockGoodsNoserialByShopId(shopId);
	}
	public List<StockGoods> searchStockGoods(Long sId, Long goodsId, Long goodsGroupId, Long stockGoodsStatus) {
		StockGoodsDAO sDAO = new StockGoodsDAO();
		return sDAO.searchStockGoods(sId, goodsId, goodsGroupId, stockGoodsStatus);
	}
	public List<ImportExportReport> searchDataForReport(Date fromDate,Date toDate,Long shopId){
		ImportExportReportDAO dao = new ImportExportReportDAO();
		return dao.searchDataForReport(fromDate,toDate,shopId);
	}
	public EquipmentsGroup getEquipmentsGroup(Long gId) {
		List<EquipmentsGroup> lsgGroup = getAllEquipmentsGroup();
		if (lsgGroup != null && gId != null) {
			for (EquipmentsGroup g : lsgGroup) {
				if (gId.equals(g.getEquipmentsGroupId())) {
					return g;
				}
			}
		}
		return null;
	}

	// SessionBean
	public List<ApDomain> getListGoodsStatus() {
		AppDomainDAO apDAO = new AppDomainDAO();
		return apDAO.findAllByType(InventoryConstanst.ApDomainType.GOODS_STATUS);
	}
	public List<ApDomain> getListActionType() {
		AppDomainDAO apDAO = new AppDomainDAO();
		return apDAO.findAllByType(InventoryConstanst.ApDomainType.ACTION_TYPE);
	}
	public List<ApDomain> getListUnitType() {
		AppDomainDAO apDAO = new AppDomainDAO();
		return apDAO.findAllByTypeAllStatus(InventoryConstanst.ApDomainType.UNIT_TYPE);
	}
	public List<ApDomain> getFieldNameActionAudit() {
		AppDomainDAO apDAO = new AppDomainDAO();
		return apDAO.findAllByType(InventoryConstanst.ApDomainType.ACTION_AU_DETAIL);
	}
	public List<ApDomain> getListEquipmentStatu() {
		AppDomainDAO apDAO = new AppDomainDAO();
		return apDAO.findAllByType(InventoryConstanst.ApDomainType.TRANS_STATUS);
	}
	public List<ApDomain> getListMaintenanceStatus() {
		AppDomainDAO apDAO = new AppDomainDAO();
		return apDAO.findAllByType(InventoryConstanst.ApDomainType.MANTENANCE_TYPE);
	}
	public List<ApDomain> getListOrigin() {
		AppDomainDAO apDAO = new AppDomainDAO();
		return apDAO.findAllByType(InventoryConstanst.ApDomainType.ORIGIN_TYPE);
	}

	public String getEquipsStatusName(Long id) {
		for (ApDomain d : getListGoodsStatus()) {
			if (d!= null && d.getValue().equals(id)) {
				return d.getName();
			}
		}
		return "";
	}
	public String getUnitName(Long id) {
		List<ApDomain> objs = getListUnitType();
		for (ApDomain d : objs) {
			if(d.getValue()==null) continue;
			if (d.getValue().equals(id)) {
				return d.getName();
			}
		}
		return "";
	}
	public String getTockStatusName(Long id) {
		for (ApDomain d : getListSearchStocktatus()) {
			if(d.getValue()==null) continue;
			if (d.getValue().equals(id)) {
				return d.getName();
			}
		}
		return "";
	}
	public String getWaranStatusName(Long id) {
		for (ApDomain d : getListWarantytatus()) {
			if(d.getValue()==null) continue;
			if (d.getValue().equals(id)) {
				return d.getName();
			}
		}
		return "";
	}

	public EquipmentsProfile getGoodsById(Long goodId) {
		EquipmentsProfileDAO dao = new EquipmentsProfileDAO();
		if (dao.findByProfileId(goodId) == null) {
			return null;
		}
		return dao.findByProfileId(goodId);
	}

	public List<ApDomain> getListSearchStocktatus() {
		AppDomainDAO apDAO = new AppDomainDAO();
		return apDAO.findAllByType(InventoryConstanst.ApDomainType.STOCK_STATUS);
	}
	public List<ApDomain> getListWarantytatus() {
		AppDomainDAO apDAO = new AppDomainDAO();
		return apDAO.findAllByType(InventoryConstanst.ApDomainType.WARAN_TATUS);
	}

	public List<ApDomain> getListViewStocktatus() {
		List<ApDomain> rs = new ArrayList<>();
		List<ApDomain> removes = new ArrayList<>();
		AppDomainDAO apDAO = new AppDomainDAO();
		rs.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.STOCK_STATUS,
				Long.parseLong(InventoryConstanst.StockStatus.INSTOCK)));
		rs.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.STOCK_STATUS,
				Long.parseLong(InventoryConstanst.StockStatus.BLOCK_TD)));
		rs.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.STOCK_STATUS,
				Long.parseLong(InventoryConstanst.StockStatus.BLOCK_NC)));
		rs.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.STOCK_STATUS,
				Long.parseLong(InventoryConstanst.StockStatus.BLOCK_XT)));

		rs.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.STOCK_STATUS,
				Long.parseLong(InventoryConstanst.StockStatus.CANCEL)));

		rs.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.STOCK_STATUS,
				Long.parseLong(InventoryConstanst.StockStatus.EX_USED)));
		rs.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.STOCK_STATUS,
				Long.parseLong(InventoryConstanst.StockStatus.USED)));

		rs.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.STOCK_STATUS,
				Long.parseLong(InventoryConstanst.StockStatus.EX_WARANTIED)));
		rs.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.STOCK_STATUS,
				Long.parseLong(InventoryConstanst.StockStatus.CHANGE_WARANTIED)));
		if(rs==null) return null;
		List<ApDomain> lstRemove = new ArrayList<>();
		for (ApDomain apDomain : rs) {
			if (apDomain == null)
				removes.add(apDomain);
		};
		rs.removeAll(removes);
		return rs;
	}

	public List<ApDomain> getListViewStockSold() {
		List<ApDomain> rs = new ArrayList<>();
		AppDomainDAO apDAO = new AppDomainDAO();
		rs.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.STOCK_STATUS,
				Long.parseLong(InventoryConstanst.StockStatus.SOLD)));
		List<ApDomain> lstRemove = new ArrayList<>();
		for (ApDomain apDomain : rs) {
			if (apDomain == null)
				lstRemove.add(apDomain);
		};
		rs.removeAll(lstRemove);
		return rs;
	}

	public List<ApDomain> getListViewStocktatusForExport() {
		List<ApDomain> rs = new ArrayList<>();
		AppDomainDAO apDAO = new AppDomainDAO();
		rs.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.STOCK_STATUS,
				Long.parseLong(InventoryConstanst.StockStatus.INSTOCK)));
		rs.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.STOCK_STATUS,
				Long.parseLong(InventoryConstanst.StockStatus.BLOCK_TD)));
		rs.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.STOCK_STATUS,
				Long.parseLong(InventoryConstanst.StockStatus.BLOCK_NC)));
		rs.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.STOCK_STATUS,
				Long.parseLong(InventoryConstanst.StockStatus.BLOCK_XT)));
		rs.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.STOCK_STATUS,
				Long.parseLong(InventoryConstanst.StockStatus.INSTOCK_ERR)));
		List<ApDomain> lstRemove = new ArrayList<>();
		for (ApDomain apDomain : rs) {
			if (apDomain == null)
				lstRemove.add(apDomain);
		};
		rs.removeAll(lstRemove);
		return rs;
	}

	public List<Shop> findAllShop() {
		ShopDAO shopDAO = new ShopDAO();
		return shopDAO.findAllShop();
	}

	public List<EquipmentsGroup> getAllEquipmentsGroup() {
		EquipmentsGroupDAO ggDAO = new EquipmentsGroupDAO();
		return ggDAO.findAll();
	}
	public List<EquipmentsGroup> getAllEquipmentsGroupOrderCode() {
		EquipmentsGroupDAO ggDAO = new EquipmentsGroupDAO();
		return ggDAO.findAllOrderCode();
	}
	public List<EquipmentsGroup> getAllEquipmentsGroupActive() {
		EquipmentsGroupDAO ggDAO = new EquipmentsGroupDAO();
		return ggDAO.findAllActive();
	}

	public List<EquipmentsProfile> getAllGoods() {
		EquipmentsProfileDAO gsDAO = new EquipmentsProfileDAO();
		return gsDAO.getAllProfiles();
	}

	public List<Provider> getAllProvider() {
		ProviderDAO pvDAO = new ProviderDAO();
		return pvDAO.getAllProviders();
	}
	public List<Provider> getAllProviderActive() {
		ProviderDAO pvDAO = new ProviderDAO();
		return pvDAO.getAllProviderActive();
	}

	public List<Provider> getActiveProvider() {
		ProviderDAO pvDAO = new ProviderDAO();
		return pvDAO.getProvidersByStatus(InventoryConstanst.ProviderStatus.ACTIVE.toString());
	}

	// ApproveGoods
	public List<Staff> getListStaffByShop(Long sid) {
		StaffDAO sfDAO = new StaffDAO();
		return sfDAO.findByShopId(sid == null ? shopId : sid);
	}

	public List<ApDomain> getApproveType(String tranType) {
		List<ApDomain> reasons = new ArrayList<>();
		AppDomainDAO apDAO = new AppDomainDAO();
		if(tranType==null || tranType.isEmpty()){
			tranType = InventoryConstanst.TransactionType.IM;
		}
		reasons.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_TYPE,
				Long.parseLong(tranType)));
		List<ApDomain> lstRemove = new ArrayList<>();
		for (ApDomain apDomain : reasons) {
			if (apDomain == null)
				lstRemove.add(apDomain);
		};
		reasons.removeAll(lstRemove);
		return reasons;
	}

	public List<ApDomain> getApproveStatus() {
		List<ApDomain> listStatus = new ArrayList<>();
		AppDomainDAO apDAO = new AppDomainDAO();
		listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
				Long.parseLong(InventoryConstanst.TransactionStatus.CREATE)));
		listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
				Long.parseLong(InventoryConstanst.TransactionStatus.APPROVE)));
		listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
				Long.parseLong(InventoryConstanst.TransactionStatus.CANCEL)));
		List<ApDomain> lstRemove = new ArrayList<>();
		for (ApDomain apDomain : listStatus) {
			if (apDomain == null)
				lstRemove.add(apDomain);
		};
		listStatus.removeAll(lstRemove);
		return listStatus;
	}
	
	public List<ApDomain> getApproveStatusWarranty() {
		List<ApDomain> listStatus = new ArrayList<>();
		AppDomainDAO apDAO = new AppDomainDAO();
		listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
				Long.parseLong(InventoryConstanst.TransactionStatus.CREATE_WARRANTY)));
		listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
				Long.parseLong(InventoryConstanst.TransactionStatus.CANCEL_WARRANTY)));
		listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
				Long.parseLong(InventoryConstanst.TransactionStatus.IM_WARRANTY)));
		List<ApDomain> lstRemove = new ArrayList<>();
		for (ApDomain apDomain : listStatus) {
			if (apDomain == null)
				lstRemove.add(apDomain);
		};
		listStatus.removeAll(lstRemove);
		return listStatus;
	}
	
	public List<ApDomain> getApproveStatusStaff() {
		List<ApDomain> listStatus = new ArrayList<>();
		AppDomainDAO apDAO = new AppDomainDAO();
		listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
				Long.parseLong(InventoryConstanst.TransactionStatus.CREATE_STAFF)));
		listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
				Long.parseLong(InventoryConstanst.TransactionStatus.EX_STAFF)));
		List<ApDomain> lstRemove = new ArrayList<>();
		for (ApDomain apDomain : listStatus) {
			if (apDomain == null)
				lstRemove.add(apDomain);
		};
		listStatus.removeAll(lstRemove);
		return listStatus;
	}

	public Long getStockTransactionSerialSize(Long providerId, Long goodsId, Long trasActionId, Long goodsStatus) {
		StockTransactionSerialDAO sgiDAO = new StockTransactionSerialDAO();
		return sgiDAO.getSize(providerId, goodsId, trasActionId, goodsStatus);
	}

	public Long getStockSerialSizeByDetail(Long detailId) {
		StockTransactionSerialDAO sgiDAO = new StockTransactionSerialDAO();
		return sgiDAO.getSizeByDetail(detailId);
	}

	public List<TransactionActionDetail> getTranActionDetailsByTransId(Long transActionId) {
		TransactionActionDetailDAO taDAO = new TransactionActionDetailDAO();
		return taDAO.getTransactionActionDetails(transActionId);
	}

	public List<TransactionAction> searchTransactionAction(TransactionAction ta, List<ApDomain> lsType,
			List<ApDomain> lsStatus, Integer start, Integer get) {
		TransactionActionDAO taDAO = new TransactionActionDAO();
		return taDAO.search(ta, lsType, lsStatus, start, get);
	}

	public List<TransactionAction> searchAndSortTransactionAction(TransactionAction ta, List<ApDomain> lsType,
			List<ApDomain> lsStatus, Integer start, Integer get, boolean desc, String sortField) {
		TransactionActionDAO taDAO = new TransactionActionDAO();
		return taDAO.searchAndSort(ta, lsType, lsStatus, start, get, desc, sortField);
	}

	public Long searchTransactionActionSize(TransactionAction ta, List<ApDomain> lsType, List<ApDomain> lsStatus) {
		TransactionActionDAO taDAO = new TransactionActionDAO();
		return taDAO.searchSize(ta, lsType, lsStatus);
	}

	public Long searchTransactionActionSize(Long shopId, Boolean isImport, String[] lsType, String stautus) {

		List<ApDomain> lpStatus = new ArrayList<>();
		List<ApDomain> lpType = new ArrayList<>();
		TransactionAction ta = new TransactionAction();
		if (isImport) {
			ta.setToShopId(shopId);
		} else {
			ta.setFromShopId(shopId);
		}
		for (String s : lsType) {
			lpType.add(new ApDomain(null, new Long(s)));
		}
		lpStatus.add(new ApDomain(null, new Long(stautus)));
		TransactionActionDAO taDAO = new TransactionActionDAO();
		return taDAO.searchSize(ta, lpType, lpStatus);
	}

	public List<TransactionActionDetail> getTransactionActionDetailByTransactionActionId(Long id) {
		TransactionActionDetailDAO transactionActionDetailDAO = new TransactionActionDetailDAO();
		if (transactionActionDetailDAO.getTransactionActionDetailByTransactionActionId(id) == null) {
			return null;
		}
		return transactionActionDetailDAO.getTransactionActionDetailByTransactionActionId(id);
	}

	public Boolean approveApprove(TransactionAction ta) {
		ApproveGoods appDAO = new ApproveGoods(ta, null);
		return appDAO.approve();
	}

	public Boolean approveReject(TransactionAction ta) {
		ApproveGoods appDAO = new ApproveGoods(ta, null);
		return appDAO.reject();
	}

	public Boolean approveApproveWaranty(TransactionAction ta, Long handleType) {
		ApproveGoods appDAO = new ApproveGoods(ta, handleType);
		return appDAO.approveWaranty();
	}
	public String getSerialOldBySerialNew(String newValue, String feildName) {
		EquipmentHistoryDetailDAO appDAO = new EquipmentHistoryDetailDAO();
		return appDAO.getSerialOldBySerialNew(newValue, feildName);
	}

	public EquipmentHistoryDetail getDetailBySerialOld(String oldValue, String feildName) {
		EquipmentHistoryDetailDAO appDAO = new EquipmentHistoryDetailDAO();
		return appDAO.getDetailBySerialOld(oldValue, feildName);
	}
	public void deleteHisDetailSerial(String newValue, String oldValue, String feildName) {
		EquipmentHistoryDetailDAO appDAO = new EquipmentHistoryDetailDAO();
		appDAO.deleteHisDetailSerial(oldValue, feildName);
	}

	public Boolean approveRejectWaranty(TransactionAction ta, Long handleType) {
		ApproveGoods appDAO = new ApproveGoods(ta, handleType);
		return appDAO.rejectWaranty();
	}

	public Long checkApproveGoods(Long taId) {
		StockGoodsInvSerialDAO sgisDAO = new StockGoodsInvSerialDAO();
		return sgisDAO.checkApproveGoods(taId);
	}

	// Evaluate
	public List<StockGoodsInvSerialDTO> searchStockEquipmentsInvSerial(String from, String to, StockGoodsInvSerialDTO sgi,
			Long goodsGroupId, Integer start, Integer get) {
		//		if (from != null && from.length() >= 8)
		//			from = from.substring(from.length() - 8);
		//		if (to != null && to.length() >= 8)
		//			to = to.substring(to.length() - 8);
		StockGoodsInvSerialDAO sgisDAO = new StockGoodsInvSerialDAO();
		EquipmentsProfileDAO profileDAO = new EquipmentsProfileDAO();
		EquipmentsProfile profile = null;
		List<StockGoodsInvSerialDTO> lsSearch = new ArrayList<>();
		if(sgi.getEquipmentProfileId()!=null && sgi.getEquipmentProfileId() > 0L){
			profile = profileDAO.findById(sgi.getEquipmentProfileId());
		}
		lsSearch.addAll(sgisDAO.search(sgi, start, get, null, null));
		lsSearch.addAll(sgisDAO.searchNoserial(sgi, start, get, null, null));
		return lsSearch;
//		if(profile == null
//				|| InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
//			return sgisDAO.search(sgi, start, get);
//		}else {
//			if (sgi.getSerial()==null || sgi.getSerial().trim().isEmpty()){
//				return sgisDAO.searchNoserial(sgi, start, get);
//			}
//			return new ArrayList<>();
//		}

	}
	public void updateStockGoodsEquipstatus(List<Long> ids, Long equimentStatus) {
		StockGoodsInvSerialDAO sgisDAO = new StockGoodsInvSerialDAO();
		sgisDAO.updateStockGoodsEquipstatus(ids, equimentStatus);
	}

	public void updateStockNoSerialEquipstatus(Long gStatus, String stockSts, Long id, Long quantity)  {
		StockGoodsInvNoSerialDAO sgisDAO = new StockGoodsInvNoSerialDAO();
		sgisDAO.updateStatusBySql(gStatus, stockSts, id, quantity);
	}

	public void saveOrUpdateSerial(StockGoodsInvSerial stockGoodsInvSerial){
		StockGoodsInvSerialDAO sgisDAO = new StockGoodsInvSerialDAO();
		sgisDAO.saveOrUpdate(stockGoodsInvSerial);
	}
	public void saveOrUpdateSerial(StockGoodsInvSerialDTO stockGoodsInvSerialDTO){
		StockGoodsInvSerialDAO sgisDAO = new StockGoodsInvSerialDAO();
		sgisDAO.saveOrUpdate(sgisDAO.convertDTOtoEntity(stockGoodsInvSerialDTO));
	}
	public void saveOrUpdateNoSerial(StockGoodsInvNoSerial stockGoodsInvNoSerial){
		StockGoodsInvNoSerialDAO sgisDAO = new StockGoodsInvNoSerialDAO();
		sgisDAO.saveOrUpdate(stockGoodsInvNoSerial);
	}
	public StockGoodsInvNoSerial getStockGoodsNoserial(Long goodsId, Long status, Long shopId, String stockStatus) {
		StockGoodsInvNoSerialDAO sgisDAO = new StockGoodsInvNoSerialDAO();
		return sgisDAO.getStockGoodsNoserial(goodsId, status,
				shopId, stockStatus);
	}
	public StockGoodsInvSerial findBySerial(String serial) {
		StockGoodsInvSerialDAO sgisDAO = new StockGoodsInvSerialDAO();
		return sgisDAO.findBySerial(serial);
	}


	public List<StockGoodsInvSerialDTO> searchEquipment(StockGoodsInvSerialDTO sgi,
			Integer start, Integer get) {
		StockGoodsInvSerialDAO sgisDAO = new StockGoodsInvSerialDAO();
		return sgisDAO.searchEquipment(sgi, start, get);
	}
	public List<StockGoodsInvSerialDTO> searchEquipmentNoSerial(StockGoodsInvSerialDTO sgi,
			Integer start, Integer get) {
		StockGoodsInvSerialDAO sgisDAO = new StockGoodsInvSerialDAO();
		return sgisDAO.searchEquipmentNoSerial(sgi, start, get);
	}
	public List<StockGoodsInvSerialDTO> searchEquipmentId(Long equipmentid,
			Integer start, Integer get) {
		StockGoodsInvSerialDAO sgisDAO = new StockGoodsInvSerialDAO();
		return sgisDAO.searchEquipmentId(equipmentid, start, get);
	}
	public List<StockGoodsInvSerialDTO> searchEquipmentAudit(String serial,
			Integer start, Integer get) {
		StockGoodsInvSerialDAO sgisDAO = new StockGoodsInvSerialDAO();
		return sgisDAO.searchActionAudit(serial, start, get);
	}
	public List<StockGoodsInvSerialDTO> searchMainTen(String serial, Long equipmentId,
			Integer start, Integer get) {
		StockGoodsInvSerialDAO sgisDAO = new StockGoodsInvSerialDAO();
		return sgisDAO.searchMaintainEquiment(serial,  equipmentId, start, get);
	}

	public List<StockGoodsInvSerialDTO> searchStockEquipmentsInvSerialContainDatetime(Date fromDate, Date toDate, String from,
			String to, StockGoodsInvSerial sgi, Long goodsGroupId, Integer start, Integer get) {
		if (from != null && from.length() >= 8)
			from = from.substring(from.length() - 8);
		if (to != null && to.length() >= 8)
			to = to.substring(to.length() - 8);
		StockGoodsInvSerialDAO sgisDAO = new StockGoodsInvSerialDAO();
		return sgisDAO.searchContainOutstockDateTime(fromDate, toDate, from, to, sgi, goodsGroupId, start, get);
	}

	public Long getSizeStockWithLsGoodsId(List<Long> listGoodsId, Long status, Long shopId, Long stockSts) {
		StockGoodsInvSerialDAO sgisDAO = new StockGoodsInvSerialDAO();
		return sgisDAO.getSizeStock(listGoodsId, status, shopId, stockSts);
	}

	public Long searchSizeToShop(String transactionActionCode,Long toShopId, Date fromCreateDatetime, Date toCreateDatetime, List<Long> lsType,
			List<ApDomain> lsStatus) {
		TransactionActionDetailDAO dao = new TransactionActionDetailDAO();
		return dao.searchSizeToShop(transactionActionCode, toShopId, fromCreateDatetime, toCreateDatetime, lsType, lsStatus);
	}

	public Long searchSizeFromShop(String transactionActionCode, Long fromShopId, Date fromCreateDatetime, Date toCreateDatetime, List<Long> lsType,
			List<ApDomain> lsStatus) {
		TransactionActionDetailDAO dao = new TransactionActionDetailDAO();
		return dao.searchSizeFromShop(transactionActionCode,fromShopId, fromCreateDatetime, toCreateDatetime, lsType, lsStatus);
	}

	public List<TransactionActionDetail> searchFromShop(String transactionActionCode, Long fromShopId, Date fromCreateDatetime, Date toCreateDatetime,
			List<Long> lsType, List<ApDomain> lsStatus, Integer start, Integer get) {
		TransactionActionDetailDAO dao = new TransactionActionDetailDAO();
		return dao.searchFromShop(transactionActionCode, fromShopId, fromCreateDatetime, toCreateDatetime, lsType, lsStatus, start, get, null, null);
	}

	public List<TransactionActionDetail> searchToShop(String transactionActionCode, Long toShopId, Date fromCreateDatetime, Date toCreateDatetime,
			List<Long> lsType, List<ApDomain> lsStatus, Integer start, Integer get) {
		TransactionActionDetailDAO dao = new TransactionActionDetailDAO();
		return dao.searchToShop(transactionActionCode,toShopId, fromCreateDatetime, toCreateDatetime, lsType, lsStatus, start, get, null, null);
	}

	public Long searchStockEquipmentsInvSerialSize(String from, String to, StockGoodsInvSerialDTO sgi, Long goodsGroupId) {
		if (from != null && from.length() >= 8)
			from = from.substring(from.length() - 8);
		if (to != null && to.length() >= 8)
			to = to.substring(to.length() - 8);
		StockGoodsInvSerialDAO sgisDAO = new StockGoodsInvSerialDAO();
		return sgisDAO.searchSize(sgi);
	}

	public Long searchSumFromShop(Long fromShopId, Date fromCreateDatetime, Date toCreateDatetime, List<Long> lsType,
			List<ApDomain> lsStatus) {
		TransactionActionDetailDAO dao = new TransactionActionDetailDAO();
		return dao.searchSumFromShop(fromShopId, fromCreateDatetime, toCreateDatetime, lsType, lsStatus);
	}

	public Long searchSumToShop(Long toShopId, Date fromCreateDatetime, Date toCreateDatetime, List<Long> lsType,
			List<ApDomain> lsStatus) {
		TransactionActionDetailDAO dao = new TransactionActionDetailDAO();
		return dao.searchSumToShop(toShopId, fromCreateDatetime, toCreateDatetime, lsType, lsStatus);
	}

	public Long searchSizeStockEquipmentsInvSerialContainDatetime(Date fromDate, Date toDate, String from, String to,
			StockGoodsInvSerialDTO sgi, Long goodsGroupId) {
		if (from != null && from.length() >= 8)
			from = from.substring(from.length() - 8);
		if (to != null && to.length() >= 8)
			to = to.substring(to.length() - 8);
		StockGoodsInvSerialDAO sgiDAO = new StockGoodsInvSerialDAO();
		return sgiDAO.searchSizeContainOutstockDatetime(fromDate, toDate, from, to, sgiDAO.convertDTOtoEntity(sgi), goodsGroupId);
	}
	public Long searchSizeStockEquipmentsInvSerialContainDatetime(Date fromDate, Date toDate, String from, String to,
			StockGoodsInvSerial sgi, Long goodsGroupId) {
		if (from != null && from.length() >= 8)
			from = from.substring(from.length() - 8);
		if (to != null && to.length() >= 8)
			to = to.substring(to.length() - 8);
		StockGoodsInvSerialDAO sgiDAO = new StockGoodsInvSerialDAO();
		return sgiDAO.searchSizeContainOutstockDatetime(fromDate, toDate, from, to, sgi, goodsGroupId);
	}

	public List<StockGoodsInvSerialDTO> searchStockEquipmentsInvSerialContainDateTime(Date fromOutstockDatetime,
			Date toOutstockDatetime, String from, String to, StockGoodsInvSerialDTO sgi, Long goodsGroupId, Integer start,
			Integer get) {
		if (from != null && from.length() >= 8)
			from = from.substring(from.length() - 8);
		if (to != null && to.length() >= 8)
			to = to.substring(to.length() - 8);
		StockGoodsInvSerialDAO sgiDAO = new StockGoodsInvSerialDAO();
		return sgiDAO.searchContainOutstockDateTime(fromOutstockDatetime, toOutstockDatetime, from, to, sgiDAO.convertDTOtoEntity(sgi),
				goodsGroupId, start, get);
	}

	public Boolean evaluate(GoodsStatusTrans gs, Set<StockGoodsInvSerial> listSerial) {
		EvaluateGoods egDAO = new EvaluateGoods(listSerial, gs);
		return egDAO.evaluate();
	}

	// Export
	public List<ApDomain> getExportReason() {
		List<ApDomain> listReasons = new ArrayList<>();
		AppDomainDAO apDAO = new AppDomainDAO();
		listReasons.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_TYPE,
				Long.parseLong(InventoryConstanst.TransactionType.EX_WARANTY)));
		listReasons.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_TYPE,
				Long.parseLong(InventoryConstanst.TransactionType.EX_REPAIR)));
		List<ApDomain> lstRemove = new ArrayList<>();
		for (ApDomain apDomain : listReasons) {
			if (apDomain == null)
				lstRemove.add(apDomain);
		};
		listReasons.removeAll(lstRemove);
		return listReasons;
	}

	public Long getTransactionActionCount(String type) {
		TransactionActionDAO taDAO = new TransactionActionDAO();
		return taDAO.getCount(type);
	}
	public void updateReasonId(List<Long> ids, Long reasonId) {
		TransactionActionDAO taDAO = new TransactionActionDAO();
		taDAO.updateReasonId(ids, reasonId);
	}

	public List<TransactionAction> findAllJoinEqDetail() {
		TransactionActionDAO taDAO = new TransactionActionDAO();
		return taDAO.findAllJoinEqDetail();
	}
	public Boolean exportSave(Long tranTypeId, String orderCode, StockTransaction st, Long reasonId, String staffName) {
		StockTransaction strs = new StockTransaction(st.getExportStaffId() == null ? staffId : st.getExportStaffId(),
				st.getFromShopId() == null ? shopId : st.getFromShopId(), null, null, tranTypeId.toString(),
						InventoryConstanst.StockTransactionStatus.SUCCESS,
						InventoryConstanst.StockTransactionDeliveryType.TRANS);
		strs.setLsDetail(st.getLsDetail());
		ExportGoods egDAO = new ExportGoods(strs,tranTypeId.toString(), orderCode, reasonId, staffName);
		return egDAO.export();
	}
	public Boolean exportSaveKTV(Long reason, String orderCode, StockTransaction st, Long reasonId, String referencode, String note) {
		StockTransaction strs = new StockTransaction(st.getExportStaffId() == null ? staffId : st.getExportStaffId(),
				st.getFromShopId() == null ? shopId : st.getFromShopId(), st.getImportStaffId(), st.getToShopId(), reason.toString(),
						InventoryConstanst.StockTransactionStatus.SUCCESS,
						InventoryConstanst.StockTransactionDeliveryType.TRANS);
		strs.setLsDetail(st.getLsDetail());
		ExEGoods egDAO = new ExEGoods(strs, orderCode, reasonId, referencode, note);
		return egDAO.exportKTV();
	}

	// Import
	public Boolean importGoods(List<StockTransactionDetail> lsG, Staff staff, String orderCode, Long reasonId, Long handleType)
			throws AppException {
		ImportGoods igDAO = new ImportGoods(lsG, staff, orderCode, reasonId, handleType);
		return igDAO.importGoods();
	}

	// Left Menu
	public Shop getAllShop(Long sId) {
		sId = sId == null ? shopId : sId;
		ShopDAO sDAO = new ShopDAO();
		return sDAO.getAllShop(sId);
	}
	public Shop getAllShopAllStatus(Long sId) {
		sId = sId == null ? shopId : sId;
		ShopDAO sDAO = new ShopDAO();
		return sDAO.getAllShopAllStatus(sId);
	}
	// search Equipment
	public Shop getAllShopStatus(Long sId) {
		sId = sId == null ? shopId : sId;
		ShopDAO sDAO = new ShopDAO();
		return sDAO.getAllShopStatus(sId);
	}

	public List<Shop> getAllParentShopById(Long sId) {
		sId = sId == null ? shopId : sId;
		ShopDAO sDAO = new ShopDAO();
		return sDAO.getAllParentShopById(sId);
	}

	// Im-subshop
	public List<ApDomain> getImReasons() {
		List<ApDomain> listReasons = new ArrayList<>();
		AppDomainDAO apDAO = new AppDomainDAO();
		listReasons.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_TYPE,
				Long.parseLong(InventoryConstanst.TransactionType.INSTANT_TD)));
		//		listReasons.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_TYPE,
		//				Long.parseLong(InventoryConstanst.TransactionType.PLAN)));
		List<ApDomain> lstRemove = new ArrayList<>();
		for (ApDomain apDomain : listReasons) {
			if (apDomain == null)
				lstRemove.add(apDomain);
		};
		listReasons.removeAll(lstRemove);
		return listReasons;
	}
	public List<ApDomain> getImReasonsNC() {
		List<ApDomain> listReasons = new ArrayList<>();
		AppDomainDAO apDAO = new AppDomainDAO();
		listReasons.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_TYPE,
				Long.parseLong(InventoryConstanst.TransactionType.INSTANT_NC)));
		//		listReasons.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_TYPE,
		//				Long.parseLong(InventoryConstanst.TransactionType.PLAN)));
		List<ApDomain> lstRemove = new ArrayList<>();
		for (ApDomain apDomain : listReasons) {
			if (apDomain == null)
				lstRemove.add(apDomain);
		};
		listReasons.removeAll(lstRemove);
		return listReasons;
	}

	public List<ApDomain> getImReasonsNoPlanTD() {
		List<ApDomain> listReasons = new ArrayList<>();
		AppDomainDAO apDAO = new AppDomainDAO();
		listReasons.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_TYPE,
				Long.parseLong(InventoryConstanst.TransactionType.INSTANT_TD)));
		List<ApDomain> lstRemove = new ArrayList<>();
		for (ApDomain apDomain : listReasons) {
			if (apDomain == null)
				lstRemove.add(apDomain);
		};
		listReasons.removeAll(lstRemove);
		return listReasons;
	}
	public List<ApDomain> getImReasonsNoPlanNC() {
		List<ApDomain> listReasons = new ArrayList<>();
		AppDomainDAO apDAO = new AppDomainDAO();
		listReasons.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_TYPE,
				Long.parseLong(InventoryConstanst.TransactionType.INSTANT_NC)));
		List<ApDomain> lstRemove = new ArrayList<>();
		for (ApDomain apDomain : listReasons) {
			if (apDomain == null)
				lstRemove.add(apDomain);
		};
		listReasons.removeAll(lstRemove);
		return listReasons;
	}

	public List<ApDomain> getImApproveStatus(boolean isAgency) {
		List<ApDomain> listStatus = new ArrayList<>();
		AppDomainDAO apDAO = new AppDomainDAO();
		if(isAgency) {
			listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
					Long.parseLong(InventoryConstanst.TransactionStatus.CREATE)));
			listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
					Long.parseLong(InventoryConstanst.TransactionStatus.APPROVE)));
			listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
					Long.parseLong(InventoryConstanst.TransactionStatus.CANCEL)));
		}else{
			listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
					Long.parseLong(InventoryConstanst.TransactionStatus.CREATE_R)));
			listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
					Long.parseLong(InventoryConstanst.TransactionStatus.APPROVE_R)));
			listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
					Long.parseLong(InventoryConstanst.TransactionStatus.CANCEL_R)));
		}
		List<ApDomain> lstRemove = new ArrayList<>();
		for (ApDomain apDomain : listStatus) {
			if (apDomain == null)
				lstRemove.add(apDomain);
		};
		listStatus.removeAll(lstRemove);
		return listStatus;

	}

	public List<ApDomain> getImExGoodsStatus(boolean isAgency) {
		List<ApDomain> listStatus = new ArrayList<>();
		AppDomainDAO apDAO = new AppDomainDAO();
		if(isAgency) {
			listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
					Long.parseLong(InventoryConstanst.TransactionStatus.APPROVE)));
			listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
					Long.parseLong(InventoryConstanst.TransactionStatus.EX)));
		}else{
			listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
					Long.parseLong(InventoryConstanst.TransactionStatus.APPROVE_R)));
			listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
					Long.parseLong(InventoryConstanst.TransactionStatus.EX_R)));
		}
		List<ApDomain> lstRemove = new ArrayList<>();
		for (ApDomain apDomain : listStatus) {
			if (apDomain == null)
				lstRemove.add(apDomain);
		};
		listStatus.removeAll(lstRemove);
		return listStatus;
	}

	public List<ApDomain> getImImGoodsStatus(boolean isAgency) {
		List<ApDomain> listStatus = new ArrayList<>();
		AppDomainDAO apDAO = new AppDomainDAO();
		if(isAgency) {
			listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
					Long.parseLong(InventoryConstanst.TransactionStatus.EX)));
			listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
					Long.parseLong(InventoryConstanst.TransactionStatus.IM)));
		}else{
			listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
					Long.parseLong(InventoryConstanst.TransactionStatus.EX_R)));
			listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
					Long.parseLong(InventoryConstanst.TransactionStatus.IM_R)));
		}
		List<ApDomain> lstRemove = new ArrayList<>();
		for (ApDomain apDomain : listStatus) {
			if (apDomain == null)
				lstRemove.add(apDomain);
		};
		listStatus.removeAll(lstRemove);
		return listStatus;
	}

	public List<ApDomain> getDomainGoodsGroup() {
		AppDomainDAO apDAO = new AppDomainDAO();
		return apDAO.findAllByType(InventoryConstanst.ApDomainType.EQUIP_TYPE);
	}


	public boolean imRequire(TransactionAction ta, Long actionType, String tockStatus) {
		ImSubShop imDAO = new ImSubShop(ta, staffId, actionType, tockStatus);
		return imDAO.imRequire();
	}

	public List<Shop> getChildShop() {
		ShopDAO sDAO = new ShopDAO();
		return sDAO.findByParentId(shopId);
	}


	public List<Shop> getChildEShop(long shopID) {
		ShopDAO sDAO = new ShopDAO();
		return sDAO.findEshopByParentId(shopID);
	}

	public List<Shop> getSameLevelShop(Long parentId) {
		ShopDAO sDAO = new ShopDAO();
		return sDAO.findByParentId(parentId);
	}

	public StockTransaction getByTransaction(Long taId, String status) {
		StockTransactionDAO stDAO = new StockTransactionDAO();
		return stDAO.getByTransaction(taId, status);
	}

	public StockTransaction getByTransactionAndType(Long taId, String type) {
		StockTransactionDAO stDAO = new StockTransactionDAO();
		return stDAO.getByTransactionAndType(taId, type);
	}
public List<TransactionActionDetail> getDetailsByTransaction(Long stockId) {
	TransactionActionDetailDAO dao = new TransactionActionDetailDAO();
	return dao.getTransactionActionDetailByTransactionActionId(stockId);
}

	public List<StockTransactionDetail> getDetailsByStock(Long stockId) {
		StockTransactionDetailDAO stdDAO = new StockTransactionDetailDAO();
		return stdDAO.getDetailsByStock(stockId);
	}

	public boolean imApproveReject(TransactionAction ta, Long actionType, String tockStatus) {
		ImSubShop imDAO = new ImSubShop(ta, new Long(ta.getAssessmentStaffId()), actionType, tockStatus);
		return imDAO.imReject();
	}

	public boolean imApproveApprove(TransactionAction ta, Long actionType, String tockStatus) {
		ImSubShop imDAO = new ImSubShop(ta, new Long(ta.getAssessmentStaffId()), actionType, tockStatus);
		return imDAO.imApproveApprove();
	}

	public boolean imExReject(TransactionAction ta, Long actionType, String tockStatus) {
		ImSubShop imDAO = new ImSubShop(ta, staffId, actionType, tockStatus);
		return imDAO.imReject();
	}

	public boolean imExApprove(TransactionAction ta, Long actionType, String stockStatus) throws AppException {
		ImSubShop imDAO = new ImSubShop(ta, staffId, actionType, stockStatus);
		return imDAO.imExApprove();
	}
	public Long getQuantityByProfileId( Long profileId) throws AppException {
		StockGoodsInvSerialDAO serialDAO = new StockGoodsInvSerialDAO();
		StockGoodsInvNoSerialDAO noSerialDAO = new StockGoodsInvNoSerialDAO();
		return serialDAO.getQuantityByProfileId(profileId) + noSerialDAO.getQuantityByProfileId(profileId);
	}

	public StockGoods getStockGoodQuantity(Long goodsId, Long status, Long shopId) throws AppException {
		StockGoodsDAO sgDAO = new StockGoodsDAO();
		return sgDAO.getStockGoods(goodsId, status, shopId);
	}

	public List<StockGoods> getStockGoodsNotStatusError(Long goodsId, Long shopId)  throws AppException {
		StockGoodsDAO sgDAO = new StockGoodsDAO();
		return sgDAO.getStockGoodsNotStatusError(goodsId, shopId);
	}

	public boolean imImReject(TransactionAction ta, Long actionType, String stockStatus) {
		ImSubShop imDAO = new ImSubShop(ta, staffId, actionType, stockStatus);
		return imDAO.imReject();
	}

	public boolean imImApprove(TransactionAction ta, Long actionType, String stockStatus) {
		ImSubShop imDAO = new ImSubShop(ta, staffId, actionType, stockStatus);
		return imDAO.imImApprove();
	}

	public List<StockTransactionSerial> searchWaran(String serial) {
		StockTransactionSerialDAO stsDAO = new StockTransactionSerialDAO();
		return stsDAO.searchWaran(serial);
	}
	public List<StockTransactionSerial> getByDetail(Long detailId) {
		StockTransactionSerialDAO stsDAO = new StockTransactionSerialDAO();
		return stsDAO.getByDetail(detailId);
	}
	public List<StockGoodsInvSerialDTO> getSerialStockGoods(Long shopId , Long equipmentId, Long providerId, String stockStatus ) {
		StockGoodsInvSerialDAO stsDAO = new StockGoodsInvSerialDAO();
		return stsDAO.getSerialStockGoods(shopId, equipmentId, providerId, stockStatus);
	}
	public List<StockGoodsInvSerialDTO> getSerialStockGoodsImportKTV(Long shopId , Long equipmentId, Long providerId, String stockStatus ) {
		StockGoodsInvSerialDAO stsDAO = new StockGoodsInvSerialDAO();
		return stsDAO.getSerialStockGoodsImportKTV(shopId, equipmentId, providerId, stockStatus);
	}
	public List<StockGoodsInvSerialDTO> getSerialStockGoodsExMaintain(StockGoodsInvSerialDTO dto ) {
		StockGoodsInvSerialDAO stsDAO = new StockGoodsInvSerialDAO();
		return stsDAO.getSerialStockGoodsExMaintain(dto);
	}
	public List<StockGoodsInvSerialDTO> getNoStockGoodsExMaintain(StockGoodsInvSerialDTO dto ) {
		StockGoodsInvSerialDAO stsDAO = new StockGoodsInvSerialDAO();
		return stsDAO.getNoSerialStockGoodsExMaintain(dto);
	}

	// Ex - subshop
	public List<ApDomain> getListExSubShopReasons() {
		List<ApDomain> listReasons = new ArrayList<>();
		AppDomainDAO apDAO = new AppDomainDAO();
		listReasons.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_TYPE,
				Long.parseLong(InventoryConstanst.TransactionType.EX_PARENT_ERR)));
		listReasons.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_TYPE,
				Long.parseLong(InventoryConstanst.TransactionType.EX_PARENT)));
		listReasons.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_TYPE,
				Long.parseLong(InventoryConstanst.TransactionType.EX_PARENT_USED)));
		List<ApDomain> lstRemove = new ArrayList<>();
		for (ApDomain apDomain : listReasons) {
			if (apDomain == null)
				lstRemove.add(apDomain);
		};
		listReasons.removeAll(lstRemove);
		return listReasons;

	}

	public boolean exRequire(TransactionAction ta, StockTransaction st, String stockStatus) throws AppException {
		ExSubShop exDAO = new ExSubShop(ta, st, staffId, stockStatus);
		return exDAO.exRequire();
	}

	public boolean exAppApprove(TransactionAction ta, StockTransaction st, String tockStatus) {
		ExSubShop exDAO = new ExSubShop(ta, st, new Long(ta.getAssessmentStaffId()), tockStatus);
		return exDAO.exAppApprove();
	}

	public boolean exReject(TransactionAction ta, StockTransaction st, String tockStatus) {
		ExSubShop exDAO = new ExSubShop(ta, st, new Long(ta.getAssessmentStaffId()), tockStatus);
		return exDAO.exReject();
	}

	public boolean exExApprove(TransactionAction ta, StockTransaction st, String tockStatus) {
		ExSubShop exDAO = new ExSubShop(ta, st, staffId, tockStatus);
		return exDAO.exExApprove();
	}

	public boolean exImApprove(TransactionAction ta, StockTransaction st, String tockStatus) {
		ExSubShop exDAO = new ExSubShop(ta, st, staffId, tockStatus);
		return exDAO.exImApprove();
	}
	// EtagSearch

	public EquipmentsDetail getByEtagSerial(Long taId, String serial) {
		EquipmentDetailDAO eDAO = new EquipmentDetailDAO();
		return eDAO.getBySerial(taId, serial);
	}

	public List<TransactionAction> getTaBySerial(Long prvId, Long gId, String serial) {
		TransactionActionDAO taDAO = new TransactionActionDAO();
		return taDAO.getBySerial(prvId, gId, serial);
	}

	public List<GoodsStatusTransSerial> getTransSerial(Long prvId, Long gId, String serial) {
		GoodsStatusTransSerialDAO gsDAO = new GoodsStatusTransSerialDAO();
		return gsDAO.getBySerial(prvId, gId, serial);
	}
	public void saveOrUpdatGSTS(GoodsStatusTransSerial obj) {
		GoodsStatusTransSerialDAO gsDAO = new GoodsStatusTransSerialDAO();
		gsDAO.saveOrUpdate(obj);
	}
	public GoodsStatusTrans getGoodsStatusTrans(Long id) {
		GoodsStatusTransDAO gDAO = new GoodsStatusTransDAO();
		return gDAO.findById(id);
	}
	public void saveOrUpdateGoodsStatusTrans(GoodsStatusTrans obj) {
		GoodsStatusTransDAO gDAO = new GoodsStatusTransDAO();
		gDAO.saveOrUpdate(obj);
	}

	public List<ApDomain> getListTransStatus() {
		List<ApDomain> listStatus = new ArrayList<>();
		AppDomainDAO apDAO = new AppDomainDAO();

		listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
				Long.parseLong(InventoryConstanst.TransactionStatus.CREATE)));
		listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
				Long.parseLong(InventoryConstanst.TransactionStatus.APPROVE)));
		listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
				Long.parseLong(InventoryConstanst.TransactionStatus.EX)));
		listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
				Long.parseLong(InventoryConstanst.TransactionStatus.IM)));
		listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
				Long.parseLong(InventoryConstanst.TransactionStatus.CANCEL)));

		listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
				Long.parseLong(InventoryConstanst.TransactionStatus.CREATE_R)));
		listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
				Long.parseLong(InventoryConstanst.TransactionStatus.APPROVE_R)));
		listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
				Long.parseLong(InventoryConstanst.TransactionStatus.EX_R)));
		listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
				Long.parseLong(InventoryConstanst.TransactionStatus.IM_R)));
		listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
				Long.parseLong(InventoryConstanst.TransactionStatus.CANCEL_R)));

		listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
				Long.parseLong(InventoryConstanst.TransactionStatus.IM_STAFF)));
		listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
				Long.parseLong(InventoryConstanst.TransactionStatus.EX_STAFF)));

		listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
				Long.parseLong(InventoryConstanst.TransactionStatus.CREATE_WARRANTY)));
		listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
				Long.parseLong(InventoryConstanst.TransactionStatus.EX_WARRANTY)));
		listStatus.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_STATUS,
				Long.parseLong(InventoryConstanst.TransactionStatus.IM_WARRANTY)));

		List<ApDomain> lstRemove =  new ArrayList<>();

		for (ApDomain apDomain : listStatus) {
			if (apDomain == null)
				lstRemove.add(apDomain);
		};
		listStatus.removeAll(lstRemove);
		return listStatus;
	}

	public List<ApDomain> getListTransType() {

		List<ApDomain> listReasons = new ArrayList<>();
		AppDomainDAO apDAO = new AppDomainDAO();

		ApDomain domain = apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_TYPE,
				Long.parseLong(InventoryConstanst.TransactionType.IM));
		if (domain != null)
			listReasons.add(domain);
		
		domain = apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_TYPE,
				Long.parseLong(InventoryConstanst.TransactionType.IM_STAFF));
		if (domain != null)
			listReasons.add(domain);
		
		domain = apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_TYPE,
				Long.parseLong(InventoryConstanst.TransactionType.IM_WARRANTY));
		if (domain != null)
			listReasons.add(domain);

		domain = apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_TYPE,
				Long.parseLong(InventoryConstanst.TransactionType.INSTANT_TD));
		if (domain != null)
			listReasons.add(domain);

		domain = apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_TYPE,
				Long.parseLong(InventoryConstanst.TransactionType.INSTANT_NC));
		if (domain != null)
			listReasons.add(domain);

		domain = apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_TYPE,
				Long.parseLong(InventoryConstanst.TransactionType.EX_PARENT_ERR));
		if (domain != null)
			listReasons.add(domain);

		domain = apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_TYPE,
				Long.parseLong(InventoryConstanst.TransactionType.EX_PARENT_USED));
		if(domain != null)
			listReasons.add(domain);

		domain = apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_TYPE,
				Long.parseLong(InventoryConstanst.TransactionType.EX_PARENT));
		if(domain !=null)
			listReasons.add(domain);

		domain = apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_TYPE,
				Long.parseLong(InventoryConstanst.TransactionType.EX_STAFF));
		if (domain != null)
			listReasons.add(domain);

		domain = apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_TYPE,
				Long.parseLong(InventoryConstanst.TransactionType.EX_WARANTY));
		if (domain != null)
			listReasons.add(domain);
		
		domain = apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_TYPE,
				Long.parseLong(InventoryConstanst.TransactionType.EX_REPAIR));
		if (domain != null)
			listReasons.add(domain);
		
		//		listReasons.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_TYPE,
		//				Long.parseLong(InventoryConstanst.TransactionType.PLAN)));
		//	listReasons.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_TYPE,
		//			Long.parseLong(InventoryConstanst.TransactionType.WARRANTY)));
		//	listReasons.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_TYPE,
		//			Long.parseLong(InventoryConstanst.TransactionType.BACK)));
		//	listReasons.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.TRANS_TYPE,
		//			Long.parseLong(InventoryConstanst.TransactionType.KTV_CREATE)));
		return listReasons;
	}

	// Search Trans
	public List<StockTransactionSerial> searchTransSearch(String from, String to, Long providerId, Long gid,
			Long gstatus, Long taId, Long stockId,Long detailId, Integer start, Integer get) {
		StockTransactionSerialDAO stsDAO = new StockTransactionSerialDAO();
		return stsDAO.searchTransSearch(from, to, providerId, gid, gstatus, taId, stockId, null, start, get, null, null);
	}
	public List<StockTransactionSerial> searchTransSearchXK(String from, String to, Long providerId, Long gid,
														  Long gstatus, Long taId, Long stockId,Long detailId, Integer start, Integer get) {
		StockTransactionSerialDAO stsDAO = new StockTransactionSerialDAO();
		return stsDAO.searchTransSearchXK(from, to, providerId, gid, gstatus, taId, stockId, null, start, get, null, null);
	}

	public Long searchTransSearchSize(String from, String to, Long providerId, Long gid, Long gstatus, Long taId,
			Long stockId) {
		StockTransactionSerialDAO stsDAO = new StockTransactionSerialDAO();
		return stsDAO.searchTransSearchSize(from, to, providerId, gid, gstatus, taId, stockId);
	}

	public StockTransaction getLastByTransaction(Long taId) {
		StockTransactionDAO stDAO = new StockTransactionDAO();
		return stDAO.getLastByTransaction(taId);
	}

	// Login
	public Staff getByUserName(String usname) {
		if (usname == null || usname.trim().isEmpty()) {
			return null;
		} else {
			StaffDAO stDAO = new StaffDAO();
			return stDAO.getByUserName(usname);
		}
	}

	// Get TransActio
	public TransactionAction getTransActionById(Long id) {
		TransactionActionDAO taDAO = new TransactionActionDAO();
		return taDAO.findById(id);
	}

	public Boolean checkSerial(String serial, Long providerId) {
		StockGoodsInvSerialDAO sgiDAO = new StockGoodsInvSerialDAO();
		return sgiDAO.checkSerial(serial, providerId);
	}

	// List Reason
	public List<ApDomain> getListReason(String type) {
		List<ApDomain> rs = new ArrayList<>();
		if(type == null || type.isEmpty()) return null;
		AppDomainDAO apDaAO = new AppDomainDAO();
		if (type == null) {
			rs.addAll(apDaAO.findAllByType(InventoryConstanst.ApDomainType.TRANS_REASON));
		} else {
			if (apDaAO.findByTypeAndCode(InventoryConstanst.ApDomainType.TRANS_REASON, type.toString()) == null) {
				return null;
			}
			rs.addAll(apDaAO.findByTypeAndCode(InventoryConstanst.ApDomainType.TRANS_REASON, type.toString()));
		}
		return rs;
	}

	public ApDomain getReasonByValue(Long id) {
		List<ApDomain> ls = getListReason(null);
		for (ApDomain d : ls) {
			if (d.getValue().equals(id)) {
				return d;
			}
		}
		return new ApDomain();
	}

	public List<ApDomain> getListEvaluaType() {
		AppDomainDAO apDAO = new AppDomainDAO();
		return apDAO.findAllByType(InventoryConstanst.ApDomainType.GOODS_TRANS_TYPE);
	}

	public List<ApDomain> getListEvaluaReason(Long type) {
		List<ApDomain> rs = new ArrayList<>();
		AppDomainDAO apDaAO = new AppDomainDAO();
//		if(rs==null || rs.isEmpty()){
//			return rs;
//		}
		if (type == null) {
			rs.addAll(apDaAO.findAllByType(InventoryConstanst.ApDomainType.GOODS_TRANS_REASON));
		} else {
			rs.addAll(apDaAO.findByTypeAndCode(InventoryConstanst.ApDomainType.GOODS_TRANS_REASON, type.toString()));
		}
		return rs;
	}

	public List<ApDomain> getListTransStockStatus() {
		List<ApDomain> rs = new ArrayList<>();
		AppDomainDAO apDAO = new AppDomainDAO();
		rs.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.STOCK_STATUS,
				Long.parseLong(InventoryConstanst.StockStatus.INSTOCK_ERR)));
		rs.add(apDAO.findByTypeAndValue(InventoryConstanst.ApDomainType.STOCK_STATUS,
				Long.parseLong(InventoryConstanst.StockStatus.INSTOCK)));
		return rs;
	}

	public Staff getStaffById(Long id) {
		StaffDAO sDAO = new StaffDAO();
		return id == null ? new Staff() : sDAO.findById(id);
	}

	// TransactionNotification
	public List<TransactionNotification> getListNotification(List<String> lsUrl) {
		TransactionNotificationDAO tnDAO = new TransactionNotificationDAO();
		return tnDAO.getListByListUrl(lsUrl);
	}

	public List<Shop> getListShop(Long parentShopId,Long shopId){
		ShopDAO shopDao = new ShopDAO();
		return shopDao.getListShop(parentShopId,shopId);
	}

	public List<Shop> getTreeShop(Long parentId){
		ShopDAO shopDAO = new ShopDAO();
		return shopDAO.getTreeShop(parentId);
	}
	public List<Shop> getTreeShopAll(Long parentId){
		ShopDAO shopDAO = new ShopDAO();
		return shopDAO.getTreeShopAll(parentId);
	}

	public String getProviderName(Long providerId){
		ProviderDAO dao = new ProviderDAO();
		Provider p = dao.findById(providerId);

		if(p!=null) return p.getProviderName();

		return null;
	}

	public List<TransactionAction> getAllTransactionAction() {
		TransactionActionDAO taDAO = new TransactionActionDAO();
		return taDAO.findAll();
	}
	public List<TransactionAction> searchTranByEDtail(TransactionAction ta) {
		TransactionActionDAO taDAO = new TransactionActionDAO();
		return taDAO.searchTranByEDtail(ta);
	}
	public List<TransactionAction> searchTranByEDtail(String code, String tranType) {
		TransactionActionDAO taDAO = new TransactionActionDAO();
		return taDAO.searchTranByEDtail(code, tranType);
	}
	public List<TransactionAction> searchTranByEDtailImportWaran(String code, String tranType1, String tranType2) {
		TransactionActionDAO taDAO = new TransactionActionDAO();
		return taDAO.searchTranByEDtailImportWaran(code, tranType1, tranType2);
	}
	public List<Staff> getAllStaffByTransactionAction() {
		StaffDAO sDAO = new StaffDAO();
		return sDAO.getByTransactionAction();
	}

	public List<Provider> getProviderByStockSerial() {
		ProviderDAO pvDAO = new ProviderDAO();
		return pvDAO.getProviderByStockSerial();
	}
	public StockTransactionSerial getStockSerials(String serial, Long goodId) {
		StockTransactionSerialDAO pvDAO = new StockTransactionSerialDAO();
		return pvDAO.getStockSerials(serial, goodId);
	}
	public StockTransactionDetail findStockTransactionDetailById(Long staDetailId) {
		StockTransactionDetailDAO pvDAO = new StockTransactionDetailDAO();
		return pvDAO.findStockTransactionDetailById(staDetailId);
	}
	public StockTransaction findStockTransactionById(Long id) {
		StockTransactionDAO pvDAO = new StockTransactionDAO();
		return pvDAO.findById(id);
	}
	public void deleteTransactionActionDetail(Long taId) {
		TransactionActionDetailDAO pvDAO = new TransactionActionDetailDAO();
		pvDAO.deleteByTaId(taId);
	}


	public void insertHistory(EquipmentsDetail equipmentsDetail, String[] lstColumn,
							  String[] valuesNew, String[] valuesOld, Long actionType){
		try {
			if(lstColumn.length != valuesNew.length || lstColumn.length != valuesOld.length)
			{
				throw new Exception("Error:EquipmentsDetail size not map");
			}

			EquipmentHistoryDetailDAO equipmentHistoryDetailDAO = new EquipmentHistoryDetailDAO();
			EquipmentHistoryDAO equipmentHistoryDAO = new EquipmentHistoryDAO();
			ActionAuditDAO actionAuditDAO = new ActionAuditDAO();

			ActionAudit actionAudit =  new ActionAudit();
			EquipmentHistory equipmentHistory = new EquipmentHistory();
			EquipmentHistoryDetail equipmentHistoryDetail = new EquipmentHistoryDetail();

			equipmentHistory.setEquipmentId(equipmentsDetail.getId());
			equipmentHistory.setCreatedDatetime(DateTimeUtils.getDate());
			equipmentHistory.setStaffId(equipmentsDetail.getStaffId());
			equipmentHistory.setShopId(equipmentsDetail.getShopId());
			equipmentHistoryDAO.saveOrUpdate(equipmentHistory);


			actionAudit.setActionDatetime(DateTimeUtils.getDate());
			actionAudit.setActionType(actionType);
			actionAudit.setReferenceId(equipmentHistory.getEquipmentHistoryId());
			actionAuditDAO.saveOrUpdate(actionAudit);

			List<EquipmentHistoryDetail> lstHistoryDetail = new ArrayList<>();

			for (int i=0; i<lstColumn.length;i++){
				EquipmentHistoryDetail obj = new EquipmentHistoryDetail();
				obj.setEquipmentHistoryId(equipmentHistory.getEquipmentHistoryId());
				obj.setFeildName(lstColumn[i]);//gia tri Long. Phai quy dinh name column khi gọi
				obj.setNewValue(valuesNew[i]);
				obj.setOldValue(valuesOld[i]);
				lstHistoryDetail.add(obj);
			}
			equipmentHistoryDetailDAO.saveOrUpdate(lstHistoryDetail);

		}catch (Exception ex){
			ex.printStackTrace();
		}
	}

}
