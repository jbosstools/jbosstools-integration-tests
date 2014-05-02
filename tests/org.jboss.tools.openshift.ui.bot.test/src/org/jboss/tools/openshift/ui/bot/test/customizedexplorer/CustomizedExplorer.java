package org.jboss.tools.openshift.ui.bot.test.customizedexplorer;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;

public abstract class CustomizedExplorer extends WorkbenchView {

	public CustomizedExplorer(String viewTitle) {
		super(viewTitle);
	}

	public List<CustomizedProject> getProjects() {
		List<CustomizedProject> projects = new ArrayList<CustomizedProject>();
		
		open();
		List<TreeItem> projectItems = new DefaultTree().getItems();
		for (TreeItem item: projectItems) {
			projects.add(new CustomizedProject(item));
		}
		
		return projects;
	}
	
	public boolean containsProject(String projectName) {
		try {
			getProject(projectName);
			return true;
		} catch (EclipseLayerException ex) {
			return false;
		}
	}
	
	public CustomizedProject getProject(String projectName) {
		for (CustomizedProject project: getProjects()) {
			if (project.getName().equals(projectName)) {
				return project;
			}
		}
		throw new EclipseLayerException("There is no project with name " + projectName);
	}	
	
}
