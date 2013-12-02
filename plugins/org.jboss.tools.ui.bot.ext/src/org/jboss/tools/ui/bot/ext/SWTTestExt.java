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
package org.jboss.tools.ui.bot.ext;

import java.util.Properties;
import org.apache.log4j.Logger;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.tools.ui.bot.ext.config.ConfiguredState;
import org.jboss.tools.ui.bot.ext.view.ConsoleView;
import org.jboss.tools.ui.bot.ext.view.PackageExplorer;
import org.jboss.tools.ui.bot.ext.view.ProblemsView;
import org.jboss.tools.ui.bot.ext.view.ProjectExplorer;
import org.jboss.tools.ui.bot.ext.view.PropertiesView;
import org.jboss.tools.ui.bot.ext.view.ServersView;
/**
 * Base class for SWTBot Tests using SWTBotExt
 * @author jpeterka
 *
 */
public class SWTTestExt extends SWTBotTestCase{

	public static Logger log = Logger.getLogger(SWTTestExt.class);
	public static final SWTBotExt bot = new SWTBotExt();
	public static final SWTEclipseExt eclipse = new SWTEclipseExt(bot);
	public static final SWTUtilExt util = new SWTUtilExt(bot);
	public static final SWTOpenExt open = new SWTOpenExt(bot);
	public static final SWTJBTExt jbt = new SWTJBTExt(bot);
	
	// Views
	public static final PackageExplorer packageExplorer = new PackageExplorer();
	public static final ProjectExplorer projectExplorer = new ProjectExplorer();
	public static final ServersView servers = new ServersView();
	public static final ProblemsView problems = new ProblemsView();
	public static final ConsoleView console = new ConsoleView();
	public static final PropertiesView properties = new PropertiesView();
	
	// config & state
	/**
	 * represents state of configured stuff like server, runtimes etc
	 * NOTE : this state can change right before test class is instantiated (because of possibly various requirements of tests defined by its annotations), 
	 * please do not bind its properties, use them directly  
	 */
	public static final ConfiguredState configuredState = new ConfiguredState();
	
	public static Properties props;
	/**
	 * Get properties for tests
	 * @param key
	 */
	public static String getProperty(String key) {
		return util.getValue(props,key);
	}	
	static {
		System.setProperty("org.eclipse.swtbot.playback.delay","10");
	}
	// Wait Constants
	public static int TIME_500MS = Timing.time500MS();
	public static int TIME_1S = Timing.time1S();
	public static int TIME_5S = Timing.time5S();
	public static int TIME_10S = Timing.time10S();
	public static int TIME_20S = Timing.time20S();
	public static int TIME_30S = Timing.time30S();
	public static int TIME_60S = Timing.time60S();
	public static int TIME_UNLIMITED = Timing.timeUnlimited();
	
  protected void setUp() throws Exception {
    super.setUp();
  }
  
  @Override
  public void runBare() throws Throwable {
      super.runBare();
  }
}
