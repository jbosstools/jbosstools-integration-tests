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

import org.jboss.tools.aerogear.ui.bot.test.app.RunOnAndroid;
import org.jboss.tools.aerogear.ui.bot.test.export.ExportMobileApplication;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;

@SuiteClasses({ 
  ExportMobileApplication.class,
  RunOnAndroid.class })
@RunWith(RedDeerSuite.class)
public class AerogearNonMvnReadyBotTests {

}
