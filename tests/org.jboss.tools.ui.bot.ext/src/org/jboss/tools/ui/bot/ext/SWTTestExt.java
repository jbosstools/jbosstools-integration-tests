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

import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.tools.ui.bot.ext.config.ConfiguredState;
import org.jboss.tools.ui.bot.ext.config.requirement.RequirementBase;
import org.jboss.tools.ui.bot.ext.gen.IView;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.view.ConsoleView;
import org.jboss.tools.ui.bot.ext.view.PackageExplorer;
import org.jboss.tools.ui.bot.ext.view.ProblemsView;
import org.jboss.tools.ui.bot.ext.view.ProjectExplorer;
import org.jboss.tools.ui.bot.ext.view.ServersView;
import org.junit.AfterClass;
import org.junit.BeforeClass;
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
	
	// config & state
	public static final ConfiguredState configuredState = new ConfiguredState();
	public static final List<RequirementBase> beforeRequirements = new Vector<RequirementBase>();
	public static final List<RequirementBase> afterRequirements = new Vector<RequirementBase>();
	
	public static Properties properties;
	/**
	 * Get properties for hibernate tests
	 * @param key
	 */
	public static String getProperty(String key) {
		return util.getValue(properties,key);
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
	
	/**
	 * fullfills given requirements
	 * @param requirements
	 */
	@BeforeClass
	public static void beforeTest() throws Exception {
		// try to close Welcome view
		open.viewClose(new IView(){
			public List<String> getGroupPath() {
				return new Vector<String>();
			}
			public String getName() {
				return IDELabel.View.WELCOME;
			}});
		log.info("Fullfilling requirements before test");
		for (RequirementBase r : beforeRequirements) {
			r.fullfill();
		}		
	}
	@AfterClass
	public static void afterTest() throws Exception {
		log.info("Fullfilling requirements before test");
		for (RequirementBase r : afterRequirements) {
			r.fullfill();
		}
	}

}
