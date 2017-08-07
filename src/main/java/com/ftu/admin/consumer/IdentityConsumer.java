package com.ftu.admin.consumer;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ftu.admin.consumer.config.ClientConsumerConfig;
import com.ftu.admin.consumer.entity.IdentityOutput;
import com.ftu.admin.consumer.entity.OrganizationEntity;
import com.ftu.admin.consumer.entity.OrganizationOutput;
import com.ftu.admin.consumer.entity.TransEntity;
import com.ftu.admin.consumer.utils.ConsumerPropeties;

public class IdentityConsumer {

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

	public static IdentityOutput getAllIdentitiesForRole(TransEntity transEntity, String roleCodes) throws Exception {
		IdentityOutput output = null;
		Response response = null;

		Invocation.Builder builder = ClientConsumerConfig.clientRequest(transEntity,
				ConsumerPropeties.getPath("path.identities-for-roles") + "/" + roleCodes, MediaType.APPLICATION_JSON);

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

	public static OrganizationOutput addOrganization(TransEntity transEntity, OrganizationEntity organizationEntity)
			throws Exception {
		OrganizationOutput output = new OrganizationOutput();
		Response response = null;
		try {
			Invocation.Builder builder = ClientConsumerConfig.clientRequest(transEntity,
					ConsumerPropeties.getPath("path.organization-add"), MediaType.APPLICATION_JSON);
			Entity<OrganizationEntity> entity = Entity.entity(organizationEntity, MediaType.APPLICATION_JSON);
			response = builder.post(entity);
			output = response.readEntity(new GenericType<OrganizationOutput>() {
			});
			/*
			 * if
			 * (output.getEquipmentStatus().equals(StaticData.RESPONSE_STATUS_SUCCESS)) {
			 * return output.getOrganizations().get(0); }
			 */
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("api response status: " + response.getStatus());
			throw e;
		} finally {
			response.close();
		}
		return output;
	}

	public static OrganizationOutput updateOrganization(TransEntity transEntity, OrganizationEntity organizationEntity)
			throws Exception {
		OrganizationOutput output = new OrganizationOutput();
		Response response = null;
		try {
			Invocation.Builder builder = ClientConsumerConfig.clientRequest(transEntity,
					ConsumerPropeties.getPath("path.organization-update"), MediaType.APPLICATION_JSON);
			Entity<OrganizationEntity> entity = Entity.entity(organizationEntity, MediaType.APPLICATION_JSON);
			response = builder.post(entity);
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

	public static OrganizationOutput deleteOrganization(TransEntity transEntity, String strOrgCode) throws Exception {
		OrganizationOutput output = new OrganizationOutput();
		Response response = null;
		try {
			Invocation.Builder builder = ClientConsumerConfig.clientRequest(transEntity,
					ConsumerPropeties.getPath("path.organization-delete") + "/" + strOrgCode,
					MediaType.APPLICATION_JSON);
			builder = ClientConsumerConfig.appendBuilder(transEntity, builder, null);
			response = builder.delete();
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

	public static OrganizationOutput addOrganizationChild(TransEntity transEntity,
			OrganizationEntity organizationEntity, String strParentCode) throws Exception {
		OrganizationOutput output = new OrganizationOutput();
		Response response = null;
		try {
			Invocation.Builder builder = ClientConsumerConfig.clientRequest(transEntity,
					ConsumerPropeties.getPath("path.organization-add-child") + "/" + strParentCode,
					MediaType.APPLICATION_JSON);
			Entity<OrganizationEntity> entity = Entity.entity(organizationEntity, MediaType.APPLICATION_JSON);
			response = builder.post(entity);
			output = response.readEntity(new GenericType<OrganizationOutput>() {
			});
			/*
			 * if
			 * (output.getEquipmentStatus().equals(StaticData.RESPONSE_STATUS_SUCCESS)) {
			 * return output.getOrganizations().get(0); }
			 */
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

	public static OrganizationOutput getOrganizationsByType(TransEntity transEntity, String strOrgType)
			throws Exception {
		OrganizationOutput output = null;
		Response response = null;

		Invocation.Builder builder = ClientConsumerConfig.clientRequest(transEntity,
				ConsumerPropeties.getPath("path.organizations-by-type") + "/" + strOrgType, MediaType.APPLICATION_JSON);

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

	public static IdentityOutput getIdentityById(TransEntity transEntity, long longIdentityId) throws Exception {
		IdentityOutput output = new IdentityOutput();
		Response response = null;

		Invocation.Builder builder = ClientConsumerConfig.clientRequest(transEntity,
				ConsumerPropeties.getPath("path.identity-by-id") + "/" + longIdentityId, MediaType.APPLICATION_JSON);

		try {
			response = builder.get();
			output = response.readEntity(new GenericType<IdentityOutput>() {
			});
			// if
			// (output.getEquipmentStatus().equals(StaticData.RESPONSE_STATUS_SUCCESS)) {
			// return output.getIdentities().get(0);
			// }
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("api response status: " + response.getStatus());
			throw e;
		} finally {
			response.close();
		}
		return output;
	}

	public static IdentityOutput getIdentityByUsername(TransEntity transEntity, long strUsername) throws Exception {
		IdentityOutput output = new IdentityOutput();
		Response response = null;

		Invocation.Builder builder = ClientConsumerConfig.clientRequest(transEntity,
				ConsumerPropeties.getPath("path.identity-by-username") + "/" + strUsername, MediaType.APPLICATION_JSON);

		try {
			response = builder.get();
			output = response.readEntity(new GenericType<IdentityOutput>() {
			});
			// if
			// (output.getEquipmentStatus().equals(StaticData.RESPONSE_STATUS_SUCCESS)) {
			// return output.getIdentities().get(0);
			// }
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
