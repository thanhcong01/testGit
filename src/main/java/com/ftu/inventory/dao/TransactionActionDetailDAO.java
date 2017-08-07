/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.dao;

import com.ftu.constanst.InventoryConstanst;
import com.ftu.hibernate.GenericDAOHibernate;
import com.ftu.inventory.bo.TransactionAction;
import com.ftu.inventory.bo.TransactionActionDetail;
import com.ftu.staff.bo.ApDomain;
import com.ftu.utils.DateTimeUtils;
import com.ftu.utils.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.text.StrBuilder;
import org.docx4j.docProps.variantTypes.Array;
import org.hibernate.Query;

/**
 *
 * @author E5420
 */
public class TransactionActionDetailDAO extends GenericDAOHibernate<TransactionActionDetail, Long> {

	public TransactionActionDetailDAO() {
		super(TransactionActionDetail.class);
	}

	@Override
	public void saveOrUpdate(TransactionActionDetail g) {
		if (g != null) {
			super.saveOrUpdate(g);
		}
		getSession().flush();
	}

	public void save(TransactionActionDetail g) {
		if (g != null) {
			getSession().save(g);
		}
		getSession().flush();
	}

	public List<TransactionActionDetail> searchFromShop(String transactionActionCode, Long fromShopId, Date fromCreateDatetime, Date toCreateDatetime,
			List<Long> lsType, List<ApDomain> lsStatus, Integer start, Integer get, String sortField, Boolean desc) {
		StringBuilder s = new StringBuilder(
				"SELECT DISTINCT td.GOODS_ID, td.QUANTITY, t.TRANSACTION_ACTION_CODE transactionActionCode, t.TRANSACTION_TYPE tranType," +
						" td.CREATE_DATETIME, td.PROVIDER_ID,"
				+ " t.TRANSACTION_ACTION_ID, td.TRANSACTION_ACTION_DETAIL_ID, staff.STAFF_NAME, shop.SHOP_NAME TO_SHOP, shop1.SHOP_NAME FROM_SHOP "
						+ ", egr.EQUIPMENT_GROUP_NAME goodsGroupName, eq.PROFILE_CODE goodsCode, eq.PROFILE_NAME goodsName " +
						" , TO_CHAR(td.CREATE_DATETIME,'yyyy-MM-dd') as createDatetime "
						+ "FROM TRANSACTION_ACTION_DETAIL td "
						+ "JOIN TRANSACTION_ACTION t ON td.TRANSACTION_ACTION_ID = t.TRANSACTION_ACTION_ID "
						+ "JOIN EQUIPMENT_PROFILE eq ON td.GOODS_ID = eq.PROFILE_ID "
						+ "JOIN EQUIPMENT_GROUP egr ON egr.EQUIPMENT_GROUP_ID = eq.EQUIPMENT_GROUP_ID "
						+ "JOIN staff staff ON staff.STAFF_ID = t.CREATE_STAFF_ID and staff.SHOP_ID = t.CREATE_SHOP_ID "
						+ "LEFT JOIN shop shop ON shop.SHOP_ID = t.TO_SHOP_ID "
						+ "JOIN shop shop1 ON shop1.SHOP_ID = t.FROM_SHOP_ID "
						+ "WHERE 1=1  ");
		List docParams = new ArrayList();
		if(transactionActionCode!=null && !transactionActionCode.isEmpty()){
			s.append(" AND lower(t.TRANSACTION_ACTION_CODE)  like ? escape '/' ");
			docParams.add(StringUtils.toLikeString(transactionActionCode));
		}
		if (fromShopId != null && fromShopId != 0) {
			s.append("AND t.FROM_SHOP_ID = ?");
			docParams.add(fromShopId);
		}
		if (fromCreateDatetime != null) {
			s.append(" and TO_CHAR(td.CREATE_DATETIME,'yyyy-MM-dd') >= ? ");
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			docParams.add(sf.format(fromCreateDatetime));

		}

		if (toCreateDatetime != null) {
			s.append(" and TO_CHAR(td.CREATE_DATETIME,'yyyy-MM-dd') < ? ");
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			docParams.add(sf.format(DateTimeUtils.addDay(toCreateDatetime, 1)));

		}

		s.append(" and ( ");
		for (int i = 0; i < lsType.size(); i++) {
//			System.out.println(lsType.get(i));
			s.append(" ( t.TRANSACTION_TYPE = ? and ( t.STATUS = ? or t.STATUS = ? or t.STATUS = ? or t.STATUS = ? or t.STATUS = ? or t.STATUS = ? or t.STATUS = ? or t.STATUS = ? ))");
			docParams.add(lsType.get(i));
			docParams.add(InventoryConstanst.TransactionStatus.IM);
			docParams.add(InventoryConstanst.TransactionStatus.EX);
			docParams.add(InventoryConstanst.TransactionStatus.IM_R);
			docParams.add(InventoryConstanst.TransactionStatus.EX_R);

			docParams.add(InventoryConstanst.TransactionStatus.IM_STAFF);
			docParams.add(InventoryConstanst.TransactionStatus.EX_STAFF);
			
			docParams.add(InventoryConstanst.TransactionStatus.IM_WARRANTY);
			docParams.add(InventoryConstanst.TransactionStatus.EX_WARRANTY);
			
			if (i < lsType.size() - 1) {
				s.append(" OR ");
			}
		}
		s.append(" )");
		if(sortField != null){
			if(desc){
				s.append(" order by UPPER(" + sortField + ") desc ");
			} else{
				s.append(" order by UPPER(" + sortField + ")  asc ");
			}
		}else {
			s.append(" ORDER BY td.CREATE_DATETIME DESC");
		}

		Query q = getSession().createSQLQuery(s.toString());
		System.out.println("search from shop: "+s);
		System.out.println("search from shop: "+docParams);
		
		for (int i = 0; i < docParams.size(); i++) {
			q.setParameter(i, docParams.get(i));
		}
		if (start != null && get != null && get > 0) {
			q.setFirstResult(start);
			q.setMaxResults(get);
		}

		List<Object[]> a = q.list();
		List<TransactionActionDetail> rs = new ArrayList<>();
		for (Object[] o : a) {
			rs.add(convertObjectToTransactionActionDetail2(o));
		}
		return rs;
	}

