package com.ftu.sm.model;

import java.util.*;

/**
 * <p>
 * Title: </p>
 *
 * <p>
 * Description: </p>
 *
 * <p>
 * Copyright: Copyright (c) 2008</p>
 *
 * <p>
 * Company: FPT - IS</p>
 *
 * @author HoaiPN
 * @version 1.0
 */
public class SaleGoodsList {

    public static final String TYPE_TRANS_CUST = "1";  // ban le

    private int transId;
    private List<SaleGoods> saleGoodsList;
    private double totalWithTax;
    private double totalTax;
    private double totalNoTax;
    private double total;
    private double promotion;
    private double discount;
    private SaleCustomer customer;
    private Long staffId;
    private String staffCode;
    private String staffName;
    private String transactionType;
    private String paymentMethod;
    private String shopId;
    private String dateCreated;

    public SaleGoodsList() {
        saleGoodsList = new ArrayList();
    }

    public void addSaleGoods(SaleGoods saleGoods) {
        totalWithTax += saleGoods.getTotalWithTax();//
        totalTax += saleGoods.getTotalTax();
        totalNoTax += saleGoods.getTotalNoTax();
        total += saleGoods.getTotal();
        promotion += saleGoods.getPromotion();
        discount += saleGoods.getDiscount();
        saleGoodsList.add(saleGoods);
    }

    public void append(SaleGoodsList saleGoodsList) {
        if (saleGoodsList == null || saleGoodsList.getSaleGoodsList().size() == 0) {
            return;
        }

        for (SaleGoods saleGoods : saleGoodsList.getSaleGoodsList()) {
            this.addSaleGoods(saleGoods);
        }
    }

    public void removeSaleGoods(SaleGoods _saleGoods) {
        for (int i = saleGoodsList.size() - 1; i >= 0; i--) {
            SaleGoods saleGoods = saleGoodsList.get(i);
            if (saleGoods == _saleGoods) {
                saleGoodsList.remove(i);
            }
        }
    }

    public int getSaleGoodsCount() {
        return saleGoodsList.size();
    }

    public void setSaleGoodsList(List saleGoodsList) {
        this.saleGoodsList = saleGoodsList;
    }

    public void setTotalWithTax(double totalWithTax) {
        this.totalWithTax = totalWithTax;
    }

    public void setTotalTax(double totalTax) {
        this.totalTax = totalTax;
    }

    public void setTotalNoTax(double totalNoTax) {
        this.totalNoTax = totalNoTax;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setPromotion(double promotion) {
        this.promotion = promotion;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setCustomer(SaleCustomer customer) {
        this.customer = customer;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setTransId(int transId) {
        this.transId = transId;
    }

    public List<SaleGoods> getSaleGoodsList() {
        if (saleGoodsList == null) {
            saleGoodsList = new ArrayList();
        }
        return saleGoodsList;
    }

    public double getTotalWithTax() {
        return totalWithTax;
    }

    public double getTotalTax() {
        return totalTax;
    }

    public double getTotalNoTax() {
        return totalNoTax;
    }

    public double getTotal() {
        return total;
    }

    public double getPromotion() {
        return promotion;
    }

    public double getDiscount() {
        return discount;
    }

    public SaleCustomer getCustomer() {
        return customer;
    }

    public Long getStaffId() {
        return staffId;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public String getStaffName() {
        return staffName;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getShopId() {
        return shopId;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public int getTransId() {
        return transId;
    }

    /**
     * How many goods have check serial
     *
     * @return int
     */
    public int countSerials() {
        int count = 0;
        for (SaleGoods saleGoods : saleGoodsList) {
            if (saleGoods.isCheckSerial()) {
                count += saleGoods.getQuantity();
            }
        }
        return count;
    }
}
