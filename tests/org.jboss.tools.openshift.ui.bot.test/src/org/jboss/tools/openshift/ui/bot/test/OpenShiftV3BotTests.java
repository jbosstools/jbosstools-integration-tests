package org.jboss.tools.openshift.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.openshift.ui.bot.test.connection.v3.CreateNewOpenShiftv3ConnectionTest;
import org.jboss.tools.openshift.ui.bot.test.connection.v3.OpenShiftV3ConnectionDialogHandlingTest;
import org.jboss.tools.openshift.ui.bot.test.connection.v3.StoreOpenShiftV3ConnectionTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RedDeerSuite.class)
@SuiteClasses({
	CreateNewOpenShiftv3ConnectionTest.class,
	OpenShiftV3ConnectionDialogHandlingTest.class,
	StoreOpenShiftV3ConnectionTest.class
})
public class OpenShiftV3BotTests {
	
}