	public Long searchSizeFromShop(String transactionActionCode, Long fromShopId, Date fromCreateDatetime, Date toCreateDatetime, List<Long> lsType,
			List<ApDomain> lsStatus) {
		StringBuilder s = new StringBuilder("SELECT COUNT(1) "
				+ "FROM TRANSACTION_ACTION_DETAIL td "
				+ "JOIN TRANSACTION_ACTION t ON td.TRANSACTION_ACTION_ID = t.TRANSACTION_ACTION_ID "
				+ "JOIN staff staff ON staff.STAFF_ID = t.CREATE_STAFF_ID and staff.SHOP_ID = t.CREATE_SHOP_ID "
				+ "JOIN shop shop ON shop.SHOP_ID = t.FROM_SHOP_ID "
				+ "WHERE 1=1  ");
		List docParams = new ArrayList();
		if(transactionActionCode!=null && !transactionActionCode.isEmpty()){
			s.append(" AND lower(t.TRANSACTION_ACTION_CODE)  like ? escape '/' ");
			docParams.add(StringUtils.toLikeString(transactionActionCode));
		}
		if (fromShopId != null && fromShopId != 0) {
			s.append("AND t.FROM_SHOP_ID = ?");
			docParams.add(fromShopId);
		}
		if (fromCreateDatetime != null) {
			s.append(" and TO_CHAR(td.CREATE_DATETIME,'yyyy-MM-dd') >= ? ");
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			docParams.add(sf.format(fromCreateDatetime));

		}

		if (toCreateDatetime != null) {
			s.append(" and TO_CHAR(td.CREATE_DATETIME,'yyyy-MM-dd') < ? ");
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			docParams.add(sf.format(DateTimeUtils.addDay(toCreateDatetime, 1)));

		}

		s.append(" and ( ");
		for (int i = 0; i < lsType.size(); i++) {

			s.append(" ( t.TRANSACTION_TYPE = ? and ( t.STATUS = ? or t.STATUS = ? or t.STATUS = ? or t.STATUS = ? or t.STATUS = ? or t.STATUS = ? or t.STATUS = ? or t.STATUS = ? ))");
			docParams.add(lsType.get(i));
			docParams.add(InventoryConstanst.TransactionStatus.IM);
			docParams.add(InventoryConstanst.TransactionStatus.EX);
			docParams.add(InventoryConstanst.TransactionStatus.IM_R);
			docParams.add(InventoryConstanst.TransactionStatus.EX_R);


			docParams.add(InventoryConstanst.TransactionStatus.IM_STAFF);
			docParams.add(InventoryConstanst.TransactionStatus.EX_STAFF);
			
			docParams.add(InventoryConstanst.TransactionStatus.IM_WARRANTY);
			docParams.add(InventoryConstanst.TransactionStatus.EX_WARRANTY);
			if (i < lsType.size() - 1) {
				s.append(" OR ");
			}
		}
		s.append(" )");
		Query q = getSession().createSQLQuery(s.toString());
		for (int i = 0; i < docParams.size(); i++) {
			q.setParameter(i, docParams.get(i));
		}
		BigDecimal temp = (BigDecimal) q.uniqueResult();
		if (temp == null) {
			return null;
		}
		return temp.longValue();
	}

