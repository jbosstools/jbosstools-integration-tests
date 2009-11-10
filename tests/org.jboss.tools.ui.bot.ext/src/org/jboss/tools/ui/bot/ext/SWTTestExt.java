 /*******************************************************************************
  * Copyright (c) 2007-2009 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.ui.bot.ext;

import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Base class for SWTBot Tests using SWTBotExt
 * @author jpeterka
 *
 */
public class SWTTestExt {

	public static Logger log = Logger.getLogger(SWTTestExt.class);
	public static SWTBotExt bot = new SWTBotExt();
	public static SWTUtilExt util = new SWTUtilExt();
	public static SWTEclipseExt eclipse = new SWTEclipseExt();
	private static Properties properties;
	
	/**
	 * Get properties for hibernate tests
	 * @param key
	 */
	public static String getProperty(String key) {
		return util.getValue(properties,key);
	}	
	
	// Wait Constants
	public static int TIME_1S = 1000;
	public static int TIME_5S = 5000;
	public static int TIME_10S = 10000;
	public static int TIME_20S = 20000;
}
