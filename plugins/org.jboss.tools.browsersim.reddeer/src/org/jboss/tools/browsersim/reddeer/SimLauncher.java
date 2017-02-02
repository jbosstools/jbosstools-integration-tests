/******************************************************************************* 
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.browsersim.reddeer;

import java.io.File;
import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.pde.internal.core.exports.PluginExportOperation;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.eclipse.pde.internal.core.exports.FeatureExportInfo;
import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.swt.api.ToolItem;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.browsersim.eclipse.launcher.BrowserSimLauncher;
import org.jboss.tools.browsersim.eclipse.launcher.ExternalProcessLauncher;
import org.jboss.tools.browsersim.reddeer.condition.LaunchExists;
import org.jboss.tools.browsersim.rmi.BrowsersimUtil;
import org.jboss.tools.browsersim.rmi.IBrowsersimHandler;
import org.jboss.tools.browsersim.rmi.MySecurityManager;
import org.osgi.framework.Bundle;

public class SimLauncher {
	
	protected static Registry registry;
	private static final String HAMCREST_BUNDLE = "org.hamcrest.core";
	private static ILaunch simLaunch;
	
	protected void stopRMIRegistry() {
		try {
			UnicastRemoteObject.unexportObject(registry, true);
		} catch (NoSuchObjectException e) {
			e.printStackTrace();
		}
	}

	protected void startRMIRegistry(String rmiCodebase) {
		try {
			if(System.getSecurityManager() == null){
				System.setSecurityManager(new MySecurityManager());
			}
			if(rmiCodebase != null){
				System.setProperty("java.rmi.server.codebase",rmiCodebase);
			}
			registry = LocateRegistry.createRegistry(1099);
		} catch (RemoteException ex) {
			try {
				registry = LocateRegistry.getRegistry(1099);
				IBrowsersimHandler bsHandler = (IBrowsersimHandler) registry.lookup(BrowsersimUtil.BS_HANDLER);
				UnicastRemoteObject.unexportObject(bsHandler, true);
				registry.unbind(BrowsersimUtil.BS_HANDLER);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	protected ILaunchConfigurationWorkingCopy getSimLaunchConfig(ContextMenu menu, ToolItem item) {
		BrowserSimLaunchListener launchListener = new BrowserSimLaunchListener();
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		manager.addLaunchListener(launchListener);
		if (menu != null) {
			menu.select();
		} else {
			item.click();
		}

		new WaitUntil(new LaunchExists(launchListener));
		IProcess[] processes = launchListener.getBrowserSimLaunch().getProcesses();
		try {
			processes[0].terminate();
		} catch (DebugException e1) {
			throw new BrowserSimException("Unable to terminate *Sim", e1);
		}
		launchListener.getBrowserSimLaunch().removeProcess(processes[0]);
		try {
			return launchListener.getBrowserSimLaunch().getLaunchConfiguration().getWorkingCopy();
		} catch (CoreException e) {
			throw new BrowserSimException("Unable to obtain *Sim launch configuration", e);
		}
	}
	
	protected void launchSimWithRMI(List<Bundle> additionalBundles, String mainClass,
			ContextMenu menu, ToolItem item) {
		ILaunchConfigurationWorkingCopy wc = getSimLaunchConfig(menu, item);
		try {
			List<String> classPath = wc.getAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, new ArrayList<String>());
			Bundle hamcrest = Platform.getBundle(HAMCREST_BUNDLE);
			String hamcrestLocation = FileLocator.getBundleFile(hamcrest).getCanonicalPath();
			List<String> mem = ExternalProcessLauncher.getClassPathMementos(hamcrestLocation);
			deleteDir(new File("target/bundleBuild"));
			
			List<String> additionalBundlesFinalLoc = exportAndGetBundlePath(additionalBundles);
			for(String additionalBundle: additionalBundlesFinalLoc){
				mem.addAll(ExternalProcessLauncher.getClassPathMementos(additionalBundle));
			}
			
			//temp for testing changes in cordovasim plugins
			List<String> cpTemp = new ArrayList<>(classPath);
			classPath = new ArrayList<>();
			for(String cp: cpTemp){
				String[] a = cp.split("externalArchive");
				String[] b = a[1].split("path");
				String[] c = b[0].split("=");
				String[] d = c[1].split("\"");
				String archivePath = cp.split("externalArchive")[1].split("path")[0].split("=")[1].split("\"")[1];
				String archiveName;
				if(archivePath.endsWith("bin")){
					archiveName = archivePath.split("_")[0].split("/")[archivePath.split("_")[0].split("/").length-2];
				} else {
					archiveName = archivePath.split("_")[0].split("/")[archivePath.split("_")[0].split("/").length-1];
				}
				;
				String exportedBundlePath = exportAndGetBundlePath(archivePath,archiveName);
				if(exportedBundlePath == null){
					classPath.add(cp);
				} else{
					classPath.addAll(ExternalProcessLauncher.getClassPathMementos(exportedBundlePath));
				}
				
			}
			///
			
			classPath.addAll(mem);
			wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, classPath);
			wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
					mainClass);
			List<String> bsBundles = BrowserSimLauncher.getBundles();
			bsBundles.addAll(BrowserSimLauncher.getJettyBundles());
			
			StringBuilder allBundlesBuilder = new StringBuilder();
			for(String bsBundle: bsBundles){
				Bundle b = Platform.getBundle(bsBundle);
				allBundlesBuilder.append("file://"+FileLocator.getBundleFile(b).getCanonicalPath()+" ");
			}
			for(String additionalFinal: additionalBundlesFinalLoc){
				allBundlesBuilder.append("file://"+additionalFinal+" ");
			}
			startRMIRegistry(allBundlesBuilder.toString());
			simLaunch = wc.launch(ILaunchManager.RUN_MODE, null);
		} catch (CoreException | IOException e) {
			throw new BrowserSimException("Unable to start *Sim", e);
		}
	}
	
	private List<String> exportAndGetBundlePath(List<Bundle> bundles) throws IOException{
		List<String> bundlesFileLocation = new ArrayList<>();
		for(Bundle additionalBundle: bundles){
			String aBundleLocation = FileLocator.getBundleFile(additionalBundle).getCanonicalPath();
			//check if bundle is folder or jar
			if(!aBundleLocation.endsWith("jar")){
				//export folder to jar
				String bundleLoc = export(additionalBundle);
				File bundleLocFile = new File(bundleLoc);
				new WaitUntil(new BundleIsExported(bundleLocFile,additionalBundle.getSymbolicName()), TimePeriod.LONG);
				for(File f: bundleLocFile.listFiles()){
					if(f.getName().contains(additionalBundle.getSymbolicName())){
						aBundleLocation = f.getAbsolutePath();
					}
				}
			}
			bundlesFileLocation.add(aBundleLocation);
		}
		return bundlesFileLocation;
	}
	
	private String exportAndGetBundlePath(String bundlePath, String name) throws IOException{
		//check if bundle is folder or jar
		if(!bundlePath.endsWith("jar")){
			//export folder to jar
			String bundleLoc = export(name);
			File bundleLocFile = new File(bundleLoc);
			new WaitUntil(new BundleIsExported(bundleLocFile,name), TimePeriod.LONG);
			for(File f: bundleLocFile.listFiles()){
				if(f.getName().contains(name)){
					return f.getAbsolutePath();
				}
			}
		}
		return null;
	}
	
	private String export(String bundleName){
		File bundleBuildDir = new File("target/bundleBuild");
		
		IPluginModelBase pb = PluginRegistry.findModel(bundleName);
		FeatureExportInfo info = new FeatureExportInfo();
		info.toDirectory = true;
		info.useJarFormat = true;
		info.exportSource = false;
		info.exportSourceBundle = true;
		info.allowBinaryCycles = true;
		info.useWorkspaceCompiledClasses = false;
		String bungleTargetLoc = bundleBuildDir.getAbsolutePath();
		info.destinationDirectory = bungleTargetLoc;
		info.zipFileName = null;
		info.items = new Object[] {pb};
		info.signingInfo = null;
		info.qualifier = null;
		PluginExportOperation peo = new PluginExportOperation(info, bundleName);
		peo.schedule();
		try {
			peo.join(); //wait for job to finish
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bungleTargetLoc+"/plugins/";
	}
	
	protected String export(Bundle bundle){
		File bundleBuildDir = new File("target/bundleBuild");
		
		IPluginModelBase pb = PluginRegistry.findModel(bundle.getSymbolicName());
		FeatureExportInfo info = new FeatureExportInfo();
		info.toDirectory = true;
		info.useJarFormat = true;
		info.exportSource = false;
		info.exportSourceBundle = true;
		info.allowBinaryCycles = true;
		info.useWorkspaceCompiledClasses = false;
		String bungleTargetLoc = bundleBuildDir.getAbsolutePath();
		info.destinationDirectory = bungleTargetLoc;
		info.zipFileName = null;
		info.items = new Object[] {pb};
		info.signingInfo = null;
		info.qualifier = null;
		PluginExportOperation peo = new PluginExportOperation(info, bundle.getSymbolicName());
		peo.schedule();
		try {
			peo.join(); //wait for job to finish
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bungleTargetLoc+"/plugins/";
	}
	
	
	public static boolean deleteDir(File dir) {
	    if (dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i = 0; i < children.length; i++) {
	            boolean success = deleteDir(new File(dir, children[i]));
	            if (!success) {
	                return false;
	            }
	        }
	    }

	    return dir.delete(); // The directory is empty now and can be deleted.
	}
	
	protected static void stopSim() {
		if (simLaunch == null) {
			throw new BrowserSimException("*Sim launch does not exist");
		}
		try {
			if(simLaunch.getProcesses()[0].isTerminated()){
				throw new BrowserSimException("*Sim process is already terminated");
			}
			
			simLaunch.getProcesses()[0].terminate();
			simLaunch.terminate();
			simLaunch = null;
		} catch (DebugException e) {
			throw new BrowserSimException("Unable to terminate *Sim process",e);
		}
	}
	
	private class BundleIsExported extends AbstractWaitCondition {
		
		private File bundleFolder;
		private String bundleName;
		
		public BundleIsExported(File bundleFolder,String bundleName) {
			this.bundleFolder = bundleFolder;
			this.bundleName = bundleName;
		}

		@Override
		public boolean test() {
			if(bundleFolder.exists()){
				for(File f: bundleFolder.listFiles()){
					if(f.getName().contains(bundleName)){
						return true;
					}
				}
			}
			return false;
		}
		
		@Override
		public String description() {
			return bundleName +" is exported";
		}
	}
	

}
