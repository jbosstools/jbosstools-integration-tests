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

package org.jboss.tools.cdi.bot.test.quickfix.dialog;

import static org.junit.Assert.*;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.jface.text.contentassist.ContentAssistant;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.cdi.text.ext.hyperlink.AssignableBeansDialog;
import org.junit.Test;

@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
@OpenPerspective(JavaEEPerspective.class)
@CleanWorkspace
public class AssignableDialogFilterTest extends CDITestBase {

	private String appClass = "App.java";

	@Override
	public String getProjectName() {
		return "AssignableDialogFilterTest";
	}

	@Test
	public void testFilterAssignableBeans() {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(getProjectName()).getProjectItem(CDIConstants.JAVA_RESOURCES, CDIConstants.SRC,
				getPackageName(), appClass).open();
		TextEditor ed = new TextEditor(appClass);
		ed.selectText("animal");
		ContentAssistant ca = ed.openOpenOnAssistant();
		ca.chooseProposal(CDIConstants.SHOW_ALL_ASSIGNABLE);

		AssignableBeansDialog assignDialog = new AssignableBeansDialog();

		/** test lower and upper case */
		assignDialog.typeInFilter("cat");
		assertEquals(1, assignDialog.getAllBeans().size());
		assertTrue(assignDialog
				.getAllBeans()
				.get(0)
				.equals("Cat - " + getPackageName() + " - /" + getProjectName()
						+ "/src"));

		assignDialog.typeInFilter("CAT");
		assertTrue(assignDialog.getAllBeans().size() == 1);
		assertTrue(assignDialog
				.getAllBeans()
				.get(0)
				.equals("Cat - " + getPackageName() + " - /" + getProjectName()
						+ "/src"));

		/** test '*' asterisk */
		assignDialog.typeInFilter("*at");
		assertTrue(assignDialog.getAllBeans().size() == 2);
		assertTrue(assignDialog.getAllBeans().contains(
				"Cat - " + getPackageName() + " - /" + getProjectName()
						+ "/src"));
		assertTrue(assignDialog.getAllBeans().contains(
				"@Decorator AnimalDecorator - " + getPackageName() + " - /"
						+ getProjectName() + "/src"));

		/** test '?' asterisk */
		assignDialog.typeInFilter("??g");
		assertTrue(assignDialog.getAllBeans().size() == 1);
		assertTrue(assignDialog
				.getAllBeans()
				.get(0)
				.equals("Dog - " + getPackageName() + " - /" + getProjectName()
						+ "/src"));

		/** test non-existing bean */
		assignDialog.typeInFilter("?*?s");
		assertTrue(assignDialog.getAllBeans().size() == 0);
		assignDialog.close();

	}

	@Test
	public void testFilterNonAssignableBeans() {

		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(getProjectName()).getProjectItem(CDIConstants.JAVA_RESOURCES, CDIConstants.SRC,
				getPackageName(), appClass).open();
		TextEditor ed = new TextEditor(appClass);
		ed.selectText("animal");
		ContentAssistant ca = ed.openOpenOnAssistant();
		ca.chooseProposal(CDIConstants.SHOW_ALL_ASSIGNABLE);

		AssignableBeansDialog assignDialog = new AssignableBeansDialog();

		assignDialog.hideDecorators();

		/** test lower and upper case */
		assignDialog.typeInFilter("animaldecorator");
		assertTrue(assignDialog.getAllBeans().size() == 0);

		assignDialog.typeInFilter("ANIMALDECORATOR");
		assertTrue(assignDialog.getAllBeans().size() == 0);

		/** test '*' asterisk */
		assignDialog.typeInFilter("*at");
		assertTrue(assignDialog.getAllBeans().size() == 1);
		assertTrue(assignDialog.getAllBeans().contains(
				"Cat - " + getPackageName() + " - /" + getProjectName()
						+ "/src"));
		assertFalse(assignDialog.getAllBeans().contains(
				"@Decorator AnimalDecorator - " + getPackageName() + " - /"
						+ getProjectName() + "/src"));

		/** test '?' asterisk */
		assignDialog.typeInFilter("??i");
		assertTrue(assignDialog.getAllBeans().size() == 0);
		assignDialog.close();

	}

}
