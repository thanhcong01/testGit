/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;

/**
 * GenericDAOHibernate
 *
 * @author GPDN_NgocTM3
 * @param <T>
 * @param <ID>
 */
public class GenericDAOHibernate<T, ID extends Serializable> extends GenericHibernateDAO<T, ID> {

    private final Class<T> type;
    protected Session session = getSession();
    private Long lastId;
    Character[] aZ = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'u', 'x', 'y', 'z', '-', '_',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    public Long getLastId() {
        return lastId;
    }

    public void setLastId(Long lastId) {
        this.lastId = lastId;
    }

    public Date addOneDay(Date date) {
        Calendar c = Calendar.getInstance();
        if (date != null) {
            c.setTime(date);
            c.add(Calendar.DAY_OF_MONTH, 1);
        }
        return c.getTime();
    }


    public String convertToLikeString(String source) {
        if (source == null) {
            return "";
        }

        source = source.trim().toLowerCase();
        String des = "";
        Boolean isSpecial;

        char[] specialChar = {'%', '_', '?', '\''};
        for (int i = 0; i < source.length(); i++) {
            isSpecial = false;
            for (int j = 0; j < specialChar.length; j++) {
                if (specialChar[j] == source.charAt(i)) {
                    isSpecial = true;
                    break;

                }
            }
            if (isSpecial) {
                des = des + '!' + source.charAt(i);
            } else {
                des = des + source.charAt(i);
            }

        }
        return des;
    }

    public boolean checkEmptyString(String str) {
        if (str == null) {
            return true;
        }
        str = str.trim();
        if (str.length() == 0) {
            return true;
        }
        return false;
    }

    public GenericDAOHibernate(Class<T> type) {
        this.type = type;
    }

    public ID create(T o) {
        return (ID) session.save(o);
    }

    public void saveOrUpdate(T o) {
        session.saveOrUpdate(o);
    }

    public ID create(T o, String type) {
//        Session sessionTmp = null;
////        if (type != null && "LTCQ".equals(type)) {
////            sessionTmp = getSession("ltcq");
////        } else {
//        sessionTmp = getSession();
////        }
//        return (ID) sessionTmp.save(o);
        return (ID) session.save(o);
    }

    public T read(ID id) {
        return (T) session.get(this.type, id);
    }

    public void update(T o) {
        session.update(o);
    }

    public void update(T o, String type) {
        session.update(o);
    }

    public void delete(T o) {
        session.delete(o);
    }

    public Date getSysdate() throws Exception {
        String query = "Select sysdate as system_datetime from dual";
        SQLQuery queryObj = getSession().createSQLQuery(query);
        //queryObj.addScalar("system_datetime", Hibernate.TIMESTAMP);
        //queryObj.addScalar("system_datetime", Hibernate.TIMESTAMP);
        Date sysdate = (Date) queryObj.uniqueResult();
        return sysdate;
    }

//    public List<T> getDataByFields(Class entityClass, List<String> key, List<Object> valueOfKey, String sortField) {
//        String entityName = entityClass.getSimpleName();
//        // Build sql
//        StringBuilder sqlBuilder = new StringBuilder();
//        sqlBuilder.append(" FROM ").append(entityName).append(" obj WHERE ");
//        if (key != null && valueOfKey != null && key.size() > 0 && (key.size() == valueOfKey.size())) {
//            sqlBuilder.append("obj.").append(key.get(0)).append("= :value0");
//            for (int i = 1; i < key.size(); i++) {
//                sqlBuilder.append(" AND ").append("obj.").append(key.get(i)).append("= :value").append(i);
//            }
//        }
//        if (!"".equals(sortField)) {
//            sqlBuilder.append(" ORDER BY ");
//            sqlBuilder.append("  nlssort(lower(");
//            sqlBuilder.append(validateColumnName(sortField));
//            sqlBuilder.append("),'nls_sort = BINARY_AI')");
//        }
//
//        Query query = session.createQuery(sqlBuilder.toString());
//        if (key != null && valueOfKey != null && key.size() > 0 && (key.size() == valueOfKey.size())) {
//            query.setParameter("value0", valueOfKey.get(0));
//            for (int i = 1; i < key.size(); i++) {
//                query.setParameter("value" + i, valueOfKey.get(i));
//            }
//        }
//
//        return query.list();
//    }

//    public List<T> getListByKeys(String entityField, List<ID> key) {
//        String entityName = type.getSimpleName();
//        StringBuilder sqlBuilder = new StringBuilder();
//        sqlBuilder.append(" FROM ").append(entityName).append(" obj WHERE obj.").append(entityField).append(" IN (:key)");
//        Query query = session.createQuery(sqlBuilder.toString()).setParameterList("key", key);
//        return query.list();
//    }

