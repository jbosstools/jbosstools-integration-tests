/*******************************************************************************
 * Copyright (c) 2010-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.archives.ui.bot.test;

import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.archives.ui.bot.test.condition.ExplorerInProjectExplorer;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Checks if project archive support can be enabled/disabled on 
 * some project
 * 
 * @author jjankovi
 *
 */
public class ArchivesSupportTest extends ArchivesTestBase {

	private static final String project = "prj";
	
	@BeforeClass
	public static void setup() {
		createJavaProject(project);
	}
	
	@Test
	public void testArchiveSupport() {
		testAddArchiveSupport();
		testRemoveArchiveSupport();
	}
	
	private void testAddArchiveSupport() {
		
		addArchivesSupport(project);
		new WaitUntil(new ExplorerInProjectExplorer(project));
	}
	
	private void testRemoveArchiveSupport() {
		
		removeArchivesSupport(project);
		new WaitWhile(new ExplorerInProjectExplorer(project));
		
		
	}
	
}
