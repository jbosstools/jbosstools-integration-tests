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
import org.jboss.tools.ws.ui.bot.test.ti.wizard.RESTFullExplorerWizard;
import org.junit.Test;

public class DefaultValueAnnotationSupportTest extends RESTfulTestBase {
	
	private String queryParam = "param";
	private String defaultValue = "abc";
	private String queryParamType = "java.lang.String";
	
	@Override
	public void cleanup() {		
		projectExplorer.deleteAllProjects();
	}
	
	@Test
	public void testQueryParamDefaultValue() {
		
		/* prepare project */
		importRestWSProject("default1");
		
		/* get JAX-RS REST explorer for the project */
		restfulWizard = new RESTFullExplorerWizard("default1");
		SWTBotTreeItem[] restServices = restfulWizard.getAllRestServices();
		
		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices[0], 
				"/rest?" + queryParam + "={" + queryParam + ":" + 
						  queryParamType + "=" + defaultValue + "}");
		
	}
	
	@Test
	public void testMatrixParamDefaultValue() {
		
		/* prepare project */
		importRestWSProject("default2");
		
		/* get JAX-RS REST explorer for the project */
		restfulWizard = new RESTFullExplorerWizard("default2");
		SWTBotTreeItem[] restServices = restfulWizard.getAllRestServices();
		
		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices[0], 
				"/rest:" + queryParam + "={" + queryParam + ":" + 
						  queryParamType + "=" + defaultValue + "}");
	}
	
	@Test
	public void testPathParamDefaultValue() {

		/* prepare project */
		importRestWSProject("default3");
		
		/* get JAX-RS REST explorer for the project */
		restfulWizard = new RESTFullExplorerWizard("default3");
		SWTBotTreeItem[] restServices = restfulWizard.getAllRestServices();
		
		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices[0], 
				"/rest/{" + queryParam + ":" + 
						  queryParamType + "=" + defaultValue + "}");
		
	}
	
}
