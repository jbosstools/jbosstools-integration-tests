package org.jboss.tools.hibernate.factory;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.tools.hibernate.reddeer.page.GenerateEntitiesWizardPage;
import org.jboss.tools.hibernate.reddeer.wizard.GenerateEntitiesWizard;

public class EntityGenerationFactory {

	public static void generateJPAEntities(DatabaseConfiguration cfg, String prj, String pkg, String hbVersion) {
		
    	ProjectExplorer pe = new ProjectExplorer();
    	pe.open();
    	pe.selectProjects(prj);
    	
    	GenerateEntitiesWizard w = new GenerateEntitiesWizard();
    	w.open();
    		
    	GenerateEntitiesWizardPage p = new GenerateEntitiesWizardPage();
    	p.setUseConsole(false);
    	p.setPackage(pkg);
    	p.setHibernateVersion(hbVersion);
    	p.setDatabaseConnection(cfg.getProfileName());

    	w.finish();		
	}

}
