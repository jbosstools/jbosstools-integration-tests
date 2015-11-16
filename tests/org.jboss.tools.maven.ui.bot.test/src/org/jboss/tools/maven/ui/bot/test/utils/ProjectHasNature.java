package org.jboss.tools.maven.ui.bot.test.utils;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;

public class ProjectHasNature extends AbstractWaitCondition{
	
	private String projectName;
	private String natureID; 
	private String version;
	private String natureParent;
	
	public ProjectHasNature(String projectName, String natureID, String version){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(projectName).select();
		this.projectName=projectName;
		this.natureID=natureID;
		this.version=version;
	}
	
	public ProjectHasNature(String projectName, String natureParent, String natureID, String version){
		ProjectExplorer pe = new ProjectExplorer();
        pe.open();
        pe.getProject(projectName).select();
        this.projectName=projectName;
        this.natureID=natureID;
        this.version=version;
        this.natureParent = natureParent;
    }
	
	public boolean test() {
		new ContextMenu("Properties").select();
		new WaitUntil(new ShellWithTextIsActive("Properties for "+projectName), TimePeriod.NORMAL);
		new DefaultTreeItem("Project Facets").select();
		boolean result;
		if(natureParent != null){
		    result = new DefaultTreeItem(new DefaultTree(1),natureParent, natureID).isChecked();
		    if(version!=null){
	            result = result && new DefaultTreeItem(new DefaultTree(1),natureParent, natureID).getCell(1).equals(version);
	        }
		} else {
		    result = new DefaultTreeItem(new DefaultTree(1),natureID).isChecked();
		    if(version!=null){
                result = result && new DefaultTreeItem(new DefaultTree(1),natureID).getCell(1).equals(version);
            }
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
