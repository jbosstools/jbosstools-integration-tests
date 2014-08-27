/*******************************************************************************
 * Copyright (c) 2007-2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.ws.ui.bot.test.rest.param;

import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.tools.ws.reddeer.editor.ExtendedTextEditor;
import org.jboss.tools.ws.reddeer.jaxrs.core.RestService;
import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.junit.Test;

/**
 * {@link PathParam} annotation support test<br/><br/>
 * 
 * @author Radoslav Rabara
 */
public class PathParamAnnotationSupportTest extends RESTfulTestBase {

	/**
	 * Project contains method with {@link PathParam}s bound to method's {@link Path}'s parameters
	 */
	private final static String projectPath1 = "path1";
	/**
	 * Project contains field with {@link PathParam} bound to method's {@link Path}'s parameter
	 */
	private final static String projectPath2 = "path2";//uses field annotated with @PathParam
	/**
	 * Project contains field with {@link PathParam} bound to class's {@link Path}'s parameter
	 */
	private final static String projectPath3 = "path3";//uses field annotated with @PathParam

	private final String pathParam1 = "author";
	private final String pathParam2 = "country";
	private final String pathType1 = "String";
	private final String pathType2 = "Integer";

	private final String pathParam1New = "newParam1";
	private final String pathType1New = "Long";

	@Override
	public void setup() {
		
	}

	@Test
	public void testPathParamSupport() {
		/* prepare project */
		importRestWSProject(projectPath1);

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RestService> restServices = restfulServicesForProject(projectPath1);

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices.get(0),
				"/rest/{" + pathParam1 + ":" + pathType1 + "}"
						+ "/{" + pathParam2 + ":" + pathType2 + "}");
	}
	
	/**
	 * Fails due to JBIDE-17663
	 * @see https://issues.jboss.org/browse/JBIDE-17663
	 */
	@Test
	public void testPathParamFieldSupport() {
		/* prepare project */
		importRestWSProject(projectPath2);

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RestService> restServices = restfulServicesForProject(projectPath2);

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices.get(0),
				"/rest/{" + pathParam1 + ":" + pathType1 + "}");

		/* prepare project */
		importRestWSProject(projectPath3);

		/* get RESTful services from JAX-RS REST explorer for the project */
		restServices = restfulServicesForProject(projectPath3);

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService("JBIDE-17663: ", restServices.get(0),
				"/rest/{" + pathParam2 + ":" + pathType2 + "}");
	}
	
	/**
	 * Fails due to JBIDE-17663
	 * (JAX-RS Explorer doesn't reflect binding parameter to field)
	 * 
	 * @see https://issues.jboss.org/browse/JBIDE-17663
	 */
	@Test
	public void testAdvancedPathParamFieldSupport() {
		final String newPathParam = "year";
		final String newPathType = "Integer";
		/* prepare project */
		importRestWSProject(projectPath2);

		/* open RestService.java */
		openRestService(projectPath2);

		/* add new path param to @Path annotation */
		ExtendedTextEditor editor = new ExtendedTextEditor();
		editor.activate();
		editor.replaceLine("@Path(\"{author}\"", "	@Path(\"{author}/{" + newPathParam + "}\")");

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RestService> restServices = restfulServicesForProject(projectPath2);

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices.get(0),
				"/rest/{" + pathParam1 + ":" + pathType1 + "}/{"
						+ newPathParam + ":.*}");

		/* add new @PathParam */
		editor.activate();
		editor.insertBeforeLine("	@PathParam(\"" + newPathParam + "\")\n	"
				+ newPathType + " year;", "@PathParam(\"author\")");

		/* get RESTful services from JAX-RS REST explorer for the project */
		restServices = restfulServicesForProject(projectPath2);

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService("JBIDE-17663: ", restServices.get(0),
				"/rest/{" + pathParam1 + ":" + pathType1 + "}/{"
						+ newPathParam + ":" + newPathType + "}");
	}
	
	@Test
	public void testEditingPathParam() {
		/* prepare project */
		importRestWSProject(projectPath1);

		/* open RestService.java */
		openRestService(projectPath1);

		/* replace path param's value */
		ExtendedTextEditor editor = new ExtendedTextEditor();
		editor.activate();
		editor.replace(pathParam1, pathParam1New);

		AbstractWait.sleep(TimePeriod.getCustom(2));

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RestService> restServices = restfulServicesForProject(projectPath1);

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices.get(0),
				"/rest/{" + pathParam1New + ":" + pathType1 + "}"
						+ "/{" + pathParam2 + ":" + pathType2 + "}");
	}

	@Test
	public void testEditingTypeOfPathParam() {
		/* prepare project */
		importRestWSProject(projectPath1);

		/* open RestService.java */
		openRestService(projectPath1);

		/* replace path param's type */
		ExtendedTextEditor editor = new ExtendedTextEditor();
		editor.activate();
		editor.replace(pathType1, pathType1New);

		AbstractWait.sleep(TimePeriod.getCustom(2));

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RestService> restServices = restfulServicesForProject(projectPath1);

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices.get(0),
				"/rest/{" + pathParam1 + ":" + pathType1New + "}"
						+ "/{" + pathParam2 + ":" + pathType2 + "}");
	}

	private void openRestService(String projectName) {
		openJavaFile(projectName, "org.rest.test", "RestService.java");
	}
}
