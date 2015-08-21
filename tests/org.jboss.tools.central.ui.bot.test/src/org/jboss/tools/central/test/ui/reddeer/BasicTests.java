package org.jboss.tools.central.test.ui.reddeer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.eclipse.ui.browser.BrowserEditor;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.central.reddeer.api.JavaScriptHelper;
import org.jboss.tools.central.reddeer.wait.CentralIsLoaded;
import org.junit.Before;
import org.junit.Test;

public class BasicTests {

	private static final String CONTACTS_MOBILE_BASIC = "contacts-mobile-basic";
	private static final String KITCHENSINK_JSP = "kitchensink-jsp";
	private static Logger log = new Logger(HTML5Parameterized.class);
	private static InternalBrowser centralBrowser;
	private static JavaScriptHelper jsHelper = JavaScriptHelper.getInstance();
	
	@Before
	public void setup() {
		new DefaultToolItem(new WorkbenchShell(), "JBoss Central").click();
		// activate central editor
		new DefaultEditor("JBoss Central");
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
	public void getStartedButton() {
		centralBrowser.execute("$(\'a[href=\"http://www.jboss.org/get-started/\"]\').click()");
		new BrowserEditor("Get started with JBoss products").close();
		;
	}

	@Test
	public void catButton() {
		centralBrowser.execute("$(\'a[href=\"http://tools.jboss.org/cat/\"]\').click()");
		new BrowserEditor("JBoss Tools - Community Acceptance Testing (CAT)").close();
		;
	}

	@Test
	public void archetypesArePresent() {
		List<String> wizards = Arrays.asList(jsHelper.getWizards());
		assertTrue(wizards.contains("HTML5 Project"));
		assertTrue(wizards.contains("OpenShift Application"));
		assertTrue(wizards.contains("AngularJS Forge"));
		assertTrue(wizards.contains("Java EE Web Project"));
		assertTrue(wizards.contains("Maven Project"));
		assertTrue(wizards.contains("Hybrid Mobile Project"));
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
	public void HTML5ProjectWizardCanBeEnvoked(){
		jsHelper.clickWizard("HTML5 Project");
		new DefaultShell("New Project Example").close();
	}
	
	@Test
	public void newExampleWizardCanBeEnvoked(){
		jsHelper.searchFor(CONTACTS_MOBILE_BASIC);
		jsHelper.clickExample(CONTACTS_MOBILE_BASIC);
		new DefaultShell("New Project Example").close();
		jsHelper.clearSearch();
	}
}
