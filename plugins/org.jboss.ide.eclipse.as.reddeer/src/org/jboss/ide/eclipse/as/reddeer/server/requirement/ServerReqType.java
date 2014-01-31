package org.jboss.ide.eclipse.as.reddeer.server.requirement;

/**
 * Enumeration of server's types
 * 
 * @author Radoslav Rabara
 *
 */
public enum ServerReqType {
	/**
	 * Matches any server
	 */
	ANY,
	
	/**
	 * JBoss Application Server
	 */
	AS,
	
	/**
	 * JBoss Enterprise Application Platform
	 */
	EAP,
	
	/**
	 * WildFly
	 * - successor of AS
	 */
	WILDFLY
}
