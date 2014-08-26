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

import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Logger;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.common.reddeer.label.IDELabel;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.helper.ImportHelper;
import org.jboss.tools.ws.reddeer.ui.wizards.wst.WebServiceWizardPageBase.SliderLevel;
import org.jboss.tools.ws.ui.bot.test.utils.DeploymentHelper;
import org.jboss.tools.ws.ui.bot.test.utils.ProjectHelper;
import org.jboss.tools.ws.ui.bot.test.utils.ResourceHelper;
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
		servers.removeAllProjectsFromServer();
	}

	@AfterClass
	public static void afterClass() {
		deleteAllProjects();
	}

	protected boolean projectExists(String name) {
		return new PackageExplorer().containsProject(name);
	}

	protected static void deleteAllProjects() {
		List<Project> projects = new ProjectExplorer().getProjects();
		for(int i=0;i<projects.size();i++) {
			Project project = projects.get(i);
			try {
				project.delete(true);
			} catch(SWTLayerException exception) {
				LOGGER.severe("Project was not deleted");
				try {
					LOGGER.severe("Project name: " + project.getName());
				} catch(Exception t) {
					LOGGER.severe("Can't get project name" + t.getMessage());
				}
			}
		}
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
				bot.viewByTitle(IDELabel.View.WEB_SERVICE_TESTER).isActive());
	}

	public static String getSoapRequest(String body) {
		return MessageFormat.format(SOAP_REQUEST_TEMPLATE, body);
	}

	protected static void importWSTestProject(String projectName) {
		String location = "/resources/projects/" + projectName;
		importWSTestProject(location, projectName);
		cleanAllProjects();
		AbstractWait.sleep(TimePeriod.getCustom(2));
	}

	protected static void importWSTestProject(String projectLocation, String dir) {
		ImportHelper.importProject(projectLocation, dir, Activator.PLUGIN_ID);

		eclipse.addConfiguredRuntimeIntoProject(dir, configuredState.getServer().name);
	}

	/**
	 * Cleans All Projects
	 */
	protected static void cleanAllProjects() {
		new ShellMenu("Project", "Clean...").select();
		new DefaultShell("Clean");
		new RadioButton("Clean all projects").click();
		new PushButton("OK").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG, false);
	}
}
