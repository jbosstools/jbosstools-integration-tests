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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test operates on JAX-RS completion
 * @author jjankovi
 *
 */
public class RESTfulCompletionTest extends RESTfulTestBase{

	private final String CORRECT_PATH_PARAM = "userId";
	
	private final String INCORRECT_PATH_PARAM = "someId";
	
	private final String PATH_PARAM_NAVIGATION = "@PathParam(";
	
	private final List<String> EXP_NON_EMPTY_COMPLETION_RESULT = Arrays.asList("userId - JAX-RS Mapping");
	
	private final List<String> EXP_EMPTY_COMPLETION_RESULT = Arrays.asList("No Default Proposals");
	
	@Before
	public void setup() {
		
	}
	
	@After
	public void cleanup() {
		projectExplorer.deleteAllProjects();
	}
	
	@Test
	public void testWithEmptyPrefix() {
		
		setWsProjectName("restCompletion1");
		prepareRestProject();
		
		packageExplorer.openFile(getWsProjectName(), "src", 
				getWsPackage(), getWsName() + ".java").toTextEditor();
		ContentAssistHelper.checkContentAssistContent(bot, 
				getWsName() + ".java", PATH_PARAM_NAVIGATION, PATH_PARAM_NAVIGATION.length() + 1, 
				0, EXP_NON_EMPTY_COMPLETION_RESULT);
		
	}
	
	@Test
	public void testWithValidPrefixAtTheEnd() {
		
		setWsProjectName("restCompletion2");
		prepareRestProject();
		
		packageExplorer.openFile(getWsProjectName(), "src", 
				getWsPackage(), getWsName() + ".java").toTextEditor();
		ContentAssistHelper.checkContentAssistContent(bot, 
				getWsName() + ".java", PATH_PARAM_NAVIGATION, PATH_PARAM_NAVIGATION.length() + 
				CORRECT_PATH_PARAM.length() + 1, 
				0, EXP_NON_EMPTY_COMPLETION_RESULT);
		
	}
	
	@Test
	public void testWithValidPrefixInTheBeginning() {
	
		setWsProjectName("restCompletion2");
		prepareRestProject();
		
		packageExplorer.openFile(getWsProjectName(), "src", 
				getWsPackage(), getWsName() + ".java").toTextEditor();
		ContentAssistHelper.checkContentAssistContent(bot, 
				getWsName() + ".java", PATH_PARAM_NAVIGATION, PATH_PARAM_NAVIGATION.length() + 1, 
				0, EXP_NON_EMPTY_COMPLETION_RESULT);
		
	}
	
	@Test
	public void testWithInvalidPrefixAtTheEnd() {
		
		setWsProjectName("restCompletion3");
		prepareRestProject();
		
		packageExplorer.openFile(getWsProjectName(), "src", 
				getWsPackage(), getWsName() + ".java").toTextEditor();
		ContentAssistHelper.checkContentAssistContent(bot, 
				getWsName() + ".java", PATH_PARAM_NAVIGATION, PATH_PARAM_NAVIGATION.length() + 
				INCORRECT_PATH_PARAM.length() + 1, 
				0, EXP_EMPTY_COMPLETION_RESULT);
		
	}

	@Test
	public void testWithInvalidPrefixInTheBeginning() {
	
		setWsProjectName("restCompletion3");
		prepareRestProject();
		
		packageExplorer.openFile(getWsProjectName(), "src", 
				getWsPackage(), getWsName() + ".java").toTextEditor();
		ContentAssistHelper.checkContentAssistContent(bot, 
				getWsName() + ".java", PATH_PARAM_NAVIGATION, PATH_PARAM_NAVIGATION.length() + 1, 
				0, EXP_NON_EMPTY_COMPLETION_RESULT);
		
	}
	
	@Test
	public void testWithAllInvalidParamSelection() {
		
		setWsProjectName("restCompletion3");
		prepareRestProject();
		
		packageExplorer.openFile(getWsProjectName(), "src", 
				getWsPackage(), getWsName() + ".java").toTextEditor();
		ContentAssistHelper.checkContentAssistContent(bot, 
				getWsName() + ".java", PATH_PARAM_NAVIGATION, PATH_PARAM_NAVIGATION.length() + 1, 
				INCORRECT_PATH_PARAM.length(), EXP_NON_EMPTY_COMPLETION_RESULT);
		
	}
	
}
