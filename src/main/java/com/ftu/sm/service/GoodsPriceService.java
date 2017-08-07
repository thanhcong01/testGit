package com.ftu.sm.service;

import java.util.List;

import com.ftu.sm.bo.GoodsPrice;

public interface GoodsPriceService {
	public void saveOrUpdate(GoodsPrice a) ;
	public GoodsPrice findByGoodId(Long goodId) ;
	public List<GoodsPrice> getAllGoods(GoodsPrice g, boolean search,int first,int pageSize);
	public void delete(GoodsPrice gp);
}
