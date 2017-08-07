/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.staff.dao;

import com.ftu.staff.bo.Staff;
import com.ftu.admin.consumer.utils.StringUtil;
import com.ftu.hibernate.GenericDAOHibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author E5420
 */
public class StaffDAO extends GenericDAOHibernate<Staff, Long> implements Serializable {

	public StaffDAO() {
		super(Staff.class);
	}

	@Override
	public Staff findById(Long id) {
		Query q = getSession().createQuery("Select s from Staff s where s.staffId = ? and s.staffStatus = ? ");
		q.setParameter(0, id);
		q.setParameter(1, 1L);
		if (q.list().isEmpty()) {
			return null;
		} else {
			return (Staff) q.list().get(0);
		}
	}
	public Staff findByStaffId(Long id) {
		Query q = getSession().createQuery("Select s from Staff s where s.staffId = ? ");
		q.setParameter(0, id);
		if (q.list().isEmpty()) {
			return null;
		} else {
			return (Staff) q.list().get(0);
		}
	}

	public Staff getByUserName(String usName) {
		Query q = getSession().createQuery("Select s from Staff s where lower(s.userName) = ? and s.staffStatus = ? ");
		q.setParameter(0, usName.trim().toLowerCase());
		q.setParameter(1, 1L);
		if (q.list().isEmpty()) {
			return null;
		} else {
			return (Staff) q.list().get(0);
		}
	}
	public Staff getByUserNameAll(String usName) {
		Query q = getSession().createQuery("Select s from Staff s where lower(s.userName) = ? ");
		q.setParameter(0, usName.trim().toLowerCase());
		if (q.list().isEmpty()) {
			return null;
		} else {
			return (Staff) q.list().get(0);
		}

	}
	public List<Staff> findByShopId(Long id) {
		Query q = getSession().createQuery("Select s from Staff s where s.shopId = ? and s.staffStatus = ? ");
		q.setParameter(0, id);
		q.setParameter(1, 1L);
		if (q.list().isEmpty()) {
			return null;
		} else {
			return q.list();
		}
	}
	
	

	public boolean checkUsernameExist(String userName) {
		String sql = "select count(*) from Staff s where s.userName = :userName and s.staffStatus = :status";
		Query q = getSession().createQuery(sql);
		if(userName != null){
			userName = userName.trim();
		}
		q.setParameter("userName", userName);
		q.setParameter(1, 1L);
		Long count = (Long) q.uniqueResult();
		return (count > 0 ? true : false);
	}
	
	public Staff checkUsernameExists(String userName) {
		String sql = "select s from Staff s where s.userName = :userName and s.staffStatus = :status";
		Staff st = null;
		Query q = getSession().createQuery(sql);
		q.setParameter("userName", userName.trim());
		q.setParameter("status", 1L);
		List<Staff> lst = q.list();
		if (!lst.isEmpty()) {
			st = lst.get(0);
		}
		return st;
	}
	
