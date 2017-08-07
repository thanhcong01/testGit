package com.ftu.admin.consumer.log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

//import com.fasterxml.jackson.databind.ObjectMapper;

public class ActionLogDelete {
	static SimpleDateFormat FM_DATE_TIME = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	public static SecModuleLogEntity buildLog(Long userId, Object[] cls, String moduleCode, Date logDate,
			String ipAddress, String transId) {
		ActionLogDelete actionLog = new ActionLogDelete();
		return actionLog.buildModuleLog(userId, cls, moduleCode, logDate, ipAddress, transId);
	}

	private SecModuleLogEntity buildModuleLog(Long userId, Object[] cls, String moduleCode, Date logDate,
			String ipAddress, String transId) {
		try {
			Long logId = null;
			List<SecTableLogEntity> lstTable = new ArrayList<SecTableLogEntity>();
			SecModuleLogEntity module = new SecModuleLogEntity(logId, moduleCode, userId, logDate, ipAddress, transId);
			SecTableLogEntity table = null;
			for (Object obj : cls) {
				table = buildTableLog(obj);
				lstTable.add(table);
			}
			module.setSecTableLogEntitys(lstTable);
			module.setActionType("D");
			return module;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private SecTableLogEntity buildTableLog(Object cls) {
		try {
			Long changeId = null;
			Long logId = null;
			String rowId = System.currentTimeMillis() + "ROWID";
			String tableName = cls.getClass().getName();

			SecTableLogEntity table = new SecTableLogEntity(changeId, logId, tableName, rowId);
			List<SecColumnLogEntity> columns = buildColumnLog(cls);
			table.setSecColumnLogEntitys(columns);
			table.setActionType("Delete");
			return table;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<SecColumnLogEntity> buildColumnLog(Object cls) {
		try {
			List<SecColumnLogEntity> lstColumnLog = new ArrayList<SecColumnLogEntity>();
			SecColumnLogEntity column = null;

			ObjectMapper mapper = new ObjectMapper();
			mapper.setDateFormat(ActionLogDelete.FM_DATE_TIME);
			Map<String, Object> props = mapper.convertValue(cls, Map.class);
			for (String columnName : props.keySet()) {
				Object val = props.get(columnName);
				if (val != null) {
					String oldValue = val.toString();
					column = new SecColumnLogEntity(null, columnName, oldValue, "");
					lstColumnLog.add(column);
				}
			}

			return lstColumnLog;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
