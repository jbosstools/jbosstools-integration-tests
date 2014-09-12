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

import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.tools.ws.reddeer.jaxrs.core.RestFullAnnotations;
import org.jboss.tools.ws.reddeer.jaxrs.core.RestService;
import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.junit.Test;

/**
 * Test operates on exploring RESTFul services in RESTful explorer
 * 
 * @author jjankovi
 */
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
		importRestWSProject(projectName);

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RestService> restServices = restfulServicesForProject(projectName);

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 4);
		assertAllRESTServicesInExplorer(restServices);
		assertPathOfAllRESTWebServices(restServices, "/rest");
	}

	@Test
	public void testAddingAdvancedRESTMethods() {
		final String projectName = REST_ADVANCED_PROJECET_NAME;

		/* import project */
		importRestWSProject(projectName);

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RestService> restServices = restfulServicesForProject(projectName);

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 4); 
		assertAllRESTServicesInExplorer(restServices);
		testAdvancedRESTServices(restServices);
	}

	@Test
	public void testEditingSimpleRESTMethods() {
		final String projectName = REST_BASIC_PROJECT_NAME;

		/* import project */
		importRestWSProject(projectName);

		/* replace @DELETE annotation to @GET annotation */
		replaceInRestService(projectName, "@DELETE", "@GET");

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RestService> restServices = restfulServicesForProject(projectName);

		/* test JAX-RS REST explorer */
		assertNotAllRESTServicesInExplorer(restServices);
		assertAbsenceOfRESTWebService(restServices,
				RestFullAnnotations.DELETE.getLabel());
	}

	/**
	 * Resolved - JBIDE-16163
	 * (Missing update in JAX-RS explorer when facing to quick change in code)
	 * 
	 * @see https://issues.jboss.org/browse/JBIDE-16163
	 */
	@Test
	public void testEditingAdvancedRESTMethods() {
		final String projectName = REST_ADVANCED_PROJECET_NAME;

		/* import project */
		importRestWSProject(projectName);

		/* edit @DELETE annotation */
		replaceInRestService(projectName, "/delete/{id}", "delete/edited//{id}");
		replaceInRestService(projectName, "@DELETE", "@DELETE" + LINE_SEPARATOR
				+ "@Produces(\"text/plain\")");
		AbstractWait.sleep(TimePeriod.getCustom(2));

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RestService> restServices = restfulServicesForProject(projectName);

		/* test JAX-RS REST explorer */
		testEditedDeleteRestWebResource(restServices);
	}

	@Test
	public void testDeletingRESTMethods() {
		final String projectName = REST_BASIC_PROJECT_NAME;

		/* prepare project*/
		importRestWSProject(projectName);
		prepareRestService(projectName, "EmptyRestfulWS.java.ws");
		AbstractWait.sleep(TimePeriod.getCustom(2));

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RestService> restServices = restfulServicesForProject(projectName);

		/* none of REST web services found */
		assertCountOfRESTServices(restServices, 0);
	}

	private void testEditedDeleteRestWebResource(List<RestService> restServices) {
		for (RestService restService : restServices) {
			if (restService.getName().equals(RestFullAnnotations.DELETE.getLabel())) {
				assertEquals("Path of DELETE operation ", "/rest/delete/edited/{id:int}", restService.getPath());
				assertEquals("Produces info of DELETE operation ", "text/plain", restService.getProducesInfo());
			}
		}
	}

	private void testAdvancedRESTServices(List<RestService> restServices) {
		for (RestService restService : restServices) {
			if (restService.getName().equals(RestFullAnnotations.GET.getLabel())) {
				assertTrue("Path of GET operation ", restService.getPath().equals("/rest/{id:int}"));
				assertTrue("Consumes info of GET operation ", restService.getConsumesInfo().equals("*/*"));
				assertTrue("Produces info of GET operation ", restService.getProducesInfo().equals("text/plain"));
			}
			if (restService.getName().equals(RestFullAnnotations.PUT.getLabel())) {
				assertTrue("Path of PUT operation ", restService.getPath().equals("/rest/put/{id:int}"));
				assertTrue("Consumes info of PUT operation ", restService.getConsumesInfo().equals("text/plain"));
				assertTrue("Produces info of PUT operation ", restService.getProducesInfo().equals("*/*"));
			}
			if (restService.getName().equals(RestFullAnnotations.POST.getLabel())) {
				assertTrue("Path of POST operation ", restService.getPath().equals("/rest/post/{id:int}"));
				assertTrue("Consumes info of POST operation ", restService.getConsumesInfo().equals("text/plain"));
				assertTrue("Produces info of POST operation ", restService.getProducesInfo().equals("text/plain"));
			}
			if (restService.getName().equals(RestFullAnnotations.DELETE.getLabel())) {
				assertEquals("Path of DELETE operation ", restService.getPath(), "/rest/delete/{id:int}");
				assertEquals("Consumes info of DELETE operation ", restService.getConsumesInfo(), "*/*");
				assertEquals("Produces info of DELETE operation ", restService.getProducesInfo(), "*/*");
			}
		}
	}

	protected void assertPathOfAllRESTWebServices(List<RestService> restServices,
			String path) {
		for (RestService restService : restServices) {
			assertTrue("RESTful Web Service \"" + restService.getName()
					+ "\" has been set wrong path",
						restService.getPath().equals(path));
		}
	}

	protected void assertAbsenceOfRESTWebService(List<RestService> restServices,
			String restWebService) {
		for (RestService restService : restServices) {
			if (restService.getName().equals(restWebService)) {
				fail("There should not be " + restWebService + "RESTful services");
			}
		}
	}
}
