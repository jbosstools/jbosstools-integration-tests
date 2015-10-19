package org.jboss.tools.openshift.ui.bot.test.util;

import java.io.File;

import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView.AuthenticationMethod;

/**
 * Storage for OpenShift v3 credentials.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class DatastoreOS3 {

	// used for basic authentization
	public static String SERVER = System.getProperty("openshift.server");
	public static String USERNAME = System.getProperty("openshift.username");
	public static String PASSWORD = System.getProperty("openshift.password");
	public static String TOKEN = System.getProperty("openshift.token");
	
	public static String PROJECT1 = "project01";
	public static String PROJECT1_DISPLAYED_NAME = "project01name";
	public static String PROJECT2 = "project02";
	
	public static AuthenticationMethod AUTH_METHOD;
	
	public static String TEMPLATE_PATH = new File("").getAbsolutePath() + File.separator + 
			"resources" + File.separator + "eap6-basic-s2i.json";
}
