/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.drools.ui.bot.test;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.jboss.tools.drools.ui.bot.test.smoke.ManageDroolsRuntime;
import org.jboss.tools.drools.ui.bot.test.smoke.ManageDroolsProject;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * 
 * This is struts swtbot testcase for JBoss Tools.
 * 
 * @author Vladimir Pakan
 * 
 */
@RunWith(Suite.class)
@SuiteClasses({ManageDroolsRuntime.class,
  ManageDroolsProject.class})  
public class DroolsAllBotTests extends SWTTestExt {
  public static final String DROOLS_PROJECT_NAME = "droolsTest";
  public static final String DROOLS_RUNTIME_NAME = "Drools Test Runtime";
  public static String DROOLS_RUNTIME_LOCATION = null;
  @BeforeClass
  public static void setUpTest() {
    DROOLS_RUNTIME_LOCATION = System.getProperty("java.io.tmpdir");
    properties = util.loadProperties(Activator.PLUGIN_ID);
    try{
      SWTBotView welcomeView = eclipse.getBot().viewByTitle(IDELabel.View.WELCOME);
      welcomeView.close();
    } catch (WidgetNotFoundException wnfe){
      // Do nothing ignore this error
    }
    	
  }

  @AfterClass
  public static void tearDownTest() {
    // Ready for later usage
  }
 
}