package org.jboss.tools.runtime.as.ui.bot.test.download;

import org.junit.Test;

public class SmokeRuntimeDownload extends ProjectRuntimeDownloadTestBase {

	@Test
	public void downloadWildfly90Final() {
		downloadAndCheckServer("WildFly 9.0.1 Final", 1);
	}
	
}
