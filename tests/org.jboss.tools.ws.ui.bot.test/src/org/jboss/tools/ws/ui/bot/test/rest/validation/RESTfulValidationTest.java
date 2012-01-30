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

package org.jboss.tools.ws.ui.bot.test.rest.validation;

import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.jboss.tools.ws.ui.bot.test.rest.explorer.RESTfulExplorerTest;
import org.junit.Test;

/**
 * Test operates on JAX-RS validation
 * @author jjankovi
 *
 */
public class RESTfulValidationTest extends RESTfulTestBase {
	
	private final String GET_METHOD_PATH = "/{id}";
	private final String CORRECT_PATH_PARAM = "id";
	private final String BAD_PATH_PARAM = "customerId";
	private final String SIMPLE_REST_WS_RESOURCE = "/resources/restful/SimpleRestWS.java.ws";
	
	protected String getWsProjectName() {
		return "RestServicesValidation";
	}
	
	protected String getWsPackage() {
		return "org.rest.validation.services.test";
	}

	protected String getWsName() {
		return "RestService";
	}
	
	@Test
	public void testCorrectValueValidation() {
		
		resourceHelper.copyResourceToClass(bot.editorByTitle(getWsName() + ".java"), 
				   RESTfulExplorerTest.class.getResourceAsStream(SIMPLE_REST_WS_RESOURCE), 
				   false, getWsPackage(), getWsName(), GET_METHOD_PATH, CORRECT_PATH_PARAM);
		
		assertTrue(getRESTValidationErrors(getWsProjectName()).length == 0);
	}
	
	@Test
	public void testBadValueValidation() {
		
		resourceHelper.copyResourceToClass(bot.editorByTitle(getWsName() + ".java"), 
				   RESTfulExplorerTest.class.getResourceAsStream(SIMPLE_REST_WS_RESOURCE), 
				   false, getWsPackage(), getWsName(), GET_METHOD_PATH, BAD_PATH_PARAM);
		
		assertTrue("" + getRESTValidationErrors(getWsProjectName()).length, 
				   getRESTValidationErrors(getWsProjectName()).length == 1);
	}
	
	@Test
	public void testCorrectToBadValueValidation() {
		
		resourceHelper.copyResourceToClass(bot.editorByTitle(getWsName() + ".java"), 
				   RESTfulExplorerTest.class.getResourceAsStream(SIMPLE_REST_WS_RESOURCE), 
				   false, getWsPackage(), getWsName(), GET_METHOD_PATH, CORRECT_PATH_PARAM);
		
		resourceHelper.copyResourceToClass(bot.editorByTitle(getWsName() + ".java"), 
				   RESTfulExplorerTest.class.getResourceAsStream(SIMPLE_REST_WS_RESOURCE), 
				   false, getWsPackage(), getWsName(), GET_METHOD_PATH, BAD_PATH_PARAM);
		
		assertTrue("" + getRESTValidationErrors(getWsProjectName()).length, 
				   getRESTValidationErrors(getWsProjectName()).length == 1);
	}
	
	@Test
	public void testBadToCorrectValueValidation() {
		resourceHelper.copyResourceToClass(bot.editorByTitle(getWsName() + ".java"), 
				   RESTfulExplorerTest.class.getResourceAsStream(SIMPLE_REST_WS_RESOURCE), 
				   false, getWsPackage(), getWsName(), GET_METHOD_PATH, BAD_PATH_PARAM);
		
		resourceHelper.copyResourceToClass(bot.editorByTitle(getWsName() + ".java"), 
				   RESTfulExplorerTest.class.getResourceAsStream(SIMPLE_REST_WS_RESOURCE), 
				   false, getWsPackage(), getWsName(), GET_METHOD_PATH, CORRECT_PATH_PARAM);
		
		assertTrue("" + getRESTValidationErrors(getWsProjectName()).length, 
				   getRESTValidationErrors(getWsProjectName()).length == 0);
	}
	
	@Test
	public void testJAX_RS_Validator() {
		
		disableRESTValidation();
		
		resourceHelper.copyResourceToClass(bot.editorByTitle(getWsName() + ".java"), 
				   RESTfulExplorerTest.class.getResourceAsStream(SIMPLE_REST_WS_RESOURCE), 
				   false, getWsPackage(), getWsName(), GET_METHOD_PATH, BAD_PATH_PARAM);
		
		assertTrue("" + getRESTValidationErrors(getWsProjectName()).length, 
				   getRESTValidationErrors(getWsProjectName()).length == 0);
		
		enableRESTValidation();
		
		assertTrue("" + getRESTValidationErrors(getWsProjectName()).length, 
				   getRESTValidationErrors(getWsProjectName()).length == 1);
	}

}
