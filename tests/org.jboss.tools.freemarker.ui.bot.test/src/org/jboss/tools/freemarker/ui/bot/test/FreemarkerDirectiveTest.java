package org.jboss.tools.freemarker.ui.bot.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.eclipse.ui.views.contentoutline.OutlineView;
import org.jboss.reddeer.eclipse.ui.views.log.LogView;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.api.TreeItem;
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
	public void assignDirectiveOutlineTest() {
		emptyErrorLog();
		log.step("Import test project for freemarker test");
		importTestProject();
		log.step("Open ftl file in freemarker editor");
		openFTLFileInEditor("assign-directive.ftl","assign variable1=value1 variable2=value2");		
		checkErrorLog();
	}	

	@Test
	public void attemptDirectiveOutlineTest() {
		emptyErrorLog();
		log.step("Import test project for freemarker test");
		importTestProject();
		log.step("Open ftl file in freemarker editor");
		openFTLFileInEditor("attempt-directive.ftl","attempt");		
		checkErrorLog();
	}	
	

	private void openFTLFileInEditor(String file, String outline) {
		
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

	@AfterClass
	public static void aterClass() {
		// wait for all jobs
		new WaitWhile(new JobIsRunning());
	}
}
