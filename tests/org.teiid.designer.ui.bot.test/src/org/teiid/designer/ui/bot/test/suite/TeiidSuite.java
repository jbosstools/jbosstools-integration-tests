package org.teiid.designer.ui.bot.test.suite;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.swtbot.swt.finder.junit.ScreenshotCaptureListener;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.util.Bot;
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

		serverName = type + "-" + version;

		ServerPreferencePage serverPP = new ServerPreferencePage();
		serverPP.open();
		serverPP.addServerRuntime(serverName, path, getServerRuntime(type, version));
		serverPP.ok();

		ServerWizard serverWizard = new ServerWizard();
		serverWizard.setType(getServerType(type, version));
		serverWizard.setName(serverName);
		serverWizard.execute();
	}

	private static String[] getServerType(String type, String version) {
		String[] serverType = new String[2];
		if (type.equals("SOA")) {
			serverType[0] = "JBoss Enterprise Middleware";
			if (version.startsWith("5")) {
				serverType[1] = "JBoss Enterprise Application Platform 5.x";
			}
		} else if (type.equals("AS")) {
			serverType[0] = "JBoss Community";
			serverType[1] = "JBoss AS " + version;
		} else {
			throw new RuntimeException("You have to specify if it is AS or SOA");
		}
		return serverType;
	}

	private static String[] getServerRuntime(String type, String version) {
		String[] serverRuntime = new String[2];
		if (type.equals("SOA")) {
			serverRuntime[0] = "JBoss Enterprise Middleware";
			if (version.startsWith("5")) {
				serverRuntime[1] = "JBoss Enterprise Application Platform 5.x Runtime";
			}
		} else if (type.equals("AS")) {
			serverRuntime[0] = "JBoss Community";
			serverRuntime[1] = "JBoss " + version + " Runtime";
		} else {
			throw new RuntimeException("You have to specify if it is AS or SOA");
		}
		return serverRuntime;
	}
}