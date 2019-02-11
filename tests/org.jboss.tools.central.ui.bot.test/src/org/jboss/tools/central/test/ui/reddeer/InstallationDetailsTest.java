/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.central.test.ui.reddeer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.common.matcher.RegexMatcher;
import org.eclipse.reddeer.common.platform.RunningPlatform;
import org.eclipse.reddeer.common.util.Display;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.matcher.WithTextMatcher;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.swt.api.CTabFolder;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.eclipse.ui.internal.dialogs.AboutDialog;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author vprusa
 *
 */

@RunWith(RedDeerSuite.class)
public class InstallationDetailsTest {

	private static String HELP_BUTTON = "Help";

	private static String ABOUT_MENU_BUTTON = ".*About Red Hat.*Developer Studio.*";
	private static String MODAL_DIALOG_ABOUT_DEVSTUDIO_INSTALLATION_DETAILS_BUTTON = ".*Installation Details.*";
	private static String MODAL_DIALOG_ABOUT_DEVSTUDIO_INSTALLATION_DETAILS_TITLE = ".*Red Hat.*Developer Studio Installation Details.*";
	private static String MODAL_DIALOG_CONFIG_MENU_BUTTON = "Configuration";

	private Logger log = new Logger(this.getClass());

	private DefaultShell about;
	private DefaultShell installationDetails;

	// set on execution
	private String jbossToolsVersion = "";

	/**
	 * this class handles downloading and operations on configuration properties
	 * file located at
	 * http://download.jboss.org/jbosstools/configuration/ide-config.properties and
	 * local configuration properties Also:
	 * http://www.mkyong.com/java/how-to-get-url-content-in-java/
	 * 
	 */
	class ConfigPropertiesHelper {

		private final String urlStr = "http://download.jboss.org/jbosstools/configuration/ide-config.properties";

		private String localText = "";
		private String remoteText = "";

		private Logger log = new Logger(this.getClass());

		public ConfigPropertiesHelper(String localText) {
			this.localText = localText;

			URL url;

			try {
				// get URL content
				url = new URL(urlStr);
				URLConnection conn = url.openConnection();

				// open the stream and put it into BufferedReader
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				// loading to array list because the file is not too long
				StringBuilder b = new StringBuilder();
				br.lines().forEach(line -> b.append(line + "\n"));
				remoteText = b.toString();
				br.close();

				log.info("Config file laoded from URL: " + urlStr);
				log.debug("Config file content: \n" + remoteText);

			} catch (MalformedURLException e) {
				log.error("Could not get content at URL: " + urlStr);
				e.printStackTrace();
			} catch (IOException e) {
				log.error("IOException occured while downlaoding configuration file at URL: " + urlStr);
				e.printStackTrace();
			}

		}

		public boolean isRegexValid(String regex) {
			return remoteText.matches("(?s).*" + regex + ".*") && localText.matches("(?s).*" + regex + ".*");
		}

	}

	// substring {majorVersion} is replaced with current major version
	private final String[] regexArray = {
			"jboss\\.discovery\\.directory\\.url\\|devstudio\\|{majorVersion}.*devstudio-directory\\.xml",
			"jboss\\.discovery\\.site\\.url\\|devstudio\\|{majorVersion}.*=https://devstudio\\.redhat\\.com/{majorVersion}.*/updates/",
			"jboss\\.discovery\\.earlyaccess\\.site\\.url\\|devstudio\\|{majorVersion}.*=https://devstudio\\.redhat\\.com/{majorVersion}.*\\.earlyaccess/",
			"jboss\\.discovery\\.earlyaccess\\.list\\.url\\|devstudio\\|{majorVersion}.*=https://devstudio\\.redhat\\.com/{majorVersion}.*\\.earlyaccess/.*/devstudio-earlyaccess\\.properties",
			"jboss\\.central\\.webpage\\.url\\|devstudio\\|{majorVersion}.*=.*jbosstools-central-webpage.*zip"//,
			//"jboss\\.discovery\\.site\\.integration-stack\\.url\\|devstudio\\|{majorVersion}.*=https://devstudio\\.redhat\\.com/{majorVersion}.*integration-stack/discovery/",
			//"jboss\\.discovery\\.earlyaccess\\.site\\.integration-stack\\.url\\|devstudio\\|{majorVersion}.*=https://devstudio\\.redhat\\.com/{majorVersion}.*/integration-stack/discovery/earlyaccess/",
			//"jboss\\.discovery\\.site\\.integration-stack-sap\\.url\\|devstudio\\|{majorVersion}.*=https://devstudio\\.redhat\\.com/{majorVersion}.*/integration-stack/extras/" 
			};

