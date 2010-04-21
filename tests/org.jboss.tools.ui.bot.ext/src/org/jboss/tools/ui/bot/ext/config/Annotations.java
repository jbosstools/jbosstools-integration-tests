package org.jboss.tools.ui.bot.ext.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



public class Annotations {
	/**
	 * annotation which defines requirement of whole test Class
	 * by default all sub-annotations are optional and are 
	 * @author lzoubek
	 *
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface SWTBotTestRequires {	
		/**
		 * optionally require server
		 */
		Server server() default @Server( required = false );
		Seam seam() default @Seam( required = false );
	}
	/**
	 * Server requirement, by default matches all server types and versions
	 * @author lzoubek
	 *
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Server {
		/**
		 * true if Server is required (default)
		 * @return
		 */
		boolean required() default true;
		/**
		 * server type to match (Default ALL)
		 * @return
		 */
		ServerType type() default ServerType.ALL;
		/**
		 * version of required server (use * for all versions) default *
		 * @return
		 */
		String version() default "*";
		/**
		 * defines operator to match server version, possible values (=,<,>=<=,>=,!=) default =
		 * @return
		 */
		String operator() default "=";	
	}
	/**
	 * 
	 * @author lzoubek@redhat.com
	 *
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Seam {
		/**
		 * true if Seam is required (default)
		 * @return
		 */
		boolean required() default true;
		/**
		 * version of required server (use * for all)
		 * @return
		 */
		String version() default "*";
		/**
		 * defines operator on server, possible values (=,<,>=<=,>=,!=) default =
		 * @return
		 */
		String operator() default "=";
		
	}
	public enum ServerType {
		/**
		 * EAP
		 */
		EAP, 
		/**
		 * Jboss community version
		 */
		JbossAS,
		/**
		 * all server types acceptable
		 */
		ALL
	}
}
