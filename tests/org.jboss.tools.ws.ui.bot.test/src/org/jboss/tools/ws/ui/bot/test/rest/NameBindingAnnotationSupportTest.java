package org.jboss.tools.ws.ui.bot.test.rest;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.jboss.tools.ui.bot.ext.parts.QuickFixBot;
import org.jboss.tools.ui.bot.ext.parts.SWTBotEditorExt;
import org.jboss.tools.ws.reddeer.editor.ExtendedTextEditor;
import org.junit.Test;

/**
 * Testing support for custom Name Binding annotation<br/><br/>
 * 
 * Run with J2EE7+ server
 * 
 * @author Radoslav Rabara
 * 
 * @see http://tools.jboss.org/documentation/whatsnew/jbosstools/4.2.0.Beta1.html#webservices
 * @since JBT 4.2.0.Beta1
 */
@Require(server = @Server(type = ServerType.WildFly, state = ServerState.Present))
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.WILDFLY)
public class NameBindingAnnotationSupportTest extends RESTfulTestBase {
	
	@Override
	public void setup() {
		// no setup required
	}
	
	/**
	 * Resolved: Wrong @NameBinding quick fix for a @Target annotation)
	 * {@link https://issues.jboss.org/browse/JBIDE-17177}
	 */
	@Test
	public void definingNameBindingAnnotationWithoutTargetAndRetentionTest() {
		/* import the project */
		String projectName = "namebinding1";
		importAndCheckErrors(projectName);

		openJavaFile(projectName, "org.rest.test", "Authorized.java");

		/* remove @Target and @Retention annotations*/
		ExtendedTextEditor textEditor = new ExtendedTextEditor();
		textEditor.removeLine("@Target({ElementType.METHOD, ElementType.TYPE})");
		textEditor.removeLine("@Retention(RetentionPolicy.RUNTIME)");

		/* there should be 2 errors complaining about missing deleted annotations */
		assertCountOfValidationErrors(projectName, 2);
		assertCountOfValidationErrors(projectName, "Retention", 1);
		assertCountOfValidationErrors(projectName, "Target", 1);
		assertCountOfErrors(projectName, 2);

		/* get quickfix bot for HttpMethod annotation */
		QuickFixBot qBot = quickFixBot(projectName, "Authorized");

		/* check that there are quick fixes for both required annotations */
		qBot.checkQuickFix("Add @Retention annotation on type 'Authorized'", true);
		new TextEditor().save();

		/* one error should disappear as a result of using a quickfix */
		assertCountOfValidationErrors(projectName, 1);
		assertCountOfValidationErrors(projectName, "Retention", 0);

		qBot.checkQuickFix("Add @Target annotation on type 'Authorized'", true);
		new TextEditor().save();

		/* both quickfixes were used which means that there should be no error */
		assertCountOfValidationErrors(projectName, 0);
		assertCountOfValidationErrors(projectName, "Retention", 0);

	}

	@Test
	public void usingNameBindingAnnotationWithoutFilterOrInterceptor() {
		/* import the project */
		String projectName = "namebinding2";
		importRestWSProject(projectName);

		/* remove the filter */
		new PackageExplorer().getProject(projectName)
			.getProjectItem("src", "org.rest.test", "Filter.java").delete();
		
		/* there should be an error */
		assertCountOfValidationErrors(projectName, 1);
		assertCountOfValidationErrors(projectName, "no Filter or Interceptor", 1);
	}

	private QuickFixBot quickFixBot(String wsProjectName, String underlinedText) {
		openJavaFile(wsProjectName, "org.rest.test", "Authorized.java");
		
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