	public String getJbossToolsMajorVersion() {
		return jbossToolsVersion.substring(0, jbossToolsVersion.indexOf('.'));
	}

	@Before
	public void setup() {
		log.info("Navigate to installation details");
		WorkbenchShell ws = new WorkbenchShell();
		ws.maximize();
		// for OSX (MacOS) use eclipse API to open About dialog
		if (RunningPlatform.isOSX()) {
			log.info("Open " + HELP_BUTTON + " -> " + ABOUT_MENU_BUTTON + "directly on Mac OSX");
			Display.asyncExec(new Runnable() {
				@Override
				public void run() {
					@SuppressWarnings("restriction")
					AboutDialog dialog = new AboutDialog(ws.getSWTWidget());
					dialog.open();
				}
			});
		} else {
			new ShellMenuItem(new WithTextMatcher(new RegexMatcher(HELP_BUTTON)), new WithTextMatcher(new RegexMatcher(ABOUT_MENU_BUTTON))).select();
		}
		about = new DefaultShell(new WithTextMatcher(new RegexMatcher(ABOUT_MENU_BUTTON)));

		// get styled text from help
		DefaultStyledText dt = new DefaultStyledText();
		String text = dt.getText();

		// TODO use
		// org.eclipse.core.runtime.Platform.getBundle("org.jbosstools").getVersion();
		int versionIndex = 0;
		jbossToolsVersion = text.substring(versionIndex = text.indexOf("Version:") + 9,
				text.indexOf("Build id", ++versionIndex));
		log.info("JBossTools version: " + jbossToolsVersion);

		assertFalse("The JBossTools version value is empty!", jbossToolsVersion.isEmpty());

		new PushButton(new WithTextMatcher(new RegexMatcher(MODAL_DIALOG_ABOUT_DEVSTUDIO_INSTALLATION_DETAILS_BUTTON))).click();
		// validate that shell opened
		installationDetails = new DefaultShell(new WithTextMatcher(new RegexMatcher(MODAL_DIALOG_ABOUT_DEVSTUDIO_INSTALLATION_DETAILS_TITLE)));

		CTabFolder tabFolder = new DefaultCTabItem(MODAL_DIALOG_CONFIG_MENU_BUTTON).getFolder();

		String[] labels = tabFolder.getTabItemLabels();
		Boolean isTabPresent = Arrays.stream(labels).anyMatch(MODAL_DIALOG_CONFIG_MENU_BUTTON::equals);
		assertTrue("Tab item '" + MODAL_DIALOG_CONFIG_MENU_BUTTON + "' is missing", isTabPresent);

		new DefaultCTabItem(MODAL_DIALOG_CONFIG_MENU_BUTTON).activate();
		new WaitWhile(new JobIsRunning(new RegexMatcher("Fetching configuration"), true), TimePeriod.LONG);
	}

	// JBDS-5064
	@Test
	public void validateLinksWithGlobalConfig() {
		log.info("Navigate to '" + MODAL_DIALOG_CONFIG_MENU_BUTTON + "' and validate links with global config");
		DefaultText dt = new DefaultText(installationDetails);
		String localText = dt.getText();
		log.debug("Text of global config:\n" + localText);
		ConfigPropertiesHelper rcph = new ConfigPropertiesHelper(localText);

		// note that the version substring may change with devstudio version
		String majorVersion = getJbossToolsMajorVersion();
		log.info("Major version: " + majorVersion);

		Arrays.stream(regexArray).forEach(regexRaw -> {
			String regex = regexRaw.replace("{majorVersion}", majorVersion);
			log.info("Validating local and remote config with regex: '" + regex + "'");
			boolean result = rcph.isRegexValid(regex);
			assertTrue("Unable to match regex '" + regex + "' to either local or remote config with result: " + result,
					result);
		});

		log.info("Finished: validateLinksWithGlobalConfig");
	}

	@After
	public void finish() {
		log.info("Closing shells: " + installationDetails.getText() + " and " + about.getText());
		installationDetails.close();
		about.close();
	}

}
