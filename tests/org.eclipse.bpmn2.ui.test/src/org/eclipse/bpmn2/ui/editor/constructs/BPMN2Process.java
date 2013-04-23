package org.eclipse.bpmn2.ui.editor.constructs;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class BPMN2Process extends Process {

	/**
	 * Creates a new instance of Process.
	 */
	public BPMN2Process() {
		super();
		
		this.name = "Default Process Diagram";
	}
	
	/**
	 * 
	 * @param type
	 */
	public void setProcessType(String type) {
		properties.selectTab("Process");
		new DefaultCombo("Process Type").setSelection(type);
	}
	
	/**
	 * 
	 * @param b
	 */
	public void setClosed(boolean b) {
		properties.selectTab("Process");
		properties.selectCheckBox(new CheckBox("Is Closed"), b);
	}
	
	/**
	 * ISSUES:
	 * 	1) Adding a lane set (e.g. name 'xxx') and adding a new lane (e.g. 'Lane1') will
	 *     create a new lane set with name 'xxx' and subelement 'Lane1'). Deleting the
	 *     lane set will not delete the lane and will put the process into an inconsistent
	 *     state.
	 *     
	 *     The element lane set 'xxx' will remain!
	 *     
	 *     Why is 'Lane1' create in the 'xxx' lane set?
	 *     
	 *     Delete lane set does not work. It does not delete the 'Lane1" lane!
	 *     	- set will be removed in the editor but not in text!
	 *     Delete lane does not delete the lane when it's in a set!
	 *     
	 * NOTE:
	 * 	- not usable with the jBPM5 engine.
	 * 
	 * @param name
	 */
	public void addLaneSet(String name) {
		// TBD
	}
	
	// TBD add other methods.
	
 }
