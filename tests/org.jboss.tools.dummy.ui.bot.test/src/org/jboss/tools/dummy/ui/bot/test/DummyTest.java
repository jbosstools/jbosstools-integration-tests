package org.jboss.tools.dummy.ui.bot.test;


import org.jboss.reddeer.junit.logging.Logger;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.workbench.preference.WorkbenchPreferencePage;
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

		WorkbenchPreferencePage page;
		page = new WorkbenchPreferencePage("General");	
		page.open();
		page.cancel();

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
