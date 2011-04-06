package org.jboss.tools.deltacloud.ui.bot.test;

import java.util.Arrays;

import org.jboss.tools.deltacloud.ui.bot.test.view.CloudConnection;
import org.jboss.tools.deltacloud.ui.bot.test.view.CloudConnection.InstanceAction;
import org.jboss.tools.deltacloud.ui.bot.test.view.CloudViewer;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.SWTBotTestRequires;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

@SWTBotTestRequires(runOnce=true,perspective="Deltacloud",secureStorage=true)
public class CloudViewerTest extends SWTTestExt {

	private CloudViewer cloudViewer = new CloudViewer();

	@AfterClass
	public static void waitAMinute() {
		//bot.sleep(Long.MAX_VALUE);
	}
	@BeforeClass
	public static void setUpInstances() throws Exception {
		Util.setupPreferences(false, false);
		Util.setupDefaultConnection(new CloudViewer());
		Util.setupInstances(new CloudViewer().getConnection(Util.CONNECTION_DEFAULT_NAME), 5);
	}
	
	@Test
	public void instanceActions() {
		CloudConnection con = cloudViewer.getConnection(Util.CONNECTION_DEFAULT_NAME);
		log.info(Arrays.toString(con.getInstances().toArray())); 
		log.info(Arrays.toString(con.getImages().toArray()));
		for (String instance :con.getInstances()) {
			log.info("Instance "+ instance + " "+con.canDo(InstanceAction.Reboot, instance));
			log.info("Instance "+ instance + " "+con.canDo(InstanceAction.Start, instance));
			log.info("Instance "+ instance + " "+con.canDo(InstanceAction.Stop, instance));
			log.info("Instance "+ instance + " "+con.canDo(InstanceAction.Destroy, instance));
			con.selectInstance(instance);
			properties.getPropertyValue("alias");
		}
		log.info(con.multiAction(InstanceAction.Stop, con.getInstances()));
		log.info(con.multiAction(InstanceAction.Start, con.getInstances()));
		log.info(con.multiAction(InstanceAction.Reboot, con.getInstances()));
	}
	@Test
	public void instanceMultiActions() {
		
	}

}
