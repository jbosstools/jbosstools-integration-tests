package org.eclipse.bpmn2.ui.editor.properties.variables;

import org.eclipse.bpmn2.ui.editor.properties.datatypes.AbstractDataState;
import org.eclipse.bpmn2.ui.editor.properties.datatypes.AbstractDataType;

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
