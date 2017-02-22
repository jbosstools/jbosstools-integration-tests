/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.hibernate.ui.bot.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.jboss.reddeer.common.platform.OS;
import org.jboss.reddeer.common.platform.RunningPlatform;

/**
 * Utility class for manipulating with test database
 *
 */
public class DatabaseUtils {

	private static final Logger LOG = Logger.getLogger(DatabaseUtils.class.getName());
	private static Process sakila;

	/**
	 * Start sakila db located in a set folder
	 * 
	 * @param dbFolder
	 *            folder the database is located in
	 */
	public static void runSakilaDB(String dbFolder) {
		if (sakila == null) {
			try {
				String path = new File(dbFolder).getCanonicalPath();

				ProcessBuilder pb = new ProcessBuilder("java", "-cp", "h2-1.3.161.jar", "org.h2.tools.Server",
						"-ifExists", "-tcp", "-web");
				pb.directory(new File(path));
				sakila = pb.start();
				LOG.info("Starting h2 server - jdbc url: jdbc:h2:tcp://localhost/sakila");

				// check if the process really started
				long startTime = System.currentTimeMillis();
				boolean isAlive = false;
				while (System.currentTimeMillis() - startTime < 10000) {
					if (isRunning(sakila)) {
						isAlive = true;
						break;
					}
					Thread.sleep(200);
				}
				if (!isAlive) {
					throw new RuntimeException("Sakila Database startup failed");
				}

				LOG.info("Sakila DB started");
			} catch (IOException | InterruptedException e) {
				LOG.warning("An error occured starting sakila DB");
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Stop the sakila database instance if it exists
	 */
	public static void stopSakilaDB() {
		// destroy the process itself
		if (sakila != null) {
			sakila.destroy();
			sakila = null;

			try {
				List<String> processes = new ArrayList<String>();
				Process p = Runtime.getRuntime().exec("jps -l");
				p.waitFor();
				BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

				String line;
				while ((line = input.readLine()) != null) {
					processes.add(line);
				}
				input.close();

				// and kill the java process it created
				for (String process : processes) {
					if (process.contains("org.h2.tools.Server")) {
						int pid = Integer.parseInt(process.split(" ")[0]);
						Process proc = null;
						if (RunningPlatform.isOperationSystem(OS.WINDOWS)) {
							proc = Runtime.getRuntime().exec("taskkill /f /pid " + pid);
						} else {
							proc = Runtime.getRuntime().exec("kill -9 " + pid);
						}
						proc.waitFor();
						break;
					}
				}
			} catch (IOException | InterruptedException e) {
				LOG.warning("An error occured stopping Sakila DB");
				throw new RuntimeException(e);
			}
		} else {
			LOG.warning("Sakila DB is not running");
		}
	}

	private static boolean isRunning(Process process) {
		try {
			process.exitValue();
			return false;
		} catch (IllegalThreadStateException e) {
			return true;
		}
	}
}