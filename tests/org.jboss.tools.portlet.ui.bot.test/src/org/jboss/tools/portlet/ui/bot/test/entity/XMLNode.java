package org.jboss.tools.portlet.ui.bot.test.entity;


public class XMLNode {

	private String path;
	
	private String content;
	
	public XMLNode(String path, String content) {
		super();
		this.path = path;
		this.content = content;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "XML path = " + getPath() + ", content = " + getContent(); 
	}
}
