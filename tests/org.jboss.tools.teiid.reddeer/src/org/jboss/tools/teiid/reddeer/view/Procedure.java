package org.jboss.tools.teiid.reddeer.view;

import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;

public class Procedure {

	private String project;

	private String model;

	private String procedure;

	public Procedure(String project, String model, String procedure) {
		this.project = project;
		this.model = model;
		this.procedure = procedure;
	}

	public void addParameter(String name, String type) {
		addParameterName(name);
		addParameterType(name, type);
	}

	private void addParameterName(String parameter) {
		new DefaultTreeItem(project, model, procedure).select();
		new ContextMenu("New Child", "Procedure Parameter").select();
		new LabeledText("NewProcedureParameter").setText(parameter);

		// bot.waitUntil(new TreeContainsNode(bot.tree(), project, model,
		// procedure, parameter), TaskDuration.NORMAL.getTimeout());
	}

	private void addParameterType(String parameter, String type) {
		new DefaultTreeItem(project, model, procedure).select();
		new ContextMenu("Modeling", "Set Datatype").select();

		Shell shell = new DefaultShell("Select a Datatype");
		Bot.get().table().getTableItem(type).select();
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive(shell.getText()), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
}
