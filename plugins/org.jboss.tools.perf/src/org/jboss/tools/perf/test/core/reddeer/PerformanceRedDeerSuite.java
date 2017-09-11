package org.jboss.tools.perf.test.core.reddeer;

import org.jboss.perf.test.client.listener.junit.PerfListener;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.junit.runners.model.RunnerBuilder;

/**
 * RedDeer testsuite runner measuring performance.
 * 
 * @author Vlado Pakan
 */
public class PerformanceRedDeerSuite extends RedDeerSuite {
  final static PerfListener[] performanceListeners = new PerfListener[] {new PerfListener()};
  
  static{
    runListeners = performanceListeners;
  }
  
	public PerformanceRedDeerSuite(Class<?> klass , RunnerBuilder builder) throws Throwable {
    super(klass,builder);
  }
}
