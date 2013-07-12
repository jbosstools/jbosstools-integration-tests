package org.jboss.tools.switchyard.reddeer.server;

import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
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
	public static final String FULL_PUBLISH = "Full Publish";

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
				checkDeployment();
				return;
			}
		}
		throw new RuntimeException("Cannot find server '" + server + "'");
	}

	public void fullPublish(String project) {
		ServersView serversView = new ServersView();
		serversView.open();
		Tree tree = new DefaultTree();
		for (TreeItem item : tree.getItems()) {
			ServerLabel serverLabel = new ServerLabel(item);
			if (serverLabel.getName().equals(server)) {
				item.expand();
				for (TreeItem treeItem : item.getItems()) {
					if (treeItem.getText().startsWith(project)) {
						treeItem.select();
						clearConsole();
						new ContextMenu(FULL_PUBLISH).select();
						checkDeployment();
					}
				}

			}
		}
	}

	private void clearConsole() {
		ConsoleView consoleView = new ConsoleView();
		consoleView.open();
		consoleView.clearConsole();
	}

	private void checkDeployment() {
		Bot.get().sleep(60 * 1000);
		ConsoleView consoleView = new ConsoleView();
		consoleView.open();
		consoleView.clearConsole();
		String consoleText = consoleView.getConsoleText().toUpperCase();
		if (consoleText.contains("ERROR")) {
			throw new RuntimeException("An error occured during full publishing");
		}
	}
}
