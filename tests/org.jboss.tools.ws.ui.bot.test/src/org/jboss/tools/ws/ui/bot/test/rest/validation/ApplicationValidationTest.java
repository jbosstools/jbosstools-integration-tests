/*******************************************************************************
 * Copyright (c) 2010-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.ws.ui.bot.test.rest.validation;

import org.jboss.tools.ws.reddeer.editor.ExtendedTextEditor;
import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author jjankovi
 *
 */
public class ApplicationValidationTest extends RESTfulTestBase {

	
	@Before
	public void setup() {
		
	}
	/**
	 * Resolved - Doubled warnings Multiple JAX-RS Activators
	 * {@link https://issues.jboss.org/browse/JBIDE-15402}
	 */
	@Test
	public void testMultipleApplicationClasses() {
		final String projectName = "app1";

		/* prepare project */
		importRestWSProject(projectName);
		
		/* test validation error */
		assertCountOfValidationErrors(projectName, 2);
		assertCountOfValidationErrors(projectName, "Multiple JAX-RS Activators", 2);
	}
	
	@Test
	public void testWebXmlAndApplicationClassWithWarning() {
		final String projectName = "app2";

		/* prepare project */
		importRestWSProject(projectName);

		/* test validation error */
		assertCountOfValidationErrors(projectName, 2);
		assertCountOfValidationErrors(projectName, "Multiple JAX-RS Activators", 2);
	}
	
	@Test
	public void testWebXmlAndApplicationClassWithoutWarning() {
		final String projectName = "app3";

		/* prepare project */
		importRestWSProject(projectName);

		/* test validation error */
		assertCountOfValidationWarnings(projectName, 0);
		assertCountOfValidationErrors(projectName, 0);
	}
	
	@Test
	public void testNotExtendingApplicationClass() {
		final String projectName = "app4";

		/* prepare project */
		importRestWSProject(projectName);

		/* test validation error */
		assertCountOfValidationErrors(projectName, 1);

		/* fix class - should be no error */
		openJavaFile(projectName, "test", "App.java");
		ExtendedTextEditor textEditor = new ExtendedTextEditor();
		textEditor.replace("@ApplicationPath(\"/rest\")", "");

		/* test validation error */
		assertCountOfValidationErrors(projectName, 0);
	}

	@Test
	public void testApplicationClassWithoutPath() {
		final String projectName = "app5";

		/* prepare project */
		importRestWSProject(projectName);

		/* test validation error */
		assertCountOfValidationErrors(projectName, 1);

		/* fix class - should be no error */
		openJavaFile(projectName, "test", "App.java");
		ExtendedTextEditor textEditor = new ExtendedTextEditor();
		textEditor.replace("extends Application", "");

		/* test validation error */
		assertCountOfValidationErrors("app5", 0);
	}
}
