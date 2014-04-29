package org.jboss.tools.ws.ui.bot.test.rest;

import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.hamcrest.core.Is;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
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
@Require(server = @Server(type = ServerType.WildFly, state = ServerState.Present))
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
	 * Tests if filters and interceptors defined as inner class are recognized.
	 * 
	 * If they are recognized than there should be warning for each filter/interceptor
	 * because they are not registered as JAX-RS Providers (missing @Provider annotation)
	 * 
	 * Fails due to JBIDE-17178
	 * 
	 * @see https://issues.jboss.org/browse/JBIDE-17178
	 */
	@Test
	public void filterInterceptorDefinedAsInnerClassesSupportTest() {
		String projectName = "filterinterceptor1";
		importAndCheckErrors(projectName);
		assertCountOfApplicationAnnotationValidationWarnings(projectName, 4);
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
		//wait for JAX-RS validator
		new ProblemsView().open();
		bot.sleep(Timing.time2S());
		
		// there should be "No JAX-RS Activator is defined for the project." warning
		SWTBotTreeItem[] warningsBefore = restfulHelper.getRESTValidationWarnings(null);
		
		//remove @Provider annotation
		resourceHelper.replaceInEditor(
				editorForClass(projectName, "src", "org.rest.test",
						className + ".java").toTextEditor(),
						"@Provider", "", true);
		//remove unused import
		resourceHelper.replaceInEditor(
				editorForClass(projectName, "src", "org.rest.test",
						className + ".java").toTextEditor(),
						"import javax.ws.rs.ext.Provider;", "", true);
		
		//wait for JAX-RS validator
		new ProblemsView().open();
		bot.sleep(Timing.time2S());

		//one more warning Description "The @Provider annotation is missing on this java type."
		SWTBotTreeItem[] warningsAfter = restfulHelper.getRESTValidationWarnings(null);
		assertThat("Expected one more warnings.\nBefore: "
				+ Arrays.toString(warningsBefore) + "\nAfter: "
				+ Arrays.toString(warningsAfter), warningsAfter.length - warningsBefore.length, Is.is(1));
		
		//there should be no error
		assertCountOfApplicationAnnotationValidationErrors(projectName, 0);
	}
}
