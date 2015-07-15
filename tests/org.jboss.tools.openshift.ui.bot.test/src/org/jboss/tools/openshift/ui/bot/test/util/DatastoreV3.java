package org.jboss.tools.openshift.ui.bot.test.util;

/**
 * Storage for OpenShift v3 credentials.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class DatastoreV3 {

	public static String OPENSHIFT_SERVER = System.getProperty("openshift.server");
	public static String OPENSHIFT_USER = System.getProperty("openshift.user");
	public static String OPENSHIFT_PASSWORD = System.getProperty("openshift.password");
	
	public static String OPENSHIFT_SERVER2 = System.getProperty("openshift.server2");
	public static String OPENSHIFT_USER2 = System.getProperty("openshift.user2");
	public static String OPENSHIFT_TOKEN2 = System.getProperty("openshift.token2");
	
	public static String PROJECT1 = "project01";
	public static String PROJECT2 = "project02";
}
