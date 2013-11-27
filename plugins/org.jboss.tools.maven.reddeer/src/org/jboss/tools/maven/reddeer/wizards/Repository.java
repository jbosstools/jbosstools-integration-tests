package org.jboss.tools.maven.reddeer.wizards;

public class Repository {
	
	private String name;
	private String url;
	private boolean enabled;
	
	public Repository(String name, String url, boolean enabled){
		this.name=name;
		this.url=url;
		this.enabled = enabled;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	

}
