/*******************************************************************************
 * Copyright (c) 2007-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.ws.ui.bot.test.rest.explorer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.ws.reddeer.jaxrs.core.RESTfulWebService;
import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test operates on exploring RESTFul services in RESTful explorer
 * 
 * @author jjankovi
 */
@RunWith(RedDeerSuite.class)
public class RESTfulExplorerTest extends RESTfulTestBase {

	private static final String REST_BASIC_PROJECT_NAME = "restBasic";
	private static final String REST_ADVANCED_PROJECET_NAME = "restAdvanced";

	@Override
	public void setup() {

	}

	@Test
	public void testAddingSimpleRESTMethods() {
		final String projectName = REST_BASIC_PROJECT_NAME;

		/* import project */
		importWSTestProject(projectName);

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RESTfulWebService> restServices = restfulServicesForProject(projectName);

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 4);
		assertAllRESTServicesInExplorer(restServices);
		assertPathOfAllRESTWebServices(restServices, "/rest");
	}

	@Test
	public void testAddingAdvancedRESTMethods() {
		final String projectName = REST_ADVANCED_PROJECET_NAME;

		/* import project */
		importWSTestProject(projectName);

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RESTfulWebService> restServices = restfulServicesForProject(projectName);

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 4); 
		assertAllRESTServicesInExplorer(restServices);
		testAdvancedRESTServices(restServices);
	}

	@Test
	public void testEditingSimpleRESTMethods() {
		final String projectName = REST_BASIC_PROJECT_NAME;

		/* import project */
		importWSTestProject(projectName);

		/* replace @DELETE annotation to @GET annotation */
		replaceInRestService(projectName, "@DELETE", "@GET");

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RESTfulWebService> restServices = restfulServicesForProject(projectName);

		/* test JAX-RS REST explorer */
		assertNotAllRESTServicesInExplorer(restServices);
		assertAbsenceOfRESTWebService(restServices, "DELETE");
	}

	@Test
	public void testEditingAdvancedRESTMethods() {
		final String projectName = REST_ADVANCED_PROJECET_NAME;

		/* import project */
		importWSTestProject(projectName);

		/* edit @DELETE annotation */
		replaceInRestService(projectName, "/delete/{id}", "delete/edited//{id}");
		replaceInRestService(projectName, "@DELETE", "@DELETE" + LINE_SEPARATOR
				+ "@Produces(\"text/plain\")");

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RESTfulWebService> restServices = restfulServicesForProject(projectName);

		/* test JAX-RS REST explorer */
		testEditedDeleteRestWebResource(restServices);
	}

	@Test
	public void testDeletingRESTMethods() {
		final String projectName = REST_BASIC_PROJECT_NAME;

		/* prepare project*/
		importWSTestProject(projectName);
		prepareRestService(projectName, "EmptyRestfulWS.java.ws");

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RESTfulWebService> restServices = restfulServicesForProject(projectName);

		/* none of REST web services found */
		assertCountOfRESTServices(restServices, 0);
	}

	private void testEditedDeleteRestWebResource(List<RESTfulWebService> restServices) {
		for (RESTfulWebService restService : restServices) {
			if (restService.getMethod().equals("DELETE")) {
				assertEquals("Path of DELETE operation ", "/rest/delete/edited/{id:int}", restService.getPath());
				assertEquals("Produces info of DELETE operation ", "text/plain", restService.getProducingContentType());
			}
		}
	}

	private void testAdvancedRESTServices(List<RESTfulWebService> restServices) {
		for (RESTfulWebService restService : restServices) {
			if (restService.getMethod().equals("GET")) {
				assertTrue("Path of GET operation ", restService.getPath().equals("/rest/{id:int}"));
				assertTrue("Consumes info of GET operation ", restService.getConsumingContentType().equals("*/*"));
				assertTrue("Produces info of GET operation ", restService.getProducingContentType().equals("text/plain"));
			}
			if (restService.getMethod().equals("PUT")) {
				assertTrue("Path of PUT operation ", restService.getPath().equals("/rest/put/{id:int}"));
				assertTrue("Consumes info of PUT operation ", restService.getConsumingContentType().equals("text/plain"));
				assertTrue("Produces info of PUT operation ", restService.getProducingContentType().equals("*/*"));
			}
			if (restService.getMethod().equals("POST")) {
				assertTrue("Path of POST operation ", restService.getPath().equals("/rest/post/{id:int}"));
				assertTrue("Consumes info of POST operation ", restService.getConsumingContentType().equals("text/plain"));
				assertTrue("Produces info of POST operation ", restService.getProducingContentType().equals("text/plain"));
			}
			if (restService.getMethod().equals("DELETE")) {
				assertEquals("Path of DELETE operation ", restService.getPath(), "/rest/delete/{id:int}");
				assertEquals("Consumes info of DELETE operation ", restService.getConsumingContentType(), "*/*");
				assertEquals("Produces info of DELETE operation ", restService.getProducingContentType(), "*/*");
			}
		}
	}

	protected void assertPathOfAllRESTWebServices(List<RESTfulWebService> restServices,
			String path) {
		for (RESTfulWebService restService : restServices) {
			assertTrue("RESTful Web Service \"" + restService.getMethod()
					+ "\" has been set wrong path",
						restService.getPath().equals(path));
		}
	}

	protected void assertAbsenceOfRESTWebService(List<RESTfulWebService> restServices,
			String restWebServiceMethod) {
		for (RESTfulWebService restService : restServices) {
			if (restService.getMethod().equals(restWebServiceMethod)) {
				fail("There should not be " + restWebServiceMethod + "RESTful services");
			}
		}
	}
}
