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

package org.jboss.tools.ws.ui.bot.test.rest.param;

import java.util.List;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.ws.reddeer.jaxrs.core.RESTfulWebService;
import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.junit.ComparisonFailure;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
public class DefaultValueAnnotationSupportTest extends RESTfulTestBase {

	private String paramName = "param";
	private String defaultValue = "abc";
	private String paramType = "String";

	@Override
	public void setup() {

	}

	@Test
	public void testQueryParamDefaultValue() {
		/* prepare project */
		importWSTestProject("default1");

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RESTfulWebService> restServices = restfulServicesForProject("default1");

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices.get(0), 
				"/rest?" + paramName + "={" + paramType + ":\"" + defaultValue + "\"}");
	}

	@Test
	public void testMatrixParamDefaultValue() {
		/* prepare project */
		importWSTestProject("default2");

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RESTfulWebService> restServices = restfulServicesForProject("default2");

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices.get(0), 
				"/rest;" + paramName + "={" + paramType + ":\"" + defaultValue + "\"}");
	}

	/**
	 * Fails due to JBIDE-12027
	 * 
	 * @see https://issues.jboss.org/browse/JBIDE-12027
	 */
	@Test(expected=ComparisonFailure.class)
	public void testPathParamDefaultValue() {
		/* prepare project */
		importWSTestProject("default3");

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RESTfulWebService> restServices = restfulServicesForProject("default3");

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService("JBIDE-12027: ", restServices.get(0), 
				"/rest/{" + paramName +":" + paramType + ":" + defaultValue + "}");
	}
	
}
