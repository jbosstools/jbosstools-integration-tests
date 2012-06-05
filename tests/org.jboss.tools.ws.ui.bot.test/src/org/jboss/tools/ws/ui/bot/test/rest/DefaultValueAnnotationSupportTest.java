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
	
	private String projectName = "default1";

	@Override
	protected String getWsProjectName() {
		return projectName;
	}
	
	@Override
	public void cleanup() {		
		projectExplorer.deleteAllProjects();
	}
	
	@Test
	public void testQueryParamDefaultValue() {
		
		String queryParam = "param";
		String defaultValue = "abc";
		String queryParamType = "java.lang.String";
		
		/* prepare project */
		importRestWSProject(getWsProjectName());
		
		/* get JAX-RS REST explorer for the project */
		restfulWizard = new RESTFullExplorerWizard(getWsProjectName());
		SWTBotTreeItem[] restServices = restfulWizard.getAllRestServices();
		
		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices[0], 
				"/rest?" + queryParam + "={" + queryParam + ":" + 
						  queryParamType + "=" + defaultValue + "}");
		
	}
	
	@Test
	public void testPathParamDefaultValue() {
		
	}
	
	@Test
	public void testMatrixParamDefaultValue() {
		
	}
	
}
