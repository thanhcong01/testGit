package com.ftu.inventory.dao;

import com.ftu.admin.consumer.utils.StringUtil;
import com.ftu.constanst.InventoryConstanst;
import com.ftu.hibernate.GenericDAOHibernate;
import com.ftu.inventory.bo.GoodsStatusTransSerial;
import com.ftu.inventory.bo.ImportExportReport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;

import org.apache.poi.ss.usermodel.DateUtil;
import org.hibernate.Query;

/**
 * Created by PhamTan on 9/21/2016.
 */
public class ImportExportReportDAO extends GenericDAOHibernate<ImportExportReport, Long> {

    public ImportExportReportDAO() {
        super(ImportExportReport.class);
    }

    public List<ImportExportReport> getDataForReport(String fromDate,String toDate,Long shopId,Integer first,Integer pageSize){
    	StringBuilder sql = new StringBuilder();
    	if (fromDate == null || toDate == null || shopId == null)
    		return new ArrayList<ImportExportReport>();
		try {
//			sql.append( org.apache.commons.io.IOUtils.toString(
//					FacesContext.getCurrentInstance().getExternalContext()
//					.getResourceAsStream("/sql/report-import-export-instock.sql")));
			sql.append("SELECT PROFILE_ID id,\n" +
					"  profile_code,\n" +
					"  profile_name,\n" +
					"  unit,\n" +
					"  shop_name,\n" +
					"  equipment_group_name,\n" +
					"  1 as EQUIPMENT_STATUS,\n" +
					"  NVL(old_balance,0) old_balance,\n" +
					"  NVL(import_amount,0) import_amount,\n" +
					"  NVL(export_amount,0) export_amount,\n" +
					"  NVL(new_balance,0) new_balance\n" +
					"FROM\n" +
					"  (SELECT SHOP_ID,\n" +
					"    EQUIPMENT_PROFILE_ID PROFILE_ID ,\n" +
//					"    EQUIPMENT_STATUS ,\n" +
					"    SUM(QUANTITY) AS new_balance\n" +
					"  FROM REPORT_SHOP_GOODS_STOCK\n" +
					"  WHERE 1=1 " +
//					" and TRUNC(REPORT_CREATED_DATE) = TRUNC(:TO_DATE) \n" +
					" and TO_DATE(SUBSTR(REPORT_CREATED_DATE, 1,9 ),'dd-mon-yy') = TO_DATE(:TO_DATE,'dd-mon-yy') \n" +
//					"  and EQUIPMENT_STATUS = 1\n" +
					"  and stock_status in(1, 15, 16, 17)\n" +
					"  and SHOP_ID = :SHOP_ID\n" +
					"  GROUP BY SHOP_ID,\n" +
					"    EQUIPMENT_PROFILE_ID\n" +
//					"    ,EQUIPMENT_STATUS\n" +
					"  ORDER BY SHOP_ID,\n" +
					"    EQUIPMENT_PROFILE_ID\n" +
//					"    , EQUIPMENT_STATUS\n" +
					"  ) result\n" +
					"  \n" +
					"  full JOIN\n" +
					"  (SELECT SHOP_ID,\n" +
					"    EQUIPMENT_PROFILE_ID PROFILE_ID, \n" +
//					"    , EQUIPMENT_STATUS ,\n" +
					"    SUM(QUANTITY) AS old_balance\n" +
					"  FROM REPORT_SHOP_GOODS_STOCK\n" +
					"  WHERE 1 = 1 " +
					" and TO_DATE(SUBSTR(REPORT_CREATED_DATE, 1,9 ),'dd-mon-yy') = TO_DATE(:FROM_DATE,'dd-mon-yy') \n" +
//					" and TRUNC(REPORT_CREATED_DATE) = TRUNC(:FROM_DATE) \n" +
					"  and stock_status in(1, 15, 16, 17)\n" +
					"  and SHOP_ID = :SHOP_ID\n" +
					"  GROUP BY SHOP_ID,\n" +
					"    EQUIPMENT_PROFILE_ID\n" +
//					"    ,EQUIPMENT_STATUS\n" +
					"  ORDER BY SHOP_ID,\n" +
					"    EQUIPMENT_PROFILE_ID\n" +
//					"    ,EQUIPMENT_STATUS\n" +
					"  ) USING (SHOP_ID, PROFILE_ID)\n" +
					"  \n" +
					"  \n" +
					"full JOIN\n" +
					"  (SELECT \n" +
					"    t.TO_SHOP_ID SHOP_ID,\n" +
					"    td.GOODS_ID PROFILE_ID,\n" +
//					"    std.EQUIPMENT_STATUS EQUIPMENT_STATUS,\n" +
					"    SUM(td.QUANTITY) import_amount\n" +
					"  FROM TRANSACTION_ACTION_DETAIL td\n" +
					"  JOIN TRANSACTION_ACTION t\n" +
					"  ON td.TRANSACTION_ACTION_ID = t.TRANSACTION_ACTION_ID\n" +
					"  WHERE lower(t.TRANSACTION_ACTION_CODE) LIKE '%' ESCAPE '/'\n" +
					"  AND ( ( t.TRANSACTION_TYPE = 10 \n" +
					"  AND t.STATUS              IN ( 2 ) )\n" +
					"  OR ( 1 = 1 \n" +
					"  AND t.STATUS              IN (5, 10, 15, 20) ) )\n" +
					"  AND t.TO_SHOP_ID           = :SHOP_ID\n" +
					"  AND TO_DATE(SUBSTR(t.CREATE_DATETIME, 1,9 ),'dd-mon-yy') >= TO_DATE(:FROM_DATE,'dd-mon-yy')\n" +
					"  AND TO_DATE(SUBSTR(t.CREATE_DATETIME, 1,9 ),'dd-mon-yy')  < TO_DATE(:TO_DATE,'dd-mon-yy')\n" +
					"  GROUP BY t.TO_SHOP_ID,\n" +
					"    td.GOODS_ID \n" +
//					"    ,std.EQUIPMENT_STATUS\n" +
					"  ) USING (shop_id, PROFILE_ID)\n" +
					"  \n" +
					"  full JOIN\n" +
					"  (SELECT t.FROM_SHOP_ID shop_id,\n" +
					"    td.GOODS_ID PROFILE_ID, \n" +
//					"     std.EQUIPMENT_STATUS ,\n" +
					"    SUM(td.QUANTITY) export_amount\n" +
					"  FROM TRANSACTION_ACTION_DETAIL td\n" +
					"  JOIN TRANSACTION_ACTION t\n" +
					"  ON td.TRANSACTION_ACTION_ID = t.TRANSACTION_ACTION_ID\n" +
					"  JOIN\n" +
					"    (SELECT transaction_action_id,\n" +
					"      goods_id PROFILE_ID \n" +
//					"      , goods_status EQUIPMENT_STATUS\n" +
					"    FROM STOCK_TRANSACTION_DETAIL stdd\n" +
					"    JOIN STOCK_TRANSACTION stt\n" +
					"    ON stdd.STOCK_TRANSACTION_ID       = stt.STOCK_TRANSACTION_ID\n" +
					"    ) std ON std.transaction_action_id = t.transaction_action_id\n" +
					"  AND std.PROFILE_ID                     = td.goods_id\n" +
					"  JOIN staff staff\n" +
					"  ON staff.STAFF_ID = t.CREATE_STAFF_ID\n" +
					"  AND staff.SHOP_ID = t.CREATE_SHOP_ID\n" +
					"  JOIN shop shop\n" +
					"  ON shop.SHOP_ID = t.FROM_SHOP_ID\n" +
					"  WHERE lower(t.TRANSACTION_ACTION_CODE) LIKE '%' ESCAPE '/'\n" +
					"  AND t.FROM_SHOP_ID      = :SHOP_ID\n" +
					"  AND TO_DATE(SUBSTR(t.CREATE_DATETIME, 1,9 ),'dd-mon-yy') >= TO_DATE(:FROM_DATE,'dd-mon-yy')\n" +
					"  AND TO_DATE(SUBSTR(t.CREATE_DATETIME, 1,9 ),'dd-mon-yy')  < TO_DATE(:TO_DATE,'dd-mon-yy')\n" +
					"  AND( 1 = 1 "+//t.TRANSACTION_TYPE IN (1,2,3,4,5,6,7,8)\n" +
					"  AND t.STATUS           IN (4, 5, 9, 10,14, 15, 19, 20) )\n" +
					"  GROUP BY t.FROM_SHOP_ID,\n" +
					"    td.goods_id \n" +
//					"   , std.EQUIPMENT_STATUS \n" +
					"  ) USING (shop_id, PROFILE_ID)\n" +
					"  JOIN shop USING (shop_id)\n" +
					" JOIN\n" +
					"  (\n" +
					"  SELECT  * FROM EQUIPMENT_GROUP  JOIN EQUIPMENT_PROFILE equ USING (EQUIPMENT_GROUP_ID)\n" +
					"  ) equip USING(PROFILE_ID)\n" +
					"  \n");
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<ImportExportReport>();
		}

    	Query query = getSession().createSQLQuery(sql.toString()).addEntity(ImportExportReport.class);
		
		query.setParameter("FROM_DATE", fromDate);


		query.setParameter("TO_DATE", toDate);
		
		if (shopId != null && shopId > -1)
		{
			query.setParameter("SHOP_ID", shopId);
		}
    	if(first != null) query.setFirstResult(first);
		if(pageSize != null) query.setMaxResults(pageSize);
        List<ImportExportReport> listResult = query.list();
        if (listResult.isEmpty())
        	return new ArrayList<ImportExportReport>();
        return listResult;
    }

