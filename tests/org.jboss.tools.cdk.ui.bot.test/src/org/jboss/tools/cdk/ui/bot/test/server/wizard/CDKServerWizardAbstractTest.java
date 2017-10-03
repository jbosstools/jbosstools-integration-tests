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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.reddeer.common.exception.WaitTimeoutExpiredException;
import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.exception.CoreLayerException;
import org.eclipse.reddeer.eclipse.wst.server.ui.wizard.NewServerWizard;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.condition.ControlIsEnabled;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.CancelButton;
import org.eclipse.reddeer.swt.impl.button.NextButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.hamcrest.Matcher;
import org.jboss.tools.cdk.reddeer.core.condition.SystemJobIsRunning;
import org.jboss.tools.cdk.reddeer.core.matcher.JobMatcher;
import org.jboss.tools.cdk.reddeer.requirements.DisableSecureStorageRequirement.DisableSecureStorage;
import org.jboss.tools.cdk.ui.bot.test.CDKAbstractTest;
import org.jboss.tools.cdk.ui.bot.test.utils.CDKTestUtils;
import org.junit.After;
import org.junit.BeforeClass;

/**
 * Abstract class for CDK Server Wizard tests
 * @author odockal
 *
 */
@DisableSecureStorage
public abstract class CDKServerWizardAbstractTest extends CDKAbstractTest {
	
	private static final String separator = System.getProperty("file.separator");
	
	// page description messages
	
	protected static final String NO_USER = "Red Hat Access credentials";
	
	protected static final String DOES_NOT_EXIST = "does not exist";
	
	protected static final String CANNOT_RUN_PROGRAM = "Cannot run program";
	
	protected static final String NOT_EXECUTABLE = "is not executable";
	
	// possible dialog values passed by user

	protected static boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("win");
	
	protected static final String EXISTING_PATH = System.getProperty("user.dir");
	
	protected static final String NON_EXISTING_PATH = EXISTING_PATH + separator + "some_random_filename";
	
	protected static final String NON_EXECUTABLE_FILE = getProjectAbsolutePath("/resources/non-executable");
	
	protected static final String EXECUTABLE_FILE = getProjectAbsolutePath("resources/" + (IS_WINDOWS ? "executable.sh" : "executable.sh"));	
	
	protected static final String MINISHIFT_VALIDATION_JOB = "Validate minishift location";
	
	private static Logger log = Logger.getLogger(CDKServerWizardAbstractTest.class);
	
	protected abstract String getServerAdapter();
	
	public static Matcher<?> getJobMatcher(String title) {
		return new JobMatcher(new Job(title) {

			@Override
			protected IStatus run(IProgressMonitor arg0) {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}
	
	@BeforeClass
	public static void cleanAll() {
		CDKTestUtils.deleteAllCDEServers(SERVER_ADAPTER);
	}
	
	@After
	public void teardown() {
		closeOpenShells();
		CDKTestUtils.deleteCDEServer(getServerAdapter());
		CDKTestUtils.removeAccessRedHatCredentials(CREDENTIALS_DOMAIN, USERNAME);
	}
	
	protected void assertServerType(final String serverType) {
		NewServerWizard wizard = CDKTestUtils.openNewServerWizardDialog();
		try {
			TreeItem item = new DefaultTreeItem(new String[] {SERVER_TYPE_GROUP}).getItem(serverType);
			item.select();
			assertTrue(item.getText().equalsIgnoreCase(serverType));
			new WaitWhile(new JobIsRunning(), TimePeriod.MEDIUM, false);
		} catch (CoreLayerException coreExp) {
			log.error(coreExp.getMessage());
			fail("Server type " + serverType + " was not found in New Server Wizard");
		}
		assertEquals(new LabeledText("Server's host name:").getText(), "localhost");
		assertEquals(new LabeledText("Server name:").getText(), getServerAdapter());
		new WaitUntil(new ControlIsEnabled(new NextButton()), TimePeriod.MEDIUM, false);
		assertTrue("Dialog button Next is not enabled!", wizard.isNextEnabled());
		try {
			new WaitWhile(new JobIsRunning(), TimePeriod.MEDIUM, false);
			new CancelButton().click();
		} catch (WaitTimeoutExpiredException exc) {
			exc.printStackTrace();
			log.error("Dialog could not be canceled because there were unfinished jobs running after timeout" + 
			"\n\rTrying to cancel dialog manually");
			new DefaultShell("New Server").close();
		}
	}
	
	protected void checkWizardPagewidget(final String widgetLabel, final String servetType) {
		// assert that based on choosen server type we got proper cdk server wizard page
		try {
			new LabeledText(widgetLabel);
		} catch (CoreLayerException exc) {
			fail("According to choosen server type (" + servetType + ") "
					+ "it was expected to obtain proper CDK (2.x or 3) based server wizard page.");
		}
	}
	
	protected void assertSameMessage(final NewServerWizard dialog, final String message) {
		new WaitWhile(new SystemJobIsRunning(getJobMatcher(MINISHIFT_VALIDATION_JOB)), TimePeriod.DEFAULT, false);
		String description = dialog.getMessage();
		assertTrue("Expected page description should contain text: " + message +
				" but has: " + description,
				description.contains(message));		
	}
	
	protected void assertDiffMessage(final NewServerWizard dialog, final String message) {
		new WaitWhile(new SystemJobIsRunning(getJobMatcher(MINISHIFT_VALIDATION_JOB)), TimePeriod.DEFAULT, false);
		String description = dialog.getMessage();
		assertFalse("Page descrition should not contain: " + message,
				description.contains(message));
	}
	
	private void closeOpenShells() {
		try {
			new WaitWhile(new ShellIsAvailable("New Server"), TimePeriod.MEDIUM);
		} catch (WaitTimeoutExpiredException exc) {
			new DefaultShell("New Server").close();
		}
	}

}
