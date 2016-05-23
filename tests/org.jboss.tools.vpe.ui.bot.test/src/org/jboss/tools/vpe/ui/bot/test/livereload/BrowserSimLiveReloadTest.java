/*******************************************************************************
 * Copyright (c) 2013 - 2016 Red Hat, Inc.
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
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.jboss.tools.vpe.ui.bot.test.browsersim.BrowserSimAssertions;
import org.jboss.tools.vpe.ui.bot.test.editor.VPEEditorTestCase;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardDialog;
import org.jboss.ide.eclipse.as.reddeer.server.wizard.page.NewServerWizardPageWithErrorCheck;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerState;
import org.jboss.reddeer.swt.impl.button.LabeledCheckBox;
import org.jboss.reddeer.workbench.api.Editor;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.browsersim.reddeer.BrowserSimHandler;
import org.junit.After;
import org.junit.Test;

/**
 * Tests LiveReload functionality in BrowserSim
 * 
 * @author Vladimir Pakan
 *
 */
public class BrowserSimLiveReloadTest extends VPEEditorTestCase {
	private static final Logger log = Logger.getLogger(BrowserSimLiveReloadTest.class);
	private static final String TEST_PAGE_NAME = "browserSimLiveReloadPage.html";
	private static final String SERVER_NAME = "LiveReloadTestServer";
	private BrowserSimHandler browserSimHandler = null;
	private TextEditor editor;
	private ServersView serversView;

	/**
	 * Checks LiveReload reloading functionality in BrowserSim
	 */
	@Test
	public void testLiveReloadInBrowserSim() {
		createHtmlPage(BrowserSimLiveReloadTest.TEST_PAGE_NAME);
		editor = new TextEditor(BrowserSimLiveReloadTest.TEST_PAGE_NAME);
		final String headingV0 = "<h1>LiveReload Test heading version 0</h1>";
		final String pageTextV0 = "<!DOCTYPE html>\n" + "<html>\n" + "  <head>\n"
				+ "    <title>Test LiveReload</title>\n" + "  </head>\n" + "  <body>\n" + "    " + headingV0 + "\n"
				+ "  </body>\n" + "</html>";
		editor.setText(pageTextV0);
		editor.save();
		serversView = new ServersView();
		serversView.open();
		NewServerWizardDialog serverWizard = new NewServerWizardDialog();
		try {
			serverWizard.open();
			NewServerWizardPageWithErrorCheck sp = new NewServerWizardPageWithErrorCheck();
			sp.selectType("Basic", "LiveReload Server");
			sp.setName(BrowserSimLiveReloadTest.SERVER_NAME);
			sp.checkErrors();
			serverWizard.finish();
		} catch (RuntimeException e) {
			serverWizard.cancel();
			throw e;
		} catch (AssertionError e) {
			serverWizard.cancel();
			throw e;
		}
		Server liveReloadServer = serversView.getServer(BrowserSimLiveReloadTest.SERVER_NAME);
		liveReloadServer.start();
		// loads URL when BrowserSim is starting
		String urlToLoad = "http://localhost:35729/" + VPEAutoTestCase.JBT_TEST_PROJECT_NAME + "/WebContent/pages/"
				+ BrowserSimLiveReloadTest.TEST_PAGE_NAME;
		log.info("BrowserSim is loading url: " + urlToLoad);
		browserSimHandler = new BrowserSimHandler(urlToLoad, TimePeriod.NORMAL);
		BrowserSimAssertions.assertBrowserTextContains(browserSimHandler, headingV0);
		browserSimHandler.checkContextMenu(false, "Enable LiveReload");
		final String headingV1 = "<h2>LiveReload Test heading version 1</h2>";
		final String pageTextV1 = "<!DOCTYPE html>\n" + "<html>\n" + "  <head>\n"
				+ "    <title>Test LiveReload</title>\n" + "  </head>\n" + "  <body>\n" + "    " + headingV1 + "\n"
				+ "  </body>\n" + "</html>";
		editor.setText(pageTextV1);
		editor.save();
		AbstractWait.sleep(TimePeriod.getCustom(5));
		// LiveReload script is not injected so no changes should happen
		BrowserSimAssertions.assertBrowserTextContains(browserSimHandler, headingV0);
		// Enable LiveReload in BrowserSim
		browserSimHandler.checkContextMenu(true, "Enable LiveReload");
		AbstractWait.sleep(TimePeriod.getCustom(5));
		// page has to be refreshed
		BrowserSimAssertions.assertBrowserTextContains(browserSimHandler, headingV1);
		editor.setText(pageTextV0);
		editor.save();
		AbstractWait.sleep(TimePeriod.getCustom(5));
		// page has to be refreshed
		BrowserSimAssertions.assertBrowserTextContains(browserSimHandler, headingV0);
		// Disable LiveReload in BrowserSim
		browserSimHandler.clickContextMenu("Enable LiveReload");
		editor.setText(pageTextV1);
		editor.save();
		AbstractWait.sleep(TimePeriod.getCustom(5));
		// LiveReload script is disabled no changes should happen
		BrowserSimAssertions.assertBrowserTextContains(browserSimHandler, headingV0);
		liveReloadServer.open();
		Editor serverEditor = new DefaultEditor(BrowserSimLiveReloadTest.SERVER_NAME);
		new LabeledCheckBox("Inject the livereload.js script in HTML pages").toggle(true);
		serverEditor.save();
		serverEditor.close();
		liveReloadServer.restart();
		browserSimHandler.loadUrlFromAddressBar(urlToLoad, TimePeriod.NORMAL);
		BrowserSimAssertions.assertBrowserTextContains(browserSimHandler, headingV1);
		// change page and test LiveReload propagating change
		editor.setText(pageTextV0);
		editor.save();
		AbstractWait.sleep(TimePeriod.getCustom(5));
		BrowserSimAssertions.assertBrowserTextContains(browserSimHandler, headingV0);
	}

	/**
	 * Clean up test
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		// close opened BrowserSim
		if (browserSimHandler != null) {
			browserSimHandler.checkContextMenu(false, "Enable LiveReload");
			browserSimHandler.close();
		}
		// Stops LiveReload server if is running
		if (serversView != null) {
			Server liveReloadServer = serversView.getServer(BrowserSimLiveReloadTest.SERVER_NAME);
			if (!liveReloadServer.getLabel().getState().equals(ServerState.STOPPED)){
				liveReloadServer.stop();
			}
			liveReloadServer.delete();
		}
		if (editor != null) {
			editor.close();
		}

	}
}
