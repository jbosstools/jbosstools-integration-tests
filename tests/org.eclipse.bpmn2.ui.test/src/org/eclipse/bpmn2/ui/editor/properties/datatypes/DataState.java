package org.eclipse.bpmn2.ui.editor.properties.datatypes;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.reddeer.swt.util.Bot;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class DataState extends AbstractDataState {

	private int index;
	
	/**
	 * Creates a new instance of DataState.
	 * 
	 * @param stateName
	 */
	public DataState(String stateName) {
		this(stateName, 0);
	}
	
	/**
	 * Creates a new instance of DataState.
	 *
	 * @param stateName
	 * @param index
	 */
	public DataState(String stateName, int index) {
		super(stateName);
		this.index = index;
	}
	
	/**
	 * 
	 */
	public void add() {
		SWTBot bot = Bot.get();
		bot.button(index).click();
		
		SWTBot dialogBot = Bot.get().shell("Create New Data State").bot();
		dialogBot.textWithLabel("Name").setText(stateName);
		dialogBot.button("OK").click();
		
		bot.toolbarButtonWithTooltip("Close").click();
	}

}