    /**
     * kiem tra trung khi update trungnq
     *
     * @param idField
     * @param codeField
     * @param entityCode
     * @param entityId
     * @return
     */
//    public Boolean checkEntityExistedForUpdate(String idField, String codeField, String entityCode, Long entityId) {
//        Boolean isExisted = false;
//        try {
//            String entityName = type.getSimpleName();
//            StringBuilder sqlBuilder = new StringBuilder();
//            sqlBuilder.append(" FROM ").append(entityName).append(" obj WHERE ");
//            sqlBuilder.append("lower(").append(codeField).append(")").append("  like ? ");
//            sqlBuilder.append(" and ").append(idField).append("  <> ? ");
//            Query query = session.createQuery(sqlBuilder.toString());
//            query.setParameter(0, entityCode.toLowerCase());
//            query.setParameter(1, entityId);
//            List result = query.list();
//            if ((result != null) && (!result.isEmpty()) && (result.size() > 0)) {
//                isExisted = true;
//            }
//        } catch (Exception ex) {
//            LogUtils.addLog(ex.getMessage());
//            //     throw ex;
//        }
//        return isExisted;
//    }

    /**
     * kiem tra trung khi insert * trungnq
     *
     * @param codeField
     * @param entityCode
     * @return
     */
//    public Boolean checkEntityExistedForInsert(String codeField, String entityCode) {
//        Boolean isExisted = false;
//        try {
//
////            Criterion[] criterion = {Restrictions.like(codeField, entityCode.toLowerCase(),MatchMode.EXACT).ignoreCase()};// new Criterion[1];
////            List result = this.findByCriteria(-1,-1,criterion);
//            String entityName = type.getSimpleName();
//            StringBuilder sqlBuilder = new StringBuilder();
//            sqlBuilder.append(" FROM ").append(entityName).append(" obj WHERE ");
//            sqlBuilder.append("lower(").append(codeField).append(")").append("  like ? ");
//            Query query = session.createQuery(sqlBuilder.toString());
//            query.setParameter(0, entityCode.toLowerCase());
//            List result = query.list();
//            if ((result != null) && (!result.isEmpty()) && (result.size() > 0)) {
//                isExisted = true;
//            }
//        } catch (Exception ex) {
//            LogUtils.addLog(ex.getMessage());
//            // throw ex;
//        }
//        return isExisted;
//    }

    /**
     * Check exist in DB by list key
     *
     * @author DPDN_NgocTM3
     * @param entityClass
     * @param key
     * @param valueOfKey
     * @return
     */
//    public Boolean isExistIDInDb(Class entityClass, List<String> key, List<Object> valueOfKey) {
//        String entityName = entityClass.getSimpleName();
//        // Build sql
//        StringBuilder sqlBuilder = new StringBuilder();
//        sqlBuilder.append(" FROM ").append(entityName).append(" obj WHERE ");
//        if (key != null && valueOfKey != null && key.size() > 0 && (key.size() == valueOfKey.size())) {
//            sqlBuilder.append("obj.").append(key.get(0)).append("= :value0");
//            for (int i = 1; i < key.size(); i++) {
//                sqlBuilder.append(" AND ").append("obj.").append(key.get(i)).append("= :value").append(i);
//            }
//        }
//        Query query = session.createQuery(sqlBuilder.toString());
//        if (key != null && valueOfKey != null && key.size() > 0 && (key.size() == valueOfKey.size())) {
//            query.setParameter("value0", valueOfKey.get(0));
//            for (int i = 1; i < key.size(); i++) {
//                query.setParameter("value" + i, valueOfKey.get(i));
//            }
//        }
//        List<Object> lsObject = query.list();
//        return lsObject != null && lsObject.size() > 0;
//    }

