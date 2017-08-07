package com.ftu.sm.service.imp;

import java.io.Serializable;
import java.util.List;

import com.ftu.sm.bo.GoodsPrice;
import com.ftu.sm.dao.GoodsPriceDAO;
import com.ftu.sm.service.GoodsPriceService;

public class GoodsPriceServiceImp implements GoodsPriceService,Serializable {

	@Override
	public void saveOrUpdate(GoodsPrice a) {
		// TODO Auto-generated method stub
		GoodsPriceDAO dao = new GoodsPriceDAO();
		dao.saveOrUpdate(a);
	}

	@Override
	public GoodsPrice findByGoodId(Long goodId) {
		// TODO Auto-generated method stub
		GoodsPriceDAO dao = new GoodsPriceDAO();
		return dao.findByGoodId(goodId);
	}

	@Override
	public List<GoodsPrice> getAllGoods(GoodsPrice g, boolean search, int first, int pageSize) {
		// TODO Auto-generated method stub
		GoodsPriceDAO dao = new GoodsPriceDAO();
		return dao.getAllGoods(g, search, first, pageSize);
	}

	@Override
	public void delete(GoodsPrice gp) {
		// TODO Auto-generated method stub
		GoodsPriceDAO dao = new GoodsPriceDAO();
		dao.delete(gp);
	}

}
