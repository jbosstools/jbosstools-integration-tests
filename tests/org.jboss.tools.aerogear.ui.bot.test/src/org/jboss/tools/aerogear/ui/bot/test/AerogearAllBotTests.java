/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.aerogear.ui.bot.test;

import org.jboss.tools.aerogear.ui.bot.test.app.CreateHybridApplication;
import org.jboss.tools.aerogear.ui.bot.test.app.OpenConfigEditor;
import org.jboss.tools.aerogear.ui.bot.test.app.RunWithCordovaSim;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({ 
	CreateHybridApplication.class, 
	OpenConfigEditor.class, 
	RunWithCordovaSim.class })
@RunWith(RequirementAwareSuite.class)
public class AerogearAllBotTests {

}
