/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.java.bo;

import com.ftu.inventory.bo.StockGoodsInvSerial;
import com.ftu.inventory.dao.StockGoodsInvSerialDAO;
import com.ftu.inventory.dto.StockGoodsInvSerialDTO;
import com.ftu.staff.bo.Shop;
import com.ftu.utils.StringUtil;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author E5420
 */
public class LazySelledGoodsModel extends LazyDataModel<StockGoodsInvSerialDTO> implements Serializable {

	private Date fromDate;
	private Date toDate;
	private List<StockGoodsInvSerialDTO> datasource;
	private List<Shop> listShop;
	private StockGoodsInvSerial sgi;
	private Long goodsGroupId;
	String from;
	String to;
	int f = -1;

	public LazySelledGoodsModel(Date fromDate, Date toDate, String from, String to, StockGoodsInvSerialDTO sgi,
			Long goodsGroupId, List<Shop> listShop) {
		StockGoodsInvSerialDAO sgiDAO = new StockGoodsInvSerialDAO();
		this.listShop = listShop;
		this.sgi = sgiDAO.convertDTOtoEntity(sgi);
		this.from = from;
		this.to = to;
		this.goodsGroupId = goodsGroupId;
		this.toDate = toDate;
		this.fromDate = fromDate;
		f = 0;
	}

	private String getShopName(Long shopId) {
		if (listShop != null) {
			for (Shop d : listShop) {
				if (d.getShopId().equals(shopId)) {
					return d.getShopName();
				}
			}
		}
		return "";
	}

	@Override
	public StockGoodsInvSerialDTO getRowData(String rowKey) {
		if (datasource != null) {
			for (StockGoodsInvSerialDTO g : datasource) {
				if (g.getId().toString().equals(rowKey)) {
					return g;
				}
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(StockGoodsInvSerialDTO g) {
		return g.getId();
	}

	@Override
	public List<StockGoodsInvSerialDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder,
										  Map<String, Object> filters) {
		StockGoodsInvSerialDAO sgiDAO = new StockGoodsInvSerialDAO();
		if (f == 0) {
			first = 0;
		}
		datasource = sgiDAO.searchContainOutstockDateTime(fromDate, toDate, from, to, sgi, goodsGroupId, first,
				pageSize);
		if (datasource != null) {
			SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
			for (StockGoodsInvSerialDTO s : datasource) {
				s.setShopName(getShopName(s.getShopId()));
				if(!StringUtil.stringIsNullOrEmty(s.getOutstockDatetime()))
				s.setStrOutstockDatetime(sf.format(s.getOutstockDatetime()));
			}
		}

		int dataSize = sgiDAO.searchSizeContainOutstockDatetime(fromDate, toDate, from, to, sgi, goodsGroupId)
				.intValue();
		this.setRowCount(dataSize);
		f = -1;
		return datasource;
	}
}
