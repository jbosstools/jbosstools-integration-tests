/*************************************************************************************
 * Copyright (c) 2019 Red Hat, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     JBoss by Red Hat - Initial implementation.
 ************************************************************************************/
package org.jboss.tools.maven.ui.bot.test.ui;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.reddeer.common.matcher.RegexMatcher;
import org.eclipse.reddeer.common.platform.RunningPlatform;
import org.eclipse.reddeer.common.util.Display;
import org.eclipse.reddeer.core.matcher.WithTextMatcher;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.swt.api.Table;
import org.eclipse.reddeer.swt.api.TableItem;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.eclipse.ui.internal.dialogs.AboutDialog;
import org.jboss.tools.maven.ui.bot.test.AbstractMavenSWTBotTest;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author rawagner@redhat.com
 * 
 */

@RunWith(RedDeerSuite.class)
public class SeamPluginsTest extends AbstractMavenSWTBotTest {

	private static String HELP_BUTTON = "Help";
	private static String ABOUT_MENU_BUTTON = ".*About Red Hat.*(Developer|CodeReady) Studio.*";

	@Test
	public void testSeamIsNotPresent() {
		log.info("Navigate to installation details");
		WorkbenchShell ws = new WorkbenchShell();
		ws.maximize();
		// for OSX (MacOS) use eclipse API to open About dialog
		if (RunningPlatform.isOSX()) {
			log.info("Open " + HELP_BUTTON + " -> " + ABOUT_MENU_BUTTON + "directly on Mac OSX");
			Display.asyncExec(new Runnable() {
				@Override
				public void run() {
					@SuppressWarnings("restriction")
					AboutDialog dialog = new AboutDialog(ws.getSWTWidget());
					dialog.open();
				}
			});
		} else {
			new ShellMenuItem(new WithTextMatcher(new RegexMatcher(HELP_BUTTON)),
					new WithTextMatcher(new RegexMatcher(ABOUT_MENU_BUTTON))).select();
		}

		new DefaultShell(new WithTextMatcher(new RegexMatcher(ABOUT_MENU_BUTTON)));
		new PushButton("Installation Details").click();
		new DefaultShell(new WithTextMatcher(new RegexMatcher(".*Installation Details")));
		new DefaultCTabItem("Plug-ins").activate();
		new DefaultText().setText("seam");
		Table pluginsTable = new DefaultTable();
		List<TableItem> items = pluginsTable.getItems();
		int columntIndex = pluginsTable.getHeaderIndex("Plug-in Id");
		List<String> foundSeamPlugins = new ArrayList<String>();
		for (TableItem ti : items) {
			String pluginId = ti.getText(columntIndex);
			if (pluginId.equals("org.jboss.tools.cdi.xml") || pluginId.equals("org.jboss.tools.cdi.xml.ui")
					|| pluginId.equals("org.jboss.tools.cdi.seam.solder.core")
					|| pluginId.equals("org.jboss.tools.seam.reddeer")) {
				// these seam plugins can exist in default installation (seam3 or reddeer)
			} else {
				foundSeamPlugins.add(pluginId);
			}
		}
		new PushButton("Close").click();
		new PushButton("Close").click();
		if (!foundSeamPlugins.isEmpty()) {
			fail("seam plugins " + foundSeamPlugins + " is/are present but all seam2 plugins should be removed");
		}
	}

}
