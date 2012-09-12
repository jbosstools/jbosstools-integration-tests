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
import org.junit.Test;

public class QueryAnnotationSupportTest extends RESTfulTestBase {
	
	private final String QUERY_TWO_PARAM_RESOURCE = "QueryTwoParam.java.ws";
	
	private String queryParam1 = "param1";
	private String queryParam2 = "param2";
	private String queryType = "java.lang.String";
	private String queryParam1New = "newParam1";
	private String queryType1 = "java.lang.String";
	private String queryType2 = "java.lang.Integer";
	private String queryTypeNew = "java.lang.Long";
	
	@Override
	public void setup() {
	
	}
	
	@Test
	public void testQueryParamSupport() {
		
		/* prepare project */
		importRestWSProject("query1");
		
		/* get RESTful services from JAX-RS REST explorer for the project */
		SWTBotTreeItem[] restServices = restfulServicesForProject("query1"); 
		
		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);	
		assertExpectedPathOfService(restServices[0], 
				"/rest?" + queryParam1 + "={" + queryParam1 + ":" + queryType + "}");
		
		/* prepare project*/
		importRestWSProject("query2");
		
		/* get RESTful services from JAX-RS REST explorer for the project */
		restServices = restfulServicesForProject("query2");
		
		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);	
		assertExpectedPathOfService(restServices[0], 
				"/rest?" + queryParam1 + "={" + queryParam1 + ":" + queryType + "}&" +
						queryParam2 + "={" + queryParam2 + ":" + queryType + "}");
	}

	@Test
	public void testEditingQueryParam() {
		
		/* prepare project */
		importRestWSProject("query2");
		
		/* replace param1 to newParam1 */
		resourceHelper.replaceInEditor(editorForClass("query2", "src", 
				"org.rest.test", "RestService.java").toTextEditor(), 
				queryParam1, queryParam1New, true);
		bot.sleep(Timing.time2S());
		
		/* get RESTful services from JAX-RS REST explorer for the project */
		SWTBotTreeItem[] restServices = restfulServicesForProject("query2");
		
		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices[0], 
				"/rest?" + queryParam1New + "={" + queryParam1New + ":" + queryType + "}&" +
						queryParam2 + "={" + queryParam2 + ":" + queryType + "}");
	}
	
	@Test
	public void testEditingTypeOfQueryParam() {
		
		/* prepare project anc class */
		importRestWSProject("query2");
		prepareRestfulResource(editorForClass("query2", "src", 
				"org.rest.test", "RestService.java"), QUERY_TWO_PARAM_RESOURCE, 
				"org.rest.test", "RestService",
				queryParam1, queryType1, queryParam2, queryType2);
		resourceHelper.replaceInEditor(editorForClass("query2", "src", 
				"org.rest.test", "RestService.java").toTextEditor(), 
				queryType1, queryTypeNew, true);
		bot.sleep(Timing.time2S());
		
		/* get RESTful services from JAX-RS REST explorer for the project */
		SWTBotTreeItem[] restServices = restfulServicesForProject("query2");
		
		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices[0], 
				"/rest?" + queryParam1 + "={" + queryParam1 + ":" + queryTypeNew + "}&" +
						queryParam2 + "={" + queryParam2 + ":" + queryType2 + "}");
	}
}
