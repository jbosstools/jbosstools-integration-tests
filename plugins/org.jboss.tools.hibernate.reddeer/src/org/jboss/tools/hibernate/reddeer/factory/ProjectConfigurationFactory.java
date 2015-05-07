package org.jboss.tools.hibernate.reddeer.factory;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.wst.common.project.facet.ui.FacetsPropertyPage;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.uiforms.impl.hyperlink.DefaultHyperlink;
import org.jboss.tools.hibernate.reddeer.dialog.ProjectFacetsPage;
import org.jboss.tools.hibernate.reddeer.dialog.ProjectPropertyDialog;
import org.jboss.tools.hibernate.reddeer.editor.JpaXmlEditor;
import org.jboss.tools.hibernate.reddeer.wizard.JPAFacetWizardPage;


/**
 * Project configuration factory provides common routines for setting Hibernate
 * nature for selected proejct
 * 
 * @author jpeterka
 *
 */
public class ProjectConfigurationFactory {

	private static final Logger log = Logger.getLogger(ProjectConfigurationFactory.class);
	
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
	 * Sets JPA project facets for given database configuration with JPA 2.1
	 * @param prj given project
	 * @param cfg given database configuration
	 */
	public static void setProjectFacetForDB(String prj,	DatabaseConfiguration cfg) {
		setProjectFacetForDB(prj,cfg,"2.1");
	}
	
	/**
	 * Sets JPA project facets for given database configuration
	 * @param prj given project
	 * @param cfg given database configuration
	 * @param jpaVersion JPA version (2.0 or 2.1 is supported)
	 */
	public static void setProjectFacetForDB(String prj,
			
		DatabaseConfiguration cfg, String jpaVersion) {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.selectProjects(prj);
		Project project = pe.getProject(prj);
		
		ProjectPropertyDialog prjDlg = new ProjectPropertyDialog(prj);
		prjDlg.open();
		prjDlg.select("Project Facets");
		
		FacetsPropertyPage pp = new FacetsPropertyPage(project);
		pp.selectFacet("JPA");
		pp.selectVersion("JPA",jpaVersion);
		addFurtherJPAConfiguration(jpaVersion);
				
		prjDlg.ok();
		
		prjDlg.open();		
		prjDlg.select("JPA");
 
		JPAFacetWizardPage jpaPage = new JPAFacetWizardPage();
		
		jpaPage.setConnectionProfile(cfg.getProfileName());
		jpaPage.setAutoDiscovery(true);
		prjDlg.ok();
		
		checkPersistenceXML(prj);
	}	

	/**
	 * Check persistence.xml 
	 * @param prj project name
	 */
	public static void checkPersistenceXML(String prj) {
		log.info("Open persistence xml file");
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		DefaultTreeItem i = new DefaultTreeItem(prj, "JPA Content", "persistence.xml");
		i.doubleClick();

		log.info("In editor set some hibernate properties on hibernate tab");
		JpaXmlEditor pexml = new JpaXmlEditor();

		String sourceText = pexml.getSourceText();

		pexml.close();

		assertTrue("persistence.xml cannot be empty", sourceText.length() > 0);
	}
	
	private static void addFurtherJPAConfiguration(String jpaVersion) {	

		DefaultHyperlink hyperlink = new DefaultHyperlink();
		hyperlink.activate();	
						
		new WaitUntil(new ShellWithTextIsActive("Modify Faceted Project"));
		DefaultGroup group = new DefaultGroup("Platform");
				
		new DefaultCombo(group).setSelection("Hibernate (JPA " + jpaVersion + ")");
		
		
		new LabeledCombo("Type:").setSelection("Disable Library Configuration");
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsActive("Modify Faceted Project"));
	}
	
	


}
