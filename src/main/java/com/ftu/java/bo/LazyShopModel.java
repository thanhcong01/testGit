package com.ftu.java.bo;

import com.ftu.staff.bo.Shop;
import com.ftu.staff.bo.Staff;
import com.ftu.staff.bussiness.StaffService;
import com.ftu.staff.dao.ShopDAO;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.*;

public class LazyShopModel extends LazyDataModel<Shop> implements Serializable {

	private Shop s;
	private boolean search;
	private List<Shop> liShop;
	private boolean firstLoad = false;

	@Override
	public Shop getRowData(String rowKey) {
		if (rowKey != null) {
			for (Shop s : liShop) {
				if (s.getShopId().equals(new Long(rowKey)))
					return s;
			}
		}
		return null;
	}

	public LazyShopModel(Shop s, boolean search, boolean firstLoad) {
		// TODO Auto-generated constructor stub
		this.s = s;
		this.search = search;
		this.firstLoad = firstLoad;
	}

	@Override
	public Object getRowKey(Shop object) {
		// TODO Auto-generated method stub
		return object.getShopId();
	}

	@Override
	public List<Shop> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
		if (firstLoad) {
			first = 0;
			final DataTable d = (DataTable) FacesContext.getCurrentInstance().getViewRoot()
					.findComponent("frm:tblShop");
			d.setFirst(first);
			firstLoad = false;
		}
        ShopDAO shopDao = new ShopDAO();
		int count = shopDao.getListShopByParentId(s, true,-1, -1 , null, null).size();
		boolean desc;
		if(sortOrder.name().equals("DESCENDING")){
			desc = false;
		} else{
			desc = true;
		}

		liShop = shopDao.getListShopByParentId(s, true,first, pageSize, sortField, desc );

//		Collections.sort(liShop, new Comparator(){
//			public int compare(Object o1, Object o2) {
//				Shop p1 = (Shop) o1;
//				Shop p2 = (Shop) o2;
//				return p1.getShopName().compareToIgnoreCase(p2.getShopName());
//			}
//		});
		this.setRowCount(count);
		return liShop;
	}
//	public List<Staff> load(int first, int pageSize, String sortField, SortOrder sortOrder,
//							Map<String, Object> filters) {
//		if (firstLoad) {
//			first = 0;
//			final DataTable d = (DataTable) FacesContext.getCurrentInstance().getViewRoot()
//					.findComponent("frm:tblStaff");
//			d.setFirst(first);
//			firstLoad = false;
//		}
//		liStaffs = service.getAllWithParams(s, search, first, pageSize);
//		int count = 0;
//		if (service.getAllWithParams(s, search, -1, -1) != null) {
//			count = service.getAllWithParams(s, search, -1, -1).size();
//		}
//		this.setRowCount(count);
//		return liStaffs;
//	}
	public LazyShopModel() {
		// TODO Auto-generated constructor stub
	}

}
