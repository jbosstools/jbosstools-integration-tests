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

package org.jboss.tools.ws.ui.bot.test.rest;

import java.util.List;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.ProjectItem;
import org.jboss.tools.ui.bot.ext.Timing;
import org.junit.Test;

public class MatrixAnnotationSupportTest extends RESTfulTestBase {

	private String projectName = "matrix1";
	private String matrixParam1 = "author";
	private String matrixParam2 = "country";
	private String matrixParamNew = "library";
	private String matrixParamType1 = "String";
	private String matrixParamType2 = "Integer";
	private String matrixParamTypeNew = "Long";

	@Override
	protected String getWsProjectName() {
		return projectName;
	}
	
	/**
	 * Fails due to JBIDE-12027
	 * 
	 * @see https://issues.jboss.org/browse/JBIDE-12027
	 */
	@Test
	public void testMatrixParamSupport() {

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<ProjectItem> restServices = restfulServicesForProject("matrix1");
		
		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);	
		assertExpectedPathOfService(restServices.get(0), 
				"/rest;" + matrixParam1 + "={" + matrixParamType1 + "};"
						+ matrixParam2 + "={" + matrixParamType2 + "}");

	}

	@Test
	public void testEditingMatrixParam() {

		/* prepare project */
		resourceHelper.replaceInEditor(editorForClass("matrix1", "src", 
				"org.rest.test", "RestService.java").toTextEditor(), 
				matrixParam1, matrixParamNew, true);
		bot.sleep(Timing.time2S());

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<ProjectItem> restServices = restfulServicesForProject("matrix1");

		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);	
		assertExpectedPathOfService(restServices.get(0), 
				"/rest;" + matrixParamNew + "={" + matrixParamType1 + "};"
						+ matrixParam2 + "={" + matrixParamType2 + "}");
	}
	
	@Test
	public void testEditingTypeOfMatrixParam() {
		
		/* prepare project */
		resourceHelper.replaceInEditor(editorForClass("matrix1", "src", 
				"org.rest.test", "RestService.java").toTextEditor(), 
				matrixParamType1, matrixParamTypeNew, true);
		bot.sleep(Timing.time2S());

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<ProjectItem> restServices = restfulServicesForProject("matrix1");
		
		/* test JAX-RS REST explorer */
		assertCountOfRESTServices(restServices, 1);	
		assertExpectedPathOfService(restServices.get(0), 
				"/rest;" + matrixParam1 + "={" + matrixParamTypeNew + "};"
						+ matrixParam2 + "={" + matrixParamType2 + "}");
	}
}
