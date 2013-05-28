package org.jboss.tools.bpmn2.itests.editor.constructs.other;

import org.eclipse.swtbot.swt.finder.SWTBot;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.constructs.AbstractTask;
import org.jboss.tools.bpmn2.itests.editor.properties.variables.IParameter;


/**
 * ISSUES:
 * 	1) Unable to set name - the name is set but UI not updated.
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class CallActivity extends AbstractTask {
	
	/**
	 * 
	 * @param name
	 */
	public CallActivity(String name) {
		super(name, ConstructType.CALL_ACTIVITY);
	}
	
	public void setIndependent(boolean b) {
		properties.selectTab("Call Activity");
		//properties.selectCheckBox(new CheckBox("Independent"), b);
		properties.selectCheckBox(new CheckBox(1), b);
	}
	
	public void setWaitForCompletion(boolean b) {
		properties.selectTab("Call Activity");
//		properties.selectCheckBox(new CheckBox("Wait For Completion"), b);
		properties.selectCheckBox(new CheckBox(0), b);
	}
	
	public void setCalledActivity(String id) {
		properties.selectTab("Call Activity");
		new PushButton(0).click();
		
		SWTBot viewBot = Bot.get().shell("Called Activity").bot();
		viewBot.text().setText(id);
		viewBot.button("OK").click();
		
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.constructs.AbstractTask#setIsForCompensation(boolean)
	 */
	@Override
	public void setIsForCompensation(boolean b) {
		super.setIsForCompensation(b);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.constructs.AbstractTask#setOnEntryScript(java.lang.String, java.lang.String)
	 */
	@Override
	public void setOnEntryScript(String language, String script) {
		super.setOnEntryScript(language, script);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.constructs.AbstractTask#setOnExistScript(java.lang.String, java.lang.String)
	 */
	@Override
	public void setOnExistScript(String language, String script) {
		super.setOnExistScript(language, script);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.constructs.AbstractTask#addParameterMapping(org.jboss.tools.bpmn2.itests.editor.properties.variables.IParameter)
	 */
	@Override
	public void addParameterMapping(IParameter parameter) {
		super.addParameterMapping(parameter);
	}
	
	
	
}
