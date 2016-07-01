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

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.workbench.condition.EditorWithTitleIsActive;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.junit.Before;
import org.junit.Test;

public class CDISetupWizardTest extends WizardTestBase {

	private static String CDI_VERSION = "1.1";
	private static String DEPENDENCY = "cdi-api : " + CDI_VERSION + " [provided]";
	
	@Before
	public void prepare(){
		newProject(PROJECT_NAME);
		cdiSetup(PROJECT_NAME, CDI_VERSION);
	}
	
	@Test
	public void testBeansXmlCreated(){
		ProjectExplorer pe = new ProjectExplorer();
		assertTrue("beans.xml has not been created!", pe.getProject(PROJECT_NAME)
				.containsItem("src", "main", "webapp", "WEB-INF", "beans.xml"));
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
