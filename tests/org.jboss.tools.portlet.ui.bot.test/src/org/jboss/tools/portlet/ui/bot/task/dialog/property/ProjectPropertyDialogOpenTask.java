package org.jboss.tools.portlet.ui.bot.task.dialog.property;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;

public class ProjectPropertyDialogOpenTask extends AbstractSWTTask{

	private String project;
	
	private String propertyPage;

	@Override
	public void perform() {
		new ProjectExplorer().getProject(project).select();
		new ContextMenu("Properties").select();
		new DefaultTreeItem(propertyPage).select();
	}	

	public void setProject(String project) {
		this.project = project;
	}
	
	public void setPropertyPage(String propertyPage) {
		this.propertyPage = propertyPage;
	}
}
