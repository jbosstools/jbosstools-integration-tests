package org.jboss.tools.ui.bot.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;

public abstract class JBTSWTBotTestCase extends SWTBotTestCase implements
		ILogListener {

	protected static final String BUILDING_WS = "Building workspace";
	protected static final String VISUAL_UPDATE = "Visual Editor View Update";
	protected static final String VISUAL_REFRESH = "Visual Editor Refresh";
	protected static final String UPDATING_INDEXES = "Updating indexes";
	protected static final String STARTING_JBOSS_EAP_43_RUNTIME = "Starting JBoss EAP 4.3 Runtime Serve";
	protected static final String STOPPING_JBOSS_EAP_43_RUNTIME = "Stoppig JBoss EAP 4.3 Runtime Server";
	private static Properties SWT_BOT_PROPERTIES;
	private volatile Throwable exception;
	public static final String PATH_TO_SWT_BOT_PROPERTIES = "SWTBot.properties";
	protected SWTJBTBot bot = new SWTJBTBot();
	private static int sleepTime = 1000;

	/*
	 * (non-Javadoc) This static block read properties from
	 * org.jboss.tools.ui.bot.test/resources/SWTBot.properties file and set up
	 * parameters for SWTBot tests. You may change a number of parameters in
	 * static block and their values in property file.
	 */

	static {
		try {
			InputStream inputStream = JBTSWTBotTestCase.class
					.getResourceAsStream("/" + PATH_TO_SWT_BOT_PROPERTIES);
			SWT_BOT_PROPERTIES = new Properties();
			SWT_BOT_PROPERTIES.load(inputStream);
			SWTBotPreferences.PLAYBACK_DELAY = Long
					.parseLong(SWT_BOT_PROPERTIES
							.getProperty("SWTBotPreferences.PLAYBACK_DELAY"));
			SWTBotPreferences.TIMEOUT = Long.parseLong(SWT_BOT_PROPERTIES
					.getProperty("SWTBotPreferences.TIMEOUT"));
			inputStream.close();
		} catch (IOException e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID,
					"Can't load properties from " + PATH_TO_SWT_BOT_PROPERTIES
							+ " file", e);
			Activator.getDefault().getLog().log(status);
			e.printStackTrace();
		} catch (IllegalStateException e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID,
					"Property file " + PATH_TO_SWT_BOT_PROPERTIES
							+ " was not found", e);
			Activator.getDefault().getLog().log(status);
			e.printStackTrace();
		}
		// try {
		// InputStream swtPreferenceIS =
		// Platform.getBundle(Activator.PLUGIN_ID).getResource(PATH_TO_SWT_BOT_PROPERTIES)
		// .openStream();
		// SWT_BOT_PROPERTIES = new Properties();
		// SWT_BOT_PROPERTIES.load(swtPreferenceIS);
		// SWTBotPreferences.PLAYBACK_DELAY = Long
		// .parseLong(SWT_BOT_PROPERTIES
		// .getProperty("SWTBotPreferences.PLAYBACK_DELAY"));
		// SWTBotPreferences.TIMEOUT = Long.parseLong(SWT_BOT_PROPERTIES
		// .getProperty("SWTBotPreferences.TIMEOUT"));
		// swtPreferenceIS.close();
		// } catch (IOException e) {
		// fail("Can't load properties from " + PATH_TO_SWT_BOT_PROPERTIES +
		// " file");
		// } catch (IllegalStateException e) {
		// fail("Property file " + PATH_TO_SWT_BOT_PROPERTIES +
		// " was not found");
		// }
	}

	public void logging(IStatus status, String plugin) {
		switch (status.getSeverity()) {
		case IStatus.ERROR:
			Throwable throwable = status.getException();
			if (throwable == null) {
				throwable = new Throwable(status.getMessage() + " in "
						+ status.getPlugin());
			}
			setException(throwable);
			break;
		default:
			break;
		}
	}

	/**
	 * Getter method for exception that may be thrown during test execution.
	 * <p>
	 * You can call this method from any place of your methods and verify if any
	 * exception was thrown during test executing including Error Log.
	 * 
	 * @return exception
	 * @see Throwable
	 */

	protected synchronized Throwable getException() {
		return exception;
	}

	/**
	 * Setter method for exception. If param is not null test will fail and you
	 * will see stack trace in JUnit Error Log
	 * 
	 * @param e
	 *            - exception, that can be frown during test executing
	 * @see Throwable
	 */

	protected synchronized void setException(Throwable e) {
		this.exception = e;
	}

	/**
	 * Delete .log file from junit-workspace .metadata, if it hadn't been
	 * deleted before
	 * <p>
	 * So we can catch exceptions and errors, which were thrown during current
	 * test.
	 */

	private void deleteLog() {
		try {
			Platform.getLogFileLocation().toFile().delete();
		} catch (Exception e) {
		}
	}

	/**
	 * Make a default preconditions before test launch
	 * 
	 * @see #activePerspective()
	 * @see #openErrorLog()
	 * @see #openPackageExplorer()
	 * @see #setException(Throwable)
	 * @see #deleteLog()
	 * @see #delay()
	 */

	@Override
	protected void setUp() throws Exception {
		activePerspective();
		try {
			bot.viewByTitle(WidgetVariables.WELCOME).close();
		} catch (WidgetNotFoundException e) {
		}
		openErrorLog();
		openPackageExplorer();
		// openProgressStatus();
		deleteLog();
		setException(null);
		Platform.addLogListener(this);
		// delay();
	}

	/**
	 * Tears down the fixture. Verify Error Log.
	 * 
	 * @see #getException()
	 */

	@Override
	protected void tearDown() throws Exception {
		Platform.removeLogListener(this);
		deleteLog();
		if (getException() != null) {
			throw new Exception(getException());
		}
	}

	/**
	 * A little delay between test's steps. Use it where necessary.
	 */

	protected void delay() {
		bot.sleep(sleepTime);
	}

	/**
	 * Defines which kind of perspective should be activated before tests' run.
	 */

	abstract protected void activePerspective();

	/**
	 * Open and activate Error Log view if it hadn't been opened before
	 */

	protected void openErrorLog() {
		try {
			bot.viewByTitle(WidgetVariables.ERROR_LOG);
		} catch (WidgetNotFoundException e) {
			bot.menu("Window").menu("Show View").menu("Other...").click();
			SWTBotTree viewTree = bot.tree();
			delay();
			viewTree.expandNode("General")
					.expandNode(WidgetVariables.ERROR_LOG).select();
			bot.button("OK").click();
		}
	}

	/**
	 * Open and activate Package Explorer view if it hadn't been opened before
	 */

	protected void openPackageExplorer() {
		try {
			bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).setFocus();
		} catch (WidgetNotFoundException e) {
			bot.menu("Window").menu("Show View").menu("Other...").click();
			SWTBotTree viewTree = bot.tree();
			delay();
			viewTree.expandNode("Java").expandNode(
					WidgetVariables.PACKAGE_EXPLORER).select();
			bot.button("OK").click();
		}
	}
	
	/**
   * Open and activate Web Projects view if it hadn't been opened before
   */

  protected void openWebProjects() {
    try {
      bot.viewByTitle(WidgetVariables.WEB_PROJECTS).setFocus();
    } catch (WidgetNotFoundException e) {
      bot.menu("Window").menu("Show View").menu("Other...").click();
      SWTBotTree viewTree = bot.tree();
      delay();
      viewTree.expandNode("Java").expandNode(
          WidgetVariables.WEB_PROJECTS).select();
      bot.button("OK").click();
    }
  }

  /**
   * Open and activate Server View if it hadn't been opened before
   */

  protected void openServerView() {
    try {
      bot.viewByTitle(WidgetVariables.SERVERS).setFocus();
    } catch (WidgetNotFoundException e) {
      bot.menu("Window").menu("Show View").menu("Other...").click();
      SWTBotTree viewTree = bot.tree();
      delay();
      viewTree.expandNode("Server").expandNode(
          WidgetVariables.SERVERS).select();
      bot.button("OK").click();
    }
  }
  /**
   * Open and activate Properties View if it hadn't been opened before
   */

  protected void openPropertiesView() {
    try {
      bot.viewByTitle(WidgetVariables.PROPERTIES).setFocus();
    } catch (WidgetNotFoundException e) {
      bot.menu("Window").menu("Show View").menu("Other...").click();
      SWTBotTree viewTree = bot.tree();
      delay();
      viewTree.expandNode("General").expandNode(
          WidgetVariables.PROPERTIES).select();
      bot.button("OK").click();
    }
  }
  /**
   * Open and activate Outline View if it hadn't been opened before
   */

  protected void openOutlineView() {
    try {
      bot.viewByTitle(WidgetVariables.OUTLINE).setFocus();
    } catch (WidgetNotFoundException e) {
      bot.menu("Window").menu("Show View").menu("Other...").click();
      SWTBotTree viewTree = bot.tree();
      delay();
      viewTree.expandNode("General").expandNode(
          WidgetVariables.OUTLINE).select();
      bot.button("OK").click();
    }
  }
	// protected void openProgressStatus() {
	// try {
	// bot.viewByTitle(WidgetVariables.PROGRESS_STATUS);
	// } catch (WidgetNotFoundException e) {
	// bot.menu("Window").menu("Show View").menu("Other...").click();
	// SWTBotTree viewTree = bot.tree();
	// delay();
	// viewTree.expandNode("General").expandNode(WidgetVariables.PROGRESS_STATUS).select();
	// bot.button("OK").click();
	// }
	// }

	/**
	 * Use delay() method instead
	 * 
	 * @see #delay()
	 */
	@Deprecated
	protected final void waitForJobs() {
		delay();
	}

	protected final void waitForBlockingJobsAcomplished(final long timeOut,
			final String... jobNames) {
		if (jobNames == null) {
			return;
		} else {
			Map<String, Boolean> runningProcessesMap = new HashMap<String, Boolean>();
			for (int i = 0; i < jobNames.length; i++) {
				runningProcessesMap.put(jobNames[i], false);
			}
			boolean isRequiredProcessesStarted = false;
			long startTime = System.currentTimeMillis();
			while (!isRequiredProcessesStarted) {
				Job[] jobs = Job.getJobManager().find(null);
				for (Job job : jobs) {
					for (String jobName : jobNames) {
						if (jobName.equalsIgnoreCase(job.getName())) {
							if (!runningProcessesMap.get(jobName)) {
								runningProcessesMap.remove(jobName);
								runningProcessesMap.put(jobName, true);
							}
						}
					}
				}
				isRequiredProcessesStarted = true;
				for (String jobName : jobNames) {
					isRequiredProcessesStarted = isRequiredProcessesStarted
							& runningProcessesMap.get(jobName);
				}
				if (!isRequiredProcessesStarted) {
					long endTime = System.currentTimeMillis();
					if (endTime - startTime > timeOut) {
						System.out.println("Next processes are already started or finished: "+
								runningProcessesToString(jobNames)+" called in " + getName() + " class");
						return;
					}
				}
			}
			while (isRequiredProcessesStarted) {
				isRequiredProcessesStarted = false;
				Job[] jobs = Job.getJobManager().find(null);
				for (Job job : jobs) {
					for (String jobName : jobNames) {
						if (jobName.equalsIgnoreCase(job.getName())) {
							isRequiredProcessesStarted = true;
							delay();
						}
					}
				}
			}
		}
	}

	protected final void waitForBlockingJobsAcomplished(String... jobNames) {
		waitForBlockingJobsAcomplished(5 * 1000L, jobNames);
	}

	private String runningProcessesToString (String ... processes){
		String process = "";
		for (String processRunning : processes) {
			process = process + processRunning + ", ";
		}
		return process;
	}
	
}
