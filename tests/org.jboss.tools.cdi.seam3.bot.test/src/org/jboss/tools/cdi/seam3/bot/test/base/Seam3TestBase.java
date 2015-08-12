/*******************************************************************************
 * Copyright (c) 2010-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.cdi.seam3.bot.test.base;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.cdi.reddeer.uiutils.EditorResourceHelper;
import org.jboss.tools.cdi.reddeer.uiutils.OpenOnHelper;
import org.jboss.tools.cdi.reddeer.uiutils.ValidationHelper;
import org.jboss.tools.cdi.seam3.bot.test.util.LibraryHelper;
import org.jboss.tools.cdi.seam3.bot.test.util.SeamLibrary;
import org.jboss.tools.common.reddeer.preferences.SourceLookupPreferencePage;
import org.jboss.tools.common.reddeer.preferences.SourceLookupPreferencePage.SourceAttachmentEnum;

/**
 * 
 * @author jjankovi
 *
 */
public class Seam3TestBase {
	

	protected static final Logger LOGGER = Logger.getLogger(Seam3TestBase.class.getName());

	protected static String projectName = "CDISeam3Project";
	private String packageName = "cdi.seam";
	
	protected final static LibraryHelper libraryHelper = new LibraryHelper();
	protected final static EditorResourceHelper editResourceUtil = new EditorResourceHelper();
	protected static final ValidationHelper validationHelper = new ValidationHelper();
	protected static final OpenOnHelper openOnHelper = new OpenOnHelper();
	
	public String getPackageName() {
		return packageName;
	}
	
	public void prepareWorkspace() {
		
	}
	
	/**
	 * 
	 * @param projectName
	 * @param projectLocation
	 * @param dir
	 */
	protected static void importSeam3TestProject(String projectName, String projectLocation, String runtimeName) {
		
		ExternalProjectImportWizardDialog iDialog = new ExternalProjectImportWizardDialog();
		iDialog.open();
		WizardProjectsImportPage fPage = new WizardProjectsImportPage();
		fPage.copyProjectsIntoWorkspace(true);
		try {
			fPage.setRootDirectory((new File(projectLocation)).getParentFile().getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fPage.selectProjects(projectName);
		iDialog.finish();
		
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(projectName).select();
		new ContextMenu("Properties").select();
		new DefaultShell("Properties for "+projectName);
		new DefaultTreeItem("Targeted Runtimes").select();
		for(TableItem i: new DefaultTable().getItems()){
			i.setChecked(false);
			if(i.getText().equals(runtimeName)){
				i.setChecked(true);
			}
		}
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsAvailable("Properties for "+projectName));
		new WaitWhile(new JobIsRunning());
	}
	
	/**
	 * 
	 * @param projectName
	 * @param library
	 */
	protected static void importSeam3ProjectWithLibrary(String projectName, 
			SeamLibrary library, String runtimeName) {
		importSeam3TestProject(projectName, "resources/projects/",runtimeName);
		addAndCheckLibraryInProject(projectName, library);
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProjects().get(0).select();
		new ShellMenu("Project","Clean...").select();
		new DefaultShell("Clean");
		new RadioButton("Clean all projects").toggle(true);
		new PushButton("OK").click();
		new WaitWhile(new JobIsRunning(),TimePeriod.LONG);
	}
	
	protected void cleanProjects(){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProjects().get(0).select();
		new ShellMenu("Project","Clean...").select();
		new DefaultShell("Clean");
		new RadioButton("Clean all projects").toggle(true);
		new PushButton("OK").click();
		new WaitWhile(new JobIsRunning(),TimePeriod.LONG);
	}
	
	/**
	 * 
	 * @param projectName
	 * @param library
	 */
	protected static void addAndCheckLibraryInProject(String projectName, 
			SeamLibrary library) {
		addLibraryIntoProject(projectName, library.getLibrariesNames());
	}
	
	/**
	 * 
	 * @param projectName
	 * @param libraries
	 */
	private static void addLibraryIntoProject(String projectName, Collection<String> libraries) {
		try {
			Iterator<String> iter = libraries.iterator();
			while (iter.hasNext()) {
				String temp = iter.next();
				libraryHelper.addLibraryIntoProjectFolder(projectName, temp);
				LOGGER.info("Library: \"" + temp + "\" copied");
				new WaitWhile(new JobIsRunning());
			}
		} catch (IOException exc) {
			LOGGER.log(Level.SEVERE, "Error while adding " + libraries + " library into project");
			LOGGER.log(Level.SEVERE, exc.getMessage());
		}	
		libraryHelper.addLibrariesToProjectsClassPath(projectName, libraries);
		LOGGER.info("Library: \"" + libraries + "\" on class path of project\"" + projectName + "\"");
	}
	
	protected static void disableSourceLookup(){
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		SourceLookupPreferencePage sp = new SourceLookupPreferencePage();
		preferenceDialog.select(sp);
		sp.setSourceAttachment(SourceAttachmentEnum.NEVER);
		preferenceDialog.ok();
	}
	
}
