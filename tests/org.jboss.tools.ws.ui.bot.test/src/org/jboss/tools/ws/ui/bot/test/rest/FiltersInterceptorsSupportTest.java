package org.jboss.tools.ws.ui.bot.test.rest;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.core.Is;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.condition.ExactNumberOfProblemsExists;
import org.jboss.reddeer.eclipse.condition.ProblemExists;
import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.autobuilding.AutoBuildingRequirement.AutoBuilding;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.tools.ws.reddeer.editor.ExtendedTextEditor;
import org.jboss.tools.ws.ui.bot.test.utils.ProjectHelper;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Testing support for custom Name-Binding annotation
 * 
 * Run with J2EE7+ server
 * 
 * @author Radoslav Rabara
 * 
 * @see http://tools.jboss.org/documentation/whatsnew/jbosstools/4.2.0.Beta1.html#webservices
 * @since JBT 4.2.0.Beta1
 */
@RunWith(RedDeerSuite.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.WILDFLY)
@AutoBuilding(value = false, cleanup = true)
public class FiltersInterceptorsSupportTest extends RESTfulTestBase {

	@Override
	public void setup() {
		// no setup required
	}

	@Test
	public void requestFilterSupportTest() {
		filterSupportTest("filter1");
	}

	@Test
	public void responseFilterSupportTest() {
		filterSupportTest("filter2");
	}

	@Test
	public void readInterceptorSupportTest() {
		interceptorSupportTest("interceptor1");
	}

	@Test
	public void writeInterceptorSupportTest() {
		interceptorSupportTest("interceptor2");
	}

	@Test
	public void filterInterceptorDefinedAsInnerClassesSupportTest() {
		String projectName = "filterinterceptor1";
		importAndCheckErrors(projectName);
		ProjectHelper.cleanAllProjects();
		assertCountOfValidationProblemsExists(ProblemType.WARNING, projectName, null, null, 4);
	}

	private void filterSupportTest(String projectName) {
		importAndCheckErrors(projectName);
		providerValidationWarning(projectName, "Filter");
	}

	private void interceptorSupportTest(String projectName) {
		importAndCheckErrors(projectName);
		providerValidationWarning(projectName, "Interceptor");
	}

	private void providerValidationWarning(String projectName, String className) {
		ProjectHelper.cleanAllProjects();
		/* wait for JAX-RS validator */
		new WaitUntil(new ProblemExists(ProblemType.ANY),
				TimePeriod.NORMAL, false);

		// there should be "No JAX-RS Activator is defined for the project." warning
		List<Problem> warningsBefore = new ProblemsView().getProblems(ProblemType.WARNING);
		List<String> warningsBeforeStrings = new ArrayList<String>();
		for (Problem warning: warningsBefore) {
			warningsBeforeStrings.add(warning.toString());
		}

		//remove @Provider annotation
		openJavaFile(projectName, "org.rest.test", className + ".java");
		new ExtendedTextEditor().removeLine("@Provider");

		//remove unused import
		new ExtendedTextEditor().removeLine("import javax.ws.rs.ext.Provider;");
		new ExtendedTextEditor().save();
		ProjectHelper.cleanAllProjects();

		/* wait for JAX-RS validator */
		new WaitUntil(new ExactNumberOfProblemsExists(ProblemType.WARNING,
				warningsBefore.size()+1), TimePeriod.NORMAL, false);

		//one more warning Description "The @Provider annotation is missing on this java type."
		List<Problem> warningsAfter = new ProblemsView().getProblems(ProblemType.WARNING);
		assertThat("Expected one more warnings.\nBefore: "
				+ Arrays.toString(warningsBeforeStrings.toArray()) + "\nAfter: "
				+ Arrays.toString(warningsAfter.toArray()), warningsAfter.size() - warningsBefore.size(), Is.is(1));
		
		/* there should be no error */
		assertCountOfProblemsExists(ProblemType.ERROR, projectName, null, null, 0);
	}
}
