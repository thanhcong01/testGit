package com.ftu.admin.consumer;

import java.util.Date;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ftu.admin.consumer.entity.LogInputParams;
import com.ftu.admin.consumer.entity.LogRelatedEntity;
import com.ftu.admin.consumer.entity.SecAccessLogOutput;
//import com.ftu.admin.consumer.config.ClientConsumerConfig;
//import com.ftu.admin.consumer.entity.LogInputParams;
//import com.ftu.admin.consumer.entity.LogRelatedEntity;
//import com.ftu.admin.consumer.entity.SecAccessLogOutput;
import com.ftu.admin.consumer.entity.TransEntity;
import com.ftu.admin.consumer.log.ActionLogDelete;
import com.ftu.admin.consumer.log.ActionLogInsert;
import com.ftu.admin.consumer.log.ActionLogUpdate;
import com.ftu.admin.consumer.log.SecModuleLogEntity;
//import com.ftu.admin.consumer.utils.ConsumerPropeties;
import com.ftu.admin.consumer.utils.ClientConsumerConfig;
import com.ftu.utils.ResourceBundleUtil;

public class LogsConsumer {

	public static int accessLog(TransEntity transEntity, String strAssetObj, String strAssetCode, String strAppCode)
			throws Exception {
		Response response = null;
		try {
			Form form = new Form();
			form.param("granted_obj", strAssetObj);
			form.param("grantee_code", strAssetCode);
			form.param("app_code", strAppCode);
			Invocation.Builder builder = ClientConsumerConfig.clientRequest(transEntity,
					ResourceBundleUtil.getString("resource.url") + ResourceBundleUtil.getString("path.logs-accesslog"),
					MediaType.APPLICATION_JSON);
			Entity<Form> entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE);
			response = builder.post(entity);
			if (200 == response.getStatus())
				return 1;
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			response.close();
		}
		return 0;
	}

	public static void logInsert(TransEntity transEntity, String strTableName, String strPrimaryKeyValue,
			String strPrimaryKeyName, String strModuleName) throws Exception {
		try {
			LogInputParams params = new LogInputParams();
			params.setIpAddress(transEntity.getRemoteAddr());
			params.setModuleName(strModuleName);
			params.setPrimaryKeyName(strPrimaryKeyName);
			params.setPrimaryKeyValue(strPrimaryKeyValue);
			params.setTableName(strTableName);
			params.setvObjectRelated(null);

			Invocation.Builder builder = ClientConsumerConfig.clientRequest(transEntity,
					ResourceBundleUtil.getString("resource.url") + ResourceBundleUtil.getString("path.logs-insert"),
					MediaType.APPLICATION_JSON);
			Entity<LogInputParams> entity = Entity.entity(params, MediaType.APPLICATION_JSON);
			builder.post(entity).close();
			;
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
		}
	}

	public static void logInsertRelated(TransEntity transEntity, String strTableName, String strPrimaryKeyValue,
			String strPrimaryKeyName, String strModuleName, LogRelatedEntity vObjectRelated) throws Exception {
		try {
			LogInputParams params = new LogInputParams();
			params.setIpAddress(transEntity.getRemoteAddr());
			params.setModuleName(strModuleName);
			params.setPrimaryKeyName(strPrimaryKeyName);
			params.setPrimaryKeyValue(strPrimaryKeyValue);
			params.setTableName(strTableName);
			params.setvObjectRelated(vObjectRelated);

			Invocation.Builder builder = ClientConsumerConfig
					.clientRequest(transEntity,
							ResourceBundleUtil.getString("resource.url")
									+ ResourceBundleUtil.getString("path.logs-insert-related"),
							MediaType.APPLICATION_JSON);
			Entity<LogInputParams> entity = Entity.entity(params, MediaType.APPLICATION_JSON);
			builder.post(entity).close();
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
		}
	}

	public static LogRelatedEntity logActionBeforeUpdate(TransEntity transEntity, String strTableName,
			String strPrimaryKeyValue, String strPrimaryKeyName, String strModuleName, LogRelatedEntity objectRelated)
			throws Exception {
		Response response = null;
		try {
			LogInputParams params = new LogInputParams();
			params.setIpAddress(transEntity.getRemoteAddr());
			params.setModuleName(strModuleName);
			params.setPrimaryKeyName(strPrimaryKeyName);
			params.setPrimaryKeyValue(strPrimaryKeyValue);
			params.setTableName(strTableName);
			params.setvObjectRelated(objectRelated);

			Invocation.Builder builder = ClientConsumerConfig
					.clientRequest(transEntity,
							ResourceBundleUtil.getString("resource.url")
									+ ResourceBundleUtil.getString("path.logs-before-update"),
							MediaType.APPLICATION_JSON);
			Entity<LogInputParams> entity = Entity.entity(params, MediaType.APPLICATION_JSON);
			response = builder.post(entity);
			return response.readEntity(new GenericType<LogRelatedEntity>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			response.close();
		}
	}

	public static LogRelatedEntity logActionBeforeUpdateRelated(TransEntity transEntity, String strTableName,
			String strPrimaryKeyValue, String strPrimaryKeyName, String strModuleName, LogRelatedEntity objectRelated)
			throws Exception {
		Response response = null;
		try {
			LogInputParams params = new LogInputParams();
			params.setIpAddress(transEntity.getRemoteAddr());
			params.setModuleName(strModuleName);
			params.setPrimaryKeyName(strPrimaryKeyName);
			params.setPrimaryKeyValue(strPrimaryKeyValue);
			params.setTableName(strTableName);
			params.setvObjectRelated(objectRelated);

			Invocation.Builder builder = ClientConsumerConfig.clientRequest(transEntity,
					ResourceBundleUtil.getString("resource.url")
							+ ResourceBundleUtil.getString("path.logs-before-update-related"),
					MediaType.APPLICATION_JSON);
			Entity<LogInputParams> entity = Entity.entity(params, MediaType.APPLICATION_JSON);
			response = builder.post(entity);
			return response.readEntity(new GenericType<LogRelatedEntity>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			response.close();
		}
	}

	public static void logActionAfterUpdate(TransEntity transEntity, String strTableName, String strPrimaryKeyValue,
			String strPrimaryKeyName, String strModuleName, LogRelatedEntity objectRelated) throws Exception {
		try {
			LogInputParams params = new LogInputParams();
			params.setIpAddress(transEntity.getRemoteAddr());
			params.setModuleName(strModuleName);
			params.setPrimaryKeyName(strPrimaryKeyName);
			params.setPrimaryKeyValue(strPrimaryKeyValue);
			params.setTableName(strTableName);
			params.setvObjectRelated(objectRelated);

			Invocation.Builder builder = ClientConsumerConfig
					.clientRequest(transEntity,
							ResourceBundleUtil.getString("resource.url")
									+ ResourceBundleUtil.getString("path.logs-after-update"),
							MediaType.APPLICATION_JSON);
			Entity<LogInputParams> entity = Entity.entity(params, MediaType.APPLICATION_JSON);
			builder.post(entity).close();
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
		}
	}

	public static void logActionDelete(TransEntity transEntity, String strTableName, String strPrimaryKeyValue,
			String strPrimaryKeyName, String strModuleName, LogRelatedEntity objectRelated) throws Exception {
		try {
			LogInputParams params = new LogInputParams();
			params.setIpAddress(transEntity.getRemoteAddr());
			params.setModuleName(strModuleName);
			params.setPrimaryKeyName(strPrimaryKeyName);
			params.setPrimaryKeyValue(strPrimaryKeyValue);
			params.setTableName(strTableName);
			params.setvObjectRelated(objectRelated);

			Invocation.Builder builder = ClientConsumerConfig.clientRequest(transEntity,
					ResourceBundleUtil.getString("resource.url") + ResourceBundleUtil.getString("path.logs-delete"),
					MediaType.APPLICATION_JSON);
			Entity<LogInputParams> entity = Entity.entity(params, MediaType.APPLICATION_JSON);
			builder.post(entity).close();
			;
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
		}
	}

	public static SecAccessLogOutput logAccessDashboard(TransEntity transEntity) throws Exception {
		Response response = null;
		try {
			Form form = new Form();
			Invocation.Builder builder = ClientConsumerConfig.clientRequest(transEntity,
					ResourceBundleUtil.getString("resource.url")
							+ ResourceBundleUtil.getString("path.logs-accesslog-dashboard"),
					MediaType.APPLICATION_JSON);
			Entity<Form> entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE);
			response = builder.post(entity);
			SecAccessLogOutput enti = response.readEntity(SecAccessLogOutput.class);
			if (200 == response.getStatus())
				return enti;
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			response.close();
		}
		return null;
	}

	public static int logInsert(TransEntity transEntity, Object[] cls, String moduleCode, Date logDate)
			throws Exception {
		try {
			SecModuleLogEntity logData = ActionLogInsert.buildLog(new Long(transEntity.getIdentityId()), cls,
					moduleCode, logDate, transEntity.getRemoteAddr(), transEntity.getTransId());

			Invocation.Builder builder = ClientConsumerConfig.clientRequest(transEntity,
					ResourceBundleUtil.getString("resource.url") + ResourceBundleUtil.getString("path.log-insert"),
					MediaType.APPLICATION_JSON);
			Entity<SecModuleLogEntity> entity = Entity.entity(logData, MediaType.APPLICATION_JSON);
			Response response = builder.post(entity);
			response.close();
			return response.getStatus();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
		}
	}

	public static int logDelete(TransEntity transEntity, Object[] cls, String moduleCode, Date logDate)
			throws Exception {
		try {
			SecModuleLogEntity logData = ActionLogDelete.buildLog(new Long(transEntity.getIdentityId()), cls,
					moduleCode, logDate, transEntity.getRemoteAddr(), transEntity.getTransId());

			Invocation.Builder builder = ClientConsumerConfig.clientRequest(transEntity,
					ResourceBundleUtil.getString("resource.url") + ResourceBundleUtil.getString("path.log-update"),
					MediaType.APPLICATION_JSON);
			Entity<SecModuleLogEntity> entity = Entity.entity(logData, MediaType.APPLICATION_JSON);
			Response response = builder.post(entity);
			response.close();
			return response.getStatus();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
		}
	}

	public static int logUpdate(TransEntity transEntity, Object[] oldCls, Object[] newCls, String moduleCode,
			Date logDate) throws Exception {
		try {
			SecModuleLogEntity logData = ActionLogUpdate.buildLog(new Long(transEntity.getIdentityId()), oldCls, newCls,
					moduleCode, logDate, transEntity.getRemoteAddr(), transEntity.getTransId());

			Invocation.Builder builder = ClientConsumerConfig.clientRequest(transEntity,
					ResourceBundleUtil.getString("resource.url") + ResourceBundleUtil.getString("path.log-update"),
					MediaType.APPLICATION_JSON);
			Entity<SecModuleLogEntity> entity = Entity.entity(logData, MediaType.APPLICATION_JSON);
			Response response = builder.post(entity);
			response.close();
			return response.getStatus();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
		}
	}
}
