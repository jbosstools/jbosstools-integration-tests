package org.jboss.tools.portlet.ui.bot.task.wizard.importing.project;

import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;

/**
 * Fills the wizard page for importing existing projects into workspace. 
 * 
 * @author Lucia Jelinkova
 * @author Petr Suchy
 *
 */
public class ExistingProjectWizardTask extends AbstractSWTTask {

	private WizardProjectsImportPage page; 
	
	private String[] projectNames;

	private String projectPath;

	private String zipFilePath;

	private boolean copyProjectsIntoWorkspace;

	public ExistingProjectWizardTask() {
		super();
	}

	@Override
	public void perform() {
		ExternalProjectImportWizardDialog dialog = new ExternalProjectImportWizardDialog();
		dialog.open();
		page = new WizardProjectsImportPage();
		loadProjects();
		selectProjects();
		dialog.finish();
	}

	private void loadProjects() {
		if (projectPath != null && zipFilePath != null){
			throw new IllegalArgumentException("You have to choose between folder and ZIP path");
		}

		if (projectPath != null){
			page.setRootDirectory(projectPath);
			return;
		}

		if (zipFilePath != null){
			page.setArchiveFile(zipFilePath);
			return;
		}

		throw new IllegalArgumentException("You have to provide either folder or ZIP path");
	}

	private void selectProjects() {
		selectCopyProjectsIntoWorkspace();
		page.selectProjects(projectNames);
	}

	private void selectCopyProjectsIntoWorkspace() {
		if (isFileSystem()){
			if (copyProjectsIntoWorkspace){
				page.copyProjectsIntoWorkspace(true);
			} else {
				page.copyProjectsIntoWorkspace(false);
			}
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
