package org.jboss.tools.bpel.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Temporary class.
 * 
 * @author mbaluch
 *
 */
public class SendSoapMessage {

	/**
	 * 
	 * @param url			URL string
	 * @param message		SOAP message
	 * @param soapAction	SOAP action name. This may be {@code null}. For more info see
	 * 						{@link http://www.w3.org/TR/2000/NOTE-SOAP-20000508/#Toc478383528}
	 * 
	 * @return				response				
	 * @throws IOException  in case of an I/O error;
	 */
	public static String sendMessage(String url, String message, String soapAction) throws IOException {
		// setup connection
		URL endpoint = new URL(url);
		HttpURLConnection httpConnection = (HttpURLConnection) endpoint.openConnection();
		httpConnection.setDoInput(true);
		httpConnection.setDoOutput(true);
		httpConnection.setUseCaches(false);
		httpConnection.setRequestMethod("POST");
		httpConnection.setRequestProperty("Content-Type", "text/xml");
		httpConnection.setRequestProperty("SOAPAction", soapAction);
		httpConnection.connect();
		
		// send the SOAP message
		PrintWriter pw = new PrintWriter(httpConnection.getOutputStream());
		pw.write(message);
		pw.flush();
		pw.close();
		
		// get the response
		Writer w = new StringWriter();
		Reader r = new BufferedReader(new InputStreamReader(
				httpConnection.getInputStream()));
		
		char[] b = new char[4096];
		int size;
		while ((size = r.read(b)) != -1) {
			w.write(b, 0, size);
		}
		
		// return responses
		return w.toString();
	}
	
}
