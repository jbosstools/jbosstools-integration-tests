package org.jboss.tools.central.test.ui.reddeer.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.tools.central.reddeer.projects.Project;

public class ErrorsReporter {
	
	private static ErrorsReporter instance = new ErrorsReporter();
	
	private static Map<Project, List<String>> errors;
	private static Map<Project, List<String>> warnings;
	private static List<Project> projects;
	
	
	protected ErrorsReporter(){
		errors = new HashMap<Project, List<String>>();
		warnings = new HashMap<Project, List<String>>();
		projects = new ArrayList<Project>();
	}
	
	public static ErrorsReporter getInstance(){
		return instance;
	}
	
	public void addError(Project p, String error) {
		if (!projects.contains(p)) {
			projects.add(p);
		}
		if (!errors.containsKey(p)) {
			errors.put(p, new ArrayList<String>());
		}
		errors.get(p).add(error);
	}
	
	public void addWarning(Project p, String warning) {
		if (!projects.contains(p)) {
			projects.add(p);
		}
		if (!warnings.containsKey(p)) {
			warnings.put(p, new ArrayList<String>());
		}
		warnings.get(p).add(warning);
	}
	
	
	public void generateReport() {
		for (Project project : projects) {
			System.out.println("QUICKSTART: " + project.getName());
			if (errors.containsKey(project)) {
				System.out.println("\tERRORS:");

				for (String error : errors.get(project)) {
					System.out.println("\t\t" + error);
				}
			}
			if (warnings.containsKey(project)) {
				System.out.println("\tWARNINGS:");

				for (String warning : warnings.get(project)) {
					System.out.println("\t\t" + warning);
				}
			}
		}
	}
	
	
	public void cleanReports(){
		errors = new HashMap<Project, List<String>>();
		warnings = new HashMap<Project, List<String>>();
		projects = new ArrayList<Project>();
	}

}
