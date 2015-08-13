package org.jboss.tools.forge.ui.bot.test.util;

/**
 * Scaffolding types enumeration
 * @author Jan Richter
 *
 */
public enum ScaffoldType {
	FACES("Faces"), ANGULARJS("AngularJS");

	private final String name;

	private ScaffoldType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
