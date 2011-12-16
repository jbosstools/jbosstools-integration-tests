/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.ws.ui.bot.test.wsclient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.Service.Mode;

public class WSClient {

	private static final Logger L = Logger.getLogger(WSClient.class.getName());
	private Dispatch<SOAPMessage> d;
	
	public WSClient(URL location, QName serviceName, QName portName) {
		Service s = Service.create(location, serviceName);
		d = s.createDispatch(portName, SOAPMessage.class, Mode.MESSAGE);
	}
	
	public String callService(String message) {
		SOAPMessage result = null;
		try {
			SOAPMessage msg = MessageFactory.newInstance().createMessage( null, new ByteArrayInputStream(message.getBytes()));
			msg.saveChanges();
			result = d.invoke(msg);
		} catch (SOAPException e) {
			L.log(Level.WARNING, e.getMessage(), e);
		} catch (IOException e) {
			L.log(Level.WARNING, e.getMessage(), e);
		}
		String s = msgToString(result);
		L.fine("SOAP Request :\n" + message);
		L.fine("SOAP Response:\n" + s);
		return s;
	}
	
	private String msgToString(SOAPMessage msg) {
		if (msg == null) {
			return "";
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			msg.writeTo(out);
		} catch (SOAPException e) {
			L.log(Level.WARNING, e.getMessage(), e);
		} catch (IOException e) {
			L.log(Level.WARNING, e.getMessage(), e);
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				//ignore
				L.log(Level.WARNING, e.getMessage(), e);
			}
		}
		return out.toString();
	}
}
