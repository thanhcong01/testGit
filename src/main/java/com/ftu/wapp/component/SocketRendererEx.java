package com.ftu.wapp.component;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.primefaces.component.socket.Socket;
import org.primefaces.context.RequestContext;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.Constants;
import org.primefaces.util.WidgetBuilder;

/**
 * @version n/a
 * @author haitx3
 */
public class SocketRendererEx extends CoreRenderer {
	private Object serverUrl = null;

	@Override
	public void decode(FacesContext context, UIComponent component) {
		decodeBehaviors(context, component);
	}

	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		serverUrl = component.getAttributes().get("SERVER_URL");
		super.encodeBegin(context, component);
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		Socket socket = (Socket) component;
		String channel = socket.getChannel();
		String channelUrl = Constants.PUSH_PATH + channel;
		String url = getResourceURL(context, channelUrl);
		String pushServer = RequestContext.getCurrentInstance().getApplicationContext().getConfig().getPushServerURL();

		String clientId = socket.getClientId(context);

		if (pushServer != null) {
			url = pushServer + url;
		}

		if (serverUrl != null) {
			url = serverUrl.toString() + channel;
		}

		WidgetBuilder wb = getWidgetBuilder(context);
		wb.initWithDomReady("Socket", socket.resolveWidgetVar(), clientId);

		wb.attr("url", url).attr("autoConnect", socket.isAutoConnect()).attr("transport", socket.getTransport())
				.attr("fallbackTransport", socket.getFallbackTransport()).callback("onMessage", socket.getOnMessage())
				.callback("onError", "function(response)", socket.getOnError())
				.callback("onClose", "function(response)", socket.getOnClose())
				.callback("onOpen", "function(response)", socket.getOnOpen())
				.callback("onReconnect", "function(response)", socket.getOnReconnect())
				.callback("onMessagePublished", "function(response)", socket.getOnMessagePublished())
				.callback("onTransportFailure", "function(response, request)", socket.getOnTransportFailure())
				.callback("onLocalMessage", "function(response)", socket.getOnLocalMessage());

		encodeClientBehaviors(context, socket);

		wb.finish();
	}

}
