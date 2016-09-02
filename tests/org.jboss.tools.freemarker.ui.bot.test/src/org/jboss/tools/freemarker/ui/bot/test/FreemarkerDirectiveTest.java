package org.jboss.tools.freemarker.ui.bot.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.views.contentoutline.OutlineView;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Freemarker Directives tests
 * @author Jiri Peterka
 *
 */
@RunWith(RedDeerSuite.class)
public class FreemarkerDirectiveTest extends FreemarkerTest {
	
	
	private static final Logger log = Logger.getLogger(FreeMarkerEditorTest.class);
	
	@BeforeClass
	public static void beforeClass() {
		setFullOutlineView();
	}
	
	@Before
	public void before() {
		emptyErrorLog();
		log.step("Import test project for freemarker test");
		importTestProject();
		log.step("Open ftl file in freemarker editor");		
	}

	@Test
	public void assignDirectiveOutlineTest() {
		openFTLFileInEditor("assign-directive.ftl","assign variable1=value1 variable2=value2");		
		checkErrorLog();
	}	
	
	@Test
	public void globalDirectiveOutlineTest() {
		openFTLFileInEditor("global-directive.ftl","global var1=value1 var2=value2");		
		checkErrorLog();
	}	

	@Test
	public void attemptDirectiveOutlineTest() {
		openFTLFileInEditor("attempt-directive.ftl","attempt");		
		checkErrorLog();
	}	

	@Test
	public void localDirectiveOutlineTest() {
		openFTLFileInEditor("local-directive.ftl","local name1=value1 name2=value2");		
		checkErrorLog();
	}	

	@Test
	public void macroDefDirectiveOutlineTest() {
		openFTLFileInEditor("macro-def-directive.ftl","test");		
		checkErrorLog();
	}	

	@Test
	public void macroCallDirectiveOutlineTest() {
		openFTLFileInEditor("macro-call-directive.ftl","test");		
		checkErrorLog();
	}	

	@Test
	public void importDefDirectiveOutlineTest() {
		openFTLFileInEditor("import-def-directive.ftl","import \"/libs/mylib.ftl\" as my");		
		checkErrorLog();
	}	
	
	@Test
	public void importCallDirectiveOutlineTest() {
		openFTLFileInEditor("import-call-directive.ftl","my.copyright date=\"1999-2002\"");		
		checkErrorLog();
	}	
		
	@Test
	public void ifDirectiveOutlineTest() {
		openFTLFileInEditor("if-directive.ftl","if x == 1","else");		
		checkErrorLog();		
	}

	@Test
	public void listDirectiveOutlineTest() {
		openFTLFileInEditor("list-directive.ftl","list users as user","else");		
		checkErrorLog();		
	}

	@Test
	public void ftlDirectiveOutlineTest() {
		openFTLFileInEditor("ftl-directive.ftl","ftl param1=value1 param2=value2");		
		checkErrorLog();		
	}
	
	private void openFTLFileInEditor(String file, String... outline) {
		
		PackageExplorer explorer = new PackageExplorer();
		Project project = explorer.getProject(projectName);
		project.expand();
		project.refresh();
		ProjectItem item = project.getProjectItem(parentFolder, file);
		item.open();
		
		new TextEditor(file);
		
		log.step("Open outline view and check freemarker elements there");
		OutlineView ov = new OutlineView();
		ov.open();
		
		Collection<TreeItem> outlineElements = ov.outlineElements();
		
		List<String> list = new ArrayList<String>();
		boolean found = false;
		for (String o : outline ) {			
			for (TreeItem i : outlineElements) {			
				
				if (i.getText().equals(o)) {
					i.expand();
					outlineElements = i.getItems();
					found = true;
					break;
				}				
			}
			if (!found) assertTrue("Outline is expected to contain \"" + o + "\"", list.contains(o));
			found = false; 
		}
	}
	
	
	@After
	public void cleanup() {
		removeTestProject(projectName);
	}
	
	@AfterClass
	public static void aterClass() {
		new WaitWhile(new JobIsRunning());
	}
}
