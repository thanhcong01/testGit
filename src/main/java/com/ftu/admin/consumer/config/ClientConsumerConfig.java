package com.ftu.admin.consumer.config;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.jackson.JacksonFeature;

import com.ftu.admin.consumer.entity.TransEntity;
import com.ftu.admin.consumer.utils.ConsumerPropeties;

/**
 * @version 1.0 09/15/2015
 * @author haitx3
 */

public class ClientConsumerConfig {
	private static Client client;
	static {

		client = ClientBuilder.newClient().register(JacksonFeature.class);
		client.register(ClientLoggingFilter.class);
	}

	public static Invocation.Builder clientRequest(TransEntity transEntity, String strPath, String strMediaType) {
		WebTarget webTarget = getWebTarget(strPath);
		Invocation.Builder builder = null;
		if (strMediaType != null) {
			builder = webTarget.request(strMediaType);
		} else {
			builder = webTarget.request();
		}
		appendBuilder(transEntity, builder, null);

		return builder;
	}

	public static WebTarget getWebTarget(String strPath) {
		WebTarget webTarget = ClientConsumerConfig.client.target(ConsumerPropeties.getResourceUrl()).path(strPath);
		return webTarget;
	}

	public static Invocation.Builder appendBuilder(TransEntity transEntity, Invocation.Builder builder,
			String strMediaType) {
		if (strMediaType != null) {
			builder.accept(strMediaType);
		}
		builder = appendBuilder(transEntity, builder);
		return builder;
	}

	public static Invocation.Builder appendBuilder(TransEntity transEntity, Invocation.Builder builder) {
		if (transEntity != null) {
			if (transEntity.getTransId() != null) {
				builder = builder.header("TRANS_ID", transEntity.getTransId());
				builder.cookie("JSESSIONID", transEntity.getTransId());
			}
			builder = builder.header("X-FORWARDED-FOR", transEntity.getRemoteAddr());
		}
		return builder;
	}

	public static Client getClient() {
		return client;
	}

	public static void setClient(Client client) {
		ClientConsumerConfig.client = client;
	}

}
