/******************************************************************************* 
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.jst.ui.bot.test.tern;

import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.tools.jst.reddeer.tern.ui.TernModulesPropertyPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

/**
 * Tests for Tern -> Modules
 * 
 * @author psrna
 *
 */
public class TernModulesTest extends TernTestBase {

	@Before
	public void prepare() {
		createJSProject(PROJECT_NAME);
	}

	@After
	public void cleanup() {
		ShellHandler.getInstance().closeAllNonWorbenchShells();
		new ProjectExplorer().deleteAllProjects();
	}

	@Test
	public void testDefaultTernModulesEnabled() {
		TernModulesPropertyPage propPage = new TernModulesPropertyPage();
		openProjectProperties().select(propPage);
		assertTrue("Browser module not found!", new DefaultTable().containsItem("Browser"));
		assertTrue("Browser module is not enabled!", new DefaultTable().getItem("Browser").isChecked());
		assertTrue("Outline module not found!", new DefaultTable().containsItem("Outline"));
		assertTrue("Outline module is not enabled!", new DefaultTable().getItem("Outline").isChecked());
		assertTrue("'Completion Guess' module not found!", new DefaultTable().containsItem("Completion Guess"));
		assertTrue("'Completion Guess' module is not enabled!",
				new DefaultTable().getItem("Completion Guess").isChecked());
	}

	@Test
	public void testTernModulesAvailable() {
		TernModulesPropertyPage propPage = new TernModulesPropertyPage();
		openProjectProperties().select(propPage);
		List<TableItem> items = new DefaultTable().getItems();
		List<String> currentModules = new LinkedList<String>();
		for (TableItem i : items) {
			currentModules.add(i.getText());
		}

		String missingModules = getMisingString(currentModules, getTernExpectedModules());
		assertTrue("There are missing Tern Modules:" + missingModules, missingModules.length() == 0);

	}

	private static List<String> getTernExpectedModules() {
		LinkedList<String> result = new LinkedList<String>();

		result.add("AlloyUI");
		result.add("AngularJS");
		result.add("Bootstrap");
		result.add("Browser");
		result.add("Browser Extension");
		result.add("Chai");
		result.add("Chrome Apps");
		result.add("Chrome Extension");
		result.add("CKEditor");
		result.add("Closure");
		result.add("Completion Guess");
		result.add("Completion String");
		result.add("Cordova JavaScript");
		result.add("Delite");
		result.add("Dojo Toolkit");
		result.add("ESLint");
		result.add("Express");
		result.add("ExtJS");
		result.add("Fetch");
		result.add("Google Apps Script");
		result.add("Google Charts");
		result.add("Google Maps");
		result.add("Grunt");
		result.add("Gulp");
		result.add("highlight");
		result.add("Jasmine");
		result.add("jQuery");
		result.add("jQuery Extension");
		result.add("jQuery Mobile");
		result.add("jQuery UI");
		result.add("JSCS");
		result.add("JSHint");
		result.add("Liferay");
		result.add("Lint");
		result.add("Meteor");
		result.add("Mongo DB Native NodeJS Driver");
		result.add("Mongoose");
		result.add("Node Extension");
		result.add("Node.js");
		result.add("nuxeo");
		result.add("Outline");
		result.add("Phaser");
		result.add("Protractor");
		result.add("Qooxdoo");
		result.add("QUnit");
		result.add("RequireJS");
		result.add("RequireJS Extension");
		result.add("snabbt.js");
		result.add("tabris.js");
		result.add("three.js");
		result.add("Titanium");
		result.add("Underscore");
		result.add("WebIDL");
		result.add("YUI Library");

		return result;
	}

}
