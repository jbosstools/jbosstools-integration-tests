/*******************************************************************************
 * Copyright (c) 2007-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.ui.bot.ext.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.bindings.keys.ParseException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.eclipse.ui.texteditor.ITextEditor;
import org.jboss.tools.common.model.ui.editor.EditorPartWrapper;
import org.jboss.tools.common.model.ui.editors.multipage.DefaultMultipageEditor;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTJBTExt;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.condition.ActiveEditorHasTitleCondition;
import org.jboss.tools.ui.bot.ext.condition.ActiveShellContainsWidget;
import org.jboss.tools.ui.bot.ext.types.IDELabel;

/**
 * Helper for Open On functionality testing
 * 
 * @author Vladimir Pakan
 * @author jjankovi
 * 
 */
public class OpenOnHelper {

	protected static final Logger log = Logger.getLogger(OpenOnHelper.class);

	public static SWTBotEditor checkOpenOnFileIsOpened(SWTBotExt bot,
			String editorTitle, String textToSelect, String expectedOpenedFileName) {
		
		int offset = textToSelect.contains("@")?1:0;
		return checkOpenOnFileIsOpened(bot, editorTitle, textToSelect, 
				offset, textToSelect.length() - offset, 0, expectedOpenedFileName);
		
	}
	
	/**
	 * Applies Open On (F3) on textToSelect within editor with editorTitle and
	 * checks if expectedOpenedFileName was opened
	 * 
	 * <p>
	 * This test fails if is executed in Xephyr and ibus-daemon is on. Ibus-daemon causes rejection
	 * of all keyboard events.
	 * </p><p>
	 * Related reading (bugreports): <br />
	 * <a href="https://bugs.launchpad.net/ubuntu/+source/ibus/+bug/481656"> iBus blocks input in Java application </a> <br />
	 * <a href="https://bugzilla.redhat.com/show_bug.cgi?id=800736"> Freemind (java) loses keyboard input when used with ibus </a>
	 * </p>
	 * 
	 * @param editorTitle
	 * @param textToSelect
	 * @param selectionOffset
	 * @param selectionLength
	 * @param textToSelectIndex
	 * @param expectedOpenedFileName
	 */
	public static SWTBotEditor checkOpenOnFileIsOpened(SWTBotExt bot,
			String editorTitle, String textToSelect, int selectionOffset,
			int selectionLength, int textToSelectIndex,
			String expectedOpenedFileName) {

		SWTBotEclipseEditor sourceEditor = SWTJBTExt.selectTextInSourcePane(
				bot, editorTitle, textToSelect, selectionOffset,
				selectionLength, textToSelectIndex);

		sourceEditor.setFocus();
		// process UI Events
		UIThreadRunnable.syncExec(new VoidResult() {
			@Override
			public void run() {
			}
		});
		
		new SWTUtilExt(bot).waitForNonIgnoredJobs();
		
		assertTrue(editorTitle.equals(bot.activeEditor().getTitle()));
		bot.activeShell().setFocus();
		
		openOnUsingKeyStroke(sourceEditor);
		if (!wasWpenOnSuccessful(bot, expectedOpenedFileName)) {
			log.info("openon using F3 keystroke failed");
			openOnUsingAction();
			waitForEditorToOpen(bot, expectedOpenedFileName);
		}
		
		return checkActiveEditorTitle(bot, expectedOpenedFileName);
	}

	private static void waitForEditorToOpen(SWTBotExt bot, String editorTitle) {
		// process UI Events
		UIThreadRunnable.syncExec(new VoidResult() {
			@Override
			public void run() {
			}
		});
		new SWTUtilExt(bot).waitForNonIgnoredJobs();
		bot.waitUntil(new ActiveEditorHasTitleCondition(bot,
		    editorTitle), Timing.time10S());
		bot.activeEditor().setFocus();
	}

	private static void openOnUsingKeyStroke(SWTBotEclipseEditor editor) {
		try {
			KeyStroke f3KeyStroke = KeyStroke.getInstance("F3");
			editor.pressShortcut(f3KeyStroke);
		} catch (ParseException ex) {
			throw new RuntimeException("Keystroke 'F3' could not be created", ex);
		}
	}

	private static boolean wasWpenOnSuccessful(SWTBotExt bot, String expectedEditorTitle) {
		try {
		  waitForEditorToOpen(bot, expectedEditorTitle);
		} catch (TimeoutException ex) {
			return false;
		}
		return true;
	}

	private static void openOnUsingAction() {
		Display.getDefault().syncExec(new OpenResourceRunnable());
	}
	
	private static class OpenResourceRunnable implements Runnable {
		/**
		 * know-how: org.jboss.tools.ui.bot.ext.parts.ContentAssistBot.invokeContentAssist()
		 * 'F3' key press simulated by direct action invocation; sending keystroke directly
		 * using {@link java.awt.Robot} was very unreliable
		 */
		@Override
		public void run() {
			EditorPartWrapper editor = (EditorPartWrapper) new SWTBotExt().activeEditor()
					.getReference().getEditor(false);
			DefaultMultipageEditor editor2 = (DefaultMultipageEditor) editor.getEditor();
			ITextEditor activeEditor = (ITextEditor) editor2.getActiveEditor();
			IAction openResourceAction = activeEditor.getAction("OpenFileFromSource");
			assertNotNull("Obtaining open resource editor action", openResourceAction);
			openResourceAction.run();
		}
	}
	
