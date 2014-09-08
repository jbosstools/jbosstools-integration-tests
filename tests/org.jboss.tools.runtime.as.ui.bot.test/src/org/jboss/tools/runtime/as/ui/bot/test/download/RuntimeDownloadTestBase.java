package org.jboss.tools.runtime.as.ui.bot.test.download;

import java.io.File;

import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
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
		tmpPath.delete();
		removeAllPaths();
		removeAllServerRuntimes();
		removeAllSeamRuntimes();
	}
	
	protected void invokeDownloadRuntimesWizard() {
		runtimeDetectionPage.open();
		
		new PushButton("Download...").click();
		new WaitUntil(new ShellWithTextIsActive("Download Runtimes"), TimePeriod.VERY_LONG);
		runtimeDownloadWizard = new WizardDialog();
	}
	
	protected void processSelectingRuntime(String runtime) {
		TaskWizardFirstPage selectRuntimePage = new TaskWizardFirstPage();
		selectRuntimePage.selectRuntime(runtime);
		runtimeDownloadWizard.next();
	}
	
	protected void processInsertingCredentials(String username, String password) {
		TaskWizardLoginPage credentialsPage = new TaskWizardLoginPage();
		credentialsPage.setUsername(username);
		credentialsPage.setPassword(password);
		runtimeDownloadWizard.next();
	}
	
	protected void processRuntimeDownload() {
		TaskWizardThirdPage downloadRuntimePage = new TaskWizardThirdPage(); 
		downloadRuntimePage.setInstallFolder(tmpPath.getAbsolutePath());
		
		//wizard.finish();  -- does not work (Problem with slow downloading)
		new PushButton("Finish").click();
		runtimeDownloadWizard = null;
		
		new WaitUntil(new JobIsRunning(), TimePeriod.NORMAL, false);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		
		runtimeDetectionPage.ok();
	}
	
	protected void processLicenceAgreement() {
		TaskWizardSecondPage licenceAgreementPage = new TaskWizardSecondPage();
		licenceAgreementPage.acceptLicense(true);
		runtimeDownloadWizard.next();
	}
}
