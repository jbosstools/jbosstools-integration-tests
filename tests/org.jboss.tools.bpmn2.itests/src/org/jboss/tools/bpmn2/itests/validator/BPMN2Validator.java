package org.jboss.tools.bpmn2.itests.validator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jboss.tools.bpmn2.itests.test.Activator;
import org.jboss.tools.ui.bot.ext.helper.ResourceHelper;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class BPMN2Validator {

	private static final Logger log = Logger.getLogger(BPMN2Validator.class);
	
	private final File xsd;
	
	private final List<String> warningList;
	private final List<String> errorList;
	private final List<String> fatalErrorList;
	
	/**
	 * 
	 */
	public BPMN2Validator() {
		xsd = new File(ResourceHelper.getResourceAbsolutePath(
				Activator.PLUGIN_ID, "resources/bpmn2/BPMN20.xsd"));
		
		warningList = new ArrayList<String>();
		errorList = new ArrayList<String>();
		fatalErrorList = new ArrayList<String>();
	}
	
	/**
	 * 
	 * @param xmlContent
	 * @return
	 */
	public boolean validate(String xmlContent) {
		try {
			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema s = sf.newSchema(new StreamSource(xsd));

			Validator v = s.newValidator();
			v.setErrorHandler(new ErrorHandler());
			v.validate(new StreamSource(new ByteArrayInputStream(xmlContent.getBytes())));

			if (!fatalErrorList.isEmpty()) logIssues(Level.FATAL, "Fatal Errors:", fatalErrorList);
			if (!errorList.isEmpty()) logIssues(Level.ERROR, "Errors:", errorList);
			if (!warningList.isEmpty()) logIssues(Level.WARN, "Warnings:", warningList);
			
			return errorList.isEmpty() && fatalErrorList.isEmpty();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public List<String> getWarningList() {
		return warningList;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<String> getErrorList() {
		return errorList;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<String> getFatalErrorList() {
		return fatalErrorList;
	}
	
	/**
	 * 
	 * @param stringList
	 */
	private void logIssues(Level level, String title, List<String> stringList) {
		log.log(level, title);
		for (String s : stringList) {
			log.log(level, "\t" + s);
		}
	}
	
	/**
	 * Error handler. 
	 */
	private class ErrorHandler implements org.xml.sax.ErrorHandler {

		public ErrorHandler() {
			warningList.clear();
			errorList.clear();
			fatalErrorList.clear();
		}
		
		public void warning(SAXParseException e) throws SAXException {
			warningList.add(format(e));
		}

		public void error(SAXParseException e) throws SAXException {
			errorList.add(format(e));
		}

		public void fatalError(SAXParseException e) throws SAXException {
			fatalErrorList.add(format(e));
		}
		
		private String format(SAXParseException e) {
			return e.getLineNumber() + ":" + e.getColumnNumber() + " - " + e.getMessage();
		}
		
	}
	
}
