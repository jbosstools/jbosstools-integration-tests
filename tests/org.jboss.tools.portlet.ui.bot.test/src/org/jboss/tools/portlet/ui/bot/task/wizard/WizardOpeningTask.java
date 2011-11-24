package org.jboss.tools.portlet.ui.bot.task.wizard;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.gen.INewObject;

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
		INewObject wizardPath = new INewObject() {
			
			@Override
			public String getName() {
				return name;
			}
			
			@Override
			public List<String> getGroupPath() {
				if (category == null){
					return Collections.emptyList();
				}
				
				return Arrays.asList(category.split("/"));
			}
		};
		SWTBotFactory.getOpen().newObject(wizardPath);
	}
}
