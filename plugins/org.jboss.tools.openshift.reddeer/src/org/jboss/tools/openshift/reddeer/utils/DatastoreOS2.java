package org.jboss.tools.openshift.reddeer.utils;

import java.util.Random;

/**
 * 
 * Storage of credentials and user data.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class DatastoreOS2 {

	public static String SERVER = System.getProperty("openshift.server");
	public static String USERNAME = System.getProperty("openshift.username");
	public static String DOMAIN = "jbdstestdomain" + new Random().nextInt(100);
	public static String SECOND_DOMAIN = "secondjbdsqe" + new Random().nextInt(100);

	public static String X_SERVER = "https://openshift.redhat.com";
	public static String X_USERNAME = "equo@mail.muni.cz";
	public static String X_DOMAIN = "xjbdsqedomain" + new Random().nextInt(100);
	public static String X_PASSWORD = "rhqetestjbds19";
	
	public static String SSH_HOME;
	public static String SSH_KEY_NAME = "MyKey";
	public static String SSH_KEY_FILENAME = "OpShKey" + System.currentTimeMillis(); 
	
	static {
		String server = System.getProperty("openshift.server");
		String xServer = System.getProperty("openshift.xserver");
		if (server != null && !server.isEmpty()) {
			SERVER = server;
		} else if (xServer != null && !xServer.isEmpty()) {
			SERVER = xServer;
		}
	}
	
	private DatastoreOS2() {}
}
