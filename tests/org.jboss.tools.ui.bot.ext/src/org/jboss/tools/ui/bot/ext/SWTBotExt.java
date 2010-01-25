 /*******************************************************************************
  * Copyright (c) 2007-2009 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.ui.bot.ext;

import static org.junit.Assert.fail;

import org.apache.log4j.Logger;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCCombo;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.jboss.tools.ui.bot.ext.parts.SWTBotEditorExt;

/**
 * Extended version of SWTWorkbenchBot, logging added
 * 
 * @author jpeterka
 * 
 */
public class SWTBotExt extends SWTWorkbenchBot {

	private Logger log = Logger.getLogger(SWTBotExt.class);

	public void logAndFail(String msg) {
		log.error(msg);
		fail(msg);
	}

	// ------------------------------------------------------------
	// SWTBot method wrapper ( for better logging mainly )
	// ------------------------------------------------------------

	@Override
	public SWTBotMenu menu(String text) {
		log.info("Menu \"" + text + "\" secleted");
		return super.menu(text);
	}

	@Override
	public SWTBotButton button(String text) {
		log.info("Button \"" + text + "\" selected");
		return super.button(text);
	}

	@Override
	public SWTBotTree tree() {
		log.info("Tree selected");
		return super.tree();
	}

	@Override
	public SWTBotCCombo ccomboBox(String text) {
		log.info("Combobox \"" + text + "\" selected");
		return super.ccomboBox(text);
	}

	@Override
	public SWTBotTable table() {
		log.info("Table selected");
		return super.table();
	}
	@Override
	public SWTBotEditorExt editorByTitle(String fileName) {		
		SWTBotEditor editor = super.editorByTitle(fileName);
		return new SWTBotEditorExt(editor.toTextEditor().getReference(), (SWTWorkbenchBot)this);
	}

}
