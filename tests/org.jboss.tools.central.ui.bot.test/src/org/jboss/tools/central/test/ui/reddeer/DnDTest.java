/*******************************************************************************
 * Copyright (c) 2007-2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.central.test.ui.reddeer;

import static org.hamcrest.MatcherAssert.assertThat;

import org.eclipse.reddeer.common.exception.RedDeerException;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.browser.InternalBrowser;
import org.eclipse.reddeer.swt.impl.button.NoButton;
import org.eclipse.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.central.reddeer.api.JavaScriptHelper;
import org.jboss.tools.central.test.ui.reddeer.internal.CentralBrowserIsLoading;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author rhopp
 * @contributor jkopriva@redhat.com
 *
 */

@RunWith(RedDeerSuite.class)
public class DnDTest {

	private static final String CENTRAL_LABEL = "Red Hat Central";

	@Before
	public void setup() {
		new DefaultToolItem(new WorkbenchShell(), CENTRAL_LABEL).click();
		new DefaultEditor(CENTRAL_LABEL);
		InternalBrowser centralBrowser = new InternalBrowser();
		JavaScriptHelper.getInstance().setBrowser(centralBrowser);
		new WaitWhile(new CentralBrowserIsLoading(), TimePeriod.LONG);
	}

	@Test
	public void centralBrowserDnDTestNegative() {
		dropStringToCentralBrowser("wrongInput!");
		assertThat("Installation should have not been started", !installationStartedCheck());
	}

	@Test
	public void centralBrowserDnDTestPositive() {
		dropStringToCentralBrowser(
				"http://download.jboss.org/jbosstools/central/install?connectors=org.tigris.subversion.subclipse.mylyn,org.jboss.tools.arquillian,org.eclipse.m2e");
		assertThat("Installation should have been started", installationStartedCheck());
	}

	private void dropStringToCentralBrowser(String whatToSend) {
		InternalBrowser centralBrowser = new InternalBrowser();
		String script = String.format(
				"drop({preventDefault:function(){}, dataTransfer:{getData:function(test){return \"%s\"}}})",
				whatToSend);
		centralBrowser.execute(script);
	}

	private boolean installationStartedCheck() {
		try {
			new WaitUntil(new ShellIsAvailable("Progress Information"), TimePeriod.LONG);
			new WaitUntil(new ShellIsAvailable("Install New Software"), TimePeriod.LONG);
		} catch (RedDeerException e) {
			return false;
		}
		new NoButton().click();
		return true;
	}
}
