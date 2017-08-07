/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.ws;

import com.ftu.constanst.InventoryConstanst;
import com.ftu.exception.AppException;
import com.ftu.hibernate.HibernateUtil;
import com.ftu.inventory.bo.*;
import com.ftu.inventory.dto.StockGoodsInvSerialDTO;
import com.ftu.staff.bo.Shop;
import com.ftu.staff.bo.Staff;
import com.ftu.inventory.bo.StockGoodsInvSerial;
import com.ftu.java.bo.SerialA;
import com.ftu.staff.bo.ApDomain;
import com.ftu.staff.dao.ShopDAO;
import com.ftu.staff.dao.StaffDAO;
import com.ftu.utils.AnalysisSerial;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.jws.WebService;
import javax.jws.WebMethod;

/**
 *
 * @author E5420
 */
@WebService(serviceName = "EquipmentManagementService")
public class EquipmentManagementService {

	/**
	 * This is a sample web service operation
	 */
	BusinessService service;
	Shop shop;
	Staff staff;

	public EquipmentManagementService() {
		// Kết quả
		StaffDAO stDAO = new StaffDAO();
		staff = stDAO.getByUserName("admin");
		if (staff != null && staff.getShopId() != null) {
			ShopDAO shDAO = new ShopDAO();
			shop = shDAO.findById(staff.getShopId());
			if (shop != null) {
				service = new BusinessService(shop.getShopId(), staff.getStaffId());
			}
		}
	}

	public void setStaff(String userName) {
		// Kết quả
		StaffDAO stDAO = new StaffDAO();
		staff = stDAO.getByUserName(userName);
		if (staff != null && staff.getShopId() != null) {
			ShopDAO shDAO = new ShopDAO();
			shop = shDAO.findById(staff.getShopId());
			if (shop != null) {
				service = new BusinessService(shop.getShopId(), staff.getStaffId());
			}
		}
	}

	private boolean check() {
		return service != null;
	}

