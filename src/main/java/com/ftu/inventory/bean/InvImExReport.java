/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bean;

import com.ftu.constanst.InventoryConstanst;
import com.ftu.inventory.bo.*;
import com.ftu.language.LanguageBean;
import com.ftu.staff.bo.ApDomain;
import com.ftu.staff.bo.Shop;
import com.ftu.staff.bussiness.ShopService;
import com.ftu.staff.dao.ShopDAO;
import com.ftu.utils.ComponentUtils;
import com.ftu.ws.BusinessService;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.*;

/**
 *
 * @author E5420
 */
@ManagedBean
@SessionScoped
public class InvImExReport implements Serializable {

    @ManagedProperty(value = "#{sessionBean}")
    private SessionBean sessionBean;
    @ManagedProperty(value = "#{languageBean}")
    private LanguageBean languageBean;
    private List<Shop> listShopFrom;
    private List<Shop> listShopTo;
    private Long fromShopId;
    private Long toShopId;
    private Date fromDate;
    private Date toDate;
    private String orderCode;
    private String status;
    private String orderType;
    private String orderGroup;
    private List<InvImExReportBo> lsData;
    private Map<String,String> mapOrderType;
    private Map<String,String> mapOrderStatus;
    private Map<String,String> mapOrderGroup;


    @PostConstruct
    public void init() {
    	sessionBean.loadData(sessionBean.getStaff(), sessionBean.getShop());
        ShopService service = new ShopService();
        listShopTo = service.getAllShop();

        listShopFrom = service.findAllShopTree(sessionBean.getShop().getShopId());
        mapOrderGroup = new LinkedHashMap<>();
        mapOrderGroup.put("1","Nhập");
        mapOrderGroup.put("2","Xuất");
        mapOrderGroup.put("3","Đánh giá");
        setOrderGroup("1");
        setOrderType("1");
        setFromShopId(sessionBean.getShop().getShopId());
        changeOrderGroup();
        changeOrderType();
    }

    public void search() {


        if("2NC".equals(orderType)){
            orderType = "2";
        }
        if("1".equals(orderType)){
            fromShopId = null;
            toShopId = 1L;
        }
        lsData = new ArrayList<InvImExReportBo>();
        lsData = sessionBean.getService().searchInvImExRp(fromDate, toDate, fromShopId, toShopId, orderType,status,orderCode);//(fromSerial, getToSerial(), sgis, goodsGroupId, listShop);

    }

    public void changeOrderGroup(){
        String orderGroup = getOrderGroup();
        switch (orderGroup){
            case "1" :
                mapOrderType = new LinkedHashMap<>();
                mapOrderType.put("1","Nhập nhà cung cấp");
                mapOrderType.put("2","Nhập hàng cấp trên");
                mapOrderType.put("3","Nhập hàng cấp dưới");
                mapOrderType.put("2NC","Nhập hàng ngang cấp");
                break;
            case "2" :
                mapOrderType = new LinkedHashMap<>();
                mapOrderType.put("6","Xuất nhà cung cấp");
                mapOrderType.put("3","Xuất hàng cấp trên");
                mapOrderType.put("2","Xuất hàng cấp dưới");
                mapOrderType.put("2NC","Xuất hàng ngang cấp");
                mapOrderType.put("8","Xuất bán");
                break;
            case "3" :
                mapOrderType = new LinkedHashMap<>();
                mapOrderType.put("7","Đánh giá");
                break;
            default:
                mapOrderType = new LinkedHashMap<>();
                mapOrderType.put("1","Nhập nhà cung cấp");
                mapOrderType.put("2","Nhập hàng cấp trên");
                mapOrderType.put("3","Nhập hàng cấp dưới");
                mapOrderType.put("2NC","Nhập hàng ngang cấp");
                break;
        }
    }

    public void changeOrderType(){
        String orderType = getOrderType();
        ShopService service = new ShopService();
        ShopDAO shopDAO = new ShopDAO();
        listShopTo = new ArrayList<>();
        Shop s = shopDAO.findById(fromShopId);
        switch (orderType){

            case "2NC" :
                mapOrderStatus = new LinkedHashMap<>();
                mapOrderStatus.put("6","Lập");
                mapOrderStatus.put("7","Duyệt");
                mapOrderStatus.put("10","Nhập");
                mapOrderStatus.put("9","Xuất");

                listShopTo = service.findByParentId(s.getShopParentId());
                break;
            case "2":
                listShopTo = service.findByParentId(s.getShopId());
                break;
            default:
                listShopTo = service.getAllShop();
                mapOrderStatus = new LinkedHashMap<>();
                mapOrderStatus.put("1","Lập");
                mapOrderStatus.put("2","Duyệt");
                mapOrderStatus.put("5","Nhập");
                mapOrderStatus.put("4","Xuất");
                break;
        }

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

    public List<Shop> getListShopFrom() {
        return listShopFrom;
    }

    public void setListShopFrom(List<Shop> listShopFrom) {
        this.listShopFrom = listShopFrom;
    }

    public List<Shop> getListShopTo() {
        return listShopTo;
    }

    public void setListShopTo(List<Shop> listShopTo) {
        this.listShopTo = listShopTo;
    }

    public Long getFromShopId() {
        return fromShopId;
    }

    public void setFromShopId(Long fromShopId) {
        this.fromShopId = fromShopId;
    }

    public Long getToShopId() {
        return toShopId;
    }

    public void setToShopId(Long toShopId) {
        this.toShopId = toShopId;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderGroup() {
        return orderGroup;
    }

    public void setOrderGroup(String orderGroup) {
        this.orderGroup = orderGroup;
    }

    public List<InvImExReportBo> getLsData() {
        return lsData;
    }

    public void setLsData(List<InvImExReportBo> lsData) {
        this.lsData = lsData;
    }

    public Map<String, String> getMapOrderType() {
        return mapOrderType;
    }

    public void setMapOrderType(Map<String, String> mapOrderType) {
        this.mapOrderType = mapOrderType;
    }

    public Map<String, String> getMapOrderStatus() {
        return mapOrderStatus;
    }

    public void setMapOrderStatus(Map<String, String> mapOrderStatus) {
        this.mapOrderStatus = mapOrderStatus;
    }

    public Map<String, String> getMapOrderGroup() {
        return mapOrderGroup;
    }

    public void setMapOrderGroup(Map<String, String> mapOrderGroup) {
        this.mapOrderGroup = mapOrderGroup;
    }
}
