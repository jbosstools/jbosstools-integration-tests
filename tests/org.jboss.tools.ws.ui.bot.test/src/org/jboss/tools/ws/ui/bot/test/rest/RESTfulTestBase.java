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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.eclipse.reddeer.common.exception.WaitTimeoutExpiredException;
import org.eclipse.reddeer.common.matcher.RegexMatcher;
import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.matcher.WithTextMatchers;
import org.eclipse.reddeer.eclipse.condition.ExactNumberOfProblemsExists;
import org.eclipse.reddeer.eclipse.condition.ProblemExists;
import org.eclipse.reddeer.eclipse.core.resources.Project;
import org.eclipse.reddeer.eclipse.ui.markers.matcher.AbstractMarkerMatcher;
import org.eclipse.reddeer.eclipse.ui.markers.matcher.MarkerDescriptionMatcher;
import org.eclipse.reddeer.eclipse.ui.markers.matcher.MarkerPathMatcher;
import org.eclipse.reddeer.eclipse.ui.markers.matcher.MarkerTypeMatcher;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.swt.api.MenuItem;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.hamcrest.core.Is;
import org.hamcrest.core.StringContains;
import org.hamcrest.core.StringStartsWith;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.tools.ws.reddeer.jaxrs.core.RESTfulWebService;
import org.jboss.tools.ws.reddeer.jaxrs.core.RESTfulWebServicesNode;
import org.jboss.tools.ws.reddeer.ui.tester.views.WsTesterView;
import org.jboss.tools.ws.ui.bot.test.WSTestBase;
import org.jboss.tools.ws.ui.bot.test.uiutils.RunOnServerDialog;
import org.jboss.tools.ws.ui.bot.test.utils.ResourceHelper;
import org.junit.Assert;

/**
 * Test base for bot tests using RESTFul support
 * 
 * @author jjankovi
 * 
 */
@JBossServer(state=ServerRequirementState.STOPPED, cleanup=false)
@OpenPerspective(JavaEEPerspective.class)
public class RESTfulTestBase extends WSTestBase {

	public static final String PATH_PARAM_VALID_ERROR = "@PathParam value";
	public static final String JAX_RS_PROBLEM = "JAX-RS Problem";
	
	protected final static RESTfulHelper restfulHelper = new RESTfulHelper();

	protected RESTfulWebServicesNode restWebServicesNode = null;

	protected final String SIMPLE_REST_WS_RESOURCE = "SimpleRestWS.java.ws";

	private final TimePeriod WAIT_FOR_PROBLEMS_FALSE_POSItIVE_TIMEOUT = TimePeriod.getCustom(2);
	private final TimePeriod WAIT_FOR_PROBLEMS_FALSE_NEGATIVE_TIMEOUT = TimePeriod.getCustom(5);
	
	protected String getWsPackage() {
		return "org.rest.test";
	}

	protected String getWsName() {
		return "RestService";
	}

	@Override
	public void setup() {
		if (!projectExists(getWsProjectName())) {
			importWSTestProject(getWsProjectName());
		}
	}
	
	@Override
	public void cleanup() {
		deleteAllProjects();
	}

	protected void importAndCheckErrors(String projectName) {
		importWSTestProject(projectName);

		assertCountOfValidationProblemsExists(ProblemType.ERROR, projectName, null, null, 0);
		assertCountOfProblemsExists(ProblemType.ERROR, null, null, null, 0);
	}
	
	private AbstractMarkerMatcher[] getProblemMatchers(String description, String project, String type) {
		List<AbstractMarkerMatcher> matcherList = new ArrayList<AbstractMarkerMatcher>();
		if (description != null) {
			matcherList.add(new MarkerDescriptionMatcher(StringContains.containsString(description)));
		}
		if (project != null) {
			matcherList. add(new MarkerPathMatcher(StringStartsWith.startsWith("/" + project)));
		}	
		if (type != null) {
			matcherList.add(new MarkerTypeMatcher(Is.is(type)));
		}
		AbstractMarkerMatcher[] matchers = new AbstractMarkerMatcher[matcherList.size()];
		return matchers = matcherList.toArray(matchers);
	}
	
