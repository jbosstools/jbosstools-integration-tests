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

package org.jboss.tools.ws.ui.bot.test.rest.explorer;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ws.ui.bot.test.WSTestBase;
import org.jboss.tools.ws.ui.bot.test.utils.NodeContextUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author jjankovi
 *
 */
public class RESTfulValidationTest extends WSTestBase {
	
	private static final String CONFIGURE_MENU_LABEL = "Configure";
	private static final String REST_SUPPORT_MENU_LABEL_ADD = "Add JAX-RS 1.1 support...";
	private static final String REST_SUPPORT_MENU_LABEL_REMOVE = "Remove JAX-RS 1.1 support...";
	private static final String REST_EXPLORER_LABEL = "JAX-RS REST Web Services";
	
	protected String getWsProjectName() {
		return "RestExplorerTest";
	}
	
	protected String getWsPackage() {
		return "org.rest.explorer.validation.test";
	}

	protected String getWsName() {
		return "RestService";
	}
	
	@Before
	public void setup() {		
		if (!projectExists(getWsProjectName())) {
			projectHelper.createProject(getWsProjectName());
		}
	}
	
	
	@Test
	public void testSupportJAX_RS1_1_Explorer() {
		
		addRestSupport(getWsProjectName());
		
		projectExplorer.selectProject(getWsProjectName());
		SWTBotTree tree = projectExplorer.bot().tree();
		SWTBotTreeItem item = tree.getTreeItem(getWsProjectName());		
		assertTrue(NodeContextUtil.nodeContextMenu(tree, item, CONFIGURE_MENU_LABEL, 
				REST_SUPPORT_MENU_LABEL_REMOVE).isVisible());
		assertTrue(projectExplorer.isFilePresent(getWsProjectName(), REST_EXPLORER_LABEL));
		
	}

	private void addRestSupport(String wsProjectName) {
		projectExplorer.selectProject(wsProjectName);
		SWTBotTree tree = projectExplorer.bot().tree();
		SWTBotTreeItem item = tree.getTreeItem(wsProjectName);
		item.expand();
		NodeContextUtil.nodeContextMenu(tree, item, CONFIGURE_MENU_LABEL, 
				REST_SUPPORT_MENU_LABEL_ADD).click();		
		bot.sleep(Timing.time2S());		
		util.waitForNonIgnoredJobs();
	}
	
}
