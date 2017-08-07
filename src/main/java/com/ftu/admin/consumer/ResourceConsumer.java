package com.ftu.admin.consumer;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ftu.admin.consumer.config.ClientConsumerConfig;
import com.ftu.admin.consumer.entity.AttributeOutput;
import com.ftu.admin.consumer.entity.IPAddressOutput;
import com.ftu.admin.consumer.entity.LocationOutput;
import com.ftu.admin.consumer.entity.OrgTypeOutput;
import com.ftu.admin.consumer.entity.TransEntity;
import com.ftu.admin.consumer.utils.ConsumerPropeties;

public class ResourceConsumer {
	public static IPAddressOutput getIpAddressForGroup(TransEntity transEntity, String strGroupCodes) throws Exception {
		IPAddressOutput output = null;
		Response response = null;

		Invocation.Builder builder = ClientConsumerConfig
				.clientRequest(transEntity,
						ConsumerPropeties.getPath("path.ipaddress-for-group") + "/" + strGroupCodes,
						MediaType.APPLICATION_JSON);

		try {
			response = builder.get();
			output = response.readEntity(new GenericType<IPAddressOutput>() {
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

	public static AttributeOutput getAttributesByApp(TransEntity transEntity) throws Exception {
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

	public static LocationOutput getAllLocation(TransEntity transEntity) throws Exception {
		LocationOutput output = null;
		Response response = null;

		Invocation.Builder builder = ClientConsumerConfig.clientRequest(transEntity,
				ConsumerPropeties.getPath("path.locations"), MediaType.APPLICATION_JSON);

		try {
			response = builder.get();
			output = response.readEntity(new GenericType<LocationOutput>() {
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

	public static OrgTypeOutput getAllOrgType(TransEntity transEntity) throws Exception {
		OrgTypeOutput output = null;
		Response response = null;

		Invocation.Builder builder = ClientConsumerConfig.clientRequest(transEntity,
				ConsumerPropeties.getPath("path.org-types"), MediaType.APPLICATION_JSON);

		try {
			response = builder.get();
			output = response.readEntity(new GenericType<OrgTypeOutput>() {
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
