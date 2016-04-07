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
package org.jboss.tools.openshift.ui.bot.test.project;

import static org.junit.Assert.*;

import org.hamcrest.core.StringContains;
import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.eclipse.ui.browser.BrowserEditor;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS3;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.junit.Test;

public class ShowProjectInWebConsoleTest {

	@Test
	public void testShowProjectInWebConsole() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		explorer.getOpenShift3Connection().getProject().select();
		new ContextMenu(OpenShiftLabel.ContextMenu.SHOW_IN_WEB_CONSOLE).select();
		
		try {
			BrowserEditor browser = new BrowserEditor(new StringContains("Login - Red Hat"));
			assertTrue("Web console was not opened in context of a project", 
					browser.getPageURL().contains(DatastoreOS3.PROJECT1));
			browser.close();
		} catch (RedDeerException ex) {
			fail("Web Console for a project has not been opened");
		}
	}
	
}
