package org.jboss.tools.eclipsecs.ui.test;

import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Suite for all EclipseCS UI tests
 * @author Jiri Peterka
 *
 */
@RunWith(RedDeerSuite.class)
@Suite.SuiteClasses({

	CheckstyleTest.class,
	CheckStyleUIPartsTest.class
})
public class AllSuite {

}