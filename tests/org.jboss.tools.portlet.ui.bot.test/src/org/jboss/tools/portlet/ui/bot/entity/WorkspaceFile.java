package org.jboss.tools.portlet.ui.bot.entity;

public class WorkspaceFile {

	public static final String FILE_SEPARATOR = "/";

	private String project;

	private String filePath;

	public WorkspaceFile(String project, String filePath) {
		super();
		this.project = project;
		this.filePath = filePath;
	}

	public String getFileName(){
		return getFilePathAsArray()[getFilePathAsArray().length - 1];
	}

	public String[] getFilePathAsArray(){
		return getFilePath().split(FILE_SEPARATOR);
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String file) {
		this.filePath = file;
	}

	@Override
	public String toString() {
		return "Workspace file: " + getProject() + FILE_SEPARATOR + getFilePath();
	}
}
