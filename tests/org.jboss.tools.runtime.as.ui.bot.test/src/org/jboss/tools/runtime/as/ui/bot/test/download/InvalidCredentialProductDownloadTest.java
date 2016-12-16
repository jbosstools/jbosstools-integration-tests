package org.jboss.tools.runtime.as.ui.bot.test.download;

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.condition.WaitCondition;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.progressbar.DefaultProgressBar;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.tools.cdk.reddeer.requirements.DisableSecureStorageRequirement.DisableSecureStorage;
import org.junit.Test;

/**
 * Download of runtime via $0 subscription download
 * 
 * Downloads several product runtimes and checks if they were successfully downloaded and added
 * 
 * @author Radoslav Rabara
 *
 */

@DisableSecureStorage
public class InvalidCredentialProductDownloadTest {

	/**
	 * Test downloading of runtime via $0 subscription download with invalid
	 * credentials
	 */
	@Test
	public void useInvalidCredentials() {
		RuntimeDownloadTestUtility util = new RuntimeDownloadTestUtility();
		util.invokeDownloadRuntimesWizard();

		util.processSelectingRuntime("JBoss EAP 6.2.0");
		util.processInsertingCredentials("Invalid username", "Invalid password");
		new WaitWhile(new ValidatingCredentialsProgressBarIsRunning());
		
		assertErrorMessageIsShown();
		
		new DefaultShell("Download Runtimes");
		new CancelButton().click();
		new CancelButton().click();
	}
	

	private void assertErrorMessageIsShown() {
		try{
			new WaitUntil(new ErrorMessageIsShown());
		}catch (WaitTimeoutExpiredException e){
			e.printStackTrace();
			fail("Error message was not shown. "+e.getMessage());
		}
	}

	private class ErrorMessageIsShown implements WaitCondition{
		
		@Override
		public boolean test() {
			try {
				new DefaultText(" Your credentials are incorrect. Please review the values and try again.");
				return true;
			} catch (CoreLayerException e) {
				return false;
			}
		}
		
		@Override
		public String errorMessage() {
			return "error message was not shown.";
		}
		
		@Override
		public String description() {
			return "error message is shown.";
		}
	}
	
	private class ValidatingCredentialsProgressBarIsRunning implements WaitCondition{

		@Override
		public boolean test() {
			try{
				new DefaultProgressBar("Validating Credentials");
				return true;
			}catch(CoreLayerException e){
				return false;
			}
		}

		@Override
		public String description() {
			return "Validating Credentials progress bar is running";
		}

		@Override
		public String errorMessage() {
			return "Validating Credentials progress bar is not running";
		}
		
	}
}
