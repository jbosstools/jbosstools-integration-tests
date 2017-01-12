package org.jboss.tools.runtime.as.ui.bot.test.parametized.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.osgi.util.NLS;
import org.jboss.tools.runtime.as.ui.bot.test.Activator;
import org.jboss.tools.runtime.as.ui.bot.test.SuiteConstants;
import org.jboss.tools.runtime.as.ui.bot.test.parametized.MatrixUtils;
import org.jboss.tools.runtime.as.ui.bot.test.reddeer.Runtime;

public class ServerRuntimeUIConstants {

	/*
	 * DL RT Strings
	 */

	public static final String GATE_IN_3_6 = "GateIn Portal 3.6.0";
	public static final String JBAS_328 = "JBoss 3.2.8 SP 1";
	public static final String JBAS_405 = "JBoss 4.0.5";
	public static final String JBAS_423 = "JBoss 4.2.3";
	public static final String JBAS_501 = "JBoss 5.0.1";
	public static final String JBAS_510 = "JBoss 5.1.0";
	public static final String JBAS_600 = "JBoss 6.0.0";
	public static final String JBAS_701 = "JBoss 7.0.1";
	public static final String JBAS_702 = "JBoss 7.0.2";
	public static final String JBAS_710 = "JBoss 7.1.0";
	public static final String JBAS_711 = "JBoss AS 7.1.1 (Brontes)";
	public static final String JBEAP_610 = "JBoss EAP 6.1.0";
	public static final String WF_800 = "WildFly 8.0.0 Final";
	public static final String WF_810 = "WildFly 8.1.0 Final";
	public static final String WF_820 = "WildFly 8.2.0 Final";
	public static final String WF_821 = "WildFly 8.2.1 Final";
	public static final String WF_900 = "WildFly 9.0.0 Final";
	public static final String WF_901 = "WildFly 9.0.1 Final";
	public static final String WF_902 = "WildFly 9.0.2 Final";
	public static final String WF_10_0_0 = "WildFly 10.0.0 Final";
	public static final String WF_10_1_0 = "WildFly 10.1.0 Final";

	// Requires credentials
	// public static final String JBEAP_610 = "JBoss EAP 6.1.0";
	public static final String JBEAP_620 = "JBoss EAP 6.2.0";
	public static final String JBEAP_630 = "JBoss EAP 6.3.0";
	public static final String JBEAP_640 = "JBoss EAP 6.4.0";
	public static final String JBEAP_700 = "JBoss EAP 7.0.0";
	public static final String JPP_610 = "JBoss Portal Platform 6.1.0";

	
	// Manual Downloads
//	public static final String JPP_600 = "JBoss Portal Platform 6.0.0";
//	public static final String JBEAP_600 = "JBoss EAP 6.0.0";
//	public static final String JBEAP_601 = "JBoss EAP 6.0.1";

	
	public static final String[] LATEST_MAJORS_FREE_DOWNLOADS = new String[] {
			GATE_IN_3_6, JBAS_423, JBAS_510, JBAS_600, JBAS_711, 
			WF_821, WF_902, WF_10_1_0};

	
	public static final String[] FREE_DOWNLOADS = new String[] {
			GATE_IN_3_6, JBAS_328, JBAS_405, JBAS_423, JBAS_501, JBAS_510, JBAS_600, 
			JBAS_701, JBAS_702, JBAS_710, JBAS_711, WF_800, WF_810,
			WF_820, WF_821, WF_901, WF_902, WF_10_0_0, WF_10_1_0};

//	public static final String[] MANUAL_DOWNLOAD = new String[]{JPP_600,JBEAP_600, JBEAP_601};

	public static final String[] ZERO_DOLLAR = new String[] { 
			JPP_610, JBEAP_610, JBEAP_620,
			JBEAP_630, JBEAP_640,JBEAP_700, };

	public static final String[] ALL_DOWNLOADS = Stream
			.concat(Arrays.stream(FREE_DOWNLOADS), Arrays.stream(ZERO_DOLLAR)).toArray(String[]::new);

	public static final String[] SMOKETEST_DOWNLOADS = new String[] { WF_902, WF_10_1_0 };

	private static HashMap<String, ModelWrapper> expectations = null;

