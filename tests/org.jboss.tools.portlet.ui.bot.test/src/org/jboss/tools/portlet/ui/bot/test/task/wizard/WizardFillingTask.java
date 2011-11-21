package org.jboss.tools.portlet.ui.bot.test.task.wizard;

import org.jboss.tools.portlet.ui.bot.test.task.CompositeSWTTask;

/**
 * 
 * Provides the functionality of navigating between wizard pages. The data
 * should fill every wizard page itself.  
 * 
 * @author ljelinko
 *
 */
public class WizardFillingTask extends CompositeSWTTask<WizardPageFillingTask>{

	@Override
	public void perform() {
		super.perform();
		getBot().button("Finish").click();
	}
	
	@Override
	protected void beforeTask(WizardPageFillingTask task) {
		if (!isFirst(task)){
			getBot().button("Next >").click();
		}
	}
	
	private boolean isFirst(WizardPageFillingTask task){
		return getTasks().indexOf(task) == 0;
	}
}
