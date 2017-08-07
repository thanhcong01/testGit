package com.ftu.staff.bo;

import com.ftu.staff.bussiness.PositionService;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class LazyPositionModel extends LazyDataModel<Position> implements Serializable {
	private PositionService service = new PositionService();
	private Position s;
	private boolean search;
	private List<Position> liPositions;
	private boolean firstLoad = false;

	@Override
	public Position getRowData(String rowKey) {
		if (rowKey != null) {
			for (Position s : liPositions) {
				if (s.getPositionId().equals(new Long(rowKey)))
					return s;
			}
		}
		return null;
	}

	public LazyPositionModel(Position s, boolean search, boolean firstLoad) {
		// TODO Auto-generated constructor stub
		this.s = s;
		this.search = search;
		this.firstLoad = firstLoad;
	}

	@Override
	public Object getRowKey(Position object) {
		// TODO Auto-generated method stub
		return object.getPositionId();
	}

	@Override
	public List<Position> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
		if (firstLoad) {
			first = 0;
			final DataTable d = (DataTable) FacesContext.getCurrentInstance().getViewRoot()
					.findComponent("frm:tblPosition");
			d.setFirst(first);
			firstLoad = false;
		}
		boolean desc;
		if(sortOrder.name().equals("DESCENDING")){
			desc = false;
		} else{
			desc = true;
		}

		liPositions = service.getAllWithParams(s, search, first, pageSize, sortField, desc);
		int count = 0;
		if (liPositions != null) {

			count = service.getAllWithParams(s, search, -1, -1, null, null).size();
		}
		this.setRowCount(count);
		return liPositions;
	}

	public LazyPositionModel() {
		// TODO Auto-generated constructor stub
	}

}
