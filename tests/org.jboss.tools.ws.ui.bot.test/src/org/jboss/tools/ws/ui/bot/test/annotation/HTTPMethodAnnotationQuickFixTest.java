package org.jboss.tools.ws.ui.bot.test.annotation;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.parts.QuickFixBot;
import org.jboss.tools.ui.bot.ext.parts.SWTBotEditorExt;
import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.junit.Test;

/**
 * 
 * @author jjankovi
 *
 */
@Require(server = @Server(state = ServerState.NotRunning), perspective = "Java EE")
@JBossServer(state=ServerReqState.STOPPED)
@OpenPerspective(JavaEEPerspective.class)
public class HTTPMethodAnnotationQuickFixTest extends RESTfulTestBase {

	@Override
	public void setup() {
		
	}

	@Override
	public void cleanup() {
		deleteAllProjects();
	}

	@Test
	public void testHTTPMethodWithoutParameters() {
		/* import the project */
		String wsProjectName = "httpAnnot1";
		importWSTestProject(wsProjectName);

		/* assert that there is one Java problem */
		assertCountOfErrors(1);

		/* get quickfix bot for HttpMethod annotation */
		QuickFixBot qBot = quickFixBot(wsProjectName, "@HttpMethod");

		/* check that there are quick fixes for both required annotations */
		qBot.checkQuickFix("Add missing attributes", true);
		new TextEditor().save();
		AbstractWait.sleep(TimePeriod.getCustom(2));
		
		/* assert that there is one JAX-RS errors - empty value */
		assertCountOfValidationErrors(wsProjectName, 1);
	}
	
	/**
	 * Resolved - No JAX-RS problems when importing a project that contains
	 * HTTPMethod annotation without @Target and @Retention
	 * {@link https://issues.jboss.org/browse/JBIDE-15428}
	 * 
	 * Resolved - Quick fix for HTTPMethod annotation without @Target and
	 * @Retention doesn't add all imports
	 * {@link https://issues.jboss.org/browse/JBIDE-17667}
	 */
	@Test
	public void testTargetRetentionQuickFixes() {
		/* import the project */
		String wsProjectName = "httpAnnot2";
		importWSTestProject(wsProjectName);

		/* assert that there are two JAX-RS errors */
		assertCountOfValidationErrors(wsProjectName, 2);

		/* get quickfix bot for MyAnnot annotation */
		QuickFixBot qBot = quickFixBot(wsProjectName, "MyAnnot");

		/* check that there are quick fixes for both required annotations */
		qBot.checkQuickFix("Add @Target annotation on type 'MyAnnot'", true);

		/* there is need to wait a while until validation starts to work */
		AbstractWait.sleep(TimePeriod.getCustom(1));//makes a delay between applying quickfixes
		qBot.checkQuickFix("Add @Retention annotation on type 'MyAnnot'", true);

		/* assert that there are no JAX-RS errors */
		assertCountOfValidationErrors(wsProjectName, 0);
	}
	
	private QuickFixBot quickFixBot(String wsProjectName, String underlinedText) {
		new PackageExplorer().getProject(wsProjectName)
			.getProjectItem("src", "test", "MyAnnot.java").open();

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
}
