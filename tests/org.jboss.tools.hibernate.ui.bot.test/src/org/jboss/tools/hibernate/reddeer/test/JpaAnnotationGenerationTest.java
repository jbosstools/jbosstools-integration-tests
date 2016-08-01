package org.jboss.tools.hibernate.reddeer.test;

import java.util.HashMap;
import java.util.Map;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.reddeer.swt.api.Menu;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.hibernate.reddeer.common.StringHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test generates JPA annotations on plain POJO class
 * @author jpeterka
 *
 */
@RunWith(RedDeerSuite.class)
@Database(name="testdb")
public class JpaAnnotationGenerationTest extends HibernateRedDeerTest {
	
	private final String PRJ = "configurationtest";
	private final String PCKG = "org.test.generation.annotation";
	protected  Map<String,String> libraries = new HashMap<String, String>() {{
	    put("hsqldb-2.3.4.jar",null);
	}};
	
	private static final Logger log = Logger.getLogger(JpaAnnotationGenerationTest.class);
	
	@Before 
	public void prepare() {
		log.step("Import testing project");
		importProject(PRJ, libraries);
	}
	
	@After
	public void cleanUp() {
		deleteAllProjects();
	}
	
	@Test
	public void testGenerateJPAHibernateAnnotationsContextMenu() {		
		selectItem();
		log.step("Generate Hibernate/JPA annotations from context menu");
		Menu menu = new ContextMenu("Source","Generate Hibernate/JPA annotations...");
		menu.select();
		postCheck();
	}

	@Test
	public void testGenerateJPAHibernateAnnotationsMenuBar() {		
		selectItem();		
		log.step("Generate Hibernate/JPA annotations from main menu");
		Menu menu = new ShellMenu("Source","Generate Hibernate/JPA annotations...");
		menu.select();		
		postCheck();
	}
	
	private void postCheck() {
		new DefaultShell("Hibernate: add JPA annotations");
		new PushButton("Next >").click();
		new PushButton("Finish").click();
		new WaitWhile(new ShellWithTextIsAvailable("Hibernate: add JPA annotations"));
		new WaitWhile(new JobIsRunning());
		
		new WorkbenchShell();
		
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(PRJ).getProjectItem("src",PCKG,"Dog.java").open();;		

		TextEditor editor = new TextEditor("Dog.java");
		StringHelper sh = new StringHelper(editor.getText());
		sh.getPositionAfter("@Entity");		
	}
	
	private void selectItem() {
		log.step("Select package with POJO clases");
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(PRJ).getProjectItem("src",PCKG,"Dog.java").select();
	}
	
	
	
}