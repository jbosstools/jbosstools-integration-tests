package org.jboss.tools.teiid.reddeer.editor;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;

public class VDBEditor extends SWTBotEditor {
	
	public VDBEditor(String name){
		super(new SWTWorkbenchBot().editorByTitle(name).getReference(), new SWTWorkbenchBot());
	}
	
	public static VDBEditor getInstance(String name){
		VDBEditor editor = new VDBEditor(name);
		editor.show();
		return editor;
	}
	
	public void addModel(String projectName, String model){
		new SWTWorkbenchBot().toolbarButtonWithTooltip("Add model").click();
		
		SWTBotShell shell = bot.shell("Add File(s) to VDB");
		shell.activate();
		shell.bot().tree(0).expandNode(projectName).select(model);

		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive(shell.getText()), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	/**
	 * 
	 * @param projectName
	 * @param model
	 * @param longerPath true if path to model contains folders
	 */
	public void addModel(boolean longerPath, String... pathToModel){
		new SWTWorkbenchBot().toolbarButtonWithTooltip("Add model").click();
		
		SWTBotShell shell = bot.shell("Add File(s) to VDB");
		shell.activate();
		shell.bot().tree(0).expandNode(pathToModel).select();

		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive(shell.getText()), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	public String getModel(int index){
		return new SWTWorkbenchBot().table(0).cell(index, 0);
	}

	public void synchronizeAll() {
		new PushButton("Synchronize All").click();
	}
}
