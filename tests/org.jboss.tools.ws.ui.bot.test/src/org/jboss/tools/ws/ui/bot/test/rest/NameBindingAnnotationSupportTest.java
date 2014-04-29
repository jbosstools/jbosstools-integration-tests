package org.jboss.tools.ws.ui.bot.test.rest;

import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.hamcrest.core.Is;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.ProjectItem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.jboss.tools.ui.bot.ext.parts.QuickFixBot;
import org.jboss.tools.ui.bot.ext.parts.SWTBotEditorExt;
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
public class NameBindingAnnotationSupportTest extends RESTfulTestBase {
	
	@Override
	public void setup() {
		// no setup required
	}
	
	/**
	 * Fails due to JBIDE-17177
	 * 
	 * @see https://issues.jboss.org/browse/JBIDE-17177
	 */
	@Test
	public void definingNameBindingAnnotationWithoutTargetAndRetentionTest() {
		String projectName = "namebinding1";
		
		importAndCheckErrors(projectName);

		resourceHelper.replaceInEditor(
				editorForClass(projectName, "src", "org.rest.test",
						"Authorized.java").toTextEditor(),
						"@Target({ElementType.METHOD, ElementType.TYPE})",
						"", true);
		
		resourceHelper.replaceInEditor(
				editorForClass(projectName, "src", "org.rest.test",
						"Authorized.java").toTextEditor(),
						"@Retention(RetentionPolicy.RUNTIME)",
						"", true);
		
		//wait for JAX-RS validator
		new ProblemsView().open();
		bot.sleep(Timing.time2S());
				
		assertCountOfApplicationAnnotationValidationErrors(projectName, 2);
		assertCountOfApplicationAnnotationValidationErrors(projectName, "Retention", 1);
		assertCountOfApplicationAnnotationValidationErrors(projectName, "Target", 1);
		
		List<TreeItem> errors = new org.jboss.reddeer.eclipse.ui.problems.ProblemsView()
				.getAllErrors();
		
		assertThat("There are errors " + Arrays.toString(errors.toArray()),
				errors.size(), Is.is(2));
		
		/* get quickfix bot for HttpMethod annotation */
		QuickFixBot qBot = quickFixBot(projectName, "Authorized");
		
		/* check that there are quick fixes for both required annotations */
		qBot.checkQuickFix("Add @Retention annotation on type Authorized", true);
		bot.activeEditor().save();
		
		assertCountOfApplicationAnnotationValidationErrors(projectName, 1);
		assertCountOfApplicationAnnotationValidationErrors(projectName, "Retention", 0);
		
		qBot.checkQuickFix("Add @Target annotation on type Authorized", true);
		
		bot.activeEditor().save();
		
		assertCountOfApplicationAnnotationValidationErrors(projectName, 0);
		assertCountOfApplicationAnnotationValidationErrors(projectName, "Retention", 0);

	}
	
	private QuickFixBot quickFixBot(String wsProjectName, String underlinedText) {
		packageExplorer.openFile(wsProjectName, "src", "org.rest.test", "Authorized.java");
		
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
	
	@Test
	public void usingNameBindingAnnotationWithoutFilterOrInterceptor() {
		String projectName = "namebinding2";
		
		importRestWSProject(projectName);
		
		//remove Filter.java
		org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer pa = new org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer();
		pa.getProject(projectName).getProjectItem("src", "org.rest.test", "Filter.java").delete();
				
		assertCountOfApplicationAnnotationValidationErrors(projectName, 1);
		assertCountOfApplicationAnnotationValidationErrors(projectName, "no Filter or Interceptor", 1);
	}
}
