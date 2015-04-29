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
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.tools.browsersim.reddeer.BrowserSimHandler;
/**
 * Tests loading URL in BrowserSim
 * @author Vladimir Pakan
 *
 */
public class BrowserSimLoadUrlTest extends BrowserSimTest{
  private static final Logger log = Logger.getLogger(BrowserSimLoadUrlTest.class);
  private BrowserSimHandler browserSimHandler = null;
  /**
   * Opens BrowserSim with loaded URL and loads another Url
   */
	public void testLoadUrlInBrowserSim(){
	  // loads URL when BrowserSim is starting
	  String urlToLoad = "www.github.com";
	  log.info("BrowserSim is loading url: " + urlToLoad);
		browserSimHandler = new BrowserSimHandler(urlToLoad, TimePeriod.NORMAL);
		BrowserSimAssertions.assertBrowserTextContains(browserSimHandler, "<title>GitHub");
		// loads URL via Browser widget
		urlToLoad = "http://www.eurosport.com/";
		log.info("BrowserSim is loading url: " + urlToLoad);
		browserSimHandler.loadUrlToBrowser(urlToLoad , TimePeriod.NORMAL);
		BrowserSimAssertions.assertBrowserTextContains(browserSimHandler, "<title>Eurosport.com");
		BrowserSimAssertions.assertAddressBarContains(browserSimHandler,"www.eurosport");
		BrowserSimAssertions.assertTitleBarContains(browserSimHandler,"Eurosport");
		// loads URL via address bar
		urlToLoad = "www.jboss.org/tools";
		browserSimHandler.loadUrlFromAddressBar(urlToLoad , TimePeriod.getCustom(TIME_20S));
    BrowserSimAssertions.assertBrowserTextContains(browserSimHandler, "<title>JBoss Tools");
    BrowserSimAssertions.assertAddressBarContains(browserSimHandler,urlToLoad);
    BrowserSimAssertions.assertTitleBarContains(browserSimHandler,"JBoss Tools");
  }
  @Override
  protected BrowserSimHandler getBrowserSimHandler() {
    return this.browserSimHandler;
  }
}