	public Long searchSumFromShop(Long fromShopId, Date fromCreateDatetime, Date toCreateDatetime, List<Long> lsType,
			List<ApDomain> lsStatus) {
		StringBuilder s = new StringBuilder("SELECT SUM(td.QUANTITY) "
				+ "FROM TRANSACTION_ACTION_DETAIL td "
				+ "JOIN TRANSACTION_ACTION t ON td.TRANSACTION_ACTION_ID = t.TRANSACTION_ACTION_ID "
				+ "JOIN staff staff ON staff.STAFF_ID = t.CREATE_STAFF_ID and staff.SHOP_ID = t.CREATE_SHOP_ID "
				+ "JOIN shop shop ON shop.SHOP_ID = t.FROM_SHOP_ID "
				+ "WHERE lower(t.TRANSACTION_ACTION_CODE)  like '%' escape '/' ");
		List docParams = new ArrayList();

		if (fromShopId != null && fromShopId != 0) {
			s.append("AND t.FROM_SHOP_ID = ?");
			docParams.add(fromShopId);
		}
		// if(fromCreateDatetime != null){
		// s.append(" and TO_DATE(td.CREATE_DATETIME,'dd/mon/yyyy') > ? ");
		// docParams.add(DateTimeUtils.addDay(fromCreateDatetime, -1));
		// }
		// if(toCreateDatetime != null){
		// s.append(" and TO_DATE(td.CREATE_DATETIME,'dd/mon/yyyy') < ? ");
		// docParams.add(DateTimeUtils.addDay(toCreateDatetime, 1));
		// }
		if (fromCreateDatetime != null) {
			s.append(" and TO_CHAR(td.CREATE_DATETIME,'yyyy-MM-dd') >= ? ");
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			docParams.add(sf.format(fromCreateDatetime));

		}

		if (toCreateDatetime != null) {
			s.append(" and TO_CHAR(td.CREATE_DATETIME,'yyyy-MM-dd') < ? ");
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			docParams.add(sf.format(DateTimeUtils.addDay(toCreateDatetime, 1)));

		}

		s.append(" and ( ");
		for (int i = 0; i < lsType.size(); i++) {

			s.append(" ( t.TRANSACTION_TYPE = ? and ( t.STATUS = ? or t.STATUS = ? or t.STATUS = ? or t.STATUS = ? or  t.STATUS = ? or t.STATUS = ? or t.STATUS = ? or t.STATUS = ? ))");
			docParams.add(lsType.get(i));
			docParams.add(InventoryConstanst.TransactionStatus.IM);
			docParams.add(InventoryConstanst.TransactionStatus.EX);
			docParams.add(InventoryConstanst.TransactionStatus.IM_R);
			docParams.add(InventoryConstanst.TransactionStatus.EX_R);

			docParams.add(InventoryConstanst.TransactionStatus.IM_STAFF);
			docParams.add(InventoryConstanst.TransactionStatus.EX_STAFF);
			
			docParams.add(InventoryConstanst.TransactionStatus.IM_WARRANTY);
			docParams.add(InventoryConstanst.TransactionStatus.EX_WARRANTY);
			if (i < lsType.size() - 1) {
				s.append(" OR ");
			}
		}
		s.append(" )");
//		System.out.println("from: "+s);
		Query q = getSession().createSQLQuery(s.toString());
		for (int i = 0; i < docParams.size(); i++) {
			q.setParameter(i, docParams.get(i));
		}
		BigDecimal temp = (BigDecimal) q.uniqueResult();
		if (temp == null) {
			return null;
		}
		return temp.longValue();
	}

