package org.jboss.tools.switchyard.reddeer.editor;

import java.util.List;

import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.workbench.editor.DefaultEditor;
import org.jboss.tools.switchyard.reddeer.shell.DomainPropertyShell;
import org.jboss.tools.switchyard.reddeer.wizard.SecurityConfigurationWizard;

/**
 * 
 * @author apodhrad
 * 
 */
public class DomainEditor extends DefaultEditor {

	public static final String SWITCHYARD_FILE = "switchyard.xml";
	public static final String ENABLE_MESSAGE_TRACE = "Enable Message Trace";

	public DomainEditor() {
		super(SWITCHYARD_FILE);
		new DefaultCTabItem("Domain").activate();
	}

	public void setMessageTrace(boolean isMessageTrace) {
		new CheckBox(ENABLE_MESSAGE_TRACE).toggle(isMessageTrace);
	}

	public boolean isMessageTraced() {
		return new CheckBox(ENABLE_MESSAGE_TRACE).isChecked();
	}

	public void addProperty(String name, String value) {
		clickAddProperty();
		DomainPropertyShell shell = new DomainPropertyShell();
		shell.setName(name);
		shell.setValue(value);
		shell.ok();
	}

	public void removeProperty(String name) {
		selectProperty(name);
		clickRemoveProperty();
	}

	public void removeAllProperties() {
		List<TreeItem> properties = getProperties();
		for (TreeItem property : properties) {
			property.select();
			clickRemoveProperty();
		}
	}

	public String getProperty(String name) {
		List<TreeItem> items = getProperties();
		for (TreeItem item : items) {
			String configName = item.getCell(0);
			if (configName != null && configName.equals(name)) {
				return item.getCell(1);
			}
		}
		return null;
	}

	public void clickAddProperty() {
		new PushButton(0, "Add").click();
	}

	public void clickRemoveProperty() {
		new PushButton(0, "Remove").click();
	}

	public void selectProperty(String name) {
		List<TreeItem> items = getProperties();
		for (TreeItem item : items) {
			String configName = item.getCell(0);
			if (configName != null && configName.equals(name)) {
				item.select();
				break;
			}
		}
	}

	public List<TreeItem> getProperties() {
		return new DefaultTree(0).getItems();
	}

	public void addSecurityConfiguration(String name, String rolesAllowed, String runAs,
			String securityDomain, String handlerClass) {
		new DomainEditor().clickAddSecurityConfiguration();
		SecurityConfigurationWizard wizard = new SecurityConfigurationWizard();
		if (name == null) {
			throw new IllegalArgumentException("name is required, cannot be null");
		}
		wizard.setName(name);
		if (rolesAllowed != null) {
			wizard.setRolesAllowed(rolesAllowed);
		}
		if (runAs != null) {
			wizard.setRunAs(runAs);
		}
		if (securityDomain != null) {
			wizard.setSecurityDomain(securityDomain);
		}
		if (handlerClass != null) {
			wizard.selectCallbackHandlerClass(handlerClass);
		}
		wizard.finish();
	}

	public void clickAddSecurityConfiguration() {
		new PushButton(1, "Add").click();
	}

	public void clickEditSecurityConfiguration() {
		new PushButton("Edit").click();
	}

	public void editSecurityConfiguration(String name) {
		selectSecurityConfiguration(name);
		clickEditSecurityConfiguration();
	}

	public void removeSecurityConfiguration(String name) {
		selectSecurityConfiguration(name);
		clickRemoveSecurityConfiguration();
	}

	public void clickRemoveSecurityConfiguration() {
		new PushButton(1, "Remove").click();
	}

	public void selectSecurityConfiguration(String name) {
		TreeItem item = getSecurityConfiguration(name);
		if (item == null) {
			throw new RuntimeException("Cannot find security configuration with name '" + name
					+ "'");
		}
		item.select();
	}

	public TreeItem getSecurityConfiguration(String name) {
		List<TreeItem> items = getSecurityConfigurations();
		for (TreeItem item : items) {
			String configName = item.getCell(0);
			if (configName != null && configName.equals(name)) {
				return item;
			}
		}
		return null;
	}

	public List<TreeItem> getSecurityConfigurations() {
		return new DefaultTree(1).getItems();
	}

}
