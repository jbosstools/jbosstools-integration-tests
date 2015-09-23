package org.jboss.tools.openshift.ui.bot.test.util;

/**
 * Storage for OpenShift v3 credentials.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class DatastoreV3 {

	// used for basic authentization
	public static String OPENSHIFT_SERVER = System.getProperty("openshift.server");
	public static String OPENSHIFT_USERNAME = System.getProperty("openshift.username");
	public static String OPENSHIFT_PASSWORD = System.getProperty("openshift.password");
	
	// used for OAuth authentization
	public static String OPENSHIFT_SERVER2 = System.getProperty("openshift.server2");
	public static String OPENSHIFT_USERNAME2 = System.getProperty("openshift.username2");
	public static String OPENSHIFT_TOKEN2 = System.getProperty("openshift.token2");
	
	public static String PROJECT1 = "project01";
	public static String PROJECT2 = "project02";
}
