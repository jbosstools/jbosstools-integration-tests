package org.jboss.tools.maven.ui.bot.test.utils;

import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.exception.CoreLayerException;
import org.eclipse.reddeer.eclipse.core.resources.Project;
import org.eclipse.reddeer.eclipse.ui.dialogs.PropertyDialog;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;

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
		PropertyDialog pd = new PropertyDialog(project.getName());
		pd.open();
		new WaitUntil(new ShellIsAvailable("Properties for " + projectName), TimePeriod.DEFAULT);
		new DefaultTreeItem("Project Facets").select();
		
		// When Properties window is opened on newer Java than version 1.8 a "Setting
		// Java Build Path" dialog is sometimes shown and it needs to apply the
		// changes of Java Build Path (it is usually shown when previous test has worked
		// with the Properties window (Java Build Path) and was using different version
		// of Java than the current test). In case that the dialog will not appear
		// CoreLayerException is thrown and nothing is needed to do in the catch block
		if (!JavaVersionHelper.getJavaVersion().equals("1.8")) {
			try {
				new DefaultShell("Setting Java Build Path");
				new PushButton("Apply").click();
			} catch (CoreLayerException e) {
				// No 'Setting Java Build Path' dialog has appeared (running on newer Java than 1.8)
			}
		}
		
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
		new WaitWhile(new ShellIsAvailable("Properties for " + projectName), TimePeriod.DEFAULT);
		return result;
	}

	@Override
	public String description() {
		return "Project " + projectName + " doesn't not have nature " + natureID;
	}

}
