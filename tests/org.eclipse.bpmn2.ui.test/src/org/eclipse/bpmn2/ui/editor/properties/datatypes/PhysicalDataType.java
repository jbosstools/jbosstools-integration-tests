package org.eclipse.bpmn2.ui.editor.properties.datatypes;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.reddeer.swt.util.Bot;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class PhysicalDataType extends AbstractDataType {

	private int index;
	
	private boolean isCollection;
	
	/**
	 * Creates a new instance of PhysicalDataType.
	 * 
	 * @param typeName
	 * @param isCollection
	 */
	public PhysicalDataType(String typeName, boolean isCollection) {
		this(typeName, isCollection, 0);
	}
	
	/**
	 * Creates a new instance of PhysicalDataType.
	 *
	 * @param typeName
	 * @param isCollection
	 * @param index
	 */
	public PhysicalDataType(String typeName, boolean isCollection, int index) {
		super(typeName);
		
		this.index = index;
	}

	/**
	 * 
	 */
	public void add() {
		SWTBot bot = Bot.get();
		bot.button(index).click();

		SWTBot dialogBot = Bot.get().shell("Create New Data Type").bot();
		dialogBot.comboBoxWithLabel("Item Kind").setSelection("Physical");
		if (isCollection) dialogBot.checkBoxWithLabel("Is Collection").click();
		dialogBot.textWithLabel("Data Type").setText(typeName);
		dialogBot.button("OK").click();
		
		bot.toolbarButtonWithTooltip("Close").click();
	}
		
}
