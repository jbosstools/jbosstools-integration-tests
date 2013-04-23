package org.jboss.tools.bpmn2.itests.editor.properties.variables;

import org.jboss.tools.bpmn2.itests.editor.properties.datatypes.AbstractDataState;
import org.jboss.tools.bpmn2.itests.editor.properties.datatypes.AbstractDataType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class OutputParameter extends Parameter implements IParameter {

	/**
	 * 
	 * @param name
	 */
	public OutputParameter(String name) {
		this(name, null, null, null);
	}
	
	/**
	 * 
	 * @param name
	 * @param dataState
	 * @param dataType
	 * @param mapping
	 */
	public OutputParameter(String name, AbstractDataState dataState, AbstractDataType dataType, IMapping mapping) {
		this(name, dataState, dataType, mapping, "Output Parameters");
	}
	
	/**
	 * 
	 * @param name
	 * @param dataType
	 * @param mapping
	 * @param sectionName
	 */
	public OutputParameter(String name, AbstractDataState dataState, AbstractDataType dataType, IMapping mapping, String sectionName) {
		super(name, dataState, dataType, mapping, sectionName);
	}
	
}
