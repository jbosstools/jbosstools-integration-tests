/*******************************************************************************
 * Copyright (c) 2007-2012 Red Hat, Inc.
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

public class QueryParamSupportTest extends RESTfulTestBase {
	
	private RESTFullExplorerWizard restfulWizard = null;
	
	private String projectName = "restEmpty";
	
	private final String QUERY_ONE_PARAM_RESOURCE = "/resources/restful/QueryOneParam.java.ws";
	
	private final String QUERY_TWO_PARAM_RESOURCE = "/resources/restful/QueryTwoParam.java.ws";
	
	@Override
	protected String getWsProjectName() {
		return projectName;
	}
	
	@Override
	public void cleanup() {		
		 bot.activeEditor().toTextEditor().save();
	}
	
	@Test
	public void testSupportInExplorer() {
		
		String queryParam1 = "param1";
		String queryParam2 = "param2";
		String queryType = "java.lang.String";
		
		
		packageExplorer.openFile(getWsProjectName(), "src", 
				getWsPackage(), getWsName() + ".java").toTextEditor();
		resourceHelper.copyResourceToClass(bot.editorByTitle(getWsName() + ".java"),
				PathAnnotationSupportTest.class.getResourceAsStream(QUERY_ONE_PARAM_RESOURCE), 
				false, false, getWsPackage(), getWsName(), queryParam1, queryType);
		bot.sleep(Timing.time2S());
		
		restfulWizard = new RESTFullExplorerWizard(getWsProjectName());
		SWTBotTreeItem[] restServices = restfulWizard.getAllRestServices();
		
		assertTrue("There should be one RESTful web service instead of " + 
					restServices.length, restServices.length == 1);
		String path = restfulWizard.getPathForRestFulService(restServices[0]);
		assertEquals("/rest?" + queryParam1 + "={" + queryParam1 + ":" + queryType + "}", path);
		
		
		
		resourceHelper.copyResourceToClass(bot.editorByTitle(getWsName() + ".java"),
				PathAnnotationSupportTest.class.getResourceAsStream(QUERY_TWO_PARAM_RESOURCE), 
				false, false, getWsPackage(), getWsName(), 
				queryParam1, queryType, queryParam2, queryType);
		bot.sleep(Timing.time2S());
		
		restfulWizard = new RESTFullExplorerWizard(getWsProjectName());
		restServices = restfulWizard.getAllRestServices();
		
		assertTrue("There should be one RESTful web service instead of " + 
					restServices.length, restServices.length == 1);
		
		path = restfulWizard.getPathForRestFulService(restServices[0]);
		assertEquals("/rest?" + queryParam1 + "={" + queryParam1 + ":" + queryType + "}&" +
								queryParam2 + "={" + queryParam2 + ":" + queryType + "}", path);
	}
	
	@Test
	public void testEditingQueryParam() {
		
		String queryParam1 = "param1";
		String queryParam1New = "newParam1";
		String queryParam2 = "param2";
		String queryParam2New = "newParam2";
		String queryType = "java.lang.String";
		
		packageExplorer.openFile(getWsProjectName(), "src", 
				getWsPackage(), getWsName() + ".java").toTextEditor();
		resourceHelper.copyResourceToClass(bot.editorByTitle(getWsName() + ".java"),
				PathAnnotationSupportTest.class.getResourceAsStream(QUERY_TWO_PARAM_RESOURCE), 
				false, false, getWsPackage(), getWsName(), 
				queryParam1, queryType, queryParam2, queryType);
		bot.sleep(Timing.time2S());
		resourceHelper.replaceInEditor(bot.activeEditor().toTextEditor(), 
				queryParam1, queryParam1New, false);
		bot.sleep(Timing.time2S());
		
		restfulWizard = new RESTFullExplorerWizard(getWsProjectName());
		SWTBotTreeItem[] restServices = restfulWizard.getAllRestServices();
		
		assertTrue("There should be one RESTful web service instead of " + 
					restServices.length, restServices.length == 1);
		String path = restfulWizard.getPathForRestFulService(restServices[0]);
		assertEquals("/rest?" + queryParam1New + "={" + queryParam1New + ":" + queryType + "}&" +
								queryParam2 + "={" + queryParam2 + ":" + queryType + "}", path);
		
		
		resourceHelper.replaceInEditor(bot.activeEditor().toTextEditor(), 
				queryParam1New, queryParam1, false);
		resourceHelper.replaceInEditor(bot.activeEditor().toTextEditor(), 
				queryParam2, queryParam2New, false);
		bot.sleep(Timing.time2S());
		restfulWizard = new RESTFullExplorerWizard(getWsProjectName());
		restServices = restfulWizard.getAllRestServices();
		
		assertTrue("There should be one RESTful web service instead of " + 
					restServices.length, restServices.length == 1);
		path = restfulWizard.getPathForRestFulService(restServices[0]);
		assertEquals("/rest?" + queryParam1 + "={" + queryParam1 + ":" + queryType + "}&" +
								queryParam2New + "={" + queryParam2New + ":" + queryType + "}", path);
		
	}
	
	@Test
	public void testEditingTypeOfParam() {
		
		String queryParam1 = "param1";
		String queryParam2 = "param2";
		String queryType1 = "java.lang.String";
		String queryType2 = "java.lang.Integer";
		String queryTypeNew = "java.lang.Long";
		
		packageExplorer.openFile(getWsProjectName(), "src", 
				getWsPackage(), getWsName() + ".java").toTextEditor();
		resourceHelper.copyResourceToClass(bot.editorByTitle(getWsName() + ".java"),
				PathAnnotationSupportTest.class.getResourceAsStream(QUERY_TWO_PARAM_RESOURCE), 
				false, false, getWsPackage(), getWsName(), 
				queryParam1, queryType1, queryParam2, queryType2);
		bot.sleep(Timing.time2S());
		resourceHelper.replaceInEditor(bot.activeEditor().toTextEditor(), 
				queryType1, queryTypeNew, false);
		bot.sleep(Timing.time2S());
		
		restfulWizard = new RESTFullExplorerWizard(getWsProjectName());
		SWTBotTreeItem[] restServices = restfulWizard.getAllRestServices();
		
		assertTrue("There should be one RESTful web service instead of " + 
					restServices.length, restServices.length == 1);
		String path = restfulWizard.getPathForRestFulService(restServices[0]);
		assertEquals("/rest?" + queryParam1 + "={" + queryParam1 + ":" + queryTypeNew + "}&" +
								queryParam2 + "={" + queryParam2 + ":" + queryType2 + "}", path);
		
		
		resourceHelper.replaceInEditor(bot.activeEditor().toTextEditor(), 
				queryTypeNew, queryType1, false);
		resourceHelper.replaceInEditor(bot.activeEditor().toTextEditor(), 
				queryType2, queryTypeNew, false);
		bot.sleep(Timing.time2S());
		restfulWizard = new RESTFullExplorerWizard(getWsProjectName());
		restServices = restfulWizard.getAllRestServices();
		
		assertTrue("There should be one RESTful web service instead of " + 
					restServices.length, restServices.length == 1);
		path = restfulWizard.getPathForRestFulService(restServices[0]);
		assertEquals("/rest?" + queryParam1 + "={" + queryParam1 + ":" + queryType1 + "}&" +
								queryParam2 + "={" + queryParam2 + ":" + queryTypeNew + "}", path);
		
	}

}
