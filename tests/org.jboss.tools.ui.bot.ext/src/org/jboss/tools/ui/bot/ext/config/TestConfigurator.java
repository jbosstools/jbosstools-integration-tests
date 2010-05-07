package org.jboss.tools.ui.bot.ext.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jboss.tools.ui.bot.ext.Activator;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.ESB;
import org.jboss.tools.ui.bot.ext.config.Annotations.SWTBotTestRequires;
import org.jboss.tools.ui.bot.ext.config.Annotations.Seam;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.jboss.tools.ui.bot.ext.config.requirement.RequirementBase;

public class TestConfigurator {
	private static final Logger log = Logger
	.getLogger(TestConfigurator.class);
	public class Keys {
		public static final String SERVER = "SERVER";
		public static final String SEAM = "SEAM";
		public static final String JAVA_HOME_15 = "JAVA_HOME_15";
		public static final String JAVA_HOME_16 = "JAVA_HOME_16";
		public static final String ESB = "ESB";
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
	public static ESBBean esb;
	static {
		try {
			// try to load from file first
			String propFile = System.getProperty(SWTBOT_TEST_PROPERTIES_FILE,
					null);
			if (propFile != null) {
				try {
					if (new File(propFile).exists()) {
						log.info("Loading exeternaly provided configuration file '"+propFile+"'");
					swtTestProperties.load(new FileInputStream(propFile));
					} else {
						throw new IOException(SWTBOT_TEST_PROPERTIES_FILE+" "+propFile+" does not exist!");
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					log.info("Loading default configuration");
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

		// properties got loaded
		try {
			server = ServerBean.fromString(getProperty(Keys.SERVER));
			seam = SeamBean.fromString(getProperty(Keys.SEAM));
			esb = ESBBean.fromString(getProperty(Keys.ESB));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
/**
 * check config values if they seem to be valid (existing dirs)
 * @throws FileNotFoundException 
 */
	public static boolean checkConfig()  {
		try {
		checkDirExists(getProperty(Keys.JAVA_HOME_15));
		checkDirExists(getProperty(Keys.JAVA_HOME_16));
		checkDirExists(seam.seamHome);
		checkDirExists(server.runtimeHome);
		checkDirExists(esb.esbHome);
		return true;
		}
		catch (Exception ex) {
			log.error("'"+ex.getMessage()+ "' - incorrect configuration, set your custom properties file via '"+SWTBOT_TEST_PROPERTIES_FILE+"' java property");
			return false;
		}
		
	}
	private static void checkDirExists(String dir) throws FileNotFoundException {
		if (!new File(dir).exists() || !new File(dir).isDirectory()) {
			throw new FileNotFoundException("File '"+dir+"' does not exist or is not directory");
		}
	}
	/**
	 * returns null when given Server annotation does not match global test
	 * configuration (e.g. Test wants Server type EAP but we are running on
	 * JbossAS)
	 * 
	 * @param s
	 *            Server annotation
	 * @return StartServer requirement otherwise
	 */
	private static RequirementBase getServerRequirement(Server s) {
		if (!s.required()) {
			return null;
		}				
		if (!s.type().equals(ServerType.ALL)) {
			if (s.type().equals(ServerType.EAP)
					&& !server.type.equals(ServerBean.ServerType.EAP)) {
				return null;
			}
			if (s.type().equals(ServerType.JbossAS)
					&& !server.type.equals(ServerBean.ServerType.JBOSS_AS)) {
				return null;
			}
		}
		if (!matches(server.version, s.operator(), s.version())) {
			return null;
		}
		if (ServerState.Disabled.equals(s.state())) {
			RequirementBase removeServer = RequirementBase.createRemoveServer();
			removeServer.getDependsOn().add(RequirementBase.createStopServer());
			return removeServer;
		}
		if (ServerState.NotRunning.equals(s.state())) {
			RequirementBase stopServer = RequirementBase.createStopServer();
			stopServer.getDependsOn().add(RequirementBase.createAddServer());
			return stopServer;
		}
		return RequirementBase.createStartServer();
	}

	/**
	 * returns null when given Seam annotation does not match global test
	 * configuration (e.g. Test wants Seam version 2.2 but we are running on
	 * 1.2)
	 * 
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
		return RequirementBase.createAddSeam();
	}
	private static RequirementBase getESBRequirement(ESB e) {
		if (!e.required()) {
			return null;
		}
		if (!matches(esb.version, e.operator(), e.version())) {
			return null;
		}
		return RequirementBase.createAddESB();
	}

	/**
	 * returns list of requirements if given class (Test) can run, all this is
	 * done by exploring class'es annotations (see {@link SWTBotTestRequires} if
	 * class cannot run returns null
	 */
	public static List<RequirementBase> getClassRequirements(Class<?> klass) {

		SWTBotTestRequires requies = klass
				.getAnnotation(SWTBotTestRequires.class);
		// internal list
		List<RequirementBase> reqs = new ArrayList<RequirementBase>();
		reqs.add(RequirementBase.createPrepareViews());
		// all not annotated classes can run
		if (requies == null) {
			return reqs;
		}
		if (requies.server().required()) {
			RequirementBase req = getServerRequirement(requies.server());
			if (req == null) {
				return null;
			}
			reqs.add(req);
		}
		if (requies.seam().required()) {
			RequirementBase req = getSeamRequirement(requies.seam());
			if (req == null) {
				return null;
			}
			reqs.add(req);
		}
		if (requies.esb().required()) {
			RequirementBase req = getESBRequirement(requies.esb());
			if (req == null) {
				return null;
			}
			reqs.add(req);
		}
		if (!"".equals(requies.perspective())) {
			reqs.add(RequirementBase.createSwitchPerspective(requies
					.perspective()));
		}
		if (requies.clearWorkspace()) {
			reqs.add(RequirementBase.createClearWorkspace());
		}
		if (requies.clearProjects()) {
			reqs.add(RequirementBase.createClearProjects());
		}
		// sort requirements by priority
		Collections.sort(reqs, new Comparator<RequirementBase>() {
			public int compare(RequirementBase o1, RequirementBase o2) {
				return o1.getPriority() - o2.getPriority();
			}
		});

		return reqs;
	}

	/**
	 * implements comparison of 2 params by given operator (in this order)
	 * params are expected version strings (in form X.X or XX) if param1 or
	 * param2 is '*' true is returned
	 * 
	 * @param param1
	 * @param operator
	 *            (=,<,>=<=,>=,!=)
	 * @param param2
	 * 
	 * @return
	 */
	public static boolean matches(String param1, String operator, String param2) {
		if ("*".equals(param1) || "*".equals(param2)) {
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
		// return SWTTestExt.util.getValue(swtTestProperties, key);
	}
}
