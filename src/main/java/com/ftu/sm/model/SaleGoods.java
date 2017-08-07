package com.ftu.sm.model;

import java.util.*;

public class SaleGoods {

    private String goodsCode;// GOODS
    private Long quantity;//ACTION_GOODS_MAP
    private double price;//GOODS_PRICE
    private double promotion;
    private String unit;
    private double discount;
    private String promotionCode;
    private Long priceId;//GOODS_PRICE
    private Long goodsId;//GOODS
    private Long discountId;
    private String goodsSerial;
    private String checkSerial;
    private double vas;
    private List serials;
    private Long stockId;
    private Long stateId;

    public SaleGoods() {
    }

    public SaleGoods(String goodsCode, Long quantity, double price, double promotion,
            String unit,
            double discount,
            String promotionCode,
            Long priceId,
            Long goodsId,
            Long discountId,
            String goodsSerial,
            String checkSerial,
            double vas,
            List serials) {
        this.goodsCode = goodsCode;
        this.quantity = quantity;
        this.price = price;
        this.promotion = promotion;
        this.unit = unit;
        this.discount = discount;
        this.promotionCode = promotionCode;
        this.priceId = priceId;
        this.goodsId = goodsId;
        this.discountId = discountId;
        this.goodsSerial = goodsSerial;
        this.checkSerial = checkSerial;
        this.vas = vas;
        this.serials = serials;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setPromotion(double promotion) {
        this.promotion = promotion;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public void setPriceId(Long priceId) {
        this.priceId = priceId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }

    public void setGoodsSerial(String goodsSerial) {
        this.goodsSerial = goodsSerial;
    }

    public void setCheckSerial(String checkSerial) {
        this.checkSerial = checkSerial;
    }

    public void setVas(double vas) {
        this.vas = vas;
    }

    public void setSerials(List serials) {
        this.serials = serials;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public Long getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getPromotion() {
        return promotion;
    }

    public String getUnit() {
        return unit;
    }

    public double getTotalWithTax() {
        return price * quantity;
    }

    public double getTotalTax() {
        return getTotalWithTax() * (vas / 100) / (vas / 100 + 1);
    }

    public double getTotalNoTax() {
        return getTotalWithTax() - getTotalTax();
    }

    public double getTotal() {
        return getTotalWithTax() - getDiscount() - getPromotion();
    }

    public double getDiscount() {
        return discount;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public Long getPriceId() {
        return priceId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public Long getDiscountId() {
        return discountId;
    }

    public String getGoodsSerial() {
        return goodsSerial;
    }

    public String getCheckSerial() {
        return checkSerial;
    }

    public boolean isCheckSerial() {
        return (checkSerial != null && checkSerial.equals("1"));
    }

    public double getVas() {
        return vas;
    }

    public List getSerials() {
        return serials;
    }

    public Long getStockId() {
        return stockId;
    }

    public Long getStateId() {
        return stateId;
    }
}
