/*******************************************************************************
 * Copyright (c) 2007-2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v 1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.ui.bot.test.project;

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.resources.OpenShift3Connection;
import org.junit.Test;

public class ProjectNameValidationTest {
	
	public static final String PROJECT_NAME_FORMAT_ERROR = 
			" Project name may only contain lower-case letters, numbers, and dashes. "
					+ "It may not start or end with a dash";
	
	public static final String PROJECT_NAME_SHORT_ERROR = 
			" Project name length must be between 2 and 63 characters";
	
	public static final String PROJECT_NAME_MAX_LENGTH_ERROR = 
			" Maximum length allowed is 63 characters for project name";
	
	@Test
	public void testShortProjectName() {
		openNewProjectShell();
		new LabeledText(OpenShiftLabel.TextLabels.PROJECT_NAME).setText("s");
		
		try {
			new DefaultText(PROJECT_NAME_SHORT_ERROR);
		} catch (RedDeerException ex) {
			fail("There is no validation message warning about small length of a project name.");
		}
		
		closeNewProjectShell();
	}
	
	@Test
	public void testInvalidProjectNameFormat() {
		openNewProjectShell();
		new LabeledText(OpenShiftLabel.TextLabels.PROJECT_NAME).setText("--");
		
		try {
			new DefaultText(PROJECT_NAME_FORMAT_ERROR);
		} catch (RedDeerException ex) {
			fail("There is no validation message warning about inappropriate project name.");
		}
		
		closeNewProjectShell();
	}
	
	@Test
	public void testForbiddenCharactersInProjectName() {
		openNewProjectShell();
		new LabeledText(OpenShiftLabel.TextLabels.PROJECT_NAME).setText("AAA");
		
		try {
			new DefaultText(PROJECT_NAME_FORMAT_ERROR);
		} catch (RedDeerException ex) {
			fail("There is no validation message warning about inappropriate project name.");
		}
		
		closeNewProjectShell();
	}
	
	@Test
	public void testLongProjectName() {
		openNewProjectShell();
		new LabeledText(OpenShiftLabel.TextLabels.PROJECT_NAME).setText("012345678901234567890" +
				"12345678901234567890123456789012345678901234567890123456789012345678901234567890" +
				"123456789012345678901234");
		try {
			new DefaultText(PROJECT_NAME_MAX_LENGTH_ERROR);
		} catch (RedDeerException ex) {
			fail("There is no validation message warning about small length of a project name.");
		}
		
		closeNewProjectShell();
	}
	
	private void openNewProjectShell() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.reopen();
		
		OpenShift3Connection connection = explorer.getOpenShift3Connection();
		connection.select();
		new ContextMenu(OpenShiftLabel.ContextMenu.NEW_OS_PROJECT).select();
		
		new DefaultShell(OpenShiftLabel.Shell.CREATE_OS_PROJECT);
	}
	
	private void closeNewProjectShell() {
		new CancelButton().click();
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.CREATE_OS_PROJECT), TimePeriod.LONG);
	}
}
