package org.jboss.tools.portlet.ui.bot.entity;


public class XMLNode {

	public static final String NODES_SEPARATOR = "/";
	
	private String path;
	
	private String content;
	
	public XMLNode(String path, String content) {
		super();
		this.path = path;
		this.content = content;
	}

	public String getNodeName(){
		return getPathAsArray()[getPathAsArray().length - 1];
	}
	
	public String[] getPathAsArray(){
		return path.split(NODES_SEPARATOR);
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
