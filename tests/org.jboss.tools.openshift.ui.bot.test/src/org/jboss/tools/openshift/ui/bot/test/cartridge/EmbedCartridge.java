package org.jboss.tools.openshift.ui.bot.test.cartridge;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.bot.test.OpenShiftBotTest;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EmbedCartridge extends OpenShiftBotTest {

	private final String DYI_APP = "diyapp" + new Date().getTime();

	@Before
	public void createDYIApp() {
		createOpenShiftApplication(DYI_APP, OpenShiftLabel.AppType.DIY);
	}

	@Test
	public void canEmbedCartridge() {
		embedCartrige(OpenShiftLabel.Cartridge.CRON);
	}

	
	public static void embedCartrige(String cartridge) {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		TreeItem connection = new DefaultTree().getAllItems().get(0);
		connection.expand();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		assertTrue(connection.getItems().size() > 0);
		
		connection.getItems().get(0).doubleClick();;
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		connection.getItems().get(0).getItems().get(0).select();
		new ContextMenu(OpenShiftLabel.Labels.EDIT_CARTRIDGES).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.EDIT_CARTRIDGES));
		
		new DefaultShell(OpenShiftLabel.Shell.EDIT_CARTRIDGES).setFocus();
		new DefaultTable().getItem(cartridge).setChecked(true);
		new PushButton(OpenShiftLabel.Button.FINISH).click();
		
		new WaitUntil(new ShellWithTextIsAvailable("Embedded Cartridges"), TimePeriod.VERY_LONG);
		
		new DefaultShell("Embedded Cartridges").setFocus();
		new PushButton(OpenShiftLabel.Button.OK).click();
	
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	@After
	public void deleteDIYApp() {
		deleteOpenShiftApplication(DYI_APP, OpenShiftLabel.AppType.DIY_TREE);
	}

}
