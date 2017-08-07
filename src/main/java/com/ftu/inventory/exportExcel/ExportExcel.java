/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.exportExcel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;

import com.ftu.inventory.bo.*;
import com.ftu.inventory.dto.StockGoodsInvSerialDTO;
import com.ftu.utils.DateTimeUtils;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;

import com.ftu.inventory.bo.StockGoodsInvSerial;
import com.ftu.java.bo.SerialA;
import com.ftu.staff.bo.Shop;

/**
 *
 * @author Cao Cuong
 */
public class ExportExcel implements Serializable {

	public String exportGeneralReport(List<StockGoods> stockGoods, String userName, String shopName)
			throws FileNotFoundException, IOException {
		String tempStockGoods = "/WEB-INF/template/temp_bc_tonghop.xlsx";
		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
		InputStream is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(tempStockGoods);
		String out = "exportGeneralReport_" + new Long(new Date().getTime()).toString() + ".xlsx";
		OutputStream os = new FileOutputStream(out);
		Context context = new Context();
		context.putVar("date", sf.format(new Date()));
		context.putVar("name", userName);
		context.putVar("shopName", shopName);
		context.putVar("stockGoods", stockGoods);
		JxlsHelper.getInstance().processTemplate(is, os, context);
		is.close();
		os.close();
		return out;
	}
	
	public String exportListStockGoodsSold(List<StockGoods> stockGoods, String userName, String shopName)
			throws FileNotFoundException, IOException {
		String tempStockGoods = "/WEB-INF/template/temp_bc_daban_tonghop.xlsx";
		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
		Long total1 = 0L;
		InputStream is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(tempStockGoods);
		String out = "exportStocks_" + new Long(new Date().getTime()).toString() + ".xlsx";
		for (int i = 0; i < stockGoods.size(); i++) {
			StockGoods s = stockGoods.get(i);
			total1 += s.getSold();
			s.setIndex(i + 1L);
		}
		OutputStream os = new FileOutputStream(out);
		Context context = new Context();
		context.putVar("date", sf.format(new Date()));
		context.putVar("name", userName);
		context.putVar("shopName", shopName);
		context.putVar("total", total1.toString());
		context.putVar("stockGoods", stockGoods);
		JxlsHelper.getInstance().processTemplate(is, os, context);
		is.close();
		os.close();
		return out;
	}

	public String exportListStockGoods(List<StockGoods> stockGoods, String userName, String shopName)
			throws FileNotFoundException, IOException {
		String tempStockGoods = "/WEB-INF/template/temp_bc_hangton_tonghop.xlsx";
		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
		Long total1 = 0L;
		Long total2 = 0L;
		InputStream is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(tempStockGoods);
		String out = "exportStocks_" + new Long(new Date().getTime()).toString() + ".xlsx";
		for (int i = 0; i < stockGoods.size(); i++) {
			StockGoods s = stockGoods.get(i);
			total1 += s.getAvailableQuantity();
			total2 += s.getQuantityBlock();
			s.setIndex(i + 1L);
		}
		OutputStream os = new FileOutputStream(out);
		Context context = new Context();
		context.putVar("date", sf.format(new Date()));
		context.putVar("name", userName);
		context.putVar("shopName", shopName);
		context.putVar("total1", total1.toString());
		context.putVar("total2", total2.toString());
		context.putVar("stockGoods", stockGoods);
		JxlsHelper.getInstance().processTemplate(is, os, context);
		is.close();
		os.close();
		return out;
	}
	public String exportListStockGoodsKKVT(List<StockGoods> stockGoods, String userName, String shopName)
			throws FileNotFoundException, IOException {
		String tempStockGoods = "/WEB-INF/template/temp_bc_kiemke_kho.xlsx";
		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
		Long total1 = 0L;
		Long total2 = 0L;
		InputStream is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(tempStockGoods);
		String out = "exportStocks_" + new Long(new Date().getTime()).toString() + ".xlsx";
		for (int i = 0; i < stockGoods.size(); i++) {
			StockGoods s = stockGoods.get(i);
			if( s.getAvailableQuantity()!=null){
				total1 += s.getAvailableQuantity();
			}
//			total2 += s.getQuantityBlock();
			s.setIndex(i + 1L);
		}
		OutputStream os = new FileOutputStream(out);
		Context context = new Context();
		String today =  sf.format(new Date());
		String[] lsDay = today.split("/");
//		Ngày ${day} tháng ${month} năm ${year}
		context.putVar("today",today);
		context.putVar("day",lsDay[0]);
		context.putVar("month",lsDay[1]);
		context.putVar("year",lsDay[2]);
		context.putVar("dayMonth",lsDay[0] +"/"+lsDay[1]);
//		context.putVar("name", userName);
//		context.putVar("shopName", shopName);
		context.putVar("total1", total1.toString());
//		context.putVar("total2", total2.toString());
		context.putVar("stockGoods", stockGoods);
		JxlsHelper.getInstance().processTemplate(is, os, context);
		is.close();
		os.close();
		return out;
	}
	
