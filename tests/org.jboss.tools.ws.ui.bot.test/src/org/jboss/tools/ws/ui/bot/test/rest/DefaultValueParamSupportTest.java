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

public class DefaultValueParamSupportTest extends RESTfulTestBase {
	
	private RESTFullExplorerWizard restfulWizard = null;
	
	private String projectName = "restEmpty";
	
	private final String DEFAULT_VALUE_RESOURCE = "/resources/restful/DefaultValue.java.ws";

	@Override
	protected String getWsProjectName() {
		return projectName;
	}
	
	@Override
	public void cleanup() {		
		 bot.activeEditor().toTextEditor().save();
	}
	
	@Test
	public void testDefaultValueSupport() {
		
		String queryParam = "param";
		String defaultValue = "abc";
		String defaultValueNew = "cba";
		String queryParamType = "java.lang.String";
		
		/**  test showing default DefaultValue in RESTful explorer  **/
		packageExplorer.openFile(getWsProjectName(), "src", 
				getWsPackage(), getWsName() + ".java").toTextEditor();
		resourceHelper.copyResourceToClassWithSave(bot.editorByTitle(getWsName() + ".java"),
				DefaultValueParamSupportTest.class.getResourceAsStream(DEFAULT_VALUE_RESOURCE), 
				false, false, getWsPackage(), getWsName(), queryParam, defaultValue, queryParamType);
		bot.sleep(Timing.time2S());
		
		restfulWizard = new RESTFullExplorerWizard(getWsProjectName());
		SWTBotTreeItem[] restServices = restfulWizard.getAllRestServices();
		
		assertTrue("There should be one RESTful web service instead of " + 
					restServices.length, restServices.length == 1);
		String path = restfulWizard.getPathForRestFulService(restServices[0]);
		assertEquals("/rest?" + queryParam + "={" + queryParam + ":" + 
					  queryParamType + "=" + defaultValue + "}", path);
		
		/**  test showing edited DefaultValue value in RESTful explorer  **/
		resourceHelper.replaceInEditor(bot.activeEditor().toTextEditor(), 
				defaultValue, defaultValueNew, false);
		bot.sleep(Timing.time2S());
		
		restfulWizard = new RESTFullExplorerWizard(getWsProjectName());
		restServices = restfulWizard.getAllRestServices();
		
		assertTrue("There should be one RESTful web service instead of " + 
					restServices.length, restServices.length == 1);
		path = restfulWizard.getPathForRestFulService(restServices[0]);
		assertEquals("/rest?" + queryParam + "={" + queryParam + ":" + 
				  queryParamType + "=" + defaultValueNew + "}", path);
		
		/**  test showing deleted DefaultValue value in RESTful explorer  **/
		resourceHelper.replaceInEditor(bot.activeEditor().toTextEditor(), 
				"@DefaultValue(\"" + defaultValueNew  + "\")", "", false);
		bot.sleep(Timing.time2S());
		
		restfulWizard = new RESTFullExplorerWizard(getWsProjectName());
		restServices = restfulWizard.getAllRestServices();
		
		assertTrue("There should be one RESTful web service instead of " + 
					restServices.length, restServices.length == 1);
		path = restfulWizard.getPathForRestFulService(restServices[0]);
		assertEquals("/rest?" + queryParam + "={" + queryParam + ":" + 
				  queryParamType + "}", path);
		
	}
	
}
