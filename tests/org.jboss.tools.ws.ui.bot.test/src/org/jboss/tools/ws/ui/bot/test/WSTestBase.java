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
import java.util.logging.Logger;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.helper.ImportHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.WsWizardBase.Slider_Level;
import org.jboss.tools.ws.ui.bot.test.utils.DeploymentHelper;
import org.jboss.tools.ws.ui.bot.test.utils.ProjectHelper;
import org.jboss.tools.ws.ui.bot.test.utils.ResourceHelper;
import org.jboss.tools.ws.ui.bot.test.utils.WebServiceClientHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Basic test base for all web service bot tests
 * 
 * @author jjankovi
 * 
 */
@Require(perspective = "Java EE", 
		 server = @Server) 
//		 server = @Server(type = ServerType.JbossAS, version = "7.1", operator = ">="))
// @Require(perspective="Java EE",
// server=@Server(type=ServerType.EAP,
// version = "5.1", operator = ">="))
@RunWith(RequirementAwareSuite.class)
@SuiteClasses({ WSAllBotTests.class })
public class WSTestBase extends SWTTestExt {

	private Slider_Level level;
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

	protected boolean projectExists(String name) {
		PackageExplorer packageExplorer = new PackageExplorer();
		packageExplorer.open();
		return packageExplorer.containsProject(name);
	}

	protected Slider_Level getLevel() {
		return level;
	}

	protected void setLevel(Slider_Level level) {
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
		eclipse.cleanAllProjects();
	}

	protected static void importWSTestProject(String projectLocation, String dir) {
		ImportHelper.importProject(projectLocation, dir, Activator.PLUGIN_ID);

		eclipse.addConfiguredRuntimeIntoProject(dir, configuredState.getServer().name);
	}

}
