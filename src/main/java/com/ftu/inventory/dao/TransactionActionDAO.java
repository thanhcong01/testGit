/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.dao;

import com.ftu.constanst.InventoryConstanst;
import com.ftu.hibernate.GenericDAOHibernate;
import com.ftu.inventory.bo.GoodsStatusTransSerial;
import com.ftu.inventory.bo.TransactionAction;
import com.ftu.inventory.bo.TransactionActionDetail;
import com.ftu.staff.bo.ApDomain;
import com.ftu.utils.DateTimeUtils;
import com.ftu.utils.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.hibernate.Query;
import org.hibernate.SQLQuery;

/**
 *
 * @author E5420
 */
public class TransactionActionDAO extends GenericDAOHibernate<TransactionAction, Long> {

	public TransactionActionDAO() {
		super(TransactionAction.class);
	}

	@Override
	public TransactionAction findById(Long id) {
		Query q = getSession().getNamedQuery("TransactionAction.findByTransactionActionId");
		q.setParameter("transactionActionId", id);
		if (q.list().isEmpty()) {
			return null;
		} else {
			return (TransactionAction) q.list().get(0);
		}
	}

	@Override
	public void saveOrUpdate(TransactionAction g) {
		if (g != null) {
			super.saveOrUpdate(g);
		}
		getSession().flush();
	}

	public void delete(Long taId) {
		Query q = getSession().createQuery("Delete from TransactionAction where transactionActionId = ?");
		q.setParameter(0, taId);
		q.executeUpdate();
		getSession().flush();
	}

	public Long getCount(String type) {
		Query q = getSession().createQuery("Select count(1) from TransactionAction where transactionType = ? ");
		q.setParameter(0, type);
		return (Long) q.uniqueResult();
	}

	public List<TransactionAction> search(TransactionAction ta, List<ApDomain> lsType, List<ApDomain> lsStatus,
			Integer start, Integer get) {
		try {
			StringBuilder s = new StringBuilder(
					"Select t from TransactionAction t where lower(transactionActionCode)  like ? escape '/' ");
			List docParams = new ArrayList();
			SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
			if (ta.getTransactionType() != null && !ta.getTransactionType().trim().isEmpty()
					&& !ta.getTransactionType().trim().equals("0")) {
				s.append(" and transactionType = ? ");
				docParams.add(ta.getTransactionType());
			} else {
				if (!lsType.isEmpty()) {
					s.append(" and transactionType in ( ");
					for (int i = 0; i < lsType.size(); i++) {
						if (i != lsType.size() - 1) {
							s.append("?,");
							docParams.add(lsType.get(i).getValue().toString());
						} else {
							s.append("?");
							docParams.add(lsType.get(i).getValue().toString());

						}
					}
					s.append(")");
				} else {
					return new ArrayList<TransactionAction>();
				}
			}
			if (ta.getCreateDatetime() != null) {
				String date = sf.format(ta.getCreateDatetime());
				s.append(" and TO_CHAR(CREATE_DATETIME,'dd-MM-yyyy') = ? ");
				docParams.add(date);
			} else {
				if (ta.getFromDate() != null) {
					s.append(" and createDatetime >= ? ");
					docParams.add(ta.getFromDate());
				}
				if (ta.getToDate() != null) {
					s.append(" and createDatetime < ? ");
					docParams.add(DateTimeUtils.addDay(ta.getToDate(), 1));
				}
			}
			if (ta.getAssessmentStaffId() != null && ta.getAssessmentStaffId() > 0) {
				s.append(" and assessmentStaffId = ? ");
				docParams.add(ta.getAssessmentStaffId());
			}
			if (ta.getAssessmentDatetime() != null) {
				String date = sf.format(ta.getAssessmentDatetime());
				s.append(" and TO_CHAR(ASSESSMENT_DATETIME,'dd-MM-yyyy') = ? ");
				docParams.add(date);
			}
			if (ta.getCreateStaffId() != null && ta.getCreateStaffId() > 0) {
				s.append(" and createStaffId = ? ");
				docParams.add(ta.getCreateStaffId());
			}
			if (ta.getCreateShopId() != null && ta.getCreateShopId() > 0) {
				s.append(" and createShopId = ? ");
				docParams.add(ta.getCreateShopId());
			}
			if (ta.getFromShopId() != null && ta.getFromShopId() > 0) {
				s.append(" and fromShopId = ? ");
				docParams.add(ta.getFromShopId());
			}
			if (ta.getReasonId() != null && ta.getReasonId() > 0) {
				s.append(" and reasonId = ? ");
				docParams.add(ta.getReasonId());
			}
			if (ta.getToShopId() != null && ta.getToShopId() > 0) {
				s.append(" and toShopId = ? ");
				docParams.add(ta.getToShopId());
			}
			if (ta.getStatus() != null && !ta.getStatus().trim().isEmpty() && !ta.getStatus().trim().equals("0")) {
				s.append(" and status = ? ");
				docParams.add(ta.getStatus());
			} else {
				if (!lsStatus.isEmpty()) {
					s.append(" and status in ( ");
					for (int i = 0; i < lsStatus.size(); i++) {
						if (i != lsStatus.size() - 1) {
							s.append("?,");
							docParams.add(lsStatus.get(i).getValue().toString());
						} else {
							s.append("?");
							docParams.add(lsStatus.get(i).getValue().toString());

						}
					}
					s.append(")");
				} else {
					return new ArrayList<TransactionAction>();
				}
			}
			s.append(" order by createDatetime");
			Query q = getSession().createQuery(s.toString());
			String code = ta.getTransactionActionCode() == null ? "" : ta.getTransactionActionCode();
			q.setParameter(0, StringUtils.toLikeString(code));
			for (int i = 0; i < docParams.size(); i++) {
				q.setParameter(i + 1, docParams.get(i));
			}
			if (start != null && get != null && get > 0) {
				q.setFirstResult(start);
				q.setMaxResults(get);
			}
			if (q.list().isEmpty()) {
				return new ArrayList<TransactionAction>();
			} else {
				return q.list();
			}
		} catch (Exception ex) {
			return new ArrayList<TransactionAction>();
		}
	}

