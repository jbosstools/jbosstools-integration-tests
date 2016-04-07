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
package org.jboss.tools.openshift.ui.bot.test.connection;

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.core.util.Display;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;
import org.jboss.reddeer.swt.impl.link.AnchorLink;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.openshift.reddeer.condition.BrowserContainsText;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.junit.After;
import org.junit.Test;

/**
 * Test opening a new account link in connection shell.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID111OpenNewConnectionLinkTest {

	@Test
	public void testNewUserLink() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		explorer.openConnectionShell();
		
		new DefaultShell(OpenShiftLabel.Shell.NEW_CONNECTION);

		new AnchorLink("sign up here").click();
		
		try {
			new WaitUntil(new BrowserContainsText("Create an account"), TimePeriod.LONG);
			// PASS
		} catch (WaitTimeoutExpiredException ex) {
			fail("Registration page has not been opened successfully.");
		}
	}
	
	@After
	public void closeBrowser() {
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
				new InternalBrowser().getSWTWidget().close();
			}
		});
	}
}