	public List<ImportExportReport> searchDataForReport(Date fromDate,Date toDate,Long shopId){
		StringBuilder sql = new StringBuilder();
		if (fromDate == null || toDate == null || shopId == null)
			return new ArrayList<ImportExportReport>();
		try {
			sql.append("SELECT PROFILE_ID id,\n" +
					"  profile_code,\n" +
					"  profile_name,\n" +
					"  unit,\n" +
					"  shop_name,\n" +
					"  equipment_group_name,\n" +
					"  EQUIPMENT_STATUS,\n" +
					"  NVL(old_balance,0) old_balance,\n" +
					"  NVL(import_amount,0) import_amount,\n" +
					"  NVL(export_amount,0) export_amount,\n" +
					"  NVL(new_balance,0) new_balance\n" +
					"FROM\n" +
					"  (SELECT SHOP_ID,\n" +
					"    EQUIPMENT_PROFILE_ID PROFILE_ID ,\n" +
					"    EQUIPMENT_STATUS ,\n" +
					"    SUM(QUANTITY) AS new_balance\n" +
					"  FROM REPORT_SHOP_GOODS_STOCK\n" +
					"  WHERE 1=1 " +
//					" and TRUNC(REPORT_CREATED_DATE) = TRUNC(:TO_DATE) \n" +
					"  and EQUIPMENT_STATUS = 1\n" +
					"  and SHOP_ID = :SHOP_ID\n" +
					"  GROUP BY SHOP_ID,\n" +
					"    EQUIPMENT_PROFILE_ID,\n" +
					"    EQUIPMENT_STATUS\n" +
					"  ORDER BY SHOP_ID,\n" +
					"    EQUIPMENT_PROFILE_ID,\n" +
					"    EQUIPMENT_STATUS\n" +
					"  ) result\n" +
					"  \n" +
					"  full JOIN\n" +
					"  (SELECT SHOP_ID,\n" +
					"    EQUIPMENT_PROFILE_ID PROFILE_ID ,\n" +
					"    EQUIPMENT_STATUS ,\n" +
					"    SUM(QUANTITY) AS old_balance\n" +
					"  FROM REPORT_SHOP_GOODS_STOCK\n" +
					"  WHERE 1 = 1" +
//					" and TRUNC(REPORT_CREATED_DATE) = TRUNC(:FROM_DATE) \n" +
					"  and stock_status = 1\n" +
					"  and SHOP_ID = :SHOP_ID\n" +
					"  GROUP BY SHOP_ID,\n" +
					"    EQUIPMENT_PROFILE_ID,\n" +
					"    EQUIPMENT_STATUS\n" +
					"  ORDER BY SHOP_ID,\n" +
					"    EQUIPMENT_PROFILE_ID,\n" +
					"    EQUIPMENT_STATUS\n" +
					"  ) USING (SHOP_ID, PROFILE_ID,EQUIPMENT_STATUS)\n" +
					"  \n" +
					"  \n" +
					"full JOIN\n" +
					"  (SELECT \n" +
					"    t.TO_SHOP_ID SHOP_ID,\n" +
					"    td.GOODS_ID PROFILE_ID,\n" +
					"    std.EQUIPMENT_STATUS EQUIPMENT_STATUS,\n" +
					"    SUM(td.QUANTITY) import_amount\n" +
					"  FROM TRANSACTION_ACTION_DETAIL td\n" +
					"  JOIN TRANSACTION_ACTION t\n" +
					"  ON td.TRANSACTION_ACTION_ID = t.TRANSACTION_ACTION_ID\n" +
					"  JOIN\n" +
					"    (SELECT stt.transaction_action_id,\n" +
					"      stdd.goods_id PROFILE_ID,\n" +
					"      stdd.goods_status EQUIPMENT_STATUS\n" +
					"    FROM STOCK_TRANSACTION_DETAIL stdd\n" +
					"    JOIN STOCK_TRANSACTION stt\n" +
					"    ON stdd.STOCK_TRANSACTION_ID       = stt.STOCK_TRANSACTION_ID\n" +
					"    ) std ON std.transaction_action_id = t.transaction_action_id\n" +
					"  AND std.PROFILE_ID                     = td.GOODS_ID\n" +
					"  JOIN staff staff\n" +
					"  ON staff.STAFF_ID = t.CREATE_STAFF_ID\n" +
					"  AND staff.SHOP_ID = t.CREATE_SHOP_ID\n" +
					"  JOIN shop shop\n" +
					"  ON shop.SHOP_ID = t.TO_SHOP_ID\n" +
					"  WHERE lower(t.TRANSACTION_ACTION_CODE) LIKE '%' ESCAPE '/'\n" +
					"  AND ( ( t.TRANSACTION_TYPE = 1\n" +
					"  AND t.STATUS              IN (2, 6) )\n" +
					"  OR ( t.TRANSACTION_TYPE    > 1\n" +
					"  AND t.STATUS              IN (5, 10,14) ) )\n" +
					"  AND t.TO_SHOP_ID           = :SHOP_ID\n" +
//					"  AND td.CREATE_DATETIME >= :FROM_DATE\n" +
//					"  AND td.CREATE_DATETIME  <= :TO_DATE\n" +
					"  GROUP BY t.TO_SHOP_ID,\n" +
					"    td.GOODS_ID,\n" +
					"    std.EQUIPMENT_STATUS\n" +
					"  ) USING (shop_id, PROFILE_ID, EQUIPMENT_STATUS)\n" +
					"  \n" +
					"  full JOIN\n" +
					"  (SELECT t.FROM_SHOP_ID shop_id,\n" +
					"    td.GOODS_ID PROFILE_ID,\n" +
					"    std.EQUIPMENT_STATUS ,\n" +
					"    SUM(td.QUANTITY) export_amount\n" +
					"  FROM TRANSACTION_ACTION_DETAIL td\n" +
					"  JOIN TRANSACTION_ACTION t\n" +
					"  ON td.TRANSACTION_ACTION_ID = t.TRANSACTION_ACTION_ID\n" +
					"  JOIN\n" +
					"    (SELECT transaction_action_id,\n" +
					"      goods_id PROFILE_ID,\n" +
					"      goods_status EQUIPMENT_STATUS\n" +
					"    FROM STOCK_TRANSACTION_DETAIL stdd\n" +
					"    JOIN STOCK_TRANSACTION stt\n" +
					"    ON stdd.STOCK_TRANSACTION_ID       = stt.STOCK_TRANSACTION_ID\n" +
					"    ) std ON std.transaction_action_id = t.transaction_action_id\n" +
					"  AND std.PROFILE_ID                     = td.goods_id\n" +
					"  JOIN staff staff\n" +
					"  ON staff.STAFF_ID = t.CREATE_STAFF_ID\n" +
					"  AND staff.SHOP_ID = t.CREATE_SHOP_ID\n" +
					"  JOIN shop shop\n" +
					"  ON shop.SHOP_ID = t.FROM_SHOP_ID\n" +
					"  WHERE lower(t.TRANSACTION_ACTION_CODE) LIKE '%' ESCAPE '/'\n" +
					"  AND t.FROM_SHOP_ID      = :SHOP_ID\n" +
//					"  AND td.CREATE_DATETIME >= :FROM_DATE\n" +
//					"  AND td.CREATE_DATETIME  <= :TO_DATE\n" +
					"  AND(t.TRANSACTION_TYPE IN (1,2,3,4,5,6,7,8)\n" +
					"  AND t.STATUS           IN (4, 5, 9, 10,14) )\n" +
					"  GROUP BY t.FROM_SHOP_ID,\n" +
					"    td.goods_id ,\n" +
					"    std.EQUIPMENT_STATUS \n" +
					"  ) USING (shop_id, PROFILE_ID, EQUIPMENT_STATUS)\n" +
					"  JOIN shop USING (shop_id)\n" +
					" JOIN\n" +
					"  (\n" +
					"  SELECT  * FROM EQUIPMENT_GROUP  JOIN EQUIPMENT_PROFILE equ USING (EQUIPMENT_GROUP_ID)\n" +
					"  ) equip USING(PROFILE_ID)\n" +
					"  \n");
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<ImportExportReport>();
		}

		Query query = getSession().createSQLQuery(sql.toString()).addEntity(ImportExportReport.class);

//		query.setParameter("FROM_DATE", fromDate);
//
//		query.setParameter("TO_DATE", toDate);

		if (shopId != null && shopId > -1)
		{
			query.setParameter("SHOP_ID", shopId);
		}
		List<ImportExportReport> listResult = query.list();
		if (listResult.isEmpty())
			return new ArrayList<ImportExportReport>();
		return listResult;
	}

//    public Integer getReportSize(Date fromDate,Date toDate,Long shopId){
//    	return getDataForReport(fromDate,toDate,shopId,null,null).size();
//
//    }
}