	public Long searchSumToShop(Long toShopId, Date fromCreateDatetime, Date toCreateDatetime, List<Long> lsType,
			List<ApDomain> lsStatus) {
		StringBuilder s = new StringBuilder("SELECT SUM(td.QUANTITY) "
				+ "FROM TRANSACTION_ACTION_DETAIL td "
				+ "JOIN TRANSACTION_ACTION t ON td.TRANSACTION_ACTION_ID = t.TRANSACTION_ACTION_ID "
				+ "JOIN staff staff ON staff.STAFF_ID = t.CREATE_STAFF_ID and staff.SHOP_ID = t.CREATE_SHOP_ID "
				+ "JOIN shop shop ON shop.SHOP_ID = t.TO_SHOP_ID "
				+ "WHERE lower(t.TRANSACTION_ACTION_CODE)  like '%' escape '/' ");
		List docParams = new ArrayList();

		if (toShopId != null && toShopId != 0) {
			s.append("AND t.TO_SHOP_ID = ?");
			docParams.add(toShopId);
		}
		// if(fromCreateDatetime != null){
		// s.append(" and TO_DATE(td.CREATE_DATETIME,'dd/mon/yyyy') > ? ");
		// docParams.add(DateTimeUtils.addDay(fromCreateDatetime, -1));
		// }
		// if(toCreateDatetime != null){
		// s.append(" and TO_DATE(td.CREATE_DATETIME,'dd/mon/yyyy') < ? ");
		// docParams.add(DateTimeUtils.addDay(toCreateDatetime, 1));
		// }
		if (fromCreateDatetime != null) {
			s.append(" and TO_CHAR(td.CREATE_DATETIME,'yyyy-MM-dd') >= ? ");
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			docParams.add(sf.format(fromCreateDatetime));

		}

		if (toCreateDatetime != null) {
			s.append(" and TO_CHAR(td.CREATE_DATETIME,'yyyy-MM-dd') < ? ");
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			docParams.add(sf.format(DateTimeUtils.addDay(toCreateDatetime, 1)));

		}

		s.append(" and ( ");
		for (int i = 0; i < lsType.size(); i++) {
			String strTypeId = "" + lsType.get(i);
			s.append(" ( t.TRANSACTION_TYPE = ? ");
			
			docParams.add(Long.parseLong(strTypeId));
			
			if (strTypeId.equals(InventoryConstanst.TransactionType.IM)) {
				s.append("and t.STATUS in ( 2 ) ) ");
			} else if (strTypeId.equals(InventoryConstanst.TransactionType.IM_WARRANTY)) {
				s.append("and t.STATUS in ( 20 ) ) ");
			}  
			else /*if (strTypeId.equals(InventoryConstanst.TransactionType.INSTANT)) */{
				s.append("and t.STATUS in ( 5, 10 ) ) ");
			}

			if (i < lsType.size() - 1) {
				s.append(" OR ");
			}
		}
		s.append(" )");
//		System.out.println("to: "+s);
		Query q = getSession().createSQLQuery(s.toString());
		for (int i = 0; i < docParams.size(); i++) {
			q.setParameter(i, docParams.get(i));
		}
		BigDecimal temp = (BigDecimal) q.uniqueResult();
		if (temp == null) {
			return null;
		}
		return temp.longValue();
	}

