package org.jboss.tools.ws.ui.bot.test.rest;

import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.autobuilding.AutoBuildingRequirement.AutoBuilding;
import org.jboss.tools.ws.reddeer.editor.ExtendedTextEditor;
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
		ExtendedTextEditor textEditor = new ExtendedTextEditor();
		textEditor.replace("Car fromString(", "CarFromString fromString(");
		ProjectHelper.cleanAllProjects();

		/* error disappeared */
		assertCountOfProblemsExists(ProblemType.ERROR, getWsProjectName(), null, null, 0);
	}
}
