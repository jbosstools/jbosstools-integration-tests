/*******************************************************************************
 * Copyright (c) 2007-2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jsf.ui.bot.test.smoke;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.tools.jsf.reddeer.ProjectType;
import org.jboss.tools.jsf.reddeer.ui.editor.FacesConfigEditor;

/**
 * Test Editing of faces-config.xml file for JSF 2.0 project
 * 
 * @author Vladimir Pakan
 *
 */
public class FacesConfigEditingTestJSF2 extends AbstractFacesConfigEditingTest {

	@Override
	protected FacesConfigEditor getFacesConfigEditor() {
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(getTestProjectName())
				.getProjectItem("WebContent", "WEB-INF", FacesConfigEditingTest.FACES_CONFIG_FILE_NAME).open();
		return new FacesConfigEditor(FacesConfigEditingTestJSF2.FACES_CONFIG_FILE_NAME);
	}

	@Override
	protected String getTestProjectName() {
		return JSF2_TEST_PROJECT_NAME;
	}

	@Override
	protected void intializeTestProject() {
		createJSF2Project(getTestProjectName());
	}

	@Override
	protected ProjectType getTestProjectType() {
		return ProjectType.JSF2;
	}

	@Override
	protected boolean getCheckForExistingManagedBeanClass() {
		return false;
	}
}
