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
package org.jboss.tools.archives.ui.bot.test;

import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 *
 * @author Jaroslav Jankovic
 */
@RunWith(RequirementAwareSuite.class)
@SuiteClasses({
	ViewIsPresentTest.class,
	BuildingArchiveNode.class,
	BuildingProjectTest.class,
	BuildingArchiveTest.class,
	CreatingArchiveTest.class,
	DeletingArchiveTest.class,
	ModifyingArchiveTest.class,
	DeployingArchiveTest.class,
})
public class ArchivesAllBotTests {
		
}
