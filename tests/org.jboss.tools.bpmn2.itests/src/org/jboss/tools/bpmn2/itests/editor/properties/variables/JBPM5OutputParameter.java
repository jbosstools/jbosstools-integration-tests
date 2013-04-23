package org.jboss.tools.bpmn2.itests.editor.properties.variables;

import org.jboss.tools.bpmn2.itests.editor.properties.datatypes.JBPM5DataType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class JBPM5OutputParameter extends JBPM5Parameter implements IParameter {
	
	/**
	 * 
	 * @param name
	 */
	public JBPM5OutputParameter(String name) {
		this(name, null, null);
	}
	
	/**
	 * 
	 * @param name
	 * @param dataType
	 * @param mapping
	 */
	public JBPM5OutputParameter(String name, JBPM5DataType dataType, IMapping mapping) {
		this(name, dataType, mapping, "Output Parameters");
	}
	
	/**
	 * 
	 * @param name
	 * @param dataType
	 * @param mapping
	 * @param sectionName
	 */
	public JBPM5OutputParameter(String name, JBPM5DataType dataType, IMapping mapping, String sectionName) {
		super(name, dataType, mapping, sectionName);
	}
	
}
