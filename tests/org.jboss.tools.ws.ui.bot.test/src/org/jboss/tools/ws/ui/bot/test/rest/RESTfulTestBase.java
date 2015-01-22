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

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.core.StringContains;
import org.hamcrest.core.StringStartsWith;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.condition.ProblemsExists;
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
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.ws.reddeer.editor.ExtendedTextEditor;
import org.jboss.tools.ws.reddeer.jaxrs.core.RESTfulWebService;
import org.jboss.tools.ws.reddeer.jaxrs.core.RESTfulWebServicesNode;
import org.jboss.tools.ws.reddeer.swt.condition.ProblemsCount;
import org.jboss.tools.ws.reddeer.ui.tester.views.WsTesterView;
import org.jboss.tools.ws.ui.bot.test.WSTestBase;
import org.jboss.tools.ws.ui.bot.test.uiutils.RunOnServerDialog;
import org.junit.Assert;

/**
 * Test base for bot tests using RESTFul support
 * 
 * @author jjankovi
 * 
 */
@JBossServer(state=ServerReqState.STOPPED)
@OpenPerspective(JavaEEPerspective.class)
public class RESTfulTestBase extends WSTestBase {

	protected final static RESTfulHelper restfulHelper = new RESTfulHelper();

	protected RESTfulWebServicesNode restWebServicesNode = null;

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
		

		if (getConfiguredServerType().equals("JBoss Enterprise Application Platform")
				&& getConfiguredServerVersion().equals("5.x")) {
			throw new UnsupportedOperationException("When EAP 5.1 is used,"
					+ " then jaxrs-api.jar must be added to build path of the imported project");
		}
	}

	protected void importAndCheckErrors(String projectName) {
		importRestWSProject(projectName);

		assertCountOfValidationErrors(projectName, 0);
		assertCountOfErrors(0);
	}

	protected void assertCountOfRESTServices(List<RESTfulWebService> restServices,
			int expectedCount) {
		assertTrue(restServices.size() + " RESTful services"
				+ " was found instead of " + expectedCount,
				restServices.size() == expectedCount);
	}

	protected void assertAllRESTServicesInExplorer(List<RESTfulWebService> restServices) {
		assertTrue("All RESTful services (GET, DELETE, POST, PUT) "
				+ "should be present but they are not\nThere are "
				+ Arrays.toString(restServices.toArray()),
				allRestServicesArePresent(restServices));
	}

	protected void assertNotAllRESTServicesInExplorer(List<RESTfulWebService> restServices) {
		assertFalse("All RESTful services (GET, DELETE, POST, PUT) "
				+ "shouldnt be present but they are"
				+ Arrays.toString(restServices.toArray()),
				allRestServicesArePresent(restServices));
	}

	protected void assertExpectedPathOfService(String message,
			RESTfulWebService service, String expectedPath) {
		assertEquals(message + "Failure when comparing paths\n", expectedPath,
				service.getPath());
	}

	protected void assertExpectedPathOfService(RESTfulWebService service,
			String expectedPath) {
		assertExpectedPathOfService("", service, expectedPath);
	}

	/*
	 * Assert errors (project, validation, validation of path annotation)
	 */
	private void waitForErrors(int expectedCount) {
		if(expectedCount == 0) {//prevent from false-positive
			new WaitWhile(new ProblemsExists(ProblemsExists.ProblemType.ERROR), TimePeriod.getCustom(2), false);
		} else {
			new WaitUntil(new ProblemsCount(ProblemsCount.ProblemType.ERROR, expectedCount), TimePeriod.getCustom(2), false);
		}
	}

	private void waitForErrors(int expectedCount, Matcher<String> path) {
		if(expectedCount == 0) {//prevent from false-positive
			new WaitWhile(new ProblemsExists(ProblemsExists.ProblemType.ERROR), TimePeriod.getCustom(2), false);
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
			new WaitWhile(new ProblemsExists(ProblemsExists.ProblemType.WARNING), TimePeriod.getCustom(2), false);
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

	protected List<RESTfulWebService> restfulServicesForProject(String projectName) {
		restWebServicesNode = new RESTfulWebServicesNode(projectName);
		return restWebServicesNode.getWebServices();
	}

	protected void invokeMethodInWSTester(WsTesterView wsTesterView, WsTesterView.RequestType type) {
		wsTesterView.setRequestType(type);
		wsTesterView.invoke();
	}

	protected void copyRestfulResource(String resourceFile, Object... param) {
		String streamPath = "/resources/restful/" + resourceFile;
		InputStream stream = this.getClass().getResourceAsStream(streamPath);
		resourceHelper.copyResourceToClassWithSave(
				stream, true, param);
		AbstractWait.sleep(TimePeriod.getCustom(2));
	}

	private boolean allRestServicesArePresent(List<RESTfulWebService> restServices) {
		String[] allRestMethods = {"GET", "POST", "POST", "DELETE"};
		for (String restMethod : allRestMethods) {
			if(!restServiceIsPresent(restServices, restMethod)) {
				return false;
			}
		}
		return true;
	}
	
	private boolean restServiceIsPresent(List<RESTfulWebService> restServices, String restMethod) {
		for (RESTfulWebService restService : restServices) {
			if (restService.getMethod().equals(restMethod)) {
				return true;
			}
		}
		return false;
	}

	protected void runRestServiceOnServer(String restWebServiceMethod) {
		runRestServiceOnServer(restWebServicesNode.getWebServiceByMethod(restWebServiceMethod).get(0));
	}

	protected void runRestServiceOnServer(RESTfulWebService restWebService) {
		runRestServiceOnServer(restWebService, getConfiguredServerName());
	}

	protected void runRestServiceOnServer(RESTfulWebService restWebService, String serverName) {
		restWebService.select();

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

	protected void prepareSimpleRestService(String pathArgument, String pathParamArgument) {
		prepareRestService(getWsProjectName(), SIMPLE_REST_WS_RESOURCE,
				pathArgument, pathParamArgument);
	}

	protected void prepareRestService(String projectName, String resourceFileName,
			Object... param) {
		openJavaFile(projectName, "org.rest.test", "RestService.java");
		Object[] newParam = new Object[2 + (param != null ? param.length : 0)];
		newParam[0] = "org.rest.test";
		newParam[1] = "RestService";
		for(int i=2;i<newParam.length;i++) {
			newParam[i] = param[i-2];
		}
		copyRestfulResource(resourceFileName, newParam);
	}

	protected void replaceInRestService(String regex, String replacement) {
		replaceInRestService(getWsProjectName(), regex, replacement);
	}

	protected void replaceInRestService(String projectName,
			String regex, String replacement) {
		openJavaFile(projectName, "org.rest.test", "RestService.java");
		ExtendedTextEditor editor = new ExtendedTextEditor();
		editor.replace(regex, replacement);
	}

	protected TextEditor setCursorPositionToLineInTextEditor(String text) {
		ExtendedTextEditor editor = new ExtendedTextEditor();
		int line = editor.getLineNum(StringContains.containsString(text));
		editor.setCursorPosition(line, 0);
		return editor;
	}

	protected TextEditor setCursorPositionToTextInTextEditor(String text) {
		ExtendedTextEditor editor = new ExtendedTextEditor();
		int line = editor.getLineNum(StringContains.containsString(text));
		editor.setCursorPosition(line, editor.getTextAtLine(line).indexOf(text)
				+ text.length());
		return editor;
	}
}
