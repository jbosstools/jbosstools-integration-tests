package org.jboss.tools.bpmn2.itests.editor.jbpm.activities;

import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.ContainerConstruct;
import org.jboss.tools.bpmn2.itests.editor.jbpm.DataType;

/**
 * TBD
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class SubProcess extends ContainerConstruct {

	/**
	 * 
	 * @param name
	 */
	public SubProcess(String name) {
		super(name, ConstructType.SUB_PROCESS);
	}
	
	public void addLocalVariable(String name, String dataType) {
		properties.selectTab("Sub Process");
		properties.toolbarButton("Variable List", "Add").click();
		new LabeledText("Name").setText(name);
		new DataType(dataType, 0).add();
		bot.toolbarButtonWithTooltip("Close").click();
	}
}
