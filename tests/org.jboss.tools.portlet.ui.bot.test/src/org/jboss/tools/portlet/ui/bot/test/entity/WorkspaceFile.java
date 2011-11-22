package org.jboss.tools.portlet.ui.bot.test.entity;

public class WorkspaceFile {

	private String project;
	
	private String file;

	public WorkspaceFile(String project, String file) {
		super();
		this.project = project;
		this.file = file;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
	
	@Override
	public String toString() {
		return "Workspace file: " + getProject() + "/" + getFile();
	}
}
