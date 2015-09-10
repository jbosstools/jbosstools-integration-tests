package org.jboss.tools.cdi.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.cdi.bot.test.beans.bean.cdi10.AsYouTypeValidationTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.bean.cdi10.BeanValidationQuickFixTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.bean.cdi11.AsYouTypeValidationTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.bean.cdi11.BeanValidationQuickFixTestCDI11;
import org.jboss.tools.cdi.bot.test.beansxml.openon.cdi10.BeansXMLOpenOnTestCDI10;
import org.jboss.tools.cdi.bot.test.beansxml.openon.cdi11.BeansXMLOpenOnTestCDI11;
import org.jboss.tools.cdi.bot.test.wizard.cdi10.CDIWebProjectWizardTestCDI10;
import org.jboss.tools.cdi.bot.test.wizard.cdi11.CDIWebProjectWizardTestCDI11;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RedDeerSuite.class)
@SuiteClasses({	
	CDIWebProjectWizardTestCDI10.class,
	CDIWebProjectWizardTestCDI11.class,
	AsYouTypeValidationTestCDI10.class,
	AsYouTypeValidationTestCDI11.class,
	BeanValidationQuickFixTestCDI10.class,
	BeanValidationQuickFixTestCDI11.class,
	BeansXMLOpenOnTestCDI10.class,
	BeansXMLOpenOnTestCDI11.class,
})
public class SmokeSuite {

}
