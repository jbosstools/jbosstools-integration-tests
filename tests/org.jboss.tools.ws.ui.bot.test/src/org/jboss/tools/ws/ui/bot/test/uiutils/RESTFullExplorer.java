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

package org.jboss.tools.ws.ui.bot.test.uiutils;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ws.ui.bot.test.rest.RESTFulAnnotations;

public class RESTFullExplorer extends SWTTestExt {

	private SWTBotTreeItem restFulExplorer;
	
	public RESTFullExplorer(String wsProjectName) {
		String[] pathToRestExplorer = {wsProjectName};
		restFulExplorer = projectExplorer.selectTreeItem(
				RESTFulAnnotations.REST_EXPLORER_LABEL.getLabel(), 
				pathToRestExplorer).expand().expand();		
	}
	
	/**
	 * 
	 * @return
	 */
	public SWTBotTreeItem[] getAllRestServices() {
		return restFulExplorer.getItems();
	}
	
	public SWTBotTreeItem restService(String method) {
		SWTBotTreeItem getMethod = null;
		for (SWTBotTreeItem ti : getAllRestServices()) {
			if (ti.getText().contains("GET")) {
				getMethod = ti;
				break;
			}
		}
		return getMethod;
	}
	
	public RunOnServerDialog runOnServer(SWTBotTreeItem service) {
		SWTBotTree tree = projectExplorer.bot().tree();
		ContextMenuHelper.prepareTreeItemForContextMenu(tree, service);
		SWTBotMenu menu = new SWTBotMenu(ContextMenuHelper.
				getContextMenu(tree, "Run As", false));
		menu.menu("1 Run on Server").click();
		return new RunOnServerDialog();
	}
	
	/**
	 * 
	 * @param restService
	 * @return
	 */
	public SWTBotTreeItem[] getAllInfoAboutRestService(SWTBotTreeItem restService) {
		restService.expand();
		return restService.getItems();
	}
	
	/**
	 * 
	 * @param restService
	 * @return
	 */
	public String getConsumesInfo(SWTBotTreeItem restService) {
		for (SWTBotTreeItem ti: getAllInfoAboutRestService(restService)) {
			if (ti.getText().contains("consumes:")) {
				return ti.getText().split(" ")[1];
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param restService
	 * @return
	 */
	public String getProducesInfo(SWTBotTreeItem restService) {
		for (SWTBotTreeItem ti: getAllInfoAboutRestService(restService)) {
			if (ti.getText().contains("produces:")) {
				return ti.getText().split(" ")[1];
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param restService
	 * @return
	 */
	public String getRestServiceName(SWTBotTreeItem restService) {
		return restService.getText().split(" ")[0];
	}
	
	/**
	 * 
	 * @param restService
	 * @return
	 */
	public String getPathForRestFulService(SWTBotTreeItem restService) {		
		return restService.getText().split(" ")[1];
	}
	
}