	public void assertCountOfProblemsExists(ProblemType problemType, String projectName, 
			String description, String type, int count) {
		AbstractMarkerMatcher[] matchers = getProblemMatchers(description, projectName, type);
		try {
			new WaitUntil(new ExactNumberOfProblemsExists(problemType, count, matchers));
		} catch (WaitTimeoutExpiredException ex) {
			Assert.fail("There is not " + count + " number of problems matching specified matchers existing.");
		}		
	}
	
	public void assertCountOfProblemsExists(ProblemType problemType, int count) {
		assertCountOfProblemsExists(problemType, null, null, null, count);
	}
	
	public void assertCountOfValidationProblemsExists(ProblemType problemType, String projectName, 
			String description, String type, int count) {
		AbstractMarkerMatcher[] matchers = getProblemMatchers(description, projectName, type);
		
		if(count == 0 && !new ProblemExists(ProblemType.ALL).test()) {//prevent from false positive result when we do not expect errors and there is no error
			new WaitWhile(new ExactNumberOfProblemsExists(ProblemType.ERROR, count, matchers), 
					WAIT_FOR_PROBLEMS_FALSE_POSItIVE_TIMEOUT, false);
		} else {//prevent from false negative result
			new WaitUntil(new ExactNumberOfProblemsExists(ProblemType.ERROR, count, matchers),
					WAIT_FOR_PROBLEMS_FALSE_NEGATIVE_TIMEOUT, false);
		}
		
		try {
			new WaitUntil(new ExactNumberOfProblemsExists(problemType, count, matchers));
		} catch (WaitTimeoutExpiredException ex) {
			Assert.fail("There is not " + count + " number of problems matching specified matchers existing.");
		}		
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
		ResourceHelper.copyResourceToClassWithSave(
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

		MenuItem menu = new ContextMenuItem(
				new WithTextMatchers(new RegexMatcher(".*Run.*"),
						new RegexMatcher(".*Run on Server.*")).getMatchers());
		menu.select();

		RunOnServerDialog dialog = new RunOnServerDialog();
		String dialogTitle = dialog.getText();
		dialog.chooseExistingServer();
		dialog.selectServer(serverName);
		dialog.finish();
		
		new WaitWhile(new ShellIsAvailable(dialogTitle), TimePeriod.getCustom(20));
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
		TextEditor editor = new TextEditor();
		editor.setText(editor.getText().replace(regex, replacement));
		editor.save();
	}

	protected TextEditor setCursorPositionToLineInTextEditor(String text) {
		TextEditor editor = new TextEditor();
		int line = editor.getLineOfText(text);
		editor.setCursorPosition(line, 0);
		return editor;
	}

	protected TextEditor setCursorPositionToTextInTextEditor(String text) {
		TextEditor editor = new TextEditor();
		int line = editor.getLineOfText(text);
		editor.setCursorPosition(line, editor.getTextAtLine(line).indexOf(text)
				+ text.length());
		return editor;
	}
	
	protected void refreshRestServices(String projectName) {
		Project project = new ProjectExplorer().getProject(projectName);
		project.getResource("JAX-RS Web Services").refresh();
	}
	
	protected class RestServicePathsHaveUpdated extends AbstractWaitCondition {
		
		private String projectName;
		private Set<String> previousState = new HashSet<>();
		
		public RestServicePathsHaveUpdated(String projectName) {
			this.projectName = projectName;
			for (RESTfulWebService service : restfulServicesForProject(projectName)) {
				previousState.add(service.getPath());
			}
		}
		
		@Override
		public boolean test() {
			Set<String> currentState = new HashSet<>();
			for (RESTfulWebService service : restfulServicesForProject(projectName)) {
				currentState.add(service.getPath());
			}
			
			return !currentState.equals(previousState);
		}
	}
}
