/*******************************************************************************
 * Copyright (c) 2007-2012 Red Hat, Inc.
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

import javax.ws.rs.MatrixParam;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.ws.reddeer.jaxrs.core.RESTfulWebService;
import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * {@link MatrixParam} annotation support test<br/><br/>
 * 
 * @author jjankovi
 * @author Radoslav Rabara
 */
@RunWith(RedDeerSuite.class)
public class MatrixParamAnnotationSupportTest extends RESTfulTestBase {

	private final String projectName = "matrix1";
	private final String project2Name = "matrix2";
	
	private final String matrixParam1 = "author";
	private final String matrixParam2 = "country";
	private final String matrixParamNew = "library";
	private final String matrixParamType1 = "String";
	private final String matrixParamType2 = "Integer";
	private final String matrixParamTypeNew = "Long";

	@Override
	protected String getWsProjectName() {
		return projectName;
	}

	@Test
	public void testMatrixParamSupport() {
		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RESTfulWebService> restServices = restfulServicesForProject(projectName);

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices.get(0),
				"/rest;" + matrixParam1 + "={" + matrixParamType1 + "};"
						+ matrixParam2 + "={" + matrixParamType2 + "}");
	}
	
	@Test
	public void testMatrixParamFieldSupport() {
		/* prepare project */
		importWSTestProject(project2Name);

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RESTfulWebService> restServices = restfulServicesForProject(project2Name);

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices.get(0),
				"/rest;" + matrixParam1 + "={" + matrixParamType1 + "};"
						+ matrixParam2 + "={" + matrixParamType2 + "}");
	}

	@Test
	public void testEditingMatrixParam() {
		/* prepare project */
		replaceInRestService(matrixParam1, matrixParamNew);
		new WaitUntil(new RestServicePathsHaveUpdated(projectName), TimePeriod.getCustom(2), false);
		refreshRestServices(projectName);

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RESTfulWebService> restServices = restfulServicesForProject(projectName);

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices.get(0),
				"/rest;" + matrixParamNew + "={" + matrixParamType1 + "};"
						+ matrixParam2 + "={" + matrixParamType2 + "}");
	}

	@Test
	public void testEditingTypeOfMatrixParam() {
		/* prepare project */
		replaceInRestService(matrixParamType1, matrixParamTypeNew);
		new WaitUntil(new RestServicePathsHaveUpdated(projectName), TimePeriod.getCustom(2), false);

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RESTfulWebService> restServices = restfulServicesForProject(projectName);

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);
		assertExpectedPathOfService(restServices.get(0),
				"/rest;" + matrixParam1 + "={" + matrixParamTypeNew + "};"
						+ matrixParam2 + "={" + matrixParamType2 + "}");
	}
}
