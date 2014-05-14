package org.jboss.tools.runtime.as.ui.bot.test.download;


/**
 * ProjectRuntimeDownloadTestBase is base class for testing download of project runtime and seam
 * 
 * @author Radoslav Rabara
 * @author Petr Suchy
 *
 */
public class ProjectRuntimeDownloadTestBase extends RuntimeDownloadTestBase {
	
	protected void downloadRuntime(String runtime){
		invokeDownloadRuntimesWizard();
		
		processSelectingRuntime(runtime);
		processLicenceAgreement();
		processRuntimeDownload();
	}
	
	protected void downloadAndCheckRuntime(String runtime, int serversCount, int seamsCount) {
		downloadRuntime(runtime);
		assertServerRuntimesNumber(serversCount);
		assertSeamRuntimesNumber(seamsCount);
	}
	
	protected void downloadAndCheckSeam(String seam, int seamsCount) {
		downloadAndCheckRuntime(seam, 0, seamsCount);
	}
	
	protected void downloadAndCheckServer(String server, int serversCount) {
		downloadAndCheckRuntime(server, serversCount, 0);
	}
}
