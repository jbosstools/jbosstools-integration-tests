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

package org.jboss.tools.ws.ui.bot.test.rest;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ws.ui.bot.test.WSTestBase;
import org.jboss.tools.ws.ui.bot.test.ti.wizard.RESTFullExplorerWizard;

/**
 * Test base for bot tests using RESTFul support
 * 
 * @author jjankovi
 * 
 */
@Require(server = @Server(state = ServerState.NotRunning), perspective = "Java EE")
public class RESTfulTestBase extends WSTestBase {

	protected final RESTfulHelper restfulHelper = new RESTfulHelper();

	protected RESTFullExplorerWizard restfulWizard = null;

	protected final String SIMPLE_REST_WS_RESOURCE = "SimpleRestWS.java.ws";

	protected String getWsPackage() {
		return "org.rest.test";
	}

	protected String getWsName() {
		return "RestService";
	}

	@Override
	public void setup() {

	}

	@Override
	public void cleanup() {

	}

	protected void importRestWSProject(String projectName) {

		// importing project without targeted runtime set
		importWSTestProject(projectName);

		projectExplorer.selectProject(projectName);
		eclipse.cleanAllProjects();
		bot.sleep(Timing.time3S());

		// workaround for EAP 5.1
		if (configuredState.getServer().type.equals("EAP")
				&& configuredState.getServer().version.equals("5.1")) {
			restfulHelper.addRestEasyLibs(projectName);
		}
	}

	protected void assertRestFullSupport(String projectName) {
		assertTrue("JAX-RS REST Web Services explorer is missing in "
				+ "project \"" + projectName + "\"",
				restfulHelper.isRestSupportEnabled(getWsProjectName()));
	}

	protected void assertCountOfRESTServices(SWTBotTreeItem[] restServices,
			int expectedCount) {
		assertTrue(restServices.length + " RESTful services"
				+ " was found instead of " + expectedCount,
				restServices.length == expectedCount);
	}

	protected void assertAllRESTServicesInExplorer(SWTBotTreeItem[] restServices) {
		assertTrue("All RESTful services (GET, DELETE, POST, PUT) "
				+ "should be present but they are not",
				allRestServicesArePresent(restServices));
	}

	protected void assertNotAllRESTServicesInExplorer(
			SWTBotTreeItem[] restServices) {
		assertFalse("All RESTful services (GET, DELETE, POST, PUT) "
				+ "shouldnt be present but they are",
				allRestServicesArePresent(restServices));
	}

	protected void assertNoRESTServicesInExplorerFound(
			SWTBotTreeItem[] restServices) {
		assertTrue(restServices.length
				+ " RESTful services was found instead of 0.",
				restServices.length == 0);
	}

	protected void assertPathOfAllRESTWebServices(
			SWTBotTreeItem[] restServices, String path) {
		for (SWTBotTreeItem restService : restServices) {
			assertTrue(
					"RESTful Web Service \""
							+ restfulWizard.getRestServiceName(restService)
							+ "\" has been set wrong path", restfulWizard
							.getPathForRestFulService(restService).equals(path));
		}
	}

	protected void assertAbsenceOfRESTWebService(SWTBotTreeItem[] restServices,
			String restWebService) {
		for (SWTBotTreeItem restService : restServices) {
			if (restfulWizard.getRestServiceName(restService).equals(
					restWebService)) {
				fail("There should not be " + restWebService
						+ "RESTful services");
			}
		}
	}

	protected void assertPresenceOfRESTWebService(
			SWTBotTreeItem[] restServices, String restWebService) {
		boolean found = false;
		for (SWTBotTreeItem restService : restServices) {
			if (restfulWizard.getRestServiceName(restService).equals(
					restWebService)) {
				found = true;
				break;
			}
		}
		assertTrue("There should be " + restWebService + "RESTful services",
				found);
	}

	protected void assertExpectedPathOfService(SWTBotTreeItem service,
			String expectedPath) {
		String path = restfulWizard.getPathForRestFulService(service);
		assertEquals("Failure when comparing paths = ", expectedPath, path);
	}

	protected void assertCoundOfValidationErrors(String projectName,
			int expectedCount) {
		int foundErrors = restfulHelper.getRESTValidationErrors(projectName).length;
		assertTrue("There should be " + expectedCount
				+ " validation error. Found: " + foundErrors,
				foundErrors == expectedCount);
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

	private boolean allRestServicesArePresent(SWTBotTreeItem[] restServices) {

		String[] restMethods = { RESTFulAnnotations.GET.getLabel(),
				RESTFulAnnotations.POST.getLabel(),
				RESTFulAnnotations.POST.getLabel(),
				RESTFulAnnotations.DELETE.getLabel() };
		for (String restMethod : restMethods) {
			boolean serviceFound = false;
			for (SWTBotTreeItem restService : restServices) {
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
