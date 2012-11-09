package org.jboss.tools.maven.ui.bot.test.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.jobs.Job;

public class WaitForAll {
	public static final int NONE = 0;
	public static final int SLEEPING = 1;
	public static final int WAITING = 2;
	public static final int RUNNING = 4;

	public boolean test() {
		// Find all jobs
		Job[] jobs = Job.getJobManager().find(null);
		List<String> listNames = new ArrayList<String>();

		for (Job job : jobs) {
			listNames.add(job.getName());
		}

		// Create rest job list
		String[] names = new String[listNames.size()];
		for (int i = 0; i < listNames.size(); i++) {
			names[i] = listNames.get(i);
		}

		// Jobs prescribed
		long startTime = System.currentTimeMillis();
		List<String> blockingJobs = new ArrayList<String>();

		// Go through jobs and wait for completion
		for (String jobName : names) {
			if (isJobRunning(jobName)) {
				blockingJobs.add(jobName);
			}
		}

		// Wait until all blocking jobs aren't finished or timeout
		for (String jobName : blockingJobs) {
			while (true) {
				if (!isJobRunning(jobName)) {
					boolean jobStoped = true;

					if (jobStoped) {
						break;
					}
				}
				return true;
			}
		}
		return false;
	}

	public boolean isJobRunning(String jobName) {
		Job[] jobs = Job.getJobManager().find(null);
		for (Job job : jobs) {
			if ((jobName.equalsIgnoreCase(job.getName()))
					&& ((job.getState() == RUNNING) || (job.getState() == WAITING)))
				return true;
		}
		return false;
	}

}
