/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.utils;

import com.ftu.inventory.bo.EquipmentsDetail;
import com.ftu.inventory.bo.StockGoodsInvSerial;
import com.ftu.inventory.bo.StockTransactionSerial;
import com.ftu.inventory.bussiness.EquipmentsDetailService;
import com.ftu.inventory.dao.StockGoodsInvSerialDAO;
import com.ftu.inventory.dao.StockTransactionSerialDAO;
import com.ftu.inventory.dto.StockGoodsInvSerialDTO;
import com.ftu.java.bo.SerialA;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author E5420
 */
public class AnalysisSerial {

	private List<StockTransactionSerial> lsE;
	List<StockGoodsInvSerialDTO> lsStock;

//	@ManagedProperty(value = "#{equipmentsDetailService}")
	private EquipmentsDetailService equipmentsDetailService = new EquipmentsDetailService();
	private StockGoodsInvSerialDAO stockGoodsInvSerialDAO = new StockGoodsInvSerialDAO();
	public AnalysisSerial(List<StockTransactionSerial> lsE, List<StockGoodsInvSerialDTO> lsStock) {
		this.lsE = lsE;
		this.lsStock = lsStock;
	}

	public void sort() {
		Collections.sort(lsE, new Comparator<StockTransactionSerial>() {
			@Override
			public int compare(StockTransactionSerial t, StockTransactionSerial t1) {
				try {
					int rs = StringUtils.compareHexStrings(
							t.getSerialSearch(), t.getSerialSearch()) ? 1 : -1;
					return rs == 0 ? t.getProviderId().intValue() - t1.getProviderId().intValue() : rs;
				} catch (Exception ex) {
					return 1;
				}
			}
		});
	}

	public void sortSgi() {
		Collections.sort(lsStock, new Comparator<StockGoodsInvSerialDTO>() {
			@Override
			public int compare(StockGoodsInvSerialDTO t, StockGoodsInvSerialDTO t1) {
				try {
					int rs = StringUtils.compareHexStrings(
							t.getSerialSearch(),t1.getSerialSearch()) ? 1 : -1;
					return rs == 0 ? t.getProviderId().intValue() - t1.getProviderId().intValue() : rs;
				} catch (Exception ex) {
					return 1;
				}
			}
		});
	}

	public List<SerialA> analysis() {
		sort();
		List<SerialA> ls = new ArrayList<>();
//		SerialA s = new SerialA();
//		s.setIndex(index++);
//		System.out.println(lsStock);	
		Long index = 1l;

		for (int i = 0; i < lsE.size(); i++) {
			StockTransactionSerial ss = lsE.get(i);
			SerialA s = new SerialA();
			s.setIndex(index++);
//			s.setFromSerial(ss.getEquipmentDetail().getSerial());
			s.setFromSerial(ss.getSerial());
			s.setCount(ss.getQuantity());
			s.setToSerial(ss.getSerial());
			s.setStatus(ss.getEquipmentProfileStatus());
			s.setProviderName(ss.getProvider());
			s.setProviderId(ss.getProviderId());
			s.setGoodsId(ss.getEquipmentProfileId());

			if(ss.getEquipmentDetail()==null){
				List<EquipmentsDetail> lstDt = equipmentsDetailService.findByProfileAndSerial(s.getGoodsId(), ss.getSerial());
				if(lstDt!=null&&!lstDt.isEmpty()){
					ss.setEquipmentDetail(lstDt.get(0));
				}
			}
			StockGoodsInvSerial stg =  stockGoodsInvSerialDAO.getStockByProfileAndSerial(ss.getSerial(),s.getGoodsId());
			if(stg!=null){
				s.setInstockDatetime(stg.getInstockDatetime());
				s.setEquipmentProfileStatus(stg.getEquipmentProfileStatus());
				s.setStockStatus(stg.getStockStatus());
			}
			if(ss.getEquipmentDetail()!=null){
				s.setGoodsCode(ss.getEquipmentDetail().getEquipmentsProfileCode());
				s.setGoodsName(ss.getEquipmentDetail().getProfileName());
				s.setTransactionActionCode(ss.getTransactionActionCode());
				s.setLyceCycle(ss.getEquipmentDetail().getLifeCycle());
				s.setMaintancePeriod(ss.getEquipmentDetail().getMaintancePeriod());
				s.setExpireDateWarranty(ss.getEquipmentDetail().getWarantyExpiredDate());
				s.setWarranStatus(ss.getEquipmentDetail().getWarantyStatus());
				s.setWanranprio(ss.getEquipmentDetail().getWarrantyPeriod());
				s.setExpireDateWarranty(ss.getEquipmentDetail().getWarantyExpiredDate());
				s.setCountCo(ss.getEquipmentDetail().getCoNumber());
				s.setCountCq(ss.getEquipmentDetail().getCqNumber());
				s.setTransactionActionCode(ss.getEquipmentDetail().getReferenCode());

			}
			//todo:
			s.setCount(1L);
			s.setGoodsGroupName(ss.getGoodsGroupName());
			s.setTypeName(ss.getTypeName());
			s.setStrCreateDatetime(ss.getStrCreateDatetime());
			ls.add(s);
//			if (s.getFromSerial() == null) {
//				//System.out.println(ss.getSerial() + ": null");
//				s.setFromSerial(ss.getEquipmentDetail().getSerial());
//				s.setCount(1L);
//				s.setToSerial(ss.getEquipmentDetail().getSerial());
//				s.setEquipmentStatus(ss.getEquipmentDetail().getEquipmentStatus());
//				s.setProviderName(ss.getProvider());
//				s.setProviderId(ss.getProviderId());
//				s.setGoodsId(ss.getEquipmentProfileId());
//				s.setProfileCode(ss.getEquipmentDetail().getEquipmentsProfileCode());
//				s.setProfileName(ss.getEquipmentDetail().getProfileName());
//				s.setGoodsGroupName(ss.getGoodsGroupName());
//				s.setTransactionActionCode(ss.getTransactionActionCode());
//				s.setMaintancePeriod(ss.getEquipmentDetail().getMaintancePeriod());
//				s.setLyceCycle(ss.getEquipmentDetail().getLifeCycle());
//				s.setTypeName(ss.getTypeName());
//				s.setStrCreateDatetime(ss.getStrCreateDatetime());
//			} else if (checkNext(s.getToSerialSearch(), ss.getSerialSearch())
//					&& (s.getEquipmentStatus() == null || s.getEquipmentStatus().equals(ss.getEquipmentProfileStatus()))
//					&& (s.getProviderId() == null || s.getProviderId().equals(ss.getProviderId()))
//					&& (s.getGoodsId() == null || s.getGoodsId().equals(ss.getEquipmentProfileId()))) {
//				//System.out.println(ss.getSerial() + ": next");
//				s.setCount(s.getCount());
//				s.setToSerial(ss.getSerial());
//			} else {
//				//System.out.println(ss.getSerial() + ": else");
//				ls.add(s);
//				s = new SerialA(ss.getSerial(), ss.getSerial());
//				s.setIndex(index++);
//				s.setCount(1L);
//				s.setEquipmentStatus(ss.getEquipmentProfileStatus());
//				s.setProviderId(ss.getProviderId());
//				s.setGoodsId(ss.getEquipmentProfileId());
//				s.setProviderName(ss.getProvider());
//				s.setProfileCode(ss.getProfileCode());
//				s.setGoodsGroupName(ss.getGoodsGroupName());
//				s.setTransactionActionCode(ss.getTransactionActionCode());
//				s.setTypeName(ss.getTypeName());
//				s.setProfileName(ss.getProfileName());
//				s.setMaintancePeriod(ss.getEquipmentDetail().getMaintancePeriod());
//				s.setLyceCycle(ss.getEquipmentDetail().getLifeCycle());
//				s.setStrCreateDatetime(ss.getStrCreateDatetime());
//			}
//			if (i == lsE.size() - 1) {
//				s.setCount(s.getCount());
//				ls.add(s);
//			}
		}
		return ls;
	}

