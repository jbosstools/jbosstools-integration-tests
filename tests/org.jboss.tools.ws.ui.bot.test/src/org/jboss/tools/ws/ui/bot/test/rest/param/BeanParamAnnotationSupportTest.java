package org.jboss.tools.ws.ui.bot.test.rest.param;

import java.util.List;

import javax.ws.rs.MatrixParam;
import javax.ws.rs.PathParam;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.ProjectItem;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.jboss.tools.ws.reddeer.editor.ExtendedTextEditor;
import org.jboss.tools.ws.reddeer.jaxrs.core.RestService;
import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.junit.Test;

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
@Require(server = @Server(type = ServerType.WildFly, state = ServerState.Present))
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.WILDFLY)
public class BeanParamAnnotationSupportTest extends RESTfulTestBase {

	/**
	 * Project contains RestService with method with {@link BeanParam}
	 * bound to class Car, which contains field annotated with {@link @PathParam}
	 */
	private final String PROJECT1_NAME = "bean1";

	/**
	 * Project contains RestService with method with {@link BeanParam}
	 * bound to class Car, which contains field annotated with {@link @QueryParam}
	 */
	private final String PROJECT2_NAME = "bean2";

	/**
	 * Project contains RestService with method with {@link BeanParam}
	 * bound to class Car, which contains field annotated with {@link MatrixParam}
	 */
	private final String PROJECT3_NAME = "bean3";

	/**
	 * Project contains RestService with method with {@link BeanParam}
	 * bound to class Car, which contains method annotated with {@link PathParam}
	 */
	private final String PROJECT4_NAME = "bean4";

	/**
	 * Project contains RestService with method with {@link BeanParam}
	 * bound to class Car, which contains method annotated with {@link PathParam}
	 */
	private final String PROJECT5_NAME = "bean5";

	/**
	 * Project contains RestService with method with {@link BeanParam}
	 * bound to class Car, which contains method annotated with {@link PathParam}
	 */
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

	/**
	 * Fails due to JBIDE-17796
	 * (BeanParam: unbound @PathParam error is present after the problem had been fixed)
	 * 
	 * @see https://issues.jboss.org/browse/JBIDE-17796
	 */
	@Test
	public void testBeanClassWithPathParamField() {
		final String pathAnnotation = "@Path(\"{" + pathParam1 + "}\")";
		final String pathParameterNotBoundToAnyFieldWarning = "The @Path template parameter 'id' is not bound to any field, property or method parameter annotated with @PathParam(\"id\").";
		final String pathParamNotBoundToPathParameterError = "@PathParam value 'id' does not match any @Path annotation template parameters of the java method 'post' and its enclosing java type 'org.rest.test.RestService'.";
		final String pathParameterNotBoundToPathParamError = "@PathParam value 'id' in associated Parameter Aggregator 'org.rest.test.BeanClass' does not match any @Path annotation template parameters of the java method 'post' and its enclosing java type 'org.rest.test.RestService'.";
		/* prepare project */
		importRestWSProject(PROJECT1_NAME);

		/* there is no error */
		assertCountOfValidationErrors(PROJECT1_NAME, 0);
		assertCountOfValidationWarnings(PROJECT1_NAME,  pathParameterNotBoundToAnyFieldWarning, 0);

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RestService> restServices = restfulServicesForProject(PROJECT1_NAME);

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices.get(0),
				"/rest/{" + pathParam1 + ":" + pathType1 + "}");

		/* open RestService class */
		openJavaFile(PROJECT1_NAME, "org.rest.test", "RestService.java");
		ExtendedTextEditor editor  = new ExtendedTextEditor();

		/* remove @Path annotation from RestService and assert there are two errors (one in RestService and one in BeanClass */
		editor.removeLine(pathAnnotation);
		assertCountOfValidationErrors(PROJECT1_NAME, pathParamNotBoundToPathParameterError, 1);
		assertCountOfValidationErrors(PROJECT1_NAME, pathParameterNotBoundToPathParamError, 1);
		assertCountOfValidationErrors(PROJECT1_NAME, 2);

		/* add @Path annotation into RestService and assert that errors disappear */
		editor.insertBeforeLine(pathAnnotation, "public void post(");
		assertCountOfValidationErrors(PROJECT1_NAME, 0, "JBIDE-17796");

		/* open BeanClass class */
		openBeanClass(PROJECT2_NAME);
		editor = new ExtendedTextEditor();

