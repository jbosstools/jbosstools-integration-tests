package org.jboss.tools.portlet.ui.bot.task.importing;

import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;
import org.jboss.tools.ui.bot.ext.wizards.SWTBotImportWizard;

/**
 * Imports the specified object under the specified category. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class ImportingTask extends AbstractSWTTask {

	private String category;

	private String name;
	
	public ImportingTask(String name) {
		super();
		this.name = name;
	}
	
	public ImportingTask(String category, String name) {
		super();
		this.category = category;
		this.name = name;
	}

	@Override
	public void perform() {
		new SWTBotImportWizard().open(name, getGroupPath());
	}

	private String[] getGroupPath() {
		if (category == null){
			return new String[0]; 
		}
		
		return category.split("/");
	}
}
