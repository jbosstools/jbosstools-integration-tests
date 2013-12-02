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
package org.jboss.tools.ui.bot.ext.types;

/** 
 * Class provides job lists like list of jobs which should be ignored for wait methods
 **/
public class JobLists {

	public static String[] ignoredJobs = { "Process Console Input Job", "Usage Data Event consumer",
			"Updating indexes","Reporting JBoss Developer Studio usage", "Asking User to allow reporting", "Usage Data Event consumer" };
}
