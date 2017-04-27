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

import java.util.List;

import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.eclipse.core.resources.Resource;
import org.jboss.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.jboss.tools.forge.ui.bot.test.util.ScaffoldType;
import org.junit.Before;
import org.junit.Test;

/**
 * Class for testing scaffold setup
 * @author Jan Richter
 *
 */
public class ScaffoldSetupWizardTest extends WizardTestBase {

	private Project project;

	@Before
	public void prepare() {
		newProject(PROJECT_NAME);
		project = new ProjectExplorer().getProject(PROJECT_NAME);
		persistenceSetup(PROJECT_NAME);
	}

	/**
	 * Tests scaffold setup for JSF
	 */
	@Test
	public void testJSFScaffoldSetup() {
		ScaffoldType type = ScaffoldType.FACES;
		scaffoldSetup(PROJECT_NAME, type);
		project.refresh();
		checkResources(type);
		checkDependencies(type);
	}

	/**
	 * Tests scaffold setup for AngularJS
	 */
	@Test
	public void testAngularJSScaffoldSetup() {
		ScaffoldType type = ScaffoldType.ANGULARJS;
		scaffoldSetup(PROJECT_NAME, type);
		project.refresh();
		checkResources(type);
		checkDependencies(type);
	}

	private void checkResources(ScaffoldType type) {
		ProjectItem webapp = project.getProjectItem("src", "main", "webapp");
		ProjectItem webInf = webapp.getProjectItem("WEB-INF");
		assertNotNull("Missing WEB-INF folder", webInf);
		assertTrue("Missing beans.xml file", webInf.containsResource("beans.xml"));

		if (type == ScaffoldType.FACES) {
			assertTrue("Missing webapp/resources folder", webapp.containsResource("resources"));
			assertTrue("Missing webapp/resources/scaffold folder",
					webapp.containsResource("resources", "scaffold"));

			assertTrue("Missing faces config file", webInf.containsResource("faces-config.xml"));
			assertTrue("Missing web.xml file", webInf.containsResource("web.xml"));

			assertTrue("Missing index.xhtml file", webapp.containsResource("index.xhtml"));

		} else if (type == ScaffoldType.ANGULARJS) {
			assertTrue("Missing webapp/scripts folder", webapp.containsResource("scripts"));
			assertTrue("Missing webapp/views folder", webapp.containsResource("views"));
			assertTrue("Missing webapp/styles folder", webapp.containsResource("styles"));
			ProjectItem javaResources = project.getProjectItem("Java Resources", "src/main/java");
			Resource rest = null;

			for (Resource item : javaResources.getChildren()) {
				if (item.getName().endsWith(".rest")) {
					rest = item;
					break;
				}
			}
			assertNotNull("Missing rest package", rest);
			assertTrue("Missing RestApplication class", rest.containsResource("RestApplication.java") );
		}
	}

	private void checkDependencies(ScaffoldType type) {
		List<Resource> dependencies = project.getProjectItem("Java Resources", "Libraries", "Maven Dependencies")
				.getChildren();
		assertTrue("Missing CDI dependency", containsItemWithText(dependencies, "cdi-api"));
		assertTrue("Missing EJB dependency", containsItemWithText(dependencies, "ejb-api"));
		assertTrue("Missing Servlet API dependency", containsItemWithText(dependencies, "servlet-api"));

		if (type == ScaffoldType.FACES) {
			assertTrue("Missing JSF dependency", containsItemWithText(dependencies, "jsf-api"));
		} else if (type == ScaffoldType.ANGULARJS) {
			assertTrue("Missing JAX-RS dependency", containsItemWithText(dependencies, "jaxrs"));
		}
	}

	private boolean containsItemWithText(List<Resource> items, String text) {
		for (Resource item : items) {
			if (item.getText().contains(text)) {
				return true;
			}
		}
		return false;
	}
}
