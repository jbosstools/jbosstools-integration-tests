package org.jboss.tools.runtime.as.ui.bot.test.reddeer.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.eclipse.wst.server.ui.RuntimePreferencePage;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.runtime.as.ui.bot.test.parametized.CleanEnvironmentUtils;
import org.jboss.tools.runtime.as.ui.bot.test.reddeer.ui.RuntimeDetectionPreferencePage;
import org.jboss.tools.runtime.as.ui.bot.test.reddeer.ui.SeamPreferencePage;
import org.jboss.tools.runtime.as.ui.bot.test.reddeer.ui.SearchingForRuntimesDialog;
import org.jboss.tools.runtime.core.model.RuntimePath;
import org.jboss.tools.runtime.ui.RuntimeUIActivator;

/**
 * Provides useful methods that can be used by its descendants. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class RuntimeDetectionUtility {

	public static RuntimeDetectionPreferencePage runtimeDetectionPage = new RuntimeDetectionPreferencePage();

	public static SeamPreferencePage seamPreferencePage = new SeamPreferencePage();

	public static RuntimePreferencePage runtimePreferencePage = new RuntimePreferencePage();

	public static WorkbenchPreferenceDialog preferences = new WorkbenchPreferenceDialog();

	public static SearchingForRuntimesDialog addPath(String path){
		RuntimeUIActivator.getDefault().getModel().addRuntimePath(new RuntimePath(new File(path).getAbsolutePath()));
		runtimeDetectionPage = new RuntimeDetectionPreferencePage();
		preferences.open();
		preferences.select(runtimeDetectionPage);
		if(!runtimeDetectionPage.getAllPaths().contains(path)) {
			preferences.cancel();
			preferences.open();
			preferences.select(runtimeDetectionPage);
		}
		return runtimeDetectionPage.search();
	}

	public static SearchingForRuntimesDialog searchFirstPath(){
		runtimeDetectionPage = new RuntimeDetectionPreferencePage();
		preferences.open();
		preferences.select(runtimeDetectionPage);
		return runtimeDetectionPage.search();
	}

	public static void removeAllPaths(){
		CleanEnvironmentUtils.cleanPaths();
	}

	public static void removeAllSeamRuntimes(){
		CleanEnvironmentUtils.cleanSeamRuntimes();
	}

	public static void removeAllServerRuntimes(){
		CleanEnvironmentUtils.cleanServerRuntimes();
	}

	public static void assertSeamRuntimesNumber(int expected) {
		preferences.open();
		try{
			preferences.select(seamPreferencePage);
		}catch (CoreLayerException ex){
			if (expected>0){
				fail("Seam runtimes expected, but seam tooling is not installed.");
			}else{
				preferences.ok();
				return;
			}
		}
		assertThat(seamPreferencePage.getRuntimes().size(), is(expected));
		preferences.ok();
	}

	public static void assertServerRuntimesNumber(int expected) {
		preferences.open();
		preferences.select(runtimePreferencePage);
		List<org.jboss.reddeer.eclipse.wst.server.ui.Runtime> runtimes = 
				runtimePreferencePage.getServerRuntimes();
		assertThat("Expected are " + expected + " runtimes but there are:\n"
				+ Arrays.toString(runtimes.toArray()), runtimes.size(), is(expected));
		preferences.ok();
	}
}
