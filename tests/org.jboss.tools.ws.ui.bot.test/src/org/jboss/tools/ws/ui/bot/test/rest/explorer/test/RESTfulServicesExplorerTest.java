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

package org.jboss.tools.ws.ui.bot.test.rest.explorer.test;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ws.ui.bot.test.rest.explorer.RESTFulAnnotations;
import org.jboss.tools.ws.ui.bot.test.rest.explorer.RESTfulTestBase;
import org.jboss.tools.ws.ui.bot.test.ti.wizard.RESTFullExplorerWizard;
import org.junit.Test;

/**
 * Test operates on exploring RESTFul services in RESTful explorer 
 * 
 * TO DO: I have to add rest libraries for EAP 5.1
 * @author jjankovi
 *
 */
public class RESTfulServicesExplorerTest extends RESTfulTestBase {

	private RESTFullExplorerWizard restfulWizard = null;
	
	protected String getWsProjectName() {
		return "RestServicesExplorer";
	}
	
	protected String getWsPackage() {
		return "org.rest.explorer.services.test";
	}

	protected String getWsName() {
		return "RestService";
	}
	
	@Override
	public void setup() {
		if (!projectExists(getWsProjectName())) {
			projectHelper.createProject(getWsProjectName());			
		}
		if (!isRestSupportEnabled(getWsProjectName())) {
			addRestSupport(getWsProjectName());
		}
	}
	
	@Override
	public void cleanup() {
		if (projectExists(getWsProjectName())) {			
			removeRestSupport(getWsProjectName());			
		}
	}
	
	@Test
	public void testAddingSimpleRESTMethods() {
		
		if (!projectExplorer.isFilePresent(getWsProjectName(), "Java Resources", "src", getWsPackage(), getWsName() + ".java")) {
			projectHelper.createClass(getWsProjectName(), getWsPackage(), getWsName());
		}
		
		resourceHelper.copyResourceToClass(bot.editorByTitle(getWsName() + ".java"), 
										   RESTfulServicesExplorerTest.class.
										   getResourceAsStream("/resources/restful/BasicRestfulWS.java.ws"), 
										   false, getWsPackage(), getWsName());
		/**
		 * workaround
		 */
		eclipse.cleanAllProjects();
		bot.sleep(Timing.time3S());

		restfulWizard = new RESTFullExplorerWizard(getWsProjectName());
		SWTBotTreeItem[] restServices = restfulWizard.getAllRestServices();
		
		assertTrue(restServices.length == 4);		
		assertTrue(areAllRestServicesPresent(restServices));
		
		for (SWTBotTreeItem restService : restServices) {
			assertTrue(restfulWizard.getPathForRestFulService(restService).equals("/rest"));
			assertTrue(restfulWizard.getConsumesInfo(restService).equals("*/*"));
			assertTrue(restfulWizard.getProducesInfo(restService).equals("*/*"));
		}
	}
	
