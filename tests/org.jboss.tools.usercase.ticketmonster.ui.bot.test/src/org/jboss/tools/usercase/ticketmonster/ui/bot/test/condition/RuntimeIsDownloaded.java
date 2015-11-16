package org.jboss.tools.usercase.ticketmonster.ui.bot.test.condition;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;

public class RuntimeIsDownloaded extends AbstractWaitCondition{

	@Override
	public boolean test() {
		if(new ShellWithTextIsAvailable("Question").test()){
			new PushButton("Yes To All").click();
		}
		return !new JobIsRunning().test();
	}

	@Override
	public String description() {
		return "Runtime is downloaded";
	}

}
