package org.jboss.ide.eclipse.as.ui.bot.test.wizard;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.keyboard.KeyboardFactory;
import org.eclipse.swtbot.swt.finder.keyboard.Keystrokes;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.wizards.SWTBotImportWizard;

public class ImportProjectWizard {

	private SWTBotImportWizard wizard = new SWTBotImportWizard();
	
	private String[] projectNames;

	private String projectPath;

	private String zipFilePath;

	private boolean copyProjectsIntoWorkspace;
	
	public void execute(){
		wizard.open(ActionItem.Import.GeneralExistingProjectsintoWorkspace.LABEL.getName(), ActionItem.Import.GeneralExistingProjectsintoWorkspace.LABEL.getGroupPath().get(0));
		loadProjects();
		selectProjects();
		wizard.finishWithWait();
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
		for (SWTBotTreeItem item : projectsTree.getAllItems()){
			System.out.println(item.getText());
		}
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
	
	private SWTBot getBot(){
		return SWTBotFactory.getBot();
	}
}
