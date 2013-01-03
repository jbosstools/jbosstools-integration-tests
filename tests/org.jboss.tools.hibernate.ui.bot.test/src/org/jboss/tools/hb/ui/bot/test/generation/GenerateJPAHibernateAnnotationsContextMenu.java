package org.jboss.tools.hb.ui.bot.test.generation;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.jboss.reddeer.swt.api.Menu;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.tree.ViewTreeItem;
import org.jboss.tools.hb.ui.bot.test.HibernateBaseTest;
import org.jboss.tools.ui.bot.ext.config.Annotations.DB;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.helper.StringHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.view.PackageExplorer;
import org.junit.Test;

@Require(db = @DB, clearProjects = true, perspective = "JPA")
public class GenerateJPAHibernateAnnotationsContextMenu extends HibernateBaseTest {
	
	final String prj = "configurationtest";
	final String out = "src";
	final String hbcfg = "hibernate.cfg.xml";
	final String pckg = "org.test.generation.annotation";
	

	
	@Test
	public void testGenerateJPAHibernateAnnotationsContextMenu() {
		importTestProject("/resources/prj/" + prj);
		generateJPAHibernateAnnotationsContextMenu();
	}

	
	private void generateJPAHibernateAnnotationsContextMenu() {
		PackageExplorer pe = new PackageExplorer();
		pe.selectProject(prj);		
		
		TreeItem item = new ViewTreeItem(prj,"src",pckg,"Dog.java");
		item.select();
		
		Menu menu = new ContextMenu("Source","Generate Hibernate/JPA annotations");
		menu.select();
		
		Shell s = new DefaultShell("Hibernate: add JPA annotations");
		new PushButton("Next >").click();
		new PushButton("Finish").click();
		
		Shell ws = new WorkbenchShell();
		TreeItem itemre = new ViewTreeItem(prj,"src",pckg,"Dog.java");
		item.doubleClick();
		
		SWTBotEditor editor = bot.editorByTitle("Dog.java");
		editor.setFocus();
		StringHelper sh = new StringHelper(editor.toTextEditor().getText());
		sh.getPositionAfter("@Entity");		
	}
}