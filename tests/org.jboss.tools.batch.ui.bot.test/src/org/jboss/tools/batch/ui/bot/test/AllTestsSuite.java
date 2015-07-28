package org.jboss.tools.batch.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RedDeerSuite.class)
@SuiteClasses({
	CreateJobXMLFileTest.class, 
	CreateBatchArtifactTest.class
	})
public class AllTestsSuite {

}
