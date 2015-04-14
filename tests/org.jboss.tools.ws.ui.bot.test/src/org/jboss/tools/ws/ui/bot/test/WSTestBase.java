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
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Logger;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.jboss.reddeer.eclipse.utils.DeleteUtils;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ViewWithToolTipIsActive;
import org.jboss.tools.common.reddeer.label.IDELabel;
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
@OpenPerspective(JavaEEPerspective.class)
@JBossServer()
public class WSTestBase {

	@InjectRequirement
    private static ServerRequirement serverReq;

	private SliderLevel level;
	private String wsProjectName = null;

	private static final String SOAP_REQUEST_TEMPLATE = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>"
			+ "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\""
			+ " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
			+ " xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">"
			+ "<soap:Body>{0}</soap:Body>" + "</soap:Envelope>";

	protected static final Logger LOGGER = Logger.getLogger(WSTestBase.class
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
		new ConsoleView().clearConsole();
	}

	@AfterClass
	public static void deleteAll() {
		deleteAllProjects();
	}

	protected static String getConfiguredRuntimeName() {
		return serverReq.getRuntimeNameLabelText(serverReq.getConfig());
	}

	protected static String getConfiguredServerName() {
		return serverReq.getServerNameLabelText(serverReq.getConfig());
	}

	protected static String getConfiguredServerType() {
		return serverReq.getConfig().getServerFamily().getLabel();
	}

	protected static String getConfiguredServerVersion() {
		return serverReq.getConfig().getServerFamily().getVersion();
	}

	protected boolean projectExists(String name) {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		return projectExplorer.containsProject(name);
	}
	
	protected static void deleteAllProjects() {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		List<Project> projects = projectExplorer.getProjects();
		try {
			for(int i=0;i<projects.size();i++) {
				Project project = projects.get(i);
				project.delete(true);
			}
		} catch(SWTLayerException | EclipseLayerException e) {
			projectExplorer.close();
			projectExplorer.open();
			projects = projectExplorer.getProjects();
			for(int i=0;i<projects.size();i++) {
				Project project = projects.get(i);
				try {
					LOGGER.severe("Forcing removal of " + project);
					DeleteUtils.forceProjectDeletion(project, true);
				} catch(RuntimeException exception) {
					LOGGER.severe("Project was not deleted");
					try {
						LOGGER.severe("Project name: " + project.getName());
					} catch(Exception t) {
						LOGGER.severe("Can't get project name" + t.getMessage());
					}
					throw exception;
				}
			}
		}
	}

	protected static void deleteAllProjectsFromServer() {
		serversViewHelper.removeAllProjectsFromServer(getConfiguredServerName());
	}

	protected void openJavaFile(String projectName, String pkgName, String javaFileName) {
		new ProjectExplorer().getProject(projectName)
			.getProjectItem("Java Resources", "src", pkgName, javaFileName).open();
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
		projectHelper.addConfiguredRuntimeIntoProject(projectName, getConfiguredRuntimeName());
		projectHelper.setProjectJRE(projectName);
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
