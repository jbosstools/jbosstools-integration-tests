/*******************************************************************************
 * Copyright (c) 2010-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.ws.ui.bot.test.rest.validation;

import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ApplicationPathParameterTest extends RESTfulTestBase {

	@Override
	public String getWsProjectName() {
		return "restApplication1";
	}
	
	@Before
	public void setup() {		
		
	}
	
	@After
	public void cleanup() {
		projectExplorer.deleteAllProjects();
	}
	
	@Test
	public void testMultipleAppClasses() {
		
	}
	
	@Test
	public void testWebXmlApplicationOption() {
		
	}
	
}
