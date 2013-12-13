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

package org.jboss.tools.ws.ui.bot.test.rest;

import java.util.List;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.ProjectItem;

import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.junit.Ignore;
import org.jboss.tools.ui.bot.ext.Timing;
import org.junit.Test;

/**
 * Test operates on exploring RESTFul services in RESTful explorer 
 * 
 * @author jjankovi
 *
 */
public class PathAnnotationSupportTest extends RESTfulTestBase {

	@Override
	public void setup() {
	
	}
	
	@Test
	public void testAddingSimpleRESTMethods() {
		
		/* import project */
		importRestWSProject("restBasic");
		
		/* get RESTful services from JAX-RS REST explorer for the project */
		List<ProjectItem> restServices = restfulServicesForProject("restBasic");

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
		List<ProjectItem> restServices = restfulServicesForProject("restAdvanced");
		
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
		List<ProjectItem> restServices = restfulServicesForProject("restBasic"); 
		
		/* test JAX-RS REST explorer */
		assertNotAllRESTServicesInExplorer(restServices);
		assertAbsenceOfRESTWebService(restServices,
				RESTFulAnnotations.DELETE.getLabel());
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
		List<ProjectItem> restServices = restfulServicesForProject("restAdvanced");

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
		List<ProjectItem> restServices = restfulServicesForProject("restBasic");
		
		/* none of REST web services found */
		assertCountOfRESTServices(restServices, 0);
	}

	private void testEditedDeleteRestWebResource(List<ProjectItem> restServices) {
		for (ProjectItem restService : restServices) {
			if (restfulWizard.getRestServiceName(restService).equals(RESTFulAnnotations.DELETE.getLabel())) {
				assertTrue(restfulWizard.getPathForRestFulService(restService).equals("/rest/delete/edited/{id}"));
				assertTrue(restfulWizard.getProducesInfo(restService).equals("text/plain"));
			}
		}
	}

	private void testAdvancedRESTServices(List<ProjectItem> restServices) {
		for (ProjectItem restService : restServices) {
			if (restfulWizard.getRestServiceName(restService).equals(RESTFulAnnotations.GET.getLabel())) {
				assertTrue(restfulWizard.getPathForRestFulService(restService).equals("/rest/{id}"));
				assertTrue(restfulWizard.getConsumesInfo(restService).equals("*/*"));
				assertTrue(restfulWizard.getProducesInfo(restService).equals("text/plain"));
			}
			if (restfulWizard.getRestServiceName(restService).equals(RESTFulAnnotations.PUT.getLabel())) {
				assertTrue(restfulWizard.getPathForRestFulService(restService).equals("/rest/put/{id}"));
				assertTrue(restfulWizard.getConsumesInfo(restService).equals("text/plain"));
				assertTrue(restfulWizard.getProducesInfo(restService).equals("*/*"));
			}
			if (restfulWizard.getRestServiceName(restService).equals(RESTFulAnnotations.POST.getLabel())) {
				assertTrue(restfulWizard.getPathForRestFulService(restService).equals("/rest/post/{id}"));
				assertTrue(restfulWizard.getConsumesInfo(restService).equals("text/plain"));
				assertTrue(restfulWizard.getProducesInfo(restService).equals("text/plain"));
			}
			if (restfulWizard.getRestServiceName(restService).equals(RESTFulAnnotations.DELETE.getLabel())) {				
				assertTrue(restfulWizard.getPathForRestFulService(restService).equals("/rest/delete/{id}"));
				assertTrue(restfulWizard.getConsumesInfo(restService).equals("*/*"));
				assertTrue(restfulWizard.getProducesInfo(restService).equals("*/*"));
			}			
		}
	}
}
