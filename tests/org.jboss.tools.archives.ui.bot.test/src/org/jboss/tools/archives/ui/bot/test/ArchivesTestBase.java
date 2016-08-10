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


import static org.junit.Assert.*;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.ide.NewJavaProjectWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.ide.NewJavaProjectWizardPage;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.eclipse.ui.views.log.LogView;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.archives.reddeer.archives.ui.NewJarDialog;
import org.jboss.tools.archives.reddeer.archives.ui.ProjectArchivesExplorer;
import org.jboss.tools.archives.reddeer.archives.ui.ProjectArchivesView;
import org.jboss.tools.common.reddeer.label.IDELabel;
import org.junit.BeforeClass;

/**
 * 
 * @author jjankovi
 *
 */
@OpenPerspective(JavaPerspective.class)
@CleanWorkspace
public class ArchivesTestBase{

	protected static ProjectExplorer projectExplorer = new ProjectExplorer();
	protected static ProjectArchivesView view = new ProjectArchivesView();
	protected static final Logger log = Logger.getLogger(ArchivesTestBase.class);
	
	@BeforeClass
	public static void maximizeWorkbench(){
		new WorkbenchShell().maximize();
	}
	
	protected ProjectArchivesView openProjectArchivesView() {
		view.open();
		return view;
	}
	
	protected static ProjectArchivesView viewForProject(String projectName) {
		view.open();
		projectExplorer.open();
		projectExplorer.getProject(projectName).select();
		view.open();
		return view;
	}
	
	protected ProjectArchivesExplorer explorerForProject(String projectName) {
		return new ProjectArchivesExplorer(projectName);
	}
	
	protected void assertArchiveIsInView(String project, ProjectArchivesView view, String archiveName) {
		view.open();
		assertTrue(view.getProject(project).hasArchive(archiveName));
	}
	
	protected void assertArchiveIsNotInView(String project, ProjectArchivesView view, String archiveName) {
		view.open();
		assertFalse(view.getProject(project).hasArchive(archiveName));
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
	
	protected static void deleteErrorView() {
		LogView lw = new LogView();
		lw.open();
		lw.deleteLog();
	}
	
	protected static void createJavaProject(String projectName) {
		NewJavaProjectWizardDialog javaProject = new NewJavaProjectWizardDialog();
		javaProject.open();
		
		NewJavaProjectWizardPage javaWizardPage = new NewJavaProjectWizardPage();
		javaWizardPage.setProjectName(projectName);
		
		javaProject.finish(false);
	}
	
	protected static void addArchivesSupport(String projectName) {
		addRemoveArchivesSupport(projectName, true);
	}
	
	protected static void removeArchivesSupport(String projectName) {
		addRemoveArchivesSupport(projectName, false);
	}
	
	private static void addRemoveArchivesSupport(String projectName, boolean add) {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(projectName).select();
		if (add) {
			new ContextMenu(IDELabel.Menu.PACKAGE_EXPLORER_CONFIGURE, 
					IDELabel.Menu.ADD_ARCHIVE_SUPPORT).select();
		} else {
			new ContextMenu(IDELabel.Menu.PACKAGE_EXPLORER_CONFIGURE, 
					IDELabel.Menu.REMOVE_ARCHIVE_SUPPORT).select();
		}
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	protected static void createArchive(NewJarDialog dialog, String archiveName, boolean standardCompression) {
		dialog.setArchiveName(archiveName);
		if (standardCompression) {
			dialog.setZipStandardArchiveType();
		} else {
			dialog.setNoCompressionArchiveType();
		}
		dialog.finish();
	}
	
	protected static void createArchive(String project, String archiveName, boolean standardCompression) {
		view = viewForProject(project);
		NewJarDialog dialog = view.getProject(project).newJarArchive();
		dialog.setArchiveName(archiveName);
		if (standardCompression) {
			dialog.setZipStandardArchiveType();
		} else {
			dialog.setNoCompressionArchiveType();
		}
		dialog.finish();
	}
	
}
