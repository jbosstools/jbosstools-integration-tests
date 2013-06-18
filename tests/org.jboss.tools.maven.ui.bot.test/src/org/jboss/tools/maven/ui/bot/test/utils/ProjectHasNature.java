package org.jboss.tools.maven.ui.bot.test.utils;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;

public class ProjectHasNature implements WaitCondition{
	
	private String projectName;
	private String natureID; 
	private String version;
	
	public ProjectHasNature(String projectName, String natureID, String version){
		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.open();
		pexplorer.getProject(projectName).select();
		this.projectName=projectName;
		this.natureID=natureID;
		this.version=version;
		
	}
	
	public boolean test() {
		new ContextMenu("Properties").select();
		new WaitUntil(new ShellWithTextIsActive("Properties for "+projectName), TimePeriod.NORMAL);
		new DefaultTreeItem("Project Facets").select();
		boolean result = new DefaultTreeItem(1,natureID).isChecked();
		if(version!=null){
			result = result && new DefaultTreeItem(1,natureID).getCell(1).equals(version);
		}
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive("Properties for "+projectName), TimePeriod.NORMAL);
		return result;
	}
	
	@Override
	public String description() {
		return "Project "+projectName+" doesn't not have nature "+natureID;
	}

}
