package org.jboss.ide.eclipse.as.reddeer.server.requirement;

/**
 * Enumeration of version matchers.
 * 
 * @author Radoslav Rabara
 *
 */
public enum ServerReqVersion {
	/**
	 * =
	 */
	EQUAL,
	
	/**
	 * !=
	 */
	NOT_EQUAL,
	
	/**
	 * >
	 */
	GREATER,
	
	/**
	 * <
	 */
	LESS,
	
	/**
	 * >=
	 */
	GREATER_OR_EQUAL,
	
	/**
	 * <=
	 */
	LESS_OR_EQUAL
}
