/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdsic.pcm.service.handler;

import com.rdsic.pcm.common.Util;
import java.io.InputStream;
import java.io.PrintStream;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.ws.LogicalMessage;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.LogicalHandler;
import javax.xml.ws.handler.LogicalMessageContext;
import javax.xml.ws.handler.MessageContext;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

/**
 *
 * @author langl
 */
public class SchemaValidationHandler implements LogicalHandler<LogicalMessageContext> {

    private NamedNodeMap getNamespaces(Element el) {
        return el.getAttributes();
    }

    private String validateAgainstWSDL(MessageContext ctx, Source payload)
            throws Exception {
        if (ctx == null) {
            throw new RuntimeException("MessageContext is null.");
        }
        ServletContext sc = (ServletContext) ctx.get("javax.xml.ws.servlet.context");
        if (sc == null) {
            throw new RuntimeException("ServletContext is null.");
        }
        InputStream wsdlStream = sc.getResourceAsStream("/WEB-INF/wsdl/UserManagement/UserManagement_v1.0.wsdl");

        Document wsdlDoc = Util.documentFactory.newDocumentBuilder().newDocument();
        Transformer xformer = TransformerFactory.newInstance().newTransformer();

        xformer.transform(new StreamSource(wsdlStream), new DOMResult(wsdlDoc));

        NodeList schemaNodes = wsdlDoc.getElementsByTagNameNS("http://www.w3.org/2001/XMLSchema", "schema");
        int numOfSchemas = schemaNodes.getLength();
        Source[] schemas = new Source[numOfSchemas];
        for (int i = 0; i < numOfSchemas; i++) {
            Document schemaDoc = Util.documentFactory.newDocumentBuilder().newDocument();
            Element schemaElt = (Element) schemaDoc.importNode(schemaNodes.item(i), true);

            NamedNodeMap nsDecls = schemaElt.getAttributes();
            for (int j = 0; j < nsDecls.getLength(); j++) {
                Attr a = (Attr) schemaDoc.importNode(nsDecls.item(j), true);
                schemaElt.setAttributeNodeNS(a);
            }
            schemaDoc.appendChild(schemaElt);
            schemas[i] = new DOMSource(schemaDoc);
        }
        SchemaFactory schemaFac = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        Schema schema = schemaFac.newSchema(schemas);

        Validator validator = schema.newValidator();
        if ((!DOMSource.class.isInstance(payload)) && (!SAXSource.class.isInstance(payload))) {
            Document payloadDoc = Util.documentFactory.newDocumentBuilder().newDocument();
            xformer.transform(payload, new DOMResult(payloadDoc));
            payload = new DOMSource(payloadDoc);
        }
        try {
            validator.validate(payload);
        } catch (Exception se) {
            se.printStackTrace();
            return "validation error: " + se.getMessage();
        }
        return null;
    }

    @Override
    public boolean handleMessage(LogicalMessageContext context) {
        if (((Boolean) context.get("javax.xml.ws.handler.message.outbound"))) {
            return true;
        }
        try {
            LogicalMessage lm = context.getMessage();
            Source payload = lm.getPayload();

            String validated = validateAgainstWSDL(context, payload);
            if (validated != null) {
                throw new WebServiceException(validated);
            }
            System.out.println("validated ok");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean handleFault(LogicalMessageContext context) {
        return true;
    }

    @Override
    public void close(MessageContext context) {
        
    }

}
