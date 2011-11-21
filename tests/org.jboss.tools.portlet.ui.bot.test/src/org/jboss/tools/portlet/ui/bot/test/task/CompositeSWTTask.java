package org.jboss.tools.portlet.ui.bot.test.task;

import java.util.ArrayList;
import java.util.List;

/**
 * Performs a set of tasks and enables subclasses to define actions before and after 
 * each of the tasks is performed. 
 * 
 * @author ljelinko
 *
 */
public class CompositeSWTTask<T extends SWTTask> extends AbstractSWTTask {

	private List<T> tasks;
	
	public CompositeSWTTask() {
		super();
		tasks = new ArrayList<T>();
	}
	
	@Override
	public void perform() {
		for (T task : tasks){
			beforeTask(task);
			performInnerTask(task);
			afterTask(task);
		}
	}

	protected void beforeTask(T task) {
		// hook up method
	}

	protected void afterTask(T task) {
		// hook up method
	}

	public void setTasks(List<T> tasks) {
		this.tasks = tasks;
	}
	
	public List<T> getTasks() {
		return tasks;
	}
}
