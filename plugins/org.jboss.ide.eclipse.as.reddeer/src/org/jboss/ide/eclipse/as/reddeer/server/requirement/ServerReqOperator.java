package org.jboss.ide.eclipse.as.reddeer.server.requirement;

/**
 * Enumeration of operators used to match server's version
 * 
 * @author Radoslav Rabara
 *
 */
public enum ServerReqOperator {
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
