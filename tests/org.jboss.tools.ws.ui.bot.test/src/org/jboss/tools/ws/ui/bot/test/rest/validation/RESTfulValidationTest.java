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
import org.junit.Test;

/**
 * Test operates on JAX-RS validation
 * @author jjankovi
 *
 */
public class RESTfulValidationTest extends RESTfulTestBase {
	
	private static final String GET_METHOD_PATH = "/{id}";
	private static final String CORRECT_PATH_PARAM = "id";
	private static final String BAD_PATH_PARAM = "customerId";
	
	@Override
	protected String getWsProjectName() {
		return "restEmpty";
	}
	
	@Test
	public void testCorrectValueValidation() {
		
		/* prepare project */
		prepareRestfulResource(editorForClass(getWsProjectName(), "src", 
				"org.rest.test", "RestService.java"), SIMPLE_REST_WS_RESOURCE, 
				"org.rest.test", "RestService",
				GET_METHOD_PATH, CORRECT_PATH_PARAM);
		
		/* test count of validation errors */
		assertCountOfPathAnnotationValidationErrors(getWsProjectName(), 0);
	}
	
	

	@Test
	public void testBadValueValidation() {
		
		/* prepare project */
		prepareRestfulResource(editorForClass(getWsProjectName(), "src", 
				"org.rest.test", "RestService.java"), SIMPLE_REST_WS_RESOURCE, 
				"org.rest.test", "RestService",
				GET_METHOD_PATH, BAD_PATH_PARAM);
		
		/* test count of validation errors */
		assertCountOfPathAnnotationValidationErrors(getWsProjectName(), 1);
	}
	
	@Test
	public void testCorrectToBadValueValidation() {
		
		/* prepare project */
		prepareRestfulResource(editorForClass(getWsProjectName(), "src", 
				"org.rest.test", "RestService.java"), SIMPLE_REST_WS_RESOURCE, 
				"org.rest.test", "RestService",
				GET_METHOD_PATH, CORRECT_PATH_PARAM);
		prepareRestfulResource(bot.activeEditor(), SIMPLE_REST_WS_RESOURCE, 
				"org.rest.test", "RestService",
				GET_METHOD_PATH, BAD_PATH_PARAM);
		
		/* test count of validation errors */
		assertCountOfPathAnnotationValidationErrors(getWsProjectName(), 1);
	}
	
	@Test
	public void testBadToCorrectValueValidation() {
		
		/* prepare project */
		prepareRestfulResource(editorForClass(getWsProjectName(), "src", 
				"org.rest.test", "RestService.java"), SIMPLE_REST_WS_RESOURCE, 
				"org.rest.test", "RestService",
				GET_METHOD_PATH, BAD_PATH_PARAM);
		prepareRestfulResource(bot.activeEditor(), SIMPLE_REST_WS_RESOURCE, 
				"org.rest.test", "RestService",
				GET_METHOD_PATH, CORRECT_PATH_PARAM);
		
		/* test count of validation errors */
		assertCountOfPathAnnotationValidationErrors(getWsProjectName(), 0);
	}
	
	@Test
	public void testJaxRsValidator() {
		
		/* disable restful validation */
		restfulHelper.disableRESTValidation();
		
		/* prepare project */
		prepareRestfulResource(editorForClass(getWsProjectName(), "src", 
				"org.rest.test", "RestService.java"), SIMPLE_REST_WS_RESOURCE, 
				"org.rest.test", "RestService",
				GET_METHOD_PATH, BAD_PATH_PARAM);
		
		/* test count of validation errors */
		assertCountOfPathAnnotationValidationErrors(getWsProjectName(), 0);
		
		/* enable restful validation */
		restfulHelper.enableRESTValidation();
		
		/* test count of validation errors */
		assertCountOfPathAnnotationValidationErrors(getWsProjectName(), 1);
	}

}
