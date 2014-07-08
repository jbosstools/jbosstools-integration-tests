package org.jboss.tools.maven.ui.bot.test.utils;

import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.swt.condition.WaitCondition;

public class ProjectIsBuilt implements WaitCondition {
	
	@Override
	public boolean test() {
		ConsoleView cview = new ConsoleView();
		cview.open();
		return cview.getConsoleText().contains("BUILD SUCCESS") || cview.getConsoleText().contains("BUILD FAILURE");
	}
	@Override
	public String description() {
		return "Project build is complete";
	}
	
	public boolean isBuildSuccesfull(){
	    ConsoleView cview = new ConsoleView();
        cview.open();
	    return cview.getConsoleText().contains("BUILD SUCCESS");
	}
}
