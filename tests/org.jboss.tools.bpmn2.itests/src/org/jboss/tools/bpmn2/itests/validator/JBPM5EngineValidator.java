package org.jboss.tools.bpmn2.itests.validator;

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
public class JBPM5EngineValidator {

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
	
	*/
	
}