    /**
     * Check exist in DB by list key
     *
     * @author DPDN_NgocTM3
     * @param entityClass
     * @param key
     * @param valueOfKey
     * @return
     */
//    public Boolean isExistIDInDb(List<String> key, List<Object> valueOfKey, String enityUpdate, Object valueUpdate) {
//        String entityName = type.getSimpleName();
//        // Build sql
//        StringBuilder sqlBuilder = new StringBuilder();
//        sqlBuilder.append(" FROM ").append(entityName).append(" obj WHERE ");
//        if (key != null && valueOfKey != null && key.size() > 0 && (key.size() == valueOfKey.size())) {
//            sqlBuilder.append("obj.").append(key.get(0)).append("= :value0");
//            for (int i = 1; i < key.size(); i++) {
//                sqlBuilder.append(" AND ").append("obj.").append(key.get(i)).append("= :value").append(i);
//            }
//            if (enityUpdate != null && !"".equals(entityName) && valueUpdate != null) {
//                sqlBuilder.append(" and ").append("obj.").append(enityUpdate).append("  <> :").append(enityUpdate);
//            }
//        }
//        Query query = session.createQuery(sqlBuilder.toString());
//        if (key != null && valueOfKey != null && key.size() > 0 && (key.size() == valueOfKey.size())) {
//            query.setParameter("value0", valueOfKey.get(0));
//            for (int i = 1; i < key.size(); i++) {
//                query.setParameter("value" + i, valueOfKey.get(i));
//            }
//            if (enityUpdate != null && !"".equals(entityName) && valueUpdate != null) {
//                query.setParameter(enityUpdate, valueUpdate);
//            }
//        }
//        List<Object> lsObject = query.list();
//        return lsObject != null && lsObject.size() > 0;
//    }

//    public List findAll(Class entityClass, String... orderFields) {
//        String entityName = entityClass.getSimpleName();
//        // Build sql
//        StringBuilder sqlBuilder = new StringBuilder();
//        sqlBuilder.append(" FROM ").append(entityName);
//        if (orderFields != null && orderFields.length > 0) {
//            int length = orderFields.length;
//            sqlBuilder.append(" ORDER BY ");
//
//            for (int i = 0; i < length; i++) {
//                sqlBuilder.append("  nlssort(lower(");
//                sqlBuilder.append(orderFields[i]);
//                sqlBuilder.append("),'nls_sort = BINARY_AI')");
//                if (i < length - 1) {
//                    sqlBuilder.append(",");
//                }
//            }
//        }
//        Query query = session.createQuery(sqlBuilder.toString());
//        List<Object> lsObject = query.list();
//        return lsObject;
//    }

//    public List findAllActive(Class entityClass, String... orderFields) {
//        String entityName = entityClass.getSimpleName();
//        // Build sql
//        StringBuilder sqlBuilder = new StringBuilder();
//        sqlBuilder.append(" FROM ").append(entityName).append(" obj ");
//        if (entityClass.getSimpleName().equalsIgnoreCase("position")) {
//            sqlBuilder.append(" WHERE obj.status = 1");
//        } else {
//            sqlBuilder.append(" WHERE obj.isActive = 1");
//        }
//        if (orderFields != null && orderFields.length > 0) {
//            int length = orderFields.length;
//            sqlBuilder.append(" ORDER BY ");
//            for (int i = 0; i < length; i++) {
//                sqlBuilder.append("  nlssort(lower(");
//                sqlBuilder.append(orderFields[i]);
//                sqlBuilder.append("),'nls_sort = BINARY_AI')");
//                if (i < length - 1) {
//                    sqlBuilder.append(",");
//                }
//            }
//        } else {
//            sqlBuilder.append(" ORDER BY ");
//            sqlBuilder.append("  nlssort(lower(");
//            sqlBuilder.append("name");
//            sqlBuilder.append("),'nls_sort = BINARY_AI')");
//        }
//        Query query = session.createQuery(sqlBuilder.toString());
//        List<Object> lsObject = query.list();
//        return lsObject;
//    }

//    public Object findByName(Class entityClass, String name) {
//        String entityName = entityClass.getSimpleName();
//        // Build sql
//        StringBuilder sqlBuilder = new StringBuilder();
//        List listParam = new ArrayList();
//        sqlBuilder.append(" FROM ").append(entityName).append(" obj WHERE obj.isActive = 1 and lower(obj.name) like ? escape'!'");
//        listParam.add("%" + convertToLikeString(name) + "%");
//
//        Query query = session.createQuery(sqlBuilder.toString());
//        for (int i = 0; i < listParam.size(); i++) {
//            query.setParameter(i, listParam.get(i));
//        }
//
//        List<Object> lsObject = query.list();
//        if (lsObject != null && lsObject.size() > 0) {
//            return lsObject.get(0);
//        }
//        return null;
//    }
//
//    public Object findById(Class entityClass, String idName, Long id) {
//        String entityName = entityClass.getSimpleName();
//        Object returnOb = null;
//        // Build sql
//        if (id != null && id > 0L) {
//            String hql = "from " + entityName + " where " + idName + " = " + id.toString();
//            Query query = session.createQuery(hql);
//            List<Object> lsObject = query.list();
//            if (lsObject != null && lsObject.size() > 0) {
//                returnOb = lsObject.get(0);
//            }
//        }
//        return returnOb;
//    }

