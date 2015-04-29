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
package org.jboss.tools.vpe.ui.bot.test.livereload;

import org.apache.log4j.Logger;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.parts.SWTBotEditorExt;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.jboss.tools.vpe.ui.bot.test.browsersim.BrowserSimAssertions;
import org.jboss.tools.vpe.ui.bot.test.editor.VPEEditorTestCase;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.tools.browsersim.reddeer.BrowserSimHandler;
import org.junit.After;
/**
 * Tests LiveReload functionality in BrowserSim
 * @author Vladimir Pakan
 *
 */
public class BrowserSimLiveReloadTest extends VPEEditorTestCase{
  private static final Logger log = Logger.getLogger(BrowserSimLiveReloadTest.class);
  private static final String TEST_PAGE_NAME = "browserSimLiveReloadPage.html";
  private BrowserSimHandler browserSimHandler = null;
  private SWTBotEditorExt editor;
  /**
   * Checks LiveReload reloading functionality in BrowserSim
   */
	public void testLiveReloadInBrowserSim(){
	  createHtmlPage(BrowserSimLiveReloadTest.TEST_PAGE_NAME);
	  editor = SWTTestExt.bot.swtBotEditorExtByTitle(BrowserSimLiveReloadTest.TEST_PAGE_NAME);
	  final String headingV0 = "<h1>LiveReload Test heading version 0</h1>";
	  final String pageTextV0 = "<!DOCTYPE html>\n" + 
	      "<html>\n" + 
	      "  <head>\n" + 
	      "    <title>Test LiveReload</title>\n" + 
	      "  </head>\n" +
	      "  <body>\n" + 
	      "    " + headingV0 + "\n" +
	      "  </body>\n" + 
	      "</html>";
	  editor.setText(pageTextV0);	      
	  editor.save();
	  SWTTestExt.eclipse.addServer(ActionItem.Server.BasicLiveReloadServer.LABEL,
	      LiveReloadServerTest.SERVER_NAME);
    servers.startServer(LiveReloadServerTest.SERVER_NAME);
	  // loads URL when BrowserSim is starting
	  String urlToLoad = "http://localhost:35729/"
	      + VPEAutoTestCase.JBT_TEST_PROJECT_NAME 
	      + "/WebContent/pages/" 
	      + BrowserSimLiveReloadTest.TEST_PAGE_NAME;
	  log.info("BrowserSim is loading url: " + urlToLoad);
		browserSimHandler = new BrowserSimHandler(urlToLoad, TimePeriod.NORMAL);
		BrowserSimAssertions.assertBrowserTextContains(browserSimHandler, headingV0);
		final String headingV1 = "<h2>LiveReload Test heading version 1</h2>";
		final String pageTextV1 = "<!DOCTYPE html>\n" + 
        "<html>\n" + 
        "  <head>\n" + 
        "    <title>Test LiveReload</title>\n" + 
        "  </head>\n" +
        "  <body>\n" + 
        "    " + headingV1 + "\n" +
        "  </body>\n" + 
        "</html>";
		editor.setText(pageTextV1);
		editor.save();
		bot.sleep(Timing.time5S());
		// LiveReload script is not injected so no changes should happen 
		BrowserSimAssertions.assertBrowserTextContains(browserSimHandler, headingV0);
		// Enable LiveReload in BrowserSim
		browserSimHandler.checkContextMenu(true,"Enable LiveReload");
		bot.sleep(Timing.time5S());
		// page has to be refreshed
		BrowserSimAssertions.assertBrowserTextContains(browserSimHandler, headingV1);
		editor.setText(pageTextV0);
		editor.save();
		bot.sleep(Timing.time5S());
    // page has to be refreshed
    BrowserSimAssertions.assertBrowserTextContains(browserSimHandler, headingV0);
    // Disable LiveReload in BrowserSim
    browserSimHandler.clickContextMenu("Enable LiveReload");
    editor.setText(pageTextV1);
    editor.save();
    bot.sleep(Timing.time5S());
    // LiveReload script is disabled no changes should happen 
    BrowserSimAssertions.assertBrowserTextContains(browserSimHandler, headingV0);
		servers.openServerEditor(LiveReloadServerTest.SERVER_NAME);
		SWTBotEditor edLiveReloadServer = bot.editorByTitle(LiveReloadServerTest.SERVER_NAME);
		edLiveReloadServer.bot().checkBox("Inject the livereload.js script in HTML pages").click();
		edLiveReloadServer.save();
		edLiveReloadServer.close();
		servers.restartServer(LiveReloadServerTest.SERVER_NAME);
		browserSimHandler.loadUrlFromAddressBar(urlToLoad, TimePeriod.NORMAL);
		BrowserSimAssertions.assertBrowserTextContains(browserSimHandler, headingV1);
		// change page and test LiveReload propagating change
    editor.setText(pageTextV0);
    editor.save();
    bot.sleep(Timing.time5S());
    BrowserSimAssertions.assertBrowserTextContains(browserSimHandler, headingV0);
  }
  /**
   * Clean up test
   * @throws Exception
   */
  @After
  public void tearDown() throws Exception {
    // close opened BrowserSim
    if (browserSimHandler != null) {
      browserSimHandler.checkContextMenu(false,"Enable LiveReload");
      browserSimHandler.close(); 
    }
    // Stops LiveReload server if is running
    if (servers != null){ 
      servers.stopServer(LiveReloadServerTest.SERVER_NAME);
      servers.deleteServer(LiveReloadServerTest.SERVER_NAME);
    }
    if (editor != null){
      editor.close();
    }

  }
}