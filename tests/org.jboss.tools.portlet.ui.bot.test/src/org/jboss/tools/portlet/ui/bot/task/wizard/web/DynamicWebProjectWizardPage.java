package org.jboss.tools.portlet.ui.bot.task.wizard.web;

import java.util.List;

import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.tools.portlet.ui.bot.entity.FacetDefinition;

/**
 * Wizard Page for New Dynamic Web Project Wizard
 * 
 * @author Petr Suchy
 * 
 */
public class DynamicWebProjectWizardPage extends WizardPage {

	public DynamicWebProjectWizardPage(WizardDialog wizardDialog) {
		super(wizardDialog, 0);
	}

	public void setProjectName(String projectName) {
		new LabeledText("Project name:").setText(projectName);
	}

	public void setWebModuleVersion(String webModuleVersion) {
		new DefaultCombo(1)
				.setSelection(webModuleVersion);
	}

	public void setServerName(String serverName) {
		new DefaultCombo(0).setSelection(serverName);
	}

	public void setFacets(List<FacetDefinition> facets) {
		new PushButton("Modify...").click();
		List<TreeItem> items = new DefaultTree().getAllItems();
		for (FacetDefinition facet : facets) {
			for (TreeItem item : items) {
				if (item.getText().equals(facet.getName())) {
					item.setChecked(true);
					changeVersion(facet, item);
					break;
				}
			}
		}
		new PushButton("OK").click();
	}
	
	private void changeVersion(FacetDefinition facet, TreeItem facetTreeItem) {
		if (facet.getVersion() != null){
			facetTreeItem.select();
			new ContextMenu("Change Version...").select();
			new LabeledCombo("Version:").setSelection(facet.getVersion());
			new PushButton("OK").click();
		}
	}

}