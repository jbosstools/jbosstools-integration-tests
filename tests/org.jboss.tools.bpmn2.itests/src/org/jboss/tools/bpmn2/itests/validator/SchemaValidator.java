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

import org.jboss.tools.bpmn2.itests.test.Activator;
import org.jboss.tools.ui.bot.ext.helper.ResourceHelper;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class SchemaValidator {

	private final File xsd;
	
	private final List<String> warningList;
	private final List<String> errorList;
	private final List<String> fatalErrorList;
	
	/**
	 * 
	 */
	public SchemaValidator() {
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
			
			ErrorHandler eh = new ErrorHandler();
			
			Schema s = sf.newSchema(new StreamSource(xsd));
			Validator v = s.newValidator();
			v.setErrorHandler(eh);
			v.validate(new StreamSource(new ByteArrayInputStream(xmlContent.getBytes())));

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
