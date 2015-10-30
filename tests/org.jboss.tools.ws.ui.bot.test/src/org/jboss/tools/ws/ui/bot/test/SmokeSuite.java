package org.jboss.tools.ws.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.ws.ui.bot.test.preferences.JBossWSPreferencesTest;
import org.jboss.tools.ws.ui.bot.test.rest.explorer.RESTfulSupportTest;
import org.jboss.tools.ws.ui.bot.test.sample.SampleSoapServicesTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	SampleSoapServicesTest.class,
	JBossWSPreferencesTest.class,
	RESTfulSupportTest.class,
})
@RunWith(RedDeerSuite.class)
public class SmokeSuite {

}