	public Long searchSizeToShop(String transactionActionCode, Long toShopId, Date fromCreateDatetime, Date toCreateDatetime, List<Long> lsType,
			List<ApDomain> lsStatus) {
		StringBuilder s = new StringBuilder("SELECT COUNT(1) "
				+ "FROM TRANSACTION_ACTION_DETAIL td "
				+ "JOIN TRANSACTION_ACTION t ON td.TRANSACTION_ACTION_ID = t.TRANSACTION_ACTION_ID "
				+ "JOIN staff staff ON staff.STAFF_ID = t.CREATE_STAFF_ID and staff.SHOP_ID = t.CREATE_SHOP_ID "
				+ "JOIN shop shop ON shop.SHOP_ID = t.TO_SHOP_ID "
				+ "WHERE 1= 1 ");
		List docParams = new ArrayList();
		if(transactionActionCode!=null && !transactionActionCode.isEmpty()){
			s.append(" AND lower(t.TRANSACTION_ACTION_CODE)  like ? escape '/' ");
			docParams.add(StringUtils.toLikeString(transactionActionCode));
		}
		if (toShopId != null && toShopId != 0) {
			s.append("AND t.TO_SHOP_ID = ?");
			docParams.add(toShopId);
		}
		if (fromCreateDatetime != null) {
			s.append(" and TO_CHAR(td.CREATE_DATETIME,'yyyy-MM-dd') >= ? ");
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			docParams.add(sf.format(fromCreateDatetime));

		}

		if (toCreateDatetime != null) {
			s.append(" and TO_CHAR(td.CREATE_DATETIME,'yyyy-MM-dd') < ? ");
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			docParams.add(sf.format(DateTimeUtils.addDay(toCreateDatetime, 1)));

		}

		s.append(" and ( ");
		for (int i = 0; i < lsType.size(); i++) {
			String strTypeId = "" + lsType.get(i);
			s.append(" ( t.TRANSACTION_TYPE = ? ");//and t.STATUS = ? ) ");
			docParams.add(Long.parseLong(strTypeId));
			if (strTypeId.equals(InventoryConstanst.TransactionType.IM)) {
				s.append("and t.STATUS in ( 2 ) ) ");
			} else if (strTypeId.equals(InventoryConstanst.TransactionType.IM_WARRANTY)) {
				s.append("and t.STATUS in ( 20 ) ) ");
			}  
			else /*if (strTypeId.equals(InventoryConstanst.TransactionType.INSTANT)) */{
				s.append("and t.STATUS in ( 5, 10, 15 ) ) ");
			}

			if (i < lsType.size() - 1) {
				s.append(" OR ");
			}
		}
		s.append(" )");

		Query q = getSession().createSQLQuery(s.toString());
		for (int i = 0; i < docParams.size(); i++) {
			q.setParameter(i, docParams.get(i));
		}
		BigDecimal temp = (BigDecimal) q.uniqueResult();
		if (temp == null) {
			return null;
		}
		return temp.longValue();
	}

