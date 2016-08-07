package org.jboss.tools.bpmn2.ui.bot.test.suite;

public class PathInProject {

	public static String buildPath(String... path) {
		StringBuilder builder = new StringBuilder("/");
		for (String e : path) {
			builder.append(e);
			builder.append("/");
		}
		return builder.toString();
	}
	
}