	public List<TransactionAction> searchAndSort(TransactionAction ta, List<ApDomain> lsType, List<ApDomain> lsStatus,
			Integer start, Integer get, boolean desc, String sortField) {
		try {
			StringBuilder s = new StringBuilder(
					"Select t from TransactionAction t where lower(transactionActionCode)  like ? escape '/' ");
			List docParams = new ArrayList();
			SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
			if (ta.getTransactionType() != null && !ta.getTransactionType().trim().isEmpty()
					&& !ta.getTransactionType().trim().equals("0")) {
				s.append(" and transactionType = ? ");
				docParams.add(ta.getTransactionType());
			} else {
				if (!lsType.isEmpty()) {
					s.append(" and transactionType in ( ");
					for (int i = 0; i < lsType.size(); i++) {
						if (i != lsType.size() - 1) {
							s.append("?,");
							docParams.add(lsType.get(i).getValue().toString());
						} else {
							s.append("?");
							docParams.add(lsType.get(i).getValue().toString());

						}
					}
					s.append(")");
				} else {
					return new ArrayList<TransactionAction>();
				}
			}
			if (ta.getCreateDatetime() != null) {
				String date = sf.format(ta.getCreateDatetime());
				s.append(" and TO_CHAR(CREATE_DATETIME,'dd-MM-yyyy') = ? ");
				docParams.add(date);
			} else {
				if (ta.getFromDate() != null) {
					s.append(" and createDatetime >= ? ");
					docParams.add(ta.getFromDate());
				}
				if (ta.getToDate() != null) {
					s.append(" and createDatetime < ? ");
					docParams.add(DateTimeUtils.addDay(ta.getToDate(), 1));
				}
			}
			if (ta.getAssessmentStaffId() != null && ta.getAssessmentStaffId() > 0) {
				s.append(" and assessmentStaffId = ? ");
				docParams.add(ta.getAssessmentStaffId());
			}
			if (ta.getAssessmentDatetime() != null) {
				String date = sf.format(ta.getAssessmentDatetime());
				s.append(" and TO_CHAR(ASSESSMENT_DATETIME,'dd-MM-yyyy') = ? ");
				docParams.add(date);
			}
			if (ta.getCreateStaffId() != null && ta.getCreateStaffId() > 0) {
				s.append(" and createStaffId = ? ");
				docParams.add(ta.getCreateStaffId());
			}
			if (ta.getCreateShopId() != null && ta.getCreateShopId() > 0) {
				s.append(" and createShopId = ? ");
				docParams.add(ta.getCreateShopId());
			}
			if (ta.getFromShopId() != null && ta.getFromShopId() > 0) {
				s.append(" and fromShopId = ? ");
				docParams.add(ta.getFromShopId());
			}
			if (ta.getReasonId() != null && ta.getReasonId() > 0) {
				s.append(" and reasonId = ? ");
				docParams.add(ta.getReasonId());
			}
			if (ta.getToShopId() != null && ta.getToShopId() > 0) {
				s.append(" and toShopId = ? ");
				docParams.add(ta.getToShopId());
			}
			if (ta.getStatus() != null && !ta.getStatus().trim().isEmpty() && !ta.getStatus().trim().equals("0")) {
				s.append(" and status = ? ");
				docParams.add(ta.getStatus());
			} else {
				if (!lsStatus.isEmpty()) {
					s.append(" and status in ( ");
					for (int i = 0; i < lsStatus.size(); i++) {
						if (i != lsStatus.size() - 1) {
							s.append("?,");
							docParams.add(lsStatus.get(i).getValue().toString());
						} else {
							s.append("?");
							docParams.add(lsStatus.get(i).getValue().toString());

						}
					}
					s.append(")");
				} else {
					return new ArrayList<TransactionAction>();
				}
			}
			if(sortField != null){
				if(desc){
					s.append(" order by UPPER(" + sortField + ") desc ");
				} else{
					s.append(" order by UPPER(" + sortField + ")  asc ");
				}
			}
			Query q = getSession().createQuery(s.toString());
			String code = ta.getTransactionActionCode() == null ? "" : ta.getTransactionActionCode();
			q.setParameter(0, StringUtils.toLikeString(code));
			for (int i = 0; i < docParams.size(); i++) {
				q.setParameter(i + 1, docParams.get(i));
			}
			if (start != null && get != null && get > 0) {
				q.setFirstResult(start);
				q.setMaxResults(get);
			}
			if (q.list().isEmpty()) {
				return new ArrayList<TransactionAction>();
			} else {
				return q.list();
			}
		} catch (Exception ex) {
			return new ArrayList<TransactionAction>();
		}
	}

