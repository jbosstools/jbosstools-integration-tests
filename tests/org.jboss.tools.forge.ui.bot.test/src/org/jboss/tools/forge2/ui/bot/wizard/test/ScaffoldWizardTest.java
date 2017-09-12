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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.eclipse.reddeer.eclipse.core.resources.Project;
import org.eclipse.reddeer.eclipse.core.resources.ProjectItem;
import org.eclipse.reddeer.eclipse.core.resources.Resource;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.ui.views.contentoutline.ContentOutline;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.jboss.tools.forge.ui.bot.test.util.ScaffoldType;
import org.junit.Before;
import org.junit.Test;

/**
 * Class for testing scaffold generation from entities
 * 
 * @author Jan Richter
 *
 */
public class ScaffoldWizardTest extends WizardTestBase {

	private static final String[] ENTITY_NAMES = { "Customer", "Product", "Order" };
	private Project project;

	@Before
	public void prepare() {
		newProject(PROJECT_NAME);
		project = new ProjectExplorer().getProject(PROJECT_NAME);
		persistenceSetup(PROJECT_NAME);
		newEntity(ENTITY_NAMES[0]);
		newEntity(ENTITY_NAMES[1]);
		newEntity(ENTITY_NAMES[2]);
	}

	/**
	 * Test scaffold generation for JSF
	 */
	@Test
	public void testCreateFacesScaffold() {
		ScaffoldType facesType = ScaffoldType.FACES;
		setupScaffold(facesType);
		createScaffold(facesType);
		checkGeneratedFiles(facesType);
	}

	/**
	 * Test scaffold generation for AngularJS
	 */
	@Test
	public void testCreateAngularJSScaffold() {
		ScaffoldType angular = ScaffoldType.ANGULARJS;
		setupScaffold(angular);
		createScaffold(angular);
		checkGeneratedFiles(angular);
	}

	private void checkGeneratedFiles(ScaffoldType type) {
		project.refresh();
		ProjectItem model = project.getProjectItem("Java Resources", "src/main/java", GROUPID + ".model");

		for (String name : ENTITY_NAMES) {
			assertTrue("No class for entity " + name, model.containsResource(name + ".java"));
			checkWebResources(name, type);

			if (type == ScaffoldType.FACES) {
				checkViewClass(name);
			}
		}
	}

	private void checkWebResources(String entityName, ScaffoldType type) {
		ProjectItem webapp = project.getProjectItem("src", "main", "webapp");
		if (type == ScaffoldType.FACES) {
			ProjectItem entityFolder = webapp
					.getProjectItem(Character.toLowerCase(entityName.charAt(0)) + entityName.substring(1));

			String[] fileNames = { "create.xhtml", "search.xhtml", "view.xhtml" };
			assertNotNull(entityFolder);

			for (String fileName : fileNames) {
				assertTrue("File " + fileName + " missing for entity " + entityName,
						entityFolder.containsResource(fileName));
			}

		} else if (type == ScaffoldType.ANGULARJS) {
			ProjectItem scriptsFolder = webapp.getProjectItem("scripts");
			assertNotNull("Missing webapp/scripts folder", scriptsFolder);

			ProjectItem controllers = scriptsFolder.getProjectItem("controllers");
			assertNotNull("Missing webapp/scripts/controllers folder", controllers);

			ProjectItem services = scriptsFolder.getProjectItem("services");
			assertNotNull("Missing webapp/scripts/services folder", services);

			String cont = "Controller.js";
			assertTrue("Missing edit controller for entity " + entityName,
					controllers.containsResource("edit" + entityName + cont));
			assertTrue("Missing new controller for entity " + entityName,
					controllers.containsResource("new" + entityName + cont));
			assertTrue("Missing search controller for entity " + entityName,
					controllers.containsResource("search" + entityName + cont));

			assertTrue("Missing factory service for entuty " + entityName,
					services.containsResource(entityName + "Factory.js"));
		}
	}

	private void checkViewClass(String entityName) {
		ProjectItem javaResources = project.getProjectItem("Java Resources", "src/main/java");
		Resource view = null;

		for (Resource item : javaResources.getChildren()) {
			if (item.getName().endsWith(".view")) {
				view = item;
				break;
			}
		}
		assertNotNull("Missing view package", view);

		assertTrue("No bean for entity" + entityName, view.containsResource(entityName + "Bean.java"));

		view.getResource(entityName + "Bean.java").open();
		ContentOutline outline = new ContentOutline();
		outline.open();

		TreeItem beanItem = null;
		Collection<TreeItem> items = outline.outlineElements();
		for (TreeItem item : items) {
			if (item.getText().equals(entityName + "Bean")) {
				beanItem = item;
			}
		}
		// check id
		assertNotNull(beanItem.getItem("id : Long"));
		assertNotNull(beanItem.getItem("getId() : Long"));
		assertNotNull(beanItem.getItem("setId(Long) : void"));

		// check the entity field
		String entityFieldName = Character.toLowerCase(entityName.charAt(0)) + entityName.substring(1);
		assertNotNull(beanItem.getItem(entityFieldName + " : " + entityName));
		assertNotNull(beanItem.getItem("get" + entityName + "() : " + entityName));
		assertNotNull(beanItem.getItem("set" + entityName + "(" + entityName + ")" + " : void"));

		// check CRUD operations
		assertNotNull(beanItem.getItem("create() : String"));
		assertNotNull(beanItem.getItem("retrieve() : void"));
		assertNotNull(beanItem.getItem("findById(Long) : " + entityName));
		assertNotNull(beanItem.getItem("update() : String"));
		assertNotNull(beanItem.getItem("delete() : String"));
		assertNotNull(beanItem.getItem("getAll() : List<" + entityName + ">"));
	}

	private void newEntity(String name) {
		newJPAEntity(PROJECT_NAME, name, "", GROUPID + ".model");
	}


}
