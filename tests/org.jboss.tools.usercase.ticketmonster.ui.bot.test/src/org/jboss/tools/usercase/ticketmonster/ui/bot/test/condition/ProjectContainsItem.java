package org.jboss.tools.usercase.ticketmonster.ui.bot.test.condition;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.swt.condition.WaitCondition;

public class ProjectContainsItem implements WaitCondition {
	
	private String[] path;
	private Project project;
	
	public ProjectContainsItem(Project project, String... item){
		path = item;
		this.project = project;
	}

	@Override
	public boolean test() {
		return project.containsItem(path);
	}

	@Override
	public String description() {
		return "Project "+project+" contains item "+path;
	}

}
