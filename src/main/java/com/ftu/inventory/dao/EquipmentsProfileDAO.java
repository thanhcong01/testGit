/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.dao;

import com.ftu.inventory.bo.EquipmentsProfile;
import com.ftu.inventory.bo.EquipmentsGroup;
import com.ftu.admin.consumer.utils.StringUtil;
import com.ftu.hibernate.GenericDAOHibernate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SQLQuery;

/**
 *
 * @author E5420
 */
public class EquipmentsProfileDAO extends GenericDAOHibernate<EquipmentsProfile, Long> {

	public EquipmentsProfileDAO() {
		super(EquipmentsProfile.class);
	}

	@Override
	public void saveOrUpdate(EquipmentsProfile g) {
		if (g != null) {
			getSession().merge(g);
		}
		getSession().flush();
	}

	@Override
	public void delete(EquipmentsProfile g) {
		getSession().delete(g);
		getSession().flush();
	}

	public List<EquipmentsProfile> getAllProfiles() {
		Query q = getSession().getNamedQuery("EquipmentsProfile.findAll");
		if (q.list().isEmpty()) {
			return null;
		} else {
			return q.list();
		}
	}
	
	public EquipmentsProfile checkExists(String code) {
		String sql = "select s from EquipmentsProfile s where UPPER(s.profileCode) = :code ";
		EquipmentsProfile st = null;
		Query q = getSession().createQuery(sql);
		q.setParameter("code", code.trim().toUpperCase());
		List<EquipmentsProfile> lst = q.list();
		if (!lst.isEmpty()) {
			st = lst.get(0);
		}
		return st;
	}

	public List<EquipmentsProfile> findByGroupId(Long groupId) {
		String sql = "select s from  EquipmentsProfile s where s.equipProfileStatus = 1 and s.equipmentsGroupId = :equipmentsGroupId order by s.profileCode ASC, s.profileName ASC";
		Query q = getSession().createQuery(sql);
		q.setParameter("equipmentsGroupId", groupId);
		return q.list();
	}

	public EquipmentsProfile findByProfileId(Long goodId) {// added Huytv10
		Query query = getSession().getNamedQuery("EquipmentsProfile.findByEquipmentsProfileId");
		query.setParameter("profileId", goodId);
		if (query.list().isEmpty()) {
			return null;
		} else {
			return (EquipmentsProfile) query.list().get(0);
		}
	}

	public List<EquipmentsProfile> getAllProfiles(EquipmentsProfile g, boolean search,int first,int pageSize) {

		StringBuilder sql = new StringBuilder("SELECT s.PROFILE_ID, s.PROFILE_NAME, s.EQUIPMENT_GROUP_ID, s.MANAGEMENT_TYPE, s.PROFILE_CODE,g.EQUIPMENT_GROUP_NAME,g.EQUIPMENT_GROUP_CODE, " +
				" s.UNIT,\n" +
				"  s.SPARE_PART,\n" +
				"  s.MANUFACTURE,\n" +
				"  s.ORIGIN,\n" +
				"  TO_CHAR(s.SPECIFICATION),\n" +
				"  s.ICON, s.EQUIPMENT_PROFILE_STATUS\n" +
				" FROM EQUIPMENT_PROFILE s INNER JOIN EQUIPMENT_GROUP g ON s.EQUIPMENT_GROUP_ID = g.EQUIPMENT_GROUP_ID WHERE 1=1 ");
		HashMap<String, Object> mapParams = new HashMap<String, Object>();
		if (g != null) {
			if (g.getEquipmentsGroupId() != null) {
				sql.append(" and s.EQUIPMENT_GROUP_ID = :EQUIPMENT_GROUP_ID ");
				mapParams.put("EQUIPMENT_GROUP_ID",  g.getEquipmentsGroupId());
			}
			if(!StringUtil.stringIsNullOrEmty(g.getProfileCode())){
				if (search) {
					sql.append(" and UPPER(s.PROFILE_CODE) like :code ");
					mapParams.put("code", "%" + g.getProfileCode().trim().toUpperCase() + "%");
				} else {
					sql.append(" and s.PROFILE_CODE = :code ");
					mapParams.put("code", g.getProfileCode().trim());
				}
			}
			if(!StringUtil.stringIsNullOrEmty(g.getProfileName())){
				if (search) {
					sql.append(" and UPPER(s.PROFILE_NAME) like :name ");
					mapParams.put("name", "%" + g.getProfileName().trim().toUpperCase() + "%");
				} else {
					sql.append(" and s.PROFILE_NAME = :name ");
					mapParams.put("name", g.getProfileName().trim());
				}
			}
			if(!StringUtil.stringIsNullOrEmty(g.getUnit())){
				if (search) {
					sql.append(" and UPPER(s.UNIT) like :UNIT ");
					mapParams.put("UNIT", "%" + g.getUnit().trim().toUpperCase() + "%");
				} else {
					sql.append(" and s.UNIT = :UNIT ");
					mapParams.put("UNIT", g.getUnit().trim());
				}
			}
			if(!StringUtil.stringIsNullOrEmty(g.getManufacture())){
				if (search) {
					sql.append(" and UPPER(s.MANUFACTURE) like :MANUFACTURE ");
					mapParams.put("MANUFACTURE", "%" + g.getManufacture().trim().toUpperCase() + "%");
				} else {
					sql.append(" and s.MANUFACTURE = :MANUFACTURE ");
					mapParams.put("MANUFACTURE", g.getManufacture().trim());
				}
			}
			if(!StringUtil.stringIsNullOrEmty(g.getOrigin())){
				if (search) {
					sql.append(" and UPPER(s.ORIGIN) like :ORIGIN ");
					mapParams.put("ORIGIN", "%" + g.getManufacture().trim().toUpperCase() + "%");
				} else {
					sql.append(" and s.ORIGIN = :ORIGIN ");
					mapParams.put("ORIGIN", g.getManufacture().trim());
				}
			}
			
			if(!StringUtil.stringIsNullOrEmty(g.getManagementType())){
				sql.append(" and s.MANAGEMENT_TYPE = :type ");
				mapParams.put("type", g.getManagementType().trim());
			}
			if(g.getEquipProfileStatus()!=null){
				sql.append(" and s.EQUIPMENT_PROFILE_STATUS = :EQUIPMENT_PROFILE_STATUS ");
				mapParams.put("EQUIPMENT_PROFILE_STATUS", g.getEquipProfileStatus());
			}

//			if(g.getEquipmentsGroupId() != null){
//				sql.append(" and s.EQUIPMENT_GROUP_ID = :group ");
//				mapParams.put("group", g.getEquipmentsGroupId());
//			}
			
		}

		sql.append(" order by g.EQUIPMENT_GROUP_CODE asc,  s.PROFILE_CODE asc ");

		//Query q = getSession().createQuery(sql);
		SQLQuery q = getSession().createSQLQuery(sql.toString());
		if (first >= 0) {
			q.setMaxResults(pageSize);
			q.setFirstResult(first);
		}
		
		for (String param : mapParams.keySet()) {
			if (mapParams.get(param) instanceof List) {
				q.setParameterList(param, (Collection) mapParams.get(param));
			} else {
				q.setParameter(param, mapParams.get(param));
			}
		}
		List<Object[]> result =  q.list();
		List<EquipmentsProfile> list = new ArrayList<EquipmentsProfile>();
		for (Object[] object : result) {
            list.add(convertRowToGoods(object));
        }
		return list;

	}

