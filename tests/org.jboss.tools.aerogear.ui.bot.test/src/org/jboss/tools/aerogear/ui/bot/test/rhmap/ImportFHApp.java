package org.jboss.tools.aerogear.ui.bot.test.rhmap;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.aerogear.ui.bot.test.FeedHenryBotTest;
import org.jboss.reddeer.core.util.FileUtil;
import org.junit.BeforeClass;
import org.junit.Test;

@CleanWorkspace
public class ImportFHApp extends FeedHenryBotTest {

	@BeforeClass
	public static void importApp() {
		importApp(FH_PROJECT, FH_APP_NAME);
	}

	@Test
	public void testAppIsImported() {
		assertTrue(new ProjectExplorer().containsProject(FH_APP_NAME));
	}

	@Test
	public void testConfigXmlOpenedInEditor() {
		DefaultEditor configEditor = new DefaultEditor();
		assertTrue(configEditor.isActive());
		assertTrue(configEditor.getTitle().equals(FH_APP_NAME));
	}

	@Test
	public void testProjectHasHybridAppNature() throws IOException {
		String projectConfig = FileUtil.readFile(WS_PATH + "/" + FH_APP_NAME + "/.project");
		assertTrue(projectConfig.contains("<nature>org.eclipse.thym.core.HybridAppNature</nature>"));
	}

	@Test
	public void testProjectHasJSNature() throws IOException {
		String projectConfig = FileUtil.readFile(WS_PATH + "/" + FH_APP_NAME + "/.project");
		assertTrue(projectConfig.contains("<nature>org.eclipse.wst.jsdt.core.jsNature</nature>"));
	}

}
