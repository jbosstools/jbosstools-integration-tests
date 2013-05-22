package org.jboss.tools.switchyard.reddeer.server;

import org.jboss.reddeer.eclipse.wst.server.ui.view.ServerLabel;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.swt.api.Tree;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;

/**
 * 
 * @author apodhrad
 * 
 */
public class ServerDeployment {

	public static final String ADD_REMOVE_LABEL = "Add and Remove...";

	private String server;

	public ServerDeployment(String server) {
		this.server = server;
	}

	public void deployProject(String project) {
		ServersView serversView = new ServersView();
		serversView.open();
		Tree tree = new DefaultTree();
		for (TreeItem item : tree.getItems()) {
			ServerLabel serverLabel = new ServerLabel(item);
			if (serverLabel.getName().equals(server)) {
				item.select();
				new ContextMenu(ADD_REMOVE_LABEL).select();
				Bot.get().shell(ADD_REMOVE_LABEL).activate();
				new DefaultShell(ADD_REMOVE_LABEL);
				new DefaultTreeItem(project).select();
				new PushButton("Add >").click();
				new PushButton("Finish").click();
				new WaitWhile(new ShellWithTextIsActive(ADD_REMOVE_LABEL), TimePeriod.LONG);
				new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
				Bot.get().sleep(60 * 1000);
			}
		}
	}
}
