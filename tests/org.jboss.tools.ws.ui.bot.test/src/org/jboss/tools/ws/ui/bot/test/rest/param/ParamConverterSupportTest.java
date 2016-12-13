package org.jboss.tools.ws.ui.bot.test.rest.param;

import javax.ws.rs.PathParam;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardPage;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.autobuilding.AutoBuildingRequirement.AutoBuilding;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.jboss.tools.ws.ui.bot.test.utils.ProjectHelper;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * Testing support for JAX-RS ParamConverterProvider
 * 
 * Run with J2EE7+ server
 * 
 * @author Radoslav Rabara
 * @since JBT 4.2.0 Beta3
 * @see https://issues.jboss.org/browse/JBIDE-16763
 */
@RunWith(RedDeerSuite.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.WILDFLY)
@AutoBuilding(value = false, cleanup = true)
public class ParamConverterSupportTest extends RESTfulTestBase {
	
	/**
	 * Project contains RestService with @POST method with {@link PathParam}
	 * bound to class Car
	 */
	final String PROJECT1_NAME = "paramConverter";
	
	@Override
	public void setup() {
		
	}
	
	@Test
	public void testParamConverterSupport() {
		/* prepare project */
		importWSTestProject(PROJECT1_NAME);
		ProjectHelper.cleanAllProjects();
		
		/* assert that type Car is not valid for the parameter */
		assertCountOfValidationProblemsExists(ProblemType.ERROR, PROJECT1_NAME, null, null, 1);
		assertCountOfValidationProblemsExists(ProblemType.ERROR, PROJECT1_NAME, 
				"The type 'org.rest.test.Car' is not valid for this parameter. "
				+ "See JAX-RS 2.0 Specification (section 3.2) for more information. ", null, 1);
		
		/* create class implementing ParamConverterProvider */
		createParamConverter();
		ProjectHelper.cleanAllProjects();
		
		/* there is no error anymore */
		assertCountOfValidationProblemsExists(ProblemType.ERROR, PROJECT1_NAME, null, null, 0);
	}
	
	private void createParamConverter() {
		NewJavaClassWizardDialog newJavaClassDialog = new NewJavaClassWizardDialog();
		newJavaClassDialog.open();
		NewJavaClassWizardPage newJavaClassPage = new NewJavaClassWizardPage();
		newJavaClassPage.setPackage("org.rest.test");
		newJavaClassPage.setName("Converter");
		newJavaClassDialog.finish();
		
		TextEditor editor = new TextEditor();
		editor.activate();
		editor.setText(PARAM_CONVERTER_PROVIDER_CLASS_TEXT);
		editor.save();
	}
	
	private final String PARAM_CONVERTER_PROVIDER_CLASS_TEXT = 
			"package org.rest.test;\n"
			+ "import java.lang.annotation.Annotation;\n"
					+ "import java.lang.reflect.Type;\n\n"
					+ "import javax.ws.rs.ext.ParamConverter;\n"
					+ "import javax.ws.rs.ext.Provider;\n"
					+ "import javax.ws.rs.ext.ParamConverterProvider;\n\n"

					+ "@Provider\n"
					+ "public class Converter implements ParamConverterProvider {\n"
					+ "   @Override\n   public <T> ParamConverter<T> getConverter(Class<T> arg0, Type arg1, Annotation[] arg2) {\n"
					+ "      return null;\n"
					+ "   }\n"
					+ "}\n";
}
