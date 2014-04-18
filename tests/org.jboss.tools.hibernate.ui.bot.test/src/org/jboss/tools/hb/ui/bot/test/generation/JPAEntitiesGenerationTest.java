package org.jboss.tools.hb.ui.bot.test.generation;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.hibernate.reddeer.console.HibernateConfiguration;
import org.jboss.tools.hibernate.reddeer.test.HibernateRedDeerTest;
import org.junit.Test;

public class JPAEntitiesGenerationTest extends HibernateRedDeerTest {
	
	final String prj = "jpatest35";
	final String out = "src";
	final String hbcfg = "hibernate.cfg.xml";
	
	@Test
	public void generateJPAEntitiesFromDB() {
		importProject("/resources/prj/" + prj);
		createHBConfigurationAndSetPersistence();
		generateEntitiesFromDB();
	}

	private void createHBConfigurationAndSetPersistence() {
		HibernateConfiguration c = new HibernateConfiguration();
		c.setProject(prj);
		ProjectExplorer p = new ProjectExplorer();
		p.open();
		new DefaultTreeItem(prj,"src", hbcfg).select();
	}

	private void generateEntitiesFromDB() {
		ProjectExplorer p = new ProjectExplorer();
		p.open();
		p.selectProjects(prj);

		// JPA Tools -> Generate Tables From Entities
		// workaround for https://issues.jboss.org/browse/JBIDE-12796
		try {
			new ContextMenu("JPA Tools","Generate Entities from Tables...");
		}
		catch(Exception e) {
			new ContextMenu("JPA Tools","Generate Entities from Tables...");
		}

		// DDL Generation Dialog
		String outputDir = prj + "/" + out;
		new LabeledText("Output directory:").setText(outputDir);
		new LabeledText("Package:").setText("org.gen");

		new FinishButton().click();			
	}
}