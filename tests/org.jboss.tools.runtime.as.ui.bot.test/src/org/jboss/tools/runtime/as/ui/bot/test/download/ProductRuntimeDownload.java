package org.jboss.tools.runtime.as.ui.bot.test.download;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.hamcrest.core.StringContains;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.junit.Test;

/**
 * Download of runtime via $0 subscription download
 * 
 * Downloads several product runtimes and checks if they were successfully downloaded and added
 * 
 * @author Radoslav Rabara
 *
 */
public class ProductRuntimeDownload extends ProductRuntimeDownloadTestBase {

	/**
	 * Test downloading of runtime via $0 subscription download with invalid
	 * credentials
	 */
	@Test
	public void useInvalidCredentials() { 
		invokeDownloadRuntimesWizard();
		
		processSelectingRuntime("JBoss EAP 6.2.0");
		processInsertingCredentials("Invalid username", "Invalid password");
		
		try {
			Text errorText = new org.jboss.reddeer.swt.impl.text.DefaultText(2);
			assertThat(errorText.getText(), StringContains.containsString("Your credentials are incorrect"));
		} catch(SWTLayerException e) {
			fail("Error text not found\n" + e.getMessage());
		}
	}
	
	/**
	 * Download of EAP 6 via $0 subscription download
	 */
	@Test
	public void downloadEAP61() {
		downloadRuntime("JBoss EAP 6.1.0");
	}
	
	@Test
	public void downloadEAP62() {
		downloadRuntime("JBoss EAP 6.2.0");
	}
	
	@Test
	public void downloadJPP610() {
		downloadRuntime("JBoss Portal Platform 6.1.0");
	}
}
