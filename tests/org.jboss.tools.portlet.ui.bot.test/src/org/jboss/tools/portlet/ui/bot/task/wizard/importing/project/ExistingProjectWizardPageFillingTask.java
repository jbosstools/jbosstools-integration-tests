package org.jboss.tools.portlet.ui.bot.task.wizard.importing.project;

import org.eclipse.swtbot.swt.finder.keyboard.KeyboardFactory;
import org.eclipse.swtbot.swt.finder.keyboard.Keystrokes;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.WizardPageFillingTask;

/**
 * Fills the wizard page for importing existing projects into workspace. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class ExistingProjectWizardPageFillingTask extends AbstractSWTTask implements WizardPageFillingTask {

	private String[] projectNames;

	private String projectPath;

	private String zipFilePath;

	private boolean copyProjectsIntoWorkspace;

	public ExistingProjectWizardPageFillingTask() {
		super();
	}

	@Override
	public void perform() {
		loadProjects();
		selectProjects();
	}

	private void loadProjects() {
		if (projectPath != null && zipFilePath != null){
			throw new IllegalArgumentException("You have to choose between folder and ZIP path");
		}

		if (projectPath != null){
			loadProjectsFromFolder();
			return;
		}

		if (zipFilePath != null){
			loadProjectsFromZIP();
			return;
		}

		throw new IllegalArgumentException("You have to provide either folder or ZIP path");
	}

	private void loadProjectsFromFolder() {
		getBot().text(0).setText(projectPath);
		KeyboardFactory.getAWTKeyboard().pressShortcut(Keystrokes.TAB);
	}

	private void loadProjectsFromZIP() {
		getBot().radio("Select archive file:").click();
		getBot().text(1).setText(zipFilePath);
		KeyboardFactory.getAWTKeyboard().pressShortcut(Keystrokes.TAB);
	}

	private void selectProjects() {
		selectCopyProjectsIntoWorkspace();
		getBot().button("Deselect All").click();
		SWTBotTree projectsTree = getBot().treeWithLabel("Projects:");
		for (String projectName : projectNames){
			SWTBotTreeItem  projectItem = projectsTree.getTreeItem(getProjectLabel(projectName));
			projectItem.check();
		}
	}

	private void selectCopyProjectsIntoWorkspace() {
		if (isFileSystem()){
			if (copyProjectsIntoWorkspace){
				getBot().checkBox("Copy projects into workspace").select();
			} else {
				getBot().checkBox("Copy projects into workspace").deselect();
			}
		}
	}

	private String getProjectLabel(String project){
		if (isFileSystem()){
			return project + " (" + projectPath + "/" + project + ")";
		} else {
			return project + " (" + project + ")";
		}
	}

	private boolean isFileSystem(){
		return projectPath != null;
	}

	public void setProjectNames(String... projectNames) {
		this.projectNames = projectNames;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	public void setZipFilePath(String zipFilePath) {
		this.zipFilePath = zipFilePath;
	}

	public void setCopyProjectsIntoWorkspace(boolean copyProjectsIntoWorkspace) {
		this.copyProjectsIntoWorkspace = copyProjectsIntoWorkspace;
	}
}
