package com.ftu.sm.service.imp;

import com.ftu.constanst.InventoryConstanst;
import com.ftu.exception.AppException;
import com.ftu.inventory.bo.*;
import com.ftu.inventory.bo.StockGoodsInvSerial;
import com.ftu.inventory.dao.EquipmentsProfileDAO;
import com.ftu.inventory.dao.StockGoodsDAO;
import com.ftu.inventory.dao.StockGoodsInvSerialDAO;
import com.ftu.inventory.dao.StockTransactionDAO;
import com.ftu.inventory.dao.StockTransactionDetailDAO;
import com.ftu.inventory.dao.StockTransactionSerialDAO;
import com.ftu.inventory.dao.TransactionActionDAO;
import com.ftu.inventory.dao.TransactionActionDetailDAO;
import com.ftu.sm.bo.ActionGoodsMap;
import com.ftu.sm.bo.GoodsPrice;
import com.ftu.sm.bo.TransactionEntity;
import com.ftu.sm.bo.TransactionDetail;
import com.ftu.sm.model.SaleCustomer;
import com.ftu.sm.model.SaleGoodsList;
import com.ftu.sm.service.SaleGoodsListService;
import com.ftu.sm.dao.*;
import com.ftu.sm.model.SaleGoods;
import com.ftu.staff.bo.Shop;
import com.ftu.staff.bo.Staff;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SaleGoodsListServiceImp implements SaleGoodsListService {

    List<TransactionActionDetail> lsTAD = new ArrayList<>();
    List<StockTransactionDetail> lsSTD = new ArrayList<>();
    Shop shop;
    Staff staff;

    public SaleGoodsListServiceImp() {
    }

    public SaleGoodsListServiceImp(Staff staff, Shop shop) {
        this.staff = staff;
        this.shop = shop;
    }

    @Override
    public Double initList(String actionCode, List<String> serials) throws AppException {// return total with tax;

        SaleGoodsList saleGoodsList = new SaleGoodsList();
        saleGoodsList = getSaleGoodsList(actionCode, serials);
        return saleGoodsList.getTotalWithTax();

    }

    @Override
    public SaleCustomer getCustomer(String customerType, String customerName, String address, String textCode, String company) {
        SaleCustomer a = new SaleCustomer(customerType, customerName, address, textCode, company);
        return a;
    }

   
    private boolean check() {
        return staff != null && shop != null;
    }

    private TransactionActionDetail getTransDetailFromList(Long gid, Long prvId) {

        for (TransactionActionDetail tad : lsTAD) {
            if (tad.getGoodsId().equals(gid) && tad.getProviderId().equals(prvId)) {
                return tad;
            }

        }
        TransactionActionDetail tad = new TransactionActionDetail(gid, prvId);
        lsTAD.add(tad);
        return tad;
    }

    private StockTransactionDetail getStockDetailFromList(Long gid, Long gstatus) {

        for (StockTransactionDetail std : lsSTD) {
            if (std.getGoodsId().equals(gid) && std.getGoodsStatus().equals(gstatus)) {
                return std;
            }

        }
        StockTransactionDetail std = new StockTransactionDetail(0L, gid, gstatus);
        lsSTD.add(std);
        return std;
    }

    public SaleGoodsList getSaleGoodsList(String actionCode, List<String> serials) throws AppException {

        ActionGoodsMapDAO actionGoodsMapDAO = new ActionGoodsMapDAO();
        List<ActionGoodsMap> listAGM = actionGoodsMapDAO.findByActionCode(actionCode);
        GoodsPriceDAO goodsPriceDAO = new GoodsPriceDAO();
        EquipmentsProfileDAO goodsDAO = new EquipmentsProfileDAO();
        SaleGoods saleGoods;
        SaleGoodsList saleGoodsList = new SaleGoodsList();
        if (listAGM != null) {
            for (int i = 0; i < listAGM.size(); i++) {
                ActionGoodsMap am = listAGM.get(i);
                EquipmentsProfile g = goodsDAO.findByProfileId(am.getGoodsId());
                GoodsPrice gp = goodsPriceDAO.findByGoodId(am.getGoodsId());
                saleGoods = new SaleGoods();
                saleGoods.setGoodsId(am.getGoodsId());
                saleGoods.setGoodsCode(g.getProfileCode());
                saleGoods.setQuantity(am.getQuantity());
                saleGoods.setPrice(gp.getPrice());
                saleGoods.setPriceId(gp.getPriceId());

                saleGoodsList.addSaleGoods(saleGoods);

            }
        }

        saleGoodsList.setStaffId(staff.getStaffId());
        saleGoodsList.setStaffCode(staff.getStaffCode());
        saleGoodsList.setStaffName(staff.getStaffName());
        saleGoodsList.setShopId(String.valueOf(shop.getShopId()));

        return saleGoodsList;
    }

}
