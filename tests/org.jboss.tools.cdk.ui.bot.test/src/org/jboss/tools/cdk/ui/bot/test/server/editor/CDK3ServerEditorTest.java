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

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardDialog;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardPage;
import org.jboss.tools.cdk.reddeer.requirements.DisableSecureStorageRequirement.DisableSecureStorage;
import org.jboss.tools.cdk.reddeer.server.ui.CDEServersView;
import org.jboss.tools.cdk.reddeer.server.ui.editor.CDEServerEditor;
import org.jboss.tools.cdk.reddeer.server.ui.editor.CDK3ServerEditor;
import org.jboss.tools.cdk.reddeer.server.ui.wizard.NewCDK3ServerContainerWizardPage;
import org.jboss.tools.cdk.ui.bot.test.server.wizard.CDKServerWizardAbstractTest;
import org.jboss.tools.cdk.ui.bot.test.utils.CDKTestUtils;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Class tests CDK3 server editor page
 * @author odockal
 *
 */
@DisableSecureStorage
public class CDK3ServerEditorTest extends CDKServerWizardAbstractTest {
	
	private ServersView serversView;
	
	private CDEServerEditor editor;
	
	private static final String ANOTHER_HYPERVISOR = "virtualbox";
	
	private void setServerEditor() {
		serversView = new CDEServersView(true);
		serversView.open();
		serversView.getServer(SERVER_ADAPTER).open();
		editor = new CDK3ServerEditor(SERVER_ADAPTER);
		editor.activate();
	}
	
	@After
	public void tearDown() {
		cleanUp();
	}
	
	@Test
	public void testCDK3ServerEditor() {
		addCDK3Server(MINISHIFT_HYPERVISOR, MINISHIFT_PATH);
		setServerEditor();
		assertTrue(editor.getUsernameLabel().getText().equalsIgnoreCase("minishift_username"));
		assertTrue(editor.getPasswordLabel().getText().equalsIgnoreCase("minishift_password"));
		assertTrue(editor.getDomainCombo().getSelection().equalsIgnoreCase(CREDENTIALS_DOMAIN));
		assertTrue(editor.getHostnameLabel().getText().equalsIgnoreCase(SERVER_HOST));
		assertTrue(((CDK3ServerEditor)editor).getHypervisorCombo().getSelection().equalsIgnoreCase(MINISHIFT_HYPERVISOR));
		assertTrue(editor.getServernameLabel().getText().equals(SERVER_ADAPTER));
		assertTrue(((CDK3ServerEditor)editor).getMinishiftBinaryLabel().getText().equals(MINISHIFT_PATH));
		assertTrue(((CDK3ServerEditor)editor).getMinishiftHomeLabel().getText().contains(".minishift"));
	}
	
	@Test
	public void testCDK3Hypervisor() {
		addCDK3Server(ANOTHER_HYPERVISOR, MINISHIFT_PATH);
		setServerEditor();
		assertTrue(((CDK3ServerEditor)editor).getHypervisorCombo().getSelection().equalsIgnoreCase(ANOTHER_HYPERVISOR));
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
		if (serversView.isOpened()) {
			serversView.close();
			serversView = null;
		}
	}

	private static void addCDK3Server(String hypervisor, String binary) {
		NewServerWizardDialog dialog = CDKTestUtils.openNewServerWizardDialog();
		NewServerWizardPage page = new NewServerWizardPage();
		
		page.selectType(SERVER_TYPE_GROUP, CDK3_SERVER_NAME);
		dialog.next();
		NewCDK3ServerContainerWizardPage containerPage = new NewCDK3ServerContainerWizardPage();
		containerPage.setCredentials(USERNAME, PASSWORD);
		containerPage.setHypervisor(hypervisor);
		containerPage.setMinishiftBinary(binary);
		if (!dialog.isFinishEnabled()) {
			new WaitUntil(new JobIsRunning(), TimePeriod.SHORT, false);			
		}
		dialog.finish();
	}
	
}
