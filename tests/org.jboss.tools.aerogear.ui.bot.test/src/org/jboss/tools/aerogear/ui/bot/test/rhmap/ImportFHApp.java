package org.jboss.tools.aerogear.ui.bot.test.rhmap;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.aerogear.ui.bot.test.FeedHenryBotTest;
import org.junit.Test;

@CleanWorkspace
public class ImportFHApp extends FeedHenryBotTest{

	@Test
	public void testAppIsImported(){
	
		importApp(FH_PROJECT, FH_APP_NAME);
		assertTrue(new ProjectExplorer().containsProject(FH_APP_NAME));
	}
	
}