	public Staff checkExists(String code) {
		String sql = "select s from Staff s where UPPER(s.staffCode) = :code";
		Staff st = null;
		Query q = getSession().createQuery(sql);
		q.setParameter("code", code.trim().toUpperCase());
		List<Staff> lst = q.list();
		if (!lst.isEmpty()) {
			st = lst.get(0);
		}
		return st;
	}

	
	public List<Staff> getAllWithParams(Staff st, boolean search,int first,int pageSize, String sortField,Boolean desc) {
		String sql = "select s from Staff s where 1=1 ";
		HashMap<String, Object> mapParams = new HashMap<String, Object>();
		if (st != null) {
			if (st.getShopId() != null) {
				if (st.getListShopId() != null && !st.getListShopId().isEmpty()) {
					st.getListShopId().add(st.getShopId());
					sql += " and s.shopId in (:lshopId) ";
					mapParams.put("lshopId", st.getListShopId());
				} else {
					sql += " and s.shopId IN( select s.shop_id from shop s \n" +
							"START WITH s.SHOP_ID = :shopId \n" +
							"CONNECT BY PRIOR SHOP_ID = SHOP_PARENT_ID) ";
					mapParams.put("shopId", st.getShopId());
				}
			}
			if (!StringUtil.stringIsNullOrEmty(st.getStaffCode())) {
				if (search) {
					sql += " and UPPER(s.staffCode) like :code ";
					mapParams.put("code", "%" + st.getStaffCode().toUpperCase().trim() + "%");
				} else {
					sql += " and s.staffCode = :code ";
					mapParams.put("code", st.getStaffCode());
				}
			}
			if (!StringUtil.stringIsNullOrEmty(st.getUserName())) {
				sql += " and UPPER(s.userName) = :username ";
				mapParams.put("username", st.getUserName().trim().toUpperCase());
			}
			if (!StringUtil.stringIsNullOrEmty(st.getStaffName())) {
				if (search) {
					sql += " and UPPER(s.staffName) like :name ";
					mapParams.put("name", "%" + st.getStaffName().trim().toUpperCase() + "%");
				} else {
					sql += " and UPPER(s.staffName) = :name ";
					mapParams.put("name", st.getStaffName().trim().toUpperCase());
				}
			}
			if (!StringUtil.stringIsNullOrEmty(st.getIdNo())) {
				if (search) {
					sql += " and UPPER(s.idNo) like :idNo ";
					mapParams.put("idNo", "%" + st.getIdNo().trim().toUpperCase() + "%");
				} else {
					sql += " and s.idNo = :idNo ";
					mapParams.put("idNo", st.getIdNo());
				}
			}
			if (!StringUtil.stringIsNullOrEmty(st.getMobiNumber())) {
				if (search) {
					sql += " and s.mobiNumber like :number ";
					mapParams.put("number","%"+ st.getMobiNumber()+"%");
				} else {
					sql += " and s.mobiNumber = :number ";
					mapParams.put("number", st.getMobiNumber());
				}
			}
			if (!StringUtil.stringIsNullOrEmty(st.getEmail())) {
				if(search){
					sql += " and UPPER(s.email) like :email ";
					mapParams.put("email","%"+ st.getEmail().trim().toUpperCase()+"%");
				}else{
					sql += " and UPPER(s.email) = :email ";
					mapParams.put("email", st.getEmail().trim().toUpperCase());
				}
				
			}
			if (st.getDob() != null) {
				sql += " and s.dob = :dob ";
				mapParams.put("dob", st.getDob());
			}
			if (st.getStaffStatus() != null) {
				sql += " and s.staffStatus = :status ";
				mapParams.put("status", st.getStaffStatus());
			}
			if (st.getStartDate() != null) {
				sql += " and s.startDate = :startDate ";
				mapParams.put("startDate", st.getStartDate());
			}
			if (st.getEndDate() != null) {
				sql += " and s.endDate = :endDate ";
				mapParams.put("endDate", st.getEndDate());
			}
			if (!StringUtil.stringIsNullOrEmty(st.getStaffType())) {
				sql += " and s.staffType = :staffType ";
				mapParams.put("staffType", st.getStaffType());
			}
			if (st.getIssueDate() != null) {
				sql += " and s.issueDate = :issueDate ";
				mapParams.put("issueDate", st.getIssueDate());
			}
			if (!StringUtil.stringIsNullOrEmty(st.getIssuePlace())) {
				if(search){
					sql += " and UPPER(s.issuePlace) like :issuePlace ";
					mapParams.put("issuePlace", "%"+st.getIssuePlace().trim().toUpperCase()+"%");
				}else{
					sql += " and s.issuePlace = :issuePlace ";
					mapParams.put("issuePlace", st.getIssuePlace());
				}
			}
			if (!StringUtil.stringIsNullOrEmty(st.getGender())) {
				sql += " and s.gender = :gender ";
				mapParams.put("gender", st.getGender());
			}

		}
		if(sortField != null){
			if(desc){
				sql += " order by UPPER(" + sortField + ") desc ";
			} else{
				sql += " order by UPPER(" +  sortField + ") asc ";
			}
		}else {
			sql += " order by s.staffCode asc ";
		}


		Query q = getSession().createQuery(sql);
		for (String param : mapParams.keySet()) {
			if (mapParams.get(param) instanceof List) {
				q.setParameterList(param, (Collection) mapParams.get(param));
			} else {
				q.setParameter(param, mapParams.get(param));
			}
		}
		
		if (first >= 0) {
			q.setMaxResults(pageSize);
			q.setFirstResult(first);
		}
		
		if (q.list().isEmpty()) {
			return null;
		} else {
			return q.list();
		}

	}

	@Override
	public void saveOrUpdate(Staff g) {
		if (g != null) {
			getSession().merge(g);
		}
		getSession().flush();
	}

	@Override
	public void delete(Staff g) {
		Query q = getSession().createQuery("Delete from Staff where staffId = ?");
		q.setParameter(0, g.getStaffId());
		q.executeUpdate();
		getSession().flush();
	}

	public List<Staff> getAll() {
		Query q = getSession().createQuery("Select s from Staff s ");
		if (q.list().isEmpty()) {
			return null;
		} else
			return q.list();
	}


	public List<Staff> getByTransactionAction() {
		try{
			StringBuilder s = new StringBuilder("SELECT " +
					"st.STAFF_ID, st.STAFF_CODE, st.STAFF_NAME "+
					" FROM STAFF  st JOIN TRANSACTION_ACTION tr ON st.STAFF_ID = tr.ASSESSMENT_STAFF_ID" +
					" WHERE 1 = 1 ");
			s.append(" GROUP BY st.STAFF_ID, st.STAFF_CODE, st.STAFF_NAME  ORDER BY st.STAFF_CODE, st.STAFF_NAME ");
			Query q = getSession().createSQLQuery(s.toString());
			List<Staff> rs = new ArrayList<>();
			List<Object[]> a = q.list();
			for (Object[] o : a) {
				Staff st = new Staff();
					st.setStaffId(o[0] == null ? null : Long.parseLong(o[0].toString()));
					st.setStaffCode(o[1] == null ? null :o[1].toString());
					st.setStaffName(o[2] == null ? null : o[2].toString());
				rs.add(st);
			}
			return rs;
		}catch(Exception ex)
		{
			return new ArrayList<Staff>();
		}
	}
}
