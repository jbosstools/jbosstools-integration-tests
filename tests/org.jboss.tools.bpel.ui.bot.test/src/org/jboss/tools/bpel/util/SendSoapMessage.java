package org.jboss.tools.bpel.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

/**
 * Temporary class.
 * 
 * @author mbaluch, apodhrad
 *
 */
public class SendSoapMessage {

	/**
	 * 
	 * @param url			URL string
	 * @param message		SOAP message
	 * 
	 * @return				response				
	 * @throws IOException  in case of an I/O error;
	 * @throws SOAPException	in case of a SOAP error
	 */
	public static String sendMessage(String url, String message) throws SOAPException, IOException {
		URL endpoint = new URL(url);
		
		MessageFactory factory = MessageFactory.newInstance();
		SOAPMessage soapRequest = factory.createMessage(null, new ByteArrayInputStream(message.getBytes()));
		SOAPConnection connection = SOAPConnectionFactory.newInstance().createConnection();
		SOAPMessage soapResponse = connection.call(soapRequest, endpoint);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		soapResponse.writeTo(out);
		return out.toString();
	}
	
}
