package org.jboss.tools.bpmn2.itests.validator;

import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

import org.jboss.tools.bpmn2.itests.reddeer.ResourceHelper;
import org.jboss.tools.bpmn2.itests.test.Activator;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class JBPM5Validator extends BPMN2Validator {

	@SuppressWarnings("unused")
	private Logger log = Logger.getLogger(JBPM5Validator.class); 
	
	public JBPM5Validator() {
		super();
		
		schemaList.add(new StreamSource(
				ResourceHelper.getResourceAbsolutePath(
						Activator.PLUGIN_ID, "resources/bpmn2/drools.xsd")));
	}
	
}
