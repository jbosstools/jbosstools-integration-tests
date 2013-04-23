package org.eclipse.bpmn2.ui.editor.constructs.other;

import org.eclipse.bpmn2.ui.editor.ConstructType;
import org.eclipse.bpmn2.ui.editor.constructs.AbstractTask;
import org.eclipse.swtbot.swt.finder.SWTBot;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.util.Bot;


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
		CheckBox box = new CheckBox("Wait For Completion");
		if ((box.isChecked() && !b) || (!box.isChecked() && b)) {
			box.click();
		}
	}
	
	public void setWaitForCompletion(boolean b) {
		properties.selectTab("Call Activity");
		CheckBox box = new CheckBox("Independent");
		if ((box.isChecked() && !b) || (!box.isChecked() && b)) {
			box.click();
		}
	}
	
	public void setCalledActivity(String id) {
		properties.selectTab("Call Activity");
		new PushButton(0).click();
		
		SWTBot viewBot = Bot.get().shell("Called Activity").bot();
		viewBot.text().setText(id);
		viewBot.button("OK").click();
		
	}

	/**
	 * @see org.eclipse.bpmn2.ui.editor.constructs.AbstractTask#setIsForCompensation(boolean)
	 */
	@Override
	protected void setIsForCompensation(boolean b) {
		super.setIsForCompensation(b);
	}

	/**
	 * @see org.eclipse.bpmn2.ui.editor.constructs.AbstractTask#setOnEntryScript(java.lang.String, java.lang.String)
	 */
	@Override
	protected void setOnEntryScript(String language, String script) {
		super.setOnEntryScript(language, script);
	}

	/**
	 * @see org.eclipse.bpmn2.ui.editor.constructs.AbstractTask#setOnExistScript(java.lang.String, java.lang.String)
	 */
	@Override
	protected void setOnExistScript(String language, String script) {
		super.setOnExistScript(language, script);
	}
	
}
