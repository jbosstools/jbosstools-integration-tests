package org.jboss.tools.bpmn2.itests.editor.jbpm;

import org.eclipse.swtbot.swt.finder.SWTBot;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.bpmn2.itests.editor.IMappingSide;

/**
 * Represents the target side of parameter mapping.
 * 
 * @author mbaluch
 */
public class FromDataOutput implements IMappingSide {

	private String name;
	
	private String dataType;

	/**
	 * Creates a new instance of FromDataInput.
	 * @param name
	 */
	public FromDataOutput(String name) {
		this(name, null);
	}
	
	/**
	 * Creates a new instance of FromDataInput.
	 * 
	 * @param name
	 * @param dataType
	 */
	public FromDataOutput(String name, String dataType) {
		this.name = name;
		this.dataType = dataType;
	}
	
	/**
	 * Define new data input.
	 */
	public void add() {
		new LabeledText("Name").setText(name);
		
		if (dataType != null) {
			try {
				new DefaultCombo("Data Type").setSelection(dataType);
			} catch (Exception e) {
				new PushButton(0).click();
				SWTBot windowBot = Bot.get().shell("Create New Data Type").bot();
				windowBot.textWithLabel("Structure").setText(dataType);
				windowBot.button("OK").click();
			}
		}
	}
	
	/**
	 * 
	 * @return 
	 */
	@Override
	public String getName() {
		return name;
	}
	
}