	public String exportForGeneralReport(Long instockSum, Long instockErrSum, Long blockSum, List<StockGoods> stockGoods, String userName, String shopName)
			throws FileNotFoundException, IOException {
		String tempStockGoods = "/WEB-INF/template/temp_bc_tonghop.xlsx";
		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
		InputStream is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(tempStockGoods);
		String out = "exportStocks_" + new Long(new Date().getTime()).toString() + ".xlsx";
		for (int i = 0; i < stockGoods.size(); i++) {
			StockGoods s = stockGoods.get(i);
			s.setIndex(i + 1L);
		}
		OutputStream os = new FileOutputStream(out);
		Context context = new Context();
		context.putVar("date", sf.format(new Date()));
		context.putVar("name", userName);
		context.putVar("shopName", shopName);
		context.putVar("stockGoods", stockGoods);
		context.putVar("instockSum", instockSum);
		context.putVar("instockErrSum", instockErrSum);
		context.putVar("blockSum", blockSum);
		JxlsHelper.getInstance().processTemplate(is, os, context);
		is.close();
		os.close();
		return out;
	}
	
//	public String exportGeneralTransactionReport(List<TransactionAction> listTA,  String userName, String shopName, Date fromDate, Date toDate, Long total) throws FileNotFoundException, IOException {
//		String tempStockGoods = "/WEB-INF/template/temp_bc_tonghop_xuatnhap_kho.xlsx";
//		SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
//		InputStream is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(tempStockGoods);
//		String out = "exportStocks_" + new Long(new Date().getTime()).toString() + ".xlsx";
//		
//		OutputStream os = new FileOutputStream(out);
//		Context context = new Context();
//		context.putVar("date", sf.format(new Date()));
//		context.putVar("name", userName);
//		context.putVar("shopName", shopName);
//		if(fromDate != null){
//			context.putVar("fromDate", sf.format(fromDate));
//		}
//		if(toDate != null){
//			context.putVar("toDate", sf.format(toDate));
//		}
//		context.putVar("total", total);
//		context.putVar("StockGoodsInvSerial", listTA);
//		JxlsHelper.getInstance().processTemplate(is, os, context);
//		is.close();
//		os.close();
//		return out;
//	}
	