	private static void initialize() {
		HashMap<String, ModelWrapper> map = new HashMap<String,ModelWrapper>();
		expectations = map;
		
		
		addEntry(JBAS_328, 		"JBoss AS 3.2", 		"3.2", 	"AS", 		"jboss-3.2.8.SP1",				LEGACY_DEPLOY, LEGACY_UNDEPLOY, legacyEditorPorts());
		addEntry(JBAS_405, 		"JBoss AS 4.0", 		"4.0", 	"AS", 		"jboss-4.0.5.GA", 				LEGACY_DEPLOY, LEGACY_UNDEPLOY, legacyEditorPorts());
		addEntry(JBAS_423, 		"JBoss AS 4.2", 		"4.2", 	"AS", 		"jboss-4.2.3.GA", 				LEGACY_DEPLOY, LEGACY_UNDEPLOY, legacyEditorPorts());
		addEntry(JBAS_501, 		"JBoss AS 5.0", 		"5.0", 	"AS", 		"jboss-5.0.1.GA", 				LEGACY_DEPLOY, LEGACY_UNDEPLOY, legacyEditorPorts());
		addEntry(JBAS_510, 		"JBoss AS 5.1", 		"5.1", 	"AS", 		"jboss-5.1.0.GA", 				LEGACY_DEPLOY, LEGACY_UNDEPLOY, legacyEditorPorts());
		addEntry(JBAS_600, 		"JBoss AS 6.0", 		"6.0", 	"AS", 		"jboss-6.0.0.Final", 			LEGACY_DEPLOY, LEGACY_UNDEPLOY, as6EditorPorts());
		
		
		addEntry(GATE_IN_3_6, 	"GateIn 3.6", 			"3.6",	"GateIn", 	"GateIn-3.6.0.Final-jbossas7", 	STANDARD_DEPLOY, STANDARD_UNDEPLOY, as7EditorPorts());

		
		addEntry(JBAS_701, 		"JBoss AS 7.0", 		"7.0", 	"AS", 		"jboss-as-7.0.1.Final", 		STANDARD_DEPLOY, STANDARD_UNDEPLOY, as7EditorPorts());
		addEntry(JBAS_702, 		"JBoss AS 7.0",			"7.0", 	"AS", 		"jboss-as-7.0.2.Final", 		STANDARD_DEPLOY, STANDARD_UNDEPLOY, as7EditorPorts());
		addEntry(JBAS_710, 		"JBoss AS 7.1",			"7.1", 	"AS", 		"jboss-as-7.1.0.Final", 		STANDARD_DEPLOY, STANDARD_UNDEPLOY, as7EditorPorts());
		addEntry(JBAS_711, 		"JBoss AS 7.1", 		"7.1", 	"AS", 		"jboss-as-7.1.1.Final", 		STANDARD_DEPLOY, STANDARD_UNDEPLOY, as7EditorPorts());
		addEntry(WF_800, 		"WildFly 8.0",		 	"8.0", 	"WildFly", 	"wildfly-8.0.0.Final", 			STANDARD_DEPLOY, STANDARD_UNDEPLOY, wfEditorPorts());
		addEntry(WF_810, 		"WildFly 8.1",		 	"8.1", 	"WildFly", 	"wildfly-8.1.0.Final", 			STANDARD_DEPLOY, STANDARD_UNDEPLOY, wfEditorPorts());
		addEntry(WF_820, 		"WildFly 8.2",		 	"8.2", 	"WildFly", 	"wildfly-8.2.0.Final", 			STANDARD_DEPLOY, STANDARD_UNDEPLOY, wfEditorPorts());
		addEntry(WF_821, 		"WildFly 8.2",		 	"8.2", 	"WildFly", 	"wildfly-8.2.1.Final", 			STANDARD_DEPLOY, STANDARD_UNDEPLOY, wfEditorPorts());
		addEntry(WF_900, 		"WildFly 9.0",		 	"9.0", 	"WildFly", 	"wildfly-9.0.0.Final", 			STANDARD_DEPLOY, STANDARD_UNDEPLOY, wfEditorPorts());
		addEntry(WF_901, 		"WildFly 9.0",		 	"9.0", 	"WildFly", 	"wildfly-9.0.1.Final", 			STANDARD_DEPLOY, STANDARD_UNDEPLOY, wfEditorPorts());
		addEntry(WF_902, 		"WildFly 9.0",		 	"9.0", 	"WildFly", 	"wildfly-9.0.2.Final", 			STANDARD_DEPLOY, STANDARD_UNDEPLOY, wfEditorPorts());
		addEntry(WF_10_0_0, 	"WildFly 10.0",			"10.0", "WildFly", 	"wildfly-10.0.0.Final",			STANDARD_DEPLOY, STANDARD_UNDEPLOY, wfEditorPorts());
		addEntry(WF_10_1_0, 	"WildFly 10.1",			"10.1", "WildFly", 	"wildfly-10.1.0.Final", 		STANDARD_DEPLOY, STANDARD_UNDEPLOY, wfEditorPorts());
		addEntry(JBEAP_610, 	"Red Hat JBoss EAP 6.1", "6.1",	"EAP", 		"jboss-eap-6.1", 				STANDARD_DEPLOY, STANDARD_UNDEPLOY, as7EditorPorts());
		addEntry(JBEAP_620, 	"Red Hat JBoss EAP 6.2", "6.2",	"EAP", 		"jboss-eap-6.2", 				STANDARD_DEPLOY, STANDARD_UNDEPLOY, as7EditorPorts());
		addEntry(JBEAP_630, 	"Red Hat JBoss EAP 6.3", "6.3",	"EAP",		"jboss-eap-6.3", 				STANDARD_DEPLOY, STANDARD_UNDEPLOY, as7EditorPorts());
		addEntry(JBEAP_640, 	"Red Hat JBoss EAP 6.4", "6.4",	"EAP", 		"jboss-eap-6.4", 				STANDARD_DEPLOY, STANDARD_UNDEPLOY, as7EditorPorts());
		addEntry(JBEAP_700, 	"Red Hat JBoss EAP 7.0", "7.0",	"EAP", 		"jboss-eap-7.0", 				STANDARD_DEPLOY, STANDARD_UNDEPLOY, wfEditorPorts());
		addEntry(JPP_610, 		"JBoss Portal 6.1",		"6.1", "JPP", 		"jboss-jpp-6.1", 				STANDARD_DEPLOY, STANDARD_UNDEPLOY, as7EditorPorts());
		
		
		// TODO manual downloads
//		map.put(JBEAP_600, asList(createRuntime(JBEAP_600, "", "", "", "")));
//		map.put(JBEAP_601, asList(createRuntime(JBEAP_601, "", "", "", "")));
//		map.put(JPP_600, asList(createRuntime(JPP_600, "", "", "", "")));

		

	}

	
	public static EditorPort[] legacyEditorPorts() {
		return new EditorPort[] { new EditorPort("Web", "8080"), new EditorPort("JNDI", "1099")};
	}
	public static EditorPort[] as7EditorPorts() {
		return new EditorPort[] { new EditorPort("Web", "8080"), new EditorPort("Management", "9999")};
	}
	public static EditorPort[] wfEditorPorts() {
		return new EditorPort[] { new EditorPort("Web", "8080"), new EditorPort("Management", "9990")};
	}
	public static EditorPort[] as6EditorPorts() {
		return new EditorPort[] { new EditorPort("Web", "8080"), new EditorPort("JNDI", "1099"), new EditorPort("JMX RMI", "1090")};
	}
	
