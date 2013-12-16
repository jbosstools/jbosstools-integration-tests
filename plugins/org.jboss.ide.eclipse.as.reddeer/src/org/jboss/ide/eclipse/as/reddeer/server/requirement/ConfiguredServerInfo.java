package org.jboss.ide.eclipse.as.reddeer.server.requirement;

/**
 * Contains informations about configured server via server requirement.
 * The configured server is defined by its name(name displayed in servers view)
 * and configuration (ServerRequirementConfig).
 * 
 * @author Radoslav Rabara
 * @see ServerRequirementConfig
 */
class ConfiguredServerInfo {
	
	private String serverName;
	private ServerRequirementConfig config;
	
	/**
	 * Define configured server by its name and configuration.
	 * 
	 * @param serverName is the name of the configured server
	 * @param config configuration which was used to configure server
	 */
	public ConfiguredServerInfo(String serverName, ServerRequirementConfig config) {
		this.serverName = serverName;
		this.config = config;
	}
	
	public String getServerName() {
		return serverName;
	}
	
	public ServerRequirementConfig getConfig() {
		return config;
	}
}
