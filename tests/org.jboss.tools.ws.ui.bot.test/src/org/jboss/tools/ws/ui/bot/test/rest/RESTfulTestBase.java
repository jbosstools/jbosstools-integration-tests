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

package org.jboss.tools.ws.ui.bot.test.rest;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.gen.IPreference;
import org.jboss.tools.ui.bot.ext.view.ProblemsView;
import org.jboss.tools.ws.ui.bot.test.WSTestBase;
import org.jboss.tools.ws.ui.bot.test.utils.NodeContextUtil;

/**
 * Test base for bot tests using RESTFul support
 * 
 * @author jjankovi
 * 
 */
@Require(server = @Server(), perspective = "Java EE")
public class RESTfulTestBase extends WSTestBase {

	protected final String PATH_PARAM_VALID_ERROR = "Invalid @PathParam value";
	protected final String VALIDATION_PREFERENCE = "Validation";
	protected final String ENABLE_ALL = "Enable All";
	protected final String JAX_RS_VALIDATOR = "JAX-RS Metamodel Validator";
	protected final String CONFIGURE_MENU_LABEL = "Configure";
	protected final String REST_SUPPORT_MENU_LABEL_ADD = "Add JAX-RS 1.1 support...";
	protected final String REST_SUPPORT_MENU_LABEL_REMOVE = "Remove JAX-RS 1.1 support...";
	protected final String REST_EXPLORER_LABEL = "JAX-RS REST Web Services";
	protected final String REST_EXPLORER_LABEL_BUILD = "Building RESTful Web Services...";
	protected final String VALIDATION_SETTINGS_CHANGED = "Validation Settings Changed";

	private enum ConfigureOption {
		ADD, REMOVE;
	}

	protected SWTBotTreeItem[] getRESTValidationErrors(String wsProjectName) {
		return ProblemsView.getFilteredErrorsTreeItems(bot,
				PATH_PARAM_VALID_ERROR, "/" + wsProjectName, null, null);
	}

	protected void enableRESTValidation() {
		modifyRESTValidation(ConfigureOption.ADD);
	}

	protected void disableRESTValidation() {
		modifyRESTValidation(ConfigureOption.REMOVE);
	}

	
	/**
	 * DO IT BETTER!!!!!!!!!!!!!!!!!!
	 */
	protected void modifyRESTValidation(ConfigureOption option) {
		
		SWTBot validationBot = openValidationPreference(VALIDATION_PREFERENCE, new ArrayList<String>());
		
		validationBot.button(ENABLE_ALL).click();
		
		if (option == ConfigureOption.REMOVE) {
			
			SWTBotTable validatorTable = validationBot.table();
			int restValidationRow = -1;
			for (int row = 0; row < validatorTable.rowCount(); row++) {
				if (validatorTable.getTableItem(row).getText()
						.equals(JAX_RS_VALIDATOR)) {
					restValidationRow = row;
					break;
				}
			}
			
			assertTrue(restValidationRow >= 0);
			
			validatorTable.click(restValidationRow, 1);
			validatorTable.click(restValidationRow, 2);
		}
		
		validationBot.button("OK").click();
		if (bot.activeShell().getText().equals(VALIDATION_SETTINGS_CHANGED)) {
			bot.activeShell().bot().button("Yes").click();
		}
		
		bot.sleep(Timing.time3S());
		util.waitForNonIgnoredJobs();
		
	}

	protected void addRestSupport(String wsProjectName) {
		configureRestSupport(wsProjectName, ConfigureOption.ADD);
	}

	protected void removeRestSupport(String wsProjectName) {
		configureRestSupport(wsProjectName, ConfigureOption.REMOVE);
	}

	
	protected boolean isRestSupportEnabled(String wsProjectName) {
		return (projectExplorer.isFilePresent(wsProjectName,
				RESTFulAnnotations.REST_EXPLORER_LABEL.getLabel()) || projectExplorer
				.isFilePresent(wsProjectName,
						RESTFulAnnotations.REST_EXPLORER_LABEL_BUILD.getLabel()));
	}

	protected static void addRestEasyLibs(String wsProjectName) {
		assertTrue(configuredState.getServer().type.equals("EAP"));
	}
	
	
	private void configureRestSupport(String wsProjectName,
			ConfigureOption option) {
		projectExplorer.selectProject(wsProjectName);
		SWTBotTree tree = projectExplorer.bot().tree();
		SWTBotTreeItem item = tree.getTreeItem(wsProjectName);
		item.expand();
		NodeContextUtil.nodeContextMenu(tree, item,
						RESTFulAnnotations.CONFIGURE_MENU_LABEL.getLabel(),
						option == ConfigureOption.ADD ? RESTFulAnnotations.REST_SUPPORT_MENU_LABEL_ADD
						.getLabel() : RESTFulAnnotations.REST_SUPPORT_MENU_LABEL_REMOVE.getLabel()).click();
		bot.sleep(Timing.time2S());
		util.waitForNonIgnoredJobs();
	}
	
	private SWTBot openValidationPreference(final String name, final List<String> groupPath) {
		return open.preferenceOpen(new IPreference() {
			
			@Override
			public String getName() {
				return name;
			}
			
			@Override
			public List<String> getGroupPath() {
				return groupPath;
			}
		});
	}

}
