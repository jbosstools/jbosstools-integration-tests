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

import javax.ws.rs.QueryParam;

import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.tools.ws.reddeer.jaxrs.core.RestService;
import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.junit.Test;

/**
 * {@link QueryParam} annotation support test<br/><br/>
 * 
 * @author jjankovi
 * @author Radoslav Rabara
 */
public class QueryParamAnnotationSupportTest extends RESTfulTestBase {

	private final String QUERY_TWO_PARAM_RESOURCE = "QueryTwoParam.java.ws";

	private final String project1Name = "query1";
	private final String project2Name = "query2";
	private final String project3Name = "query3";

	private final String queryParam1 = "param1";
	private final String queryParam2 = "param2";
	private final String queryParam1New = "newParam1";
	private final String queryType1 = "String";
	private final String queryType2 = "Integer";
	private final String queryTypeNew = "Long";

	@Override
	public void setup() {
	
	}

	@Test
	public void testQueryParamSupport() {
		/* prepare project */
		importRestWSProject(project1Name);

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RestService> restServices = restfulServicesForProject(project1Name);

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);	
		assertExpectedPathOfService(restServices.get(0), 
				"/rest?" + queryParam1 + "={" + queryType1 + "}");

		/* prepare project*/
		importRestWSProject(project2Name);

		/* get RESTful services from JAX-RS REST explorer for the project */
		restServices = restfulServicesForProject(project2Name);

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);	
		assertExpectedPathOfService(restServices.get(0),
				"/rest?" + queryParam1 + "={" + queryType1 + "}&" +
						queryParam2 + "={" + queryType1 + "}");
	}

	@Test
	public void testMatrixParamFieldSupport() {
		/* prepare project */
		importRestWSProject(project3Name);

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RestService> restServices = restfulServicesForProject(project3Name);

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices.get(0),
				"/rest?" + queryParam1 + "={" + queryType1 + "}&" +
						queryParam2 + "={" + queryType2 + "}");
	}

	@Test
	public void testEditingQueryParam() {
		/* prepare project */
		importRestWSProject(project2Name);

		/* replace param1 to newParam1 */
		replaceInRestService(project2Name, queryParam1, queryParam1New);
		AbstractWait.sleep(TimePeriod.getCustom(2));

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RestService> restServices = restfulServicesForProject(project2Name);

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices.get(0),
				"/rest?" + queryParam1New + "={" + queryType1 + "}&" +
						queryParam2 + "={" + queryType1 + "}");
	}

	@Test
	public void testEditingTypeOfQueryParam() {
		final String query2ProjectName = "query2";

		/* prepare project and class */
		importRestWSProject(query2ProjectName);
		prepareRestService(query2ProjectName, QUERY_TWO_PARAM_RESOURCE,
				queryParam1, queryType1, queryParam2, queryType2);
		replaceInRestService(query2ProjectName, queryType1, queryTypeNew);
		AbstractWait.sleep(TimePeriod.getCustom(2));

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RestService> restServices = restfulServicesForProject(query2ProjectName);

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices.get(0),
				"/rest?" + queryParam1 + "={" + queryTypeNew + "}&" +
						queryParam2 + "={" + queryType2 + "}");
	}
}
