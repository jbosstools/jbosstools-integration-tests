package org.jboss.tools.ws.ui.bot.test.annotation;

import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.autobuilding.AutoBuildingRequirement.AutoBuilding;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.workbench.condition.EditorIsDirty;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.common.reddeer.requirements.JavaFoldingRequirement.JavaFolding;
import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.jboss.tools.ws.ui.bot.test.utils.ProjectHelper;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author jjankovi
 *
 */
@RunWith(RedDeerSuite.class)
@JBossServer(state=ServerRequirementState.STOPPED)
@OpenPerspective(JavaEEPerspective.class)
@AutoBuilding(value = false, cleanup = true)
@JavaFolding(false)
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
		String projectName = "httpAnnot1";
		importWSTestProject(projectName);

		/* assert that there is one Java problem */
		assertCountOfProblemsExists(ProblemType.ERROR, projectName, null, null, 1);

		/* open MyAnnot.java */
		openMyAnnotJavaFile(projectName);
		TextEditor editor = setCursorPositionToLineInTextEditor("@HttpMethod");

		/* check that there are quick fixes for both required annotations */
		editor.openQuickFixContentAssistant().chooseProposal(
				"Add missing attributes");
		new WaitUntil(new EditorIsDirty(editor));
		editor.save();
		ProjectHelper.cleanAllProjects();
		
		
		/* assert that there is one JAX-RS errors - empty value */		
		assertCountOfProblemsExists(ProblemType.ERROR, projectName, null, JAX_RS_PROBLEM, 1);
	}

	@Test
	public void testTargetRetentionQuickFixes() {
		/* import the project */
		String projectName = "httpAnnot2";
		importWSTestProject(projectName);

		/* assert that there are two JAX-RS errors */
		assertCountOfProblemsExists(ProblemType.ERROR, projectName, null, JAX_RS_PROBLEM, 2);

		/* open MyAnnot.java */
		openMyAnnotJavaFile(projectName);
		TextEditor editor = setCursorPositionToLineInTextEditor("MyAnnot");

		/* check that there are quick fixes for both required annotations */
		AbstractWait.sleep(TimePeriod.DEFAULT);
		editor.openQuickFixContentAssistant().chooseProposal(
				"Add @Target annotation on type 'MyAnnot'");
		AbstractWait.sleep(TimePeriod.getCustom(1));//makes a delay between applying quickfixes

		/* there is need to wait a while until validation starts to work */
		editor.openQuickFixContentAssistant().chooseProposal(
				"Add @Retention annotation on type 'MyAnnot'");

		/* save edited file */
		if(editor.isDirty()) {
			editor.save();
		}
		ProjectHelper.cleanAllProjects();

		/* assert that there are no JAX-RS errors */
		assertCountOfValidationProblemsExists(ProblemType.ERROR, projectName, null, JAX_RS_PROBLEM, 0);
	}

	private void openMyAnnotJavaFile(String projectName) {
		openJavaFile(projectName, "test", "MyAnnot.java");
	}
}
