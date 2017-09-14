package org.jboss.tools.maven.ui.bot.test.utils;

import org.eclipse.reddeer.eclipse.ui.console.ConsoleView;
import org.eclipse.reddeer.common.condition.AbstractWaitCondition;

public class ProjectIsBuilt extends AbstractWaitCondition {
	
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
