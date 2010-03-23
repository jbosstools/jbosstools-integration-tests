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
package org.jboss.tools.ui.bot.ext.view;

import org.apache.log4j.Logger;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.types.IDELabel;

/**
 * Eclipse project type enum
 * 
 * @author jpeterka
 * 
 */
public class ProjectExplorer extends SWTBotExt {

	Logger log = Logger.getLogger(ProjectExplorer.class);

	public SWTBotEditor openFile(String projectName, String... path) {
		SWTBot viewBot = viewByTitle(IDELabel.View.PROJECT_EXPLORER).bot();

		viewByTitle(IDELabel.View.PROJECT_EXPLORER).show();
		viewByTitle(IDELabel.View.PROJECT_EXPLORER).setFocus();
		SWTBotTree tree = viewBot.tree();
		SWTBotTreeItem item = tree.expandNode(projectName);
		StringBuilder builder = new StringBuilder(projectName);

		// Go through path
		for (String nodeName : path) {
			item = item.expandNode(nodeName);
			builder.append("/" + nodeName);
		}

		item.select().doubleClick();
		log.info("File Opened:" + builder.toString());

		SWTBotEditor editor = activeEditor();
		return editor;
	}
}
