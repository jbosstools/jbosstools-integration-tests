package org.jboss.tools.usercase.ticketmonster.ui.bot.test.wizard;

import java.util.List;

import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.WaitWhile;

public class GenerateHashCodeAndEqualsDialog{
	
	private Shell shell;
	
	public void open(boolean viaShellMenu){
		if(viaShellMenu){
			new ShellMenu("Source","Generate hashCode() and equals()...").select();;
		} else {
			new ContextMenu("Source","Generate hashCode() and equals()...").select();
		}
		shell = new DefaultShell("Generate hashCode() and equals()");
	}
	
	
	public List<TreeItem> getFields(){
		return new DefaultTree().getAllItems();
	}
	
	public void selectAll(){
		new PushButton("Select All").click();
	}
	
	public void deselectAll(){
		new PushButton("Deselect All").click();
	}
	
	public void ok(){
		String shellText = shell.getText();
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsAvailable(shellText));
	}
	
	public void cancel(){
		String shellText = shell.getText();
		new PushButton("Cancel").click();
		new WaitWhile(new ShellWithTextIsAvailable(shellText));
	}
	
	
}