	public List<SerialA> analysisInvSerial() {
		sortSgi();
		List<SerialA> ls = new ArrayList<>();

		for (int i = 0; i < lsStock.size(); i++) {
			StockGoodsInvSerialDTO sgi = lsStock.get(i);
			SerialA s = new SerialA();
			s.setCount(1L);
			s.setFromSerial(sgi.getSerial());
			s.setToSerial(sgi.getSerial());
			s.setStatus(sgi.getEquipmentProfileStatus());
			s.setGoodsId(sgi.getEquipmentProfileId());
			s.setProviderId(sgi.getProviderId());
			s.setProviderName(sgi.getProviderName());
			s.setInstockDatetime(sgi.getInstockDatetime()) ;
			s.setEquipmentProfileStatus(sgi.getEquipmentProfileStatus());
			s.setStockStatus(sgi.getStockStatus());
//			if(!ls.contains(s)){
				ls.add(s);
//			}

		}
		//System.out.println(lsStock);	
//		for (int i = 0; i < lsStock.size(); i++) {
//			StockGoodsInvSerial sgi = lsStock.get(i);
//			if (s.getFromSerial() == null) {
//				s.setCount(1L);
//				s.setFromSerial(sgi.getSerial());
//				s.setToSerial(sgi.getSerial());
//				s.setEquipmentStatus(sgi.getEquipmentProfileStatus());
//				s.setGoodsId(sgi.getEquipmentProfileId());
//				s.setProviderId(sgi.getProviderId());
//				s.setProviderName(sgi.getProviderName());
//			} else if (checkNext(s.getToSerialSearch(), sgi.getSerialSearch())
//					&& (s.getProviderId() == null || s.getProviderId().equals(sgi.getProviderId()))
//					&& (s.getEquipmentStatus() == null || s.getEquipmentStatus().equals(sgi.getEquipmentProfileStatus()))
//					&& (s.getGoodsId() == null || s.getGoodsId().equals(sgi.getEquipmentProfileId()))) {
//				s.setCount(1l);
//				s.setToSerial(sgi.getSerial());
//			} else {
//				ls.add(s);
//				s = new SerialA(sgi.getSerial(), sgi.getSerial());
//				s.setCount(1L);
//				s.setEquipmentStatus(sgi.getEquipmentProfileStatus());
//				s.setGoodsId(sgi.getEquipmentProfileId());
//				s.setProviderId(sgi.getProviderId());
//				s.setProviderName(sgi.getProviderName());
//			}
//			if (i == lsStock.size() - 1) {
//				s.setCount(s.getCount());
//				ls.add(s);
//			}
//		}
		return ls;
	}

	private static boolean checkNext(String s, String k) {
		boolean ret = false;
		//System.out.println(s + " - " + k);
		if (s == null || k == null) return ret;
		try {
			ret = new BigInteger(k,10).subtract(new BigInteger(s,10)).equals(BigInteger.ONE);// == 0;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ret = false;
		}
		return ret;
	}
}