	public String exportGeneralTransactionReport(List<TransactionActionDetail> listIM, List<TransactionActionDetail> listEX,  Date fromDate, Date toDate, String userName, String shopName, Long sumIM, Long sumEX) throws FileNotFoundException, IOException {
		String tempStockGoods = "";
		if (listEX.size() == 0)
			tempStockGoods = "/WEB-INF/template/temp_bc_tonghop_xuatnhapkho_noex.xlsx";
		else //if (listEX.size() == 0)
			tempStockGoods = "/WEB-INF/template/temp_bc_tonghop_xuatnhapkho.xlsx";
		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
		InputStream is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(tempStockGoods);
		String out = "exportStocks_" + new Long(new Date().getTime()).toString() + ".xlsx";
		
		OutputStream os = new FileOutputStream(out);
		Context context = new Context();
		context.putVar("createDate", sf.format(new Date()));
		context.putVar("staffName", userName);
		context.putVar("shopName", shopName);
		if(fromDate != null){
			context.putVar("fromDate", sf.format(fromDate));
		}
		if(toDate != null){
			context.putVar("toDate", sf.format(toDate));
		}
		if(sumIM != null){
			context.putVar("sumIM", sumIM);
		}
		if(sumEX != null){
			context.putVar("sumEX", sumEX);
		}
		if(listEX != null && listEX.size() > 0){
			context.putVar("listEX", listEX);
		}
		if(listIM != null){
			context.putVar("listIM", listIM);
		}
		JxlsHelper.getInstance().processTemplate(is, os, context);
		is.close();
		os.close();
		return out;
	}
	
	public String exportDetailTransactionReport( List<StockTransactionSerial> list, String userName, String shopName, Long sum) throws FileNotFoundException, IOException {
		String tempStockGoods = "";
//		if (listEX.size() == 0)
//			tempStockGoods = "/WEB-INF/template/temp_bc_chitiet_xuatnhapkho_noex.xlsx";
//		else if (listEX.size() > 0)
			tempStockGoods = "/WEB-INF/template/temp_bc_chitiet_xuatnhapkho.xlsx";

		
		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
		InputStream is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(tempStockGoods);
		String out = "exportStocks_" + new Long(new Date().getTime()).toString() + ".xlsx";
		
		OutputStream os = new FileOutputStream(out);
		Context context = new Context();
		context.putVar("createDate", sf.format(new Date()));
		context.putVar("staffName", userName);
		context.putVar("shopName", shopName);
//		if(fromDate != null){
//			context.putVar("fromDate", sf.format(fromDate));
//		}
//		if(toDate != null){
//			context.putVar("toDate", sf.format(toDate));
//		}
//		if(sumIM != null){
//			context.putVar("sumIM", sumIM);
//		}
		if(sum != null){
			context.putVar("sum", sum);
		}
		if(list != null && list.size() > 0){
			context.putVar("list", list);
		}
//		if(listIM != null){
//			context.putVar("listIM", listIM);
//		}
		JxlsHelper.getInstance().processTemplate(is, os, context);
		is.close();
		os.close();
		return out;
	}

	public String exportListStockInvenSerial(List<StockGoodsInvSerialDTO> listStockGoodsInvSerial, String userName,
                                             String shopName, String templateFileName) throws FileNotFoundException, IOException {

		String tempStockGoodsItem = "/WEB-INF/template/" + templateFileName;
		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");

		InputStream is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(tempStockGoodsItem);
		String output = "exportListStockGoodsInvenSerial_" + new Long(new Date().getTime()).toString() + ".xlsx";
		OutputStream os = new FileOutputStream(output);
		Context context = new Context();
		context.putVar("date", sf.format(new Date()));
		context.putVar("name", userName);
		context.putVar("shopName", shopName);
		context.putVar("stockGoodsInvSerial", listStockGoodsInvSerial);
		JxlsHelper.getInstance().processTemplate(is, os, context);
		is.close();
		os.close();
		return output;
	}
	