	private static String getSubstringPattern(String stringToMatch) {
		String quoted = Pattern.quote(stringToMatch);
		String pattern = ".*" + quoted + ".*";
		return pattern;
	}
	
	public static SWTBotEditor selectOpenOnOption(SWTBotExt bot, String editorTitle, 
			String textToSelect, String openOnOption) {
		int offset = textToSelect.contains("@")?1:0;
		return selectOpenOnOption(bot, editorTitle, textToSelect, offset, 
				textToSelect.length() - offset, 0, openOnOption);
	}
	
	/**
	 * Selects specified text in specified opened editor, shows open on options
	 * via selecting Navigate -> Open Hyperlink and select one of them specified 
	 * by openOnOption
	 * 
	 * @param bot
	 * @param editorTitle
	 * @param textToSelect
	 * @param selectionOffset
	 * @param selectionLength
	 * @param textToSelectIndex
	 * @param openOnOption
	 */
	public static SWTBotEditor selectOpenOnOption(SWTBotExt bot, String editorTitle, 
			String textToSelect, int selectionOffset,
			int selectionLength, int textToSelectIndex, String openOnOption) {
		showOpenOnOptions(bot, editorTitle, textToSelect, selectionOffset, 
				selectionLength, textToSelectIndex);
		
		SWTBotTable table = bot.activeShell().bot().table(0);

		boolean optionFound = false;
		String foundOptions = "";

		for (int i = 0; i < table.rowCount(); i++) {
			String foundOption = table.getTableItem(i).getText();
			foundOptions = foundOptions + foundOption + ", ";
			if (foundOption.contains(openOnOption)) {
				optionFound = true;
				table.click(i, 0);
				break;
			}
		}
		foundOptions = foundOptions.substring(0, foundOptions.length() - 3);
		assertTrue(openOnOption + " was not found in open on options of "
				+ textToSelect + " Found: " + foundOptions, optionFound);
		return bot.activeEditor();
	}
	
	public static void showOpenOnOptions(SWTBotExt bot, String editorTitle, 
			String textToSelect) {
		int offset = textToSelect.contains("@")?1:0;
		showOpenOnOptions(bot, editorTitle, textToSelect, offset, 
				textToSelect.length() - offset, 0);
	}
	
	/**
	 * Selects specified text in specified opened editor and shows open on options
	 * via selecting Navigate -> Open Hyperlink
	 * 
	 * @param bot
	 * @param editorTitle
	 * @param textToSelect
	 * @param selectionOffset
	 * @param selectionLength
	 * @param textToSelectIndex
	 */
	public static void showOpenOnOptions(SWTBotExt bot,
			String editorTitle, String textToSelect, int selectionOffset,
			int selectionLength, int textToSelectIndex) {
		
		SWTJBTExt.selectTextInSourcePane(
				bot, editorTitle, textToSelect, selectionOffset,
				selectionLength, textToSelectIndex);
		bot.editorByTitle(editorTitle).show();
		bot.editorByTitle(editorTitle).setFocus();
		bot.menu(IDELabel.Menu.NAVIGATE).menu(IDELabel.Menu.OPEN_HYPERLINK)
				.click();

		bot.waitUntil(new ActiveShellContainsWidget(bot, Table.class));
		
	}
	
	public static SWTBotEditor checkOpenOnFileIsOpened(SWTBotExt bot,
			String editorTitle, String textToSelect, String openOnOption,
			String expectedOpenedFileName) {
		
		int offset = textToSelect.contains("@")?1:0;
		return checkOpenOnFileIsOpened(bot, editorTitle, textToSelect, 
				offset, textToSelect.length() - offset, 0, 
				openOnOption, expectedOpenedFileName);
	}

	/**
	 * Shows all open on options on textToSelect within editor with editorTitle, 
	 * select the one containing text openOnOption and finally checks if 
	 * expectedOpenedFileName was opened
	 * 
	 * @param bot
	 * @param editorTitle
	 * @param textToSelect
	 * @param selectionOffset
	 * @param selectionLength
	 * @param textToSelectIndex
	 * @param openOnOption
	 * @param expectedOpenedFileName
	 * @return
	 */
	public static SWTBotEditor checkOpenOnFileIsOpened(SWTBotExt bot,
			String editorTitle, String textToSelect, int selectionOffset,
			int selectionLength, int textToSelectIndex, String openOnOption,
			String expectedOpenedFileName) {
		
		selectOpenOnOption(bot, editorTitle, textToSelect, selectionOffset, 
				selectionLength, textToSelectIndex, openOnOption);
		return checkActiveEditorTitle(bot, expectedOpenedFileName);
		
	}
	
	private static SWTBotEditor checkActiveEditorTitle(SWTBotExt bot, String expectedOpenedFileName) {
		
		SWTBotEditor activeEditor = null;
		try {
			bot.waitUntil(new ActiveEditorHasTitleCondition(bot,
					expectedOpenedFileName), Timing.time10S());
			activeEditor = bot.activeEditor();
		} catch (TimeoutException toe) {
			activeEditor = bot.activeEditor();
			fail("Opened file has to have title " + expectedOpenedFileName
					+ " but has " + activeEditor.getTitle());
		}
		
		return activeEditor;
		
	}

}
