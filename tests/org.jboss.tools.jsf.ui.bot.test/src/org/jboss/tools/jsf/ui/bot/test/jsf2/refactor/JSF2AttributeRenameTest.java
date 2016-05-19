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
package org.jboss.tools.jsf.ui.bot.test.jsf2.refactor;

import java.io.IOException;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.condition.ShellIsActive;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.junit.Test;

public class JSF2AttributeRenameTest extends JSF2AbstractRefactorTest {
	@Test
	public void testJSF2AttributeRename() throws Exception {
		createCompositeComponent();
		createTestPage();
		renameCompositeAttribute();
		checkContent();
	}

	private void renameCompositeAttribute() throws IOException {
		TextEditor editor = new TextEditor("echo.xhtml");
		editor.setCursorPosition(9, 29);
		// for Eclipse Juno focus has to be moved out and back from editor
		packageExplorer.open();
		editor.activate();
		new ContextMenu("Refactor","Rename Composite Component Attribute").select();
		Shell renameShell = new DefaultShell("Rename Composite Attribute");
		new LabeledText("New name:").setText("echo1");
		new PushButton("Preview >").click();
		checkPreview();
		new OkButton().click();
		new WaitWhile(new ShellIsActive(renameShell));	
	}

	private void checkContent() throws IOException {
		packageExplorer.open();
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent",JSF2_Test_Page_Name + ".xhtml").open();
		TextEditor editor = new TextEditor(JSF2_Test_Page_Name + ".xhtml");
		assertEquals(
				loadFileContent("refactor/jsf2RenameAttrTestPageRefactor.html"), editor.getText());
		editor.close();
		packageExplorer.activate();
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent","resources","mycomp","echo.xhtml").open();
		assertEquals(
				loadFileContent("refactor/compositeComponentAfterRename.html"), new TextEditor("echo.xhtml").getText());
	}

	@Override
	protected void createCompositeComponent() throws Exception {
		super.createCompositeComponent();
		packageExplorer.open();
		ProjectItem projectItem = packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent","resources","mycomp");
		projectItem.refresh();
		projectItem.getProjectItem("echo.xhtml").open();
		TextEditor editor = new TextEditor("echo.xhtml");
		editor.setText(loadFileContent("refactor/compositeComponent.html"));
		editor.save();
	}

	@Override
	public void tearDown() throws Exception {
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent",JSF2_Test_Page_Name + ".xhtml").delete();
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent","resources","mycomp","echo.xhtml").delete();
		super.tearDown();
	}

	private void checkPreview() throws IOException {
		new PushButton("< Back");
		new DefaultTreeItem("Composite attribute name changes","echo.xhtml - " + JBT_TEST_PROJECT_NAME + "/WebContent/resources/mycomp","Rename composite attribute name");
		new DefaultTreeItem("Composite attribute name changes","jsf2TestPage.xhtml - " + JBT_TEST_PROJECT_NAME + "/WebContent","Rename composite attribute");
		assertEquals(
				loadFileContent("refactor/compositeComponent.html"), new DefaultStyledText().getText());
	}

}
