package org.jboss.tools.runtime.as.ui.bot.test.template;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.Is.is;

import java.util.List;

import org.jboss.tools.runtime.as.ui.bot.test.RuntimeProperties;
import org.jboss.tools.runtime.as.ui.bot.test.dialog.preferences.SearchingForRuntimesDialog;
import org.jboss.tools.runtime.as.ui.bot.test.entity.Runtime;
import org.jboss.tools.runtime.as.ui.bot.test.matcher.RuntimeMatcher;
import org.jboss.tools.runtime.core.model.RuntimePath;
import org.jboss.tools.runtime.ui.RuntimeUIActivator;
import org.junit.After;
import org.junit.Test;

/**
 * Common scenario for runtime detection tests. It adds the runtime's installation
 * folder to the runtime detection and checks if it is correctly recognized and created. 
 *   
 * @author Lucia Jelinkova
 * @author Petr Suchy
 *
 */
public abstract class DetectRuntimeTemplate extends RuntimeDetectionTestCase {

	private SearchingForRuntimesDialog searchingForRuntimesDialog;

	protected abstract String getPathID();

	protected abstract List<Runtime> getExpectedRuntimes();

	@Test
	public void detectRuntime(){
		String path = RuntimeProperties.getInstance().getRuntimePath(getPathID());
		searchingForRuntimesDialog = addPath(path);

		List<Runtime> runtimes = searchingForRuntimesDialog.getRuntimes(); 
		int size = runtimes.size();
		
		if(size > 0){
			assertThat(runtimes.size(), is(getExpectedRuntimes().size()));
		}else{
			searchingForRuntimesDialog.cancel();
			throw new AssertionError("No runtime detected in folder: " + path);
		}

		for (Runtime runtime : getExpectedRuntimes()){
			assertThat(runtimes, hasItem(new RuntimeMatcher(runtime)));			
		}
	}

	@After
	public void closePreferences(){
		searchingForRuntimesDialog.ok();
//		runtimeDetectionPreferences.removeAllPaths();
		runtimeDetectionPreferences.ok();

		// this call to API is due to wrongly functioning of Remove button. Once it is fixed
		// it should be changed back to removing via SWTBot
		for (RuntimePath path : RuntimeUIActivator.getDefault().getModel().getRuntimePaths()){
			RuntimeUIActivator.getDefault().getModel().removeRuntimePath(path);
		}
	}
}
