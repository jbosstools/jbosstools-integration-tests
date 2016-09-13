package org.jboss.tools.freemarker.ui.bot.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.platform.RunningPlatform;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.matcher.WithTextMatcher;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
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
 * Freemarker UI Editor test
 * @author Jiri Peterka
 *
 */
@RunWith(RedDeerSuite.class)
public class FreeMarkerEditorTest extends FreemarkerTest {
	
	
	private static final Logger log = Logger.getLogger(FreeMarkerEditorTest.class);
	
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
		
		checkFreemMarkerOutput();
		checkErrorLog();
	}	


	private void openFTLFileInEditor() {
		
		PackageExplorer explorer = new PackageExplorer();
		Project project = explorer.getProject(projectName);
		project.expand();
		project.refresh();
		ProjectItem item = project.getProjectItem(parentFolder, "welcome.ftl");
		item.open();
		
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
		assertTrue("Should contain latestProduct.url + see JBIDE-11287", list.contains("latestProduct.url"));		
	}


	@SuppressWarnings({ "unchecked" })
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

		PackageExplorer explorer = new PackageExplorer();
		Project project = explorer.getProject(projectName);
		ProjectItem item = project.getProjectItem("src","org.jboss.tools.freemarker.testprj","FMTest.java");
		item.select();
		new ContextMenu(new WithTextMatcher("Run As"), new WithTextMatcher(new RegexMatcher(".*Java Application.*"))).select();

		new WaitWhile(new ShellWithTextIsActive("Progress Information"));
		new WaitWhile(new JobIsRunning());
		
		ConsoleView cv = new ConsoleView();
		cv.open();
		new WaitUntil(new ConsoleHasText(outputExpected), TimePeriod.NORMAL, false);
		String consoleText = cv.getConsoleText();
		
		if (!consoleText.equals(outputExpected)) {
			log.error("Console text doesn't correspond with expected text");
			log.dump("Console text:" + consoleText);
			log.dump("Expected text:" + outputExpected);
		}
		// workaround for slightly different format on Windows
		if (RunningPlatform.isWindows())
			assertTrue("Output equal check",consoleText.contains("Bunny does 6 little hops :)"));
		else
			assertTrue("Output equal check",consoleText.equals(outputExpected));		
	}

	@After
	public void after() {
		removeTestProject(projectName);
	}
}
