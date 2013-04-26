package org.jboss.tools.bpel.ui.bot.test.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.eclipse.swtbot.swt.finder.exceptions.AssertionFailedException;
import org.jboss.tools.bpel.ui.bot.test.Activator;
import org.xml.sax.SAXException;

/**
 * Temporary class.
 * 
 * @author mbaluch, apodhrad
 * 
 */
public class SoapClient {

	public static final String MESSAGE_DIR = "messages";

	/**
	 * 
	 * @param url
	 *            URL string
	 * @param message
	 *            SOAP message
	 * 
	 * @return response
	 * @throws IOException
	 *             in case of an I/O error;
	 * @throws SOAPException
	 *             in case of a SOAP error
	 */
	public static String sendMessage(String url, String message) throws SOAPException, IOException {
		URL endpoint = new URL(url);

		MessageFactory factory = MessageFactory.newInstance();
		SOAPMessage soapRequest = factory.createMessage(null,
				new ByteArrayInputStream(message.getBytes()));
		SOAPConnection connection = SOAPConnectionFactory.newInstance().createConnection();
		SOAPMessage soapResponse = connection.call(soapRequest, endpoint);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		soapResponse.writeTo(out);
		return out.toString();
	}

	public static void testResponses(String url, String dir) {
		try {
			List<String> requests = getRequestMessages(dir);
			for (String request : requests) {
				request = dir + "/" + request;
				String response = request.replace("request", "response");
				testResponse(url, request, response);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new AssertionFailedException("IOException during testing response.");
		}
	}

	public static void testResponse(String url, String requestFile, String responseFile) {
		XMLUnit.setIgnoreWhitespace(true);
		try {
			String requestMessage = getMessageFromFile(requestFile);
			String responseMessage = getMessageFromFile(responseFile);
			String response = SoapClient.sendMessage(url, requestMessage);
			Diff diff = new Diff(response, responseMessage);
			assertTrue("Expected response is\n" + responseMessage + "\nbut it was\n" + response,
					diff.similar());
		} catch (IOException e) {
			e.printStackTrace();
			throw new AssertionFailedException("IOException during testing response.");
		} catch (SAXException e) {
			e.printStackTrace();
			throw new AssertionFailedException("SAXException during testing response.");
		} catch (SOAPException e) {
			e.printStackTrace();
			throw new AssertionFailedException("SOAPException during testing response.");
		}
	}

	private static List<String> getRequestMessages(String dir) throws IOException {
		List<String> messages = new ArrayList<String>();
		File messages_dir = new File(ResourceHelper.getResourceAbsolutePath(Activator.PLUGIN_ID,
				MESSAGE_DIR + "/" + dir));
		String[] file = messages_dir.list();
		for (int i = 0; i < file.length; i++) {
			if (file[i].endsWith(".xml") && file[i].contains("request")) {
				messages.add(file[i]);
			}
		}
		return messages;
	}

	private static String getMessageFromFile(String fileName) throws IOException {
		return getMessageFromFile(fileName, MESSAGE_DIR);
	}

	private static String getMessageFromFile(String fileName, String dir) throws IOException {
		String path = dir + "/" + fileName;
		String message = readFile(ResourceHelper.getResourceAbsolutePath(Activator.PLUGIN_ID, path));
		assertNotNull("Couldn't get message from " + path, message);
		return message;
	}

	private static String readFile(String file) throws IOException {
		if (file == null) {
			throw new NullPointerException("Couldn't read from null");
		}
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
		}
		reader.close();
		return stringBuilder.toString();
	}
}
