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

import java.util.Arrays;
import java.util.List;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.hamcrest.Matcher;
import org.hamcrest.core.StringStartsWith;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.condition.ProblemsExists;
import org.jboss.reddeer.eclipse.condition.ProblemsExists.ProblemType;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.api.Menu;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.matcher.RegexMatcher;
import org.jboss.reddeer.swt.matcher.WithTextMatchers;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ws.reddeer.jaxrs.core.RestFullAnnotations;
import org.jboss.tools.ws.reddeer.jaxrs.core.RestFullExplorer;
import org.jboss.tools.ws.reddeer.jaxrs.core.RestService;
import org.jboss.tools.ws.reddeer.swt.condition.ProblemsCount;
import org.jboss.tools.ws.reddeer.ui.tester.views.WsTesterView;
import org.jboss.tools.ws.reddeer.ui.tester.views.WsTesterView.RequestType;
import org.jboss.tools.ws.ui.bot.test.WSTestBase;
import org.jboss.tools.ws.ui.bot.test.uiutils.RunOnServerDialog;
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

	protected RestFullExplorer restfulWizard = null;

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
		deleteAllProjects();
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

		assertCountOfValidationErrors(projectName, 0);
		assertCountOfErrors(0);
	}

	protected void assertCountOfRESTServices(List<RestService> restServices,
			int expectedCount) {
		assertTrue(restServices.size() + " RESTful services"
				+ " was found instead of " + expectedCount,
				restServices.size() == expectedCount);
	}

	protected void assertAllRESTServicesInExplorer(List<RestService> restServices) {
		assertTrue("All RESTful services (GET, DELETE, POST, PUT) "
				+ "should be present but they are not\nThere are "
				+ Arrays.toString(restServices.toArray()),
				allRestServicesArePresent(restServices));
	}

	protected void assertNotAllRESTServicesInExplorer(List<RestService> restServices) {
		assertFalse("All RESTful services (GET, DELETE, POST, PUT) "
				+ "shouldnt be present but they are"
				+ Arrays.toString(restServices.toArray()),
				allRestServicesArePresent(restServices));
	}

	protected void assertExpectedPathOfService(String message,
			RestService service, String expectedPath) {//TODO: is it neccesary?
		assertEquals(message + "Failure when comparing paths\n", expectedPath,
				service.getPath());
	}
	
	protected void assertExpectedPathOfService(RestService service,
			String expectedPath) {
		assertExpectedPathOfService("", service, expectedPath);
	}

	/*
	 * Assert errors (project, validation, validation of path annotation)
	 */
	private void waitForErrors(int expectedCount) {
		if(expectedCount == 0) {//prevent from false-positive
			new WaitWhile(new ProblemsExists(ProblemType.ERROR), TimePeriod.getCustom(2), false);
		} else {
			new WaitUntil(new ProblemsCount(ProblemsCount.ProblemType.ERROR, expectedCount), TimePeriod.getCustom(2), false);
		}
	}

	private void waitForErrors(int expectedCount, Matcher<String> path) {
		if(expectedCount == 0) {//prevent from false-positive
			new WaitWhile(new ProblemsExists(ProblemType.ERROR), TimePeriod.getCustom(2), false);
		} else {//prevent from false-negative
			new WaitUntil(new ProblemsCount(ProblemsCount.ProblemType.ERROR, expectedCount, null, null,
					path, null, null), TimePeriod.getCustom(2), false);
		}
	}

	protected void assertCountOfErrors(int expectedCount) {
		waitForErrors(expectedCount);
		assertCountOfErrors(new ProblemsView().getAllErrors(), expectedCount, null);
	}

	protected void assertCountOfErrors(String projectName, int expectedCount) {
		assertCountOfErrors(projectName, expectedCount, null);
	}

	protected void assertCountOfErrors(String projectName, int expectedCount, String message) {
		Matcher<String> pathMatcher = StringStartsWith.startsWith("/" + projectName);
		waitForErrors(expectedCount, pathMatcher);
		assertCountOfErrors(new ProblemsView().getErrors(null, null,
				pathMatcher, null, null), expectedCount, message);
	}

	protected void assertCountOfValidationErrors(String projectName,
			int expectedCount) {
		assertCountOfValidationErrors(projectName, expectedCount, null);
	}

	protected void assertCountOfValidationErrors(String projectName,
			int expectedCount, String message) {
		assertCountOfValidationErrors(projectName, null, expectedCount, message);
	}

	protected void assertCountOfValidationErrors(String projectName,
			String description, int expectedCount) {
		assertCountOfValidationErrors(projectName, description, expectedCount, null);
	}

	protected void assertCountOfValidationErrors(String projectName,
			String description, int expectedCount, String message) {
		assertCountOfErrors(
				restfulHelper.getRESTValidationErrors(projectName, description,
						expectedCount), expectedCount, message);
	}

	protected void assertCountOfPathAnnotationValidationErrors(String projectName, int expectedCount) {
		assertCountOfPathAnnotationValidationErrors(projectName, expectedCount, null);
	}

	protected void assertCountOfPathAnnotationValidationErrors(String projectName, int expectedCount, String message) {
		assertCountOfErrors(
				restfulHelper.getPathAnnotationValidationErrors(projectName, expectedCount),
				expectedCount, message);
	}

	/*
	 * Assert warnings (project, validation)
	 */
	private void waitForWarnings(int expectedCount) {
		if(expectedCount == 0) {//prevent from false-positive
			new WaitWhile(new ProblemsExists(ProblemType.WARNING), TimePeriod.getCustom(2), false);
		} else {//prevent from false-negative
			new WaitUntil(new ProblemsCount(ProblemsCount.ProblemType.WARNING, expectedCount), TimePeriod.getCustom(2), false);
		}
	}

	protected void assertCountOfWarnings(String projectName, int expectedCount) {
		assertCountOfWarnings(projectName, expectedCount, null);
	}

	protected void assertCountOfWarnings(String projectName, int expectedCount, String message) {
		waitForWarnings(expectedCount);
		assertCountOfErrors(new ProblemsView().getAllWarnings(), expectedCount, message);
	}

	protected void assertCountOfValidationWarnings(String projectName,
			int expectedCount) {
		assertCountOfValidationWarnings(projectName, null, expectedCount);
	}

	protected void assertCountOfValidationWarnings(String projectName,
			int expectedCount, String message) {
		assertCountOfValidationWarnings(projectName, null, expectedCount, message);
	}

	protected void assertCountOfValidationWarnings(String projectName,
			String description, int expectedCount) {
		assertCountOfValidationWarnings(projectName, description, expectedCount, null);
	}

	protected void assertCountOfValidationWarnings(String projectName,
			String description, int expectedCount, String message) {
		assertCountOfWarning(
				restfulHelper.getRESTValidationWarnings(projectName, description, expectedCount),
				expectedCount, message);
	}

	private void assertCountOfErrors(List<TreeItem> errors, int expectedCount, String message) {
		assertCountOfProblems("error", errors, expectedCount, message);
	}

	private void assertCountOfWarning(List<TreeItem> warnings, int expectedCount, String message) {
		assertCountOfProblems("warning", warnings, expectedCount, message);
	}

	private void assertCountOfProblems(String problemType, List<TreeItem> problems, int expectedCount, String message) {
		int foundCount = problems.size();
		if(foundCount != expectedCount) {
			StringBuilder problemsInfo = new StringBuilder();
			if(problems.size() > 0) {
				problemsInfo.append("\nFound problems:\n");
			}
			for(TreeItem item : problems) {
				for(int i=0;i<4;i++) {
					if(i>0) {
						problemsInfo.append("\t");
					}
					problemsInfo.append(item.getCell(i));
				}
				problemsInfo.append("\n");
			}
			Assert.fail((message != null ? message + " " : "")
					+ "Expected count of validation " + problemType + "s: "
					+ expectedCount + ".\nCount of found validation "
					+ problemType + "s: " + foundCount
					+ problemsInfo.toString());
		}
	}

	protected List<RestService> restfulServicesForProject(String projectName) {
		restfulWizard = new RestFullExplorer(projectName);
		return restfulWizard.getAllRestServices();
	}

	protected void invokeMethodInWSTester(WsTesterView wsTesterView, RequestType type) {
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

	private boolean allRestServicesArePresent(List<RestService> restServices) {
		String[] allRestMethods = { RestFullAnnotations.GET.getLabel(),
				RestFullAnnotations.POST.getLabel(),
				RestFullAnnotations.POST.getLabel(),
				RestFullAnnotations.DELETE.getLabel() };
		for (String restMethod : allRestMethods) {
			if(!restServiceIsPresent(restServices, restMethod)) {
				return false;
			}
		}
		return true;
	}
	
	private boolean restServiceIsPresent(List<RestService> restServices, String restMethod) {
		for (RestService restService : restServices) {
			if (restService.getName().equals(restMethod)) {
				return true;
			}
		}
		return false;
	}

	protected void runRestServiceOnServer(String restServiceName) {
		runRestServiceOnServer(restfulWizard.getRestService(restServiceName));
	}

	protected void runRestServiceOnServer(RestService restService) {
		runRestServiceOnServer(restService, configuredState.getServer().name);
	}

	protected void runRestServiceOnServer(RestService restService, String serverName) {
		restService.getProjectItem().select();

		Menu menu = new ContextMenu(
				new WithTextMatchers(new RegexMatcher(".*Run.*"),
						new RegexMatcher(".*Run on Server.*")).getMatchers());
		menu.select();

		RunOnServerDialog dialog = new RunOnServerDialog();
		dialog.chooseExistingServer();
		dialog.selectServer(serverName);
		dialog.finish();
		new WsTesterView();
		new WaitWhile(new JobIsRunning(), TimePeriod.getCustom(20));
	}
}
