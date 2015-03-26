package org.jboss.tools.arquillian.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.arquillian.ui.bot.test.project.AddArquillianProfile;
import org.jboss.tools.arquillian.ui.bot.test.project.CreateArquillianProject;
import org.jboss.tools.arquillian.ui.bot.test.testcase.CreateArquillianTestCase;
import org.jboss.tools.arquillian.ui.bot.test.testcase.RunArquillianTestCase;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RedDeerSuite.class)
@SuiteClasses({
	CreateArquillianProject.class,
	AddArquillianProfile.class,
	CreateArquillianTestCase.class,
	RunArquillianTestCase.class
	})
public class ArquillianSuite {
	
}
