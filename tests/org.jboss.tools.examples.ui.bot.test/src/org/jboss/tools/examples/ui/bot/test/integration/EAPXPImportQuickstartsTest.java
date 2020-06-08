/*******************************************************************************
 * Copyright (c) 2020 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.examples.ui.bot.test.integration;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.eclipse.reddeer.common.exception.WaitTimeoutExpiredException;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.eclipse.condition.ConsoleHasText;
import org.eclipse.reddeer.eclipse.ui.browser.WebBrowserView;
import org.eclipse.reddeer.eclipse.ui.console.ConsoleView;
import org.eclipse.reddeer.junit.internal.runner.ParameterizedRequirementsRunnerFactory;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.UseParametersRunnerFactory;

/**
 * This testsuite consist of one test. It imports all examples (location of
 * examples is defined by system property "examplesLocation") and checks for
 * errors and warnings and deploys on server.
 * 
 * @author jkopriva
 *
 */

@RunWith(RedDeerSuite.class)
@UseParametersRunnerFactory(ParameterizedRequirementsRunnerFactory.class)
@JBossServer(state = ServerRequirementState.RUNNING)
public class EAPXPImportQuickstartsTest extends AbstractImportQuickstartsTest {
	public static final String SERVER_NAME = "Enterprise Application Platform";
	public static final String BLACKLIST_FILE = "resources/servers/eap-blacklist";
	public static final String BLACKLIST_ERRORS_REGEXES_FILE = "resources/servers/eap-xp-blacklist-test-errors-regexes.json";
	public static final String URLS_FILE = "resources/servers/eap-xp-quickstarts-urls.json";
	protected JSONObject urlsFileContents;

	@Parameters(name = "{0}")
	public static Collection<Quickstart> data() {
		return createQuickstartsList();
	}

	@Parameter
	public Quickstart qstart;

	/*
	 * Main tests. Imports quickstart as maven project, performs checks and deploy
	 * on server.
	 */
	@Test
	public void quickstartTest() {
		runQuickstarts(qstart, SERVER_NAME, BLACKLIST_FILE, BLACKLIST_ERRORS_REGEXES_FILE);
	}

	/**
	 * Checks whether is project deployed properly.
	 * 
	 * @param projectName
	 * @param serverNameLabel
	 */
	@Override
	public void checkDeployedProject(Quickstart qstart, String serverNameLabel) {
		this.urlsFileContents = loadURLsForQuickstartsFromFile(URLS_FILE);
		// Close Opened browser
		closeBrowser();
		ConsoleView console = new ConsoleView();
		console.open();
		new WaitUntil(new ConsoleHasText(console, "Deployed"), TimePeriod.LONG);

		WebBrowserView browser = new WebBrowserView();
		browser.open();
		if (!(urlsFileContents == null
				|| (!urlsFileContents.containsKey(qstart.getName()) && !urlsFileContents.containsKey("*")))) {

			String quickstartURL = "";
			if (urlsFileContents.containsKey(qstart.getName())) {
				quickstartURL = (String) urlsFileContents.get(qstart.getName()).toString();
			}
			browser.openPageURL(quickstartURL);
		}

		try {
			new WaitUntil(new BrowserIsnotEmpty(browser), TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException e) {
			// try to refresh browser and wait one more time.
			browser.refreshPage();
			new WaitUntil(new BrowserIsnotEmpty(browser), TimePeriod.LONG);
		}

		assertNotEquals("", browser.getText());
		browser.close();

		checkConsoleForException();

	}

	public static JSONObject loadURLsForQuickstartsFromFile(String urlsFile) {
		JSONObject urlsFileContents = null;

		if (!urlsFile.isEmpty()) {
			String pathToFile = "";
			try {
				pathToFile = new File(urlsFile).getCanonicalPath();
				JSONParser parser = new JSONParser();
				urlsFileContents = (JSONObject) parser.parse(new FileReader(pathToFile));
			} catch (IOException ex) {
				fail("urls file not found! Path is: " + pathToFile);
			} catch (ParseException e) {
				fail("ParseException: unable to parse file at path is: " + pathToFile);
			}
		}

		return urlsFileContents;
	}

	class BrowserIsnotEmpty extends AbstractWaitCondition {

		WebBrowserView browser;

		public BrowserIsnotEmpty(WebBrowserView browser) {
			this.browser = browser;
		}

		public boolean test() {
			return !browser.getText().equals("");
		}

		public String description() {
			return "Browser is empty!";
		}
	}

}