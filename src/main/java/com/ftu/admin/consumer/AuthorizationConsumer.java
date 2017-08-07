package com.ftu.admin.consumer;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ftu.admin.consumer.config.ClientConsumerConfig;
import com.ftu.admin.consumer.entity.AttributeOutput;
import com.ftu.admin.consumer.entity.AuthenticationOutput;
import com.ftu.admin.consumer.entity.IdentityOutput;
import com.ftu.admin.consumer.entity.OrganizationOutput;
import com.ftu.admin.consumer.entity.PermissionOutput;
import com.ftu.admin.consumer.entity.SiteMapOutput;
import com.ftu.admin.consumer.entity.TransEntity;
import com.ftu.admin.consumer.utils.ConsumerPropeties;

public class AuthorizationConsumer {

	@Context
	public static void main(String[] args) {
		long a = System.currentTimeMillis();

		// getTreeSiteMapByApp("VETC.SEC");
		try {
			AuthenticationOutput out1 = AuthenticationConsumer.login(null, "haitx", "yXYg+sJy14RDpo4beVdRW6E6L9Q=",
					"VETC.SEC");
			// PermissionOutput out = getPermissionsByRole("ADMINISTRATOR",
			// "SEC_SITEMAP");
			AuthorizationConsumer.getAllIdentities(null);
			// OrganizationOutput out = getOrganizations();

			// System.out.println(out.getErrorMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// getPermissionsByRole("ADMINISTRATOR");
		// getPermissionByRole("ADMINISTRATOR", "SEC_SITEMAP", "SEC.REPORT_G");
		//
		// allUsers();

		// System.out.println(out);
		// System.out.println(out.getErrorMessage());

		System.out.println(System.currentTimeMillis() - a);
	}

	public static SiteMapOutput getTreeSiteMapByApp(TransEntity transEntity, String strAppCode) throws Exception {
		SiteMapOutput output = null;
		Response response = null;

		Invocation.Builder builder = ClientConsumerConfig.clientRequest(transEntity,
				ConsumerPropeties.getPath("path.sitemaps-by-app") + "/" + strAppCode, MediaType.APPLICATION_JSON);

		try {
			response = builder.get();
			output = response.readEntity(new GenericType<SiteMapOutput>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("api response status: " + response.getStatus());
			throw e;
		} finally {
			response.close();
		}
		return output;
	}

	public static SiteMapOutput getTreeSiteMapForRole(TransEntity transEntity, String strAppCode) throws Exception {
		SiteMapOutput output = null;
		Response response = null;

		Invocation.Builder builder = ClientConsumerConfig.clientRequest(transEntity,
				ConsumerPropeties.getPath("path.sitemaps-for-role") + "/" + strAppCode, MediaType.APPLICATION_JSON);

		try {
			response = builder.get();
			output = response.readEntity(new GenericType<SiteMapOutput>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("api response status: " + response.getStatus());
			throw e;
		} finally {
			response.close();
		}
		return output;
	}

	public static SiteMapOutput getTreeSiteMapForRolePrl(TransEntity transEntity, String strAppCode,
			String strHasPrivilege) throws Exception {
		SiteMapOutput output = null;
		Response response = null;

		Invocation.Builder builder = ClientConsumerConfig.clientRequest(transEntity,
				ConsumerPropeties.getPath("path.sitemaps-for-role-prl") + "/" + strAppCode + "&" + strHasPrivilege,
				MediaType.APPLICATION_JSON);

		try {
			response = builder.get();
			output = response.readEntity(new GenericType<SiteMapOutput>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("api response status: " + response.getStatus());
			throw e;
		} finally {
			response.close();
		}
		return output;
	}

	public static SiteMapOutput getTreeSiteMapByRolesAndApp(TransEntity transEntity, String strRoleCodes,
			String strAppCode) throws Exception {
		SiteMapOutput output = null;
		Response response = null;

		Invocation.Builder builder = ClientConsumerConfig.clientRequest(transEntity,
				ConsumerPropeties.getPath("path.sitemaps-by-grantee-app") + "/" + strRoleCodes + "&" + strAppCode,
				MediaType.APPLICATION_JSON);

		try {
			response = builder.get();
			output = response.readEntity(new GenericType<SiteMapOutput>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("api response status: " + response.getStatus());
			throw e;
		} finally {
			response.close();
		}
		return output;
	}

	public static SiteMapOutput getTreeSiteMapForLeaves(TransEntity transEntity, String strLeavesCode) throws Exception {
		SiteMapOutput output = null;
		Response response = null;

		Invocation.Builder builder = ClientConsumerConfig.clientRequest(transEntity,
				ConsumerPropeties.getPath("path.sitemaps-leaves") + "/" + strLeavesCode, MediaType.APPLICATION_JSON);

		try {
			response = builder.get();
			output = response.readEntity(new GenericType<SiteMapOutput>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("api response status: " + response.getStatus());
			throw e;
		} finally {
			response.close();
		}
		return output;
	}

	public static OrganizationOutput getOrganizations(TransEntity transEntity) throws Exception {
		OrganizationOutput output = null;
		Response response = null;

		Invocation.Builder builder = ClientConsumerConfig.clientRequest(transEntity,
				ConsumerPropeties.getPath("path.organizations"), MediaType.APPLICATION_JSON);

		try {
			response = builder.get();
			output = response.readEntity(new GenericType<OrganizationOutput>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("api response status: " + response.getStatus());
			throw e;
		} finally {
			response.close();
		}
		return output;
	}

	public static IdentityOutput getAllIdentities(TransEntity transEntity) throws Exception {
		IdentityOutput output = null;
		Response response = null;

		Invocation.Builder builder = ClientConsumerConfig.clientRequest(transEntity,
				ConsumerPropeties.getPath("path.identities"), MediaType.APPLICATION_JSON);

		try {
			response = builder.get();
			output = response.readEntity(new GenericType<IdentityOutput>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("api response status: " + response.getStatus());
			throw e;
		} finally {
			response.close();
		}
		return output;
	}

	public static PermissionOutput getPermissionsByRole(TransEntity transEntity, String strRoleCode,
			String strGrantedObj) throws Exception {
		PermissionOutput output = null;
		Response response = null;
		Invocation.Builder builder = ClientConsumerConfig.clientRequest(
				transEntity,
				ConsumerPropeties.getPath("path.permissions-by-role") + "/"
						+ ConsumerPropeties.getProperty("consumer.app") + "&" + strGrantedObj,
				MediaType.APPLICATION_JSON);
		try {
			response = builder.get();
			output = response.readEntity(new GenericType<PermissionOutput>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("api response status: " + response.getStatus());
			throw e;
		} finally {
			response.close();
		}
		return output;
	}

	public static PermissionOutput getPermissionByRole(TransEntity transEntity, String strRoleCode,
			String strGrantedObj, String strGrantedCode) throws Exception {
		PermissionOutput output = null;
		Response response = null;

		Invocation.Builder builder = ClientConsumerConfig.clientRequest(
				transEntity,
				ConsumerPropeties.getPath("path.permission-by-role") + "/"
						+ ConsumerPropeties.getProperty("consumer.app") + "&" + strGrantedObj + "&" + strGrantedCode,
				MediaType.APPLICATION_JSON);

		try {
			response = builder.get();
			output = response.readEntity(new GenericType<PermissionOutput>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("api response status: " + response.getStatus());
			throw e;
		} finally {
			response.close();
		}
		return output;
	}

	public static PermissionOutput getPermissionsByGrantee(TransEntity transEntity, String strGranteeObj,
			String strGranteeCodes, String strGrantedObj) throws Exception {
		PermissionOutput output = null;
		Response response = null;
		Invocation.Builder builder = ClientConsumerConfig.clientRequest(
				transEntity,
				ConsumerPropeties.getPath("path.permissions-by-grantee") + "/"
						+ ConsumerPropeties.getProperty("consumer.app") + "&" + strGranteeObj + "&" + strGranteeCodes
						+ "&" + strGrantedObj, MediaType.APPLICATION_JSON);
		try {
			response = builder.get();
			output = response.readEntity(new GenericType<PermissionOutput>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("api response status: " + response.getStatus());
			throw e;
		} finally {
			response.close();
		}
		return output;
	}

	public static PermissionOutput getPermissionByGrantee(TransEntity transEntity, String strGranteeObj,
			String strGranteeCodes, String strGrantedObj, String strGrantedCodes) throws Exception {
		PermissionOutput output = null;
		Response response = null;

		Invocation.Builder builder = ClientConsumerConfig.clientRequest(
				transEntity,
				ConsumerPropeties.getPath("path.permission-by-grantee") + "/"
						+ ConsumerPropeties.getProperty("consumer.app") + "&" + strGranteeObj + "&" + strGranteeCodes
						+ "&" + strGrantedObj + "&" + strGrantedCodes, MediaType.APPLICATION_JSON);

		try {
			response = builder.get();
			output = response.readEntity(new GenericType<PermissionOutput>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("api response status: " + response.getStatus());
			throw e;
		} finally {
			response.close();
		}
		return output;
	}

	public static AttributeOutput getAttributesByApp(TransEntity transEntity, String strAssetRefCode) throws Exception {
		AttributeOutput output = null;
		Response response = null;

		Invocation.Builder builder = ClientConsumerConfig.clientRequest(
				transEntity,
				ConsumerPropeties.getPath("path.attributes-by-app") + "/"
						+ ConsumerPropeties.getProperty("consumer.app"), MediaType.APPLICATION_JSON);

		try {
			response = builder.get();
			output = response.readEntity(new GenericType<AttributeOutput>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("api response status: " + response.getStatus());
			throw e;
		} finally {
			response.close();
		}
		return output;
	}

}
