package org.jboss.tools.freemarker.ui.bot.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.eclipse.ui.views.contentoutline.OutlineView;
import org.jboss.reddeer.eclipse.ui.views.log.LogMessage;
import org.jboss.reddeer.eclipse.ui.views.log.LogView;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.handler.EditorHandler;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.junit.AfterClass;
import org.junit.BeforeClass;
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
		JavaPerspective p = new JavaPerspective();
		p.open();
		EditorHandler.getInstance().closeAll(false);
		new WaitWhile(new JobIsRunning());		

		JavaPerspective jp = new JavaPerspective();
		jp.open();
		
		WorkbenchPreferenceDialog dlg = new WorkbenchPreferenceDialog();
		dlg.open();
		dlg.select("FreeMarker");
		
		log.step("Set Freemarker outline level to full level on freemarker preference page");
		FreemarkerPreferencePage page = new FreemarkerPreferencePage();
		page.setOutlineLevelOfDetail(OutlineLevelOfDetail.FULL);
		
		dlg.ok();
	}

	@Test
	public void emptyTest() {
		assertTrue(true);
	}

	@Test
	public void freeMarkerTest() {
		emptyErrorLog();
		log.step("Import test project for freemarker test");
		importTestProject();
		log.step("Open ftl file in freemarker editor");
		openFTLFileInEditor();
		// disabled until target platform in running instance is resolved
		checkFreemMarkerOutput();
		checkErrorLog();
	}	

	private void importTestProject() {
		
		ExternalProjectImportWizardDialog wizard = new ExternalProjectImportWizardDialog();		
		wizard.open();

		String rpath = getResourceAbsolutePath(
				Activator.PLUGIN_ID, "resources/prj");
		String wpath = getWorkspaceAbsolutePath();
		File rfile = new File(rpath);
		File wfile = new File(wpath);
		try {
			copyFilesBinaryRecursively(rfile, wfile, null);
		} catch (IOException e) {
			fail("Unable to copy freemarker test project");
		}
		
		WizardProjectsImportPage firstPage = new WizardProjectsImportPage();
		
		firstPage.setRootDirectory(wpath);
		firstPage.selectAllProjects();
		firstPage.copyProjectsIntoWorkspace(false);
		wizard.finish();		
	}

	private void openFTLFileInEditor() {
		
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();

		
		new DefaultTreeItem(prj, "ftl", "assign-directive.ftl").doubleClick();
		new TextEditor("assign-directive.ftl");
		
		log.step("Open outline view and check freemarker elements there");
		OutlineView ov = new OutlineView();
		ov.open();
		
		Collection<TreeItem> outlineElements = ov.outlineElements();
		
		List<String> list = new ArrayList<String>();
		for (TreeItem i : outlineElements) {
			list.add(i.getText());
		}
		
		assertTrue(list.contains("assign variable1=value1 variable2=value2"));
		
	    // https://issues.jboss.org/browse/JBIDE-11287
		// remove comment when this jira is fixed
		//assertTrue(list.contains("latestProduct.url"));		
	}

	private void emptyErrorLog() {
		
		LogView elv = new LogView();
		elv.open();
		//new ContextMenu("Delete Log").select();
		new WaitWhile(new JobIsRunning());
	}

	private void checkErrorLog() {
		LogView el = new LogView();
		List<LogMessage> errorMessages = el.getErrorMessages();
		for (LogMessage lm : errorMessages) { 
			log.info(lm.getMessage());
		}
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

	private String readTextFileToString(String filePath) throws IOException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filePath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);				
				line = br.readLine();
				if (line != null)
					sb.append("\n");
			}
			String everything = sb.toString();
			return everything;
		} finally {
			br.close();
		}
	}

	@AfterClass
	public static void aterClass() {
		// wait for all jobs
		new WaitWhile(new JobIsRunning());
	}
}
