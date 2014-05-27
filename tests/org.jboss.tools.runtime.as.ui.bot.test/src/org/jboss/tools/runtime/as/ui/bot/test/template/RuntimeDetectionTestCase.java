package org.jboss.tools.runtime.as.ui.bot.test.template;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.Arrays;
import java.util.List;

import org.jboss.reddeer.eclipse.wst.server.ui.RuntimePreferencePage;
import org.jboss.tools.runtime.as.ui.bot.test.dialog.preferences.RuntimeDetectionPreferencesDialog;
import org.jboss.tools.runtime.as.ui.bot.test.dialog.preferences.SeamPreferencesDialog;
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

	protected RuntimeDetectionPreferencesDialog runtimeDetectionPreferences = new RuntimeDetectionPreferencesDialog();

	protected SeamPreferencesDialog seamPreferences = new SeamPreferencesDialog();
	
	protected RuntimePreferencePage serverRuntimesPreferences = new RuntimePreferencePage();
	
	protected SearchingForRuntimesDialog addPath(String path){
		RuntimeUIActivator.getDefault().getModel().addRuntimePath(new RuntimePath(path));
		runtimeDetectionPreferences = new RuntimeDetectionPreferencesDialog();
		runtimeDetectionPreferences.open();
		if(!runtimeDetectionPreferences.getAllPaths().contains(path)) {
			runtimeDetectionPreferences.cancel();
			runtimeDetectionPreferences.open();
		}
		return runtimeDetectionPreferences.search();
	}
	
	protected SearchingForRuntimesDialog searchFirstPath(){
		runtimeDetectionPreferences = new RuntimeDetectionPreferencesDialog();
		runtimeDetectionPreferences.open();
		return runtimeDetectionPreferences.search();
	}
	
	protected void removeAllPaths(){
		runtimeDetectionPreferences.open();
		runtimeDetectionPreferences.removeAllPaths();
		runtimeDetectionPreferences.ok();
	}
	
	protected void removeAllSeamRuntimes(){
		seamPreferences.open();
		seamPreferences.removeAllRuntimes();
		seamPreferences.ok();
	}
	
	protected void removeAllServerRuntimes(){
		serverRuntimesPreferences.open();
		serverRuntimesPreferences.removeAllRuntimes();
		serverRuntimesPreferences.ok();
	}
	
	protected void assertSeamRuntimesNumber(int expected) {
		seamPreferences.open();
		assertThat(seamPreferences.getRuntimes().size(), is(expected));
		seamPreferences.ok();
	}
	
	protected void assertServerRuntimesNumber(int expected) {
		serverRuntimesPreferences.open();
		List<org.jboss.reddeer.eclipse.wst.server.ui.Runtime> runtimes = 
				serverRuntimesPreferences.getServerRuntimes();
		assertThat("Expected are " + expected + " runtimes but there are:\n"
				+ Arrays.toString(runtimes.toArray()), runtimes.size(), is(expected));
		serverRuntimesPreferences.ok();
	}
}
