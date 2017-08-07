package com.ftu.admin.consumer.config;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;

public class ClientLoggingFilter implements ClientRequestFilter, ClientResponseFilter {

	@Override
	public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
		// Object responseTransId = requestContext.getHeaderString("TRANS_ID");
		// HttpSession session = SessionBean.getSession();
		// AuthenticationOutput authen = (AuthenticationOutput)
		// session.getAttribute("username");
		// if ((responseTransId == null) ||
		// !responseTransId.toString().equals(authen.getTransEntity().transId))
		// {
		// System.out.println("New trans...");
		// }
		System.out.print("Response Context: " + responseContext.getDate());
		// TransData.buildTransData(responseContext);
		System.out.print("\t Status: " + responseContext.getStatus());
		System.out.print("\t URI: " + requestContext.getUri());
		System.out.println("\n-------------------------------------");

	}

	@Override
	public void filter(ClientRequestContext requestContext) throws IOException {

	}

}