		/* remove @PathParam from BeanClass and assert that there is an warning */
		editor.removeLine("@PathParam");
		assertCountOfValidationWarnings(PROJECT1_NAME, pathParameterNotBoundToAnyFieldWarning, 1);
	}

	@Test
	public void testBeanClassWithQueryParamField() {
		final String defaultValue = "defVal";

		/* prepare project */
		importRestWSProject(PROJECT2_NAME);

		/* there is no error */
		assertCountOfValidationErrors(PROJECT2_NAME, 0);

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RestService> restServices = restfulServicesForProject(PROJECT2_NAME);

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices.get(0),
				"/rest?" + queryParam1 + "={" + queryType1 + "}");

		/* open BeanClass class */
		openBeanClass(PROJECT2_NAME);
		ExtendedTextEditor editor = new ExtendedTextEditor();
		
		/* remove @QueryParam from BeanClass and assert that endpoint URI was updated */
		editor.removeLine("@QueryParam");

		restServices = restfulServicesForProject(PROJECT2_NAME);
		assertExpectedPathOfService("unstable ", restServices.get(0), "/rest");

		/* add @QueryParam and @DefaultValue into BeanClass and assert that endpoint URI was updated */
		editor.insertBeforeLine("@QueryParam(\"" + queryParam1 + "\")", queryType1);
		editor.insertBeforeLine("import javax.ws.rs.DefaultValue;", "import javax.ws.rs.QueryParam;");
		editor.insertBeforeLine("@DefaultValue(\"" + defaultValue + "\")", queryType1);
		restServices = restfulServicesForProject(PROJECT2_NAME);
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices.get(0),
				"/rest?" + queryParam1 + "={" + queryType1 + ":\"" + defaultValue + "\"}");
	}

	@Test
	public void testBeanClassWithMatrixParamField() {
		final String defaultValue = "1";

		/* prepare project */
		importRestWSProject(PROJECT3_NAME);

		/* there is no error */
		assertCountOfValidationErrors(PROJECT3_NAME, 0);

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RestService> restServices = restfulServicesForProject(PROJECT3_NAME);

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices.get(0),
				"/rest;" + matrixParam1 + "={" + matrixType1 + "}");

		/* open BeanClass class */
		openBeanClass(PROJECT3_NAME);
		ExtendedTextEditor editor = new ExtendedTextEditor();

		/* remove @MatrixParam from BeanClass and assert that endpoint URI was updated */
		editor.removeLine("@MatrixParam");

		restServices = restfulServicesForProject(PROJECT3_NAME);
		assertExpectedPathOfService("unstable ", restServices.get(0), "/rest");

		/* add @MatrixParam and @DefaultValue into BeanClass and assert that endpoint URI was updated */
		editor.insertBeforeLine("@MatrixParam(\"" + matrixParam1 + "\")", matrixType1);
		editor.insertBeforeLine("import javax.ws.rs.DefaultValue;", "import javax.ws.rs.MatrixParam;");
		editor.insertBeforeLine("@DefaultValue(\"" + defaultValue + "\")", matrixType1);
		restServices = restfulServicesForProject(PROJECT3_NAME);
		assertExpectedPathOfService(restServices.get(0),
				"/rest;" + matrixParam1 + "={" + matrixType1 + ":\"" + defaultValue + "\"}");//unstable
	}

	@Test
	public void testBeanClassWithPathParamMethod() {
		/* prepare project */
		importRestWSProject(PROJECT4_NAME);

		/* there is no error */
		assertCountOfValidationErrors(PROJECT4_NAME, 0);

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RestService> restServices = restfulServicesForProject(PROJECT4_NAME);

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices.get(0),
				"/rest/{" + pathParam1 + ":" + pathType1 + "}");
	}

	@Test
	public void testBeanClassWithQueryParamMethod() {
		/* prepare project */
		importRestWSProject(PROJECT5_NAME);

		/* there is no error */
		assertCountOfValidationErrors(PROJECT5_NAME, 0);

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RestService> restServices = restfulServicesForProject(PROJECT5_NAME);

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices.get(0),
				"/rest?" + queryParam1 + "={" + queryType1 + "}");
	}

	@Test
	public void testBeanClassWithMatrixParamMethod() {
		/* prepare project */
		importRestWSProject(PROJECT6_NAME);

		/* there is no error */
		assertCountOfValidationErrors(PROJECT6_NAME, 0);

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RestService> restServices = restfulServicesForProject(PROJECT6_NAME);

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices.get(0),
				"/rest;" + matrixParam1 + "={" + matrixType1 + "}");
	}

	private void openBeanClass(String projectName) {
		openJavaFile(projectName, "org.rest.test", "BeanClass.java");
	}
}
