package org.jboss.tools.perf.test.core.swtbot;

import org.jboss.perf.test.client.listener.junit.PerfListener;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.notification.RunListener;

/**
 * JUnit4 requirement aware testsuite runner measuring performance.
 * 
 * @author Vlado Pakan
 */
public class PerformanceRequirementAwareSuite extends RequirementAwareSuite {
  final static PerfListener[] performanceListeners = new PerfListener[] {new PerfListener()};
	public PerformanceRequirementAwareSuite(Class<?> klass) throws Throwable {
    super(klass);
  }
  @Override
  public RunListener[] getRunListenersToAdd (){
    return performanceListeners;
  }
  @Override
  public RunListener[] getRunListenersToRemove (){
    return performanceListeners;
  }
  
}