    @Override
    public T findById(ID id, boolean lock) {
        return super.findById(id, false);
    }

    public T findById(ID id) {
        if (id == null) {
            return null;
        }
        return super.findById(id, false);
    }

    // tham khao cua LLTP
    public T getById(String idName, Object idValue) {
        Criteria crit = getSession().createCriteria(getPersistentClass());
        crit.add(Restrictions.eq(idName, idValue));
        List lst = crit.list();
        if (lst != null && lst.size() > 0) {
            return (T) crit.list().get(0);
        } else {
            return null;
        }
    }

    public T getById(String idName, Object idValue, String type) {
        Session currentSession = null;
//        if (type != null && "LTCQ".equals(type)) {
//            currentSession = getSession("ltcq");
//        } else {
        currentSession = getSession();
//        }
        Criteria crit = currentSession.createCriteria(getPersistentClass());
        crit.add(Restrictions.eq(idName, idValue));
        return (T) crit.list().get(0);
    }

    @SuppressWarnings("unchecked")
    public List<T> getByProperty(String propertyName, Object value) {
        Criteria crit = getSession().createCriteria(getPersistentClass());
        crit.add(Restrictions.eq("isActive", "1"));
        crit.add(Restrictions.eq(propertyName, value));
        return (List<T>) crit.list();
    }

    public List<T> getByProperty(String propertyName, Object value, String type) {
        Session currentSession = null;
//        if (type != null && "LTCQ".equals(type)) {
//            currentSession = getSession("ltcq");
//        } else {
        currentSession = getSession();
//        }
        Criteria crit = currentSession.createCriteria(getPersistentClass());
        crit.add(Restrictions.eq("isActive", "1"));
        crit.add(Restrictions.eq(propertyName, value));
        return (List<T>) crit.list();
    }

