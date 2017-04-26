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
import static org.junit.Assert.fail;

import java.io.IOException;

import org.jboss.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.forge.ui.bot.test.util.ResourceUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests of the Forge2 'jpa-setup' wizard.
 * @author Pavol Srna
 *
 */
public class JPASetupWizardTest extends WizardTestBase {
	
	@Before
	public void prepare(){
		newProject(PROJECT_NAME);
		persistenceSetup(PROJECT_NAME);
	}
	
	@Test
	public void testPersistenceXmlCreated(){
		ProjectExplorer pe = new ProjectExplorer();
		assertTrue("persistence.xml not found in project explorer", 
					pe.getProject(PROJECT_NAME).containsResource("src", "main", "resources", "META-INF", "persistence.xml"));
		
	}
	
	@Test
	public void testPersistenceOpenedInEditor(){
		DefaultEditor e = new DefaultEditor();
		assertTrue("Persistence editor is not active", e.isActive());
		assertTrue(e.getTitle().equals("persistence.xml"));
		e.close();
	}
	
	@Test
	public void testPersistenceHasRightContent(){
		try {
			String pContent = ResourceUtils.readFile(WORKSPACE + "/" + PROJECT_NAME + "/src/main/resources/META-INF/persistence.xml");
			assertTrue(pContent.contains("<provider>org.hibernate.ejb.HibernatePersistence</provider>"));
			assertTrue(pContent.contains("<jta-data-source>java:jboss/datasources/ExampleDS</jta-data-source>"));
			assertTrue(pContent.contains("<property name=\"hibernate.hbm2ddl.auto\" value=\"create-drop\"/>"));
			assertTrue(pContent.contains("<property name=\"hibernate.show_sql\" value=\"true\"/>"));
			assertTrue(pContent.contains("<property name=\"hibernate.format_sql\" value=\"true\"/>"));
			assertTrue(pContent.contains("<property name=\"hibernate.transaction.flush_before_completion\" value=\"true\"/>"));
		} catch (IOException e) {
			e.printStackTrace();
			fail("Attempt to read the 'persistence.xml' file failed!");
		}
	}
}
