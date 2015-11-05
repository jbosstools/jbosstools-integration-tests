package org.jboss.tools.freemarker.ui.bot.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.views.contentoutline.OutlineView;
import org.jboss.reddeer.eclipse.ui.views.log.LogView;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Freemarker Directive tests
 * @author Jiri Peterka
 *
 */
@RunWith(RedDeerSuite.class)
public class FreemarkerDirectiveTest extends FreemarkerTest {
	
	
	private static final Logger log = Logger.getLogger(FreeMarkerEditorTest.class);
	private String prj = "org.jboss.tools.freemarker.testprj";
	
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

	@Ignore  
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

	private void openFTLFileInEditor(String file, String outline) {
		
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();

		
		new DefaultTreeItem(prj, "ftl", file).doubleClick();
		new TextEditor(file);
		
		log.step("Open outline view and check freemarker elements there");
		OutlineView ov = new OutlineView();
		ov.open();
		
		Collection<TreeItem> outlineElements = ov.outlineElements();
		
		List<String> list = new ArrayList<String>();
		for (TreeItem i : outlineElements) {
			list.add(i.getText());
		}
		
		assertTrue(list.contains(outline));
	}

	private void emptyErrorLog() {
		
		LogView elv = new LogView();
		elv.open();
		//new ContextMenu("Delete Log").select();
		new WaitWhile(new JobIsRunning());
	}

	@After
	public void cleanup() {
		removeTestProject(prj);
	}
	
	@AfterClass
	public static void aterClass() {
		// wait for all jobs
		new WaitWhile(new JobIsRunning());
	}
}
