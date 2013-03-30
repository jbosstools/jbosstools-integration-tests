package org.jboss.tools.bpmn2.ui.bot.ext;

public class Translation {

	/**
	 * 
	 * @param path
	 * @return
	 */
	public static String projectPath(String... path) {
		StringBuilder builder = new StringBuilder("/");
		for (String e : path) {
			builder.append(e);
			builder.append("/");
		}
		return builder.toString();
	}
	
	/**
	 * 
	 * @param e
	 * @return
	 */
	public static String contextMenuItemName(Enum<?> e) {
		StringBuilder r = new StringBuilder();
		for (String w : e.name().split("_")) {
			r.append(w.charAt(0) + w.substring(1).toLowerCase());
			r.append(" ");
		}
		return r.toString().trim();
	}
	
}
