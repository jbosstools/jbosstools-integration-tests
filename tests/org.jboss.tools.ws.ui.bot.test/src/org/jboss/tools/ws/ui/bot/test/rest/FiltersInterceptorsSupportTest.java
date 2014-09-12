package org.jboss.tools.ws.ui.bot.test.rest;

import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.core.Is;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.condition.ProblemsExists;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.ws.reddeer.editor.ExtendedTextEditor;
import org.jboss.tools.ws.reddeer.swt.condition.ProblemsCount;
import org.junit.Test;

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
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.WILDFLY)
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

	/**
	 * Resolved - JBIDE-17178
	 * (Problem with filters and interceptors defined as inner classes)
	 * 
	 * @see https://issues.jboss.org/browse/JBIDE-17178
	 */
	@Test
	public void filterInterceptorDefinedAsInnerClassesSupportTest() {
		String projectName = "filterinterceptor1";
		importAndCheckErrors(projectName);
		assertCountOfValidationWarnings(projectName, 4, "JBIDE-17178");
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
		/* wait for JAX-RS validator */
		new WaitUntil(new ProblemsExists(ProblemsExists.ProblemType.WARNING),
				TimePeriod.NORMAL, false);

		// there should be "No JAX-RS Activator is defined for the project." warning
		List<TreeItem> warningsBefore = new ProblemsView().getAllWarnings();

		//remove @Provider annotation
		openJavaFile(projectName, "org.rest.test", className + ".java");
		new ExtendedTextEditor().removeLine("@Provider");

		//remove unused import
		new ExtendedTextEditor().removeLine("import javax.ws.rs.ext.Provider;");

		/* wait for JAX-RS validator */
		new WaitUntil(new ProblemsCount(ProblemsCount.ProblemType.WARNING,
				warningsBefore.size()+1), TimePeriod.NORMAL, false);

		//one more warning Description "The @Provider annotation is missing on this java type."
		List<TreeItem> warningsAfter = new ProblemsView().getAllWarnings();
		assertThat("Expected one more warnings.\nBefore: "
				+ Arrays.toString(warningsBefore.toArray()) + "\nAfter: "
				+ Arrays.toString(warningsAfter.toArray()), warningsAfter.size() - warningsBefore.size(), Is.is(1));
		
		/* there should be no error */
		assertCountOfErrors(projectName, 0);
	}
}
