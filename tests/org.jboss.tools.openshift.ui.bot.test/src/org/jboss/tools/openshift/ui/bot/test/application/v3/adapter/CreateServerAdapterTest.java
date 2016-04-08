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
package org.jboss.tools.openshift.ui.bot.test.application.v3.adapter;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardDialog;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardPage;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.impl.button.BackButton;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.openshift.reddeer.condition.AmountOfResourcesExists;
import org.jboss.tools.openshift.reddeer.condition.ResourceExists;
import org.jboss.tools.openshift.reddeer.condition.ServerAdapterExists;
import org.jboss.tools.openshift.reddeer.enums.Resource;
import org.jboss.tools.openshift.reddeer.enums.ResourceState;
import org.jboss.tools.openshift.reddeer.exception.OpenShiftToolsException;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS3;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.resources.ServerAdapter;
import org.jboss.tools.openshift.reddeer.view.resources.ServerAdapter.Version;
import org.jboss.tools.openshift.ui.bot.test.application.v3.create.AbstractCreateApplicationTest;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class CreateServerAdapterTest extends AbstractCreateApplicationTest {

	@BeforeClass
	public static void waitTillApplicationIsRunning() {
		new WaitWhile(new ResourceExists(Resource.BUILD, "eap-app-1", ResourceState.COMPLETE),
				TimePeriod.getCustom(360), false);
		new WaitUntil(new AmountOfResourcesExists(Resource.POD, 2), TimePeriod.LONG, false);
	}
	
	@Test
	public void testCreateOpenShift3ServerAdapterViaShellMenu() {
		NewServerWizardDialog dialog = new NewServerWizardDialog();
		NewServerWizardPage page = new NewServerWizardPage();
		
		dialog.open();
		page.selectType(OpenShiftLabel.Others.OS3_SERVER_ADAPTER);
		next();
		
		next();
		
		setAdapterDetailsAndCreateAdapterAndVerifyExistence();
	}
	
	@Test
	public void testCreateOpenShift3ServerAdapterViaServersView() {
		ServersView serversView = new ServersView();
		serversView.open();
		new ContextMenu(OpenShiftLabel.ContextMenu.NEW_SERVER).select();
		
		new DefaultShell(OpenShiftLabel.Shell.ADAPTER);
		new DefaultTreeItem(OpenShiftLabel.Others.OS3_SERVER_ADAPTER).select();
		next();
		
		next();
		
		setAdapterDetailsAndCreateAdapterAndVerifyExistence();
	}
	
	private void setAdapterDetailsAndCreateAdapterAndVerifyExistence() {
		new LabeledText("Eclipse Project: ").setText(projectName);
		new DefaultTreeItem(DatastoreOS3.PROJECT1).getItems().get(0).select();
		next();
		
		new FinishButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.ADAPTER));
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG, false);
		
		assertTrue("OpenShift 3 server adapter was not created.", 
				new ServerAdapterExists(Version.OPENSHIFT3, buildConfigName).test());
	}
	
	private void next() {
		new WaitUntil(new WidgetIsEnabled(new NextButton()));
		
		new NextButton().click();

		new WaitUntil(new WidgetIsEnabled(new BackButton()));
	}
	
	@After
	public void removeAdapterIfExists() {
		try {
			new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
			new ServerAdapter(Version.OPENSHIFT3, buildConfigName).delete();
		} catch (OpenShiftToolsException ex) {
			// do nothing, adapter does not exists
		}
	}
}
