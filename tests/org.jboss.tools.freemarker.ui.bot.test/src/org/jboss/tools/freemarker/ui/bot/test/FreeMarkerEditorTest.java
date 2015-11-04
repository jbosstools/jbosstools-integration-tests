package org.jboss.tools.freemarker.ui.bot.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.matcher.WithTextMatcher;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.ui.views.contentoutline.OutlineView;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Freemarker ui bot test
 * @author Jiri Peterka
 *
 */
@RunWith(RedDeerSuite.class)
public class FreeMarkerEditorTest extends FreemarkerTest {
	
	
	private static final Logger log = Logger.getLogger(FreeMarkerEditorTest.class);
	private String prj = "org.jboss.tools.freemarker.testprj";
	
	@BeforeClass
	public static void beforeClass() {
		setFullOutlineView();
	}
	
	@Test
	public void emptyTest() {
		assertTrue(true);
	}

	@Test
	public void freeMarkerTest() {
		log.step("Import test project for freemarker test");
		importTestProject();
		log.step("Open ftl file in freemarker editor");
		openFTLFileInEditor();
		// disabled until target platform in running instance is resolved
		checkFreemMarkerOutput();
		checkErrorLog();
	}	


	private void openFTLFileInEditor() {
		
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();

		
		new DefaultTreeItem(prj, "ftl", "welcome.ftl").doubleClick();
		new TextEditor("welcome.ftl");
		
		log.step("Open outline view and check freemarker elements there");
		OutlineView ov = new OutlineView();
		ov.open();
		
		Collection<TreeItem> outlineElements = ov.outlineElements();
		
		List<String> list = new ArrayList<String>();
		for (TreeItem i : outlineElements) {
			list.add(i.getText());
		}
		
		assertTrue(list.contains("user"));
		assertTrue(list.contains("latestProduct.name"));
		
	    // https://issues.jboss.org/browse/JBIDE-11287
		// remove comment when this jira is fixed
		//assertTrue(list.contains("latestProduct.url"));		
	}


	@SuppressWarnings({ "unused", "unchecked" })
	private void checkFreemMarkerOutput() {
		
		String outputExpected = "";
		String rpath = getResourceAbsolutePath(
				Activator.PLUGIN_ID, "resources/fm-output.txt");
		try {
			outputExpected = readTextFileToString(rpath);
			log.info("------------------------------------------");
			log.info(outputExpected);
			log.info("------------------------------------------");
		} catch (IOException e) {
			log.error(e.getMessage());
			new RuntimeException("Unable to read from resource");
		}

		ProjectExplorer pe = new ProjectExplorer();
		pe.open();

		new DefaultTreeItem(prj,"src","org.jboss.tools.freemarker.testprj","FMTest.java").select();
		new ContextMenu(new WithTextMatcher("Run As"), new WithTextMatcher(new RegexMatcher(".*Java Application.*"))).select();

		new WaitWhile(new ShellWithTextIsActive("Progress Information"));
		new WaitWhile(new JobIsRunning());
		
		ConsoleView cv = new ConsoleView();
		cv.open();		
		String consoleText = cv.getConsoleText();
		
		assertTrue("Output equal check",consoleText.equals(outputExpected));
	}

	@After
	public void after() {
		removeTestProject(prj);
	}
}
