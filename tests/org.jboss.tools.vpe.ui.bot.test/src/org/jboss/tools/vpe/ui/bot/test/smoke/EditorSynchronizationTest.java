/*******************************************************************************
 * Copyright (c) 2007-2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.smoke;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.eclipse.ui.views.contentoutline.OutlineView;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.reddeer.eclipse.ui.views.properties.PropertiesView;
import org.jboss.tools.vpe.ui.bot.test.editor.VPEEditorTestCase;
import org.junit.Test;

/**
 * Test VPE Editor synchronization with Properties and Outline View
 * 
 * @author Vladimir Pakan
 *
 */
public class EditorSynchronizationTest extends VPEEditorTestCase {
	@Test
	public void testEditorSynchronization() throws Throwable {
		openPage();
		openOutlineView();
		openPropertiesView();
		checkVPEEditorSynchronization();
	}

	/**
	 * Check VPE Editor synchronization with Properties and Outline View
	 */
	private void checkVPEEditorSynchronization() {

		final TextEditor jspTextEditor = new TextEditor(TEST_PAGE);
		jspTextEditor.setCursorPosition(jspTextEditor.getPositionOfText("<h:messages ") + 3);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.ARROW_LEFT);
		new OutlineView().activate();
		assertTrue("h:messages node is not selected within Outline View",
				new DefaultTree().getSelectedItems().get(0).getText().startsWith("h:messages "));

		PropertiesView propertiesView = new PropertiesView();
		propertiesView.activate();
		assertTrue(
				"style attribute of h:message node has wrong value within Properties view" + ". Should be 'color: red'",
				propertiesView.getProperty("General", "style").getPropertyValue().equals("color: red"));

		jspTextEditor.close();

	}
}