package org.eclipse.bpmn2.ui.editor.constructs;

import org.eclipse.bpmn2.ui.editor.ConstructType;
import org.eclipse.swtbot.swt.finder.SWTBot;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.util.Bot;


/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class AbstractDataObject extends Construct {

	/**
	 * 
	 * @param name
	 * @param type
	 */
	public AbstractDataObject(String name, ConstructType type) {
		super(name, type);
	}

	/**
	 * 
	 * @param name
	 */
	protected void setDataObjectName(String name) {
		properties.selectTab(type.toName());
		new LabeledText("Name").setText(name);
	}
	
	/**
	 * 
	 * @param dataType
	 */
	protected void setDataObjectType(String dataType) {
		properties.selectTab(type.toName());
		
		try {
			new DefaultCombo("Data Type").setSelection(dataType);
		} catch (Exception e) {
			new PushButton(0).click();
			SWTBot viewBot = Bot.get().shell("Create New Data Type").bot();
			viewBot.textWithLabel("Data Type").setText(dataType);
			viewBot.button("OK").click();
		}
	}
	
}
