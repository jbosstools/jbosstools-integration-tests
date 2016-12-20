package org.jboss.tools.runtime.as.ui.bot.test;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jboss.tools.runtime.as.ui.bot.test";

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	
	

	public static IPath getStateFolder() {
		IPath stateLoc = Activator.getDefault().getStateLocation();
		IPath servers = stateLoc.append("servers");
		return servers;
	}

	public static IPath getDownloadPath(String runtimeString) {
		IPath servers = getStateFolder();
		servers.toFile().mkdirs();
		String serverFolder = runtimeString.replaceAll("[^A-Za-z0-9]", "");
		IPath serverFolderPath = servers.append(serverFolder);
		return serverFolderPath;
	}

	public static File getDownloadFolder(String runtimeString) {
		IPath p = getDownloadPath(runtimeString);
		p.toFile().mkdirs();
		return p.toFile();
	}

	
}
