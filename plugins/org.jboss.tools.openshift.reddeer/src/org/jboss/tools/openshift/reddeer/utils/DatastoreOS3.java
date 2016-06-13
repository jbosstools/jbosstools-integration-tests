/*******************************************************************************
 * Copyright (c) 2007-2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v 1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.reddeer.utils;

import java.io.File;

import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView.AuthenticationMethod;

/**
 * Storage for OpenShift v3 credentials.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class DatastoreOS3 {

	static {
		String password = System.getProperty("openshift.password");
		if (!(password == null || password.equals("") || password.startsWith("${"))) {
			PASSWORD = password;
		}
		
		String token = System.getProperty("openshift.token");
		if (!(token == null || token.equals("") || token.startsWith("${"))) {
			TOKEN = token;
		}
	}
	
	// used for basic authentization
	public static String SERVER = System.getProperty("openshift.server");
	public static String USERNAME = System.getProperty("openshift.username");
	public static String PASSWORD = System.getProperty("openshift.password");
	public static String TOKEN = System.getProperty("openshift.token");
	
	// github credentials
	public static String GIT_USERNAME = "openshift-tools-testing-account";
	public static String GIT_PASSWORD = System.getProperty("github.password");
	
	public static String PROJECT1 = "project-name01";
	public static String PROJECT1_DISPLAYED_NAME = "displayedName-01";
	public static String PROJECT2 = "project-name02";
	
	public static AuthenticationMethod AUTH_METHOD;
	
	public static String TEMPLATE_PATH = new File("").getAbsolutePath() + File.separator + 
			"resources" + File.separator + "eap64-basic-s2i.json";
	
	private static int seed = 0;
	
	/**
	 * Generates a new project name for DatastoreOS3.PROJECT1 variable.
	 */
	public static void generateProjectName() {
		PROJECT1 = "generated-name" + ++seed;
		PROJECT1_DISPLAYED_NAME = "displayedName-" + seed;
	}
}
