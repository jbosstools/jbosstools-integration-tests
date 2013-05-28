/*******************************************************************************
 * Copyright (c) 2011-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.cdi.bot.test.wizard;

import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.junit.After;
import org.junit.Test;

public class DynamicWebProjectWithCDITest extends CDITestBase {
	
	@Override	
	public void prepareWorkspace() {
		if (!projectHelper.projectExists(getProjectName())) {
			projectHelper.createDynamicWebProject(getProjectName());
		}
	}
	
	@After
	public void cleanUp() {
		packageExplorer.deleteProject(getProjectName(), true);
	}
	
	@Override
	public String getProjectName() {
		return "CDIDynamicWebProject";
	}
	
	@Test
	public void testAddCDISupportWithOKButton() {
		
		projectHelper.addCDISupport(getProjectName());
		assertTrue(projectHelper.checkCDISupport(getProjectName()));
		
	}
	
	@Test
	public void testAddCDISupportWithEnterKey() {
		
		projectHelper.addCDISupportWithEnterKey(getProjectName());
		assertTrue(projectHelper.checkCDISupport(getProjectName()));
		
	}

}
