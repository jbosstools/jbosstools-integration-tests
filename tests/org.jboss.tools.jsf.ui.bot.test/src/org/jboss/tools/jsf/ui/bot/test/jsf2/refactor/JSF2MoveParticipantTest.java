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

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.condition.ShellIsActive;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.junit.Test;

public class JSF2MoveParticipantTest extends JSF2AbstractRefactorTest {
	@Test
	public void testJSF2MoveParticipant() throws Exception {
		createTestPage();
		createCompositeComponent();
		createDistResFolder();
		moveCurrResFolder();
		checkContent();
	}

	private void checkContent() throws Exception {
		packageExplorer.open();
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent",JSF2_Test_Page_Name + ".xhtml").open();
		TextEditor editor = new TextEditor(JSF2_Test_Page_Name + ".xhtml");
		assertEquals(
				loadFileContent("refactor/jsf2MoveTestPageRefactor.html"), editor.getText());
		editor.close();
	}

	private void moveCurrResFolder() throws Exception {
		packageExplorer.open();
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent","resources","mycomp").select();
		new ContextMenu("Refactor","Move...").select();
		Shell moveShell = new DefaultShell("Move");
		new DefaultTreeItem(JBT_TEST_PROJECT_NAME,"WebContent","resources","mycomp1").select(); 
		new PushButton("Preview >").click();
		checkPreview();
		new OkButton().click();
		new WaitWhile(new ShellIsActive(moveShell));
		new WaitWhile(new JobIsRunning());
	}

	private void createDistResFolder() throws Exception {
	  	packageExplorer.open();
		try {
			packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent", "resources" , "mycomp1").select();
		} catch (EclipseLayerException ele) {
			packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent", "resources").select();
			new ShellMenu("File","New","Folder").select();
			new LabeledText("Folder name:").setText("mycomp1");
			new FinishButton().click();
		}
	}

	@Override
	public void tearDown() throws Exception {
		packageExplorer.open();
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent",JSF2_Test_Page_Name + ".xhtml").delete();
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent","resources","mycomp1").delete();
		super.tearDown();
	}
	
	private void checkPreview() {
		new PushButton("< Back");
		new DefaultTreeItem("Rename composite URI changes",
				"jsf2TestPage.xhtml - " + JBT_TEST_PROJECT_NAME + "/WebContent", "Rename composite URI").select();
	}

}
