package org.jboss.tools.bpmn2.itests.editor.jbpm.dataobjects;

import org.eclipse.swtbot.swt.finder.SWTBot;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.util.Bot;

import org.jboss.tools.bpmn2.itests.editor.Construct;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.jbpm.DataType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class DataObject extends Construct {

	public DataObject(String name) {
		super(name, ConstructType.DATA_OBJECT);
	}

	/**
	 * 
	 * @param name
	 */
	protected void setDataObjectName(String name) {
		properties.selectTab(type.toToolName());
		new LabeledText("Name").setText(name);
	}
	
	/**
	 * 
	 * @param dataType
	 */
	protected void setDataObjectType(DataType dataType) {
		properties.selectTab(type.toToolName());
		
		try {
			new DefaultCombo("Item Subject").setSelection(dataType.getName());
			
			dataType.add();
		} catch (Exception e) {
			new PushButton(0).click();
			SWTBot viewBot = Bot.get().shell("Create New Data Type").bot();
			viewBot.textWithLabel("Data Type").setText(dataType.getName());
			viewBot.button("OK").click();
		}
	}
	
}
