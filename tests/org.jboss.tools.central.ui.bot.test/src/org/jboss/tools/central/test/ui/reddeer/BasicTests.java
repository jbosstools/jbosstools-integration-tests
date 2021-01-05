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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.common.matcher.RegexMatcher;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.core.matcher.WithTextMatcher;
import org.eclipse.reddeer.eclipse.ui.browser.BrowserEditor;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.browser.InternalBrowser;
import org.eclipse.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.central.reddeer.api.JavaScriptHelper;
import org.jboss.tools.central.reddeer.wait.CentralIsLoaded;
import org.jboss.tools.quarkus.reddeer.wizard.QuarkusWizard;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author rhopp
 *
 */

@RunWith(RedDeerSuite.class)
public class BasicTests {

	private static final String CONTACTS_MOBILE_BASIC = "contacts-mobile-basic";
	private static final String KITCHENSINK_JSP = "kitchensink-jsp";
	private static final String CENTRAL_LABEL = "Red Hat Central";
	private static Logger log = new Logger(HTML5Parameterized.class);
	private static InternalBrowser centralBrowser;
	private static JavaScriptHelper jsHelper = JavaScriptHelper.getInstance();

	@Before
	public void setup() {
		try {
			new DefaultEditor(CENTRAL_LABEL).close();
		} catch (Exception e) {
		}
		new DefaultToolItem(new WorkbenchShell(), CENTRAL_LABEL).click();
		// activate central editor
		new DefaultEditor(CENTRAL_LABEL);
		new WaitUntil(new CentralIsLoaded());
		centralBrowser = new InternalBrowser();

		jsHelper.setBrowser(centralBrowser);
	}

	@Test
	public void addToolsButton() {
		log.step("Clicking AddToolsButton");
		centralBrowser.execute("$(\"#addtools\").click()");
		assertTrue(new DefaultCTabItem("Software/Update").isShowing());
		new DefaultCTabItem("Getting Started").activate();
	}

	@Test
	public void learnAboutRedHatButton() {
		centralBrowser.execute("$(\'a[href=\"https://developers.redhat.com/\"]\').get( 0 ).click()");
		BrowserEditor be = new BrowserEditor(new WithTextMatcher(new RegexMatcher(".*Red.*Hat.*Developer.*")));
		assertTrue("The url in the browser has to be https://developers.redhat.com/.",
				be.getPageURL().equals("https://developers.redhat.com/"));
		be.close();
	}

	@Test
	public void archetypesArePresent() {
		List<String> wizards = Arrays.asList(jsHelper.getWizards());
		assertTrue(wizards.contains("OpenShift Application"));
		assertTrue(wizards.contains("Maven Project"));
		assertTrue(wizards.contains("Launcher Application"));
		assertTrue(wizards.contains("Quarkus Project"));
	}

	@Test
	public void searchIsWorking() {
		jsHelper.searchFor(KITCHENSINK_JSP);
		String[] examples = jsHelper.getExamples();
		assertTrue("At least one example should be found", examples.length > 0);
		for (String example : examples) {
			String description = jsHelper.getDescriptionForExample(example);
			assertNotNull("Description should not be null.", description);
			assertFalse("Description should not be empty", description.isEmpty());
			String[] labels = jsHelper.getLabelsForExample(example);
			assertTrue("There should be at least one label", labels.length > 0);
		}
		jsHelper.clearSearch();
	}

	@Test
	public void newExampleWizardCanBeEnvoked() {
		jsHelper.searchFor(CONTACTS_MOBILE_BASIC);
		jsHelper.clickExample(CONTACTS_MOBILE_BASIC);
		new DefaultShell("New Project Example").close();
		jsHelper.clearSearch();
	}

	@Test
	public void launcherApplicationWizardCanBeEnvoked() {
		jsHelper.clickWizard("Launcher Application");
		new DefaultShell("New Launcher project").close();
	}

	@Test
	public void quarkusProjectWizardCanBeEnvoked() {
		jsHelper.clickWizard("Quarkus Project");
		new WaitUntil(new ShellIsAvailable("New Quarkus project"));
		QuarkusWizard qw = new QuarkusWizard();
		qw.cancel();
	}
}
