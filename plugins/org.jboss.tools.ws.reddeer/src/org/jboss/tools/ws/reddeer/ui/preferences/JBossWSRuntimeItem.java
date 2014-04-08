package org.jboss.tools.ws.reddeer.ui.preferences;

/**
 * Model for jboss ws runtime encapsulates one instance of such an object 
 * 
 * @author jjankovi
 *
 */
public class JBossWSRuntimeItem {

	private String name;
	private String version;
	private String path;
	
	public JBossWSRuntimeItem(String name, String version, String path) {
		super();
		this.name = name;
		this.version = version;
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}