	public List<TransactionActionDetail> searchToShop(String transactionActionCode, Long toShopId, Date fromCreateDatetime, Date toCreateDatetime,
			List<Long> lsType, List<ApDomain> lsStatus, Integer start, Integer get, String sortField, Boolean desc) {
		StringBuilder s = new StringBuilder(
				"SELECT DISTINCT td.GOODS_ID, td.QUANTITY quantity, t.TRANSACTION_ACTION_CODE transactionActionCode, t.TRANSACTION_TYPE tranType, " +
						" td.CREATE_DATETIME , td.PROVIDER_ID,"
				+ " t.TRANSACTION_ACTION_ID, td.TRANSACTION_ACTION_DETAIL_ID, staff.STAFF_NAME, shop.SHOP_NAME TO_SHOP, shop1.SHOP_NAME FROM_SHOP "
						+ ", egr.EQUIPMENT_GROUP_NAME goodsGroupName, eq.PROFILE_CODE goodsCode, eq.PROFILE_NAME goodsName " +
						" , TO_CHAR(td.CREATE_DATETIME,'yyyy-MM-dd') as createDatetime "
						+ "FROM TRANSACTION_ACTION_DETAIL td "
						+ "JOIN TRANSACTION_ACTION t ON td.TRANSACTION_ACTION_ID = t.TRANSACTION_ACTION_ID "
						+ "JOIN EQUIPMENT_PROFILE eq ON td.GOODS_ID = eq.PROFILE_ID "
						+ "JOIN EQUIPMENT_GROUP egr ON egr.EQUIPMENT_GROUP_ID = eq.EQUIPMENT_GROUP_ID "
						+ "JOIN staff staff ON staff.STAFF_ID = t.CREATE_STAFF_ID and staff.SHOP_ID = t.CREATE_SHOP_ID "
						+ "JOIN shop shop ON shop.SHOP_ID = t.TO_SHOP_ID "
						+ "left JOIN shop shop1 ON shop1.SHOP_ID = t.FROM_SHOP_ID "
						+ "WHERE 1=1 ");
		List docParams = new ArrayList();
		if(transactionActionCode!=null && !transactionActionCode.isEmpty()){
			s.append(" AND lower(t.TRANSACTION_ACTION_CODE)  like ? escape '/' ");
			docParams.add(StringUtils.toLikeString(transactionActionCode));
		}

		if (toShopId != null && toShopId != 0) {
			s.append(" AND t.TO_SHOP_ID = ?");
			docParams.add(toShopId);
		}
		// if(fromCreateDatetime != null){
		// s.append(" and TO_DATE(td.CREATE_DATETIME,'dd/mon/yyyy') > ? ");
		// docParams.add(DateTimeUtils.addDay(fromCreateDatetime, -1));
		// }
		// if(toCreateDatetime != null){
		// s.append(" and TO_DATE(td.CREATE_DATETIME,'dd/mon/yyyy') < ? ");
		// docParams.add(DateTimeUtils.addDay(toCreateDatetime, 1));
		// }
		if (fromCreateDatetime != null) {
			s.append(" and TO_CHAR(td.CREATE_DATETIME,'yyyy-MM-dd') >= ? ");
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			docParams.add(sf.format(fromCreateDatetime));

		}

		if (toCreateDatetime != null) {
			s.append(" and TO_CHAR(td.CREATE_DATETIME,'yyyy-MM-dd') < ? ");
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			docParams.add(sf.format(DateTimeUtils.addDay(toCreateDatetime, 1)));

		}

		s.append(" and ( ");
		for (int i = 0; i < lsType.size(); i++) {
			String strTypeId = "" + lsType.get(i);
			s.append(" ( t.TRANSACTION_TYPE = ? ");//and t.STATUS in (5,10) )");//and t.STATUS = ? ) ");
			docParams.add(Long.parseLong(strTypeId));
			if (strTypeId.equals(InventoryConstanst.TransactionType.IM)) {
				s.append("and t.STATUS in ( 2 ) ) ");
			} else if (strTypeId.equals(InventoryConstanst.TransactionType.IM_WARRANTY)) {
				s.append("and t.STATUS in ( 20 ) ) ");
			}
			else /*if (strTypeId.equals(InventoryConstanst.TransactionType.INSTANT)) */{
				s.append("and t.STATUS in ( 5, 10, 15 ) ) ");
			}

			if (i < lsType.size() - 1) {
				s.append(" OR ");
			}
		}
		s.append(" )");
		if(sortField != null){
			if(desc){
				s.append(" order by UPPER(" + sortField + ") desc ");
			} else{
				s.append(" order by UPPER(" + sortField + ")  asc ");
			}
		}else {
			s.append(" ORDER BY td.CREATE_DATETIME DESC");
		}
		System.out.println(s.toString());
		Query q = getSession().createSQLQuery(s.toString());
		for (int i = 0; i < docParams.size(); i++) {
			q.setParameter(i, docParams.get(i));

		}
		System.out.println(docParams);
		if (start != null && get != null && get > 0) {
			q.setFirstResult(start);
			q.setMaxResults(get);
		}

		List<Object[]> a = q.list();
		List<TransactionActionDetail> rs = new ArrayList<>();
		for (Object[] o : a) {
			rs.add(convertObjectToTransactionActionDetail2(o));
		}
		return rs;
	}

