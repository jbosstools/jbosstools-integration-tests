package org.jboss.tools.ws.ui.bot.test.rest;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.autobuilding.AutoBuildingRequirement.AutoBuilding;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.workbench.condition.EditorIsDirty;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.common.reddeer.requirements.JavaFoldingRequirement.JavaFolding;
import org.jboss.tools.ws.ui.bot.test.utils.ProjectHelper;
import org.junit.Test;
import org.junit.runner.RunWith;

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
@RunWith(RedDeerSuite.class)
@JBossServer(state=ServerRequirementState.PRESENT)
@AutoBuilding(value = false, cleanup = true)
@JavaFolding(false)
public class NameBindingAnnotationSupportTest extends RESTfulTestBase {
	
	@Override
	public void setup() {
		// no setup required
	}
	
	@Test
	public void definingNameBindingAnnotationWithoutTargetAndRetentionTest() {
		/* import the project */
		String projectName = "namebinding1";
		importAndCheckErrors(projectName);

		openJavaFile(projectName, "org.rest.test", "Authorized.java");

		/* remove @Target and @Retention annotations*/
		TextEditor editor = new TextEditor();
		editor.setText(editor.getText().replace("@Target({ElementType.METHOD, ElementType.TYPE})", ""));
		editor.setText(editor.getText().replace("@Retention(RetentionPolicy.RUNTIME)", ""));
		editor.save();
		ProjectHelper.cleanAllProjects();

		/* there should be 2 errors complaining about missing deleted annotations */
		assertCountOfValidationProblemsExists(ProblemType.ERROR, projectName, null, null, 2);
		assertCountOfValidationProblemsExists(ProblemType.ERROR, projectName, "Retention", null, 1);
		assertCountOfValidationProblemsExists(ProblemType.ERROR, projectName, "Target", null, 1);
		assertCountOfProblemsExists(ProblemType.ERROR, projectName, null, null, 2);

		/* prepare editor */
		openAuthorizedJavaFile(projectName);
		editor = setCursorPositionToTextInTextEditor("Authorized");

		/* check that there are quick fixes for both required annotations */
		editor.openQuickFixContentAssistant().chooseProposal("Add @Retention annotation on type 'Authorized'");
		ProjectHelper.cleanAllProjects();

		/* one error should disappear as a result of using a quickfix */
		assertCountOfValidationProblemsExists(ProblemType.ERROR, projectName, null, null, 1);
		assertCountOfValidationProblemsExists(ProblemType.ERROR, projectName, "Retention", null, 0);

		/* apply the second quixk fix */
		editor.activate();
		editor.openQuickFixContentAssistant().chooseProposal("Add @Target annotation on type 'Authorized'");
		ProjectHelper.cleanAllProjects();

		/* both quickfixes were used which means that there should be no error */
		assertCountOfValidationProblemsExists(ProblemType.ERROR, projectName, null, null, 0);
		assertCountOfValidationProblemsExists(ProblemType.ERROR, projectName, "Retention", null, 0);

	}

	@Test
	public void usingNameBindingAnnotationWithoutFilterOrInterceptor() {
		/* import the project */
		String projectName = "namebinding2";
		importWSTestProject(projectName);

		/* remove the filter */
		new ProjectExplorer().getProject(projectName)
			.getProjectItem("src", "org.rest.test", "Filter.java").delete();
		ProjectHelper.cleanAllProjects();
		
		/* there should be an error */
		assertCountOfValidationProblemsExists(ProblemType.ERROR, projectName, null, null, 1);
		assertCountOfValidationProblemsExists(ProblemType.ERROR, projectName, "no JAX-RS filter or interceptor", null, 1);
	}

	private void openAuthorizedJavaFile(String projectName) {
		openJavaFile(projectName, "org.rest.test", "Authorized.java");
	}
}
