package org.jboss.tools.bpmn2.itests.editor.jbpm.activities;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.itests.editor.AbstractTask;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.IParameterMapping;


/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class UserTask extends AbstractTask {

	public UserTask(String name) {
		super(name, ConstructType.USER_TASK);
	}
	
	public void setTaskName(String name) {
		properties.selectTab("User Task");
		new LabeledText("Task Name").setText(name);
	}
	
	public void setPriority(int priority) {
		properties.selectTab("User Task");
		new LabeledText("Priority").setText(String.valueOf(priority));
	}
	
	public void setComment(String comment) {
		properties.selectTab("User Task");
		new LabeledText("Comment").setText(comment);
	}
	
	public void setGroupId(String id) {
		properties.selectTab("User Task");
		new LabeledText("Group Id").setText(id);
	}
	
	public void setSkippable(boolean skippable) {
		properties.selectTab("User Task");
		CheckBox box = new CheckBox("Skippable");
		if ((box.isChecked() && !skippable) || (!box.isChecked() && skippable)) {
			box.click();
		}
		
	}
	
	public void setContent(String content) {
		properties.selectTab("User Task");
		new LabeledText("Content").setText(content);
	}
	
	public void setLocale(String locale) {
		properties.selectTab("User Task");
		new LabeledText("Locale").setText(locale);
	}
	
	/**
	 * Used before Reddeer code bellow to set the actor data:
	 * 
	 * <code>
	 *	if (!language.isEmpty()) {
	 *		new DefaultCombo("Script Language").setSelection(language);
	 *	}
     *	new LabeledText("Script").setText(script);
	 * </code>
	 *
	 * The code created an issue when creating the 3rd UserTask in the SimpleModelingTest. When
	 * this method was called on the 
	 * 
	 * When the following code from SimpleModelingTest was executed an error was found. That
	 * error was caused because another properties view was selected (the process view) instead
	 * of the UserTask view.
	 * 
	 * <code>
	 *  UserTask userTask3 = new UserTask("PM Evaluation");
	 *  userTask3.addActor("John", "mvel");
	 * </code>
	 * 
	 * Looks like the error is caused by the Reddeer code because it works fine with plain old
	 * SWT Bot.
	 * 
	 * @param script
	 * @param language
	 */
	public void addActor(String language, String script) {
		properties.selectTab("User Task");
		properties.toolbarButton("Actors", "Add").click();
		
		if (!language.isEmpty()) {
			bot.comboBoxWithLabel("Script Language").setSelection(language);
		}
		bot.textWithLabel("Script").setText(script);
		bot.toolbarButtonWithTooltip("Close").click();
	}
	
	public void removeActor(String name) {
		properties.selectTab("User Task");
		properties.toolbarButton("Actors", "Remove").click();
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.AbstractTask#setIsForCompensation(boolean)
	 */
	@Override
	public void setIsForCompensation(boolean b) {
		super.setIsForCompensation(b);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.AbstractTask#setOnEntryScript(java.lang.String, java.lang.String)
	 */
	@Override
	public void setOnEntryScript(String language, String script) {
		super.setOnEntryScript(language, script);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.AbstractTask#setOnExistScript(java.lang.String, java.lang.String)
	 */
	@Override
	public void setOnExistScript(String language, String script) {
		super.setOnExistScript(language, script);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.AbstractTask#addInputParameter(org.jboss.tools.bpmn2.itests.editor.IParameterMapping)
	 */
	@Override
	public void addParameterMapping(IParameterMapping parameter) {
		super.addParameterMapping(parameter);
	}

}
