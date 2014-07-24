package org.jboss.tools.ws.ui.bot.test.annotation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.ui.bot.ext.Timing;
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
@JBossServer(state=ServerReqState.STOPPED)
@OpenPerspective(JavaEEPerspective.class)
public class HTTPMethodAnnotationQuickFixTest extends WSTestBase {

	@Override
	public void setup() {
		
	}
	
	@Override
	public void cleanup() {
		projectExplorer.deleteAllProjects();
	}
	
	@Test
	public void testHTTPMethodWithoutParameters() {
		/* import the project */
		String wsProjectName = "httpAnnot1";
		importWSTestProject(wsProjectName);
		
		/* assert that there is one Java problem */
		assertThat(errorsByType("Java Problem").length, equalTo(1));

		/* get quickfix bot for HttpMethod annotation */
		QuickFixBot qBot = quickFixBot(wsProjectName, "@HttpMethod");
		
		/* check that there are quick fixes for both required annotations */
		qBot.checkQuickFix("Add missing attributes", true);
		bot.activeEditor().save();
		
		/* assert that there is one JAX-RS errors - empty value */
		assertThat(errorsByType("JAX-RS Problem").length, equalTo(1));
	}
	
	/**
	 * Fails due to JBIDE-17667 (Quick fix for HTTPMethod annotation
	 * without @Target and @Retention doesn't add all imports)
	 * 
	 * @see https://issues.jboss.org/browse/JBIDE-17667
	 * 
	 * Resolved - o JAX-RS problems when importing a project that contains
	 * HTTPMethod annotation without @Target and @Retention
	 * {@link https://issues.jboss.org/browse/JBIDE-15428}
	 */
	@Test
	public void testTargetRetentionQuickFixes() {
		
		/* import the project */
		String wsProjectName = "httpAnnot2";
		importWSTestProject(wsProjectName);
		
		/* assert that there are two JAX-RS errors */
		assertThat(errorsByType("JAX-RS Problem").length, equalTo(2));

		/* get quickfix bot for MyAnnot annotation */
		QuickFixBot qBot = quickFixBot(wsProjectName, "MyAnnot");
		
		/* check that there are quick fixes for both required annotations */
		qBot.checkQuickFix("Add @Target annotation on type 'MyAnnot'", true);
		
		/* there is need to wait a while until validation starts to work */
		bot.sleep(Timing.time2S());
		qBot.checkQuickFix("Add @Retention annotation on type 'MyAnnot'", true);
		bot.sleep(Timing.time2S());
		
		/* assert that there are no JAX-RS errors */
		assertThat(errorsByType("JAX-RS Problem").length, equalTo(0));
	}
	
	private QuickFixBot quickFixBot(String wsProjectName, String underlinedText) {
		packageExplorer.openFile(wsProjectName, "src", "test", "MyAnnot.java");
		
		SWTBotEditorExt editor = new SWTBotEditorExt(bot.activeEditor().getReference(), bot);
		SWTBotEclipseEditor eclipseEditor = editor.toTextEditor();
		int lineIndex = 0;
		for (String line : eclipseEditor.getLines()) {
			if (line.contains(underlinedText)) {
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
