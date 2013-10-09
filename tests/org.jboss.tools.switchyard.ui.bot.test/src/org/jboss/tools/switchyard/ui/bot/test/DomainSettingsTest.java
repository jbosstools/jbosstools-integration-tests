package org.jboss.tools.switchyard.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.tools.switchyard.reddeer.editor.DomainEditor;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.wizard.SecurityConfigurationWizard;
import org.jboss.tools.switchyard.reddeer.wizard.SwitchYardProjectWizard;
import org.jboss.tools.switchyard.ui.bot.test.suite.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.switchyard.ui.bot.test.suite.PerspectiveRequirement.Perspective;
import org.jboss.tools.switchyard.ui.bot.test.suite.SwitchyardSuite;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test for domain settings
 * 
 * @author apodhrad
 * 
 */
@CleanWorkspace
@Perspective(name = "Java EE")
@RunWith(SwitchyardSuite.class)
public class DomainSettingsTest {

	public static final String PROJECT = "domain-project";
	public static final String CALLBACK_CLASS = "org.switchyard.security.callback.handler.NamePasswordCallbackHandler";

	@BeforeClass
	public static void createProject() {
		closeSwitchYardEditor();
		new SwitchYardProjectWizard(PROJECT).create();
	}

	@AfterClass
	public static void deleteProject() {
		closeSwitchYardEditor();
		new ProjectExplorer().getProject(PROJECT).delete(true);
	}

	@Test
	public void messageTraceTest() {
		openSwitchYardEditor();
		assertFalse(new DomainEditor().isMessageTraced());
		new DomainEditor().setMessageTrace(true);
		new DomainEditor().close(true);
		openSwitchYardEditor();
		assertTrue(new DomainEditor().isMessageTraced());
		new DomainEditor().setMessageTrace(false);
		new DomainEditor().close(true);
		openSwitchYardEditor();
		assertFalse(new DomainEditor().isMessageTraced());
	}

	@Test
	public void propertiesTest() {
		openSwitchYardEditor();
		new DomainEditor().removeAllProperties();
		assertEquals(0, new DomainEditor().getProperties().size());
		new DomainEditor().addProperty("foo", "bar");
		new DomainEditor().close(true);

		openSwitchYardEditor();
		assertEquals(1, new DomainEditor().getProperties().size());
		assertEquals("bar", new DomainEditor().getProperty("foo"));
		new DomainEditor().removeProperty("foo");
		new DomainEditor().close(true);

		openSwitchYardEditor();
		assertEquals(0, new DomainEditor().getProperties().size());
		assertNull(new DomainEditor().getProperty("foo"));
	}

	@Test
	public void securityConfigurationTest() {
		/* Add configuration security */
		openSwitchYardEditor();
		new DomainEditor().addSecurityConfiguration("test-security", "tester, admin", "user",
				"other", "NamePasswordCallbackHandler");
		new DomainEditor().close(true);

		/* Test if it is displayed in the editor */
		openSwitchYardEditor();
		List<TreeItem> items = new DomainEditor().getSecurityConfigurations();
		assertEquals(1, items.size());
		TreeItem item = new DomainEditor().getSecurityConfiguration("test-security");
		assertEquals("test-security", item.getCell(0));
		assertEquals(CALLBACK_CLASS, item.getCell(1));
		assertEquals("tester, admin", item.getCell(2));
		assertEquals("user", item.getCell(3));
		assertEquals("other", item.getCell(4));

		/* Test if all properties are displayed in the wizard */
		new DomainEditor().editSecurityConfiguration("test-security");
		SecurityConfigurationWizard wizard = new SecurityConfigurationWizard();
		assertEquals("test-security", wizard.getName());
		assertEquals(CALLBACK_CLASS, wizard.getCallbackHandlerClass());
		assertEquals("tester, admin", wizard.getRolesAllowed());
		assertEquals("user", wizard.getRunAs());
		assertEquals("other", wizard.getSecurityDomain());
		wizard.cancel();

		/* Test if the configuration can be properly deleted */
		new DomainEditor().removeSecurityConfiguration("test-security");
		new DomainEditor().close(true);
		openSwitchYardEditor();
		items = new DomainEditor().getSecurityConfigurations();
		assertEquals(0, items.size());
		new DomainEditor().close(true);
	}

	public static void closeSwitchYardEditor() {
		try {
			new SwitchYardEditor().saveAndClose();
		} catch (Exception ex) {
			// it is ok, we just try to close switchyard.xml if it is open
		}
	}

	public static void openSwitchYardEditor() {
		Project project = new ProjectExplorer().getProject(PROJECT);
		project.getProjectItem("SwitchYard").open();
	}
}