	public String printStockTracsactionDetail(String createShop, String fromShop, List<StockTransactionDetail> listDT,TransactionAction ta, Long sum) throws FileNotFoundException, IOException {
		String tempStockGoodsItem = "/WEB-INF/template/temp_phieu_nhap_kho.xlsx";
//		SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
		InputStream is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(tempStockGoodsItem);
		String output = "printStockTransactionDetail_" + new Long(new Date().getTime()).toString() + ".xlsx";
		OutputStream os = new FileOutputStream(output);
		Context context = new Context();
		SimpleDateFormat fomat = new SimpleDateFormat("dd/MM/yyyy");
		String[] strD = fomat.format(new Date()).split("/");
//		context.putVar("createDate", fomat.format(new Date()));
//		context.putVar("importDate", ta.getAssDateString());
		context.putVar("toShop", fromShop);
//		context.putVar("typeName", ta.getTypeName());
//		context.putVar("reason", ta.getReasonName());
//		context.putVar("createStaff", ta.getStaffName());
//		context.putVar("assStaff", ta.getAssStaffName());
		context.putVar("sum", sum);
		context.putVar("stockTransactionDetail", listDT);
//		context.putVar("createShop", createShop);
		context.putVar("orderCode", ta.getTransactionActionCode());
		context.putVar("day", strD[0]);
		context.putVar("month", strD[1]);
		context.putVar("year", strD[2]);
		JxlsHelper.getInstance().processTemplate(is, os, context);
		is.close();
		os.close();
		return output;
	}
	public String printStockTracsactionDetailAssign(String createShop, String fromShop, List<StockTransactionDetail> listDT,TransactionAction ta, Long sum) throws FileNotFoundException, IOException {
		String tempStockGoodsItem = "/WEB-INF/template/temp_phieu_dieu_chuyen.xlsx";
//		SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
		InputStream is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(tempStockGoodsItem);
		String output = "printStockTransactionDetail_" + new Long(new Date().getTime()).toString() + ".xlsx";
		OutputStream os = new FileOutputStream(output);
		Context context = new Context();
		SimpleDateFormat fomat = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat fomat2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		String[] strD = fomat.format(new Date()).split("/");
		String[] time = fomat2.format(new Date()).split(" ");
//		context.putVar("createDate", fomat.format(new Date()));
//		context.putVar("importDate", ta.getAssDateString());
		context.putVar("toShop", fromShop);
//		context.putVar("typeName", ta.getTypeName());
//		context.putVar("reason", ta.getReasonName());
//		context.putVar("createStaff", ta.getStaffName());
//		context.putVar("assStaff", ta.getAssStaffName());
//		context.putVar("sum", sum);
		context.putVar("stockTransactionDetail", listDT);
		context.putVar("createShop", createShop);
		context.putVar("orderCode", ta.getTransactionActionCode());
		context.putVar("day", strD[0]);
		context.putVar("month", strD[1]);
		context.putVar("year", strD[2]);
		context.putVar("hour", time[1]);
		JxlsHelper.getInstance().processTemplate(is, os, context);
		is.close();
		os.close();
		return output;
	}
	public String  printStockMovementDetail( Shop createShop, Shop fromShop, Shop toShop, List<StockTransactionDetail> listDT,TransactionAction ta, Long sum) throws FileNotFoundException, IOException {
		String tempStockGoodsItem = "/WEB-INF/template/temp_phieu_dieu_chuyen_im.xlsx";
//		SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
		InputStream is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(tempStockGoodsItem);
		String output = "printStockMovementDetail_" + new Long(new Date().getTime()).toString() + ".xlsx";
		OutputStream os = new FileOutputStream(output);
		Context context = new Context();

		context.putVar("createShop", createShop);
		SimpleDateFormat fomat = new SimpleDateFormat("dd/MM/yyyy");
		context.putVar("createDate", fomat.format(new Date()));
		context.putVar("ordercode", ta.getTransactionActionCode());
		
		context.putVar("moveDate", ta.getAssDateString());
		
		context.putVar("fromShop", fromShop);
		context.putVar("toShop", toShop);
		context.putVar("reason", ta.getReasonName());
		context.putVar("assStaff", ta.getAssStaffName());
        context.putVar("createStaff", ta.getStaffName());
		context.putVar("sum", sum);
		context.putVar("stockTransactionDetail", listDT);
		JxlsHelper.getInstance().processTemplate(is, os, context);
		is.close();
		os.close();
		return output;
	}
	public String  printStockMovementDetail1( Shop createShop, Shop fromShop, Shop toShop, List<TransactionActionDetail> listDT,TransactionAction ta, Long sum) throws FileNotFoundException, IOException {
		String tempStockGoodsItem = "/WEB-INF/template/temp_phieu_dieu_chuyen_ex.xlsx";
//		SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
		InputStream is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(tempStockGoodsItem);
		String output = "printStockMovementDetail_" + new Long(new Date().getTime()).toString() + ".xlsx";
		OutputStream os = new FileOutputStream(output);
		Context context = new Context();

		context.putVar("createShop", createShop);
		SimpleDateFormat fomat = new SimpleDateFormat("dd/MM/yyyy");
		context.putVar("createDate", fomat.format(new Date()));
		context.putVar("ordercode", ta.getTransactionActionCode());
		
		context.putVar("moveDate", ta.getAssDateString());
		
		context.putVar("fromShop", fromShop);
		context.putVar("toShop", toShop);
		context.putVar("reason", ta.getReasonName());
        context.putVar("createStaff", ta.getStaffName());
		context.putVar("assStaff", ta.getAssStaffName());
		context.putVar("sum", sum);
		context.putVar("stockTransactionDetail", listDT);
		JxlsHelper.getInstance().processTemplate(is, os, context);
		is.close();
		os.close();
		return output;
	}
	
