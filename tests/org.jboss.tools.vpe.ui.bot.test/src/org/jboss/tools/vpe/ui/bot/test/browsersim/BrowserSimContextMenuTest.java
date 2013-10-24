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

import org.eclipse.swt.widgets.Menu;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.vpe.ui.bot.test.tools.BrowserSimHandler;
/**
 * Tests BrowserSim Context Menu
 * @author Vladimir Pakan
 *
 */
public class BrowserSimContextMenuTest extends BrowserSimTest{
  private BrowserSimHandler browserSimHandler = null;
  /**
   * Checks BrowserSim root context menu
   */
	public void testBrowserSimContextMenu(){
		browserSimHandler = new BrowserSimHandler(bot);
		Menu browserSimContextMenu = browserSimHandler.openBrowserSimContextMenu();
		BrowserSimAssertions.assertMenuContent(ContextMenuHelper.getMenuItemLabels(browserSimContextMenu),
		    new String[]{"Use Skins",
          "Skin",
          "",
          "Rotate Left",
          "Rotate Right",
          "",
          "Debug",
          "Screenshot",
          "Open Synchronized Window",
          "Enable LiveReload",
          "Enable Touch Events",
          "",
          "Open in Default Browser",
          "View Page Source",
          "Preferences...",
          "",
          "About",
          "",
          "Close"});
		ContextMenuHelper.hideMenuRecursively(browserSimContextMenu);
  }
  @Override
  protected void activePerspective() {
    // do nothing here it's not working 
  }
  @Override
  protected BrowserSimHandler getBrowserSimHandler() {
    return this.browserSimHandler;
  }
}