package org.jboss.tools.cdi.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.cdi.bot.test.wizard.cdi11.AllWizardsTestCDI11;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RedDeerSuite.class)
@SuiteClasses({	
	AllWizardsTestCDI11.class
})
public class SmokeSuite {

}
