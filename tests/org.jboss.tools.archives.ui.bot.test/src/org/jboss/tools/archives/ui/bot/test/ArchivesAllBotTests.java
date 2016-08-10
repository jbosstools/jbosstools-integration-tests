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

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 *
 * @author Jaroslav Jankovic
 */
@RunWith(RedDeerSuite.class)
@SuiteClasses({
	ViewIsPresentTest.class,
	ArchivePreferencesTest.class,
	ArchiveViewReSwitchingTest.class,
	FolderTest.class,
	FilesetTest.class,
	UserLibrariesFilesetTest.class,
	ArchivesSupportTest.class,
	BuildingArchiveNode.class,
	BuildingProjectTest.class,
	BuildingArchiveTest.class,
	CreatingArchiveTest.class,
	DeletingArchiveTest.class, 
	ModifyingArchiveTest.class,
	VariousProjectsArchiving.class,
	DeployingArchiveTest.class,
})
public class ArchivesAllBotTests {
		
}