	// Menu search Shop
	@WebMethod(operationName = "getShopById")
	public Shop getShopById() throws Exception {
		if (!check()) {
			return null;
		}
		Shop rs = service.getShopById();
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	@WebMethod(operationName = "getShopChildById")
	public Shop getShopChildById(Long id) throws Exception {
		if (!check()) {
			return null;
		}
		Shop rs = service.getShopChildById(id);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	@WebMethod(operationName = "getSizeStock")
	public Long getSizeStock(Long goodsGroupId, Long goodsId, Long status, Long shopId, Long stockSts)
			throws Exception {
		if (!check()) {
			return null;
		}
		Long rs = service.getSizeStock(goodsGroupId, goodsId, status, shopId, stockSts);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}
	
	@WebMethod(operationName = "getSizeStockWithLsGoodsId")
	public Long getSizeStockWithLsGoodsId(List<Long> listGoodsId, Long status, Long shopId, Long stockSts) throws Exception{
		if (!check()) {
			return null;
		}
		
		Long rs = service.getSizeStockWithLsGoodsId(listGoodsId, status, shopId, stockSts);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	//để làm báo cáo tổng hợp hàng tồn kho
	@WebMethod(operationName = "getStockGoodsForInstockGeneralExp")
	public List<StockGoods> getStockGoodsForInstockGeneralExp(Long sId, Long goodsId, Long goodsGroupId,
			Long stockGoodsStatus) throws Exception {
		if (!check()) {
			return null;
		}
		List<StockGoods> lsStockGoods = service.searchStockGoods(sId, goodsId, goodsGroupId, stockGoodsStatus);
		if (lsStockGoods != null) {
			for (StockGoods sg : lsStockGoods) {
					EquipmentsProfile g = service.getGoodsById(sg.getGoodsId());
					sg.setProfileName(g.getProfileName());
					sg.setProfileCode(g.getProfileCode());
					sg.setEquipmentsGroup(service.getEquipmentsGroup(g.getEquipmentsGroupId()).getEquipmentsGroupName());
					sg.setGoodsStatusName(service.getEquipsStatusName(sg.getGoodsStatus()));
			}
		}
		HibernateUtil.commitCurrentSessions();
		return lsStockGoods;
	}

	@WebMethod(operationName = "getStockSize")
	public Long getStockSize(Long goodsId, Long status, Long shopId, Long stockSts) throws Exception {
		if (!check()) {
			return null;
		}
		Long rs = service.getStockSize(goodsId, status, shopId, stockSts,true);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	@WebMethod(operationName = "getStockSizeByTaId")
	public Long getStockSizeByTaId(Long taId, String stockStatus, Long gId, Long gStatus) throws Exception {
		if (!check()) {
			return null;
		}
		Long rs = service.getStockSizeByTaId(taId, stockStatus, gId, gStatus);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	@WebMethod(operationName = "getStockGoodsByShop")
	public List<StockGoods> getStockGoodsByShop(Long shopId) throws Exception {
		if (!check()) {
			return null;
		}
		List<StockGoods> rs = service.getStockGoodsByShop(shopId);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	// SessionBean Hàng mới/Hàng hỏng
	@WebMethod(operationName = "getListGoodsStatus")
	public List<ApDomain> getListGoodsStatus() throws Exception {
		if (!check()) {
			return null;
		}
		List<ApDomain> rs = service.getListGoodsStatus();
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	@WebMethod(operationName = "getListSearchStocktatus")
	public List<ApDomain> getListSearchStocktatus() throws Exception {
		if (!check()) {
			return null;
		}
		List<ApDomain> rs = service.getListSearchStocktatus();
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	@WebMethod(operationName = "findAllShop")
	public List<Shop> findAllShop() throws Exception {
		if (!check()) {
			return null;
		}
		List<Shop> rs = service.findAllShop();
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	// Lấy tên theo id (Hàng mới/Hàng hỏng)
	@WebMethod(operationName = "getEquipsStatusName")
	public String getGoodsStatusName(Long id) throws Exception {
		if (!check()) {
			return null;
		}
		String rs = service.getEquipsStatusName(id);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	// Lấy nhóm hàng
	@WebMethod(operationName = "getAllEquipmentsGroup")
	public List<EquipmentsGroup> getAllEquipmentsGroup() throws Exception {
		if (!check()) {
			return null;
		}
		List<EquipmentsGroup> rs = service.getAllEquipmentsGroup();
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	// Lấy tất cả mặt hàng
	@WebMethod(operationName = "getAllGoods")
	public List<EquipmentsProfile> getAllGoods() throws Exception {
		if (!check()) {
			return null;
		}
		List<EquipmentsProfile> rs = service.getAllGoods();
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	// Lấy tất cả nhà cung cấp
	@WebMethod(operationName = "getAllProvider")
	public List<Provider> getAllProvider() throws Exception {
		if (!check()) {
			return null;
		}
		List<Provider> rs = service.getAllProvider();
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	@WebMethod(operationName = "getActiveProvider")
	public List<Provider> getActiveProvider() throws Exception {
		if (!check()) {
			return null;
		}
		List<Provider> rs = service.getActiveProvider();
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	// ApproveGoods
	// Lấy danh sách nhân viên theo shop
	@WebMethod(operationName = "getListStaffByShop")
	public List<Staff> getListStaffByShop(Long sid) throws Exception {
		if (!check()) {
			return null;
		}
		List<Staff> rs = service.getListStaffByShop(sid);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	// Lấy danh sách lý do khi phê duyệt (TransactionActionType)
	@WebMethod(operationName = "getApproveType")
	public List<ApDomain> getApproveType() throws Exception {
		if (!check()) {
			return null;
		}
		List<ApDomain> rs = service.getApproveType(null);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	// Lấy danh sách trạng thái khi phê duyệt
	@WebMethod(operationName = "getApproveStatus")
	public List<ApDomain> getApproveStatus() throws Exception {
		if (!check()) {
			return null;
		}
		List<ApDomain> rs = service.getApproveStatus();
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	// Search size theo imTransactionId(transaction nhập hàng)
	@WebMethod(operationName = "getStockTransactionSerialSize")
	public Long getStockTransactionSerialSize(Long providerId, Long goodsId, Long trasActionId, Long goodsStatus)
			throws Exception {
		if (!check()) {
			return null;
		}
		Long rs = service.getStockTransactionSerialSize(providerId, goodsId, trasActionId, goodsStatus);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	// Lấy list TransactionActionDetail
	@WebMethod(operationName = "getTranActionDetailsByTransId")
	public List<TransactionActionDetail> getTranActionDetailsByTransId(Long transActionId) throws Exception {
		if (!check()) {
			return null;
		}
		List<TransactionActionDetail> rs = service.getTranActionDetailsByTransId(transActionId);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	// Tìm kiếm transactionAction
	@WebMethod(operationName = "searchTransactionAction")
	public List<TransactionAction> searchTransactionAction(TransactionAction ta, List<ApDomain> lsType,
			List<ApDomain> lsStatus, Integer start, Integer get) throws Exception {
		if (!check()) {
			return null;
		}
		List<TransactionAction> rs = service.searchTransactionAction(ta, lsType, lsStatus, start, get);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}
	
	// Tìm kiếm transactionAction và sắp xếp theo ngày tạo
		@WebMethod(operationName = "searchAndSortTransactionAction")
		public List<TransactionAction> searchAndSortTransactionAction(TransactionAction ta, List<ApDomain> lsType,
				List<ApDomain> lsStatus, Integer start, Integer get, boolean desc, String sortField) throws Exception {
			if (!check()) {
				return null;
			}
			List<TransactionAction> rs = service.searchAndSortTransactionAction(ta, lsType, lsStatus, start, get, desc, sortField);
			HibernateUtil.commitCurrentSessions();
			return rs;
		}

	@WebMethod(operationName = "searchTransactionActionSize")
	public Long searchTransactionActionSize(TransactionAction ta, List<ApDomain> lsType, List<ApDomain> lsStatus)
			throws Exception {
		if (!check()) {
			return null;
		}
		Long rs = service.searchTransactionActionSize(ta, lsType, lsStatus);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	@WebMethod(operationName = "searchTransactionNotificationSize")
	public Long searchTransactionNotificationSize(Long shopId, Boolean isImport, String[] lsType, String stautus)
			throws Exception {
		if (!check()) {
			return null;
		}
		Long rs = service.searchTransactionActionSize(shopId, isImport, lsType, stautus);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	// Duyệt hàng
	@WebMethod(operationName = "approveApprove")
	public Boolean approveApprove(TransactionAction ta) throws AppException {
		if (!check()) {
			return null;
		}
		try {
			boolean k = service.approveApprove(ta);
//			HibernateUtil.getSession().getTransaction().commit();
			HibernateUtil.commitCurrentSessions();
			return k;
		} catch (Exception ex) {

			AppException ap = new AppException("ERROR-U001", new Locale(InventoryConstanst.local));
			throw ap;
		}
	}

	// Hủy đơn hàng (ko duyệt)
	@WebMethod(operationName = "approveReject")
	public Boolean approveReject(TransactionAction ta) throws AppException {
		if (!check()) {
			return null;
		}
		try {
			boolean k = service.approveReject(ta);
//			HibernateUtil.getSession().getTransaction().commit();
			HibernateUtil.commitCurrentSessions();
			return k;
		} catch (Exception ex) {

			AppException ap = new AppException("ERROR-U001", new Locale(InventoryConstanst.local));
			throw ap;
		}
	}

	@WebMethod(operationName = "checkApproveGoods")
	public Long checkApproveGoods(Long taId) throws Exception {
		if (!check()) {
			return null;
		}
		Long rs = service.checkApproveGoods(taId);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	// Evaluate Đánh giá mặt hàng
	// Search Serial cần đánh giá
	@WebMethod(operationName = "searchStockEquipmentsInvSerial")
	public List<StockGoodsInvSerialDTO> searchStockEquipmentsInvSerial(String from, String to, StockGoodsInvSerialDTO sgi,
																	Long goodsGroupId, Integer start, Integer get) throws Exception {
		if (!check()) {
			return null;
		}
		List<StockGoodsInvSerialDTO> rs = service.searchStockEquipmentsInvSerial(from, to, sgi, goodsGroupId, start, get);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	@WebMethod(operationName = "searchStockEquipmentsInvSerialContainDatetime")
	public List<StockGoodsInvSerialDTO> searchStockEquipmentsInvSerialContainDatetime(Date fromDate, Date toDate, String from, String to, StockGoodsInvSerial sgi,
																				   Long goodsGroupId, Integer start, Integer get) throws Exception {
		if (!check()) {
			return null;
		}
		List<StockGoodsInvSerialDTO> rs = service.searchStockEquipmentsInvSerialContainDatetime(fromDate, toDate, from, to, sgi, goodsGroupId, start, get);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	@WebMethod(operationName = "searchStockEquipmentsInvSerialSize")
	public Long searchStockEquipmentsInvSerialSize(String from, String to, StockGoodsInvSerialDTO sgi, Long goodsGroupId)
			throws Exception {
		if (!check()) {
			return null;
		}
		Long rs = service.searchStockEquipmentsInvSerialSize(from, to, sgi, goodsGroupId);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	@WebMethod(operationName = "searchStockEquipmentsInvSerialSizeContainDatetime")
	public Long searchStockEquipmentsInvSerialSizeContainDatetime(Date fromDate, Date toDate, String from, String to, StockGoodsInvSerial sgi, Long goodsGroupId)
			throws Exception {
		if (!check()) {
			return null;
		}
		Long rs = service.searchSizeStockEquipmentsInvSerialContainDatetime(fromDate, toDate, from, to, sgi, goodsGroupId);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	@WebMethod(operationName = "searchStockEquipmentsInvSerialAnalysis")
	public List<SerialA> searchStockEquipmentsInvSerialAnalysis(String from, String to, StockGoodsInvSerialDTO sgi,
			Long goodsGroupId) throws Exception {
		if (!check()) {
			return null;
		}
		List<StockGoodsInvSerialDTO> listS = service.searchStockEquipmentsInvSerial(from, to, sgi, goodsGroupId, null, null);
		AnalysisSerial ann = new AnalysisSerial(null, listS);
		List<SerialA> rs = ann.analysisInvSerial();
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	// Đánh giá
	@WebMethod(operationName = "evaluate")
	public Boolean evaluate(GoodsStatusTrans gs, Set<StockGoodsInvSerial> listSerial) throws AppException {
		if (!check()) {
			return null;
		}
		try {
			boolean k = service.evaluate(gs, listSerial);
//			HibernateUtil.getSession().getTransaction().commit();
			HibernateUtil.commitCurrentSessions();
			return k;
		} catch (Exception ex) {

			AppException ap = new AppException("ERROR-U001", new Locale(InventoryConstanst.local));
			throw ap;
		}
	}

	// Export Xuất hàng khỏi kho
	// Danh sách lý do (TransactionActionType) Id-name
	@WebMethod(operationName = "getExportReason")
	public List<ApDomain> getExportReason() throws Exception {
		if (!check()) {
			return null;
		}
		List<ApDomain> rs = service.getExportReason();
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	// Lấy count TransactionAction để tạo TransactionActionCode
	@WebMethod(operationName = "getTransactionActionCount")
	public Long getTransactionActionCount(String type) throws Exception {
		if (!check()) {
			return null;
		}
		Long rs = service.getTransactionActionCount(type);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	// Xuất hàng (Trong st có list detail, trong detail có list serial
	@WebMethod(operationName = "exportSave")
	public Boolean exportSave(Long reason, String orderCode, StockTransaction st, Long reasonId) throws AppException {
		if (!check()) {
			return null;
		}
		try {
			boolean k = service.exportSave(reason, orderCode, st, reasonId, null);
//			HibernateUtil.getSession().getTransaction().commit();
			HibernateUtil.commitCurrentSessions();
			return k;
		} catch (Exception ex) {

			AppException ap = new AppException("ERROR-U001", new Locale(InventoryConstanst.local));
			throw ap;
		}
	}

	// Import nhập hàng từ nhà cung cấp
	// Nhập hàng Trong từng detail có list Serial
	@WebMethod(operationName = "importGoods")
	public Boolean importGoods(List<StockTransactionDetail> lsG, Staff staff, String orderCode, Long reasonId)
			throws AppException {
		if (!check()) {
			return null;
		}
		try {
			boolean k = service.importGoods(lsG, staff, orderCode, reasonId, InventoryConstanst.ImportType.importGood);
//			HibernateUtil.getSession().getTransaction().commit();
			HibernateUtil.commitCurrentSessions();
			return k;
		} catch (Exception ex) {

			AppException ap = new AppException("ERROR-U001", new Locale(InventoryConstanst.local));
			throw ap;
		}
	}

	// Left Menu
	// Lấy đệ quy các List shop con của shop cần tìm ...
	@WebMethod(operationName = "getAllShop")
	public Shop getAllShop(Long sId) throws Exception {
		if (!check()) {
			return null;
		}
		Shop rs = service.getAllShop(sId);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}
	
	@WebMethod(operationName = "getAllParentShopById")
	public List<Shop> getAllParentShopById(Long sId) throws Exception {
		if (!check()) {
			return null;
		}
		List<Shop> rs = service.getAllParentShopById(sId);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	// Im-subshop-require Kho con yêu cầu nhập hàng
	// Danh sách lý do (TransactionActionType)
	@WebMethod(operationName = "getImReasons")
	public List<ApDomain> getImReasons() throws Exception {
		if (!check()) {
			return null;
		}
		List<ApDomain> rs = service.getImReasons();
		HibernateUtil.commitCurrentSessions();
		return rs;
	}
//todo: service đã tach yêu cầu ngang cấp và trên dưới. cần xem lại hàm này
	@WebMethod(operationName = "getImReasonsNoPlan")
	public List<ApDomain> getImReasonsNoPlan() throws Exception {
		if (!check()) {
			return null;
		}
		List<ApDomain> rs = service.getImReasonsNoPlanTD();
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	// Danh sách trạng thái (kho cha duyệt yêu cầu)-TransactionAction Status
	@WebMethod(operationName = "getImApproveStatus")
	public List<ApDomain> getImApproveStatus(boolean isAgency) throws Exception {
		if (!check()) {
			return null;
		}
		List<ApDomain> rs = service.getImApproveStatus(isAgency);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	// Danh sách trạng thái (kho cha xuất hàng theo yêu cầu)-TransactionAction
	// Status
	@WebMethod(operationName = "getImExGoodsStatus")
	public List<ApDomain> getImExGoodsStatus(boolean isAgency) throws Exception {
		if (!check()) {
			return null;
		}
		List<ApDomain> rs = service.getImExGoodsStatus(isAgency);
		HibernateUtil.commitCurrentSessions();
		return rs;

	}

	// Danh sách trạng thái (kho con nhập hàng)
	@WebMethod(operationName = "getImImGoodsStatus")
	public List<ApDomain> getImImGoodsStatus(boolean isAgency) throws Exception {
		if (!check()) {
			return null;
		}
		List<ApDomain> rs = service.getImImGoodsStatus(isAgency);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	// Tạo transaction yêu cầu nhập hàng
	@WebMethod(operationName = "imRequire")
	public Boolean imRequire(TransactionAction ta) throws AppException {
		if (!check()) {
			return null;
		}
		try {
			boolean k = service.imRequire(ta, null, InventoryConstanst.StockStatus.BLOCK_TD);
//			HibernateUtil.getSession().getTransaction().commit();
			HibernateUtil.commitCurrentSessions();
			return k;
		} catch (Exception ex) {

			AppException ap = new AppException("ERROR-U001", new Locale(InventoryConstanst.local));
			throw ap;
		}
	}

	// Lấy danh sách kho con
	@WebMethod(operationName = "getChildShop")
	public List<Shop> getChildShop() throws Exception {
		if (!check()) {
			return null;
		}
		List<Shop> rs = service.getChildShop();
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	// Tìm theo TransactionAction và status
	@WebMethod(operationName = "getByTransaction")
	public StockTransaction getByTransaction(Long taId, String status) throws Exception {
		if (!check()) {
			return null;
		}
		StockTransaction rs = service.getByTransaction(taId, status);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	// Tìm theo TransactionAction và type
	@WebMethod(operationName = "getByTransactionAndType")
	public StockTransaction getByTransactionAndType(Long taId, String type) throws Exception {
		if (!check()) {
			return null;
		}
		StockTransaction rs = service.getByTransactionAndType(taId, type);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	// Lấy danh sách theo StockTransactionId
	@WebMethod(operationName = "getDetailsByStock")
	public List<StockTransactionDetail> getDetailsByStock(Long stockId) throws Exception {
		if (!check()) {
			return null;
		}
		List<StockTransactionDetail> rs = service.getDetailsByStock(stockId);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	// Kho cha từ chối TransactionAction (ko duyệt)
	@WebMethod(operationName = "imApproveReject")
	public Boolean imApproveReject(TransactionAction ta) throws AppException {
		if (!check()) {
			return null;
		}
		try {
			boolean k = service.imApproveReject(ta, null, InventoryConstanst.StockStatus.BLOCK_TD);
			HibernateUtil.commitCurrentSessions();
			return k;
		} catch (Exception ex) {

			AppException ap = new AppException("ERROR-U001", new Locale(InventoryConstanst.local));
			throw ap;
		}
	}

	// Kho cha duyệt yêu cầu
	@WebMethod(operationName = "imApproveApprove")
	public Boolean imApproveApprove(TransactionAction ta) throws AppException {
		if (!check()) {
			return null;
		}
		try {
			boolean k = service.imApproveApprove(ta, null, InventoryConstanst.StockStatus.BLOCK_TD);
			HibernateUtil.commitCurrentSessions();
//			HibernateUtil.getSession().getTransaction().commit();
			return k;
		} catch (Exception ex) {

			AppException ap = new AppException("ERROR-U001", new Locale(InventoryConstanst.local));
			throw ap;
		}
	}

	// Kho cha từ chối xuất hàng, hủy TransactionAction
	@WebMethod(operationName = "imExReject")
	public Boolean imExReject(TransactionAction ta) throws AppException {
		if (!check()) {
			return null;
		}
		try {
			boolean k = service.imExReject(ta, null, InventoryConstanst.StockStatus.BLOCK_TD);
			HibernateUtil.commitCurrentSessions();
//			HibernateUtil.getSession().getTransaction().commit();
			return k;
		} catch (Exception ex) {

			AppException ap = new AppException("ERROR-U001", new Locale(InventoryConstanst.local));
			throw ap;
		}
	}

	// Kho cha đồng ý xuất hàng
	@WebMethod(operationName = "imExApprove")
	public Boolean imExApprove(TransactionAction ta) throws AppException {
		if (!check()) {
			return null;
		}
		try {
			boolean k = service.imExApprove(ta, null, InventoryConstanst.StockStatus.BLOCK_TD);
			HibernateUtil.commitCurrentSessions();
//			HibernateUtil.getSession().getTransaction().commit();
			return k;
		} catch (Exception ex) {

			AppException ap = new AppException("ERROR-U001", new Locale(InventoryConstanst.local));
			throw ap;
		}
	}

	// Kho con từ chối nhập hàng
	@WebMethod(operationName = "imImReject")
	public Boolean imImReject(TransactionAction ta) throws AppException {
		if (!check()) {
			return null;
		}
		try {
			boolean k = service.imImReject(ta, null, InventoryConstanst.StockStatus.BLOCK_TD);
			HibernateUtil.commitCurrentSessions();
//			HibernateUtil.getSession().getTransaction().commit();
			return k;
		} catch (Exception ex) {

			AppException ap = new AppException("ERROR-U001", new Locale(InventoryConstanst.local));
			throw ap;
		}
	}

	// Kho con đồng ý nhập hàng
	@WebMethod(operationName = "imImApprove")
	public Boolean imImApprove(TransactionAction ta) throws AppException {
		if (!check()) {
			return null;
		}
		try {
			boolean k = service.imImApprove(ta, null, InventoryConstanst.StockStatus.BLOCK_TD);
			HibernateUtil.commitCurrentSessions();
//			HibernateUtil.getSession().getTransaction().commit();
			return k;
		} catch (Exception ex) {

			AppException ap = new AppException("ERROR-U001", new Locale(InventoryConstanst.local));
			throw ap;
		}
	}

	// Search danh sách theo detailId
	@WebMethod(operationName = "getByDetail")
	public List<StockTransactionSerial> getByDetail(Long detailId) throws Exception {
		if (!check()) {
			return null;
		}
		List<StockTransactionSerial> rs = service.getByDetail(detailId);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	// Ex - subshop
	@WebMethod(operationName = "getListExSubShopReasons")
	public List<ApDomain> getListExSubShopReasons() throws Exception {
		if (!check()) {
			return null;
		}
		List<ApDomain> rs = service.getListExSubShopReasons();
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	@WebMethod(operationName = "exRequire")
	public Boolean exRequire(TransactionAction ta, StockTransaction st) throws AppException {
		if (!check()) {
			return null;
		}
		try {
			boolean k = service.exRequire(ta, st, InventoryConstanst.StockStatus.BLOCK_TD);
			HibernateUtil.commitCurrentSessions();
//			HibernateUtil.getSession().getTransaction().commit();
			return k;
		} catch (Exception ex) {

			AppException ap = new AppException("ERROR-U001", new Locale(InventoryConstanst.local));
			throw ap;
		}
	}

	@WebMethod(operationName = "exAppApprove")
	public Boolean exAppApprove(TransactionAction ta, StockTransaction st) throws AppException {
		if (!check()) {
			return null;
		}
		try {
			boolean k = service.exAppApprove(ta, st, InventoryConstanst.StockStatus.BLOCK_TD);
			HibernateUtil.commitCurrentSessions();
//			HibernateUtil.getSession().getTransaction().commit();
			return k;
		} catch (Exception ex) {

			AppException ap = new AppException("ERROR-U001", new Locale(InventoryConstanst.local));
			throw ap;
		}
	}

	@WebMethod(operationName = "exReject")
	public Boolean exReject(TransactionAction ta, StockTransaction st) throws AppException {
		if (!check()) {
			return null;
		}
		try {
			boolean k = service.exReject(ta, st, InventoryConstanst.StockStatus.BLOCK_TD);
			HibernateUtil.commitCurrentSessions();
//			HibernateUtil.getSession().getTransaction().commit();
			return k;
		} catch (Exception ex) {

			AppException ap = new AppException("ERROR-U001", new Locale(InventoryConstanst.local));
			throw ap;
		}
	}

	@WebMethod(operationName = "exExApprove")
	public Boolean exExApprove(TransactionAction ta, StockTransaction st) throws AppException {
		if (!check()) {
			return null;
		}
		try {
			boolean k = service.exExApprove(ta, st, InventoryConstanst.StockStatus.BLOCK_TD);
			HibernateUtil.commitCurrentSessions();
//			HibernateUtil.getSession().getTransaction().commit();
			return k;
		} catch (Exception ex) {

			AppException ap = new AppException("ERROR-U001", new Locale(InventoryConstanst.local));
			throw ap;
		}
	}

	@WebMethod(operationName = "exImApprove")
	public Boolean exImApprove(TransactionAction ta, StockTransaction st) throws AppException {
		if (!check()) {
			return null;
		}
		try {
			boolean k = service.exImApprove(ta, st, InventoryConstanst.StockStatus.BLOCK_TD);
			HibernateUtil.commitCurrentSessions();
//			HibernateUtil.getSession().getTransaction().commit();
			return k;
		} catch (Exception ex) {

			AppException ap = new AppException("ERROR-U001", new Locale(InventoryConstanst.local));
			throw ap;
		}
	}

	// EtagSearch
	@WebMethod(operationName = "getByEtagSerial")
	public EquipmentsDetail getByEtagSerial(Long taId, String serial) throws Exception {
		if (!check()) {
			return null;
		}
		EquipmentsDetail rs = service.getByEtagSerial(taId, serial);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	@WebMethod(operationName = "getTaBySerial")
	public List<TransactionAction> getTaBySerial(Long prvId, Long gId, String serial) throws Exception {
		if (!check()) {
			return null;
		}
		List<TransactionAction> rs = service.getTaBySerial(prvId, gId, serial);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	@WebMethod(operationName = "getTransSerial")
	public List<GoodsStatusTransSerial> getTransSerial(Long prvId, Long gId, String serial) throws Exception {
		if (!check()) {
			return null;
		}
		List<GoodsStatusTransSerial> rs = service.getTransSerial(prvId, gId, serial);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	@WebMethod(operationName = "getGoodsStatusTrans")
	public GoodsStatusTrans getGoodsStatusTrans(Long id) throws Exception {
		if (!check()) {
			return null;
		}
		GoodsStatusTrans rs = service.getGoodsStatusTrans(id);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	@WebMethod(operationName = "getListTransStatus")
	public List<ApDomain> getListTransStatus() throws Exception {
		if (!check()) {
			return null;
		}
		List<ApDomain> rs = service.getListTransStatus();
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	@WebMethod(operationName = "getListTransType")
	public List<ApDomain> getListTransType() throws Exception {
		if (!check()) {
			return null;
		}
		List<ApDomain> rs = service.getListTransType();
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	// Search Trans
	@WebMethod(operationName = "searchTransSearch")
	public List<StockTransactionSerial> searchTransSearch(String from, String to, Long providerId, Long gid,
			Long gstatus, Long taId, Long stockId, Integer start, Integer get) throws Exception {
		if (!check()) {
			return null;
		}
		List<StockTransactionSerial> rs = service.searchTransSearch(from, to, providerId, gid, gstatus, taId, stockId,null,
				start, get);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	@WebMethod(operationName = "searchTransSearchAnalysis")
	public List<SerialA> searchTransSearchAnalysis(String from, String to, Long providerId, Long gid, Long gstatus,
			Long taId, Long stockId) throws Exception {
		if (!check()) {
			return null;
		}
		List<StockTransactionSerial> listS = service.searchTransSearch(from, to, providerId, gid, gstatus, taId,
				stockId, null,null, null);
		AnalysisSerial ann = new AnalysisSerial(listS, null);
		List<SerialA> rs = ann.analysis();
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	@WebMethod(operationName = "searchTransSearchSize")
	public Long searchTransSearchSize(String from, String to, Long providerId, Long gid, Long gstatus, Long taId,
			Long stockId) throws Exception {
		if (!check()) {
			return null;
		}
		Long rs = service.searchTransSearchSize(from, to, providerId, gid, gstatus, taId, stockId);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	@WebMethod(operationName = "getLastByTransaction")
	public StockTransaction getLastByTransaction(Long taId) throws Exception {
		if (!check()) {
			return null;
		}
		StockTransaction rs = service.getLastByTransaction(taId);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	@WebMethod(operationName = "getByUserName")
	public Staff getByUserName(String usname) throws Exception {
		if (!check()) {
			return null;
		}
		Staff rs = service.getByUserName(usname);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	@WebMethod(operationName = "getTransActionById")
	public TransactionAction getTransActionById(Long id) throws Exception {
		if (!check()) {
			return null;
		}
		TransactionAction rs = service.getTransActionById(id);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	@WebMethod(operationName = "getStockSerialSizeByDetail")
	public Long getStockSerialSizeByDetail(Long detailid) throws Exception {
		if (!check()) {
			return null;
		}
		Long rs = service.getStockSerialSizeByDetail(detailid);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	@WebMethod(operationName = "checkSerial")
	public Boolean checkSerial(String serial, Long providerId) throws Exception {
		if (!check()) {
			return null;
		}
		Boolean rs = service.checkSerial(serial, providerId);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	@WebMethod(operationName = "getListReason")
	public List<ApDomain> getListReason(String type) throws Exception {
		if (!check()) {
			return null;
		}
		List<ApDomain> rs = service.getListReason(type);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	// Evaluate
	@WebMethod(operationName = "getReasonById")
	public ApDomain getReasonById(Long id) throws Exception {
		if (!check()) {
			return null;
		}
		ApDomain rs = service.getReasonByValue(id);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	@WebMethod(operationName = "getListEvaluaType")
	public List<ApDomain> getListEvaluaType() throws Exception {
		if (!check()) {
			return null;
		}
		List<ApDomain> rs = service.getListEvaluaType();
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	@WebMethod(operationName = "getListEvaluaReason")
	public List<ApDomain> getListEvaluaReason(Long type) throws Exception {
		if (!check()) {
			return null;
		}
		List<ApDomain> rs = service.getListEvaluaReason(type);
		HibernateUtil.commitCurrentSessions();
		return rs;
	}

	@WebMethod(operationName = "getListTransStockStatus")
	public List<ApDomain> getListTransStockStatus() throws Exception {
		if (!check()) {
			return null;
		}
		List<ApDomain> rs = service.getListTransStockStatus();
		HibernateUtil.commitCurrentSessions();
		return rs;
	}
}
