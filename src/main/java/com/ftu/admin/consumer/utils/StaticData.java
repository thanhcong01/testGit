package com.ftu.admin.consumer.utils;

import java.util.HashMap;

public class StaticData {
	public static String RESPONSE_STATUS_SUCCESS = "1";
	public static String RESPONSE_STATUS_FAILUE = "0";

	public static String STATUS_ACTIVE = "1";
	public static String STATUS_DEACTIVE = "0";

	public static String OBJECT_TABLE = "SEC_OBJECT";
	// -----Identity/Access:----
	public static String OBJECT_SEC_USER = "SEC_USER";
	public static String OBJECT_SEC_CHANNEL = "SEC_CHANNEL";
	public static String OBJECT_ORACLE_LDAP = "ORACLE_LDAP";
	public static String OBJECT_OPEN_LDAP = "OPEN_LDAP";
	// ----Grantee:----
	public static String OBJECT_SEC_IDENTITY = "SEC_IDENTITY";
	public static String OBJECT_SEC_ORGANIZATION = "SEC_ORGANIZATION";
	public static String OBJECT_SEC_ROLE = "SEC_ROLE";
	// ----Asset:----
	public static String OBJECT_SEC_APP = "SEC_APPLICATION";
	public static String OBJECT_SEC_SITEMAP = "SEC_SITEMAP";
	public static String OBJECT_SEC_BUS_API = "SEC_BUS_API";
	public static String OBJECT_SEC_IP = "SEC_IP";
	public static String OBJECT_SEC_SCHEDULE = "SEC_SCHEDULE";
	// ----Permission----
	public static String OBJECT_SEC_ATTRIBUTE_GROUP = "SEC_ATTRIBUTE_GROUP";
	public static String OBJECT_SEC_PERMISSION_GRANTED = "SEC_PERMISSION_GRANTED";
	public static String OBJECT_SEC_PRIVILEGE = "SEC_PRIVILEGE";
	public static String OBJECT_TOLL = "TOLL";
	// public static String ASSET_CODE_COLUMN_NAME = "CODE";

	public static HashMap<String, String> mapAssetCode = new HashMap<String, String>();
	public static HashMap<String, String> mapAssetPK = new HashMap<String, String>();

	static {
		mapAssetCode.put(OBJECT_SEC_SITEMAP, "CODE");
		mapAssetCode.put(OBJECT_SEC_APP, "ACODE");
		mapAssetCode.put(OBJECT_SEC_BUS_API, "CODE");
		mapAssetCode.put(OBJECT_SEC_SCHEDULE, "SCHEDULE_ID");
		mapAssetCode.put(OBJECT_SEC_IP, "CODE");
		mapAssetCode.put(OBJECT_SEC_ROLE, "CODE");
		mapAssetCode.put(OBJECT_TABLE, "CODE");
		mapAssetCode.put(OBJECT_SEC_ORGANIZATION, "CODE");
		mapAssetCode.put(OBJECT_SEC_ATTRIBUTE_GROUP, "CODE");
		mapAssetCode.put(OBJECT_SEC_PRIVILEGE, "CODE");
		mapAssetCode.put(OBJECT_TOLL, "TOLL_CODE");
	}

	static {
		mapAssetPK.put(OBJECT_SEC_SITEMAP, "SITE_MAP_ID");
		mapAssetPK.put(OBJECT_SEC_APP, "APP_ID");
		mapAssetPK.put(OBJECT_SEC_BUS_API, "BUS_API_ID");
		mapAssetPK.put(OBJECT_SEC_SCHEDULE, "SCHEDULE_ID");
		mapAssetPK.put(OBJECT_SEC_IP, "IP_ID");
		mapAssetPK.put(OBJECT_SEC_ROLE, "ROLE_ID");
		mapAssetPK.put(OBJECT_TABLE, "OBJECT_ID");
		mapAssetPK.put(OBJECT_SEC_ORGANIZATION, "ORG_ID");
		mapAssetPK.put(OBJECT_SEC_ATTRIBUTE_GROUP, "AGROUP_ID");
		mapAssetPK.put(OBJECT_SEC_PRIVILEGE, "PRIVILEGE_ID");
		mapAssetCode.put(OBJECT_TOLL, "TOLL_ID");
	}

}
