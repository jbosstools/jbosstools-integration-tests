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

package org.jboss.tools.cdi.bot.test.uiutils.wizards;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.keyboard.Keystrokes;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotStyledText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTableItem;
import org.jboss.tools.cdi.bot.test.matcher.WidgetExistsInShellMatcher;
import org.junit.internal.matchers.TypeSafeMatcher;

@SuppressWarnings("restriction")
public class OpenOnOptionsDialog {
	
	private SWTBot bot = null;
	
	private SWTBotTable proposalTable = null;
	
	public OpenOnOptionsDialog(SWTBot bot) {
		this.bot = bot;
		proposalTable = getShellWithTable().bot().table();
	}
	
	public List<SWTBotTableItem> getAllOptions() {
		List<SWTBotTableItem> allItems = new ArrayList<SWTBotTableItem>();
		for (int i = 0; i < proposalTable.rowCount(); i++) {
			allItems.add(proposalTable.getTableItem(i));
		}
		return allItems;
	}
	
	public String setProposalOption(SWTBotTableItem ti) {
		ti.select();
		String styledText = getProposalText().getText();
		ti.setFocus();
		ti.pressShortcut(Keystrokes.LF);
		return styledText;
	}
	
	public SWTBotStyledText getProposalText() {
		return getShellWithOnlyStyledText().bot().styledText();
	}
	
	private SWTBotShell getShellWithTable() {
		TypeSafeMatcher<SWTBotShell> tableExistsMatcher = 
				new WidgetExistsInShellMatcher(Table.class);
		
		for (SWTBotShell s : bot.shells()) {
			if (tableExistsMatcher.matchesSafely(s)) {
				return s;
			}
		}
		return null;
	}
	
	private SWTBotShell getShellWithOnlyStyledText() {
		TypeSafeMatcher<SWTBotShell> styledTextExistsMatcher = 
				new WidgetExistsInShellMatcher(StyledText.class);
		
		for (SWTBotShell s : bot.shells()) {
			if (styledTextExistsMatcher.matchesSafely(s)) {
				boolean onlyStyledText = false;
				try {
					s.bot().tree();
				} catch (WidgetNotFoundException exc) {
					onlyStyledText = true;
				}
				if (onlyStyledText) {
					return s;
					
				}				
			}
		}
		return null;
	}
	
}
