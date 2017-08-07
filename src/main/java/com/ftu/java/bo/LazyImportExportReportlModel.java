/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.java.bo;

import com.ftu.constanst.InventoryConstanst;
import com.ftu.inventory.bo.ImportExportReport;
import com.ftu.inventory.bo.TransactionActionDetail;
import com.ftu.inventory.dao.ImportExportReportDAO;
import com.ftu.inventory.dao.TransactionActionDetailDAO;
import com.ftu.staff.bo.ApDomain;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author E5420
 */
public class LazyImportExportReportlModel extends LazyDataModel<ImportExportReport> implements Serializable {

	private List<ImportExportReport> datasource;
	private Long shopId;
	private Date fromCreateDatetime;
	private Date toCreateDatetime;
	int f = -1;

	public LazyImportExportReportlModel() {
		super();
		shopId = -1L;
		fromCreateDatetime = null;
		toCreateDatetime = null;
	}
	public LazyImportExportReportlModel( Long shopId, Date fromCreateDatetime, Date toCreateDatetime) {
		super();
		this.shopId = shopId;
		this.fromCreateDatetime = fromCreateDatetime;
		this.toCreateDatetime = toCreateDatetime;
	}
	


	@Override
	public ImportExportReport getRowData(String rowKey) {
		if (datasource != null) {
			for (ImportExportReport g : datasource) {
				if (String.valueOf(g.getId()).equals(rowKey)) {
					return g;
				}
			}
		}

		return null;
	}
	
	@Override
    public Object getRowKey(ImportExportReport transactionActionDetail) {
        return transactionActionDetail.getId();
    }

	@Override
	public List<ImportExportReport> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
		ImportExportReportDAO dao = new ImportExportReportDAO();
		if (f == 0) {
			first = 0;
		}
//		datasource = dao.getDataForReport(fromCreateDatetime,toCreateDatetime,shopId,first,pageSize);
//
//        setRowCount(dao.getReportSize(fromCreateDatetime,toCreateDatetime,shopId));

		f = -1;
		return datasource;
	}
}