	public String printTransactionActionDetail(String createShop, String toShop, List<TransactionActionDetail> listDT,TransactionAction ta, Long sum) throws FileNotFoundException, IOException {
		String tempStockGoodsItem = "/WEB-INF/template/temp_phieu_xuat_kho.xlsx";
		InputStream is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(tempStockGoodsItem);
		String output = "printStockTransactionDetail_" + new Long(new Date().getTime()).toString() + ".xlsx";
		OutputStream os = new FileOutputStream(output);
		Context context = new Context();
		SimpleDateFormat fomat = new SimpleDateFormat("dd/MM/yyyy");
		String[] strD = fomat.format(new Date()).split("/");
//		context.putVar("createDate", fomat.format(new Date()));
//		context.putVar("exportDate", ta.getAssDateString());
//		context.putVar("toShop", toShop);
//		context.putVar("typeName", ta.getTypeName());
//		context.putVar("reason", ta.getReasonName());
//		context.putVar("createStaff", ta.getStaffName());
//		context.putVar("assStaff", ta.getAssStaffName());
		context.putVar("sum", sum);
		context.putVar("stockTransactionDetail", listDT);
		context.putVar("createShop", createShop);
		context.putVar("ordercode", ta.getTransactionActionCode());
		context.putVar("day", strD[0]);
		context.putVar("month", strD[1]);
		context.putVar("year", strD[2]);
		JxlsHelper.getInstance().processTemplate(is, os, context);
		is.close();
		os.close();
		return output;
	}

