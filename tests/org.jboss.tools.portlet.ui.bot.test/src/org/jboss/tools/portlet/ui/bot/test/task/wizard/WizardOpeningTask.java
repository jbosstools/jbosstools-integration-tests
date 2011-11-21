package org.jboss.tools.portlet.ui.bot.test.task.wizard;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.portlet.ui.bot.test.task.AbstractSWTTask;

/**
 * Opens a specified wizard. 
 * 
 * @author ljelinko
 *
 */
public class WizardOpeningTask extends AbstractSWTTask {
	
	private String category;
	
	private String name;
	
	public WizardOpeningTask(String name) {
		super();
		this.name = name;
	}
	
	public WizardOpeningTask(String name, String category) {
		this(name);
		this.category = category;
	}

	@Override
	public void perform() {
		getBot().menu("File").menu("New").menu("Other...").click();

		SWTBotTree tree  = getBot().tree();
		getTreeItem(tree).select();
		 
		getBot().button("Next >").click();
	}
	
	private SWTBotTreeItem getTreeItem(SWTBotTree tree){
		if (category == null){
			return tree.getTreeItem(name);
		} else {
			tree.expandNode(category);
			return tree.getTreeItem(category).getNode(name);			
		}
	}
}
