package org.jboss.ide.eclipse.as.reddeer.server.requirement;

/**
 * Enumeration of server types.
 * <br/>
 * 
 * Server type can be defined just by family({@link #AS}, {@link #EAP} or {@link #WILDFLY})
 * or by family and version (e.g. {@link #AS7_1}, {@link #EAP6_1plus} or {@link #WILDFLY8_0)
 * <br/><br/>
 *
 * When server type is defined just by family, it matches any version (version is empty string).
 * When server type has defined version, it must match to version defined
 * as part of server adapter string in New Server dialog window.
 * <br/>
 * 
 * e.g. Server type matching EAP 6.1 must have version <code>6.1+</code> because server adapter for EAP 6.1
 * has string "JBoss Enterprise Application Platform <b>6.1+</b>"
 * 
 * @author Radoslav Rabara
 *
 */
public enum ServerReqType {
	
	/**
	 * Matches any server
	 */
	ANY(ServerReqFamily.ANY),
	
	/**
	 * JBoss Application Server with any version
	 */
	AS(ServerReqFamily.AS),
	/**
	 * JBoss Application Server 3.2
	 */
	AS3_2(ServerReqFamily.AS, "3.2"),
	/**
	 * JBoss Application Server 4.0
	 */
	AS4_0(ServerReqFamily.AS, "4.0"),
	/**
	 * JBoss Application Server 4.2
	 */
	AS4_2(ServerReqFamily.AS, "4.2"),
	/**
	 * JBoss Application Server 5.0
	 */
	AS5_0(ServerReqFamily.AS, "5.0"),
	/**
	 * JBoss Application Server 5.1
	 */
	AS5_1(ServerReqFamily.AS, "5.1"),
	/**
	 * JBoss Application Server 6.x
	 */
	AS6x(ServerReqFamily.AS, "6.x"),
	/**
	 * JBoss Application Server 7.0
	 */
	AS7_0(ServerReqFamily.AS, "7.0"),
	/**
	 * JBoss Application Server 7.1
	 */
	AS7_1(ServerReqFamily.AS, "7.1"),
	
	/**
	 * JBoss Enterprise Application Platform with any version
	 */
	EAP(ServerReqFamily.EAP),
	/**
	 * JBoss Enterprise Application Platform 4.3
	 */
	EAP4_3(ServerReqFamily.EAP, "4.3"),
	/**
	 * JBoss Enterprise Application Platform 5.x
	 */
	EAP5x(ServerReqFamily.EAP, "5.x"),
	/**
	 * JBoss Enterprise Application Platform 6.0
	 */
	EAP6_0(ServerReqFamily.EAP, "6.0"),
	/**
	 * JBoss Enterprise Application Platform 6.1+
	 */
	EAP6_1plus(ServerReqFamily.EAP, "6.1+"),
	
	/**
	 * WildFly
	 * - successor of AS
	 */
	WILDFLY(ServerReqFamily.WILDFLY),
	/**
	 * WildFly 8.x
	 */
	WILDFLY8x(ServerReqFamily.WILDFLY, "8.x");


	private String version;
	
	private ServerReqFamily family;
	
	/**
	 * Define server type by {@link ServerReqFamily} with any version.
	 * 
	 * @param family family of defined server
	 */
	ServerReqType(ServerReqFamily family) {
		this(family, "");
	}
	
	/**
	 * Define server type by {@link ServerReqFamily} with specified <var><version/var>
	 * 
	 * @param family family of defined server
	 * @param version version of defined server
	 */
	ServerReqType(ServerReqFamily family, String version) {
		this.version = version;
		this.family = family;
	}
	
	public String getVersion() {
		return version;
	}
	
	public ServerReqFamily getFamily() {
		return family;
	}
	
	enum ServerReqFamily {
		AS, EAP, WILDFLY, ANY
	}
}
