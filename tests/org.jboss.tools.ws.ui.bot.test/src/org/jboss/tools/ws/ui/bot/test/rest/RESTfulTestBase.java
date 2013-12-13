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

package org.jboss.tools.ws.ui.bot.test.rest;

import java.util.List;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.ProjectItem;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.condition.NonSystemJobRunsCondition;
import org.jboss.tools.ui.bot.ext.condition.ViewIsActive;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ws.ui.bot.test.WSTestBase;
import org.jboss.tools.ws.ui.bot.test.uiutils.RESTFullExplorer;
import org.jboss.tools.ws.ui.bot.test.uiutils.RunOnServerDialog;
import org.jboss.tools.ws.ui.bot.test.widgets.WsTesterView;
import org.jboss.tools.ws.ui.bot.test.widgets.WsTesterView.Request_Type;

/**
 * Test base for bot tests using RESTFul support
 * 
 * @author jjankovi
 * 
 */
@Require(server = @Server(state = ServerState.NotRunning), perspective = "Java EE")
public class RESTfulTestBase extends WSTestBase {

	protected final static RESTfulHelper restfulHelper = new RESTfulHelper();

	protected RESTFullExplorer restfulWizard = null;

	protected final String SIMPLE_REST_WS_RESOURCE = "SimpleRestWS.java.ws";

	protected String getWsPackage() {
		return "org.rest.test";
	}

	protected String getWsName() {
		return "RestService";
	}

	@Override
	public void setup() {
		if (!projectExists(getWsProjectName())) {
			importRestWSProject(getWsProjectName());
		}
	}
	
	@Override
	public void cleanup() {		
		 projectExplorer.deleteAllProjects();
	}

	protected static void importRestWSProject(String projectName) {

		// importing project without targeted runtime set
		importWSTestProject(projectName);

		// workaround for EAP 5.1
		if (configuredState.getServer().type.equals("EAP")
				&& configuredState.getServer().version.equals("5.1")) {
			restfulHelper.addRestEasyLibs(projectName);
		}
	}

	protected void assertRestFullSupport(String projectName) {
		RESTFullExplorer explorer = null;
		try {
			explorer = new RESTFullExplorer(projectName);
		} catch (WidgetNotFoundException wnfe) {
			
		}
		assertNotNull("JAX-RS REST Web Services explorer is missing in "
				+ "project \"" + projectName + "\"", explorer);
	}

	protected void assertCountOfRESTServices(List<ProjectItem> restServices,
			int expectedCount) {
		assertTrue(restServices.size() + " RESTful services"
				+ " was found instead of " + expectedCount,
				restServices.size() == expectedCount);
	}

	protected void assertAllRESTServicesInExplorer(List<ProjectItem> restServices) {
		assertTrue("All RESTful services (GET, DELETE, POST, PUT) "
				+ "should be present but they are not",
				allRestServicesArePresent(restServices));
	}

	protected void assertNotAllRESTServicesInExplorer(List<ProjectItem> restServices) {
		assertFalse("All RESTful services (GET, DELETE, POST, PUT) "
				+ "shouldnt be present but they are",
				allRestServicesArePresent(restServices));
	}

	protected void assertPathOfAllRESTWebServices(List<ProjectItem> restServices,
			String path) {
		for (ProjectItem restService : restServices) {
			assertTrue(
					"RESTful Web Service \""
							+ restfulWizard.getRestServiceName(restService)
							+ "\" has been set wrong path", restfulWizard
							.getPathForRestFulService(restService).equals(path));
		}
	}

	protected void assertAbsenceOfRESTWebService(List<ProjectItem> restServices,
			String restWebService) {
		for (ProjectItem restService : restServices) {
			if (restfulWizard.getRestServiceName(restService).equals(
					restWebService)) {
				fail("There should not be " + restWebService
						+ "RESTful services");
			}
		}
	}

	protected void assertPresenceOfRESTWebService(List<ProjectItem> restServices,
			String restWebService) {
		boolean found = false;
		for (ProjectItem restService : restServices) {
			if (restfulWizard.getRestServiceName(restService).equals(
					restWebService)) {
				found = true;
				break;
			}
		}
		assertTrue("There should be " + restWebService + "RESTful services",
				found);
	}

	protected void assertExpectedPathOfService(ProjectItem service,
			String expectedPath) {
		String path = restfulWizard.getPathForRestFulService(service);
		assertEquals("Failure when comparing paths = ", expectedPath, path);
	}

	protected void assertCountOfPathAnnotationValidationErrors(String projectName,
			int expectedCount) {
		int foundErrors = restfulHelper.getPathAnnotationValidationErrors(projectName).length;
		assertCountOfValidationError(expectedCount, foundErrors);
	}
	
	protected void assertCountOfApplicationAnnotationValidationWarnings(String projectName,
			int expectedCount) {
		int foundProblems = restfulHelper.getRESTValidationWarnings(projectName, null).length;
		assertCountOfValidationError(expectedCount, foundProblems);
	}
	
	protected void assertCountOfApplicationAnnotationValidationErrors(String projectName,
			int expectedCount) {
		int foundProblems = restfulHelper.getRESTValidationErrors(projectName, null).length;
		assertCountOfValidationError(expectedCount, foundProblems);
	}
	
	private void assertCountOfValidationError(int expectedCount, int foundCount) {
		assertTrue("Expected count of validation errors: " + expectedCount
				+ ". Count of found validation errors: " + foundCount,
				foundCount == expectedCount);
	}
	
	protected void runRestServiceOnConfiguredServer(ProjectItem webService) {
		RunOnServerDialog dialog = restfulWizard.runOnServer(webService);
		dialog.chooseExistingServer().selectServer(configuredState.getServer().name).finish();
		bot.waitUntil(new ViewIsActive(IDELabel.View.WEB_SERVICE_TESTER), TIME_20S);
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_20S);
	}

	protected List<ProjectItem> restfulServicesForProject(String projectName) {
		restfulWizard = new RESTFullExplorer(projectName);
		return restfulWizard.getAllRestServices();
	}

	protected void invokeMethodInWSTester(WsTesterView wsTesterView, Request_Type type) {
		wsTesterView.setRequestType(type);
		wsTesterView.invoke();
	}

	protected SWTBotEditor editorForClass(String projectName, String... path) {
		return packageExplorer.openFile(projectName, path);
	}

	protected void prepareRestfulResource(SWTBotEditor editor,
			String resourceFile, Object... parameters) {
		String streamPath = "/resources/restful/" + resourceFile;
		resourceHelper.copyResourceToClassWithSave(editor,
				QueryAnnotationSupportTest.class
						.getResourceAsStream(streamPath), true, false,
				parameters);
		bot.sleep(Timing.time2S());
	}

	private boolean allRestServicesArePresent(List<ProjectItem> restServices) {
		String[] restMethods = { RESTFulAnnotations.GET.getLabel(),
				RESTFulAnnotations.POST.getLabel(),
				RESTFulAnnotations.POST.getLabel(),
				RESTFulAnnotations.DELETE.getLabel() };
		for (String restMethod : restMethods) {
			boolean serviceFound = false;
			for (ProjectItem restService : restServices) {
				if (restfulWizard.getRestServiceName(restService).equals(
						restMethod)) {
					serviceFound = true;
					break;
				}
			}
			if (!serviceFound)
				return false;
		}
		return true;
	}
}
