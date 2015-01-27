package org.jboss.tools.forge2.ui.bot.wizard.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.core.Is.is;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.wst.common.project.facet.ui.FacetsPropertyPage;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.workbench.condition.EditorWithTitleIsActive;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Pavol Srna
 *
 */
public class ServletSetupWizardTest extends WizardTestBase {

	private static String FACET = "Dynamic Web Module";
	private static String FACET_VERSION  = "3.1";
	private static String DEPENDENCY = "javax.servlet-api : 3.1.0 [provided]";
	
	@Before
	public void prepare(){
		newProject(PROJECT_NAME);
		servletSetup(PROJECT_NAME, FACET_VERSION);
	}
	
	@Test
	public void testFacetEnabled(){	
		ProjectExplorer pe = new ProjectExplorer();
		FacetsPropertyPage facetsPage = new FacetsPropertyPage(pe.getProject(PROJECT_NAME));
		facetsPage.open();
		TreeItem facet = new DefaultTreeItem(new DefaultTree(1), "Dynamic Web Module");
		assertTrue(FACET + " facet is not checked!", facet.isChecked());
		assertThat(facet.getCell(1), is(FACET_VERSION));
	}
	
	@Test
	public void testDependenciesAddedToPom(){
		ProjectExplorer pe = new ProjectExplorer();
		pe.getProject(PROJECT_NAME).getTreeItem().getItem("pom.xml").doubleClick();
		new WaitUntil(new EditorWithTitleIsActive(PROJECT_NAME + "/pom.xml"));
		new DefaultEditor(PROJECT_NAME + "/pom.xml").activate();
		new DefaultCTabItem("Dependencies").activate();
		assertTrue("Dependency: '" + DEPENDENCY + "' not added!", new DefaultTable(1).containsItem(DEPENDENCY));
	}
}
