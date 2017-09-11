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

import org.eclipse.reddeer.eclipse.core.resources.ProjectItem;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.junit.Before;
import org.junit.Test;

/**
 * Class for testing JPA entity generation
 * @author Jan Richter
 *
 */
public class JPAEntityWizardTest extends WizardTestBase {

	private static final String ENTITY_NAME = "Customer";
	private static final String TABLE_NAME = "Customers";
	private static final String PACKAGE_NAME = GROUPID + ".model";

	@Before
	public void prepare() {
		newProject(PROJECT_NAME);
		persistenceSetup(PROJECT_NAME);
	}

	/**
	 * Test new JPA entity creation
	 */
	@Test
	public void testNewEntity() {
		newEntity();
		ProjectItem entityItem = new ProjectExplorer().getProject(PROJECT_NAME)
				.getProjectItem("Java Resources", "src/main/java", PACKAGE_NAME, ENTITY_NAME + ".java");
		assertTrue("New entity is not selected", entityItem.isSelected());
		
		TextEditor editor = new TextEditor();
		assertTrue("Java editor is not active", editor.isActive());
		assertTrue("Editor title does not match class name", editor.getTitle().equals(ENTITY_NAME + ".java"));
		
		String contents = editor.getText();
		assertTrue("Entity annotation missing", contents.contains("@Entity"));
		assertTrue("Table name annotation mismatch", contents.contains("@Table(name = \"" + TABLE_NAME + "\")"));
		assertTrue("Class definition mismatch", contents.contains("public class " + ENTITY_NAME));
		editor.close();
	}

	private void newEntity() {
		newJPAEntity(PROJECT_NAME, ENTITY_NAME, TABLE_NAME, PACKAGE_NAME);
	}
}
