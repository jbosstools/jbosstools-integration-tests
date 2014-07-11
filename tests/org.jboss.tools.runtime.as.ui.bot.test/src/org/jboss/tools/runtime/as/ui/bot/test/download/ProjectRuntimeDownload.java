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
	public void downloadWildfly81Final() {
		downloadAndCheckServer("WildFly 8.1.0 Final", 1);
	}
	
	@Test
	public void downloadAS711() {
		downloadAndCheckServer("JBoss AS 7.1.1 (Brontes)", 1);
	}
	
	@Test
	public void downloadSeam231() {
		downloadAndCheckSeam("JBoss Seam 2.3.1", 1);
	}
}