    public String UpcaseFirst(String str) {
        String first = str.substring(0, 1);
        String concat = str.substring(1);
        return first.toUpperCase() + concat;
    }

//    public String convertToJSONArray(List<T> array, String id, String searchAttr1, String searchAttr2) {
//        JSONArray jsonArray = new JSONArray();
//        try {
//            for (T o : array) {
//                Class cls = o.getClass();
//                String getMethodName;
//                Method getMethod;
//                JSONObject jsonObj = new JSONObject();
//
//                getMethodName = "get" + UpcaseFirst(id);
//                getMethod = cls.getMethod(getMethodName);
//                String idValue = getMethod.invoke(o).toString();
//                jsonObj.put("id", idValue);
//
//                getMethodName = "get" + UpcaseFirst(searchAttr1);
//                getMethod = cls.getMethod(getMethodName);
//                String search1 = getMethod.invoke(o).toString();
//                jsonObj.put("search1", StringUtils.escapeHtml(search1));
//                //jsonObj.put("search1", (search1));
//                String search2 = "";
//                if (searchAttr2 != null && !"".equals(searchAttr2)) {
//                    getMethodName = "get" + UpcaseFirst(searchAttr2);
//                    getMethod = cls.getMethod(getMethodName);
//                    if (getMethod.invoke(o) != null) {
//                        search2 = getMethod.invoke(o).toString();
//                    }
//                }
//                //jsonObj.put("search2", StringUtils.escapeHTML(search2));
//                jsonObj.put("search2", StringUtils.escapeHtml(search2));
//                jsonArray.add(jsonObj);
//            }
//        } catch (Exception e) {
//            System.out.print(e.getMessage());
//        }
//        return jsonArray.toString();
//    }
    public String validateColumnName(String name) {
        List<Character> aToZ = new ArrayList<Character>();
        aToZ.addAll(Arrays.asList(aZ));
        if (name != null && !"".equals(name)) {
            if (name.length() > 30) {
                //
                // Ten cot dai nhat trong sql = 30
                //
                return "";
            } else {
                for (int ind = 0; ind < name.length(); ind++) {
                    String nameLower = name.toLowerCase();
                    if (!aToZ.contains(nameLower.charAt(ind))) {
                        return "";
                    }
                }
            }
        }
        return name;
    }

    /**
     * Split chuoi 1;2 hoac 1;2; thanh mang long [1,2]
     * Split chuoi 1; thanh mang long [1]
     * @param input
     * @param delimiter
     * @return 
     */
    public static Long[] splitToLong(String input, String delimiter) {
        Long[] output = null;
        if (input != null && !"".equals(input)) {
            input = org.apache.commons.lang.StringUtils.strip(input.trim(), delimiter);
            if (input.contains(";")) {
                String[] tmps = org.apache.commons.lang.StringUtils.split(input, delimiter);
                output = new Long[tmps.length];
                for (int i=0;i<tmps.length;i++) {
                    output[i] = Long.parseLong(tmps[i]);
                }
            } else {
                output = new Long[1];
                output[0] = Long.parseLong(input);
            }
        }
        return output;
    }
    
    /**
     * Split chuoi 1;2 hoac 1;2; thanh mang chuoi ['1', '2']
     * Split chuoi 1; thanh mang chuoi ['1']
     * @param input
     * @param delimiter
     * @return 
     */
    public static String[] splitTostring(String input, String delimiter) {
        String[] output = null;
        if (input != null && !"".equals(input)) {
            input = org.apache.commons.lang.StringUtils.strip(input.trim(), delimiter);
            if (input.contains(";")) {
                output = org.apache.commons.lang.StringUtils.split(input, delimiter);
            } else {
                output = new String[1];
                output[0] = input;
            }
        }
        return output;
    }
    
    @Override
    protected void finalize() throws Throwable {
        try {
            //HibernateUtil.closeCurrentSessions();
        } finally {
            super.finalize();
        }
    }
}
