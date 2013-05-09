package org.jboss.ide.eclipse.as.ui.bot.test.wizard;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.waits.ICondition;
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
		getBot().text(0).setFocus();
		getProjectsTree().setFocus();
		getBot().waitUntil(new ProjectIsLoaded());
	}

	private void loadProjectsFromZIP() {
		getBot().radio("Select archive file:").click();
		getBot().comboBox(1).setText(zipFilePath);
		getBot().comboBox(1).setFocus();
		getProjectsTree().setFocus();
		getBot().waitUntil(new ProjectIsLoaded());
	}

	private void selectProjects() {
		selectCopyProjectsIntoWorkspace();
		getBot().button("Deselect All").click();
		SWTBotTree projectsTree = getProjectsTree();
		
		for (String projectName : projectNames){
			SWTBotTreeItem  projectItem = getProjectTreeItem(projectsTree, projectName);
			projectItem.check();
		}
	}

	private SWTBotTree getProjectsTree() {
		return getBot().treeWithLabel("Projects:");
	}

	private SWTBotTreeItem getProjectTreeItem(SWTBotTree projectsTree, String projectName) {
		for (SWTBotTreeItem item : projectsTree.getAllItems()){
			if (projectName.equals(getProjectLabel(item.getText()))){
				return item;
			}
		}
		throw new IllegalStateException("Project " + projectName + " not available");
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
		return project.substring(0, project.indexOf('(')).trim();
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
	
	private class ProjectIsLoaded implements ICondition {

		private SWTBotTree tree;
		
		@Override
		public boolean test() {
			return tree.hasItems();
		}

		@Override
		public String getFailureMessage() {
			return "At least one project is loaded";
		}

		@Override
		public void init(SWTBot bot) {
			tree = getBot().treeWithLabel("Projects:");
		}
	}
}
