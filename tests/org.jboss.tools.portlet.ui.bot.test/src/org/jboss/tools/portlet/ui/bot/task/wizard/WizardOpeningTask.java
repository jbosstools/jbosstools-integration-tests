package org.jboss.tools.portlet.ui.bot.task.wizard;

import org.apache.log4j.Logger;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.wizards.SWTBotNewObjectWizard;

/**
 * Opens a specified wizard. 
 * 
 * @author ljelinko
 *
 */
public class WizardOpeningTask extends AbstractSWTTask {
	
	private static final Logger log = Logger.getLogger(WizardOpeningTask.class);

	private String category;

	private String name;

	public WizardOpeningTask(String name) {
		super();
		this.name = name;
	}

	/**
	 * 
	 * @param name Name of the wizard to open
	 * @param categoryPath path to the wizard (categories separated by '/')
	 */
	public WizardOpeningTask(String name, String categoryPath) {
		this(name);
		this.category = categoryPath;
	}

	@Override
	public void perform() {
		log.info("Opening wizard");
		log.info("All shells: ");
		for (SWTBotShell shell : SWTBotFactory.getBot().shells()){
			log.info(shell.getText() + ": " + shell);
		}
		new SWTBotNewObjectWizard().open(name, getGroupPath());
	}
	
	private String[] getGroupPath() {
		if (category == null){
			return new String[0];
		}
		
		return category.split("/");
	}
}
