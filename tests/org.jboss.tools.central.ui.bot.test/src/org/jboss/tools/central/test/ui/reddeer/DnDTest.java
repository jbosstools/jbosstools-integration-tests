/*******************************************************************************
 * Copyright (c) 2007-2016 Red Hat, Inc.
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

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;
import org.jboss.reddeer.swt.impl.button.NoButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.central.reddeer.api.JavaScriptHelper;
import org.jboss.tools.central.test.ui.reddeer.internal.CentralBrowserIsLoading;
import org.junit.Before;
import org.junit.Test;

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
		new WaitWhile(new JobIsRunning());
		try {
			new DefaultShell("Install New Software");
		} catch (RedDeerException e) {
			return false;
		}
		new NoButton().click();
		return true;
	}
}
