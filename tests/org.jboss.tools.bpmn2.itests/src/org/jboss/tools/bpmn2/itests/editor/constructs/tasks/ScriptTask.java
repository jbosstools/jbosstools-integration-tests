package org.jboss.tools.bpmn2.itests.editor.constructs.tasks;

import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.constructs.AbstractTask;


/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class ScriptTask extends AbstractTask {

	/**
	 * 
	 * @param name
	 */
	public ScriptTask(String name) {
		super(name, ConstructType.SCRIPT_TASK);
	}

	public void setScript(String language, String script) {
		properties.selectTab("Script Task");
		new DefaultCombo("Script Language").setSelection(language);
		new LabeledText("Script").setText(script);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.constructs.AbstractTask#setIsForCompensation(boolean)
	 */
	@Override
	public void setIsForCompensation(boolean b) {
		super.setIsForCompensation(b);
	}
		
}
