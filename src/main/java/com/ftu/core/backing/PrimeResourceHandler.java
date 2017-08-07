/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.core.backing;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import javax.faces.application.ResourceHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.util.Constants;

/**
 *
 * @author E5420
 */
public class PrimeResourceHandler extends org.primefaces.application.resource.PrimeResourceHandler {

    public PrimeResourceHandler(ResourceHandler wrapped) {
        super(wrapped);
    }

    @Override
    public void handleResourceRequest(FacesContext context) throws IOException {
        try {
            Map<String, String> params = context.getExternalContext().getRequestParameterMap();
            String library = params.get("ln");
            String dynamicContentId = params.get(Constants.DYNAMIC_CONTENT_PARAM);

            if (dynamicContentId != null && library != null && library.equals("primefaces")) {
                Map<String, Object> session = context.getExternalContext().getSessionMap();
                StreamedContent streamedContent = null;

                try {

                    Object dynamicContentEL = (Object) session.get(dynamicContentId);

                //String dynamicContentEL = (String) session.get(dynamicContentId);
                    //ELContext eLContext = context.getELContext();
                    //ValueExpression ve = context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), dynamicContentEL, StreamedContent.class);
                    //streamedContent = (StreamedContent) ve.getValue(eLContext);
                    streamedContent = (StreamedContent) dynamicContentEL;
                    if (streamedContent == null) {
                        streamedContent = (StreamedContent) session.get("dlgPDF");
                    }

                    ExternalContext externalContext = context.getExternalContext();
                    externalContext.setResponseStatus(200);
                    externalContext.setResponseContentType(streamedContent.getContentType());

                    byte[] buffer = new byte[2048];

                    int length;
                    InputStream inputStream = streamedContent.getStream();
                    if (inputStream.markSupported()) {
                        inputStream.mark(0);
                    }
                    while ((length = (inputStream.read(buffer))) >= 0) {
                        externalContext.getResponseOutputStream().write(buffer, 0, length);
                    }

                    if (inputStream.markSupported()) {
                        inputStream.reset();
                    }

                    externalContext.responseFlushBuffer();
                    context.responseComplete();

                } catch (Exception e) {
                } finally {
                //cleanup
                    //session.remove(dynamicContentId);

                    if (streamedContent != null) {
                        streamedContent.getStream().close();
                    }
                }
            } else {
                super.handleResourceRequest(context);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
