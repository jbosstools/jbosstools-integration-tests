package org.jboss.tools.bpmn2.itests.validator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jboss.tools.bpmn2.itests.reddeer.ResourceHelper;
import org.jboss.tools.bpmn2.itests.test.Activator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class BPMN2Validator {

	private static final Logger log = Logger.getLogger(BPMN2Validator.class);
	
	protected final List<String> warningList;
	protected final List<String> errorList;
	protected final List<String> fatalErrorList;
	
	protected List<Source> schemaList;
	
	/**
	 * 
	 */
	public BPMN2Validator() {
		warningList = new ArrayList<String>();
		errorList = new ArrayList<String>();
		fatalErrorList = new ArrayList<String>();
		
		schemaList = new ArrayList<Source>();
		
		schemaList.add(new StreamSource(
				ResourceHelper.getResourceAbsolutePath(
						Activator.PLUGIN_ID, "resources/bpmn2/BPMN20.xsd")));
	}
	
	/**
	 * 
	 * @param xmlFile
	 * @return
	 */
	public boolean validate(File file) throws IOException {
		String content = null;
		FileInputStream stream = new FileInputStream(file);
		try {
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			/* Instead of using default, pass in a decoder. */
			content = Charset.defaultCharset().decode(bb).toString();
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				// ignore
				log.error(e.getMessage());
			}
		}
		
		return validate(content);
	}
	
	/**
	 * 
	 * @param xmlContent
	 * @return
	 */
	public boolean validate(String xml) {
		try {
			Source[] sa = schemaList.toArray(new Source[schemaList.size()]); 
			
			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema s = sf.newSchema(sa);

			Validator v = s.newValidator();
			v.setErrorHandler(new ErrorHandler());
			v.validate(new StreamSource(new ByteArrayInputStream(xml.getBytes())));

			if (!fatalErrorList.isEmpty()) logIssues(Level.FATAL, fatalErrorList.size() + " Fatal Errors:", fatalErrorList);
			if (!errorList.isEmpty()) logIssues(Level.ERROR, errorList.size() + " Errors:", errorList);
			if (!warningList.isEmpty()) logIssues(Level.WARN, warningList.size() + " Warnings:", warningList);
			
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
