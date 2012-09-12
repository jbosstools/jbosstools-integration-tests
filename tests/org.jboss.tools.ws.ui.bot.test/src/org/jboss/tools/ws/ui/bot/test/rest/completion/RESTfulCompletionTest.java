/*******************************************************************************
 * Copyright (c) 2010-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.ws.ui.bot.test.rest.completion;

import java.util.Arrays;
import java.util.List;

import org.jboss.tools.ui.bot.ext.helper.ContentAssistHelper;
import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.junit.Test;

/**
 * Test operates on JAX-RS completion
 * @author jjankovi
 *
 */
public class RESTfulCompletionTest extends RESTfulTestBase{

	private static final String GET_METHOD_PATH = "/{userId}";
	private static final String CORRECT_PATH_PARAM = "userId";
	private static final String INCORRECT_PATH_PARAM = "someId";
	private static final String PATH_PARAM_NAVIGATION = "@PathParam(";
	private static final List<String> EXP_NON_EMPTY_COMPLETION_RESULT = 
			Arrays.asList("userId - JAX-RS Mapping");
	private static final List<String> EXP_EMPTY_COMPLETION_RESULT = 
			Arrays.asList("No Default Proposals");
	
	@Override
	protected String getWsProjectName() {
		return "restEmpty";
	}
	
	@Test
	public void testWithEmptyPrefix() {
		
		/* prepare project */
		prepareRestfulResource(editorForClass(getWsProjectName(), "src", 
				"org.rest.test", "RestService.java"), SIMPLE_REST_WS_RESOURCE, 
				"org.rest.test", "RestService",
				GET_METHOD_PATH, "");
		
		/* test content assist proposal */
		ContentAssistHelper.checkContentAssistContent(bot, 
				getWsName() + ".java", PATH_PARAM_NAVIGATION, PATH_PARAM_NAVIGATION.length() + 1, 
				0, EXP_NON_EMPTY_COMPLETION_RESULT);
		
	}
	
	@Test
	public void testWithValidPrefixAtTheEnd() {
		
		/* prepare project */
		prepareRestfulResource(editorForClass(getWsProjectName(), "src", 
				"org.rest.test", "RestService.java"), SIMPLE_REST_WS_RESOURCE, 
				"org.rest.test", "RestService",
				GET_METHOD_PATH, CORRECT_PATH_PARAM);
		
		/* test content assist proposal */
		ContentAssistHelper.checkContentAssistContent(bot, 
				getWsName() + ".java", PATH_PARAM_NAVIGATION, PATH_PARAM_NAVIGATION.length() + 
				CORRECT_PATH_PARAM.length() + 1, 
				0, EXP_NON_EMPTY_COMPLETION_RESULT);
		
	}
	
	@Test
	public void testWithValidPrefixInTheBeginning() {
	
		/* prepare project */
		prepareRestfulResource(editorForClass(getWsProjectName(), "src", 
				"org.rest.test", "RestService.java"), SIMPLE_REST_WS_RESOURCE, 
				"org.rest.test", "RestService",
				GET_METHOD_PATH, CORRECT_PATH_PARAM);
		
		/* test content assist proposal */
		ContentAssistHelper.checkContentAssistContent(bot, 
				getWsName() + ".java", PATH_PARAM_NAVIGATION, PATH_PARAM_NAVIGATION.length() + 1, 
				0, EXP_NON_EMPTY_COMPLETION_RESULT);
		
	}
	
	@Test
	public void testWithInvalidPrefixAtTheEnd() {
		
		/* prepare project */
		prepareRestfulResource(editorForClass(getWsProjectName(), "src", 
				"org.rest.test", "RestService.java"), SIMPLE_REST_WS_RESOURCE, 
				"org.rest.test", "RestService",
				GET_METHOD_PATH, INCORRECT_PATH_PARAM);
		
		/* test content assist proposal */
		ContentAssistHelper.checkContentAssistContent(bot, 
				getWsName() + ".java", PATH_PARAM_NAVIGATION, PATH_PARAM_NAVIGATION.length() + 
				INCORRECT_PATH_PARAM.length() + 1, 
				0, EXP_EMPTY_COMPLETION_RESULT);
		
	}

	@Test
	public void testWithInvalidPrefixInTheBeginning() {
	
		/* prepare project */
		prepareRestfulResource(editorForClass(getWsProjectName(), "src", 
				"org.rest.test", "RestService.java"), SIMPLE_REST_WS_RESOURCE, 
				"org.rest.test", "RestService",
				GET_METHOD_PATH, INCORRECT_PATH_PARAM);
		
		/* test content assist proposal */
		ContentAssistHelper.checkContentAssistContent(bot, 
				getWsName() + ".java", PATH_PARAM_NAVIGATION, PATH_PARAM_NAVIGATION.length() + 1, 
				0, EXP_NON_EMPTY_COMPLETION_RESULT);
		
	}
	
	@Test
	public void testWithAllInvalidParamSelection() {
		
		/* prepare project */
		prepareRestfulResource(editorForClass(getWsProjectName(), "src", 
				"org.rest.test", "RestService.java"), SIMPLE_REST_WS_RESOURCE, 
				"org.rest.test", "RestService",
				GET_METHOD_PATH, INCORRECT_PATH_PARAM);
		
		/* test content assist proposal */
		ContentAssistHelper.checkContentAssistContent(bot, 
				getWsName() + ".java", PATH_PARAM_NAVIGATION, PATH_PARAM_NAVIGATION.length() + 1, 
				INCORRECT_PATH_PARAM.length(), EXP_NON_EMPTY_COMPLETION_RESULT);
		
	}
	
}
