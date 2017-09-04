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
package org.jboss.tools.cdk.ui.bot.test.server.wizard;

import static org.junit.Assert.assertTrue;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.eclipse.wst.server.ui.wizard.NewServerWizard;
import org.eclipse.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardPage;
import org.eclipse.reddeer.swt.condition.ControlIsEnabled;
import org.eclipse.reddeer.swt.impl.button.FinishButton;
import org.jboss.tools.cdk.reddeer.server.ui.wizard.NewCDK3ServerContainerWizardPage;
import org.jboss.tools.cdk.ui.bot.test.utils.CDKTestUtils;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Class for testing CDK3 server wizard functionality
 * @author odockal
 *
 */
public class CDK3ServerWizardTest extends CDKServerWizardAbstractTest {

	// page description messages
	
	private static final String CHECK_MINISHIFT_VERSION = "Unknown error while checking minishift version";
	
	@Override
	protected String getServerAdapter() {
		return SERVER_ADAPTER_3;
	}
	
	@BeforeClass
	public static void setup() {
		checkMinishiftParameters();
	}
	
	@Test
	public void testCDK3ServerType() {
		assertServerType(CDK3_SERVER_NAME);
	}
	
	@Test
	public void testNewCDK3ServerWizard() {
		NewServerWizard dialog = CDKTestUtils.openNewServerWizardDialog();
		NewServerWizardPage page = new NewServerWizardPage(dialog);
		
		page.selectType(SERVER_TYPE_GROUP, CDK3_SERVER_NAME);
		page.setName(getServerAdapter());
		dialog.next();
		NewCDK3ServerContainerWizardPage containerPage = new NewCDK3ServerContainerWizardPage();
		
		checkWizardPagewidget("Minishift Binary: ", CDK3_SERVER_NAME);

		// just check that default domain is choosen correctly
		assertTrue(containerPage.getDomain().equalsIgnoreCase(CREDENTIALS_DOMAIN));
		
		// to change dialog page description folder must be set with path where is no vagrantfile
		containerPage.setMinishiftBinary(NON_EXECUTABLE_FILE);
		
		// first error description will demand vagrantfile in the path or not?
		// seems that adding the user has priority over checking vagrantfile
		assertSameMessage(dialog, NO_USER);
		containerPage.setCredentials(USERNAME, PASSWORD);
		// now the description should have changed to "no vagrantfile" 
		assertSameMessage(dialog, CANNOT_RUN_PROGRAM);
		containerPage.setMinishiftBinary(NON_EXISTING_PATH);
		assertSameMessage(dialog, DOES_NOT_EXIST);
		containerPage.setMinishiftBinary(EXECUTABLE_FILE);
		assertSameMessage(dialog, CHECK_MINISHIFT_VERSION);
		containerPage.setMinishiftBinary(MINISHIFT_PATH);
		assertDiffMessage(dialog, CHECK_MINISHIFT_VERSION);
		new WaitUntil(new ControlIsEnabled(new FinishButton()), TimePeriod.MEDIUM, false);
		assertTrue("Expected Finish button is not enabled", dialog.isFinishEnabled());
		dialog.finish();
	}

}
