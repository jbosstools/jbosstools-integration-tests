package org.jboss.tools.openshift.ui.bot.test.application.create;

import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteUtils;
import org.jboss.tools.openshift.reddeer.wizard.v2.Templates;
import org.jboss.tools.openshift.ui.bot.test.util.Datastore;
import org.junit.After;
import org.junit.Before;

public class IDXXXCreateTestingApplication {

	protected String applicationName = "diy" + System.currentTimeMillis();
	
	@Before
	public void createApplication() {
		new Templates(Datastore.USERNAME, Datastore.SERVER, Datastore.DOMAIN, false).
			createSimpleApplicationOnBasicCartridges(
				OpenShiftLabel.Cartridge.DIY, applicationName, false, true, true);
	}
	
	@After
	public void deleteApplication() {
		new DeleteUtils(Datastore.USERNAME, Datastore.SERVER, Datastore.DOMAIN, 
				applicationName, applicationName).perform();
	}
	
}
