/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.forge2.ui.bot.wizard.test;

import static org.junit.Assert.assertTrue;

import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.workbench.condition.EditorWithTitleIsActive;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.junit.Before;
import org.junit.Test;

public class FacesSetupWizardTest extends WizardTestBase {

	private static String JSF_VERSION = "2.2";
	private static String DEPENDENCY = "javax.faces-api : 2.2 [provided]";
	
	@Before
	public void prepare(){
		newProject(PROJECT_NAME);
		facesSetup(PROJECT_NAME, JSF_VERSION);
	}
	
	@Test
	public void testFacesConfigXmlCreated(){
		ProjectExplorer pe = new ProjectExplorer();
		assertTrue("faces-config.xml has not been created!", pe.getProject(PROJECT_NAME)
				.containsResource("src", "main", "webapp", "WEB-INF", "faces-config.xml"));
	}
	
	@Test
	public void testDependenciesAddedToPom(){
		ProjectExplorer pe = new ProjectExplorer();
		pe.getProject(PROJECT_NAME).getTreeItem().getItem("pom.xml").doubleClick();
		new WaitUntil(new EditorWithTitleIsActive(PROJECT_NAME + "/pom.xml"));
		new DefaultEditor(PROJECT_NAME + "/pom.xml").activate();
		new DefaultCTabItem("Dependencies").activate();
		assertTrue("Dependency: '" + DEPENDENCY + "' not added!", new DefaultTable(1).containsItem(DEPENDENCY));
	}
	
}
