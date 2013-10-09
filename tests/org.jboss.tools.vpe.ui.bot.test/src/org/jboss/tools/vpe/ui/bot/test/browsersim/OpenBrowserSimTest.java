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

import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.test.JBTSWTBotTestCase;
/**
 * Test opening of Browsersim within JBoss Perspective
 * @author Vladimir Pakan
 *
 */
public class OpenBrowserSimTest extends JBTSWTBotTestCase{
  /**
   * Opens and closes BrowserSim
   */
	public void testOpenBrowserSim(){
	  if (!bot.activePerspective().getLabel().equals(IDELabel.SelectPerspectiveDialog.JBOSS)){
	    bot.perspectiveByLabel(IDELabel.SelectPerspectiveDialog.JBOSS).activate();
	  }
	  final String browserSimmProcessName = "BrowserSimRunner";
	  int countBrowserSimmProcesses = SWTUtilExt.countJavaProcess(browserSimmProcessName);
	  // this also asserts that BrowserSim runs without error within JBT
		bot.toolbarButtonWithTooltip(IDELabel.ToolbarButton.RUN_BROWSER_SIM).click();
		assertTrue("No new BrowserSim process was started",countBrowserSimmProcesses + 1 == SWTUtilExt.countJavaProcess(browserSimmProcessName));
		// currently there is no way how to close BrowserSim within running JBT
		// BrowserSim is automatically closed when JBT are
	}
  @Override
  protected void activePerspective() {
    // do nothing here it's not working 
  }
  	
}