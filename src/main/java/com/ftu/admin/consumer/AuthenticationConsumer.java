package com.ftu.admin.consumer;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.ftu.admin.consumer.config.ClientConsumerConfig;
import com.ftu.admin.consumer.entity.AuthenticationOutput;
import com.ftu.admin.consumer.entity.IPAddressEntity;
import com.ftu.admin.consumer.entity.IPAddressOutput;
import com.ftu.admin.consumer.entity.IdentityEntity;
import com.ftu.admin.consumer.entity.PermissionEntity;
import com.ftu.admin.consumer.entity.PermissionOutput;
import com.ftu.admin.consumer.entity.TransEntity;
import com.ftu.admin.consumer.utils.ConsumerPropeties;
import com.ftu.admin.consumer.utils.ConsumerUtil;

public class AuthenticationConsumer {

	@Context
	public static void main(String[] args) {
		long a = System.currentTimeMillis();

		try {
			AuthenticationOutput out = loginJson(null, "admin", "I9QvXz9mSYssj/TCC4xayCbkcUY=", "VETC.SEC");
			System.out.println("Error: " + out.getErrorCode());
			String orgCode = out.getIdentityEntity().getOrganization().getCode();
			PermissionOutput pem = AuthorizationConsumer.getPermissionsByGrantee(out.getTransEntity(),
					"SEC_ORGANIZATION", orgCode, "SEC_IP");
			if (pem.getPermissionsData() != null) {
				for (PermissionEntity pe : pem.getPermissionsData()) {
					System.out.println(pe.getGrantedRefCode() + " - " + pe.getPrivilegeCode());
					IPAddressOutput ip = ResourceConsumer.getIpAddressForGroup(out.getTransEntity(),
							pe.getGrantedRefCode());
					for (IPAddressEntity i : ip.getIpAddress()) {
						System.out.println(i.getIpAddress() + "-" + i.getIpGroupCode());
					}
				}
			}
			logout(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// AuthenticationOutput out = changePassword("haitx", "haitx", "haitx");
		// try {
		// System.out.println(StringUtil.encrypt("abc","SHA"));
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// allUsers();

		// System.out.println(out);
		// System.out.println(out.getErrorMessage());

		System.out.println(System.currentTimeMillis() - a);
	}

	public static AuthenticationOutput login(TransEntity transEntity, String strUsername, String strPassword,
			String strAppCode) throws Exception {
		AuthenticationOutput output = null;
		Response response = null;
		try {
			Form form = new Form();
			form.param("username", strUsername);
			form.param("password", strPassword);
			form.param("app_code", strAppCode);
			Invocation.Builder builder = ClientConsumerConfig.clientRequest(transEntity,
					ConsumerPropeties.getPath("path.login"), MediaType.APPLICATION_JSON);
			Entity<Form> entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE);
			response = builder.post(entity);
			output = response.readEntity(AuthenticationOutput.class);
			System.out.println(output.getErrorCode() + " - " + output.getErrorMessage());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("api response status: " + response.getStatus());
			throw e;
		} finally {
			response.close();
		}
		return output;
	}

	public static AuthenticationOutput loginJson(TransEntity transEntity, String strUsername, String strPassword,
			String strAppCode) throws Exception {

		// Invocation.Builder builder =
		// VBCClientConfig.clientRequest(URLServiceConstant.URL_BLACKLIST_ADD,
		// MediaType.APPLICATION_JSON);
		// Entity<VbcBlacklist> entity = Entity.entity(blacklist,
		// MediaType.APPLICATION_JSON);
		// Response response = builder.post(entity);
		// return response.readEntity(Long.class);

		AuthenticationOutput output = null;
		Response response = null;
		try {
			IdentityEntity identityEntity = new IdentityEntity();
			identityEntity.setUsername(strUsername);
			identityEntity.setPassword(strPassword);
			identityEntity.setAppCode(strAppCode);
			Invocation.Builder builder = ClientConsumerConfig.clientRequest(transEntity,
					ConsumerPropeties.getPath("path.login"), MediaType.APPLICATION_JSON);
			Entity<IdentityEntity> entity = Entity.entity(identityEntity, MediaType.APPLICATION_JSON);
			response = builder.post(entity);
			output = response.readEntity(AuthenticationOutput.class);
			System.out.println(output.getErrorCode() + " - " + output.getErrorMessage());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("api response status: " + response.getStatus());
			throw e;
		} finally {
			response.close();
		}
		return output;
	}

	public static AuthenticationOutput verifyPassword(TransEntity transEntity, String strUsername, String strPassword)
			throws Exception {
		AuthenticationOutput output = null;
		Response response = null;
		try {
			Form form = new Form();
			form.param("username", strUsername);

			if (!ConsumerUtil.stringIsNullOrEmty(strPassword)) {
				form.param("password",
						ConsumerUtil.encrypt(strPassword, ConsumerPropeties.getPath("conf.encrypt.algorithm")));
			} else {
				form.param("password", null);
			}

			Invocation.Builder builder = ClientConsumerConfig.clientRequest(transEntity,
					ConsumerPropeties.getPath("path.verify-password"), MediaType.APPLICATION_JSON);
			Entity<Form> entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE);
			response = builder.post(entity);
			output = response.readEntity(AuthenticationOutput.class);
			System.out.println(output.getErrorCode() + " - " + output.getErrorMessage());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("api response status: " + response.getStatus());
			throw e;
		} finally {
			response.close();
		}
		return output;
	}

