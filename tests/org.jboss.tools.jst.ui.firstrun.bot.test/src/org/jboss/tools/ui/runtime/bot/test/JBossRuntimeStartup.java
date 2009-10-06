package org.jboss.tools.ui.runtime.bot.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.datatools.connectivity.ConnectionProfileConstants;
import org.eclipse.datatools.connectivity.ConnectionProfileException;
import org.eclipse.datatools.connectivity.ProfileManager;
import org.eclipse.datatools.connectivity.db.generic.IDBConnectionProfileConstants;
import org.eclipse.datatools.connectivity.db.generic.IDBDriverDefinitionConstants;
import org.eclipse.datatools.connectivity.drivers.DriverInstance;
import org.eclipse.datatools.connectivity.drivers.DriverManager;
import org.eclipse.datatools.connectivity.drivers.IDriverMgmtConstants;
import org.eclipse.datatools.connectivity.drivers.IPropertySet;
import org.eclipse.datatools.connectivity.drivers.PropertySetImpl;
import org.eclipse.datatools.connectivity.drivers.models.TemplateDescriptor;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.ui.IStartup;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IRuntimeType;
import org.eclipse.wst.server.core.IRuntimeWorkingCopy;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerType;
import org.eclipse.wst.server.core.IServerWorkingCopy;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.ServerUtil;
import org.eclipse.wst.server.core.internal.RuntimeWorkingCopy;
import org.eclipse.wst.server.core.internal.ServerWorkingCopy;
import org.jboss.tools.test.TestProperties;

@SuppressWarnings("restriction")
public class JBossRuntimeStartup implements IStartup {
	private static Properties properties;
	private static final String RUNTIME = Messages.JBossRuntimeStartup_Runtime;

	private static final String RUNTIME_PROPERTIES = "runtimePaths.properties";
	
	static{
		try {
			InputStream inputStream = JBossRuntimeStartup.class.getResourceAsStream("/"+RUNTIME_PROPERTIES);
			properties = new TestProperties();
			properties.load(inputStream);
		} catch (IOException e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Can't load properties from " + RUNTIME_PROPERTIES + " file", e);
			Activator.getDefault().getLog().log(status);
			e.printStackTrace();
		} catch (IllegalStateException e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Property file " + RUNTIME_PROPERTIES + " was not found", e);
			Activator.getDefault().getLog().log(status);
			e.printStackTrace();
		}
	}
	
	// This constants are made to avoid dependency with org.jboss.ide.eclipse.as.core plugin
	public static final String JBOSS_AS_RUNTIME_TYPE_ID[] = {
		"org.jboss.ide.eclipse.as.runtime.32", //$NON-NLS-1$
		"org.jboss.ide.eclipse.as.runtime.40", //$NON-NLS-1$
		"org.jboss.ide.eclipse.as.runtime.42", //$NON-NLS-1$
		"org.jboss.ide.eclipse.as.runtime.50", //$NON-NLS-1$
		"org.jboss.ide.eclipse.as.runtime.eap.43" //$NON-NLS-1$
		};

	public static final String JBOSS_AS_TYPE_ID[] = {
		"org.jboss.ide.eclipse.as.32", //$NON-NLS-1$
		"org.jboss.ide.eclipse.as.40", //$NON-NLS-1$
		"org.jboss.ide.eclipse.as.42", //$NON-NLS-1$
		"org.jboss.ide.eclipse.as.50", //$NON-NLS-1$
		"org.jboss.ide.eclipse.as.eap.43" //$NON-NLS-1$
		};
	
	public static final String JBOSS_AS_NAME[] = {
		Messages.JBossRuntimeStartup_JBoss_Application_Server_3_2,
		Messages.JBossRuntimeStartup_JBoss_Application_Server_4_0,
		Messages.JBossRuntimeStartup_JBoss_Application_Server_4_2,
		Messages.JBossRuntimeStartup_JBoss_Application_Server_5_0,
		Messages.JBossRuntimeStartup_JBoss_EAP_Server_4_3
		};
	
	public static final String JBOSS_AS_HOST = "localhost"; //$NON-NLS-1$

