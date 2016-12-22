/******************************************************************************* 
 * Copyright (c) 2016 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.portlet.bot.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jst.servlet.ui.WebProjectWizard;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.ui.views.log.LogMessage;
import org.jboss.reddeer.eclipse.ui.views.log.LogView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.ModifyModulesDialog;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.ModifyModulesPage;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.common.reddeer.utils.StackTraceUtils;
import org.jboss.tools.portlet.bot.test.wizard.NewPortletClassOptionsWizardPage;
import org.jboss.tools.portlet.bot.test.wizard.NewPortletClassOptionsWizardPage.Methods;
import org.jboss.tools.portlet.bot.test.wizard.NewPortletClassWizardPage;
import org.jboss.tools.portlet.bot.test.wizard.NewPortletWizard;
import org.jboss.tools.portlet.bot.test.wizard.PortletFacetInstallPage;
import org.jboss.tools.portlet.bot.test.wizard.WebProjectFirstPage2;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@JBossServer(type = ServerReqType.EAP6_1plus, state = ServerReqState.RUNNING)
public class CreateAndDeployPortletTest {

	private final String PROJECT_NAME = "JavaPortletProject";
	private final String PORTLET_CLASS_NAME = "PortletClass";

	private final static String JPP62_STARTED_TEXT = "JBoss Portal 6.2.0.GA (AS 7.5.0.Final-redhat-21) started";
	private final String PROJECT_SUCCESFULLY_DEPLOYED = "Deployed \"JavaPortletProject.war\"";

	@InjectRequirement
	ServerRequirement req;

	private static LogView logView;

	@BeforeClass
	public static void setupClass() {
		// JPP takes a bit longer to start
		waitForJPPToStart();

		clearLogView();
	}

	private static void waitForJPPToStart() {
		ConsoleView consoleView = new ConsoleView();
		consoleView.open();
		new WaitUntil(new ConsoleHasText(JPP62_STARTED_TEXT), TimePeriod.LONG);
	}

	private static void clearLogView() {
		logView = new LogView();
		logView.open();
		logView.clearLog();
	}

	@Test
	public void createAndDeployPortletProjectTest() {
		createDynamicWebProjectWithPortletFacet();

		deployPortletProject();

		// check logs
		assertErrorLogIsEmpty();

		cleanup();
	}

	@Test
	public void createAndDeployPortletTest() {
		createDynamicWebProjectWithPortletFacet();

		createPortletClass();

		assertErrorLogIsEmpty();

		assertPortletIsCreated();
		
		deployPortletProject();
		
		cleanup();

	}

	private void assertPortletIsCreated() {
		TextEditor editor = new TextEditor(PORTLET_CLASS_NAME + ".java");
		assertTrue(editor.isActive());
		String text = editor.getText();
		System.out.println(text);
		assertTrue(text.contains("protected void doView"));
		assertTrue(text.contains("protected void doEdit"));
		assertTrue(text.contains("public void processAction"));
	}

	private void createPortletClass() {
		NewPortletWizard portletWizard = new NewPortletWizard();
		portletWizard.open();

		new NewPortletClassWizardPage().setClassName(PORTLET_CLASS_NAME);
		assertTrue(portletWizard.isFinishEnabled());
		portletWizard.next();

		NewPortletClassOptionsWizardPage optionsPage = new NewPortletClassOptionsWizardPage();
		optionsPage.setMethods(Methods.DO_VIEW, Methods.DO_EDIT, Methods.PROCESS_ACTION);
		portletWizard.next();

		portletWizard.finish();

	}

	private void assertErrorLogIsEmpty() {
		logView.open();
		List<LogMessage> errorMessages = logView.getErrorMessages();
		assertTrue("There is something in error log:\n" + errorMessages.toString(), errorMessages.isEmpty());
	}

	private void cleanup() {
		// undeploy project
		handleAddModulesWizard(false);

		// delete project
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.getProject(PROJECT_NAME).delete(true);
	}

	private void deployPortletProject() {
		handleAddModulesWizard(true);
		assertProjectIsDeployedSuccesfully();
	}

	private void assertProjectIsDeployedSuccesfully() {
		try {
			new WaitUntil(new ConsoleHasText(PROJECT_SUCCESFULLY_DEPLOYED));
		} catch (WaitTimeoutExpiredException ex) {
			fail("Project was not succesfully deployed.\n" + StackTraceUtils.stackTraceToString(ex));
		}
	}

	/**
	 * 
	 * @param deploy
	 *            deploys if true. Otherwise undeploys
	 */
	private void handleAddModulesWizard(boolean deploy) {
		Server server = new ServersView().getServer(getServerName());
		ModifyModulesDialog mmDialog = server.addAndRemoveModules();
		ModifyModulesPage mmPage = new ModifyModulesPage();
		if (deploy) {
			mmPage.add(PROJECT_NAME);
		} else {
			mmPage.remove(PROJECT_NAME);
		}
		mmDialog.finish();
	}

	private String getServerName() {
		return req.getServerNameLabelText(req.getConfig());
	}

	protected void createDynamicWebProjectWithPortletFacet() {
		WebProjectWizard webProjectWizard = new WebProjectWizard();
		webProjectWizard.open();
		WebProjectFirstPage2 webProjectFirstPage = new WebProjectFirstPage2();
		webProjectFirstPage.setProjectName(PROJECT_NAME);
		webProjectFirstPage.setTargetRuntime(req.getRuntimeNameLabelText(req.getConfig()));
		webProjectFirstPage.activateFacet("2.0", "JBoss Portlets", "JBoss Core Portlet");

		webProjectWizard.next();
		webProjectWizard.next();
		webProjectWizard.next();
		new PortletFacetInstallPage().selectPortletImplementationLibrary("Portlet Target Runtime Provider");

		webProjectWizard.finish();
	}
}
