package org.jboss.tools.deltacloud.ui.bot.test.view;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.deltacloud.ui.bot.test.ConnectionWizardTest;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.types.IDELabel;

public class CloudConnection {
	
	Logger log = Logger.getLogger(CloudConnection.class);
	private static final String INSTANCES="Instances";
	private static final String IMAGES="Images";
	public enum InstanceAction {
		Start,
		Destroy,
		Reboot,
		Stop,
	}
	private final CloudViewer viewer;
	private final String name;
	private final SWTUtilExt util = SWTTestExt.util;
	public CloudConnection(CloudViewer viewer, String name) {
		this.viewer = viewer;
		this.name = name;
		// force client to load instances and images
		viewer.bot().tree().expandNode(name).expandNode(INSTANCES);
		viewer.bot().tree().expandNode(name).expandNode(IMAGES);
		util.waitForNonIgnoredJobs();
	}
	/**
	 * gets list of instances in cloud
	 * @return
	 */
	public List<String> getInstances() {
		List<String> instances = new ArrayList<String>();
		for (SWTBotTreeItem instance : getItems(INSTANCES)) {
			instances.add(instance.getText());
		}
		return instances;
	}
	private SWTBotTreeItem[] getItems(String node) {
		return viewer.bot().tree().expandNode(name).expandNode(node).getItems();
	}
	/**
	 * gets list of images in cloud
	 * @return
	 */
	public List<String> getImages() {
		List<String> instances = new ArrayList<String>();
		for (SWTBotTreeItem instance : getItems(IMAGES)) {
			instances.add(instance.getText());
		}
		return instances;
	}
	/**
	 * performs an action on an instance
	 * @param action
	 * @param instance
	 */
	public void action(InstanceAction action, String instance) {
		selectInstance(instance);
		viewer.bot().tree().contextMenu(action.toString()).click();
		util.waitForNonIgnoredJobs();
	}
	/**
	 * performs click on item in context menu 
	 * @param menu
	 * @return
	 */
	public SWTBotMenu contextMenuClick(String menu) {
		return viewer.show().bot().tree().contextMenu(menu).click();
	}
	/**
	 * performs given action on all instances, if action cannot be performed it is skipped
	 * @param action
	 */
	public void actionAll(InstanceAction action) {
		for (String instance : getInstances()) {
			if (canDo(action, instance)) {
				action(action, instance);
			}
		}
	}
	/**
	 * performs action on multiple instances (instances gets selected and than one action is performed)
	 * @param action
	 * @param instances
	 * @return count of instances that have been affected by action (action was performed on them)
	 */
	public int multiAction(InstanceAction action, List<String> instances) {
		int instances_before = instances.size();
		viewer.bot().tree().expandNode(name).expandNode(INSTANCES).select(instances.toArray(new String[0]));
		if (!viewer.bot().tree().contextMenu(action.toString()).isEnabled()) {			
			return 0;
		}
		viewer.bot().tree().contextMenu(action.toString()).click();
		SWTBotShell shell = new SWTBotExt().activeShell();
		int count = shell.bot().table().rowCount();
		shell.bot().button(IDELabel.Button.OK).click();
		util.waitForNonIgnoredJobs();
		int instances_after = getInstances().size();
		assertEquals(String.format("Count of instances before multi-action '%s' was performed is different, ",action.toString()),instances_before, instances_after);
		return count;
	}
	
	public void destroyAllInstances() {
		for (String instance : getInstances()) {
			if (canDo(InstanceAction.Destroy, instance)) {
				action(InstanceAction.Destroy, instance);
			}
			else {
				action(InstanceAction.Stop, instance);
				action(InstanceAction.Destroy, instance);
			}
		}
	}
	public boolean canDo(InstanceAction action, String instance) {
		selectInstance(instance);
		return viewer.bot().tree().contextMenu(action.toString()).isEnabled();		
	}
	public SWTBotTreeItem selectInstance(String instance) {
		return viewer.bot().tree().expandNode(name).expandNode(INSTANCES).select(instance);
	}
	public SWTBotTreeItem selectImage(String image) {
		return viewer.bot().tree().expandNode(name).expandNode(IMAGES).select(image);
	}
	public SWTBotTreeItem selectImages() {
		return viewer.bot().tree().expandNode(name).expandNode(IMAGES).select();
	}
	public SWTBotTreeItem selectInstances() {
		return viewer.bot().tree().expandNode(name).expandNode(INSTANCES).select();
	}
	public LaunchInstance launch() {
		contextMenuClick("Launch Instance");
		return new LaunchInstance();
	}
	public LaunchInstance launch(String imageName) {
		selectImage(imageName);
		contextMenuClick("Launch Instance");
		return new LaunchInstance();
	}
	public ManageKeys manageKeys() {
		contextMenuClick("Manage Keys");
		return new ManageKeys(SWTTestExt.bot.activeShell().widget);
	}
	public ConnectionWizard edit() {
		viewer.bot().tree().expandNode(name).select();
		contextMenuClick("Edit Connection");
		return new ConnectionWizard();
	}
	/**
	 * launches default instance (without RSE or Server Adapter)
	 * @param name
	 * @param imageName
	 * @param key is created in management if does not exist
	 */
	public void launchDefault(String name, String imageName, String key) {
		
		LaunchInstance launch = launch(imageName);
		launch.setName(name);
		launch.setImage(imageName);
		launch.setKey(key);
		launch.bot().sleep(Timing.time1S());
		ManageKeys keys = launch.manageKeys();
			if (keys.getKeys().contains(key)) {
				keys.clickCancel();
			}
			else {
				keys.newKey(key, true);
				keys.selectKey(key);
				keys.clickOK();
			}
		launch.next();
		launch.setCreateServer(false);
		launch.setCreateRSE(false);
		launch.finish();
	}
	
}