	public static final String JBOSS_AS_DEFAULT_CONFIGURATION_NAME = "default"; //$NON-NLS-1$

	//public static final String FIRST_START_PREFERENCE_NAME = "FIRST_START";

	public static final String HSQL_DRIVER_DEFINITION_ID 
												= "DriverDefn.Hypersonic DB"; //$NON-NLS-1$

	public static final String HSQL_DRIVER_NAME = "Hypersonic DB"; //$NON-NLS-1$

	public static final String HSQL_DRIVER_TEMPLATE_ID 
						= "org.eclipse.datatools.enablement.hsqldb.1_8.driver"; //$NON-NLS-1$

	public static final String DTP_DB_URL_PROPERTY_ID 
								= "org.eclipse.datatools.connectivity.db.URL"; //$NON-NLS-1$
	
	
	public void earlyStartup() {
		boolean firstStart = Activator.getDefault().getPreferenceStore().getBoolean(Activator.FIRST_START);
		if (!firstStart) {
			return;
		}
		Activator.getDefault().getPreferenceStore().setValue(Activator.FIRST_START, false);
		File jbossASDir = new File(properties.getProperty("JBossEap4.3"));
		createJBossServer(jbossASDir,4, "jboss-eap", "jboss-eap " + RUNTIME); //$NON-NLS-1$ //$NON-NLS-2$
	}


	private void createJBossServer(File asLocation, int index, String name, String runtimeName) {
		if (!asLocation.isDirectory()) {
			return;
		}
		IPath jbossAsLocationPath = new Path(asLocation.getAbsolutePath());

		IServer[] servers = ServerCore.getServers();
		for (int i = 0; i < servers.length; i++) {
			IRuntime runtime = servers[i].getRuntime();
			if(runtime != null && runtime.getLocation().equals(jbossAsLocationPath)) {
				return;
			}
		}

		IRuntime runtime = null;
		IRuntime[] runtimes = ServerCore.getRuntimes();
		for (int i = 0; i < runtimes.length; i++) {
			if (runtimes[0].getLocation().equals(jbossAsLocationPath)) {
				runtime = runtimes[0].createWorkingCopy();
				break;
			}
		}

		IProgressMonitor progressMonitor = new NullProgressMonitor();
		try {
			if (runtime == null) {
				runtime = createRuntime(runtimeName, asLocation.getAbsolutePath(), progressMonitor, index);
			}
			if (runtime != null) {
				createServer(progressMonitor, runtime, index, name);
			}

			createDriver(asLocation.getAbsolutePath());
		} catch (CoreException e) {
			log(e,Messages.JBossRuntimeStartup_Cannot_create_new_JBoss_Server);
		} catch (ConnectionProfileException e) {
			log(e,Messages.JBossRuntimeStartup_Cannot_create_new_DTP_Connection_Profile);
		}
	}

