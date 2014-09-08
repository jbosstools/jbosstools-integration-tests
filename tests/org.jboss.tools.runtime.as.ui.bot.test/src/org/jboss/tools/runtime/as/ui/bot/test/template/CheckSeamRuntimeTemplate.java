package org.jboss.tools.runtime.as.ui.bot.test.template;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;
import org.jboss.tools.runtime.as.ui.bot.test.entity.Runtime;
import org.jboss.tools.runtime.as.ui.bot.test.matcher.RuntimeMatcher;
import org.junit.After;
import org.junit.Test;

public abstract class CheckSeamRuntimeTemplate extends RuntimeDetectionTestCase {

	protected abstract Runtime getExpectedRuntime();
	
	@Test
	public void checkSeamRuntime(){
		WorkbenchPreferenceDialog dialog = new WorkbenchPreferenceDialog();
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
