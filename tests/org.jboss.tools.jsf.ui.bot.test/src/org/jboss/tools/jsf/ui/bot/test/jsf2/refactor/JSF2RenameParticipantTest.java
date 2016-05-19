package org.jboss.tools.jsf.ui.bot.test.jsf2.refactor;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.condition.ShellIsActive;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.Test;

/*******************************************************************************
 * Copyright (c) 2007-2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
public class JSF2RenameParticipantTest extends JSF2AbstractRefactorTest {
	@Test
	public void testJSF2RenameParticipant() throws Exception {
		createCompositeComponent();
		createTestPage();
		renameCompositeComponent();
		checkContent();
	}

	private void renameCompositeComponent() throws Exception {
		packageExplorer.open();
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).refresh();
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent","resources","mycomp","echo.xhtml").select();
		new ContextMenu("Refactor","Rename...").select();
		Shell renameShell = new DefaultShell(IDELabel.Shell.RENAME_RESOURCE);
		new LabeledText("New name:").setText("echo1.xhtml");
		new PushButton("Preview >").click();
		checkPreview();
		new OkButton().click();
		new WaitWhile(new ShellIsActive(renameShell));
	}

	private void checkContent() throws Exception {
		packageExplorer.open();
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent",JSF2_Test_Page_Name + ".xhtml").open();
		TextEditor editor = new TextEditor(JSF2_Test_Page_Name + ".xhtml");
		assertEquals(
				loadFileContent("refactor/jsf2RenameTestPageRefactor.html"), editor.getText());
		editor.close();
	}

	@Override
	public void tearDown() throws Exception {
		packageExplorer.open();
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent",JSF2_Test_Page_Name + ".xhtml").delete();
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent","resources","mycomp","echo1.xhtml").delete();
		super.tearDown();
	}

	private void checkPreview() {
		new PushButton("< Back");
		new DefaultTreeItem("Rename composite component changes"
			,"jsf2TestPage.xhtml - " + JBT_TEST_PROJECT_NAME + "/WebContent"
			,"Rename composite component");
	}

}
