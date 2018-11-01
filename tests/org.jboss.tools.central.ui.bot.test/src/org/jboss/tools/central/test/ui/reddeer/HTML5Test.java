/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.central.test.ui.reddeer;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.swt.impl.browser.InternalBrowser;
import org.eclipse.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.eclipse.reddeer.workbench.handler.WorkbenchShellHandler;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.central.reddeer.api.ExamplesOperator;
import org.jboss.tools.central.reddeer.api.JavaScriptHelper;
import org.jboss.tools.central.reddeer.wizards.NewProjectExamplesWizardDialogCentral;
import org.jboss.tools.central.test.ui.reddeer.internal.ErrorsReporter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author rhopp
 *
 */

@RunWith(RedDeerSuite.class)
public class HTML5Test {

	private static DefaultEditor centralEditor;
	private InternalBrowser browser;
	private Logger log = new Logger(this.getClass());
	private ProjectExplorer projectExplorer;
	private static ErrorsReporter reporter = ErrorsReporter.getInstance();
	private ExamplesOperator operator = ExamplesOperator.getInstance();
	private JavaScriptHelper jsHelper = JavaScriptHelper.getInstance();

	@BeforeClass
	public static void setupClass() {
		new DefaultToolItem(new WorkbenchShell(), "JBoss Central").click();
		centralEditor = new DefaultEditor("JBoss Central");
	}

	@Before
	public void setup() {
		projectExplorer = new ProjectExplorer();
		projectExplorer.open();
	}

	@After
	public void teardown() {
		WorkbenchShellHandler.getInstance().closeAllNonWorbenchShells();
		new ProjectExplorer().deleteAllProjects(true);
		new DefaultToolItem(new WorkbenchShell(), "JBoss Central").click();
		// activate central editor
		new DefaultEditor("JBoss Central");
	}

	@AfterClass
	public static void teardownClass() {
		reporter.generateReport();
	}

	@Test
	public void importEAP64ExamplesTest() {
		centralEditor.activate();
		browser = new InternalBrowser();
		jsHelper.setBrowser(browser);
		jsHelper.searchFor("eap-6.4.0.GA");
		// jsHelper.searchFor("Shows how to use Java EE Declarative Security to
		// Control Access to EJB 3");
		do {
			String[] examples = jsHelper.getExamples();
			log.error("List of examples: " + Arrays.toString(examples));
			for (String exampleName : examples) {
				log.step("Processing example: " + exampleName);
				if (!(exampleName.equals("app-client") || exampleName.equals("cluster-ha-singleton")
						|| exampleName.equals("ejb-asynchronous"))) {
					processCurrentExample(exampleName);
				}

			}
			jsHelper.nextPage();
		} while (jsHelper.hasNext());
	}

	/**
	 * Imports current example, checks for warnings/errors, tries to deploy it
	 * to server and finally deletes it.
	 * 
	 * @param exampleName
	 */
	private void processCurrentExample(String exampleName) {

		// import
		try {
			importExample(exampleName);
		} catch (Exception e) {
			reporter.addError(new org.jboss.tools.central.reddeer.projects.Project(exampleName, null),
					"Error importing example: " + stacktraceToString(e));
		}

	}

	private String stacktraceToString(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

	private void importExample(String exampleName) {
		log.step("Importing example: " + exampleName);
		centralEditor.activate();
		jsHelper.clickExample(exampleName);
		NewProjectExamplesWizardDialogCentral wizardDialog = new NewProjectExamplesWizardDialogCentral();
		wizardDialog.finish(exampleName);
	}

}
