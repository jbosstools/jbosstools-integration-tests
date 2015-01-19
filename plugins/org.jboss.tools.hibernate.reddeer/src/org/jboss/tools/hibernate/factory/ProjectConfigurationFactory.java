package org.jboss.tools.hibernate.factory;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.tools.hibernate.reddeer.dialog.ProjectFacetsPage;
import org.jboss.tools.hibernate.reddeer.dialog.ProjectPropertyDialog;
import org.jboss.tools.hibernate.reddeer.wizard.JPAFacetWizardPage;

public class ProjectConfigurationFactory {


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
	public static void setProjectFacetForDB(String prj,
		DatabaseConfiguration cfg) {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.selectProjects(prj);
		ProjectPropertyDialog prjDlg = new ProjectPropertyDialog(prj);
		prjDlg.open();    	
		prjDlg.select("JPA");
		JPAFacetWizardPage jpaPage = new JPAFacetWizardPage();
		
		jpaPage.setConnectionProfile(cfg.getProfileName());
		prjDlg.ok();	
	}	
}
