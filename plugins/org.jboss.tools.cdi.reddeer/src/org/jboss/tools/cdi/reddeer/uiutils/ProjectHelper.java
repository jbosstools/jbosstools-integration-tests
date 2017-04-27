package org.jboss.tools.cdi.reddeer.uiutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.jboss.reddeer.swt.condition.ControlIsEnabled;
import org.jboss.reddeer.swt.condition.ShellIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.core.condition.JobIsRunning;

public class ProjectHelper {
	
	public void addLibrariesIntoProject(String projectName, String libDir) {

		File[] libraries = addLibraryIntoProjectFolder(projectName, new File(libDir));
		if (libraries == null)
			return;
		/** refresh the workspace **/

		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(projectName).select();
		new ContextMenu("Refresh").select();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);

		new ContextMenu("Properties").select();
		new DefaultShell("Properties for "+projectName);
		new DefaultTreeItem("Java Build Path").select();
		new DefaultTabItem("Libraries").activate();

		for (File library : libraries) {
			new PushButton("Add JARs...").click();
			new DefaultShell("JAR Selection");
			new DefaultTreeItem(projectName, library.getName()).select();
			new WaitUntil(new ControlIsEnabled(new PushButton("OK")));
			new PushButton("OK").click();
			new WaitWhile(new ShellIsAvailable("JAR Selection"));
		}
		new PushButton("OK").click();
		new WaitWhile(new ShellIsAvailable("Properties for "+projectName));
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);

	}

	private File[] addLibraryIntoProjectFolder(String projectName,
			File librariesFolder) {
		FileChannel inChannel = null;
		FileChannel outChannel = null;

		List<File> libraryFiles = new ArrayList<File>();
		FileInputStream istream = null;
		FileOutputStream ostream = null;
		try {
			for (File in : librariesFolder.listFiles()) {
				if (in.isDirectory() || 
					!in.getName().substring(in.getName().lastIndexOf(".") + 1).equals("jar")) {
					continue;
				}
				File out = new File(Platform.getLocation() + File.separator
						+ projectName + File.separator + File.separator
						+ in.getName());

				istream = new FileInputStream(in);
				ostream = new FileOutputStream(out);
				
				inChannel = istream.getChannel();
				outChannel = ostream.getChannel();

				inChannel.transferTo(0, inChannel.size(), outChannel);
				libraryFiles.add(in);
			}
		} catch (IOException ioException) {

		} finally {
			try{
				if (istream != null){
					istream.close();
				}
				if (ostream != null){
					ostream.close();
				}
			} catch (IOException ex){
				
			}
		}
		return libraryFiles.toArray(new File[libraryFiles.size()]);
	}
	
	public boolean projectExists(String projectName){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		return pe.containsProject(projectName);
	}

}
