/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdsic.pcm.service.handler;

import java.util.Iterator;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author langl
 */
public class LoggerHandler implements SOAPHandler<SOAPMessageContext> {

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        SOAPMessage msg = context.getMessage();
        try {
            String service = ((QName) context.get(SOAPMessageContext.WSDL_SERVICE)).getLocalPart();
            String operation = ((QName) context.get(SOAPMessageContext.WSDL_OPERATION)).getLocalPart();

            SOAPEnvelope soapEnvelope = msg.getSOAPPart().getEnvelope();
            SOAPBody body = soapEnvelope.getBody();

            Boolean isResponse = (Boolean) context.get(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY);

            if (!isResponse) { // log request
                String clientId = null;
                String requestId = null;
                String token = null;
                Iterator itr = body.getChildElements();
                while (itr.hasNext()) {
                    Node node = (Node) itr.next();
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element ele = (Element) node;
                        switch (ele.getNodeName()) {
                            case "ClientId":
                                clientId = ele.getTextContent();
                                break;
                            case "RequestId":
                                requestId = ele.getTextContent();
                                break;
                            case "Token":
                                token = ele.getTextContent();
                                break;
                            default:
                                break;
                        }
                    }
                }
                System.out.println(String.format("Request [Serivce=%s][Operation=%s][ClientId=%s][RequestId=%s][Token=%s][Data=%s]", service, operation, clientId, requestId, token, msg.toString()));
            } else {
                System.out.println(String.format("Response [Serivce=%s][Operation=%s][Data=%s]", service, operation, msg.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    @Override
    public void close(MessageContext context) {
    }
}
