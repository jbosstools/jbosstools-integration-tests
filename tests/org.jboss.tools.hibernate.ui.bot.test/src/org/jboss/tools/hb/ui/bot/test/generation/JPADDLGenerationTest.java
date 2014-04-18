package org.jboss.tools.hb.ui.bot.test.generation;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.hibernate.reddeer.console.HibernateConfiguration;
import org.jboss.tools.hibernate.reddeer.test.HibernateRedDeerTest;
import org.junit.Test;

/**
 * JPA Entities can be exported into database
 * @author jpeterka
 *
 */
public class JPADDLGenerationTest extends HibernateRedDeerTest {
	
	final String prj = "jpatest35";
	final String out = "src";
	final String hbcfg = "hibernate.cfg.xml";
	
	@Test
	public void jpaDDLGenerationTest() {
		importProject("/resources/prj/hibernatelib");
		importProject("/resources/prj/" + prj);
		createHBConfigurationAndSetPersistence();
		generateDDLFromEntities();
	}
	
	private void createHBConfigurationAndSetPersistence() {
		HibernateConfiguration c = new HibernateConfiguration();
		c.setProject(prj);
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		new DefaultTreeItem(prj,"src",hbcfg).select();
	}
	
	private void generateDDLFromEntities() {
		// Select project
		ProjectExplorer p = new ProjectExplorer();
		p.selectProjects(prj);
		
		// JPA Tools -> Generate Tables From Entities
		// workaround for https://issues.jboss.org/browse/JBIDE-12796
		try {
			new ContextMenu("JPA Tools",
				"Generate Tables from Entities...").select();
		}
		catch(Exception e) {
			new ContextMenu("JPA Tools",
					"Generate Tables from Entities...");
		}

		
		// DDL Generation Dialog
		String outputDir = prj + "/" + out;
		new LabeledText("Output directory:").setText(outputDir);
		new LabeledText("File name").setText(out);

		// temporarily disabled until fixe db harming
		// bot.button(IDELabel.Button.FINISH).click();
	}

}
