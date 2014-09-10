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
package org.jboss.tools.ws.ui.bot.test;

import junit.framework.TestSuite;

import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTEclipseExt;
import org.jboss.tools.ui.bot.ext.types.ViewType;
import org.jboss.tools.ws.ui.bot.test.utils.EclipseCDIHelper;
import org.junit.BeforeClass;

public abstract class AbstractTestSuite extends TestSuite {
	
	private static final SWTBotExt bot = new SWTBotExt();
	
	private static final SWTEclipseExt eclipse = new SWTEclipseExt(bot);
	
	/*
	 * init method "setup()" shows a project explorer view as default, disable
	 * folding (to easier source code editing)
	 */
	@BeforeClass
	public static void setUpSuite() {
		eclipse.showView(ViewType.PROJECT_EXPLORER);
		EclipseCDIHelper.disableFolding();
	}

}
