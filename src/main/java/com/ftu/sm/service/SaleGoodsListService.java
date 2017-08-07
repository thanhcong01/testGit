package com.ftu.sm.service;

import com.ftu.exception.AppException;
import com.ftu.sm.model.SaleCustomer;
import com.ftu.sm.model.SaleGoodsList;
import java.util.List;

public interface SaleGoodsListService {

    public Double initList(String actionCode, List<String> serials) throws AppException;

    //public Long genTrans(String actionCode, List<String> serials, SaleCustomer cust) throws AppException;
    
    public SaleCustomer getCustomer(String customerType, String customerName, String address, String textCode, String company);
}
