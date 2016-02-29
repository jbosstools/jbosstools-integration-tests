package org.jboss.tools.runtime.as.ui.bot.test.download;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.common.reddeer.utils.FileUtils;
import org.jboss.tools.runtime.as.ui.bot.test.template.RuntimeDetectionTestCase;
import org.jboss.tools.runtime.reddeer.wizard.TaskWizardFirstPage;
import org.jboss.tools.runtime.reddeer.wizard.TaskWizardLoginPage;
import org.jboss.tools.runtime.reddeer.wizard.TaskWizardSecondPage;
import org.jboss.tools.runtime.reddeer.wizard.TaskWizardThirdPage;
import org.junit.After;
import org.junit.Before;
/**
 * RuntimeDownloadTestBase is base class for testing download of runtimes.
 * 
 * It provides methods to process runtime downloading.
 * 
 * @author Radoslav Rabara
 * @author Petr Suchy
 *
 */
public class RuntimeDownloadTestBase extends RuntimeDetectionTestCase {
	
	private File tmpPath;
	protected WizardDialog runtimeDownloadWizard;
	
	@Before
	public void setUp(){
		File tmpDir = new File(System.getProperty("java.io.tmpdir"));
		tmpPath = new File(tmpDir, "tmpServer_" + System.currentTimeMillis());
		tmpPath.mkdir();
	}
	
	@After
	public void clean(){
		try {
			FileUtils.deleteDirectory(tmpPath);
		} catch (IOException e) {
			fail("Unable to delete temporary directory.");
		}
		removeAllPaths();
		removeAllServerRuntimes();
		removeAllSeamRuntimes();
	}
	
	protected void invokeDownloadRuntimesWizard() {
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		preferenceDialog.select(runtimeDetectionPage);
		
		new PushButton("Download...").click();
		new WaitUntil(new ShellWithTextIsAvailable("Download Runtimes"), TimePeriod.VERY_LONG);
		runtimeDownloadWizard = new WizardDialog();
	}
	
	protected void processSelectingRuntime(String runtime) {
		TaskWizardFirstPage selectRuntimePage = new TaskWizardFirstPage();
		selectRuntimePage.selectRuntime(runtime);
		runtimeDownloadWizard.next();
	}
	
	protected void processInsertingCredentials(String username, String password) {
		TaskWizardLoginPage credentialsPage = new TaskWizardLoginPage();
		assertEquals("Domain is set to jboss.org", "jboss.org", credentialsPage.getDomain());
		
		//username is not enabled -> we have to add credential
		if (!credentialsPage.containsUsername(username)){
			credentialsPage.addCredentials(username, password);
		}
		new DefaultShell("Download Runtimes").setFocus();
		credentialsPage.setUsername(username);
		new WaitWhile(new JobIsRunning());
		runtimeDownloadWizard.next();
		new WaitWhile(new JobIsRunning());
	}
	
	protected void processRuntimeDownload() {
		TaskWizardThirdPage downloadRuntimePage = new TaskWizardThirdPage(); 
		downloadRuntimePage.setInstallFolder(tmpPath.getAbsolutePath());
		
		//wizard.finish();  -- does not work (Problem with slow downloading)
		new PushButton("Finish").click();
		runtimeDownloadWizard = null;
		
		new WaitUntil(new JobIsRunning(), TimePeriod.VERY_LONG, false);
		new WaitWhile(new JobIsRunning(), TimePeriod.getCustom(900));
		
		runtimeDetectionPage.ok();
	}
	
	protected void processLicenceAgreement() {
		TaskWizardSecondPage licenceAgreementPage = new TaskWizardSecondPage();
		licenceAgreementPage.acceptLicense(true);
		runtimeDownloadWizard.next();
	}
}