	public static boolean logout(TransEntity transEntity) throws Exception {
		Response response = null;
		try {
			Invocation.Builder builder = ClientConsumerConfig.clientRequest(transEntity,
					ConsumerPropeties.getPath("path.logout"), MediaType.APPLICATION_JSON);
			builder = ClientConsumerConfig.appendBuilder(transEntity, builder, null);
			response = builder.delete();
			System.out.println("logout api response status: " + response.getStatus());
			if (200 == response.getStatus())
				return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("api response status: " + response.getStatus());
			throw e;
		} finally {
			response.close();
		}
		return false;
	}

	public static boolean logout(TransEntity transEntity, boolean allSession) throws Exception {
		Response response = null;
		try {
			Invocation.Builder builder = ClientConsumerConfig.clientRequest(transEntity,
					ConsumerPropeties.getPath("path.logout") + "/" + transEntity.getUsername(),
					MediaType.APPLICATION_JSON);
			builder = ClientConsumerConfig.appendBuilder(transEntity, builder, null);
			response = builder.delete();
			System.out.println("logout api response status: " + response.getStatus());
			if (200 == response.getStatus())
				return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("api response status: " + response.getStatus());
			throw e;
		} finally {
			response.close();
		}
		return false;
	}

	public static AuthenticationOutput changePassword(TransEntity transEntity, String strPassword, String strNewPassword)
			throws Exception {
		AuthenticationOutput output = null;
		Response response = null;
		try {
			Form form = new Form();
			form.param("username", transEntity.getUsername());

			if (!ConsumerUtil.stringIsNullOrEmty(strPassword)) {
				form.param("password",
						ConsumerUtil.encrypt(strPassword, ConsumerPropeties.getPath("conf.encrypt.algorithm")));
			} else {
				form.param("password", null);

			}
			form.param("new_password",
					ConsumerUtil.encrypt(strNewPassword, ConsumerPropeties.getPath("conf.encrypt.algorithm")));

			Invocation.Builder builder = ClientConsumerConfig.clientRequest(transEntity,
					ConsumerPropeties.getPath("path.change-password"), MediaType.APPLICATION_JSON);

			Entity<Form> entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE);
			response = builder.post(entity);
			output = response.readEntity(new GenericType<AuthenticationOutput>() {
			});
			System.out.println(output.getErrorCode() + " - " + output.getErrorMessage());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("api response status: " + response.getStatus());
			throw e;
		} finally {
			response.close();
		}
		return output;
	}

	public static boolean keepAlive(TransEntity transEntity) throws Exception {
		Response response = null;
		Invocation.Builder builder = ClientConsumerConfig.clientRequest(transEntity,
				ConsumerPropeties.getPath("path.keep-alive"), MediaType.APPLICATION_JSON);
		try {
			response = builder.get();
			if (response.getStatus() == 200) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("api response status: " + response.getStatus());
			throw e;
		} finally {
			response.close();
		}
		return false;
	}
}
