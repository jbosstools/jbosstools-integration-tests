package org.jboss.tools.aerogear.ui.bot.test;

import org.jboss.tools.aerogear.ui.bot.test.app.CreateHybridApplication;
import org.jboss.tools.aerogear.ui.bot.test.app.OpenConfigEditor;
import org.jboss.tools.aerogear.ui.bot.test.app.RunWithCordovaSim;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({ 
	CreateHybridApplication.class, 
	OpenConfigEditor.class, 
	RunWithCordovaSim.class })
@RunWith(RequirementAwareSuite.class)
public class AerogearAllBotTests {

}
