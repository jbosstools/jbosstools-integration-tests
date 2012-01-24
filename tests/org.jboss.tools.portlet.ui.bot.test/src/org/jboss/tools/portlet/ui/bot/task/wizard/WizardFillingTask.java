package org.jboss.tools.portlet.ui.bot.task.wizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.tools.portlet.ui.bot.task.CompositeSWTTask;
import org.jboss.tools.portlet.ui.bot.task.wait.TaskDuration;
import org.jboss.tools.portlet.ui.bot.task.wait.WaitWhileTask;
import org.jboss.tools.portlet.ui.bot.task.wait.condition.NonSystemJobRunsCondition;
import org.jboss.tools.portlet.ui.bot.task.wait.condition.ShellIsActiveCondition;

/**
 * 
 * Provides the functionality of navigating between pages of open wizard. The data
 * should fill every wizard page itself.
 * 
 * @author Lucia Jelinkova
 *
 */
public class WizardFillingTask extends CompositeSWTTask<WizardPageFillingTask>{

	private List<WizardPageFillingTask> wizardPages;

	public WizardFillingTask() {
		super();
		wizardPages = new ArrayList<WizardPageFillingTask>();
	}
	
	@Override
	public void perform() {
		SWTBotShell activeShell = getActiveShell();

		super.setTasks(wizardPages);
		super.perform();

		getBot().button("Finish").click();
		performInnerTask(new WaitWhileTask(new ShellIsActiveCondition(activeShell), TaskDuration.LONG));
		performInnerTask(new WaitWhileTask(new NonSystemJobRunsCondition(), TaskDuration.LONG));
	}

	public void addWizardPage(WizardPageFillingTask task){
		wizardPages.add(task);
	}

	public void addAllWizardPages(List<WizardPageFillingTask> tasks){
		wizardPages.addAll(tasks);
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

	private SWTBotShell getActiveShell(){
		for (SWTBotShell shell : getBot().shells()){
			if (shell.isActive()){
				return shell;
			}
		}

		throw new IllegalStateException("No active shell found");
	}
}