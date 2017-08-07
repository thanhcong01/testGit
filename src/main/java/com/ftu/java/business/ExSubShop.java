/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.java.business;

import com.ftu.constanst.InventoryConstanst;
import com.ftu.exception.AppException;
import com.ftu.inventory.bo.*;
import com.ftu.inventory.bo.StockGoodsInvSerial;
import com.ftu.inventory.dao.*;
import com.ftu.inventory.dto.StockGoodsInvSerialDTO;
import com.ftu.staff.bo.Shop;
import com.ftu.staff.dao.ShopDAO;
import com.ftu.ws.BusinessService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author E5420
 */
public class ExSubShop {

	StockTransaction st;
	TransactionAction ta;
	Long staffId;
	List<StockTransactionDetail> lsDetail;
	String description;
	String stockStatusBlock = InventoryConstanst.StockStatus.BLOCK_XT;
	public ExSubShop(TransactionAction ta, StockTransaction st, Long staffId, String stockStatus) {
		this.ta = ta;
		this.st = st;
		description=ta.getDescription() == null ? "" : ta.getDescription();
		if (st != null) {
			lsDetail = st.getLsDetail();
		}
		this.staffId = staffId;
		this.stockStatusBlock = stockStatus;
	}

	public boolean exRequire() throws AppException {
		TransactionActionDAO taDAO = new TransactionActionDAO();
		Date d = new Date();
		StockTransactionDAO stDAO = new StockTransactionDAO();
		StockTransactionDetailDAO stdDAO = new StockTransactionDetailDAO();
		StockGoodsInvSerialDAO sgiDAO = new StockGoodsInvSerialDAO();
		StockGoodsInvNoSerialDAO sginDAO = new StockGoodsInvNoSerialDAO();
		StockTransactionSerialDAO stsDAO = new StockTransactionSerialDAO();
		TransactionActionDetailDAO tadDAO = new TransactionActionDetailDAO();
		StockGoodsDAO sgDAO = new StockGoodsDAO();
		ta.setCreateDatetime(d);
		ta.setStatus(InventoryConstanst.TransactionStatus.CREATE);
		//
		//        ta.setFromShopId(ta.getFromShopId() == null ? ta.getCreateShopId() : ta.getFromShopId());
		//        ta.setToShopId(ta.getToShopId() == null ? ta.getCreateShopId() : ta.getToShopId());
		//
		taDAO.saveOrUpdate(ta);

		st.setTransactionActionId(ta.getTransactionActionId());
		st.setStockTransactionDatetime(d);
		stDAO.saveOrUpdate(st);
		HashMap<String, TransactionActionDetail> mapDetail = new HashMap<>();
		for (StockTransactionDetail std : lsDetail) {
			std.setStockTransactionId(st.getStockTransactionId());
			//			std.setQuantity(0L + std.getSetSerial().size());
			std.setStdDatetime(d);
			stdDAO.saveOrUpdate(std);
			EquipmentsProfileDAO eDao = new EquipmentsProfileDAO();
			EquipmentsProfile profile = eDao.findByProfileId(std.getGoodsId());
			if (profile.getSerial())
			{
				for (StockGoodsInvSerialDTO sgi : std.getSetSerial()) {
					//            	if(sgi.getShopId().equals(ta.getFromShopId()))
					//            	{
					//            		AppException a = new AppException("ERROR-U003");
					//            		throw a;
					//            	}
					// sgi = sgiDAO.findById(sgi.getId());
					TransactionActionDetail tad = mapDetail.get(sgi.getProviderId().toString()+"-"+sgi.getEquipmentProfileId().toString());
					if (tad == null) {
						tad = new TransactionActionDetail(sgi.getEquipmentProfileId(), sgi.getProviderId());
						tad.setDescription(std.getDescription());
						mapDetail.put(sgi.getProviderId().toString()+"-"+sgi.getEquipmentProfileId().toString(), tad);
					}
					tad.setQuantity(tad.getQuantity() + 1);
					sgi.setCurrentTaId(ta.getTransactionActionId());
					sgi.setStockStatus(stockStatusBlock);
					sgiDAO.updateStockTransBySql(sgiDAO.convertDTOtoEntity(sgi));
				}
				stsDAO.InsertByStockEquipmentsInvSerial(std.getStockTransactionDetailId(), ta.getTransactionActionId(), std.getGoodsId(), std.getGoodsStatus());
			}
			else
			{
				TransactionActionDetail tad = mapDetail.get("-"+std.getGoodsId().toString());
				if (tad == null) {
					tad = new TransactionActionDetail(std.getGoodsId(), null);
					tad.setDescription(std.getDescription());
					mapDetail.put("-"+std.getGoodsId().toString(), tad);
				}
				tad.setQuantity(std.getQuantity() + tad.getQuantity());


				StockGoodsInvNoSerial instock_sgin = sginDAO.getStockGoodsNoserial(std.getGoodsId(), 
						std.getGoodsStatus(),ta.getFromShopId(), InventoryConstanst.StockStatus.INSTOCK);

				if (instock_sgin == null)
				{
					AppException e = new AppException("ERROR-U001", new Locale(InventoryConstanst.local));
					return false;
				}

				instock_sgin.setQuantity(instock_sgin.getQuantity() - std.getQuantity());

				StockGoodsInvNoSerial block_sgin = sginDAO.getStockGoodsNoserial(std.getGoodsId(), 
						std.getGoodsStatus(),ta.getFromShopId(), stockStatusBlock);

				if (block_sgin == null)
				{
					block_sgin = new StockGoodsInvNoSerial(ta.getFromShopId(), std.getGoodsId(), 0L, std.getGoodsStatus(),
							stockStatusBlock, null, null, ta.getTransactionActionId());
				}

				block_sgin.setQuantity(block_sgin.getQuantity() + std.getQuantity());
				sginDAO.saveOrUpdate(instock_sgin);
				sginDAO.saveOrUpdate(block_sgin);

			}
			Long k = stsDAO.getSizeByDetail(std.getStockTransactionDetailId());
			if (!(k.intValue() - std.getSetSerial().size() == 0)) {
				AppException e = new AppException("ERROR-U001", new Locale(InventoryConstanst.local));
				return false;
			}
			StockGoods sg = sgDAO.getStockGoods(std.getGoodsId(), std.getGoodsStatus(), ta.getCreateShopId());
			if (sg != null) {
				sg.setAvailableQuantity(sg.getAvailableQuantity() - std.getQuantity());
				sgDAO.saveOrUpdate(sg);
			}
		}
		for (TransactionActionDetail tad : mapDetail.values()) {
			tad.setTransactionActionId(ta.getTransactionActionId());
			tad.setCreateDatetime(d);
			tadDAO.saveOrUpdate(tad);
		}
		return true;
	}

