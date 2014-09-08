package org.jboss.tools.runtime.as.ui.bot.test.template;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.core.Is;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.runtime.as.ui.bot.test.RuntimeProperties;
import org.jboss.tools.runtime.as.ui.bot.test.dialog.preferences.RuntimeDetectionPreferencePage;
import org.jboss.tools.runtime.as.ui.bot.test.dialog.preferences.SearchingForRuntimesDialog;
import org.jboss.tools.runtime.as.ui.bot.test.entity.Runtime;
import org.jboss.tools.runtime.as.ui.bot.test.matcher.RuntimeMatcher;
import org.jboss.tools.runtime.core.model.RuntimePath;
import org.jboss.tools.runtime.ui.RuntimeUIActivator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Common scenario for runtime detection tests.
 * 
 * It adds the runtime's installation folder to the runtime detection,
 * checks if it is correctly recognized and created and then remove
 * added runtime's installation folder.
 * 
 * 
 *   
 * @author Lucia Jelinkova
 * @author Petr Suchy
 * @author Radoslav Rabara
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//first detectRuntime and then removePath
public abstract class DetectRuntimeTemplate extends RuntimeDetectionTestCase {
	
	private SearchingForRuntimesDialog searchingForRuntimesDialog;

	protected abstract String getPathID();

	protected abstract List<Runtime> getExpectedRuntimes();

	@Test
	public void detectRuntime(){
		String path = RuntimeProperties.getInstance().getRuntimePath(getPathID());
		assertTrue("Path doesn't exists", new File(path).exists());
		searchingForRuntimesDialog = addPath(path);
		
		List<Runtime> runtimes = searchingForRuntimesDialog.getRuntimes();
		
		searchingForRuntimesDialog.ok();
		runtimeDetectionPage.ok();
		
		assertCountOfRuntimes(runtimes, path);
		assertThatExpectedRuntimesArePresent(runtimes);
	}

	@Test
	public void removePath() {
		runtimeDetectionPage.open();
				
		List<String> allPaths = runtimeDetectionPage.getAllPaths();
		String requiredPath = RuntimeProperties.getInstance().getRuntimePath(getPathID());
		assertTrue("Expected is presence of path " + requiredPath + " but there are:\n"
				+ Arrays.toString(allPaths.toArray()), allPaths.contains(requiredPath));
		
		runtimeDetectionPage.removeAllPaths();
		
		allPaths = runtimeDetectionPage.getAllPaths();
		
		runtimeDetectionPage.ok();
		
		assertThat("Not all paths were removed. There are " + Arrays.toString(allPaths.toArray()), allPaths.size(), Is.is(0));
	}
	
	@After
	public void closeShells(){
		//close opened shells
		String[] openedShells = new String[]{
				SearchingForRuntimesDialog.DIALOG_TITLE,
				RuntimeDetectionPreferencePage.DIALOG_TITLE};
		for(String title : openedShells) {
			if(new ShellWithTextIsAvailable(title).test()) {
				new DefaultShell(title);
				new PushButton("Cancel").click();
			}
		}
	}
	
	@AfterClass
	public static void cleanPaths() {
		// make sure that paths were removed
		for (RuntimePath path : RuntimeUIActivator.getDefault().getModel().getRuntimePaths()){
			RuntimeUIActivator.getDefault().getModel().removeRuntimePath(path);
		}
	}
	
	private void assertCountOfRuntimes(List<Runtime> runtimes, String path) {
		int size = runtimes.size();
		int expectedSize = getExpectedRuntimes().size();
		if(size > 0) {
			assertThat("Expected " + expectedSize + " but there were " + size
					+ ":\nExpected runtimes: "+Arrays.toString(getExpectedRuntimes().toArray())
					+ "\nBut there were " + Arrays.toString(runtimes.toArray()), size, is(expectedSize));
		} else {
			searchingForRuntimesDialog.cancel();
			fail("No runtime detected in folder: " + path);
		}
	}
	
	private void assertThatExpectedRuntimesArePresent(List<Runtime> runtimes) {
		for (Runtime runtime : getExpectedRuntimes()){
			assertThat(runtimes, hasItem(new RuntimeMatcher(runtime)));
		}
	}
}
