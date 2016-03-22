/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/


package org.jboss.tools.examples.ui.bot.test.integration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class gathers info of all errors and warnings for all quickstarts.
 * Also it is able to report it to system output or write them to files.
 * 
 * @author rhopp
 *
 */

public class QuickstartsReporter {
	private static Map<Quickstart, List<String>> errors;
	private static Map<Quickstart, List<String>> warnings;
	private static List<Quickstart> qstarts;
	private static QuickstartsReporter instance = new QuickstartsReporter();

	protected QuickstartsReporter() {
		errors = new HashMap<Quickstart, List<String>>();
		warnings = new HashMap<Quickstart, List<String>>();
		qstarts = new ArrayList<Quickstart>();
	}

	public static QuickstartsReporter getInstance() {
		return instance;
	}

	public void addError(Quickstart q, String error) {
		if (!qstarts.contains(q)) {
			qstarts.add(q);
		}
		if (!errors.containsKey(q)) {
			errors.put(q, new ArrayList<String>());
		}
		errors.get(q).add(error);
	}

	public void addWarning(Quickstart q, String warning) {
		if (!qstarts.contains(q)) {
			qstarts.add(q);
		}
		if (!warnings.containsKey(q)) {
			warnings.put(q, new ArrayList<String>());
		}
		warnings.get(q).add(warning);
	}

	public void generateReport() {
		for (Quickstart quickstart : qstarts) {
			System.out.println("QUICKSTART: " + quickstart.getName());
			if (errors.containsKey(quickstart)) {
				System.out.println("\tERRORS:");

				for (String error : errors.get(quickstart)) {
					System.out.println("\t\t" + error);
				}
			}
			if (warnings.containsKey(quickstart)) {
				System.out.println("\tWARNINGS:");

				for (String warning : warnings.get(quickstart)) {
					System.out.println("\t\t" + warning);
				}
			}
		}
	}
	
	public void generateAllErrorsFile(File file){
		PrintWriter writer = null;
		try {
			 writer = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		for (Quickstart q : qstarts) {
			if (errors.containsKey(q)) {
				writer.println("Errors in quickstart \"" + q.getName()+"\"");
				for (String error : errors.get(q)) {
					writer.println("\t" + error);
				}
				writer.println();
			}
		}
		writer.close();
	}
	
	public void generateErrorFilesForEachProject(File directory){
		if (!directory.exists()){
			directory.mkdir();
		}
		if (!directory.isDirectory()){
			throw new IllegalArgumentException();
		}
		for (Quickstart q : qstarts){
			if (errors.containsKey(q)){
				PrintWriter writer = null;
				try {
					writer = new PrintWriter(directory.getAbsolutePath()+"/"+q.getName()+".txt");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				for (String error : errors.get(q)){
					writer.println(error);
				}
				writer.close();
			}
		}
	}
}