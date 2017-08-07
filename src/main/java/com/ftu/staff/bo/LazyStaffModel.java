package com.ftu.staff.bo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.ftu.staff.bussiness.StaffService;

public class LazyStaffModel extends LazyDataModel<Staff> implements Serializable {
	private StaffService service = new StaffService();
	private Staff s;
	private boolean search;
	private List<Staff> liStaffs;
	private boolean firstLoad = false;

	@Override
	public Staff getRowData(String rowKey) {
		if (rowKey != null) {
			for (Staff s : liStaffs) {
				if (s.getStaffId().equals(new Long(rowKey)))
					return s;
			}
		}
		return null;
	}

	public LazyStaffModel(Staff s, boolean search, boolean firstLoad) {
		// TODO Auto-generated constructor stub
		this.s = s;
		this.search = search;
		this.firstLoad = firstLoad;
	}

	@Override
	public Object getRowKey(Staff object) {
		// TODO Auto-generated method stub
		return object.getStaffId();
	}

	@Override
	public List<Staff> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
		if (firstLoad) {
			first = 0;
			final DataTable d = (DataTable) FacesContext.getCurrentInstance().getViewRoot()
					.findComponent("frm:tblStaff");
			d.setFirst(first);
			firstLoad = false;
		}
		boolean desc;
		if(sortOrder.name().equals("DESCENDING")){
			desc = false;
		} else{
			desc = true;
		}

		liStaffs = service.getAllWithParams(s, search, first, pageSize, sortField, desc);
		int count = 0;
		if (liStaffs != null) {
			count = service.getAllWithParams(s, search, -1, -1, null, null).size();
		}
		this.setRowCount(count);
		return liStaffs;
	}

	public LazyStaffModel() {
		// TODO Auto-generated constructor stub
	}

}
