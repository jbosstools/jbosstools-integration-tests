package org.jboss.tools.openshift.ui.bot.test.cartridge;

import java.util.Date;

import org.jboss.tools.openshift.ui.bot.test.OpenShiftBotTest;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EmbedCartridges extends OpenShiftBotTest {

	private final String DYI_APP = "dapp" + new Date().getTime();

	@Before
	public void createDYIApp() {
		createOpenShiftApplication(DYI_APP, OpenShiftLabel.AppType.DIY);
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
		deleteOpenShiftApplication(DYI_APP, OpenShiftLabel.AppType.DIY_TREE);
	}

}
