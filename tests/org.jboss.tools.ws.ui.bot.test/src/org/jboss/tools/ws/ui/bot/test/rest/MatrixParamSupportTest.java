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

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ws.ui.bot.test.ti.wizard.RESTFullExplorerWizard;
import org.junit.Test;

public class MatrixParamSupportTest extends RESTfulTestBase {

	private RESTFullExplorerWizard restfulWizard = null;
	private String projectName = "restEmpty";
	private final String MATRIX_PARAM_RESOURCE = "/resources/restful/MatrixParam.java.ws";

	private String matrixParam1 = "author";
	private String matrixParam2 = "country";
	private String matrixParamNew = "library";
	private String matrixParamType1 = "java.lang.String";
	private String matrixParamType2 = "java.lang.Integer";
	private String matrixParamTypeNew = "java.lang.Long";

	@Override
	protected String getWsProjectName() {
		return projectName;
	}

	@Override
	public void cleanup() {
		bot.activeEditor().toTextEditor().save();
	}

	@Test
	public void testMatrixParamSupport() {

		prepareWSResource(MATRIX_PARAM_RESOURCE, getWsPackage(), getWsName(),
				matrixParam1, matrixParamType1, matrixParam2, matrixParamType2);

		restfulWizard = new RESTFullExplorerWizard(getWsProjectName());
		SWTBotTreeItem[] restServices = restfulWizard.getAllRestServices();

		assertTrue("There should be one RESTful web service instead of "
				+ restServices.length, restServices.length == 1);
		String path = restfulWizard.getPathForRestFulService(restServices[0]);
		assertEquals("/rest;" + matrixParam1 + "={" + matrixParamType1 + "};"
				+ matrixParam2 + "={" + matrixParamType2 + "}", path);

	}

	@Test
	public void testEditingMatrixParam() {

		prepareWSResource(MATRIX_PARAM_RESOURCE, getWsPackage(), getWsName(),
				matrixParam1, matrixParamType1, matrixParam2, matrixParamType2);

		resourceHelper.replaceInEditor(bot.activeEditor().toTextEditor(),
				matrixParam1, matrixParamNew, false);
		bot.sleep(Timing.time2S());

		restfulWizard = new RESTFullExplorerWizard(getWsProjectName());
		SWTBotTreeItem[] restServices = restfulWizard.getAllRestServices();

		assertTrue("There should be one RESTful web service instead of "
				+ restServices.length, restServices.length == 1);
		String path = restfulWizard.getPathForRestFulService(restServices[0]);
		assertEquals("/rest;" + matrixParamNew + "={" + matrixParamType1 + "};"
				+ matrixParam2 + "={" + matrixParamType2 + "}", path);

		resourceHelper.replaceInEditor(bot.activeEditor().toTextEditor(),
				matrixParamNew, matrixParam1, false);
		resourceHelper.replaceInEditor(bot.activeEditor().toTextEditor(),
				matrixParam2, matrixParamNew, false);
		bot.sleep(Timing.time2S());
		restfulWizard = new RESTFullExplorerWizard(getWsProjectName());
		restServices = restfulWizard.getAllRestServices();

		assertTrue("There should be one RESTful web service instead of "
				+ restServices.length, restServices.length == 1);
		path = restfulWizard.getPathForRestFulService(restServices[0]);
		assertEquals("/rest;" + matrixParam1 + "={" + matrixParamType1 + "};"
				+ matrixParamNew + "={" + matrixParamType2 + "}", path);

	}
	
	@Test
	public void testEditingTypeOfMatrixParam() {
		
		prepareWSResource(MATRIX_PARAM_RESOURCE, getWsPackage(), getWsName(),
				matrixParam1, matrixParamType1, matrixParam2, matrixParamType2);

		resourceHelper.replaceInEditor(bot.activeEditor().toTextEditor(),
				matrixParamType1, matrixParamTypeNew, false);
		bot.sleep(Timing.time2S());
		
		restfulWizard = new RESTFullExplorerWizard(getWsProjectName());
		SWTBotTreeItem[] restServices = restfulWizard.getAllRestServices();
		
		assertTrue("There should be one RESTful web service instead of " + 
					restServices.length, restServices.length == 1);
		String path = restfulWizard.getPathForRestFulService(restServices[0]);
		assertEquals("/rest;" + matrixParam1 + "={" + matrixParamTypeNew + "};"
				+ matrixParam2 + "={" + matrixParamType2 + "}", path);
		
		
		resourceHelper.replaceInEditor(bot.activeEditor().toTextEditor(), 
				matrixParamTypeNew, matrixParamType1, false);
		resourceHelper.replaceInEditor(bot.activeEditor().toTextEditor(), 
				matrixParamType2, matrixParamTypeNew, false);
		bot.sleep(Timing.time2S());
		restfulWizard = new RESTFullExplorerWizard(getWsProjectName());
		restServices = restfulWizard.getAllRestServices();
		
		assertTrue("There should be one RESTful web service instead of " + 
					restServices.length, restServices.length == 1);
		path = restfulWizard.getPathForRestFulService(restServices[0]);
		assertEquals("/rest;" + matrixParam1 + "={" + matrixParamType1 + "};"
					+ matrixParam2 + "={" + matrixParamTypeNew + "}", path);
		
	}

	private void prepareWSResource(String streamPath, Object... parameters) {

		packageExplorer.openFile(getWsProjectName(), "src", getWsPackage(),
				getWsName() + ".java").toTextEditor();
		resourceHelper.copyResourceToClassWithSave(
				bot.editorByTitle(getWsName() + ".java"),
				QueryParamSupportTest.class.getResourceAsStream(streamPath),
				false, false, parameters);
		bot.sleep(Timing.time2S());

	}

}
