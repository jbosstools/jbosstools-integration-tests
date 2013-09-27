package org.jboss.tools.teiid.ui.bot.test.suite;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.swtbot.swt.finder.junit.ScreenshotCaptureListener;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
import org.jboss.tools.teiid.reddeer.preference.ServerPreferencePage;
import org.jboss.tools.teiid.reddeer.wizard.ServerWizard;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

/**
 * 
 * @author apodhrad
 *
 */
public class TeiidSuite extends RedDeerSuite {

	public static final String PROPERTIES_FILE = "swtbot.test.properties.file";

	private static String serverName;

	public TeiidSuite(Class<?> clazz, RunnerBuilder builder) throws InitializationError {
		super(clazz, foo(builder));
	}

	@Override
	public void run(RunNotifier notifier) {
		RunListener failureSpy = new ScreenshotCaptureListener();
		notifier.removeListener(failureSpy);
		notifier.addListener(failureSpy);
		try {
			super.run(notifier);
		} finally {
			notifier.removeListener(failureSpy);
		}
	}

	private static RunnerBuilder foo(RunnerBuilder builder) {
		Properties props = loadSWTBotProperties();
		addServer(props.getProperty("SERVER"));
		closeWelcome();

		return builder;
	}

	public static String getServerName() {
		return serverName;
	}

	private static void closeWelcome() {
		try {
			Bot.get().viewByTitle("Welcome").close();
		} catch (Exception ex) {
			// ok
		}
	}

	private static Properties loadSWTBotProperties() {
		Properties props = new Properties();
		String propsFile = System.getProperty(PROPERTIES_FILE);
		if (propsFile != null) {
			try {
				props.load(new FileReader(propsFile));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				throw new RuntimeException("Couldn't find properties file!");
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("I/O excpetion during reading properties file!");
			}
		}
		return props;
	}

	public static void addServerWithProperties(String propertyFile){
		System.setProperty(PROPERTIES_FILE, propertyFile);//set -Dswtbot.test.properties.file
		Properties props = loadSWTBotProperties();
		addServer(props.getProperty("SERVER"));
	}
	
	private static void addServer(String serverConfig) {
		if (serverConfig == null) {
			return;
		}
		String[] param = serverConfig.split(",");
		if (param.length < 4) {
			throw new RuntimeException("Bad format of SERVER config");
		}
		String type = param[0];
		String version = param[1];
		String path = new File(param[3]).getAbsolutePath();
		
		String configFile = "standalone.xml";//default (standalone.xml)
		if (param.length > 4){
			configFile = param[4];//teiid designer (standalone-teiid.xml) 
		}
		
		serverName = type + "-" + version;

		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
		ServerPreferencePage serverPP = new ServerPreferencePage();
		serverPP.open();
		serverPP.addServerRuntime(serverName, path, configFile, getServerRuntime(type, version));
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
		serverPP.ok();

		ServerWizard serverWizard = new ServerWizard();
		serverWizard.setType(getServerType(type, version));
		serverWizard.setName(serverName);
		serverWizard.execute();
	}

	private static String[] getServerType(String type, String version) {
		String[] serverType = new String[2];
		if (type.equals("EAP")) {
            serverType[0] = "JBoss Enterprise Middleware";
            if (version.startsWith("6.0")) {
                    serverType[1] = "JBoss Enterprise Application Platform 6.0";
            }
            if (version.startsWith("6.1")) {
                    serverType[1] = "JBoss Enterprise Application Platform 6.1";
            }
		} else if (type.equals("SOA")) {
			serverType[0] = "JBoss Enterprise Middleware";
			if (version.startsWith("5")) {
				serverType[1] = "JBoss Enterprise Application Platform 5.x";
			}
			if (version.startsWith("6")) {
                serverType[1] = "JBoss Enterprise Application Platform 6.1";
			}
		} else if (type.equals("AS")) {
			serverType[0] = "JBoss Community";
			serverType[1] = "JBoss AS " + version;
		} else {
			throw new RuntimeException("You have to specify if it is AS or SOA or EAP");
		}
		return serverType;
	}

	private static String[] getServerRuntime(String type, String version) {
		String[] serverRuntime = new String[2];
		if (type.equals("EAP")) {
            serverRuntime[0] = "JBoss Enterprise Middleware";
            if (version.startsWith("6.0")) {
                    serverRuntime[1] = "JBoss Enterprise Application Platform 6.0 Runtime";
            }
            if (version.startsWith("6.1")) {
                    serverRuntime[1] = "JBoss Enterprise Application Platform 6.1 Runtime";
            }
		} else if (type.equals("SOA")) {
			serverRuntime[0] = "JBoss Enterprise Middleware";
			if (version.startsWith("5")) {
				serverRuntime[1] = "JBoss Enterprise Application Platform 5.x Runtime";
			}
			if (version.startsWith("6")) {
                serverRuntime[1] = "JBoss Enterprise Application Platform 6.1 Runtime";
			}
		} else if (type.equals("AS")) {
			serverRuntime[0] = "JBoss Community";
			serverRuntime[1] = "JBoss " + version + " Runtime";
		} else {
			throw new RuntimeException("You have to specify if it is AS or SOA or EAP");
		}
		return serverRuntime;
	}
}