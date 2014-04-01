package org.jboss.tools.examples.ui.bot.test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Examples {

	private static Map<String, List<String>> projectNames = new HashMap<String, List<String>>();
	
	/**
	 * Get all examples as Map of their names and paths
	 * 
	 * @return Map of archive name associated to examples paths
	 */
	public static Map<String,List<String>> getAllExamples() {
		return Collections.unmodifiableMap(projectNames);
	}
	
	/**
	 * Add archive with the given archive name to the collection or archives
	 * 
	 * @param archiveName archive name
	 * @param pathsToExamples list of paths to examples in the archive
	 */
	public static void addArchive(String archiveName, List<String> pathsToExamples) {
		projectNames.put(archiveName, pathsToExamples);
	}
	
	/**
	 * Get path to examples in the archive with the specific name
	 * 
	 * @param archiveName archive name
	 * @return list of paths to examples in the archive
	 */
	public static List<String> getExamples(String archiveName) {
		return projectNames.get(archiveName);
	}
	
	/**
	 * Get archive examples names as key set. 
	 * Useful for manipulating with getting examples paths. 
	 * 
	 * @return set of archive names containing examples
	 */
	public static Set<String> getExamplesNames() {
		return projectNames.keySet();
	}
	
	/**
	 * Remove mapped example name and associated examples paths
	 * @param archive name of desired archive to remove
	 */
	public static void removeArchive(String archiveName) {
		projectNames.remove(archiveName);
	}
}
