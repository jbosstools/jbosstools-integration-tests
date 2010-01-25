private package org.jboss.tools.ui.bot.ext.parts;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * This provides working Content assist functionality. 
 * SWTBot (2.0.0#467) funcionality provided in SWTEclipseEditor doesn't work (at least on GTK linux) 
 * @author jpeterka
 *
 */
public class ContentAssistBot {
	/**
	 * Performs content assist for given editor and string
	 * 
	 * @param editor
	 * @param text
	 */

	Logger log = Logger.getLogger(ContentAssistBot.class);
	SWTBotEditorExt editor;
	SWTBot bot;

	// ------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------
	/**
	 * Basic constructor
	 */
	public ContentAssistBot(SWTBotEditorExt editor) {
		this.editor = editor;
		this.bot = editor.bot();
	}

	// ------------------------------------------------------------
	// Public
	// ------------------------------------------------------------
	/**
	 * Use selected content proposal with given proposal text 
	 */
	public void useProposal(String text) {
		SWTBotTable table = getProposalTable(openProposalList());
		List<String> items = getTableItems(table);		
		if (items.contains(text)) {
			final int index = items.indexOf(text);
			seletctCCTableItem(table, index);
		} else {
			fail("ContentAssist doens't contain proposed text");
		}
	}
	/**
	 * Use selected content proposal with given proposal index 
	 */
	public void useProposal(int index) {
		SWTBotTable table = getProposalTable(openProposalList());
		seletctCCTableItem(table, index);
	}

	/**
	 * Logs proposal list contents, usual for debug purposes
	 * 
	 * @param text
	 */
	public void logProposalList() {
		SWTBotShell shell = openProposalList();
		SWTBotTable table = getProposalTable(shell);
		List<String> list = getTableItems(table);
		log.info("Proposal item list: " + list.size() + " item(s)");
		for (int i = 0; i < list.size(); i++) {
			log.info("Item i:" + list.get(i));
		}
		
		shell.close();
	}

	// ------------------------------------------------------------
	// Private
	// ------------------------------------------------------------
	/*
	 * Invokes ContentAssistProposal shell action
	 */
	private void invokeContentAssist() {
		String actionId = "ContentAssistProposal";
		//final IAction action = ((ITextEditor) partReference.getEditor(false)).getAction(actionId);
		final IAction action = ((ITextEditor) editor.getReference().getEditor(
				false)).getAction(actionId);
		syncExec(new VoidResult() {
			public void run() {
				action.run();
			}
		});
	}

	/**
	 * Return list of table items. Requires activated proposal table
	 * @param table
	 * @return
	 */
	private List<String> getTableItems(SWTBotTable table) {
		int rows = table.rowCount();
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < rows; i++) {
			list.add(table.cell(i, 0));
		}
		return list;
	}
	
	/**
	 * Returns proposal table from propsal shell
	 * @param ccShell
	 * @return
	 */
	private SWTBotTable getProposalTable(SWTBotShell ccShell) {
		SWTBot ccBot = new SWTBot(ccShell.widget);
		return ccBot.table();		
	}

	/**
	 * Opens proposal table
	 * @return proposal bot shell
	 */
	private SWTBotShell openProposalList() {
		SWTBotShell[] shells1 = bot.shells();
		invokeContentAssist();
		SWTBotShell[] shells2 = bot.shells();
		SWTBotShell ccShell = getContentAssistShell(shells1, shells2);
		return ccShell;
	
	}

	/**
	 * Select table item from code completion table. It's workaround because
	 * SWTBotTable methods doesn't work property for this case
	 * 
	 * @param botTable
	 * @param index
	 */
	private void seletctCCTableItem(final SWTBotTable botTable, final int index) {
		UIThreadRunnable.asyncExec(new VoidResult() {
			public void run() {
				Table table = botTable.widget;
				table.setSelection(index);
				Event event = new Event();
				event.type = SWT.Selection;
				event.widget = table;
				event.item = table.getItem(index);
				table.notifyListeners(SWT.Selection, event);
				table.notifyListeners(SWT.DefaultSelection, event);
			}
		});
	}

	/**
	 * Return content assist shell as new shell from two collections of shells
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	private SWTBotShell getContentAssistShell(SWTBotShell[] s1, SWTBotShell[] s2) {
		SWTBotShell ccShell = null;
		for (SWTBotShell bs2 : s2) {
			boolean found = false;
			for (SWTBotShell bs1 : s1) {
				if (bs2.widget.equals(bs1.widget)) {
					found = true;
					break;
				}
			}
			if (found == false) {
				ccShell = bs2;
				break;
			}
		}
		return ccShell;
	}
}
