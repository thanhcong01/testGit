/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.dao;

import com.ftu.constanst.InventoryConstanst;
import com.ftu.inventory.bo.EquipmentsProfile;
import com.ftu.inventory.bo.EquipmentsGroup;
import com.ftu.admin.consumer.utils.StringUtil;
import com.ftu.hibernate.GenericDAOHibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author E5420
 */
public class EquipmentsGroupDAO extends GenericDAOHibernate<EquipmentsGroup, Long> {

    public EquipmentsGroupDAO() {
        super(EquipmentsGroup.class);
    }

    @Override
    public void saveOrUpdate(EquipmentsGroup g) {
        if (g != null) {
            getSession().merge(g);
        }
        getSession().flush();
    }

    @Override
    public List<EquipmentsGroup> findAll() {
        Query q = getSession().createQuery("Select g from EquipmentsGroup g order by UPPER(g.equipmentsGroupName) ASC ");
        return q.list().isEmpty()? new ArrayList<EquipmentsGroup>():q.list();
    }



	public List<EquipmentsGroup> findAllOrderCode() {
		Query q = getSession().createQuery("Select g from EquipmentsGroup g order by UPPER(g.equipmentsGroupCode) ASC, UPPER(g.equipmentsGroupName) ASC ");
		return q.list().isEmpty()? new ArrayList<EquipmentsGroup>():q.list();
	}
	public List<EquipmentsGroup> findAllActive() {
		Query q = getSession().createQuery("Select g from EquipmentsGroup g Where g.equipmentsGroupStatus = 1 order by UPPER(g.equipmentsGroupCode) ASC " +
				" , UPPER(g.equipmentsGroupName) ASC ");
		return q.list().isEmpty()? new ArrayList<EquipmentsGroup>():q.list();
	}

    @Override
    public void delete(EquipmentsGroup g) {
        getSession().delete(g);
        getSession().flush();
    }
    
    public EquipmentsGroup checkExists(String code) {
		String sql = "select s from EquipmentsGroup s where UPPER(s.equipmentsGroupCode) = :code ";
		EquipmentsGroup st = null;
		Query q = getSession().createQuery(sql);
		q.setParameter("code", code.trim().toUpperCase());
		List<EquipmentsGroup> lst = q.list();
		if (!lst.isEmpty()) {
			st = lst.get(0);
		}
		return st;
	}

	public Long checkGroupIsSerial(Long groupId) {
		try {
			List param = new ArrayList();
			StringBuilder s = new StringBuilder(" select count(1) from equipment_group where 1=1\n" +
					" and EQUIPMENT_GROUP_ID = ? and\n" +
					" EQUIPMENT_GROUP_ID \n" +
					" in(select EQUIPMENT_GROUP_ID from equipment_profile pr where pr.management_type = ? ) order by EQUIPMENT_GROUP_NAME ASC ");
			param.add(groupId);
			param.add(InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L);
			Query q = getSession().createSQLQuery(s.toString());
			for (int i = 0; i < param.size(); i++) {
				q.setParameter(i, param.get(i));
			}
			return Long.parseLong(q.uniqueResult().toString());
		} catch (Exception e) {
			e.printStackTrace();
			return 0L;
		}

	}
    
    public List<EquipmentsGroup> getAllEquipmentsGroup(EquipmentsGroup g, boolean search,int first,int pageSize) {

		String sql = "select s from EquipmentsGroup s where 1=1 ";
		HashMap<String, Object> mapParams = new HashMap<String, Object>();
		if (g != null) {
			
			if(g.getEquipmentsGroupId() != null){
				sql += " and s.equipmentsGroupId = :ID ";
				mapParams.put("ID", g.getEquipmentsGroupId());
			}
			
			if(!StringUtil.stringIsNullOrEmty(g.getEquipmentsGroupName())){
				if (search) {
					sql += " and UPPER(s.equipmentsGroupName) like :name ";
					mapParams.put("name", "%" + g.getEquipmentsGroupName().trim().toUpperCase() + "%");
				} else {
					sql += " and s.equipmentsGroupName = :name ";
					mapParams.put("name", g.getEquipmentsGroupName().trim());
				}
			}
			
			if(!StringUtil.stringIsNullOrEmty(g.getEquipmentsGroupCode())){
				if (search) {
					sql += " and UPPER(s.equipmentsGroupCode) like :code ";
					mapParams.put("code", "%" + g.getEquipmentsGroupCode().trim().toUpperCase() + "%");
				} else {
					sql += " and s.equipmentsGroupCode = :code ";
					mapParams.put("code", g.getEquipmentsGroupCode().trim());
				}
			}
			if(!StringUtil.stringIsNullOrEmty(g.getEquipmentsGroupType())){
					sql += " and UPPER(s.equipmentsGroupType) = :equipmentsGroupType ";
					mapParams.put("equipmentsGroupType",  g.getEquipmentsGroupType());
			}
			if(!StringUtil.stringIsNullOrEmty(g.getMaintanceScript())){
				sql += " and UPPER(s.maintanceScript) like :maintanceScript ";
				mapParams.put("maintanceScript",  "%" + g.getMaintanceScript().trim().toUpperCase()+"%");
			}
			if(g.getEquipmentsGroupStatus()!=null){
				sql += " and s.equipmentsGroupStatus = :equipmentsGroupStatus ";
				mapParams.put("equipmentsGroupStatus", g.getEquipmentsGroupStatus() );
			}
		}

		sql += " order by s.equipmentsGroupCode asc ";

		Query q = getSession().createQuery(sql);

		if (first >= 0) {
			q.setMaxResults(pageSize);
			q.setFirstResult(first);
		}
		
		for (String param : mapParams.keySet()) {
			q.setParameter(param, mapParams.get(param));
		}
		
		if (q.list().isEmpty()) {
			return new ArrayList<EquipmentsGroup>();
		} else {
			return q.list();
		}

	}

}
