package org.jboss.tools.batch.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.batch.ui.bot.test.wizard.CreateAllArtifactsFromAbstractClassTest;
import org.jboss.tools.batch.ui.bot.test.wizard.CreateAllArtifactsFromInterfaceTest;
import org.jboss.tools.batch.ui.bot.test.wizard.CreateJobXMLFileTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RedDeerSuite.class)
@SuiteClasses({
	CreateJobXMLFileTest.class, 
	CreateAllArtifactsFromInterfaceTest.class, 
	CreateAllArtifactsFromAbstractClassTest.class
	})
public class AllTestsSuite {

}