	public List<TransactionAction> findAllJoinEqDetail() {
		try {
			StringBuilder s = new StringBuilder(
					"Select t from TransactionAction t where t.transactionActionId " +
					"in(Select d.imTransactionId from EquipmentsDetail d) order by t.transactionActionCode ");
			Query q = getSession().createQuery(s.toString());
			if (q.list().isEmpty()) {
				return new ArrayList<TransactionAction>();
			} else {
				return q.list();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ArrayList<TransactionAction>();
		}
	}
	public List<TransactionAction> findAllByType(String type) {
		try {
			List docParams = new ArrayList();
			StringBuilder s = new StringBuilder(
					"Select t from TransactionAction t where 1 =1  ");
//			if(type!=null){
//				s.append(" and t.transactionType = ? ");
//				docParams.add(type);
//			}
			Query q = getSession().createQuery(s.toString());
			for (int i = 0; i < docParams.size(); i++) {
				q.setParameter(i, docParams.get(i));
			}
			return q.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ArrayList<TransactionAction>();
		}
	}

	public List<TransactionAction> lazySearchAndSort(TransactionAction ta, 
			List<ApDomain> lsType, List<ApDomain> lsStatus,
			Integer start, Integer get, boolean desc, String sortField) {
		try {
			StringBuilder s = new StringBuilder(
					"Select t.TRANSACTION_ACTION_ID,\n" +
							"  t.TRANSACTION_ACTION_CODE as transactionActionCode,\n" +
							"  t.TRANSACTION_TYPE transactionType,\n" +
							"  t.CREATE_STAFF_ID createStaffId,\n" +
							"  t.CREATE_DATETIME ,\n" +
							"  t.ASSESSMENT_STAFF_ID assessmentStaffId,\n" +
							"  t.ASSESSMENT_DATETIME ,\n" +
							"  t.DESCRIPTION description,\n" +
							"  t.STATUS status,\n" +
							"  t.CREATE_SHOP_ID createShopId,\n" +
							"  t.FROM_SHOP_ID,\n" +
							"  t.TO_SHOP_ID,\n" +
							"  t.REASON_ID, TO_CHAR(t.CREATE_DATETIME,'yyyy-MM-dd') as createDatetime," +
							"  TO_CHAR(t.ASSESSMENT_DATETIME,'yyyy-MM-dd') as assessmentDatetime " +
							" from TRANSACTION_ACTION t where lower(t.TRANSACTION_ACTION_CODE)  like ? escape '/' ");
			List docParams = new ArrayList();
			SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
			if (ta.getTransactionType() != null && !ta.getTransactionType().trim().isEmpty()) {
				s.append(" and t.TRANSACTION_TYPE = ? ");
				docParams.add(ta.getTransactionType());
			} else {
				if (!lsType.isEmpty()) {
					s.append(" and t.TRANSACTION_TYPE in ( ");
					for (int i = 0; i < lsType.size(); i++) {
						if (i != lsType.size() - 1) {
							s.append("?,");
							docParams.add(lsType.get(i).getValue().toString());
						} else {
							s.append("?");
							docParams.add(lsType.get(i).getValue().toString());

						}
					}
					s.append(")");
				} else {
					return new ArrayList<TransactionAction>();
				}
			}
			if (ta.getCreateDatetime() != null) {
				String date = sf.format(ta.getCreateDatetime());
				s.append(" and TO_CHAR(t.CREATE_DATETIME,'dd-MM-yyyy') = ? ");
				docParams.add(date);
			} else {
				if (ta.getFromDate() != null) {
					s.append(" and t.CREATE_DATETIME >= ? ");
					docParams.add(ta.getFromDate());
				}
				if (ta.getToDate() != null) {
					s.append(" and t.CREATE_DATETIME < ? ");
					docParams.add(DateTimeUtils.addDay(ta.getToDate(), 1));
				}
			}
			if (ta.getAssessmentStaffId() != null && ta.getAssessmentStaffId() > 0) {
				s.append(" and t.ASSESSMENT_STAFF_ID = ? ");
				docParams.add(ta.getAssessmentStaffId());
			}
			if (ta.getAssessmentDatetime() != null) {
				String date = sf.format(ta.getAssessmentDatetime());
				s.append(" and TO_CHAR(t.ASSESSMENT_DATETIME,'dd-MM-yyyy') = ? ");
				docParams.add(date);
			}
			if (ta.getCreateStaffId() != null && ta.getCreateStaffId() > 0) {
				s.append(" and t.CREATE_STAFF_ID = ? ");
				docParams.add(ta.getCreateStaffId());
			}

			if (ta.getCreateShopId() != null && ta.getCreateShopId() > 0) {
				s.append(" and t.CREATE_SHOP_ID = ? ");
				docParams.add(ta.getCreateShopId());
			} else
				if (ta.getCodePath() != null && !ta.getCodePath().isEmpty()) {
					s.append(" and t.CREATE_SHOP_ID in (select shop_Id from shop where lower(code_Path) like ? escape '/' ) ");
					docParams.add(StringUtils.toLikeString(ta.getCodePath()));
				}

			if (ta.getFromShopId() != null && ta.getFromShopId() > 0 && ta.getToShopId() != null && ta.getToShopId() > 0) {
				s.append(" and ( t.FROM_SHOP_ID = ? or TO_SHOP_ID = ?) ");
				docParams.add(ta.getFromShopId());
				docParams.add(ta.getToShopId());
			}else{
				if(ta.getFromShopId() != null && ta.getFromShopId() > 0){
					s.append(" and  t.FROM_SHOP_ID = ? ");
					docParams.add(ta.getFromShopId());
				}

				if (ta.getToShopId() != null && ta.getToShopId() > 0) {
					s.append(" and t.TO_SHOP_ID = ? ");
					docParams.add(ta.getToShopId());
				}
			}

			if (ta.getReasonId() != null && ta.getReasonId() > 0) {
				s.append(" and t.REASON_ID = ? ");
				docParams.add(ta.getReasonId());
			}

			if (ta.getStatus() != null && !ta.getStatus().trim().isEmpty() && !ta.getStatus().trim().equals("0")) {
				s.append(" and t.STATUS = ? ");
				docParams.add(ta.getStatus());
			} else {
				if (!lsStatus.isEmpty()) {
					s.append(" and t.STATUS in ( ");
					for (int i = 0; i < lsStatus.size(); i++) {
						if (i != lsStatus.size() - 1) {
							s.append("?,");
							docParams.add(lsStatus.get(i).getValue().toString());
						} else {
							s.append("?");
							docParams.add(lsStatus.get(i).getValue().toString());

						}
					}
					s.append(")");
				} else {
					return new ArrayList<TransactionAction>();
				}
			}
			if(sortField != null){
				if(desc){
					s.append(" order by UPPER(" + sortField + ") desc ");
				} else{
					s.append(" order by UPPER(" + sortField + ")  asc ");
				}
			}
			System.out.println(s.toString());
			System.out.println(docParams);
			SQLQuery q = getSession().createSQLQuery(s.toString());
//			q.addEntity(TransactionAction.class);
			String code = ta.getTransactionActionCode() == null ? "" : ta.getTransactionActionCode();
			q.setParameter(0, StringUtils.toLikeString(code));
			for (int i = 0; i < docParams.size(); i++) {
				q.setParameter(i + 1, docParams.get(i));
			}
			if (start != null && get != null && get > 0) {
				q.setFirstResult(start);
				q.setMaxResults(get);
			}
			List<Object[]> ls  = q.list();
			List<TransactionAction> lsResult = new ArrayList<>();
			if (ls == null || ls.isEmpty()) {
				return new ArrayList<TransactionAction>();
			} else {
				for (Object[] o : ls) {
					lsResult.add(convertObjectToTransactionActionAppro(o));
				}
				return lsResult;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ArrayList<TransactionAction>();
		}
	}

	private TransactionAction convertObjectToTransactionActionAppro(Object[] o) {
		TransactionAction obj  = new TransactionAction();
		int i = 0;
		obj.setTransactionActionId(o[i] == null ? null : Long.parseLong(o[i].toString()));
		i++;
		obj.setTransactionActionCode(o[i] == null ? null : o[i].toString());
		i++;
		obj.setTransactionType(o[i] == null ? null : o[i].toString());
		i++;
		obj.setCreateStaffId(o[i] == null ? null : Long.parseLong(o[i].toString()));
		i++;
		obj.setCreateDatetime(o[i] == null ? null : (Date) o[i]);
		i++;
		obj.setAssessmentStaffId(o[i] == null ? null : Long.parseLong(o[i].toString()));
		i++;
		obj.setAssessmentDatetime(o[i] == null ? null : (Date) o[i]);
		i++;
		obj.setDescription(o[i] == null ? null : o[i].toString());
		i++;
		obj.setStatus(o[i] == null ? null : o[i].toString());
		i++;
		obj.setCreateShopId(o[i] == null ? null : Long.parseLong(o[i].toString()));
		i++;
		obj.setFromShopId(o[i] == null ? null : Long.parseLong(o[i].toString()));
		i++;
		obj.setToShopId(o[i] == null ? null : Long.parseLong(o[i].toString()));
		i++;
		obj.setReasonId(o[i] == null ? null : Long.parseLong(o[i].toString()));
		i++;
		return obj;
	}
	public Long searchSize(TransactionAction ta, List<ApDomain> lsType, List<ApDomain> lsStatus) {
		StringBuilder s = new StringBuilder(
				"Select Count(1) from TransactionAction t where lower(transactionActionCode)  like ? escape '/' ");
		List docParams = new ArrayList();
		SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
		if (ta.getTransactionType() != null && !ta.getTransactionType().trim().isEmpty()
				&& !ta.getTransactionType().trim().equals("0")) {
			s.append(" and transactionType = ? ");
			docParams.add(ta.getTransactionType());
		} else {
			if (!lsType.isEmpty()) {
				s.append(" and transactionType in ( ");
				for (int i = 0; i < lsType.size(); i++) {
					if (i != lsType.size() - 1) {
						s.append("?,");
						docParams.add(lsType.get(i).getValue().toString());
					} else {
						s.append("?");
						docParams.add(lsType.get(i).getValue().toString());

					}
				}
				s.append(")");
			} else {
				return 0L;
			}
		}
		if (ta.getCreateDatetime() != null) {
			String date = sf.format(ta.getCreateDatetime());
			s.append(" and TO_CHAR(CREATE_DATETIME,'dd-MM-yyyy') = ? ");
			docParams.add(date);
		} else {
			if (ta.getFromDate() != null) {
				s.append(" and createDatetime >= ? ");
				docParams.add(ta.getFromDate());
			}
			if (ta.getToDate() != null) {
				s.append(" and createDatetime < ? ");
				docParams.add(DateTimeUtils.addDay(ta.getToDate(), 1));
			}
		}
		if (ta.getAssessmentStaffId() != null && ta.getAssessmentStaffId() > 0) {
			s.append(" and assessmentStaffId = ? ");
			docParams.add(ta.getAssessmentStaffId());
		}
		if (ta.getAssessmentDatetime() != null) {
			String date = sf.format(ta.getAssessmentDatetime());
			s.append(" and TO_CHAR(ASSESSMENT_DATETIME,'dd-MM-yyyy') = ? ");
			docParams.add(date);
		}
		if (ta.getCreateStaffId() != null && ta.getCreateStaffId() > 0) {
			s.append(" and createStaffId = ? ");
			docParams.add(ta.getCreateStaffId());
		}
		if (ta.getCreateShopId() != null && ta.getCreateShopId() > 0) {
			s.append(" and createShopId = ? ");
			docParams.add(ta.getCreateShopId());
		}
		if (ta.getReasonId() != null && ta.getReasonId() > 0) {
			s.append(" and reasonId = ? ");
			docParams.add(ta.getReasonId());
		}
		if (ta.getFromShopId() != null && ta.getFromShopId() > 0) {
			s.append(" and fromShopId = ? ");
			docParams.add(ta.getFromShopId());
		}
		if (ta.getToShopId() != null && ta.getToShopId() > 0) {
			s.append(" and toShopId = ? ");
			docParams.add(ta.getToShopId());
		}
		if (ta.getStatus() != null && !ta.getStatus().trim().isEmpty() && !ta.getStatus().trim().equals("0")) {
			s.append(" and status = ? ");
			docParams.add(ta.getStatus());
		} else {
			if (!lsStatus.isEmpty()) {
				s.append(" and status in ( ");
				for (int i = 0; i < lsStatus.size(); i++) {
					if (i != lsStatus.size() - 1) {
						s.append("?,");
						docParams.add(lsStatus.get(i).getValue().toString());
					} else {
						s.append("?");
						docParams.add(lsStatus.get(i).getValue().toString());

					}
				}
				s.append(")");
			} else {
				return 0L;
			}
		}
		Query q = getSession().createQuery(s.toString());
		String code = ta.getTransactionActionCode() == null ? "" : ta.getTransactionActionCode();
		q.setParameter(0, StringUtils.toLikeString(code));
		for (int i = 0; i < docParams.size(); i++) {
			q.setParameter(i + 1, docParams.get(i));
		}
		return (Long) q.uniqueResult();
	}

	public Long lazySearchSize(TransactionAction ta, List<ApDomain> lsType, List<ApDomain> lsStatus) {
		StringBuilder s = new StringBuilder(
				"Select Count(1) from Transaction_Action t where lower(transaction_Action_Code)  like ? escape '/' ");
		List docParams = new ArrayList();
		SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
		if (ta.getTransactionType() != null && !ta.getTransactionType().trim().isEmpty()
				&& !ta.getTransactionType().trim().equals("0")) {
			s.append(" and TRANSACTION_TYPE = ? ");
			docParams.add(ta.getTransactionType());
		} else {
			if (!lsType.isEmpty()) {
				s.append(" and TRANSACTION_TYPE in ( ");
				for (int i = 0; i < lsType.size(); i++) {
					if (i != lsType.size() - 1) {
						s.append("?,");
						docParams.add(lsType.get(i).getValue().toString());
					} else {
						s.append("?");
						docParams.add(lsType.get(i).getValue().toString());

					}
				}
				s.append(")");
			} else {
				return 0L;
			}
		}
		if (ta.getCreateDatetime() != null) {
			String date = sf.format(ta.getCreateDatetime());
			s.append(" and TO_CHAR(CREATE_DATETIME,'dd-MM-yyyy') = ? ");
			docParams.add(date);
		} else {
			if (ta.getFromDate() != null) {
				s.append(" and CREATE_DATETIME >= ? ");
				docParams.add(ta.getFromDate());
			}
			if (ta.getToDate() != null) {
				s.append(" and CREATE_DATETIME < ? ");
				docParams.add(DateTimeUtils.addDay(ta.getToDate(), 1));
			}
		}
		if (ta.getAssessmentStaffId() != null && ta.getAssessmentStaffId() > 0) {
			s.append(" and ASSESSMENT_STAFF_ID = ? ");
			docParams.add(ta.getAssessmentStaffId());
		}
		if (ta.getAssessmentDatetime() != null) {
			String date = sf.format(ta.getAssessmentDatetime());
			s.append(" and TO_CHAR(ASSESSMENT_DATETIME,'dd-MM-yyyy') = ? ");
			docParams.add(date);
		}
		if (ta.getCreateStaffId() != null && ta.getCreateStaffId() > 0) {
			s.append(" and CREATE_STAFF_ID = ? ");
			docParams.add(ta.getCreateStaffId());
		}
		if (ta.getCreateShopId() != null && ta.getCreateShopId() > 0) {
			s.append(" and CREATE_SHOP_ID = ? ");
			docParams.add(ta.getCreateShopId());
		} else
			if (ta.getCodePath() != null) {
				s.append(" and CREATE_SHOP_ID in (select shop_Id from shop where lower(code_Path) like ? escape '/' ) ");
				docParams.add(StringUtils.toLikeString(ta.getCodePath()));
			}
		if (ta.getReasonId() != null && ta.getReasonId() > 0) {
			s.append(" and REASON_ID = ? ");
			docParams.add(ta.getReasonId());
		}
		if (ta.getFromShopId() != null && ta.getFromShopId() > 0 && ta.getToShopId() != null && ta.getToShopId() > 0) {
			s.append(" and ( FROM_SHOP_ID = ? or TO_SHOP_ID = ?) ");
			docParams.add(ta.getFromShopId());
			docParams.add(ta.getToShopId());
		}else {
			if (ta.getFromShopId() != null && ta.getFromShopId() > 0) {
				s.append(" and  FROM_SHOP_ID = ? ");
				docParams.add(ta.getFromShopId());
			}
			if (ta.getToShopId() != null && ta.getToShopId() > 0) {
				s.append(" and TO_SHOP_ID = ? ");
				docParams.add(ta.getToShopId());
			}
		}
		if (ta.getStatus() != null && !ta.getStatus().trim().isEmpty() && !ta.getStatus().trim().equals("0")) {
			s.append(" and status = ? ");
			docParams.add(ta.getStatus());
		} else {
			if (!lsStatus.isEmpty()) {
				s.append(" and status in ( ");
				for (int i = 0; i < lsStatus.size(); i++) {
					if (i != lsStatus.size() - 1) {
						s.append("?,");
						docParams.add(lsStatus.get(i).getValue().toString());
					} else {
						s.append("?");
						docParams.add(lsStatus.get(i).getValue().toString());

					}
				}
				s.append(")");
			} else {
				return 0L;
			}
		}
		SQLQuery q = getSession().createSQLQuery(s.toString());

		String code = ta.getTransactionActionCode() == null ? "" : ta.getTransactionActionCode();
		q.setParameter(0, StringUtils.toLikeString(code));
		for (int i = 0; i < docParams.size(); i++) {
			q.setParameter(i + 1, docParams.get(i));
		}
		return ((BigDecimal) q.uniqueResult()).longValue();
	}

	private TransactionAction convertObjectToTransactionAction(Object[] o) throws ParseException {
		TransactionAction rs = new TransactionAction();
		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ssss");
		String k = o[4].toString();
		rs.setTransactionActionId(o[0] == null ? null : Long.parseLong(o[0].toString()));
		rs.setTransactionActionCode(o[1] == null ? null : o[1].toString());
		rs.setTransactionType(o[2] == null ? null : o[2].toString());
		rs.setCreateStaffId(o[3] == null ? null : Long.parseLong(o[3].toString()));
		rs.setCreateDatetime(o[4] == null ? null : sf.parse(k));
		rs.setAssessmentStaffId(o[5] == null ? null : Long.parseLong(o[5].toString()));
		rs.setAssessmentDatetime(o[6] == null ? null : (Date) o[6]);
		rs.setDescription(o[7] == null ? null : o[7].toString());
		rs.setStatus(o[8] == null ? null : o[8].toString());
		rs.setCreateShopId(o[9] == null ? null : Long.parseLong(o[9].toString()));
		rs.setFromShopId(o[10] == null ? null : Long.parseLong(o[10].toString()));
		rs.setToShopId(o[11] == null ? null : Long.parseLong(o[11].toString()));
		rs.setReasonId(o[12] == null ? null : Long.parseLong(o[12].toString()));
		return rs;
	}

	public List<TransactionAction> getBySerial(Long prvId, Long gId, String serial) {
		Query q = getSession().createSQLQuery(
				"SELECT  t.TRANSACTION_ACTION_ID, t.TRANSACTION_ACTION_CODE, t.TRANSACTION_TYPE, t.CREATE_STAFF_ID, TO_CHAR(t.CREATE_DATETIME,'dd-MM-yyyy HH:mm:ssss'), t.ASSESSMENT_STAFF_ID, t.ASSESSMENT_DATETIME, t.DESCRIPTION, t.STATUS, t.CREATE_SHOP_ID, t.FROM_SHOP_ID, t.TO_SHOP_ID, t.REASON_ID "
						+ "FROM TRANSACTION_ACTION t JOIN STOCK_TRANSACTION_SERIAL s ON t.TRANSACTION_ACTION_ID = s.IM_TRANSACTION_ID WHERE s.GOODS_ID = ? AND s.PROVIDER_ID = ? AND s.SERIAL = ? ORDER BY t.CREATE_DATETIME");
		q.setParameter(0, gId);
		q.setParameter(1, prvId);
		q.setParameter(2, serial);
		List<Object[]> a = q.list();
		List<TransactionAction> rs = new ArrayList<>();
		for (Object[] o : a) {
			try {
				rs.add(convertObjectToTransactionAction(o));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return rs;
	}

	@Override
	public void delete(TransactionAction g) {
		super.delete(g);
		getSession().flush();
	}

	public void updateTransactionAction(Long id, String status, Long staffId) {
		Query q2 = session.createQuery(
				"Update TransactionAction set status = ? , assessmentDatetime = ? , assessmentStaffId = ?  where transactionActionId = ?");
		q2.setParameter(0, status);
		q2.setParameter(1, new Date());
		q2.setParameter(2, staffId);
		q2.setParameter(3, id);
		q2.executeUpdate();
		getSession().flush();
	}
	public void updateReasonId(List<Long> ids, Long resultId) {
		Query q2 = session.createQuery(
				"Update TransactionAction set reasonId = ? where transactionActionId = :ids");
		q2.setParameter(0, resultId);
		q2.setParameterList("ids", ids);
		q2.executeUpdate();
		getSession().flush();
	}

	public List<TransactionAction> searchTranByEDtail(TransactionAction ta) {
		try {
			StringBuilder s = new StringBuilder(
					"Select t from TransactionAction t where 1 = 1  ");
			List docParams = new ArrayList();

			if (ta.getTransactionActionCode() != null) {
				s.append(" and lower(transactionActionCode)  like ? escape '/' "); 
				docParams.add(StringUtils.toLikeString(ta.getTransactionActionCode()));
				//			docParams.add("%"+ InventoryConstanst.TransactionActionCode.EMEX.toLowerCase()+"%");
			}
			
			if (ta.getCreateShopId() != null && ta.getCreateShopId() > 0) {
				s.append(" and CREATE_SHOP_ID = ? ");
				docParams.add(ta.getCreateShopId());
			} 
			
			Query q = getSession().createQuery(s.toString());

			for (int i = 0; i < docParams.size(); i++) {
				q.setParameter(i, docParams.get(i));
			}
			if (q.list().isEmpty()) {
				return new ArrayList<TransactionAction>();
			} else {
				return q.list();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ArrayList<TransactionAction>();
		}
	}
	public List<TransactionAction> searchTranByEDtail(String code, String tranType) {
		try {
			StringBuilder s = new StringBuilder(
					"Select t from TransactionAction t where 1 = 1  ");
			List docParams = new ArrayList();

			if (code != null && !code.isEmpty()) {
				s.append(" and lower(transactionActionCode)  like ? escape '/' ");
				docParams.add(StringUtils.toLikeString(code));
				//				docParams.add("%"+ code.trim().toLowerCase() +"%");
			}
			if (tranType != null) {
				s.append(" and transactionType  = ? ");
				docParams.add(tranType);
			}
			Query q = getSession().createQuery(s.toString());

			for (int i = 0; i < docParams.size(); i++) {
				q.setParameter(i, docParams.get(i));
			}
			if (q.list().isEmpty()) {
				return new ArrayList<TransactionAction>();
			} else {
				return q.list();
			}
		} catch (Exception ex) {
			return new ArrayList<TransactionAction>();
		}
	}

	public List<TransactionAction> searchTranByEDtailImportWaran(String code, String tranType1, String tranType2) {
		try {
			StringBuilder s = new StringBuilder(
					"Select t from TransactionAction t where 1 = 1  ");
			List docParams = new ArrayList();

			if (code != null && !code.isEmpty()) {
				s.append(" and lower(transactionActionCode)  like ? ");
				docParams.add("%"+ code.trim().toLowerCase() +"%");
			}
			if (tranType1 != null && tranType2 !=null) {
				s.append(" and (transactionType  = ? OR transactionType  = ?)");
				docParams.add(tranType1);
				docParams.add(tranType2);
			}
			Query q = getSession().createQuery(s.toString());

			for (int i = 0; i < docParams.size(); i++) {
				q.setParameter(i, docParams.get(i));
			}
			if (q.list().isEmpty()) {
				return new ArrayList<TransactionAction>();
			} else {
				return q.list();
			}
		} catch (Exception ex) {
			return new ArrayList<TransactionAction>();
		}
	}


}