	public boolean exReject() {
		TransactionActionDAO taDAO = new TransactionActionDAO();
		StockTransactionDAO stDAO = new StockTransactionDAO();
		StockGoodsInvNoSerialDAO sginDAO = new StockGoodsInvNoSerialDAO();
		if (!ta.getStatus().equals(InventoryConstanst.TransactionStatus.CREATE)) {
			return false;
		}
		StockGoodsDAO sgDAO = new StockGoodsDAO();
		if (st != null) {
			st = stDAO.findById(st.getStockTransactionId());
			//            if (!st.getStockTransStatus().equals(InventoryConstanst.StockTransactionStatus.WAIT)) {
			//                return false;
			//            }
			st.setStockTransStatus(InventoryConstanst.StockTransactionStatus.REJECT);
			st.setFinishDatetime(new Date());
			stDAO.saveOrUpdate(st);
			for (StockTransactionDetail std : lsDetail) {
				EquipmentsProfileDAO eDao = new EquipmentsProfileDAO();
				EquipmentsProfile profile = eDao.findByProfileId(std.getGoodsId());
				if (!profile.getSerial())
				{
					StockGoodsInvNoSerial block_sgin = sginDAO.getStockGoodsNoserial(std.getGoodsId(), 
							std.getGoodsStatus(),ta.getFromShopId(), stockStatusBlock);

					if (block_sgin == null)
					{
						AppException e = new AppException("ERROR-U001", new Locale(InventoryConstanst.local));
						return false;
					}
					block_sgin.setQuantity(block_sgin.getQuantity() - std.getQuantity());

					StockGoodsInvNoSerial instock_sgin = sginDAO.getStockGoodsNoserial(std.getGoodsId(), 
							std.getGoodsStatus(),ta.getFromShopId(), InventoryConstanst.StockStatus.INSTOCK);

					if (instock_sgin == null)
					{
						instock_sgin = new StockGoodsInvNoSerial(ta.getFromShopId(), std.getGoodsId(), 0L, std.getGoodsStatus(),  
								InventoryConstanst.StockStatus.INSTOCK, null, null, ta.getTransactionActionId());
					}

					instock_sgin.setQuantity(instock_sgin.getQuantity() + std.getQuantity());
					sginDAO.saveOrUpdate(instock_sgin);
					sginDAO.saveOrUpdate(block_sgin);
				}
				StockGoods sg = sgDAO.getStockGoods(std.getGoodsId(), std.getGoodsStatus(), ta.getCreateShopId());
				sg.setAvailableQuantity(sg.getAvailableQuantity() + std.getQuantity());
				sgDAO.saveOrUpdate(sg);
			}
		}
		StockGoodsInvSerialDAO sgiDAO = new StockGoodsInvSerialDAO();
		ta.setStatus(InventoryConstanst.TransactionStatus.CANCEL);
		ta.setAssessmentDatetime(new Date());
		ta.setAssessmentStaffId(staffId);
		ta.setDescription(description);
		taDAO.saveOrUpdate(ta);

		sgiDAO.updateStatusByTaId(InventoryConstanst.StockStatus.INSTOCK, ta.getTransactionActionId(), ta.getTransactionActionId());

		return true;
	}

