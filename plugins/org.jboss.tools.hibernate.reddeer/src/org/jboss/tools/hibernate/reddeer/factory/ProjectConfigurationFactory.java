package org.jboss.tools.hibernate.reddeer.factory;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.uiforms.impl.hyperlink.DefaultHyperlink;
import org.jboss.tools.hibernate.reddeer.dialog.ProjectFacetsPage;
import org.jboss.tools.hibernate.reddeer.dialog.ProjectPropertyDialog;
import org.jboss.tools.hibernate.reddeer.entity.FacetDefinition;
import org.jboss.tools.hibernate.reddeer.entity.Facets;
import org.jboss.tools.hibernate.reddeer.wizard.JPAFacetWizardPage;


/**
 * Project configuration factory provides common routines for setting Hibernate
 * nature for selected proejct
 * 
 * @author jpeterka
 *
 */
public class ProjectConfigurationFactory {

	/**
	 * Convert project to facet form
	 * @param prj given project name
	 */
	public static void convertProjectToFacetsForm(String prj) {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.selectProjects(prj);
		ProjectPropertyDialog prjDlg = new ProjectPropertyDialog(prj);
		prjDlg.open();    	
		prjDlg.select("Project Facets");
		ProjectFacetsPage facetsPage = new ProjectFacetsPage(prj);
		facetsPage.convertToFacetedForm();		
		prjDlg.ok();
	}
	
	/**
	 * Sets JPA project facets for given database configuration
	 * @param prj given project
	 * @param cfg given database configuration
	 */
	public static void setProjectFacetForDB(String prj,
			
		DatabaseConfiguration cfg) {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.selectProjects(prj);
		ProjectPropertyDialog prjDlg = new ProjectPropertyDialog(prj);
		prjDlg.open();
		prjDlg.select("Project Facets");
		
		List<FacetDefinition> fd = new ArrayList<FacetDefinition>();
		fd.add(Facets.JPA);		
		setProjectFacets(fd);
		prjDlg.ok();
		
		prjDlg.open();		
		prjDlg.select("JPA");
 
		JPAFacetWizardPage jpaPage = new JPAFacetWizardPage();
		
		jpaPage.setConnectionProfile(cfg.getProfileName());
		jpaPage.setAutoDiscovery(true);
		prjDlg.ok();	
	}	
	
	private static void setProjectFacets(List<FacetDefinition> facets) {
		List<TreeItem> items = new DefaultTree(1).getAllItems();
		for (FacetDefinition facet : facets) {
			for (TreeItem item : items) {
				if (item.getText().equals(facet.getName())) {
					item.setChecked(true);
					changeVersion(facet, item);
					break;
				}
			}		
		}

		DefaultHyperlink hyperlink = new DefaultHyperlink();
		hyperlink.activate();	
						
		new WaitUntil(new ShellWithTextIsActive("Modify Faceted Project"));
		DefaultGroup group = new DefaultGroup("Platform");
		new DefaultCombo(group).setSelection("Hibernate (JPA 2.1)");
		
		
		new LabeledCombo("Type:").setSelection("Disable Library Configuration");
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsActive("Modify Faceted Project"));
	}
	
	
	private static void changeVersion(FacetDefinition facet, TreeItem facetTreeItem) {
		if (facet.getVersion() != null){
			facetTreeItem.select();
			new ContextMenu("Change Version...").select();
			new LabeledCombo("Version:").setSelection(facet.getVersion());
			new PushButton("OK").click();
		}
	}

}