	@Test
	public void testAddingAdvancedRESTMethods() {
		
		if (!projectExplorer.isFilePresent(getWsProjectName(), "Java Resources", "src", getWsPackage(), getWsName() + ".java")) {
			projectHelper.createClass(getWsProjectName(), getWsPackage(), getWsName());
		}
		
		resourceHelper.copyResourceToClass(bot.editorByTitle(getWsName() + ".java"), 
										   RESTfulServicesExplorerTest.class.
										   getResourceAsStream("/resources/restful/AdvancedRestfulWS.java.ws"), 
										   false, getWsPackage(), getWsName());
		/**
		 * workaround
		 */
		eclipse.cleanAllProjects();		
		bot.sleep(Timing.time3S());

		restfulWizard = new RESTFullExplorerWizard(getWsProjectName());
		SWTBotTreeItem[] restServices = restfulWizard.getAllRestServices();
		
		assertTrue(restServices.length == 4);
		assertTrue(areAllRestServicesPresent(restServices));
		
		for (SWTBotTreeItem restService : restServices) {
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
	
	@Test
	public void testEditingSimpleRESTMethods() {
		if (!projectExplorer.isFilePresent(getWsProjectName(), "Java Resources", "src", getWsPackage(), getWsName() + ".java")) {
			projectHelper.createClass(getWsProjectName(), getWsPackage(), getWsName());			
		}
		
		resourceHelper.copyResourceToClass(bot.editorByTitle(getWsName() + ".java"), 
				   RESTfulServicesExplorerTest.class.
				   getResourceAsStream("/resources/restful/BasicRestfulWS.java.ws"), 
				   false, getWsPackage(), getWsName());
		
		/**
		 * workaround
		 */
		eclipse.cleanAllProjects();		
		bot.sleep(Timing.time3S());

		restfulWizard = new RESTFullExplorerWizard(getWsProjectName());
		SWTBotTreeItem[] restServices = restfulWizard.getAllRestServices();
		
		assertTrue(areAllRestServicesPresent(restServices));
		
		resourceHelper.replaceInEditor(bot.activeEditor().toTextEditor(), "@DELETE", "@GET");
		
		/**
		 * workaround
		 */
		eclipse.cleanAllProjects();		
		bot.sleep(Timing.time3S());

		restfulWizard = new RESTFullExplorerWizard(getWsProjectName());
		restServices = restfulWizard.getAllRestServices();
		
		assertFalse(areAllRestServicesPresent(restServices));
		
		for (SWTBotTreeItem restService : restServices) {
			if (restfulWizard.getRestServiceName(restService).equals(RESTFulAnnotations.DELETE.getLabel())) {
				fail("There should not be DELETE RESTful services");
			}
		}
		
	}
	
	@Test
	public void testEditingAdvancedRESTMethods() {
		if (!projectExplorer.isFilePresent(getWsProjectName(), "Java Resources", "src", getWsPackage(), getWsName() + ".java")) {
			projectHelper.createClass(getWsProjectName(), getWsPackage(), getWsName());			
		}
		
		resourceHelper.copyResourceToClass(bot.editorByTitle(getWsName() + ".java"), 
				   RESTfulServicesExplorerTest.class.
				   getResourceAsStream("/resources/restful/AdvancedRestfulWS.java.ws"), 
				   false, getWsPackage(), getWsName());
		
		/**
		 * workaround
		 */
		eclipse.cleanAllProjects();
		bot.sleep(Timing.time3S());

		restfulWizard = new RESTFullExplorerWizard(getWsProjectName());
		SWTBotTreeItem[] restServices = restfulWizard.getAllRestServices();
		
		for (SWTBotTreeItem restService : restServices) {
			if (restfulWizard.getRestServiceName(restService).equals(RESTFulAnnotations.DELETE.getLabel())) {
				assertTrue(restfulWizard.getPathForRestFulService(restService).equals("/rest/delete/{id}"));
				assertTrue(restfulWizard.getProducesInfo(restService).equals("*/*"));
			}
		}
		
		resourceHelper.replaceInEditor(bot.activeEditor().toTextEditor(), "/delete/{id}", "delete/edited/{id}");
		resourceHelper.replaceInEditor(bot.activeEditor().toTextEditor(), "@DELETE", 
									   "@DELETE" + LINE_SEPARATOR + "@Produces(\"text/plain\")");
		
		/**
		 * workaround
		 */
		eclipse.cleanAllProjects();
		bot.sleep(Timing.time3S());

		restfulWizard = new RESTFullExplorerWizard(getWsProjectName());
		restServices = restfulWizard.getAllRestServices();
		
		for (SWTBotTreeItem restService : restServices) {
			if (restfulWizard.getRestServiceName(restService).equals(RESTFulAnnotations.DELETE.getLabel())) {
				assertTrue(restfulWizard.getPathForRestFulService(restService).equals("/rest/delete/edited/{id}"));
				assertTrue(restfulWizard.getProducesInfo(restService).equals("text/plain"));
			}
		}
	}
	
	@Test
	public void testDeletingRESTMethods() {
		if (!projectExplorer.isFilePresent(getWsProjectName(), "Java Resources", "src", getWsPackage(), getWsName() + ".java")) {
			projectHelper.createClass(getWsProjectName(), getWsPackage(), getWsName());			
		}
		
		resourceHelper.copyResourceToClass(bot.editorByTitle(getWsName() + ".java"), 
				   RESTfulServicesExplorerTest.class.
				   getResourceAsStream("/resources/restful/BasicRestfulWS.java.ws"), 
				   false, getWsPackage(), getWsName());
		
		/**
		 * workaround
		 */
		eclipse.cleanAllProjects();
		bot.sleep(Timing.time3S());

		restfulWizard = new RESTFullExplorerWizard(getWsProjectName());
		SWTBotTreeItem[] restServices = restfulWizard.getAllRestServices();
		
		assertTrue(restServices.length == 4);
		
		resourceHelper.copyResourceToClass(bot.editorByTitle(getWsName() + ".java"), 
				   RESTfulServicesExplorerTest.class.
				   getResourceAsStream("/resources/restful/EmptyRestfulWS.java.ws"), 
				   false, getWsPackage(), getWsName());
		
		/**
		 * workaround
		 */
		eclipse.cleanAllProjects();
		bot.sleep(Timing.time3S());

		restfulWizard = new RESTFullExplorerWizard(getWsProjectName());
		restServices = restfulWizard.getAllRestServices();
		
		assertTrue(restServices.length == 0);
		
	}
	
	private boolean areAllRestServicesPresent(SWTBotTreeItem[] restServices) {
		
		String[] restMethods = {RESTFulAnnotations.GET.getLabel(), RESTFulAnnotations.POST.getLabel(), 
								RESTFulAnnotations.POST.getLabel(), RESTFulAnnotations.DELETE.getLabel()};
		for (String restMethod : restMethods) {
			boolean serviceFound = false;
				for (SWTBotTreeItem restService : restServices) {
					if (restfulWizard.getRestServiceName(restService).equals(restMethod)) {
						serviceFound = true;
						break;
					}
				}
				if (!serviceFound) return false;
		}
		return true;
	}
	
}
