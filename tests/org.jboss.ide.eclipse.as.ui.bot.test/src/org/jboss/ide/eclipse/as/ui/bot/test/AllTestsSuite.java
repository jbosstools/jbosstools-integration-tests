package org.jboss.ide.eclipse.as.ui.bot.test;

import org.jboss.ide.eclipse.as.ui.bot.test.as7.CreateAS7Test;
import org.jboss.ide.eclipse.as.ui.bot.test.as7.DeleteAS7Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as7.DeployJSPProject;
import org.jboss.ide.eclipse.as.ui.bot.test.as7.OperateAS7Server;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RequirementAwareSuite.class)
@Suite.SuiteClasses({
		CreateAS7Test.class, 
		OperateAS7Server.class,
		DeployJSPProject.class,
		DeleteAS7Server.class
})
public class AllTestsSuite {

}