	public String printTransactionActionDetailWaranty(String createShop, String toShop, List<TransactionActionDetail> listDT,TransactionAction ta, Long sum) throws FileNotFoundException, IOException {
		String tempStockGoodsItem = "/WEB-INF/template/temp_phieu_xuat_kho_warranty.xlsx";
		InputStream is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(tempStockGoodsItem);
		String output = "printStockTransactionDetail_" + new Long(new Date().getTime()).toString() + ".xlsx";
		OutputStream os = new FileOutputStream(output);
		Context context = new Context();
		SimpleDateFormat fomat = new SimpleDateFormat("dd/MM/yyyy");
		String[] strD = fomat.format(new Date()).split("/");
//		context.putVar("createDate", fomat.format(new Date()));
//		context.putVar("exportDate", ta.getAssDateString());
//		context.putVar("toShop", toShop);
//		context.putVar("typeName", ta.getTypeName());
//		context.putVar("reason", ta.getReasonName());
//		context.putVar("createStaff", ta.getStaffName());
//		context.putVar("assStaff", ta.getAssStaffName());
		context.putVar("sum", sum);
		context.putVar("stockTransactionDetail", listDT);
		context.putVar("createShop", createShop);
		context.putVar("ordercode", ta.getTransactionActionCode());
		context.putVar("day", strD[0]);
		context.putVar("month", strD[1]);
		context.putVar("year", strD[2]);
		JxlsHelper.getInstance().processTemplate(is, os, context);
		is.close();
		os.close();
		return output;
	}
	public String printTransactionActionAssign(String createShop, String toShop, List<TransactionActionDetail> listDT,TransactionAction ta, Long sum) throws FileNotFoundException, IOException {
		String tempStockGoodsItem = "/WEB-INF/template/temp_phieu_dieu_chuyen.xlsx";
		InputStream is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(tempStockGoodsItem);
		String output = "printStockTransactionDetail_" + new Long(new Date().getTime()).toString() + ".xlsx";
		OutputStream os = new FileOutputStream(output);
		Context context = new Context();
		SimpleDateFormat fomat = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat fomat2 = new SimpleDateFormat("dd/MM/yyyy HH'h'mm");
		String[] strD = fomat.format(new Date()).split("/");
		String[] time = fomat2.format(new Date()).split(" ");
//		context.putVar("createDate", fomat.format(new Date()));
//		context.putVar("exportDate", ta.getAssDateString());
		context.putVar("toShop", toShop);
//		context.putVar("typeName", ta.getTypeName());
//		context.putVar("reason", ta.getReasonName());
//		context.putVar("createStaff", ta.getStaffName());
//		context.putVar("assStaff", ta.getAssStaffName());
//		context.putVar("sum", sum);
		context.putVar("stockTransactionDetail", listDT);
		context.putVar("createShop", createShop);
//		context.putVar("ordercode", ta.getTransactionActionCode());
		context.putVar("day", strD[0]);
		context.putVar("month", strD[1]);
		context.putVar("year", strD[2]);
		context.putVar("hour", time[1]);
		JxlsHelper.getInstance().processTemplate(is, os, context);
		is.close();
		os.close();
		return output;
	}

	public String printWarranty(String createShop, String toShop, List<StockGoodsInvSerialDTO> listDT,
								TransactionAction ta, Long sum) throws FileNotFoundException, IOException {
		String tempStockGoodsItem = "/WEB-INF/template/temp_phieu_xuat_kho_waranty.xlsx";
		InputStream is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(tempStockGoodsItem);
		String output = "printStockTransactionDetail_" + new Long(new Date().getTime()).toString() + ".xlsx";
		OutputStream os = new FileOutputStream(output);
		Context context = new Context();
		SimpleDateFormat fomat = new SimpleDateFormat("dd/MM/yyyy");
		context.putVar("createDate", fomat.format(new Date()));
		context.putVar("exportDate", ta.getAssDateString());
//		context.putVar("toShop", toShop);
		context.putVar("typeName", ta.getTypeName());
		context.putVar("reason", ta.getReasonName());
		context.putVar("createStaff", ta.getStaffName());
		context.putVar("assStaff", ta.getAssStaffName());
		context.putVar("sum", sum);
		context.putVar("stockTransactionDetail", listDT);
		context.putVar("createShop", createShop);
		context.putVar("ordercode", ta.getTransactionActionCode());
		JxlsHelper.getInstance().processTemplate(is, os, context);
		is.close();
		os.close();
		return output;
	}
	public String printStockTransactionDetailKTV(String createShop, String toShop, List<StockTransactionDetail> listDT,TransactionAction ta, Long sum) throws FileNotFoundException, IOException {
		String tempStockGoodsItem = "/WEB-INF/template/temp_phieu_xuat_kho_KTV.xlsx";
		InputStream is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(tempStockGoodsItem);
		String output = "printStockTransactionDetail_" + new Long(new Date().getTime()).toString() + ".xlsx";
		OutputStream os = new FileOutputStream(output);
		Context context = new Context();
		SimpleDateFormat fomat = new SimpleDateFormat("dd/MM/yyyy");
		context.putVar("createDate", fomat.format(new Date()));
		context.putVar("exportDate", ta.getAssDateString());
		context.putVar("toShop", toShop);//
		context.putVar("typeName", ta.getTypeName());
//		context.putVar("reason", ta.getReasonName());
		context.putVar("createStaff", ta.getStaffName());//
		context.putVar("assStaff", ta.getAssStaffName());
		context.putVar("sum", sum);
		context.putVar("stockTransactionDetail", listDT);
		context.putVar("createShop", createShop);
		context.putVar("ordercode", ta.getTransactionActionCode());
		JxlsHelper.getInstance().processTemplate(is, os, context);
		is.close();
		os.close();
		return output;
	}

