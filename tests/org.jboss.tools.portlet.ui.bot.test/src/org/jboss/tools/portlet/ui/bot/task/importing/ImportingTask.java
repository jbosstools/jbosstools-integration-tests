package org.jboss.tools.portlet.ui.bot.task.importing;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.gen.IImport;

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
		IImport importPath = new IImport() {
			
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
		SWTBotFactory.getOpen().newImport(importPath);
	}
}
