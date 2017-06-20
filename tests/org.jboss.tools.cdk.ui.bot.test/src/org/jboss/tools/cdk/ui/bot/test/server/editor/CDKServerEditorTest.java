/******************************************************************************* 
 * Copyright (c) 2017 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.cdk.ui.bot.test.server.editor;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardDialog;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardPage;
import org.jboss.tools.cdk.reddeer.server.ui.CDEServersView;
import org.jboss.tools.cdk.reddeer.server.ui.editor.CDEServerEditor;
import org.jboss.tools.cdk.reddeer.server.ui.editor.CDKServerEditor;
import org.jboss.tools.cdk.reddeer.server.ui.wizard.NewCDKServerContainerWizardPage;
import org.jboss.tools.cdk.ui.bot.test.server.wizard.CDKServerWizardAbstractTest;
import org.jboss.tools.cdk.ui.bot.test.utils.CDKTestUtils;
import org.junit.After;
import org.junit.Test;

/**
 * Tests for CDK 2.x server editor page
 * @author odockal
 *
 */
public class CDKServerEditorTest extends CDKServerWizardAbstractTest {
	
	private ServersView serversView;
	
	private CDEServerEditor editor;
	
	private void setServerEditor() {
		serversView = new CDEServersView(true);
		serversView.open();
		serversView.getServer(SERVER_ADAPTER).open();
		editor = new CDKServerEditor(SERVER_ADAPTER);
		editor.activate();
	}
	
	@After
	public void tearDown() {
		cleanUp();
	}
	
	@Test
	public void testCDK3ServerEditor() {
		addCDKServer(VAGRANTFILE_PATH);
		setServerEditor();
		assertTrue(editor.getUsernameLabel().getText().equalsIgnoreCase("sub_username"));
		assertTrue(editor.getPasswordLabel().getText().equalsIgnoreCase("sub_password"));
		assertTrue(editor.getDomainCombo().getSelection().equalsIgnoreCase(CREDENTIALS_DOMAIN));
		assertTrue(editor.getHostnameLabel().getText().equalsIgnoreCase(SERVER_HOST));
		assertTrue(((CDKServerEditor)editor).getVagrantfileLocation().getText().equals(VAGRANTFILE_PATH));
		assertTrue(editor.getServernameLabel().getText().equals(SERVER_ADAPTER));
	}
	
	private void cleanUp() {
		if (editor != null) {
			if (!editor.isActive()) {
				editor.activate();
			}
			editor.save();
			editor.close();
			editor = null;
		}
		if (serversView.isOpened() && serversView != null) {
			serversView.close();
			serversView = null;
		}
	}

	private static void addCDKServer(String vagrantfile) {
		NewServerWizardDialog dialog = CDKTestUtils.openNewServerWizardDialog();
		NewServerWizardPage page = new NewServerWizardPage();
		
		page.selectType(SERVER_TYPE_GROUP, CDK_SERVER_NAME);
		dialog.next();
		NewCDKServerContainerWizardPage containerPage = new NewCDKServerContainerWizardPage();
		containerPage.setCredentials(USERNAME, PASSWORD);
		containerPage.setFolder(vagrantfile);
		if (!dialog.isFinishEnabled()) {
			new WaitUntil(new JobIsRunning(), TimePeriod.SHORT, false);			
		}
		dialog.finish();
	}
}
