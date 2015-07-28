package org.jboss.tools.batch.ui.bot.test;

import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	public static final String PLUGIN_ID = "org.jboss.tools.batch.ui.bot.test";
	
	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

	 public static String getPathToFileWithinPlugin(String fileName) {

		    String filePath = null;
		    try {
		      filePath = FileLocator.toFileURL(
		          Platform.getBundle(PLUGIN_ID).getEntry("/")).getFile() + fileName;
		    } catch (IOException e) {
		      throw new IllegalStateException("Cannot locate file " + fileName + " in plugin " + PLUGIN_ID);
		    }
		    return filePath;
		  }
}
