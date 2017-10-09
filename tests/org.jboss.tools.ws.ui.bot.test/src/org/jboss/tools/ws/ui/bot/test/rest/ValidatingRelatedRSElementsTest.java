package org.jboss.tools.ws.ui.bot.test.rest;

import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.autobuilding.AutoBuildingRequirement.AutoBuilding;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.ws.ui.bot.test.utils.ProjectHelper;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Testing validation of related JAX-RS elements.
 *
 * @author Radoslav Rabara
 *
 * @see http://tools.jboss.org/documentation/whatsnew/jbosstools/4.2.0.Beta3.html#validating-related-jax-rs-elements
 * @since 4.2.0.Beta3
 */
@RunWith(RedDeerSuite.class)
@AutoBuilding(value = false, cleanup = true)
public class ValidatingRelatedRSElementsTest extends RESTfulTestBase {

	@Override
	protected String getWsProjectName() {
		return "restRelatedValidation";
	}

	@Test
	public void validParameterTest() {
		ProjectHelper.cleanAllProjects();
		/* assert there is invalid parameter error */
		assertCountOfValidationProblemsExists(ProblemType.ERROR, getWsProjectName(), null, null, 1);
		assertCountOfValidationProblemsExists(ProblemType.ERROR, getWsProjectName(), 
				"The type 'org.rest.test.CarFromString' is not valid for this parameter.", null, 1);

		/* fix the error */
		openJavaFile(getWsProjectName(), "org.rest.test", "CarFromString.java");
		TextEditor editor = new TextEditor();
		editor.setText(editor.getText().replace("Car fromString(", "CarFromString fromString("));
		editor.save();
		ProjectHelper.cleanAllProjects();

		/* error disappeared */
		assertCountOfProblemsExists(ProblemType.ERROR, getWsProjectName(), null, null, 0);
	}
}
