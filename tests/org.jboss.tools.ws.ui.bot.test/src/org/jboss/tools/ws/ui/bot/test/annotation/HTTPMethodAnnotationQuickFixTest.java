package org.jboss.tools.ws.ui.bot.test.annotation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.parts.QuickFixBot;
import org.jboss.tools.ui.bot.ext.parts.SWTBotEditorExt;
import org.jboss.tools.ui.bot.ext.view.ProblemsView;
import org.jboss.tools.ws.ui.bot.test.WSTestBase;
import org.junit.Test;

/**
 * 
 * @author jjankovi
 *
 */
@Require(server = @Server(state = ServerState.NotRunning), perspective = "Java EE")
public class HTTPMethodAnnotationQuickFixTest extends WSTestBase {

	@Override
	protected String getWsProjectName() {
		return "httpAnnot";
	}
	
	@Override
	public void setup() {
		importWSTestProject(getWsProjectName());
	}
	
	@Override
	public void cleanup() {
		projectExplorer.deleteAllProjects();
	}
	
	@Test
	public void testQuickFixes() {
		
		/* workaround for JBIDE-12690 */
		jbide12680Workaround(getWsProjectName(), "src", "test", "MyAnnot.java");
		
		/* assert that there are two JAX-RS errors */
		assertThat(errorsByType("JAX-RS Problem").length, equalTo(2));

		/* get quickfix bot for MyAnnot annotation */
		QuickFixBot bot = quickFixBot();
		
		/* check that there are quick fixes for both required annotations */
		bot.checkQuickFix("Add @Target annotation on type MyAnnot", false);
		bot.checkQuickFix("Add @Retention annotation on type MyAnnot", false);
		
	}
	
	private void jbide12680Workaround(String projectName, String... path) {
		SWTBotEditor editor = packageExplorer.openFile(projectName, path);
		SWTBotEclipseEditor eclipseEditor = editor.toTextEditor();
		eclipseEditor.insertText(" ");
		eclipseEditor.save();
	}

	private QuickFixBot quickFixBot() {
		SWTBotEditorExt editor = new SWTBotEditorExt(bot.activeEditor().getReference(), bot);
		SWTBotEclipseEditor eclipseEditor = editor.toTextEditor();
		int lineIndex = 0;
		for (String line : eclipseEditor.getLines()) {
			if (line.contains("@Http")) {
				 break;
			}
			lineIndex++;
		}
		eclipseEditor.navigateTo(lineIndex, 0);
		return new QuickFixBot(editor);
	}

	private SWTBotTreeItem[] errorsByType(String problemType) {
		return ProblemsView.
				getFilteredErrorsTreeItems(bot, null, null, null, problemType);
	}
	
}
