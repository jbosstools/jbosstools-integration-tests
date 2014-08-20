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

import java.util.List;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.jboss.tools.ui.bot.ext.Timing;
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

	@Override
	public void setup() {

	}

	@Test
	public void testAddingSimpleRESTMethods() {
		/* import project */
		importRestWSProject("restBasic");

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RestService> restServices = restfulServicesForProject("restBasic");

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 4);
		assertAllRESTServicesInExplorer(restServices);
		assertPathOfAllRESTWebServices(restServices, "/rest");
	}

	@Test
	public void testAddingAdvancedRESTMethods() {
		/* import project */
		importRestWSProject("restAdvanced");

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RestService> restServices = restfulServicesForProject("restAdvanced");

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 4);
		assertAllRESTServicesInExplorer(restServices);
		testAdvancedRESTServices(restServices);
	}
	
	@Test
	public void testEditingSimpleRESTMethods() {
		/* import project */
		importRestWSProject("restBasic");

		/* replace @DELETE annotation to @GET annotation */
		resourceHelper.replaceInEditor(editorForClass("restBasic", "src",
				"org.rest.test", "RestService.java").toTextEditor(), "@DELETE", "@GET", true);

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RestService> restServices = restfulServicesForProject("restBasic");

		/* test JAX-RS REST explorer */
		assertNotAllRESTServicesInExplorer(restServices);
		assertAbsenceOfRESTWebService(restServices,
				RestFullAnnotations.DELETE.getLabel());
	}
	
	/**
	 * Fails due to JBIDE-16163.
	 * Workaround is possible: delay between typing
	 *  - add bot.sleep(Timing.time2S()) after replacing the text in editor
	 * 
	 * @see https://issues.jboss.org/browse/JBIDE-16163
	 */
	@Test
	public void testEditingAdvancedRESTMethods() {
		/* import project */
		importRestWSProject("restAdvanced");

		/* edit @DELETE annotation */
		SWTBotEclipseEditor editor = editorForClass("restAdvanced", "src",
				"org.rest.test", "RestService.java").toTextEditor();
		resourceHelper.replaceInEditor(editor, "/delete/{id}",
				"delete/edited/{id}", true);
		resourceHelper.replaceInEditor(editor, "@DELETE", "@DELETE"
				+ LINE_SEPARATOR + "@Produces(\"text/plain\")", true);
		//bot.sleep(Timing.time2S());

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RestService> restServices = restfulServicesForProject("restAdvanced");

		/* test JAX-RS REST explorer */
		testEditedDeleteRestWebResource(restServices);
	}
	
	@Test
	public void testDeletingRESTMethods() {
		/* prepare project*/
		importRestWSProject("restBasic");
		prepareRestfulResource(editorForClass("restBasic", "src",
				"org.rest.test", "RestService.java"), "EmptyRestfulWS.java.ws",
				"org.rest.test", "RestService");
		bot.sleep(Timing.time2S());

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RestService> restServices = restfulServicesForProject("restBasic");

		/* none of REST web services found */
		assertCountOfRESTServices(restServices, 0);
	}

	private void testEditedDeleteRestWebResource(List<RestService> restServices) {
		for (RestService restService : restServices) {
			if (restService.getName().equals(RestFullAnnotations.DELETE.getLabel())) {
				assertEquals("Path of DELETE operation ", restService.getPath(), "/rest/delete/edited/{id:int}");
				assertEquals("Produces info of DELETE operation ", restService.getProducesInfo(), "text/plain");
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
