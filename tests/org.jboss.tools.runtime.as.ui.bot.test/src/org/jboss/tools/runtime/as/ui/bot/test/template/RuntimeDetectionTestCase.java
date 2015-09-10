package org.jboss.tools.runtime.as.ui.bot.test.template;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.fail;

import java.awt.image.CropImageFilter;
import java.util.Arrays;
import java.util.List;

import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.reddeer.core.exception.CoreLayerException;
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
		preferences.open();
		preferences.select(runtimeDetectionPage);
		if(!runtimeDetectionPage.getAllPaths().contains(path)) {
			preferences.cancel();
			preferences.open();
			preferences.select(runtimeDetectionPage);
		}
		return runtimeDetectionPage.search();
	}

	protected SearchingForRuntimesDialog searchFirstPath(){
		runtimeDetectionPage = new RuntimeDetectionPreferencePage();
		preferences.open();
		preferences.select(runtimeDetectionPage);
		return runtimeDetectionPage.search();
	}

	protected void removeAllPaths(){
		preferences.open();
		preferences.select(runtimeDetectionPage);
		runtimeDetectionPage.removeAllPaths();
		preferences.ok();
	}

	protected void removeAllSeamRuntimes(){
		preferences.open();
		try{
			preferences.select(seamPreferencePage);
		}catch (CoreLayerException ex){
			//seam is not installed
			preferences.ok();
			return;
		}
		seamPreferencePage.removeAllRuntimes();
		preferences.ok();
	}

	protected void removeAllServerRuntimes(){
		preferences.open();
		preferences.select(runtimePreferencePage);
		runtimePreferencePage.removeAllRuntimes();
		preferences.ok();
	}

	protected void assertSeamRuntimesNumber(int expected) {
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

	protected void assertServerRuntimesNumber(int expected) {
		preferences.open();
		preferences.select(runtimePreferencePage);
		List<org.jboss.reddeer.eclipse.wst.server.ui.Runtime> runtimes = 
				runtimePreferencePage.getServerRuntimes();
		assertThat("Expected are " + expected + " runtimes but there are:\n"
				+ Arrays.toString(runtimes.toArray()), runtimes.size(), is(expected));
		preferences.ok();
	}
}
