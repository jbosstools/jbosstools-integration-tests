package org.jboss.tools.ws.ui.bot.test.rest.param;

import java.util.List;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.autobuilding.AutoBuildingRequirement.AutoBuilding;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.ws.reddeer.jaxrs.core.RESTfulWebService;
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
 * @see https://issues.jboss.org/browse/JBIDE-16825
 */
@RunWith(RedDeerSuite.class)
@JBossServer(state=ServerRequirementState.PRESENT)
@AutoBuilding(value = false, cleanup = true)
public class BeanParamAnnotationSupportTest extends RESTfulTestBase {

	
	// Projects containing RestService with method with {@link BeanParam}
	// bound to class Car, which contains field annotated with {@link @PathParam}
	private final String PROJECT1_NAME = "bean1";
	private final String PROJECT2_NAME = "bean2";
	private final String PROJECT3_NAME = "bean3";
	private final String PROJECT4_NAME = "bean4";
	private final String PROJECT5_NAME = "bean5";
	private final String PROJECT6_NAME = "bean6";

	private final String pathParam1 = "id";
	private final String pathType1 = "Integer";
	private final String queryParam1 = "author";
	private final String queryType1 = "String";
	private final String matrixParam1 = "country";
	private final String matrixType1 = "Long";

	@Override
	public void setup() {
		
	}

	@Test
	public void testBeanClassWithPathParamField() {
		final String pathAnnotation = "@Path(\"{" + pathParam1 + "}\")";
		final String pathParameterNotBoundToAnyFieldWarning = "The @Path template parameter 'id' is not bound to any field, property or method parameter annotated with @PathParam(\"id\").";
		final String pathParamNotBoundToPathParameterError = "@PathParam value 'id' does not match any @Path annotation template parameters in 'org.rest.test.RestService'.";
		final String pathParameterNotBoundToPathParamError = "@PathParam value 'id' in associated Parameter Aggregator 'org.rest.test.BeanClass' does not match any @Path annotation template parameters of the java method 'post' and its enclosing java type 'org.rest.test.RestService'.";

		/* prepare project */
		importWSTestProject(PROJECT1_NAME);

		/* there is no error */
		assertCountOfProblemsExists(ProblemType.ERROR, PROJECT1_NAME, null, null, 0);
		assertCountOfValidationProblemsExists(ProblemType.WARNING, PROJECT1_NAME, pathParameterNotBoundToAnyFieldWarning, null, 0);

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RESTfulWebService> restServices = restfulServicesForProject(PROJECT1_NAME);

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices.get(0),
				"/rest/{" + pathParam1 + ":" + pathType1 + "}");

		/* open RestService class */
		openJavaFile(PROJECT1_NAME, "org.rest.test", "RestService.java");
		TextEditor editor  = new TextEditor();

		/* remove @Path annotation from RestService and assert there are two errors (one in RestService and one in BeanClass */
		editor.setText(editor.getText().replace(pathAnnotation, ""));
		editor.save();
		ProjectHelper.cleanAllProjects();
		new WaitUntil(new RestServicePathsHaveUpdated(PROJECT1_NAME), TimePeriod.getCustom(3), false);
		
		assertCountOfProblemsExists(ProblemType.ERROR, PROJECT1_NAME, pathParamNotBoundToPathParameterError, null, 1);
		assertCountOfProblemsExists(ProblemType.ERROR, PROJECT1_NAME, pathParameterNotBoundToPathParamError, null, 1);
		assertCountOfProblemsExists(ProblemType.ERROR, PROJECT1_NAME, null, null, 2);

		/* add @Path annotation into RestService and assert that errors disappear */
		editor.insertLine(editor.getLineOfText("public void post("), pathAnnotation);
		editor.save();
		ProjectHelper.cleanAllProjects();
		assertCountOfValidationProblemsExists(ProblemType.ERROR, PROJECT1_NAME, null, null, 0);

		/* open BeanClass class */
		openBeanClass(PROJECT1_NAME);
		editor = new TextEditor();

