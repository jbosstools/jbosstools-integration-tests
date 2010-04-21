package org.jboss.tools.ui.bot.ext.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.jboss.tools.ui.bot.ext.Activator;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.*;
import org.jboss.tools.ui.bot.ext.config.requirement.AddSeam;
import org.jboss.tools.ui.bot.ext.config.requirement.RequirementBase;
import org.jboss.tools.ui.bot.ext.config.requirement.StartServer;

public class TestConfigurator {
	
	
	public class Keys {
		public static final String SERVER = "SERVER";
		public static final String SEAM = "SEAM";
		public static final String JAVA_HOME_15 = "JAVA_HOME_15";
		public static final String JAVA_HOME_16 = "JAVA_HOME_16";
	}

	public class Values {
		public static final String SERVER_TYPE_EAP = "EAP";
		public static final String SERVER_TYPE_JBOSSAS = "JBOSS_AS";
		public static final String SERVER_WITH_DEFAULT_JAVA = "default";
	}

	public static final String SWTBOT_TEST_PROPERTIES_FILE = "swtbot.test.properties.file";
	private static Properties swtTestProperties = new Properties();
	public static ServerBean server;
	public static SeamBean seam;
	static {
		try {
			// try to load from file first
			String propFile = System.getProperty(SWTBOT_TEST_PROPERTIES_FILE,
					null);
			if (propFile != null && new File(propFile).exists()) {
				try {
					swtTestProperties.load(new FileInputStream(propFile));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					swtTestProperties.load(new FileInputStream(SWTTestExt.util
							.getResourceFile(Activator.PLUGIN_ID,
									"/SWTBotTest-default.properties")));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		//properties got loaded
		try {
			server = ServerBean.fromString(getProperty(Keys.SERVER));
			seam = SeamBean.fromString(getProperty(Keys.SEAM));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * returns null when given Server annotation does not match global test configuration 
	 * (e.g. Test wants Server type EAP but we are running on JbossAS)
	 * @param s Server annotation
	 * @return StartServer requirement otherwise
	 */
	private static RequirementBase getServerRequirement(Server s) {
		if (!s.required()) {
			return null;
		}
		if (!s.type().equals(ServerType.ALL)) {
			if (s.type().equals(ServerType.EAP) && !server.type.equals(ServerBean.ServerType.EAP)) {
				return null;
			}
			if (s.type().equals(ServerType.JbossAS) && !server.type.equals(ServerBean.ServerType.JBOSS_AS)) {
				return null;
			}
		}
		
		if (!matches(server.version, s.operator(), s.version())) {
			return null;
		}
		return new StartServer();
	}
	/**
	 * returns null when given Seam annotation does not match global test configuration 
	 * (e.g. Test wants Seam version 2.2 but we are running on 1.2)
	 * @param s
	 * @return AddSeam requirement otherwise
	 */
	private static RequirementBase getSeamRequirement(Seam s) {
		if (!s.required()) {
			return null;
		}
		if (!matches(seam.version, s.operator(), s.version())) {
			return null;
		}
		return new AddSeam();		
	}
	/**
	 * returns true, if given class (Test) can run, this is done by exploring class'es
	 * annotations (see {@link SWTBotTestRequires}
	 */
	public static boolean canRunClass(Class<?> klass) {
		SWTBotTestRequires requies = klass.getAnnotation(SWTBotTestRequires.class);	
		// all not annotated classes can run
		if (requies==null) { 
			return true; 
			}
		if (requies.server().required()) {
			RequirementBase req = getServerRequirement(requies.server());
			if (req==null) {
				return false;
			}
			SWTTestExt.beforeRequirements.add(req);
		}
		if (requies.seam().required()) {
			RequirementBase req = getSeamRequirement(requies.seam());
			if (req==null) {
				return false;
			}
			SWTTestExt.beforeRequirements.add(req);
		}
		return true;
	}
	/**
	 * implements comparison of 2 params by given operator (in this order)
	 * params are expected version strings (in form X.X or XX) 
	 * if param1 or param2 is '*' true is returned
	 * @param param1
	 * @param operator (=,<,>=<=,>=,!=)
	 * @param param2
	 * 
	 * @return
	 */
	public static boolean matches(String param1,String operator,String param2) {
		if ("*".equals(param1)||"*".equals(param2)) {
			return true;
		}
		if ("=".equals(operator)) {
			return param1.equals(param2);			
		}
		if ("!=".equals(operator)) {
			return !param1.equals(param2);			
		}		
		int ver1 = versionToNumber(param1);
		int ver2 = versionToNumber(param2);
		if (">".equals(operator)) {
			return ver1 > ver2;
		}
		if (">=".equals(operator)) {
			return ver1 >= ver2;
		}
		if ("<".equals(operator)) {
			return ver1 < ver2;
		}
		if ("<=".equals(operator)) {
			return ver1 <= ver2;
		}
		return false;
	}
	private static int versionToNumber(String version) {
		return Integer.parseInt(version.replaceAll("\\.", ""));
	}
	public static String getProperty(String key) {
		return swtTestProperties.getProperty(key);
		//return SWTTestExt.util.getValue(swtTestProperties, key);
	}
}
