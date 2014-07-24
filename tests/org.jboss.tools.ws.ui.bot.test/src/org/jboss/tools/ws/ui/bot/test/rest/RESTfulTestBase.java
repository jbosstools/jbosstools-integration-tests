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

import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.hamcrest.core.Is;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.ProjectItem;
import org.jboss.reddeer.eclipse.jface.exception.JFaceLayerException;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.api.TreeItem;
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
import org.junit.Assert;

/**
 * Test base for bot tests using RESTFul support
 * 
 * @author jjankovi
 * 
 */
@Require(server = @Server(state = ServerState.NotRunning), perspective = "Java EE")
@JBossServer(state=ServerReqState.STOPPED)
@OpenPerspective(JavaEEPerspective.class)
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
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		for(Project p : pe.getProjects()) {
			p.delete(true);
		}
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

	protected void importAndCheckErrors(String projectName) {
		importRestWSProject(projectName);

		assertCountOfApplicationAnnotationValidationErrors(projectName, 0);
		assertCountOfErrors(0);
	}
	
	protected void assertRestFullSupport(String projectName) {
		RESTFullExplorer explorer = null;
		String missingRESTExplorerMessage = "JAX-RS REST Web Services explorer is missing in "
				+ "project \"" + projectName + "\"";
		try {
			explorer = new RESTFullExplorer(projectName);
		} catch (WidgetNotFoundException | JFaceLayerException e) {
			Assert.fail(missingRESTExplorerMessage + "\nThrown exception: " + e.getMessage());
		}
		assertNotNull(missingRESTExplorerMessage, explorer);
	}

	protected void assertCountOfRESTServices(List<ProjectItem> restServices,
			int expectedCount) {
		assertTrue(restServices.size() + " RESTful services"
				+ " was found instead of " + expectedCount,
				restServices.size() == expectedCount);
	}
	
	protected void assertAllRESTServicesInExplorer(List<ProjectItem> restServices) {
		assertTrue("All RESTful services (GET, DELETE, POST, PUT) "
				+ "should be present but they are not\nThere are "
				+ Arrays.toString(parseRestServices(restServices)),
				allRestServicesArePresent(restServices));
	}

	protected void assertNotAllRESTServicesInExplorer(List<ProjectItem> restServices) {
		assertFalse("All RESTful services (GET, DELETE, POST, PUT) "
				+ "shouldnt be present but they are"
				+ Arrays.toString(parseRestServices(restServices)),
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

	protected void assertExpectedPathOfService(String message,
			ProjectItem service, String expectedPath) {
		String path = restfulWizard.getPathForRestFulService(service);
		assertEquals(message + "Failure when comparing paths\n", expectedPath, path);
	}
	
	protected void assertExpectedPathOfService(ProjectItem service,
			String expectedPath) {
		assertExpectedPathOfService("", service, expectedPath);
	}

	protected void assertCountOfErrors(int expectedCount) {
		List<TreeItem> errors = new ProblemsView().getAllErrors();
		assertThat("Expected is " + expectedCount + " erros, but there are "
				+ errors.size() +" errors: " + Arrays.toString(errors.toArray()),
				errors.size(), Is.is(expectedCount));
	}

	protected void assertCountOfPathAnnotationValidationErrors(String projectName, int expectedCount) {
		assertCountOfValidationError(
				restfulHelper.getPathAnnotationValidationErrors(projectName),
				expectedCount);
	}
	
	protected void assertCountOfApplicationAnnotationValidationWarnings(String projectName,
			int expectedCount) {
		assertCountOfValidationWarning(
				restfulHelper.getRESTValidationWarnings(projectName),
				expectedCount);
	}
	
	protected void assertCountOfApplicationAnnotationValidationWarnings(String projectName,
			String description, int expectedCount) {
		assertCountOfValidationWarning(
				restfulHelper.getRESTValidationWarnings(projectName, description),
				expectedCount);
	}
	
	protected void assertCountOfApplicationAnnotationValidationErrors(String projectName,
			String description, int expectedCount) {
		assertCountOfValidationError(
				restfulHelper.getRESTValidationErrors(projectName, description),
				expectedCount);
	}
	
	protected void assertCountOfApplicationAnnotationValidationErrors(String projectName,
			int expectedCount) {
		assertCountOfValidationError(
				restfulHelper.getRESTValidationErrors(projectName),
				expectedCount);
	}
	
	private void assertCountOfValidationProblem(String problemType, SWTBotTreeItem[] problems, int expectedCount) {
		int foundCount = problems.length;
		assertTrue("Expected count of validation " + problemType + "s: " + expectedCount
				+ ".\nCount of found validation " + problemType + "s: " + foundCount
				+ (foundCount > 0 ? " - " + Arrays.toString(problems) : ""),
				foundCount == expectedCount);
	}
	
	private void assertCountOfValidationError(SWTBotTreeItem[] errors, int expectedCount) {
		assertCountOfValidationProblem("error", errors, expectedCount);
	}
	
	private void assertCountOfValidationWarning(SWTBotTreeItem[] warnings, int expectedCount) {
		assertCountOfValidationProblem("warning", warnings, expectedCount);
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
				this.getClass()
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
	
	private String[] parseRestServices(List<ProjectItem> restServices) {
		String[] services = new String[restServices.size()];
		for(int i=0;i<restServices.size();i++) {
			services[i] = restfulWizard.getRestServiceName(restServices.get(i));
		}
		return services;
	}
}
