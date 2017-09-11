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
package org.jboss.tools.forge.ui.bot.console.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.jboss.tools.forge.ui.bot.test.suite.ForgeConsoleTestBase;
import org.jboss.tools.forge.ui.bot.test.util.ResourceUtils;
import org.eclipse.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.junit.Test;
/**
 * 
 * @author psrna
 *
 */
@CleanWorkspace
public class PersistenceTest extends ForgeConsoleTestBase {

	@Test
	public void hibernateJBossAS7(){
		
		createProject();
		createPersistence("HIBERNATE", "JBOSS_AS7");
		
		DefaultEditor editor = new DefaultEditor();
		assertTrue("Persistence editor is not active", editor.isActive());
		assertTrue(editor.getTitle().equals("persistence.xml"));
		editor.close();
		
		File persistence = new File(WORKSPACE + "/" + PROJECT_NAME + "/src/main/resources/META-INF/persistence.xml");
		assertTrue("persistence.xml file does not exist", persistence.exists());
		assertTrue("persistence.xml not found in project explorer", 
					pExplorer.getProject(PROJECT_NAME).containsResource("src", "main", "resources", "META-INF", "persistence.xml"));
			
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
	
	
	@Test
	public void openjpaJBossAS7(){
		
		createProject();
		createPersistence("OPENJPA", "JBOSS_AS7");
		
		DefaultEditor editor = new DefaultEditor();
		assertTrue("Persistence editor is not active", editor.isActive());
		assertTrue(editor.getTitle().equals("persistence.xml"));
		editor.close();
		
		File persistence = new File(WORKSPACE + "/" + PROJECT_NAME + "/src/main/resources/META-INF/persistence.xml");
		assertTrue("persistence.xml file does not exist", persistence.exists());
		assertTrue("persistence.xml not found in project explorer", 
					pExplorer.getProject(PROJECT_NAME).containsResource("src", "main", "resources", "META-INF", "persistence.xml"));
			
		try {
			String pContent = ResourceUtils.readFile(WORKSPACE + "/" + PROJECT_NAME + "/src/main/resources/META-INF/persistence.xml");
			assertTrue(pContent.contains("<provider>org.apache.openjpa.persistence.PersistenceProviderImpl</provider>"));
			assertTrue(pContent.contains("<jta-data-source>java:jboss/datasources/ExampleDS</jta-data-source>"));
		} catch (IOException e) {
			e.printStackTrace();
			fail("Attempt to read the 'persistence.xml' file failed!");
		}

	}
	
	@Test
	public void eclipselinkJBossAS7(){
		
		createProject();
		createPersistence("ECLIPSELINK", "JBOSS_AS7");
		
		DefaultEditor editor = new DefaultEditor();
		assertTrue("Persistence editor is not active", editor.isActive());
		assertTrue(editor.getTitle().equals("persistence.xml"));
		editor.close();
		
		File persistence = new File(WORKSPACE + "/" + PROJECT_NAME + "/src/main/resources/META-INF/persistence.xml");
		assertTrue("persistence.xml file does not exist", persistence.exists());
		assertTrue("persistence.xml not found in project explorer", 
					pExplorer.getProject(PROJECT_NAME).containsResource("src", "main", "resources", "META-INF", "persistence.xml"));
		
		try {
			String pContent = ResourceUtils.readFile(WORKSPACE + "/" + PROJECT_NAME + "/src/main/resources/META-INF/persistence.xml");
			assertTrue(pContent.contains("<provider>org.eclipse.persistence.jpa.PersistenceProvider</provider>"));
			assertTrue(pContent.contains("<jta-data-source>java:jboss/datasources/ExampleDS</jta-data-source>"));
			assertTrue(pContent.contains("<property name=\"eclipselink.ddl-generation\" value=\"drop-and-create-tables\"/>"));
		} catch (IOException e) {
			e.printStackTrace();
			fail("Attempt to read the 'persistence.xml' file failed!");
		}
	}
	
	@Test
	public void infinispanJBossAS7(){

		createProject();
		createPersistence("INFINISPAN", "JBOSS_AS7");
		
		DefaultEditor editor = new DefaultEditor();
		assertTrue("Persistence editor is not active", editor.isActive());
		assertTrue(editor.getTitle().equals("persistence.xml"));
		editor.close();
		
		File persistence = new File(WORKSPACE + "/" + PROJECT_NAME + "/src/main/resources/META-INF/persistence.xml");
		assertTrue("persistence.xml file does not exist", persistence.exists());
		assertTrue("persistence.xml not found in project explorer", 
					pExplorer.getProject(PROJECT_NAME).containsResource("src", "main", "resources", "META-INF", "persistence.xml"));
		
		try {
			String pContent = ResourceUtils.readFile(WORKSPACE + "/" + PROJECT_NAME + "/src/main/resources/META-INF/persistence.xml");
			assertTrue(pContent.contains("<provider>org.hibernate.ogm.HibernateOgmPersistence</provider>"));
			assertTrue(pContent.contains("<jta-data-source>java:jboss/datasources/ExampleDS</jta-data-source>"));
			assertTrue(pContent.contains("<property name=\"hibernate.dialect\" value=\"org.hibernate.ogm.dialect.NoopDialect\"/>"));
		} catch (IOException e) {
			e.printStackTrace();
			fail("Attempt to read the 'persistence.xml' file failed!");
		}
	}
}
