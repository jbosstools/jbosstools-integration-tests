package org.jboss.tools.runtime.as.ui.bot.test.download;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Downloads all runtimes and checks if they were succesfully downloaded and added.
 * 
 * @author Radoslav Rabara
 * @version JBDS 7.0.1
 */
public class AllRuntimeDownload extends ProjectRuntimeDownloadTestBase {
	
	@Test
	public void gateInPortal360(){
		downloadAndCheckServer("GateIn Portal 3.6.0", 1);
	}
	
	@Test
	public void jboss328SP1(){
		downloadAndCheckServer("JBoss 3.2.8 SP 1", 1);
	}
	
	@Test
	public void jboss405(){
		downloadAndCheckServer("JBoss 4.0.5", 1);
	}
	
	@Test
	public void jboss423(){
		downloadAndCheckServer("JBoss 4.2.3", 1);
	}
	
	@Test
	public void jboss501(){
		downloadAndCheckServer("JBoss 5.0.1", 1);
	}
	
	@Test
	public void jboss510(){
		downloadAndCheckServer("JBoss 5.1.0", 1);
	}
	
	@Test
	public void jboss600(){
		downloadAndCheckServer("JBoss 6.0.0", 1);
	}
	
	@Test
	public void jboss701(){
		downloadAndCheckServer("JBoss 7.0.1", 1);
	}
	
	@Test
	public void jboss702(){
		downloadAndCheckServer("JBoss 7.0.2", 1);
	}
	
	@Test
	public void jboss710(){
		downloadAndCheckServer("JBoss 7.1.0", 1);
	}
	
	@Test
	public void jbossAS711(){
		downloadAndCheckServer("JBoss AS 7.1.1 (Brontes)", 1);
	}
	
	/**
	 * Download of EAP 6.0.0 is available as manual download
	 */
	@Test
	@Ignore
	public void jbossEAP600(){
		downloadAndCheckServer("JBoss EAP 6.0.0", 1);
	}
	
	/**
	 * Download of EAP 6.0.1 is available as manual download
	 */
	@Test
	@Ignore
	public void jbossEAP601(){
		downloadAndCheckServer("JBoss EAP 6.0.1", 1);
	}
	
	/**
	 * Download of EAP 6.1.0 is available as manual download
	 */
	@Test
	@Ignore
	public void jbossEAP610(){
		downloadAndCheckServer("JBoss EAP 6.1.0", 1);
	}
	
	/**
	 * Download of JPP 6.0.0 is available as manual download
	 */
	@Test
	@Ignore
	public void jbossPortalPlatform600(){
		downloadAndCheckServer("JBoss Portal Platform 6.0.0", 1);
	}
	
	/**
	 * Download of JPP 6.0.1 is available as manual download
	 */
	@Test
	@Ignore
	public void jbossPortalPlatform601(){
		downloadAndCheckServer("JBoss Portal Platform 6.0.1", 1);
	}
	
	/**
	 * Download of EAP 6.1.0 is available as manual download
	 */
	@Test
	@Ignore
	public void jbossPortalPlatform610(){
		downloadAndCheckServer("JBoss Portal Platform 6.1.0 Beta", 1);
	}
	
	@Test
	public void downloadSeam202(){
		downloadAndCheckSeam("JBoss Seam 2.0.2.SP1", 1);
	}
	
	@Test
	public void downloadSeam212(){
		downloadAndCheckSeam("JBoss Seam 2.1.2.SP1", 1);
	}
	
	@Test
	public void downloadSeam222(){
		downloadAndCheckSeam("JBoss Seam 2.2.2", 1);
	}
	
	@Test
	public void downloadSeam230(){
		downloadAndCheckSeam("JBoss Seam 2.3.0", 1);
	}
}
