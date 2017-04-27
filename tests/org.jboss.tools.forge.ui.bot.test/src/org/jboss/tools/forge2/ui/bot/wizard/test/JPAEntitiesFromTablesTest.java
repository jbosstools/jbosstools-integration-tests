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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.tools.forge.reddeer.ui.wizard.EntitiesFromTablesWizardFirstPage;
import org.jboss.tools.forge.reddeer.ui.wizard.EntitiesFromTablesWizardSecondPage;
import org.jboss.tools.forge.ui.bot.test.util.DatabaseUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Class for testing JPA entity generation from database tables
 * 
 * @author Jan Richter
 *
 */
public class JPAEntitiesFromTablesTest extends WizardTestBase {

	private List<String> tableNames = new ArrayList<String>();
	
	private static final String PACKAGE = GROUPID + ".model";
	
	@Before
	public void prepare() {
		startSakilaDatabase();
		setUpSakilaDatabase();
	}

	@Test
	public void testGenerateEntities() {
		new ProjectExplorer().selectProjects(PROJECT_NAME);
		WizardDialog dialog = getWizardDialog("JPA: Generate Entities From Tables");
		EntitiesFromTablesWizardFirstPage firstPage = new EntitiesFromTablesWizardFirstPage();
		firstPage.setPackage(PACKAGE);
		assertTrue("Missing connection profile selection", firstPage.getAllProfiles().contains(PROFILE_NAME));
		firstPage.setConnectionProfile(PROFILE_NAME);
		dialog.next();

		EntitiesFromTablesWizardSecondPage secondPage = new EntitiesFromTablesWizardSecondPage();
		List<TableItem> tables = secondPage.getAllTables();
		assertFalse("No database tables found", tables.isEmpty());
		for (TableItem item : tables) {
			tableNames.add(item.getText());
		}
		secondPage.selectAll();
		dialog.finish();

		checkEntityClasses();
	}

	@Override
	public void cleanup() {
		super.cleanup();
		DatabaseUtils.stopSakilaDB();
	}

	private void checkEntityClasses() {
		Project project = new ProjectExplorer().getProject(PROJECT_NAME);
		project.refresh();
		boolean hasResources = project.containsResource("Java Resources", "src/main/java", PACKAGE);
		if(!hasResources)
			fail("No resources have been generated!");
		ProjectItem model = project.getProjectItem("Java Resources", "src/main/java", PACKAGE);

		for (String name : tableNames) {
			// replace UNDERSCORES with UpperCamelCase
			String entityName = "";
			String[] nameParts = name.split("_");
			for (int i = 0; i < nameParts.length; i++) {
				entityName += nameParts[i].charAt(0) + nameParts[i].substring(1).toLowerCase();
			}

			assertTrue("Class for entity " + entityName + " is missing", model.containsResource(entityName + ".java"));
		}
	}
}