	private static void log(Throwable e, String message) {
		IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, message, e);
		Activator.getDefault().getLog().log(status);
	}
	
	/**
	 * Creates new JBoss AS Runtime, Server and hsqldb driver
	 * @param jbossASLocation location of JBoss Server
	 * @param progressMonitor to report progress
	 * @return server working copy
	 * @throws CoreException
	 * @throws ConnectionProfileException
	 */
	public static IServerWorkingCopy initJBossAS(String jbossASLocation, IProgressMonitor progressMonitor) throws CoreException, ConnectionProfileException {
		IRuntime runtime = createRuntime(null, jbossASLocation, progressMonitor, 2);
		IServerWorkingCopy server = null;
		if (runtime != null) {
			server = createServer(progressMonitor, runtime, 2, null);
		}
		createDriver(jbossASLocation);
		return server;
	}

	/**
	 * Creates new JBoss AS Runtime
	 * @param jbossASLocation location of JBoss AS
	 * @param progressMonitor
	 * @return runtime working copy
	 * @throws CoreException
	 */
	private static IRuntime createRuntime(String runtimeName, String jbossASLocation, IProgressMonitor progressMonitor, int index) throws CoreException {
		IRuntimeWorkingCopy runtime = null;
		String type = null;
		String version = null;
		String runtimeId = null;
		IPath jbossAsLocationPath = new Path(jbossASLocation);
		IRuntimeType[] runtimeTypes = ServerUtil.getRuntimeTypes(type, version, JBOSS_AS_RUNTIME_TYPE_ID[index]);
		if (runtimeTypes.length > 0) {
			runtime = runtimeTypes[0].createRuntime(runtimeId, progressMonitor);
			runtime.setLocation(jbossAsLocationPath);
			if(runtimeName!=null) {
				runtime.setName(runtimeName);				
			}
			IVMInstall defaultVM = JavaRuntime.getDefaultVMInstall();
			// IJBossServerRuntime.PROPERTY_VM_ID
			((RuntimeWorkingCopy) runtime).setAttribute("PROPERTY_VM_ID", defaultVM.getId()); //$NON-NLS-1$
			// IJBossServerRuntime.PROPERTY_VM_TYPE_ID
			((RuntimeWorkingCopy) runtime).setAttribute("PROPERTY_VM_TYPE_ID", defaultVM.getVMInstallType().getId()); //$NON-NLS-1$
			// IJBossServerRuntime.PROPERTY_CONFIGURATION_NAME
			((RuntimeWorkingCopy) runtime).setAttribute("org.jboss.ide.eclipse.as.core.runtime.configurationName", JBOSS_AS_DEFAULT_CONFIGURATION_NAME); //$NON-NLS-1$

			return runtime.save(false, progressMonitor);
		}
		return runtime;
	}

	/**
	 * Creates new JBoss Server
	 * @param progressMonitor
	 * @param runtime parent JBoss AS Runtime
	 * @return server working copy
	 * @throws CoreException
	 */
	private static IServerWorkingCopy createServer(IProgressMonitor progressMonitor, IRuntime runtime, int index, String name) throws CoreException {
		IServerType serverType = ServerCore.findServerType(JBOSS_AS_TYPE_ID[index]);
		IServerWorkingCopy server = serverType.createServer(null, null, runtime, progressMonitor);

		server.setHost(JBOSS_AS_HOST);
		if(name != null)
			server.setName(name);
		else
			server.setName(JBOSS_AS_NAME[index]);
		
		// JBossServer.DEPLOY_DIRECTORY
		String deployVal = runtime.getLocation().append("server").append(JBOSS_AS_DEFAULT_CONFIGURATION_NAME).append("deploy").toOSString(); //$NON-NLS-1$ //$NON-NLS-2$
		((ServerWorkingCopy) server).setAttribute("org.jboss.ide.eclipse.as.core.server.deployDirectory", deployVal); //$NON-NLS-1$

		// IDeployableServer.TEMP_DEPLOY_DIRECTORY
		String deployTmpFolderVal = runtime.getLocation().append("server").append(JBOSS_AS_DEFAULT_CONFIGURATION_NAME).append("tmp").append("jbosstoolsTemp").toOSString(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		((ServerWorkingCopy) server).setAttribute("org.jboss.ide.eclipse.as.core.server.tempDeployDirectory", deployTmpFolderVal); //$NON-NLS-1$

		// If we'd need to set up a username / pw for JMX, do it here.
//		((ServerWorkingCopy)serverWC).setAttribute(JBossServer.SERVER_USERNAME, authUser);
//		((ServerWorkingCopy)serverWC).setAttribute(JBossServer.SERVER_PASSWORD, authPass);

		server.save(false, progressMonitor);
		return server;
	}

	private static boolean driverIsCreated = false;

	/**
	 * Creates HSQL DB Driver
	 * @param jbossASLocation location of JBoss AS
	 * @throws ConnectionProfileException
	 * @return driver instance
	 */
	private static void createDriver(String jbossASLocation) throws ConnectionProfileException {
		if(driverIsCreated) {
			// Don't create the driver a few times
			return;
		}
		String driverPath;
		try {
			driverPath = new File(jbossASLocation + "/server/default/lib/hsqldb.jar").getCanonicalPath(); //$NON-NLS-1$
		} catch (IOException e) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR,
					Activator.PLUGIN_ID, Messages.JBossRuntimeStartup_Cannot_create_new_HSQL_DB_Driver, e));
			return;
		}

		DriverInstance driver = DriverManager.getInstance().getDriverInstanceByName(HSQL_DRIVER_NAME);
		if (driver == null) {
			TemplateDescriptor descr = TemplateDescriptor.getDriverTemplateDescriptor(HSQL_DRIVER_TEMPLATE_ID);
			IPropertySet instance = new PropertySetImpl(HSQL_DRIVER_NAME, HSQL_DRIVER_DEFINITION_ID);
			instance.setName(HSQL_DRIVER_NAME);
			instance.setID(HSQL_DRIVER_DEFINITION_ID);
			Properties props = new Properties();

			IConfigurationElement[] template = descr.getProperties();
			for (int i = 0; i < template.length; i++) {
				IConfigurationElement prop = template[i];
				String id = prop.getAttribute("id"); //$NON-NLS-1$

				String value = prop.getAttribute("value"); //$NON-NLS-1$
				props.setProperty(id, value == null ? "" : value); //$NON-NLS-1$
			}
			props.setProperty(DTP_DB_URL_PROPERTY_ID, "jdbc:hsqldb:."); //$NON-NLS-1$
			props.setProperty(IDriverMgmtConstants.PROP_DEFN_TYPE, descr.getId());
			props.setProperty(IDriverMgmtConstants.PROP_DEFN_JARLIST, driverPath);

			instance.setBaseProperties(props);
			DriverManager.getInstance().removeDriverInstance(instance.getID());
			System.gc();
			DriverManager.getInstance().addDriverInstance(instance);
		}

		driver = DriverManager.getInstance().getDriverInstanceByName(HSQL_DRIVER_NAME);
		if (driver != null && ProfileManager.getInstance().getProfileByName("DefaultDS") == null) { //$NON-NLS-1$
			// create profile
			Properties props = new Properties();
			props.setProperty(ConnectionProfileConstants.PROP_DRIVER_DEFINITION_ID, HSQL_DRIVER_DEFINITION_ID);
			props.setProperty(IDBConnectionProfileConstants.CONNECTION_PROPERTIES_PROP_ID, ""); //$NON-NLS-1$
			props.setProperty(IDBDriverDefinitionConstants.DRIVER_CLASS_PROP_ID, driver.getProperty(IDBDriverDefinitionConstants.DRIVER_CLASS_PROP_ID));
			props.setProperty(IDBDriverDefinitionConstants.DATABASE_VENDOR_PROP_ID,	driver.getProperty(IDBDriverDefinitionConstants.DATABASE_VENDOR_PROP_ID));
			props.setProperty(IDBDriverDefinitionConstants.DATABASE_VERSION_PROP_ID, driver.getProperty(IDBDriverDefinitionConstants.DATABASE_VERSION_PROP_ID));
			props.setProperty(IDBDriverDefinitionConstants.DATABASE_NAME_PROP_ID, "Default"); //$NON-NLS-1$
			props.setProperty(IDBDriverDefinitionConstants.PASSWORD_PROP_ID, ""); //$NON-NLS-1$
			props.setProperty(IDBConnectionProfileConstants.SAVE_PASSWORD_PROP_ID, "false"); //$NON-NLS-1$
			props.setProperty(IDBDriverDefinitionConstants.USERNAME_PROP_ID, driver.getProperty(IDBDriverDefinitionConstants.USERNAME_PROP_ID));
			props.setProperty(IDBDriverDefinitionConstants.URL_PROP_ID, driver.getProperty(IDBDriverDefinitionConstants.URL_PROP_ID));

			ProfileManager.getInstance().createProfile("DefaultDS",	Messages.JBossRuntimeStartup_The_JBoss_AS_Hypersonic_embedded_database, IDBConnectionProfileConstants.CONNECTION_PROFILE_ID, props, "", false); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		if(driver!=null) {
			driverIsCreated = true;
		}
	}

}