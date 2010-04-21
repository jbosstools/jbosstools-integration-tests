package org.jboss.tools.ui.bot.ext.config.requirement;

import static org.junit.Assert.fail;

import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.TestConfigurator;
import org.jboss.tools.ui.bot.ext.gen.IServer;
import org.jboss.tools.ui.bot.ext.gen.IServerRuntime;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.Server.JBossCommunityJBossAS42;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.Server.JBossCommunityJBossAS50;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.Server.JBossEnterpriseMiddlewareJBossEnterpriseApplicationPlatform43;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.Server.JBossEnterpriseMiddlewareJBossEnterpriseApplicationPlatform50;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.ServerRuntime.JBossCommunityJBoss42Runtime;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.ServerRuntime.JBossCommunityJBoss50Runtime;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.ServerRuntime.JBossEnterpriseMiddlewareJBossEnterpriseApplicationPlatform43Runtime;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.ServerRuntime.JBossEnterpriseMiddlewareJBossEnterpriseApplicationPlatform50Runtime;
/**
 * adds server (version and type depends on {@link TestConfigurator#server}
 * @author lzoubek
 *
 */
public class AddServer extends RequirementBase {

	private String javaName=null;
	public AddServer() {
		String javaVer = getNeededJavaVersion(TestConfigurator.server.withJavaVersion);
		if (javaVer!=null) {
			AddJRE addJava = new AddJRE(javaVer);
			getDependsOn().add(addJava);
			javaName=addJava.getAddedAsName();
		}
		
	}

	@Override
	public void handle() {
		ServerInfo serverInfo = getRuntime(TestConfigurator.server.type.toString(),TestConfigurator.server.version);
		String runtimeHome=TestConfigurator.server.runtimeHome;
		String runtimeName=TestConfigurator.server.type.toString()+"-"+TestConfigurator.server.version;
		SWTTestExt.eclipse.addJbossServerRuntime(serverInfo.runtime, 
				runtimeHome, runtimeName, javaName);
		SWTTestExt.eclipse.addServer(serverInfo.server, runtimeName);
		SWTTestExt.configuredState.getServer().isConfigured=true;
		SWTTestExt.configuredState.getServer().name=runtimeName;
		SWTTestExt.configuredState.getServer().version=TestConfigurator.server.version;
		SWTTestExt.configuredState.getServer().type=TestConfigurator.server.type.toString();
	}

	
	class ServerInfo {
		public ServerInfo(IServerRuntime runtime,IServer server) {
			this.runtime=runtime;
			this.server=server;
		}
		public IServerRuntime runtime;
		public IServer server;
	}
	private String getNeededJavaVersion(String javaVer) {
		if (TestConfigurator.Values.SERVER_WITH_DEFAULT_JAVA.equals(javaVer)) {
			return null;
		}
		else {return javaVer;}

	}
	private ServerInfo getRuntime(String serverType, String version) {
		if (TestConfigurator.Values.SERVER_TYPE_EAP.equals(serverType)) {
			if ("4.3".equals(version)) {
				return new ServerInfo(JBossEnterpriseMiddlewareJBossEnterpriseApplicationPlatform43Runtime.LABEL,
						JBossEnterpriseMiddlewareJBossEnterpriseApplicationPlatform43.LABEL
						);				
			}
			if ("5.0".equals(version)) {
				return new ServerInfo(JBossEnterpriseMiddlewareJBossEnterpriseApplicationPlatform50Runtime.LABEL,
						JBossEnterpriseMiddlewareJBossEnterpriseApplicationPlatform50.LABEL);
			}
		} else if (TestConfigurator.Values.SERVER_TYPE_JBOSSAS.equals(serverType)) {
			if ("4.2".equals(version)) {
				return new ServerInfo(JBossCommunityJBoss42Runtime.LABEL,JBossCommunityJBossAS42.LABEL);				
			}
			if ("5.0".equals(version)) {
				return new ServerInfo(JBossCommunityJBoss50Runtime.LABEL,JBossCommunityJBossAS50.LABEL);				
			}
		}
		failParsing();
		return null;
	}
	private void failParsing() {
		fail("Unable to add server runtime, unparsable or not supported property value: "+TestConfigurator.getProperty(TestConfigurator.Keys.SERVER));
	}
	@Override
	public boolean checkFulfilled() {
		return SWTTestExt.configuredState.getServer().isConfigured;
	}

}
