package org.jboss.tools.runtime.as.ui.bot.test.download;

import static org.junit.Assert.fail;

import org.junit.BeforeClass;

/**
 * ProductRuntimeDownloadTestBase is base class for testing download of product runtime
 * 
 * It's necessary to set username and password properties.
 * 
 * @author Radoslav Rabara
 *
 */
public class ProductRuntimeDownloadTestBase extends RuntimeDownloadTestBase {
	
	private static String username;
	private static String password;
	
	private static final String JBOSS_ORG_USERNAME_PROPERTY_KEY = "jboss.org.username";
	private static final String JBOSS_ORG_PASSWORD_PROPERTY_KEY = "jboss.org.password";
	
	@BeforeClass
	public static void readCredentials() {
		username = System.getProperty(JBOSS_ORG_USERNAME_PROPERTY_KEY);
		password = System.getProperty(JBOSS_ORG_PASSWORD_PROPERTY_KEY);
	}
	
	protected void downloadRuntime(String runtime) {
		if(username == null || username.length() == 0) {
			fail("To download product runtime you have to set property \""+JBOSS_ORG_USERNAME_PROPERTY_KEY+"\"");
		}
		if(password == null || password.length() == 0) {
			fail("To download product runtime you have to set property \""+JBOSS_ORG_PASSWORD_PROPERTY_KEY+"\"");
		}
		final int SEVERS_COUNT = 1;
		final int SEAMS_COUNT = 0;
		downloadRuntime(username, password, runtime, SEVERS_COUNT, SEAMS_COUNT);
	}
	
	protected void downloadRuntime(String username, String password, String runtime, int serversCount, int seamsCount) {
		downloadRuntime(runtime, username, password);
		assertServerRuntimesNumber(serversCount);
		assertSeamRuntimesNumber(seamsCount);
	}
	
	protected void downloadRuntime(String runtime, String username, String password){
		invokeDownloadRuntimesWizard();
		
		processSelectingRuntime(runtime);
		processInsertingCredentials(username, password);
		processLicenceAgreement();
		processRuntimeDownload();
	}
}
