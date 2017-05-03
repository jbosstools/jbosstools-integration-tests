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
package org.jboss.tools.openshift.ui.bot.test.application.cartridge;

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.reddeer.jface.handler.TreeViewerHandler;
import org.jboss.reddeer.swt.condition.ControlIsEnabled;
import org.jboss.reddeer.swt.condition.ShellIsAvailable;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.workbench.core.condition.JobIsRunning;
import org.jboss.tools.openshift.reddeer.condition.v2.OpenShiftApplicationExists;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteUtils;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.resources.OpenShift2Application;
import org.jboss.tools.openshift.ui.bot.test.application.create.IDXXXCreateTestingApplication;
import org.junit.Test;

public class ID604AddJenkinsCartridgeWithoutJenkinsApplicationTest extends IDXXXCreateTestingApplication {

	private String jenkinsApplication = "jenkins" + System.currentTimeMillis();
	
	@Test
	public void testAddJenkinsCartridgeWithoutJenkinsApplication() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		TreeViewerHandler treeViewerHandler = TreeViewerHandler.getInstance();
		OpenShift2Application application = explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER).
				getDomain(DatastoreOS2.DOMAIN).getApplication(applicationName);
		application.select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.EMBED_CARTRIDGE).select();
		
		new DefaultShell(OpenShiftLabel.Shell.EDIT_CARTRIDGES);
		
		new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.JENKINS).select();
		try {
			new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.JENKINS).setChecked(true);
		} catch (WaitTimeoutExpiredException ex) {
			// pass
		}
		
		new DefaultShell(OpenShiftLabel.Shell.ADD_CARTRIDGE_DIALOG);
		new PushButton(OpenShiftLabel.Button.APPLY).click();
		
		new DefaultShell("New Jenkins application");
		new DefaultText().setText(jenkinsApplication);
		
		new WaitUntil(new ControlIsEnabled(new OkButton()));
		
		new OkButton().click();
		
		new WaitUntil(new ShellIsAvailable(OpenShiftLabel.Shell.EMBEDDED_CARTRIDGE), TimePeriod.VERY_LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.EMBEDDED_CARTRIDGE);
		new OkButton().click();
		
		new DefaultShell(OpenShiftLabel.Shell.EDIT_CARTRIDGES);
		new FinishButton().click();
		
		new WaitUntil(new ShellIsAvailable(OpenShiftLabel.Shell.EMBEDDED_CARTRIDGE), TimePeriod.VERY_LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.EMBEDDED_CARTRIDGE).setFocus();
		new OkButton().click();
		
		new WaitWhile(new ShellIsAvailable(OpenShiftLabel.Shell.EDIT_CARTRIDGES), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	
		new WaitUntil(new OpenShiftApplicationExists(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, 
				jenkinsApplication), TimePeriod.LONG);
		
		try {
			application.select();
			treeViewerHandler.getTreeItem(application.getTreeItem(), "Jenkins Client");
		} catch (JFaceLayerException ex) {
			fail("Jenkins cartridge has not been added into the application. " + application.
					getTreeItem().getItems().get(0).getText());
		}
		
		explorer.open();
		new DeleteUtils(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, 
				jenkinsApplication, jenkinsApplication).deleteOpenShiftApplication();
	}
}
