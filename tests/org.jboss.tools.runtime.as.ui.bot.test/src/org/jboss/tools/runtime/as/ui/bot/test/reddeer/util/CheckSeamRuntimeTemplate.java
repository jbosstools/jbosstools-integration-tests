package org.jboss.tools.runtime.as.ui.bot.test.reddeer.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.runtime.as.ui.bot.test.reddeer.Runtime;
import org.jboss.tools.runtime.as.ui.bot.test.reddeer.RuntimeMatcher;
import org.junit.After;
import org.junit.Test;

public abstract class CheckSeamRuntimeTemplate extends RuntimeDetectionUtility {

	protected abstract Runtime getExpectedRuntime();
	
	@Test
	public void checkSeamRuntime(){
		WorkbenchPreferenceDialog dialog = new WorkbenchPreferenceDialog();
		dialog.open();
		dialog.select(seamPreferencePage);
		
		assertThat(seamPreferencePage.getRuntimes().size(), is(1));
		assertThat(seamPreferencePage.getRuntimes().get(0), new RuntimeMatcher(getExpectedRuntime()));
	}
	
	@After
	public void cleanup(){
		removeAllSeamRuntimes();
		assertSeamRuntimesNumber(0);
	}
}
