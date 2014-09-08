package org.jboss.tools.runtime.as.ui.bot.test.template;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.Arrays;
import java.util.List;

import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;
import org.jboss.reddeer.eclipse.wst.server.ui.RuntimePreferencePage;
import org.jboss.tools.runtime.as.ui.bot.test.dialog.preferences.RuntimeDetectionPreferencePage;
import org.jboss.tools.runtime.as.ui.bot.test.dialog.preferences.SeamPreferencePage;
import org.jboss.tools.runtime.as.ui.bot.test.dialog.preferences.SearchingForRuntimesDialog;
import org.jboss.tools.runtime.core.model.RuntimePath;
import org.jboss.tools.runtime.ui.RuntimeUIActivator;

/**
 * Provides useful methods that can be used by its descendants. 
 * 
 * @author Lucia Jelinkova
 *
 */
public abstract class RuntimeDetectionTestCase {

	protected RuntimeDetectionPreferencePage runtimeDetectionPage = new RuntimeDetectionPreferencePage();

	protected SeamPreferencePage seamPreferencePage = new SeamPreferencePage();

	protected RuntimePreferencePage runtimePreferencePage = new RuntimePreferencePage();

	protected WorkbenchPreferenceDialog preferences = new WorkbenchPreferenceDialog();

	protected SearchingForRuntimesDialog addPath(String path){
		RuntimeUIActivator.getDefault().getModel().addRuntimePath(new RuntimePath(path));
		runtimeDetectionPage = new RuntimeDetectionPreferencePage();
		preferences.select(runtimeDetectionPage);
		if(!runtimeDetectionPage.getAllPaths().contains(path)) {
			runtimeDetectionPage.cancel();
			preferences.select(runtimeDetectionPage);
		}
		return runtimeDetectionPage.search();
	}

	protected SearchingForRuntimesDialog searchFirstPath(){
		runtimeDetectionPage = new RuntimeDetectionPreferencePage();
		preferences.select(runtimeDetectionPage);
		return runtimeDetectionPage.search();
	}

	protected void removeAllPaths(){
		preferences.select(runtimeDetectionPage);
		runtimeDetectionPage.removeAllPaths();
		runtimeDetectionPage.ok();
	}

	protected void removeAllSeamRuntimes(){
		preferences.select(seamPreferencePage);
		seamPreferencePage.removeAllRuntimes();
		seamPreferencePage.ok();
	}

	protected void removeAllServerRuntimes(){
		preferences.select(runtimePreferencePage);
		runtimePreferencePage.removeAllRuntimes();
		runtimePreferencePage.ok();
	}

	protected void assertSeamRuntimesNumber(int expected) {
		preferences.select(seamPreferencePage);
		assertThat(seamPreferencePage.getRuntimes().size(), is(expected));
		seamPreferencePage.ok();
	}

	protected void assertServerRuntimesNumber(int expected) {
		preferences.select(runtimePreferencePage);
		List<org.jboss.reddeer.eclipse.wst.server.ui.Runtime> runtimes = 
				runtimePreferencePage.getServerRuntimes();
		assertThat("Expected are " + expected + " runtimes but there are:\n"
				+ Arrays.toString(runtimes.toArray()), runtimes.size(), is(expected));
		runtimePreferencePage.ok();
	}
}
