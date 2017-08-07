    /*
     * To change this license header, choose License Headers in Project Properties.
     * To change this template file, choose Tools | Templates
     * and open the template in the editor.
     */
    package com.ftu.staff.dao;

    import com.ftu.admin.consumer.utils.StringUtil;
    import com.ftu.hibernate.GenericDAOHibernate;
    import com.ftu.staff.bo.Position;
    import com.ftu.staff.bo.Staff;
    import org.hibernate.Query;

    import java.io.Serializable;
    import java.util.ArrayList;
    import java.util.Collection;
    import java.util.HashMap;
    import java.util.List;

    /**
     * @author E5420
     */
    public class PositionDAO extends GenericDAOHibernate<Position, Long> implements Serializable {

        public PositionDAO() {
            super(Position.class);
        }

        @Override
        public Position findById(Long id) {
            Query q = getSession().createQuery("Select s from Position s where s.positionId = ? ");
            q.setParameter(0, id);
            if (q.list().isEmpty()) {
                return null;
            } else {
                return (Position) q.list().get(0);
            }
        }

        public Position findByPositionCode(String positionCode) {
            Query q = getSession().createQuery("Select s from Position s where UPPER(s.positionCode) = ?  ");
            q.setParameter(0, positionCode.trim().toUpperCase());
            if (q.list().isEmpty()) {
                return null;
            } else {
                return (Position) q.list().get(0);
            }
        }

        public List<Position> findByPositionName(String positionName) {
            Query q = getSession().createQuery("Select s from Position s where UPPER(s.positionName) = ?  ");
            q.setParameter(0, positionName.trim().toUpperCase());
            if (q.list().isEmpty()) {
                return null;
            } else {
                return q.list();
            }
        }

        public List<Position> findByLinkId(Long linkId) {
            Query q = getSession().createQuery("Select s from Position s where s.linkId = ?  ");
            q.setParameter(0, linkId);
            if (q.list().isEmpty()) {
                return null;
            } else {
                return q.list();
            }
        }

        public List<Position> findByLaneCode(String laneCode) {
            Query q = getSession().createQuery("Select s from Position s where UPPER(s.laneCode) = ?  ");
            q.setParameter(0, laneCode.trim().toUpperCase());
            if (q.list().isEmpty()) {
                return new ArrayList<>();
            } else {
                return q.list();
            }
        }

        public List<Position> findByIpAddress(String ipAddress) {
            Query q = getSession().createQuery("Select s from Position s where UPPER(s.ipAddress) = ?  ");
            q.setParameter(0, ipAddress.trim().toUpperCase());
            if (q.list().isEmpty()) {
                return new ArrayList<>();
            } else {
                return q.list();
            }
        }

        public List<Position> findByShopId(Long id) {
        	StringBuilder sql = new StringBuilder();
        	HashMap<String, Object> mapParams = new HashMap<String, Object>();
        	
        	sql.append("Select s from Position s where 1=1 ");
            
            if (id != null) {
                sql.append(" and s.shopId = :SHOP_ID ");
                mapParams.put("SHOP_ID", id);
            }
            Query q = getSession().createQuery(sql.toString());
            for (String param : mapParams.keySet()) {
                if (mapParams.get(param) instanceof List) {
                    q.setParameterList(param, (Collection) mapParams.get(param));
                } else {
                    q.setParameter(param, mapParams.get(param));
                }
            }

            if (q.list().isEmpty()) {
                return new ArrayList<>();
            } else {
                return q.list();
            }
        }

        public List<Position> findByShopIdAllChi(Long id) {
            HashMap<String, Object> mapParams = new HashMap<String, Object>();
            StringBuilder sql = new StringBuilder("select \n" +
                    " p.POSITION_ID,\n" +
                    "  p.POSITION_CODE,\n" +
                    "  p.POSITION_NAME,\n" +
                    "  p.LINK_ID,\n" +
                    "  p.SHOP_ID,\n" +
                    "  p.LANE_CODE,\n" +
                    "  p.IP_ADDRESS,\n" +
                    "  p.FIGURE_NAME,\n" +
                    "  p.FIGURE_X,\n" +
                    "  p.FIGURE_Y,\n" +
                    "  p.DESCRIPTION,\n" +
                    "  p.MANAGEMENT_TYPE, p.POSITION_STATUS \n" +
                    " from Position p Where 1=1  ");
            if (id != null) {
                sql.append(" and p.SHOP_ID IN(select b.SHOP_ID from Shop b  " +
                        "                          start with b.SHOP_ID = :SHOP_ID \n" +
                        "                          connect by prior b.SHOP_ID = b.SHOP_PARENT_ID )");
                mapParams.put("SHOP_ID", id);
            }
            sql.append(" order by p.POSITION_NAME asc ");
            Query q = getSession().createSQLQuery(sql.toString());
            for (String param : mapParams.keySet()) {
                if (mapParams.get(param) instanceof List) {
                    q.setParameterList(param, (Collection) mapParams.get(param));
                } else {
                    q.setParameter(param, mapParams.get(param));
                }
            }

            List<Object> lstObj = q.list();
            List<Position> result = new ArrayList<Position>();
            if(lstObj==null||lstObj.isEmpty()){
                return result;
            }
            for (Object object : lstObj) {
                Object[] arr = (Object[]) object;
                Position tempLog = new Position();
                tempLog.setPositionId((arr[0] != null) ? Long.parseLong(arr[0].toString()) : null);
                tempLog.setPositionCode((arr[1] != null) ? arr[1].toString() : "");
                tempLog.setPositionName((arr[2] != null) ? arr[2].toString() : "");
                tempLog.setLinkId((arr[3] != null) ? Long.parseLong(arr[3].toString()) : null);
                tempLog.setShopId((arr[4] != null) ? Long.parseLong(arr[4].toString()) : null);
                tempLog.setLaneCode((arr[5] != null) ? arr[5].toString() : "");
                tempLog.setIpAddress((arr[6] != null) ? arr[6].toString() : "");
                tempLog.setFigureName((arr[7] != null) ? arr[7].toString() : "");
                tempLog.setFigureX((arr[8] != null) ? arr[8].toString() : "");
                tempLog.setFigureY((arr[9] != null) ? arr[9].toString() : "");
                tempLog.setDescription((arr[10] != null) ? arr[10].toString() : "");
                tempLog.setManagementType((arr[11] != null) ? Long.parseLong(arr[11].toString()) : null);
                tempLog.setPositionStatus((arr[12] != null) ? Long.parseLong(arr[12].toString()) : null);
                result.add(tempLog);
            }
            return result;
        }

        public List<Position> findByShopIdActive(Long id) {
            StringBuilder sql = new StringBuilder();
            HashMap<String, Object> mapParams = new HashMap<String, Object>();

            sql.append("Select s from Position s where s.positionStatus = 1 and 1=1  ");

            if (id != null) {
                sql.append(" and s.shopId = :SHOP_ID ");
                mapParams.put("SHOP_ID", id);
            }
            Query q = getSession().createQuery(sql.toString());
            for (String param : mapParams.keySet()) {
                if (mapParams.get(param) instanceof List) {
                    q.setParameterList(param, (Collection) mapParams.get(param));
                } else {
                    q.setParameter(param, mapParams.get(param));
                }
            }

            if (q.list().isEmpty()) {
                return new ArrayList<>();
            } else {
                return q.list();
            }
        }

        public List<Position> getAllWithParams(Position position, boolean search, int first, int pageSize, String sortField, Boolean desc) {
            List<Position> result = new ArrayList<Position>();
            StringBuilder sql = new StringBuilder("select \n" +
                    " p.POSITION_ID positionId,\n" +
                    "  p.POSITION_CODE positionCode,\n" +
                    "  p.POSITION_NAME positionName,\n" +
                    "  p.LINK_ID linkId,\n" +
                    "  p.SHOP_ID shopId,\n" +
                    "  p.LANE_CODE laneCode,\n" +
                    "  p.IP_ADDRESS ipAddress,\n" +
                    "  p.FIGURE_NAME figureName,\n" +
                    "  p.FIGURE_X figureX,\n" +
                    "  p.FIGURE_Y figureY,\n" +
                    "  p.DESCRIPTION description,\n" +
                    "  l.POSITION_CODE linkPostionCode,\n" +
                    "  l.POSITION_NAME linkPostionName, \n" +
                    "  p.MANAGEMENT_TYPE managementType, p.POSITION_STATUS positionStatus \n" +
                    " from Position p left join Position l on p.link_Id = l.position_Id where 1=1   ");
            HashMap<String, Object> mapParams = new HashMap<String, Object>();
            if (position != null) {
                if (position.getPositionId() != null) {
                    sql.append(" and p.POSITION_ID = :POSITION_ID ");
                    mapParams.put("POSITION_ID", position.getPositionId());
                }
                if (position.getPositionCode() != null && !position.getPositionCode().isEmpty()) {
                    sql.append(" and UPPER(p.POSITION_CODE) LIKE :POSITION_CODE ");
                    mapParams.put("POSITION_CODE", "%" + position.getPositionCode().trim().toUpperCase() + "%");
                }
                if (position.getPositionName() != null && !position.getPositionName().isEmpty()) {
                    sql.append(" and UPPER(p.POSITION_NAME) LIKE :POSITION_NAME ");
                    mapParams.put("POSITION_NAME", "%" + position.getPositionName().trim().toUpperCase() + "%");
                }
                if (position.getDescription() != null && !position.getDescription().isEmpty()) {
                    sql.append(" and UPPER(p.DESCRIPTION) LIKE :DESCRIPTION ");
                    mapParams.put("DESCRIPTION", "%" + position.getDescription().trim().toUpperCase() + "%");
                }
                if (position.getFigureX() != null && !position.getFigureX().isEmpty()) {
                    sql.append(" and UPPER(p.FIGURE_X) LIKE :FIGURE_X ");
                    mapParams.put("FIGURE_X", "%" + position.getFigureX().trim().toUpperCase() + "%");
                }
                if (position.getFigureY() != null && !position.getFigureY().isEmpty()) {
                    sql.append(" and UPPER(p.FIGURE_Y) LIKE :FIGURE_Y ");
                    mapParams.put("FIGURE_Y", "%" + position.getFigureY().trim().toUpperCase() + "%");
                }
                if (position.getLinkId() != null) {
                    sql.append(" and p.LINK_ID = :LINK_ID ");
                    mapParams.put("LINK_ID",  position.getLinkId() );
                }
                if (position.getFigureName() != null && !position.getFigureName().isEmpty()) {
                    sql.append(" and p.FIGURE_NAME = :FIGURE_NAME ");
                    mapParams.put("FIGURE_NAME",  position.getFigureName() );
                }
                if (position.getManagementType() != null) {
                    sql.append(" and p.MANAGEMENT_TYPE = :MANAGEMENT_TYPE ");
                    mapParams.put("MANAGEMENT_TYPE",  position.getManagementType() );
                }
                if (position.getLaneCode() != null && !position.getLaneCode().isEmpty()) {
                    sql.append(" and UPPER(p.LANE_CODE) LIKE :LANE_CODE ");
                    mapParams.put("LANE_CODE", "%" + position.getLaneCode().trim().toUpperCase() + "%");
                }
                if (position.getIpAddress() != null && !position.getIpAddress().isEmpty()) {
                    sql.append(" and UPPER(p.IP_ADDRESS) LIKE :IP_ADDRESS ");
                    mapParams.put("IP_ADDRESS", "%" + position.getIpAddress().trim().toUpperCase() + "%");
                }
                if (position.getShopId() != null) {
                    if (position.getShopIds() != null && !position.getShopIds().isEmpty()) {
                        position.getShopIds().add(position.getShopId());
                        sql.append(" and p.SHOP_ID in (:shopIds) ");
                        mapParams.put("shopIds", position.getShopIds());
                    } else {
                        sql.append(" and p.SHOP_ID = :SHOP_ID ");
                        mapParams.put("SHOP_ID", position.getShopId());
                    }
                }
                if (position.getPositionStatus() != null) {
                    sql.append(" and p.POSITION_STATUS = :POSITION_STATUS ");
                    mapParams.put("POSITION_STATUS", position.getPositionStatus());
                }
                if(sortField != null){
                    if(desc){
                        sql.append(" order by UPPER(" + sortField + ") desc ");
                    } else{
                        sql.append(" order by UPPER(" + sortField + ")  asc ");
                    }
                }else {
                    sql.append(" order by p.POSITION_CODE asc ");
                }

                Query q = getSession().createSQLQuery(sql.toString());

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

                List<Object> lstObj = q.list();
                if(lstObj==null||lstObj.isEmpty()){
                    return result;
                }
                for (Object object : lstObj) {
                    Object[] arr = (Object[]) object;
                    Position tempLog = new Position();
                    tempLog.setPositionId((arr[0] != null) ? Long.parseLong(arr[0].toString()) : null);
                    tempLog.setPositionCode((arr[1] != null) ? arr[1].toString() : "");
                    tempLog.setPositionName((arr[2] != null) ? arr[2].toString() : "");
                    tempLog.setLinkId((arr[3] != null) ? Long.parseLong(arr[3].toString()) : null);
                    tempLog.setShopId((arr[4] != null) ? Long.parseLong(arr[4].toString()) : null);
                    tempLog.setLaneCode((arr[5] != null) ? arr[5].toString() : "");
                    tempLog.setIpAddress((arr[6] != null) ? arr[6].toString() : "");
                    tempLog.setFigureName((arr[7] != null) ? arr[7].toString() : "");
                    tempLog.setFigureX((arr[8] != null) ? arr[8].toString() : "");
                    tempLog.setFigureY((arr[9] != null) ? arr[9].toString() : "");
                    tempLog.setDescription((arr[10] != null) ? arr[10].toString() : "");
                    tempLog.setLinkPostionCode((arr[11] != null) ? arr[11].toString() : "");
                    tempLog.setLinkPostionName((arr[12] != null) ? arr[12].toString() : "");
                    tempLog.setManagementType((arr[13] != null) ? Long.parseLong(arr[13].toString()) : null);
                    tempLog.setPositionStatus((arr[14] != null) ? Long.parseLong(arr[14].toString()) : null);
                    result.add(tempLog);
                }
//                if (q.list().isEmpty()) {
//                    return new ArrayList<>();
//                } else {
//                    return q.list();
//                }

            }
            return result;
        }
        public List<Position> getPositionNotSetting(Long shopId) {
            List<Position> result = new ArrayList<Position>();
            HashMap<String, Object> mapParams = new HashMap<String, Object>();
            StringBuilder sql = new StringBuilder("SELECT POSITION_ID,\n" +
                    "  POSITION_CODE,\n" +
                    "  POSITION_NAME,\n" +
                    "  LINK_ID,\n" +
                    "  SHOP_ID,\n" +
                    "  LANE_CODE,\n" +
                    "  IP_ADDRESS,\n" +
                    "  FIGURE_NAME,\n" +
                    "  FIGURE_X,\n" +
                    "  FIGURE_Y,\n" +
                    "  DESCRIPTION,\n" +
                    "  MANAGEMENT_TYPE\n" +
                    "FROM POSITION P \n" +
                    "WHERE P.POSITION_STATUS = 1 AND P.POSITION_ID\n" +
                    " not in(SELECT D.POSITION_ID  FROM EQUIPMENT_DETAIL D WHERE D.POSITION_ID IS NOT NULL) \n" +
                    "     ");
                if(shopId!=null){
                    sql.append(" and P.SHOP_ID = :SHOP_ID ");
                    mapParams.put("SHOP_ID", shopId);
                }

                sql.append(" order by P.POSITION_CODE asc ");
                Query q = getSession().createSQLQuery(sql.toString());
            for (String param : mapParams.keySet()) {
                if (mapParams.get(param) instanceof List) {
                    q.setParameterList(param, (Collection) mapParams.get(param));
                } else {
                    q.setParameter(param, mapParams.get(param));
                }
            }
                List<Object> lstObj = q.list();
                if(lstObj==null||lstObj.isEmpty()){
                    return result;
                }
                for (Object object : lstObj) {
                    Object[] arr = (Object[]) object;
                    Position tempLog = new Position();
                    tempLog.setPositionId((arr[0] != null) ? Long.parseLong(arr[0].toString()) : null);
                    tempLog.setPositionCode((arr[1] != null) ? arr[1].toString() : "");
                    tempLog.setPositionName((arr[2] != null) ? arr[2].toString() : "");
                    tempLog.setLinkId((arr[3] != null) ? Long.parseLong(arr[3].toString()) : null);
                    tempLog.setShopId((arr[4] != null) ? Long.parseLong(arr[4].toString()) : null);
                    tempLog.setLaneCode((arr[5] != null) ? arr[5].toString() : "");
                    tempLog.setIpAddress((arr[6] != null) ? arr[6].toString() : "");
                    tempLog.setFigureName((arr[7] != null) ? arr[7].toString() : "");
                    tempLog.setFigureX((arr[8] != null) ? arr[8].toString() : "");
                    tempLog.setFigureY((arr[9] != null) ? arr[9].toString() : "");
                    tempLog.setDescription((arr[10] != null) ? arr[10].toString() : "");
                    tempLog.setManagementType((arr[11] != null) ? Long.parseLong(arr[11].toString()) : null);
                    result.add(tempLog);
                }
            return result;
        }

        @Override
        public void saveOrUpdate(Position obj) {
            if (obj.getPositionId() != null) {
                getSession().merge(obj);
            }else {
                getSession().saveOrUpdate(obj);
            }
            getSession().flush();
        }

        @Override
        public void delete(Position obj) {
            Query q = getSession().createQuery("Delete from Position where positionId = ?");
            q.setParameter(0, obj.getPositionId());
            q.executeUpdate();
            getSession().flush();
        }

        public List<Position> getAll() {
            Query q = getSession().createQuery("Select p from Position p where p.positionStatus = 1 ");
            if (q.list().isEmpty()) {
                return new ArrayList<>();
            } else
                return q.list();
        }

        public List<Position> findByPositonCodeId(String positionCode, Long positionId) {
            StringBuilder sql = new StringBuilder();
            HashMap<String, Object> mapParams = new HashMap<String, Object>();

            sql.append("Select s from Position s where  1=1  ");

            if (positionId != null) {
                sql.append(" and s.positionId <> :positionId ");
                mapParams.put("positionId", positionId);
            }
            if (positionCode != null && !positionCode.isEmpty()) {
                sql.append(" and UPPER(s.positionCode) like :positionCode ");
                mapParams.put("positionCode", positionCode.trim().toUpperCase());
            }
            Query q = getSession().createQuery(sql.toString());
            for (String param : mapParams.keySet()) {
                if (mapParams.get(param) instanceof List) {
                    q.setParameterList(param, (Collection) mapParams.get(param));
                } else {
                    q.setParameter(param, mapParams.get(param));
                }
            }

            if (q.list().isEmpty()) {
                return new ArrayList<>();
            } else {
                return q.list();
            }
        }
    }
