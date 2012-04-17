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

package org.jboss.tools.ws.ui.bot.test.rest.explorer;

import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.junit.Before;
import org.junit.Test;

/**
 * Test checks if context menu 'Add RESTful 1.1 Support' works properly
 * @author jjankovi
 *
 */
public class RESTfulSupportTest extends RESTfulTestBase {
	
	protected String getWsProjectName() {
		return "RestExplorerTest";
	}
	
	@Before
	public void setup() {		
		if (!projectExists(getWsProjectName())) {
			projectHelper.createProject(getWsProjectName());
		}
	}
	
	
	@Test
	public void test_JAXRS_ExplorerSupport() {
		
		restfulHelper.addRestSupport(getWsProjectName());
		assertTrue(restfulHelper.isRestSupportEnabled(getWsProjectName()));
		
	}

}