	public static class EditorPort {
		private String label, value;
		public EditorPort(String k, String v) {
			this.label = k;
			this.value = v;
		}
		public String getLabel() {
			return label;
		}
		public String getValue() {
			return value;
		}
	}
	
	private static interface IStringFormatter {
		public String getString(String projectName, String suffix);
	}
	private static class PatternFormatter implements IStringFormatter {
		private String pattern;
		private int numReplace;
		public PatternFormatter(String pattern, int numReplace) {
			this.pattern = pattern;
			this.numReplace = numReplace;
		}
		public String getString(String projectName, String suffix) {
			if( numReplace == 1 )
				return NLS.bind(pattern, projectName);
			return NLS.bind(pattern, projectName, suffix);
		}
	}
	private static final IStringFormatter LEGACY_DEPLOY = new PatternFormatter("deploy, ctxPath=/{0}", 1);
	private static final IStringFormatter LEGACY_UNDEPLOY = new PatternFormatter("undeploy, ctxPath=/{0}", 1);
	private static final IStringFormatter STANDARD_DEPLOY = new PatternFormatter("Deployed \"{0}{1}\"",2);
	private static final IStringFormatter STANDARD_UNDEPLOY = new PatternFormatter("Undeployed \"{0}{1}\"",2);
	
	private static class ModelWrapper {
		List<Runtime> runtimes;
		IStringFormatter deployString;
		IStringFormatter undeployString;
		EditorPort[] ports;
		public ModelWrapper(List<Runtime> runtimes, IStringFormatter deployString, IStringFormatter undeployString, EditorPort[] ports) {
			this.runtimes = runtimes;
			this.deployString = deployString;
			this.undeployString = undeployString;
			this.ports = ports;
		}
	}
	
	
	
