package org.jboss.tools.maven.ui.bot.test.utils;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.dialogs.ExplorerItemPropertyDialog;
import org.jboss.reddeer.eclipse.ui.dialogs.PropertyDialog;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

public class ProjectHasNature extends AbstractWaitCondition {

	private String projectName;
	private String natureID;
	private String version;
	private String natureParent;
	private Project project;

	public ProjectHasNature(String projectName, String natureID, String version) {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		this.project = pe.getProject(projectName);
		this.project.select();
		this.projectName = projectName;
		this.natureID = natureID;
		this.version = version;
	}

	public ProjectHasNature(String projectName, String natureParent, String natureID, String version) {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		this.project = pe.getProject(projectName);
		this.project.select();
		this.projectName = projectName;
		this.natureID = natureID;
		this.version = version;
		this.natureParent = natureParent;
	}

	public boolean test() {
		PropertyDialog pd = new ExplorerItemPropertyDialog(project);
		pd.open();
		new WaitUntil(new ShellWithTextIsActive("Properties for " + projectName), TimePeriod.NORMAL);
		new DefaultTreeItem("Project Facets").select();
		boolean result;
		if (natureParent != null) {
			result = new DefaultTreeItem(new DefaultTree(1), natureParent, natureID).isChecked();
			if (version != null) {
				result = result
						&& new DefaultTreeItem(new DefaultTree(1), natureParent, natureID).getCell(1).equals(version);
			}
		} else {
			result = new DefaultTreeItem(new DefaultTree(1), natureID).isChecked();
			if (version != null) {
				result = result && new DefaultTreeItem(new DefaultTree(1), natureID).getCell(1).equals(version);
			}
		}
		pd.ok();
		new WaitWhile(new ShellWithTextIsAvailable("Properties for " + projectName), TimePeriod.NORMAL);
		return result;
	}

	@Override
	public String description() {
		return "Project " + projectName + " doesn't not have nature " + natureID;
	}

}
