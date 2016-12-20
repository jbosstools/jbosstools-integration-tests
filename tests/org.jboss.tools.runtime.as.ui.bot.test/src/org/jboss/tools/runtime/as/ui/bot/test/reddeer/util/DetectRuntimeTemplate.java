package org.jboss.tools.runtime.as.ui.bot.test.reddeer.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.core.Is;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.runtime.as.ui.bot.test.reddeer.Runtime;
import org.jboss.tools.runtime.as.ui.bot.test.reddeer.RuntimeMatcher;
import org.jboss.tools.runtime.as.ui.bot.test.reddeer.ui.SearchingForRuntimesDialog;

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
public class DetectRuntimeTemplate extends RuntimeDetectionUtility {
	
	public static void detectRuntime(String path, List<Runtime> expected) {
		assertTrue("Path " + path + " doesn't exists", new File(path).exists());
		SearchingForRuntimesDialog searchingForRuntimesDialog = addPath(path);
		
		List<Runtime> runtimes = searchingForRuntimesDialog.getRuntimes();
		
		searchingForRuntimesDialog.ok();
		runtimeDetectionPage.ok();
		
		assertCountOfRuntimes(searchingForRuntimesDialog, expected, runtimes, path);
		assertThatExpectedRuntimesArePresent(expected, runtimes);
	}

	
	public static void removePath(String requiredPath) {
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		preferenceDialog.select(runtimeDetectionPage);
						
		List<String> allPaths = runtimeDetectionPage.getAllPaths();
		assertTrue("Expected is presence of path " + requiredPath + " but there are:\n"
				+ Arrays.toString(allPaths.toArray()), allPaths.contains(requiredPath));
		
		runtimeDetectionPage.removeAllPaths();
		
		allPaths = runtimeDetectionPage.getAllPaths();
		
		runtimeDetectionPage.ok();
		
		assertThat("Not all paths were removed. There are " + Arrays.toString(allPaths.toArray()), allPaths.size(), Is.is(0));
	}

	
	private static void assertCountOfRuntimes(SearchingForRuntimesDialog dialog,
			List<Runtime> expected,
			List<Runtime> runtimes, String path) {
		int size = runtimes.size();
		int expectedSize = expected.size();
		if(size > 0) {
			assertThat("Expected " + expectedSize + " but there were " + size
					+ ":\nExpected runtimes: "+Arrays.toString(expected.toArray())
					+ "\nBut there were " + Arrays.toString(runtimes.toArray()), size, is(expectedSize));
		} else {
			dialog.cancel();
			fail("No runtime detected in folder: " + path);
		}
	}
	
	private static void assertThatExpectedRuntimesArePresent(List<Runtime> expected, List<Runtime> runtimes) {
		for (Runtime runtime : expected){
			assertThat(runtimes, hasItem(new RuntimeMatcher(runtime)));
		}
	}
}
