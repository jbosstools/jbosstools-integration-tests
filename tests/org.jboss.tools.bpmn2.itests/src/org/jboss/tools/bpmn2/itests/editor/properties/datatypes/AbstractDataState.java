package org.jboss.tools.bpmn2.itests.editor.properties.datatypes;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public abstract class AbstractDataState implements IDataState {

	protected String stateName;
	
	/**
	 * 
	 * @param stateName
	 */
	public AbstractDataState(String stateName) {
		this.stateName = stateName;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getStateName() {
		return stateName;
	}
	
	/**
	 * 
	 */
	public abstract void add();
	
}