	private static void addEntry(String key, String name, String version, String type, String suffix, 
			IStringFormatter deployString, IStringFormatter undeployString,
			EditorPort[] ports) {
		List<Runtime> l = asList(createRuntime(key, name, version, type, suffix));
		ModelWrapper mw = new ModelWrapper(l, deployString, undeployString, ports);
		expectations.put(key, mw);
	}
	
	private static List<Runtime> asList(Runtime r) {
		return Arrays.asList(new Runtime[] { r });
	}


	private static Runtime createRuntime(String key, String name, String version, String type, String suffix) {
		return new Runtime(name, version, type,
				Activator.getDownloadPath(key).append(suffix).toOSString());
	}

	public static List<Runtime> getRuntimesForDownloadable(String dlRuntimeString) {
		if (expectations == null) {
			initialize();
		}
		ModelWrapper mw = expectations.get(dlRuntimeString);
		return mw == null ? null : mw.runtimes;
	}
	
	
	public static String getDeployString(String dlRuntimeString, String projectName, String suffix) {
		if (expectations == null) {
			initialize();
		}
		ModelWrapper mw = expectations.get(dlRuntimeString);
		return mw == null ? null : mw.deployString.getString(projectName, suffix);
	}

	public static String getUndeployString(String dlRuntimeString, String projectName, String suffix) {
		if (expectations == null) {
			initialize();
		}
		ModelWrapper mw = expectations.get(dlRuntimeString);
		return mw == null ? null : mw.undeployString.getString(projectName, suffix);
	}

	public static EditorPort[] getPorts(String dlRuntimeString) {
		if (expectations == null) {
			initialize();
		}
		ModelWrapper mw = expectations.get(dlRuntimeString);
		return mw == null ? null : mw.ports;
		
	}
    public static String getServerName(String runtimeString) {
    	String serverName = ServerRuntimeUIConstants.getRuntimesForDownloadable(runtimeString).get(0).getName();
    	return serverName;
    }
	
    public static Collection<Object[]> getParametersForScope(String scope){
    	ArrayList<Object[]> ret;
    	Object[] free = new Object[]{SuiteConstants.FREE};
    	Object[] zeroDollar = new Object[]{SuiteConstants.ZERO_DOLLAR};
    	
    	
    	if( SuiteConstants.SCOPE_MAJORS.equals(scope)) { 
    		// latest from majors only, ie one of 4.x, 5.x, 6.x, each one being newest in the stream
    		ret = MatrixUtils.toMatrix(new Object[][]{LATEST_MAJORS_FREE_DOWNLOADS, free});
    	} else if( SuiteConstants.SCOPE_FREE.equals(scope)) {
    		// ALL free
    		ret = MatrixUtils.toMatrix(new Object[][]{FREE_DOWNLOADS, free});
    	} else if( SuiteConstants.SCOPE_ALL.equals(scope)) {
			ret = MatrixUtils.toMatrix(new Object[][] { FREE_DOWNLOADS, free});
			ArrayList<Object[]> ret2 = MatrixUtils.toMatrix(new Object[][] { ZERO_DOLLAR, zeroDollar});
			ret.addAll(ret2);
    	} else {
        	// If smoke test
    		ret = MatrixUtils.toMatrix(new Object[][]{SMOKETEST_DOWNLOADS, free});
    	}
    	return ret;
    }
	

}
