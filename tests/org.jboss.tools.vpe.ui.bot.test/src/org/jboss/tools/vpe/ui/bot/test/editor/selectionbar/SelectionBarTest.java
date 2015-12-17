/*******************************************************************************
 * Copyright (c) 2007-2016 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.editor.selectionbar;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.core.matcher.WithTextMatcher;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.menu.ToolItemMenu;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.junit.Test;

/**
 * The Class SelectionBarTest.
 */
public class SelectionBarTest extends VPEAutoTestCase {

	private static final String SELECTED_TEXT = "<h:inputText value=\"#{user.name}\" required=\"true\">    <f:validateLength maximum=\"30\" minimum=\"3\"/>    </h:inputText>"; //$NON-NLS-1$
	private static final String SELECTED_TEXT2 = "<f:validateLength maximum=\"30\" minimum=\"3\"/>"; //$NON-NLS-1$
	private String sashStatus = "restored";

	@Test
	public void testSelectionBarContent() {
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME)
				.getProjectItem("WebContent", "pages", VPEAutoTestCase.TEST_PAGE).open();
		// Navigate to '<h:inputText value="#{user.name}" required="true">'
		new TextEditor(VPEAutoTestCase.TEST_PAGE).selectText("<h:in");
		AbstractWait.sleep(TimePeriod.getCustom(3));
		String errorMessage = checkSelectionBarContent();
		assertNull(errorMessage, errorMessage);
		maximizeVisualPane(VPEAutoTestCase.TEST_PAGE);
		sashStatus = "VisualPageMaximized";
		errorMessage = checkSelectionBarContent();
		assertNull(errorMessage, errorMessage);
		maximizeSourcePane(VPEAutoTestCase.TEST_PAGE);
		sashStatus = "SourcePageMaximized";
		errorMessage = checkSelectionBarContent();
		assertNull(errorMessage, errorMessage);
		restoreVisualPane(VPEAutoTestCase.TEST_PAGE);
		sashStatus = "restored";

	}

	@Test
	public void testSelectionBarButtonSelection() {
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent", "pages", TEST_PAGE).open();
		// Navigate to '<h:inputText value="#{user.name}" required="true">'
		TextEditor editor = new TextEditor(VPEAutoTestCase.TEST_PAGE);
		editor.selectText("<h:in");
		/*
		 * Send key press event to fire VPE listeners
		 */
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.ARROW_LEFT);
		AbstractWait.sleep(TimePeriod.getCustom(3));
		/*
		 * Click on the tag in the selection bar
		 */
		editor.activate();
		new DefaultToolItem(new WithTextMatcher("h:inputText")).click(); //$NON-NLS-1$
		String line = editor.getSelectedText();
		line = line.replaceAll("\n", ""); //$NON-NLS-1$ //$NON-NLS-2$
		line = line.replaceAll("\r", ""); //$NON-NLS-1$ //$NON-NLS-2$
		line = line.replaceAll("\t", ""); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("<h:inputText> should be selected", SELECTED_TEXT, line); //$NON-NLS-1$

		new ToolItemMenu(new DefaultToolItem(new WithTextMatcher("h:inputText")), "f:validateLength")
				.select();
		line = editor.getSelectedText();
		assertEquals("<f:validateLength> should be selected", SELECTED_TEXT2, line); //$NON-NLS-1$
	}

	/**
	 * Checks if Selection Bar has proper buttons
	 * 
	 * @return error message when Selection Bar has wrong content
	 */
	private String checkSelectionBarContent() {
		String errorMessage = null;
		String buttonLabel = "html";
		try {
			new DefaultToolItem(new WithTextMatcher(buttonLabel));
			buttonLabel = "body";
			new DefaultToolItem(new WithTextMatcher(buttonLabel));
			buttonLabel = "f:view";
			new DefaultToolItem(new WithTextMatcher(buttonLabel));
			buttonLabel = "h:inputText";
			new DefaultToolItem(new WithTextMatcher(buttonLabel));
		} catch (SWTLayerException swtle) {
			errorMessage = "Selection Bar has to contain Drop Down Button with label " + buttonLabel
					+ " but it doesn't.";
		}

		return errorMessage;
	}

	@Override
	public void tearDown() throws Exception {
		if (sashStatus.equals("VisualPageMaximized")) {
			restoreSourcePane(VPEAutoTestCase.TEST_PAGE);
		} else if (sashStatus.equals("SourcePageMaximized")) {
			restoreVisualPane(VPEAutoTestCase.TEST_PAGE);
		}
		super.tearDown();
	}
}
