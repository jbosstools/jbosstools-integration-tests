 /*******************************************************************************
  * Copyright (c) 2007-2011 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/

package org.jboss.tools.ws.ui.bot.test.rest.validation;

import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.autobuilding.AutoBuildingRequirement.AutoBuilding;
import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.jboss.tools.ws.ui.bot.test.utils.ProjectHelper;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test operates on JAX-RS validation
 * @author jjankovi
 *
 */
@RunWith(RedDeerSuite.class)
@AutoBuilding(value = false, cleanup = true)
public class RESTfulValidationTest extends RESTfulTestBase {

	private static final String GET_METHOD_PATH = "/{id}";
	private static final String CORRECT_PATH_PARAM = "id";
	private static final String BAD_PATH_PARAM = "customerId";

	@Override
	protected String getWsProjectName() {
		return "restEmpty";
	}

	@Test
	public void testCorrectValueValidation() {
		/* prepare project */
		prepareSimpleRestService(GET_METHOD_PATH, CORRECT_PATH_PARAM);
		ProjectHelper.cleanAllProjects();

		/* test count of validation errors */
		assertCountOfProblemsExists(ProblemType.ERROR, getWsProjectName(), PATH_PARAM_VALID_ERROR, null, 0);		
	}

	@Test
	public void testBadValueValidation() {
		/* prepare project */
		prepareSimpleRestService(GET_METHOD_PATH, BAD_PATH_PARAM);
		ProjectHelper.cleanAllProjects();

		/* test count of validation errors */
		assertCountOfProblemsExists(ProblemType.ERROR, getWsProjectName(), PATH_PARAM_VALID_ERROR, null, 1);
	}

	@Test
	public void testCorrectToBadValueValidation() {
		/* prepare project */
		prepareSimpleRestService(GET_METHOD_PATH, CORRECT_PATH_PARAM);
		final String pathParamPrefix = "@PathParam(\"";
		replaceInRestService(pathParamPrefix + CORRECT_PATH_PARAM,
				pathParamPrefix + BAD_PATH_PARAM);
		ProjectHelper.cleanAllProjects();

		/* test count of validation errors */
		assertCountOfProblemsExists(ProblemType.ERROR, getWsProjectName(), PATH_PARAM_VALID_ERROR,null, 1);
	}

	@Test
	public void testBadToCorrectValueValidation() {
		/* prepare project */
		prepareSimpleRestService(GET_METHOD_PATH, BAD_PATH_PARAM);
		replaceInRestService(BAD_PATH_PARAM, CORRECT_PATH_PARAM);
		ProjectHelper.cleanAllProjects();

		/* test count of validation errors */
		assertCountOfProblemsExists(ProblemType.ERROR, getWsProjectName(), PATH_PARAM_VALID_ERROR, null, 0);
	}
}