	public boolean exAppApprove() {
		TransactionActionDAO taDAO = new TransactionActionDAO();
		ta = taDAO.findById(ta.getTransactionActionId());
		if (!ta.getStatus().equals(InventoryConstanst.TransactionStatus.CREATE)) {
			return false;
		}
		ta.setDescription(description);
		ta.setAssessmentDatetime(new Date());
		ta.setAssessmentStaffId(staffId);
		ta.setStatus(InventoryConstanst.TransactionStatus.APPROVE);
		taDAO.saveOrUpdate(ta);
		return true;
	}

	public boolean exExApprove() {
		TransactionActionDAO taDAO = new TransactionActionDAO();
		StockTransactionDAO stDAO = new StockTransactionDAO();
		ta = taDAO.findById(ta.getTransactionActionId());
		if (!ta.getStatus().equals(InventoryConstanst.TransactionStatus.APPROVE)) {
			return false;
		}
		ta.setStatus(InventoryConstanst.TransactionStatus.EX);
		ta.setDescription(description);
		taDAO.saveOrUpdate(ta);
		st = stDAO.findById(st.getStockTransactionId());
		st.setExportStaffId(staffId);
		st.setStockTransactionDatetime(new Date());
		stDAO.saveOrUpdate(st);
		return true;
	}

