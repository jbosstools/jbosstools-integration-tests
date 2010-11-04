/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.ws.ui.bot.test.uiutils.actions;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;

public class TreeItemAction extends Action<SWTBot> {

	private final SWTBotTreeItem item;
	
	public TreeItemAction(SWTBotTreeItem tree, String... actionPath) {
		super(actionPath);
		this.item = tree;
	}

	@Override
	public SWTBot run() {
		return performMenu();
	}

	@Override
	protected SWTBot performMenu() {
		assert item != null : "TreeItem cannot be null!";
		Tree tree = UIThreadRunnable.syncExec(new Result<Tree>() {

			public Tree run() {
				return item.widget.getParent();
			}
			
		});
		final SWTBotTree t = new SWTBotTree(tree);
		final String[] actionPath = super.getActionPath(); 
		ContextMenuHelper.prepareTreeItemForContextMenu(t, item);
		UIThreadRunnable.syncExec(new Result<SWTBotMenu>() {

			public SWTBotMenu run() {
				SWTBotMenu m = new SWTBotMenu(ContextMenuHelper.getContextMenu(
						t, actionPath[0], false));
				for (int i = 1; i < actionPath.length; i++) {
					m = m.menu(actionPath[i]);
				}
				return m;
			}
		}).click();
		return new SWTBot();
	}

}
