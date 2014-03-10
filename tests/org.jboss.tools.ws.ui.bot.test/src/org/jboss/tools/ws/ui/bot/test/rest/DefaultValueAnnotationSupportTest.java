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

import java.util.List;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.ProjectItem;
import org.junit.Test;

public class DefaultValueAnnotationSupportTest extends RESTfulTestBase {
	
	private String queryParam = "param";
	private String defaultValue = "abc";
	private String queryParamType = "java.lang.String";
	
	@Override
	public void setup() {
	
	}
	
	@Test
	public void testQueryParamDefaultValue() {
		
		/* prepare project */
		importRestWSProject("default1");
		
		/* get RESTful services from JAX-RS REST explorer for the project */
		List<ProjectItem> restServices = restfulServicesForProject("default1");
		
		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices.get(0), 
				"/rest?" + queryParam + "={" + queryParam + ":" + 
						  queryParamType + "=" + defaultValue + "}");
		
	}
	
	/**
	 * Fails due to JBIDE-12027
	 * 
	 * @see https://issues.jboss.org/browse/JBIDE-12027
	 */
	@Test
	public void testMatrixParamDefaultValue() {
		
		/* prepare project */
		importRestWSProject("default2");
		
		/* get RESTful services from JAX-RS REST explorer for the project */
		List<ProjectItem> restServices = restfulServicesForProject("default2");
		
		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices.get(0), 
				"/rest:" + queryParam + "={" + queryParam + ":" + 
						  queryParamType + "=" + defaultValue + "}");
	}
	
	/**
	 * Fails due to JBIDE-12027
	 * 
	 * @see https://issues.jboss.org/browse/JBIDE-12027
	 */
	@Test
	public void testPathParamDefaultValue() {

		/* prepare project */
		importRestWSProject("default3");
		
		/* get RESTful services from JAX-RS REST explorer for the project */
		List<ProjectItem> restServices = restfulServicesForProject("default3");
		
		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices.get(0), 
				"/rest/{" + queryParam + ":" + 
						  queryParamType + "=" + defaultValue + "}");
		
	}
	
}
