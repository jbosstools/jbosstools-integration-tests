/*******************************************************************************
 * Copyright (c) 2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.browsersim;

import org.apache.log4j.Logger;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.ui.bot.ext.SWTEclipseExt;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.test.JBTSWTBotTestCase;
/**
 * Test opening of Browsersim within JBoss Perspective
 * @author Vladimir Pakan
 *
 */
public class OpenBrowserSimTest extends JBTSWTBotTestCase{
  public static Logger log = Logger.getLogger(OpenBrowserSimTest.class);
  /**
   * Opens and closes BrowserSim
   * @throws IllegalAccessException 
   * @throws IllegalArgumentException 
   */
	public void testOpenBrowserSim() throws IllegalArgumentException, IllegalAccessException{
	  if (!bot.activePerspective().getLabel().equals(IDELabel.SelectPerspectiveDialog.JBOSS)){
	    bot.perspectiveByLabel(IDELabel.SelectPerspectiveDialog.JBOSS).activate();
	  }
	  final String browserSimmProcessName = "BrowserSimRunner";
	  int countBrowserSimmProcesses = SWTUtilExt.countJavaProcess(browserSimmProcessName);
	  Object[] beforeListeners = SWTEclipseExt.getWorkbenchListeners().getListeners();
	  // this also asserts that BrowserSim runs without error within JBT
		bot.toolbarButtonWithTooltip(IDELabel.ToolbarButton.RUN_BROWSER_SIM).click();
		assertTrue("No new BrowserSim process was started",countBrowserSimmProcesses + 1 == SWTUtilExt.countJavaProcess(browserSimmProcessName));
		// currently there is no way how to close BrowserSim within running JBT
		// BrowserSim is automatically closed when JBT are but not when run via test
		// So invoking explicitly WorkbenchListener added by BrowserSim
		SWTEclipseExt.retainFromCurrentWorkbenchListeners(beforeListeners)
		  .get(0).postShutdown(PlatformUI.getWorkbench());
	}
  @Override
  protected void activePerspective() {
    // do nothing here it's not working 
  }


}