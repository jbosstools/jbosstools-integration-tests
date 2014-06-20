package org.jboss.tools.maven.ui.bot.test.utils;

import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;

public class RepositoryExists{
	
	private String repoID;
	
	public RepositoryExists(String repoID){
		this.repoID=repoID;
	}

	public boolean test() {
		new WaitUntil(new JobIsRunning(), TimePeriod.NORMAL,false);
		new WaitWhile(new JobIsRunning(),TimePeriod.NORMAL);
		new WorkbenchView("Maven Repositories").open();
		new WaitUntil(new TreeCanBeExpanded(new DefaultTreeItem("Global Repositories")),TimePeriod.NORMAL);
		for(TreeItem item: new DefaultTreeItem("Global Repositories").getItems()){
			if(item.getText().contains(repoID)){
				return true;
			}
		}
		return false;
	}
}