	private TransactionActionDetail convertObjectToTransactionActionDetail(Object[] o) {
		TransactionActionDetail rs = new TransactionActionDetail();
		rs.setGoodsId(o[0] == null ? null : Long.parseLong(o[0].toString()));
		rs.setQuantity(o[1] == null ? null : Long.parseLong(o[1].toString()));
		rs.setTransactionAction(new TransactionAction());
		rs.getTransactionAction().setTransactionActionCode(o[2] == null ? null : o[2].toString());
		rs.getTransactionAction().setTransactionType(o[3] == null ? null : o[3].toString());
		rs.setCreateDatetime(o[4] == null ? null : (Date) o[4]);
		rs.setProviderId(o[5] == null ? null : Long.parseLong(o[5].toString()));
		rs.setTransactionActionId(o[6] == null ? null : Long.parseLong(o[6].toString()));
		rs.setTransactionActionDetailId(o[7] == null ? null : Long.parseLong(o[7].toString()));
		rs.setStaffName(o[8] == null ? null : o[8].toString());
		rs.setFromShop(o[10] == null ? null : o[10].toString());
		rs.setGoodsGroupName(o[11] == null ? null : o[11].toString());
		rs.setGoodsCode(o[12] == null ? null : o[12].toString());
		rs.setGoodsName(o[13] == null ? null : o[13].toString());
		return rs;
	}
	private TransactionActionDetail convertObjectToTransactionActionDetail2(Object[] o) {
		TransactionActionDetail rs = new TransactionActionDetail();
		rs.setGoodsId(o[0] == null ? null : Long.parseLong(o[0].toString()));
		rs.setQuantity(o[1] == null ? null : Long.parseLong(o[1].toString()));
		rs.setTransactionAction(new TransactionAction());
		rs.getTransactionAction().setTransactionActionCode(o[2] == null ? null : o[2].toString());
		rs.getTransactionAction().setTransactionType(o[3] == null ? null : o[3].toString());
		rs.setCreateDatetime(o[4] == null ? null : (Date) o[4]);
		rs.setProviderId(o[5] == null ? null : Long.parseLong(o[5].toString()));
		rs.setTransactionActionId(o[6] == null ? null : Long.parseLong(o[6].toString()));
		rs.setTransactionActionDetailId(o[7] == null ? null : Long.parseLong(o[7].toString()));
		rs.setStaffName(o[8] == null ? null : o[8].toString());
		rs.setFromShop(o[10] == null ? null : o[10].toString());
		rs.setGoodsGroupName(o[11] == null ? null : o[11].toString());
		rs.setGoodsCode(o[12] == null ? null : o[12].toString());
		rs.setGoodsName(o[13] == null ? null : o[13].toString());
		rs.setStrCreateDatetimeSord(o[14] == null ? null : o[14].toString());
		return rs;
	}

	public List<TransactionActionDetail> getTransactionActionDetails(Long taId) {
		Query q = getSession().createQuery("Select t from TransactionActionDetail t where transactionActionId = ? ");
		q.setParameter(0, taId);
		if (q.list().isEmpty()) {
			return null;
		} else {
			return q.list();
		}
	}

	public List<TransactionActionDetail> getTransactionActionDetailByTransactionActionId(Long taId) {
		Query q = getSession().createQuery("select t from TransactionActionDetail t WHERE transactionActionId = ?");
		q.setParameter(0, taId);
		if (q.list() == null) {
			return null;
		}

		return q.list();
	}

	@Override
	public void delete(TransactionActionDetail g) {
		super.delete(g);
		getSession().flush();
	}

	public void deleteByTaId(Long taId) {
		Query q1 = getSession().createQuery("Delete from TransactionActionDetail where transactionActionId = ? ");
		q1.setParameter(0, taId);
		q1.executeUpdate();
		getSession().flush();
	}
}