	public boolean exImApprove() {
		TransactionActionDAO taDAO = new TransactionActionDAO();
		StockTransactionDAO stDAO = new StockTransactionDAO();
		
		ta = taDAO.findById(ta.getTransactionActionId());
		//		if (!ta.getEquipmentStatus().equals(InventoryConstanst.TransactionStatus.EX)
		//				&& ta.getEquipmentStatus().equals(InventoryConstanst.TransactionStatus.EX_R)) {
		//
		//		} 
		if (ta.getStatus().equals(InventoryConstanst.TransactionStatus.EX))
		{
			ta.setStatus(InventoryConstanst.TransactionStatus.IM);
		}
		else if (ta.getStatus().equals(InventoryConstanst.TransactionStatus.EX_R))
		{
			ta.setStatus(InventoryConstanst.TransactionStatus.IM_R);
		}
		else
			return false;



		ta.setDescription(description);
		taDAO.saveOrUpdate(ta);
		StockGoodsDAO sgDAO = new StockGoodsDAO();
		StockGoodsInvSerialDAO sgiDAO = new StockGoodsInvSerialDAO();
		StockGoodsInvNoSerialDAO sginDAO = new StockGoodsInvNoSerialDAO();
		EquipmentDetailDAO detailDAO = new EquipmentDetailDAO();
		BusinessService businessService = new BusinessService();
		String[] lstColumn = {InventoryConstanst.ACTION_AU_DETAIL.SHOP_ID};
		String[] lstColumnNosrial = {InventoryConstanst.ACTION_AU_DETAIL.SHOP_ID, InventoryConstanst.ACTION_AU_DETAIL.QUANTITY_XT};
		st = stDAO.findById(st.getStockTransactionId());
		st.setStockTransStatus(InventoryConstanst.StockTransactionStatus.SUCCESS);
		st.setDeliveryType(InventoryConstanst.StockTransactionDeliveryType.TRANS);
		st.setImportStaffId(staffId);
		st.setFinishDatetime(new Date());
		stDAO.saveOrUpdate(st);
		ShopDAO shopDAO  = new ShopDAO();
		for (StockTransactionDetail std : lsDetail) {
			EquipmentsProfileDAO eDao = new EquipmentsProfileDAO();
			EquipmentsProfile profile = eDao.findByProfileId(std.getGoodsId());
			if (profile.getSerial())
			{
				List<StockGoodsInvSerialDTO> setSerial = new ArrayList<>(std.getSetSerial());
				if (setSerial.isEmpty()) {
					setSerial =sgiDAO.convertEmtitystoDTOs(sgiDAO.searchByTaId(ta.getTransactionActionId(), stockStatusBlock, std.getGoodsId(), std.getGoodsStatus()));
				}
				StockGoods sgp = sgDAO.getStockGoods(std.getGoodsId(), std.getGoodsStatus(), ta.getToShopId());
				if (sgp == null) {
					sgp = new StockGoods(0L, 0L, ta.getToShopId(), std.getGoodsId());
					sgp.setGoodsStatus(std.getGoodsStatus());
				}
				sgp.setQuantity(sgp.getQuantity() + setSerial.size());
				sgp.setAvailableQuantity(sgp.getAvailableQuantity() + setSerial.size());
				sgDAO.saveOrUpdate(sgp);
				StockGoods sgc = sgDAO.getStockGoods(std.getGoodsId(), std.getGoodsStatus(), ta.getCreateShopId());
				if (sgc == null) {
					sgc = new StockGoods(0L, 0L, ta.getCreateShopId(), std.getGoodsId());
					sgc.setGoodsStatus(std.getGoodsStatus());
				}
				sgc.setQuantity(sgc.getQuantity() - setSerial.size());
				sgDAO.saveOrUpdate(sgc);
				sgiDAO.updateByTaId(ta.getTransactionActionId(), ta.getToShopId(), InventoryConstanst.StockStatus.INSTOCK, ta.getTransactionActionId());
				//insert history
				for (StockGoodsInvSerialDTO objSerial:setSerial) {
					EquipmentsDetail detail = detailDAO.findBySerial(objSerial.getSerial());
					if(detail!=null){
						detail.setStaffId(ta.getCreateStaffId());
						detail.setShopId(ta.getCreateShopId());
						Shop shop = null;
						if(ta.getFromShopId()!=null){
							shop = shopDAO.findById(ta.getFromShopId());
						}
						String[] valuesOld = new String[]{shop==null?"":shop.getShopName()};

						if(ta.getCreateShopId()!=null){
							shop = shopDAO.findById(ta.getToShopId());
						}
						String[] valuesNew = new String[]{shop==null?"":shop.getShopName()};
						businessService.insertHistory(detail, lstColumn, valuesNew,
								valuesOld, InventoryConstanst.ACTION_TYPE.TYPE_9L);
					}
				}
			}
			else
			{
				StockGoodsInvNoSerial block_sgin = sginDAO.getStockGoodsNoserial(std.getGoodsId(), 
						std.getGoodsStatus(),ta.getFromShopId(), stockStatusBlock);

				if (block_sgin == null)
				{
					AppException e = new AppException("ERROR-U001", new Locale(InventoryConstanst.local));
					return false;
				}

				block_sgin.setQuantity(block_sgin.getQuantity() - std.getQuantity());

				StockGoodsInvNoSerial instock_sgin = sginDAO.getStockGoodsNoserial(std.getGoodsId(), 
						std.getGoodsStatus(),ta.getToShopId(), InventoryConstanst.StockStatus.INSTOCK);

				if (instock_sgin == null)
				{
					instock_sgin = new StockGoodsInvNoSerial(ta.getToShopId(), std.getGoodsId(), std.getQuantity(), std.getGoodsStatus(),  
							InventoryConstanst.StockStatus.INSTOCK, null, null, ta.getTransactionActionId());
				}

				instock_sgin.setQuantity(instock_sgin.getQuantity() + std.getQuantity());
				sginDAO.saveOrUpdate(instock_sgin);
				sginDAO.saveOrUpdate(block_sgin);
				
				StockGoods sgp = sgDAO.getStockGoods(std.getGoodsId(), std.getGoodsStatus(), ta.getToShopId());
				if (sgp == null) {
					sgp = new StockGoods(0L, 0L, ta.getToShopId(), std.getGoodsId());
					sgp.setGoodsStatus(std.getGoodsStatus());
				}
				sgp.setQuantity(sgp.getQuantity() + std.getQuantity());
				sgp.setAvailableQuantity(sgp.getAvailableQuantity() + std.getQuantity());
				sgDAO.saveOrUpdate(sgp);
				StockGoods sgc = sgDAO.getStockGoods(std.getGoodsId(), std.getGoodsStatus(), ta.getCreateShopId());
				if (sgc == null) {
					sgc = new StockGoods(0L, 0L, ta.getCreateShopId(), std.getGoodsId());
					sgc.setGoodsStatus(std.getGoodsStatus());
				}
				sgc.setQuantity(sgp.getQuantity() - std.getQuantity());
				sgDAO.saveOrUpdate(sgc);
				//insert history
				List<EquipmentsDetail> details = detailDAO.findByProfile(sgp.getGoodsId());
				if(details!=null && !details.isEmpty()){
					EquipmentsDetail detail = details.get(0);
					if(detail!=null){
						detail.setStaffId(ta.getCreateStaffId());
						detail.setShopId(ta.getCreateShopId());
						Shop shop = null;
						if(ta.getFromShopId()!=null){
							shop = shopDAO.findById(ta.getFromShopId());
						}
						String[] valuesOld = new String[]{shop==null?"":shop.getShopName(), ""};

						if(ta.getCreateShopId()!=null){
							shop = shopDAO.findById(ta.getToShopId());
						}
						String[] valuesNew = new String[]{shop==null?"":shop.getShopName(), std.getQuantity().toString()};
						businessService.insertHistory(detail, lstColumnNosrial, valuesNew,
								valuesOld, InventoryConstanst.ACTION_TYPE.TYPE_9L);
					}
				}
			}
		}
		return true;
	}
}
