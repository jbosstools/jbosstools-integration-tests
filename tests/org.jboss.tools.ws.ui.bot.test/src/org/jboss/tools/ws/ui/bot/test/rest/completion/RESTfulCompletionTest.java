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

import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.junit.Test;

/**
 * Test operates on JAX-RS completion
 * @author jjankovi
 *
 */
public class RESTfulCompletionTest extends RESTfulTestBase{

	protected String getWsProjectName() {
		return "RestServicesCompletion";
	}
	
	protected String getWsPackage() {
		return "org.rest.services.completion.test";
	}

	protected String getWsName() {
		return "RestService";
	}
	
	@Override
	public void setup() {		
		if (!projectExists(getWsProjectName())) {
			projectHelper.createProject(getWsProjectName());	
			addRestSupport(getWsProjectName());
		}
		if (!projectExplorer.isFilePresent(getWsProjectName(), "Java Resources", 
										  "src", getWsPackage(), getWsName() + ".java")) {
			projectHelper.createClass(getWsProjectName(), getWsPackage(), getWsName());
		}
	}
	
	@Test
	public void testWithEmptyPrefix() {
		
	}
	
	@Test
	public void testWithValidPrefixAtTheEnd() {
		
	}
	
	@Test
	public void testWithValidPrefixInTheBeginning() {
		
	}
	
	@Test
	public void testWithInvalidPrefixAtTheEnd() {
		
	}
	
	@Test
	public void testWithInvalidPrefixInTheBeginning() {
		
	}
	
	@Test
	public void testWithAllInvalidParamSelection() {
		
	}
	
}