	public String exportListStockInvenSerialContainDatetime(String fromSerial, String toSerial, Date fromDateTemp,
                                                            Date toDateTemp, List<StockGoodsInvSerialDTO> listStockGoodsInvSerial, String userName, String shopName,
                                                            String templateFileName) throws FileNotFoundException, IOException {

		String tempStockGoodsItem = "/WEB-INF/template/" + templateFileName;
		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");

		InputStream is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(tempStockGoodsItem);
		String output = "exportListStockGoodsInvenSerial_" + new Long(new Date().getTime()).toString() + ".xlsx";
		OutputStream os = new FileOutputStream(output);
		Context context = new Context();
		context.putVar("date", sf.format(new Date()));
		context.putVar("name", userName);
		context.putVar("shopName", shopName);
		if(fromDateTemp != null){
			context.putVar("fromDate", sf.format(fromDateTemp));
		}
		if(toDateTemp  !=  null){
			context.putVar("toDate", sf.format(toDateTemp));
		}
		context.putVar("fromSerial", fromSerial);
		context.putVar("toSerial", toSerial);
		context.putVar("StockGoodsInvSerial", listStockGoodsInvSerial);
		JxlsHelper.getInstance().processTemplate(is, os, context);
		is.close();
		os.close();
		return output;
	}

	public String exportImportExportInstockReport(Date fromDate, Date toDate, String shopName, 
			List<ImportExportReport> lsResult, long totalOld, long totalImport, long totalExport, long totalNew) throws FileNotFoundException, IOException {
		
		String tempStockGoodsItem = "/WEB-INF/template/temp_bc_nhap_xuat_ton.xlsx";
		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");

		InputStream is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(tempStockGoodsItem);
		String output = "exportListStockGoodsInvenSerial_" + new Long(new Date().getTime()).toString() + ".xlsx";
		OutputStream os = new FileOutputStream(output);
		Context context = new Context();
		context.putVar("date", sf.format(new Date()));
		context.putVar("fromDate", sf.format(fromDate));
		context.putVar("toDate", sf.format(toDate));
		context.putVar("shopName", shopName);
		
//		context.putVar("totalOld", totalOld);
//		context.putVar("totalImport", totalImport);
//		context.putVar("totalExport", totalExport);
//		context.putVar("totalNew", totalNew);
		context.putVar("lsData", lsResult);
		JxlsHelper.getInstance().processTemplate(is, os, context);
		is.close();
		os.close();
		return output;
	}

	public String exportAudit(List<StockGoodsInvSerialDTO> lsResult, String staffName) throws FileNotFoundException, IOException {

		String tempStockGoodsItem = "/WEB-INF/template/temp_bc_dieu_chinh_tbvt.xlsx";
		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");

		InputStream is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(tempStockGoodsItem);
		String output = "dieu_chinh_tbvt_" + new Long(new Date().getTime()).toString() + ".xlsx";
		OutputStream os = new FileOutputStream(output);
		Context context = new Context();
		context.putVar("date", sf.format(new Date()));
//
//		context.putVar("totalOld", totalOld);
//		context.putVar("totalImport", totalImport);
//		context.putVar("totalExport", totalExport);
		context.putVar("totalRecord", lsResult.size());
		context.putVar("staffName", staffName);
		context.putVar("lsData", lsResult);
		JxlsHelper.getInstance().processTemplate(is, os, context);
		is.close();
		os.close();
		return output;
	}

}
