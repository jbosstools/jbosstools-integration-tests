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

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Logger;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.jboss.reddeer.eclipse.utils.DeleteUtils;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.condition.ViewWithToolTipIsActive;
import org.jboss.tools.common.reddeer.label.IDELabel;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ws.reddeer.ui.wizards.wst.WebServiceWizardPageBase.SliderLevel;
import org.jboss.tools.ws.ui.bot.test.utils.DeploymentHelper;
import org.jboss.tools.ws.ui.bot.test.utils.ProjectHelper;
import org.jboss.tools.ws.ui.bot.test.utils.ResourceHelper;
import org.jboss.tools.ws.ui.bot.test.utils.ServersViewHelper;
import org.jboss.tools.ws.ui.bot.test.utils.WebServiceClientHelper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;

/**
 * Basic test base for all web service bot tests
 * 
 * @author jjankovi
 * 
 */
@Require(perspective = "Java EE", 
		 server = @Server)
@OpenPerspective(JavaEEPerspective.class)
@JBossServer()
public class WSTestBase extends SWTTestExt {

	private SliderLevel level;
	private String wsProjectName = null;

	private static final String SOAP_REQUEST_TEMPLATE = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>"
			+ "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\""
			+ " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
			+ " xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">"
			+ "<soap:Body>{0}</soap:Body>" + "</soap:Envelope>";

	protected static final Logger LOGGER = Logger.getLogger(WSAllBotTests.class
			.getName());

	protected final String LINE_SEPARATOR = System
			.getProperty("line.separator");

	protected static ServersViewHelper serversViewHelper = new ServersViewHelper();
	protected static ResourceHelper resourceHelper = new ResourceHelper();
	protected static ProjectHelper projectHelper = new ProjectHelper();
	protected static DeploymentHelper deploymentHelper = new DeploymentHelper();
	protected static WebServiceClientHelper clientHelper = new WebServiceClientHelper();

	@Before
	public void setup() {
		if (getEarProjectName() != null && !projectExists(getEarProjectName())) {
			projectHelper.createEARProject(getEarProjectName());
			if (!projectExists(getWsProjectName())) {
				projectHelper.createProjectForEAR(getWsProjectName(),
						getEarProjectName());
			}
		}
		if (!projectExists(getWsProjectName())) {
			projectHelper.createProject(getWsProjectName());
		}
	}

	@After
	public void cleanup() {
		deleteAllProjectsFromServer();
	}

	@AfterClass
	public static void deleteAll() {
		deleteAllProjects();
	}

	protected boolean projectExists(String name) {
		PackageExplorer packageExplorer = new PackageExplorer();
		packageExplorer.open();
		return packageExplorer.containsProject(name);
	}

	protected static void deleteAllProjects() {
		PackageExplorer packageExplorer = new PackageExplorer();
		packageExplorer.open();
		List<Project> projects = packageExplorer.getProjects();
		for(int i=0;i<projects.size();i++) {
			Project project = projects.get(i);
			try {
				DeleteUtils.forceProjectDeletion(project, true);
			} catch(RuntimeException exception) {
				LOGGER.severe("Project was not deleted");
				try {
					LOGGER.severe("Project name: " + project.getName());
				} catch(Exception t) {
					LOGGER.severe("Can't get project name" + t.getMessage());
				}
			}
		}
	}

	protected static void deleteAllProjectsFromServer() {
		serversViewHelper.removeAllProjectsFromServer(configuredState.getServer().name);
	}

	protected void openJavaFile(String projectName, String pkgName, String javaFileName) {
		new PackageExplorer().getProject(projectName)
			.getProjectItem("src", pkgName, javaFileName).open();
	}

	protected SliderLevel getLevel() {
		return level;
	}

	protected void setLevel(SliderLevel level) {
		this.level = level;
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

	protected void assertWebServiceTesterIsActive() {
		assertTrue("Web Service Tester view should be active", 
				new ViewWithToolTipIsActive(IDELabel.View.WEB_SERVICE_TESTER).test());
	}

	public static String getSoapRequest(String body) {
		return MessageFormat.format(SOAP_REQUEST_TEMPLATE, body);
	}

	protected static void importWSTestProject(String projectName) {
		try {
			importProject(new File("resources/projects/" + projectName).getCanonicalPath());
		} catch(IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		projectHelper.addConfiguredRuntimeIntoProject(projectName, configuredState.getServer().name);
		cleanAllProjects();
		AbstractWait.sleep(TimePeriod.getCustom(2));
	}

	private static void importProject(String projectLocation) {
		ExternalProjectImportWizardDialog importDialog = new ExternalProjectImportWizardDialog();
		importDialog.open();
		WizardProjectsImportPage importPage = importDialog.getFirstPage();
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
		new WaitWhile(new JobIsRunning());
		new ShellMenu(IDELabel.Menu.PROJECT, "Clean...").select();
		new DefaultShell("Clean");
		new RadioButton("Clean all projects").click();
		new PushButton(IDELabel.Button.OK).click();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG, false);
	}
}
