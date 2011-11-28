package org.jboss.tools.portlet.ui.bot.task.progress;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.waits.ICondition;
import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;

/**
 * Waits until there is no job running.
 * 
 * @author Lucia Jelinkova
 *
 */
public class AllJobsFinishedWaitingTask extends AbstractSWTTask {
	
	public enum JobDuration {
		SHORT(1 * 1000), NORMAL(10 * 1000), LONG(1 * 60 * 1000), VERY_LONG(10 * 60 * 1000);
		
		private long timeout;
		
		private JobDuration(long timeout) {
			this.timeout = timeout;
		}
		
		public long getTimeout() {
			return timeout;
		}
	}
	
	private JobDuration jobDuration;
	
	public AllJobsFinishedWaitingTask(JobDuration timeout) {
		super();
		this.jobDuration = timeout;
	}

	@Override
	public void perform() {
		getBot().waitUntil(new ICondition() {
			
			@Override
			public boolean test() throws Exception {
				return Job.getJobManager().isIdle();
			}
			
			@Override
			public void init(SWTBot bot) {
				
			}
			
			@Override
			public String getFailureMessage() {
				return null;
			}
		}, jobDuration.getTimeout());
	}
}
