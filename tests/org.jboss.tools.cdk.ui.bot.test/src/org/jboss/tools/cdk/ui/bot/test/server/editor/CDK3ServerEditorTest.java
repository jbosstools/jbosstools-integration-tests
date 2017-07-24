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

import org.jboss.reddeer.common.condition.WaitCondition;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.util.Display;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardDialog;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardPage;
import org.jboss.reddeer.swt.condition.ShellIsAvailable;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.workbench.handler.EditorHandler;
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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.IEditorPart;
import org.hamcrest.BaseMatcher;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * Class tests CDK3 server editor page
 * 
 * @author odockal
 *
 */
@DisableSecureStorage
public class CDK3ServerEditorTest extends CDKServerWizardAbstractTest {

	private ServersView serversView;

	private CDEServerEditor editor;
	
	@SuppressWarnings("rawtypes")
	private Matcher jobMatcher;

	private static final String ANOTHER_HYPERVISOR = "virtualbox";
	
	private static final String MINISHIFT_VALIDATION_JOB = "Validate minishift location";

	private static Logger log = Logger.getLogger(CDK3ServerEditorTest.class);

	public CDK3ServerEditorTest() {
		this.jobMatcher = new JobMatcher(new Job(MINISHIFT_VALIDATION_JOB) {

			@Override
			protected IStatus run(IProgressMonitor arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
		});
	}
	
	private void setServerEditor() {
		serversView = new CDEServersView(true);
		serversView.open();
		serversView.getServer(SERVER_ADAPTER).open();
		editor = new CDK3ServerEditor(SERVER_ADAPTER);
		editor.activate();
		new WaitUntil(new JobIsRunning(), TimePeriod.getCustom(1), false);
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
		assertTrue(
				((CDK3ServerEditor) editor).getHypervisorCombo().getSelection().equalsIgnoreCase(MINISHIFT_HYPERVISOR));
		assertTrue(editor.getServernameLabel().getText().equals(SERVER_ADAPTER));
		assertTrue(((CDK3ServerEditor) editor).getMinishiftBinaryLabel().getText().equals(MINISHIFT_PATH));
		assertTrue(((CDK3ServerEditor) editor).getMinishiftHomeLabel().getText().contains(".minishift"));
	}

	@Test
	public void testCDK3Hypervisor() {
		addCDK3Server(ANOTHER_HYPERVISOR, MINISHIFT_PATH);
		setServerEditor();

		assertTrue(
				((CDK3ServerEditor) editor).getHypervisorCombo().getSelection().equalsIgnoreCase(ANOTHER_HYPERVISOR));
	}

	@Test
	public void testInvalidMinishiftLocation() {
		addCDK3Server(MINISHIFT_HYPERVISOR, MINISHIFT_PATH);
		setServerEditor();

		checkEditorStateAfterSave(NON_EXECUTABLE_FILE, false);
		checkEditorStateAfterSave(NON_EXISTING_PATH, false);
		checkEditorStateAfterSave(EXECUTABLE_FILE, false);
		checkEditorStateAfterSave(MINISHIFT_PATH, true);
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

	private void checkEditorStateAfterSave(String location, boolean canSave) {
		LabeledText label = ((CDK3ServerEditor) editor).getMinishiftBinaryLabel();
		label.setText(location);
		new WaitUntil(new SystemJobIsRunning(jobMatcher), TimePeriod.SHORT, false);
		new WaitWhile(new SystemJobIsRunning(jobMatcher), TimePeriod.SHORT, false);
		if (canSave) {
			verifyEditorCanSave();
		} else {
			verifyEditorCannotSave();
		}
	}

	/**
	 * We need to override save method from EditorHandler to be executed in async
	 * thread in order to be able to work with message dialog from invalid server
	 * editor location
	 * 
	 * @param editor
	 *            IEditorPart to work with during saving
	 */
	private void performSave(final IEditorPart editor) {
		EditorHandler.getInstance().activate(editor);
		Display.asyncExec(new Runnable() {

			@Override
			public void run() {
				editor.doSave(new NullProgressMonitor());

			}
		});
		new WaitUntil(new WaitCondition() {

			@Override
			public boolean test() {
				return !editor.isDirty();
			}

			@Override
			public String errorMessage() {
				return "Could not save the editor";
			}

			@Override
			public String description() {
				return " editor is not dirty...";
			}
		}, TimePeriod.SHORT);
	}

	private void verifyEditorCannotSave() {
		assertTrue(editor.isDirty());
		try {
			performSave(editor.getEditorPart());
			new WaitWhile(new JobIsRunning());
			fail("Editor was saved successfully but exception was expected");
		} catch (WaitTimeoutExpiredException exc) {
			log.info("WaitTimeoutExpiredException occured, editor was not saved as expected");
		}
		errorDialogAppeared();
		assertTrue(editor.isDirty());
	}

	private void verifyEditorCanSave() {
		assertTrue(editor.isDirty());
		try {
			performSave(editor.getEditorPart());
			log.info("Editor was saved as expected");
		} catch (WaitTimeoutExpiredException exc) {
			fail("Editor was not saved successfully but exception was thrown");
		}
		assertFalse(editor.isDirty());
	}

	private void errorDialogAppeared() {
		try {
			new WaitUntil(new ShellIsAvailable(new DefaultShell(SERVER_ADAPTER)), TimePeriod.SHORT);
			log.info("Error Message Dialog appeared as expected");
		} catch (WaitTimeoutExpiredException exc) {
			log.error(exc.getMessage());
			fail("Error Message Dialog did not appear while trying to save editor");
		}
		new OkButton().click();
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
	
	private class JobMatcher extends BaseMatcher<Job> {

		private Job jobToMatch;
		
		public JobMatcher(Job job) {
			this.jobToMatch = job;
		}
		
		@Override
		public void describeTo(Description description) {
			description.appendText("Job with name ");
			description.appendValue(jobToMatch.getName());
			description.appendText(" matches running job");
		}

		@Override
		public boolean matches(Object item) {
			return jobToMatch.getName().equalsIgnoreCase(item.toString());
		}
	}
	
	private class SystemJobIsRunning extends JobIsRunning {
		
		@SuppressWarnings("rawtypes")
		private Matcher[] consideredJobs;
		
		public SystemJobIsRunning(Matcher<?> matcher) {
			this(new Matcher[] { matcher });
		}
		
		public SystemJobIsRunning(Matcher<?>[] consideredJobs) {
			this.consideredJobs = consideredJobs;
		}
		
		/* (non-Javadoc)
		 * @see org.jboss.reddeer.common.condition.WaitCondition#test()
		 */
		@SuppressWarnings("unchecked")
		@Override
		public boolean test() {
			Job[] currentJobs = Job.getJobManager().find(null);
			for (Job job: currentJobs) {
				
				if (CoreMatchers.anyOf(consideredJobs).matches(job.getName())) {
					log.debug("  job '%s' has no excuses, wait for it", job.getName());
					return true;
				}
			}
			return false;
		}
	}

}