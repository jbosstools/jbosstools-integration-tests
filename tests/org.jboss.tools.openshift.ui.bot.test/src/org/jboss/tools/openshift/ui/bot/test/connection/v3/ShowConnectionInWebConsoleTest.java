/*******************************************************************************
 * Copyright (c) 2007-2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v 1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.ui.bot.test.connection.v3;

import static org.junit.Assert.fail;

import org.hamcrest.core.StringContains;
import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.eclipse.ui.browser.BrowserEditor;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.junit.Test;

public class ShowConnectionInWebConsoleTest {

	@Test
	public void testShowConnectionInWebConsole() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		explorer.getOpenShift3Connection().select();
		new ContextMenu(OpenShiftLabel.ContextMenu.SHOW_IN_WEB_CONSOLE).select();
		
		try {
			BrowserEditor browser = new BrowserEditor(new StringContains("Login - Red Hat"));
			browser.close();
		} catch (RedDeerException ex) {
			fail("Web Console for a project has not been opened");
		}
	}
}
