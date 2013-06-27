package org.jboss.tools.bpmn2.itests.editor.jbpm.startevents;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.tools.bpmn2.itests.editor.AbstractEvent;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class StartEvent extends AbstractEvent {

	/**
	 * 
	 * @param name
	 */
	public StartEvent(String name) {
		super(name, ConstructType.START_EVENT);
	}

	/**
	 * 
	 * @param name
	 * @param type
	 */
    StartEvent(String name, ConstructType type) {
		super(name, type);
	}
	
	/**
	 * 
	 * @param b
	 */
	public void setInterrupting(boolean b) {
		properties.selectCheckBox(new CheckBox("Is Interrupting"), b);
	}
	
	/**
	 * 
	 * @param b
	 */
	public void setParallelMultiple(boolean b) {
		properties.selectCheckBox(new CheckBox("Parallel Multiple"), b);
	}
	
}