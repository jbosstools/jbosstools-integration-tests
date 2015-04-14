/*******************************************************************************
 * Copyright (c) 2010-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.archives.ui.bot.test;


import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.ide.NewJavaProjectWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.ide.NewJavaProjectWizardPage;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.eclipse.ui.views.log.LogView;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.archives.reddeer.archives.ui.ProjectArchivesExplorer;
import org.jboss.tools.archives.reddeer.archives.ui.ProjectArchivesView;
import org.jboss.tools.common.reddeer.label.IDELabel;
import org.junit.After;

/**
 * 
 * @author jjankovi
 *
 */
@CleanWorkspace
@OpenPerspective(JavaPerspective.class)
public class ArchivesTestBase{

	protected ProjectExplorer projectExplorer = new ProjectExplorer();
	protected ProjectArchivesView view = new ProjectArchivesView();
	
	@After
	public void cleanUp() {
		projectExplorer.open();
		for (Project project : projectExplorer.getProjects()) {
		    project.select();
		    new ContextMenu("Refresh").select();
		    new WaitWhile(new JobIsRunning());
			project.delete(true);
		}
	}
	
	protected ProjectArchivesView openProjectArchivesView() {
		view.open();
		return view;
	}
	
	protected ProjectArchivesView viewForProject(String projectName) {
		view.open();
		projectExplorer.open();
		projectExplorer.getProject(projectName).select();
		view.open();
		return view;
	}
	
	protected ProjectArchivesExplorer explorerForProject(String projectName) {
		return new ProjectArchivesExplorer(projectName);
	}
	
	protected void assertArchiveIsInView(ProjectArchivesView view, 
			String archiveName) {
		try {
			view.open();
			view.getProject().getArchive(archiveName);
		} catch (Exception sle) {
			fail("'" + archiveName + "' is not in archives view but it should!");
		}
	}
	
	protected void assertArchiveIsNotInView(ProjectArchivesView view, 
			String archiveName) {
		try {
			view.open();
			view.getProject().getArchive(archiveName);
			fail("'" + archiveName + "' is in archives view but it should not!");
		} catch (Exception sle) {
			
		}
	}
	
	protected void assertArchiveIsInExplorer(ProjectArchivesExplorer explorer, 
			String archiveName) {
		try {
			explorer.getArchive(archiveName);
		} catch (Exception sle) {
			fail("'" + archiveName + "' is not in archives explorer but it should!");
		}
	}
	
	protected void assertArchiveIsNotInExplorer(ProjectArchivesExplorer explorer, 
			String archiveName) {
		try {
			explorer.getArchive(archiveName);
			fail("'" + archiveName + "' is in archives explorer but it should not!");
		} catch (Exception sle) {
		}
	}
	
	protected static void clearErrorView() {
		LogView lw = new LogView();
		lw.open();
		lw.clearLog();
	}
	
	protected static void createJavaProject(String projectName) {
		NewJavaProjectWizardDialog javaProject = new NewJavaProjectWizardDialog();
		javaProject.open();
		
		NewJavaProjectWizardPage javaWizardPage = javaProject.getFirstPage();
		javaWizardPage.setProjectName(projectName);
		
		javaProject.finish(false);
	}
	
	protected static void importArchiveProjectWithoutRuntime(String projectName) {
		
		String location = "resources/prj/" + projectName;
		
		ExternalProjectImportWizardDialog importDialog = new ExternalProjectImportWizardDialog();
		importDialog.open();
		WizardProjectsImportPage importPage = (WizardProjectsImportPage)importDialog.getFirstPage();
		try {
			importPage.setRootDirectory((new File(location)).getCanonicalPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		importPage.copyProjectsIntoWorkspace(true);
		importDialog.finish();
	}
	
	protected void addArchivesSupport(String projectName) {
		addRemoveArchivesSupport(projectName, true);
	}
	
	protected void removeArchivesSupport(String projectName) {
		addRemoveArchivesSupport(projectName, false);
	}
	
	private void addRemoveArchivesSupport(String projectName, boolean add) {
		projectExplorer.open();
		projectExplorer.getProject(projectName).select();
		if (add) {
			new ContextMenu(IDELabel.Menu.PACKAGE_EXPLORER_CONFIGURE, 
					IDELabel.Menu.ADD_ARCHIVE_SUPPORT).select();
		} else {
			new ContextMenu(IDELabel.Menu.PACKAGE_EXPLORER_CONFIGURE, 
					IDELabel.Menu.REMOVE_ARCHIVE_SUPPORT).select();
		}
		new WaitWhile(new JobIsRunning());
	}
	
}
