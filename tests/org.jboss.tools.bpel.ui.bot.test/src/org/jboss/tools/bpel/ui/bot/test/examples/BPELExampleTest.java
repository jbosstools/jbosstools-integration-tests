package org.jboss.tools.bpel.ui.bot.test.examples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.SOAPException;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.AssertionFailedException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.bpel.ui.bot.test.BPELTest;
import org.jboss.tools.bpel.ui.bot.test.OdeDeployTest;
import org.jboss.tools.bpel.ui.bot.test.util.ResourceHelper;
import org.jboss.tools.bpel.util.SendSoapMessage;
import org.jboss.tools.ui.bot.ext.ExampleTest;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.xml.sax.SAXException;

/**
 * 
 * @author apodhrad
 * 
 */
public class BPELExampleTest extends ExampleTest {

	public static final String MESSAGE_DIR = "messages";

	@Override
	public String getExampleCategory() {
		return "BPEL";
	}

	protected static void deployExamples(String... projectName) {
		for (int i = 0; i < projectName.length; i++) {
			BPELTest.deployProject(projectName[0]);
		}
	}

	protected static void testDeployment(String projectName) {
		assertTrue(BPELTest.isProjectDeployed(projectName));
	}

	protected void testResponse(String url, String requestFile, String responseFile) {
		XMLUnit.setIgnoreWhitespace(true);
		try {
			String requestMessage = getMessageFromFile(requestFile);
			String responseMessage = getMessageFromFile(responseFile);
			String response = SendSoapMessage.sendMessage(url, requestMessage);
			Diff diff = new Diff(response, responseMessage);
			assertTrue("Expected response is\n" + responseMessage + "\nbut it was\n" + response,
					diff.similar());
		} catch (IOException e) {
			e.printStackTrace();
			throw new AssertionFailedException(getExampleName()
					+ ": IOException during testing response.");
		} catch (SAXException e) {
			e.printStackTrace();
			throw new AssertionFailedException(getExampleName()
					+ ": SAXException during testing response.");
		} catch (SOAPException e) {
			e.printStackTrace();
			throw new AssertionFailedException(getExampleName()
					+ ": SOAPException during testing response.");
		}
	}

	protected void testResponses(String url, String dir) {
		try {
			List<String> requests = getRequestMessages(dir);
			for(String request: requests) {
				request = dir + "/" + request;
				String response = request.replace("request", "response");
				testResponse(url, request, response);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new AssertionFailedException(getExampleName()
					+ ": IOException during testing response.");
		}
	}

	private static List<String> getRequestMessages(String dir) throws IOException {
		List<String> messages = new ArrayList<String>();
		File messages_dir = new File(ResourceHelper.getPath(BPELTest.BUNDLE, MESSAGE_DIR + "/"
				+ dir));
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
		String message = readFile(ResourceHelper.getPath(BPELTest.BUNDLE, path));
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
