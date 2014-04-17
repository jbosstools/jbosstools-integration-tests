package org.jboss.tools.openshift.ui.bot.test.cartridge;

import java.util.Date;

import org.jboss.tools.openshift.ui.bot.test.application.wizard.DeleteApplication;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.NewApplicationTemplates;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EmbedCartridges {

	private final String DIY_APP = "dapp" + new Date().getTime();

	@Before
	public void createDYIApp() {
		new NewApplicationTemplates(false).createSimpleApplicationWithoutCartridges(
				OpenShiftLabel.AppType.DIY, DIY_APP, false, true, true);
	}

	@Test
	public void canEmbedCartridges() {
		EmbedCartridge.embedCartrige(OpenShiftLabel.Cartridge.CRON);
		
		EmbedCartridge.embedCartrige(OpenShiftLabel.Cartridge.MYSQL);
		
		EmbedCartridge.embedCartrige(OpenShiftLabel.Cartridge.POSTGRESQL);
		
		EmbedCartridge.embedCartrige(OpenShiftLabel.Cartridge.MONGODB);
	}

	@After
	public void deleteDIYApp() {
		new DeleteApplication(DIY_APP, OpenShiftLabel.AppType.DIY_TREE).perform();
	}

}
