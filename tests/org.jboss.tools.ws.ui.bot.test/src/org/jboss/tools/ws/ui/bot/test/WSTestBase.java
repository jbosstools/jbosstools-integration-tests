/*******************************************************************************
 * Copyright (c) 2010-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.ws.ui.bot.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import org.jboss.tools.common.reddeer.label.IDELabel;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.matcher.WithTextMatcher;
import org.eclipse.reddeer.eclipse.ui.console.ConsoleView;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.eclipse.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.eclipse.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.button.RadioButton;
import org.eclipse.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.eclipse.reddeer.swt.impl.menu.ShellMenu;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.ws.ui.bot.test.utils.ProjectHelper;
import org.jboss.tools.ws.ui.bot.test.utils.ServersViewHelper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.runner.RunWith;

/**
 * Basic test base for all web service bot tests
 * 
 * @author jjankovi
 * 
 */
@RunWith(RedDeerSuite.class)
@OpenPerspective(JavaEEPerspective.class)
@JBossServer()
public class WSTestBase {

	@InjectRequirement
    private static ServerRequirement serverReq;

	private String wsProjectName = null;

	protected static final Logger LOGGER = Logger.getLogger(WSTestBase.class
			.getName());

	protected final String LINE_SEPARATOR = System
			.getProperty("line.separator");
	
	@Before
	public void setup() {
		if (getEarProjectName() != null && !ProjectHelper.projectExists(getEarProjectName())) {
			ProjectHelper.createEARProject(getEarProjectName());
			if (!ProjectHelper.projectExists(getWsProjectName())) {
				ProjectHelper.createProjectForEAR(getWsProjectName(),
						getEarProjectName());
			}
		}
		if (!ProjectHelper.projectExists(getWsProjectName())) {
			ProjectHelper.createProject(getWsProjectName());
		}
	}

	@After
	public void cleanup() {
		ConsoleView consoleView = new ConsoleView();
		if (consoleView.isOpen()) {
			consoleView.clearConsole();
		}
	}

	@AfterClass
	public static void deleteAll() {
		deleteAllProjectsFromServer();
		deleteAllProjects();
	}

	protected static String getConfiguredRuntimeName() {
		return serverReq.getRuntimeNameLabelText();
	}

	protected static String getConfiguredServerName() {
		return serverReq.getServerNameLabelText();
	}

	protected static String getConfiguredServerType() {
		return serverReq.getConfiguration().getFamily().getLabel();
	}

	protected static String getConfiguredServerVersion() {
		return serverReq.getConfiguration().getVersion();
	}

	protected boolean projectExists(String name) {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		return projectExplorer.containsProject(name);
	}
	
	protected static void deleteAllProjects() {
		ProjectHelper.deleteAllProjects();
	}

	protected static void deleteAllProjectsFromServer() {
		ServersViewHelper.removeAllProjectsFromServer(getConfiguredServerName());
	}

	protected void openJavaFile(String projectName, String pkgName, String javaFileName) {
		new ProjectExplorer().getProject(projectName)
			.getProjectItem("Java Resources", "src", pkgName, javaFileName).open();
	}

	protected String getWsProjectName() {
		return wsProjectName;
	}

	protected void setWsProjectName(String wsProjectName) {
		this.wsProjectName = wsProjectName;
	}

	protected String getEarProjectName() {
		return null;
	}

	protected String getWsPackage() {
		return null;
	}

	protected String getWsName() {
		return null;
	}

//	protected void assertWebServiceTesterIsActive() {
//		assertTrue("Web Service Tester view should be active", 
//				new DefaultCTabItem(new WorkbenchShell(),
//						new WithTextMatcher(IDELabel.View.WEB_SERVICE_TESTER)).isEnabled());
//	}

	protected static void importWSTestProject(String projectName) {
		try {
			importProject(new File("resources/projects/" + projectName).getCanonicalPath());
		} catch(IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		ProjectHelper.addConfiguredRuntimeIntoProject(projectName, getConfiguredRuntimeName());
		ProjectHelper.setProjectJRE(projectName);
		cleanAllProjects();
		AbstractWait.sleep(TimePeriod.getCustom(2));
	}

	private static void importProject(String projectLocation) {
		ExternalProjectImportWizardDialog importDialog = new ExternalProjectImportWizardDialog();
		importDialog.open();
		WizardProjectsImportPage importPage = new WizardProjectsImportPage(importDialog);
		importPage.setRootDirectory(projectLocation);
		assertFalse("There is no project to import", importPage.getProjects().isEmpty());
		importPage.selectAllProjects();
		importPage.copyProjectsIntoWorkspace(true);
		importDialog.finish();
	}

	/**
	 * Cleans All Projects
	 */
	protected static void cleanAllProjects() {
		ProjectHelper.cleanAllProjects();
	}
}
