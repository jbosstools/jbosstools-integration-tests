package org.jboss.tools.ui.bot.ext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swtbot.swt.finder.junit.ScreenshotCaptureListener;
import org.jboss.tools.ui.bot.ext.config.TestConfigurator;
import org.jboss.tools.ui.bot.ext.config.Annotations.SWTBotTestRequires;
import org.jboss.tools.ui.bot.ext.config.requirement.RequirementBase;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;
import org.junit.runners.model.Statement;

/**
 * JUnit4 requirement aware testsuite runner. If suite class is annotated by @
 * RunWith({@link RequirementAwareSuite}) class, test classes can have
 * {@link SWTBotTestRequires} annotations
 * 
 * @author lzoubek@redhat.com
 */
public class RequirementAwareSuite extends Suite {
	// we have one global instance of cleanup listener
	final static DoAfterAllTestsRunListener cleanUp = new DoAfterAllTestsRunListener();

	class ReqAwareClassRunner extends BlockJUnit4ClassRunner {
		private final List<RequirementBase> requirements;

		public ReqAwareClassRunner(Class<?> klass,
				List<RequirementBase> requirements) throws InitializationError {
			super(klass);
			this.requirements = requirements;
		}

		@Override
		public void run(RunNotifier notifier) {
			// planned test counter must know about all tests (methods) within a class
			cleanUp.incrPlanned(getChildren().size()-1);
			// ensure that we have exactly 1 cleanup listener registered
			notifier.removeListener(cleanUp);
			notifier.addListener(cleanUp);
			// adding ability to create screen shot (taken from
			// SWTBotJunit4ClassRunner)
			RunListener failureSpy = new ScreenshotCaptureListener();
			notifier.removeListener(failureSpy);
			notifier.addListener(failureSpy);
			try {
				super.run(notifier);
			} finally {
				notifier.removeListener(failureSpy);
			}
		}
		@Override
		protected Statement withBeforeClasses(Statement statement) {
			log.info("Fullfilling requirements before test "
					+ getTestClass().getJavaClass());
			try {
				for (RequirementBase r : requirements) {
					r.fulfill();
				}
			} catch (Exception e) {
				log.error("Fulfilling failed", e);
			}

			return super.withBeforeClasses(statement);
		}
	}

	private static final Logger log = Logger
			.getLogger(RequirementAwareSuite.class);

	private class RequirementAwareRunnerBuilder extends RunnerBuilder {
		@Override
		public Runner runnerForClass(Class<?> klass) throws Throwable {
			List<RequirementBase> reqs = TestConfigurator
					.getClassRequirements(klass);
			if (reqs != null) {
				if (!TestConfigurator.checkConfig()) {
					log.info("Skipping class '" + klass.getCanonicalName()
							+ "' - incorrect configuration");
					return null;
				}
				log.info("Returning runner for test class "
						+ klass.getCanonicalName());
				// ensure creating runner instance
				Runner runner = new ReqAwareClassRunner(klass, reqs);
				// increment number of tests planned to run by 1 (class contains at least 1 test method)
				cleanUp.incrPlanned();
				return runner;
			}
			log.info("Skipping class '" + klass.getCanonicalName()
					+ "' - annotations do not met configuration");
			return null;
		}

	}

	/**
	 * listener which listens to test runs, does some cleanup after all tests
	 * have run
	 * 
	 * @author lzoubek
	 * 
	 */
	static class DoAfterAllTestsRunListener extends RunListener {
		// As we can run more suites at once, we need to count tests which are planned to run
		// and the ones which already passed (or failed), perform cleanups when the last one finishes
		private int testsAboutToRun = 0;
		private int testsFinished = 0;

		public void incrPlanned() {
			testsAboutToRun += 1;
		}
		public void incrPlanned(int amount) {
			testsAboutToRun += amount;
		}

		public void incrFinished() {
			testsFinished += 1;
		}

		public int getPlanned() {
			return testsAboutToRun;
		}

		public int getFinished() {
			return testsFinished;
		}		
		@Override
		public void testFinished(Description description) throws Exception {
			incrFinished();
			log.info("Finished tests : "+getFinished()+"/"+getPlanned());
			if (getFinished() >= getPlanned()) {
				log.info("All tests finished, performing cleanup requirements ");
				try {
					RequirementBase.createStopServer().fulfill();

					log.info("All cleanup requirements performed");
				} catch (Exception ex) {
					log.error("Unable to fulfill cleanup requirements", ex);
				}
			}
			super.testFinished(description);
		}
	}

	private final ArrayList<Runner> runners = new ArrayList<Runner>();

	/**
	 * Only called reflectively. Do not use programmatically.
	 */

	/**
	 * Called reflectively on classes annotated with
	 * <code>@RunWith(RequirementAwareSuite.class)</code>
	 * 
	 * @param klass
	 *            the root class
	 */
	public RequirementAwareSuite(Class<?> klass) throws Throwable {
		super(klass, Collections.<Runner> emptyList());
		runners.add(new Suite(klass, new RequirementAwareRunnerBuilder()));
	}

	@Override
	protected List<Runner> getChildren() {
		return runners;
	}
}
