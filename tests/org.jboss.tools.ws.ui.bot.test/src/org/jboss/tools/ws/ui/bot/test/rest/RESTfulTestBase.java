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

import org.hamcrest.core.Is;
import org.hamcrest.core.StringContains;
import org.hamcrest.core.StringStartsWith;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.matcher.WithTextMatchers;
import org.jboss.reddeer.eclipse.condition.ExactNumberOfProblemsExists;
import org.jboss.reddeer.eclipse.condition.ProblemExists;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.eclipse.ui.problems.matcher.AbstractProblemMatcher;
import org.jboss.reddeer.eclipse.ui.problems.matcher.ProblemsDescriptionMatcher;
import org.jboss.reddeer.eclipse.ui.problems.matcher.ProblemsPathMatcher;
import org.jboss.reddeer.eclipse.ui.problems.matcher.ProblemsTypeMatcher;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.api.Menu;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.ws.reddeer.editor.ExtendedTextEditor;
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
@JBossServer(state=ServerReqState.STOPPED, cleanup=false)
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
	
	private AbstractProblemMatcher[] getProblemMatchers(String description, String project, String type) {
		List<AbstractProblemMatcher> matcherList = new ArrayList<AbstractProblemMatcher>();
		if (description != null) {
			matcherList.add(new ProblemsDescriptionMatcher(StringContains.containsString(description)));
		}
		if (project != null) {
			matcherList. add(new ProblemsPathMatcher(StringStartsWith.startsWith("/" + project)));
		}	
		if (type != null) {
			matcherList.add(new ProblemsTypeMatcher(Is.is(type)));
		}
		AbstractProblemMatcher[] matchers = new AbstractProblemMatcher[matcherList.size()];
		return matchers = matcherList.toArray(matchers);
	}
	
	public void assertCountOfProblemsExists(ProblemType problemType, String projectName, 
			String description, String type, int count) {
		AbstractProblemMatcher[] matchers = getProblemMatchers(description, projectName, type);
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
		AbstractProblemMatcher[] matchers = getProblemMatchers(description, projectName, type);
		
		if(count == 0 && !new ProblemExists(ProblemType.ANY).test()) {//prevent from false positive result when we do not expect errors and there is no error
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

		Menu menu = new ContextMenu(
				new WithTextMatchers(new RegexMatcher(".*Run.*"),
						new RegexMatcher(".*Run on Server.*")).getMatchers());
		menu.select();

		RunOnServerDialog dialog = new RunOnServerDialog();
		String dialogTitle = dialog.getText();
		dialog.chooseExistingServer();
		dialog.selectServer(serverName);
		dialog.finish();
		
		new WaitWhile(new ShellWithTextIsAvailable(dialogTitle), TimePeriod.getCustom(20));
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
	
	protected void refreshRestServices(String projectName) {
		Project project = new ProjectExplorer().getProject(projectName);
		project.getChild("JAX-RS Web Services").refresh();
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
