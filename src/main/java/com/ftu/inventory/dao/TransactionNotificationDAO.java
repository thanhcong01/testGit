/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.dao;

import com.ftu.hibernate.GenericDAOHibernate;
import com.ftu.inventory.bo.TransactionNotification;
import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author Cao Cuong
 */
public class TransactionNotificationDAO extends GenericDAOHibernate<TransactionNotification, Long> {

	public TransactionNotificationDAO() {
		super(TransactionNotification.class);
	}

	@Override
	public void saveOrUpdate(TransactionNotification g) {
		if (g != null) {
			super.saveOrUpdate(g);
		}
		getSession().flush();
	}

	public List<TransactionNotification> getListByListUrl(List<String> urlList) {
		if (urlList == null || urlList.isEmpty()) {
			return null;
		} else {
			
			StringBuilder s = new StringBuilder("Select c from TransactionNotification c where url in ( ");
			for (int i = 0; i < urlList.size(); i++) {
				if (i < urlList.size() - 1) {
					s.append("?, ");
				} else {
					s.append("?");
				}
			}
			s.append(" )");
			s.append(" order by id ");
			Query q = getSession().createQuery(s.toString());
			for (int i = 0; i < urlList.size(); i++) {
					q.setParameter(i, urlList.get(i).substring(1, urlList.get(i).length()));
			}

			return q.list();
		}
	}
}