	public Integer getSizeProfilesByGroup(List<Long> objs){
		String sql ="SELECT PROFILE_ID From EQUIPMENT_PROFILE p where EQUIPMENT_GROUP_ID in :objs";
		if(objs==null||objs.isEmpty()){
			return 0;
		}else {
			SQLQuery q = getSession().createSQLQuery(sql);
			q.setParameterList("objs", objs);
			List<Object[]> result =  q.list();
			if(result==null||result.isEmpty()){
				return 0;
			}
			return result.size();
		}
	}

	
	private EquipmentsProfile convertRowToGoods(Object[] object){
		EquipmentsProfile profile = new EquipmentsProfile();
        EquipmentsGroup gg = new EquipmentsGroup();
		profile.setProfileId((object[0] != null ? new Long(object[0].toString().trim()) : null));
		profile.setProfileName((object[1] != null ? object[1].toString().trim() : null));
		profile.setEquipmentsGroupId((object[2] != null ? new Long(object[2].toString().trim()) : null));
		profile.setManagementType((object[3] != null ? object[3].toString().trim() : null));
		profile.setProfileCode((object[4] != null ? object[4].toString().trim() : null));
        gg.setEquipmentsGroupId(profile.getEquipmentsGroupId());
        gg.setEquipmentsGroupName((object[5] != null ? object[5].toString().trim() : null));
        gg.setEquipmentsGroupCode((object[6] != null ? object[6].toString().trim() : null));
		profile.setEquipmentsGroup(gg);
		profile.setUnit((object[7] != null ? object[7].toString().trim() : null));
		if(object[8] != null && object[8].toString().equals("1")){
			profile.setSparePart(true);
		}else {
			profile.setSparePart(false);
		}
//		profile.setSparePart((object[8] != null ? Boolean.parseBoolean(object[8].toString().trim()) : false));
		profile.setManufacture((object[9] != null ? object[9].toString().trim() : null));
		profile.setOrigin((object[10] != null ? object[10].toString().trim() : null));
		profile.setSpecification((object[11] != null ? object[11].toString().trim() : null));
		profile.setIcon((object[12] != null ? object[12].toString().trim() : null));
		profile.setEquipProfileStatus((object[13] != null ? new Long(object[13].toString().trim())  : null));
//		if(object[13] != null && object[13].toString().equals("1")){
//			profile.setSerial(true);
//		}else {
//			profile.setSerial(false);
//		}
//		profile.setSerial((object[13] != null ? Boolean.parseBoolean(object[13].toString().trim()) : false));
        return profile;
	}

}

