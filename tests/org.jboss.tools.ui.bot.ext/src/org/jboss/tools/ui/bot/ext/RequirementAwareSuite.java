 package org.jboss.tools.ui.bot.ext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.jboss.tools.ui.bot.ext.config.TestConfigurator;
import org.jboss.tools.ui.bot.ext.config.Annotations.SWTBotTestRequires;
import org.jboss.tools.ui.bot.ext.config.requirement.RequirementBase;
import org.junit.runner.Runner;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

/**
 * JUnit4 requirement aware testsuite runner. If suite class is annotated by @ RunWith({@link RequirementAwareSuite}) class, test classes can have {@link SWTBotTestRequires} annotations
 * 
 *  @author lzoubek@redhat.com
 */
public class RequirementAwareSuite extends Suite {	
	class ReqAwareClassRunner extends BlockJUnit4ClassRunner {
		private final List<RequirementBase> requirements;
		public ReqAwareClassRunner(Class<?> klass, List<RequirementBase> requirements) throws InitializationError {
			super(klass);
			this.requirements = requirements;
		}
		@Override
		protected Object createTest() throws Exception {
			log.info("Fullfilling requirements before test "+getTestClass().getJavaClass());
			for (RequirementBase r : requirements) {
				r.fulfill();
			}
			return super.createTest();
		}
		
	}
	
	private static final Logger log = Logger.getLogger(RequirementAwareSuite.class);
	private class RequirementAwareRunnerBuilder extends RunnerBuilder {	
		@Override
		public Runner runnerForClass(Class<?> klass) throws Throwable {	
			List<RequirementBase> reqs = TestConfigurator.getClassRequirements(klass);
			if (reqs!=null) {
				log.info("Returning runner for test class "+klass.getCanonicalName());
				return new ReqAwareClassRunner(klass,reqs);
			}
			log.info("Skipping test class "+klass.getCanonicalName());
			return null;
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
	public RequirementAwareSuite(Class<?> klass)
			throws Throwable {
		super(klass, Collections.<Runner> emptyList());
		runners.add(new Suite(klass, new RequirementAwareRunnerBuilder()));

	}	
	@Override
	protected List<Runner> getChildren() {
		return runners;
	}	
}
