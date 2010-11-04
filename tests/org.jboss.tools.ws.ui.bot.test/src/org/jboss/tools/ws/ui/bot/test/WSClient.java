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
package org.jboss.tools.ws.ui.bot.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.Service.Mode;

public class WSClient {

	private Dispatch<SOAPMessage> d;
	
	public WSClient(URL location, QName serviceName, QName portName) {
		Service s = Service.create(location, serviceName);
		d = s.createDispatch(portName, SOAPMessage.class, Mode.MESSAGE);
	}
	
	public String callService(String message) {
		SOAPMessage result = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			SOAPMessage msg = MessageFactory.newInstance().createMessage( null, new ByteArrayInputStream(message.getBytes()));
			msg.saveChanges();
			result = d.invoke(msg);
			result.writeTo(out);
		} catch (IOException e) {
			e.printStackTrace(System.err);
		} catch (SOAPException e) {
			e.printStackTrace(System.err);
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				//ignore
			}
		}
		return out.toString();
	}	
}
