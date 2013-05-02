package org.jboss.tools.bpmn2.itests.validator;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/*
import org.drools.compiler.BPMN2ProcessFactory;
import org.drools.compiler.DroolsError;
import org.drools.compiler.PackageBuilder;
import org.drools.io.impl.ReaderResource;

import org.jbpm.compiler.ProcessBuilderImpl;
*/

/**
 * 
 * @author mbaluch
 */
public class JBPM5Validator {

	/*
	private Logger log = Logger.getLogger(JBPM5Validator.class);
	
	private List<DroolsError> errorList = new ArrayList<DroolsError>();
	
	public boolean validate(String xmlContent) {
		errorList.clear();
		
		Reader reader = new InputStreamReader(new ByteArrayInputStream(xmlContent.getBytes()));
		PackageBuilder pb = new PackageBuilder();
		BPMN2ProcessFactory.configurePackageBuilder(pb);
		
		ProcessBuilderImpl pbi = new ProcessBuilderImpl(pb);
		errorList = pbi.addProcessFromXml(new ReaderResource(reader));

		return errorList.isEmpty();
	}
	
	public List<String> getErrorList() {
		return errorList;
	}
	
	private void logIssues(Level level, String title, List<String> stringList) {
		log.log(level, title);
		for (String s : stringList) {
			log.log(level, "\t" + s);
		}
	}
	*/
}
