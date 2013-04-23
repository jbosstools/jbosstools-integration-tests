package org.eclipse.bpmn2.ui.editor.constructs.tasks;

import org.eclipse.bpmn2.ui.editor.ConstructType;
import org.eclipse.bpmn2.ui.editor.constructs.AbstractTask;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;


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
	 * @see org.eclipse.bpmn2.ui.editor.constructs.AbstractTask#setIsForCompensation(boolean)
	 */
	@Override
	public void setIsForCompensation(boolean b) {
		super.setIsForCompensation(b);
	}
		
}
