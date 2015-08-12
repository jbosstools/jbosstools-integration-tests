package org.jboss.tools.dummy.ui.bot.test;


import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.reddeer.jface.preference.PreferencePage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Dummy bot tests - designed to test Jenkins slaves 
 * @author jpeterka
 *
 */
public class DummyTest {
	
	Logger log = Logger.getLogger(DummyTest.class);
	
	@BeforeClass
	public static void before() {		
	}

	@Test
	public void dummyTest() {

		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		PreferencePage preferencePage = new PreferencePage("General") {	};	
		preferenceDialog.select(preferencePage);
		preferenceDialog.cancel();

		new WorkbenchShell();
	}
	
	@Test
	public void hundredTimes() {
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