		/* remove @PathParam from BeanClass and assert that there is an warning */
		editor.setText(editor.getText().replace("@PathParam", ""));
		editor.save();
		ProjectHelper.cleanAllProjects();
		assertCountOfValidationProblemsExists(ProblemType.WARNING, PROJECT1_NAME, pathParameterNotBoundToAnyFieldWarning, null, 1);
	}

	@Test
	public void testBeanClassWithQueryParamField() {
		final String defaultValue = "defVal";

		/* prepare project */
		importWSTestProject(PROJECT2_NAME);

		/* there is no error */
		assertCountOfValidationProblemsExists(ProblemType.ERROR, PROJECT2_NAME, null, null, 0);


		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RESTfulWebService> restServices = restfulServicesForProject(PROJECT2_NAME);

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices.get(0),
				"/rest?" + queryParam1 + "={" + queryType1 + "}");

		/* open BeanClass class */
		openBeanClass(PROJECT2_NAME);
		TextEditor editor = new TextEditor();
		
		/* remove @QueryParam from BeanClass and assert that endpoint URI was updated */
		editor.setText(editor.getText().replace("@QueryParam(\"author\")", ""));
		editor.save();
		ProjectHelper.cleanAllProjects();
		
		refreshRestServices(PROJECT2_NAME);
		new WaitUntil(new RestServicePathsHaveUpdated(PROJECT2_NAME), TimePeriod.getCustom(3), false);
		
		restServices = restfulServicesForProject(PROJECT2_NAME);
		assertExpectedPathOfService("unstable ", restServices.get(0), "/rest");

		/* add @QueryParam and @DefaultValue into BeanClass and assert that endpoint URI was updated */
		editor.insertLine(editor.getLineOfText(queryType1), "@QueryParam(\"" + queryParam1 + "\")");
		editor.insertLine(editor.getLineOfText("import javax.ws.rs.QueryParam;"), "import javax.ws.rs.DefaultValue;");
		editor.insertLine(editor.getLineOfText(queryType1), "@DefaultValue(\"" + defaultValue + "\")");
		editor.save();
		ProjectHelper.cleanAllProjects();
		new WaitUntil(new RestServicePathsHaveUpdated(PROJECT2_NAME), TimePeriod.SHORT, false);
		
		restServices = restfulServicesForProject(PROJECT2_NAME);
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices.get(0),
				"/rest?" + queryParam1 + "={" + queryType1 + ":\"" + defaultValue + "\"}");
	}

	@Test
	public void testBeanClassWithMatrixParamField() {
		final String defaultValue = "1";

		/* prepare project */
		importWSTestProject(PROJECT3_NAME);

		/* there is no error */
		assertCountOfValidationProblemsExists(ProblemType.ERROR, PROJECT3_NAME, null, null, 0);

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RESTfulWebService> restServices = restfulServicesForProject(PROJECT3_NAME);

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices.get(0),
				"/rest;" + matrixParam1 + "={" + matrixType1 + "}");

		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		/* open BeanClass class */
		openBeanClass(PROJECT3_NAME);
		TextEditor editor = new TextEditor();

		/* remove @MatrixParam from BeanClass and assert that endpoint URI was updated */
		editor.setText(editor.getText().replace("@MatrixParam(\"country\")", ""));
		editor.save();
		ProjectHelper.cleanAllProjects();

		refreshRestServices(PROJECT3_NAME);
		new WaitUntil(new RestServicePathsHaveUpdated(PROJECT3_NAME), TimePeriod.getCustom(3), false);
		
		restServices = restfulServicesForProject(PROJECT3_NAME);
		
		assertExpectedPathOfService("unstable ", restServices.get(0), "/rest");

		/* add @MatrixParam and @DefaultValue into BeanClass and assert that endpoint URI was updated */
		editor.insertLine(editor.getLineOfText(matrixType1),"@MatrixParam(\"" + matrixParam1 + "\")");
		editor.insertLine(editor.getLineOfText("import javax.ws.rs.MatrixParam;"),"import javax.ws.rs.DefaultValue;");
		editor.insertLine(editor.getLineOfText( matrixType1), "@DefaultValue(\"" + defaultValue + "\")");
		editor.save();
		ProjectHelper.cleanAllProjects();
		new WaitUntil(new RestServicePathsHaveUpdated(PROJECT3_NAME), TimePeriod.SHORT, false);
		
		restServices = restfulServicesForProject(PROJECT3_NAME);
		assertExpectedPathOfService(restServices.get(0),
				"/rest;" + matrixParam1 + "={" + matrixType1 + ":\"" + defaultValue + "\"}");//unstable
	}

	@Test
	public void testBeanClassWithPathParamMethod() {
		/* prepare project */
		importWSTestProject(PROJECT4_NAME);

		/* there is no error */
		assertCountOfValidationProblemsExists(ProblemType.ERROR, PROJECT4_NAME, null, null, 0);

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RESTfulWebService> restServices = restfulServicesForProject(PROJECT4_NAME);

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices.get(0),
				"/rest/{" + pathParam1 + ":" + pathType1 + "}");
	}

	@Test
	public void testBeanClassWithQueryParamMethod() {
		/* prepare project */
		importWSTestProject(PROJECT5_NAME);

		/* there is no error */
		assertCountOfValidationProblemsExists(ProblemType.ERROR, PROJECT5_NAME, null, null, 0);

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RESTfulWebService> restServices = restfulServicesForProject(PROJECT5_NAME);

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices.get(0),
				"/rest?" + queryParam1 + "={" + queryType1 + "}");
	}

	@Test
	public void testBeanClassWithMatrixParamMethod() {
		/* prepare project */
		importWSTestProject(PROJECT6_NAME);

		/* there is no error */
		assertCountOfValidationProblemsExists(ProblemType.ERROR, PROJECT6_NAME, null, null, 0);

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RESTfulWebService> restServices = restfulServicesForProject(PROJECT6_NAME);

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices.get(0),
				"/rest;" + matrixParam1 + "={" + matrixType1 + "}");
	}

	private void openBeanClass(String projectName) {
		openJavaFile(projectName, "org.rest.test", "BeanClass.java");
	}
}
