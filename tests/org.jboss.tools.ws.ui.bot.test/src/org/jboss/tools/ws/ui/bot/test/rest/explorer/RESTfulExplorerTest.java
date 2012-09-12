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
import org.junit.Test;

/**
 * Test checks if context menu 'Add RESTful 1.1 Support' works properly
 * @author jjankovi
 *
 */
public class RESTfulExplorerTest extends RESTfulTestBase {
	
	protected String getWsProjectName() {
		return "RestExplorerTest";
	}
	
	@Override
	public void setup() {
	
	}
	
	@Test
	public void testJaxRsExplorerSupport() {
		
		/* create dynamic web project */
		projectHelper.createProject(getWsProjectName());
		
		/* add RESTful support into project */
		restfulHelper.addRestSupport(getWsProjectName());
		
		/* test if RESYful explorer is not missing */
		assertRestFullSupport(getWsProjectName());
		
	}

}
