package org.jboss.tools.openshift.ui.utils;

import java.util.Random;

/**
 * 
 * Storage of user data provided.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class Datastore {

	public static String SERVER = System.getProperty("libra.server");
	public static String USERNAME = System.getProperty("user.name");
	public static String DOMAIN = "jbdstestdomain" + new Random().nextInt(100);
	public static String SECOND_DOMAIN = "secondjbdsqe" + new Random().nextInt(100);
	public static String X_DOMAIN = "xjbdsqedomain" + new Random().nextInt(100);

	public static String X_SERVER = "openshift.redhat.com";
	public static String X_USERNAME = "equo@mail.muni.cz";
	public static String X_PASSWORD = "rhqetestjbds19";
	
	public static String SSH_HOME;
	public static String SSH_KEY_NAME = "MyKey";
	public static String SSH_KEY_FILENAME = "OpShKey" + System.currentTimeMillis(); 
	
	private Datastore() {}
}
