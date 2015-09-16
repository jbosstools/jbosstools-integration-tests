package org.jboss.tools.dummy.ui.bot.test;


import static org.junit.Assert.*;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.jface.preference.PreferencePage;
import org.jboss.reddeer.junit.screenshot.CaptureScreenshotException;
import org.jboss.reddeer.junit.screenshot.ImageTool;
import org.jboss.reddeer.junit.screenshot.ScreenshotCapturer;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Dummy bot tests - designed to test Jenkins slaves 
 * @author jpeterka
 *
 */
public class DummyTest {
	
	private Logger log = Logger.getLogger(DummyTest.class);
	private boolean screenshotTaken = false;
	
	@BeforeClass
	public static void before() {		
	}

	@Test
	public void dummyTest() {
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		PreferencePage preferencePage = new PreferencePage("General") {	};	
		preferenceDialog.select(preferencePage);
		
		// Take a screenshot
		if (!screenshotTaken) {
			ScreenshotCapturer sc = ScreenshotCapturer.getInstance();
			try {
				screenshotTaken = true;
				String path = sc.captureScreenshot("dummyOK.png");
				ImageTool it = ImageTool.getInstance();
				boolean isScreenshotBlank = it.isImageBlank(path);
				if (isScreenshotBlank) log.error("Screenshot " + path + " is blank");
				else log.info("Screenshot " + path + " has visible content");
				assertTrue("Screenshot " + path + " should not be blank", !isScreenshotBlank );
			} catch (CaptureScreenshotException e) {
				log.error("Cannot capture screenshot:" + e.getMessage(), e);
			}
		}
		
		preferenceDialog.cancel();

		new WorkbenchShell();
	}
	
	@Test
	public void hundredTimesDummyTest() {
		final int cycles = 100;
		for (int i = 0 ; i < cycles ; i++)
		{		
			dummyTest();
			log.info(i+1 + "/" + cycles + " try passed");
		}
	}
	
	@AfterClass
	public static void after() {
	}
}
