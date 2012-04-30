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

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ws.ui.bot.test.ti.wizard.RESTFullExplorerWizard;
import org.junit.Test;

/**
 * Test operates on exploring RESTFul services in RESTful explorer 
 * 
 * @author jjankovi
 *
 */
public class PathAnnotationSupportTest extends RESTfulTestBase {

	private RESTFullExplorerWizard restfulWizard = null;

	private String restEmptyProjectName = "restEmpty";
	
	@Override
	protected String getWsProjectName() {
		return restEmptyProjectName;
	}
	
	@Override
	public void cleanup() {		
		 bot.activeEditor().toTextEditor().save();
	}
	
	@Test
	public void testAddingSimpleRESTMethods() {
		
		packageExplorer.openFile(getWsProjectName(), "src", 
				getWsPackage(), getWsName() + ".java").toTextEditor();
		resourceHelper.copyResourceToClass(bot.editorByTitle(getWsName() + ".java"),
				PathAnnotationSupportTest.class.getResourceAsStream(BASIC_WS_RESOURCE), 
				false, false, getWsPackage(), getWsName());
		bot.sleep(Timing.time2S());
		
		restfulWizard = new RESTFullExplorerWizard(getWsProjectName());
		SWTBotTreeItem[] restServices = restfulWizard.getAllRestServices();
		
		assertTrue(restServices.length + " RESTful services was found instead of 4.", 
				   restServices.length == 4);		
		assertTrue("All RESTful services (GET, DELETE, POST, PUT) should be present but they are not", 
				   allRestServicesArePresent(restServices));
		
		for (SWTBotTreeItem restService : restServices) {
			assertTrue(restfulWizard.getPathForRestFulService(restService).equals("/rest"));
			assertTrue(restfulWizard.getConsumesInfo(restService).equals("*/*"));
			assertTrue(restfulWizard.getProducesInfo(restService).equals("*/*"));
		}
	}
	
	@Test
	public void testAddingAdvancedRESTMethods() {
		
		packageExplorer.openFile(getWsProjectName(), "src", 
				getWsPackage(), getWsName() + ".java").toTextEditor();
		resourceHelper.copyResourceToClass(bot.editorByTitle(getWsName() + ".java"),
				PathAnnotationSupportTest.class.getResourceAsStream(ADVANCED_WS_RESOURCE), 
				false, false, getWsPackage(), getWsName());
		bot.sleep(Timing.time2S());
		
		restfulWizard = new RESTFullExplorerWizard(getWsProjectName());
		SWTBotTreeItem[] restServices = restfulWizard.getAllRestServices(); 
		
		assertTrue(restServices.length + " RESTful services was found instead of 4.",
				   restServices.length == 4);
		assertTrue("All RESTful services (GET, DELETE, POST, PUT) should be present but they are not", 
					allRestServicesArePresent(restServices));
		
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
		
		packageExplorer.openFile(getWsProjectName(), "src", 
				getWsPackage(), getWsName() + ".java").toTextEditor();
		resourceHelper.copyResourceToClass(bot.editorByTitle(getWsName() + ".java"),
				PathAnnotationSupportTest.class.getResourceAsStream(BASIC_WS_RESOURCE), 
				false, false, getWsPackage(), getWsName());
		
		restfulWizard = new RESTFullExplorerWizard(getWsProjectName());
		SWTBotTreeItem[] restServices = restfulWizard.getAllRestServices();
		
		assertTrue("All RESTful services (GET, DELETE, POST, PUT) should be present but they are not", 
					allRestServicesArePresent(restServices));
		
		packageExplorer.openFile(getWsProjectName(), "src", 
				getWsPackage(), getWsName() + ".java").toTextEditor();
		resourceHelper.replaceInEditor(bot.activeEditor().toTextEditor(), "@DELETE", "@GET", false);
		
		bot.sleep(Timing.time2S());
		restfulWizard = new RESTFullExplorerWizard(getWsProjectName());
		restServices = restfulWizard.getAllRestServices();
		
		assertFalse("All RESTful services (GET, DELETE, POST, PUT) shouldnt be present but they are", 
					 allRestServicesArePresent(restServices));
		
		for (SWTBotTreeItem restService : restServices) {
			if (restfulWizard.getRestServiceName(restService).equals(RESTFulAnnotations.DELETE.getLabel())) {
				fail("There should not be DELETE RESTful services");
			}
		}
	}
	
	@Test
	public void testEditingAdvancedRESTMethods() {
		
		packageExplorer.openFile(getWsProjectName(), "src", 
				getWsPackage(), getWsName() + ".java").toTextEditor();
		resourceHelper.copyResourceToClass(bot.editorByTitle(getWsName() + ".java"),
				PathAnnotationSupportTest.class.getResourceAsStream(ADVANCED_WS_RESOURCE), 
				false, false, getWsPackage(), getWsName());
		
		restfulWizard = new RESTFullExplorerWizard(getWsProjectName());
		SWTBotTreeItem[] restServices = restfulWizard.getAllRestServices();
		
		assertTrue(restServices.length > 0);
		
		for (SWTBotTreeItem restService : restServices) {
			if (restfulWizard.getRestServiceName(restService).equals(RESTFulAnnotations.DELETE.getLabel())) {
				assertTrue(restfulWizard.getPathForRestFulService(restService).equals("/rest/delete/{id}"));
				assertTrue(restfulWizard.getProducesInfo(restService).equals("*/*"));
			}
		}
		
		packageExplorer.openFile(getWsProjectName(), "src", 
				getWsPackage(), getWsName() + ".java").toTextEditor();
		resourceHelper.replaceInEditor(bot.activeEditor().toTextEditor(), 
				"/delete/{id}", "delete/edited/{id}", false);
		resourceHelper.replaceInEditor(bot.activeEditor().toTextEditor(), "@DELETE", 
									   "@DELETE" + LINE_SEPARATOR + "@Produces(\"text/plain\")", false);
		
		bot.sleep(Timing.time2S());
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
		
		packageExplorer.openFile(getWsProjectName(), "src", 
				getWsPackage(), getWsName() + ".java").toTextEditor();
		resourceHelper.copyResourceToClass(bot.editorByTitle(getWsName() + ".java"),
				PathAnnotationSupportTest.class.getResourceAsStream(BASIC_WS_RESOURCE), 
				false, false, getWsPackage(), getWsName());
		
		restfulWizard = new RESTFullExplorerWizard(getWsProjectName());
		SWTBotTreeItem[] restServices = restfulWizard.getAllRestServices();
		
		assertTrue(restServices.length + " RESTful services was found instead of 4.", 
				   restServices.length == 4);
		
		packageExplorer.openFile(getWsProjectName(), "src", 
				getWsPackage(), getWsName() + ".java").toTextEditor();
		resourceHelper.copyResourceToClass(bot.editorByTitle(getWsName() + ".java"), 
				   PathAnnotationSupportTest.class.
				   getResourceAsStream(EMPTY_WS_RESOURCE), 
				   false, false, getWsPackage(), getWsName());
		
		bot.sleep(Timing.time2S());
		restfulWizard = new RESTFullExplorerWizard(getWsProjectName());
		restServices = restfulWizard.getAllRestServices();
		
		assertTrue(restServices.length + " RESTful services was found instead of 0.",
				   restServices.length == 0);
		
	}
	
	private boolean allRestServicesArePresent(SWTBotTreeItem[] restServices) {
		
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
