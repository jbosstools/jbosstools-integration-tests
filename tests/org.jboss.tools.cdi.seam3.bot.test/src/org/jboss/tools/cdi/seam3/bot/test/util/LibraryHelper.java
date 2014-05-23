/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.cdi.seam3.bot.test.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.*;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.jboss.tools.cdi.seam3.bot.test.Activator;
import org.jboss.tools.common.reddeer.label.IDELabel;

/**
 * 
 * @author jjankovi
 * 
 */
public class LibraryHelper {

	/**
	 * Method adds library named "libraryName" located in project folder to
	 * project's classpath
	 * 
	 * @param projectName
	 * @param libraryName
	 */
	public void addLibrariesToProjectsClassPath(String projectName,
			Collection<String> libraries) {
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).select();
		new ContextMenu(IDELabel.Menu.REFRESH).select();
		new WaitWhile(new JobIsRunning());
		new ContextMenu(IDELabel.Menu.PROPERTIES).select();
		new DefaultShell(IDELabel.Shell.PROPERTIES_FOR+" " +projectName);
		new DefaultTreeItem(IDELabel.JavaBuildPathPropertiesEditor.
				JAVA_BUILD_PATH_TREE_ITEM_LABEL).select();
		new DefaultTabItem(IDELabel.JavaBuildPathPropertiesEditor.
				LIBRARIES_TAB_LABEL).activate();

		Iterator<String> iter = libraries.iterator();
		while (iter.hasNext()) {
			String library = iter.next();
			new PushButton(CDIConstants.ADD_JARS).click();
			new DefaultShell(IDELabel.Shell.JAR_SELECTION);
			new DefaultTreeItem(projectName,library).select();
			
			new PushButton(IDELabel.Button.OK).click();
			new WaitWhile(new ShellWithTextIsAvailable(IDELabel.Shell.JAR_SELECTION));
		}
		new PushButton(IDELabel.Button.OK).click();
		new WaitWhile(new ShellWithTextIsAvailable(IDELabel.Shell.PROPERTIES_FOR+" " +projectName));
	}

	/**
	 * Method copies library named "libraryName" located in
	 * "projectName"/resources/libraries into project default folder
	 * 
	 * @param projectName
	 * @param libraryName
	 * @throws IOException
	 */
	public void addLibraryIntoProjectFolder(String projectName,
			String libraryName) throws IOException {
		File in = null;
		FileChannel inChannel = null;
		FileChannel outChannel = null;
		
		StringBuilder builder = new StringBuilder();
		String[] path = {"libraries",libraryName};
		for (String fragment : path) {
			builder.append("/" + fragment);
		}

		String filePath = "";
		try {
			filePath = FileLocator.toFileURL(
					Platform.getBundle(Activator.PLUGIN_ID).getEntry("/")).getFile()
					+ "resources" + builder.toString();
			File file = new File(filePath);
			if (!file.isFile()) {
				filePath = FileLocator.toFileURL(
						Platform.getBundle(Activator.PLUGIN_ID).getEntry("/")).getFile()
						+ builder.toString();
			}
		} catch (IOException ex) {
			String message = filePath + " resource file not found";
			//log.error(message);
			fail(message);
		}

		in = new File(filePath);

		File out = new File(Platform.getLocation() + File.separator
				+ projectName + File.separator + File.separator + libraryName);

		inChannel = new FileInputStream(in).getChannel();
		outChannel = new FileOutputStream(out).getChannel();

		inChannel.transferTo(0, inChannel.size(), outChannel);

		if (inChannel != null)
			inChannel.close();
		if (outChannel != null)
			outChannel.close();
	}

}
