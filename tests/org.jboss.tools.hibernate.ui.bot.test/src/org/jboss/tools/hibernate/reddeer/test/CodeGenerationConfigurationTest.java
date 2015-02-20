package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.fail;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.requirements.db.DatabaseRequirement;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.table.DefaultTableItem;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.impl.tree.TreeItemNotFoundException;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.hibernate.reddeer.factory.HibernateToolsFactory;
import org.jboss.tools.hibernate.reddeer.perspective.HibernatePerspective;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Test prepares project and generate entities via Hibernate Code Generation Configuration 
 * @author Jiri Peterka
 */
@RunWith(RedDeerSuite.class)
@Database(name="testdb")
public class CodeGenerationConfigurationTest extends HibernateRedDeerTest {

	private final String PRJ = "mvn-hibernate43"; 
    @InjectRequirement    
    private DatabaseRequirement dbRequirement;
    
    @Before
	public void testConnectionProfile() {
    	importProject(PRJ);
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		HibernateToolsFactory.testCreateConfigurationFile(cfg, PRJ, "hibernate.cfg.xml", true);
	}
    
    
    
    @Test
    public void testHibernateGenerateConfiguration() {
    	
    	HibernatePerspective p = new HibernatePerspective();
    	p.open();
    	
    	new ShellMenu("Run", "Hibernate Code Generation...","Hibernate Code Generation Configurations...").select();
    	new WaitUntil(new ShellWithTextIsActive("Hibernate Code Generation Configurations"));
    	new DefaultTreeItem("Hibernate Code Generation").select();
    	new DefaultToolItem("New launch configuration").click();
    	new LabeledCombo("Console configuration:").setSelection(PRJ);
    	new LabeledText("Output directory:").setText("/" + PRJ + "/src/main/java");
    	new CheckBox("Reverse engineer from JDBC Connection").click();
    	new LabeledText("Package:").setText("org.gen");
    	
    	
    	new DefaultCTabItem("Exporters").activate();
    	new DefaultTableItem(0).setChecked(true);
    	new DefaultTableItem(1).setChecked(true);
    	
    	new PushButton("Apply").click();
    	new PushButton("Run").click();
    	
    	new WaitWhile(new ShellWithTextIsActive("Hibernate Code Generation Configurations"));
    	new WaitUntil(new JobIsRunning());
    	    	
    	PackageExplorer pe = new PackageExplorer();    
    	pe.open();    	
    	try {
    		// need to wait here to get treeitem ready, TODO: implement proper condition
    		AbstractWait.sleep(TimePeriod.NORMAL);
    		new DefaultTreeItem(PRJ,"src/main/java","org.gen","Actor.java").doubleClick();
    	}
    	catch (TreeItemNotFoundException e) {
    		fail("Entities not generated, possible cause https://issues.jboss.org/browse/JBIDE-19217");
    	}
    	new DefaultEditor("Actor.java");
    }
    
	@After
	public void cleanUp() {
		// do nothing
	}
}