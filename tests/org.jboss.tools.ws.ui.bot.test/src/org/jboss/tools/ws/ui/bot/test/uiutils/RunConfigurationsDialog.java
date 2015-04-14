package org.jboss.tools.ws.ui.bot.test.uiutils;

import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.common.reddeer.label.IDELabel;

public class RunConfigurationsDialog {

	private Shell shell;

	public void open() {
		new ShellMenu(IDELabel.Menu.RUN, "Run Configurations...").select();
		shell = new DefaultShell("Run Configurations");
	}

	public void run() {
		new PushButton(IDELabel.Button.RUN).click();
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL);
	}

	public void createNewConfiguration(String typeText) {
		for(TreeItem ti : new DefaultTree().getItems()) {
			if(ti.getText().equals(typeText)) {
				ti.doubleClick();
				return;
			}
		}
		throw new IllegalArgumentException("Specified type text '" + typeText
				+ "' was not found");
	}

	public void setProjectName(String projectName) {
		selectMainTab();
		new DefaultText(new DefaultGroup("Project:")).setText(projectName);
	}

	public void setClassName(String className) {
		selectMainTab();
		new DefaultText(new DefaultGroup("Main class:")).setText(className);
	}

	private void selectMainTab() {
		shell.setFocus();
		new DefaultCTabItem("Main").activate();
	}
}
