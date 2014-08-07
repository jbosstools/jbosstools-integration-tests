package org.jboss.tools.openshift.ui.bot.test.application.adapter;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.wst.server.ui.editor.ServerEditor;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.bot.test.application.create.IDXXXCreateTestingApplication;
import org.jboss.tools.openshift.ui.utils.Datastore;
import org.junit.Test;

/**
 * Test correctness of data in server details (ServerEditor).
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID802ServerAdapterOverviewTest extends IDXXXCreateTestingApplication {

	@Test
	public void testServerAdapterOverview() {
		TreeViewerHandler treeViewerHandler = TreeViewerHandler.getInstance();
		ServersView servers = new ServersView();
		servers.open();
		
		TreeItem serverAdapter = treeViewerHandler.getTreeItem(new DefaultTree(), 
				applicationName + " at OpenShift");
		serverAdapter.select();
		serverAdapter.doubleClick();
		
		new ServerEditor(applicationName + " at OpenShift").activate();
		
		assertTrue("Deployed project name is not same as in project explorer.",
				new LabeledCombo("Deploy Project: ").getSelection().equals(applicationName));
		
		assertTrue("Connection contains incorrect information. There should be user and server.",
				new LabeledText("Connection: ").getText().contains(Datastore.USERNAME) &&
				new LabeledText("Connection: ").getText().contains(Datastore.SERVER));
		
		assertTrue("Domain name is not shown properly. " + new LabeledText("Domain Name: ").getText() +
				" is shown instead of " + Datastore.DOMAIN,
				new LabeledText("Domain Name: ").getText().equals(Datastore.DOMAIN));
		
		assertTrue("Application name is incorrect. Was " + new LabeledText("Application Name: ").getText() +
				" but should be " + applicationName,
				new LabeledText("Application Name: ").getText().equals(applicationName));
		
		new CheckBox(0).click();
		
		assertTrue("Remote should be enabled after allowing overriding project settings.", 
				new LabeledText("Remote: ").isEnabled());
		
		new ServerEditor(applicationName + " at OpenShift").close();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
}
