package org.jboss.tools.ui.bot.ext.view;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.types.IDELabel;

/**
 * Class provides bot routines related to Package Explorer View
 * @author jpeterka
 *
 */
public class PackageExplorer extends SWTBotExt {
	
	/*
	 * Selects project in Package Explorer
	 */
	public void selectProject(String projectName) {
		SWTBot viewBot = viewByTitle(IDELabel.View.PACKAGE_EXPLORER).bot();
		viewBot.tree().expandNode(projectName).select();
	}

}
