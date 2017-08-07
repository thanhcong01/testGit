package com.ftu.admin.consumer.utils;

import java.net.URI;

import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.jackson.JacksonFeature;

import com.ftu.admin.consumer.entity.AuthenticationOutput;
import com.ftu.admin.controller.CommonFacesContext;
import com.ftu.utils.ResourceBundleUtil;
import javax.faces.context.FacesContext;

/**
 * @version 1.0 09/15/2015
 * @author haitx3
 */

public class ClientConfig {
	public static Client client;

	static {
		client = ClientBuilder.newClient().register(JacksonFeature.class);
	}

	public static Invocation.Builder clientRequest(String path, String mediaType) {
		WebTarget webTarget = getWebTarget(path);
		Invocation.Builder builder = null;
		if (mediaType != null) {
			builder = webTarget.request(mediaType);
		} else {
			builder = webTarget.request();
		}
		appendBuilder(builder, null);

		return builder;
	}

	public static WebTarget getWebTarget(String path) {
		WebTarget webTarget = ClientConfig.client.target(path);
		return webTarget;
	}

	public static Invocation.Builder appendBuilder(Invocation.Builder builder, String mediaType) {
		if (mediaType != null) {
			builder.accept(mediaType);
		}
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		AuthenticationOutput authen = null;
		if (session != null) {
			authen = (AuthenticationOutput) session.getAttribute("username");
		}
		if (authen != null) {
			builder = builder.header("TRANS_ID", authen.getTransEntity().getTransId());
		}
		builder = builder.header("X-FORWARDED-FOR", CommonFacesContext.getRemoteAddr());
		return builder;
	}
}
