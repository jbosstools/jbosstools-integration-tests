package org.jboss.tools.runtime.as.ui.bot.test.download;

import org.junit.Test;

/**
 * Downloads several project runtimes and checks if they were successfully downloaded and added
 * 
 * @author Petr Suchy
 * @author Radoslav Rabara
 */
public class ProjectRuntimeDownload extends ProjectRuntimeDownloadTestBase {
	
	@Test
	public void downloadWildfly90Final() {
		downloadAndCheckServer("WildFly 9.0.1 Final", 1);
	}
	
	@Test
	public void downloadAS711() {
		downloadAndCheckServer("JBoss AS 7.1.1 (Brontes)", 1);
	}
}
