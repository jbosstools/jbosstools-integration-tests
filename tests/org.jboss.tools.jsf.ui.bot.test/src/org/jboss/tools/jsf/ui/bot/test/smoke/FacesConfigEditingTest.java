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
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
/** Test Editing of faces-config.xml file for JSF 1.2 project
 * @author Vladimir Pakan
 *
 */
public class FacesConfigEditingTest extends AbstractFacesConfigEditingTest{
  
  @Override
  protected FacesConfigEditor getFacesConfigEditor() {
	PackageExplorer pe = new PackageExplorer();
	pe.open();
	pe.getProject(getTestProjectName()).getProjectItem("WebContent","WEB-INF",FacesConfigEditingTest.FACES_CONFIG_FILE_NAME).open();
	return new FacesConfigEditor(FacesConfigEditingTest.FACES_CONFIG_FILE_NAME);
  }
  @Override
  protected String getTestProjectName(){
    return VPEAutoTestCase.JBT_TEST_PROJECT_NAME;
  }
  @Override
  protected void intializeTestProject() {
    // This test is using default JSF 1.2 project which is already created
  }
  @Override
  protected ProjectType getTestProjectType() {
    return ProjectType.JSF;
  }
  @Override
  protected boolean getCheckForExistingManagedBeanClass() {
    return true;
  }
}
  
