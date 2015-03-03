package org.jboss.tools.hibernate.reddeer.test;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.utils.DeleteUtils;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.api.Menu;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
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
public class JpaAnnotationGenerationTest extends HibernateRedDeerTest {
	
	private final String PRJ = "configurationtest";
	private final String PCKG = "org.test.generation.annotation";
	
	
	@Before 
	public void prepare() {
		importProject(PRJ);
	}
	
	@Test
	public void testGenerateJPAHibernateAnnotationsContextMenu() {
		selectItem();
		Menu menu = new ContextMenu("Source","Generate Hibernate/JPA annotations...");
		menu.select();
		postCheck();
	}

	@Test
	public void testGenerateJPAHibernateAnnotationsMenuBar() {
		selectItem();		
		Menu menu = new ShellMenu("Source","Generate Hibernate/JPA annotations...");
		menu.select();		
		postCheck();
	}
	
	private void postCheck() {
		new DefaultShell("Hibernate: add JPA annotations");
		new PushButton("Next >").click();
		new PushButton("Finish").click();
				
		new WorkbenchShell();
		
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.selectProjects(PRJ);		
		
		TreeItem item = new DefaultTreeItem(PRJ,"src",PCKG,"Dog.java");
		item.doubleClick();
		
		TextEditor editor = new TextEditor("Dog.java");
		StringHelper sh = new StringHelper(editor.getText());
		sh.getPositionAfter("@Entity");		
	}
	
	private void selectItem() {
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.selectProjects(PRJ);		
		
		TreeItem item = new DefaultTreeItem(PRJ,"src",PCKG,"Dog.java");
		item.select();
	}
	
	@After
	public void cleanUp() {
		PackageExplorer packageExplorer = new PackageExplorer();
		packageExplorer.open();
		for (Project project : packageExplorer.getProjects()){
			DeleteUtils.forceProjectDeletion(project, true);
		}
		packageExplorer.activate();
	}
	
}