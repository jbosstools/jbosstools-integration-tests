/*******************************************************************************
 * Copyright (c) 2010-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.ws.ui.bot.test.rest.explorer;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ws.ui.bot.test.WSTestBase;
import org.jboss.tools.ws.ui.bot.test.utils.NodeContextUtil;

/**
 * Test base for bot tests using RESTFul support
 * @author jjankovi
 *
 */
public class RESTfulTestBase extends WSTestBase {

	private enum ConfigureOption {
		ADD, REMOVE;
	}
	
	/**
	 * 
	 * @param wsProjectName
	 */
	protected void addRestSupport(String wsProjectName) {
		configureRestSupport(wsProjectName, ConfigureOption.ADD);
	}
	
	protected void removeRestSupport(String wsProjectName) {
		configureRestSupport(wsProjectName, ConfigureOption.REMOVE);
	}
	
	private void configureRestSupport(String wsProjectName, ConfigureOption option) {
		projectExplorer.selectProject(wsProjectName);
		SWTBotTree tree = projectExplorer.bot().tree();
		SWTBotTreeItem item = tree.getTreeItem(wsProjectName);
		item.expand();
		NodeContextUtil.nodeContextMenu(tree, item, RESTFulAnnotations.CONFIGURE_MENU_LABEL.getLabel(), 
				option==ConfigureOption.ADD?
						RESTFulAnnotations.REST_SUPPORT_MENU_LABEL_ADD.getLabel():
						RESTFulAnnotations.REST_SUPPORT_MENU_LABEL_REMOVE.getLabel()).click();		
		bot.sleep(Timing.time2S());		
		util.waitForNonIgnoredJobs();
	}
	
	/**
	 * 
	 * @param wsProjectName
	 * @return
	 */
	protected boolean isRestSupportEnabled(String wsProjectName) {		
		return (projectExplorer.isFilePresent(wsProjectName, 
					RESTFulAnnotations.REST_EXPLORER_LABEL.getLabel()) ||
				projectExplorer.isFilePresent(wsProjectName, 
					RESTFulAnnotations.REST_EXPLORER_LABEL_BUILD.getLabel()));
	}
	
}
